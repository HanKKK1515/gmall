package com.hll.gmall.api.service;

import com.hll.gmall.api.bean.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

public interface SkuService {

    String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getSkuList();

    List<PmsSkuInfo> getSkuListBySpuId(String spuId);

    Boolean checkPrice(String productSkuId, BigDecimal price);
}
