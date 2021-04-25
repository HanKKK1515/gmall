package com.hll.gmall.payment.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.hll.gmall.annotations.LoginRequired;
import com.hll.gmall.api.bean.OmsOrder;
import com.hll.gmall.api.bean.OmsOrderItem;
import com.hll.gmall.api.bean.PaymentInfo;
import com.hll.gmall.api.enums.PaymentStatus;
import com.hll.gmall.api.service.OrderService;
import com.hll.gmall.api.service.PaymentInfoService;
import com.hll.gmall.payment.conf.AlipayConfig;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class PaymentController {
    @DubboReference
    OrderService orderService;

    @Autowired
    PaymentInfoService paymentInfoService;

    @RequestMapping("index.html")
    @LoginRequired
    public String index(String outTradeNo, HttpServletRequest request, ModelMap modelMap) {
        String memberId = (String) request.getAttribute("memberId");
        String username = (String) request.getAttribute("username");

        OmsOrder order = orderService.getOrderBySn(memberId, outTradeNo);
        if (order == null) {
            return "redirect:http://localhost:8086/list.html";
        }

        String paySubject = getPaySubject(order);
        modelMap.put("nickname", username);
        modelMap.put("totalAmount", order.getTotalAmount());
        modelMap.put("outTradeNo", outTradeNo);
        modelMap.put("orderId", order.getId());
        modelMap.put("paySubject", paySubject);

        return "index";
    }

    private String getPaySubject(OmsOrder order) {
        StringBuilder subject = new StringBuilder();
        List<OmsOrderItem> omsOrderItems = order.getOmsOrderItems();
        for (int i = 0; i < omsOrderItems.size(); i++) {
            if (i > 0) {
                subject.append(",");
            }
            subject.append(omsOrderItems.get(i).getProductName());
        }
        return subject.toString();
    }

    @RequestMapping("alipay/submit")
    @ResponseBody
    @LoginRequired
    public String alipaySubmit(String outTradeNo, BigDecimal totalAmount, String orderId, String paySubject, HttpServletRequest request) {
        try {
            AlipayTradePagePayResponse payResponse = Factory.Payment.Page().pay(paySubject, outTradeNo, totalAmount.toString(), AlipayConfig.returnUrl);

            if (ResponseChecker.success(payResponse)) {
                PaymentInfo paymentInfo = new PaymentInfo();
                paymentInfo.setOrderId(orderId);
                paymentInfo.setOrderSn(outTradeNo);
                paymentInfo.setTotalAmount(totalAmount);
                paymentInfo.setCreateTime(new Date());
                paymentInfo.setPaymentStatus(PaymentStatus.WAIT_BUYER_PAY.ordinal());
                paymentInfo.setSubject(paySubject);

                paymentInfoService.savePaymentInfo(paymentInfo);
                return payResponse.body;
            } else {
                return "payFail";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "异常";
        }
    }

    @RequestMapping("alipay/callback/notify")
    @LoginRequired(loginSuccess = false)
    public String alipayCallbackNotify(HttpServletRequest request) {
        Map<String, String> paramMap = getParamMap(request);
        try {
            if (Factory.Payment.Common().verifyNotify(paramMap)) {
                PaymentInfo paymentInfo = getPaymentInfo(paramMap, request);
                paymentInfoService.updatePaymentInfo(paymentInfo);

                if (PaymentStatus.TRADE_SUCCESS.toString().equals(paramMap.get("out_trade_no"))) {
                    return "success";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    private PaymentInfo getPaymentInfo(Map<String, String> paramMap, HttpServletRequest request) {
        PaymentInfo paymentInfo = paymentInfoService.getPaymentInfo(paramMap.get("out_trade_no"));
        paymentInfo.setAlipayTradeNo(paramMap.get("trade_no"));
        String queryString = request.getQueryString();
        paymentInfo.setCallbackContent(request.getQueryString());
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setConfirmTime(new Date());

        PaymentStatus paymentStatus = PaymentStatus.valueOf(paramMap.get("trade_status"));
        switch (paymentStatus) {
            case WAIT_BUYER_PAY:
                paymentInfo.setPaymentStatus(PaymentStatus.WAIT_BUYER_PAY.ordinal());
                break;
            case TRADE_CLOSED:
                paymentInfo.setPaymentStatus(PaymentStatus.TRADE_CLOSED.ordinal());
                break;
            case TRADE_SUCCESS:
                paymentInfo.setPaymentStatus(PaymentStatus.TRADE_SUCCESS.ordinal());
                break;
            case TRADE_FINISHED:
                paymentInfo.setPaymentStatus(PaymentStatus.TRADE_FINISHED.ordinal());
                break;
            default:
                paymentInfo.setPaymentStatus(-1);
        }
        return paymentInfo;
    }

    private Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            paramMap.put(paramName, paramValue);
        }
        return paramMap;
    }

    @RequestMapping("alipay/callback/return")
    @LoginRequired(loginSuccess = false)
    public String alipayCallbackReturn(HttpServletRequest request) {
        Map<String, String> paramMap = getParamMap(request);
        try {
            if (Factory.Payment.Common().verifyNotify(paramMap)) {
                if (PaymentStatus.WAIT_BUYER_PAY.toString().equals(paramMap.get("out_trade_no"))) {
                    return "paymentindex";
                } else if (PaymentStatus.TRADE_CLOSED.toString().equals(paramMap.get("out_trade_no"))) {
                    return "payFail";
                } else {
                    return "finish";
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "payFail";
    }

    @RequestMapping("wx/submit")
    @LoginRequired
    public String wxSubmit(String outTradeNo, BigDecimal totalAmount, HttpServletRequest request) {

        return "";
    }

}
