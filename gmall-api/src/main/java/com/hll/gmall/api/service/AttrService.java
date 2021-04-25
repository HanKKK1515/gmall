package com.hll.gmall.api.service;

import com.hll.gmall.api.bean.PmsBaseAttrInfo;

import java.util.List;
import java.util.Set;

public interface AttrService {

    List<PmsBaseAttrInfo> getBaseAttrByValueIds(Set<String> valueIdsSet);
}
