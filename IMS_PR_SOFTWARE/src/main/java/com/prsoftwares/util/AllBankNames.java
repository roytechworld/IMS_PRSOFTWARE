package com.prsoftwares.util;

import java.util.ArrayList;

public class AllBankNames {
	public String bankId;
	 public String banknames;
	public synchronized String getBankId() {
		return bankId;
	}
	public synchronized void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public synchronized String getBanknames() {
		return banknames;
	}
	public synchronized void setBanknames(String banknames) {
		this.banknames = banknames;
	}
	public AllBankNames(String bankId, String banknames) {
		super();
		this.bankId = bankId;
		this.banknames = banknames;
	}
	public AllBankNames() {
		super();
		// TODO Auto-generated constructor stub
	}


	 

}
