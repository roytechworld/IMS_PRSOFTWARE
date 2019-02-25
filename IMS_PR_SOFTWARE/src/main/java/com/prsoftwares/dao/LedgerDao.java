package com.prsoftwares.dao;

import java.sql.SQLException;
import java.util.List;

import com.prsoftwares.dto.CustomerLedgerDto;
import com.prsoftwares.dto.PurchaseLedgerDto;


public interface LedgerDao {

	public List<PurchaseLedgerDto> getPLedgerDetailsBySuppplierId(int supplierId ,String mode,String fromdate ,String todate)  throws DaoException, SQLException;
	
	List<PurchaseLedgerDto> getLedgerByBillNoDnNo(String bill, String dn,int supplierId) throws DaoException, SQLException;
	//###############################################################     Print related Configuration queries -- start here ###################################################################	
		String printledgerDetailsByBillAndDnNo="SELECT  st.ldate, st.billno,st.itemId,i.itemname,st.qty,st.buyRate,st.itemTot,sto.taxamount ,sto.qty AS ReturnQuantity,sto.itemTot AS ReturnItemTot  FROM purchaseLedgermaster sto JOIN purchaseLedgermaster st  ON st.itemId=sto.itemId JOIN itemmaster i ON sto.itemId=i.itemId AND sto.dnNo=? AND sto.billNo=? AND sto.companyId=?  GROUP BY st.itemId ";
	//############################################################### Print related Configuration queries -- start here ###################################################################	
		public boolean uploadFileToWebServer(String localuploadFilePath);
		public List<PurchaseLedgerDto> printLedgerByBillNoDnNo(String bill, String dn) throws DaoException, SQLException ;
		public List<PurchaseLedgerDto> getTotalPurchaseLedger(String fromdate ,String todate,int supplierID)  throws DaoException, SQLException;
		public List<PurchaseLedgerDto> getTotalPurchaseLedgerByEachSupId(int SupplierId)  throws DaoException, SQLException;

		List<PurchaseLedgerDto> getAllBillNo(String fromdate, String todate,
				int supplierId) throws DaoException, SQLException;

		List<PurchaseLedgerDto> getpLedgerByModeAndSup(int supplierId,
				String mode) throws DaoException, SQLException;

		List<PurchaseLedgerDto> getPLedgerExportBySuppplierId(int supplierId,
				String mode, String fromdate, String todate)
				throws DaoException, SQLException;
		
		
		//------------------------------------------------- Customer Ledger  Interface start here -------------------------------------
		public List<CustomerLedgerDto> getSaleLedger(String fromdate ,String todate,int cID)  throws DaoException, SQLException;
		public List<CustomerLedgerDto> getBillNoDetailsByCustomerID(int cid,String fromdate ,String todate )throws DaoException ,SQLException;
		public List<CustomerLedgerDto> printCNLedgerByBillNoCnNo(String bill, String cn) throws DaoException, SQLException ;
		public List<CustomerLedgerDto> getItemDetailsForCustomerLedgerByBillNo(String billno, String fromdate,String todate)throws DaoException ,SQLException;
		public List<CustomerLedgerDto> getItemDetailsOFCNByBillNo(String billno)throws DaoException ,SQLException;
		public List<CustomerLedgerDto> getpaymentHistoryListByBillNo(String billno)throws DaoException ,SQLException;
		public List<CustomerLedgerDto> getpaymentHistoryListByBillNoByFromDateTodate(String billno,String fromdate, String todate)throws DaoException ,SQLException;
		public List<CustomerLedgerDto> getItemDetailsOFCNByBillNoWithFromDateTodate(String billno,String fromdate, String todate)throws DaoException ,SQLException;
		public double getMinDueAmountBybillNoAndFROMDATETODATE(String billno,String fromdate, String todate)throws DaoException ,SQLException;
		//------------------------------------------------- Customer Ledger  Interface end here ---------------------------------------
		
		//------------------------------------------------- Purchase Ledger  Interface start here -------------------------------------
		public List<PurchaseLedgerDto> getBillNoDetailsBySupplierIDPURCHASE(int cid,String fromdate,String todate)throws DaoException ,SQLException;
		public List<PurchaseLedgerDto> getItemDetailsForPurchaseLedgerByBillNoPURCHASE(String billno)throws DaoException ,SQLException;
		public List<CustomerLedgerDto> getpaymentHistoryListByBillNoPURCHASE(String billno)throws DaoException ,SQLException;
		public List<PurchaseLedgerDto> getItemDetailsOFDNByBillNoPURCHASE(String billno)throws DaoException ,SQLException;
		public List<PurchaseLedgerDto> getPurchaseLedger(String fromdate ,String todate,int supplierID)  throws DaoException, SQLException;
		public List<PurchaseLedgerDto> getSpaymentHistoryListByBillNo(String billno)throws DaoException ,SQLException;
		public List<PurchaseLedgerDto> printDNLedgerByBillNoDnNo(String bill, String dn)throws DaoException, SQLException;
		public List<PurchaseLedgerDto> getSpaymentHistoryListByBillNoBYFROMDATETODATE(String billno,String fromdate,String todate)throws DaoException ,SQLException;
		public double getMinDueAmountByBillnoFromdateTotDate(String billno,String fromdate,String todate)throws DaoException ,SQLException;
		public List<PurchaseLedgerDto> getItemDetailsOFDNByBillNoPURCHASEByFromDateTodate(String billno,String fromdate,String todate)throws DaoException ,SQLException;
		public double getMaxDueAmountByBillnoFromdateTotDate(String billno,String fromdate,String todate)throws DaoException ,SQLException;
		
		//------------------------------------------------- Purchase Ledger  Interface Ends here -------------------------------------	
		
		public List<PurchaseLedgerDto> getSpaymentRecord(String supplierid)throws DaoException ,SQLException;
		
		
		
		
		
		
		
}
