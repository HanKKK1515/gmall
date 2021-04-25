package com.hll.gmall.manager.mapper;

import com.hll.gmall.api.bean.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {
    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(@Param("spuId") String spuId, @Param("skuId") String skuId);
}
