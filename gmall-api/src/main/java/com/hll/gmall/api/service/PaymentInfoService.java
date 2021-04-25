package com.hll.gmall.api.service;

import com.hll.gmall.api.bean.PaymentInfo;

public interface PaymentInfoService {
    void savePaymentInfo(PaymentInfo paymentInfo);

    PaymentInfo getPaymentInfo(String outTradeNo);

    void updatePaymentInfo(PaymentInfo paymentInfo);
}
