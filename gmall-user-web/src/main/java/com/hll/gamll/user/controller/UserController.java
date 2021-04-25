package com.hll.gamll.user.controller;

import com.hll.gmall.api.bean.UmsMember;
import com.hll.gmall.api.bean.UmsMemberReceiveAddress;
import com.hll.gmall.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @DubboReference
    private UserService userService;

    @RequestMapping("/findAll")
    public List<UmsMember> findAll() {
        List<UmsMember> users = userService.findAll();
        return users;
    }

    @RequestMapping("/findReceiveAddressByUserId")
    public List<UmsMemberReceiveAddress> findReceiveAddressByUserId(String userId) {
        List<UmsMemberReceiveAddress> receiveAddress = userService.findReceiveAddressByUserId(userId);
        return receiveAddress;
    }

}
