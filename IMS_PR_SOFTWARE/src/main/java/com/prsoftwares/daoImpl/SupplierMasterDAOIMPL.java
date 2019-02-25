package com.prsoftwares.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dao.SupplierMasterDAO;
import com.prsoftwares.dto.SupplierMasterDTO;
import com.prsoftwares.util.CurrentDate;



public class SupplierMasterDAOIMPL extends ConnectCore implements SupplierMasterDAO {
	protected static final Log log = LogFactory.getLog(SupplierMasterDAOIMPL.class);
	
	public boolean saveSupplierDetails(SupplierMasterDTO sDTO)throws DaoException,SQLException
	{
		boolean result=false;
		Connection con=null;
		PreparedStatement ps=null;
		int x=0;
		try{
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String uid=(String)session.getAttribute("userId");
			uid=uid.trim();
			String cid=(String)session.getAttribute("companyId");
			cid=cid.trim();
			con = ConnectCore.getConnection();
			String query="insert into suppliermaster(SUPPLIERNAME,ADDRESS,ContactPerson,Mobile,PHONE,Fax,PIN,STATE,VATNo,CSTNo,STNo,EMAIL,PAN,NOTE,userId,companyId,dateofCreation,status)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(query);
			
			
			if(!sDTO.getSUPPLIERNAME().equals(""))
			{
				ps.setString(1, sDTO.getSUPPLIERNAME());
			}
			else
			{
				ps.setString(1, "NA");
			}
			
			if(!sDTO.getADDRESS().equals(""))
			{
				ps.setString(2, sDTO.getADDRESS());
			}
			else
			{
				ps.setString(2, "NA");
			}
			
			if(!sDTO.getContactPerson().equals(""))
			{
				ps.setString(3, sDTO.getContactPerson());
			}
			else
			{
				ps.setString(3, "NA");
			}
			
			if(!sDTO.getMOBILE().equals(""))
			{
				ps.setString(4, sDTO.getMOBILE());
			}
			else
			{
				ps.setString(4, "NA");
			}
			if(!sDTO.getPHONE().equals(""))
			{
				ps.setString(5, sDTO.getPHONE());
			}
			else
			{
				ps.setString(5, "NA");
			}
			if(!sDTO.getFax().equals(""))
			{
				ps.setString(6, sDTO.getFax());
			}
			else
			{
				ps.setString(6, "NA");
			}
			if(!sDTO.getPIN().equals(""))
			{
				ps.setString(7, sDTO.getPIN());
			}
			else
			{
				ps.setString(7, "NA");
			}
           
            if(!sDTO.getSTATE().equals(""))
			{
            	 ps.setString(8, sDTO.getSTATE());
			}
			else
			{
				ps.setString(8, "NA");
			}
            if(!sDTO.getVATNo().equals(""))
			{
				ps.setString(9, sDTO.getVATNo());
			}
			else
			{
				ps.setString(9, "NA");
			}
			if(!sDTO.getCSTNo().equals(""))
			{
				ps.setString(10, sDTO.getCSTNo());
			}
			else
			{
				ps.setString(10, "NA");
			}
			if(!sDTO.getSTNo().equals(""))
			{
				ps.setString(11, sDTO.getSTNo());
			}
			else
			{
				ps.setString(11, "NA");
			}
			if(!sDTO.getEMAIL().equals(""))
			{
				ps.setString(12, sDTO.getEMAIL());
			}
			else
			{
				ps.setString(12, "NA");
			}
			if(!sDTO.getPAN().equals(""))
			{
				ps.setString(13, sDTO.getPAN());
			}
			else
			{
				ps.setString(13, "NA");
			}
			if(!sDTO.getNOTE().equals(""))
			{
				ps.setString(14, sDTO.getNOTE());
			}
			else
			{
				ps.setString(14, "NA");
			}
			ps.setString(15, uid);
			ps.setString(16, cid);
			ps.setString(17, CurrentDate.getDate());
			ps.setString(18, "1");
			
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
	

	public List<SupplierMasterDTO> getAllSupplierDetails() throws DaoException, SQLException {
		List<SupplierMasterDTO> list = new LinkedList<SupplierMasterDTO>();
		PreparedStatement pstat =null;
		Connection con = null;
		try {
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=null;
			String query;
			try
			{
			 session=request.getSession();
			 String cid=(String)session.getAttribute("companyId");
			 query = "SELECT s.SupplierId,s.SUPPLIERNAME,s.ADDRESS,s.ContactPerson,s.Mobile,s.PHONE,s.Fax,s.PIN,s.STATE,s.VATNo,s.CSTNo,s.STNo,S.EMAIL,s.PAN,s.NOTE,s.userId,s.companyId,s.dateofCreation,s.status,c.nameofcompany,u.userName FROM suppliermaster s JOIN companymaster c ON s.companyId=c.companyId JOIN usermaster u ON s.userId=u.userID where s.status=1 AND c.companyId='"+cid+"'";
			}
			catch(NullPointerException n)
			{
			 query = "SELECT s.SupplierId,s.SUPPLIERNAME,s.ADDRESS,s.ContactPerson,s.Mobile,s.PHONE,s.Fax,s.PIN,s.STATE,s.VATNo,s.CSTNo,s.STNo,S.EMAIL,s.PAN,s.NOTE,s.userId,s.companyId,s.dateofCreation,s.status,c.nameofcompany,u.userName FROM suppliermaster s JOIN companymaster c ON s.companyId=c.companyId JOIN usermaster u ON s.userId=u.userID WHERE s.status=1";
			}
			con = ConnectCore.getConnection();
			 pstat = con.prepareStatement(query);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				SupplierMasterDTO dto = new SupplierMasterDTO();
				dto.setSupplierId(rs.getInt("SupplierId"));
				dto.setSUPPLIERNAME(rs.getString("SUPPLIERNAME"));
				dto.setADDRESS(rs.getString("ADDRESS"));
				dto.setContactPerson(rs.getString("ContactPerson"));
				dto.setMOBILE(rs.getString("Mobile"));
				dto.setPHONE(rs.getString("PHONE"));
				dto.setFax(rs.getString("Fax"));
				dto.setPIN(rs.getString("PIN"));
				dto.setSTATE(rs.getString("STATE"));
				dto.setVATNo(rs.getString("VATNo"));
				dto.setCSTNo(rs.getString("CSTNo"));
				dto.setSTNo(rs.getString("STNo"));
				dto.setEMAIL(rs.getString("EMAIL"));
				dto.setPAN(rs.getString("PAN"));
				dto.setNOTE(rs.getString("NOTE"));
				dto.setUserID(rs.getInt("userId"));
				dto.setCompanyId(rs.getInt("companyId"));
				dto.setCompanyName(rs.getString("nameofcompany"));
				dto.setUsername(rs.getString("userName"));
				dto.setStatus(rs.getString("status"));
				
				System.out.println("id= "+dto.getSupplierId());
				list.add(dto);
				
			}
			rs.close();
			if(pstat!=null)
			{
				pstat.close();
			}
			System.out.println(" suppp list size= "+list.size());
			
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				
				con.close();
			}
		}
		return list;
	}
	
	
	
	public List<SupplierMasterDTO> getAllSupplierDetailsBySUPNAME(String SupplierName) throws DaoException, SQLException {
		List<SupplierMasterDTO> list = new LinkedList<SupplierMasterDTO>();
		Connection con = null;
		PreparedStatement pstat=null;
		try {
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			con = ConnectCore.getConnection();
			String cid=(String)session.getAttribute("companyId");
			String query = "SELECT s.SupplierId,s.SUPPLIERNAME,s.ADDRESS,s.ContactPerson,s.Mobile,s.PHONE,s.Fax,s.PIN,s.STATE,s.VATNo,s.CSTNo,s.STNo,S.EMAIL,s.PAN,s.NOTE,s.userId,s.companyId,s.dateofCreation,s.status,c.nameofcompany,u.userName FROM suppliermaster s JOIN companymaster c ON s.companyId=c.companyId JOIN usermaster u ON s.userId=u.userID where s.status=1 AND s.SUPPLIERNAME like ? AND s.companyId=?";
			pstat = con.prepareStatement(query);
			pstat.setString(1, "%" + SupplierName + "%");
			pstat.setString(2, cid);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				SupplierMasterDTO dto = new SupplierMasterDTO();
				dto.setSupplierId(rs.getInt("SupplierId"));
				dto.setSUPPLIERNAME(rs.getString("SUPPLIERNAME"));
				dto.setADDRESS(rs.getString("ADDRESS"));
				dto.setContactPerson(rs.getString("ContactPerson"));
				dto.setMOBILE(rs.getString("Mobile"));
				dto.setPHONE(rs.getString("PHONE"));
				dto.setFax(rs.getString("Fax"));
				dto.setPIN(rs.getString("PIN"));
				dto.setSTATE(rs.getString("STATE"));
				dto.setVATNo(rs.getString("VATNo"));
				dto.setCSTNo(rs.getString("CSTNo"));
				dto.setSTNo(rs.getString("STNo"));
				dto.setEMAIL(rs.getString("EMAIL"));
				dto.setPAN(rs.getString("PAN"));
				dto.setNOTE(rs.getString("NOTE"));
				dto.setUserID(rs.getInt("userId"));
				dto.setCompanyId(rs.getInt("companyId"));
				dto.setCompanyName(rs.getString("nameofcompany"));
				dto.setUsername(rs.getString("userName"));
				dto.setStatus(rs.getString("status"));
				
				System.out.println("id= "+dto.getSupplierId());
				
				list.add(dto);
			}
			System.out.println("list size= "+list.size());
			
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
				pstat.close();
			}
		}
		return list;
	}
	
	public SupplierMasterDTO getSupplierDetailsById(int id)throws DaoException,SQLException{
		Connection con = null;
		PreparedStatement ps=null;
		SupplierMasterDTO dto = new SupplierMasterDTO();
		try {
			con = ConnectCore.getConnection();
			String query="SELECT s.SupplierId,s.SUPPLIERNAME,s.ADDRESS,s.ContactPerson,s.Mobile,s.PHONE,s.Fax,s.PIN,s.STATE,s.VATNo,s.CSTNo,s.STNo,S.EMAIL,s.PAN,s.NOTE,s.userId,s.companyId,s.dateofCreation,s.status,c.nameofcompany,u.userName FROM suppliermaster s JOIN companymaster c ON s.companyId=c.companyId JOIN usermaster u ON s.userId=u.userID WHERE s.SupplierId=?";
			ps = con.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dto.setSupplierId(rs.getInt("SupplierId"));
				dto.setSUPPLIERNAME(rs.getString("SUPPLIERNAME"));
				dto.setADDRESS(rs.getString("ADDRESS"));
				dto.setContactPerson(rs.getString("ContactPerson"));
				dto.setMOBILE(rs.getString("Mobile"));
				dto.setPHONE(rs.getString("PHONE"));
				dto.setFax(rs.getString("Fax"));
				dto.setPIN(rs.getString("PIN"));
				dto.setSTATE(rs.getString("STATE"));
				dto.setVATNo(rs.getString("VATNo"));
				dto.setCSTNo(rs.getString("CSTNo"));
				dto.setSTNo(rs.getString("STNo"));
				dto.setEMAIL(rs.getString("EMAIL"));
				dto.setPAN(rs.getString("PAN"));
				dto.setNOTE(rs.getString("NOTE"));
				dto.setUserID(rs.getInt("userId"));
				dto.setCompanyId(rs.getInt("companyId"));
				dto.setDateOfCreation(rs.getString("dateofCreation"));
				dto.setCompanyName(rs.getString("nameofcompany"));
				dto.setUsername(rs.getString("userName"));
				dto.setStatus(rs.getString("status"));
			}
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
		return dto;
	}
	
	
	/*public SupplierMasterDTO getSupplierDetailsByName(String  supplierName)throws DaoException,SQLException{
		Connection con = null;
		SupplierMasterDTO dto = new SupplierMasterDTO();
		try {
			con = ConnectCore.getConnection();
			String query="SELECT s.SupplierId,s.SUPPLIERNAME,s.ADDRESS,s.ContactPerson,s.Mobile,s.PHONE,s.Fax,s.PIN,s.STATE,s.VATNo,s.CSTNo,s.STNo,S.EMAIL,s.PAN,s.NOTE,s.userId,s.companyId,s.dateofCreation,c.nameofcompany,u.userName FROM suppliermaster s JOIN companymaster c ON s.companyId=c.companyId JOIN usermaster u ON s.userId=u.userID WHERE LIKE ?; ";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, "%" + supplierName+ "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dto.setSupplierId(rs.getInt("SupplierId"));
				dto.setSUPPLIERNAME(rs.getString("SUPPLIERNAME"));
				dto.setADDRESS(rs.getString("ADDRESS"));
				dto.setContactPerson(rs.getString("ContactPerson"));
				dto.setMOBILE(rs.getString("Mobile"));
				dto.setPHONE(rs.getString("PHONE"));
				dto.setFax(rs.getString("Fax"));
				dto.setPIN(rs.getString("PIN"));
				dto.setSTATE(rs.getString("STATE"));
				dto.setVATNo(rs.getString("VATNo"));
				dto.setCSTNo(rs.getString("CSTNo"));
				dto.setSTNo(rs.getString("STNo"));
				dto.setEMAIL(rs.getString("EMAIL"));
				dto.setPAN(rs.getString("PAN"));
				dto.setNOTE(rs.getString("NOTE"));
				dto.setUserID(rs.getInt("userId"));
				dto.setCompanyId(rs.getInt("companyId"));
				dto.setDateOfCreation(rs.getString("dateofCreation"));
				dto.setCompanyName(rs.getString("nameofcompany"));
				dto.setUsername(rs.getString("userName"));
				
			}
		} catch (Exception e) {
			System.out.println("exception= " + e);
		} finally {
			con.close();
		}
		return dto;
	}
	*/
	
	public boolean UpdateSupplierDetails(SupplierMasterDTO sDTO)throws DaoException,SQLException
	{
		boolean result=false;
		Connection con = null;
		int sid=sDTO.getSupplierId();
		PreparedStatement ps=null;
		System.out.println(sid);
		
		try {
			con = ConnectCore.getConnection();
			String query="UPDATE suppliermaster SET SUPPLIERNAME=?,ADDRESS =?,ContactPerson=?,Mobile=?,PHONE=?,Fax=?,PIN=?,STATE=?,VATNo=?,CSTNo=?,STNo=?,EMAIL=?,PAN=?,NOTE=?,userId=?,companyId=?,dateofCreation=?,status=?  WHERE SupplierId=?";
			ps = con.prepareStatement(query);
			
			ps.setString(1, sDTO.getSUPPLIERNAME());
			ps.setString(2, sDTO.getADDRESS());
			ps.setString(3, sDTO.getContactPerson());
			ps.setString(3, sDTO.getContactPerson());
			ps.setString(4, sDTO.getMOBILE());
			ps.setString(5, sDTO.getPHONE());
			if(!sDTO.getFax().equals(""))
			{ps.setString(6, sDTO.getFax() );}
			else
			{ps.setString(6, "NA");	}
			if(!sDTO.getPIN().equals(""))
			{ps.setString(7, sDTO.getPIN() );}
			else
			{ps.setString(7, "NA");	}
            ps.setString(8, sDTO.getSTATE());
			if(!sDTO.getVATNo().equals(""))
			{ps.setString(9, sDTO.getVATNo() );}
			else
			{ps.setString(9, "NA");	}
			if(!sDTO.getCSTNo().equals(""))
			{ps.setString(10, sDTO.getCSTNo() );}
			else
			{ps.setString(10, "NA");}
			if(!sDTO.getSTNo().equals(""))
			{ps.setString(11, sDTO.getSTNo() );}
			else
			{ps.setString(11, "NA");}
			ps.setString(12, sDTO.getEMAIL());
			if(!sDTO.getPAN().equals(""))
			{ps.setString(13, sDTO.getPAN() );}
			else
			{ps.setString(13, "NA");}
			if(!sDTO.getPAN().equals(""))
			{ps.setString(14, sDTO.getNOTE() );}
			else
			{ps.setString(14, "NA");}
			ps.setInt(15, sDTO.getUserID());
			ps.setInt(16, sDTO.getCompanyId());
			ps.setString(17, CurrentDate.getDate());
			ps.setString(18, "1");
			ps.setInt(19, sid);
			
			int x=ps.executeUpdate();
			if(x>0)
			{
				System.out.println("Updated Sucessfully one record updated !!!");
				result=true;
			}
			
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
		
		return result;
	}
	
	public boolean checkSupplierDuplicateORNOT(SupplierMasterDTO sDTO) throws DaoException, SQLException {
		boolean returnResult = false;
		Connection con = null;
		PreparedStatement ps=null;
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		try {
			con = ConnectCore.getConnection();
			ps = con.prepareStatement("SELECT * FROM suppliermaster where SUPPLIERNAME=? and ADDRESS =? and ContactPerson=? and" +
					" Mobile? and PIN=? and STATE=? and userId=? and companyId=?");
			ps.setString(1, sDTO.getSUPPLIERNAME());
			ps.setString(2, sDTO.getADDRESS());
			ps.setString(3, sDTO.getContactPerson());
			ps.setString(4, sDTO.getMOBILE());
			
			ps.setString(5, sDTO.getPIN() );
			ps.setString(6, sDTO.getSTATE());
			ps.setInt(7, sDTO.getUserID());
			ps.setInt(8, sDTO.getCompanyId());
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				returnResult = true;
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
	
	@Override
	public boolean deleteSupplierById(int sid) throws DaoException, SQLException {
		boolean returnResult = false;
		Connection con = null;
		PreparedStatement ps=null;
		try {
				con = ConnectCore.getConnection();
				ps = con.prepareStatement("UPDATE suppliermaster SET status=0 where SupplierId=?");
				ps.setInt(1, sid);
				int x=ps.executeUpdate();
				if (x>0) {
					returnResult=true;
				}else{
					returnResult = false;
				}
		}catch(Exception e){
			System.out.println("Ex: "+e);
		}finally{
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
