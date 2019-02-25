package com.prsoftwares.daoImpl;

import java.awt.Font;
import java.io.File;
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

import com.onbarcode.barcode.Code128;
import com.onbarcode.barcode.IBarcode;
import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.CustomerMasterDAO;
import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dto.CustomerMasterDTO;
import com.prsoftwares.util.CurrentDate;
import com.prsoftwares.util.Messages;



public class CustomerMasterDaoIMPL extends ConnectCore implements CustomerMasterDAO  {
protected static final Log log = LogFactory.getLog(CustomerMasterDaoIMPL.class);
//############################################################################################################################################################################################
//#################################### SAVE AND EDIT of Customer in Single method -- Start here ##############################################################################################
public boolean saveCustomerDetails(CustomerMasterDTO cDTO)throws DaoException, SQLException
{
	boolean result=false;
	Connection con=null;
	int x=0;
	String query="";
	String barcodeimage="";
	boolean flagUpdate=false;
	try{
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		con = ConnectCore.getConnection();
		try
		{
			if(!cDTO.getCustomerId().equals(""))
				query="UPDATE customermaster SET cName=?,phoneno=?,mobile=?,email=?,address=?,state=?,pincode=?,Fax=?,vat=?,type=?,note=?,dateofcreate=?,companyid=?,userid=?, remarks=?,status=?,custBarcodeUrl=?,custBarcode=?,custType=?,gsttin=?,stcode=? WHERE customerId='"+cDTO.getCustomerId()+"'";
				flagUpdate=true;
		}
		catch(NullPointerException e)
		{
			query="insert into customermaster(cName,phoneno,mobile,email,address,state,pincode,Fax,vat,type,note,dateofcreate,companyid,userid,remarks,status,custBarcodeUrl,custBarcode,custType,gsttin,stcode)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		}
		PreparedStatement ps = con.prepareStatement(query);
		

		if(!cDTO.getcName().equals(""))
		{
			ps.setString(1, cDTO.getcName());
		}
		else
		{
			ps.setString(1, "NA");
		}
		if(!cDTO.getPhoneno().equals(""))
		{
			ps.setString(2, cDTO.getPhoneno());
		}
		else
		{
			ps.setString(2, "NA");
		}
		
		if(!cDTO.getMobile().equals(""))
		{
			ps.setString(3, cDTO.getMobile());
		}
		else
		{
			ps.setString(3, "NA");
		}
		if(!cDTO.getEmail().equals(""))
		{
			ps.setString(4, cDTO.getEmail());
		}
		else
		{
			ps.setString(4, "NA");
		}
		if(!cDTO.getAddress().equals(""))
		{
			ps.setString(5, cDTO.getAddress());
		}
		else
		{
			ps.setString(5, "NA");
		}
		System.out.println("State found" +cDTO.getState());
		
		if(!cDTO.getState().equals(""))
		{
			ps.setString(6, cDTO.getState());
		}
		else
		{
			ps.setString(6, "NA");
		}
		if(!cDTO.getPincode().equals(""))
		{
			ps.setString(7, cDTO.getPincode() );
		}
		else
		{
			ps.setString(7, "NA");
		}
		if(!cDTO.getFax().equals(""))
		{
			ps.setString(8, cDTO.getFax() );
		}
		else
		{
			ps.setString(8, "NA");
		}
		if(!cDTO.getVat().equals(""))
		{
			ps.setString(9, cDTO.getVat()) ;
		}
		else
		{
			ps.setString(9, "NA");
		}
		if(!cDTO.getType().equals(""))
		{
			ps.setString(10, cDTO.getType());
		}
		else
		{
			ps.setString(10, "NA");
		}
		if(!cDTO.getNote().equals(""))
		{
			ps.setString(11, cDTO.getNote() );
		}
		else
		{
			ps.setString(11, "NA");
		}
		ps.setString(12, CurrentDate.getDate());
		ps.setString(13, cid);
		ps.setString(14, uid);
		ps.setString(15, "NE");
		ps.setString(16, "1");
		

		
		
		//##################################################### Customer Barcode generateCode start here ################################################################
	    int custId=getCustomerId()+1;
		String custBarcode=cDTO.getcName()+" C"+custId;
		try
		{
		if(!cDTO.getCustomerId().equals(""))
		{
			ps.setString(17, cDTO.getCustomerBarcodeUrl());
		}
		}
		catch(NullPointerException f)
		{
			if(cDTO.getType()!="ONETIME")
			{
			barcodeimage=generatetBarcodes(custBarcode);
			if(barcodeimage.equals("Error")){	
			ps.setString(17, "NA");	}
			else{
			ps.setString(17, barcodeimage);}
			}
			else
			{
				ps.setString(17, "NA");	
			}
		}
		try
		{
			if(!cDTO.getCustomerId().equals(""))
			{
				ps.setString(18, cDTO.getCustomerBarcode());	
			}
		}
		catch(NullPointerException h)
		{
			if(cDTO.getType()!="ONETIME")
			{
				ps.setString(18, custBarcode);		
			}
			else
			{
				ps.setString(18, "NA");	
			}
			
		}
	
		ps.setString(19, cDTO.getCustomerType());
		ps.setString(20, cDTO.getGsttin());
		ps.setString(21, cDTO.getStcode());
		
		//#################################################### Customer  Barcode generateCode End here  ###################################################################
		
		System.out.println("query= "+ps);
		x=ps.executeUpdate();
		if(x>0){
			result=true;
			if(flagUpdate)
			{
			Messages.setCustomerSaveSuccesfully("Customer details updated successfully . ");
			}
			else
			{
				Messages.setCustomerSaveSuccesfully("Customer details save successfully . Barcode generated is : "+custBarcode);	
			}
			System.out.println("Customer details save Successful");	
		}
	}catch(Exception e){
		System.out.println("Exception: "+e);
	}finally{
		con.close();
	}
	return result;
}



public int getCustomerId() throws DaoException,SQLException
{
	int customerId=0;
	Connection con=null;
	try
	{
	con=ConnectCore.getConnection();
	PreparedStatement pr=con.prepareStatement("select max(customerId) as custId from customermaster");
	ResultSet rs=pr.executeQuery();
	while(rs.next())
	{
	customerId=rs.getInt("custId");
	}
	}
	catch(Exception e)
	{
	System.out.println("Exception: "+e)	;
	}
	finally
	{
	if(con!=null)
	con.close();
	}
	return customerId;
}

public String generatetBarcodes(String custCode)throws DaoException
{
//	String dir="d:\\SMS\\barcodes";
	String dir="c:\\SMS\\Tomcat7\\webapps\\sms\\img\\customerBarcode";
	String imageUrl="";
	boolean success = false;
	String uploadStatus="";
	boolean directoryMakingFlag=false;
	  try
        { 
		
		  Code128 barcode = new Code128();
    		
    		/*
    		   Code 128 Valid data char set:
    		        all 128 ASCII characters (Char from 0 to 127)
    		*/
    		barcode.setData(custCode);
    		
    		//  Set the processTilde property to true, if you want use the tilde character "~"
    		//  to specify special characters in the input data. Default is false.
    		//  1) All 128 ISO/IEC 646 characters, i.e. characters 0 to 127 inclusive, in accordance with ISO/IEC 646.
    		//       NOTE This version consists of the G0 set of ISO/IEC 646 and the C0 set of ISO/IEC 6429 with values 28 - 31
    		//       modified to FS, GS, RS and US respectively.
    		//  2) Characters with byte values 128 to 255 may also be encoded.
    		//  3) 4 non-data function characters.
    		//  4) 4 code set selection characters.
    		//  5) 3 Start characters.
    		//  6) 1 Stop character.
    		barcode.setProcessTilde(false);
    		
    		// Code 128 Unit of Measure, pixel, cm, or inch
    		barcode.setUom(IBarcode.UOM_PIXEL);
    		// Code 128 barcode bar module width (X) in pixel
    		barcode.setX(0f);
    		// Code 128 barcode bar module height (Y) in pixel
    		barcode.setY(45f);
    		
    		// barcode image margins
    		barcode.setLeftMargin(5f);
    		barcode.setRightMargin(5f);
    		barcode.setTopMargin(5f);
    		barcode.setBottomMargin(5f);
    		
    		// barcode image resolution in dpi
    		barcode.setResolution(70);
    		
    		// disply barcode encoding data below the barcode
    		barcode.setShowText(false);
    		// barcode encoding data font style
    		java.awt.Font f=new java.awt.Font("Arial", Font.BOLD, 16);
            barcode.setTextFont(f);
    		// space between barcode and barcode encoding data
    		barcode.setTextMargin(6);
    		
    		//  barcode displaying angle
    		barcode.setRotate(IBarcode.ROTATE_0);
        
             File directory = new File(dir);
             if (directory.exists())
             {System.out.println("Directory already exists ...");
              directoryMakingFlag=true; } 
             else 
             {System.out.println("Directory not exists, creating now");
              success = directory.mkdir();
             if (success) 
             {System.out.printf("Successfully created new directory : %s%n", dir);
              directoryMakingFlag=true;} 
             else {System.out.printf("Failed to create new directory: %s%n", dir);
             imageUrl="Error" ;}}
             if(directoryMakingFlag)
             {
             
//             imageUrl="d://SMS/barcodes/"+productcode+".jpg";
             imageUrl="c://SMS/Tomcat7/webapps/sms/img/customerBarcode/"+custCode+".jpg";
             
             barcode.drawBarcode(imageUrl);
             System.out.println("################################################################");
             System.out.println("######## Barcode : "+custCode+" Suceesfully generated. ######");
             System.out.println("#################################################################");
            
             
           /* File source = new File("d:\\SMS\\barcodes\\"+productcode+".jpg");
 			File dest = new File("C:\\SMS\\Tomcat7\\webapps\\sms\\img\\barcode\\"+productcode+".jpg");

 			// copy file using FileStreams
 			long start = System.nanoTime();
 			long end;
 			copyFileUsingFileStreams(source, dest);
 			System.out.println("Time taken by FileStreams Copy = "
 					+ (System.nanoTime() - start));*/
             
 			imageUrl="/img/customerBarcode/"+custCode+".jpg";
             
            /* uploadStatus=uploadFileToWebServer(imageUrl,productcode);
             
             if(uploadStatus.equals("Error"))
             {imageUrl="Error" ;}
             else
             { imageUrl= uploadStatus;}
             } */
        }}
        catch(Exception e)
        {
            e.printStackTrace();
            imageUrl="Error" ;
        }
	   return imageUrl;
}
//#################################### SAVE AND EDIT of Customer in Single method -- END here ##############################################################################################

//#################################### VIEW LIST AND EDIT PAGE CALL of Customer in Single method -- Start here ##############################################################################
public List<CustomerMasterDTO > ViewANDEditCALLCustomerDetails(int customerID) throws DaoException, SQLException {
	List<CustomerMasterDTO > list = new LinkedList<CustomerMasterDTO >();
	Connection con = null;
	try {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		con = ConnectCore.getConnection();
		String cid=(String)session.getAttribute("companyId");
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		String query;
		if(customerID>0)
		{
			query = "SELECT c.customerId,c.cName,c.phoneno,c.mobile, c.email,c.address,c.state,c.pincode,c.Fax,c.vat,c.type,c.note,c.dateofcreate,c.companyid,c.userid,c.remarks,c.custBarcodeUrl,c.custBarcode,c.custType,c.gsttin,c.stcode,co.nameofcompany,u.userName FROM customermaster c JOIN companymaster co ON c.companyid=co.companyId JOIN usermaster u ON c.userid=u.userID WHERE c.status=1 AND c.customerId='"+customerID+"'";
		}
		else
		{
			query = "SELECT c.customerId,c.cName,c.phoneno,c.mobile, c.email,c.address,c.state,c.pincode,c.Fax,c.vat,c.type,c.note,c.dateofcreate,c.companyid,c.userid,c.remarks,c.custBarcodeUrl,c.custBarcode,c.gsttin,c.custType,c.stcode,co.nameofcompany,u.userName FROM customermaster c JOIN companymaster co ON c.companyid=co.companyId JOIN usermaster u ON c.userid=u.userID WHERE c.status=1 AND c.companyId='"+cid+"' and c.cName<>'cash' ";
		}
		PreparedStatement pstat = con.prepareStatement(query);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			CustomerMasterDTO  cto = new CustomerMasterDTO ();
			cto.setCustomerId(rs.getString("customerId"));
			cto.setcName(rs.getString("cName"));
			cto.setPhoneno(rs.getString("phoneno"));
			cto.setMobile(rs.getString("mobile"));
			cto.setEmail(rs.getString("email"));
			cto.setAddress(rs.getString("address"));
			cto.setState(rs.getString("state"));
			cto.setPincode(rs.getString("pincode"));
			cto.setFax(rs.getString("Fax"));
			cto.setVat(rs.getString("vat"));
			cto.setType(rs.getString("type"));
			cto.setNote(rs.getString("note"));
			cto.setDateofcreate(rs.getString("dateofcreate"));
			cto.setCompanyid(rs.getString("companyid"));
			cto.setUserid(rs.getString("userid"));
			cto.setCompanyName(rs.getString("nameofcompany"));
			cto.setUserName(rs.getString("userName"));
			cto.setRemarks(rs.getString("remarks"));
			cto.setCustomerBarcodeUrl(rs.getString("custBarcodeUrl"));
			cto.setCustomerBarcode(rs.getString("custBarcode"));
			cto.setCustomerType(rs.getString("custType"));
			cto.setGsttin(rs.getString("gsttin"));
			cto.setStcode(rs.getString("stcode"));
			list.add(cto);
		}
		System.out.println("list size= "+list.size());
		log.info("Details taken sucessfully !!");
	} catch (Exception e) {
		e.printStackTrace();
		log.error("failed to retreive data from database", e);
	} finally {
		if (con != null) {
			con.close();
		}}
	return list;
}
//#################################### VIEW LIST AND EDIT PAGE CALL of Customer in Single method -- END here ##############################################################################
//#########################################################################################################################################################################################

@Override
public boolean checkCustomerDuplicateORNOT(CustomerMasterDTO cDTO) throws DaoException, SQLException {
		boolean returnResult = false;
		Connection con = null;
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		try {
			con = ConnectCore.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM customermaster where cName=? and mobile=? and address=? and state=? and pincode=? and companyid=? and userid=?");
			ps.setString(1, cDTO.getcName());
			ps.setString(2, cDTO.getMobile());
			ps.setString(3, cDTO.getAddress());
			ps.setString(4, cDTO.getState());
			ps.setString(5, cDTO.getPincode() );
			ps.setInt(6, Integer.parseInt(cid));
			ps.setInt(7, Integer.parseInt(uid));
			System.out.println("query= "+ps);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnResult=true;
			}
		}
		 catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			con.close();
		}
		
		return returnResult;
	}

@Override
public boolean deleteCustomerById(int cid) throws DaoException, SQLException {
	boolean returnResult = false;
	Connection con = null;
	try {
			con = ConnectCore.getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE customermaster SET status=0 where customerId=?");
			ps.setInt(1, cid);
			int x=ps.executeUpdate();
			if (x>0) {
				returnResult=true;
			}else{
				returnResult = false;
			}
	}catch(Exception e){
		System.out.println("Ex: "+e);
	}
	return returnResult;
}

public List<CustomerMasterDTO > getCustomerDetailsByCustBarcode(String  custBarcode) throws DaoException, SQLException {
	List<CustomerMasterDTO > list = new LinkedList<CustomerMasterDTO >();
	Connection con = null;
	try {

HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		con = ConnectCore.getConnection();
		String cid=(String)session.getAttribute("companyId");
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		String query;
		if(custBarcode.length()>0)
		{
			query = "SELECT c.customerId,c.cName,c.phoneno,c.mobile, c.email,c.address,c.state,c.pincode,c.Fax,c.vat,c.type,c.note,c.dateofcreate,c.companyid,c.userid,c.remarks,c.custBarcodeUrl,c.custBarcode,co.nameofcompany,u.userName FROM customermaster c JOIN companymaster co ON c.companyid=co.companyId JOIN usermaster u ON c.userid=u.userID WHERE c.status=1 AND c.custBarcode='"+custBarcode+"'";
		}
		else
		{
			query = "SELECT c.customerId,c.cName,c.phoneno,c.mobile, c.email,c.address,c.state,c.pincode,c.Fax,c.vat,c.type,c.note,c.dateofcreate,c.companyid,c.userid,c.remarks,co.nameofcompany,u.userName FROM customermaster c JOIN companymaster co ON c.companyid=co.companyId JOIN usermaster u ON c.userid=u.userID WHERE c.status=1 AND c.companyId='"+cid+"'";
		}
		PreparedStatement pstat = con.prepareStatement(query);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			CustomerMasterDTO  cto = new CustomerMasterDTO ();
			cto.setCustomerId(rs.getString("customerId"));
			cto.setcName(rs.getString("cName"));
			cto.setPhoneno(rs.getString("phoneno"));
			cto.setMobile(rs.getString("mobile"));
			cto.setEmail(rs.getString("email"));
			cto.setAddress(rs.getString("address"));
			cto.setState(rs.getString("state"));
			cto.setPincode(rs.getString("pincode"));
			cto.setFax(rs.getString("Fax"));
			cto.setVat(rs.getString("vat"));
			cto.setType(rs.getString("type"));
			cto.setNote(rs.getString("note"));
			cto.setDateofcreate(rs.getString("dateofcreate"));
			cto.setCompanyid(rs.getString("companyid"));
			cto.setUserid(rs.getString("userid"));
			cto.setCompanyName(rs.getString("nameofcompany"));
			cto.setUserName(rs.getString("userName"));
			cto.setRemarks(rs.getString("remarks"));
			cto.setCustomerBarcode(rs.getString("custBarcode"));
			cto.setCustomerBarcodeUrl(rs.getString("custBarcodeUrl"));
			list.add(cto);
		}
		System.out.println("list size= "+list.size());
		log.info("Details taken sucessfully !!");
	} catch (Exception e) {
		e.printStackTrace();
		log.error("failed to retreive data from database", e);
	} finally {
		if (con != null) {
			con.close();
		}}
	return list;
}


public List<CustomerMasterDTO> getAllCustomerDetails() throws DaoException, SQLException {
	List<CustomerMasterDTO> list = new LinkedList<CustomerMasterDTO>();
	PreparedStatement pstat =null;
	Connection con = null;
	try {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=null;
		String query;
		 String cid="";
		try
		{
		 session=request.getSession();
		
		 cid=(String)session.getAttribute("companyId");
		 query = "SELECT s.customerId,s.cName,s.address,s.mobile,s.phoneno,s.Fax,s.pincode,s.vat,s.email,s.dateofcreate,c.nameofcompany,u.userName,c.companyId FROM customermaster s JOIN companymaster c ON s.companyid=c.companyId JOIN usermaster u ON s.userId=u.userID WHERE  c.companyId='"+cid+"'";
		}
		catch(NullPointerException n)
		{
		 query = "SELECT s.customerId,s.cName,s.address,s.mobile,s.phoneno,s.Fax,s.pincode,s.vat,s.email,s.dateofcreate,c.nameofcompany,u.userName,c.companyId FROM customermaster s JOIN companymaster c ON s.companyid=c.companyId JOIN usermaster u ON s.userId=u.userID WHERE  c.companyId='"+cid+"'";
		}
		con = ConnectCore.getConnection();
		 pstat = con.prepareStatement(query);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			
			CustomerMasterDTO  cto = new CustomerMasterDTO ();
			cto.setCustomerId(rs.getString("customerId"));
			cto.setcName(rs.getString("cName"));
			cto.setPhoneno(rs.getString("phoneno"));
			cto.setMobile(rs.getString("mobile"));
			cto.setEmail(rs.getString("email"));
			cto.setAddress(rs.getString("address"));
			cto.setPincode(rs.getString("pincode"));
			cto.setFax(rs.getString("Fax"));
			cto.setVat(rs.getString("vat"));
			cto.setDateofcreate(rs.getString("dateofcreate"));
			cto.setCompanyid(rs.getString("companyId"));
			cto.setUserid("1");
			cto.setCompanyName(rs.getString("nameofcompany"));
			cto.setUserName(rs.getString("userName"));
			list.add(cto);
			
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



public List<CustomerMasterDTO> getAllCustomerDetailsByNames(String customername) throws DaoException, SQLException {
	List<CustomerMasterDTO> list = new LinkedList<CustomerMasterDTO>();
	PreparedStatement pstat =null;
	Connection con = null;
	try {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=null;
		String query="";
		 String cid="";
		try
		{
		 session=request.getSession();
		
		 cid=(String)session.getAttribute("companyId");
		 
		 
		 query="SELECT * FROM customermaster WHERE cName LIKE'%"+customername+"%'";
		 
		}
		catch(NullPointerException n)
		{
		
		}
		con = ConnectCore.getConnection();
		 pstat = con.prepareStatement(query);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			
			CustomerMasterDTO  cto = new CustomerMasterDTO ();
			cto.setCustomerId(rs.getString("customerId"));
			cto.setcName(rs.getString("cName"));
			cto.setPhoneno(rs.getString("phoneno"));
			cto.setMobile(rs.getString("mobile"));
			cto.setEmail(rs.getString("email"));
			cto.setAddress(rs.getString("address"));
			
			list.add(cto);
			
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



}

