package com.prsoftwares.dto;

public class PurchaseLedgerDto {
	private int ledgerId;
	private String dnNo;
	private String debitAmount;
	private String dnDoc;
	private int totalReturnQty;
	private String billNo;
	private String challanNo;
	private String purchaseAmount;
	private String purchaseDate;	
	private int totalItemQty;
	private String buyRate;
	private String subTot;
	private String lessUnitScheme;
	private String discounts;
	private String vat;
	private String cst;
	private String waybill;
	private String lorryFreight;
	private String itemId;
	private int itemTot;
	private int supplierId;
	private String itemName;
	private String[] itemNames;
	private int[] itemIds;
	private String totPurchaseAmount;
	private String totDebitAmount;
	private String totOutStandingAmount;
	private String taxamount;
	private String returnTotal;
	private String returnquantity;
	private String supplierName;
	private String supplieraddress;
	private String currentCompanyName;
	private String itemSize;
	private String titleforExcel;
	private String itemTotDoubleFormate;
	private String perboxqty;
	private String boxqty;
	private String pc;
	private String paymentdate;
	private String paymentamount;
	private String dueamount;
	private String paymentstatus;
	private String paymentmode;
	private String chequeno;
	private String chequebankname;
	private String chequedate;
	private String totalamount;
	private String subtotalbegoregst;
	public String gstrate;
	public String gstamt;
	
	public String sgstamt;
	public String sgstrate;
	
	public String cgstamt;
	public String cgstrate;
	
	public String igstamt;
	public String igstrate;
	
	public String gsttin;
	public String basetotal;
	public int incnum;
	
	public String stcode;
	public String suppliergsttinno;
	public String discamt;
	
	public String getPaymentdate() {
		return paymentdate;
	}
	public void setPaymentdate(String paymentdate) {
		this.paymentdate = paymentdate;
	}
	public String getPaymentamount() {
		return paymentamount;
	}
	public void setPaymentamount(String paymentamount) {
		this.paymentamount = paymentamount;
	}
	public String getDueamount() {
		return dueamount;
	}
	public void setDueamount(String dueamount) {
		this.dueamount = dueamount;
	}
	public synchronized String getPc() {
		return pc;
	}
	public synchronized void setPc(String pc) {
		this.pc = pc;
	}
	public int getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(int ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getDnNo() {
		return dnNo;
	}
	public void setDnNo(String dnNo) {
		this.dnNo = dnNo;
	}
	public String getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(String debitAmount) {
		this.debitAmount = debitAmount;
	}
	public String getDnDoc() {
		return dnDoc;
	}
	public void setDnDoc(String dnDoc) {
		this.dnDoc = dnDoc;
	}
	public int getTotalReturnQty() {
		return totalReturnQty;
	}
	public void setTotalReturnQty(int totalReturnQty) {
		this.totalReturnQty = totalReturnQty;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getChallanNo() {
		return challanNo;
	}
	public void setChallanNo(String challanNo) {
		this.challanNo = challanNo;
	}
	public String getPurchaseAmount() {
		return purchaseAmount;
	}
	public void setPurchaseAmount(String purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public int getTotalItemQty() {
		return totalItemQty;
	}
	public void setTotalItemQty(int totalItemQty) {
		this.totalItemQty = totalItemQty;
	}
	public String getBuyRate() {
		return buyRate;
	}
	public void setBuyRate(String buyRate) {
		this.buyRate = buyRate;
	}
	public String getSubTot() {
		return subTot;
	}
	public void setSubTot(String subTot) {
		this.subTot = subTot;
	}
	public String getLessUnitScheme() {
		return lessUnitScheme;
	}
	public void setLessUnitScheme(String lessUnitScheme) {
		this.lessUnitScheme = lessUnitScheme;
	}
	public String getDiscounts() {
		return discounts;
	}
	public void setDiscounts(String discounts) {
		this.discounts = discounts;
	}
	public String getVat() {
		return vat;
	}
	public void setVat(String vat) {
		this.vat = vat;
	}
	public String getCst() {
		return cst;
	}
	public void setCst(String cst) {
		this.cst = cst;
	}
	public String getWaybill() {
		return waybill;
	}
	public void setWaybill(String waybill) {
		this.waybill = waybill;
	}
	public String getLorryFreight() {
		return lorryFreight;
	}
	public void setLorryFreight(String lorryFreight) {
		this.lorryFreight = lorryFreight;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getItemTot() {
		return itemTot;
	}
	public void setItemTot(int itemTot) {
		this.itemTot = itemTot;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String[] getItemNames() {
		return itemNames;
	}
	public void setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
	}
	public int[] getItemIds() {
		return itemIds;
	}
	public void setItemIds(int[] itemIds) {
		this.itemIds = itemIds;
	}
	public String getTotPurchaseAmount() {
		return totPurchaseAmount;
	}
	public void setTotPurchaseAmount(String totPurchaseAmount) {
		this.totPurchaseAmount = totPurchaseAmount;
	}
	public String getTotDebitAmount() {
		return totDebitAmount;
	}
	public void setTotDebitAmount(String totDebitAmount) {
		this.totDebitAmount = totDebitAmount;
	}
	public String getTotOutStandingAmount() {
		return totOutStandingAmount;
	}
	public void setTotOutStandingAmount(String totOutStandingAmount) {
		this.totOutStandingAmount = totOutStandingAmount;
	}
	public String getTaxamount() {
		return taxamount;
	}
	public void setTaxamount(String taxamount) {
		this.taxamount = taxamount;
	}
	public String getReturnTotal() {
		return returnTotal;
	}
	public void setReturnTotal(String returnTotal) {
		this.returnTotal = returnTotal;
	}
	public String getReturnquantity() {
		return returnquantity;
	}
	public void setReturnquantity(String returnquantity) {
		this.returnquantity = returnquantity;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplieraddress() {
		return supplieraddress;
	}
	public void setSupplieraddress(String supplieraddress) {
		this.supplieraddress = supplieraddress;
	}
	public String getCurrentCompanyName() {
		return currentCompanyName;
	}
	public void setCurrentCompanyName(String currentCompanyName) {
		this.currentCompanyName = currentCompanyName;
	}
	public String getItemSize() {
		return itemSize;
	}
	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}
	public String getTitleforExcel() {
		return titleforExcel;
	}
	public void setTitleforExcel(String titleforExcel) {
		this.titleforExcel = titleforExcel;
	}
	public String getItemTotDoubleFormate() {
		return itemTotDoubleFormate;
	}
	public void setItemTotDoubleFormate(String itemTotDoubleFormate) {
		this.itemTotDoubleFormate = itemTotDoubleFormate;
	}
	public String getPerboxqty() {
		return perboxqty;
	}
	public void setPerboxqty(String perboxqty) {
		this.perboxqty = perboxqty;
	}
	public String getBoxqty() {
		return boxqty;
	}
	public void setBoxqty(String boxqty) {
		this.boxqty = boxqty;
	}
	
	public String getPaymentstatus() {
		return paymentstatus;
	}
	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus = paymentstatus;
	}
	public String getPaymentmode() {
		return paymentmode;
	}
	public void setPaymentmode(String paymentmode) {
		this.paymentmode = paymentmode;
	}
	
	
	public String getChequeno() {
		return chequeno;
	}
	public void setChequeno(String chequeno) {
		this.chequeno = chequeno;
	}
	public String getChequebankname() {
		return chequebankname;
	}
	public void setChequebankname(String chequebankname) {
		this.chequebankname = chequebankname;
	}
	
	
	public String getChequedate() {
		return chequedate;
	}
	public void setChequedate(String chequedate) {
		this.chequedate = chequedate;
	}
	public PurchaseLedgerDto() {
		super();
		// Default
	}
	
	
	public String getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}
	
	
	
	
	public String getSubtotalbegoregst() {
		return subtotalbegoregst;
	}
	public void setSubtotalbegoregst(String subtotalbegoregst) {
		this.subtotalbegoregst = subtotalbegoregst;
	}
	public String getGstrate() {
		return gstrate;
	}
	public void setGstrate(String gstrate) {
		this.gstrate = gstrate;
	}
	public String getGstamt() {
		return gstamt;
	}
	public void setGstamt(String gstamt) {
		this.gstamt = gstamt;
	}
	public String getSgstamt() {
		return sgstamt;
	}
	public void setSgstamt(String sgstamt) {
		this.sgstamt = sgstamt;
	}
	public String getSgstrate() {
		return sgstrate;
	}
	public void setSgstrate(String sgstrate) {
		this.sgstrate = sgstrate;
	}
	public String getCgstamt() {
		return cgstamt;
	}
	public void setCgstamt(String cgstamt) {
		this.cgstamt = cgstamt;
	}
	public String getCgstrate() {
		return cgstrate;
	}
	public void setCgstrate(String cgstrate) {
		this.cgstrate = cgstrate;
	}
	public String getIgstamt() {
		return igstamt;
	}
	public void setIgstamt(String igstamt) {
		this.igstamt = igstamt;
	}
	public String getIgstrate() {
		return igstrate;
	}
	public void setIgstrate(String igstrate) {
		this.igstrate = igstrate;
	}
	public String getGsttin() {
		return gsttin;
	}
	public void setGsttin(String gsttin) {
		this.gsttin = gsttin;
	}
	public String getBasetotal() {
		return basetotal;
	}
	public void setBasetotal(String basetotal) {
		this.basetotal = basetotal;
	}
	public int getIncnum() {
		return incnum;
	}
	public void setIncnum(int incnum) {
		this.incnum = incnum;
	}
	public String getStcode() {
		return stcode;
	}
	public void setStcode(String stcode) {
		this.stcode = stcode;
	}
	
	
	
	
	public String getDiscamt() {
		return discamt;
	}
	public void setDiscamt(String discamt) {
		this.discamt = discamt;
	}
	public String getSuppliergsttinno() {
		return suppliergsttinno;
	}
	public void setSuppliergsttinno(String suppliergsttinno) {
		this.suppliergsttinno = suppliergsttinno;
	}
	public PurchaseLedgerDto(int ledgerId, String dnNo, String debitAmount, String dnDoc, int totalReturnQty,
			String billNo, String challanNo, String purchaseAmount, String purchaseDate, int totalItemQty,
			String buyRate, String subTot, String lessUnitScheme, String discounts, String vat, String cst,
			String waybill, String lorryFreight, String itemId, int itemTot, int supplierId, String itemName,
			String[] itemNames, int[] itemIds, String totPurchaseAmount, String totDebitAmount,
			String totOutStandingAmount, String taxamount, String returnTotal, String returnquantity,
			String supplierName, String supplieraddress, String currentCompanyName, String itemSize,
			String titleforExcel, String itemTotDoubleFormate, String perboxqty, String boxqty, String pc,
			String paymentdate, String paymentamount, String dueamount, String paymentstatus, String paymentmode,
			String chequeno, String chequebankname, String chequedate, String totalamount, String subtotalbegoregst,
			String gstrate, String gstamt, String sgstamt, String sgstrate, String cgstamt, String cgstrate,
			String igstamt, String igstrate, String gsttin, String basetotal, int incnum, String stcode,
			String suppliergsttinno, String discamt) {
		super();
		this.ledgerId = ledgerId;
		this.dnNo = dnNo;
		this.debitAmount = debitAmount;
		this.dnDoc = dnDoc;
		this.totalReturnQty = totalReturnQty;
		this.billNo = billNo;
		this.challanNo = challanNo;
		this.purchaseAmount = purchaseAmount;
		this.purchaseDate = purchaseDate;
		this.totalItemQty = totalItemQty;
		this.buyRate = buyRate;
		this.subTot = subTot;
		this.lessUnitScheme = lessUnitScheme;
		this.discounts = discounts;
		this.vat = vat;
		this.cst = cst;
		this.waybill = waybill;
		this.lorryFreight = lorryFreight;
		this.itemId = itemId;
		this.itemTot = itemTot;
		this.supplierId = supplierId;
		this.itemName = itemName;
		this.itemNames = itemNames;
		this.itemIds = itemIds;
		this.totPurchaseAmount = totPurchaseAmount;
		this.totDebitAmount = totDebitAmount;
		this.totOutStandingAmount = totOutStandingAmount;
		this.taxamount = taxamount;
		this.returnTotal = returnTotal;
		this.returnquantity = returnquantity;
		this.supplierName = supplierName;
		this.supplieraddress = supplieraddress;
		this.currentCompanyName = currentCompanyName;
		this.itemSize = itemSize;
		this.titleforExcel = titleforExcel;
		this.itemTotDoubleFormate = itemTotDoubleFormate;
		this.perboxqty = perboxqty;
		this.boxqty = boxqty;
		this.pc = pc;
		this.paymentdate = paymentdate;
		this.paymentamount = paymentamount;
		this.dueamount = dueamount;
		this.paymentstatus = paymentstatus;
		this.paymentmode = paymentmode;
		this.chequeno = chequeno;
		this.chequebankname = chequebankname;
		this.chequedate = chequedate;
		this.totalamount = totalamount;
		this.subtotalbegoregst = subtotalbegoregst;
		this.gstrate = gstrate;
		this.gstamt = gstamt;
		this.sgstamt = sgstamt;
		this.sgstrate = sgstrate;
		this.cgstamt = cgstamt;
		this.cgstrate = cgstrate;
		this.igstamt = igstamt;
		this.igstrate = igstrate;
		this.gsttin = gsttin;
		this.basetotal = basetotal;
		this.incnum = incnum;
		this.stcode = stcode;
		this.suppliergsttinno = suppliergsttinno;
		this.discamt = discamt;
	}
	
	
	
	
}
