package com.prsoftwares.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dao.LoginMasterDAO;
import com.prsoftwares.dto.LoginUserCompanyMasterDto;
import com.prsoftwares.dto.SalesMasterDto;
import com.prsoftwares.util.CurrentCompAndUser;
import com.prsoftwares.util.FinancialYear;



public class LoginMasterDaoIMPL extends ConnectCore implements LoginMasterDAO {
	protected static final Log log = LogFactory.getLog(LoginMasterDaoIMPL.class);
//######################################Logged In validation start here ############################################################################# 
	public LoginUserCompanyMasterDto getValidateLoginDetails(LoginUserCompanyMasterDto logindetails)throws DaoException,SQLException
	{
		LoginUserCompanyMasterDto loginobj=new LoginUserCompanyMasterDto();
		Connection con=null;
		try
		{con=ConnectCore.getConnection();
		PreparedStatement st=con.prepareStatement("SELECT u.userName,u.userPhotoURL,u.userID,l.Password,l.RoleStatus,c.nameofcompany,c.companyId,c.phoneno,c.contactperson,c.email,c.address,c.vatno,c.panno,c.servicetaxno,c.ACno,c.bankdetails FROM loginmaster l JOIN usermaster u ON u.userId=l.loginID JOIN companymaster c ON c.userid=l.userId WHERE u.userName=? AND l.Password=? AND c.companyId=?");
		st.setString(1, logindetails.getUserName());
		st.setString(2, logindetails.getPassword());
		st.setString(3, logindetails.getCompanyId());
		
		System.out.println("st= "+st);
		
		ResultSet rs=st.executeQuery();
		while(rs.next())
		{loginobj.setPassword(rs.getString("Password"));
		loginobj.setUserName(rs.getString("userName"));
		loginobj.setRoleStatus(rs.getString("RoleStatus"));
		loginobj.setUserUrl(rs.getString("userPhotoURL"));
		loginobj.setUserId(rs.getString("userID"));
		loginobj.setNameofcompany(rs.getString("nameofcompany"));
		loginobj.setCompanyId(rs.getString("companyId"));
		loginobj.setContactperson(rs.getString("contactperson"));
		loginobj.setEmail(rs.getString("email"));
		loginobj.setAddress(rs.getString("address"));
		loginobj.setVatno(rs.getString("vatno"));
		loginobj.setPanno(rs.getString("panno"));
		loginobj.setServiceTaxno(rs.getString("servicetaxno"));
		loginobj.setAcno(rs.getString("ACno"));
		loginobj.setBankDetails(rs.getString("bankdetails"));
		loginobj.setPhoneno(rs.getString("phoneno"));
		
		}}
		catch(Exception e ){System.out.println("Error occued --"+e);}finally {
			
			if(con!=null)
			{
			con.close();
			System.out.println("connection closed.");
			}
			
		
		}return loginobj;
	}
//######################################Logged In validation End here #############################################################################
	@Override
	public int getSizeofTotalUsers() throws DaoException, SQLException {
		int size = 0;
		Connection con = null;
		PreparedStatement pstat=null;
		List<SalesMasterDto> list = new ArrayList<SalesMasterDto>();
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT COUNT(`password`) FROM loginmaster WHERE userId=?";
			pstat = con.prepareStatement(query);
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {			
				size=rs.getInt(1);
			}
			pstat.close();
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
		return size;
	}
	
	
	@Override
	public List<FinancialYear> getYearRangeOfUserStock(String companyId) throws DaoException,SQLException {
		
		List<FinancialYear> YearRange=new ArrayList<FinancialYear>();
		/*try {
		 * Connection con = null;
		PreparedStatement pstat=null;
			con = ConnectCore.getConnection();
			String query = "SELECT YEAR(purchaseDate) AS purchaseYear FROM stockmaster WHERE companyId=? GROUP BY YEAR(purchaseDate)";
			pstat = con.prepareStatement(query);
			pstat.setString(1, companyId);
			ResultSet rs = pstat.executeQuery();
			String range="";
			
			while (rs.next()) {
				FinancialYear yDto=new FinancialYear();
				range=range+rs.getString(1)+"-";
				System.out.println(range);
				if(range.length()>9){
					range=range.substring(0,9);
					System.out.println(range);
					yDto.setfYear(range);
					YearRange.add(yDto);
					range=range.substring(5,9)+"-";
					System.out.println(range);
				}*/
				YearRange.add(new FinancialYear("2000-2001"));
				YearRange.add(new FinancialYear("2001-2002"));
				YearRange.add(new FinancialYear("2002-2003"));
				YearRange.add(new FinancialYear("2003-2004"));
				YearRange.add(new FinancialYear("2004-2005"));
				YearRange.add(new FinancialYear("2005-2006"));
				YearRange.add(new FinancialYear("2006-2007"));
				YearRange.add(new FinancialYear("2008-2009"));
				YearRange.add(new FinancialYear("2009-2010"));
				YearRange.add(new FinancialYear("2010-2011"));
				YearRange.add(new FinancialYear("2011-2012"));
				YearRange.add(new FinancialYear("2012-2013"));
				YearRange.add(new FinancialYear("2013-2014"));
				YearRange.add(new FinancialYear("2014-2015"));
				YearRange.add(new FinancialYear("2015-2016"));
				YearRange.add(new FinancialYear("2016-2017"));
				YearRange.add(new FinancialYear("2017-2018"));
				YearRange.add(new FinancialYear("2018-2019"));
				YearRange.add(new FinancialYear("2019-2020"));
				YearRange.add(new FinancialYear("2021-2022"));
				YearRange.add(new FinancialYear("2022-2023"));
				YearRange.add(new FinancialYear("2023-2024"));
				YearRange.add(new FinancialYear("2024-2025"));
				
			/*}
			for (int i = 0; i < YearRange.size(); i++) {
				System.out.println("r= "+YearRange.get(i).getfYear());
			}
			pstat.close();
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
			}
		}*/
		return YearRange;
	}
}
