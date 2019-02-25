package com.prsoftwares.util;

import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import java.util.*;
import java.sql.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
public class Readexcel {
public static void main(String[] args) throws Exception{
try {

FileInputStream input = new FileInputStream
("d:\\poi-test.xls");
POIFSFileSystem fs = new POIFSFileSystem (input);
HSSFWorkbook wb = new HSSFWorkbook(fs);
HSSFSheet sheet = wb.getSheetAt(0);
Iterator rows = sheet.rowIterator();

for(int i=0; i<=sheet.getLastRowNum(); i++){
HSSFRow HSSFRow = sheet.getRow(i);
HSSFRow row=null;
String idd =  HSSFRow.getCell(0).getStringCellValue();
System.out.println(idd);
String name = HSSFRow.getCell(1).getStringCellValue();
System.out.println(name);
String salary = HSSFRow.getCell(2).getStringCellValue();
System.out.println(salary);

}

input.close();
System.out.println("Success import excel to mysql table");
}catch (IOException ioe){
System.out.println(ioe);
}
}
}