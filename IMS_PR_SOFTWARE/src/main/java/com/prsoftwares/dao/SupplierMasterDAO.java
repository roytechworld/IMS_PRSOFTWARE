package com.prsoftwares.dao;

import java.sql.SQLException;
import java.util.List;

import com.prsoftwares.dto.SupplierMasterDTO;


public interface SupplierMasterDAO {
	
	public boolean saveSupplierDetails(SupplierMasterDTO sDTO) throws DaoException,SQLException;
	public List<SupplierMasterDTO> getAllSupplierDetails() throws DaoException, SQLException ;
	public SupplierMasterDTO getSupplierDetailsById(int id) throws DaoException,SQLException;
	public boolean UpdateSupplierDetails(SupplierMasterDTO sDTO) throws DaoException,SQLException;
	boolean checkSupplierDuplicateORNOT(SupplierMasterDTO sDTO) throws DaoException, SQLException;
	public List<SupplierMasterDTO> getAllSupplierDetailsBySUPNAME(String supname) throws DaoException, SQLException;
	boolean deleteSupplierById(int cid) throws DaoException, SQLException;
	
}
