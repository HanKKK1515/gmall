package com.hll.gmall.order.controller;

import com.hll.gmall.annotations.LoginRequired;
import com.hll.gmall.api.bean.OmsCartItem;
import com.hll.gmall.api.bean.OmsOrder;
import com.hll.gmall.api.bean.OmsOrderItem;
import com.hll.gmall.api.bean.UmsMemberReceiveAddress;
import com.hll.gmall.api.service.OmsCartItemService;
import com.hll.gmall.api.service.OrderService;
import com.hll.gmall.api.service.SkuService;
import com.hll.gmall.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    @DubboReference
    OmsCartItemService omsCartItemService;

    @DubboReference
    UserService userService;

    @DubboReference
    OrderService orderService;

    @DubboReference
    SkuService skuService;


    @RequestMapping("toTrade")
    @LoginRequired
    public String toTrade(HttpServletRequest request, ModelMap modelMap) {
        String memberId = (String) request.getAttribute("memberId");

        List<OmsOrderItem> omsOrderItems = getOrderItems(memberId, false);
        if (omsOrderItems == null || omsOrderItems.size() <= 0) {
            return "tradeFail";
        }

        List<UmsMemberReceiveAddress> receiveAddresses = userService.findReceiveAddressByUserId(memberId);

        BigDecimal totalAmount = getTotalAmount(omsOrderItems);

        String tradeCode = orderService.getTradeCode(memberId);

        modelMap.put("omsOrderItems", omsOrderItems);
        modelMap.put("userAddressList", receiveAddresses);
        modelMap.put("totalAmount", totalAmount);
        modelMap.put("tradeCode", tradeCode);
        return "trade";
    }

    private List<OmsOrderItem> getOrderItems(String memberId, Boolean isSubmit) {
        List<OmsCartItem> cartItems = omsCartItemService.getCartItemByMemberId(memberId);
        List<OmsOrderItem> omsOrderItems = new ArrayList<>();

        for (OmsCartItem cartItem : cartItems) {
            if ("0".equals(cartItem.getIsChecked())) {
                continue;
            }

            OmsOrderItem omsOrderItem = new OmsOrderItem();
            omsOrderItem.setProductName(cartItem.getProductName());
            omsOrderItem.setProductPrice(cartItem.getPrice());
            omsOrderItem.setProductQuantity(cartItem.getQuantity());
            omsOrderItem.setProductPic(cartItem.getProductPic());

            if (isSubmit) {
                Boolean isChange = skuService.checkPrice(cartItem.getProductSkuId(), cartItem.getPrice());
                if (isChange) {
                    return null;
                }

                omsOrderItem.setProductId(cartItem.getProductId());
                omsOrderItem.setProductSkuId(cartItem.getProductSkuId());
                omsOrderItem.setRealAmount(cartItem.getTotalPrice());
                omsOrderItem.setProductSn("仓库对应的商品编号");
                omsOrderItem.setProductSkuCode("商品sku条码");
            }

            omsOrderItems.add(omsOrderItem);
        }

        return omsOrderItems;
    }

    private BigDecimal getTotalAmount(List<OmsOrderItem> omsOrderItems) {
        BigDecimal totalPrice = new BigDecimal("0");
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            totalPrice = totalPrice.add(omsOrderItem.getProductPrice().multiply(omsOrderItem.getProductQuantity()));
        }

        return totalPrice;
    }

    @RequestMapping("submitOrder")
    @LoginRequired
    public ModelAndView submitOrder(String receiveAddressId, String tradeCode, BigDecimal totalAmount, HttpServletRequest request) {
        String memberId = (String) request.getAttribute("memberId");
        String username = (String) request.getAttribute("username");
        ModelAndView modelAndView = new ModelAndView();

        String checkResult = orderService.checkTradeCode(memberId, tradeCode);
        if ("fail".equals(checkResult)) {
            modelAndView.setViewName("tradeFail");
            return modelAndView;
        }

        List<OmsOrderItem> omsOrderItems = getOrderItems(memberId, true);
        if (omsOrderItems == null) {
            modelAndView.setViewName("tradeFail");
            return modelAndView;
        }

        OmsOrder omsOrder = getOrder(omsOrderItems, memberId, username, receiveAddressId, totalAmount);

        orderService.saveOrder(omsOrder);
        // 删除购物车


        modelAndView.setViewName("redirect:http://localhost:8087/index.html");
        modelAndView.addObject("outTradeNo", omsOrder.getOrderSn());
        return modelAndView;
    }

    private OmsOrder getOrder(List<OmsOrderItem> omsOrderItems, String memberId, String username, String receiveAddressId, BigDecimal totalAmount) {
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOmsOrderItems(omsOrderItems);
        omsOrder.setMemberId(memberId);
        omsOrder.setMemberUsername(username);
        omsOrder.setPayAmount(totalAmount);

        omsOrder.setTotalAmount(totalAmount);
        omsOrder.setOrderSn(getOutTradeNo());
        omsOrder.setOrderType(0);
        omsOrder.setSourceType(0);
        omsOrder.setStatus(0);
        omsOrder.setCreateTime(new Date());
        omsOrder.setAutoConfirmDay(7);
        omsOrder.setNote("尽快发货");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        omsOrder.setReceiveTime(calendar.getTime());

        UmsMemberReceiveAddress receiveAddress = userService.findReceiveAddressById(receiveAddressId);
        omsOrder.setReceiverName(receiveAddress.getName());
        omsOrder.setReceiverPhone(receiveAddress.getPhoneNumber());
        omsOrder.setReceiverProvince(receiveAddress.getProvince());
        omsOrder.setReceiverCity(receiveAddress.getCity());
        omsOrder.setReceiverRegion(receiveAddress.getRegion());
        omsOrder.setReceiverDetailAddress(receiveAddress.getDetailAddress());
        omsOrder.setReceiverPostCode(receiveAddress.getPostCode());

        return  omsOrder;
    }

    private String getOutTradeNo() {
        String outTradeNo = "gmall";
        outTradeNo += System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMDDhhmmss");
        outTradeNo += simpleDateFormat.format(new Date());
        return outTradeNo;
    }

    @RequestMapping("list.html")
    @LoginRequired
    public String submitOrder() {

        return "list";
    }

    @RequestMapping("orderList")
    @LoginRequired
    public String orderList(HttpServletRequest request, ModelMap modelMap) {
        String memberId = (String) request.getAttribute("memberId");
        List<OmsOrder> orderList = orderService.getOrderByMemberId(memberId);
        modelMap.put("orderList", orderList);
        return "list";
    }

}
