/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prsoftwares.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;


/**
 *
 * @author Pradipto Roy
 */
public class ExcelExport {

    private HSSFWorkbook workbook;

    public ExcelExport() {
    }
    /**
     * 
     * @return instance of ExcelExport class
     */
    public static ExcelExport getInstance() {
        return new ExcelExport();
    }
    /**
     * 
     * @param EntityName fully qualified name of the Entity eg. com.pojo.PurchaseOrder 
     * @return List of columns present in the Entity Provided
     * @throws DAOException 
     * @author Sumit Kumar
     */

    /**
     * Use this method to create a excel workbook. The number of column specified 
     * should match with the number of properties of the POJO
     * @param data List of Map<String, Object> 
     * @param columnNames List<String> contains list of column names, you can use the 
     * getColumnFromEntity(String EntityName) function to get the column name or you can
     * specify a Custom list yourself
     * @param title title to be displayed at the top of the excel file
     * @return HSSFWorkbook which can be sent to the OutputStream
     * @throws Exception 
     */
    public HSSFWorkbook createWorkbook(List<Map<String,Object>> data, List<String> columnNames,String title) throws Exception{

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();

        HSSFCellStyle headerCellStyle = wb.createCellStyle();
        HSSFFont boldFont = wb.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerCellStyle.setFont(boldFont);

        HSSFCellStyle heading=wb.createCellStyle();
        HSSFFont headFont=wb.createFont();
        
        headFont.setFontHeightInPoints((short)12);
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        heading.setAlignment(CellStyle.ALIGN_CENTER);
        heading.setFont(headFont);
        
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell=null;
        try {
            cell=row.createCell(0);
            cell.setCellStyle(heading);
            cell.setCellValue(title);
            
            CellRangeAddress cr=new CellRangeAddress(0,0,0, columnNames.size());
            sheet.addMergedRegion(cr);
            
            row=sheet.createRow(1);
            for (int i = 0; i < columnNames.size(); i++) {
                cell = row.createCell(i);
                cell.setCellStyle(headerCellStyle);
                cell.setCellValue(columnNames.get(i));
            }
            
            Set<String> colTitle=data.get(0).keySet();
            
            ArrayList<String> ct=new ArrayList(colTitle);
            ArrayList<Integer> cols=new ArrayList<Integer>();
            for(String c:ct){
            cols.add(Integer.parseInt(c));
            }
            Collections.sort(cols);
            
            for(int index=2; index<data.size()+2;index++){
	            row=sheet.createRow(index);
	            for(int j=0; j<colTitle.size();j++){
	            		cell=row.createCell(j);
	                    cell.setCellValue((data.get(index-2).get((cols.get(j)!=null)?cols.get(j).toString():"")!=null)?data.get(index-2).get((cols.get(j)!=null)?cols.get(j).toString():"").toString():"");
	                }
            }
        } catch (Exception e) {
            throw new RuntimeException("unable to create workbook",e.getCause());
        }
        return wb;
    }
}
