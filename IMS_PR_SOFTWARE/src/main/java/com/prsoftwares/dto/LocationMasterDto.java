package com.prsoftwares.dto;

public class LocationMasterDto {
	private int locationId,companyId,userId;
	private String locationAddress,locationName,locationDescription,locationPhone;
	private String locationMobile, locationPin,locationState,locationFax,locationEmail, companyName, userName;
	
	public LocationMasterDto(){
		
	}

	public LocationMasterDto(int locationId, int companyId, int userId,
			String locationAddress, String locationName, String locationDescription,
			String locationPhone, String locationMobile, String locationPin,
			String locationState, String companyName, String userName) {
		super();
		this.locationId = locationId;
		this.companyId = companyId;
		this.userId = userId;
		this.locationName=locationName;
		this.locationAddress = locationAddress;
		this.locationDescription = locationDescription;
		this.locationPhone = locationPhone;
		this.locationMobile = locationMobile;
		this.locationPin = locationPin;
		this.locationState = locationState;
		this.companyName=companyName;
		this.userName=userName;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	
	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public String getLocationPhone() {
		return locationPhone;
	}

	public void setLocationPhone(String locationPhone) {
		this.locationPhone = locationPhone;
	}

	public String getLocationMobile() {
		return locationMobile;
	}

	public void setLocationMobile(String locationMobile) {
		this.locationMobile = locationMobile;
	}

	public String getLocationPin() {
		return locationPin;
	}

	public void setLocationPin(String locationPin) {
		this.locationPin = locationPin;
	}

	public String getLocationState() {
		return locationState;
	}

	public void setLocationState(String locationState) {
		this.locationState = locationState;
	}

	public String getLocationFax() {
		return locationFax;
	}

	public void setLocationFax(String locationFax) {
		this.locationFax = locationFax;
	}

	public String getLocationEmail() {
		return locationEmail;
	}

	public void setLocationEmail(String locationEmail) {
		this.locationEmail = locationEmail;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
}
