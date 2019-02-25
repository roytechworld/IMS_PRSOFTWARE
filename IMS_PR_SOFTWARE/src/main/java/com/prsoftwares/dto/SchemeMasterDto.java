package com.prsoftwares.dto;

public class SchemeMasterDto {
	private int customerId, schemeId, schemeQty, userId, companyId;
	private int[] itemIds, customerIds;
	private String itemId,schemeName, schemeDoc, schemeSdate, schemeEdate, ItemName, userName, companyName;
	private int schemeEditFlag=0;
	private String oldSchemeName;
	private String customername;
	private String itemname;
	
	public synchronized String getOldSchemeName() {
		return oldSchemeName;
	}

	public synchronized void setOldSchemeName(String oldSchemeName) {
		this.oldSchemeName = oldSchemeName;
	}


	public synchronized String getCustomername() {
		return customername;
	}

	public synchronized void setCustomername(String customername) {
		this.customername = customername;
	}

	public synchronized String getItemname() {
		return itemname;
	}

	public synchronized void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public SchemeMasterDto(String itemId, int customerId, int schemeId,
			int schemeQty, int userId, int companyId, int[] itemIds,
			int[] customerIds, String schemeName, String schemeDoc,
			String schemeSdate, String schemeEdate, String itemName,
			String userName, String companyName,int schemeEditFlag,String oldSchemeName,String itemname,String customername) {
		super();
		this.itemId = itemId;
		this.customerId = customerId;
		this.schemeId = schemeId;
		this.schemeQty = schemeQty;
		this.userId = userId;
		this.companyId = companyId;
		this.itemIds = itemIds;
		this.customerIds = customerIds;
		this.schemeName = schemeName;
		this.schemeDoc = schemeDoc;
		this.schemeSdate = schemeSdate;
		this.schemeEdate = schemeEdate;
		ItemName = itemName;
		this.userName = userName;
		this.companyName = companyName;
		this.schemeEditFlag=schemeEditFlag;
		this.oldSchemeName=oldSchemeName;
		this.itemname=itemname;
		this.customername=customername;
	}
	
	public SchemeMasterDto(){
		
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(int schemeId) {
		this.schemeId = schemeId;
	}
	public int getSchemeQty() {
		return schemeQty;
	}
	public void setSchemeQty(int schemeQty) {
		this.schemeQty = schemeQty;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public int[] getItemIds() {
		return itemIds;
	}
	public void setItemIds(int[] itemIds) {
		this.itemIds = itemIds;
	}
	public int[] getCustomerIds() {
		return customerIds;
	}
	public void setCustomerIds(int[] customerIds) {
		this.customerIds = customerIds;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getSchemeDoc() {
		return schemeDoc;
	}
	public void setSchemeDoc(String schemeDoc) {
		this.schemeDoc = schemeDoc;
	}
	public String getSchemeSdate() {
		return schemeSdate;
	}
	public void setSchemeSdate(String schemeSdate) {
		this.schemeSdate = schemeSdate;
	}
	public String getSchemeEdate() {
		return schemeEdate;
	}
	public void setSchemeEdate(String schemeEdate) {
		this.schemeEdate = schemeEdate;
	}
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public synchronized int getSchemeEditFlag() {
		return schemeEditFlag;
	}

	public synchronized void setSchemeEditFlag(int schemeEditFlag) {
		this.schemeEditFlag = schemeEditFlag;
	}
	
	
		
}
