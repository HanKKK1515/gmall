package com.hll.gmall.item.controller;

import com.alibaba.fastjson.JSONObject;
import com.hll.gmall.annotations.LoginRequired;
import com.hll.gmall.api.bean.PmsProductSaleAttr;
import com.hll.gmall.api.bean.PmsSkuInfo;
import com.hll.gmall.api.bean.PmsSkuSaleAttrValue;
import com.hll.gmall.api.service.SkuService;
import com.hll.gmall.api.service.SpuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class ItemController {
    @DubboReference
    SkuService skuService;

    @DubboReference
    SpuService spuService;

    @RequestMapping("{skuId}.html")
    @LoginRequired(loginSuccess = false)
    public String item(@PathVariable String skuId, ModelMap modelMap) {
        if ("index".equals(skuId)) {
            return "index";
        }

        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        modelMap.put("skuInfo", pmsSkuInfo);

        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getSpuId(), pmsSkuInfo.getId());
        modelMap.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrs);

        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuListBySpuId(pmsSkuInfo.getSpuId());
        Map<String, String> skuSaleAttrHash = new HashMap<>();
        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            StringBuilder key = new StringBuilder();
            String value = skuInfo.getId();

            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuInfo.getSkuSaleAttrValueList()) {
                if (StringUtils.isNotBlank(key)) {
                    key.append("|");
                }
                key.append(pmsSkuSaleAttrValue.getSaleAttrValueId());
            }

            if (StringUtils.isNotBlank(key.toString())) {
                skuSaleAttrHash.put(key.toString(), value);
            }
        }
        String skuSaleAttrHashJsonStr = JSONObject.toJSONString(skuSaleAttrHash);
        modelMap.put("skuSaleAttrHashJsonStr", skuSaleAttrHashJsonStr);

        return "item";
    }

}
