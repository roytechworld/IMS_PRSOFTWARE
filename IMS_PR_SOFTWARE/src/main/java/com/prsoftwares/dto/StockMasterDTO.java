package com.prsoftwares.dto;

public class StockMasterDTO {
	
	private String stockid	;
	private String billNo;
	private String challanNo;
	private int supplierId;
	private String purchaseDate;
	private String invoiceNo;
	private String itemIds;
	private int itemSizeId;
	private String itemSizeName;
	private String qty;
	private String buyRate;
	private String itemTotal;
	private String subTot;
	private String lessUnitScheme;
	private String discounts;
	private String vat;
	private String cst;
	private String waybill;
	private String lorryFreight;
	private String grandTot;
	private String itemname;
	private String suppliername;
	private String username;
	private String companyname;
	private String userId;
	private String companyId;
	
	//------ Debit Note Variable------------
	private int dnId;
	private String dnNo;
	private int returnItemQty;
	private String returnItemId,dnDoc;
	private String returnItemTotal,dnSunTot,dnGrandTot;
	private String cnNo;
	private int listSize;
	private String returnsubTot;
	private String taxamount;
	private String itemSize;
	
	
	//---------- Payment Variable-------------------------
	
	private String sPaymentId;
	private String sPaidStatus;
	private String sPaidAmt;
	private String dueAmt;
	private String sPayMode;
	private String sPayDate;
	private String chequeNo;
	private String chequeDate;
	private String chequeBank;
	public int dateDiff;
	public String totalPaid;
	public String totaldue;
	public String minDuePayment;
	
	public String gstrate;
	public String gstamt;
	
	public String sgstamt;
	public String sgstrate;
	
	public String cgstamt;
	public String cgstrate;
	
	public String igstamt;
	public String igstrate;
	
	public String discountamt;
	public String totalitempricesold;
	public String totalqtysold;
	public String totaldiscountgiven;
	public String finalitemsold;
	public String grandfinalItemSold;
	
	public synchronized String getStockid() {
		return stockid;
	}
	public synchronized void setStockid(String stockid) {
		this.stockid = stockid;
	}
	public synchronized String getBillNo() {
		return billNo;
	}
	public synchronized void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public synchronized String getChallanNo() {
		return challanNo;
	}
	public synchronized void setChallanNo(String challanNo) {
		this.challanNo = challanNo;
	}
	public synchronized int getSupplierId() {
		return supplierId;
	}
	public synchronized void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public synchronized String getPurchaseDate() {
		return purchaseDate;
	}
	public synchronized void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public synchronized String getInvoiceNo() {
		return invoiceNo;
	}
	public synchronized void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public synchronized String getItemIds() {
		return itemIds;
	}
	public synchronized void setItemIds(String itemIds) {
		this.itemIds = itemIds;
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
	public synchronized String getQty() {
		return qty;
	}
	public synchronized void setQty(String qty) {
		this.qty = qty;
	}
	public synchronized String getBuyRate() {
		return buyRate;
	}
	public synchronized void setBuyRate(String buyRate) {
		this.buyRate = buyRate;
	}
	public synchronized String getItemTotal() {
		return itemTotal;
	}
	public synchronized void setItemTotal(String itemTotal) {
		this.itemTotal = itemTotal;
	}
	public synchronized String getSubTot() {
		return subTot;
	}
	public synchronized void setSubTot(String subTot) {
		this.subTot = subTot;
	}
	public synchronized String getLessUnitScheme() {
		return lessUnitScheme;
	}
	public synchronized void setLessUnitScheme(String lessUnitScheme) {
		this.lessUnitScheme = lessUnitScheme;
	}
	public synchronized String getDiscounts() {
		return discounts;
	}
	public synchronized void setDiscounts(String discounts) {
		this.discounts = discounts;
	}
	public synchronized String getVat() {
		return vat;
	}
	public synchronized void setVat(String vat) {
		this.vat = vat;
	}
	public synchronized String getCst() {
		return cst;
	}
	public synchronized void setCst(String cst) {
		this.cst = cst;
	}
	public synchronized String getWaybill() {
		return waybill;
	}
	public synchronized void setWaybill(String waybill) {
		this.waybill = waybill;
	}
	public synchronized String getLorryFreight() {
		return lorryFreight;
	}
	public synchronized void setLorryFreight(String lorryFreight) {
		this.lorryFreight = lorryFreight;
	}
	public synchronized String getGrandTot() {
		return grandTot;
	}
	public synchronized void setGrandTot(String grandTot) {
		this.grandTot = grandTot;
	}
	public synchronized String getItemname() {
		return itemname;
	}
	public synchronized void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public synchronized String getSuppliername() {
		return suppliername;
	}
	public synchronized void setSuppliername(String suppliername) {
		this.suppliername = suppliername;
	}
	public synchronized String getUsername() {
		return username;
	}
	public synchronized void setUsername(String username) {
		this.username = username;
	}
	public synchronized String getCompanyname() {
		return companyname;
	}
	public synchronized void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public synchronized String getUserId() {
		return userId;
	}
	public synchronized void setUserId(String userId) {
		this.userId = userId;
	}
	public synchronized String getCompanyId() {
		return companyId;
	}
	public synchronized void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public synchronized int getDnId() {
		return dnId;
	}
	public synchronized void setDnId(int dnId) {
		this.dnId = dnId;
	}
	public synchronized String getDnNo() {
		return dnNo;
	}
	public synchronized void setDnNo(String dnNo) {
		this.dnNo = dnNo;
	}
	public synchronized int getReturnItemQty() {
		return returnItemQty;
	}
	public synchronized void setReturnItemQty(int returnItemQty) {
		this.returnItemQty = returnItemQty;
	}
	public synchronized String getReturnItemId() {
		return returnItemId;
	}
	public synchronized void setReturnItemId(String returnItemId) {
		this.returnItemId = returnItemId;
	}
	public synchronized String getDnDoc() {
		return dnDoc;
	}
	public synchronized void setDnDoc(String dnDoc) {
		this.dnDoc = dnDoc;
	}
	public synchronized String getReturnItemTotal() {
		return returnItemTotal;
	}
	public synchronized void setReturnItemTotal(String returnItemTotal) {
		this.returnItemTotal = returnItemTotal;
	}
	public synchronized String getDnSunTot() {
		return dnSunTot;
	}
	public synchronized void setDnSunTot(String dnSunTot) {
		this.dnSunTot = dnSunTot;
	}
	public synchronized String getDnGrandTot() {
		return dnGrandTot;
	}
	public synchronized void setDnGrandTot(String dnGrandTot) {
		this.dnGrandTot = dnGrandTot;
	}
	public synchronized String getCnNo() {
		return cnNo;
	}
	public synchronized void setCnNo(String cnNo) {
		this.cnNo = cnNo;
	}
	public synchronized int getListSize() {
		return listSize;
	}
	public synchronized void setListSize(int listSize) {
		this.listSize = listSize;
	}
	public synchronized String getReturnsubTot() {
		return returnsubTot;
	}
	public synchronized void setReturnsubTot(String returnsubTot) {
		this.returnsubTot = returnsubTot;
	}
	public synchronized String getTaxamount() {
		return taxamount;
	}
	public synchronized void setTaxamount(String taxamount) {
		this.taxamount = taxamount;
	}
	public synchronized String getItemSize() {
		return itemSize;
	}
	public synchronized void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}
	public synchronized String getsPaymentId() {
		return sPaymentId;
	}
	public synchronized void setsPaymentId(String sPaymentId) {
		this.sPaymentId = sPaymentId;
	}
	public synchronized String getsPaidStatus() {
		return sPaidStatus;
	}
	public synchronized void setsPaidStatus(String sPaidStatus) {
		this.sPaidStatus = sPaidStatus;
	}
	public synchronized String getsPaidAmt() {
		return sPaidAmt;
	}
	public synchronized void setsPaidAmt(String sPaidAmt) {
		this.sPaidAmt = sPaidAmt;
	}
	public synchronized String getDueAmt() {
		return dueAmt;
	}
	public synchronized void setDueAmt(String dueAmt) {
		this.dueAmt = dueAmt;
	}
	public synchronized String getsPayMode() {
		return sPayMode;
	}
	public synchronized void setsPayMode(String sPayMode) {
		this.sPayMode = sPayMode;
	}
	public synchronized String getsPayDate() {
		return sPayDate;
	}
	public synchronized void setsPayDate(String sPayDate) {
		this.sPayDate = sPayDate;
	}
	public synchronized String getChequeNo() {
		return chequeNo;
	}
	public synchronized void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public synchronized String getChequeDate() {
		return chequeDate;
	}
	public synchronized void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}
	public synchronized String getChequeBank() {
		return chequeBank;
	}
	public synchronized void setChequeBank(String chequeBank) {
		this.chequeBank = chequeBank;
	}
	public synchronized int getDateDiff() {
		return dateDiff;
	}
	public synchronized void setDateDiff(int dateDiff) {
		this.dateDiff = dateDiff;
	}
	public synchronized String getTotalPaid() {
		return totalPaid;
	}
	public synchronized void setTotalPaid(String totalPaid) {
		this.totalPaid = totalPaid;
	}
	public synchronized String getTotaldue() {
		return totaldue;
	}
	public synchronized void setTotaldue(String totaldue) {
		this.totaldue = totaldue;
	}
	public synchronized String getMinDuePayment() {
		return minDuePayment;
	}
	public synchronized void setMinDuePayment(String minDuePayment) {
		this.minDuePayment = minDuePayment;
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
	
	
	
	
	public String getTotalitempricesold() {
		return totalitempricesold;
	}
	public void setTotalitempricesold(String totalitempricesold) {
		this.totalitempricesold = totalitempricesold;
	}
	public String getTotalqtysold() {
		return totalqtysold;
	}
	public void setTotalqtysold(String totalqtysold) {
		this.totalqtysold = totalqtysold;
	}
	public String getTotaldiscountgiven() {
		return totaldiscountgiven;
	}
	public void setTotaldiscountgiven(String totaldiscountgiven) {
		this.totaldiscountgiven = totaldiscountgiven;
	}
	public String getFinalitemsold() {
		return finalitemsold;
	}
	public void setFinalitemsold(String finalitemsold) {
		this.finalitemsold = finalitemsold;
	}
	public String getGrandfinalItemSold() {
		return grandfinalItemSold;
	}
	public void setGrandfinalItemSold(String grandfinalItemSold) {
		this.grandfinalItemSold = grandfinalItemSold;
	}
	public String getDiscountamt() {
		return discountamt;
	}
	public void setDiscountamt(String discountamt) {
		this.discountamt = discountamt;
	}
	
	
	public StockMasterDTO(String stockid, String billNo, String challanNo, int supplierId, String purchaseDate,
			String invoiceNo, String itemIds, int itemSizeId, String itemSizeName, String qty, String buyRate,
			String itemTotal, String subTot, String lessUnitScheme, String discounts, String vat, String cst,
			String waybill, String lorryFreight, String grandTot, String itemname, String suppliername, String username,
			String companyname, String userId, String companyId, int dnId, String dnNo, int returnItemQty,
			String returnItemId, String dnDoc, String returnItemTotal, String dnSunTot, String dnGrandTot, String cnNo,
			int listSize, String returnsubTot, String taxamount, String itemSize, String sPaymentId, String sPaidStatus,
			String sPaidAmt, String dueAmt, String sPayMode, String sPayDate, String chequeNo, String chequeDate,
			String chequeBank, int dateDiff, String totalPaid, String totaldue, String minDuePayment, String gstrate,
			String gstamt, String sgstamt, String sgstrate, String cgstamt, String cgstrate, String igstamt,
			String igstrate, String discountamt, String totalitempricesold, String totalqtysold,
			String totaldiscountgiven, String finalitemsold, String grandfinalItemSold) {
		super();
		this.stockid = stockid;
		this.billNo = billNo;
		this.challanNo = challanNo;
		this.supplierId = supplierId;
		this.purchaseDate = purchaseDate;
		this.invoiceNo = invoiceNo;
		this.itemIds = itemIds;
		this.itemSizeId = itemSizeId;
		this.itemSizeName = itemSizeName;
		this.qty = qty;
		this.buyRate = buyRate;
		this.itemTotal = itemTotal;
		this.subTot = subTot;
		this.lessUnitScheme = lessUnitScheme;
		this.discounts = discounts;
		this.vat = vat;
		this.cst = cst;
		this.waybill = waybill;
		this.lorryFreight = lorryFreight;
		this.grandTot = grandTot;
		this.itemname = itemname;
		this.suppliername = suppliername;
		this.username = username;
		this.companyname = companyname;
		this.userId = userId;
		this.companyId = companyId;
		this.dnId = dnId;
		this.dnNo = dnNo;
		this.returnItemQty = returnItemQty;
		this.returnItemId = returnItemId;
		this.dnDoc = dnDoc;
		this.returnItemTotal = returnItemTotal;
		this.dnSunTot = dnSunTot;
		this.dnGrandTot = dnGrandTot;
		this.cnNo = cnNo;
		this.listSize = listSize;
		this.returnsubTot = returnsubTot;
		this.taxamount = taxamount;
		this.itemSize = itemSize;
		this.sPaymentId = sPaymentId;
		this.sPaidStatus = sPaidStatus;
		this.sPaidAmt = sPaidAmt;
		this.dueAmt = dueAmt;
		this.sPayMode = sPayMode;
		this.sPayDate = sPayDate;
		this.chequeNo = chequeNo;
		this.chequeDate = chequeDate;
		this.chequeBank = chequeBank;
		this.dateDiff = dateDiff;
		this.totalPaid = totalPaid;
		this.totaldue = totaldue;
		this.minDuePayment = minDuePayment;
		this.gstrate = gstrate;
		this.gstamt = gstamt;
		this.sgstamt = sgstamt;
		this.sgstrate = sgstrate;
		this.cgstamt = cgstamt;
		this.cgstrate = cgstrate;
		this.igstamt = igstamt;
		this.igstrate = igstrate;
		this.discountamt = discountamt;
		this.totalitempricesold = totalitempricesold;
		this.totalqtysold = totalqtysold;
		this.totaldiscountgiven = totaldiscountgiven;
		this.finalitemsold = finalitemsold;
		this.grandfinalItemSold = grandfinalItemSold;
	}
	public StockMasterDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
