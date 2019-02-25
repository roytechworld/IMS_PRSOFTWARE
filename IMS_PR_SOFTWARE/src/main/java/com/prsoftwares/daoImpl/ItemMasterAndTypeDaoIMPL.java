package com.prsoftwares.daoImpl;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.struts2.ServletActionContext;

import com.onbarcode.barcode.Code128;
import com.onbarcode.barcode.Code39;
import com.onbarcode.barcode.IBarcode;
import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dao.ItemMasterAndTypeDAO;
import com.prsoftwares.dto.ItemCategoryTypeMasterDTO;
import com.prsoftwares.util.CurrentCompAndUser;
import com.prsoftwares.util.CurrentDate;






public class ItemMasterAndTypeDaoIMPL extends ConnectCore implements ItemMasterAndTypeDAO{
protected static final Log log = LogFactory.getLog(ItemMasterAndTypeDaoIMPL.class);
	
	
//___________________________________ Item Category: Start ____________________________________________________________________
	public boolean SaveItemMasterType(ItemCategoryTypeMasterDTO itemType) throws DaoException,SQLException
	{
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
			String query="insert into itemtype(itemType,dateofcreation,userid,companyid,ItemDescription)values(?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1,itemType.getItemType());
			ps.setString(2,CurrentDate.getDate());
			ps.setInt(3,Integer.parseInt(uid));
			ps.setInt(4,Integer.parseInt(cid));
			ps.setString(5, itemType.getItemTypeDescription());
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
			System.out.println("connection closed");
			}
		}
		
		return result;
	}
	
	@Override
	public List<ItemCategoryTypeMasterDTO> getAllItemType() throws DaoException, SQLException {
		List<ItemCategoryTypeMasterDTO> dtolist = new LinkedList<ItemCategoryTypeMasterDTO>();
		Connection con = null;
		PreparedStatement pstat=null;
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT * FROM itemtype where companyId=?";
		    pstat = con.prepareStatement(query);
			pstat.setString(1, cid);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				ItemCategoryTypeMasterDTO iType = new ItemCategoryTypeMasterDTO();
				iType.setItemTypeID(rs.getInt("itemTypeID"));
				iType.setItemType(rs.getString("itemType"));
				iType.setTypeDateofcreation(rs.getString("dateofcreation"));
				iType.setUserID(rs.getInt("userID"));
				iType.setCompanyID(rs.getInt("companyID"));
				dtolist.add(iType);
			}
			System.out.println("list size= "+dtolist.size());
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				pstat.close();
				con.close();
				System.out.println("connection closed");
			}
		}
		return dtolist;
	}
	
	public ItemCategoryTypeMasterDTO getItemTypeById(int id)throws DaoException,SQLException{
		Connection con = null;
		ItemCategoryTypeMasterDTO idto = new ItemCategoryTypeMasterDTO();
		try {
			con = ConnectCore.getConnection();
			String query="SELECT it.itemTypeid, it.itemType,it.ItemDescription,c.nameofcompany,u.userName FROM itemtype it JOIN companymaster c ON it.companyid=c.companyId JOIN usermaster u ON it.userId=u.userID WHERE itemTypeid=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				idto.setItemTypeID(Integer.parseInt(rs.getString(1)));
			    idto.setItemType(rs.getString(2));
			    idto.setItemTypeDescription(rs.getString(3));
				idto.setCompanyname(rs.getString(4));
				idto.setUserName(rs.getString(5));
				
			}
		} catch (Exception e) {
			System.out.println("exception= " + e);
		} finally {
			con.close();
		}
		return idto;
	}
	
	@Override
	public boolean editItemTypeById(ItemCategoryTypeMasterDTO itemType) throws DaoException, SQLException {

		boolean result=false;
		Connection con=null;
		int x=0;
		System.out.println("item name= "+itemType.getItemName());
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		try{
			con = ConnectCore.getConnection();
			String query="update itemtype set itemType=?, dateofcreation=?, userid=?, companyid=?, ItemDescription=? where itemTypeid=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, itemType.getItemType());
			ps.setString(2, CurrentDate.getDate());
			ps.setInt(3, Integer.parseInt(uid));
			ps.setInt(4, Integer.parseInt(cid));
			ps.setString(5, itemType.getItemTypeDescription());
			ps.setInt(6, itemType.getItemTypeID());
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
			con.close();
		}
		
		return result;
	}
	//___________________________________ Item Category: End ____________________________________________________________________
	
	//___________________________________ Item : Start ____________________________________________________________________
	/*@Override
	public ItemCategoryTypeMasterDTO getItemDetailsByItemNameBrandId(String iName, String bId) throws DaoException, SQLException {
		ItemCategoryTypeMasterDTO  item=new ItemCategoryTypeMasterDTO();
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		Connection con=null;
		String companyId=(String)session.getAttribute("companyId");
		companyId=companyId.trim();	
		try {
				con=ConnectCore.getConnection();;
				String sql="SELECT * FROM itemmaster WHERE companyId=? AND itemname LIKE '"+iName+"' AND itemBrandId="+bId;
				PreparedStatement ps=con.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(companyId));
				System.out.println("ps="+ps);
				ResultSet rs=ps.executeQuery();
				if (rs.next()) {
					while(rs.next()){
						item.setItemId(rs.getString("itemId"));
						item.setItemName(rs.getString("itemName"));
						item.setItemDateofcreation(rs.getString("dateofcreation"));
						item.setItemTypeID(rs.getInt("itemTypeId"));
						item.setUserID(rs.getInt("userId"));
						item.setCompanyID(rs.getInt("companyId"));
						item.setItemSizeId(rs.getInt("itemSizeId"));
						item.setItemBrandId(rs.getInt("itemBrandId"));
						//item.setItemMrp(rs.getString("itemMrp"));
						item.setItemUnit(rs.getString("itemUnit"));
					}
				}else{
					item=null;
				}
		}catch(Exception e){
			System.out.println("ex= "+e);
		}finally{
			con.close();
		}
		
		return item;
	}
	
	@Override
	public ItemCategoryTypeMasterDTO getItemDetailsByItemCode(String iCode) throws DaoException, SQLException {
		ItemCategoryTypeMasterDTO  item=new ItemCategoryTypeMasterDTO();
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		Connection con=null;
		String companyId=(String)session.getAttribute("companyId");
		companyId=companyId.trim();	
		try {
				con=ConnectCore.getConnection();;
				String sql="SELECT * FROM itemmaster WHERE companyId=? AND itemId = '"+iCode+"'";
				PreparedStatement ps=con.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(companyId));
				System.out.println("ps="+ps);
				ResultSet rs=ps.executeQuery();
				if (rs.next()) {
					while(rs.next()){
						item.setItemId(rs.getString("itemId"));
						item.setItemName(rs.getString("itemName"));
						item.setItemDateofcreation(rs.getString("dateofcreation"));
						item.setItemTypeID(rs.getInt("itemTypeId"));
						item.setUserID(rs.getInt("userId"));
						item.setCompanyID(rs.getInt("companyId"));
						item.setItemSizeId(rs.getInt("itemSizeId"));
						item.setItemBrandId(rs.getInt("itemBrandId"));
						item.setItemMrp(rs.getString("itemMrp"));
						item.setItemUnit(rs.getString("itemUnit"));
					}
				}else{
					item=null;
				}
		}catch(Exception e){
			System.out.println("ex= "+e);
		}finally{
			con.close();
		}
		
		return item;
	}*/
	
	@Override
	public boolean getItemDetailsByItemNameBrandId(String iName, String bId) throws DaoException, SQLException {
		ItemCategoryTypeMasterDTO  item=new ItemCategoryTypeMasterDTO();
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		Connection con=null;
		String companyId=(String)session.getAttribute("companyId");
		companyId=companyId.trim();	
		boolean result=false;
		try {
				con=ConnectCore.getConnection();;
				String sql="SELECT * FROM itemmaster WHERE companyId=? AND itemname LIKE '"+iName+"' AND itemBrandId="+bId;
				PreparedStatement ps=con.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(companyId));
				System.out.println("ps="+ps);
				ResultSet rs=ps.executeQuery();
				if (rs.next()) {
					result=true;
				}else{
					result=false;
				}
		}catch(Exception e){
			System.out.println("ex= "+e);
		}finally{
			con.close();
		}
		
		return result;
	}
	
	@Override
	public boolean getItemDetailsByItemCode(String iCode) throws DaoException, SQLException {
		ItemCategoryTypeMasterDTO  item=new ItemCategoryTypeMasterDTO();
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		Connection con=null;
		String companyId=(String)session.getAttribute("companyId");
		companyId=companyId.trim();
		boolean result=false;
		try {
				con=ConnectCore.getConnection();;
				String sql="SELECT * FROM itemmaster WHERE companyId=? AND itemId = '"+iCode+"'";
				PreparedStatement ps=con.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(companyId));
				System.out.println("ps="+ps);
				ResultSet rs=ps.executeQuery();
				if (rs.next()) {
					result=true;
				}else{
					result=false;
				}
		}catch(Exception e){
			System.out.println("ex= "+e);
		}finally{
			con.close();
		}
		
		return result;
	}
	
	//#####################################################################################################################################################################	
		//############################################### PRODUCT CODE WITH BARCODE CREATING PROCESS START HERE ###############################################################
		//#####################################################################################################################################################################	
			@Override
			public boolean saveItemDetails(ItemCategoryTypeMasterDTO item) throws DaoException,SQLException
			{
				boolean result=false;
				Connection con=null;
				int x=0;
				System.out.println("item name= "+item.getItemName());
				HttpServletRequest request=ServletActionContext.getRequest();
				HttpSession session=request.getSession();
				String uid=(String)session.getAttribute("userId");
				uid=uid.trim();
				String cid=(String)session.getAttribute("companyId");
				cid=cid.trim();
				String barcodeimage="";
				boolean success=false;
				String dir="d://SMS";
				File directory = new File(dir);
		        if (directory.exists()){
		        System.out.println("Directory already exists ...");} 
		        else{System.out.println("Directory not exists, creating now");
		        success = directory.mkdir();
		        if (success) {
		        System.out.printf("Successfully created new directory : %s%n", dir);} 
		        else{
		        System.out.printf("Failed to create new directory: %s%n", dir);}}
		        
		        String itemcode=item.getItemId()+" MRP "+item.getItemMrp();
				System.out.println("Item code created is :"+itemcode);
				try{
					con = ConnectCore.getConnection();
					String query="insert into itemmaster values(default,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement ps = con.prepareStatement(query);
					ps.setString(1, itemcode);
					ps.setString(2, item.getItemName());
					ps.setInt(3, Integer.parseInt(uid));
					ps.setString(4, CurrentDate.getDate());
					ps.setInt(5, item.getItemTypeID());
					ps.setInt(6, Integer.parseInt(uid));
					ps.setInt(7, Integer.parseInt(cid));
					ps.setInt(8, item.getItemSizeId());
					ps.setInt(9, item.getItemBrandId());
					ps.setString(10, "0");
					ps.setString(11, item.getItemMrp());
					ps.setString(12, item.getItemUnit());
		
					
					
					barcodeimage=generatetBarcodes(itemcode);
					if(barcodeimage.equals("Error")){	
					ps.setString(13, "NA");	}
					else{
					ps.setString(13, barcodeimage);
					
					
					}
					ps.setString(14, item.getHsncode());
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
					con.close();
				}
				return result;
			}
			
			public String generatetBarcodes(String productcode)throws DaoException
			{
//				String dir="d:\\SMS\\barcodes";
				String dir="c:\\SMS\\Tomcat7\\webapps\\sms\\img\\barcode";
				
				
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
		        		barcode.setData(productcode);
		        		
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
		        		barcode.setShowText(true);
		        		// barcode encoding data font style
		        		java.awt.Font f=new java.awt.Font("Arial", Font.BOLD, 17);
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
			             
//			             imageUrl="d://SMS/barcodes/"+productcode+".jpg";
			             imageUrl="c://SMS/Tomcat7/webapps/sms/img/barcode/"+productcode+".jpg";
			             
			             barcode.drawBarcode(imageUrl);
			             System.out.println("################################################################");
			             System.out.println("######## Barcode : "+productcode+" Suceesfully generated. ######");
			             System.out.println("#################################################################");
			            
			             
			           /* File source = new File("d:\\SMS\\barcodes\\"+productcode+".jpg");
			 			File dest = new File("C:\\SMS\\Tomcat7\\webapps\\sms\\img\\barcode\\"+productcode+".jpg");

			 			// copy file using FileStreams
			 			long start = System.nanoTime();
			 			long end;
			 			copyFileUsingFileStreams(source, dest);
			 			System.out.println("Time taken by FileStreams Copy = "
			 					+ (System.nanoTime() - start));*/
			             
			 			imageUrl="/img/barcode/"+productcode+".jpg";
			             
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
			
			
			private static void copyFileUsingFileStreams(File source, File dest) throws IOException {
				InputStream input = null;
				OutputStream output = null;
				try {
					input = new FileInputStream(source);
					output = new FileOutputStream(dest);
					byte[] buf = new byte[1024];
					int bytesRead;
					while ((bytesRead = input.read(buf)) > 0) {
						output.write(buf, 0, bytesRead);
					}
				} finally {
					input.close();
					output.close();
				}
			}
			
			public String uploadFileToWebServer(String localuploadFilePath,String nameofUpLoadedFile) {
				String result = "";
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
					String firstRemoteFile = "httpdocs/SMS/barcode/"+nameofUpLoadedFile+".jpg";
					InputStream inputStream = new FileInputStream(firstLocalFile);
					System.out.println("Start uploading first file");
					boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
					inputStream.close();
					if (done) 
					{
					System.out.println("File ---" + nameofUpLoadedFile+ " is Uploaded to path " + localuploadFilePath);
					result = "http://testyourapps.in/SMS/barcode/"+nameofUpLoadedFile+".jpg";
					} 
					else {
					System.out.println("Error occur during uploading jpg file file !!!");
					result = "Error";}
				    } 
				    catch (Exception ex) 
				    {
					System.out.println("Error: " + ex.getMessage());
					ex.printStackTrace();
					result = "Error";
				    } 
				    finally 
				    {
					try {
					if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
					}
					} 
					catch (Exception ex) {
					ex.printStackTrace();
					}
				}
				return result;
			}
		//#####################################################################################################################################################################	
		//############################################### PRODUCT CODE WITH BARCODE CREATING PROCESS END HERE ###############################################################
		//#####################################################################################################################################################################	

	
	@Override
	public List<ItemCategoryTypeMasterDTO> getAllItemByCompanyId() throws DaoException, SQLException {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		List<ItemCategoryTypeMasterDTO> dtolist = new LinkedList<ItemCategoryTypeMasterDTO>();
		Connection con = null;
		PreparedStatement pstat=null;
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT * FROM itemmaster where companyId=?";
		    pstat = con.prepareStatement(query);
			pstat.setInt(1, Integer.parseInt(cid));
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				ItemCategoryTypeMasterDTO item = new ItemCategoryTypeMasterDTO();
				item.setItemId(rs.getString("itemId"));
				item.setItemName(rs.getString("itemName"));
				item.setItemDateofcreation(rs.getString("dateofcreation"));
				item.setItemTypeID(rs.getInt("itemTypeId"));
				item.setUserID(rs.getInt("userId"));
				item.setCompanyID(rs.getInt("companyId"));
				//item.setItemSizeId(rs.getInt("itemSizeId"));
				item.setItemBrandId(rs.getInt("itemBrandId"));
				//item.setSupplierId(rs.getInt("supplierId"));
				//item.setItemMrp(rs.getString("itemMrp"));
				item.setItemMrp(rs.getString("itemUnit"));
				item.setHsncode(rs.getString("hsncode"));
				dtolist.add(item);
			}
			System.out.println("list size= "+dtolist.size());
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				pstat.close();
				con.close();
			}
		}
		return dtolist;
	}
	
	@Override
	public ItemCategoryTypeMasterDTO getItemByCompanyIdItemId(String itemId) throws DaoException, SQLException {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		ItemCategoryTypeMasterDTO item = new ItemCategoryTypeMasterDTO();
		Connection con = null;
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT * FROM itemmaster where companyId=? and itemId=?";
			PreparedStatement pstat = con.prepareStatement(query);
			pstat.setInt(1, Integer.parseInt(cid));
			pstat.setString(2, itemId);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				
				String query1 = "SELECT i.itemType,u.userName, b.brandName FROM itemtype i, usermaster u, brandmaster b, itemmaster it where u.userID=it.userid and b.companyId=it.companyId and b.brandId=it.itemBrandId and i.itemtypeId=it.itemTypeId and it.companyId=? and it.userid=? and it.itemId=?";
				PreparedStatement pstat1 = con.prepareStatement(query1);
				pstat1.setInt(1, Integer.parseInt(cid));
				pstat1.setInt(2, Integer.parseInt(uid));
				pstat1.setString(3, itemId);
				ResultSet rs1 = pstat1.executeQuery();
				if(rs1.next()){
					item.setItemType(rs1.getString("itemType"));
					item.setUserName(rs1.getString("userName"));
					item.setItemBrandName(rs1.getString("brandName"));
					//item.setSupplierName(rs1.getString("SUPPLIERNAME")); 		// modify this along with above sql to get/set supplier from supplier table
					//item.setItemSizeName(rs1.getString("sizeName"));
				}
				
				item.setItemId(rs.getString("itemId"));
				item.setItemName(rs.getString("itemName"));
				item.setItemDateofcreation(rs.getString("dateofcreation"));
				item.setItemTypeID(rs.getInt("itemTypeId"));
				item.setUserID(rs.getInt("userId"));
				item.setCompanyID(rs.getInt("companyId"));
				//item.setItemSizeId(rs.getInt("itemSizeId"));
				item.setItemBrandId(rs.getInt("itemBrandId"));
				//item.setSupplierId(rs.getInt("supplierId"));
				item.setItemMrp(rs.getString("itemMrp"));
				item.setItemUnit(rs.getString("itemUnit"));
				item.setHsncode(rs.getString("hsncode"));
				
			}
			System.out.println(item.getItemName());
			System.out.println(item.getItemType());
			System.out.println(item.getUserName());
			//System.out.println(item.getSupplierName());
			System.out.println(item.getItemBrandName());
			//System.out.println(item.getItemSizeName());
			System.out.println(item.getItemUnit());
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
		return item;
	}
	
	@Override
	public String getLastItemCode() throws DaoException, SQLException {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		
		Connection con = null;
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		String itemId="";
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT itemId FROM itemmaster WHERE userId=? AND companyId=? AND productId=(SELECT MAX(productId) FROM itemmaster WHERE userId=? AND companyId=?);";
			PreparedStatement pstat = con.prepareStatement(query);
			pstat.setInt(1, Integer.parseInt(uid));
			pstat.setInt(2, Integer.parseInt(cid));
			pstat.setInt(3, Integer.parseInt(uid));
			pstat.setInt(4, Integer.parseInt(cid));
			ResultSet rs = pstat.executeQuery();
			if(rs.next()) {
				itemId=rs.getString(1);
				System.out.println("itemid= "+itemId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
		return itemId;
	}
	
	@Override
	public boolean editItemDetailsById(ItemCategoryTypeMasterDTO item) throws DaoException, SQLException {

		boolean result=false;
		Connection con=null;
		int x=0;
		System.out.println("item name= "+item.getItemName());
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		try{
			con = ConnectCore.getConnection();
			//String query="update itemmaster set itemname=?, createdBy=?, dateofcreation=?, itemtypeId=?, userId=?, companyId=?, itemSizeId=?, itemBrandId=?, supplierId=?, itemMrp=?, itemUnit=?, barcode=?  where itemId=?";
			String query="update itemmaster set itemname=?, createdBy=?, dateofcreation=?, itemtypeId=?, userId=?, companyId=?, itemSizeId=?, itemBrandId=?, supplierId=?, itemUnit=?,hsncode=?  where itemId=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, item.getItemName());
			ps.setInt(2, Integer.parseInt(uid));
			ps.setString(3, CurrentDate.getDate());
			ps.setInt(4, item.getItemTypeID());
			ps.setInt(5, Integer.parseInt(uid));
			ps.setInt(6, Integer.parseInt(cid));
			//ps.setInt(7, item.getItemSizeId());
			ps.setString(7, "0");
			ps.setInt(8, item.getItemBrandId());
			//ps.setInt(9, item.getSupplierId());
			ps.setString(9, "0");
			//ps.setString(10, item.getItemMrp());
			
			//ps.setString(10, "0");
			
			ps.setString(10, item.getItemUnit());
			ps.setString(11, item.getHsncode());
			
			//ps.setString(12, "NA");
			
			ps.setString(12, item.getItemId());
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
			con.close();
		}
		return result;
	}
	//___________________________________ Item : End ____________________________________________________________________
	
	
	//___________________________________ Brand: Start ____________________________________________________________________
	@Override
	public boolean saveBrandDetails(ItemCategoryTypeMasterDTO brand) throws DaoException, SQLException {
		boolean result=false;
		Connection con=null;
		int x=0;
		System.out.println("brand name= "+brand.getItemBrandName());
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		try{
			con = ConnectCore.getConnection();
			String query="insert into brandmaster values(default,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setString(1, brand.getItemBrandName());
			ps.setString(2, CurrentDate.getDate());
			ps.setInt(3, Integer.parseInt(cid));
			ps.setInt(4, Integer.parseInt(uid));
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
			con.close();
		}
		return result;
	}

	@Override
	public List<ItemCategoryTypeMasterDTO> getAllBrandByCompanyId() throws DaoException, SQLException {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		List<ItemCategoryTypeMasterDTO> dtolist = new LinkedList<ItemCategoryTypeMasterDTO>();
		PreparedStatement pstat=null;
		Connection con = null;
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT * FROM brandmaster where companyId=?";
			pstat = con.prepareStatement(query);
			pstat.setInt(1, Integer.parseInt(cid));
			ResultSet rs = pstat.executeQuery();
			
			while (rs.next()) {
				ItemCategoryTypeMasterDTO brand = new ItemCategoryTypeMasterDTO();
				brand.setItemBrandId(rs.getInt("brandId"));
				brand.setItemBrandName(rs.getString("brandName"));
				brand.setCompanyID(rs.getInt("companyId"));
				brand.setUserID(rs.getInt("userId"));
				brand.setItemBrandDOC(rs.getString("dateOfCreation"));
				dtolist.add(brand);
			}
			rs.close();
			System.out.println("list size= "+dtolist.size());
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				pstat.close();
				con.close();
			}
		}
		return dtolist;
	}

	@Override
	public ItemCategoryTypeMasterDTO getItemBrandByBrandId(int ibId){
	HttpServletRequest request=ServletActionContext.getRequest();
	HttpSession session=request.getSession();
	ItemCategoryTypeMasterDTO brand = new ItemCategoryTypeMasterDTO();
	Connection con = null;
	String cid=(String)session.getAttribute("companyId");
	cid=cid.trim();
	System.out.println("ibId= "+ibId);
	try {
		con = ConnectCore.getConnection();
		String query = "SELECT * FROM brandmaster where companyId=? and brandId=?";
		PreparedStatement pstat = con.prepareStatement(query);
		pstat.setInt(1, Integer.parseInt(cid));
		pstat.setInt(2, ibId);
		System.out.println(pstat);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			
			brand.setItemBrandId(rs.getInt("brandId"));
			brand.setItemBrandName(rs.getString("brandName"));
			brand.setCompanyID(rs.getInt("companyId"));
			brand.setUserID(rs.getInt("userId"));
			brand.setItemBrandDOC(rs.getString("dateOfCreation"));
			
			System.out.println("barnd name= "+brand.getItemBrandName());
		}
		
		log.info("Details taken sucessfully !!");
	} catch (Exception e) {
		e.printStackTrace();
		log.error("failed to retreive data from database", e);
	} finally {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	return brand;
	}

	@Override
	public Boolean editBrandDetailsById(ItemCategoryTypeMasterDTO brand){
		boolean result=false;
		Connection con=null;
		int x=0;
		System.out.println("brand name= "+brand.getItemBrandName());
		System.out.println("brand name= "+brand.getItemBrandId());
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		try{
			con = ConnectCore.getConnection();
			String query="update brandmaster set brandName=?, dateOfCreation=?, companyId=?, userId=? where brandId=?";
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setString(1, brand.getItemBrandName());
			ps.setString(2, CurrentDate.getDate());
			ps.setInt(3, Integer.parseInt(cid));
			ps.setInt(4, Integer.parseInt(uid));
			ps.setInt(5, brand.getItemBrandId());
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
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	//___________________________________ Brand: End ____________________________________________________________________
	
	@Override
	public boolean saveSizeDetails(ItemCategoryTypeMasterDTO size) throws DaoException, SQLException {
		boolean result=false;
		Connection con=null;
		int x=0;
		System.out.println("size name= "+size.getItemSizeName());
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		try{
			con = ConnectCore.getConnection();
			String query="insert into sizemaster values(default,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setString(1, size.getItemSizeName());
			ps.setString(2, CurrentDate.getDate());
			ps.setInt(3, Integer.parseInt(uid));
			ps.setInt(4, Integer.parseInt(cid));
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
			con.close();
		}
		return result;
	}

	@Override
	public List<ItemCategoryTypeMasterDTO> getAllSizeByCompanyId() throws DaoException, SQLException {
	
		List<ItemCategoryTypeMasterDTO> dtolist = new LinkedList<ItemCategoryTypeMasterDTO>();
		PreparedStatement pstat=null;
		Connection con = null;
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT * FROM sizemaster where companyId=?";
			pstat = con.prepareStatement(query);
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				ItemCategoryTypeMasterDTO size = new ItemCategoryTypeMasterDTO();
				size.setItemSizeId(rs.getInt("sizeId"));
				size.setItemSizeName(rs.getString("sizeName"));
				size.setCompanyID(rs.getInt("companyId"));
				size.setUserID(rs.getInt("userId"));
				size.setItemSizeDOC(rs.getString("sizeDoc"));
				dtolist.add(size);
			}
			rs.close();
			System.out.println("list size= "+dtolist.size());
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				pstat.close();
				con.close();
			}
		}
		return dtolist;
	}
	
	@Override
	public List<ItemCategoryTypeMasterDTO> getAllItemByItemSize() throws DaoException, SQLException {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		List<ItemCategoryTypeMasterDTO> dtolist = new LinkedList<ItemCategoryTypeMasterDTO>();
		Connection con = null;
		PreparedStatement pstat=null;
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT * FROM sizemaster where companyId=?";
		    pstat = con.prepareStatement(query);
			pstat.setInt(1, Integer.parseInt(cid));
			ResultSet rs = pstat.executeQuery();
			
			while (rs.next()) {
				ItemCategoryTypeMasterDTO item = new ItemCategoryTypeMasterDTO();
				item.setItemSizeId(rs.getInt("sizeId"));
				item.setItemSizeName(rs.getString("sizeName"));
				dtolist.add(item);
			}
			System.out.println("list size= "+dtolist.size());
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				pstat.close();
				con.close();
			}
		}
		return dtolist;
	}
	
	@Override
	public ItemCategoryTypeMasterDTO getItemSizeBySizeId(int sizeId){
	HttpServletRequest request=ServletActionContext.getRequest();
	HttpSession session=request.getSession();
	ItemCategoryTypeMasterDTO size = new ItemCategoryTypeMasterDTO();
	Connection con = null;
	String cid=(String)session.getAttribute("companyId");
	cid=cid.trim();
	System.out.println("ibId= "+sizeId);
	try {
		con = ConnectCore.getConnection();
		String query = "SELECT * FROM sizemaster where companyId=? and sizeId=?";
		
		PreparedStatement pstat = con.prepareStatement(query);
		pstat.setInt(1, Integer.parseInt(cid));
		pstat.setInt(2, sizeId);
		
		System.out.println(pstat);
		ResultSet rs = pstat.executeQuery();
		
		while (rs.next()) {
			size.setItemSizeId(rs.getInt("sizeId"));
			size.setItemSizeName(rs.getString("sizeName"));
			size.setCompanyID(rs.getInt("companyId"));
			size.setUserID(rs.getInt("userId"));
			size.setItemSizeDOC(rs.getString("sizeDoc"));
			
			System.out.println("size name= "+size.getItemSizeName());
		}
		
		log.info("Details taken sucessfully !!");
	} catch (Exception e) {
		e.printStackTrace();
		log.error("failed to retreive data from database", e);
	} finally {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	return size;
	}

	@Override
	public Boolean editSizeDetailsById(ItemCategoryTypeMasterDTO size){
		boolean result=false;
		Connection con=null;
		int x=0;
		System.out.println("size name= "+size.getItemSizeName());
		System.out.println("size name= "+size.getItemSizeId());
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		try{
			con = ConnectCore.getConnection();
			String query="update sizemaster set sizeName=?, sizeDoc=?, companyId=?, userId=? where sizeId=?";
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setString(1, size.getItemSizeName());
			ps.setString(2, CurrentDate.getDate());
			ps.setInt(3, Integer.parseInt(cid));
			ps.setInt(4, Integer.parseInt(uid));
			ps.setInt(5, size.getItemSizeId());
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
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public boolean deleteSizeById(int sid) throws DaoException, SQLException {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		boolean returnResult = false;
		Connection con = null;
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		try {
				con = ConnectCore.getConnection();
				PreparedStatement ps = con.prepareStatement("DELETE FROM sizemaster WHERE sizeId=? AND companyId=?");
				ps.setInt(1, sid);
				ps.setInt(2, Integer.parseInt(cid));
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
	
	//______________________________________________ Duplicate Checking Methods ___________________________________________
	
	@Override
	public boolean checkItemDuplicateORNOT(ItemCategoryTypeMasterDTO item) throws DaoException, SQLException {
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
			PreparedStatement ps = con.prepareStatement("SELECT * FROM itemmaster WHERE itemname=? AND createdBy=? AND itemtypeId=? AND userId=? AND companyId=? AND itemBrandId=? AND itemId=? AND itemUnit=?");
			ps.setString(1, item.getItemName());
			ps.setInt(2, Integer.parseInt(uid));
			ps.setInt(3, item.getItemTypeID());
			ps.setInt(4, Integer.parseInt(uid));
			ps.setInt(5, Integer.parseInt(cid));
			ps.setInt(6, item.getItemBrandId());
			ps.setString(7, item.getItemId());
			ps.setString(8, item.getItemUnit());
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnResult=true;
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);

		} finally {
			con.close();
		}

		return returnResult;
	}

	@Override
	public boolean checkItemBrandDuplicateORNOT(ItemCategoryTypeMasterDTO brand) throws DaoException, SQLException {
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
			PreparedStatement ps = con.prepareStatement("select * from brandmaster where brandName=? and companyId=? ");
			ps.setString(1, brand.getItemBrandName());
			ps.setInt(2, Integer.parseInt(cid));
			
			System.out.println(ps);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnResult=true;
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);

		} finally {
			con.close();
		}

		return returnResult;
	}

	@Override
	public boolean checkItemTypeDuplicateORNOT(ItemCategoryTypeMasterDTO iType) throws DaoException, SQLException {
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
			PreparedStatement ps = con.prepareStatement("select * from itemtype where itemType=? and companyId=? ");
			ps.setString(1, iType.getItemType());
			ps.setInt(2, Integer.parseInt(cid));
			
			System.out.println(ps);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnResult=true;
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);

		} finally {
			con.close();
		}

		return returnResult;
	}
	
	@Override
	public boolean checkSizeDuplicateORNOT(ItemCategoryTypeMasterDTO size) throws DaoException, SQLException {
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
			PreparedStatement ps = con.prepareStatement("select * from sizemaster where sizeName=? and companyId=?");
			ps.setString(1, size.getItemSizeName());
			ps.setInt(2, Integer.parseInt(cid));
			
			System.out.println(ps);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnResult=true;
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);

		} finally {
			con.close();
		}

		return returnResult;
	}
	
	public int getSizeofTotalItems() throws DaoException, SQLException
	{
		
		int size=0;
		Connection con=null;
		List<ItemCategoryTypeMasterDTO> list=new ArrayList<ItemCategoryTypeMasterDTO>();
            try {
			con = ConnectCore.getConnection();
			String query = "SELECT DISTINCT itemname from itemmaster where companyId=?";
			PreparedStatement pstat = con.prepareStatement(query);
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				ItemCategoryTypeMasterDTO dto = new ItemCategoryTypeMasterDTO();
				dto.setItemName(rs.getString("itemname"));
				list.add(dto);
			}
			size=list.size();
			System.out.println("list size= "+list.size());
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
	public List<ItemCategoryTypeMasterDTO> getBarcodeList(String fromdate, String todate) throws DaoException, SQLException {
		List<ItemCategoryTypeMasterDTO> itemList=new ArrayList<ItemCategoryTypeMasterDTO>();
		Connection con=null;
		String query="";
            try {
			con = ConnectCore.getConnection();
			if(fromdate=="" || todate=="")
			{
				 query = "SELECT * FROM itemmaster WHERE  companyId=? order by productId desc";
			}
			else
			{
				 query = "SELECT * FROM itemmaster WHERE dateofcreation BETWEEN '"+fromdate+"' AND '"+todate+"' and companyId=? order by productId desc";	
			}
			
			PreparedStatement pstat = con.prepareStatement(query);
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
			ItemCategoryTypeMasterDTO item = new ItemCategoryTypeMasterDTO();
			item.setProductId(rs.getInt("productId"));
			item.setItemId(rs.getString("itemId"));
			item.setItemName(rs.getString("itemName"));
			item.setItemDateofcreation(rs.getString("dateofcreation"));
			item.setItemTypeID(rs.getInt("itemTypeId"));
			item.setUserID(rs.getInt("userId"));
			item.setCompanyID(rs.getInt("companyId"));
			item.setItemBrandId(rs.getInt("itemBrandId"));
			item.setItemMrp(rs.getString("itemUnit"));
			item.setBarcode(rs.getString("barcode"));
			itemList.add(item);
			}
			System.out.println("list size= "+itemList.size());
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
		return itemList;
	}
	
	
	public boolean checkItemIDDuplicate(String id)throws DaoException, SQLException
	{
		
		boolean result=false;
		Connection con=null;
		int p=0;
		String query="";
		 try {
				con = ConnectCore.getConnection();
				 query = "SELECT itemId,productId FROM itemmaster WHERE itemId LIKE ?";
				PreparedStatement pstat = con.prepareStatement(query);
				pstat.setString(1, "%"+id+"%");
				ResultSet rs = pstat.executeQuery();
				while (rs.next()) {
					p=rs.getInt("productId");
					if(p>0)
					{
						result=true;
					}
				}
		 }
				 catch (Exception e) {
						e.printStackTrace();
						log.error("failed to retreive data from database", e);
					} finally {
						if (con != null) {
							con.close();
						}
					}
		
	return result;	
	}
	
}
