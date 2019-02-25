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

import com.sun.org.apache.bcel.internal.generic.CPInstruction;

import com.prsoftwares.util.AllBankNames;
import com.prsoftwares.util.CurrentCompAndUser;
import com.prsoftwares.util.CurrentDate;
import com.prsoftwares.util.CurrrentPreviousYear;

import com.prsoftwares.dto.ItemCategoryTypeMasterDTO;
import com.prsoftwares.dto.SalesMasterDto;
import com.prsoftwares.dto.SmsMaster;
import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.CustomerPaymentDao;
import com.prsoftwares.dao.DaoException;

public class CustomerPaymentDaoImpl extends ConnectCore implements CustomerPaymentDao {

	@Override
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
	
	public List<SalesMasterDto> getPaymentHistoryByBillNo(String billno) throws DaoException,Exception 
	{
		List<SalesMasterDto> salesList=new ArrayList<SalesMasterDto>();
		String sql="";
		if(billno=="")
		{
			sql="SELECT DISTINCT cp.customerId, c.cName, cp.saleBillNo,cp.saleDate,cp.cPaidStatus,cp.cPaidAmt,cp.dueAmt,cp.cPayDate,cp.chequeNo,cp.chequeDate,cp.chequeBank,cp.companyId,cp.userId,cp.grandTot,sl.saleDoc,cp.cPayMode  FROM customerpayment cp JOIN customermaster c ON cp.customerId=c.customerId  JOIN salemaster sl ON sl.billno=cp.saleBillNo AND cp.companyId=? GROUP BY cp.customerId";
		}
		else
		{
			sql="SELECT DISTINCT cp.customerId, c.cName, cp.saleBillNo,cp.saleDate,cp.cPaidStatus,cp.cPaidAmt,cp.dueAmt,cp.cPayDate,cp.chequeNo,cp.chequeDate,cp.chequeBank,cp.companyId,cp.userId,cp.grandTot,sl.saleDoc,cp.cPayMode FROM customerpayment cp JOIN customermaster c ON cp.customerId=c.customerId  JOIN salemaster sl ON sl.billno=cp.saleBillNo AND cp.companyId=? AND saleBillNo='"+billno+"'";	
		}
		Connection con=null;
		int datediff=0;
		try
		{
			con=ConnectCore.getConnection();	
			PreparedStatement pstat=con.prepareStatement(sql);	
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs=pstat.executeQuery();
			while(rs.next())
			{
				SalesMasterDto dto=new SalesMasterDto();
				dto.setCustomerId(rs.getString("customerId"));
				dto.setCustomerName(rs.getString("cName"));
				dto.setBillno(rs.getString("saleBillNo"));
				dto.setDoc(rs.getString("saleDate"));
				dto.setcPaidStatus(rs.getString("cPaidStatus"));
				dto.setcPaidAmt(rs.getString("cPaidAmt"));
				dto.setDueAmt(rs.getString("dueAmt"));
				dto.setcPayDate(rs.getString("cPayDate"));
				dto.setChequeNo(rs.getString("chequeNo"));
				dto.setChequeDate(rs.getString("chequeDate"));
				dto.setChequeBank(rs.getString("chequeBank"));
				dto.setCompanyId(rs.getString("companyId"));
				dto.setUserId(rs.getString("userId"));
				dto.setGrandTotal(rs.getString("grandTot"));
				dto.setDoc(rs.getString("saleDoc"));
				dto.setcPayMode(rs.getString("cPayMode"));
				datediff=checkDateDiff(rs.getDate("saleDoc"), rs.getDate("cPayDate"));
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
	}

	/*@Override
	public boolean saveCustomerPayment(SalesMasterDto sdto) throws DaoException, Exception {
		Connection con=null;
		PreparedStatement ps=null;
		int x=0;
		try {
				con=ConnectCore.getConnection();
				try
				{
					if(!sdto.getChequeDate().equals("--"))
					{
						String sql="INSERT INTO customerpayment(saleBillNo,saleDate,cPaidStatus,grandTot,cPaidAmt,dueAmt,cPayMode,cPayDate,chequeNo,chequeDate,chequeBank,companyId,userId,customerId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						ps=con.prepareStatement(sql);
						ps.setString(1, sdto.getBillno());
						ps.setString(2, sdto.getDoc());
						ps.setString(3, sdto.getcPaidStatus());
						ps.setString(4, sdto.getGrandTotal());
						ps.setString(5, sdto.getcPaidAmt());
						ps.setString(6, sdto.getDueAmt());
						ps.setString(7, sdto.getcPayMode());
						ps.setString(8, sdto.getcPayDate());
						ps.setString(9, sdto.getChequeNo());
						ps.setString(10, sdto.getChequeDate());
						ps.setString(11, sdto.getChequeBank());
						ps.setString(12, CurrentCompAndUser.getCurrentCompany());
						ps.setString(13, CurrentCompAndUser.getCurrentUserID());
						ps.setString(14, sdto.getRegcustomerId());
						
						System.out.println("ps="+ps);
						x=ps.executeUpdate();
					}
					else
					{
						String sql="INSERT INTO customerpayment(saleBillNo,saleDate,cPaidStatus,grandTot,cPaidAmt,dueAmt,cPayMode,cPayDate,chequeNo,chequeBank,companyId,userId,customerId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
						ps=con.prepareStatement(sql);
						ps.setString(1, sdto.getBillno());
						ps.setString(2, sdto.getDoc());
						ps.setString(3, sdto.getcPaidStatus());
						ps.setString(4, sdto.getGrandTotal());
						ps.setString(5, sdto.getcPaidAmt());
						ps.setString(6, sdto.getDueAmt());
						ps.setString(7, sdto.getcPayMode());
						ps.setString(8, sdto.getcPayDate());
						ps.setString(9, sdto.getChequeNo());
//						ps.setString(9, sdto.getChequeDate());
						ps.setString(10, sdto.getChequeBank());
						ps.setString(11, CurrentCompAndUser.getCurrentCompany());
						ps.setString(12, CurrentCompAndUser.getCurrentUserID());
						ps.setString(13, sdto.getRegcustomerId());
						System.out.println("ps="+ps);
						x=ps.executeUpdate();	
					}
				}
				catch(Exception d)
				{
					String sql="INSERT INTO customerpayment(saleBillNo,saleDate,cPaidStatus,grandTot,cPaidAmt,dueAmt,cPayMode,cPayDate,chequeNo,chequeBank,companyId,userId,customerId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
					ps=con.prepareStatement(sql);
					ps.setString(1, sdto.getBillno());
					ps.setString(2, sdto.getDoc());
					ps.setString(3, sdto.getcPaidStatus());
					ps.setString(4, sdto.getGrandTotal());
					ps.setString(5, sdto.getcPaidAmt());
					ps.setString(6, sdto.getDueAmt());
					ps.setString(7, sdto.getcPayMode());
					ps.setString(8, sdto.getcPayDate());
					ps.setString(9, sdto.getChequeNo());
//					ps.setString(9, sdto.getChequeDate());
					ps.setString(10, sdto.getChequeBank());
					ps.setString(11, CurrentCompAndUser.getCurrentCompany());
					ps.setString(12, CurrentCompAndUser.getCurrentUserID());
					ps.setString(13, sdto.getRegcustomerId());
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
	}*/
	
	@Override
	
	public boolean saveCustomerPaymentforSales(SalesMasterDto sdto) throws DaoException, Exception {
		Connection con=null;
		PreparedStatement ps=null;
		int x=0;
		try {
				con=ConnectCore.getConnection();
				try
				{
					if(!sdto.getChequeDate().equals("--"))
					{
						String sql="INSERT INTO customerpayment(saleBillNo,saleDate,cPaidStatus,grandTot,cPaidAmt,dueAmt,cPayMode,cPayDate,chequeNo,chequeDate,chequeBank,companyId,userId,customerId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						ps=con.prepareStatement(sql);
						ps.setString(1, sdto.getBillno());
						ps.setString(2, sdto.getDoc());
						ps.setString(3, sdto.getcPaidStatus());
						ps.setString(4, sdto.getGrandTotal());
						ps.setString(5, sdto.getcPaidAmt());
						ps.setString(6, sdto.getDueAmt());
						ps.setString(7, sdto.getcPayMode());
						ps.setString(8, sdto.getcPayDate());
						ps.setString(9, sdto.getChequeNo());
						ps.setString(10, sdto.getChequeDate());
						ps.setString(11, sdto.getChequeBank());
						ps.setString(12, CurrentCompAndUser.getCurrentCompany());
						ps.setString(13, CurrentCompAndUser.getCurrentUserID());
						ps.setString(14, sdto.getRegcustomerId());
						
						System.out.println("ps="+ps);
						x=ps.executeUpdate();
					}
					else
					{
						String sql="INSERT INTO customerpayment(saleBillNo,saleDate,cPaidStatus,grandTot,cPaidAmt,dueAmt,cPayMode,cPayDate,chequeNo,chequeBank,companyId,userId,customerId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
						ps=con.prepareStatement(sql);
						ps.setString(1, sdto.getBillno());
						ps.setString(2, sdto.getDoc());
						ps.setString(3, sdto.getcPaidStatus());
						ps.setString(4, sdto.getGrandTotal());
						ps.setString(5, sdto.getcPaidAmt());
						ps.setString(6, sdto.getDueAmt());
						ps.setString(7, sdto.getcPayMode());
						ps.setString(8, sdto.getcPayDate());
						ps.setString(9, sdto.getChequeNo());
//						ps.setString(9, sdto.getChequeDate());
						ps.setString(10, sdto.getChequeBank());
						ps.setString(11, CurrentCompAndUser.getCurrentCompany());
						ps.setString(12, CurrentCompAndUser.getCurrentUserID());
						ps.setString(13, sdto.getRegcustomerId());
						System.out.println("ps="+ps);
						x=ps.executeUpdate();	
					}
				}
				catch(Exception d)
				{
					String sql="INSERT INTO customerpayment(saleBillNo,saleDate,cPaidStatus,grandTot,cPaidAmt,dueAmt,cPayMode,cPayDate,chequeNo,chequeBank,companyId,userId,customerId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
					ps=con.prepareStatement(sql);
					ps.setString(1, sdto.getBillno());
					ps.setString(2, sdto.getDoc());
					ps.setString(3, sdto.getcPaidStatus());
					ps.setString(4, sdto.getGrandTotal());
					ps.setString(5, sdto.getcPaidAmt());
					ps.setString(6, sdto.getDueAmt());
					ps.setString(7, sdto.getcPayMode());
					ps.setString(8, sdto.getcPayDate());
					ps.setString(9, sdto.getChequeNo());
//					ps.setString(9, sdto.getChequeDate());
					ps.setString(10, sdto.getChequeBank());
					ps.setString(11, CurrentCompAndUser.getCurrentCompany());
					ps.setString(12, CurrentCompAndUser.getCurrentUserID());
					ps.setString(13, sdto.getRegcustomerId());
					System.out.println("ps="+ps);
					x=ps.executeUpdate();
					
					System.out.println("Customer Payment Saved Successfully!.");
					
					
					String query8="insert into custpayhistory(billno,paymentdate,paidamount,dueamount)values(?,?,?,?)";
			     PreparedStatement   ps5=con.prepareStatement(query8);
					ps5.setString(1,sdto.getBillno());
					ps5.setString(2, sdto.getDoc());	
				if(sdto.getDueStatus().equals("Due"))
				 {
					ps5.setString(3, "0.0");
				 }
				 else
				 {
					 ps5.setString(3, sdto.getGrandTotal());
				 }

				if(sdto.getDueStatus().equals("Due"))
				{
					ps5.setString(4, sdto.getGrandTotal());	
				}
				else
				{
					ps5.setString(4,"0.0");
				}
				 
				int k=ps5.executeUpdate();
				
			
					
					
					
					
					
					
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
	
	
	public boolean saveCustomerPayment(SalesMasterDto sdto) throws DaoException, Exception {
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps2=null;
		ResultSet rs=null;
		int x=0;
		double totalDue =0,newPaidAmt=0, gTot=0, cPaidAmt=0, dueAmts=0;
		String paidStatus="";
		try {
			double cpayment=Double.parseDouble(sdto.getcPaidAmt());
			
			if(cpayment>0)
			{
				con=ConnectCore.getConnection();
				String sql="SELECT cPaymentId,grandTot,cPaidAmt,dueAmt FROM customerpayment WHERE saleBillNo=? AND customerId=? AND companyId=?";	
				ps=con.prepareStatement(sql);
				ps.setString(1, sdto.getBillno());
				ps.setString(2, sdto.getCustomerId());
				ps.setString(3, CurrentCompAndUser.getCurrentCompany());
				rs=ps.executeQuery();
				while(rs.next()){
					gTot=Double.parseDouble(rs.getString("grandTot"));
					cPaidAmt=rs.getDouble("cPaidAmt");
					dueAmts=rs.getDouble("dueAmt");
				}
				if (ps!=null) {
					rs.close();
					ps.close();
				}
				
				totalDue=dueAmts-Double.parseDouble(sdto.getcPaidAmt());
				newPaidAmt=cPaidAmt+Double.parseDouble(sdto.getcPaidAmt());

				if(totalDue==0){
					paidStatus="Paid";
				} else{
					paidStatus="Partial";
				}
				String sql1="UPDATE customerpayment SET cPaidAmt=?,dueAmt=?,cPayMode=?,cPayDate=?,cPaidStatus=? WHERE saleBillNo=? AND customerId=? AND companyId=?";	
				ps=con.prepareStatement(sql1);
				
				ps.setDouble(1, newPaidAmt);
				ps.setDouble(2, totalDue);
				ps.setString(3, "Cash");
				ps.setString(4, sdto.getcPayDate());
				ps.setString(5, paidStatus);
				ps.setString(6, sdto.getBillno());
				ps.setString(7, sdto.getCustomerId());
				ps.setString(8, CurrentCompAndUser.getCurrentCompany());
				
				System.out.println("upQ= "+ps);
				x=ps.executeUpdate();
				if(x>0)
				{
					
					String sqlr="insert into custpayhistory(billno,paymentdate,paidamount,dueamount)values(?,?,?,?)";
                    ps2=con.prepareStatement(sqlr);
                    ps2.setInt(1, Integer.parseInt(sdto.getBillno()));
                    ps2.setString(2,sdto.getcPayDate());
                    ps2.setString(3,sdto.getcPaidAmt());
                    ps2.setString(4,totalDue+"");
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
	
	public List<SalesMasterDto> getAllCustomerPaymentDueInfo() throws DaoException,Exception 
	{
		List<SalesMasterDto> salesList=new ArrayList<SalesMasterDto>();
		List<SalesMasterDto> salesList1=new ArrayList<SalesMasterDto>();
		//String sql="SELECT MAX(cPaymentId) FROM customerpayment WHERE  companyId=? GROUP BY saleBillNo";
		String sql="SELECT MAX(cPaymentId), SUM(cPaidAmt) FROM customerpayment WHERE companyId=? AND saleDate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' GROUP BY customerId,salebillNo";
		
		String sql1="SELECT DISTINCT cp.customerId, c.cName, cp.saleBillNo,cp.cPaidStatus,cp.cPaidAmt,cp.dueAmt,cp.cPayDate,cp.chequeNo,cp.chequeDate,cp.chequeBank,cp.companyId,cp.userId,cp.grandTot,sl.saleDoc FROM customerpayment cp JOIN customermaster c ON cp.customerId=c.customerId  JOIN salemaster sl ON sl.billno=cp.saleBillNo AND cp.cPaymentId=? AND cp. cPaidStatus NOT LIKE 'Paid' AND cp.companyId=? AND sl.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
		Connection con=null;
		PreparedStatement pstat=null;
		ResultSet rs=null;
		int datediff=0;
		try
		{
			con=ConnectCore.getConnection();	
			pstat=con.prepareStatement(sql);	
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			System.out.println("qu= "+pstat);
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
		String sql="SELECT MAX(cPaymentId)AS cPaymentId ,SUM(cPaidAmt)AS cPaidAmt FROM customerpayment c JOIN salemaster s ON c.saleBillNo=s.billno WHERE c.companyId=? AND c.customerId=? AND s.saleDoc  BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'  GROUP BY c.saleBillNo";
		String sql1="SELECT DISTINCT cp.customerId, c.cName, cp.saleBillNo,cp.cPaidStatus,cp.cPaidAmt,cp.dueAmt,cp.cPayDate,cp.chequeNo,cp.chequeDate,cp.chequeBank,cp.companyId,cp.userId,cp.grandTot,sl.saleDoc FROM customerpayment cp JOIN customermaster c ON cp.customerId=c.customerId  JOIN salemaster sl ON sl.billno=cp.saleBillNo AND cp.cPaymentId=? AND cp. cPaidStatus NOT LIKE 'Paid' AND cp.companyId=? AND sl.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
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
	
	public int checkDateDiff(Date saledate, Date paymentdate) throws DaoException,Exception 
	{
		int result=0;
		String sql="SELECT DATEDIFF('"+paymentdate+"','"+saledate+"') AS diffdate";
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

	@Override
	public List<SalesMasterDto> getCustomerWithDuePay() throws DaoException, Exception {
		List<SalesMasterDto> customerList=new LinkedList<SalesMasterDto>();
		
		String sql="SELECT DISTINCT cp.customerId, c.cName FROM customerpayment cp, customermaster c WHERE c.customerId=cp.customerId AND cp.companyId=?";
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();		
			PreparedStatement pstat=con.prepareStatement(sql);	
			pstat.setString(1, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs=pstat.executeQuery();
			while(rs.next())
			{
				SalesMasterDto custDto= new SalesMasterDto();
				custDto.setRegcustomerId(rs.getString(1));
				custDto.setCustomerName(rs.getString(2));
				
				System.out.println("cName= "+custDto.getCustomerName());
				customerList.add(custDto);
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
		
		return customerList;
	}
	
	@Override
	
	
	public List<SalesMasterDto> getPaymentBillNoCustomerId(String customerId) throws DaoException, Exception {
		List<SalesMasterDto> sList=new LinkedList<SalesMasterDto>();
		
		String sql="SELECT customerId, saleBillNo,cPaidStatus,cPaidAmt,dueAmt,DATE_FORMAT(cPayDate,'%d-%m-%Y ') AS cPayDate ,chequeNo,chequeDate,chequeBank,companyId,userId,grandTot,DATE_FORMAT(saleDate,'%d-%m-%Y ') AS saleDate from customerpayment WHERE customerId=? AND companyId=?";
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();		
			PreparedStatement pstat=con.prepareStatement(sql);	
			pstat.setString(1, customerId);
			pstat.setString(2, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs=pstat.executeQuery();
			while(rs.next())
			{
				SalesMasterDto dto= new SalesMasterDto();
				dto.setCustomerId(rs.getString("customerId"));
				dto.setBillno(rs.getString("saleBillNo"));
				dto.setcPaidStatus(rs.getString("cPaidStatus"));
				//dto.setcPaidAmt(rs.getString("cPaidAmt"));
				dto.setcPaidAmt(rs.getString("cPaidAmt"));
				dto.setDueAmt(rs.getString("dueAmt"));
				dto.setcPayDate(rs.getString("cPayDate"));
				dto.setChequeNo(rs.getString("chequeNo"));
				dto.setChequeDate(rs.getString("chequeDate"));
				dto.setChequeBank(rs.getString("chequeBank"));
				dto.setCompanyId(rs.getString("companyId"));
				dto.setUserId(rs.getString("userId"));
				dto.setGrandTotal(rs.getString("grandTot"));
				dto.setDoc(rs.getString("saleDate"));
				sList.add(dto);
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
	/////////// OLD function not in use in SMS RAJU //////////////////////////////////////////////////////////////
	/*public List<SalesMasterDto> getPaymentBillNoCustomerId(String customerId) throws DaoException, Exception {
		List<SalesMasterDto> sList=new LinkedList<SalesMasterDto>();
		
		String sql="SELECT DISTINCT saleBillNo FROM customerpayment WHERE customerId=? AND companyId=? AND cPayDate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
		Connection con=null;
		try
		{
			con=ConnectCore.getConnection();		
			PreparedStatement pstat=con.prepareStatement(sql);	
			pstat.setString(1, customerId);
			pstat.setString(2, CurrentCompAndUser.getCurrentCompany());
			ResultSet rs=pstat.executeQuery();
			while(rs.next())
			{
				SalesMasterDto sDto= new SalesMasterDto();
				sDto.setBillno(rs.getString(1));
				
				System.out.println("bill= "+sDto.getBillno());
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
	}*/
	
		
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

	@Override
	public List<SalesMasterDto> getAllCustomernamesByCustomerNameWithDistinct(
			String customerName) throws DaoException, Exception {
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		List<SalesMasterDto> dtolist = new LinkedList<SalesMasterDto>();
		Connection con = null;
		PreparedStatement pstat=null;
		String cid=(String)session.getAttribute("companyId");
		cid=cid.trim();
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT customerId,cName FROM customermaster WHERE cName like'%"+customerName+"%'";
		    pstat = con.prepareStatement(query);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				SalesMasterDto custDto = new SalesMasterDto();

				custDto.setRegcustomerId(rs.getString(1));
				custDto.setCustomerName(rs.getString(2));
				
				
				dtolist.add(custDto);
			}
			System.out.println("list size= "+dtolist.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				pstat.close();
				con.close();
			}
		}
		return dtolist;
		
	}
	
	
	public List<SmsMaster> getSmsDetails() throws DaoException, Exception 
	{
		
		
		List<SmsMaster> dtolist = new LinkedList<SmsMaster>();
		Connection con = null;
		PreparedStatement pstat=null;
		
		
		try {
			con = ConnectCore.getConnection();
			String query = "SELECT * from smsmaster";
		    pstat = con.prepareStatement(query);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				SmsMaster custDto = new SmsMaster();

				custDto.setSmsid(rs.getString(1));
				custDto.setSmsamount(rs.getString(2));
				custDto.setSmsremain(rs.getString(3));
				custDto.setUserid(rs.getString(4));
				custDto.setLastsmsjson(rs.getString(5));
				
				
				
				dtolist.add(custDto);
			}
			System.out.println("list size= "+dtolist.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				pstat.close();
				con.close();
			}
		}
		return dtolist;
		
	}
	
	
	public boolean updateSmsMaster(SmsMaster s)throws DaoException
	{
		boolean result=false;
		Connection con = null;
		PreparedStatement pstat=null;
		
		try {
			con = ConnectCore.getConnection();
			String query = "update smsmaster set smsremaining='"+s.getSmsremain()+"' , lastsmsjason='"+s.getLastsmsjson()+"' where userid='1'";
		    pstat = con.prepareStatement(query);
		    
		    int i=pstat.executeUpdate();
		    if(i>0)
		    {
		    	result=true;
		    }
		
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return result;
	}
	

		
}
