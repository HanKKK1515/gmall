package com.hll.gmall.api.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

public class OmsOrderItem implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column
    private String orderId;
    @Column
    private String orderSn;
    @Column
    private String productId;
    @Column
    private String productPic;
    @Column
    private String productName;
    @Column
    private String productBrand;
    @Column
    private String productSn;
    @Column
    private BigDecimal productPrice;
    @Column
    private BigDecimal productQuantity;
    @Column
    private String productSkuId;
    @Column
    private String productSkuCode;
    @Column
    private String productCategoryId;
    @Column
    private String sp1;
    @Column
    private String sp2;
    @Column
    private String sp3;
    @Column
    private String promotionName;
    @Column
    private BigDecimal promotionAmount;
    @Column
    private BigDecimal couponAmount;
    @Column
    private BigDecimal integrationAmount;
    @Column
    private BigDecimal realAmount;
    @Column
    private int giftIntegration;
    @Column
    private int giftGrowth;
    @Column
    private String productAttr;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductSn() {
        return productSn;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(BigDecimal productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(String productSkuId) {
        this.productSkuId = productSkuId;
    }

    public String getProductSkuCode() {
        return productSkuCode;
    }

    public void setProductSkuCode(String productSkuCode) {
        this.productSkuCode = productSkuCode;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getSp1() {
        return sp1;
    }

    public void setSp1(String sp1) {
        this.sp1 = sp1;
    }

    public String getSp2() {
        return sp2;
    }

    public void setSp2(String sp2) {
        this.sp2 = sp2;
    }

    public String getSp3() {
        return sp3;
    }

    public void setSp3(String sp3) {
        this.sp3 = sp3;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public BigDecimal getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(BigDecimal promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getIntegrationAmount() {
        return integrationAmount;
    }

    public void setIntegrationAmount(BigDecimal integrationAmount) {
        this.integrationAmount = integrationAmount;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public int getGiftIntegration() {
        return giftIntegration;
    }

    public void setGiftIntegration(int giftIntegration) {
        this.giftIntegration = giftIntegration;
    }

    public int getGiftGrowth() {
        return giftGrowth;
    }

    public void setGiftGrowth(int giftGrowth) {
        this.giftGrowth = giftGrowth;
    }

    public String getProductAttr() {
        return productAttr;
    }

    public void setProductAttr(String productAttr) {
        this.productAttr = productAttr;
    }
}
