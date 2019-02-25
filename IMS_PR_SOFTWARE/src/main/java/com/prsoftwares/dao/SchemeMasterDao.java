package com.prsoftwares.dao;

import java.sql.SQLException;
import java.util.List;

import com.prsoftwares.dto.CustomerMasterDTO;
import com.prsoftwares.dto.ItemCategoryTypeMasterDTO;
import com.prsoftwares.dto.SchemeMasterDto;



public interface SchemeMasterDao {

	boolean checkschdaoSchemeDuplicateORNOT(SchemeMasterDto schemeDto) throws DaoException, SQLException;
	public Boolean saveAndEditScheme(SchemeMasterDto schemeDto,String[] itemIds, String[] customerIds,int itemSize,int customerSize) throws DaoException, SQLException;
	List<SchemeMasterDto> getAllSchemes() throws DaoException, SQLException;
	List<SchemeMasterDto> getSchemesByDate(SchemeMasterDto sdto) throws DaoException, SQLException;
	public List<ItemCategoryTypeMasterDTO> getItemNamesBySchemeName(String schemename) throws DaoException, SQLException ;
	public List<SchemeMasterDto> getAllSchemesBYFROMAndTODATE(String fromDate, String toDate) throws DaoException, SQLException;
	public List<CustomerMasterDTO> getCustomersNamesBySchemeName(String schemename) throws DaoException, SQLException;
	public boolean deleteScheme(String scheme)throws DaoException, SQLException;
	public List<ItemCategoryTypeMasterDTO> searchItemNamesByItemsName(String itemnames) throws DaoException, SQLException;
	public List<CustomerMasterDTO> searchCustomersByNames(String customernames) throws DaoException, SQLException ;
	public SchemeMasterDto checkDupScheme(String sName) throws DaoException, SQLException ;
	public SchemeMasterDto getSchemeDetailsBySchemeName(String sName) throws DaoException, SQLException;

}
