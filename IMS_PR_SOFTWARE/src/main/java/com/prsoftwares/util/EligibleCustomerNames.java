package com.prsoftwares.util;

public class EligibleCustomerNames {
	
	public String customerId;
	public String customerNames;
	public String itemname;
	public String itemId;
	public String salequantity;
	public String totalQuantitySold;
	public String schemeQty;
	
	public synchronized String getItemname() {
		return itemname;
	}
	public synchronized void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public synchronized String getItemId() {
		return itemId;
	}
	public synchronized void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public synchronized String getSalequantity() {
		return salequantity;
	}
	public synchronized void setSalequantity(String salequantity) {
		this.salequantity = salequantity;
	}
	public synchronized String getTotalQuantitySold() {
		return totalQuantitySold;
	}
	public synchronized void setTotalQuantitySold(String totalQuantitySold) {
		this.totalQuantitySold = totalQuantitySold;
	}
	public synchronized String getCustomerId() {
		return customerId;
	}
	public synchronized void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public synchronized String getCustomerNames() {
		return customerNames;
	}
	public synchronized void setCustomerNames(String customerNames) {
		this.customerNames = customerNames;
	}
	
	public String getSchemeQty() {
		return schemeQty;
	}
	public void setSchemeQty(String schemeQty) {
		this.schemeQty = schemeQty;
	}
	public EligibleCustomerNames(String customerId, String customerNames,
			String itemname, String itemId, String salequantity,
			String totalQuantitySold, String schemeQty) {
		super();
		this.customerId = customerId;
		this.customerNames = customerNames;
		this.itemname = itemname;
		this.itemId = itemId;
		this.salequantity = salequantity;
		this.totalQuantitySold = totalQuantitySold;
		this.schemeQty=schemeQty;
	}
	public EligibleCustomerNames() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
