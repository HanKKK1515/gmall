package com.hll.gmall.manager.controller;

import com.hll.gmall.api.bean.*;
import com.hll.gmall.api.service.SpuService;
import com.hll.gmall.manager.utils.PmsUploadUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin
public class SpuController {
    @DubboReference
    SpuService spuService;

    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id) {
        return spuService.spuList(catalog3Id);
    }

    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return spuService.baseSaleAttrList();
    }

    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {
        return spuService.saveSpuInfo(pmsProductInfo);
    }

    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(MultipartFile file) {
        return PmsUploadUtil.uploadImage(file);
    }

    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        return spuService.spuSaleAttrList(spuId);
    }

    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId) {
        return spuService.spuImageList(spuId);
    }

}
