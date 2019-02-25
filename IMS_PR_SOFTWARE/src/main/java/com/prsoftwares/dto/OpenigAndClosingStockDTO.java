package com.prsoftwares.dto;

public class OpenigAndClosingStockDTO {
	
	
	private int    Id;
	private String financialYear;
	private String itemIds;
	private String qty;
	private String companyId;
	private String userId;
	private String remarks;
	private String stockManupulation;
	private String stockId;
	
	
	public synchronized String getStockId() {
		return stockId;
	}
	public synchronized void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public synchronized String getRemarks() {
		return remarks;
	}
	public synchronized void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public synchronized String getStockManupulation() {
		return stockManupulation;
	}
	public synchronized void setStockManupulation(String stockManupulation) {
		this.stockManupulation = stockManupulation;
	}
	public synchronized int getId() {
		return Id;
	}
	public synchronized void setId(int id) {
		Id = id;
	}
	public synchronized String getFinancialYear() {
		return financialYear;
	}
	public synchronized void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}
	public synchronized String getItemIds() {
		return itemIds;
	}
	public synchronized void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}
	public synchronized String getQty() {
		return qty;
	}
	public synchronized void setQty(String qty) {
		this.qty = qty;
	}
	public synchronized String getCompanyId() {
		return companyId;
	}
	public synchronized void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public synchronized String getUserId() {
		return userId;
	}
	public synchronized void setUserId(String userId) {
		this.userId = userId;
	}
	

	public OpenigAndClosingStockDTO(int id, String financialYear,
			String itemIds, String qty, String companyId, String userId,
			String remarks, String stockManupulation, String stockId) {
		super();
		Id = id;
		this.financialYear = financialYear;
		this.itemIds = itemIds;
		this.qty = qty;
		this.companyId = companyId;
		this.userId = userId;
		this.remarks = remarks;
		this.stockManupulation = stockManupulation;
		this.stockId = stockId;
	}
	public OpenigAndClosingStockDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
