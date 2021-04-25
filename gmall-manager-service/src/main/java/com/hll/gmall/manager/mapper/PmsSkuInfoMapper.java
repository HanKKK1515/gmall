package com.hll.gmall.manager.mapper;

import com.hll.gmall.api.bean.PmsSkuInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {

    List<PmsSkuInfo> getSkuSaleAttrListBySpuId(@Param("spuId") String spuId);

}
