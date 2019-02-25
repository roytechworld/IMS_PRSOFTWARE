package com.prsoftwares.dto;

public class SmsMaster {
	
	
	private String smsid;
	private String smsamount;
	private String smsremain;
	private String userid;
	private String lastsmsjson;
	
	
	public String getSmsid() {
		return smsid;
	}
	public void setSmsid(String smsid) {
		this.smsid = smsid;
	}
	public String getSmsamount() {
		return smsamount;
	}
	public void setSmsamount(String smsamount) {
		this.smsamount = smsamount;
	}
	public String getSmsremain() {
		return smsremain;
	}
	public void setSmsremain(String smsremain) {
		this.smsremain = smsremain;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getLastsmsjson() {
		return lastsmsjson;
	}
	public void setLastsmsjson(String lastsmsjson) {
		this.lastsmsjson = lastsmsjson;
	}

	public SmsMaster(String smsid, String smsamount, String smsremain, String userid, String lastsmsjson) {
		super();
		this.smsid = smsid;
		this.smsamount = smsamount;
		this.smsremain = smsremain;
		this.userid = userid;
		this.lastsmsjson = lastsmsjson;
	}
	public SmsMaster() {
		super();
		// TODO Auto-generated constructor stub
	}

}
