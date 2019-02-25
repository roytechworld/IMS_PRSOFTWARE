package com.prsoftwares.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.struts2.ServletActionContext;

import com.orsoncharts.util.json.JSONObject;
import com.orsoncharts.util.json.parser.JSONParser;
import com.orsoncharts.util.json.parser.ParseException;

public class jsonformat {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		
//		String sResult1=result : "{"status":"success","mobilenumbers":"8961280870","remainingcredits":5,"msgcount":1,"selectedRoute":"transactional","refid":10878306132,"senttime":"2017-01-02 18:20:01"}" ;

		
//		String[] j='{"status":"success","mobilenumbers":"8961280870","remainingcredits":5,"msgcount":1,"selectedRoute":"transactional","refid":10878306132,"senttime":"2017-01-02 18:20:01"}';
		
		
		 try {
		String s=ServletActionContext.getServletContext().getRealPath("json/");
		System.out.println(s);
			 
		BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(s)));
        
        //write contents of StringBuffer to a file
       
			bwr.write("xZx");
			
			   bwr.flush();
		       
		        //close the stream
		        bwr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        //flush the stream
     
       
        System.out.println("Content of StringBuffer written to File.");
		
		
		
		
		
		

	}

}
