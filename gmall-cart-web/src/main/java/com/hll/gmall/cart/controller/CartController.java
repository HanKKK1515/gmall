package com.hll.gmall.cart.controller;

import com.alibaba.fastjson.JSONObject;
import com.hll.gmall.annotations.LoginRequired;
import com.hll.gmall.api.bean.OmsCartItem;
import com.hll.gmall.api.bean.PmsSkuInfo;
import com.hll.gmall.api.service.OmsCartItemService;
import com.hll.gmall.api.service.SkuService;
import com.hll.gmall.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
public class CartController {
    @DubboReference
    SkuService skuService;

    @DubboReference
    OmsCartItemService omsCartItemService;

    @RequestMapping("addToCart")
    @LoginRequired(loginSuccess = false)
    public String addCart(String skuId, long quantity, HttpServletRequest request, HttpServletResponse response) {
        String memberId = (String) request.getAttribute("memberId");
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId);
        OmsCartItem omsCartItem;
        if (StringUtils.isBlank(memberId)) {
            omsCartItem = getOmsCartItemBySkuInfo(skuInfo, quantity);
            addCartItemToCookie(request, response, omsCartItem);
        } else {
            omsCartItem = omsCartItemService.getCartItemBySkuId(memberId, skuId);
            if (omsCartItem == null) {
                omsCartItem = getOmsCartItemBySkuInfo(skuInfo, quantity);
                omsCartItem.setMemberId(memberId);
                omsCartItemService.saveOmsCartItem(omsCartItem);
            } else {
                omsCartItem.setQuantity(omsCartItem.getQuantity().add(BigDecimal.valueOf(quantity)));
                omsCartItemService.updateOmsCartItem(omsCartItem);
            }
            omsCartItemService.flushCartItemToCache(omsCartItem);
        }

        return "redirect:/success.html";
    }

    private void addCartItemToCookie(HttpServletRequest request,  HttpServletResponse response, OmsCartItem omsCartItem) {
        String cookieName = "cartListCookie";
        String cookieValue = CookieUtil.getCookieValue(request, cookieName, true);
        List<OmsCartItem> cartItems = new ArrayList<>();
        boolean isNotExist = true;

        if (StringUtils.isNotBlank(cookieValue)) {
            cartItems = JSONObject.parseArray(cookieValue, OmsCartItem.class);
            for (OmsCartItem cartItem : cartItems) {
                if (omsCartItem.getProductSkuId().equals(cartItem.getProductSkuId())) {
                    cartItem.setQuantity(cartItem.getQuantity().add(omsCartItem.getQuantity()));
                    isNotExist = false;
                    break;
                }
            }
        }
        if (StringUtils.isBlank(cookieValue) || isNotExist) {
            cartItems.add(omsCartItem);
        }

        cookieValue = JSONObject.toJSONString(cartItems);
        CookieUtil.setCookie(request, response, cookieName, cookieValue, 60 * 60 * 24 * 7, true);
    }

    private OmsCartItem getOmsCartItemBySkuInfo(PmsSkuInfo skuInfo, long quantity) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getSpuId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111");
        omsCartItem.setProductSkuId(skuInfo.getId());
        omsCartItem.setQuantity(BigDecimal.valueOf(quantity));

        return omsCartItem;
    }

    @RequestMapping("cartList")
    @LoginRequired(loginSuccess = false)
    public String cartList(HttpServletRequest request, ModelMap modelMap) {
        String memberId = (String) request.getAttribute("memberId");
        List<OmsCartItem> cartItems = new ArrayList<>();
        if (StringUtils.isBlank(memberId)) {
            String cookieName = "cartListCookie";
            String cookieValue = CookieUtil.getCookieValue(request, cookieName, true);
            if (StringUtils.isNotBlank(cookieValue)) {
                cartItems = JSONObject.parseArray(cookieValue, OmsCartItem.class);
            }
        } else {
            cartItems = omsCartItemService.getCartItemByMemberId(memberId);
        }

        modelMap.put("totalAmount", getTotalAmount(cartItems));
        modelMap.put("cartList", cartItems);
        return "cartList";
    }

    @RequestMapping("checkCart")
    @LoginRequired(loginSuccess = false)
    public String checkCart(String skuId, String isChecked, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        String memberId = (String) request.getAttribute("memberId");
        List<OmsCartItem> cartItems;
        if (StringUtils.isBlank(memberId)) {
            cartItems = checkCartToCookie(request, response, skuId, isChecked);
        } else {
            OmsCartItem omsCartItem = omsCartItemService.checkCartToDb(memberId, skuId, isChecked);
            omsCartItemService.flushCartItemToCache(omsCartItem);
            cartItems = omsCartItemService.getCartItemByMemberId(memberId);
        }

        modelMap.put("totalAmount", getTotalAmount(cartItems));
        modelMap.put("cartList", cartItems);
        return "cartListInner";
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> cartItems) {
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal totalPrice;
        for (OmsCartItem cartItem : cartItems) {
            totalPrice = cartItem.getPrice().multiply(cartItem.getQuantity());
            cartItem.setTotalPrice(totalPrice);
            if ("1".equals(cartItem.getIsChecked())) {
                totalAmount = totalAmount.add(totalPrice);
            }
        }

        return totalAmount;
    }

    private List<OmsCartItem> checkCartToCookie(HttpServletRequest request, HttpServletResponse response, String skuId, String isChecked) {
        String cookieName = "cartListCookie";
        String cookieValue = CookieUtil.getCookieValue(request, cookieName, true);
        List<OmsCartItem> cartItems = JSONObject.parseArray(cookieValue, OmsCartItem.class);
        if (cartItems == null) {
            return new ArrayList<>();
        }

        for (OmsCartItem cartItem : cartItems) {
            if (skuId.equals(cartItem.getProductSkuId())) {
                cartItem.setIsChecked(isChecked);
                cookieValue = JSONObject.toJSONString(cartItems);
                // 新值放进了 response 中，旧值还在 request 中。
                CookieUtil.setCookie(request, response, cookieName, cookieValue, 60 * 60 * 24 * 7, true);
                break;
            }
        }

        return cartItems;
    }

}
