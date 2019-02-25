package com.prsoftwares.dto;

public class ItemMasterDTO {
	private int itemId;
	private String itemName;
	private String createdBy;
	private String dateofcreation;
	private int ItemTypeID;
	private int userID;
	private int companyID;
	private String hsncode;
	
	
	
	
	public synchronized int getItemId() {
		return itemId;
	}
	public synchronized void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public synchronized String getItemName() {
		return itemName;
	}
	public synchronized void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public synchronized String getCreatedBy() {
		return createdBy;
	}
	public synchronized void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public synchronized String getDateofcreation() {
		return dateofcreation;
	}
	public synchronized void setDateofcreation(String dateofcreation) {
		this.dateofcreation = dateofcreation;
	}
	public synchronized int getItemTypeID() {
		return ItemTypeID;
	}
	public synchronized void setItemTypeID(int itemTypeID) {
		ItemTypeID = itemTypeID;
	}
	public synchronized int getUserID() {
		return userID;
	}
	public synchronized void setUserID(int userID) {
		this.userID = userID;
	}
	public synchronized int getCompanyID() {
		return companyID;
	}
	public synchronized void setCompanyID(int companyID) {
		this.companyID = companyID;
	}
	public ItemMasterDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getHsncode() {
		return hsncode;
	}
	public void setHsncode(String hsncode) {
		this.hsncode = hsncode;
	}
	public ItemMasterDTO(int itemId, String itemName, String createdBy, String dateofcreation, int itemTypeID,
			int userID, int companyID, String hsncode) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.createdBy = createdBy;
		this.dateofcreation = dateofcreation;
		ItemTypeID = itemTypeID;
		this.userID = userID;
		this.companyID = companyID;
		this.hsncode = hsncode;
	}
	
	
	
	

}
