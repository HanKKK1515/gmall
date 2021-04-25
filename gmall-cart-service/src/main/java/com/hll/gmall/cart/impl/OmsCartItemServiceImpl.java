package com.hll.gmall.cart.impl;

import com.alibaba.fastjson.JSONObject;
import com.hll.gmall.cart.mapper.OmsCartItemMapper;
import com.hll.gmall.api.bean.OmsCartItem;
import com.hll.gmall.api.service.OmsCartItemService;
import com.hll.gmall.utils.RedisUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@DubboService
public class OmsCartItemServiceImpl implements OmsCartItemService {
    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public OmsCartItem getCartItemBySkuId(String memberId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);
        if (omsCartItems != null && omsCartItems.size() > 0) {
            return omsCartItems.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void saveOmsCartItem(OmsCartItem omsCartItem) {
        omsCartItemMapper.insert(omsCartItem);
    }

    @Override
    public void updateOmsCartItem(OmsCartItem omsCartItem) {
        omsCartItemMapper.updateByPrimaryKeySelective(omsCartItem);
    }

    @Override
    public void flushCartItemToCache(OmsCartItem omsCartItem) {
        redisUtil.hSet("user:" + omsCartItem.getMemberId() + ":cart", omsCartItem.getProductSkuId(), JSONObject.toJSONString(omsCartItem));
    }

    @Override
    public List<OmsCartItem> getCartItemByMemberId(String memberId) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        Map<String, String> map = redisUtil.hGetAll("user:" + memberId + ":cart");
        if (map.isEmpty()) {
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setMemberId(memberId);
            omsCartItems = omsCartItemMapper.select(omsCartItem);
        } else {
            for (String value : map.values()) {
                omsCartItems.add(JSONObject.parseObject(value, OmsCartItem.class));
            }
        }

        return omsCartItems;
    }

    @Override
    public OmsCartItem checkCartToDb(String memberId, String skuId, String isChecked) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);
        omsCartItem = omsCartItems.get(0);
        omsCartItem.setIsChecked(isChecked);
        omsCartItemMapper.updateByPrimaryKeySelective(omsCartItem);

        return omsCartItem;
    }

}
