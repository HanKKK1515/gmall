package com.hll.gmall.manager.impl;

import com.hll.gmall.api.bean.PmsBaseAttrInfo;
import com.hll.gmall.api.service.AttrService;
import com.hll.gmall.manager.mapper.PmsBaseAttrInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@DubboService
public class AttrServiceImpl implements AttrService {
    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Override
    public List<PmsBaseAttrInfo> getBaseAttrByValueIds(Set<String> valueIdsSet) {
        String valueIds = StringUtils.join(valueIdsSet, ",");
        return pmsBaseAttrInfoMapper.selectBaseAttrByValueIds(valueIds);
    }
}
