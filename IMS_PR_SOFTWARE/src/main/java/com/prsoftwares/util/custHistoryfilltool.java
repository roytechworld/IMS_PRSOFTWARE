package com.prsoftwares.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class custHistoryfilltool {
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost/adkdb";

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "root";
	public custHistoryfilltool() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Connection con=null;
		
		try
		{
			con=getConnection();
			PreparedStatement ps=con.prepareStatement("select * from customerpayment");
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				String s="insert into custpayhistory(billno,paymentdate,paidamount,dueamount)values('"+rs.getString("saleBillNo")+"'"
						+ ",'"+rs.getString("saleDate")+"','"+rs.getString("cPaidAmt")+"','"+rs.getString("dueAmt")+"')";
				
				PreparedStatement pss=con.prepareStatement(s);
				int t=pss.executeUpdate();
				if(t>0)
				{
					System.out.println("one record inserted");
				}
				
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("exception occur");
		}
		
		
		
		
		

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
}
