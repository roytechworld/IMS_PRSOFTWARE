package com.prsoftwares.dto;

public class CreditNoteDto {
	private String cnId;
	private String cnNo;
	private String saleBillNo;
	private String cnIdentityCode;
	private String cnDoc;
	private String refundItemId;
	private String refundItemSize;
	private String refundItemQty;
	private String saleRate;
	private String customerId;
	private String refundItemTot;
	private String refundSubTot;
	private String saleVat;
	private String creditAmount;
	private String userId;
	private String companyId;
	
	private String stockId;
	private int stockQty;
	private double stockItemTot;
	private double stockBuyRate;
	
	public String getCnId() {
		return cnId;
	}
	public void setCnId(String cnId) {
		this.cnId = cnId;
	}
	public String getCnNo() {
		return cnNo;
	}
	public void setCnNo(String cnNo) {
		this.cnNo = cnNo;
	}
	public String getSaleBillNo() {
		return saleBillNo;
	}
	public void setSaleBillNo(String saleBillNo) {
		this.saleBillNo = saleBillNo;
	}
	public String getCnIdentityCode() {
		return cnIdentityCode;
	}
	public void setCnIdentityCode(String cnIdentityCode) {
		this.cnIdentityCode = cnIdentityCode;
	}
	public String getCnDoc() {
		return cnDoc;
	}
	public void setCnDoc(String cnDoc) {
		this.cnDoc = cnDoc;
	}
	public String getRefundItemId() {
		return refundItemId;
	}
	public void setRefundItemId(String refundItemId) {
		this.refundItemId = refundItemId;
	}
	public String getRefundItemSize() {
		return refundItemSize;
	}
	public void setRefundItemSize(String refundItemSize) {
		this.refundItemSize = refundItemSize;
	}
	public String getRefundItemQty() {
		return refundItemQty;
	}
	public void setRefundItemQty(String refundItemQty) {
		this.refundItemQty = refundItemQty;
	}
	public String getSaleRate() {
		return saleRate;
	}
	public void setSaleRate(String saleRate) {
		this.saleRate = saleRate;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getRefundItemTot() {
		return refundItemTot;
	}
	public void setRefundItemTot(String refundItemTot) {
		this.refundItemTot = refundItemTot;
	}
	public String getRefundSubTot() {
		return refundSubTot;
	}
	public void setRefundSubTot(String refundSubTot) {
		this.refundSubTot = refundSubTot;
	}
	public String getSaleVat() {
		return saleVat;
	}
	public void setSaleVat(String saleVat) {
		this.saleVat = saleVat;
	}
	public String getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public int getStockQty() {
		return stockQty;
	}
	public void setStockQty(int stockQty) {
		this.stockQty = stockQty;
	}
	public double getStockItemTot() {
		return stockItemTot;
	}
	public void setStockItemTot(double stockItemTot) {
		this.stockItemTot = stockItemTot;
	}
	public double getStockBuyRate() {
		return stockBuyRate;
	}
	public void setStockBuyRate(double stockBuyRate) {
		this.stockBuyRate = stockBuyRate;
	}
	public CreditNoteDto(String cnId, String cnNo, String saleBillNo,
			String cnIdentityCode, String cnDoc, String refundItemId,
			String refundItemSize, String refundItemQty, String saleRate,
			String customerId, String refundItemTot, String refundSubTot,
			String saleVat, String creditAmount, String userId, String companyId,
			String stockId, int stockQty, double stockItemTot, double stockBuyRate) {
		super();
		this.cnId = cnId;
		this.cnNo = cnNo;
		this.saleBillNo = saleBillNo;
		this.cnIdentityCode = cnIdentityCode;
		this.cnDoc = cnDoc;
		this.refundItemId = refundItemId;
		this.refundItemSize = refundItemSize;
		this.refundItemQty = refundItemQty;
		this.saleRate = saleRate;
		this.customerId = customerId;
		this.refundItemTot = refundItemTot;
		this.refundSubTot = refundSubTot;
		this.saleVat = saleVat;
		this.creditAmount = creditAmount;
		this.userId = userId;
		this.companyId = companyId;
		this.stockId=stockId;
		this.stockQty=stockQty;
		this.stockItemTot=stockItemTot;
		this.stockBuyRate=stockBuyRate;
	}
	public CreditNoteDto() {
		// Default Constructor
	}
	
	
}
