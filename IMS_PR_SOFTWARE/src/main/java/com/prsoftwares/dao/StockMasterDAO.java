package com.prsoftwares.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.prsoftwares.dto.OpenigAndClosingStockDTO;
import com.prsoftwares.dto.PurchaseLedgerDto;
import com.prsoftwares.dto.StockMasterDTO;
import com.prsoftwares.dto.SupplierMasterDTO;



public interface StockMasterDAO {
	

	public boolean saveStockDetails(List<StockMasterDTO> sDTOList)throws DaoException,SQLException;
	public int getMaxStockId()throws DaoException,SQLException;
	public List<StockMasterDTO> getAllStockDetails() throws DaoException, SQLException;
	public List<StockMasterDTO> getAllStockDetailsByBillNo(String billno) throws DaoException, SQLException ;
	public List<StockMasterDTO> getAllItemValuesGroupByItemIds() throws DaoException, SQLException;
	public List<StockMasterDTO> getTotalItemStock(String itemId) throws DaoException, SQLException;
	public List<StockMasterDTO> getAllStockDetailsByBillNoANDSupplierID(String billno,String supplierId) throws DaoException, SQLException ;
	public List<StockMasterDTO> getAllPurchaseDetails() throws DaoException, SQLException ;
	
	//####################################################### All joining queries for stock related process -- start here ###############################################
	public static String getAllStockDetailsQueryByCompanyID="SELECT st.stockid, i.itemId,i.itemname,s.SUPPLIERNAME,	st.buyRate,st.qty,st.itemTotal,st.purchaseDate,	st.billNo,st.companyId,u.userId,u.userName FROM itemmaster i, stockmaster st, suppliermaster s, usermaster u WHERE i.itemId=st.itemIds AND s.SupplierId=st.supplierId AND u.userID=st.userId AND st.companyId=?";
	public static String saveStockDetailsQuery="insert into stockmaster(billNo,challanNo,supplierId,purchaseDate,itemIds,itemSize,qty,buyRate,itemTotal,companyId,userId)values(?,?,?,?,?,?,?,?,?,?,?)";
	/*public static String queryForDisplayingStockItems="SELECT DISTINCT i.itemId,i.itemname,st.buyRate,st.qty,st.itemSize,st.itemTotal,st.purchaseDate,st.billNo,st.stockid,st.userId,u.userName,pL.vat,pl.cst, pl.waybill FROM itemmaster i, stockmaster st, usermaster u, purchaseledgermaster pL WHERE  i.itemId=st.itemIds AND u.userId=st.userId AND pL.billNo=st.billNo AND pl.itemId=st.itemIds AND st.billNo=? AND st.companyId=? AND st.supplierId=?";*/
	public static String queryForDisplayingStockItems= "SELECT DISTINCT i.itemId,i.itemname,pl.buyRate,pl.qty,pl.itemSize,pl.itemTot,pl.ldate,pl.billNo,pl.userId,u.userName,pL.vat,pl.cst, pl.waybill FROM itemmaster i,  usermaster u, purchaseledgermaster pL WHERE  i.itemId=pl.itemId AND u.userId=pl.userId   AND pl.billNo=? AND pl.companyId=? AND pl.supplierId=? AND pl.dnNo='NA'";
	//SELECT DISTINCT  i.itemId,i.itemname,st.buyRate,st.qty,st.itemTotal,st.purchaseDate,st.billNo,st.stockid,st.userId,u.userName,pL.vat FROM itemmaster i, stockmaster st, usermaster u, purchaseledgermaster pL WHERE  i.itemId=st.itemIds AND u.userId=st.userId AND pL.billNo=st.billNo AND pl.itemId=st.itemIds AND st.billNo='b001' AND st.companyId=1//
	
	/*public static String getDebitNoteDetailsBySupplier="SELECT i.itemName,st.stockid,st.itemIds,st.buyRate,st.purchaseDate,st.billNo, st.lessUnitScheme,st.discounts,st.vat,st.lorryFreight,dn.dnId, dn.dnNo,dn.returnItemId,dn.returnItemQty,dn.dnDoc,dn.returnItemTotal, dn.dnSunTot,dn.dnGrandTot,dn.cnNo,st.userId, u.userName FROM itemmaster i, stockmaster st, debitNote dn, usermaster u WHERE i.itemId=st.itemIds AND u.userID=st.userId AND st.billNo=dn.billNo AND st.itemIds=dn.returnItemId AND dn.supplierId=?  AND dn.companyId=?";*/
	/*public static String getDebitNoteDetailsBySupplier="SELECT dn.returnItemId,dn.returnItemQty,dn.buyRate,dn.returnItemTotal,dn.cnNo, i.itemname FROM debitnote dn, itemmaster i WHERE i.itemId=dn.returnItemId AND dn.dnNo='DN001' AND dn.companyId=1";*/
	public static String getDebitNoteListBySupplier="SELECT DISTINCT dn.dnNo, dn.dnDoc, dn.billNo,dn.supplierId, pl.vat, pl.cst, pl.waybill FROM debitnote dn, purchaseledgermaster pl  WHERE dn.billNo=pl.billNo AND dn.dnNo=pl.dnNo AND pl.ItemId=dn.returnItemId AND dn.supplierId=pl.supplierId AND dn.companyId=pl.companyId AND dn.supplierId=? AND dn.companyId=?";
	//####################################################### All joining queries for stock related process -- End here ###############################################
	
	//################################################### Calculation For Debit Note Functions: Start #############################################################################
	
	public List<StockMasterDTO> getDNListBySuppplierId(int suplierId) throws DaoException, Exception;
	public List<StockMasterDTO> getDNDetailsBySuppplierId(int suplierId, String dn) throws DaoException, Exception;
	public List<StockMasterDTO> getStockDetailsBySuppplierId(String billno ,String supplierId) throws DaoException, Exception;
	public List<StockMasterDTO> getBillNoBySuppplierId(int supplierId) throws DaoException, Exception ;
	public boolean insertDebitNoteTableAndUpdateStockMaster(String[] uqty, String[] returnQuantityValue, String[] updateItemtot,String[] itemids,String[] itemSize, String returnSubtotal ,String returnGrandTot, String[] stockid, String[] newReturnTotal, StockMasterDTO sdto ,String[] buyRate) throws DaoException, Exception;
	public String debitNoGenerateEngine(String cid)  throws DaoException, Exception ;
	public String lastDebitNoSaved(String companyId, String UserId) throws DaoException, Exception;
	//################################################### Calculation For Debit Note Functions: End #############################################################################
	public List<StockMasterDTO> stockDetailsByDate(String fromdate, String todate,String cstvat) throws DaoException, SQLException;
	List<StockMasterDTO> getAllBillDetails() throws DaoException, SQLException;
	public boolean editInvoiceNo(String Oldinvoiceno,String NewInvoiceNo) throws DaoException, SQLException ;
	List<SupplierMasterDTO> getAllSupplierFromStock() throws DaoException, SQLException;
	public List<StockMasterDTO> stockDetailsBySupId(String supplierId, String cstvat) throws DaoException, SQLException;
	public List<StockMasterDTO> stockDetailsByDateAndSupId(String supplierId,String fromdate, String todate,String cstvat) throws DaoException, SQLException ;
	List<PurchaseLedgerDto> exportToexcelPurchaseDetails(String fromdate, String todate, String supplierId,String vatcst) throws DaoException, SQLException;
	
   //###########################################################################################################################################################################	
   //-------------------------------------------------------- Opening Stock and Closing Stock details --------------------------------------------------------------------------
	public List<OpenigAndClosingStockDTO> getAllOpeningStock(String financialYear)throws DaoException, SQLException;
	public boolean saveOpeningClosingStock(OpenigAndClosingStockDTO dto,String fromdateyear,String todateyear)throws DaoException ,SQLException;
	public List<StockMasterDTO>getAllStockDetailsforclosingStockMaster() throws DaoException,SQLException;
	public boolean updateClosingOpeningAndStockMaster(String[] financialyear,String[] id,String[] stockid,String[] itemids,String[] qty, String[] alterQty ,String[] remarks) throws DaoException, SQLException;
	public List<OpenigAndClosingStockDTO> getAllCurrentYearOpeningStock(String financialYear)throws DaoException, SQLException;
	public boolean truncateopeningclosingstockmaster()throws DaoException,SQLException;
	public List<StockMasterDTO> getTotalItemStock(String itemId ,Connection con) throws DaoException,SQLException;
	
	
	

}