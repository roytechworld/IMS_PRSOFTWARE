package com.prsoftwares.util;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;


import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class SessionInterceptor implements Interceptor {
	  private static final long serialVersionUID = -5011962009065225959L;
	  
	    @Override
	    public void destroy() {
	        //release resources here
	    }
	 
	    @Override
	    public void init() {
	        // create resources here
	    }
	    @Override
	    public String intercept(ActionInvocation actionInvocation)throws Exception {
	     
	    	String result="";
	    	HttpServletRequest request=ServletActionContext.getRequest();
	    	HttpServletResponse response=ServletActionContext.getResponse();
		    HttpSession session = request.getSession();
		    if(session.getAttribute("login")!=null)
		    {
	    	System.out.println("login found");
	    	result= actionInvocation.invoke();
		    }
		    else
		    {
		    	request.setAttribute("loginStatus", "sessionExpired");
		    	result="sessionExpired";
		    }
		    
	       return result;
	    }

}
