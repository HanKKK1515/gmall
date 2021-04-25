package com.hll.gmall.api.service;

import com.hll.gmall.api.bean.UmsMember;
import com.hll.gmall.api.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    List<UmsMember> findAll();

    List<UmsMemberReceiveAddress> findReceiveAddressByUserId(String userId);

    UmsMember login(UmsMember member);

    void flushTokenToCache(String memberId, String token);

    UmsMember getOAuthUserFromDb(UmsMember umsMember);

    UmsMemberReceiveAddress findReceiveAddressById(String receiveAddressId);
}
