package com.prsoftwares.dto;

public class CustomerMasterDTO {
	
	
private String 	customerId;
private String	cName;
private String	phoneno;
private String	mobile;
private String	email;
private String	address;
private String	state;
private String	pincode;
private String	Fax;
private String	vat;
private String	type;
private String	note;
private String	dateofcreate	;
private String	companyid	;
private String	userid;
private String userName;
private String companyName;
private String remarks;
private String customerBarcodeUrl;
private String customerBarcode;
private String customerType;
private String gsttin;
private String stcode;

public synchronized String getCustomerType() {
	return customerType;
}
public synchronized void setCustomerType(String customerType) {
	this.customerType = customerType;
}
public synchronized String getCustomerId() {
	return customerId;
}
public synchronized void setCustomerId(String customerId) {
	this.customerId = customerId;
}
public synchronized String getcName() {
	return cName;
}
public synchronized void setcName(String cName) {
	this.cName = cName;
}
public synchronized String getPhoneno() {
	return phoneno;
}
public synchronized void setPhoneno(String phoneno) {
	this.phoneno = phoneno;
}
public synchronized String getMobile() {
	return mobile;
}
public synchronized void setMobile(String mobile) {
	this.mobile = mobile;
}
public synchronized String getEmail() {
	return email;
}
public synchronized void setEmail(String email) {
	this.email = email;
}
public synchronized String getAddress() {
	return address;
}
public synchronized void setAddress(String address) {
	this.address = address;
}
public synchronized String getState() {
	return state;
}
public synchronized void setState(String state) {
	this.state = state;
}
public synchronized String getPincode() {
	return pincode;
}
public synchronized void setPincode(String pincode) {
	this.pincode = pincode;
}
public synchronized String getFax() {
	return Fax;
}
public synchronized void setFax(String fax) {
	Fax = fax;
}
public synchronized String getVat() {
	return vat;
}
public synchronized void setVat(String vat) {
	this.vat = vat;
}
public synchronized String getType() {
	return type;
}
public synchronized void setType(String type) {
	this.type = type;
}
public synchronized String getNote() {
	return note;
}
public synchronized void setNote(String note) {
	this.note = note;
}
public synchronized String getDateofcreate() {
	return dateofcreate;
}
public synchronized void setDateofcreate(String dateofcreate) {
	this.dateofcreate = dateofcreate;
}
public synchronized String getCompanyid() {
	return companyid;
}
public synchronized void setCompanyid(String companyid) {
	this.companyid = companyid;
}
public synchronized String getUserid() {
	return userid;
}
public synchronized void setUserid(String userid) {
	this.userid = userid;
}
public synchronized String getUserName() {
	return userName;
}
public synchronized void setUserName(String userName) {
	this.userName = userName;
}
public synchronized String getCompanyName() {
	return companyName;
}
public synchronized void setCompanyName(String companyName) {
	this.companyName = companyName;
}
public synchronized String getRemarks() {
	return remarks;
}
public synchronized void setRemarks(String remarks) {
	this.remarks = remarks;
}
public synchronized String getCustomerBarcodeUrl() {
	return customerBarcodeUrl;
}
public synchronized void setCustomerBarcodeUrl(String customerBarcodeUrl) {
	this.customerBarcodeUrl = customerBarcodeUrl;
}
public synchronized String getCustomerBarcode() {
	return customerBarcode;
}
public synchronized void setCustomerBarcode(String customerBarcode) {
	this.customerBarcode = customerBarcode;
}



public String getGsttin() {
	return gsttin;
}
public void setGsttin(String gsttin) {
	this.gsttin = gsttin;
}

public String getStcode() {
	return stcode;
}
public void setStcode(String stcode) {
	this.stcode = stcode;
}

public CustomerMasterDTO(String customerId, String cName, String phoneno, String mobile, String email, String address,
		String state, String pincode, String fax, String vat, String type, String note, String dateofcreate,
		String companyid, String userid, String userName, String companyName, String remarks, String customerBarcodeUrl,
		String customerBarcode, String customerType, String gsttin, String stcode) {
	super();
	this.customerId = customerId;
	this.cName = cName;
	this.phoneno = phoneno;
	this.mobile = mobile;
	this.email = email;
	this.address = address;
	this.state = state;
	this.pincode = pincode;
	Fax = fax;
	this.vat = vat;
	this.type = type;
	this.note = note;
	this.dateofcreate = dateofcreate;
	this.companyid = companyid;
	this.userid = userid;
	this.userName = userName;
	this.companyName = companyName;
	this.remarks = remarks;
	this.customerBarcodeUrl = customerBarcodeUrl;
	this.customerBarcode = customerBarcode;
	this.customerType = customerType;
	this.gsttin = gsttin;
	this.stcode = stcode;
}
public CustomerMasterDTO() {
	super();
	// TODO Auto-generated constructor stub
}








}
