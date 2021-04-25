package com.hll.gmall.manager.controller;

import com.hll.gmall.api.bean.PmsSkuInfo;
import com.hll.gmall.api.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class SkuController {
    @DubboReference
    SkuService skuService;

    @RequestMapping("saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo) {
        // 处理默认图片
        if (StringUtils.isBlank(pmsSkuInfo.getSkuDefaultImg())) {
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
        }
        return skuService.saveSkuInfo(pmsSkuInfo);
    }
}
