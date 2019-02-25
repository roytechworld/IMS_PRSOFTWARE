package com.prsoftwares.dao;

import java.util.List;

import com.prsoftwares.dto.StockMasterDTO;
import com.prsoftwares.util.AllBankNames;



public interface SupplierPaymentDao {

	/*public List<SalesMasterDto> getAllUnpaidBillofSales() throws DaoException, Exception;
	
	public List<SalesMasterDto> getPaymentInfoByCustomerId(String customerId) throws DaoException, Exception;*/
	boolean saveSupplierPayment(StockMasterDTO sdto) throws DaoException, Exception;
	/*public List<SalesMasterDto> getAllCustomerPaymentDueInfo() throws DaoException,Exception ;
	public List<SalesMasterDto> getPaymentDueInfoByCustomerId(String customerId) throws DaoException, Exception;*/
	public List<StockMasterDTO> getSupplierPaymentHistoryByBillNo(String billNo, String supplierId) throws DaoException, Exception;
	public List<StockMasterDTO> getSupplierWithDuePay() throws DaoException, Exception;
	public List<AllBankNames> getAllBankList() throws DaoException;
	public List<StockMasterDTO> getPaymentBillNoSupplierId(String supplierId)  throws DaoException, Exception;
	public StockMasterDTO getMinDueAmt(String string) throws DaoException, Exception;
	boolean saveSupplierPayment2(StockMasterDTO sdto) throws DaoException,
			Exception;
	public boolean saveSupplierPaymentfromStockSave(StockMasterDTO sdto) throws DaoException, Exception;

}
