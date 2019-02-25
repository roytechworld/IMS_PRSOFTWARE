package com.prsoftwares.daoImpl;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.w3c.dom.CDATASection;

import com.onbarcode.barcode.Code128;
import com.onbarcode.barcode.IBarcode;
import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.CustomerPaymentDao;
import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dao.DaoFactory;
import com.prsoftwares.dao.SalesMasterDao;
import com.prsoftwares.dto.CreditNoteDto;
import com.prsoftwares.dto.CustomerMasterDTO;
import com.prsoftwares.dto.ItemCategoryTypeMasterDTO;
import com.prsoftwares.dto.SalesMasterDto;
import com.prsoftwares.dto.SchemeMasterDto;
import com.prsoftwares.util.CurrentCompAndUser;
import com.prsoftwares.util.CurrentDate;
import com.prsoftwares.util.CurrrentPreviousYear;
import com.prsoftwares.util.EligibleCustomerNames;



public class SalesMasterDaoImpl extends ConnectCore implements SalesMasterDao {
	protected static final Log log = LogFactory .getLog(SalesMasterDaoImpl.class);

	public List<CustomerMasterDTO> searchCustomerList(String custname)
			throws DaoException, SQLException {
		List<CustomerMasterDTO> cList = new ArrayList<CustomerMasterDTO>();
		Connection con = null;
		PreparedStatement pr=null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String cid = (String) session.getAttribute("companyId");
		System.out.println("cid="+cid);
		try {
			con = ConnectCore.getConnection();
			String Sql = "SELECT * FROM customermaster WHERE status=1 AND cName LIKE ? AND companyid=? AND custType='REGULAR'";
			pr = con.prepareStatement(Sql);
			pr.setString(1, "%" + custname + "%");
			pr.setString(2, cid);
			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				CustomerMasterDTO cto = new CustomerMasterDTO();
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
				cto.setRemarks(rs.getString("remarks"));
				System.out.println("name " + cto.getcName());
				cList.add(cto);
			}

			if(pr!=null)
			{
			pr.close();	
			}
		} catch (Exception e) {
			System.out.println("Ex= " + e);
		} finally {
			if(con!=null)
			{
				con.close();
				System.out.println("connection closed");
			}
		}
		return cList;
	}

	@Override
	public List<CustomerMasterDTO> getAllCustomerFromSales() throws DaoException, SQLException {
		List<CustomerMasterDTO> list = new LinkedList<CustomerMasterDTO>();
		Connection con = null;
		PreparedStatement pstat=null;
		String query;
		String cid="";
		try {
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=null;
			session=request.getSession();
			cid=(String)session.getAttribute("companyId");
			cid=cid.trim();
			query = "SELECT DISTINCT st.regcustomerId,s.cName,s.address,s.custBarcode FROM salemaster ST, customermaster s WHERE s.customerId=st.regcustomerId AND saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND st.companyId=?";
			
			con = ConnectCore.getConnection();
			pstat = con.prepareStatement(query);
			pstat.setString(1, cid);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				CustomerMasterDTO dto = new CustomerMasterDTO();
				dto.setcName(rs.getString("cName"));
				dto.setCustomerId(rs.getString("regcustomerId"));
				dto.setAddress(rs.getString("address"));
				dto.setCustomerBarcode(rs.getString("custBarcode"));
				System.out.println("id= "+dto.getCustomerId());
				list.add(dto);
			}
			System.out.println("list size= "+list.size());
			if(pstat!=null){
				rs.close();
				pstat.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
				
				System.out.println("connection closed");
			}
		}
		return list;
	}
	
	@Override
	public List<SalesMasterDto> getStockItemDetailsByItemCode(
			String articlesCode) throws DaoException, SQLException {
		List<SalesMasterDto> sList = new ArrayList<SalesMasterDto>();
		Connection con = null;
		PreparedStatement pr=null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String cid = (String) session.getAttribute("companyId");
		try {
			con = ConnectCore.getConnection();
			/*String Sql = "SELECT s.stockId, s.qty, s.itemSize, i.itemName FROM stockmaster s, itemmaster i WHERE i.itemId=s.itemIds AND s.qty<>0 AND s.itemIds=? AND s.companyId=?";*/
			String Sql="SELECT SUM(s.qty) as qty, s.itemSize,s.buyRate, i.itemName,i.itemMrp,i.hsncode FROM stockmaster s, itemmaster i WHERE i.itemId=s.itemIds AND s.qty<>0 AND s.itemIds=? AND s.companyId=? GROUP BY s.itemSize, s.buyRate";
			pr = con.prepareStatement(Sql);
			pr.setString(1, articlesCode);
			pr.setString(2, cid);
			System.out.println("ps="+pr);
			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				SalesMasterDto sDto = new SalesMasterDto();

				//sDto.setStockId(rs.getString("stockId"));
				sDto.setQty(rs.getString("qty"));
				sDto.setItemId(articlesCode);
				sDto.setItemSizeName(rs.getString("itemSize"));
				sDto.setItemName(rs.getString("itemName"));
				sDto.setBuyRate(rs.getString("itemMrp"));
				sDto.setHsncode(rs.getString("hsncode"));
				

				System.out.println("name " + sDto.getStockId());

				sList.add(sDto);
			}
        if(pr!=null)
        {
        pr.close();
        }
		} catch (Exception e) {
			System.out.println("Ex= " + e);
		} finally {
			if(con!=null)
			{
				con.close();
				System.out.println("connection closed");
			}
		}
		return sList;
	}

	@Override
	public SalesMasterDto getStockItemDetailsByStockId(String stockId)
			throws DaoException, SQLException {

		SalesMasterDto sDto = new SalesMasterDto();
		Connection con = null;
		PreparedStatement pr=null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String cid = (String) session.getAttribute("companyId");
		try {
			con = ConnectCore.getConnection();
			String Sql = "SELECT * FROM stockmaster WHERE stockId=? AND companyId=?";
			pr = con.prepareStatement(Sql);
			pr.setString(1, stockId);
			pr.setString(2, cid);
			ResultSet rs = pr.executeQuery();

			while (rs.next()) {

				sDto.setItemId(rs.getString("itemIds"));
				sDto.setStockId(rs.getString("stockId"));
				sDto.setQty(rs.getString("qty"));
				sDto.setItemSizeName(rs.getString("itemSize"));
				sDto.setBuyRate(rs.getString("buyRate"));

				System.out.println("name " + sDto.getBuyRate());
			}
			
			if(pr!=null)
			{
			pr.close();	
			}

		} catch (Exception e) {
			System.out.println("Ex= " + e);
		} finally {
			if(con!=null)
			{
				con.close();
				System.out.println("connection closed");
			}
		}
		return sDto;
	}

	// #########################################################################################################################################################################################################################################################
	// ##############################################################################Sales Save main Dao IMPL .. Start here################################################################################
	// #########################################################################################################################################################################################################################################################
	
	public boolean saveSaleDetails(SalesMasterDto sdto, String[] size, String[] qty, String[] srate, String[] tot,
			String[] itemId, String[] brate, String afterdiscount, String amountreceived, String amountreturn,
			String[] hsncode, String[] discount1, String[] discount2, String[] gstrate, String[] gstamt,
			String[] cgstrate, String[] cgstamt, String[] sgstrate, String[] sgstamt, String[] igstrate,
			String[] igstamt,String[] discountamt,String[]basetotal) throws DaoException, SQLException{

		
		String query = "insert into salemaster (saleDoc,itemId,itemSize,stockId,billno,regcustomerId,tempCustName,"
				+ "temCustMobileNo,tempCustAddress,buyRate,saleQty,saleItemtotal,saleSubtotal,"
				+ "saleLessUnit,saleDiscount,saleVat,saleLoryFreight,saleGrandTotal,userId,companyId,paidStatus,"
				+ "tempCustBarcode,tempCustBarcodeURL,CustomerType,crNo,afterdiscount,amountreceived,"
				+ "amountreturn,sdiscount2,sdiscountamt,gstrate,gstamt,cgstrate,cgstamt,sgstrate,sgstamt,igstrate,igstamt,hsno,basetotal,incnumber"
				+ ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
				+ ",?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		
		
		
		String selectStockInfo = "SELECT qty,buyRate,itemTotal FROM stockmaster WHERE stockid=? AND companyId=?";
		String updateStock = "UPDATE stockmaster SET qty=?,itemTotal=? WHERE stockid=? AND companyId=?";
		boolean result = false;
		Connection con = null;
		PreparedStatement ps=null;
		PreparedStatement ps2=null;
		PreparedStatement ps3=null;
		PreparedStatement ps5=null;
		int flagCheckforSalesSave = 0;
		int flagCheckforStockUp = 0;
		int x = 0;
		String barcodeimage="";
		int salesId=getSalesId()+1;
		try {
			con = ConnectCore.getConnection();
			ps = con.prepareStatement(query);
		    con.setAutoCommit(false);
	
			for (int i = 0; i < itemId.length; i++) 
			{
				ps.setString(1, sdto.getDoc());
				ps.setString(2, itemId[i]);
				ps.setString(3, size[i]);
				ps.setString(4, "0");
				ps.setString(5, sdto.getBillno());
				ps.setString(6, sdto.getRegcustomerId());
				ps.setString(7, sdto.getTempCustName());
				ps.setString(8, sdto.getTemCustMobileNo());
				ps.setString(9, sdto.getTempCustAddress());
				ps.setString(10, srate[i]);
				ps.setString(11, qty[i]);
				ps.setString(12, tot[i]);
				ps.setString(13, sdto.getSubtotal());//13
				ps.setString(14, sdto.getLessUnit());
				System.out.println("discount found..."+sdto.getLessDiscount());
				ps.setString(15, sdto.getLessDiscount());
				ps.setString(16, sdto.getVat());
				ps.setString(17, sdto.getLessloryFreight());
				ps.setString(18, sdto.getGrandTotal());
				ps.setString(19, CurrentCompAndUser.getCurrentUserID());
				ps.setString(20, CurrentCompAndUser.getCurrentCompany());//7
				ps.setString(21, "NA");
//				String custBarcode=sdto.getTempCustName()+" T"+salesId;
//				barcodeimage=generatetBarcodes(custBarcode);
				ps.setString(22, sdto.getTempCustBarcode());
				ps.setString(23, sdto.getTempCustBarcodeUrl());
				ps.setString(24, sdto.getCustomerType());
				ps.setString(25, "NA");//5
				ps.setString(26,  afterdiscount);//5
				ps.setString(27, amountreceived);
				ps.setString(28, amountreturn);
				ps.setString(29, discount2[i]);
				ps.setString(30, discountamt[i]);
				ps.setString(31, gstrate[i]);
				ps.setString(32, gstamt[i]);
				ps.setString(33, cgstrate[i]);
				ps.setString(34, cgstamt[i]);
				ps.setString(35, sgstrate[i]);
				ps.setString(36, sgstamt[i]);
				ps.setString(37, igstrate[i]);
				ps.setString(38, igstamt[i]);
				ps.setString(39, hsncode[i]);
				ps.setString(40, basetotal[i]);
				ps.setString(41, sdto.getIncnum()+"");
			
				System.out.println("ps= " + ps);
			    ps.addBatch();
				
			}
			
			

			  int[] xm =ps.executeBatch();
		      System.out.println("The number of rows inserted: "+ xm.length);
		      con.commit();
		      flagCheckforSalesSave=1;
			
			
		
			if (flagCheckforSalesSave > 0) {
			
				for (int k = 0; k < itemId.length; k++) 
				{
					List<Integer> stockIds=new ArrayList<Integer>();
					int qtys=0;
					float itemTotal = 0, finalItemTot = 0, buyRate = 0;
//					ps2 = con.prepareStatement("SELECT stockId FROM stockmaster WHERE itemSize=? AND buyRate=? AND qty<>0 AND itemIds=? AND companyId=?");
					ps2 = con.prepareStatement("SELECT stockId FROM stockmaster WHERE itemSize=?  AND qty<>0 AND itemIds=? AND companyId=?");
					ps2.setString(1, size[k]);
					ps2.setString(2, itemId[k]);
					ps2.setString(3, CurrentCompAndUser.getCurrentCompany());
					System.out.println("ps2= "+ps2);
					ResultSet rs = ps2.executeQuery();
					while (rs.next()) {
						 stockIds.add(rs.getInt(1));
					}
				
					int saleQty=Integer.parseInt(qty[k]);
					for (int i = 0; i < stockIds.size(); i++) 
					{
					PreparedStatement	ps2i = con.prepareStatement(selectStockInfo);
						ps2i.setInt(1, stockIds.get(i).intValue());
						ps2i.setString(2, CurrentCompAndUser.getCurrentCompany());
						
						System.out.println("ps2i= "+ps2i);
						rs = ps2i.executeQuery();
						while (rs.next()) 
						{
							qtys = Integer.parseInt(rs.getString("qty"));
							itemTotal = Float.parseFloat(rs.getString("itemTotal"));
							buyRate = Float.parseFloat(rs.getString("buyRate"));
						}
					
						if(saleQty>=qtys && saleQty!=0){
							saleQty=saleQty-qtys;
							PreparedStatement ps3i= con.prepareStatement(updateStock);
							ps3i.setString(1, "0");
							ps3i.setString(2, "0");
							ps3i.setInt(3, stockIds.get(i).intValue());
							ps3i.setString(4, CurrentCompAndUser.getCurrentCompany());
							System.out.println("up ps= "+ps3);
							
							int up = ps3i.executeUpdate();
							if (up > 0) {
								flagCheckforStockUp=1;
							}
							
						   }else if(qtys>saleQty && saleQty!=0)
						   {
							qtys=qtys-saleQty;
							saleQty=0;
							finalItemTot = buyRate * qtys;
//							itemTotal = itemTotal - finalItemTot;
							
							itemTotal = finalItemTot;
							
							PreparedStatement ps3i = con.prepareStatement(updateStock);
							ps3i.setInt(1, qtys);
							ps3i.setFloat(2, itemTotal);
							ps3i.setInt(3, stockIds.get(i).intValue());
							ps3i.setString(4, CurrentCompAndUser.getCurrentCompany());
							System.out.println("up ps= "+ps3);
							
							int up = ps3i.executeUpdate();
							 con.commit();
							if (up > 0) {
								flagCheckforStockUp=1;
							}
							
						}
						
					}
				}
				if (flagCheckforStockUp>0) {
					System.out.println("Stock Updated Successfully!");
					
					
					
					
					
					
					 if(sdto.getDueStatus().equals("Due"))
					 { 
						sdto.setcPaidStatus("NA");
					 }
					 else
					 {
					    sdto.setcPaidStatus("PAID");	 
					 }
					 
					 
					 if(sdto.getDueStatus().equals("Due"))
					 {
						sdto.setcPaidAmt("0.00"); 
					 }
					 else
					 {
						sdto.setcPaidAmt(sdto.getGrandTotal());  
					 }
					 
					 
					 
					if(sdto.getDueStatus().equals("Due"))
					{
						sdto.setDueAmt(sdto.getGrandTotal());	
					}
					else
					{
						sdto.setDueAmt("0.0");	
					}
					 
						
						sdto.setcPayDate(sdto.getDoc());
						sdto.setcPayMode("NA");
						sdto.setChequeBank("NA");
						sdto.setChequeNo("NA");
						DaoFactory dfact=new DaoFactory();
						CustomerPaymentDao cpdao=dfact.createCustomerPayManager();
						if (cpdao.saveCustomerPaymentforSales(sdto)) {
							
							result=true;	
							
							
						}
					
				}
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			if(con!=null)
			{
				con.close();
				System.out.println("connection closed");
			}
		}
		return result;
	}

	
	public int getSalesId() throws DaoException,SQLException
	{
		int salesId=0;
		Connection con=null;
		try
		{
		con=ConnectCore.getConnection();
		PreparedStatement pr=con.prepareStatement("select max(salesId) as salesId from salemaster");
		ResultSet rs=pr.executeQuery();
		while(rs.next())
		{
		salesId=rs.getInt("salesId");
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
		return salesId;
	}
	
	public String generatetBarcodes(String custCode)throws DaoException
	{
//		String dir="d:\\SMS\\barcodes";
		String dir="c:\\SMS\\Tomcat7\\webapps\\sms\\img\\tempCustomerBarcode";
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
	             
//	             imageUrl="d://SMS/barcodes/"+productcode+".jpg";
	             imageUrl="c://SMS/Tomcat7/webapps/sms/img/tempCustomerBarcode/"+custCode+".jpg";
	             
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
	             
	 			imageUrl="/img/tempCustomerBarcode/"+custCode+".jpg";
	             
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
	// #########################################################################################################################################################################################################################################################
	// ##############################################################################Sales Save main Dao IMPL .. End here#####################################################################################################################################
	// #########################################################################################################################################################################################################################################################

	@Override
	public List<SalesMasterDto> getAllSalesDetails() throws DaoException,
	SQLException {
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = null;
		PreparedStatement pstat=null;
		String query;
		String cid = "";
		double sumGrandtotal=0.0;
		try {
			con = ConnectCore.getConnection();
			session = request.getSession();
			cid = (String) session.getAttribute("companyId");
//			query = "SELECT DISTINCT s.billno ,DATE_FORMAT(s.saleDoc,'%d-%m-%Y ') AS saleDoc , s.regcustomerId, c.cName,s.CustomerType,s.saleGrandTotal  FROM salemaster s, customermaster c WHERE c.customerId=s.regcustomerId AND s.companyId=? AND s.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'  ";
			query = "SELECT DISTINCT s.billno , saleDoc , s.regcustomerId, c.cName,s.CustomerType,s.saleGrandTotal  FROM salemaster s, customermaster c WHERE c.customerId=s.regcustomerId AND s.companyId=? AND s.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'  ";
			
			pstat = con.prepareStatement(query);
			pstat.setString(1, cid);
			ResultSet rs = pstat.executeQuery();
			
			DaoFactory daoFactory=new DaoFactory();
			SalesMasterDao sdao=daoFactory.createSalesMasterManager();
			
			while (rs.next()) {
				SalesMasterDto dto = new SalesMasterDto();
				CreditNoteDto cdto=new CreditNoteDto();
				dto.setBillno(rs.getString("billno"));
				dto.setDoc(rs.getString("saleDoc"));
				dto.setRegcustomerId(rs.getString("regcustomerId"));
				dto.setCustomerName(rs.getString("cName"));
				dto.setGrandTotal(rs.getString("saleGrandTotal"));
				dto.setCustomerType(rs.getString("CustomerType"));
				sumGrandtotal=sumGrandtotal+Double.parseDouble(rs.getString("saleGrandTotal"));
				System.out.println("Sum of Grand Total="+sumGrandtotal);
				try
				{
				cdto=sdao.getCreditnoteDetailsOnerecord(dto.getBillno(), con);
				
				if(!cdto.getSaleBillNo().equals(null))
				{
					dto.setCreditnotestatus("C.N");	
					System.out.println("CN found");
				}
				else
				{
					dto.setCreditnotestatus("NO C.N");	
				}
		
				}
				catch(NullPointerException n)
				{
					dto.setCreditnotestatus("NO C.N");	
				}
				
				list.add(dto);
			}
			if(pstat!=null)
			{
			pstat.close();	
			}
			System.out.println("list size= " + list.size());
		
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if(con!=null)
			{
				con.close();
				
				System.out.println("connection closed");
			}
		}
		return list;
	}

         	
	public List<SalesMasterDto> getAllSalesDetailsByMAXBILLNO(String billno) throws DaoException, SQLException {
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
		DecimalFormat df = new DecimalFormat("#.00");
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=null;
		PreparedStatement pstat=null;
		PreparedStatement ps=null;
		String query;
		String cid="";
		String taxableamount="";
		session=request.getSession();
		cid=(String)session.getAttribute("companyId");
		try {
				con = ConnectCore.getConnection();	
	    
			//	query= "SELECT st.salesId, st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal,st.itemSize,st.saleDoc,st.billno, st.tempCustName,c.cName FROM salemaster st, itemmaster i,customermaster c WHERE i.itemId=st.itemId AND st.companyId=? AND st.regcustomerId=c.customerId AND st.billno LIKE '"+billno+"'";   
			    query= "SELECT st.salesId, st.itemId,i.itemname,st.buyRate,SUM(st.saleQty) AS saleQty ,SUM(st.saleItemtotal) AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.phoneno ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=? AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.billno = '"+billno+"' GROUP BY itemSize,itemname";	

				pstat = con.prepareStatement(query);
				pstat.setString(1, cid);
				System.out.println("ps="+pstat);
				ResultSet rs = pstat.executeQuery();
				
				while (rs.next()) {
					SalesMasterDto dto = new SalesMasterDto();
					dto.setBillno(billno);
					dto.setDoc(rs.getString("saleDoc"));
					dto.setItemName(rs.getString("itemname"));
					dto.setQty(rs.getString("saleQty"));
					dto.setItemtotal(df.format(rs.getDouble("saleItemTotal"))+"");
					dto.setBuyRate(rs.getString("buyRate"));
					dto.setItemSizeName(rs.getString("itemSize"));
					dto.setItemId(rs.getString("itemId"));
					dto.setSalesId(rs.getString("salesId"));
					
					dto.setRegcustomerId(rs.getString("regcustomerId"));
					dto.setCustaddress(rs.getString("address"));
					dto.setCustState(rs.getString("state"));
					dto.setCustpincode(rs.getString("pincode"));
					dto.setCustemail(rs.getString("email"));
										
					dto.setCompanyName(rs.getString("nameofcompany"));
					dto.setCompanyPhonenumber(rs.getString("CompanyPhone"));
					dto.setCompanyemail(rs.getString("CompanyEmail"));
					dto.setCompanyState("West Bengal");
					dto.setCompanyAddress(rs.getString("CompanyAddress"));
					
					dto.setSubtotal(rs.getString("saleSubtotal"));
					System.out.println(rs.getString("saleDiscount"));
					
					dto.setGrandTotal(rs.getString("saleGrandTotal"));
					dto.setLessDiscount(rs.getString("saleDiscount"));
					dto.setLessloryFreight(rs.getString("saleLoryFreight"));
					dto.setLessUnit(rs.getString("saleLessUnit"));
					dto.setVat(df.format(rs.getDouble("saleVat")));
					
					dto.setGrandTotRoundvalue(rs.getInt("saleGrandTotal"));
					dto.setGrandTotRoundvalueinDouble(rs.getDouble("saleGrandTotal"));

					double vat=rs.getDouble("saleVat")/100;
					double value2=(rs.getDouble("saleSubtotal")-(rs.getDouble("saleLessUnit")+rs.getDouble("saleDiscount")));
					double taxamount=value2*vat;
					dto.setTaxamount(Double.parseDouble(df.format(taxamount)));
					dto.setCustomerType(rs.getString("CustomerType"));
					dto.setCompanyVatNo(rs.getString("vatno"));
					dto.setCustomerPhoneno(rs.getString("phoneno"));
				
					System.out.println("date formte---"+dto.getDoc());
					try{
						String tempCust=rs.getString("tempCustName");
						if(tempCust.equals(""))
						{
							dto.setCustomerName(rs.getString("cName"));	
						}
						else
						{
							dto.setCustomerName(rs.getString("tempCustName"));	
						}
					}
					catch(Exception f)
					{
						dto.setCustomerName(rs.getString("cName"));	
					}
					list.add(dto);
			        }
			
			
				if(pstat!=null)
				{
					pstat.close();
				}
			System.out.println("list size= "+list.size());
			if(list.size()==0){

				query= "SELECT st.salesId, st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,temCustMobileNo,st.tempCustAddress,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail FROM salemaster st, itemmaster i,companymaster co WHERE i.itemId=st.itemId AND st.companyId=? AND st.companyId=co.companyId AND st.billno ='"+billno+"'";	
				
				ps = con.prepareStatement(query);
				ps.setString(1, cid);
				System.out.println("ps="+ps);
				ResultSet rs1 = ps.executeQuery();
				
				while (rs1.next()) {
					SalesMasterDto dto = new SalesMasterDto();
					dto.setBillno(billno);
					dto.setDoc(rs1.getString("saleDoc"));
					dto.setItemName(rs1.getString("itemname"));
					dto.setQty(rs1.getString("saleQty"));
					dto.setItemtotal(rs1.getString("saleItemTotal"));
					dto.setBuyRate(rs1.getString("buyRate"));
					dto.setItemSizeName(rs1.getString("itemSize"));
					dto.setItemId(rs1.getString("itemId"));
					dto.setSalesId(rs1.getString("salesId"));
					
					dto.setRegcustomerId(rs1.getString("regcustomerId"));
					dto.setTempCustName(rs1.getString("tempCustName"));
					dto.setTemCustMobileNo(rs1.getString("temCustMobileNo"));
					dto.setTempCustAddress(rs1.getString("tempCustAddress"));
					
					dto.setCompanyName(rs1.getString("nameofcompany"));
					dto.setCompanyPhonenumber(rs1.getString("CompanyPhone"));
					dto.setCompanyemail(rs1.getString("CompanyEmail"));
					dto.setCompanyState("West Bengal");
					dto.setCompanyAddress(rs1.getString("CompanyAddress"));
					
					dto.setSubtotal(rs1.getString("saleSubtotal"));
					dto.setGrandTotal(rs1.getString("saleGrandTotal"));
					dto.setLessDiscount(rs1.getString("saleDiscount"));
					dto.setLessloryFreight(rs1.getString("saleLoryFreight"));
					dto.setLessUnit(rs1.getString("saleLessUnit"));
					dto.setVat(rs1.getString("saleVat"));
					dto.setGrandTotRoundvalue(rs1.getInt("saleGrandTotal"));
					double vat=rs1.getDouble("saleVat")/100;
					double value2=(rs1.getDouble("saleSubtotal")-(rs1.getDouble("saleLessUnit")+rs1.getDouble("saleDiscount")));
					double taxamount=value2*vat;
					
					dto.setTaxamount(taxamount);
					
					list.add(dto);
				}
				if(ps!=null)
				{
					ps.close();
				}
			}
			
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if(con!=null)
			{
				con.close();
				
				pstat.close();
				System.out.println("connection closed");
			}
		}
		return list;
	}

	@Override
	public List<SalesMasterDto> getAllSalesDetailsByBillNo(String billno,String doc) throws DaoException, SQLException {
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
		DecimalFormat df = new DecimalFormat("#.00");
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=null;
		PreparedStatement pstat=null;
		PreparedStatement ps=null;
		String query;
		String cid="";
		String taxableamount="";
		
	
		
		
		
		session=request.getSession();
		cid=(String)session.getAttribute("companyId");
		try {
			    Date h=CurrentDate.strindtoMysql(doc);
				con = ConnectCore.getConnection();	
				System.out.println("Date found--" +h);
	    
			//query= "SELECT st.salesId, st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal,st.itemSize,st.saleDoc,st.billno, st.tempCustName,c.cName FROM salemaster st, itemmaster i,customermaster c WHERE i.itemId=st.itemId AND st.companyId=? AND st.regcustomerId=c.customerId AND st.billno LIKE '"+billno+"'";   
            //query= "SELECT st.salesId, st.itemId,i.itemname,st.buyRate,SUM(st.saleQty) AS saleQty ,SUM(st.saleItemtotal) AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.phoneno ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=? AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.billno = '"+billno+"' GROUP BY itemSize,itemname";	

				query="SELECT s.amountreceived,"
						+ "s.amountreturn,s.itemId,s.afterdiscount,s.buyRate,s.saleQty,s.saleItemtotal,"
						+ "s.itemSize,s.saleDoc,s.basetotal,s.saleSubtotal,s.saleGrandTotal,s.billno,s.regcustomerId,"
						+ "s.tempCustName,s.saleLessUnit,s.saleDiscount,"
						+ "s.saleVat,s.saleLoryFreight,s.CustomerType,s.sdiscount2,s.sdiscountamt,s.gstrate,s.gstamt,"
						+ "s.cgstrate,s.cgstamt,s.sgstrate,s.sgstamt,s.igstrate,s.igstamt,s.hsno,"
						+ "c.nameofcompany,c.gsttin,cs.gsttin as cstgsttin,cs.stcode,c.phoneno AS CompanyPhone,c.address AS CompanyAddress ,c.email AS CompanyEmail,c.vatno,cs.cName,cs.address,cs.state,cs.pincode,cs.email, cs.phoneno ,DATE_FORMAT(cs.dateofcreate,'%d-%m-%Y ') AS customerDoc "
						+ "FROM salemaster s JOIN companymaster c  JOIN customermaster cs  WHERE s.billno='"+billno+"' AND saleDoc='"+CurrentDate.mysqlDateConvertor(doc)+"' AND s.companyId=c.companyId AND s.regcustomerId=cs.customerId";
				
				pstat = con.prepareStatement(query);
				
				System.out.println("ps="+pstat);
				ResultSet rs = pstat.executeQuery();
				
				while (rs.next()) {
					SalesMasterDto dto = new SalesMasterDto();
					dto.setBillno(billno);
					
					
					dto.setQty(rs.getString("saleQty"));
					dto.setItemtotal(df.format(rs.getDouble("saleItemTotal"))+"");
					dto.setBuyRate(rs.getString("buyRate"));
					dto.setItemSizeName(rs.getString("itemSize"));
					dto.setItemId(rs.getString("itemId"));
//					dto.setSalesId(rs.getString("salesId"));
					
					dto.setSubtotal(rs.getString("saleSubtotal"));
					System.out.println(rs.getString("saleDiscount"));
					dto.setGrandTotal(rs.getString("saleGrandTotal"));
					dto.setLessDiscount(rs.getString("saleDiscount"));
					dto.setLessloryFreight(rs.getString("saleLoryFreight"));
					dto.setLessUnit(rs.getString("saleLessUnit"));
					dto.setVat(df.format(rs.getDouble("saleVat")));
					dto.setGrandTotRoundvalue(rs.getInt("saleGrandTotal"));
					dto.setGrandTotRoundvalueinDouble(rs.getDouble("saleGrandTotal"));
					double vat=rs.getDouble("saleVat")/100;
					double value2=(rs.getDouble("saleSubtotal")-(rs.getDouble("saleLessUnit")+rs.getDouble("saleDiscount")));
					double taxamount=value2*vat;
					dto.setTaxamount(Double.parseDouble(df.format(taxamount)));
					dto.setCustomerType(rs.getString("CustomerType"));
					dto.setRegcustomerId(rs.getString("regcustomerId"));
					
					dto.setItemName(getItemNameByItemId(rs.getString("itemId"), con));
					
					dto.setCompanyName(rs.getString("nameofcompany"));
					dto.setCompanyPhonenumber(rs.getString("CompanyPhone"));
					dto.setCompanyemail(rs.getString("CompanyEmail"));
					dto.setCompanyState("West Bengal");
					dto.setCompanyAddress(rs.getString("CompanyAddress"));
					dto.setCompanyVatNo(rs.getString("vatno"));
					
					dto.setCustaddress(rs.getString("address"));
					dto.setCustState(rs.getString("state"));
					dto.setCustpincode(rs.getString("pincode"));
					dto.setCustemail(rs.getString("email"));
					dto.setCustomerPhoneno(rs.getString("phoneno"));
					
					dto.setAfterdiscount(rs.getString("afterdiscount"));
					dto.setAmountreceived(rs.getString("amountreceived"));
					dto.setAmountreturn(rs.getString("amountreturn"));
					
					dto.setSdiscount2(rs.getString("sdiscount2"));
					dto.setSdiscountamt(rs.getString("sdiscountamt"));
					dto.setGstrate(rs.getString("gstrate"));
					dto.setGstamt(rs.getString("gstamt"));
					dto.setCgstrate(rs.getString("cgstrate"));
					dto.setCgstamt(rs.getString("cgstamt"));
					dto.setSgstrate(rs.getString("sgstrate"));
					dto.setSgstamt(rs.getString("sgstamt"));
					dto.setIgstrate(rs.getString("igstrate"));
					dto.setIgstamt(rs.getString("igstamt"));
					dto.setHsncode(rs.getString("hsno"));
					dto.setGsttin(rs.getString("gsttin"));
					dto.setBasetotal(rs.getString("basetotal"));
					
					System.out.println("date formte---"+dto.getDoc());
					try{
						String tempCust=rs.getString("tempCustName");
						if(tempCust.equals(""))
						{
							dto.setCustomerName(rs.getString("cName"));	
						}
						else
						{
							dto.setCustomerName(rs.getString("tempCustName"));	
						}
					}
					catch(Exception f)
					{
						dto.setCustomerName(rs.getString("cName"));	
					}
					dto.setDoc(rs.getString("saleDoc"));
					dto.setCustgsttin(rs.getString("cstgsttin"));
					dto.setStcode(rs.getString("stcode"));
					
					list.add(dto);
			        }
			
			
			
				
				if(pstat!=null)
				{
					pstat.close();
				}
			System.out.println("list size= "+list.size());
			if(list.size()==0){

				query= "SELECT st.salesId, st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,temCustMobileNo,st.tempCustAddress,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail FROM salemaster st, itemmaster i,companymaster co WHERE i.itemId=st.itemId AND st.companyId=? AND st.companyId=co.companyId AND st.billno ='"+billno+"'";	
				
				ps = con.prepareStatement(query);
				ps.setString(1, cid);
				System.out.println("ps="+ps);
				ResultSet rs1 = ps.executeQuery();
				
				while (rs1.next()) {
					SalesMasterDto dto = new SalesMasterDto();
					dto.setBillno(billno);
					dto.setDoc(rs1.getString("saleDoc"));
					dto.setItemName(rs1.getString("itemname"));
					dto.setQty(rs1.getString("saleQty"));
					dto.setItemtotal(rs1.getString("saleItemTotal"));
					dto.setBuyRate(rs1.getString("buyRate"));
					dto.setItemSizeName(rs1.getString("itemSize"));
					dto.setItemId(rs1.getString("itemId"));
					dto.setSalesId(rs1.getString("salesId"));
					
					dto.setRegcustomerId(rs1.getString("regcustomerId"));
					dto.setTempCustName(rs1.getString("tempCustName"));
					dto.setTemCustMobileNo(rs1.getString("temCustMobileNo"));
					dto.setTempCustAddress(rs1.getString("tempCustAddress"));
					
					dto.setCompanyName(rs1.getString("nameofcompany"));
					dto.setCompanyPhonenumber(rs1.getString("CompanyPhone"));
					dto.setCompanyemail(rs1.getString("CompanyEmail"));
					dto.setCompanyState("West Bengal");
					dto.setCompanyAddress(rs1.getString("CompanyAddress"));
					
					dto.setSubtotal(rs1.getString("saleSubtotal"));
					dto.setGrandTotal(rs1.getString("saleGrandTotal"));
					dto.setLessDiscount(rs1.getString("saleDiscount"));
					dto.setLessloryFreight(rs1.getString("saleLoryFreight"));
					dto.setLessUnit(rs1.getString("saleLessUnit"));
					dto.setVat(rs1.getString("saleVat"));
					dto.setGrandTotRoundvalue(rs1.getInt("saleGrandTotal"));
					double vat=rs1.getDouble("saleVat")/100;
					double value2=(rs1.getDouble("saleSubtotal")-(rs1.getDouble("saleLessUnit")+rs1.getDouble("saleDiscount")));
					double taxamount=value2*vat;
					
					dto.setTaxamount(taxamount);
					
					list.add(dto);
				}
				if(ps!=null)
				{
					ps.close();
				}
			}
			
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if(con!=null)
			{
				con.close();
				
				pstat.close();
				System.out.println("connection closed");
			}
		}
		return list;
	}
	
	public String getItemNameByItemId(String itemId ,Connection cons)throws DaoException,SQLException
	{
		Connection con=null;
		String itemname="";
		try
		{
			con=cons;
			PreparedStatement st=con.prepareStatement("Select itemname from itemmaster where itemId='"+itemId+"'");
			ResultSet rs=st.executeQuery();
			while(rs.next())
			{
			
			itemname=rs.getString("itemname");
				
			}
		}
		catch(Exception e)
		{
			System.out.println("Exceptuion occur"+e);
		}
		
		return itemname;
	}
	
	
	public int getSizeofTotalSales() throws DaoException, SQLException {
		int size = 0;
		Connection con = null;
		PreparedStatement pstat=null;
		List<SalesMasterDto> list = new ArrayList<SalesMasterDto>();
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT DISTINCT billno from salemaster where companyId=? AND saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ";
			pstat = con.prepareStatement(query);
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				SalesMasterDto dto = new SalesMasterDto();
				dto.setBillno(rs.getString("billno"));
				list.add(dto);
			}
			size = list.size();
			System.out.println("list size= " + list.size());
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
	
	
	
	public List<SalesMasterDto> salesDetailsByDate(String fromdate, String todate,String cnstatus) throws DaoException, SQLException {
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
		PreparedStatement pstat=null;
		try {
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=null;
			
			String query;
			String cid="";
			session=request.getSession();
			cid=(String)session.getAttribute("companyId");
			
			DaoFactory daoFactory=new DaoFactory();
			SalesMasterDao sdao=daoFactory.createSalesMasterManager();
			ResultSet rs=null;
			if(cnstatus.equals("No C.N"))
			{
				query= "SELECT DISTINCT s.billno ,s.saleDoc, s.regcustomerId,s.saleGrandTotal, c.cName  FROM salemaster s, customermaster c WHERE c.customerId=s.regcustomerId AND saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"' AND s.companyId=?   ORDER BY  s.salesid DESC";
				con = ConnectCore.getConnection();
				pstat = con.prepareStatement(query);
				pstat.setString(1, cid);
				System.out.println("ps="+pstat);
				rs = pstat.executeQuery();
			}
			else if(cnstatus.equals("C.N"))
			{
				
				query= "SELECT DISTINCT saleBillNo,cnDoc FROM creditnote WHERE cnDoc BETWEEN '"+fromdate+"' AND '"+todate+"'";
				con = ConnectCore.getConnection();
				pstat = con.prepareStatement(query);
				
				System.out.println("ps="+pstat);
				rs = pstat.executeQuery();
				
				
				
			}
			
			
		
			if(cnstatus.equals("No C.N"))
			{
			while (rs.next()) {
				
				if(checkCNorNotForBillNofrmandtodate(rs.getString("billno"), con,fromdate,todate)==false)
				{
					
				SalesMasterDto dto = new SalesMasterDto();
				CreditNoteDto cdto=new CreditNoteDto();
				dto.setBillno(rs.getString("billno"));
				dto.setDoc(rs.getString("saleDoc"));
				dto.setRegcustomerId(rs.getString("regcustomerId"));
				dto.setCustomerName(rs.getString("cName"));
				dto.setGrandTotal(rs.getString("saleGrandTotal"));
				
				try
				{
				cdto=sdao.getCreditnoteDetailsOnerecord(dto.getBillno(), con);
				
				if(!cdto.getSaleBillNo().equals(null))
				{
					dto.setCreditnotestatus("C.N");	
					System.out.println("CN found");
				}
				else
				{
					dto.setCreditnotestatus("NO C.N");	
				}
		
				}
				catch(NullPointerException n)
				{
					dto.setCreditnotestatus("NO C.N");	
				}
				
				list.add(dto);
				
				}
				
			}
			System.out.println("list size= "+list.size());
			
			log.info("Details taken sucessfully !!");
			}
			else if(cnstatus.equals("C.N"))
			{
				
				while (rs.next()) {
					
					if(checkCNorNotForBillNo(rs.getString("saleBillNo"), con)==true)
					{
					    PreparedStatement pstat7=null;
						ResultSet rs7=null;
//						query= "SELECT DISTINCT s.billno ,s.saleDoc, s.regcustomerId,s.saleGrandTotal, c.cName  FROM salemaster s, customermaster c WHERE c.customerId=s.regcustomerId AND s.billno='"+rs.getString("saleBillNo")+"'   AND s.companyId=?   ORDER BY  s.salesid DESC";
						
						
						
					    query="	SELECT DISTINCT s.saleBillNo ,s.cnDoc,s.creditamount  FROM creditnote s WHERE  s.saleBillNo='"+rs.getString("saleBillNo")+"'  AND s.companyId='1'";  
						
						con = ConnectCore.getConnection();
						pstat7 = con.prepareStatement(query);
					
						System.out.println("ps="+pstat7);
						rs7 = pstat7.executeQuery();
						
				      while(rs7.next())
				      {
					SalesMasterDto dto = new SalesMasterDto();
					CreditNoteDto cdto=new CreditNoteDto();
					dto.setBillno(rs7.getString("saleBillNo"));
					dto.setDoc(rs7.getString("cnDoc"));
//					dto.setRegcustomerId(rs7.getString("regcustomerId"));
//					dto.setCustomerName(rs7.getString("cName"));
					
					
					
					dto.setRegcustomerId("1");
					dto.setCustomerName("NA");
					
					
					dto.setGrandTotal(rs7.getString("creditamount"));
				    
					
					try
					{
					cdto=sdao.getCreditnoteDetailsOnerecord(dto.getBillno(), con);
					
					if(!cdto.getSaleBillNo().equals(null))
					{
						dto.setCreditnotestatus("C.N");	
						System.out.println("CN found");
					}
					else
					{
						dto.setCreditnotestatus("NO C.N");	
					}
				
			
					}
					catch(NullPointerException n)
					{
						dto.setCreditnotestatus("NO C.N");	
					}
					
					list.add(dto);	
				      }
				
			}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
				pstat.close();
				System.out.println("connection closed");
			}
		}
		return list;
	}
	
	
	
	
	/*
	
	
	@Override
	public List<SalesMasterDto> salesDetailsByDate(String fromdate, String todate,String cnstatus) throws DaoException, SQLException {
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
		PreparedStatement pstat=null;
		try {
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=null;
			
			String query;
			String cid="";
			session=request.getSession();
			cid=(String)session.getAttribute("companyId");
			
			DaoFactory daoFactory=new DaoFactory();
			SalesMasterDao sdao=daoFactory.createSalesMasterManager();
			query= "SELECT DISTINCT s.billno ,s.saleDoc, s.regcustomerId,s.saleGrandTotal, c.cName  FROM salemaster s, customermaster c WHERE c.customerId=s.regcustomerId AND saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"' AND s.companyId=?   ORDER BY  s.salesid DESC";
			con = ConnectCore.getConnection();
			pstat = con.prepareStatement(query);
			pstat.setString(1, cid);
			System.out.println("ps="+pstat);
			ResultSet rs = pstat.executeQuery();
			if(cnstatus.equals("No C.N"))
			{
			while (rs.next()) {
				
				if(checkCNorNotForBillNo(rs.getString("billno"), con)==false)
				{
					
				SalesMasterDto dto = new SalesMasterDto();
				CreditNoteDto cdto=new CreditNoteDto();
				dto.setBillno(rs.getString("billno"));
				dto.setDoc(rs.getString("saleDoc"));
				dto.setRegcustomerId(rs.getString("regcustomerId"));
				dto.setCustomerName(rs.getString("cName"));
				dto.setGrandTotal(rs.getString("saleGrandTotal"));
				
				try
				{
				cdto=sdao.getCreditnoteDetailsOnerecord(dto.getBillno(), con);
				
				if(!cdto.getSaleBillNo().equals(null))
				{
					dto.setCreditnotestatus("C.N");	
					System.out.println("CN found");
				}
				else
				{
					dto.setCreditnotestatus("NO C.N");	
				}
		
				}
				catch(NullPointerException n)
				{
					dto.setCreditnotestatus("NO C.N");	
				}
				
				list.add(dto);
				
				}
				
			}
			System.out.println("list size= "+list.size());
			
			log.info("Details taken sucessfully !!");
			}
			else if(cnstatus.equals("C.N"))
			{
				
				while (rs.next()) {
					
					
					
					
					
					
					
					if(checkCNorNotForBillNo(rs.getString("billno"), con)==true)
					{
						
					SalesMasterDto dto = new SalesMasterDto();
					CreditNoteDto cdto=new CreditNoteDto();
					dto.setBillno(rs.getString("billno"));
					dto.setDoc(rs.getString("saleDoc"));
					dto.setRegcustomerId(rs.getString("regcustomerId"));
					dto.setCustomerName(rs.getString("cName"));
					dto.setGrandTotal(rs.getString("saleGrandTotal"));
					
					try
					{
					cdto=sdao.getCreditnoteDetailsOnerecord(dto.getBillno(), con);
					
					if(!cdto.getSaleBillNo().equals(null))
					{
						dto.setCreditnotestatus("C.N");	
						System.out.println("CN found");
					}
					else
					{
						dto.setCreditnotestatus("NO C.N");	
					}
			
					}
					catch(NullPointerException n)
					{
						dto.setCreditnotestatus("NO C.N");	
					}
					
					list.add(dto);	
				
			}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
				pstat.close();
				System.out.println("connection closed");
			}
		}
		return list;
	}*/

	@Override
	public List<SalesMasterDto> salesDetailsByCusId(String customerId,String cnstatus) throws DaoException, SQLException {
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
		PreparedStatement pstat=null;
		try {
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=null;
			
			String query;
			String cid="";
			session=request.getSession();
			cid=(String)session.getAttribute("companyId");
			query= "SELECT DISTINCT s.billno ,s.saleDoc, s.regcustomerId,s.saleGrandTotal, c.cName  FROM salemaster s, customermaster c WHERE c.customerId=s.regcustomerId AND s.regcustomerId="+customerId+" AND s.companyId=?   ORDER BY  s.salesid DESC";
			con = ConnectCore.getConnection();
			pstat = con.prepareStatement(query);
			pstat.setString(1, cid);
			System.out.println("ps="+pstat);
			ResultSet rs = pstat.executeQuery();
			
			DaoFactory daoFactory=new DaoFactory();
			SalesMasterDao sdao=daoFactory.createSalesMasterManager();

			if(cnstatus.equals("No C.N"))
			{

			while (rs.next()) {
				
			
				if(checkCNorNotForBillNo(rs.getString("billno"), con)==false)
				{
				
				
				SalesMasterDto dto = new SalesMasterDto();
				CreditNoteDto cdto=new CreditNoteDto();
				dto.setBillno(rs.getString("billno"));
				dto.setDoc(rs.getString("saleDoc"));
				dto.setRegcustomerId(rs.getString("regcustomerId"));
				dto.setCustomerName(rs.getString("cName"));
				dto.setGrandTotal(rs.getString("saleGrandTotal"));
				
				try
				{
				cdto=sdao.getCreditnoteDetailsOnerecord(dto.getBillno(), con);
				
				if(!cdto.getSaleBillNo().equals(null))
				{
					dto.setCreditnotestatus("C.N");	
					System.out.println("CN found");
				}
				else
				{
					dto.setCreditnotestatus("NO C.N");	
				}
		
				}
				catch(NullPointerException n)
				{
					dto.setCreditnotestatus("NO C.N");	
				}
				
				list.add(dto);
				}
			}
			}
			else if(cnstatus.equals("C.N"))
			{
				while (rs.next()) 
				{
					SalesMasterDto dto = new SalesMasterDto();
					CreditNoteDto cdto=new CreditNoteDto();
					dto.setBillno(rs.getString("billno"));
					dto.setDoc(rs.getString("saleDoc"));
					dto.setRegcustomerId(rs.getString("regcustomerId"));
					dto.setCustomerName(rs.getString("cName"));
					dto.setGrandTotal(rs.getString("saleGrandTotal"));
					
					try
					{
					cdto=sdao.getCreditnoteDetailsOnerecord(dto.getBillno(), con);
					
					if(!cdto.getSaleBillNo().equals(null))
					{
						dto.setCreditnotestatus("C.N");	
						System.out.println("CN found");
					}
					else
					{
						dto.setCreditnotestatus("NO C.N");	
					}
			
					}
					catch(NullPointerException n)
					{
						dto.setCreditnotestatus("NO C.N");	
					}
					
					list.add(dto);	
			}
			System.out.println("list size= "+list.size());
			
			log.info("Details taken sucessfully !!");
		} }catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
				pstat.close();
				System.out.println("connection closed");
			}
		}
		return list;
	}

	@Override
	public List<SalesMasterDto> salesDetailsByDateAndCusId(String customerId,String fromdate, String todate) throws DaoException, SQLException {
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
		PreparedStatement pstat=null;
		try {
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=null;
			
			String query;
			String cid="";
			session=request.getSession();
			cid=(String)session.getAttribute("companyId");
			
			
			String CNstatus="No Cn";
			
			if(CNstatus.equals("No Cn"))
			{
				
				
				

			query= "SELECT DISTINCT s.billno ,s.saleDoc, s.regcustomerId,s.saleGrandTotal, c.cName  FROM salemaster s, customermaster c WHERE c.customerId=s.regcustomerId AND saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"' AND s.regcustomerId="+customerId+" AND s.companyId=?   ORDER BY  s.salesid DESC";
			con = ConnectCore.getConnection();
			pstat = con.prepareStatement(query);
			pstat.setString(1, cid);
			System.out.println("ps="+pstat);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {

			   if(checkCNorNotForBillNo(rs.getString("billno"), con)==false)
				{
				SalesMasterDto dto = new SalesMasterDto();
				dto.setBillno(rs.getString("billno"));
				dto.setDoc(rs.getString("saleDoc"));
				dto.setRegcustomerId(rs.getString("regcustomerId"));
				dto.setCustomerName(rs.getString("cName"));
				dto.setGrandTotal(rs.getString("saleGrandTotal"));
				list.add(dto);
				}
			}
			System.out.println("list size= "+list.size());
			log.info("Details taken sucessfully !!");
			}
			else	
			{
				
				
				
				
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
				pstat.close();
				System.out.println("connection closed");
			}
		}
		return list;
	}
	
	
	public boolean checkCNorNotForBillNo(String billno,Connection con)throws DaoException ,SQLException
	{
		boolean rst=false;
		
		try
		{
			PreparedStatement ps=con.prepareStatement("Select saleBillNo from creditnote where saleBillNo='"+billno+"'");
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				rst=true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("exception occur"+e);
		}
		return rst;
	}
	
	public boolean checkCNorNotForBillNofrmandtodate(String billno,Connection con,String fromdate,String todate)throws DaoException ,SQLException
	{
		boolean rst=false;
		
		try
		{
			PreparedStatement ps=con.prepareStatement("Select saleBillNo from creditnote where saleBillNo='"+billno+"' and cnDoc between '"+fromdate+"' and '"+todate+"'");
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				rst=true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("exception occur"+e);
		}
		return rst;
	}
	
	
	@Override
	public List<SalesMasterDto> gettmpCusBuyDetailsByBarcode(String tempCustBarcode) throws DaoException {
		List<SalesMasterDto> list=new LinkedList<SalesMasterDto>();
		Connection con = null;
		PreparedStatement pr=null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String cid = (String) session.getAttribute("companyId");
		try {
			con = ConnectCore.getConnection();
			String Sql = "SELECT s.salesId,s.saleDoc,s.itemId,i.itemname,s.itemSize,s.billno,s.tempCustName,s.temCustMobileNo,s.tempCustAddress,"+
						"s.tempCustBarcode,s.tempCustBarcodeURL,s.buyRate,s.saleQty,s.saleItemtotal,s.saleSubtotal,"+
						"s.saleLessUnit,s.saleDiscount,s.saleVat,s.saleLoryFreight,s.saleGrandTotal,s.userId"+
						" FROM salemaster s, itemmaster i WHERE i.itemId=s.itemId AND s.tempCustBarcode=? AND s.companyId=?";
			pr = con.prepareStatement(Sql);
			pr.setString(1, tempCustBarcode);
			pr.setString(2, cid.trim());
			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				SalesMasterDto dto = new SalesMasterDto();
				
				dto.setSalesId(rs.getString("salesId"));
				dto.setBillno(rs.getString("billno"));
				dto.setDoc(rs.getString("saleDoc"));
				dto.setItemName(rs.getString("itemname"));
				dto.setQty(rs.getString("saleQty"));
				dto.setItemtotal(rs.getString("saleItemTotal"));
				dto.setBuyRate(rs.getString("buyRate"));
				dto.setItemSizeName(rs.getString("itemSize"));
				dto.setItemId(rs.getString("itemId"));
				
				dto.setTempCustName(rs.getString("tempCustName"));
				dto.setTemCustMobileNo(rs.getString("temCustMobileNo"));
				dto.setTempCustAddress(rs.getString("tempCustAddress"));
				dto.setTempCustBarcode(rs.getString("tempCustBarcode"));
				dto.setTempCustBarcodeUrl(rs.getString("tempCustBarcodeURL"));
				
				dto.setSubtotal(rs.getString("saleSubtotal"));
				dto.setGrandTotal(rs.getString("saleGrandTotal"));
				dto.setLessDiscount(rs.getString("saleDiscount"));
				dto.setLessloryFreight(rs.getString("saleLoryFreight"));
				dto.setLessUnit(rs.getString("saleLessUnit"));
				dto.setVat(rs.getString("saleVat"));
				dto.setGrandTotRoundvalue(rs.getInt("saleGrandTotal"));
				double vat=rs.getDouble("saleVat")/100;
				double value2=(rs.getDouble("saleSubtotal")-(rs.getDouble("saleLessUnit")+rs.getDouble("saleDiscount")));
				double taxamount=value2*vat;
				
				dto.setTaxamount(taxamount);

				System.out.println("name " + dto.getBuyRate());
				list.add(dto);
			}
			
			if(pr!=null)
			{
				rs.close();
				pr.close();	
			}

		} catch (Exception e) {
			System.out.println("Ex= " + e);
		} finally {
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("connection closed");
			}
		}
		return list;
	}

		// #########################################################################################################################################################################
	// ################################################# Eligible Customer Selection method code Calculation -- Start here ######################################################################
	// #########################################################################################################################################################################

	public List<EligibleCustomerNames> retriveEligibleCustomer(String schemeName)
			throws DaoException, SQLException {
		SchemeMasterDto sdto = new SchemeMasterDto();
		List<SalesMasterDto> saleList = new ArrayList<SalesMasterDto>();
		List<CustomerMasterDTO> clist = new ArrayList<CustomerMasterDTO>();
		List<EligibleCustomerNames> eligibleCustTList = new ArrayList<EligibleCustomerNames>();
		int flagCheck1 = 0;
		Connection con = null;
		PreparedStatement ps=null;
		PreparedStatement ps2=null;
		PreparedStatement ps3=null;
		try {
			con = ConnectCore.getConnection();
			;
			String sql = "select * from schememaster where schemeName='" + schemeName + "' And companyId=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1,Integer.parseInt(CurrentCompAndUser.getCurrentCompany()));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				sdto.setSchemeName(rs.getString("schemeName"));
				sdto.setSchemeDoc(rs.getString("schemeDoc"));
				sdto.setSchemeSdate(rs.getString("schemeSdate"));
				sdto.setSchemeEdate(rs.getString("schemeEdate"));
				sdto.setSchemeQty(rs.getInt("schemeQty"));
				sdto.setOldSchemeName(rs.getString("schemeName"));
				flagCheck1 = 1;
			}
			if(ps!=null)
			{
			ps.close();	
			}
			if (flagCheck1 > 0) {
			con = ConnectCore.getConnection();
			String sql2 = "SELECT s.salesId,s.saleDoc,s.regcustomerId,c.cName,s.tempCustName FROM salemaster s JOIN customermaster c ON s.regcustomerId=c.customerId where s.saleDoc BETWEEN '"
			+ sdto.getSchemeSdate()
			+ "' AND '"
			+ sdto.getSchemeEdate()
			+ "' AND s.companyId=? GROUP BY c.cName";
			ps2 = con.prepareStatement(sql2);
			ps2.setInt(1, Integer.parseInt(CurrentCompAndUser.getCurrentCompany()));
			ResultSet rs2 = ps2.executeQuery();
			while (rs2.next()) {
					SalesMasterDto dto = new SalesMasterDto();
					dto.setSalesId(rs2.getString("salesId"));
					dto.setSalesId(rs2.getString("saleDoc"));
					dto.setRegcustomerId(rs2.getString("regcustomerId"));
					dto.setCustomerName(rs2.getString("cName"));
					dto.setTempCustName(rs2.getString("tempCustName"));
					saleList.add(dto);
				}
			if(ps2!=null)
			{
				ps2.close();
			}
			if (saleList.size() > 0) {
			con = ConnectCore.getConnection();
			String sql3 = "SELECT DISTINCT s.customerId,c.cName FROM schememaster s JOIN customermaster c ON s.customerId=c.customerId WHERE s.schemeName='"
			+ schemeName + "' AND s.companyId=?";
			ps3 = con.prepareStatement(sql3);
			ps3.setInt(1, Integer.parseInt(CurrentCompAndUser.getCurrentCompany()));
			ResultSet res10 = ps3.executeQuery();
			while (res10.next()) {
			System.out.println("found..."+ res10.getString("cName"));
			CustomerMasterDTO cdto = new CustomerMasterDTO();
			cdto.setcName(res10.getString("cName"));
			clist.add(cdto);
					}
			if(ps3!=null)
			{
				ps3.close();
			}
					for (int i = 0; i < saleList.size(); i++) {
						for (int j = 0; j < clist.size(); j++) {
							String cstSale = saleList.get(i).getCustomerName();
							String schemeCustList = clist.get(j).getcName();
							if (cstSale.equals(schemeCustList)) {
								EligibleCustomerNames elcName = new EligibleCustomerNames();
								elcName.setCustomerNames(saleList.get(i).getCustomerName());
								eligibleCustTList.add(elcName);
							}
						}
					}
				}
				System.out.println("doc= " + sdto.getSchemeDoc());
			}
		} catch (Exception e) {
			System.out.println("Excep= " + e);
		} finally {
			if(con!=null)
			{
				con.close();
				System.out.println("connection closed");
			}
		}
		return eligibleCustTList;
	}

	public List<EligibleCustomerNames> calculateEligiblityWithSchemeQuantity(String customername, String schemename, String startdate, String endDate) throws DaoException, SQLException {
		List<EligibleCustomerNames> elist = new ArrayList<EligibleCustomerNames>();
		List<SalesMasterDto> list = new ArrayList<SalesMasterDto>();
		List<SchemeMasterDto> schemeList = new ArrayList<SchemeMasterDto>();
		int calItemQuantity = 0;
		int flagCheck1 = 0;
		int flagCheck2 = 0;
		Connection con = null;
		PreparedStatement ps3=null;
		PreparedStatement pstat=null;
		try {
			con = ConnectCore.getConnection();
			String sql = "SELECT c.cName,s.regcustomerId,s.itemId,i.itemname,SUM(s.saleQty) as salequantity FROM salemaster s JOIN customermaster c ON s.regcustomerId=c.customerId JOIN itemmaster i ON s.itemId=i.itemId WHERE c.cName='"
					+ customername+"' AND s.companyId=? GROUP BY c.cName,i.itemname";
			ps3 = con.prepareStatement(sql);
			ps3.setInt(1, Integer.parseInt(CurrentCompAndUser.getCurrentCompany()));
			ResultSet result = ps3.executeQuery();

			while (result.next()) {
				SalesMasterDto sdto = new SalesMasterDto();
				sdto.setCustomerName(result.getString("cName"));
				sdto.setItemName(result.getString("itemname"));
				calItemQuantity = calItemQuantity + Integer.parseInt(result.getString("salequantity"));
				list.add(sdto);
				flagCheck1 = 1;
			}
			if(ps3!=null)
			{
				ps3.close();
			}
			if (flagCheck1 > 0) {
				con = ConnectCore.getConnection();
				String sql2 = "SELECT sc.schemeQty,c.cName,sc.customerId,sc.itemId,i.itemname FROM schememaster sc JOIN customermaster c ON sc.customerId= c.customerId JOIN itemmaster i ON sc.itemId=i.itemId WHERE sc.schemeName='"
						+ schemename+"' AND c.cName='"+customername+"' And sc.companyId=?";
				pstat = con.prepareStatement(sql2);
				pstat.setInt(1, Integer.parseInt(CurrentCompAndUser.getCurrentCompany()));
				ResultSet res = pstat.executeQuery();
				while (res.next()) {
					SchemeMasterDto schemeDto = new SchemeMasterDto();
					schemeDto.setSchemeQty(res.getInt("schemeQty"));
					schemeDto.setCustomerId(res.getInt("customerId"));
					schemeDto.setItemId(res.getString("itemId"));
					schemeDto.setCustomername(res.getString("cName"));
					schemeDto.setItemname(res.getString("itemname"));
					schemeList.add(schemeDto);
					flagCheck2 = 1;
				}
				if(pstat!=null)
				{
				pstat.close();
				}
				if (flagCheck2 > 0) {
				for (int i = 0; i < schemeList.size(); i++) {
				String schemeItemname = schemeList.get(i).getItemname();
				for (int k = 0; k < list.size(); k++) {
				String sellItemname = list.get(k).getItemName();
				if (schemeItemname.equals(sellItemname)) {
				EligibleCustomerNames edto = new EligibleCustomerNames();
			    edto.setItemname(list.get(k).getItemName());
				edto.setTotalQuantitySold(calItemQuantity + "");
				edto.setSchemeQty(schemeList.get(i).getSchemeQty()+"");
				elist.add(edto); }}}}}
			
		} catch (Exception e) {
			System.out.println("Excep= " + e);
		} finally {
			if(con!=null)
			{
				con.close();
				System.out.println("connection closed");
			}
		}
		return elist;
	}
	// #########################################################################################################################################################################
	// ################################################# Eligible Customer Selection method code Calculation -- End here ######################################################################
	// #########################################################################################################################################################################
	
	//######################################################## Credit Note Calculation : Start ##########################################################
	
	public List<CreditNoteDto> getcreditNoteDetailsByCustomerID(String customerId) throws DaoException, Exception 
	{
		List<CreditNoteDto> list = new LinkedList<CreditNoteDto>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			PreparedStatement ps=null;
			String cid=(String)session.getAttribute("companyId");
			try
			{
				con=ConnectCore.getConnection();
				String query="SELECT DISTINCT cnNo,saleBillNo,cnDoc , creditAmount FROM creditnote WHERE customerID = '"+customerId+"' AND cnDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND companyId=?";
				ps=con.prepareStatement(query);
				ps.setString(1, cid);
		
				System.out.println("ps= "+ps);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
					CreditNoteDto cDto=new CreditNoteDto();
					cDto.setCnNo(rs.getString("cnNo"));
					cDto.setSaleBillNo(rs.getString("saleBillNo"));
					cDto.setCnDoc(rs.getString("cnDoc"));
					cDto.setCreditAmount(rs.getString("creditAmount"));
					list.add(cDto);
				}
				if(ps!=null)
				{
				ps.close();
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					System.out.println("connection closed");
				}
			}
			return list;
	}
	
	
	public List<CreditNoteDto> getcreditNoteDetailsBybillNo(String billno) throws DaoException, Exception 
	{
		List<CreditNoteDto> list = new LinkedList<CreditNoteDto>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			PreparedStatement ps=null;
			String cid=(String)session.getAttribute("companyId");
			try
			{
				con=ConnectCore.getConnection();
				String query="SELECT refundItemId ,refundItemSize,refundItemQty FROM creditnote WHERE saleBillNo='"+billno+"'AND cnDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND companyId=?";
				ps=con.prepareStatement(query);
				ps.setString(1, cid);
		
				System.out.println("ps= "+ps);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
					CreditNoteDto cDto=new CreditNoteDto();
					cDto.setRefundItemId(rs.getString("refundItemId"));
					cDto.setRefundItemSize(rs.getString("refundItemSize"));
					cDto.setRefundItemQty(rs.getString("refundItemQty"));
					list.add(cDto);
				}
				if(ps!=null)
				{
				ps.close();
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					System.out.println("connection closed");
				}
			}
			return list;
	}
	

	public  CreditNoteDto getCreditnoteDetailsOnerecord(String Billno,Connection con)throws DaoException, Exception 
	{
		CreditNoteDto cDto=new CreditNoteDto();
		try
		{
		
		String query="Select *  FROM creditnote WHERE saleBillNo='"+Billno+"' AND companyId='1'";	
			
		PreparedStatement pt=con.prepareStatement(query);
		ResultSet rs=pt.executeQuery();
		while(rs.next())
		{
			cDto.setRefundItemId(rs.getString("refundItemId"));
			cDto.setRefundItemSize(rs.getString("refundItemSize"));
			cDto.setRefundItemQty(rs.getString("refundItemQty"));
			cDto.setSaleBillNo(rs.getString("saleBillNo"));
		}
			
			
			
		}
		catch(Exception e)
		{
			System.out.println("Exceptioon occur"+e);
		}
		
		return cDto;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	public List<SalesMasterDto> getBillNoByCustomerIdANDCustomerName(String customerId,String CustName) throws DaoException, Exception 
	{
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			PreparedStatement ps=null;
			String cid=(String)session.getAttribute("companyId");
			try
			{
				con=ConnectCore.getConnection();
				String query="SELECT DISTINCT billno FROM salemaster WHERE tempCustBarcode=? AND tempCustName='"+CustName+"' AND  companyId=? AND saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
				ps=con.prepareStatement(query);
				ps.setString(1, customerId);
				ps.setInt(2, Integer.parseInt(cid.trim()));
				System.out.println("ps= "+ps);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
					SalesMasterDto sDto=new SalesMasterDto();
					sDto.setBillno(rs.getString("billNo"));
					list.add(sDto);
				}
				if(ps!=null)
				{
				ps.close();
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					System.out.println("connection closed");
				}
			}
		return list;
	}
	
	
	@Override
	public List<SalesMasterDto> getBillNoByCustomerIdsOnly(String customerId) throws DaoException, Exception 
	{
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			PreparedStatement ps=null;
			String cid=(String)session.getAttribute("companyId");
			try
			{
				con=ConnectCore.getConnection();
				String query="SELECT DISTINCT billno FROM salemaster WHERE regcustomerId=? AND  companyId=? AND saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
				ps=con.prepareStatement(query);
				ps.setString(1, customerId);
				ps.setInt(2, Integer.parseInt(cid.trim()));
				System.out.println("ps= "+ps);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
					SalesMasterDto sDto=new SalesMasterDto();
					sDto.setBillno(rs.getString("billNo"));
					list.add(sDto);
				}
				if(ps!=null)
				{
				ps.close();
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					System.out.println("connection closed");
				}
			}
		return list;
	}
	
	
	
	
	
	
	
	@Override
	public List<SalesMasterDto> getBillNoByCustomerId(String customerId) throws DaoException, Exception 
	{
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			PreparedStatement ps=null;
			String cid=(String)session.getAttribute("companyId");
			try
			{
				con=ConnectCore.getConnection();
				String query="SELECT DISTINCT billno FROM salemaster WHERE tempCustBarcode=? AND  companyId=? AND saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
				ps=con.prepareStatement(query);
				ps.setString(1, customerId);
				ps.setInt(2, Integer.parseInt(cid.trim()));
				System.out.println("ps= "+ps);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
					SalesMasterDto sDto=new SalesMasterDto();
					sDto.setBillno(rs.getString("billNo"));
					list.add(sDto);
				}
				if(ps!=null)
				{
				ps.close();
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					System.out.println("connection closed");
				}
			}
		return list;
	}
	
	@Override
	public List<SalesMasterDto> getSalesDetailsByBillNo(String billno) throws DaoException, Exception {
		
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
		PreparedStatement ps=null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String cid=(String)session.getAttribute("companyId");
			try
			{
				con=ConnectCore.getConnection();
//				String sql="SELECT sl.salesId,sl.stockId,sl.itemId,i.itemname,sl.buyRate,sl.saleQty,sl.saleItemtotal,sl.saleDoc,sl.billno,sl.saleVat,sl.itemSize FROM salemaster sl, itemmaster i WHERE i.itemId=sl.itemId AND sl.billno LIKE ? AND sl.companyId=?";
			    String sql="SELECT sl.salesId,sl.stockId,sl.itemId,i.itemname,sl.buyRate,SUM(sl.saleQty) AS saleqty ,SUM(sl.saleItemtotal) AS saleItemtotal,sl.saleDoc,sl.billno,sl.saleVat,sl.itemSize FROM salemaster sl, itemmaster i WHERE i.itemId=sl.itemId AND sl.billno = ? AND sl.companyId=? GROUP BY itemSize,itemname";
				System.out.println("query "+sql);
				ps=con.prepareStatement(sql);
				ps.setString(1, billno);
				ps.setInt(2, Integer.parseInt(cid));
				System.out.println("ps= "+ps);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
					SalesMasterDto stkDto=new SalesMasterDto();
					stkDto.setSalesId(rs.getString("salesId"));
					stkDto.setStockId(rs.getString("stockid"));
					stkDto.setItemId(rs.getString("itemId"));
					stkDto.setItemName(rs.getString("itemname"));
					stkDto.setBuyRate(rs.getString("buyRate"));
					stkDto.setQty(rs.getString("saleQty"));
					stkDto.setItemtotal(rs.getString("saleItemtotal"));
					stkDto.setDoc(rs.getString("saleDoc"));
					stkDto.setBillno(rs.getString("billno"));
					stkDto.setVat(rs.getString("saleVat"));
					list.add(stkDto);
					
				}
				if(ps!=null)
				{
				ps.close();
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					
					System.out.println("connection closed");
				}
			}
		return list;
	}
	
	
	public List<SalesMasterDto> getTempCustomerDetails(String tempCustomerBarcode) throws DaoException
	{
		List<SalesMasterDto> slist=new ArrayList<SalesMasterDto>();
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();
			PreparedStatement pr=con.prepareStatement("SELECT * FROM salemaster WHERE tempCustBarcode='"+tempCustomerBarcode+"' AND companyId='"+CurrentCompAndUser.getCurrentCompany()+"'");
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
			SalesMasterDto sdto=new SalesMasterDto();
			sdto.setTempCustName(rs.getString("tempCustName"));
			sdto.setTemCustMobileNo(rs.getString("temCustMobileNo"));
			sdto.setTempCustAddress(rs.getString("tempCustAddress"));	
			slist.add(sdto);
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		finally
		{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		return slist;
		
	}
	
	
	
	
	public List<CustomerMasterDTO> getTempCustomerListFromSalesByProductcode(String productCode) throws DaoException
	{
		List<CustomerMasterDTO> clist=new ArrayList<CustomerMasterDTO>();
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();
			String sql="SELECT DISTINCT c.customerId, c.cName,c.custBarcode,c.custType FROM customermaster c, salemaster s WHERE c.customerId=s.regcustomerId AND s.itemId LIKE'%"+productCode+"%'    AND s.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND s.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
			PreparedStatement pr=con.prepareStatement(sql);
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
				CustomerMasterDTO cdto=new CustomerMasterDTO();
				cdto.setCustomerId(rs.getString("customerId"));
				cdto.setcName(rs.getString("cName"));
				cdto.setCustomerBarcode(rs.getString("custBarcode"));
				cdto.setCustomerType(rs.getString("custType"));
				
				clist.add(cdto);
			}
			if(pr!=null){
				rs.close();
				pr.close();
			}
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		finally
		{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return clist;
	}
	
	public List<CustomerMasterDTO> getTempCustomerListFromSalesByCustBarcode(String custBarcode) throws DaoException
	{
		List<CustomerMasterDTO> clist=new ArrayList<CustomerMasterDTO>();
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();
			String sql="SELECT DISTINCT c.customerId, c.cName,c.custBarcode,c.custType FROM customermaster c, salemaster s WHERE c.customerId=s.regcustomerId AND c.custBarcode LIKE'%"+custBarcode+"%' OR c.cName LIKE'%"+custBarcode+"%'   AND s.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND s.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
			PreparedStatement pr=con.prepareStatement(sql);
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
				CustomerMasterDTO cdto=new CustomerMasterDTO();
				cdto.setCustomerId(rs.getString("customerId"));
				cdto.setcName(rs.getString("cName"));
				cdto.setCustomerBarcode(rs.getString("custBarcode"));
				cdto.setCustomerType(rs.getString("custType"));
				
				clist.add(cdto);
			}
			if(pr!=null){
				rs.close();
				pr.close();
			}
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		finally
		{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return clist;
	}
	
	
	public List<SalesMasterDto> getSaleDetailsByProductCode(String productCode) throws DaoException
	{
		List<SalesMasterDto> slist=new ArrayList<SalesMasterDto>();
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();
			String sql="SELECT DISTINCT s.itemId FROM customermaster c, salemaster s WHERE c.customerId=s.regcustomerId AND  s.ItemId LIKE'%"+productCode+"%' AND s.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND s.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
			PreparedStatement pr=con.prepareStatement(sql);
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
				SalesMasterDto sdto=new SalesMasterDto();
				sdto.setItemId(rs.getString("itemId"));
				slist.add(sdto);
			}
			if(pr!=null){
				rs.close();
				pr.close();
			}
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		finally
		{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return slist;
	}
	
	
	
	
	@Override
	public List<CustomerMasterDTO> getTempCustomerListFromSales() throws DaoException
	{
		List<CustomerMasterDTO> clist=new ArrayList<CustomerMasterDTO>();
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();
			String sql="SELECT DISTINCT c.customerId, c.cName,c.custBarcode,c.custType FROM customermaster c, salemaster s WHERE c.customerId=s.regcustomerId AND s.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND s.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
			PreparedStatement pr=con.prepareStatement(sql);
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
				CustomerMasterDTO cdto=new CustomerMasterDTO();
				cdto.setCustomerId(rs.getString("customerId"));
				cdto.setcName(rs.getString("cName"));
				cdto.setCustomerBarcode(rs.getString("custBarcode"));
				cdto.setCustomerType(rs.getString("custType"));
				
				clist.add(cdto);
			}
			if(pr!=null){
				rs.close();
				pr.close();
			}
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		finally
		{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return clist;
	}
	
	@Override
	public List<ItemCategoryTypeMasterDTO> getSoldItemList() throws DaoException
	{
		List<ItemCategoryTypeMasterDTO> ilist=new ArrayList<ItemCategoryTypeMasterDTO>();
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();
			String sql="SELECT DISTINCT i.itemId, i.itemname FROM itemmaster i, salemaster s WHERE i.itemId=s.itemId AND s.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND s.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
			PreparedStatement pr=con.prepareStatement(sql);
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
				ItemCategoryTypeMasterDTO idto=new ItemCategoryTypeMasterDTO();
				idto.setItemId(rs.getString("itemId"));
				idto.setItemName(rs.getString("itemname"));
				
				ilist.add(idto);
			}
			if(pr!=null){
				rs.close();
				pr.close();
			}
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		finally
		{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return ilist;
	}
	
	@Override
	public List<SalesMasterDto> getCusBuyDetailsByBarcode(String CustBarcode, String billNo) throws DaoException {
		List<SalesMasterDto> list=new LinkedList<SalesMasterDto>();
		Connection con = null;
		PreparedStatement pr=null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String cid = (String) session.getAttribute("companyId");
		try {
			con = ConnectCore.getConnection();
			String Sql = "SELECT s.salesId,s.gstrate,s.gstamt,s.saleDoc,s.itemId,i.itemname,s.itemSize,s.billno,"+
						"s.buyRate,s.saleQty,s.saleItemtotal,s.saleSubtotal,s.saleLessUnit,s.saleDiscount,s.saleVat,s.saleLoryFreight,s.saleGrandTotal,s.userId"+
						" FROM salemaster s, itemmaster i WHERE i.itemId=s.itemId AND s.billno='"+billNo+"' AND s.companyId=? AND s.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
			pr = con.prepareStatement(Sql);
			
			pr.setString(1, cid.trim());
			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				SalesMasterDto dto = new SalesMasterDto();
				
				dto.setSalesId(rs.getString("salesId"));
				dto.setBillno(rs.getString("billno"));
				dto.setDoc(rs.getString("saleDoc"));
				dto.setItemName(rs.getString("itemname"));
				dto.setQty(rs.getString("saleQty"));
				dto.setItemtotal(rs.getString("saleItemTotal"));
				dto.setBuyRate(rs.getString("buyRate"));
				dto.setItemSizeName(rs.getString("itemSize"));
				dto.setItemId(rs.getString("itemId"));
				
				dto.setSubtotal(rs.getString("saleSubtotal"));
				dto.setGrandTotal(rs.getString("saleGrandTotal"));
				dto.setLessDiscount(rs.getString("saleDiscount"));
				dto.setLessloryFreight(rs.getString("saleLoryFreight"));
				dto.setLessUnit(rs.getString("saleLessUnit"));
				dto.setVat(rs.getString("gstrate"));
				dto.setGrandTotRoundvalue(rs.getInt("saleGrandTotal"));
//				double vat=rs.getDouble("saleVat")/100;
//				double value2=(rs.getDouble("saleSubtotal")-(rs.getDouble("saleLessUnit")+rs.getDouble("saleDiscount")));
//				double taxamount=value2*vat;
				
				dto.setTaxamount(Double.parseDouble(rs.getString("gstamt")));

				System.out.println("name " + dto.getBuyRate());
				list.add(dto);
			}
			
			if(pr!=null)
			{
				rs.close();
				pr.close();	
			}

		} catch (Exception e) {
			System.out.println("Ex= " + e);
		} finally {
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("connection closed");
			}
		}
		return list;
	}
	
	@Override
	public boolean addCreditNote(CreditNoteDto crDto) throws DaoException {
		Connection con = null;
		PreparedStatement pr=null;
		ResultSet rs=null;
		int x=0;
		List<CreditNoteDto> crList=new LinkedList<CreditNoteDto>();
		try {
			con = ConnectCore.getConnection();
			
			String cnSql = "INSERT INTO creditnote(cnNo,cnIdentityCode,cnDoc,saleBillNo,refundItemId,refundItemSize,refundItemQty,saleRate,refundItemTot,refundSubTot,saleVat,creditAmount,customerId,userId,companyId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pr = con.prepareStatement(cnSql);
			pr.setString(1, crDto.getCnNo());
			pr.setString(2, crDto.getCnIdentityCode());
			pr.setString(3, crDto.getCnDoc());
			pr.setString(4, crDto.getSaleBillNo());
			pr.setString(5, crDto.getRefundItemId());
			pr.setString(6, crDto.getRefundItemSize());
			pr.setString(7, crDto.getRefundItemQty());
			pr.setString(8, crDto.getSaleRate());
			pr.setString(9, crDto.getRefundItemTot());
			pr.setString(10, crDto.getRefundSubTot());
			pr.setString(11, crDto.getSaleVat());
			pr.setString(12, crDto.getCreditAmount());
			pr.setString(13, crDto.getCustomerId());
			pr.setString(14, CurrentCompAndUser.getCurrentUserID());
			pr.setString(15, CurrentCompAndUser.getCurrentCompany());
			
			System.out.println("pr="+pr);
			x=pr.executeUpdate();
			if(pr!=null)
			{
				pr.close();	
			}
			if(x>0){
				x=0;
				String fetchStockSql="SELECT stockId,qty,itemTotal,buyRate FROM stockmaster WHERE itemIds=? AND itemSize=? AND companyId=?";
				pr=con.prepareStatement(fetchStockSql);
				pr.setString(1, crDto.getRefundItemId());
				pr.setString(2, crDto.getRefundItemSize());
				pr.setString(3, CurrentCompAndUser.getCurrentCompany());
				
				rs=pr.executeQuery();
				while(rs.next()){
					crDto.setStockId(rs.getString("stockId"));
					crDto.setStockQty(rs.getInt("qty"));
					crDto.setStockItemTot(rs.getInt("itemTotal"));
					crDto.setStockBuyRate(rs.getInt("buyRate"));
					crList.add(crDto);
				}
				if(pr!=null){
					rs.close();
					pr.close();
				}
				String stId=crList.get(crList.size()-1).getStockId();
				System.out.println("stId= "+stId);
				int stQty=crList.get(crList.size()-1).getStockQty()+Integer.parseInt(crDto.getRefundItemQty());
				System.out.println("stQty= "+stQty);
				double stItmTot=(crList.get(crList.size()-1).getStockBuyRate()*stQty);
				
				String updStockSql="UPDATE stockmaster SET qty=?,itemTotal=? WHERE stockId=? AND companyId=?";
				pr=con.prepareStatement(updStockSql);
				pr.setInt(1, stQty);
				pr.setDouble(2, stItmTot);
				pr.setString(3, stId);
				pr.setString(4, CurrentCompAndUser.getCurrentCompany());
				System.out.println("pr="+pr);
				x=pr.executeUpdate();
				
				if (x>0)
				{
					
					List<SalesMasterDto> smList= new ArrayList<SalesMasterDto>();
					 SalesMasterDto sDto= new SalesMasterDto();
					 
					x=0;
					String fetchpaymentSql="SELECT saleBillNo, saleDate, grandTot, cPaidAmt, dueAmt, cPayDate FROM customerpayment WHERE saleBillNo= '"+crDto.getSaleBillNo()+"'";
					pr=con.prepareStatement(fetchpaymentSql);
					
					
					rs=pr.executeQuery();
					
					double finaldueamt=0.00;
					double dueamt=0.00;
					double cramt=Double.parseDouble(crDto.getCreditAmount());
					/*double paidamt=0.00;
					double finalPaidamt;*/
					
					while(rs.next()){
						sDto.setBillno(rs.getString("saleBillNo"));
						sDto.setDoc(rs.getString("saleDate"));
						sDto.setGrandTotal(rs.getString("grandTot"));
						sDto.setcPaidAmt(rs.getString("cPaidAmt"));
						sDto.setDueAmt(rs.getString("dueAmt"));
						sDto.setcPayDate(rs.getString("cPayDate"));
						dueamt=rs.getDouble("dueAmt");
						/*paidamt= rs.getDouble("cPaidAmt");*/
						smList.add(sDto);
					}
					if(pr!=null){
						rs.close();
						pr.close();
					
					}
					
					
					finaldueamt= dueamt-cramt;
					/*finalPaidamt= paidamt+cramt;*/
					
					
					
					String updpaymentSql="UPDATE customerpayment SET dueAmt=? WHERE saleBillNo= '"+crDto.getSaleBillNo()+"'";
					pr=con.prepareStatement(updpaymentSql);
					pr.setDouble(1, finaldueamt);
					
					System.out.println("pr="+pr);
					x=pr.executeUpdate();
				}
			}
		} catch (Exception e) {
			System.out.println("Ex= " + e);
		} finally {
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("connection closed");
			}
		}
		return x>0;
	}
	@Override
	public String creditNoGenerateEngine(String companyId) throws SQLException
	{
		String newCreditNoteNo="";
		Connection con=null;
		int cnno=1;
		int flagCheck=0;
		int cnIdentity=0;
		try
		{
			con=ConnectCore.getConnection();
			String query="SELECT MAX(cnIdentityCode)as cnIdentityCode FROM creditnote WHERE companyId=?";
			PreparedStatement ps=con.prepareStatement(query);
			ps.setString(1, companyId);
			ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					 cnIdentity=Integer.parseInt(rs.getString("cnIdentityCode"))+1;
					 newCreditNoteNo="CN00"+cnIdentity;
					flagCheck=1;
				}
			if(flagCheck==0)
			{
				newCreditNoteNo="CN00"+cnno;
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception occur type is ---"+e);
			newCreditNoteNo="CN00"+cnno;
		}
		finally
		{
			if(con!=null)
			{
				con.close();
				System.out.println("connection closed");
			}
		}
		return newCreditNoteNo;
	}
	
	@Override
	public int creditNoteIdentityGenerateEngine(String companyId) throws SQLException
	{
		
		int newCreditIdentityNoteNo=1;
		Connection con=null;
		int flagCheck=0;
		try
		{
			con=ConnectCore.getConnection();
			String query="SELECT MAX(cnIdentityCode)as cnIdentityCode FROM creditnote WHERE companyId=?";
			PreparedStatement ps=con.prepareStatement(query);
			ps.setString(1, companyId);
			ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					newCreditIdentityNoteNo=Integer.parseInt(rs.getString("cnIdentityCode"))+1;
					flagCheck=1;
				}
			if(flagCheck==0)
			{
				newCreditIdentityNoteNo=1;
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception occur type is ---"+e);
		}
		
		finally
		{
			if(con!=null)
			{
				con.close();
				System.out.println("connection closed");
			}
		}
		return newCreditIdentityNoteNo;
	}
	
	
	@Override
	public List<CreditNoteDto> getAllCreditNoteNoByCustomerCode(String cusBarcode) throws SQLException
	{
		List<CreditNoteDto> crList=new LinkedList<CreditNoteDto>();
		int newCreditIdentityNoteNo=1;
		Connection con=null;
		int flagCheck=0;
		try
		{
			con=ConnectCore.getConnection();
			String query="SELECT DISTINCT cnNo FROM creditNote WHERE customerId=? AND companyId=? AND cnDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
			PreparedStatement ps=con.prepareStatement(query);
			ps.setString(1, cusBarcode);
			ps.setString(2, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				CreditNoteDto cdto=new CreditNoteDto();
				cdto.setCnNo(rs.getString("cnNo"));
				
				crList.add(cdto);
			}
		}
		catch(Exception e) {
			System.out.println("Exception occur type is ---"+e);
		}
		
		finally
		{
			if(con!=null) {
				con.close();
				System.out.println("connection closed");
			}
		}
		return crList;
	}
	
	@Override
	public boolean saveReplaceProduct(SalesMasterDto sdto,
			String[] size, String[] qty, String[] srate,
			String[] tot, String[] itemId, String[] brate, String cusBarcode) throws DaoException, SQLException {
		String query = "insert into salemaster (saleDoc,itemId,itemSize,stockId,billno,regcustomerId,tempCustName,temCustMobileNo,tempCustAddress,buyRate,saleQty,saleItemtotal,saleSubtotal,saleLessUnit,saleDiscount,saleVat,saleLoryFreight,saleGrandTotal,userId,companyId,paidStatus,tempCustBarcode,tempCustBarcodeURL,CustomerType,crNo) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String selectStockInfo = "SELECT qty,buyRate,itemTotal FROM stockmaster WHERE stockid=? AND companyId=?";
		String updateStock = "UPDATE stockmaster SET qty=?,itemTotal=? WHERE stockid=? AND companyId=?";
		boolean result = false;
		Connection con = null;
		PreparedStatement ps=null;
		PreparedStatement ps2=null;
		PreparedStatement ps3=null;
		int flagCheckforSalesSave = 0;
		int flagCheckforStockUp = 0;
		int x = 0;
		String barcodeimage="";
		int salesId=getSalesId()+1;
		try {
			con = ConnectCore.getConnection();
			ps = con.prepareStatement(query);
			for (int i = 0; i < itemId.length; i++) 
			{
				ps.setString(1, sdto.getDoc());
				ps.setString(2, itemId[i]);
				ps.setString(3, size[i]);
				ps.setString(4, "0");
				ps.setString(5, sdto.getBillno());
				ps.setString(6, sdto.getRegcustomerId());
				ps.setString(7, sdto.getTempCustName());
				ps.setString(8, sdto.getTemCustMobileNo());
				ps.setString(9, sdto.getTempCustAddress());
				ps.setString(10, srate[i]);
				ps.setString(11, qty[i]);
				ps.setString(12, tot[i]);
				ps.setString(13, sdto.getSubtotal());
				ps.setString(14, sdto.getLessUnit());
				ps.setString(15, sdto.getLessDiscount());
				ps.setString(16, sdto.getVat());
				ps.setString(17, sdto.getLessloryFreight());
				ps.setString(18, sdto.getGrandTotal());
				ps.setString(19, CurrentCompAndUser.getCurrentUserID());
				ps.setString(20, CurrentCompAndUser.getCurrentCompany());
				ps.setString(21, "NA");
//				String custBarcode=sdto.getTempCustName()+" T"+salesId;
//				barcodeimage=generatetBarcodes(custBarcode);
				ps.setString(22, sdto.getTempCustBarcode());
				ps.setString(23, sdto.getTempCustBarcodeUrl());
				ps.setString(24, sdto.getCustomerType());
				ps.setString(25, sdto.getCrNo());
				
				System.out.println("ps= " + ps);
				x = ps.executeUpdate();
				if (x > 0) {
					System.out.println("############# Sales Details saved successfully ######");
					flagCheckforSalesSave = 1;
				}
			}
			if(ps!=null) {
				ps.close();	
			}
			if (flagCheckforSalesSave > 0) {
				for (int k = 0; k < itemId.length; k++)  {
					List<Integer> stockIds=new ArrayList<Integer>();
					int qtys=0;
					float itemTotal = 0, finalItemTot = 0, buyRate = 0;
//					ps2 = con.prepareStatement("SELECT stockId FROM stockmaster WHERE itemSize=? AND buyRate=? AND qty<>0 AND itemIds=? AND companyId=?");
					ps2 = con.prepareStatement("SELECT stockId FROM stockmaster WHERE itemSize=?  AND qty<>0 AND itemIds=? AND companyId=?");
					ps2.setString(1, size[k]);
					ps2.setString(2, itemId[k]);
					ps2.setString(3, CurrentCompAndUser.getCurrentCompany());
					System.out.println("ps2= "+ps2);
					ResultSet rs = ps2.executeQuery();
					while (rs.next()) {
						 stockIds.add(rs.getInt(1));
					}
					System.out.println("stock length= "+stockIds.size());
					if(ps2!=null) {
						rs.close();
						ps2.close();
					}
					int saleQty=Integer.parseInt(qty[k]);
					for (int i = 0; i < stockIds.size(); i++) {
						ps2 = con.prepareStatement(selectStockInfo);
						ps2.setInt(1, stockIds.get(i).intValue());
						ps2.setString(2, CurrentCompAndUser.getCurrentCompany());
						
						System.out.println("ps2= "+ps2);
						rs = ps2.executeQuery();
						while (rs.next()) {
							qtys = Integer.parseInt(rs.getString("qty"));
							itemTotal = Float.parseFloat(rs.getString("itemTotal"));
							buyRate = Float.parseFloat(rs.getString("buyRate"));
						}
					
						if(saleQty>=qtys && saleQty!=0){
							saleQty=saleQty-qtys;
							ps3 = con.prepareStatement(updateStock);
							ps3.setString(1, "0");
							ps3.setString(2, "0");
							ps3.setInt(3, stockIds.get(i).intValue());
							ps3.setString(4, CurrentCompAndUser.getCurrentCompany());
							System.out.println("up ps= "+ps3);
							
							int up = ps3.executeUpdate();
							if (up > 0) {
								flagCheckforStockUp=1;
							}
							
						   }else if(qtys>saleQty && saleQty!=0){
							qtys=qtys-saleQty;
							saleQty=0;
							finalItemTot = buyRate * qtys;
//							itemTotal = itemTotal - finalItemTot;
							
							itemTotal = finalItemTot;
							
							ps3 = con.prepareStatement(updateStock);
							ps3.setInt(1, qtys);
							ps3.setFloat(2, itemTotal);
							ps3.setInt(3, stockIds.get(i).intValue());
							ps3.setString(4, CurrentCompAndUser.getCurrentCompany());
							System.out.println("up ps= "+ps3);
							
							int up = ps3.executeUpdate();
							if (up > 0) {
								//flagCheckforStockUp=1;
								result=true;
							}
						}
						if(ps3!=null) {
							ps3.close();
						}
						if(ps2!=null) {
							rs.close();
							ps2.close();
						}
					}
				}
				
				
				
				
				if (flagCheckforStockUp>0) {
					System.out.println("Stock Updated Successfully!");
					
					 if(sdto.getDueStatus().equals("Due"))
					 { 
						sdto.setcPaidStatus("NA");
					 }
					 else
					 {
					    sdto.setcPaidStatus("PAID");	 
					 }
					 
					 
					 if(sdto.getDueStatus().equals("Due"))
					 {
						sdto.setcPaidAmt("0.00"); 
					 }
					 else
					 {
						sdto.setcPaidAmt(sdto.getGrandTotal());  
					 }
					 
					 
					 
					if(sdto.getDueStatus().equals("Due"))
					{
						sdto.setDueAmt(sdto.getGrandTotal());	
					}
					else
					{
						sdto.setDueAmt("0.0");	
					}
					 
						
						sdto.setcPayDate(sdto.getDoc());
						sdto.setcPayMode("NA");
						sdto.setChequeBank("NA");
						sdto.setChequeNo("NA");
						DaoFactory dfact=new DaoFactory();
						CustomerPaymentDao cpdao=dfact.createCustomerPayManager();
						if (cpdao.saveCustomerPaymentforSales(sdto)) {
							System.out.println("Customer Payment Saved Successfully!.");
							result=true;
						}
					
				}
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			if(con!=null) {
				con.close();
				System.out.println("connection closed");
			}
		}
		return result;
	}

	
//######################################################## Credit Note Calculation : End ##########################################################
	
	
	//#####################################################################################################################################################################################################################################################################################################################
	//############################################################################################ Excel export for sale list start here ##############################################################################################################################################################################
	//#####################################################################################################################################################################################################################################################################################################################

	
	
	public List<SalesMasterDto> exportToexcelSaleDetails(String fromdate, String todate,String customerId,String cstvat, String CNorNoCN ) throws DaoException, SQLException 
	{
		List<SalesMasterDto> list = new ArrayList<SalesMasterDto>();
		Connection con = null;
		PreparedStatement pstat=null;
		PreparedStatement pstat2=null;
		String saleSql="";
		int flagforTitle=0;
		
		DecimalFormat df= new DecimalFormat("0.00");
		
		if(!fromdate.equals("") && !todate.equals("")&& customerId.equals("0")&& cstvat.equals(""))
		{
		saleSql="SELECT DISTINCT s.billno , s.regcustomerId,c.cName,c.vat,c.gsttin, DATE_FORMAT(s.saleDoc,'%d-%m-%Y ') AS saleDoc,saleGrandTotal,s.gstrate,s.cgstrate,s.sgstrate,s.igstrate,s.basetotal FROM salemaster s JOIN customermaster c  ON c.customerId=s.regcustomerId JOIN itemmaster i ON s.itemId=i.itemId AND s.companyId=? AND s.saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"' GROUP BY billno ORDER BY saleDoc ASC";
		
		flagforTitle=1;
		}
		else if(Integer.parseInt(customerId)>0 && fromdate.equals("") && todate.equals("")&& cstvat.equals(""))
		{
			
			saleSql="SELECT  DISTINCT s.billno , s.regcustomerId,c.cName,c.vat,c,gsttin, DATE_FORMAT(s.saleDoc,'%d-%m-%Y ') AS saleDoc ,saleGrandTotal,s.gstrate,s.cgstrate,s.sgstrate,s.igstrate,s.basetotal  FROM salemaster s JOIN customermaster c  ON c.customerId=s.regcustomerId JOIN itemmaster i ON s.itemId=i.itemId where s.regcustomerId='"+customerId+"' AND s.companyId=? AND s.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' GROUP BY billno ORDER BY saleDoc ASC ";
			flagforTitle=2;
		}
		else if(Integer.parseInt(customerId)>0 && fromdate.equals("") && todate.equals("")&& !cstvat.equals(""))
		{
			if(cstvat.equals("VAT"))
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId ,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.vat<>'0.00' ORDER BY b.ldate ASC";	
			}
			else
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId ,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.vat<>'0.00' ORDER BY b.ldate ASC";	
			}
			flagforTitle=6;
		}
		else if(!fromdate.equals("") && !todate.equals("") && customerId.equals("0") && cstvat.equals(""))
		{
//			saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' ORDER BY b.ldate ASC";
			flagforTitle=3;
		}
		
		else if(fromdate.equals("") && todate.equals("") && customerId.equals("0") && !cstvat.equals(""))
		{
			
			if(cstvat.equals("VAT"))
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.vat<>'0.00' ORDER BY b.ldate ASC";	
			}
			else
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.cst<>'0.00' ORDER BY b.ldate ASC";		
			}
			
			flagforTitle=4;
		}
		else if(!fromdate.equals("") && !todate.equals("") && Integer.parseInt(customerId)>0 && !cstvat.equals(""))
		{
			
			if(cstvat.equals("VAT"))
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.vat<>'0.00' ORDER BY b.ldate ASC";
			}
			else
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.cst<>'0.00' ORDER BY b.ldate ASC";	
			}
			flagforTitle=7;
		}
		
		else if(!fromdate.equals("") && !todate.equals("") && customerId.equals("0") && !cstvat.equals(""))
		{
			
			if(cstvat.equals("VAT"))
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"'  AND b.vat<>'0.00' ORDER BY b.ldate ASC";
			}
			else
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"'  AND b.cst<>'0.00' ORDER BY b.ldate ASC";	
			}
			flagforTitle=8;
		}
		
		else if(!fromdate.equals("") && !todate.equals("") && Integer.parseInt(customerId)>0 && cstvat.equals(""))
		{
			
			
			saleSql="SELECT DISTINCT s.billno , s.regcustomerId,c.cName,c.vat,c,gsttin, DATE_FORMAT(s.saleDoc,'%d-%m-%Y ') AS saleDoc ,saleGrandTotal,s.gstrate,s.cgstrate,s.sgstrate,s.igstrate ,s.basetotal FROM salemaster s JOIN customermaster c  ON c.customerId=s.regcustomerId JOIN itemmaster i ON s.itemId=i.itemId where s.regcustomerId='"+customerId+"' AND s.companyId=? AND s.saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"' GROUP BY billno ORDER BY saleDoc ASC ";
			flagforTitle=9;
			
		}
		else 
		{
			saleSql="SELECT DISTINCT s.billno , s.regcustomerId,c.cName,c.vat,c.gsttin, DATE_FORMAT(s.saleDoc,'%d-%m-%Y ') AS saleDoc,saleGrandTotal,s.gstrate,s.cgstrate,s.sgstrate,s.igstrate ,s.basetotal FROM salemaster s JOIN customermaster c  ON c.customerId=s.regcustomerId JOIN itemmaster i ON s.itemId=i.itemId AND s.companyId=? AND s.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' GROUP BY billno ORDER BY saleDoc ASC ";
			flagforTitle=5;
		}
		try {
			ResultSet rs=null;
			DaoFactory daoFactory=new DaoFactory();
			SalesMasterDao sdao=daoFactory.createSalesMasterManager();
			con=ConnectCore.getConnection();
			 if(CNorNoCN.equals("No C.N"))
			 {
		
		pstat=con.prepareStatement(saleSql);
		pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
		rs=pstat.executeQuery();
	
			 }
		
		
		 if(CNorNoCN.equals("C.N"))
			{
				
			    saleSql= " SELECT DISTINCT(saleBillNo)saleBillNo ,cnDoc,creditamount  FROM creditnote WHERE cnDoc BETWEEN '"+fromdate+"' AND '"+todate+"'";
			    
			   
				
				pstat2 = con.prepareStatement( saleSql);
				
				System.out.println("ps="+pstat2);
				rs = pstat2.executeQuery();
				
				
				
			}
		
		
		if(CNorNoCN.equals("No C.N"))
		{
		while(rs.next())
		{
			if(checkCNorNotForBillNo(rs.getString("billno"), con)==false)
			{
			List<SalesMasterDto> salelistf	=new ArrayList<SalesMasterDto>();
			
			double totgstamt=0.0;
			double totcgstamt=0.0;
			double totsgstamt=0.0;
			double totIgstamt=0.0;
			double basetot=0.0;
			
			
			SalesMasterDto pd=new SalesMasterDto();
			CreditNoteDto cdto=new CreditNoteDto();
			pd.setBillno(rs.getString("billno"));
			pd.setDoc(rs.getString("saleDoc"));
			
			pd.setCustomerName(rs.getString("cName"));
			pd.setGrandTotal(df.format(rs.getDouble("saleGrandTotal"))+"");
			 
			
			
			
			
			
			
			salelistf=sdao.getAllSalesDetailsByBillNoOnly(pd.getBillno());
			
			for(SalesMasterDto s:salelistf)
			{
				totgstamt=totgstamt+Double.parseDouble(s.getGstamt());
				totcgstamt=totgstamt+Double.parseDouble(s.getCgstamt());
				totsgstamt=totsgstamt+Double.parseDouble(s.getSgstamt());
				totIgstamt=totIgstamt+Double.parseDouble(s.getIgstamt());
				basetot=basetot+Double.parseDouble(s.getBasetotal());
			}
			pd.setGstrate(rs.getString("gstrate"));
			pd.setVat(rs.getString("gsttin"));
			pd.setGstamt(new DecimalFormat("##.##").format(totgstamt)+"");
			pd.setCgstrate(rs.getString("cgstrate"));
			pd.setCgstamt(new DecimalFormat("##.##").format(totgstamt/2)+"");
			pd.setSgstrate(rs.getString("sgstrate"));
			pd.setSgstamt(new DecimalFormat("##.##").format(totgstamt/2)+"");
			pd.setBasetotal(new DecimalFormat("##.##").format(basetot)+"");
			try
			{
			cdto=sdao.getCreditnoteDetailsOnerecord(pd.getBillno(), con);
			
			if(!cdto.getSaleBillNo().equals(null))
			{
				pd.setCreditnotestatus("C.N");	
				System.out.println("CN found");
			}
			else
			{
				pd.setCreditnotestatus("NO C.N");	
			}
	
			}
			catch(NullPointerException n)
			{
				pd.setCreditnotestatus("NO C.N");	
			}
			if(flagforTitle==4)
			{
				pd.setTitleforExcel("TOTAL SALE REPORT "+cstvat+"% only");
			}
			else if(flagforTitle==2)
			{
				pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName"));	
			}
			else if(flagforTitle==3)
			{
				pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate);	
			}
			else if(flagforTitle==1)
			{
				pd.setTitleforExcel("TOTAL SALE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate);	
			}
			else if(flagforTitle==5)
			{
				pd.setTitleforExcel("TOTAL SALE REPORT ");
			}
			else if(flagforTitle==6)
			{
				pd.setTitleforExcel("TOTAL SALE DETAILS FROM CUSTOMER  "+rs.getString("cName")+" "+cstvat+" only");
			}
			else if(flagforTitle==7)
			{
				pd.setTitleforExcel("TOTAL SALE DETAILS FROM CUSTOMER   "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate +" "+cstvat+" only");	
			}
			
			else if(flagforTitle==8)
			{
				pd.setTitleforExcel("TOTAL SALE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate+" "+cstvat+" only");	
			}
			else if(flagforTitle==9)
			{
				pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate);	
			}
			
			list.add(pd);
			}}
			 
		}
		else if(CNorNoCN.equals("C.N"))
		{
			while (rs.next()) {
			if(checkCNorNotForBillNo(rs.getString("saleBillNo"), con)==true)
			{
				List<SalesMasterDto> salelistf	=new ArrayList<SalesMasterDto>();
				double totgstamt=0.0;
				double totcgstamt=0.0;
				double totsgstamt=0.0;
				double totIgstamt=0.0;
				double basetot=0.0;
				
				
				SalesMasterDto pd=new SalesMasterDto();
				CreditNoteDto cdto=new CreditNoteDto();
				pd.setBillno(rs.getString("billno"));
				pd.setDoc(rs.getString("saleDoc"));
				
				pd.setCustomerName(rs.getString("cName"));
				pd.setGrandTotal(df.format(rs.getDouble("saleGrandTotal"))+"");
				
				pd.setGstrate(rs.getString("gstrate"));
				
				salelistf=sdao.getAllSalesDetailsByBillNoOnly(pd.getBillno());
				
				for(SalesMasterDto s:salelistf)
				{
					totgstamt=totgstamt+Double.parseDouble(s.getGstamt());
					totcgstamt=totgstamt+Double.parseDouble(s.getCgstamt());
					totsgstamt=totsgstamt+Double.parseDouble(s.getSgstamt());
					totIgstamt=totIgstamt+Double.parseDouble(s.getIgstamt());
					basetot=basetot+Double.parseDouble(s.getBasetotal());
				}
				
				pd.setGstrate(rs.getString("gstrate"));
				pd.setVat(rs.getString("gsttin"));
				pd.setGstamt(new DecimalFormat("##.##").format(totgstamt)+"");
				pd.setCgstrate(rs.getString("cgstrate"));
				pd.setCgstamt(new DecimalFormat("##.##").format(totgstamt/2)+"");
				pd.setSgstrate(rs.getString("sgstrate"));
				pd.setSgstamt(new DecimalFormat("##.##").format(totgstamt/2)+"");
				pd.setBasetotal(new DecimalFormat("##.##").format(basetot)+"");
				
				try
				{
				cdto=sdao.getCreditnoteDetailsOnerecord(pd.getBillno(), con);
				
				if(!cdto.getSaleBillNo().equals(null))
				{
					pd.setCreditnotestatus("C.N");	
					System.out.println("CN found");
				}
				else
				{
					pd.setCreditnotestatus("NO C.N");	
				}
		
				}
				catch(NullPointerException n)
				{
					pd.setCreditnotestatus("NO C.N");	
				}
				if(flagforTitle==4)
				{
					pd.setTitleforExcel("TOTAL SALE REPORT "+cstvat+"% only");
				}
				else if(flagforTitle==2)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName"));	
				}
				else if(flagforTitle==3)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate);	
				}
				else if(flagforTitle==1)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate);	
				}
				else if(flagforTitle==5)
				{
					pd.setTitleforExcel("TOTAL SALE REPORT ");
				}
				else if(flagforTitle==6)
				{
					pd.setTitleforExcel("TOTAL SALE DETAILS FROM CUSTOMER  "+rs.getString("cName")+" "+cstvat+" only");
				}
				else if(flagforTitle==7)
				{
					pd.setTitleforExcel("TOTAL SALE DETAILS FROM CUSTOMER   "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate +" "+cstvat+" only");	
				}
				
				else if(flagforTitle==8)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate+" "+cstvat+" only");	
				}
				else if(flagforTitle==9)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate);	
				}
				
				list.add(pd);
				
				
				
				

				
				
				/*
				
				
				
				 PreparedStatement pstat7=null;
					ResultSet rs7=null;
//					query= "SELECT DISTINCT s.billno ,s.saleDoc, s.regcustomerId,s.saleGrandTotal, c.cName  FROM salemaster s, customermaster c WHERE c.customerId=s.regcustomerId AND s.billno='"+rs.getString("saleBillNo")+"'   AND s.companyId=?   ORDER BY  s.salesid DESC";
					
					
					
					saleSql="	SELECT DISTINCT s.saleBillNo ,s.cnDoc,s.creditamount  FROM creditnote s WHERE  s.saleBillNo='"+rs.getString("saleBillNo")+"'  AND s.companyId='1'";  
					
				
					pstat7 = con.prepareStatement(saleSql);
				
					System.out.println("ps="+pstat7);
					rs7 = pstat7.executeQuery();
					
			      while(rs7.next())
			      {
				SalesMasterDto pd = new SalesMasterDto();
				CreditNoteDto cdto=new CreditNoteDto();
				pd.setBillno(rs7.getString("saleBillNo"));
				pd.setDoc(rs7.getString("cnDoc"));
//				dto.setRegcustomerId(rs7.getString("regcustomerId"));
//				dto.setCustomerName(rs7.getString("cName"));
				
			
				pd.setRegcustomerId("1");
				pd.setCustomerName("NA");
				
				pd.setGrandTotal(rs7.getString("creditamount"));
				
//				pd.setBillno(rs7.getString("billno"));
//				pd.setDoc(rs7.getString("saleDoc"));
//				
//				pd.setCustomerName(rs7.getString("cName"));
//				pd.setGrandTotal(df.format(rs7.getDouble("saleGrandTotal"))+"");
				try
				{
				cdto=sdao.getCreditnoteDetailsOnerecord(pd.getBillno(), con);
				
				if(!cdto.getSaleBillNo().equals(null))
				{
					pd.setCreditnotestatus("C.N");	
					System.out.println("CN found");
				}
				else
				{
					pd.setCreditnotestatus("NO C.N");	
				}
		
				}
				catch(NullPointerException n)
				{
					pd.setCreditnotestatus("NO C.N");	
				}
				if(flagforTitle==4)
				{
					pd.setTitleforExcel("TOTAL SALE REPORT "+cstvat+"% only");
				}
				else if(flagforTitle==2)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName"));	
				}
				else if(flagforTitle==3)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate);	
				}
				else if(flagforTitle==1)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate);	
				}
				else if(flagforTitle==5)
				{
					pd.setTitleforExcel("TOTAL SALE REPORT ");
				}
				else if(flagforTitle==6)
				{
					pd.setTitleforExcel("TOTAL SALE DETAILS FROM CUSTOMER  "+rs.getString("cName")+" "+cstvat+" only");
				}
				else if(flagforTitle==7)
				{
					pd.setTitleforExcel("TOTAL SALE DETAILS FROM CUSTOMER   "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate +" "+cstvat+" only");	
				}
				
				else if(flagforTitle==8)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate+" "+cstvat+" only");	
				}
				else if(flagforTitle==9)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate);	
				}
				
				list.add(pd);
				}
			*/}
			}
		
		}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
//				pstat.close();
				System.out.println("connection closed");
			}
		}
		return list;
	}
	

	
	
	
	
	public List<SalesMasterDto> exportToexcelSaleDetailsgstr(String fromdate, String todate,String customerId,String cstvat, String CNorNoCN ) throws DaoException, SQLException 
	{
		List<SalesMasterDto> list = new ArrayList<SalesMasterDto>();
		Connection con = null;
		PreparedStatement pstat=null;
		PreparedStatement pstat2=null;
		String saleSql="";
		int flagforTitle=0;
		
		DecimalFormat df= new DecimalFormat("0.00");
		
		if(!fromdate.equals("") && !todate.equals("")&& customerId.equals("0")&& cstvat.equals(""))
		{
		saleSql="SELECT * FROM salemaster WHERE saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"' GROUP BY billno ORDER BY billno ASC";
		
		flagforTitle=1;
		}
		else if(Integer.parseInt(customerId)>0 && fromdate.equals("") && todate.equals("")&& cstvat.equals(""))
		{
			
			saleSql="SELECT * FROM salemaster WHERE saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"' GROUP BY billno ORDER BY billno ASC";
			flagforTitle=2;
		}
		else if(Integer.parseInt(customerId)>0 && fromdate.equals("") && todate.equals("")&& !cstvat.equals(""))
		{
			if(cstvat.equals("VAT"))
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId ,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.vat<>'0.00' ORDER BY b.ldate ASC";	
			}
			else
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId ,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.vat<>'0.00' ORDER BY b.ldate ASC";	
			}
			flagforTitle=6;
		}
		else if(!fromdate.equals("") && !todate.equals("") && customerId.equals("0") && cstvat.equals(""))
		{
//			saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' ORDER BY b.ldate ASC";
			flagforTitle=3;
		}
		
		else if(fromdate.equals("") && todate.equals("") && customerId.equals("0") && !cstvat.equals(""))
		{
			
			if(cstvat.equals("VAT"))
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.vat<>'0.00' ORDER BY b.ldate ASC";	
			}
			else
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.cst<>'0.00' ORDER BY b.ldate ASC";		
			}
			
			flagforTitle=4;
		}
		else if(!fromdate.equals("") && !todate.equals("") && Integer.parseInt(customerId)>0 && !cstvat.equals(""))
		{
			
			if(cstvat.equals("VAT"))
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.vat<>'0.00' ORDER BY b.ldate ASC";
			}
			else
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.cst<>'0.00' ORDER BY b.ldate ASC";	
			}
			flagforTitle=7;
		}
		
		else if(!fromdate.equals("") && !todate.equals("") && customerId.equals("0") && !cstvat.equals(""))
		{
			
			if(cstvat.equals("VAT"))
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"'  AND b.vat<>'0.00' ORDER BY b.ldate ASC";
			}
			else
			{
//				saleSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"'  AND b.cst<>'0.00' ORDER BY b.ldate ASC";	
			}
			flagforTitle=8;
		}
		
		else if(!fromdate.equals("") && !todate.equals("") && Integer.parseInt(customerId)>0 && cstvat.equals(""))
		{
			
			
			saleSql="SELECT DISTINCT s.billno , s.regcustomerId,c.cName,c.vat,c,gsttin, DATE_FORMAT(s.saleDoc,'%d-%m-%Y ') AS saleDoc ,saleGrandTotal,s.gstrate,s.cgstrate,s.sgstrate,s.igstrate ,s.basetotal FROM salemaster s JOIN customermaster c  ON c.customerId=s.regcustomerId JOIN itemmaster i ON s.itemId=i.itemId where s.regcustomerId='"+customerId+"' AND s.companyId=? AND s.saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"' GROUP BY billno ORDER BY saleDoc ASC ";
			flagforTitle=9;
			
		}
		else 
		{
			saleSql="SELECT DISTINCT s.billno , s.regcustomerId,c.cName,c.vat,c.gsttin, DATE_FORMAT(s.saleDoc,'%d-%m-%Y ') AS saleDoc,saleGrandTotal,s.gstrate,s.cgstrate,s.sgstrate,s.igstrate ,s.basetotal FROM salemaster s JOIN customermaster c  ON c.customerId=s.regcustomerId JOIN itemmaster i ON s.itemId=i.itemId AND s.companyId=? AND s.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' GROUP BY billno ORDER BY saleDoc ASC ";
			flagforTitle=5;
		}
		try {
			ResultSet rs=null;
			DaoFactory daoFactory=new DaoFactory();
			SalesMasterDao sdao=daoFactory.createSalesMasterManager();
			con=ConnectCore.getConnection();
			 if(CNorNoCN.equals("No C.N"))
			 {
		
		pstat=con.prepareStatement(saleSql);
	
		rs=pstat.executeQuery();
	
			 }
		
		
		 if(CNorNoCN.equals("C.N"))
			{
				
			    saleSql= " SELECT DISTINCT(saleBillNo)saleBillNo ,cnDoc,creditamount  FROM creditnote WHERE cnDoc BETWEEN '"+fromdate+"' AND '"+todate+"'";
			    
			   
				
				pstat2 = con.prepareStatement( saleSql);
				
				System.out.println("ps="+pstat2);
				rs = pstat2.executeQuery();
				
				
				
			}
		
		
		if(CNorNoCN.equals("No C.N"))
		{
		while(rs.next())
		{
			if(checkCNorNotForBillNo(rs.getString("billno"), con)==false)
			{
			List<SalesMasterDto> salelistf	=new ArrayList<SalesMasterDto>();
			
			double totgstamt=0.0;
			double totcgstamt=0.0;
			double totsgstamt=0.0;
			double totIgstamt=0.0;
			double basetot=0.0;
			
			
			SalesMasterDto pd=new SalesMasterDto();
			CreditNoteDto cdto=new CreditNoteDto();
			pd.setBillno(rs.getString("billno"));
			pd.setDoc(rs.getString("saleDoc"));
			
			pd.setCustomerName(getCustomerName(rs.getString("regcustomerId").toString(), con));
//			pd.setBillno(rs.getString("billno"));
			pd.setDoc(rs.getString("saleDoc"));
			
			pd.setGrandTotal(df.format(rs.getDouble("saleGrandTotal"))+"");

			pd.setGstrate(rs.getString("gstrate"));
			pd.setVat(getCustomerGSTTIN(rs.getString("regcustomerId").toString(), con));
			pd.setGstamt(rs.getString("gstamt"));
			pd.setCgstrate(rs.getString("cgstrate"));
			pd.setCgstamt(rs.getString("cgstamt"));
			pd.setSgstrate(rs.getString("sgstrate"));
			pd.setSgstamt(rs.getString("sgstamt"));
			pd.setBasetotal(getBaseTotal(rs.getString("billno").toString(), con));
			try
			{
			cdto=sdao.getCreditnoteDetailsOnerecord(pd.getBillno(), con);
			
			if(!cdto.getSaleBillNo().equals(null))
			{
				pd.setCreditnotestatus("C.N");	
				System.out.println("CN found");
			}
			else
			{
				pd.setCreditnotestatus("NO C.N");	
			}
	
			}
			catch(NullPointerException n)
			{
				pd.setCreditnotestatus("NO C.N");	
			}
			if(flagforTitle==4)
			{
				pd.setTitleforExcel("TOTAL SALE REPORT "+cstvat+"% only");
			}
			else if(flagforTitle==2)
			{
				pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName"));	
			}
			else if(flagforTitle==3)
			{
				pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate);	
			}
			else if(flagforTitle==1)
			{
				pd.setTitleforExcel("TOTAL SALE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate);	
			}
			else if(flagforTitle==5)
			{
				pd.setTitleforExcel("TOTAL SALE REPORT ");
			}
			else if(flagforTitle==6)
			{
				pd.setTitleforExcel("TOTAL SALE DETAILS FROM CUSTOMER  "+rs.getString("cName")+" "+cstvat+" only");
			}
			else if(flagforTitle==7)
			{
				pd.setTitleforExcel("TOTAL SALE DETAILS FROM CUSTOMER   "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate +" "+cstvat+" only");	
			}
			
			else if(flagforTitle==8)
			{
				pd.setTitleforExcel("TOTAL SALE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate+" "+cstvat+" only");	
			}
			else if(flagforTitle==9)
			{
				pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate);	
			}
			
			list.add(pd);
			}}
			 
		}
		else if(CNorNoCN.equals("C.N"))
		{
			while (rs.next()) {
			if(checkCNorNotForBillNo(rs.getString("saleBillNo"), con)==true)
			{
				List<SalesMasterDto> salelistf	=new ArrayList<SalesMasterDto>();
				double totgstamt=0.0;
				double totcgstamt=0.0;
				double totsgstamt=0.0;
				double totIgstamt=0.0;
				double basetot=0.0;
				
				SalesMasterDto pd=new SalesMasterDto();
				CreditNoteDto cdto=new CreditNoteDto();
				pd.setBillno(rs.getString("billno"));
				pd.setDoc(rs.getString("saleDoc"));
				
				pd.setCustomerName(getCustomerName(rs.getString("regcustomerId").toString(), con));
//				pd.setBillno(rs.getString("billno"));
				pd.setDoc(rs.getString("saleDoc"));
				
				pd.setGrandTotal(df.format(rs.getDouble("saleGrandTotal"))+"");

				pd.setGstrate(rs.getString("gstrate"));
				pd.setVat(getCustomerGSTTIN(rs.getString("regcustomerId").toString(), con)  );
				pd.setGstamt(rs.getString("gstamt"));
				pd.setCgstrate(rs.getString("cgstrate"));
				pd.setCgstamt(rs.getString("cgstamt"));
				pd.setSgstrate(rs.getString("sgstrate"));
				pd.setSgstamt(rs.getString("sgstamt"));
				pd.setBasetotal(getBaseTotal(rs.getString("billno").toString(), con));
				
				try
				{
				cdto=sdao.getCreditnoteDetailsOnerecord(pd.getBillno(), con);
				
				if(!cdto.getSaleBillNo().equals(null))
				{
					pd.setCreditnotestatus("C.N");	
					System.out.println("CN found");
				}
				else
				{
					pd.setCreditnotestatus("NO C.N");	
				}
		
				}
				catch(NullPointerException n)
				{
					pd.setCreditnotestatus("NO C.N");	
				}
				if(flagforTitle==4)
				{
					pd.setTitleforExcel("TOTAL SALE REPORT "+cstvat+"% only");
				}
				else if(flagforTitle==2)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName"));	
				}
				else if(flagforTitle==3)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate);	
				}
				else if(flagforTitle==1)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate);	
				}
				else if(flagforTitle==5)
				{
					pd.setTitleforExcel("TOTAL SALE REPORT ");
				}
				else if(flagforTitle==6)
				{
					pd.setTitleforExcel("TOTAL SALE DETAILS FROM CUSTOMER  "+rs.getString("cName")+" "+cstvat+" only");
				}
				else if(flagforTitle==7)
				{
					pd.setTitleforExcel("TOTAL SALE DETAILS FROM CUSTOMER   "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate +" "+cstvat+" only");	
				}
				
				else if(flagforTitle==8)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate+" "+cstvat+" only");	
				}
				else if(flagforTitle==9)
				{
					pd.setTitleforExcel("TOTAL SALE  DETAILS FROM CUSTOMER  "+rs.getString("cName") +" FROM DATE "+fromdate+" TO DATE "+todate);	
				}
				
				list.add(pd);
				}
			}
		
		}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if (con != null) {
				con.close();
//				pstat.close();
				System.out.println("connection closed");
			}
		}
		return list;
	}
	
	
	
	
	
	
	public String getCustomerName(String customerid,Connection con)
	{
		
		String customername="";
		try
		{
			
			PreparedStatement pr=con.prepareStatement("SELECT * from customermaster where customerId='"+customerid+"'" );
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
				customername=rs.getString("cName");
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		
		return customername;
	}
	
	
	
	public String getBaseTotal(String billno,Connection con)
	{
		
		String billnos="";
		try
		{
			
			PreparedStatement pr=con.prepareStatement("SELECT SUM(basetotal)as basetotal from salemaster where billno='"+billno+"'" );
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
				billnos=rs.getString("basetotal");
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		
		return billnos;
	}
	
	
	
	
	
	
	
	
	
	public String getCustomerGSTTIN(String customerid,Connection con)
	{
		
		String gsttin="";
		try
		{
			
			PreparedStatement pr=con.prepareStatement("SELECT * from customermaster where customerId='"+customerid+"'" );
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
				gsttin=rs.getString("gsttin");
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		
		return gsttin;
	}
	
	
	public int getMaxSaleID() throws DaoException
	{
		int maxid=0;
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();
			PreparedStatement pr=con.prepareStatement("SELECT MAX(salesId) AS salesId FROM salemaster WHERE companyId='"+CurrentCompAndUser.getCurrentCompany()+"'");
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
				maxid=Integer.parseInt(rs.getString("salesId"));
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		finally
		{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return maxid;	
		
		
	}
	
	public int getMaxSaleBillNo() throws DaoException
	{
		int maxid=0;
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();
			PreparedStatement pr=con.prepareStatement("SELECT max(incnumber)as billno FROM salemaster WHERE saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'  ORDER BY salesId DESC LIMIT 1" );
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
				maxid=Integer.parseInt(rs.getString("billno"));
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		finally
		{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return maxid;	
		
		
	}	
	
	//#####################################################################################################################################################################################################################################################################################################################
	//############################################################################################ Excel export for sale list end here ##############################################################################################################################################################################
	//#####################################################################################################################################################################################################################################################################################################################

	
	public double getTotalSellDetailsOfPerticularCustomer(String custBarcode) throws DaoException
	{
		double getTotalSellAmount=0.0;
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();
			PreparedStatement pr=con.prepareStatement("SELECT SUM(saleGrandTotal) AS saleGrandTotal FROM salemaster WHERE tempCustBarcode='"+custBarcode+"' AND companyId='"+CurrentCompAndUser.getCurrentCompany()+"'");
			ResultSet rs=pr.executeQuery();
			while(rs.next())
			{
				getTotalSellAmount=Double.parseDouble(rs.getString("saleGrandTotal"));
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Exceptionn occur"+ e);
		}
		finally
		{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return getTotalSellAmount;
	}
	
	public boolean addCreditNotes(List<CreditNoteDto> crDto) throws DaoException
	{
		Connection con = null;
		PreparedStatement pr=null;
		PreparedStatement pr2=null;
		ResultSet rs=null;
		int x=0;
		List<CreditNoteDto> crList=new LinkedList<CreditNoteDto>();
		try {
			con = ConnectCore.getConnection();
			
			for(int m=0;m<crDto.size();m++)
			{
			
			String cnSql = "INSERT INTO creditnote(cnNo,cnIdentityCode,cnDoc,saleBillNo,refundItemId,refundItemSize,refundItemQty,saleRate,refundItemTot,refundSubTot,saleVat,creditAmount,customerId,userId,companyId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pr = con.prepareStatement(cnSql);
			pr.setString(1, crDto.get(m).getCnNo());
			pr.setString(2, crDto.get(m).getCnIdentityCode());
			pr.setString(3, crDto.get(m).getCnDoc());
			pr.setString(4, crDto.get(m).getSaleBillNo());
			pr.setString(5, crDto.get(m).getRefundItemId());
			pr.setString(6, crDto.get(m).getRefundItemSize());
			pr.setString(7, crDto.get(m).getRefundItemQty());
			pr.setString(8, crDto.get(m).getSaleRate());
			pr.setString(9, crDto.get(m).getRefundItemTot());
			pr.setString(10, crDto.get(m).getRefundSubTot());
			pr.setString(11, crDto.get(m).getSaleVat());
			pr.setString(12, crDto.get(m).getCreditAmount());
			pr.setString(13, crDto.get(m).getCustomerId());
			pr.setString(14, CurrentCompAndUser.getCurrentUserID());
			pr.setString(15, CurrentCompAndUser.getCurrentCompany());
			
			System.out.println("pr="+pr);
			x=pr.executeUpdate();
			}
			if(pr!=null)
			{
				pr.close();	
			}
			if(x>0){
				x=0;
				
				for(int n=0;n<crDto.size();n++)
				{
				
				
				String fetchStockSql="SELECT stockId,qty,itemTotal,buyRate FROM stockmaster WHERE itemIds=? AND itemSize=? AND companyId=?";
				pr=con.prepareStatement(fetchStockSql);
				pr.setString(1, crDto.get(n).getRefundItemId());
				pr.setString(2, crDto.get(n).getRefundItemSize());
				pr.setString(3, CurrentCompAndUser.getCurrentCompany());
				rs=pr.executeQuery();
				while(rs.next()){
					CreditNoteDto crdto2=new CreditNoteDto();
					
					crdto2.setStockId(rs.getString("stockId"));
					crdto2.setStockQty(rs.getInt("qty"));
					crdto2.setStockItemTot(rs.getInt("itemTotal"));
					crdto2.setStockBuyRate(rs.getInt("buyRate"));
					crList.add(crdto2);
				}
				if(pr!=null){
					rs.close();
					pr.close();
				}
				String stId=crList.get(crList.size()-1).getStockId();
				System.out.println("stId= "+stId);
				int stQty=crList.get(crList.size()-1).getStockQty()+Integer.parseInt(crDto.get(n).getRefundItemQty());
				System.out.println("stQty= "+stQty);
				double stItmTot=(crList.get(crList.size()-1).getStockBuyRate()*stQty);
				
				String updStockSql="UPDATE stockmaster SET qty=?,itemTotal=? WHERE stockId=? AND companyId=?";
				pr=con.prepareStatement(updStockSql);
				pr.setInt(1, stQty);
				pr.setDouble(2, stItmTot);
				pr.setString(3, stId);
				pr.setString(4, CurrentCompAndUser.getCurrentCompany());
				System.out.println("pr="+pr);
				x=pr.executeUpdate();
				
				}
				
				if (x>0)
				{
					List<SalesMasterDto> smList= new ArrayList<SalesMasterDto>();
					SalesMasterDto sDto= new SalesMasterDto(); 
					x=0;
					String fetchpaymentSql="SELECT saleBillNo, saleDate, grandTot, cPaidAmt, dueAmt, cPayDate FROM customerpayment WHERE saleBillNo= '"+crDto.get(0).getSaleBillNo()+"'";
					pr=con.prepareStatement(fetchpaymentSql);
					rs=pr.executeQuery();
					double finaldueamt=0.00;
					double dueamt=0.00;
					double cramt=Double.parseDouble(crDto.get(0).getCreditAmount());
					double paidamt=0.00;
					double finalPaidamt;
					while(rs.next()){
						sDto.setBillno(rs.getString("saleBillNo"));
						sDto.setDoc(rs.getString("saleDate"));
						sDto.setGrandTotal(rs.getString("grandTot"));
						sDto.setcPaidAmt(rs.getString("cPaidAmt"));
						sDto.setDueAmt(rs.getString("dueAmt"));
						sDto.setcPayDate(rs.getString("cPayDate"));
						dueamt=rs.getDouble("dueAmt");
						paidamt= rs.getDouble("cPaidAmt");
						smList.add(sDto);
					}
					if(pr!=null){
						rs.close();
						pr.close();
					
					}
					finaldueamt= dueamt-cramt;
//					finalPaidamt= paidamt+cramt;
					/*String updpaymentSql="UPDATE customerpayment SET dueAmt=? WHERE saleBillNo= '"+crDto.get(0).getSaleBillNo()+"'";
					pr=con.prepareStatement(updpaymentSql);
					pr.setDouble(1, finaldueamt);

					System.out.println("pr="+pr);
					x=pr.executeUpdate();
					*/
					x=1;
				   if(x>0)
				   {
					  
					   /*String queryPayment="Update custpayhistory set dueamount=? where billno=? and dueamount=?";
					   pr2=con.prepareStatement(queryPayment);
					   pr2.setString(1, finaldueamt+"");
					   pr2.setString(2,crDto.get(0).getSaleBillNo());
					   pr2.setString(3,dueamt+"");
					   
					   int l=pr2.executeUpdate();  */
					   int l=1;
					   if(l>0)
					   {
						   x=1;
					   }
				   }	
				}
			}
		} catch (Exception e) {
			System.out.println("Ex= " + e);
		} finally {
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("connection closed");
			}
		}
		return true;
		
	}

	//-------&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& BAR CHART REPORT CODES  starts here &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&	

	public double getMonthlySaleReportOFAPR()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
			HttpServletRequest request = ServletActionContext.getRequest();
		    HttpSession session = request.getSession();
			String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
		
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+fromyear+"-04-01' AND '"+fromyear+"-04-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount= amount+rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}

	public double getMonthlySaleReportOFMAY()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
			
			HttpServletRequest request = ServletActionContext.getRequest();
		    HttpSession session = request.getSession();
		    String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
		
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+fromyear+"-05-01' AND '"+fromyear+"-05-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount=amount+rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}

	public double getMonthlySaleReportOFJUN()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
		
			HttpServletRequest request = ServletActionContext.getRequest();
		    HttpSession session = request.getSession();
		    String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
			
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+fromyear+"-06-01' AND '"+fromyear+"-06-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount=amount+rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}

	public double getMonthlySaleReportOFJUL()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
		
			HttpServletRequest request = ServletActionContext.getRequest();
		    HttpSession session = request.getSession();
		    String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
			
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+fromyear+"-07-01' AND '"+fromyear+"-07-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount= amount+ rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}

	public double getMonthlySaleReportOFAUG()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
			
			HttpServletRequest request = ServletActionContext.getRequest();
		    HttpSession session = request.getSession();
		    String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
		
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+fromyear+"-08-01' AND '"+fromyear+"-08-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount= amount+ rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}


	public double getMonthlySaleReportOFSEP()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
			
			HttpServletRequest request = ServletActionContext.getRequest();
		    HttpSession session = request.getSession();
		    String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
		
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+fromyear+"-09-01' AND '"+fromyear+"-09-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount=amount+rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}


	public double getMonthlySaleReportOFOCT()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
			HttpServletRequest request = ServletActionContext.getRequest();
		    HttpSession session = request.getSession();
		    String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
			
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+fromyear+"-10-01' AND '"+fromyear+"-10-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount=amount+rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}


	public double getMonthlySaleReportOFNOV()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
			HttpServletRequest request = ServletActionContext.getRequest();
		    HttpSession session = request.getSession();
		    String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
		
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+fromyear+"-11-01' AND '"+fromyear+"-11-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount=amount+rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}


	public double getMonthlySaleReportOFDEC()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
		

			HttpServletRequest request = ServletActionContext.getRequest();
		    HttpSession session = request.getSession();
		    String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
			
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+fromyear+"-12-01' AND '"+fromyear+"-12-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount=amount+ rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}

	public double getMonthlySaleReportOFJAN()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
			HttpServletRequest request = ServletActionContext.getRequest();
		    HttpSession session = request.getSession();
		    String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+toyear+"-01-01' AND '"+toyear+"-01-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount= amount+ rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}

	public double getMonthlySaleReportOFFEB()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+toyear+"-02-01' AND '"+toyear+"-02-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount=amount+ rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}

	public double getMonthlySaleReportOFMAR()throws DaoException ,SQLException
	{
	double amount=0.0;
		Connection con=null;
		try
		{
		
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String fromyear=session.getAttribute("curFyear").toString();
			String toyear=session.getAttribute("curToyear").toString();
			
			con=ConnectCore.getConnection();
			PreparedStatement pt=con.prepareStatement("SELECT DISTINCT billno, saleGrandTotal AS saleGrandTotal  FROM salemaster WHERE saleDoc BETWEEN '"+toyear+"-03-01' AND '"+toyear+"-03-31'");
			ResultSet rs=pt.executeQuery();
			while(rs.next())
			{
				amount= amount+ rs.getDouble("saleGrandTotal");
			}
		}
	 catch (Exception e) {
		System.out.println("Ex= " + e);
	} finally {
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("connection closed");
		}
	}
	return amount;	
	}


//-------&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& BAR CHART REPORT CODES  ends here   &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&		
	public List<SalesMasterDto> getAllSalesDetailsByBillNoOnly(String billno) throws DaoException, SQLException {
		List<SalesMasterDto> list = new LinkedList<SalesMasterDto>();
		Connection con = null;
		DecimalFormat df = new DecimalFormat("#.00");
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=null;
		PreparedStatement pstat=null;
		PreparedStatement ps=null;
		String query;
		String cid="";
		String taxableamount="";
		
	
		
		
		
		session=request.getSession();
		cid=(String)session.getAttribute("companyId");
		try {
			   
				con = ConnectCore.getConnection();	
				
	    
			//query= "SELECT st.salesId, st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal,st.itemSize,st.saleDoc,st.billno, st.tempCustName,c.cName FROM salemaster st, itemmaster i,customermaster c WHERE i.itemId=st.itemId AND st.companyId=? AND st.regcustomerId=c.customerId AND st.billno LIKE '"+billno+"'";   
            //query= "SELECT st.salesId, st.itemId,i.itemname,st.buyRate,SUM(st.saleQty) AS saleQty ,SUM(st.saleItemtotal) AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.phoneno ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=? AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.billno = '"+billno+"' GROUP BY itemSize,itemname";	

				query="SELECT s.amountreceived,"
						+ "s.amountreturn,s.itemId,s.afterdiscount,s.buyRate,s.saleQty,s.saleItemtotal,"
						+ "s.itemSize,s.saleDoc,s.basetotal,s.saleSubtotal,s.saleGrandTotal,s.billno,s.regcustomerId,"
						+ "s.tempCustName,s.saleLessUnit,s.saleDiscount,"
						+ "s.saleVat,s.saleLoryFreight,s.CustomerType,s.sdiscount2,s.sdiscountamt,s.gstrate,s.gstamt,"
						+ "s.cgstrate,s.cgstamt,s.sgstrate,s.sgstamt,s.igstrate,s.igstamt,s.hsno,"
						+ "c.nameofcompany,c.gsttin,c.phoneno AS CompanyPhone,c.address AS CompanyAddress ,c.email AS CompanyEmail,c.vatno,cs.cName,cs.address,cs.state,cs.pincode,cs.email, cs.phoneno ,DATE_FORMAT(cs.dateofcreate,'%d-%m-%Y ') AS customerDoc  FROM salemaster s JOIN companymaster c  JOIN customermaster cs  WHERE s.billno='"+billno+"'  AND s.companyId=c.companyId AND s.regcustomerId=cs.customerId";
				
				pstat = con.prepareStatement(query);
				
				System.out.println("ps="+pstat);
				ResultSet rs = pstat.executeQuery();
				
				while (rs.next()) {
					SalesMasterDto dto = new SalesMasterDto();
					dto.setBillno(billno);
					dto.setDoc(rs.getString("saleDoc"));
					
					dto.setQty(rs.getString("saleQty"));
					dto.setItemtotal(df.format(rs.getDouble("saleItemTotal"))+"");
					dto.setBuyRate(rs.getString("buyRate"));
					dto.setItemSizeName(rs.getString("itemSize"));
					dto.setItemId(rs.getString("itemId"));
//					dto.setSalesId(rs.getString("salesId"));
					
					dto.setSubtotal(rs.getString("saleSubtotal"));
					System.out.println(rs.getString("saleDiscount"));
					dto.setGrandTotal(rs.getString("saleGrandTotal"));
					dto.setLessDiscount(rs.getString("saleDiscount"));
					dto.setLessloryFreight(rs.getString("saleLoryFreight"));
					dto.setLessUnit(rs.getString("saleLessUnit"));
					dto.setVat(df.format(rs.getDouble("saleVat")));
					dto.setGrandTotRoundvalue(rs.getInt("saleGrandTotal"));
					dto.setGrandTotRoundvalueinDouble(rs.getDouble("saleGrandTotal"));
					double vat=rs.getDouble("saleVat")/100;
					double value2=(rs.getDouble("saleSubtotal")-(rs.getDouble("saleLessUnit")+rs.getDouble("saleDiscount")));
					double taxamount=value2*vat;
					dto.setTaxamount(Double.parseDouble(df.format(taxamount)));
					dto.setCustomerType(rs.getString("CustomerType"));
					dto.setRegcustomerId(rs.getString("regcustomerId"));
					
					dto.setItemName(getItemNameByItemId(rs.getString("itemId"), con));
					
					dto.setCompanyName(rs.getString("nameofcompany"));
					dto.setCompanyPhonenumber(rs.getString("CompanyPhone"));
					dto.setCompanyemail(rs.getString("CompanyEmail"));
					dto.setCompanyState("West Bengal");
					dto.setCompanyAddress(rs.getString("CompanyAddress"));
					dto.setCompanyVatNo(rs.getString("vatno"));
					
					dto.setCustaddress(rs.getString("address"));
					dto.setCustState(rs.getString("state"));
					dto.setCustpincode(rs.getString("pincode"));
					dto.setCustemail(rs.getString("email"));
					dto.setCustomerPhoneno(rs.getString("phoneno"));
					dto.setDoc(rs.getString("customerDoc"));
					dto.setAfterdiscount(rs.getString("afterdiscount"));
					dto.setAmountreceived(rs.getString("amountreceived"));
					dto.setAmountreturn(rs.getString("amountreturn"));
					
					dto.setSdiscount2(rs.getString("sdiscount2"));
					dto.setSdiscountamt(rs.getString("sdiscountamt"));
					dto.setGstrate(rs.getString("gstrate"));
					dto.setGstamt(rs.getString("gstamt"));
					dto.setCgstrate(rs.getString("cgstrate"));
					dto.setCgstamt(rs.getString("cgstamt"));
					dto.setSgstrate(rs.getString("sgstrate"));
					dto.setSgstamt(rs.getString("sgstamt"));
					dto.setIgstrate(rs.getString("igstrate"));
					dto.setIgstamt(rs.getString("igstamt"));
					dto.setHsncode(rs.getString("hsno"));
					dto.setGsttin(rs.getString("gsttin"));
					dto.setBasetotal(rs.getString("basetotal"));
					
					System.out.println("date formte---"+dto.getDoc());
					try{
						String tempCust=rs.getString("tempCustName");
						if(tempCust.equals(""))
						{
							dto.setCustomerName(rs.getString("cName"));	
						}
						else
						{
							dto.setCustomerName(rs.getString("tempCustName"));	
						}
					}
					catch(Exception f)
					{
						dto.setCustomerName(rs.getString("cName"));	
					}
				
					
					list.add(dto);
			        }
			
			
			
				
				if(pstat!=null)
				{
					pstat.close();
				}
			System.out.println("list size= "+list.size());
			if(list.size()==0){

				query= "SELECT st.salesId, st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,temCustMobileNo,st.tempCustAddress,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail FROM salemaster st, itemmaster i,companymaster co WHERE i.itemId=st.itemId AND st.companyId=? AND st.companyId=co.companyId AND st.billno ='"+billno+"'";	
				
				ps = con.prepareStatement(query);
				ps.setString(1, cid);
				System.out.println("ps="+ps);
				ResultSet rs1 = ps.executeQuery();
				
				while (rs1.next()) {
					SalesMasterDto dto = new SalesMasterDto();
					dto.setBillno(billno);
					dto.setDoc(rs1.getString("saleDoc"));
					dto.setItemName(rs1.getString("itemname"));
					dto.setQty(rs1.getString("saleQty"));
					dto.setItemtotal(rs1.getString("saleItemTotal"));
					dto.setBuyRate(rs1.getString("buyRate"));
					dto.setItemSizeName(rs1.getString("itemSize"));
					dto.setItemId(rs1.getString("itemId"));
					dto.setSalesId(rs1.getString("salesId"));
					
					dto.setRegcustomerId(rs1.getString("regcustomerId"));
					dto.setTempCustName(rs1.getString("tempCustName"));
					dto.setTemCustMobileNo(rs1.getString("temCustMobileNo"));
					dto.setTempCustAddress(rs1.getString("tempCustAddress"));
					
					dto.setCompanyName(rs1.getString("nameofcompany"));
					dto.setCompanyPhonenumber(rs1.getString("CompanyPhone"));
					dto.setCompanyemail(rs1.getString("CompanyEmail"));
					dto.setCompanyState("West Bengal");
					dto.setCompanyAddress(rs1.getString("CompanyAddress"));
					
					dto.setSubtotal(rs1.getString("saleSubtotal"));
					dto.setGrandTotal(rs1.getString("saleGrandTotal"));
					dto.setLessDiscount(rs1.getString("saleDiscount"));
					dto.setLessloryFreight(rs1.getString("saleLoryFreight"));
					dto.setLessUnit(rs1.getString("saleLessUnit"));
					dto.setVat(rs1.getString("saleVat"));
					dto.setGrandTotRoundvalue(rs1.getInt("saleGrandTotal"));
					double vat=rs1.getDouble("saleVat")/100;
					double value2=(rs1.getDouble("saleSubtotal")-(rs1.getDouble("saleLessUnit")+rs1.getDouble("saleDiscount")));
					double taxamount=value2*vat;
					
					dto.setTaxamount(taxamount);
					
					list.add(dto);
				}
				if(ps!=null)
				{
					ps.close();
				}
			}
			
			log.info("Details taken sucessfully !!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to retreive data from database", e);
		} finally {
			if(con!=null)
			{
				con.close();
				
				pstat.close();
				System.out.println("connection closed");
			}
		}
		return list;
	}
	
	
	
	
	public boolean convertExcelToJson(String filepath)
	{
		HttpServletRequest request=ServletActionContext.getRequest();
		
		boolean rs=false;
		try
		{
			String  file=filepath;
		    String jsonresult=genGsonData(file);

		     BufferedWriter bufferedWriter = null;
		        try {
		            String strContent = jsonresult;
		            
		            String savepath=request.getServletContext().getRealPath("/gstr/Excel.json");
		            
		            File myFile = new File(savepath);
		            // check if file exist, otherwise create the file before writing
		            if (!myFile.exists()) {
		                myFile.createNewFile();
		            }
		            Writer writer = new FileWriter(myFile);
		            bufferedWriter = new BufferedWriter(writer);
		            bufferedWriter.write(strContent);
		            rs=true;
		            
		        } catch (IOException e) {
		            e.printStackTrace();
		        } finally{
		            try{
		                if(bufferedWriter != null) bufferedWriter.close();
		            } catch(Exception ex){
		                 
		            }}

		}
		catch(Exception e)
		{
			System.out.println("Exception occur"+e);
		}
		
		return rs;
		
		
		
	}
	
	
	
	public static String genGsonData(String file) 
	{
	String jsonrs="";	
	try
	{
		FileInputStream inp = new FileInputStream( file );
		Workbook workbook = WorkbookFactory.create( inp );

		// Get the first Sheet.
		Sheet sheet = workbook.getSheetAt( 0 );

		    // Start constructing JSON.
		    JSONObject json = new JSONObject();

		    // Iterate through the rows.
		    JSONArray rows = new JSONArray();
		    for ( Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext(); )
		    {
		        Row row = rowsIT.next();
		        JSONObject jRow = new JSONObject();

		        // Iterate through the cells.
		        JSONArray cells = new JSONArray();
		        for ( Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext(); )
		        {
		            Cell cell = cellsIT.next();
		            cells.put( cell.getStringCellValue() );
		        }
		        jRow.put( "cell", cells );
		        rows.put( jRow );
		    }

		    // Create the JSON.
		    json.put( "rows", rows );
		    jsonrs=json.toString();
		// Get the JSON text.
		        
	}
	catch(Exception e)
	{
		System.out.println("Exception occur"+e);
	}
		return jsonrs;
	}
	
	
	
	

}