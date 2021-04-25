package com.hll.gmall.manager.controller;

import com.hll.gmall.api.bean.*;
import com.hll.gmall.api.service.CatalogService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class CatalogController {
    @DubboReference
    CatalogService catalogService;

    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1() {
        return catalogService.getCatalog1();
    }

    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {
        return catalogService.getCatalog2(catalog1Id);
    }

    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        return catalogService.getCatalog3(catalog2Id);
    }

    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        return catalogService.attrInfoList(catalog3Id);
    }

    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo) {
        return catalogService.saveAttrInfo(pmsBaseAttrInfo);
    }

    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        return catalogService.getAttrValueList(attrId);
    }


}
