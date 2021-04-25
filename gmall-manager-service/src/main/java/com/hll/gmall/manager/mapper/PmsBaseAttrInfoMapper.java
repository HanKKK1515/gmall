package com.hll.gmall.manager.mapper;

import com.hll.gmall.api.bean.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsBaseAttrInfoMapper extends Mapper<PmsBaseAttrInfo> {
    List<PmsBaseAttrInfo> selectBaseAttrByValueIds(@Param("valueIds") String valueIds);
}
