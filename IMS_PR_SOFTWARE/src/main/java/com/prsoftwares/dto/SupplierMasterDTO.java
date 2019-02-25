package com.prsoftwares.dto;

public class SupplierMasterDTO {
	
	private int SupplierId;
	private String SUPPLIERNAME;
	private String ADDRESS;
	private String ContactPerson;
	private String PHONE;
	private String MOBILE;
	private String Fax	;
	private String PIN;
	private String STATE;
	private String VATNo;
	private String CSTNo;
	private String STNo;
	private String EMAIL;
	private String PAN;
	private String NOTE;
	private int userID;
	private int CompanyId;
	private String dateOfCreation;
	private String username;
	private String CompanyName;
	private String status;
	
	public synchronized int getSupplierId() {
		return SupplierId;
	}
	public synchronized void setSupplierId(int supplierId) {
		SupplierId = supplierId;
	}
	public synchronized String getSUPPLIERNAME() {
		return SUPPLIERNAME;
	}
	public synchronized void setSUPPLIERNAME(String sUPPLIERNAME) {
		SUPPLIERNAME = sUPPLIERNAME;
	}
	public synchronized String getADDRESS() {
		return ADDRESS;
	}
	public synchronized void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public synchronized String getContactPerson() {
		return ContactPerson;
	}
	public synchronized void setContactPerson(String contactPerson) {
		ContactPerson = contactPerson;
	}
	public synchronized String getPHONE() {
		return PHONE;
	}
	public synchronized void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
	
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	public synchronized String getFax() {
		return Fax;
	}
	public synchronized void setFax(String fax) {
		Fax = fax;
	}
	public synchronized String getPIN() {
		return PIN;
	}
	public synchronized void setPIN(String pIN) {
		PIN = pIN;
	}
	public synchronized String getSTATE() {
		return STATE;
	}
	public synchronized void setSTATE(String sTATE) {
		STATE = sTATE;
	}
	public synchronized String getVATNo() {
		return VATNo;
	}
	public synchronized void setVATNo(String vATNo) {
		VATNo = vATNo;
	}
	public synchronized String getCSTNo() {
		return CSTNo;
	}
	public synchronized void setCSTNo(String cSTNo) {
		CSTNo = cSTNo;
	}
	public synchronized String getSTNo() {
		return STNo;
	}
	public synchronized void setSTNo(String sTNo) {
		STNo = sTNo;
	}
	public synchronized String getEMAIL() {
		return EMAIL;
	}
	public synchronized void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public synchronized String getPAN() {
		return PAN;
	}
	public synchronized void setPAN(String pAN) {
		PAN = pAN;
	}
	public synchronized String getNOTE() {
		return NOTE;
	}
	public synchronized void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
	public synchronized int getUserID() {
		return userID;
	}
	public synchronized void setUserID(int userID) {
		this.userID = userID;
	}
	public synchronized int getCompanyId() {
		return CompanyId;
	}
	public synchronized void setCompanyId(int companyId) {
		CompanyId = companyId;
	}
	public synchronized String getDateOfCreation() {
		return dateOfCreation;
	}
	public synchronized void setDateOfCreation(String dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	public synchronized String getUsername() {
		return username;
	}
	public synchronized void setUsername(String username) {
		this.username = username;
	}
	public synchronized String getCompanyName() {
		return CompanyName;
	}
	public synchronized void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public SupplierMasterDTO(int supplierId, String sUPPLIERNAME,
			String aDDRESS, String contactPerson, String pHONE, String MOBILE, String fax,
			String pIN, String sTATE, String vATNo, String cSTNo, String sTNo,
			String eMAIL, String pAN, String nOTE, int userID,
			int companyId, String dateOfCreation, String username,
			String companyName, String status) {
		super();
		SupplierId = supplierId;
		SUPPLIERNAME = sUPPLIERNAME;
		ADDRESS = aDDRESS;
		ContactPerson = contactPerson;
		PHONE = pHONE;
		this.MOBILE=MOBILE;
		Fax = fax;
		PIN = pIN;
		STATE = sTATE;
		VATNo = vATNo;
		CSTNo = cSTNo;
		STNo = sTNo;
		EMAIL = eMAIL;
		PAN = pAN;
		NOTE = nOTE;
		this.userID = userID;
		CompanyId = companyId;
		this.dateOfCreation = dateOfCreation;
		this.username = username;
		CompanyName = companyName;
		this.status=status;
	}
	public SupplierMasterDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
