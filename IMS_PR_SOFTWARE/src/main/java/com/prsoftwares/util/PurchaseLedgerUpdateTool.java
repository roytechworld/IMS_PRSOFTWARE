package com.prsoftwares.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dto.PurchaseLedgerDto;
import com.prsoftwares.dto.SupplierMasterDTO;



public class PurchaseLedgerUpdateTool {
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost/sms";

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "root";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
	    List<PurchaseLedgerDto> ldtolist=new ArrayList<PurchaseLedgerDto>();
		List<SupplierMasterDTO> supplierlist=new ArrayList<SupplierMasterDTO>();
		supplierlist=getAllSupplierDetails();
		
		Connection con = null;
		
		for(int i=0;i<supplierlist.size();i++)
		{
			PurchaseLedgerDto ldto=new PurchaseLedgerDto();
			PurchaseLedgerDto ldto2=new PurchaseLedgerDto();
			double totaldue=getTotaldueAmtBySupplierId(supplierlist.get(i).getSupplierId()+"");
			ldto.setSupplierName(supplierlist.get(i).getSUPPLIERNAME());
			ldto.setSupplierId(supplierlist.get(i).getSupplierId());
			ldto.setDueamount(totaldue+"");
			ldto2=getPaymentsRecordBySupplierId(supplierlist.get(i).getSupplierId()+"");
			ldto.setPaymentmode(ldto2.getPaymentmode())	;
			ldto.setPaymentstatus(ldto2.getPaymentstatus());
			ldto.setPaymentdate(ldto2.getPaymentdate());
			ldto.setChequeno(ldto2.getChequeno());
			ldto.setChequedate(ldto2.getChequedate());
			ldto.setChequebankname(ldto2.getChequebankname());
			ldtolist.add(ldto);
			
		}
//		String	query="";
		 con = getConnection();
		
//		 query = "insert into supplierpaymentRecord(supplierid,paymentdate,paidamount,dueamount,paymentstatus,paymentmode,userid,companyid,chequedate,chequeno,chequeBank)values(?,?,?,?,?,?,?,?,?,?,?)";
		 
//		 query="INSERT INTO supplierpaymentRecord(supplierid,paymentdate,paidamount,dueamount,paymentstatus,paymentmode,userid,companyid,chequedate,chequeno,chequeBank)VALUES('1','2017-01-01','01','01','Na','na','1','1','2017-01-01','na','sbi')";
		 String query = "insert into supplierpaymentRecord values(default,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 PreparedStatement pstat =null;
		 pstat = con.prepareStatement(query);
	
		 
		 for(int k=1;k<ldtolist.size();k++)
			{
			
			 pstat.setString(1, ldtolist.get(k).getSupplierId()+"");
			 pstat.setString(2, ldtolist.get(k).getPaymentdate());
			 pstat.setString(3, "0");
			 pstat.setString(4, ldtolist.get(k).getDueamount());
			 pstat.setString(5, ldtolist.get(k).getPaymentstatus());
			 pstat.setString(6, ldtolist.get(k).getPaymentmode());
			 pstat.setString(7, "1");
			 pstat.setString(8, "1");
			 pstat.setString(9, ldtolist.get(k).getChequedate());
			 pstat.setString(10, ldtolist.get(k).getChequeno());
			 pstat.setString(11, ldtolist.get(k).getChequebankname());
			 pstat.setString(12, ldtolist.get(k).getDueamount());
			 pstat.setString(13,"2016-2017");
			

			 int i=pstat.executeUpdate();
			 
			}
		 
		 
		 String query2 = "insert into supplierpaymenthistory values(default,?,?,?,?,?,?,?,?,?,?,?,?)";
		 PreparedStatement pstat2 =null;
		 pstat2 = con.prepareStatement(query2);
		
		 
		 for(int k=1;k<ldtolist.size();k++)
			{
			
			 pstat2.setString(1, ldtolist.get(k).getSupplierId()+"");
			 pstat2.setString(2, ldtolist.get(k).getPaymentdate());
			 pstat2.setString(3, "0");
			 pstat2.setString(4, ldtolist.get(k).getDueamount());
			 pstat2.setString(5, ldtolist.get(k).getPaymentmode());
			 pstat2.setString(6, ldtolist.get(k).getPaymentstatus());
			 pstat2.setString(7, ldtolist.get(k).getChequeno());
			 pstat2.setString(8, ldtolist.get(k).getChequedate());
			 pstat2.setString(9, ldtolist.get(k).getChequebankname());
			 pstat2.setString(10, "1");
			 pstat2.setString(11, "1");
			 pstat2.setString(12,"2016-2017");
			

			 int i=pstat2.executeUpdate();
			 
			
			}
		 
		 

		}
		catch(Exception e)
		{
			System.out.println("Exception occur"+e);
		}
		

	}
	
	public static List<SupplierMasterDTO> getAllSupplierDetails() throws DaoException, SQLException 
	{
		List<SupplierMasterDTO> list = new LinkedList<SupplierMasterDTO>();
		PreparedStatement pstat =null;
		Connection con = null;
		String query;
		try {
		
		
			query = "SELECT s.supplierId,sg.SUPPLIERNAME FROM supplierpayment s JOIN suppliermaster sg  WHERE s.supplierId=sg.SupplierId AND sPayDate BETWEEN '2016-04-01' AND '2017-03-31' GROUP BY supplierId";
	
			 con = getConnection();
			 pstat = con.prepareStatement(query);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				SupplierMasterDTO dto = new SupplierMasterDTO();
				dto.setSupplierId(rs.getInt("supplierId"));
				dto.setSUPPLIERNAME(rs.getString("SUPPLIERNAME"));
				list.add(dto);
			}
			rs.close();
			if(pstat!=null)
			{
				pstat.close();
			}
			System.out.println(" suppp list size= "+list.size());
			
		
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {
			if (con != null) {
				
				con.close();
			}
		}
		return list;
	}
	
	public static double  getTotaldueAmtBySupplierId(String supplierid) throws DaoException, SQLException 
	{
		double totaldueamt=0;
		
		List<SupplierMasterDTO> list = new LinkedList<SupplierMasterDTO>();
		PreparedStatement pstat =null;
		Connection con = null;
		try {
		
			String query="";
				
			 query = "SELECT * FROM supplierpayment   WHERE sPayDate BETWEEN '2016-04-01' AND '2017-03-31' AND supplierId='"+supplierid+"'";
			 con = getConnection();
			 pstat = con.prepareStatement(query);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {
				
				totaldueamt=totaldueamt+rs.getDouble("dueAmt");

				
				
			
				
			}
			System.out.println("Supplier ID  "+supplierid);
			System.out.println("Total due found  "+totaldueamt);
			rs.close();
			if(pstat!=null)
			{
				pstat.close();
			}
			System.out.println(" suppp list size= "+list.size());
			
		
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {
			if (con != null) {
				
				con.close();
			}
		}
		return totaldueamt;
		
		
	}
	
public static Connection getConnection()
{
	
	Connection conn=null;

	try
	{
		
	     Class.forName("com.mysql.jdbc.Driver");

	      //STEP 3: Open a connection
	      System.out.println("Connecting to database...");
	      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		
	}
	catch(Exception e)
	{
		System.out.println("Exception occur"+e);
	}
	
	
	
	
	
	
	
	
	return conn;
	
}

public static PurchaseLedgerDto getPaymentsRecordBySupplierId(String supplierid) throws DaoException, SQLException 
{
	PurchaseLedgerDto pt = new PurchaseLedgerDto();
	PreparedStatement pstat =null;
	Connection con = null;
	String query;
	try {
	
	
		query = "SELECT * FROM supplierpayment  WHERE sPayDate BETWEEN '2016-04-01' AND '2017-03-31' AND supplierId='"+supplierid+"' ORDER BY sPayDate DESC LIMIT 1";

		 con = getConnection();
		 pstat = con.prepareStatement(query);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			

			
			pt.setPaymentdate(rs.getString("sPayDate"));
			pt.setPaymentmode(rs.getString("sPayMode"));
			pt.setPaymentstatus(rs.getString("sPaidStatus"));
			pt.setChequedate(rs.getString("chequeDate"));
			pt.setChequebankname(rs.getString("chequeBank"));
			pt.setChequeno(rs.getString("chequeNo"));
			
			
		}
		rs.close();
		if(pstat!=null)
		{
			pstat.close();
		}
	
		
	
	} catch (Exception e) {
		e.printStackTrace();
	
	} finally {
		if (con != null) {
			
			con.close();
		}
	}
	return pt;
}


}
	




