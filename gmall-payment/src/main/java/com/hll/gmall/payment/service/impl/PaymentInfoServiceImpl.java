package com.hll.gmall.payment.service.impl;

import com.hll.gmall.api.bean.PaymentInfo;
import com.hll.gmall.api.service.PaymentInfoService;
import com.hll.gmall.payment.service.mapper.PaymentInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {
    @Autowired
    PaymentInfoMapper paymentInfoMapper;

    @Override
    public void savePaymentInfo(PaymentInfo paymentInfo) {
        paymentInfoMapper.insertSelective(paymentInfo);
    }

    @Override
    public PaymentInfo getPaymentInfo(String outTradeNo) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderSn(outTradeNo);
        return paymentInfoMapper.selectOne(paymentInfo);
    }

    @Override
    public void updatePaymentInfo(PaymentInfo paymentInfo) {
        paymentInfoMapper.updateByPrimaryKeySelective(paymentInfo);
    }
}
