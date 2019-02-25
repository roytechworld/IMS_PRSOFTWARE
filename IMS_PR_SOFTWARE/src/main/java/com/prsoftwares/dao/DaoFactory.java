package com.prsoftwares.dao;

import com.prsoftwares.daoImpl.CompanyMasterDAOIMPL;
import com.prsoftwares.daoImpl.CustomerMasterDaoIMPL;
import com.prsoftwares.daoImpl.CustomerPaymentDaoImpl;
import com.prsoftwares.daoImpl.ItemMasterAndTypeDaoIMPL;
import com.prsoftwares.daoImpl.LedgerDaoImpl;
import com.prsoftwares.daoImpl.LocationMasterDaoImpl;
import com.prsoftwares.daoImpl.LoginMasterDaoIMPL;
import com.prsoftwares.daoImpl.SalesMasterDaoImpl;
import com.prsoftwares.daoImpl.SchemeMasterDaoImpl;
import com.prsoftwares.daoImpl.StockMasterDaoImpl;
import com.prsoftwares.daoImpl.SupplierMasterDAOIMPL;
import com.prsoftwares.daoImpl.SupplierPaymentDaoImpl;

public final class DaoFactory {

	
	
	public LoginMasterDAO createLoginManager()
	{
		return new LoginMasterDaoIMPL();
	}
	
	public CompanyMasterDAO createCompanyMasterManager()
	{
		return new CompanyMasterDAOIMPL();
	}
	
	
	public ItemMasterAndTypeDAO createItemMasterManager()
	{
		return new ItemMasterAndTypeDaoIMPL();
	}
	public SchemeMasterDao createSchemeMasterManager()
	{
		return new SchemeMasterDaoImpl();
	}
	
	public LocationMasterDao CreateLocatiobManager()
	{
		return new LocationMasterDaoImpl();
	}
	
	
	public SupplierMasterDAO CreateSupplierManager()
	{
		return new SupplierMasterDAOIMPL();
	}
	public CustomerMasterDAO createCustomerManager()
	{
		return new CustomerMasterDaoIMPL();
	}
	
	public StockMasterDAO createStockManager()
	{
		return new StockMasterDaoImpl();
	}
	
	public LedgerDao createLedgerManager()
	{
		return new LedgerDaoImpl();
	}
	
	public SalesMasterDao createSalesMasterManager()
	{
		return new SalesMasterDaoImpl();
	}
	
	public CustomerPaymentDao createCustomerPayManager()
	{
		return new CustomerPaymentDaoImpl();
	}
	
	public SupplierPaymentDao createSupplierPayManager()
	{
		return new SupplierPaymentDaoImpl();
	}
}
