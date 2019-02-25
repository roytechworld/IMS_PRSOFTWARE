package com.prsoftwares.daoImpl;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.struts2.ServletActionContext;

import com.prsoftwares.dao.CompanyMasterDAO;
import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dto.LoginUserCompanyMasterDto;
import com.prsoftwares.util.CurrentCompAndUser;
import com.prsoftwares.util.CurrentDate;
import com.prsoftwares.util.Messages;




public class CompanyMasterDAOIMPL extends ConnectCore implements CompanyMasterDAO {
protected static final Log log = LogFactory.getLog(CompanyMasterDAOIMPL.class);

//#########################################################################################################################################################################################
//###################################### All Methods of Saving company details . Data will be fall in Three tables UserMaster, LoginMaster and company master.##############################
//#########################################################################################################################################################################################
public boolean saveCompanyDetails(LoginUserCompanyMasterDto companydetails) throws DaoException,SQLException
{
	
	boolean returnResult=false;
	boolean uploadToTestYourAppsResult=false;
	String flag="";
	Connection con=null;
	String imageURL = "";
	try
	{
   //----------- Directory creating utility methods used there . User image file path  will be automatically created if not exist-------------------------
   //------------------------------------------------------------------------------------------------------------------------------------------------------	
	String ImagePath = getUserImageFilePath();
  //------------------------------------------------------------------------------------------------------------------------------------------------------	
	boolean checkDublicateval=checkDuplicateUserExistOrNot(companydetails)	;
	if(checkDublicateval==false)
	{
		System.out.println("MAx user ID found :"+getNewUSerId());
		imageURL = ImagePath + "/" + "UserImage"+ getNewUSerId()+ ".jpg";
		System.out.println("Image url where wile will be saved.."+imageURL);
		File ImageToCreate = new File(ImagePath, "UserImage"+ getNewUSerId() + ".jpg");
		System.out.println(companydetails.getUserPhoto());
		try
		{
			    if(!companydetails.getUserPhoto().equals(null))
			    {
			    	FileUtils.copyFile(companydetails.getUserPhoto(), ImageToCreate);
			    	uploadToTestYourAppsResult = uploadFileToWebServer(imageURL, "UserImage" + getNewUSerId() + ".jpg");
			    	flag="Image found";
			    }
		}
		catch(NullPointerException n)
		{
			imageURL="http://testyourapps.in/SMS/userimages/default.jpg";
			uploadToTestYourAppsResult=true;
			flag="Image not found";
		}
		if(uploadToTestYourAppsResult)
		{
		con=ConnectCore.getConnection();
		String sql = "insert into usermaster(userName,Email,userPhotoURL) value(?,?,?)";
		java.sql.PreparedStatement pt=con.prepareStatement(sql);
		pt.setString(1,companydetails.getUserName());
		pt.setString(2,companydetails.getEmail());
		if(flag.equals("Image found"))
		{pt.setString(3, "http://testyourapps.in/SMS/userimages/"+ "UserImage" + getNewUSerId() + ".jpg");}
		else if(flag.equals("Image not found"))
		{pt.setString(3, "http://testyourapps.in/SMS/userimages/default.jpg");}
		int row = pt.executeUpdate();
		if (row > 0) {
			int newUSerID=getNewUSerId();
			String sql2 = "insert into loginmaster(Password,RoleStatus,userId) value(?,?,?)";
			java.sql.PreparedStatement pt2=con.prepareStatement(sql2);
			pt2.setString(1,companydetails.getPassword());
			pt2.setString(2,"ADMIN");
			pt2.setInt(3,  newUSerID );
			int row2 = pt2.executeUpdate();
			if(row2>0){
             String sql3="insert into companymaster(nameofcompany,phoneno,contactperson,email,address,vatno,panno,servicetaxno,ACno,bankdetails,userid,dateofsubmission) values(?,?,?,?,?,?,?,?,?,?,?,?)";
             java.sql.PreparedStatement pt3=con.prepareStatement(sql3);
             pt3.setString(1,companydetails.getNameofcompany());
             pt3.setString(2, companydetails.getPhoneno());
             pt3.setString(3, companydetails.getContactperson());
             pt3.setString(4, companydetails.getEmail());
             pt3.setString(5, companydetails.getAddress());
             if(!companydetails.getVatno().equals(""))
             {pt3.setString(6, companydetails.getVatno());}
             else{ pt3.setString(6, "NA"); }
             if(!companydetails.getPanno().equals("")){
             pt3.setString(7, companydetails.getPanno());}
             else
             {pt3.setString(7, "NA"); }
             if(!companydetails.getServiceTaxno().equals(""))
             {pt3.setString(8, companydetails.getServiceTaxno()); }
             else
             {pt3.setString(8, "NA"); }
             if(!companydetails.getAcno().equals(""))
             {pt3.setString(9, companydetails.getAcno());}
             else
             {pt3.setString(9, "NA");}
             if(!companydetails.getBankDetails().equals(""))
             {pt3.setString(10, companydetails.getBankDetails());}
             else
             { pt3.setString(10, "NA");}
             pt3.setInt(11, getNewUSerId());
             pt3.setString(12, CurrentDate.getDate());
             System.out.println("ps3= "+pt3);
             int row3=pt3.executeUpdate();
             if(row3>0)
             {System.out.println("Sucessfully saved USer details and Login Details with photo");returnResult=true;}}}}}}
	         catch (Exception e) {e.printStackTrace();log.error("failed to retreive data from database", e);} finally {
		    if (con != null) {con.close();
		    System.out.println("Connection closed");
		    }
	        }return returnResult;
           }


           public boolean updateCompanyDetails(LoginUserCompanyMasterDto companydetails) throws DaoException,SQLException
            {
	        boolean result=false;
	        Connection con=null; 
	        int checknumberOfUserAvailablity;
	        PreparedStatement p=null;
	        PreparedStatement p2=null;
	        PreparedStatement p3=null;
	        PreparedStatement p4=null;
	        HttpServletRequest request=ServletActionContext.getRequest();
	        HttpSession session=request.getSession();
	        String photoUrl=(String)session.getAttribute("photoUrl");
	        System.out.println("photoUrl"+photoUrl);
	        try
	        {
	        con=ConnectCore.getConnection();	
	        checknumberOfUserAvailablity=checkLoginUserAvailablity(companydetails.getUserName(),companydetails.getPassword(), CurrentCompAndUser.getCurrentCompany());
	        if(checknumberOfUserAvailablity>0)
	        {
	        	System.out.println("Updating company details");
	        	String sqlup="UPDATE companymaster SET vatno=?,servicetaxno=?, panno=? WHERE companyId=?";
	            p=con.prepareStatement(sqlup);
	        	p.setString(1, companydetails.getVatno());
	        	p.setString(2,companydetails.getServiceTaxno());
	        	p.setString(3, companydetails.getPanno());
	        	p.setString(4, CurrentCompAndUser.getCurrentCompany());
	        	int i=p.executeUpdate();
	        	if(i>0)
	        	{
	        		System.out.println("Comapany details updated successfully !!!");
	        		if(p!=null)
	        		{
	        		p.close();	
	        		Messages.setCompanyNewUserAndUpdateinfo("<p style='color:green;'>Company details updated successfully !</p>");
	        	    result=true;	
	        		}
	        	}
	        }
	        else
	        {
	        	System.out.println("Updating company details and adding new user");
	        	String sqlup="UPDATE companymaster SET vatno=?,servicetaxno=?, panno=? WHERE companyId=?";
	            p2=con.prepareStatement(sqlup);
	        	p2.setString(1, companydetails.getVatno());
	        	p2.setString(2,companydetails.getServiceTaxno());
	        	p2.setString(3, companydetails.getPanno());
	        	p2.setString(4,CurrentCompAndUser.getCurrentCompany());
	        	int i=p2.executeUpdate();
	        	if(i>0)
	        	{
	        		System.out.println("Comapany details updated successfully !!!");
	        		if(p2!=null)
	        		{
	        			p2.close();
	        			System.out.println("inserting new user details in loginmaster");
	        			String sqlloginmaster="insert into loginmaster(password,RoleStatus,userId)values(?,?,?)";
	        		    p3=con.prepareStatement(sqlloginmaster);
	        			p3.setString(1,companydetails.getPassword());
	        			p3.setString(2,"USER");
	        			p3.setString(3,CurrentCompAndUser.getCurrentCompany());
	        			int l=p3.executeUpdate();
	        			if(l>0)
	        			{
	        			System.out.println("login user details updated successfully !!!");
	        			if(p3!=null)
	        			{
	        			p3.close();
	        			String sqlusermaster="insert into usermaster(userName,Email,userPhotoURL)values(?,?,?)";
	        		    p4=con.prepareStatement(sqlusermaster);
	        			p4.setString(1, companydetails.getUserName());
	        			p4.setString(2, companydetails.getEmail());
	        			p4.setString(3, photoUrl);
	        		    int oi = p4.executeUpdate();
	        		    if(oi>0)
	        		    {
	        		    p4.close();	
	        		    System.out.println("user details updated successfully !!!"); 
	        		    Messages.setCompanyNewUserAndUpdateinfo("<p style='color:green;'>New user created successfully !</p>");
	        		    result=true;
	        		    }}}}}}}
	        catch(Exception e)
	        {
	        	System.out.println("Exception occur type is "+e);
	        }
	        finally
	        {
	        if(con!=null)	
	        {
	        	con.close();
	        	System.out.println("connection closed");
	        }	
	        }
 	       return result;
            }
           
          public int checkLoginUserAvailablity(String username,String password,String companyId) throws SQLException 
          {
        	int result=0;
        	Connection con=null; 
 	        try
 	        {
 	        con=ConnectCore.getConnection();	
 	        String sql3="SELECT u.userName,u.userPhotoURL,u.userID,l.Password,l.RoleStatus,c.nameofcompany,c.companyId,c.phoneno,c.contactperson,c.email,c.address,c.vatno,c.panno,c.servicetaxno,c.ACno,c.bankdetails FROM loginmaster l JOIN usermaster u ON u.userId=l.loginID JOIN companymaster c ON c.companyId=l.userId WHERE u.userName=? AND l.Password=? AND c.companyId=?";
 	        PreparedStatement pstat=con.prepareStatement(sql3);
 	        pstat.setString(1, username);
 	        pstat.setString(2,password);
 	        pstat.setString(3,companyId);
 	        
 	        ResultSet rs=pstat.executeQuery();
 	        while(rs.next())
 	        {
 	        	 result=1;
 	        }
 	        }
 	        catch(Exception e)
 	        {
 	        	System.out.println("Exception occur type is "+e);
 	        }
 	        finally
 	        {
 	        if(con!=null)	
 	        {
 	        	con.close();
 	        	System.out.println("connection closed");
 	        }
 	        }
        	return result;
          }
           
public String getUserImageFilePath()throws DaoException,IOException
{
	String filepath="C:/SMS/images";
	  File f = null;
      try{      
         f = new File(filepath);
         if(!f.exists())
         { f.mkdirs(); 
           System.out.println("Directory created sucessfully at" +filepath);
         }
         else
         { System.out.println("Directory already created");}  
      }catch(Exception e){
         // if any error occurs
    	  System.out.println("Error occur while creating Directory "+ e);
         e.printStackTrace();
      }
      return filepath;
}

public boolean checkDuplicateUserExistOrNot(LoginUserCompanyMasterDto cdto)throws DaoException, SQLException {
	boolean returnresult=false;
	Connection con = null;
	try {con=ConnectCore.getConnection();
	    String query="SELECT u.userName,l.Password FROM usermaster u JOIN loginmaster l ON u.userID=l.userId  JOIN companymaster c ON u.userId=c.userid WHERE u.userName='"+cdto.getUserName()+"' AND l.Password='"+cdto.getPassword()+"'";
		java.sql.PreparedStatement ps= con.prepareStatement(query);
		
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {returnresult=true;}
		else
		{returnresult=false;}
	    } catch (Exception e) {System.out.println("Exception: " + e);returnresult=false;} finally {con.close();}
	    return returnresult;
        }

public int getNewUSerId() throws DaoException, SQLException
{
	int userId=0;
	Connection con = null;
	try {con=ConnectCore.getConnection();
		java.sql.PreparedStatement ps= con.prepareStatement("SELECT MAX(userID)AS userID FROM usermaster");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) 
		{userId=Integer.parseInt(rs.getString("userID"));}} catch (Exception e) {System.out.println("Exception: " + e);} finally {con.close();}
	    return userId;
  }

public boolean uploadFileToWebServer(String localuploadFilePath,String nameofUpLoadedFile) {
	boolean result = false;
	String server = "www.testyourapps.in";
	int port = 21;
	String user = "testyourapps";
	String pass = "Apple1234#";
	FTPClient ftpClient = new FTPClient();
	try {
		ftpClient.connect(server, port);
		ftpClient.login(user, pass);
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		File firstLocalFile = new File(localuploadFilePath);
		String firstRemoteFile = "httpdocs/SMS/userimages/"+nameofUpLoadedFile;
		InputStream inputStream = new FileInputStream(firstLocalFile);
		System.out.println("Start uploading first file");
		boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
		inputStream.close();
		if (done) {
			System.out.println("File ---" + nameofUpLoadedFile+ " is Uploaded to path " + localuploadFilePath);
			result = true;
		} else {System.out.println("Error occur during uploading jpg file file !!!");}
	} catch (IOException ex) {
		System.out.println("Error: " + ex.getMessage());
		ex.printStackTrace();
	} finally {
		try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
		     } catch (IOException ex) {
		    	 	ex.printStackTrace();
		        }
	}
	            return result;
}

//#########################################################################################################################################################################################
//###################################### All Methods of Saving company details . Code End here.############################################################################################
//#########################################################################################################################################################################################
@Override
public List<LoginUserCompanyMasterDto> getCompanyDetails() throws DaoException, SQLException
{
	List<LoginUserCompanyMasterDto> companyList=new LinkedList<LoginUserCompanyMasterDto>();
	Connection con = null;
	try {
			con=ConnectCore.getConnection();
			java.sql.PreparedStatement ps= con.prepareStatement("SELECT * FROM companymaster");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) 
			{
				LoginUserCompanyMasterDto cdto=new LoginUserCompanyMasterDto();
				cdto.setCompanyId(rs.getString("companyId"));
				cdto.setContactperson(rs.getString("contactperson"));
				cdto.setNameofcompany(rs.getString("nameofcompany"));
				cdto.setAddress(rs.getString("address"));
				cdto.setEmail(rs.getString("email"));
				cdto.setBankDetails(rs.getString("bankdetails"));
				cdto.setAcno(rs.getString("ACno"));
				cdto.setServiceTaxno(rs.getString("servicetaxno"));
				cdto.setPanno(rs.getString("panno"));
				cdto.setVatno(rs.getString("vatno"));
				cdto.setPhoneno(rs.getString("phoneno"));
				
				companyList.add(cdto);
			}
			if(ps!=null){
				rs.close();
				ps.close();
			}
			System.out.println("size= "+companyList.size());
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			if(con!=null)
			{
				con.close();
				System.out.println("Connection closed");
			}
		}
	    return companyList;
  }

@Override
public LoginUserCompanyMasterDto getCurrentCompanyDetails() throws DaoException, SQLException
{
	LoginUserCompanyMasterDto cdto=new LoginUserCompanyMasterDto();
	Connection con = null;
	try {
			con=ConnectCore.getConnection();
			java.sql.PreparedStatement ps= con.prepareStatement("SELECT * FROM companymaster WHERE companyId=?");
			ps.setString(1, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) 
			{
				
				cdto.setCompanyId(rs.getString("companyId"));
				cdto.setContactperson(rs.getString("contactperson"));
				cdto.setNameofcompany(rs.getString("nameofcompany"));
				cdto.setAddress(rs.getString("address"));
				cdto.setEmail(rs.getString("email"));
				cdto.setBankDetails(rs.getString("bankdetails"));
				cdto.setAcno(rs.getString("ACno"));
				cdto.setServiceTaxno(rs.getString("servicetaxno"));
				cdto.setPanno(rs.getString("panno"));
				cdto.setVatno(rs.getString("vatno"));
				cdto.setPhoneno(rs.getString("phoneno"));
				
			}
			if(ps!=null){
				rs.close();
				ps.close();
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			if(con!=null)
			{
				con.close();
				System.out.println("Connection closed");
			}
		}
	    return cdto;
  }

}
