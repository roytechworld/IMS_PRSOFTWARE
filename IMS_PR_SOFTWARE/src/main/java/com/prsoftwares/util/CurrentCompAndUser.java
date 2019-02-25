package com.prsoftwares.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

public class CurrentCompAndUser {
	public static String getCurrentCompany()
	{
		String cid="";
		try
		{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		cid= (String) session.getAttribute("companyId");
		
		}
		catch(Exception e)
		{
			System.out.println("Anexception occur" +e);
		}
		return cid;
	}
	
	public static String getCurrentUserID()
	{
		String uid="";
		try
		{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		uid= (String) session.getAttribute("userId");
		
		}
		catch(Exception e)
		{
			System.out.println("Anexception occur" +e);
		}
		return uid;
	}

}
