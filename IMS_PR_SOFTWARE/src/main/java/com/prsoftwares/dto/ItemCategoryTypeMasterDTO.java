package com.prsoftwares.dto;

public class ItemCategoryTypeMasterDTO {
	
	private int itemTypeID;
	private int productId;
	private String itemId;
	private String itemName;
	private String itemType;
	private String typeDateofcreation;
	private String itemDateofcreation;
	private int userID;
	private String userName;
	private int companyID;
	private String companyname;
	private int itemBrandId;
	private String itemBrandName;
	private String itemBrandDOC;
	private int itemSizeId;
	private String itemSizeName;
	private String itemSizeDOC;
	private int supplierId;
	private String supplierName;
	private String itemMrp;
	private String itemUnit;
	private String ItemTypeDescription;
	private String barcode;
	private String itemsLength;
	private String hsncode;
	
	
	public synchronized String getItemsLength() {
		return itemsLength;
	}
	public synchronized void setItemsLength(String itemsLength) {
		this.itemsLength = itemsLength;
	}
	public synchronized int getItemTypeID() {
		return itemTypeID;
	}
	public synchronized void setItemTypeID(int itemTypeID) {
		this.itemTypeID = itemTypeID;
	}
	public synchronized int getProductId() {
		return productId;
	}
	public synchronized void setProductId(int productId) {
		this.productId = productId;
	}
	public synchronized String getItemId() {
		return itemId;
	}
	public synchronized void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public synchronized String getItemName() {
		return itemName;
	}
	public synchronized void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public synchronized String getItemType() {
		return itemType;
	}
	public synchronized void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public synchronized String getTypeDateofcreation() {
		return typeDateofcreation;
	}
	public synchronized void setTypeDateofcreation(String typeDateofcreation) {
		this.typeDateofcreation = typeDateofcreation;
	}
	public synchronized String getItemDateofcreation() {
		return itemDateofcreation;
	}
	public synchronized void setItemDateofcreation(String itemDateofcreation) {
		this.itemDateofcreation = itemDateofcreation;
	}
	public synchronized int getUserID() {
		return userID;
	}
	public synchronized void setUserID(int userID) {
		this.userID = userID;
	}
	public synchronized String getUserName() {
		return userName;
	}
	public synchronized void setUserName(String userName) {
		this.userName = userName;
	}
	public synchronized int getCompanyID() {
		return companyID;
	}
	public synchronized void setCompanyID(int companyID) {
		this.companyID = companyID;
	}
	public synchronized String getCompanyname() {
		return companyname;
	}
	public synchronized void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public synchronized int getItemBrandId() {
		return itemBrandId;
	}
	public synchronized void setItemBrandId(int itemBrandId) {
		this.itemBrandId = itemBrandId;
	}
	public synchronized String getItemBrandName() {
		return itemBrandName;
	}
	public synchronized void setItemBrandName(String itemBrandName) {
		this.itemBrandName = itemBrandName;
	}
	public synchronized String getItemBrandDOC() {
		return itemBrandDOC;
	}
	public synchronized void setItemBrandDOC(String itemBrandDOC) {
		this.itemBrandDOC = itemBrandDOC;
	}
	public synchronized int getItemSizeId() {
		return itemSizeId;
	}
	public synchronized void setItemSizeId(int itemSizeId) {
		this.itemSizeId = itemSizeId;
	}
	public synchronized String getItemSizeName() {
		return itemSizeName;
	}
	public synchronized void setItemSizeName(String itemSizeName) {
		this.itemSizeName = itemSizeName;
	}
	public synchronized String getItemSizeDOC() {
		return itemSizeDOC;
	}
	public synchronized void setItemSizeDOC(String itemSizeDOC) {
		this.itemSizeDOC = itemSizeDOC;
	}
	public synchronized int getSupplierId() {
		return supplierId;
	}
	public synchronized void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public synchronized String getSupplierName() {
		return supplierName;
	}
	public synchronized void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public synchronized String getItemMrp() {
		return itemMrp;
	}
	public synchronized void setItemMrp(String itemMrp) {
		this.itemMrp = itemMrp;
	}
	public synchronized String getItemUnit() {
		return itemUnit;
	}
	public synchronized void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}
	public synchronized String getItemTypeDescription() {
		return ItemTypeDescription;
	}
	public synchronized void setItemTypeDescription(String itemTypeDescription) {
		ItemTypeDescription = itemTypeDescription;
	}
	public synchronized String getBarcode() {
		return barcode;
	}
	public synchronized void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public String getHsncode() {
		return hsncode;
	}
	public void setHsncode(String hsncode) {
		this.hsncode = hsncode;
	}
	
	public ItemCategoryTypeMasterDTO(int itemTypeID, int productId, String itemId, String itemName, String itemType,
			String typeDateofcreation, String itemDateofcreation, int userID, String userName, int companyID,
			String companyname, int itemBrandId, String itemBrandName, String itemBrandDOC, int itemSizeId,
			String itemSizeName, String itemSizeDOC, int supplierId, String supplierName, String itemMrp,
			String itemUnit, String itemTypeDescription, String barcode, String itemsLength, String hsncode) {
		super();
		this.itemTypeID = itemTypeID;
		this.productId = productId;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemType = itemType;
		this.typeDateofcreation = typeDateofcreation;
		this.itemDateofcreation = itemDateofcreation;
		this.userID = userID;
		this.userName = userName;
		this.companyID = companyID;
		this.companyname = companyname;
		this.itemBrandId = itemBrandId;
		this.itemBrandName = itemBrandName;
		this.itemBrandDOC = itemBrandDOC;
		this.itemSizeId = itemSizeId;
		this.itemSizeName = itemSizeName;
		this.itemSizeDOC = itemSizeDOC;
		this.supplierId = supplierId;
		this.supplierName = supplierName;
		this.itemMrp = itemMrp;
		this.itemUnit = itemUnit;
		ItemTypeDescription = itemTypeDescription;
		this.barcode = barcode;
		this.itemsLength = itemsLength;
		this.hsncode = hsncode;
	}
	public ItemCategoryTypeMasterDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}