package com.hll.gmall.api.service;

import com.hll.gmall.api.bean.OmsCartItem;

import java.util.List;


public interface OmsCartItemService {
    OmsCartItem getCartItemBySkuId(String memberId, String skuId);

    void saveOmsCartItem(OmsCartItem omsCartItem);

    void updateOmsCartItem(OmsCartItem omsCartItem);

    void flushCartItemToCache(OmsCartItem omsCartItem);

    List<OmsCartItem> getCartItemByMemberId(String memberId);

    OmsCartItem checkCartToDb(String memberId, String skuId, String isChecked);
}
