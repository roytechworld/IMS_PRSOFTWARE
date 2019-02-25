package com.prsoftwares.dao;

import java.sql.SQLException;
import java.util.List;

import com.prsoftwares.dto.LoginUserCompanyMasterDto;
import com.prsoftwares.util.FinancialYear;

public interface LoginMasterDAO {
	
	public LoginUserCompanyMasterDto getValidateLoginDetails(LoginUserCompanyMasterDto logindetails)throws DaoException,SQLException;

	int getSizeofTotalUsers() throws DaoException, SQLException;

	public List<FinancialYear> getYearRangeOfUserStock(String companyId) throws DaoException, SQLException;

}
