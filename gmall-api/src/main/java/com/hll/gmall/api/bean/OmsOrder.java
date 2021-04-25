package com.hll.gmall.api.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OmsOrder implements Serializable {


    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column
    private String memberId;
    @Column
    private String couponId;
    @Column
    private String orderSn;
    @Column
    private Date createTime;
    @Column
    private String memberUsername;
    @Column
    private BigDecimal totalAmount;
    @Column
    private BigDecimal payAmount;
    @Column
    private BigDecimal freightAmount;
    @Column
    private BigDecimal promotionAmount;
    @Column
    private BigDecimal integrationAmount;
    @Column
    private BigDecimal couponAmount;
    @Column
    private BigDecimal discountAmount;
    @Column
    private Integer payType;
    @Column
    private Integer sourceType;
    @Column
    private Integer status;
    @Column
    private Integer orderType;
    @Column
    private String deliveryCompany;
    @Column
    private String deliverySn;
    @Column
    private Integer autoConfirmDay;
    @Column
    private Integer integration;
    @Column
    private Integer growth;
    @Column
    private String promotionInfo;
    @Column
    private Integer billType;
    @Column
    private String billHeader;
    @Column
    private String billContent;
    @Column
    private String billReceiverPhone;
    @Column
    private String billReceiverEmail;
    @Column
    private String receiverName;
    @Column
    private String receiverPhone;
    @Column
    private String receiverPostCode;
    @Column
    private String receiverProvince;
    @Column
    private String receiverCity;
    @Column
    private String receiverRegion;
    @Column
    private String receiverDetailAddress;
    @Column
    private String note;
    @Column
    private Integer confirmStatus;
    @Column
    private Integer deleteStatus;
    @Column
    private Integer useIntegration;
    @Column
    private Date paymentTime;
    @Column
    private Date deliveryTime;
    @Column
    private Date receiveTime;
    @Column
    private Date commentTime;
    @Column
    private Date modifyTime;


    @Transient
    List<OmsOrderItem> omsOrderItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMemberUsername() {
        return memberUsername;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public BigDecimal getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(BigDecimal promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public BigDecimal getIntegrationAmount() {
        return integrationAmount;
    }

    public void setIntegrationAmount(BigDecimal integrationAmount) {
        this.integrationAmount = integrationAmount;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
    }

    public String getDeliverySn() {
        return deliverySn;
    }

    public void setDeliverySn(String deliverySn) {
        this.deliverySn = deliverySn;
    }

    public Integer getAutoConfirmDay() {
        return autoConfirmDay;
    }

    public void setAutoConfirmDay(Integer autoConfirmDay) {
        this.autoConfirmDay = autoConfirmDay;
    }

    public Integer getIntegration() {
        return integration;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public String getPromotionInfo() {
        return promotionInfo;
    }

    public void setPromotionInfo(String promotionInfo) {
        this.promotionInfo = promotionInfo;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getBillHeader() {
        return billHeader;
    }

    public void setBillHeader(String billHeader) {
        this.billHeader = billHeader;
    }

    public String getBillContent() {
        return billContent;
    }

    public void setBillContent(String billContent) {
        this.billContent = billContent;
    }

    public String getBillReceiverPhone() {
        return billReceiverPhone;
    }

    public void setBillReceiverPhone(String billReceiverPhone) {
        this.billReceiverPhone = billReceiverPhone;
    }

    public String getBillReceiverEmail() {
        return billReceiverEmail;
    }

    public void setBillReceiverEmail(String billReceiverEmail) {
        this.billReceiverEmail = billReceiverEmail;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverPostCode() {
        return receiverPostCode;
    }

    public void setReceiverPostCode(String receiverPostCode) {
        this.receiverPostCode = receiverPostCode;
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverRegion() {
        return receiverRegion;
    }

    public void setReceiverRegion(String receiverRegion) {
        this.receiverRegion = receiverRegion;
    }

    public String getReceiverDetailAddress() {
        return receiverDetailAddress;
    }

    public void setReceiverDetailAddress(String receiverDetailAddress) {
        this.receiverDetailAddress = receiverDetailAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Integer getUseIntegration() {
        return useIntegration;
    }

    public void setUseIntegration(Integer useIntegration) {
        this.useIntegration = useIntegration;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public List<OmsOrderItem> getOmsOrderItems() {
        return omsOrderItems;
    }

    public void setOmsOrderItems(List<OmsOrderItem> omsOrderItems) {
        this.omsOrderItems = omsOrderItems;
    }
}
