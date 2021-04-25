package com.hll.gmall.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.hll.gmall.api.bean.UmsMember;
import com.hll.gmall.api.constant.Constants;
import com.hll.gmall.api.service.UserService;
import com.hll.gmall.utils.HttpclientUtil;
import com.hll.gmall.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {
    @DubboReference
    UserService userService;

    @RequestMapping("vLogin.html")
    public String vLogin(String code, String state, HttpServletRequest request) {
        Map<String, Object> accessTokenMap = getAccessTokenMap(code);
        String accessToken = (String) accessTokenMap.get("access_token");

        Map<String, Object> accessTokenInfoMap = getAccessTokenInfoMap(accessToken);
        String uid = accessTokenInfoMap.get("uid") + "";

        Map<String, Object> userInfoMap = getUserInfoMap(accessToken, uid);
        UmsMember umsMember = getMember(code, accessToken, userInfoMap);

        umsMember = userService.getOAuthUserFromDb(umsMember);

        String token = getToken(request, umsMember);
        userService.flushTokenToCache(umsMember.getId(), token);
        return "redirect:" + state + "?token=" + token;
    }

    private UmsMember getMember(String code, String access_token, Map<String, Object> userInfoMap) {
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceType("2");
        umsMember.setAccessCode(code);
        umsMember.setAccessToken(access_token);
        umsMember.setSourceUid((String) userInfoMap.get("idstr"));
        umsMember.setCity((String) userInfoMap.get("location"));
        umsMember.setNickname((String) userInfoMap.get("screen_name"));
        String gender = (String) userInfoMap.get("gender");
        if("m".equals(gender)){
            umsMember.setGender("1");
        } else {
            umsMember.setGender("0");
        }

        return umsMember;
    }

    private Map<String, Object> getUserInfoMap(String accessToken, String uid) {
        String getUserUrl = Constants.USERS_SHOW_URL + "?access_token=" + accessToken + "&uid=" + uid;
        String userInfoJson = HttpclientUtil.doGet(getUserUrl);
        return JSONObject.parseObject(userInfoJson, Map.class);
    }

    private Map<String, Object> getAccessTokenInfoMap(String accessToken) {
        Map<String, String> paramInfo = new HashMap<>();
        paramInfo.put("access_token", accessToken);

        String tokenInfoJson = HttpclientUtil.doPost(Constants.TOKEN_INFO_URL, paramInfo);
        return JSONObject.parseObject(tokenInfoJson, Map.class);
    }

    private Map<String, Object> getAccessTokenMap(String code) {
        Map<String, String> paramToken = new HashMap<>();
        paramToken.put("client_id", Constants.CLIENT_ID);
        paramToken.put("client_secret", Constants.CLIENT_SECRET);
        paramToken.put("grant_type", "authorization_code");
        paramToken.put("redirect_uri", Constants.REDIRECT_URL);
        paramToken.put("code", code);

        String tokenJson = HttpclientUtil.doPost(Constants.ACCESS_TOKEN_URL, paramToken);
        return JSONObject.parseObject(tokenJson, Map.class);
    }

    @RequestMapping("login")
    public String login(HttpServletRequest request, UmsMember member, String returnUrl) {
        if (StringUtils.isBlank(returnUrl)) {
            returnUrl = "http://localhost:8082/index.html";
        }

        UmsMember umsMember = userService.login(member);
        if (umsMember == null) {
            return "redirect:index.html?returnUrl=" + returnUrl;
        }

        String token = getToken(request, umsMember);
        userService.flushTokenToCache(umsMember.getId(), token);

        return "redirect:" + returnUrl + "?token=" + token;
    }

    private String getToken(HttpServletRequest request, UmsMember umsMember) {
        // 通过 nginx 转发的客户端 ip
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
            if (StringUtils.isBlank(ip)) {
                ip = "127.0.0.1";
            }
        }

        Map<String, Object> memberMap = new HashMap<>();
        memberMap.put("memberId", umsMember.getId());
        memberMap.put("username", umsMember.getUsername());
        return JwtUtil.encode(Constants.PROJECT_NAME, memberMap, ip);
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token, String currentIp) {
        Map<String, Object> tokenMap = JwtUtil.decode(token, Constants.PROJECT_NAME, currentIp);
        Map<String, String> result = new HashMap<>();

        if (tokenMap != null && !tokenMap.isEmpty()) {
            result.put("status", "success");
            result.put("memberId", (String) tokenMap.get("memberId"));
            result.put("username", (String) tokenMap.get("username"));
        } else {
            result.put("status", "fail");
        }

        return JSONObject.toJSONString(result);
    }

    @RequestMapping("index.html")
    public String index(String returnUrl, ModelMap modelMap) {
        modelMap.put("returnUrl", returnUrl);

        String weiboOauthUrl = Constants.AUTHORIZE_URL + "?client_id=" + Constants.CLIENT_ID + "&state=" + returnUrl + "&response_type=code&redirect_uri=" + Constants.REDIRECT_URL;
        modelMap.put("weiboOauthUrl", weiboOauthUrl);

        return "index";
    }
}
