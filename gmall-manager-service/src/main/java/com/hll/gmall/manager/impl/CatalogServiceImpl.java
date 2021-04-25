package com.hll.gmall.manager.impl;

import com.hll.gmall.api.bean.*;
import com.hll.gmall.api.service.CatalogService;
import com.hll.gmall.manager.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@DubboService
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;

    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;

    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return pmsBaseCatalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {
        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1Id);
        return pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        return pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);
    }

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);

        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos) {
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);

            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }
        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
        if (attrValueList == null || attrValueList.size() == 0) {
            return "false";
        }

        try {
            if (StringUtils.isBlank(pmsBaseAttrInfo.getId())) {
                pmsBaseAttrInfo.setIsEnabled("1");
                pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);

            } else {
                pmsBaseAttrInfoMapper.updateByPrimaryKeySelective(pmsBaseAttrInfo);

                Example example = new Example(PmsBaseAttrValue.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("attrId", pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.deleteByExample(example);

            }

            insertValue(pmsBaseAttrInfo);

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    private void insertValue(PmsBaseAttrInfo pmsBaseAttrInfo) {
        List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
        for (PmsBaseAttrValue attrValue : attrValueList) {
            attrValue.setAttrId(pmsBaseAttrInfo.getId());
            pmsBaseAttrValueMapper.insertSelective(attrValue);
        }
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue attrValue = new PmsBaseAttrValue();
        attrValue.setAttrId(attrId);
        return pmsBaseAttrValueMapper.select(attrValue);
    }

}
