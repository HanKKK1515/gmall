package com.hll.gmall.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.hll.gmall.annotations.LoginRequired;
import com.hll.gmall.utils.CookieUtil;
import com.hll.gmall.utils.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if (loginRequired == null) {
            return true;
        }

        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)) {
            token = CookieUtil.getCookieValue(request, "oldToken", true);
        }

        if (StringUtils.isNotBlank(token)) {
            // 通过 nginx 转发的客户端 ip
            String currentIp = request.getHeader("x-forwarded-for");
            if (StringUtils.isBlank(currentIp)) {
                currentIp = request.getRemoteAddr();
                if (StringUtils.isBlank(currentIp)) {
                    currentIp = "127.0.0.1";
                }
            }

            String result = HttpclientUtil.doGet("http://localhost:8085/verify?token=" + token + "&currentIp=" + currentIp);
            Map<String, String> tokenMap = JSONObject.parseObject(result, Map.class);
            if (tokenMap != null && "success".equals(tokenMap.get("status"))) {
                CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 3, true);
                request.setAttribute("memberId", tokenMap.get("memberId"));
                request.setAttribute("username", tokenMap.get("username"));
                return true;
            } else {
                CookieUtil.setCookie(request, response, "oldToken", "", 0, true);
                request.setAttribute("memberId", "");
                request.setAttribute("username", "");
            }
        }

        if (loginRequired.loginSuccess()) {
            response.sendRedirect("http://localhost:8085/index.html?returnUrl=" + request.getRequestURL());
            return false;
        }

        return true;
    }

}
