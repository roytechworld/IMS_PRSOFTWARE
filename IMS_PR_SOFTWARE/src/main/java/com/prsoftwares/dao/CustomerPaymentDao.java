package com.prsoftwares.dao;

import java.util.List;

import com.prsoftwares.dto.SalesMasterDto;
import com.prsoftwares.dto.SmsMaster;
import com.prsoftwares.util.AllBankNames;



public interface CustomerPaymentDao {

	public List<SalesMasterDto> getAllUnpaidBillofSales() throws DaoException, Exception;
	public List<SalesMasterDto> getPaymentHistoryByBillNo(String billNo) throws DaoException, Exception;
	public List<SalesMasterDto> getPaymentInfoByCustomerId(String customerId) throws DaoException, Exception;
	boolean saveCustomerPayment(SalesMasterDto sdto) throws DaoException, Exception;
	public List<SalesMasterDto> getAllCustomerPaymentDueInfo() throws DaoException,Exception ;
	public List<SalesMasterDto> getPaymentDueInfoByCustomerId(String customerId) throws DaoException, Exception;
	public List<SalesMasterDto> getCustomerWithDuePay() throws DaoException, Exception;
	public List<AllBankNames> getAllBankList() throws DaoException;
	public boolean saveCustomerPaymentforSales(SalesMasterDto sdto) throws DaoException, Exception;
	public List<SalesMasterDto> getPaymentBillNoCustomerId(String customerId)  throws DaoException, Exception;
    public List<SalesMasterDto>getAllCustomernamesByCustomerNameWithDistinct(String customerName) throws DaoException,Exception;
    public List<SmsMaster> getSmsDetails() throws DaoException, Exception ;
    public boolean updateSmsMaster(SmsMaster s)throws DaoException;
}
