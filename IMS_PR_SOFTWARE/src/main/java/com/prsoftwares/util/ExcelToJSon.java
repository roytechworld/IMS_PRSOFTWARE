package com.prsoftwares.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.media.sound.InvalidFormatException;

public class ExcelToJSon {
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws JSONException 
	 */
	public static void main(String[] args) {
		//	String file = "/Users/miniuno/Desktop/cameras.xls";
		
        
		String  file="E://example.xls";
	    String jsonresult=genGsonData(file);
	    
	    System.out.println(jsonresult);
		
		
		
		
		
	     BufferedWriter bufferedWriter = null;
	        try {
	            String strContent = jsonresult;
	            File myFile = new File("E:/Exp.json");
	            // check if file exist, otherwise create the file before writing
	            if (!myFile.exists()) {
	                myFile.createNewFile();
	            }
	            Writer writer = new FileWriter(myFile);
	            bufferedWriter = new BufferedWriter(writer);
	            bufferedWriter.write(strContent);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally{
	            try{
	                if(bufferedWriter != null) bufferedWriter.close();
	            } catch(Exception ex){
	                 
	            }}
		
		
		
		
		
		
		
	}
	
	
	public static String genGsonData(String file) 
	{
	String jsonrs="";	
	try
	{
		FileInputStream inp = new FileInputStream( file );
		Workbook workbook = WorkbookFactory.create( inp );

		// Get the first Sheet.
		Sheet sheet = workbook.getSheetAt( 0 );

		    // Start constructing JSON.
		    JSONObject json = new JSONObject();

		    // Iterate through the rows.
		    JSONArray rows = new JSONArray();
		    for ( Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext(); )
		    {
		        Row row = rowsIT.next();
		        JSONObject jRow = new JSONObject();

		        // Iterate through the cells.
		        JSONArray cells = new JSONArray();
		        for ( Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext(); )
		        {
		            Cell cell = cellsIT.next();
		            cells.put( cell.getStringCellValue() );
		        }
		        jRow.put( "cell", cells );
		        rows.put( jRow );
		    }

		    // Create the JSON.
		    json.put( "rows", rows );
		    jsonrs=json.toString();
		// Get the JSON text.
		    
		    
		    
		    
		    

		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
	}
	catch(Exception e)
	{
		System.out.println("Exception occur"+e);
	}
		return jsonrs;
	}
	
}