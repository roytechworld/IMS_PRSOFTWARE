package com.prsoftwares.dto;

import java.io.File;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class LoginUserCompanyMasterDto{
	
	private String userId;
	private String userName;
	private String Email;
	private File userPhoto;
	private String password;
	private String RoleStatus;
	private String userUrl;
	private String nameofcompany;
	private String phoneno;
	private String contactperson;
	private String acno;
	private String address;
	private String vatno;
	private String panno;
	private String serviceTaxno;
	private String BankDetails;
	private String companyId;
	
	private String yearRange;

	public synchronized String getCompanyId() {
		return companyId;
	}
	public synchronized void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public synchronized String getAcno() {
		return acno;
	}
	public synchronized void setAcno(String acno) {
		this.acno = acno;
	}
	public synchronized String getUserId() {
		return userId;
	}
	public synchronized void setUserId(String userId) {
		this.userId = userId;
	}
	public synchronized String getUserName() {
		return userName;
	}
	public synchronized void setUserName(String userName) {
		this.userName = userName;
	}

	public synchronized File getUserPhoto() {
		return userPhoto;
	}
	public synchronized void setUserPhoto(File userPhoto) {
		this.userPhoto = userPhoto;
	}
	public synchronized String getPassword() {
		return password;
	}
	public synchronized void setPassword(String password) {
		this.password = password;
	}
	public synchronized String getRoleStatus() {
		return RoleStatus;
	}
	public synchronized void setRoleStatus(String roleStatus) {
		RoleStatus = roleStatus;
	}
	public synchronized String getUserUrl() {
		return userUrl;
	}
	public synchronized void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}
	public synchronized String getNameofcompany() {
		return nameofcompany;
	}
	public synchronized void setNameofcompany(String nameofcompany) {
		this.nameofcompany = nameofcompany;
	}
	public synchronized String getPhoneno() {
		return phoneno;
	}
	public synchronized void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}
	public synchronized String getContactperson() {
		return contactperson;
	}
	public synchronized void setContactperson(String contactperson) {
		this.contactperson = contactperson;
	}

	public synchronized String getEmail() {
		return Email;
	}
	public synchronized void setEmail(String email) {
		Email = email;
	}
	public synchronized String getAddress() {
		return address;
	}
	public synchronized void setAddress(String address) {
		this.address = address;
	}
	public synchronized String getVatno() {
		return vatno;
	}
	public synchronized void setVatno(String vatno) {
		this.vatno = vatno;
	}
	public synchronized String getPanno() {
		return panno;
	}
	public synchronized void setPanno(String panno) {
		this.panno = panno;
	}
	public synchronized String getServiceTaxno() {
		return serviceTaxno;
	}
	public synchronized void setServiceTaxno(String serviceTaxno) {
		this.serviceTaxno = serviceTaxno;
	}
	public synchronized String getBankDetails() {
		return BankDetails;
	}
	public synchronized void setBankDetails(String bankDetails) {
		BankDetails = bankDetails;
	}
	public LoginUserCompanyMasterDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getYearRange() {
		return yearRange;
	}
	public void setYearRange(String yearRange) {
		this.yearRange = yearRange;
	}
	public LoginUserCompanyMasterDto(String userId, String userName,
			String email, File userPhoto, String password, String roleStatus,
			String userUrl, String nameofcompany, String phoneno,
			String contactperson, String acno, String address, String vatno,
			String panno, String serviceTaxno, String bankDetails,
			String companyId, String yearRange) {
		super();
		this.userId = userId;
		this.userName = userName;
		Email = email;
		this.userPhoto = userPhoto;
		this.password = password;
		RoleStatus = roleStatus;
		this.userUrl = userUrl;
		this.nameofcompany = nameofcompany;
		this.phoneno = phoneno;
		this.contactperson = contactperson;
		this.acno = acno;
		this.address = address;
		this.vatno = vatno;
		this.panno = panno;
		this.serviceTaxno = serviceTaxno;
		BankDetails = bankDetails;
		this.companyId = companyId;
		this.yearRange = yearRange;
	}
	

}
