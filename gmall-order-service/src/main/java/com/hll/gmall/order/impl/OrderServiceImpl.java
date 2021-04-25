package com.hll.gmall.order.impl;

import com.hll.gmall.api.bean.OmsOrder;
import com.hll.gmall.api.bean.OmsOrderItem;
import com.hll.gmall.api.service.OrderService;
import com.hll.gmall.order.mapper.OmsOrderItemMapper;
import com.hll.gmall.order.mapper.OmsOrderMapper;
import com.hll.gmall.utils.RedisUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@DubboService
public class OrderServiceImpl implements OrderService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsOrderMapper omsOrderMapper;

    @Autowired
    OmsOrderItemMapper omsOrderItemMapper;

    @Override
    public String getTradeCode(String memberId) {
        String key = "user:" + memberId + ":tradeCode";
        String tradeCode = UUID.randomUUID().toString();
        redisUtil.setWithExpireTime(key, tradeCode, 60 * 15);

        return tradeCode;
    }

    @Override
    public String checkTradeCode(String memberId, String tradeCode) {
        String tradeKey = "user:" + memberId + ":tradeCode";

        // 使用 lua 脚本在发现 key 的同时将 key 删除，防止并发订单攻击
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long eval = (Long) redisUtil.eval(script, Collections.singletonList(tradeKey), Collections.singletonList(tradeCode));

        return eval != null && eval != 0 ? "success" : "fail";
    }

    @Override
    public void saveOrder(OmsOrder omsOrder) {
        omsOrderMapper.insertSelective(omsOrder);

        List<OmsOrderItem> omsOrderItems = omsOrder.getOmsOrderItems();
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            omsOrderItem.setOrderId(omsOrder.getId());
            omsOrderItem.setOrderSn(omsOrder.getOrderSn());
            omsOrderItemMapper.insertSelective(omsOrderItem);
        }
    }

    @Override
    public OmsOrder getOrderBySn(String memberId, String outTradeNo) {
        OmsOrder order = new OmsOrder();
        order.setMemberId(memberId);
        order.setOrderSn(outTradeNo);
        order = omsOrderMapper.selectOne(order);

        OmsOrderItem omsOrderItem = new OmsOrderItem();
        omsOrderItem.setOrderId(order.getId());
        omsOrderItem.setOrderSn(order.getOrderSn());
        List<OmsOrderItem> orderItems = omsOrderItemMapper.select(omsOrderItem);

        order.setOmsOrderItems(orderItems);
        return order;
    }

    @Override
    public List<OmsOrder> getOrderByMemberId(String memberId) {
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setMemberId(memberId);
        List<OmsOrder> omsOrders = omsOrderMapper.select(omsOrder);

        for (OmsOrder order : omsOrders) {
            OmsOrderItem omsOrderItem = new OmsOrderItem();
            omsOrderItem.setOrderId(order.getId());
            omsOrderItem.setOrderSn(order.getOrderSn());
            List<OmsOrderItem> orderItems = omsOrderItemMapper.select(omsOrderItem);

            order.setOmsOrderItems(orderItems);
        }

        return omsOrders;
    }
}
