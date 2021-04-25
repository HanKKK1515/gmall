package com.hll.gmall.api.service;

import com.hll.gmall.api.bean.OmsOrder;

import java.util.List;

public interface OrderService {
    String getTradeCode(String memberId);

    String checkTradeCode(String memberId, String tradeCode);

    void saveOrder(OmsOrder omsOrder);

    OmsOrder getOrderBySn(String memberId, String outTradeNo);

    List<OmsOrder> getOrderByMemberId(String memberId);
}
