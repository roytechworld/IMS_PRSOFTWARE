package com.prsoftwares.daoImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.components.If;

import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dao.StockMasterDAO;
import com.prsoftwares.dto.OpenigAndClosingStockDTO;
import com.prsoftwares.dto.PurchaseLedgerDto;
import com.prsoftwares.dto.StockMasterDTO;
import com.prsoftwares.dto.SupplierMasterDTO;
import com.prsoftwares.util.CurrentCompAndUser;
import com.prsoftwares.util.CurrentDate;
import com.prsoftwares.util.CurrrentPreviousYear;


public class StockMasterDaoImpl extends ConnectCore implements StockMasterDAO {
protected static final Log log = LogFactory.getLog(SupplierMasterDAOIMPL.class);

//######################################################################################################################################################
//########################################## Stock Calucaltion Method ... Start here ###################################################################
//######################################################################################################################################################
public boolean saveStockDetails(List<StockMasterDTO> sDTOList)throws DaoException,SQLException
{
	boolean result=false;
	Connection con=null;
	PreparedStatement ps=null;
	PreparedStatement ps1=null;
	PreparedStatement ps5=null;
	PreparedStatement ps6=null;
	int flagCheckforStockUp=0;
	int x=0;
	try{
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String uid=(String)session.getAttribute("userId");
		uid=uid.trim();
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		con = ConnectCore.getConnection();
		
		String pDate=sDTOList.get(0).getPurchaseDate();
		int supplierId=sDTOList.get(0).getSupplierId();
		String gTot=sDTOList.get(0).getGrandTot();
		String billno=sDTOList.get(0).getBillNo();
		
		String query=saveStockDetailsQuery;
		ps = con.prepareStatement(query);
		for(int i=0;i<sDTOList.size();i++)
		{
			ps.setString(1, sDTOList.get(i).getBillNo());
			ps.setString(2, sDTOList.get(i).getChallanNo());
			ps.setInt(3, sDTOList.get(i).getSupplierId());
			ps.setString(4, sDTOList.get(i).getPurchaseDate());
			ps.setString(5,sDTOList.get(i).getItemIds());
			ps.setString(6,sDTOList.get(i).getItemSize());
			ps.setString(7,sDTOList.get(i).getQty());
			ps.setString(8,sDTOList.get(i).getBuyRate());
			ps.setString(9,sDTOList.get(i).getItemTotal());
			ps.setString(10,cid);
			ps.setString(11,uid);
			
			System.out.println("ps= "+ps);
			x=ps.executeUpdate();
			if(ps!=null)
			{
				
			}
			if(x>0)
			{
						String query1="insert into purchaseledgermaster values(default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					    ps1 = con.prepareStatement(query1);
						ps1.setString(1, sDTOList.get(i).getBillNo());
						ps1.setString(2, sDTOList.get(i).getChallanNo());
						ps1.setString(3, sDTOList.get(i).getPurchaseDate());
						ps1.setString(4,sDTOList.get(i).getItemIds());
						ps1.setString(5,sDTOList.get(i).getItemSize());
						ps1.setString(6,sDTOList.get(i).getQty());
						ps1.setString(7,sDTOList.get(i).getBuyRate());
						ps1.setString(8,sDTOList.get(i).getItemTotal());
						ps1.setString(9, "NA");
						ps1.setString(10,sDTOList.get(i).getSubTot());
						ps1.setString(11,sDTOList.get(i).getLessUnitScheme());
						ps1.setString(12,sDTOList.get(i).getDiscounts());
						ps1.setString(13,sDTOList.get(i).getVat());
						ps1.setString(14,sDTOList.get(i).getCst());
						ps1.setString(15,sDTOList.get(i).getWaybill());
						ps1.setString(16,sDTOList.get(i).getLorryFreight());
						ps1.setString(17,sDTOList.get(i).getGrandTot());
						ps1.setString(18, "0");
						ps1.setInt(19, sDTOList.get(i).getSupplierId());
						ps1.setString(20,uid);
						ps1.setString(21,cid);
						ps1.setString(22,"0");
						ps1.setString(23,sDTOList.get(i).getGstrate());
						ps1.setString(24,sDTOList.get(i).getGstamt());
						ps1.setString(25,sDTOList.get(i).getCgstrate());
						ps1.setString(26,sDTOList.get(i).getCgstamt());
						ps1.setString(27,sDTOList.get(i).getSgstrate());
						ps1.setString(28,sDTOList.get(i).getSgstamt());
						ps1.setString(29,sDTOList.get(i).getIgstrate());
						ps1.setString(30,sDTOList.get(i).getIgstamt());
						ps1.setString(31,sDTOList.get(i).getDiscountamt());
						System.out.println("ps1= "+ps1);
						int y=ps1.executeUpdate();
						if(ps1!=null)
						{
							
						}
						if(y>0){
								flagCheckforStockUp=1;
								result=true;
						}
				
			}else{
				result=false;
			}
			
		}
		
		if(result)
		{
			boolean checksupplierrecordexist=checkSupplierrPaymentRecordExistornot(supplierId+"", con);
			if(!checksupplierrecordexist)
			{
				
				String sqlr = "insert into supplierpaymentrecord values(default,?,?,?,?,?,?,?,?,?,?,?,?,?)";
              
				PreparedStatement ps2=con.prepareStatement(sqlr);
				ps2=con.prepareStatement(sqlr);
                ps2.setString(1, supplierId+"");
                ps2.setString(2,sDTOList.get(0).getPurchaseDate());
                ps2.setString(3, "0");
                ps2.setString(4,sDTOList.get(0).getGrandTot());
              
                ps2.setString(5,"Partial");
                ps2.setString(6, "NA");
                ps2.setString(7, "1");
                ps2.setString(8, "1");
                ps2.setString(9, "NA");
                ps2.setString(10, "NA");
                ps2.setString(11, "NA");
                ps2.setString(12, sDTOList.get(0).getGrandTot());
                ps2.setString(13, (String)session.getAttribute("fYear"));
                int n=ps2.executeUpdate();	
				
				if(n>0)
				{
					result=true;
				}
				
			}
		}
	
		if(ps1!=null)
		{
			ps1.close();
			System.out.println("Prepare Statement connection closed");
		}if(ps!=null)
		{
			ps.close();	
			System.out.println("Prepare Statement connection closed");
		}

	}catch(Exception e){
		System.out.println("Exception: "+e);
		e.printStackTrace();
	}finally{
		if(con!=null)
		{
		con.close();
		System.out.println("connection closed");
		}
	}
	return result;	
}
	
	
	public boolean checkSupplierrPaymentRecordExistornot(String supplierid,Connection con) throws SQLException
	{
		
		boolean returresult=false;
		
		String query="SELECT * FROM supplierpaymentrecord WHERE supplierid='"+supplierid+"'";	
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs=ps.executeQuery();
		try
		{
		while(rs.next())
		{
			returresult=true;
		}
		}
		catch(Exception er)
		{System.out.println("Exception occur"+er);}

		return returresult;
		
	}
public int getMaxStockId()throws DaoException,SQLException
{
	int stockId=0;
	Connection con=null;
	PreparedStatement ps=null;
	try
	{con = ConnectCore.getConnection();
	String query="SELECT MAX(stockid)AS stockid FROM stockmaster";	
	ps = con.prepareStatement(query);
	ResultSet rs=ps.executeQuery();
	while(rs.next())
	{stockId=Integer.parseInt(rs.getString("stockid"));}}
	catch(Exception er)
	{System.out.println("Exception occur"+er);}
	finally
	{
		if(con!=null)
		{
			con.close();
			ps.close();
			System.out.println("connection closed");
		}
	}
	return stockId;
}

public List<StockMasterDTO> getAllStockDetails() throws DaoException, SQLException {
	List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
	Connection con = null;
	PreparedStatement pstat=null;
	try {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=null;
		
		String query;
		String cid="";
		try
		{
		 session=request.getSession();
		 cid=(String)session.getAttribute("companyId");
		 query = "SELECT DISTINCT s.billNo ,s.purchaseDate,s.supplierId ,sup.SUPPLIERNAME  FROM stockmaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId  WHERE s.companyId=?   AND s.purchaseDate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'   ORDER BY  s.stockid DESC"; 
		 
//		 SELECT DISTINCT s.billNo ,s.ldate,s.supplierId ,sup.SUPPLIERNAME  FROM purchaseledgermaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId  WHERE s.companyId=1  AND s.ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '2016-08-01'   ORDER BY  s.ledgerId DESC
		}
		catch(NullPointerException n)
		{
		 query = "SELECT i.itemId,i.itemname,s.SUPPLIERNAME,st.buyRate,st.qty,st.itemTotal,st.purchaseDate,st.invoiceNumber,st.companyId,st.stockid,st.subTot,st.lessUnitScheme,st.discounts,st.vat,st.lorryFreight,st.invoiceNumber,st.userId,st.billNo FROM itemmaster i JOIN stockmaster st ON i.itemId=st.itemIds JOIN suppliermaster s ON s.SupplierId=st.supplierId JOIN companymaster c ON c.companyId=st.companyId JOIN usermaster u ON u.userID=st.userId  ";
		}
		con = ConnectCore.getConnection();
		pstat = con.prepareStatement(query);
		pstat.setString(1, cid);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			StockMasterDTO dto = new StockMasterDTO();
			dto.setBillNo(rs.getString("billNo"));
			dto.setPurchaseDate(rs.getString("purchaseDate"));
			dto.setSuppliername(rs.getString("SUPPLIERNAME"));
			dto.setSupplierId(rs.getInt("supplierId"));
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
			System.out.println("connection closed");
		}
	}
	return list;
}




public List<StockMasterDTO> getAllPurchaseDetails() throws DaoException, SQLException {
	List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
	Connection con = null;
	PreparedStatement pstat=null;
	try {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=null;
		
		String query;
		String cid="";
		try
		{
		 session=request.getSession();
		 cid=(String)session.getAttribute("companyId");
		 query = "SELECT DISTINCT s.billNo ,s.supplierId ,sup.SUPPLIERNAME  FROM purchaseledgermaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId  WHERE s.companyId=?   AND ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'   ORDER BY  s.ledgerId DESC"; 
		}
		catch(NullPointerException n)
		{
		 query = "SELECT i.itemId,i.itemname,s.SUPPLIERNAME,st.buyRate,st.qty,st.itemTotal,st.purchaseDate,st.invoiceNumber,st.companyId,st.stockid,st.subTot,st.lessUnitScheme,st.discounts,st.vat,st.lorryFreight,st.invoiceNumber,st.userId,st.billNo FROM itemmaster i JOIN stockmaster st ON i.itemId=st.itemIds JOIN suppliermaster s ON s.SupplierId=st.supplierId JOIN companymaster c ON c.companyId=st.companyId JOIN usermaster u ON u.userID=st.userId  ";
		}
		con = ConnectCore.getConnection();
		pstat = con.prepareStatement(query);
		pstat.setString(1, cid);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			StockMasterDTO dto = new StockMasterDTO();
			dto.setBillNo(rs.getString("billNo"));
			
			dto.setSuppliername(rs.getString("SUPPLIERNAME"));
			dto.setSupplierId(rs.getInt("supplierId"));
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
			System.out.println("connection closed");
		}
	}
	return list;
}


@Override
public List<SupplierMasterDTO> getAllSupplierFromStock() throws DaoException, SQLException {
	List<SupplierMasterDTO> list = new LinkedList<SupplierMasterDTO>();
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
		query = "SELECT DISTINCT st.supplierId,s.SUPPLIERNAME FROM stockmaster ST, suppliermaster s WHERE s.SupplierId=st.supplierId AND st.companyId=?";
		
		con = ConnectCore.getConnection();
		pstat = con.prepareStatement(query);
		pstat.setString(1, cid);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			SupplierMasterDTO dto = new SupplierMasterDTO();
			dto.setSUPPLIERNAME(rs.getString("SUPPLIERNAME"));
			dto.setSupplierId(rs.getInt("supplierId"));
			System.out.println("id= "+dto.getSupplierId());
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

public List<StockMasterDTO> getAllStockDetailsByBillNoANDSupplierID(String billno,String supplierId) throws DaoException, SQLException 
{
	
	
	List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
	Connection con = null;
	PreparedStatement pstat=null;
	try {
		String query;
//	    query="	SELECT st.stockid, i.itemId,i.itemname,s.SUPPLIERNAME,	st.buyRate,st.qty,st.itemTotal,st.itemSize,st.purchaseDate,	st.billNo,st.companyId,u.userId,u.userName FROM itemmaster i, stockmaster st, suppliermaster s, usermaster u WHERE i.itemId=st.itemIds AND s.SupplierId=st.supplierId AND u.userID=st.userId AND st.companyId=? AND billNo=? AND s.supplierId=?";
	    query="	SELECT st.stockid, i.itemId,i.itemname,s.SUPPLIERNAME,	st.buyRate,st.qty,st.itemTotal,st.itemSize,st.purchaseDate,	st.billNo,st.companyId,u.userId,u.userName FROM itemmaster i, stockmaster st, suppliermaster s, usermaster u WHERE i.itemId=st.itemIds AND s.SupplierId=st.supplierId AND u.userID=st.userId AND st.companyId=? AND billNo=? ";
	    con = ConnectCore.getConnection();
		pstat = con.prepareStatement(query);
		pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
		pstat.setString(2, billno);
//		pstat.setString(3, supplierId);
		ResultSet rs = pstat.executeQuery();
		
		while (rs.next()) {
			StockMasterDTO dto = new StockMasterDTO();
			dto.setBillNo(rs.getString("billNo"));
			dto.setPurchaseDate(rs.getString("purchaseDate"));
			dto.setItemname(rs.getString("itemname"));
			dto.setQty(rs.getString("qty"));
			dto.setItemTotal(rs.getString("itemTotal"));
			dto.setBuyRate(rs.getString("buyRate"));
			dto.setItemSize(rs.getString("itemSize"));
			
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
			System.out.println("connection closed");
		}
	}
	return list;
	
}
public List<StockMasterDTO> getAllStockDetailsByBillNo(String billno) throws DaoException, SQLException {
	List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
	Connection con = null;
	PreparedStatement pstat=null;
	try {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=null;
		String query;
		String cid="";
		session=request.getSession();
		cid=(String)session.getAttribute("companyId");
	    query="	SELECT st.stockid, i.itemId,i.itemname,s.SUPPLIERNAME,	st.buyRate,st.qty,st.itemTotal,st.itemSize,st.purchaseDate,	st.billNo,st.companyId,u.userId,u.userName FROM itemmaster i, stockmaster st, suppliermaster s, usermaster u WHERE i.itemId=st.itemIds AND s.SupplierId=st.supplierId AND u.userID=st.userId AND st.companyId=? AND billNo=?";
		con = ConnectCore.getConnection();
		pstat = con.prepareStatement(query);
		pstat.setString(1, cid);
		pstat.setString(2, billno);
		ResultSet rs = pstat.executeQuery();
		
		while (rs.next()) {
			StockMasterDTO dto = new StockMasterDTO();
			dto.setBillNo(rs.getString("billNo"));
			dto.setPurchaseDate(rs.getString("purchaseDate"));
			dto.setItemname(rs.getString("itemname"));
			dto.setQty(rs.getString("qty"));
			dto.setItemTotal(rs.getString("itemTotal"));
			dto.setBuyRate(rs.getString("buyRate"));
			dto.setItemSize(rs.getString("itemSize"));
			
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
			System.out.println("connection closed");
		}
	}
	return list;
}

@Override
public List<StockMasterDTO> getAllItemValuesGroupByItemIds() throws DaoException,SQLException
{
	List<StockMasterDTO> list=new ArrayList<StockMasterDTO>();
	Connection con = null;
	PreparedStatement pstat=null;
	HttpServletRequest request=ServletActionContext.getRequest();
	HttpSession session=null;
	session=request.getSession();
	String cid=(String)session.getAttribute("companyId");
	try
	{
		con=ConnectCore.getConnection();
		String aql="SELECT distinct itemIds  FROM stockmaster WHERE companyId=?   GROUP BY itemIds";
		pstat = con.prepareStatement(aql);
		pstat.setString(1, cid);
		ResultSet rs = pstat.executeQuery();
		
		while (rs.next()) {
			StockMasterDTO dto = new StockMasterDTO();
			dto.setItemIds(rs.getString("itemIds"));
			dto.setItemname(getItemname(rs.getString("itemIds"), con));
			list.add(dto);
		}
	}
	catch (Exception e) {
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
public String  getItemname(String Itemid,Connection con)
{
	
String totsaleDiscount="";
	int flagCheck=0;
	try
	{
		
		String query="SELECT itemname FROM itemmaster WHERE itemId='"+Itemid+"'  and userId='1'";
		PreparedStatement ps=con.prepareStatement(query);
	
		ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				totsaleDiscount=rs.getString("itemname");
				flagCheck=1;
			}

	}
	catch(Exception e)
	{
	
		totsaleDiscount="";
	}
	

	return totsaleDiscount;
	
}
@Override
public List<StockMasterDTO> getTotalItemStock(String itemId) throws DaoException,SQLException
{
	List<StockMasterDTO> list=new ArrayList<StockMasterDTO>();
	Connection con = null;
	HttpServletRequest request=ServletActionContext.getRequest();
	HttpSession session=null;
	session=request.getSession();
	PreparedStatement pstat=null;
	String cid=(String)session.getAttribute("companyId");
	try
	{
		con=ConnectCore.getConnection();
//		String aql="SELECT itemIds,itemSize,SUM(qty)AS StockQuantity  FROM stockmaster WHERE itemIds=? AND companyId=? GROUP BY itemSize";
		String aql="SELECT purchaseDate,itemIds,itemSize,SUM(qty)AS StockQuantity,buyrate ,SUM(itemTotal)AS itemTotal FROM stockmaster WHERE itemIds=? AND companyId=?  GROUP BY itemSize,buyRate";
		
		
		pstat = con.prepareStatement(aql);
		pstat.setString(1, itemId);
		pstat.setString(2, cid);
		System.out.println("ps="+pstat);
		ResultSet rs = pstat.executeQuery();
		
		while (rs.next()) {
			StockMasterDTO dto = new StockMasterDTO();
			dto.setItemIds(rs.getString("itemIds"));
			dto.setItemSize(rs.getString("itemSize"));
			dto.setQty(rs.getString("StockQuantity"));
			dto.setPurchaseDate(rs.getString("purchaseDate"));
			dto.setBuyRate(rs.getString("buyrate"));
			dto.setItemTotal(rs.getString("itemTotal"));
					

			list.add(dto);
		}
	}
	catch (Exception e) {
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
public List<StockMasterDTO> stockDetailsByDate(String fromdate, String todate,String cstvat) throws DaoException, SQLException {
	List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
	Connection con = null;
	PreparedStatement pstat=null;
	try {
		String query;
		if (cstvat==null) {
			cstvat="";
		}
		
		if(cstvat.equals(""))
		{	
			query = "SELECT DISTINCT s.billNo ,s.purchaseDate,s.supplierId ,sup.SUPPLIERNAME  FROM stockmaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId  WHERE purchaseDate BETWEEN '"+fromdate.trim()+"' AND '"+todate.trim()+"' AND s.companyId='1'  ORDER BY  s.stockid DESC";
		}
		else
		{
			if(cstvat.equals("VAT"))
			{
				query = "SELECT DISTINCT s.billNo ,s.purchaseDate,s.supplierId ,sup.SUPPLIERNAME  FROM stockmaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId JOIN purchaseledgermaster p ON s.billNo=p.billNo WHERE purchaseDate BETWEEN '"+fromdate.trim()+"' AND '"+todate.trim()+"' AND s.companyId='1' AND p.vat <> '0.00'  ORDER BY  s.stockid DESC";
			}
			else
			{
				query = "SELECT DISTINCT s.billNo ,s.purchaseDate,s.supplierId ,sup.SUPPLIERNAME  FROM stockmaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId JOIN purchaseledgermaster p ON s.billNo=p.billNo WHERE purchaseDate BETWEEN '"+fromdate.trim()+"' AND '"+todate.trim()+"' AND s.companyId='1a' AND p.cst <> '0.00'  ORDER BY  s.stockid DESC";	
			}	
		}

		con = ConnectCore.getConnection();
		pstat = con.prepareStatement(query);
		System.out.println("ps="+pstat);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			StockMasterDTO dto = new StockMasterDTO();
			dto.setBillNo(rs.getString("billNo"));
			dto.setPurchaseDate(rs.getString("purchaseDate"));
			dto.setSuppliername(rs.getString("SUPPLIERNAME"));
			dto.setSupplierId(rs.getInt("supplierId"));
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
			System.out.println("connection closed");
		}
	}
	return list;
}

@Override
public List<StockMasterDTO> stockDetailsBySupId(String supplierId, String cstvat) throws DaoException, SQLException {
	List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
	Connection con = null;
	PreparedStatement pstat=null;
	try {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=null;
		
		String query;
		String cid="";
		session=request.getSession();
		cid=(String)session.getAttribute("companyId");
		if(cstvat==null)
		{
			cstvat="";
		}
		if(cstvat.equals(""))
		{
			query = "SELECT DISTINCT s.billNo ,s.purchaseDate,s.supplierId ,sup.SUPPLIERNAME  FROM stockmaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId  WHERE s.supplierId="+supplierId+" AND s.companyId=? AND s.purchaseDate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ORDER BY  s.stockid DESC";
		}
		else 
		{
			if(cstvat.equals("VAT"))
			{
				query="SELECT DISTINCT s.billNo ,s.purchaseDate,s.supplierId ,sup.SUPPLIERNAME  FROM stockmaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId JOIN purchaseledgermaster p ON s.billNo=p.billNo WHERE s.supplierId="+supplierId+" AND s.companyId=? AND s.purchaseDate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'  AND p.vat <> '0.00' ORDER BY  s.stockid DESC";	
			}
			else
			{
				query="SELECT DISTINCT s.billNo ,s.purchaseDate,s.supplierId ,sup.SUPPLIERNAME  FROM stockmaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId JOIN purchaseledgermaster p ON s.billNo=p.billNo WHERE s.supplierId="+supplierId+" AND  s.companyId=? AND s.purchaseDate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'  AND p.cst <> '0.00' ORDER BY  s.stockid DESC";		
			}
		}

		con = ConnectCore.getConnection();
		pstat = con.prepareStatement(query);
		pstat.setString(1, cid);
		System.out.println("ps="+pstat);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			StockMasterDTO dto = new StockMasterDTO();
			dto.setBillNo(rs.getString("billNo"));
			dto.setPurchaseDate(rs.getString("purchaseDate"));
			dto.setSuppliername(rs.getString("SUPPLIERNAME"));
			dto.setSupplierId(rs.getInt("supplierId"));
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
			System.out.println("connection closed");
		}
	}
	return list;
}

@Override
public List<StockMasterDTO> stockDetailsByDateAndSupId(String supplierId,String fromdate, String todate,String cstvat) throws DaoException, SQLException {
	List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
	Connection con = null;
	PreparedStatement pstat=null;
	try {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=null;
		
		String query;
		String cid="";
		session=request.getSession();
		cid=(String)session.getAttribute("companyId");
		if(cstvat==null){
			cstvat="";
		}
		
		if(cstvat.equals(""))
		{
			query = "SELECT DISTINCT s.billNo ,s.purchaseDate,s.supplierId ,sup.SUPPLIERNAME  FROM stockmaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId  WHERE purchaseDate BETWEEN '"+fromdate+"' AND '"+todate+"' AND s.supplierId="+supplierId+" AND s.companyId=?   ORDER BY  s.stockid DESC";
		}
		else
		{
			if(cstvat.equals("VAT"))
			{
				query = "SELECT DISTINCT s.billNo ,s.purchaseDate,s.supplierId ,sup.SUPPLIERNAME  FROM stockmaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId JOIN purchaseledgermaster p ON s.billNo=p.billNo   WHERE s.purchaseDate BETWEEN '"+fromdate+"' AND '"+todate+"' AND s.companyId=? AND s.supplierId="+supplierId+" AND p.vat <> '0.00'  ORDER BY  s.stockid DESC";
			}
			else
			{
				query = "SELECT DISTINCT s.billNo ,s.purchaseDate,s.supplierId ,sup.SUPPLIERNAME  FROM stockmaster s JOIN suppliermaster sup ON s.supplierId =sup.supplierId JOIN purchaseledgermaster p ON s.billNo=p.billNo   WHERE s.purchaseDate BETWEEN '"+fromdate+"' AND '"+todate+"' AND s.companyId=? AND s.supplierId="+supplierId+" AND p.cst <> '0.00'  ORDER BY  s.stockid DESC";
			}	
		}
		con = ConnectCore.getConnection();
		pstat = con.prepareStatement(query);
		pstat.setString(1, cid);
		System.out.println("ps="+pstat);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			StockMasterDTO dto = new StockMasterDTO();
			dto.setBillNo(rs.getString("billNo"));
			dto.setPurchaseDate(rs.getString("purchaseDate"));
			dto.setSuppliername(rs.getString("SUPPLIERNAME"));
			dto.setSupplierId(rs.getInt("supplierId"));
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
			System.out.println("connection closed");
		}
	}
	return list;
}


@Override
public List<StockMasterDTO> getAllBillDetails() throws DaoException, SQLException {
	List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
	Connection con = null;
	PreparedStatement pstat=null;
	String query;
	String cid="";
	HttpSession session=null;
	try {
			HttpServletRequest request=ServletActionContext.getRequest();
			con = ConnectCore.getConnection();
			session=request.getSession();
			cid=(String)session.getAttribute("companyId");
			query = "SELECT DISTINCT billNo,purchaseDate FROM stockmaster WHERE companyId=? AND purchaseDate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ";
			pstat = con.prepareStatement(query);
			pstat.setString(1, cid);
			System.out.println("ps="+pstat);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				StockMasterDTO dto = new StockMasterDTO();
				dto.setBillNo(rs.getString("billNo"));
				dto.setPurchaseDate(rs.getString("purchaseDate"));
				list.add(dto);
			}
			if (pstat!=null) {
				rs.close();
				pstat.close();
			}
			System.out.println("list size= "+list.size());
			
			log.info("Details taken sucessfully !!");
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
//######################################################################################################################################################
//########################################## Stock Calucaltion Method ... End here ###################################################################
//######################################################################################################################################################

//###############################################################################################################################################################################################################
//######################################################################################### Invoice edit function start here ####################################################################################
//###############################################################################################################################################################################################################	
	
public boolean editInvoiceNo(String Oldinvoiceno,String NewInvoiceNo) throws DaoException, SQLException 
{
	String updatePurchaseMaster="UPDATE stockmaster SET billNo='"+NewInvoiceNo+"' WHERE billNo='"+Oldinvoiceno+"' AND companyId='"+CurrentCompAndUser.getCurrentCompany()+"'";
	String updatePurchaseLedgermaster="UPDATE purchaseledgermaster SET billNo='"+NewInvoiceNo+"' WHERE billNo='"+Oldinvoiceno+"' AND companyId='"+CurrentCompAndUser.getCurrentCompany()+"'";
	String debitnote="UPDATE debitnote SET billNo='"+NewInvoiceNo+"' WHERE billNo='"+Oldinvoiceno+"' AND companyId='"+CurrentCompAndUser.getCurrentCompany()+"'";
	String supplierpayment="UPDATE supplierpayment SET purchaseBillNo='"+NewInvoiceNo+"' WHERE purchaseBillNo='"+Oldinvoiceno+"' AND companyId='"+CurrentCompAndUser.getCurrentCompany()+"'";
	
	Connection con=null;
	boolean result=false;
	try
	{	
		con=getConnection();	
		PreparedStatement updateStock=con.prepareStatement(updatePurchaseMaster);
		int i=updateStock.executeUpdate();
		
		if(i>0)
		{
			System.out.println("#######################################################################");
			System.out.println("############### Stock Invoice update successfully ####################");
			System.out.println("#######################################################################");
			PreparedStatement updatePurchaseLedger=con.prepareStatement(updatePurchaseLedgermaster);
			int j=updatePurchaseLedger.executeUpdate();
			
			if(j>0)
			{
				System.out.println("###############################################################################");
				System.out.println("############### Purchase ledger master update successfully ####################");
				System.out.println("###############################################################################");	
				PreparedStatement supplierpaymentstat=con.prepareStatement(supplierpayment);
				int k=supplierpaymentstat.executeUpdate();
				
				if(k>0)
				{
					System.out.println("###############################################################################");
					System.out.println("############### Supplier payment master update successfully ####################");
					System.out.println("###############################################################################");
					PreparedStatement debitnotepreparestat =con.prepareStatement(debitnote);
					
					int l=debitnotepreparestat.executeUpdate();
					if(l>0)
					{
						System.out.println("###############################################################################");
						System.out.println("############### Debit note  master update successfully ####################");
						System.out.println("###############################################################################");	
						result=true;	
					}
				}
			}
		}
	}
	catch(Exception e)
	{
		System.out.println("Exception occur type is:"+e);
	}
	finally
	{
		if(con!=null)
		{
			con.close();
			result=true;
			System.out.println("connection closed");
		}
	}
	
	return result;
}
//###############################################################################################################################################################################################################
//######################################################################################### Invoice edit function start here ####################################################################################

//#####################################################################################################################################################################################################################################################################################################################
//############################################################################################ Excel export for purchase list start here ##############################################################################################################################################################################
//#####################################################################################################################################################################################################################################################################################################################

@Override
public List<PurchaseLedgerDto> exportToexcelPurchaseDetails(String fromdate, String todate,String supplierId,String cstvat) throws DaoException, SQLException 
{
	List<PurchaseLedgerDto> list = new ArrayList<PurchaseLedgerDto>();
	Connection con = null;
	PreparedStatement pstat=null;
	String purchaseLedgerSql="";
	int flagforTitle=0;
	DecimalFormat df= new DecimalFormat("0.00");
	if(!fromdate.equals("") && !todate.equals("")&& supplierId.equals("0"))
	{
		purchaseLedgerSql="SELECT * FROM purchaseledgermaster WHERE ldate BETWEEN '"+fromdate+"' AND '"+todate+"' GROUP BY billNo  ORDER BY supplierId";	
	flagforTitle=1;
	}
	else if(Integer.parseInt(supplierId)>0 && !fromdate.equals("") && !todate.equals(""))
	{
		purchaseLedgerSql="SELECT * FROM purchaseledgermaster WHERE supplierId='"+supplierId+"' AND ldate BETWEEN '"+fromdate+"' AND '"+todate+"' GROUP BY billNo  ORDER BY ldate ASC";	
		flagforTitle=2;
	}
	
	
	
	
	else if(Integer.parseInt(supplierId)>0 && fromdate.equals("") && todate.equals("")&& !cstvat.equals(""))
	{
		
		if(cstvat.equals("VAT"))
		{
			purchaseLedgerSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId ,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.vat<>'0.00' AND ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ORDER BY b.ldate ASC";	
		}
		else
		{
			purchaseLedgerSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId ,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.cst<>'0.00' AND ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ORDER BY b.ldate ASC";	
		}
		flagforTitle=6;
	}
	else if(!fromdate.equals("") && !todate.equals("") && supplierId.equals("0") && cstvat.equals(""))
	{
		purchaseLedgerSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' ORDER BY b.ldate ASC";
		flagforTitle=3;
	}
	
	else if(fromdate.equals("") && todate.equals("") && supplierId.equals("0") && !cstvat.equals(""))
	{
		
		if(cstvat.equals("VAT"))
		{
		purchaseLedgerSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.vat<>'0.00' AND ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ORDER BY b.ldate ASC";	
		}
		else
		{
		purchaseLedgerSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.cst<>'0.00' AND ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ORDER BY b.ldate ASC";		
		}
		
		flagforTitle=4;
	}
	else if(!fromdate.equals("") && !todate.equals("") && Integer.parseInt(supplierId)>0 && !cstvat.equals(""))
	{
		
		if(cstvat.equals("VAT"))
		{
		purchaseLedgerSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.vat<>'0.00' ORDER BY b.ldate ASC";
		}
		else
		{
		purchaseLedgerSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.supplierId='"+supplierId+"' AND b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND b.cst<>'0.00' ORDER BY b.ldate ASC";	
		}
		flagforTitle=7;
	}
	
	else if(!fromdate.equals("") && !todate.equals("") && supplierId.equals("0") && !cstvat.equals(""))
	{
		
		if(cstvat.equals("VAT"))
		{
			purchaseLedgerSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"'  AND b.vat<>'0.00' ORDER BY b.ldate ASC";
		}
		else
		{
			purchaseLedgerSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId where b.ldate between '"+fromdate+"' And '"+todate+"' And b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"'  AND b.cst<>'0.00' ORDER BY b.ldate ASC";	
		}
		flagforTitle=8;
	}
	else 
	{
		purchaseLedgerSql="SELECT b.billNo,b.challanNo,b.ldate,b.itemId,i.itemname,b.itemSize,b.qty,b.buyrate,b.supplierId,s.SUPPLIERNAME,b.itemTot,b.cst,b.waybill,b.vat FROM purchaseledgermaster b JOIN itemmaster i ON b.itemId=i.itemId JOIN suppliermaster s ON b.supplierId=s.SupplierId AND b.companyId='"+CurrentCompAndUser.getCurrentCompany()+"' AND ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ORDER BY b.ldate ASC";
		flagforTitle=5;
	}
	try {
	con=ConnectCore.getConnection();
	pstat=con.prepareStatement(purchaseLedgerSql);
	ResultSet rs=pstat.executeQuery();
	while(rs.next())
	{
		double basetotamt=0;
		double finalbasetot=0.0;
		double totfinal=0.0;
		double grandtot=0.0;
		
		PurchaseLedgerDto pd=new PurchaseLedgerDto();
		pd.setBillNo(rs.getString("billNo"));
		pd.setPurchaseDate(rs.getString("ldate"));
        basetotamt=Double.parseDouble(rs.getString("subTot")) * Double.parseDouble(rs.getString("discounts"))/100;
        
        finalbasetot=Double.parseDouble(rs.getString("subTot"))-basetotamt;
        
        totfinal=Double.parseDouble(rs.getString("lorryFreight"))+ finalbasetot;
		pd.setSubtotalbegoregst(new DecimalFormat("##.##").format(totfinal)+"");
		
		grandtot=Double.parseDouble(rs.getString("gstamt"))+totfinal;
		
		
		
		
		
		
		
		
		
		
		pd.setSuppliergsttinno(getSupplierGSTTIN(rs.getString("supplierId"), con));
		pd.setGstrate(rs.getString("gstrate"));
		pd.setGstamt(rs.getString("gstamt"));
		pd.setCgstrate(rs.getString("cgstrate"));
		pd.setCgstamt(rs.getString("cgstamt"));
		pd.setSgstrate(rs.getString("sgstrate"));
		pd.setSgstamt(rs.getString("sgstamt"));
		pd.setIgstrate(rs.getString("igstrate"));
		pd.setIgstamt(rs.getString("igstamt"));
		pd.setTotalamount(new DecimalFormat("##.##").format(grandtot)+"");
		pd.setDiscounts(rs.getString("discounts"));
		pd.setSubTot(rs.getString("subTot"));
		pd.setDiscamt( new DecimalFormat("##.##").format(basetotamt)+"");
		
		pd.setSupplierName(getSupplierName(rs.getString("supplierId"), con));
		if(flagforTitle==4)
		{
			pd.setTitleforExcel("TOTAL PURCHASE REPORT "+cstvat+"% only");
		}
		else if(flagforTitle==2)
		{
			pd.setTitleforExcel("TOTAL PURCHASE  DETAILS FROM SUPPLIER   "+getSupplierName(rs.getString("supplierId"), con));	
		}
		else if(flagforTitle==3)
		{
			pd.setTitleforExcel("TOTAL PURCHASE  DETAILS FROM SUPPLIER   "+getSupplierName(rs.getString("supplierId"), con) +" FROM DATE "+fromdate+" TO DATE "+todate);	
		}
		else if(flagforTitle==1)
		{
			pd.setTitleforExcel("TOTAL PURCHASE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate);	
		}
		else if(flagforTitle==5)
		{
			pd.setTitleforExcel("TOTAL PURCHASE REPORT ");
		}
		else if(flagforTitle==6)
		{
			pd.setTitleforExcel("TOTAL PURCHASE  DETAILS FROM SUPPLIER   "+getSupplierName(rs.getString("supplierId"), con)+" "+cstvat+" only");
		}
		else if(flagforTitle==7)
		{
			pd.setTitleforExcel("TOTAL PURCHASE  DETAILS FROM SUPPLIER   "+getSupplierName(rs.getString("supplierId"), con) +" FROM DATE "+fromdate+" TO DATE "+todate +" "+cstvat+" only");	
		}
		
		else if(flagforTitle==8)
		{
			pd.setTitleforExcel("TOTAL PURCHASE  DETAILS FROM DATE  "+fromdate+" TO DATE "+todate+" "+cstvat+" only");	
		}
		list.add(pd);
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


public String getSupplierName(String supplierid,Connection cons) throws DaoException,SQLException
{
	
	String val="";
	try
	{
		
		PreparedStatement st=cons.prepareStatement("select * from suppliermaster where SupplierId='"+supplierid+"'");
		ResultSet rs=st.executeQuery();
		while(rs.next())
		{
			val=rs.getString("SUPPLIERNAME");
		}
		st.close();
	}
	catch(Exception e)
	{
		val="";
		System.out.println("error occur"+e);
	}
	
	return val;
	
}


public String getSupplierGSTTIN(String supplierid,Connection cons) throws DaoException,SQLException
{
	
	String val="";
	try
	{
		
		PreparedStatement st=cons.prepareStatement("select * from suppliermaster where SupplierId='"+supplierid+"'");
		ResultSet rs=st.executeQuery();
		while(rs.next())
		{
			val=rs.getString("VATNo");
		}
		st.close();
	}
	catch(Exception e)
	{
		val="";
		System.out.println("error occur"+e);
	}
	
	return val;
	
}





//#####################################################################################################################################################################################################################################################################################################################
//############################################################################################ Excel export for purchase list start here ##############################################################################################################################################################################
//#####################################################################################################################################################################################################################################################################################################################


//____________________________________________ Debit Note Calculation Start here ____________________________________________________________

public String stockQty(String itemId,String itemSize,Connection cons) throws DaoException,SQLException
{
	Connection con=null;
	String qty="";
	try
	{
		con=cons;
		PreparedStatement st=con.prepareStatement("select qty from stockmaster where itemIds='"+itemId+"' and itemSize='"+itemSize+"'");
		ResultSet rs=st.executeQuery();
		while(rs.next())
		{
			qty=rs.getString("qty");
		}
		st.close();
	}
	catch(Exception e)
	{
		System.out.println("error occur"+e);
	}
	
	return qty;
	
}

public String stockID(String itemId,String itemSize,Connection cons) throws DaoException,SQLException
{
	Connection con=null;
	String stockid="";
	try
	{
		con=cons;
		PreparedStatement st=con.prepareStatement("select stockid from stockmaster where itemIds='"+itemId+"' and itemSize='"+itemSize+"'");
		ResultSet rs=st.executeQuery();
		while(rs.next())
		{
			stockid=rs.getString("stockid");
		}
		
		st.close();
	}
	catch(Exception e)
	{
		System.out.println("error occur"+e);
	}
	
	return stockid;
	
}

@Override
public List<StockMasterDTO> getStockDetailsBySuppplierId(String billno ,String supplierId) throws DaoException, Exception {
	
	List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
	Connection con = null;
	PreparedStatement ps=null;
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String cid=(String)session.getAttribute("companyId");
		try
		{
			con=ConnectCore.getConnection();
			String sql=queryForDisplayingStockItems;
			System.out.println("query "+sql);
			ps=con.prepareStatement(sql);
			ps.setString(1, billno);
			ps.setInt(2, Integer.parseInt(cid));
			ps.setString(3, supplierId);
			System.out.println("ps= "+ps);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				StockMasterDTO stkDto=new StockMasterDTO();
				
				stkDto.setItemIds(rs.getString("itemId"));
				stkDto.setItemname(rs.getString("itemname"));
				stkDto.setBuyRate(rs.getString("buyRate"));
				stkDto.setItemTotal(rs.getString("itemTot"));
				stkDto.setPurchaseDate(rs.getString("ldate"));
				stkDto.setBillNo(rs.getString("billNo"));
				stkDto.setUsername(rs.getString("userName"));
				stkDto.setVat(rs.getString("vat"));
				stkDto.setCst(rs.getString("cst"));
				stkDto.setWaybill(rs.getString("waybill"));
				stkDto.setItemSize(rs.getString("itemSize"));
				//-------------------------------------- Fetching stock quantity and stock ID from Stockmaster table-------------------------------
				stkDto.setQty(stockQty(rs.getString("itemId"), rs.getString("itemSize"),con));
				stkDto.setStockid(stockID(rs.getString("itemId"), rs.getString("itemSize"),con));
				//-------------------------------------- Fetching stock quantity and stock ID from Stockmaster table-------------------------------
				
				
				System.out.println("id= "+stkDto.getSupplierId());
				list.add(stkDto);
			}	System.out.println("size= "+list.size());
		} catch(Exception e){
			System.out.println("Ex= "+e);
		}finally{
			if(con!=null)
			{
				ps.close();
				con.close();
			System.out.println("connection closed");
			}
		}
	return list;
}

	
	@Override
	public List<StockMasterDTO> getDNListBySuppplierId(int supplierId) throws DaoException, Exception {
		
		List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String query;
			PreparedStatement ps=null;
			String cid=(String)session.getAttribute("companyId");
			try
			{
				con=ConnectCore.getConnection();
				String sql=getDebitNoteListBySupplier;
				System.out.println("query "+sql);
				
				ps=con.prepareStatement(sql);
				ps.setInt(1, supplierId);
				ps.setInt(2, Integer.parseInt(cid));
				System.out.println("ps= "+ps);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
					StockMasterDTO stkDto=new StockMasterDTO();
					
					stkDto.setVat(rs.getString("vat"));
					stkDto.setCst(rs.getString("cst"));
					stkDto.setWaybill(rs.getString("waybill"));
					stkDto.setBillNo(rs.getString("billNo"));
					stkDto.setDnNo(rs.getString("dnNo"));
					stkDto.setDnDoc(rs.getString("dnDoc"));
					stkDto.setSupplierId(rs.getInt("supplierId"));
					
					System.out.println("id= "+stkDto.getSupplierId());
					list.add(stkDto);
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
				con.close();
				ps.close();
				System.out.println("connection closed");
				}
			}
		return list;
	}
	
	@Override
	public List<StockMasterDTO> getDNDetailsBySuppplierId(int suplierId, String dn) throws DaoException, Exception {
		List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
		Connection con = null;
		PreparedStatement ps=null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			System.out.println("dn= "+dn);
			String cid=(String)session.getAttribute("companyId");
			try
			{
				con=ConnectCore.getConnection();
				String sql="SELECT dn.returnItemId,dn.returnItemSize,dn.returnItemQty,dn.buyRate,dn.returnItemTotal,dn.cnNo, i.itemname FROM debitnote dn, itemmaster i WHERE i.itemId=dn.returnItemId AND dn.dnNo LIKE '"+dn+"' AND dn.supplierId=? and dn.companyId=?";
				System.out.println("query "+sql);
				
				ps=con.prepareStatement(sql);
				ps.setInt(1, suplierId);
				ps.setInt(2, Integer.parseInt(cid));
				System.out.println("ps= "+ps);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
					StockMasterDTO stkDto=new StockMasterDTO();
					
					stkDto.setItemname(rs.getString("itemName"));
					stkDto.setBuyRate(rs.getString("buyRate"));
					stkDto.setReturnItemId(rs.getString("returnItemId"));
					stkDto.setReturnItemQty(rs.getInt("returnItemQty"));
					stkDto.setReturnItemTotal(rs.getString("returnItemTotal"));
					stkDto.setCnNo(rs.getString("cnNo"));
					stkDto.setItemSize(rs.getString("returnItemSize"));
						String sql1="SELECT DISTINCT pl.debitAmount FROM purchaseledgermaster pl WHERE pl.dnNo LIKE '"+dn+"' AND pl.supplierId=? AND pl.companyId=?";
						
						PreparedStatement ps1=con.prepareStatement(sql1);
						ps1.setInt(1, suplierId);
						ps1.setInt(2, Integer.parseInt(cid));
						System.out.println("ps=1 "+ps1);
						ResultSet rs1=ps1.executeQuery();
						while(rs1.next()){
							stkDto.setDnGrandTot(rs1.getString("debitAmount"));
						}
					System.out.println("id= "+stkDto.getSupplierId());
					list.add(stkDto);
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
				con.close();
				ps.close();
				System.out.println("connection closed");
				}
			}
		return list;
	}
	

	public List<StockMasterDTO> getBillNoBySuppplierId(int supplierId) throws DaoException, Exception 
	{
		List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
		Connection con = null;
		PreparedStatement ps=null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			
			String cid=(String)session.getAttribute("companyId");
			try
			{
				con=ConnectCore.getConnection();
				/*String query="SELECT DISTINCT billno FROM stockmaster WHERE supplierId=? AND  companyId=? AND purchaseDate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ";*/
				String query="SELECT DISTINCT billNo FROM purchaseledgermaster WHERE supplierId=? AND  companyId=? AND ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND billNo<>'0' ";
				System.out.println("query "+query);
				ps=con.prepareStatement(query);
				ps.setInt(1, supplierId);
				ps.setInt(2, Integer.parseInt(cid));
				System.out.println("ps= "+ps);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
					StockMasterDTO stkDto=new StockMasterDTO();
					stkDto.setBillNo(rs.getString("billNo"));
					list.add(stkDto);
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
				con.close();
				ps.close();
				System.out.println("connection closed");
				}
			}
		return list;
	}
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
//----------------------------------------------------------------------- Debit Note calculation main method ____ Start here --------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------		
	public boolean insertDebitNoteTableAndUpdateStockMaster(String[] uqty, String[] returnQuantityValue, String[] updateItemtot,String[] itemids,String[] itemSize, String returnSubtotal ,String returnGrandTot, String[] stockid, String[] newReturnTotal, StockMasterDTO sdto ,String[] buyRate) throws DaoException, Exception
	{
		boolean returnResult=false;
		String insertDnDetails="insert into debitnote(dnNo,dnIdentityCode,dnDoc,returnItemId,returnItemSize,returnItemQty,buyRate,supplierId,billNo,returnItemTotal,cnNo,userId,companyId)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String insertledgerDetails="insert into purchaseledgermaster values(default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String updateStockRecords="UPDATE stockmaster SET qty=?,itemtotal=? WHERE stockid=? AND companyId=?";

		String debitNoteNumber=new String();
		int debitNoteIdentityNumber=0;
		int flagcheckForLedgerEntry=0;
		int flagcheckForStockUpdate=0;
		Connection con = null;
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String cid=(String)session.getAttribute("companyId");
		String uid=(String)session.getAttribute("userId");
		
		for (int i = 0; i < uqty.length; i++) {
			System.out.println("qty= "+uqty[i]);
			System.out.println("updateItemTotal= "+updateItemtot[i]);
		}
		System.out.println("vat= "+sdto.getVat());
		System.out.println("cst= "+sdto.getCst());
		System.out.println("wb= "+sdto.getWaybill());
		try
		{
			con=ConnectCore.getConnection();
			debitNoteNumber=debitNoGenerateEngine(cid);
			debitNoteIdentityNumber=debitNoteIdentityGenerateEngine(cid);
			log.info("####################################################################  Processing....." );
			log.info("Debit Note number generated is : "+debitNoteNumber );
			log.info("######################################### Processing complete debit note number found.." );
			for(int i=0;i<stockid.length;i++)
			{
			    int checkReturnOrNot=Integer.parseInt(returnQuantityValue[i]);
				if(checkReturnOrNot>0)
				{
				PreparedStatement ps=con.prepareStatement(insertDnDetails);
				ps.setString(1, debitNoteNumber);
				ps.setInt(2, debitNoteIdentityNumber);
				ps.setDate(3,CurrentDate.mysqlDateConvertor(sdto.getDnDoc()) );
				ps.setString(4,itemids[i]);
				ps.setString(5,itemSize[i]);
				ps.setString(6,returnQuantityValue[i]);
				ps.setString(7,buyRate[i]);
				ps.setInt(8,sdto.getSupplierId());
				ps.setString(9, sdto.getBillNo());
				ps.setString(10, newReturnTotal[i]);
				ps.setString(11,"NA");
				ps.setString(12,uid);
				ps.setString(13,cid);
				int x=ps.executeUpdate();
				if(x>0)
				{
					log.info("###########################################################");
					log.info("######  Debit Note Deatils Inserted Sucessfully");
					log.info("############################################################");
					PreparedStatement ps2=con.prepareStatement(insertledgerDetails);
				    ps2.setString(1, sdto.getBillNo());
				    ps2.setString(2, "NA");
				    ps2.setDate(3,CurrentDate.mysqlDateConvertor(sdto.getDnDoc()));
					ps2.setString(4,itemids[i]);
					ps2.setString(5,itemSize[i]);
					ps2.setString(6,returnQuantityValue[i]);
					ps2.setString(7,buyRate[i]);
					ps2.setString(8,newReturnTotal[i]);
					ps2.setString(9,debitNoteNumber);
					ps2.setString(10,returnSubtotal );
					ps2.setString(11, "0");
				    ps2.setString(12,"0");
				    ps2.setString(13,sdto.getVat());
				    ps2.setString(14,sdto.getCst());
				    ps2.setString(15,sdto.getWaybill());
				    ps2.setString(16,"0");
				    ps2.setString(17,"0");
				    ps2.setString(18,returnGrandTot);
				    ps2.setInt(19,sdto.getSupplierId());
				    ps2.setString(20,uid);
				    ps2.setString(21, cid);
				    ps2.setString(22, sdto.getTaxamount());
				    ps2.setString(23, "0");
				    ps2.setString(24, "0");
				    ps2.setString(25, "0");
				    ps2.setString(26, "0");
				    ps2.setString(27, "0");
				    ps2.setString(28, "0");
				    ps2.setString(29, "0");
				    ps2.setString(30, "0");
				    ps2.setString(31, "0");
				
				    int z=ps2.executeUpdate();
				    if(z>0)
				    {
				    	log.info("###########################################################");
						log.info("######  Ledger Deatils Inserted Sucessfully");
						log.info("############################################################");
						flagcheckForLedgerEntry=1;
				    }}}
			}
		
			if(flagcheckForLedgerEntry>0)
			{
				PreparedStatement ps3=con.prepareStatement(updateStockRecords);
				for(int j=0;j<itemids.length;j++)
				{
					
					if(!newReturnTotal[j].trim().equalsIgnoreCase("0"))
					{
						ps3.setString(1, uqty[j]);
						ps3.setString(2,updateItemtot[j]);
						ps3.setString(3,stockid[j]);
						ps3.setString(4,cid);
						
						int a=ps3.executeUpdate();
						if(a>0)
						{
							log.info("###########################################################");
							log.info("######  Stock Deatils Updated Sucessfully");
							log.info("############################################################");	
							flagcheckForStockUpdate=1;
						}
					}
				}
				
				if(flagcheckForStockUpdate>0)
				{
					System.out.println("###############################################################################");
					System.out.println("Debit calculation Completed Sucessfully . All records saved !!!!");
					System.out.println("###############################################################################");
					returnResult=true;
				}
			}

		} catch(Exception e){
			System.out.println("Ex= "+e);
		}finally{
			if(con!=null)
			{
			
			con.close();
			System.out.println("connection closed");
			}}
		return returnResult;
	}

	
	public String debitNoGenerateEngine(String companyId) throws SQLException
	{
		String newDebitNoteNo="";
		Connection con=null;
		int dbno=1;
		int flagCheck=0;
		int dnIdentity=0;
		try
		{
			con=ConnectCore.getConnection();
			String query="SELECT MAX(dnIdentityCode)as dnIdentityCode FROM debitnote WHERE companyId=?";
			PreparedStatement ps=con.prepareStatement(query);
			ps.setString(1, companyId);
			ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					 dnIdentity=Integer.parseInt(rs.getString("dnIdentityCode"))+1;
					newDebitNoteNo="DN00"+dnIdentity;
					flagCheck=1;
				}
			if(flagCheck==0)
			{
				newDebitNoteNo="DN00"+dbno;
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception occur type is ---"+e);
			newDebitNoteNo="DN00"+dbno;
		}
		finally
		{
			if(con!=null)
			{
				con.close();
				System.out.println("connection closed");
			}
		}
		return newDebitNoteNo;
	}
	
	
	public int debitNoteIdentityGenerateEngine(String companyId) throws SQLException
	{
		
		int newDebitIdentityNoteNo=1;
		Connection con=null;
		int flagCheck=0;
		try
		{
			con=ConnectCore.getConnection();
			String query="SELECT MAX(dnIdentityCode)as dnIdentityCode FROM debitnote WHERE companyId=?";
			PreparedStatement ps=con.prepareStatement(query);
			ps.setString(1, companyId);
			ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					newDebitIdentityNoteNo=Integer.parseInt(rs.getString("dnIdentityCode"))+1;
					flagCheck=1;
				}
			if(flagCheck==0)
			{
				newDebitIdentityNoteNo=1;
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
		return newDebitIdentityNoteNo;
	}
	
	@Override
	public String lastDebitNoSaved(String companyId, String userId) throws DaoException, Exception
	{
		String newDebitNoteNo="";
		Connection con=null;
		
		try
		{
			con=ConnectCore.getConnection();
			String query="SELECT dnNo FROM debitnote WHERE companyId=? AND userId=? AND dnId=(SELECT MAX(dnId) FROM debitnote WHERE companyId=? AND userId=?)";
			PreparedStatement ps=con.prepareStatement(query);
			ps.setString(1, companyId);
			ps.setString(2, userId);
			ps.setString(3, companyId);
			ps.setString(4, userId);
			ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					newDebitNoteNo=rs.getString(1);
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
		return newDebitNoteNo;
	}

	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	//----------------------------------------------------------------------- Debit Note calculation main method ____ End here --------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	//---------------------------------------------------Opening and closing Stock Codes -----------------------------------------------
	public List<OpenigAndClosingStockDTO> getAllOpeningStock(String financialYear)throws DaoException, SQLException
	{
		
		List<OpenigAndClosingStockDTO> list = new LinkedList<OpenigAndClosingStockDTO>();
		Connection con = null;
		PreparedStatement pstat=null;	
		try
		{
		con=ConnectCore.getConnection();
		String query="SELECT * FROM closingstockmaster WHERE financialYear='"+financialYear+"'";
		pstat=con.prepareStatement(query);
		 ResultSet rs=pstat.executeQuery();
			while(rs.next())
			{
				OpenigAndClosingStockDTO csdto=new OpenigAndClosingStockDTO();
				csdto.setId(rs.getInt("CLSID"));
				csdto.setFinancialYear(financialYear);
				csdto.setQty(rs.getString("qty"));
				csdto.setItemIds(rs.getString("itemIds"));
				csdto.setStockManupulation(rs.getString("alterqty"));
				csdto.setRemarks(rs.getString("remarks"));
				csdto.setCompanyId(rs.getString("companyId"));
				csdto.setUserId(rs.getString("userId"));
				csdto.setStockId(rs.getString("stockId"));
				list.add(csdto);
			}
		
		}
		catch(Exception e)
		{
		System.out.println("Exception occur"+e)	;
		}
		finally
		{
			if(con!=null)
			{
				con.close();
				System.out.println("connection closed");
			}
		}

	return list;	
	}
	
	
	
	public List<OpenigAndClosingStockDTO> getAllCurrentYearOpeningStock(String financialYear)throws DaoException, SQLException
	{
		
		List<OpenigAndClosingStockDTO> list = new LinkedList<OpenigAndClosingStockDTO>();
		Connection con = null;
		PreparedStatement pstat=null;	
		try
		{
		con=ConnectCore.getConnection();
		String query="SELECT * FROM openingstockmaster order by stockId asc ";
		pstat=con.prepareStatement(query);
		 ResultSet rs=pstat.executeQuery();
			while(rs.next())
			{
				OpenigAndClosingStockDTO csdto=new OpenigAndClosingStockDTO();
				csdto.setId(rs.getInt("opnId"));
				csdto.setFinancialYear(rs.getString("financialYear"));
				csdto.setQty(rs.getString("qty"));
				csdto.setItemIds(rs.getString("itemIds"));
				
				csdto.setCompanyId(rs.getString("companyId"));
				csdto.setUserId(rs.getString("userId"));
				csdto.setStockId(rs.getString("stockId"));
				list.add(csdto);
			}
		
		}
		catch(Exception e)
		{
		System.out.println("Exception occur"+e)	;
		}
		finally
		{
			if(con!=null)
			{
				con.close();
				System.out.println("connection closed");
			}
		}

	return list;	
	}
	
	
	
	
	public boolean saveOpeningClosingStock(OpenigAndClosingStockDTO dto ,String fromdateyear,String todateyear)throws DaoException ,SQLException
	{
	boolean result=false;
	Connection con=null;
	PreparedStatement pstat=null;
	PreparedStatement pstat2=null;

	try
	{
	con=ConnectCore.getConnection();

	pstat=con.prepareStatement("insert into closingstockmaster(financialYear,stockId,itemIds,qty,alterqty,remarks,companyId,userId) values(?,?,?,?,?,?,?,?)");
	pstat.setString(1, dto.getFinancialYear());
	pstat.setString(2, dto.getStockId());
	pstat.setString(3, dto.getItemIds());
	pstat.setString(4, dto.getQty());
	pstat.setString(5, dto.getStockManupulation());
	pstat.setString(6, dto.getRemarks());
	pstat.setString(7, dto.getCompanyId());
	pstat.setString(8, dto.getUserId());
	
	int l=pstat.executeUpdate();
	if(l>0)
	{
		System.out.println("#################### Insert successfully one row in closing master !!! ##################################################### ");
		
		pstat2=con.prepareStatement("insert into openingstockmaster(stockId,financialYear,itemIds,qty,companyId,userId) values(?,?,?,?,?,?)");
		
		
		
		pstat2.setString(1, dto.getStockId());
		pstat2.setString(2, fromdateyear+"-"+todateyear);
		pstat2.setString(3, dto.getItemIds());
		pstat2.setString(4, dto.getQty());
		pstat2.setString(5, dto.getCompanyId());
		pstat2.setString(6, dto.getUserId());
		
		int m=pstat2.executeUpdate();
		if(m>0)
		{
			System.out.println("#################### Insert successfully one row in opening master !!! ##################################################### ");	
			result=true;
		}
		
	}
	}
	catch(Exception e)
	{
		System.out.println("Exception Occur type is "+e);
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

	public List<StockMasterDTO>getAllStockDetailsforclosingStockMaster() throws DaoException,SQLException
	{
		List<StockMasterDTO> list = new LinkedList<StockMasterDTO>();
		Connection con = null;
		PreparedStatement pstat=null;
		try {
			
            String query="select * from stockmaster";
			con = ConnectCore.getConnection();
			pstat = con.prepareStatement(query);
			System.out.println("ps="+pstat);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				StockMasterDTO dto = new StockMasterDTO();
			
		
				dto.setItemIds(rs.getString("itemIds"));
				dto.setStockid(rs.getString("stockid"));
				dto.setQty(rs.getString("qty"));
				dto.setCompanyId(rs.getString("companyId"));
				dto.setUserId(rs.getString("userId"));
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
				System.out.println("connection closed");
			}
		}
		return list;	
	}
	
	
public boolean updateClosingOpeningAndStockMaster(String[] financialyear,String[] id,String[] stockid,String[] itemids,String[] qty, String[] alterQty ,String[] remarks) throws DaoException, SQLException 
{
boolean updateresult=false;		
	
Connection con = null;
PreparedStatement pstat=null;
PreparedStatement pstat2=null;
PreparedStatement pstat3=null;
int flagcheckforclosingstockmaster=0;
int flagcheckforopeningstockmaster=0;
int flagcheckforstockmaster=0;
try {
	
    String query="UPDATE closingstockmaster SET alterqty=?,remarks=? where stockId=?";
	
	// String query="UPDATE usermaster SET userName=?,Email=? WHERE userID=?";
    String query2="UPDATE openingstockmaster SET qty=? WHERE stockId=?";
    String query3="UPDATE stockmaster SET qty=? WHERE stockId=?";
	con = ConnectCore.getConnection();
	
	for(int i=0;i<stockid.length;i++)
	{
	pstat = con.prepareStatement(query);
	pstat.setString(1, alterQty[i]);
	pstat.setString(2, remarks[i]);
	pstat.setString(3, stockid[i]);
	
	int z = pstat.executeUpdate();
	if(z>0)
	{
		
		System.out.println("#################################################");
		System.out.println("####### Closing Stock Master Update Successfully#");
		System.out.println("#################################################");
		flagcheckforclosingstockmaster=1;
	}
	
	if(flagcheckforclosingstockmaster>0)
	{
		pstat2 = con.prepareStatement(query2);
		pstat2.setString(1, alterQty[i]);
		pstat2.setString(2, stockid[i]);
		int w = pstat2.executeUpdate();
		if(w>0)
		{
			System.out.println("#################################################");
			System.out.println("####### Opening Stock Master Update Successfully#");
			System.out.println("#################################################");	
			flagcheckforopeningstockmaster=1;
		}
	}
	
	if(flagcheckforopeningstockmaster>0)
	{
		pstat3 = con.prepareStatement(query3);
		pstat3.setString(1, alterQty[i]);
		pstat3.setString(2, stockid[i]);
		
		int a=pstat3.executeUpdate();
		if(a>0)
		{
			System.out.println("#################################################");
			System.out.println("#######  Stock Master Update Successfully#");
			System.out.println("#################################################");	
			flagcheckforstockmaster=1;	
		}
	}
	
	if(flagcheckforstockmaster>0)
	{
		System.out.println("#################################################");
		System.out.println("####### All tables updated Successfully !!!!!!!!#");
		System.out.println("#################################################");	
		updateresult=true;
	}
	}
	
} catch (Exception e) {
	e.printStackTrace();
	log.error("error occured", e);
} finally {
	if (con != null) {
		con.close();
		pstat.close();
		System.out.println("connection closed");
	}
}
return updateresult;
}
	
public boolean truncateopeningclosingstockmaster()throws DaoException,SQLException
{
	Connection con=null;
	PreparedStatement tpstat=null;
	PreparedStatement tpstat2=null;
	boolean resultr=false;
	try
	{
	con=ConnectCore.getConnection();
	String tquery="TRUNCATE TABLE closingstockmaster";
	tpstat=con.prepareStatement(tquery);
	int tcl=tpstat.executeUpdate();
	if(tcl>0)
	{
	String tquery2="TRUNCATE TABLE openingstockmaster";	
	tpstat2=con.prepareStatement(tquery2);
	int tcl2=tpstat2.executeUpdate();
	if(tcl2>0)
	{
		System.out.println("#############################################################################");
		System.out.println("Closing Stock Master and Opening Stock MAster is Truncated successfully!!! ");
		System.out.println("#############################################################################");
		resultr=true;
	}
		
	}
	
	}catch(Exception e)
	{
		System.out.println("Exception occur"+e);
	}
	
return resultr;
}


public List<StockMasterDTO> getTotalItemStock(String itemId ,Connection con) throws DaoException,SQLException
{
	List<StockMasterDTO> list=new ArrayList<StockMasterDTO>();
	
	HttpServletRequest request=ServletActionContext.getRequest();
	HttpSession session=null;
	session=request.getSession();
	PreparedStatement pstat=null;
	String cid=(String)session.getAttribute("companyId");
	try
	{
		con=ConnectCore.getConnection();
//		String aql="SELECT itemIds,itemSize,SUM(qty)AS StockQuantity  FROM stockmaster WHERE itemIds=? AND companyId=? GROUP BY itemSize";
//		String aql="SELECT purchaseDate,itemIds,itemSize,SUM(qty)AS StockQuantity,buyrate ,SUM(itemTotal)AS itemTotal FROM stockmaster WHERE itemIds=? AND companyId=?  GROUP BY itemSize,buyRate";
		String aql="SELECT DISTINCT itemIds,itemSize FROM stockmaster WHERE itemIds='"+itemId+"' and companyId='1'";
		
		pstat = con.prepareStatement(aql);
		
		System.out.println("ps="+pstat);
		ResultSet rs = pstat.executeQuery();
		double grandFinalItemSold=0.0;
		while (rs.next()) 
		{
			StockMasterDTO dto = new StockMasterDTO();
			double totalitemsold=0.0;
			double finaltotalitemsold=0.0;
			dto.setItemIds(rs.getString("itemIds"));
			dto.setItemSize(rs.getString("itemSize"));
			
			dto.setQty((int)getTotalItemsqtyavailable(rs.getString("itemIds"), rs.getString("itemSize"), "1", con)+"");
//			dto.setPurchaseDate(rs.getString("purchaseDate"));
			dto.setBuyRate(getLastByRate(rs.getString("itemIds"), rs.getString("itemSize"), "1", con)+"");
			dto.setItemTotal(getItemtotalpurchased(rs.getString("itemIds"), rs.getString("itemSize"), "1", con)+"");
			dto.setTotaldiscountgiven(getTotalItemsdiscount(rs.getString("itemIds"), rs.getString("itemSize"), "1", con)+"");	
			totalitemsold=getTotalItemsCostSold(rs.getString("itemIds"), rs.getString("itemSize"), "1", con);
			finaltotalitemsold=totalitemsold-getTotalItemsdiscount(rs.getString("itemIds"), rs.getString("itemSize"), "1", con);
			dto.setTotalitempricesold(totalitemsold+"");
			dto.setFinalitemsold(finaltotalitemsold+"");
			grandFinalItemSold=grandFinalItemSold+finaltotalitemsold;
			dto.setGrandfinalItemSold(grandFinalItemSold+"");
			dto.setTotalqtysold((int)getTotalItemqtysold(rs.getString("itemIds"), rs.getString("itemSize"), "1", con)+"");
			list.add(dto);
		}
	}
	catch (Exception e) {
		e.printStackTrace();
		log.error("failed to retreive data from database", e);
	} 
	
	return list;
}


public double getTotalItemsCostSold(String Itemid,String itemsize,String userid,Connection con)
{
	
double totitemsold=0.0;
	int flagCheck=0;
	try
	{
		
		String query="SELECT SUM(saleItemtotal)AS SaleItemTotal  FROM salemaster WHERE itemId='"+Itemid+"' AND itemSize='"+itemsize+"' and userId='1'";
		PreparedStatement ps=con.prepareStatement(query);
	
		ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				totitemsold=Double.parseDouble(rs.getString("SaleItemTotal"));
				flagCheck=1;
			}

	}
	catch(Exception e)
	{
		
		totitemsold=0.0;
	}
	

	return totitemsold;
	
}


public double getItemtotalpurchased(String Itemid,String itemsize,String userid,Connection con)
{
	
double totitemsold=0.0;
	int flagCheck=0;
	try
	{
		
		String query="SELECT SUM(itemTot)as itemTot FROM purchaseledgermaster WHERE itemId='"+Itemid+"'  AND itemSize='"+itemsize+"' and userId='1'";
		PreparedStatement ps=con.prepareStatement(query);
	
		ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				totitemsold=Double.parseDouble(rs.getString("itemTot"));
				flagCheck=1;
			}

	}
	catch(Exception e)
	{
		
		totitemsold=0.0;
	}
	

	return totitemsold;
	
}




public double getLastByRate(String Itemid,String itemsize,String userid,Connection con)
{
	
double totitemsold=0.0;
	int flagCheck=0;
	try
	{
		
		String query="SELECT buyRate   FROM salemaster WHERE itemId='"+Itemid+"' AND itemSize='"+itemsize+"' and userId='1' order by purchaseDate DESC";
		PreparedStatement ps=con.prepareStatement(query);
	
		ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				totitemsold=Double.parseDouble(rs.getString("buyRate"));
				flagCheck=1;
			}

	}
	catch(Exception e)
	{
		
		totitemsold=0.0;
	}
	

	return totitemsold;
	
}


public double getTotalItemsqtyavailable(String Itemid,String itemsize,String userid,Connection con)
{
	
double totitemsold=0.0;
	int flagCheck=0;
	try
	{
		
		String query="SELECT SUM(qty)AS qty FROM stockmaster WHERE itemIds='"+Itemid+"' AND itemSize='"+itemsize+"' and userid='1'";
		PreparedStatement ps=con.prepareStatement(query);
	
		ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				totitemsold=Double.parseDouble(rs.getString("qty"));
				flagCheck=1;
			}

	}
	catch(Exception e)
	{
		
		totitemsold=0.0;
	}
	

	return totitemsold;
	
}

public double getTotalItemsdiscount(String Itemid,String itemsize,String userid,Connection con)
{
	
double totsaleDiscount=0.0;
	int flagCheck=0;
	try
	{
		
		String query="SELECT SUM(saleDiscount)AS saleDiscount  FROM salemaster WHERE itemId='"+Itemid+"' AND itemSize='"+itemsize+"' and userId='1'";
		PreparedStatement ps=con.prepareStatement(query);
	
		ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				totsaleDiscount=Double.parseDouble(rs.getString("saleDiscount"));
				flagCheck=1;
			}

	}
	catch(Exception e)
	{
	
		totsaleDiscount=0.0;
	}
	

	return totsaleDiscount;
	
}

public double getTotalItemqtysold(String Itemid,String itemsize,String userid,Connection con)
{
	
double totssaleQTY=0.0;
	int flagCheck=0;
	try
	{
		
		String query="SELECT SUM(saleQTY)AS saleQTY  FROM salemaster WHERE itemId='"+Itemid+"' AND itemSize='"+itemsize+"' and userId='1'";
		PreparedStatement ps=con.prepareStatement(query);
	
		ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				totssaleQTY=Double.parseDouble(rs.getString("saleQTY"));
				flagCheck=1;
			}

	}
	catch(Exception e)
	{
		
		totssaleQTY=0.0;
	}
	

	return totssaleQTY;
	
}
//__________________+++++++++++++++++++++++++++++++++++++ Opening closing stock ends here ++++++++++++++++++++++++++++++++++++___________________
}
