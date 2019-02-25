package com.prsoftwares.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dao.LocationMasterDao;
import com.prsoftwares.dto.LocationMasterDto;



public class LocationMasterDaoImpl implements LocationMasterDao {

	@Override
	public boolean addWareHouse(LocationMasterDto locDto) throws DaoException,Exception {
		PreparedStatement ps=null;
		boolean result=false;
		Connection con=null;
		int x=0;
		try{
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String uid=(String)session.getAttribute("userId");
			uid=uid.trim();
			String cid=(String)session.getAttribute("companyId");
			cid=cid.trim();
			con = ConnectCore.getConnection();
			String query="insert into locationmaster values(default,?,?,?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(query);
			ps.setString(1, locDto.getLocationName());
			ps.setString(2, locDto.getLocationAddress());
			ps.setString(3, locDto.getLocationDescription());
			ps.setString(4, locDto.getLocationPhone());
			ps.setString(5, locDto.getLocationMobile());
			ps.setString(6, locDto.getLocationPin());
			ps.setString(7, locDto.getLocationState());
			ps.setString(8, locDto.getLocationFax());
			ps.setString(9, locDto.getLocationEmail());
			ps.setInt(10, Integer.parseInt(cid));
			ps.setInt(11, Integer.parseInt(uid));
			System.out.println("query= "+ps);
			x=ps.executeUpdate();
			if(x>0){
				result=true;
				System.out.println("save Successful");
				
			}
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}finally{
			if(con!=null)
			{
				con.close();
				ps.close();
				System.out.println("connection closed");
			}
		}
		
		return result;
	}
	
	@Override
	public List<LocationMasterDto> getAllLocationsByCompanyId() throws DaoException, Exception {
		List<LocationMasterDto> dtolist = new LinkedList<LocationMasterDto>();
		Connection con = null;
		PreparedStatement pstat=null;
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT * FROM locationmaster where companyId=?";
			pstat = con.prepareStatement(query);
			pstat.setString(1, cid);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				LocationMasterDto locDto = new LocationMasterDto();
				locDto.setLocationId(rs.getInt(1));
				locDto.setLocationName(rs.getString(2));
				locDto.setLocationAddress(rs.getString(3));
				locDto.setLocationDescription(rs.getString(4));
				locDto.setLocationPhone(rs.getString(5));
				locDto.setLocationMobile(rs.getString(6));
				locDto.setLocationPin(rs.getString(7));
				locDto.setLocationState(rs.getString(8));
				locDto.setLocationFax(rs.getString(9));
				locDto.setLocationEmail(rs.getString(10));
				locDto.setCompanyId(rs.getInt(11));
				locDto.setUserId(rs.getInt(12));
				
				dtolist.add(locDto);
			}
			System.out.println("list size= "+dtolist.size());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("failed to retreive data from database");
		} finally {
			if (con != null) {
				con.close();
				pstat.close();
			}
		}
		return dtolist;
	}

	@Override
	public LocationMasterDto getLocationByCompanyIdLocId(int locId) throws DaoException, Exception {
		Connection con = null;
		PreparedStatement ps=null;
		LocationMasterDto locDto = new LocationMasterDto();
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		try {
			con = ConnectCore.getConnection();
			String query="SELECT l.locationId, l.locationName, l.locationAddress, l.locationDescription, l.locationPhone, l.locationMobile, l.locationPin, l.locationState, l.locationFax, l.locationEmail,c.nameofcompany, u.userName From companymaster c, usermaster u, locationmaster l where c.companyId=l.companyId and u.userId=l.userId and l.companyId=? and l.locationId=?";
			ps = con.prepareStatement(query);
			ps.setInt(1, Integer.parseInt(cid));
			ps.setInt(2, locId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				locDto.setLocationId(rs.getInt(1));
				locDto.setLocationName(rs.getString(2));
				locDto.setLocationAddress(rs.getString(3));
				locDto.setLocationDescription(rs.getString(4));
				locDto.setLocationPhone(rs.getString(5));
				locDto.setLocationMobile(rs.getString(6));
				locDto.setLocationPin(rs.getString(7));
				locDto.setLocationState(rs.getString(8));
				locDto.setLocationFax(rs.getString(9));
				locDto.setLocationEmail(rs.getString(10));
				locDto.setCompanyName(rs.getString(11));
				locDto.setUserName(rs.getString(12));
			}
			System.out.println("name= "+locDto.getLocationName());
			System.out.println("com name= "+locDto.getCompanyName());
			System.out.println("user name= "+locDto.getUserName());
		} catch (Exception e) {
			System.out.println("exception= " + e);
		} finally {
			if(con!=null)
			{
				con.close();
				ps.close();
				System.out.println("connection closed");
			}
		}
		return locDto;
	}
	
	@Override
	public boolean editLocationnDetailsById(LocationMasterDto locDto) throws DaoException, Exception {

		boolean result=false;
		Connection con=null;
		PreparedStatement ps=null;
		int x=0;
		System.out.println("loc name= "+locDto.getLocationName());
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		try{
			con = ConnectCore.getConnection();
			String query="update locationmaster set locationName=?, locationAddress=?,locationDescription=?,locationPhone=?, locationMobile=?, locationPin=?, locationState=?, locationFax=?, locationEmail=?,companyid=?, userid=? where locationId=?";
			ps = con.prepareStatement(query);
			ps.setString(1, locDto.getLocationName());
			ps.setString(2, locDto.getLocationAddress());
			ps.setString(3, locDto.getLocationDescription());
			ps.setString(4, locDto.getLocationPhone());
			ps.setString(5, locDto.getLocationMobile());
			ps.setString(6, locDto.getLocationPin());
			ps.setString(7, locDto.getLocationState());
			ps.setString(8, locDto.getLocationFax());
			ps.setString(9, locDto.getLocationEmail());
			ps.setInt(10, Integer.parseInt(cid));
			ps.setInt(11, Integer.parseInt(uid));
			ps.setInt(12, locDto.getLocationId());
			System.out.println("query= "+ps);
			x=ps.executeUpdate();
			if(x>0){
				result=true;
				System.out.println("save Successful");
			}
		}catch(Exception e){
			result=false;
			System.out.println("Exception: "+e);
		}finally{
			if(con!=null)
			{
				con.close();
				ps.close();
				System.out.println("connection closed");
			}
		}
		
		return result;
	}

	@Override
	public boolean checkLocationDuplicateORNOT(LocationMasterDto locDto) throws DaoException, Exception {
		boolean returnResult = false;
		PreparedStatement ps=null;
		Connection con = null;
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		try {
			con = ConnectCore.getConnection();
			ps = con.prepareStatement("SELECT * FROM locationmaster where locationName=? and locationAddress=? and locationDescription=? and locationPhone=? and locationMobile=? and locationPin=? and locationState=? and locationFax=? and locationEmail=? and companyid=? and userid=?");
			ps.setString(1, locDto.getLocationName());
			ps.setString(2, locDto.getLocationAddress());
			ps.setString(3, locDto.getLocationDescription());
			ps.setString(4, locDto.getLocationPhone());
			ps.setString(5, locDto.getLocationMobile());
			ps.setString(6, locDto.getLocationPin());
			ps.setString(7, locDto.getLocationState());
			ps.setString(8, locDto.getLocationFax());
			ps.setString(9, locDto.getLocationEmail());
			ps.setInt(10, Integer.parseInt(cid));
			ps.setInt(11, Integer.parseInt(uid));
			System.out.println(ps);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnResult=true;
			}	
		} catch (Exception e) {
			System.out.println("Exception: " + e);

		} finally {
			if(con!=null)
			{
				con.close();
				ps.close();
				System.out.println("connection closed");
			}
		}

		return returnResult;
	}

	
	
	
}
