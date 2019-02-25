package com.prsoftwares.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentDate {

	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String d = dateFormat.format(date);
		
		return d;
	}
	
	/*public static String getOnlyDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		Date date = new Date();
		String d = dateFormat.format(date);
		
		return d;
	}*/
	
	public static Date strindtoMysql(String dates)throws Exception
	{
		
		String string = "2012-12-31"; //You have like this now

		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Date date = formatter.parse(string);
		System.out.println(date);
		
		return date;
		
		
	}
	
	
	
	public static String getOnlyDateWithMySQLFORMAT() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String d = dateFormat.format(date);
		
		return d;
		
		
		
		
		
		
		
		
		
		
		
	}
	
	public static java.sql.Date mysqlDateConvertor(String s)
	{
		java.sql.Date sqlDate=null;
		String lastCrawlDate = s;
		Date utilDate;
		try {
			utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastCrawlDate);
			 sqlDate = new java.sql.Date(utilDate.getTime()); 
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return  sqlDate;
	}
}
