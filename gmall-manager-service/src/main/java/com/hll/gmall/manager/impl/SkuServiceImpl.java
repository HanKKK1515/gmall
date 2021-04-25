package com.hll.gmall.manager.impl;

import com.alibaba.fastjson.JSONObject;
import com.hll.gmall.api.bean.PmsSkuAttrValue;
import com.hll.gmall.api.bean.PmsSkuImage;
import com.hll.gmall.api.bean.PmsSkuInfo;
import com.hll.gmall.api.bean.PmsSkuSaleAttrValue;
import com.hll.gmall.api.service.SkuService;
import com.hll.gmall.manager.mapper.PmsSkuAttrValueMapper;
import com.hll.gmall.manager.mapper.PmsSkuImageMapper;
import com.hll.gmall.manager.mapper.PmsSkuInfoMapper;
import com.hll.gmall.manager.mapper.PmsSkuSaleAttrValueMapper;
import com.hll.gmall.utils.RedisUtil;
import com.hll.gmall.utils.RedissonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@DubboService
public class SkuServiceImpl implements SkuService {
    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedissonUtil redissonUtil;

    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        try {
            pmsSkuInfoMapper.insertSelective(pmsSkuInfo);

            List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
            for (PmsSkuImage pmsSkuImage : skuImageList) {
                pmsSkuImage.setSkuId(pmsSkuInfo.getId());
                pmsSkuImageMapper.insertSelective(pmsSkuImage);
            }

            List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
                pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
            }

            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
                pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
            }

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }

    }

    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        PmsSkuInfo pmsSkuInfo;
        String skuKey = "sku:" + skuId + ":info";
        String pmsSkuInfoJson = redisUtil.get(skuKey);
        if (StringUtils.isBlank(pmsSkuInfoJson)) {
            pmsSkuInfo = getSkuFromDbById(skuId);
            saveSkuInfoToRedis(skuKey, pmsSkuInfo);
        } else {
            pmsSkuInfo = JSONObject.parseObject(pmsSkuInfoJson, PmsSkuInfo.class);
        }

        return pmsSkuInfo;
    }

    private void saveSkuInfoToRedis(String skuKey, PmsSkuInfo pmsSkuInfo) {
        RLock lock = redissonUtil.getRLock("gmall_pmsSkuInfo_lock");
        try {
            if (lock.tryLock(100, 60 * 1000, TimeUnit.MILLISECONDS)) {
                if (pmsSkuInfo == null || StringUtils.isBlank(pmsSkuInfo.getId()) || StringUtils.isBlank(pmsSkuInfo.getSkuName())) {
                    String skuValue = JSONObject.toJSONString("");
                    redisUtil.setWithExpireTime(skuKey, skuValue, 60 * 3);
                } else {
                    String skuValue = JSONObject.toJSONString(pmsSkuInfo);
                    redisUtil.set(skuKey, skuValue);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private PmsSkuInfo getSkuFromDbById(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(pmsSkuInfo);

        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfo.setSkuImageList(pmsSkuImages);

        PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
        pmsSkuAttrValue.setSkuId(skuId);
        List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
        pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);

        PmsSkuSaleAttrValue pmsSkuSaleAttrValue = new PmsSkuSaleAttrValue();
        pmsSkuSaleAttrValue.setSkuId(skuId);
        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValues = pmsSkuSaleAttrValueMapper.select(pmsSkuSaleAttrValue);
        pmsSkuInfo.setSkuSaleAttrValueList(pmsSkuSaleAttrValues);

        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuList() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }
        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getSkuListBySpuId(String spuId) {
        return pmsSkuInfoMapper.getSkuSaleAttrListBySpuId(spuId);
    }

    @Override
    public Boolean checkPrice(String productSkuId, BigDecimal price) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(productSkuId);
        pmsSkuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        return price.compareTo(pmsSkuInfo.getPrice()) != 0;
    }

}
