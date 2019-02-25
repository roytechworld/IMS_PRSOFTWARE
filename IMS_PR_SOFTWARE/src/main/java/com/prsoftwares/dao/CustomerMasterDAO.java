package com.prsoftwares.dao;

import java.sql.SQLException;
import java.util.List;

import com.prsoftwares.dto.CustomerMasterDTO;



public interface CustomerMasterDAO {
	public boolean saveCustomerDetails(CustomerMasterDTO cDTO)throws DaoException, SQLException;	
	public List<CustomerMasterDTO > ViewANDEditCALLCustomerDetails(int customerID) throws DaoException, SQLException;
	public boolean checkCustomerDuplicateORNOT(CustomerMasterDTO cDTO)throws DaoException, SQLException;
	boolean deleteCustomerById(int cid) throws DaoException, SQLException;
	public List<CustomerMasterDTO > getCustomerDetailsByCustBarcode(String  custBarcode) throws DaoException, SQLException;
	public int getCustomerId() throws DaoException,SQLException;
	public List<CustomerMasterDTO> getAllCustomerDetails() throws DaoException, SQLException;
	public List<CustomerMasterDTO> getAllCustomerDetailsByNames(String customername) throws DaoException, SQLException;
}
