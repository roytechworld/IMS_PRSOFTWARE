package com.prsoftwares.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
public class CurrrentPreviousYear {
	
	public static String fromDate()
	{
		String fromDate="";
		try
		{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		fromDate= (String) session.getAttribute("FYFromdate");
		}
		catch(Exception e)
		{
		System.out.println("Anexception occur" +e);
		}
		return fromDate;
	}
	
	public static String toDate()
	{
		String toDate="";
		try
		{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		toDate= (String) session.getAttribute("FYTodate");
		}
		catch(Exception e)
		{
		System.out.println("Anexception occur" +e);
		}
		return toDate;
	}

}
