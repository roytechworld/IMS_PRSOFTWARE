package com.prsoftwares.dao;
import java.sql.SQLException;
import java.util.List;

import com.prsoftwares.dto.LoginUserCompanyMasterDto;



public interface CompanyMasterDAO {
	
	public boolean saveCompanyDetails(LoginUserCompanyMasterDto companydetails) throws DaoException,SQLException;
	public boolean updateCompanyDetails(LoginUserCompanyMasterDto companydetails) throws DaoException,SQLException;
	List<LoginUserCompanyMasterDto> getCompanyDetails() throws DaoException, SQLException;
	LoginUserCompanyMasterDto getCurrentCompanyDetails() throws DaoException,SQLException;
}
