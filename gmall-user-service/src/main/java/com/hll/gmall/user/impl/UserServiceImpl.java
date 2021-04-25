package com.hll.gmall.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.hll.gmall.api.bean.UmsMember;
import com.hll.gmall.api.bean.UmsMemberReceiveAddress;
import com.hll.gmall.api.service.UserService;
import com.hll.gmall.user.mapper.UserMapper;
import com.hll.gmall.user.mapper.UserReceiveAddressMapper;
import com.hll.gmall.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@DubboService
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserReceiveAddressMapper userReceiveAddressMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<UmsMember> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public List<UmsMemberReceiveAddress> findReceiveAddressByUserId(String userId) {
        Example example = new Example(UmsMemberReceiveAddress.class);
        example.createCriteria().andEqualTo("memberId", userId);
        return userReceiveAddressMapper.selectByExample(example);
    }

    @Override
    public UmsMember login(UmsMember member) {
        String memberStr = redisUtil.get("user:" + member.getUsername() + member.getPassword() + ":info");
        if (StringUtils.isNotBlank(memberStr)) {
            return JSONObject.parseObject(memberStr, UmsMember.class);
        }

        List<UmsMember> members = userMapper.select(member);
        if (members != null && members.size() > 0) {
            memberStr = JSONObject.toJSONString(members.get(0));
            redisUtil.setWithExpireTime("user:" + member.getUsername() + member.getPassword() + ":info", memberStr, 60 * 60 *24);
            return members.get(0);
        }

        return null;
    }

    @Override
    public void flushTokenToCache(String memberId, String token) {
        redisUtil.setWithExpireTime("user:" + memberId + ":token", token, 60 * 60 * 2);
    }

    @Override
    public UmsMember getOAuthUserFromDb(UmsMember umsMember) {
        UmsMember oauthMember = new UmsMember();
        oauthMember.setSourceUid(umsMember.getSourceUid());
        oauthMember = userMapper.selectOne(oauthMember);
        if (oauthMember != null) {
            return oauthMember;
        }

        userMapper.insert(umsMember);
        return umsMember;
    }

    @Override
    public UmsMemberReceiveAddress findReceiveAddressById(String receiveAddressId) {
        UmsMemberReceiveAddress receiveAddress = new UmsMemberReceiveAddress();
        receiveAddress.setId(receiveAddressId);
        return userReceiveAddressMapper.selectByPrimaryKey(receiveAddress);
    }
}
