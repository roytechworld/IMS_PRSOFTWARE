package com.prsoftwares.dao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.prsoftwares.dto.CreditNoteDto;
import com.prsoftwares.dto.CustomerMasterDTO;
import com.prsoftwares.dto.ItemCategoryTypeMasterDTO;
import com.prsoftwares.dto.SalesMasterDto;
import com.prsoftwares.util.EligibleCustomerNames;


public interface  SalesMasterDao {
	
	public List<CustomerMasterDTO>  searchCustomerList(String custname) throws DaoException,SQLException;
	public List<SalesMasterDto> getStockItemDetailsByItemCode(String articlesCode)  throws DaoException,SQLException;
	public SalesMasterDto getStockItemDetailsByStockId(String stockId)  throws DaoException,SQLException;

	public List<SalesMasterDto> getAllSalesDetails()  throws DaoException,SQLException;
	List<SalesMasterDto> getAllSalesDetailsByBillNo(String billno,String doc) throws DaoException, SQLException;
	public int getSizeofTotalSales() throws DaoException, SQLException;
	public List<SalesMasterDto> getAllSalesDetailsByMAXBILLNO(String billno) throws DaoException, SQLException ;
	List<SalesMasterDto> getBillNoByCustomerId(String customerId) throws DaoException, Exception;
	List<SalesMasterDto> getSalesDetailsByBillNo(String billno) throws DaoException, Exception;
	
	//######################################### Check Eligible Customer functions start here ######################################################
	public List<EligibleCustomerNames> calculateEligiblityWithSchemeQuantity(String customername,String schemename ,String startdate,String endDate) throws DaoException, SQLException ;
	public List<EligibleCustomerNames> retriveEligibleCustomer(String schemeName)throws DaoException, SQLException ;
    //######################################### Check Eligible Customer functions End here ######################################################
	public List<CustomerMasterDTO> getAllCustomerFromSales() throws DaoException,SQLException;
	public List<SalesMasterDto> salesDetailsByDate(String fromdate, String todate,String cnstatus) throws DaoException, SQLException;
	public List<SalesMasterDto> salesDetailsByCusId(String customerId,String cnstatus) throws DaoException, SQLException;
	public List<SalesMasterDto> salesDetailsByDateAndCusId(String customerId,String fromdate, String todate) throws DaoException, SQLException;
	public List<SalesMasterDto> exportToexcelSaleDetails(String fromdate, String todate,String customerId,String cstvat,String cnornocn) throws DaoException, SQLException ;
	public List<SalesMasterDto> getTempCustomerDetails(String tempCustomerBarcode) throws DaoException;
	public double getTotalSellDetailsOfPerticularCustomer(String custBarcode) throws DaoException;
	public List<SalesMasterDto> gettmpCusBuyDetailsByBarcode(String tempCustBarcode) throws DaoException;
	public int getMaxSaleID() throws DaoException;
	public int getMaxSaleBillNo() throws DaoException;
	
	
	//################################################# Credit note ###################################################
	List<CustomerMasterDTO> getTempCustomerListFromSales() throws DaoException;
	List<ItemCategoryTypeMasterDTO> getSoldItemList() throws DaoException;
	List<SalesMasterDto> getCusBuyDetailsByBarcode(String CustBarcode, String billNo)
			throws DaoException;
	public boolean addCreditNote(CreditNoteDto crDto)throws DaoException;
	int creditNoteIdentityGenerateEngine(String companyId) throws SQLException;
	String creditNoGenerateEngine(String companyId) throws SQLException;
	public List<CustomerMasterDTO> getTempCustomerListFromSalesByCustBarcode(String custBarcode) throws DaoException;
	public List<CustomerMasterDTO> getTempCustomerListFromSalesByProductcode(String productCode) throws DaoException;
	public List<SalesMasterDto> getSaleDetailsByProductCode(String productCode) throws DaoException;
	List<CreditNoteDto> getAllCreditNoteNoByCustomerCode(String cusBarcode)	throws SQLException;
	boolean saveReplaceProduct(SalesMasterDto sdto, String[] size,
			String[] qty, String[] srate, String[] tot, String[] itemId,
			String[] brate, String cusBarcode) throws DaoException,
			SQLException;
	public List<CreditNoteDto> getcreditNoteDetailsByCustomerID(String customerId) throws DaoException, Exception ;
	public List<SalesMasterDto> getBillNoByCustomerIdANDCustomerName(String customerId,String CustName) throws DaoException, Exception ;
	public List<SalesMasterDto> getBillNoByCustomerIdsOnly(String customerId) throws DaoException, Exception ;
	public List<CreditNoteDto> getcreditNoteDetailsBybillNo(String billno) throws DaoException, Exception ;
	public boolean addCreditNotes(List<CreditNoteDto> crDto) throws DaoException;
	public CreditNoteDto getCreditnoteDetailsOnerecord(String Billno,Connection con)throws DaoException, Exception ;
	
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ BAR CHART FOR  MONTHLY SALE DAO METHODS START HERE $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
	
			public double getMonthlySaleReportOFAPR()throws DaoException ,SQLException;
			public double getMonthlySaleReportOFMAY()throws DaoException ,SQLException;
			public double getMonthlySaleReportOFJUN()throws DaoException ,SQLException;
			public double getMonthlySaleReportOFJUL()throws DaoException ,SQLException;
			public double getMonthlySaleReportOFAUG()throws DaoException ,SQLException;
			public double getMonthlySaleReportOFSEP()throws DaoException ,SQLException;
			public double getMonthlySaleReportOFOCT()throws DaoException ,SQLException;
			public double getMonthlySaleReportOFNOV()throws DaoException ,SQLException;
			public double getMonthlySaleReportOFDEC()throws DaoException ,SQLException;
			public double getMonthlySaleReportOFJAN()throws DaoException ,SQLException;
			public double getMonthlySaleReportOFFEB()throws DaoException ,SQLException;
			public double getMonthlySaleReportOFMAR()throws DaoException ,SQLException;
			
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ BAR CHART FOR  MONTHLY SALE DAO METHODS ENDS HERE $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
			public boolean checkCNorNotForBillNofrmandtodate(String billno,Connection con,String fromdate,String todate)throws DaoException ,SQLException;
			public boolean saveSaleDetails(SalesMasterDto sdto, String[] size, String[] qty, String[] srate, String[] tot,
					String[] itemId, String[] brate, String afterdiscount, String amountreceived, String amountreturn,
					String[] hsncode, String[] discount1, String[] discount2, String[] gstrate, String[] gstamt,
					String[] cgstrate, String[] cgstamt, String[] sgstrate, String[] sgstamt, String[] igstrate,
					String[] igstamt,String[] discountamt,String[]basetotal) throws DaoException, SQLException;
			
			public List<SalesMasterDto> getAllSalesDetailsByBillNoOnly(String billno) throws DaoException, SQLException;
			public List<SalesMasterDto> exportToexcelSaleDetailsgstr(String fromdate, String todate,String customerId,String cstvat, String CNorNoCN ) throws DaoException, SQLException ;
			
	
}
