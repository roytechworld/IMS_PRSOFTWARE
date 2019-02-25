package com.prsoftwares.daoImpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.apache.struts2.ServletActionContext;
import org.w3c.dom.CDATASection;

import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dao.SupplierPaymentDao;
import com.prsoftwares.dto.StockMasterDTO;
import com.prsoftwares.util.AllBankNames;
import com.prsoftwares.util.CurrentCompAndUser;
import com.prsoftwares.util.CurrentDate;



public class SupplierPaymentDaoImpl extends ConnectCore implements SupplierPaymentDao {

	@Override
	public List<StockMasterDTO> getSupplierWithDuePay() throws DaoException, Exception {
		List<StockMasterDTO> sList=new LinkedList<StockMasterDTO>();
		
		String sql="SELECT DISTINCT cp.supplierId, c.SUPPLIERNAME FROM supplierpayment cp, suppliermaster c WHERE c.SupplierId=cp.supplierId AND cp.companyId=?";
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();		
			PreparedStatement pstat=con.prepareStatement(sql);	
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs=pstat.executeQuery();
			while(rs.next())
			{
				StockMasterDTO sDto= new StockMasterDTO();
				sDto.setSupplierId(rs.getInt(1));
				sDto.setSuppliername(rs.getString(2));
				
				System.out.println("cName= "+sDto.getSuppliername());
				sList.add(sDto);
			}
		}
		catch(Exception e)
		{
			System.out.println("ex="+e);
		}
		finally 
		{
			 if(con!=null)	
			 {
				 con.close();
			 }	
		}
		
		return sList;
	}
	
	@Override
	public List<StockMasterDTO> getPaymentBillNoSupplierId(String SupplierId) throws DaoException, Exception {
		List<StockMasterDTO> sList=new LinkedList<StockMasterDTO>();
		
		//String sql="SELECT DISTINCT purchaseBillNo FROM supplierpayment s, stockmaster st WHERE st.billNo=s.purchaseBillNo AND st.purchaseDate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND s.supplierId=? AND s.companyId=?";
		
//		String sql="SELECT sPaymentId,purchaseBillNo,sPaidStatus,gTot,sPaidAmt,dueAmt,sPayMode,DATE_FORMAT(sPayDate,'%d-%m-%Y ') AS sPayDate,supplierId FROM supplierpayment WHERE  companyId=? AND supplierId=?";
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		Connection con=null;
		PreparedStatement pstat=null;
		ResultSet rs=null;
		
		con=ConnectCore.getConnection();	
		
		String sql="SELECT * FROM supplierpaymentrecord WHERE companyId=? AND supplierId=? And userid=? and financialyear='"+(String)session.getAttribute("fYear")+"'";
	
		try
		{
				
			pstat=con.prepareStatement(sql);	
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			pstat.setString(2, SupplierId);
			pstat.setString(3, "1");
			rs=pstat.executeQuery();
			while(rs.next())
			{
				StockMasterDTO sDto= new StockMasterDTO();
				
				sDto.setBillNo("NA");
				
				sDto.setsPaymentId(rs.getString("suppayid"));
				sDto.setsPaidStatus(rs.getString("paymentstatus"));
				sDto.setGrandTot(rs.getString("totalamount"));
				sDto.setsPaidAmt(rs.getString("paidamount"));
				sDto.setDueAmt(rs.getString("dueamount"));
				sDto.setsPayMode(rs.getString("paymentmode"));
				sDto.setsPayDate(rs.getString("paymentdate"));
				sDto.setSupplierId(rs.getInt("supplierid"));
				
				System.out.println("bill= "+sDto.getBillNo());
				sList.add(sDto);
			}
			
			if(pstat!=null){
				rs.close();
				pstat.close();
			}
		}
		catch(Exception e)
		{
			System.out.println("ex="+e);
		}
		finally 
		{
			 if(con!=null)	
			 {
				 con.close();
			 }	
		}
		return sList;
	}
	
	@Override
	public List<StockMasterDTO> getSupplierPaymentHistoryByBillNo(String billno, String supplierId) throws DaoException,Exception 
	{
		List<StockMasterDTO> salesList=new ArrayList<StockMasterDTO>();
		String sql="";
	/*	if(billno=="")
		{
			sql="SELECT DISTINCT cp.customerId, c.cName, cp.saleBillNo,cp.cPaidStatus,cp.cPaidAmt,cp.dueAmt,cp.cPayDate,cp.chequeNo,cp.chequeDate,cp.chequeBank,cp.companyId,cp.userId,cp.grandTot,sl.saleDoc,cp.cPayMode  FROM customerpayment cp JOIN customermaster c ON cp.customerId=c.customerId  JOIN salemaster sl ON sl.billno=cp.saleBillNo AND cp.companyId=? GROUP BY cp.customerId";
		}
		else
		{}*/
			    sql="SELECT DISTINCT cp.supplierId, s.SUPPLIERNAME, cp.purchaseBillNo,cp.sPaidStatus,cp.sPaidAmt,cp.dueAmt,cp.sPayDate,cp.chequeNo,cp.chequeDate,cp.chequeBank,cp.companyId,cp.userId,cp.gTot,"+
				"st.purchaseDate,cp.sPayMode FROM supplierpayment cp, suppliermaster s,stockmaster st WHERE s.SupplierId=cp.supplierId AND st.billNo=cp.purchaseBillNo AND st.SupplierId=cp.supplierId AND cp.purchaseBillNo=? AND cp.supplierId=? AND cp.companyId=?";
		Connection con=null;
		int datediff=0;
		try
		{
			con=ConnectCore.getConnection();	
			PreparedStatement pstat=con.prepareStatement(sql);	
			pstat.setString(1, billno);
			pstat.setString(2, supplierId);
			pstat.setString(3, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs=pstat.executeQuery();
			while(rs.next())
			{
				StockMasterDTO dto=new StockMasterDTO();
				dto.setSupplierId(rs.getInt("supplierId"));
				dto.setSuppliername(rs.getString("SUPPLIERNAME"));
				dto.setBillNo(rs.getString("purchaseBillNo"));
				dto.setsPaidStatus(rs.getString("sPaidStatus"));
				dto.setsPaidAmt(rs.getString("sPaidAmt"));
				dto.setDueAmt(rs.getString("dueAmt"));
				dto.setsPayDate(rs.getString("sPayDate"));
				dto.setChequeNo(rs.getString("chequeNo"));
				dto.setChequeDate(rs.getString("chequeDate"));
				dto.setChequeBank(rs.getString("chequeBank"));
				dto.setCompanyId(rs.getString("companyId"));
				dto.setUserId(rs.getString("userId"));
				dto.setGrandTot(rs.getString("gTot"));
				dto.setPurchaseDate(rs.getString("purchaseDate"));
				dto.setsPayMode(rs.getString("sPayMode"));
				datediff=checkDateDiff(rs.getDate("purchaseDate"), CurrentDate.mysqlDateConvertor(CurrentDate.getOnlyDateWithMySQLFORMAT()));
				System.out.println("datediff found"+datediff);
				dto.setDateDiff(datediff);
				salesList.add(dto);
			}

			if(rs!=null)
			{
				rs.close();
			}
			if(pstat!=null)
			{
				pstat.close();
			}	
		}
		catch(Exception e)
		{
			System.out.println("Exception occur"+e);
		}
		finally
		{
			 if(con!=null)	
			 {
				 con.close();
			 }
		}
		return salesList;
	}
	
	public  StockMasterDTO getMinDueAmt( String supplierId) throws DaoException, Exception
	{
		StockMasterDTO sdto=new StockMasterDTO();
		Connection con=null;
		String minDueAmt="SELECT MIN(dueAmt) AS minDueAmt FROM supplierpayment WHERE SupplierId='"+supplierId+"' AND companyId='"+CurrentCompAndUser.getCurrentCompany()+"'";
		try
		{
			con=ConnectCore.getConnection();	
			PreparedStatement pstat=con.prepareStatement(minDueAmt);		
			ResultSet rs=pstat.executeQuery();
			
			while(rs.next())
			{
				sdto.setMinDuePayment(rs.getString("minDueAmt"));
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("Exception occur"+e);	
		}
		return sdto;
	}
	
	public int checkDateDiff(Date purchasedate, Date paymentdate) throws DaoException,Exception 
	{
		int result=0;
		String sql="SELECT DATEDIFF('"+paymentdate+"','"+purchasedate+"') AS diffdate";
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();		
			PreparedStatement pstat=con.prepareStatement(sql);	
			ResultSet rs=pstat.executeQuery();
			while(rs.next())
			{
				result=rs.getInt("diffdate");
			}
		}
		catch(Exception e)
		{
			System.out.println("ex="+e);
		}
		finally 
		{
			 if(con!=null)	
			 {
				 con.close();
			 }	
		}
		
		return result;
	}
	/*@Override
	public List<SalesMasterDto> getAllUnpaidBillofSales() throws DaoException,Exception {
		List<SalesMasterDto> saleList=new LinkedList<SalesMasterDto>();
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
				con=ConnectCore.getConnection();
				String sql="SELECT DISTINCT billno FROM salemaster WHERE companyID=?";
				ps=con.prepareStatement(sql);
				ps.setString(1, CurrentCompAndUser.getCurrentCompany());
				System.out.println("ps="+ps);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					SalesMasterDto sdto=new SalesMasterDto();
					sdto.setBillno(rs.getString(1));
					
					saleList.add(sdto);
				}
				System.out.println("size= "+saleList.size());
				if (ps!=null) {
					rs.close();
					ps.close();
				}
		} catch (Exception e) {
			System.out.println("Ex= "+e.getStackTrace());
		}finally{
			if (con!=null) {
				con.close();
			}
		}
		return saleList;
	}
	
	
	@Override
	public List<SalesMasterDto> getPaymentInfoByCustomerId(String customerId) throws DaoException,Exception 
	{
		List<SalesMasterDto> salesList=new ArrayList<SalesMasterDto>();
		List<SalesMasterDto> salesList1=new ArrayList<SalesMasterDto>();
		//String sql="SELECT MAX(cPaymentId) FROM customerpayment WHERE  companyId=? AND customerId=? GROUP BY saleBillNo";
		String sql="SELECT MAX(cPaymentId),SUM(cPaidAmt) FROM customerpayment WHERE  companyId=? AND customerId=? GROUP BY saleBillNo";
		String sql1="SELECT DISTINCT cp.customerId, c.cName, cp.saleBillNo,cp.cPaidStatus,cp.cPaidAmt,cp.dueAmt,cp.cPayDate,cp.chequeNo,cp.chequeDate,cp.chequeBank,cp.companyId,cp.userId,cp.grandTot,sl.saleDoc FROM customerpayment cp JOIN customermaster c ON cp.customerId=c.customerId  JOIN salemaster sl ON sl.billno=cp.saleBillNo AND cp.cPaymentId=? AND cp.companyId=?";
		Connection con=null;
		PreparedStatement pstat=null;
		ResultSet rs=null;
		
		int datediff=0;
		try
		{
			con=ConnectCore.getConnection();	
			pstat=con.prepareStatement(sql);	
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			pstat.setString(2, customerId);
			rs=pstat.executeQuery();
			while(rs.next())
			{
				SalesMasterDto dto=new SalesMasterDto();
				dto.setcPaymentId(rs.getString(1));
				dto.setcPaidAmt(rs.getString(2));
				salesList.add(dto);
			}
			System.out.println("payment size= "+salesList.size());
			if(rs!=null)
			{
				rs.close();
			}
			if(pstat!=null)
			{
				pstat.close();
			}
			
			for (int i = 0; i < salesList.size(); i++) {
				pstat=con.prepareStatement(sql1);	
				pstat.setString(1, salesList.get(i).getcPaymentId());
				pstat.setString(2, CurrentCompAndUser.getCurrentCompany());
				System.out.println("ps= "+pstat);
				rs=pstat.executeQuery();
				while(rs.next())
				{
					SalesMasterDto dto=new SalesMasterDto();
					dto.setCustomerId(rs.getString("customerId"));
					dto.setCustomerName(rs.getString("cName"));
					dto.setBillno(rs.getString("saleBillNo"));
					dto.setcPaidStatus(rs.getString("cPaidStatus"));
					dto.setcPaidAmt(salesList.get(i).getcPaidAmt());
					dto.setDueAmt(rs.getString("dueAmt"));
					dto.setcPayDate(rs.getString("cPayDate"));
					dto.setChequeNo(rs.getString("chequeNo"));
					dto.setChequeDate(rs.getString("chequeDate"));
					dto.setChequeBank(rs.getString("chequeBank"));
					dto.setCompanyId(rs.getString("companyId"));
					dto.setUserId(rs.getString("userId"));
					dto.setGrandTotal(rs.getString("grandTot"));
					dto.setDoc(rs.getString("saleDoc"));
					datediff=checkDateDiff(rs.getDate("saleDoc"), CurrentDate.mysqlDateConvertor(CurrentDate.getOnlyDateWithMySQLFORMAT()));
					System.out.println("datediff found"+datediff);
					dto.setDateDiff(datediff);
					salesList1.add(dto);
				}
			}
				
		}
		catch(Exception e)
		{
			System.out.println("Exception occur"+e);
		}
		finally
		{
		 if(con!=null)	
		 {
			 con.close();
		 }
		}
		return salesList1;
	}*/

	@Override
	public boolean saveSupplierPayment2(StockMasterDTO sdto) throws DaoException, Exception {
		Connection con=null;
		PreparedStatement ps=null;
		int x=0;
		try {
				con=ConnectCore.getConnection();
				try
				{
				
					if(!sdto.getChequeDate().equals("--"))
					{
						String sql="INSERT INTO supplierpayment(purchaseBillNo,sPaidStatus,gTot,sPaidAmt,dueAmt,sPayMode,sPayDate,chequeNo,chequeDate,chequeBank,companyId,userId,supplierId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
						ps=con.prepareStatement(sql);
						ps.setString(1, sdto.getBillNo());
						ps.setString(2, sdto.getsPaidStatus());
						ps.setString(3, sdto.getGrandTot());
						ps.setString(4, sdto.getsPaidAmt());
						ps.setString(5, sdto.getDueAmt());
						ps.setString(6, sdto.getsPayMode());
						ps.setString(7, sdto.getsPayDate());
						ps.setString(8, sdto.getChequeNo());
						ps.setString(9, sdto.getChequeDate());
						ps.setString(10, sdto.getChequeBank());
						ps.setString(11, CurrentCompAndUser.getCurrentCompany());
						ps.setString(12, CurrentCompAndUser.getCurrentUserID());
						ps.setInt(13, sdto.getSupplierId());
						
						System.out.println("ps="+ps);
						x=ps.executeUpdate();
					}
					else
					{
						String sql="INSERT INTO supplierpayment(purchaseBillNo,sPaidStatus,gTot,sPaidAmt,dueAmt,sPayMode,sPayDate,chequeNo,chequeBank,companyId,userId,supplierId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
						ps=con.prepareStatement(sql);
						ps.setString(1, sdto.getBillNo());
						ps.setString(2, sdto.getsPaidStatus());
						ps.setString(3, sdto.getGrandTot());
						ps.setString(4, sdto.getsPaidAmt());
						ps.setString(5, sdto.getDueAmt());
						ps.setString(6, sdto.getsPayMode());
						ps.setString(7, sdto.getsPayDate());
						ps.setString(8, sdto.getChequeNo());
						//ps.setString(9, sdto.getChequeDate());
						ps.setString(9, sdto.getChequeBank());
						ps.setString(10, CurrentCompAndUser.getCurrentCompany());
						ps.setString(11, CurrentCompAndUser.getCurrentUserID());
						ps.setInt(12, sdto.getSupplierId());
						
						System.out.println("ps="+ps);
						x=ps.executeUpdate();	
						
					}
				}
				catch(Exception d)
				{
					String sql="INSERT INTO supplierpayment(purchaseBillNo,sPaidStatus,gTot,sPaidAmt,dueAmt,sPayMode,sPayDate,chequeNo,chequeBank,companyId,userId,supplierId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
					ps=con.prepareStatement(sql);
					
					ps.setString(1, sdto.getBillNo());
					ps.setString(2, sdto.getsPaidStatus());
					ps.setString(3, sdto.getGrandTot());
					ps.setString(4, sdto.getsPaidAmt());
					ps.setString(5, sdto.getDueAmt());
					ps.setString(6, sdto.getsPayMode());
					ps.setString(7, sdto.getsPayDate());
					ps.setString(8, sdto.getChequeNo());
					//ps.setString(9, sdto.getChequeDate());
					ps.setString(9, sdto.getChequeBank());
					ps.setString(10, CurrentCompAndUser.getCurrentCompany());
					ps.setString(11, CurrentCompAndUser.getCurrentUserID());
					ps.setInt(12, sdto.getSupplierId());
					
					System.out.println("ps="+ps);
					x=ps.executeUpdate();	
				}
					
				if (ps!=null) {
					ps.close();
				}
		} catch (Exception e) {
			System.out.println("ex="+e.toString());
		}finally{
			if (con!=null) {
				con.close();
			}
		}
		
		return x>0;
	}

	@Override
	public boolean saveSupplierPayment(StockMasterDTO sdto) throws DaoException, Exception {
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps2=null;
		ResultSet rs=null;
		int x=0;
		double totalDue =0,newPaidAmt=0, gTot=0, cPaidAmt=0, dueAmt=0;
		String paidStatus="";
		try {
			
			  int cpayment=Integer.parseInt(sdto.getsPaidAmt());
			    if(cpayment>0)
			    {
			 String fyear=(String)session.getAttribute("fYear");
				con=ConnectCore.getConnection();
				String sql="SELECT * FROM supplierpaymentrecord WHERE  supplierid=? AND companyid=? AND userid='1' And financialyear='"+fyear+"'";	
				ps=con.prepareStatement(sql);
				
				ps.setInt(1, sdto.getSupplierId());
				ps.setString(2, CurrentCompAndUser.getCurrentCompany());
				rs=ps.executeQuery();
				while(rs.next()){
					gTot=Double.parseDouble(rs.getString("totalamount"));
					cPaidAmt=Double.parseDouble(rs.getString("paidamount"));
					//dueAmt=Double.parseDouble(rs.getString("dueAmt"));
				}
				if (ps!=null) {
					rs.close();
					ps.close();
				}
				if(sdto.getsPaidAmt().equalsIgnoreCase("")){
					sdto.setsPaidAmt("0.00");
				}
				newPaidAmt=cPaidAmt+Double.parseDouble(sdto.getsPaidAmt());
				totalDue=gTot-newPaidAmt;
				if(totalDue==0){
					paidStatus="Paid";
				} else{
					paidStatus="Partial";
				}
				
				String sql1="";
				if(!sdto.getsPayMode().equals("cash"))
				{
				
				sql1="UPDATE supplierpaymentrecord SET paidamount=?,dueamount=?,paymentmode=?,paymentdate=?,paymentstatus=?,chequeno=?,chequedate='"+CurrentDate.getDate()+"',chequeBank=? WHERE  supplierId=? AND companyId=? AND financialyear='"+(String)session.getAttribute("fYear")+"'";	
				}
				else
				{
					sql1="UPDATE supplierpaymentrecord SET paidamount=?,dueamount=?,paymentmode=?,paymentdate=?,paymentstatus=?,chequeno=?,chequeBank=? WHERE supplierId=? AND companyId=? AND financialyear='"+(String)session.getAttribute("fYear")+"'";	
					
				}
				
				
				
				ps=con.prepareStatement(sql1);
				ps.setDouble(1, newPaidAmt);
				ps.setDouble(2, totalDue);
				ps.setString(3, sdto.getsPayMode());
				ps.setString(4, sdto.getsPayDate());
				ps.setString(5, paidStatus);
				ps.setString(6, sdto.getChequeNo());
				
				ps.setString(7, sdto.getChequeBank());
				
				
				ps.setInt(8, sdto.getSupplierId());
				ps.setString(9, CurrentCompAndUser.getCurrentCompany());
				
				System.out.println("upQ= "+ps);
				x=ps.executeUpdate();
				
				if(x>0)
				{
				String sqlr = "insert into supplierpaymenthistory values(default,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps2=con.prepareStatement(sqlr);
                ps2.setString(1, sdto.getSupplierId()+"");
                ps2.setString(2,sdto.getsPayDate());
                ps2.setString(3, cpayment+"");
                ps2.setString(4,totalDue+"");
                ps2.setString(5,sdto.getsPayMode());
                ps2.setString(6,sdto.getsPaidStatus());
                ps2.setString(7, sdto.getChequeNo());
                ps2.setString(8, sdto.getChequeDate());
                ps2.setString(9, sdto.getChequeBank());
                ps2.setString(10, "1");
                ps2.setString(11, "1");
                ps2.setString(12, (String)session.getAttribute("fYear"));
                int n=ps2.executeUpdate();
                if(n>0)
                {
                	x=1;
                }
				}
				
				if (ps!=null) {
					ps.close();
				}
			    }
			    
			    else
			    {
			    	x=1;
			    }
		
		} catch (Exception e) {
			System.out.println("ex="+e.toString());
		}finally{
			if (con!=null) {
				con.close();
			}
		}
		return x>0;
	}
	
public boolean saveSupplierPaymentfromStockSave(StockMasterDTO sdto) throws DaoException, Exception {
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps2=null;
		ResultSet rs=null;
		int x=0;
		double totalDue =0,newdueAmt=0, gTot=0, cdueAmt=0, dueAmt=0,totalgtot=0;
		String paidStatus="";
		try {
			
			double dueamt=Double.parseDouble(sdto.getDueAmt());
			    if(dueamt>0)
			    {
			 String fyear=(String)session.getAttribute("fYear");
				con=ConnectCore.getConnection();
				String sql="SELECT * FROM supplierpaymentrecord WHERE  supplierid=? AND companyid=? AND userid='1' And financialyear='"+fyear+"'";	
				ps=con.prepareStatement(sql);
				
				ps.setInt(1, sdto.getSupplierId());
				ps.setString(2, CurrentCompAndUser.getCurrentCompany());
				rs=ps.executeQuery();
				
				boolean checkavaliblity=true;
				
				
				
				
				while(rs.next()){
					gTot=Double.parseDouble(rs.getString("totalamount"));
					cdueAmt=Double.parseDouble(rs.getString("dueamount"));
					checkavaliblity=false;
					//dueAmt=Double.parseDouble(rs.getString("dueAmt"));
				}
				
			
				
				if (ps!=null) {
					rs.close();
					ps.close();
				}
				if(sdto.getDueAmt().equalsIgnoreCase("")){
					sdto.setDueAmt("0.00");
				}
				newdueAmt=cdueAmt+Double.parseDouble(sdto.getDueAmt());
				totalDue=newdueAmt;
				totalgtot=gTot+Double.parseDouble(sdto.getDueAmt());
				
				if(totalDue==0){
					paidStatus="Paid";
				} else{
					paidStatus="Partial";
				}
				
			
				
			     String	sql1="UPDATE supplierpaymentrecord SET dueamount=?,paymentdate=?,paymentstatus=?,totalamount=? WHERE  supplierId=? AND companyId=? AND financialyear='"+(String)session.getAttribute("fYear")+"'";	
		
			
				
				
				
				ps=con.prepareStatement(sql1);
				
				ps.setDouble(1, totalDue);
		
				ps.setString(2, sdto.getsPayDate());
				ps.setString(3, paidStatus);
				ps.setString(4, totalgtot+"");
				
			
				
				
				ps.setInt(5, sdto.getSupplierId());
				ps.setString(6, CurrentCompAndUser.getCurrentCompany());
				
				System.out.println("upQ= "+ps);
				x=ps.executeUpdate();
				
				if(checkavaliblity)
				{
					 String query = "insert into supplierpaymentRecord values(default,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					 PreparedStatement pstat =null;
					 pstat = con.prepareStatement(query);
				
					 
					
						
						 pstat.setString(1, sdto.getSupplierId()+"");
						 pstat.setString(2, sdto.getsPayDate());
						 pstat.setString(3, "0");
						 pstat.setString(4, totalDue+"");
						 pstat.setString(5, "Partial");
						 pstat.setString(6, "NA");
						 pstat.setString(7, "1");
						 pstat.setString(8, "1");
						 pstat.setString(9, "NA");
						 pstat.setString(10, "NA");
						 pstat.setString(11, "NA");
						 pstat.setString(12, sdto.getGrandTot());
						 pstat.setString(13,fyear);
						
						 int i=pstat.executeUpdate();
						 if(i>0)
						 {
							 x=1;
						 }
					
				}
				
				if(x>0)
				{
				String sqlr = "insert into supplierpaymenthistory values(default,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps2=con.prepareStatement(sqlr);
                ps2.setString(1, sdto.getSupplierId()+"");
                ps2.setString(2,sdto.getsPayDate());
                ps2.setString(3, "0");
                ps2.setString(4,totalDue+"");
                ps2.setString(5,"NA");
                ps2.setString(6,sdto.getsPaidStatus());
                ps2.setString(7, "NA");
                ps2.setString(8, "NA");
                ps2.setString(9, "NA");
                ps2.setString(10, "1");
                ps2.setString(11, "1");
                ps2.setString(12, (String)session.getAttribute("fYear"));
                int n=ps2.executeUpdate();
                if(n>0)
                {
                	x=1;
                }
				}
				
				if (ps!=null) {
					ps.close();
				}
			    }
			    
			    else
			    {
			    	x=1;
			    }
		
		} catch (Exception e) {
			System.out.println("ex="+e.toString());
		}finally{
			if (con!=null) {
				con.close();
			}
		}
		return x>0;
	}
	//########################################################################################## All Customer Payment Due Calculation methods start here ################################################################################################################################################	
	
	
	
	/*public List<SalesMasterDto> getAllCustomerPaymentDueInfo() throws DaoException,Exception 
	{
		List<SalesMasterDto> salesList=new ArrayList<SalesMasterDto>();
		List<SalesMasterDto> salesList1=new ArrayList<SalesMasterDto>();
		//String sql="SELECT MAX(cPaymentId) FROM customerpayment WHERE  companyId=? GROUP BY saleBillNo";
		String sql="SELECT MAX(cPaymentId),SUM(cPaidAmt) FROM customerpayment WHERE  companyId=? GROUP BY customerId,saleBillNo";
		String sql1="SELECT DISTINCT cp.customerId, c.cName, cp.saleBillNo,cp.cPaidStatus,cp.cPaidAmt,cp.dueAmt,cp.cPayDate,cp.chequeNo,cp.chequeDate,cp.chequeBank,cp.companyId,cp.userId,cp.grandTot,sl.saleDoc FROM customerpayment cp JOIN customermaster c ON cp.customerId=c.customerId  JOIN salemaster sl ON sl.billno=cp.saleBillNo AND cp.cPaymentId=? AND cp. cPaidStatus NOT LIKE 'Paid' AND cp.companyId=?";
		Connection con=null;
		PreparedStatement pstat=null;
		ResultSet rs=null;
		int datediff=0;
		try
		{
			con=ConnectCore.getConnection();	
			pstat=con.prepareStatement(sql);	
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			rs=pstat.executeQuery();
			while(rs.next())
			{
				SalesMasterDto dto=new SalesMasterDto();
				dto.setcPaymentId(rs.getString(1));
				dto.setcPaidAmt(rs.getString(2));
				salesList.add(dto);
			}
			System.out.println("payment size= "+salesList.size());
			if(rs!=null)
			{
				rs.close();
			}
			if(pstat!=null)
			{
				pstat.close();
			}
			
			for (int i = 0; i < salesList.size(); i++) {
				pstat=con.prepareStatement(sql1);	
				pstat.setString(1, salesList.get(i).getcPaymentId());
				pstat.setString(2, CurrentCompAndUser.getCurrentCompany());
				System.out.println("ps= "+pstat);
				rs=pstat.executeQuery();
				while(rs.next())
				{
					SalesMasterDto dto=new SalesMasterDto();
					
					dto.setCustomerId(rs.getString("customerId"));
					dto.setCustomerName(rs.getString("cName"));
					dto.setBillno(rs.getString("saleBillNo"));
					dto.setcPaidStatus(rs.getString("cPaidStatus"));
					//dto.setcPaidAmt(rs.getString("cPaidAmt"));
					dto.setcPaidAmt(salesList.get(i).getcPaidAmt());
					dto.setDueAmt(rs.getString("dueAmt"));
					dto.setcPayDate(rs.getString("cPayDate"));
					dto.setChequeNo(rs.getString("chequeNo"));
					dto.setChequeDate(rs.getString("chequeDate"));
					dto.setChequeBank(rs.getString("chequeBank"));
					dto.setCompanyId(rs.getString("companyId"));
					dto.setUserId(rs.getString("userId"));
					dto.setGrandTotal(rs.getString("grandTot"));
					dto.setDoc(rs.getString("saleDoc"));
					
					datediff=checkDateDiff(rs.getDate("saleDoc"), CurrentDate.mysqlDateConvertor(CurrentDate.getOnlyDateWithMySQLFORMAT()));
					System.out.println("datediff found"+datediff);
					dto.setDateDiff(datediff);
					
					salesList1.add(dto);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception occur"+e);
		}
		finally
		{
		 if(con!=null)	
		 {
			 con.close();
		 }
		}
		return salesList1;
	}
		
	@Override
	public List<SalesMasterDto> getPaymentDueInfoByCustomerId(String customerId) throws DaoException,Exception 
	{
		List<SalesMasterDto> salesList=new ArrayList<SalesMasterDto>();
		List<SalesMasterDto> salesList1=new ArrayList<SalesMasterDto>();
		//String sql="SELECT MAX(cPaymentId) FROM customerpayment WHERE  companyId=? AND customerId=? GROUP BY saleBillNo";
		String sql="SELECT MAX(cPaymentId),SUM(cPaidAmt) FROM customerpayment WHERE  companyId=? AND customerId=? GROUP BY saleBillNo";
		
		String sql1="SELECT DISTINCT cp.customerId, c.cName, cp.saleBillNo,cp.cPaidStatus,cp.cPaidAmt,cp.dueAmt,cp.cPayDate,cp.chequeNo,cp.chequeDate,cp.chequeBank,cp.companyId,cp.userId,cp.grandTot,sl.saleDoc FROM customerpayment cp JOIN customermaster c ON cp.customerId=c.customerId  JOIN salemaster sl ON sl.billno=cp.saleBillNo AND cp.cPaymentId=? AND cp. cPaidStatus NOT LIKE 'Paid' AND cp.companyId=?";
		Connection con=null;
		PreparedStatement pstat=null;
		ResultSet rs=null;
		
		int datediff=0;
		try
		{
			con=ConnectCore.getConnection();	
			pstat=con.prepareStatement(sql);	
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			pstat.setString(2, customerId);
			System.out.println("ps= "+pstat);
			rs=pstat.executeQuery();
			while(rs.next())
			{
				SalesMasterDto dto=new SalesMasterDto();
				dto.setcPaymentId(rs.getString(1));
				dto.setcPaidAmt(rs.getString(2));
				salesList.add(dto);
			}
			System.out.println("payment size= "+salesList.size());
			if(rs!=null)
			{
				rs.close();
			}
			if(pstat!=null)
			{
				pstat.close();
			}
			
			for (int i = 0; i < salesList.size(); i++) {
				pstat=con.prepareStatement(sql1);	
				pstat.setString(1, salesList.get(i).getcPaymentId());
				pstat.setString(2, CurrentCompAndUser.getCurrentCompany());
				System.out.println("ps= "+pstat);
				rs=pstat.executeQuery();
				while(rs.next())
				{
					SalesMasterDto dto=new SalesMasterDto();
					dto.setCustomerId(rs.getString("customerId"));
					dto.setCustomerName(rs.getString("cName"));
					dto.setBillno(rs.getString("saleBillNo"));
					dto.setcPaidStatus(rs.getString("cPaidStatus"));
					//dto.setcPaidAmt(rs.getString("cPaidAmt"));
					dto.setcPaidAmt(salesList.get(i).getcPaidAmt());
					dto.setDueAmt(rs.getString("dueAmt"));
					dto.setcPayDate(rs.getString("cPayDate"));
					dto.setChequeNo(rs.getString("chequeNo"));
					dto.setChequeDate(rs.getString("chequeDate"));
					dto.setChequeBank(rs.getString("chequeBank"));
					dto.setCompanyId(rs.getString("companyId"));
					dto.setUserId(rs.getString("userId"));
					dto.setGrandTotal(rs.getString("grandTot"));
					dto.setDoc(rs.getString("saleDoc"));
					datediff=checkDateDiff(rs.getDate("saleDoc"), CurrentDate.mysqlDateConvertor(CurrentDate.getOnlyDateWithMySQLFORMAT()));
					System.out.println("datediff found"+datediff);
					dto.setDateDiff(datediff);
					salesList1.add(dto);
				}
			}
				
		}
		catch(Exception e)
		{
			System.out.println("Exception occur"+e);
		}
		finally
		{
		 if(con!=null)	
		 {
			 con.close();
		 }
		}
		return salesList1;
	}
	
	

	
	
	*/
		
	//########################################################################################## All Customer Payment Due Calculation methods End here ################################################################################################################################################	
	
	public List<AllBankNames> getAllBankList() throws DaoException
	{
		List<AllBankNames> banknamesDtoList=new ArrayList<AllBankNames>();
		
		banknamesDtoList.add( new AllBankNames("1","Allahabad Bank"));
		banknamesDtoList.add( new AllBankNames("2","Andhra Bank"));
		banknamesDtoList.add( new AllBankNames("3","Axis bank"));
		banknamesDtoList.add( new AllBankNames("4","Bank of Baroda"));
		banknamesDtoList.add( new AllBankNames("5","Bank of India"));
		banknamesDtoList.add( new AllBankNames("6","Bank of Maharashtra"));
		banknamesDtoList.add( new AllBankNames("7","Bandhan financial Bank"));
		banknamesDtoList.add( new AllBankNames("8","Canara Bank"));
		banknamesDtoList.add( new AllBankNames("9","Central Bank of India"));
		banknamesDtoList.add( new AllBankNames("10","Catholic Syrian Bank"));
		banknamesDtoList.add( new AllBankNames("11","City Union Bank"));
		banknamesDtoList.add( new AllBankNames("12","Corporation Bank"));
		banknamesDtoList.add( new AllBankNames("13","Dena Bank"));
		banknamesDtoList.add( new AllBankNames("14","Development Credit Bank"));
		banknamesDtoList.add( new AllBankNames("15","Dhanlaxmi Bank"));
		banknamesDtoList.add( new AllBankNames("16","Federal Bank"));
		banknamesDtoList.add( new AllBankNames("17","HDFC Bank"));
		banknamesDtoList.add( new AllBankNames("18","Indian Bank"));
		banknamesDtoList.add( new AllBankNames("19","Indian Overseas Bank"));
		banknamesDtoList.add( new AllBankNames("20","ICICI Bank"));
		banknamesDtoList.add( new AllBankNames("21","IndusInd Bank"));
		banknamesDtoList.add( new AllBankNames("22","Indian Bank"));
		banknamesDtoList.add( new AllBankNames("23","IDFCk"));
		banknamesDtoList.add( new AllBankNames("24","Idbi bank"));
		banknamesDtoList.add( new AllBankNames("25","Karnataka Bank"));
		banknamesDtoList.add( new AllBankNames("26","Karur Vysya Bank"));
		banknamesDtoList.add( new AllBankNames("27","Kotak Mahindra Bank"));
		banknamesDtoList.add( new AllBankNames("28","RBL Bank"));
		banknamesDtoList.add( new AllBankNames("29","Nainital Bank"));
		banknamesDtoList.add( new AllBankNames("30","South Indian Bank"));
		banknamesDtoList.add( new AllBankNames("31","Oriental Bank of Commerce"));
		banknamesDtoList.add( new AllBankNames("32","Punjab National Bank"));
		banknamesDtoList.add( new AllBankNames("33","Punjab & Sind Bank"));
		banknamesDtoList.add( new AllBankNames("34","Syndicate Bank"));
		banknamesDtoList.add( new AllBankNames("35","State Bank of India"));
		banknamesDtoList.add( new AllBankNames("36","UCO Bank"));
		banknamesDtoList.add( new AllBankNames("37","Union Bank of India"));
		banknamesDtoList.add( new AllBankNames("38","Vijaya Bank"));
		banknamesDtoList.add( new AllBankNames("39","Universe Bank"));
		
		return banknamesDtoList;
		
	}
	
}
