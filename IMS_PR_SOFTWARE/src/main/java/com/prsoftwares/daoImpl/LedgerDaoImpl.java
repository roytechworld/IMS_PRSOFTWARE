package com.prsoftwares.daoImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.struts2.ServletActionContext;

import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dao.LedgerDao;
import com.prsoftwares.dto.CustomerLedgerDto;
import com.prsoftwares.dto.PurchaseLedgerDto;
import com.prsoftwares.util.CurrrentPreviousYear;



public class LedgerDaoImpl implements LedgerDao {

public List<PurchaseLedgerDto> getPLedgerDetailsBySuppplierId(int supplierId ,String mode,String fromdate ,String todate)  throws DaoException, SQLException {
		
		List<PurchaseLedgerDto> list = new LinkedList<PurchaseLedgerDto>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String cid=(String)session.getAttribute("companyId");
			String sql="";
			PreparedStatement pr=null;
			System.out.println("sid= "+supplierId);
			System.out.println("mode= "+mode);
			System.out.println("fd= "+fromdate);
			System.out.println("td= "+todate);
			if(mode==null){
				mode="";
			}
			if(fromdate==null){
				fromdate="";
			}
			if(todate==null){
				todate="";
			}
			try
			{
				con=ConnectCore.getConnection();
				try
				{
					if(mode.equalsIgnoreCase("vat")){
						if(!fromdate.equals("") && !todate.equals("") )
						{
							sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE supplierId=? AND companyId=? AND vat<>0 AND ldate BETWEEN '"+fromdate+"' AND '"+todate+"'";
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
					    }
						else
						{
							sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND vat<>0 AND supplierId=? AND companyId=?";
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
						}
					} else if(mode.equalsIgnoreCase("cst")){
						if(!fromdate.equals("") && !todate.equals("") )
						{
							sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE supplierId=? AND companyId=? AND cst<>0 AND ldate BETWEEN '"+fromdate+"' AND '"+todate+"'";
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
					    }
						else
						{
							sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE cst<>0 AND ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND supplierId=? AND companyId=?";
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
						}
					} else{
						if(!fromdate.equals("") && !todate.equals("") )
						{
							sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE supplierId=? AND companyId=? AND ldate BETWEEN '"+fromdate+"' AND '"+todate+"'";
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
					    }
						else
						{
							sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND supplierId=? AND companyId=?";
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
						}
					}
				}
				catch(NullPointerException n)
				{
					/*if(mode.equalsIgnoreCase("vat")){
						sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND vat<>0 AND supplierId=? AND companyId=?";
						System.out.println("query "+sql);
					    pr=con.prepareStatement(sql);
						pr.setInt(1, supplierId);
						pr.setInt(2, Integer.parseInt(cid));
					} else if(mode.equalsIgnoreCase("cst")){
						sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND cst<>0 AND supplierId=? AND companyId=?";
						System.out.println("query "+sql);
					    pr=con.prepareStatement(sql);
						pr.setInt(1, supplierId);
						pr.setInt(2, Integer.parseInt(cid));
					}else {*/
						sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND supplierId=? AND companyId=?";
						System.out.println("query "+sql);
					    pr=con.prepareStatement(sql);
						pr.setInt(1, supplierId);
						pr.setInt(2, Integer.parseInt(cid));
					//}
				}
				System.out.println("pr= "+pr);
				ResultSet rs=pr.executeQuery();
				while(rs.next()){
					PurchaseLedgerDto plDto=new PurchaseLedgerDto();
					plDto.setBillNo(rs.getString("billNo"));
					plDto.setChallanNo(rs.getString("challanNo"));
					plDto.setDnDoc(rs.getString("ldate"));
					plDto.setDnNo(rs.getString("dnNo"));
					plDto.setSubTot(rs.getString("subTot"));					
					plDto.setLessUnitScheme(rs.getString("lessUnitScheme"));
					plDto.setDiscounts(rs.getString("discounts"));
					plDto.setVat(rs.getString("vat"));
					if(rs.getString("cst")==null)
					{
						plDto.setCst("0.00");	
					}
					else
					{
						plDto.setCst(rs.getString("cst"));		
					}
					if(rs.getString("waybill")==null)
					{
						plDto.setWaybill("NA");	
					}
					else
					{
						plDto.setWaybill(rs.getString("waybill"));	
					}
					plDto.setLorryFreight(rs.getString("lorryFreight"));
					plDto.setPurchaseAmount(rs.getString("purchaseAmount"));
					plDto.setDebitAmount(rs.getString("debitAmount"));
					plDto.setSupplierId(rs.getInt("supplierId"));
					System.out.println("id= "+plDto.getBillNo());
					list.add(plDto);
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					pr.close();
					System.out.println("connection closed");
				}
			}
		return list;
	}
@Override
public List<PurchaseLedgerDto> getpLedgerByModeAndSup(int supplierId ,String mode)  throws DaoException, SQLException {
	
	List<PurchaseLedgerDto> list = new LinkedList<PurchaseLedgerDto>();
	Connection con = null;
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String cid=(String)session.getAttribute("companyId");
		String sql="";
		PreparedStatement pr=null;
		try
		{
			con=ConnectCore.getConnection();
			try
			{
				if(mode.equals("vat"))
				{
					sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND supplierId=? AND vat<>0 AND companyId=?";
					System.out.println("query "+sql);
				    pr=con.prepareStatement(sql);
					pr.setInt(1, supplierId);
					pr.setInt(2, Integer.parseInt(cid));
			    }
				else if(mode.equals("cst"))
				{
					sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND supplierId=? AND cst<>0 AND companyId=?";
					System.out.println("query "+sql);
				    pr=con.prepareStatement(sql);
					pr.setInt(1, supplierId);
					pr.setInt(2, Integer.parseInt(cid));
				}
			}
			catch(NullPointerException n)
			{
				sql="SELECT DISTINCT billNo,challanNo,`dnNo`,ldate,subTot,lessUnitScheme,discounts,vat,cst,waybill,lorryFreight,purchaseAmount,debitAmount,supplierId FROM purchaseledgermaster WHERE ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' AND supplierId=? AND companyId=?";
				System.out.println("query "+sql);
			    pr=con.prepareStatement(sql);
				pr.setInt(1, supplierId);
				pr.setInt(2, Integer.parseInt(cid));
			}
			System.out.println("pr= "+pr);
			ResultSet rs=pr.executeQuery();
			while(rs.next()){
				PurchaseLedgerDto plDto=new PurchaseLedgerDto();
				plDto.setBillNo(rs.getString("billNo"));
				plDto.setChallanNo(rs.getString("challanNo"));
				plDto.setDnDoc(rs.getString("ldate"));
				plDto.setDnNo(rs.getString("dnNo"));
				plDto.setSubTot(rs.getString("subTot"));					
				plDto.setLessUnitScheme(rs.getString("lessUnitScheme"));
				plDto.setDiscounts(rs.getString("discounts"));
				plDto.setVat(rs.getString("vat"));
				if(rs.getString("cst")==null)
				{
					plDto.setCst("0.00");	
				}
				else
				{
					plDto.setCst(rs.getString("cst"));		
				}
				if(rs.getString("waybill")==null)
				{
					plDto.setWaybill("NA");	
				}
				else
				{
					plDto.setWaybill(rs.getString("waybill"));	
				}
				plDto.setLorryFreight(rs.getString("lorryFreight"));
				plDto.setPurchaseAmount(rs.getString("purchaseAmount"));
				plDto.setDebitAmount(rs.getString("debitAmount"));
				plDto.setSupplierId(rs.getInt("supplierId"));
				System.out.println("id= "+plDto.getBillNo());
				list.add(plDto);
			}
			System.out.println("size= "+list.size());
		} catch(Exception e){
			System.out.println("Ex= "+e);
		}finally{
			if(con!=null)
			{
				con.close();
				pr.close();
				System.out.println("connection closed");
			}
		}
	return list;
}

	@Override
	public List<PurchaseLedgerDto> getLedgerByBillNoDnNo(String bill, String dn, int supplierId) throws DaoException, SQLException {
		List<PurchaseLedgerDto> list = new LinkedList<PurchaseLedgerDto>();
		Connection con = null;
		PreparedStatement ps=null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String cid=(String)session.getAttribute("companyId");
			String sql="";
			try
			{
				con=ConnectCore.getConnection();
				if(supplierId>0)
				{
				 sql="SELECT pl.challanNo,pl.itemId,i.itemName,pl.itemSize,pl.vat,pl.itemTot,pl.subTot,pl.lessUnitScheme,pl.discounts,pl.lorryFreight,pl.purchaseAmount,pl.qty,pl.buyRate,pl.cst,pl.waybill,pl.gstrate,pl.gstamt,pl.sgstrate,pl.sgstamt,pl.cgstrate,pl.cgstamt FROM purchaseledgermaster pl, itemmaster i WHERE i.itemId=pl.itemId AND pl.billNo LIKE '"+bill+"' AND pl.dnNo LIKE '"+dn+"' AND pl.supplierId='"+supplierId+"' AND pl.companyId=?";
				}
				else
				{
				 sql="SELECT pl.challanNo,pl.itemId,i.itemName,pl.itemSize,pl.vat,pl.itemTot,pl.subTot,pl.lessUnitScheme,pl.discounts,pl.lorryFreight,pl.purchaseAmount,pl.qty,pl.buyRate ,pl.cst,pl.waybill,pl.gstrate,pl.gstamt,pl.sgstrate,pl.sgstamt,pl.cgstrate,pl.cgstamt  FROM purchaseledgermaster pl, itemmaster i WHERE i.itemId=pl.itemId AND pl.billNo LIKE '"+bill+"' AND pl.dnNo LIKE '"+dn+"' AND pl.companyId=?";
				}
				System.out.println("query "+sql);
				ps=con.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(cid));
				System.out.println("ps= "+ps);
				ResultSet rs=ps.executeQuery();
				
				while(rs.next()){
					PurchaseLedgerDto plDto=new PurchaseLedgerDto();
					plDto.setBillNo(bill);
					plDto.setChallanNo(rs.getString("challanNo"));
					plDto.setItemId(rs.getString("itemId"));
					plDto.setItemName(rs.getString("itemName"));
					plDto.setTotalItemQty(rs.getInt("qty"));
					plDto.setBuyRate(rs.getString("buyRate"));
					plDto.setItemTot(rs.getInt("itemTot"));
					plDto.setItemSize(rs.getString("itemSize"));
					plDto.setLessUnitScheme(rs.getString("lessUnitScheme"));
					plDto.setLorryFreight(rs.getString("lorryFreight"));
					plDto.setSubTot(rs.getString("subTot"));
					plDto.setPurchaseAmount(rs.getString("purchaseAmount"));
					plDto.setDiscounts(rs.getString("discounts"));
					plDto.setVat(rs.getString("vat"));
					plDto.setGstrate(rs.getString("vat"));
					plDto.setGstamt(rs.getString("gstamt"));
					plDto.setCgstrate(rs.getString("cgstrate"));
					plDto.setCgstamt(rs.getString("cgstamt"));
					plDto.setSgstrate(rs.getString("sgstrate"));
					plDto.setSgstamt(rs.getString("sgstamt"));
					if(rs.getString("cst")==null)
					{
						plDto.setCst("0.00");	
					}
					else
					{
						plDto.setCst(rs.getString("cst"));		
					}
					if(rs.getString("waybill")==null)
					{
						plDto.setWaybill("NA");	
					}
					else
					{
						plDto.setWaybill(rs.getString("waybill"));	
					}
					System.out.println("id= "+plDto.getBillNo());
					list.add(plDto);
				}
			System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					ps.close();
					System.out.println("connection closed");
				}
			}
		return list;
	}
	
	
	public List<PurchaseLedgerDto> printLedgerByBillNoDnNo(String bill, String dn) throws DaoException, SQLException {
		List<PurchaseLedgerDto> list = new LinkedList<PurchaseLedgerDto>();
		Connection con = null;
		PreparedStatement ps=null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String cid=(String)session.getAttribute("companyId");
			try
			{
				con=ConnectCore.getConnection();
				String sqlf="SELECT  st.ldate, st.billno,st.itemId,i.itemname,st.qty,st.buyRate,st.itemTot,sto.taxamount ,sto.qty AS ReturnQuantity,sto.itemTot AS ReturnItemTot,sup.SUPPLIERNAME,sto.subTot,sto.debitAmount ,sto.vat,sup.ADDRESS,c.nameofcompany FROM purchaseLedgermaster sto JOIN purchaseLedgermaster st  ON st.itemId=sto.itemId JOIN itemmaster i ON sto.itemId=i.itemId JOIN suppliermaster sup ON sto.supplierId=sup.SupplierId JOIN companymaster c on sto.companyId=c.companyId AND st.billno=sto.billno   AND sto.dnNo='"+dn+"' AND sto.billNo='"+bill+"' AND sto.companyId='"+cid+"'  GROUP BY st.itemId ";
				String sql="SELECT  st.ldate,st.billno,st.itemId,i.itemname,st.qty,sto.buyRate,sto.itemTot,sto.taxamount ,sto.qty AS ReturnQuantity,sto.itemTot AS ReturnItemTot,sup.SUPPLIERNAME,sto.subTot,sto.debitAmount ,sto.itemSize,sto.vat,sup.ADDRESS,c.nameofcompany FROM purchaseLedgermaster sto JOIN purchaseLedgermaster st  ON st.itemId=sto.itemId JOIN itemmaster i ON sto.itemId=i.itemId JOIN suppliermaster sup ON sto.supplierId=sup.SupplierId JOIN companymaster c ON sto.companyId=c.companyId AND st.billno=sto.billno   AND sto.dnNo='"+dn+"' AND sto.billNo='"+bill+"' AND sto.companyId='"+cid+"'  GROUP BY sto.itemSize,sto.itemId";
				System.out.println("query "+sql);
				ps=con.prepareStatement(sql);
				System.out.println("ps= "+ps);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
				PurchaseLedgerDto plDto=new PurchaseLedgerDto();
				    plDto.setDnDoc(rs.getString("ldate"));
					plDto.setBillNo(bill);
					plDto.setItemId(rs.getString("itemId"));
					plDto.setItemName(rs.getString("itemName"));
					plDto.setTotalItemQty(rs.getInt("qty"));
					plDto.setBuyRate(rs.getString("buyRAte"));
					plDto.setItemTot(rs.getInt("itemTot"));
					plDto.setTaxamount(rs.getString("taxamount"));
					plDto.setReturnquantity(rs.getString("ReturnQuantity"));
					plDto.setReturnTotal(rs.getString("ReturnItemTot"));
					plDto.setSupplierName(rs.getString("SUPPLIERNAME"));
					plDto.setSubTot(rs.getString("subTot"));
					plDto.setDebitAmount(rs.getString("debitAmount"));
					plDto.setVat(rs.getString("vat"));
					plDto.setSupplieraddress(rs.getString("ADDRESS"));
					plDto.setCurrentCompanyName(rs.getString("nameofcompany"));
					plDto.setItemSize(rs.getString("itemSize"));
					System.out.println("id= "+plDto.getBillNo());
					list.add(plDto);
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					ps.close();
					System.out.println("connection closed");
				}
			}
		return list;
	}
	
	public boolean uploadFileToWebServer(String localuploadFilePath) {
		boolean result = false;
		String server = "www.testyourapps.in";
		int port = 21;
		String user = "testyourapps";
		String pass = "Apple1234#";
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			File firstLocalFile = new File(localuploadFilePath);
			String firstRemoteFile = "httpdocs/stockPro/"+ "s.pdf";
			InputStream inputStream = new FileInputStream(firstLocalFile);
			System.out.println("Start uploading first file");
			boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
			inputStream.close();
			if (done) {
				System.out.println("File ---" +  " is Uploaded to path " + localuploadFilePath);
				result = true;
			} else {
				System.out.println("Error occur during uploading jpg file file !!!");
			}
		} catch (IOException ex) {
			System.out.println("Error: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	public List<PurchaseLedgerDto> getTotalPurchaseLedger(String fromdate ,String todate,int supplierID)  throws DaoException, SQLException
	{
		List<PurchaseLedgerDto> list = new LinkedList<PurchaseLedgerDto>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String cid=(String)session.getAttribute("companyId");
			String sql="";
			PreparedStatement ps=null;
			ResultSet rs=null;
			try
			{
				con=ConnectCore.getConnection();
				try
				
				{
					/*if(!fromdate.equals("") && !todate.equals(""))
					{
				  */
					 if(supplierID>0)
						{
							sql="SELECT DISTINCT l.supplierId,sup.SUPPLIERNAME FROM purchaseledgermaster l JOIN suppliermaster sup ON l.supplierId=sup.supplierId JOIN itemmaster i ON l.itemId=i.itemId WHERE l.companyId='"+cid+"' AND l.supplierId='"+supplierID+"' AND l.ldate between '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ORDER BY ledgerId ASC";
						}
						else
						{
							sql="SELECT DISTINCT l.supplierId,sup.SUPPLIERNAME FROM purchaseledgermaster l JOIN suppliermaster sup ON l.supplierId=sup.supplierId JOIN itemmaster i ON l.itemId=i.itemId WHERE l.companyId='"+cid+"' AND l.ldate between '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ORDER BY ledgerId ASC ";
						}
					    ps=con.prepareStatement(sql);
						System.out.println("ps= "+ps);
					    rs=ps.executeQuery();
					//}
				}
				catch(NullPointerException n)
				{
					sql="SELECT DISTINCT  l.supplierId,sup.SUPPLIERNAME FROM purchaseledgermaster l JOIN suppliermaster sup ON l.supplierId=sup.supplierId JOIN itemmaster i ON l.itemId=i.itemId WHERE l.companyId=? And l.ldate between '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'  ORDER BY ledgerId ASC";
					ps=con.prepareStatement(sql);
					ps.setString(1, cid);
					System.out.println("ps= "+ps);
					rs=ps.executeQuery();
				}
				System.out.println("query "+sql);
				while(rs.next()){
					
					PurchaseLedgerDto plDto=new PurchaseLedgerDto();
					
					plDto.setSupplierId(Integer.parseInt(rs.getString("supplierId")));
					plDto.setSupplierName(rs.getString("SUPPLIERNAME"));
					
					System.out.println("id= "+plDto.getBillNo());
					list.add(plDto);
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					ps.close();
					System.out.println("connection closed");
				}
			}
		return list;
		
	}
	

	
	
	public List<PurchaseLedgerDto> getTotalPurchaseLedgerByEachSupId(int SupplierId)  throws DaoException, SQLException
	{
		List<PurchaseLedgerDto> list = new LinkedList<PurchaseLedgerDto>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String cid=(String)session.getAttribute("companyId");
			String sql="";
			PreparedStatement ps=null;
			ResultSet rs=null;
			try
			{
				con=ConnectCore.getConnection();
				   sql="SELECT led.billNo, led.dnNo, led.challanNo, led.ldate,i.itemname,led.itemid,led.itemTot,led.subTot,led.purchaseAmount, led.debitAmount FROM purchaseledgermaster led JOIN itemmaster i ON i.itemId=led.itemId WHERE led.supplierId=? AND led.companyId=?";
				    ps=con.prepareStatement(sql);
					ps.setInt(1, SupplierId);
					ps.setString(2, cid);
					System.out.println("ps= "+ps);
				    rs=ps.executeQuery();
				System.out.println("query "+sql);
				while(rs.next()){
					
					PurchaseLedgerDto plDto=new PurchaseLedgerDto();
					plDto.setBillNo(rs.getString("billNo"));
					plDto.setDnNo(rs.getString("dnNo"));
					plDto.setChallanNo(rs.getString("challanNo"));
					plDto.setDnDoc(rs.getString("ldate"));
					plDto.setItemName(rs.getString("itemname"));
					plDto.setItemId(rs.getString("itemid"));
					plDto.setItemTot(Integer.parseInt(rs.getString("itemTot")));
					plDto.setSubTot(rs.getString("subTot"));
					plDto.setPurchaseAmount(rs.getString("purchaseAmount"));
					plDto.setDebitAmount(rs.getString("debitAmount"));
					System.out.println("id= "+plDto.getBillNo());
					list.add(plDto);
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
				con.close();
				ps.close();
				System.out.println("connection closed");
				}
			}
		return list;
		
	}

	@Override
	public List<PurchaseLedgerDto> getAllBillNo(String fromdate, String todate, int supplierId)  throws DaoException, SQLException
	{
		List<PurchaseLedgerDto> list = new LinkedList<PurchaseLedgerDto>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String cid=(String)session.getAttribute("companyId");
			String sql="";
			PreparedStatement ps=null;
			ResultSet rs=null;
			System.out.println("fd "+fromdate);
			System.out.println("td "+todate);
			System.out.println("sid "+supplierId);
			try
			{
				con=ConnectCore.getConnection();
				try
				
				{
					if(fromdate!=null && todate!=null && !fromdate.equalsIgnoreCase("") && !todate.equalsIgnoreCase(""))
					{
							sql="SELECT DISTINCT l.billNo, l.dnNo FROM purchaseledgermaster l  WHERE l.companyId='"+cid+"' AND l.ldate BETWEEN '"+fromdate+"' AND '"+todate+"' AND l.supplierId='"+supplierId+"' ORDER BY l.ledgerId ASC";
					}
					else
					{
						sql="SELECT DISTINCT l.billNo, l.dnNo FROM purchaseledgermaster l  WHERE l.companyId='"+cid+"' AND l.supplierId='"+supplierId+"' ORDER BY l.ledgerId ASC";
					}
					
					    ps=con.prepareStatement(sql);
						
						System.out.println("ps= "+ps);
					    rs=ps.executeQuery();
				}
				catch(NullPointerException n)
				{
					sql="SELECT DISTINCT billNo,dnNo FROM purchaseledgermaster WHERE companyId='"+cid+"' ORDER BY ledgerId ASC";
					ps=con.prepareStatement(sql);
					System.out.println("ps= "+ps);
					rs=ps.executeQuery();
				}
				System.out.println("query "+sql);
				while(rs.next()){
					PurchaseLedgerDto plDto=new PurchaseLedgerDto();
					plDto.setBillNo(rs.getString(1));
					plDto.setDnNo(rs.getString(2));
					
					list.add(plDto);
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					ps.close();
					System.out.println("connection closed");
				}
			}
		return list;
	}

	//################################################### EXCEL EXPORT FUNCTION #######################################################
@Override
public List<PurchaseLedgerDto> getPLedgerExportBySuppplierId(int supplierId ,String mode,String fromdate ,String todate)  throws DaoException, SQLException {
		
		List<PurchaseLedgerDto> list = new LinkedList<PurchaseLedgerDto>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String cid=(String)session.getAttribute("companyId");
			String sql="";
			DecimalFormat df= new DecimalFormat("0.00");
			int flagforTitle=0;
			PreparedStatement pr=null;
			System.out.println("sid= "+supplierId);
			System.out.println("mode= "+mode);
			System.out.println("fd= "+fromdate);
			System.out.println("td= "+todate);
			if(mode==null){
				mode="";
			}
			if(fromdate==null){
				fromdate="";
			}
			if(todate==null){
				todate="";
			}
			try
			{
				con=ConnectCore.getConnection();
				try
				{
					if(mode.equalsIgnoreCase("vat")){
						if(!fromdate.equals("") && !todate.equals("") )
						{
							sql="SELECT i.itemname,s.SUPPLIERNAME,pl.billNo,pl.challanNo,pl.dnNo,pl.waybill,pl.ldate,pl.itemId,pl.itemSize,"+
									"pl.qty,pl.buyRate,pl.itemTot,pl.subTot,pl.lessUnitScheme,pl.discounts,"+
									"pl.vat,pl.cst,pl.lorryFreight,pl.purchaseAmount,pl.debitAmount"+
									" FROM purchaseledgermaster pl, suppliermaster s, itemmaster i WHERE i.itemId=pl.itemId AND s.SupplierId=pl.supplierId AND pl.supplierId=? AND pl.companyId=? AND vat<>0 AND ldate BETWEEN '"+fromdate+"' AND '"+todate+"'";
						
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
							flagforTitle=1;
					    }
						else
						{
							sql="SELECT i.itemname,s.SUPPLIERNAME,pl.billNo,pl.challanNo,pl.dnNo,pl.waybill,pl.ldate,pl.itemId,pl.itemSize,"+
									"pl.qty,pl.buyRate,pl.itemTot,pl.subTot,pl.lessUnitScheme,pl.discounts,"+
									"pl.vat,pl.cst,pl.lorryFreight,pl.purchaseAmount,pl.debitAmount"+
									" FROM purchaseledgermaster pl, suppliermaster s, itemmaster i WHERE i.itemId=pl.itemId AND s.SupplierId=pl.supplierId AND pl.supplierId=? AND pl.companyId=? AND vat<>0 AND ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
							flagforTitle=2;
						}
					} else if(mode.equalsIgnoreCase("cst")){
						if(!fromdate.equals("") && !todate.equals("") )
						{
							sql="SELECT i.itemname,s.SUPPLIERNAME,pl.billNo,pl.challanNo,pl.dnNo,pl.waybill,pl.ldate,pl.itemId,pl.itemSize,"+
									"pl.qty,pl.buyRate,pl.itemTot,pl.subTot,pl.lessUnitScheme,pl.discounts,"+
									"pl.vat,pl.cst,pl.lorryFreight,pl.purchaseAmount,pl.debitAmount"+
									" FROM purchaseledgermaster pl, suppliermaster s, itemmaster i WHERE i.itemId=pl.itemId AND s.SupplierId=pl.supplierId AND pl.supplierId=? AND pl.companyId=? AND cst<>0 AND ldate BETWEEN '"+fromdate+"' AND '"+todate+"'";
							
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
							flagforTitle=3;
					    }
						else
						{
							sql="SELECT i.itemname,s.SUPPLIERNAME,pl.billNo,pl.challanNo,pl.dnNo,pl.waybill,pl.ldate,pl.itemId,pl.itemSize,"+
									"pl.qty,pl.buyRate,pl.itemTot,pl.subTot,pl.lessUnitScheme,pl.discounts,"+
									"pl.vat,pl.cst,pl.lorryFreight,pl.purchaseAmount,pl.debitAmount"+
									" FROM purchaseledgermaster pl, suppliermaster s, itemmaster i WHERE i.itemId=pl.itemId AND s.SupplierId=pl.supplierId AND pl.supplierId=? AND pl.companyId=? AND cst<>0 AND ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
							
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
							flagforTitle=4;
						}
					} else if(supplierId!=0){
						if(!fromdate.equals("") && !todate.equals("") )
						{
							sql="SELECT i.itemname,s.SUPPLIERNAME,pl.billNo,pl.challanNo,pl.dnNo,pl.waybill,pl.ldate,pl.itemId,pl.itemSize,"+
									"pl.qty,pl.buyRate,pl.itemTot,pl.subTot,pl.lessUnitScheme,pl.discounts,"+
									"pl.vat,pl.cst,pl.lorryFreight,pl.purchaseAmount,pl.debitAmount"+
									" FROM purchaseledgermaster pl, suppliermaster s, itemmaster i WHERE i.itemId=pl.itemId AND s.SupplierId=pl.supplierId AND pl.supplierId=? AND pl.companyId=? AND ldate BETWEEN '"+fromdate+"' AND '"+todate+"'";
							
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
							flagforTitle=5;
					    }
						else
						{
							sql="SELECT i.itemname,s.SUPPLIERNAME,pl.billNo,pl.challanNo,pl.dnNo,pl.waybill,pl.ldate,pl.itemId,pl.itemSize,"+
									"pl.qty,pl.buyRate,pl.itemTot,pl.subTot,pl.lessUnitScheme,pl.discounts,"+
									"pl.vat,pl.cst,pl.lorryFreight,pl.purchaseAmount,pl.debitAmount"+
									" FROM purchaseledgermaster pl, suppliermaster s, itemmaster i WHERE i.itemId=pl.itemId AND s.SupplierId=pl.supplierId AND pl.supplierId=? AND pl.companyId=? AND pl.ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
							System.out.println("query "+sql);
						    pr=con.prepareStatement(sql);
							pr.setInt(1, supplierId);
							pr.setInt(2, Integer.parseInt(cid));
							flagforTitle=6;
						}
					} 
					else
					{
						sql="SELECT i.itemname,s.SUPPLIERNAME,pl.billNo,pl.challanNo,pl.dnNo,pl.waybill,pl.ldate,pl.itemId,pl.itemSize,"+
								"pl.qty,pl.buyRate,pl.itemTot,pl.subTot,pl.lessUnitScheme,pl.discounts,"+
								"pl.vat,pl.cst,pl.lorryFreight,pl.purchaseAmount,pl.debitAmount"+
								" FROM purchaseledgermaster pl, suppliermaster s, itemmaster i WHERE i.itemId=pl.itemId AND s.SupplierId=pl.supplierId AND pl.companyId=? AND pl.ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
						System.out.println("query "+sql);
					    pr=con.prepareStatement(sql);
						pr.setInt(1, Integer.parseInt(cid));
						flagforTitle=7;
					}
				}
				catch(Exception n)
				{
					sql="SELECT i.itemname,s.SUPPLIERNAME,pl.billNo,pl.challanNo,pl.dnNo,pl.waybill,pl.ldate,pl.itemId,pl.itemSize,"+
							"pl.qty,pl.buyRate,pl.itemTot,pl.subTot,pl.lessUnitScheme,pl.discounts,"+
							"pl.vat,pl.cst,pl.lorryFreight,pl.purchaseAmount,pl.debitAmount"+
							" FROM purchaseledgermaster pl, suppliermaster s, itemmaster i WHERE i.itemId=pl.itemId AND s.SupplierId=pl.supplierId AND pl.companyId=? AND pl.ldate BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'";
					System.out.println("query "+sql);
				    pr=con.prepareStatement(sql);
					pr.setInt(1, Integer.parseInt(cid));
					flagforTitle=7;
				}
				System.out.println("pr= "+pr);
				ResultSet rs=pr.executeQuery();
				while(rs.next()){
					PurchaseLedgerDto plDto=new PurchaseLedgerDto();
					plDto.setBillNo(rs.getString("billNo"));
					plDto.setChallanNo(rs.getString("challanNo"));
					plDto.setDnDoc(rs.getString("ldate"));
					plDto.setDnNo(rs.getString("dnNo"));
					plDto.setDiscounts(rs.getString("discounts"));
					plDto.setVat(rs.getString("vat"));
					
					plDto.setItemId(rs.getString("itemId"));
					plDto.setItemName(rs.getString("itemName"));
					plDto.setTotalItemQty(rs.getInt("qty"));
					plDto.setBuyRate(rs.getString("buyRate"));
					plDto.setItemTotDoubleFormate(df.format(rs.getDouble("itemTot")));
					plDto.setItemSize(rs.getString("itemSize"));
					plDto.setLessUnitScheme(rs.getString("lessUnitScheme"));
					plDto.setSubTot(rs.getString("subTot"));
					
					if(rs.getString("cst")==null)
					{
						plDto.setCst("0.00");	
					}
					else
					{
						plDto.setCst(rs.getString("cst"));		
					}
					if(rs.getString("waybill")==null)
					{
						plDto.setWaybill("NA");	
					}
					else
					{
						plDto.setWaybill(rs.getString("waybill"));	
					}
					plDto.setLorryFreight(rs.getString("lorryFreight"));
					plDto.setPurchaseAmount(rs.getString("purchaseAmount"));
					plDto.setDebitAmount(rs.getString("debitAmount"));
					plDto.setSupplierName(rs.getString("SUPPLIERNAME"));
					
					if(flagforTitle==1)
					{
						plDto.setTitleforExcel("PURCHASE LEDGER REPORT OF "+rs.getString("SUPPLIERNAME")+" BY VAT% FROM "+fromdate+" TO "+todate);
					}
					else if(flagforTitle==2)
					{
						plDto.setTitleforExcel("PURCHASE LEDGER REPORT OF "+rs.getString("SUPPLIERNAME")+" BY VAT% FROM "+CurrrentPreviousYear.fromDate()+" TO "+CurrrentPreviousYear.toDate());	
					}
					else if(flagforTitle==3)
					{
						plDto.setTitleforExcel("PURCHASE LEDGER REPORT OF "+rs.getString("SUPPLIERNAME")+" BY CST% FROM "+fromdate+" TO "+todate);
					}
					else if(flagforTitle==4)
					{
						plDto.setTitleforExcel("PURCHASE LEDGER REPORT  OF "+rs.getString("SUPPLIERNAME")+" BY CST% FROM "+CurrrentPreviousYear.fromDate()+" TO "+CurrrentPreviousYear.toDate());	
					}
					else if(flagforTitle==5)
					{
						plDto.setTitleforExcel("PURCHASE LEDGER REPORT OF "+rs.getString("SUPPLIERNAME")+" FROM "+fromdate+" TO "+todate);
					}
					else if(flagforTitle==6)
					{
						plDto.setTitleforExcel("PURCHASE LEDGER REPORT OF "+rs.getString("SUPPLIERNAME")+" FROM "+CurrrentPreviousYear.fromDate()+" TO "+CurrrentPreviousYear.toDate());	
					}
					else if(flagforTitle==7)
					{
						plDto.setTitleforExcel(" WHOLE PURCHASE LEDGER REPORT "+CurrrentPreviousYear.fromDate()+" TO "+CurrrentPreviousYear.toDate());	
					}
					
					System.out.println("id= "+plDto.getBillNo());
					list.add(plDto);
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					pr.close();
					System.out.println("connection closed");
				}
			}
		return list;
	}
//################################################# EXCEL EXPORT FUNCTION #################################################################
	


//----------------------------------------------------------------------------------------------------------------------------------------------------------
//********************************************************** Customer Ledger Related Java IMPL CODES start here---> ********************************************
////----------------------------------------------------------------------------------------------------------------------------------------------------------
public List<CustomerLedgerDto> getBillNoDetailsByCustomerID(int cid,String fromdate ,String todate )throws DaoException ,SQLException
{
	List<CustomerLedgerDto> billlist=new ArrayList<CustomerLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	try
	{
		
	String sql="SELECT DISTINCT billno,saleDoc FROM salemaster WHERE regcustomerId='"+cid+"' AND  saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"'";
		con=ConnectCore.getConnection();
		ps=con.prepareStatement(sql);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+sql);
		while(rs.next()){
			CustomerLedgerDto plDto=new CustomerLedgerDto();
			plDto.setBillNo(rs.getString("billno"));
			plDto.setSaleDate(rs.getString("saleDoc"));
			System.out.println("id= "+plDto.getBillNo());
			billlist.add(plDto);
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return billlist;
}


public List<CustomerLedgerDto> getItemDetailsForCustomerLedgerByBillNo(String billno, String fromdate,String todate)throws DaoException ,SQLException
{
	List<CustomerLedgerDto> billlist=new ArrayList<CustomerLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	try
	{
//		String ledgerItemDetails="SELECT pl.itemId,pl.billno,i.itemName,pl.itemSize,pl.saleVat,pl.saleItemtotal,pl.saleSubtotal,pl.saleLessUnit,pl.saleDiscount,pl.saleLoryFreight,pl.saleGrandTotal,pl.saleQty,pl.buyRate FROM salemaster pl, itemmaster i WHERE i.itemId=pl.itemId AND  pl.billno='"+billno+"' AND pl.companyId=1";
//		String ledgerItemDetails="	SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,sime.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscountRupees,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,sime.imenumber FROM salemaster st, itemmaster i,customermaster c,companymaster co,saleimenumber sime WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=sime.itemid AND sime.billno = '"+billno+"' GROUP BY imenumber ORDER BY itemname";
//		SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno  FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno = '"+billno+"'  ORDER BY itemname Limit 1";
		
    String ledgerItemDetails="SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,"
    		+ "st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, "
    		+ "st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,"
    		+ "c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,"
    		+ "co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,"
    		+ "cu.cPaidAmt,cu.dueAmt,cu.cPayDate,st.gstrate,st.gstamt FROM salemaster st, itemmaster i,customermaster c,companymaster co ,"
    		+ "customerpayment cu WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId "
    		+ "AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno=cu.saleBillNo AND   st.billno = '"+billno+"'  "
    		+ "AND st.saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"'  ORDER BY itemname LIMIT 1";
    
    
		con=ConnectCore.getConnection();
		ps=con.prepareStatement(ledgerItemDetails);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+ledgerItemDetails);
		while(rs.next()){
			CustomerLedgerDto plDto=new CustomerLedgerDto();
			plDto.setBillNo(rs.getString("billno"));
		    plDto.setSubTot(rs.getString("saleSubtotal"));
			plDto.setSaleAmount(rs.getString("saleGrandTotal"));
			plDto.setSaleDate(rs.getString("saleDoc"));
			plDto.setPaymentdate(rs.getString("cPayDate"));
	        plDto.setPaymentamount(rs.getString("cPaidAmt"));
	        plDto.setDueamount(rs.getString("dueAmt"));
	        plDto.setVat(rs.getString("gstrate"));
	        plDto.setGstamt(rs.getString("gstamt"));
	        
			System.out.println("id= "+plDto.getBillNo());
			billlist.add(plDto);
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return billlist;
}


public List<CustomerLedgerDto> getpaymentHistoryListByBillNo(String billno)throws DaoException ,SQLException
{
	List<CustomerLedgerDto> billlist=new ArrayList<CustomerLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	HttpServletRequest request=ServletActionContext.getRequest();
	HttpSession session=request.getSession();
	String financialyear=(String)session.getAttribute("lastfinancialyears");
	try
	{
//		String ledgerItemDetails="SELECT pl.itemId,pl.billno,i.itemName,pl.itemSize,pl.saleVat,pl.saleItemtotal,pl.saleSubtotal,pl.saleLessUnit,pl.saleDiscount,pl.saleLoryFreight,pl.saleGrandTotal,pl.saleQty,pl.buyRate FROM salemaster pl, itemmaster i WHERE i.itemId=pl.itemId AND  pl.billno='"+billno+"' AND pl.companyId=1";
//		String ledgerItemDetails="	SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,sime.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscountRupees,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,sime.imenumber FROM salemaster st, itemmaster i,customermaster c,companymaster co,saleimenumber sime WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=sime.itemid AND sime.billno = '"+billno+"' GROUP BY imenumber ORDER BY itemname";
//		SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno  FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno = '"+billno+"'  ORDER BY itemname Limit 1";
		
    String ledgerItemDetails="SELECT CPHID,billno,paymentdate,paidamount,dueamount FROM custpayhistory WHERE billno='"+billno+"' ";
		con=ConnectCore.getConnection();
		ps=con.prepareStatement(ledgerItemDetails);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+ledgerItemDetails);
		while(rs.next()){
			CustomerLedgerDto plDto=new CustomerLedgerDto();
			plDto.setBillNo(rs.getString("billno"));
			plDto.setPaymentdate(rs.getString("paymentdate"));
	        plDto.setPaymentamount(rs.getString("paidamount"));
	        plDto.setDueamount(rs.getString("dueamount"));
			System.out.println("id= "+plDto.getBillNo());
			billlist.add(plDto);
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return billlist;
}
public List<CustomerLedgerDto> getpaymentHistoryListByBillNoByFromDateTodate(String billno,String fromdate, String todate)throws DaoException ,SQLException
{
	List<CustomerLedgerDto> billlist=new ArrayList<CustomerLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	HttpServletRequest request=ServletActionContext.getRequest();
	HttpSession session=request.getSession();
	String financialyear=(String)session.getAttribute("lastfinancialyears");
	try
	{
//		String ledgerItemDetails="SELECT pl.itemId,pl.billno,i.itemName,pl.itemSize,pl.saleVat,pl.saleItemtotal,pl.saleSubtotal,pl.saleLessUnit,pl.saleDiscount,pl.saleLoryFreight,pl.saleGrandTotal,pl.saleQty,pl.buyRate FROM salemaster pl, itemmaster i WHERE i.itemId=pl.itemId AND  pl.billno='"+billno+"' AND pl.companyId=1";
//		String ledgerItemDetails="	SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,sime.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscountRupees,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,sime.imenumber FROM salemaster st, itemmaster i,customermaster c,companymaster co,saleimenumber sime WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=sime.itemid AND sime.billno = '"+billno+"' GROUP BY imenumber ORDER BY itemname";
//		SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno  FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno = '"+billno+"'  ORDER BY itemname Limit 1";
		
    String ledgerItemDetails="SELECT CPHID,billno,paymentdate,paidamount,dueamount FROM custpayhistory WHERE billno='"+billno+"' and paymentdate BETWEEN '"+fromdate+"' AND '"+todate+"'";
		con=ConnectCore.getConnection();
		ps=con.prepareStatement(ledgerItemDetails);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+ledgerItemDetails);
		while(rs.next()){
			CustomerLedgerDto plDto=new CustomerLedgerDto();
			plDto.setBillNo(rs.getString("billno"));
			plDto.setPaymentdate(rs.getString("paymentdate"));
	        plDto.setPaymentamount(rs.getString("paidamount"));
	        plDto.setDueamount(rs.getString("dueamount"));
			System.out.println("id= "+plDto.getBillNo());
			billlist.add(plDto);
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return billlist;
}


public double getMinDueAmountBybillNoAndFROMDATETODATE(String billno,String fromdate, String todate)throws DaoException ,SQLException
{
	double mindue=0.0;
	List<CustomerLedgerDto> billlist=new ArrayList<CustomerLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	HttpServletRequest request=ServletActionContext.getRequest();
	HttpSession session=request.getSession();
	String financialyear=(String)session.getAttribute("lastfinancialyears");
	try
	{
//		String ledgerItemDetails="SELECT pl.itemId,pl.billno,i.itemName,pl.itemSize,pl.saleVat,pl.saleItemtotal,pl.saleSubtotal,pl.saleLessUnit,pl.saleDiscount,pl.saleLoryFreight,pl.saleGrandTotal,pl.saleQty,pl.buyRate FROM salemaster pl, itemmaster i WHERE i.itemId=pl.itemId AND  pl.billno='"+billno+"' AND pl.companyId=1";
//		String ledgerItemDetails="	SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,sime.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscountRupees,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,sime.imenumber FROM salemaster st, itemmaster i,customermaster c,companymaster co,saleimenumber sime WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=sime.itemid AND sime.billno = '"+billno+"' GROUP BY imenumber ORDER BY itemname";
//		SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno  FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno = '"+billno+"'  ORDER BY itemname Limit 1";
		
    String ledgerItemDetails="SELECT MIN(dueamount)AS dueamount  FROM custpayhistory WHERE billno='"+billno+"' and paymentdate BETWEEN '"+fromdate+"' AND '"+todate+"'";
		con=ConnectCore.getConnection();
		ps=con.prepareStatement(ledgerItemDetails);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+ledgerItemDetails);
		while(rs.next()){
			mindue=rs.getDouble("dueamount");
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return mindue;
}


public List<CustomerLedgerDto> getItemDetailsOFCNByBillNo(String billno)throws DaoException ,SQLException
{
	List<CustomerLedgerDto> billlist=new ArrayList<CustomerLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	try
	{

//  String ledgerItemDetails="SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,cu.cPaidAmt,cu.dueAmt,cu.cPayDate,cr.cnNo,cr.creditAmount,cr.cnDoc FROM salemaster st, itemmaster i,customermaster c,companymaster co ,customerpayment cu,creditnote cr WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno=cu.saleBillNo AND cr.saleBillNo=st.billno AND   st.billno = '"+billno+"'   ORDER BY itemname LIMIT 1";
		
    String ledgerItemDetails="SELECT DISTINCT cnNo,cnDoc,saleBillNo,refundSubTot,saleVat ,creditAmount FROM  creditnote WHERE saleBillNo='"+billno+"'";
    
    con=ConnectCore.getConnection();
		ps=con.prepareStatement(ledgerItemDetails);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+ledgerItemDetails);
		while(rs.next()){
			CustomerLedgerDto plDto=new CustomerLedgerDto();
			plDto.setBillNo(rs.getString("saleBillNo"));
		    plDto.setCnNo(rs.getString("cnNo"));
		    plDto.setCreditAmount(rs.getString("creditAmount"));
		    plDto.setCnDoc(rs.getString("cnDoc"));
		    plDto.setVat(rs.getString("saleVat"));
		    plDto.setReturnSubtot(rs.getString("refundSubTot"));
			System.out.println("id= "+plDto.getBillNo());
			billlist.add(plDto);
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return billlist;
}


public List<CustomerLedgerDto> getItemDetailsOFCNByBillNoWithFromDateTodate(String billno,String fromdate, String todate)throws DaoException ,SQLException
{
	List<CustomerLedgerDto> billlist=new ArrayList<CustomerLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	try
	{

//  String ledgerItemDetails="SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,cu.cPaidAmt,cu.dueAmt,cu.cPayDate,cr.cnNo,cr.creditAmount,cr.cnDoc FROM salemaster st, itemmaster i,customermaster c,companymaster co ,customerpayment cu,creditnote cr WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno=cu.saleBillNo AND cr.saleBillNo=st.billno AND   st.billno = '"+billno+"'   ORDER BY itemname LIMIT 1";
		
    String ledgerItemDetails="SELECT DISTINCT cnNo,cnDoc,saleBillNo,refundSubTot,saleVat ,creditAmount FROM  creditnote WHERE saleBillNo='"+billno+"' AND cnDoc BETWEEN '"+fromdate+"' AND '"+todate+"'";
    
    con=ConnectCore.getConnection();
		ps=con.prepareStatement(ledgerItemDetails);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+ledgerItemDetails);
		while(rs.next()){
			CustomerLedgerDto plDto=new CustomerLedgerDto();
			plDto.setBillNo(rs.getString("saleBillNo"));
		    plDto.setCnNo(rs.getString("cnNo"));
		    plDto.setCreditAmount(rs.getString("creditAmount"));
		    plDto.setCnDoc(rs.getString("cnDoc"));
		    plDto.setVat(rs.getString("saleVat"));
		    plDto.setReturnSubtot(rs.getString("refundSubTot"));
			System.out.println("id= "+plDto.getBillNo());
			billlist.add(plDto);
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return billlist;
}


public List<CustomerLedgerDto> getSaleLedger(String fromdate ,String todate,int cstID)  throws DaoException, SQLException
{
	List<CustomerLedgerDto> list = new LinkedList<CustomerLedgerDto>();
	Connection con = null;
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String cid=(String)session.getAttribute("companyId");
		String sql="";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{
			con=ConnectCore.getConnection();
			try
			
			{
				/*if(!fromdate.equals("") && !todate.equals(""))
				{
			  */
				 if((cstID>0 && (fromdate!="") && todate!=""))
					{
						sql="SELECT DISTINCT l.regcustomerId,sup.cName,sup.gsttin FROM salemaster l JOIN customermaster sup ON l.regcustomerId=sup.customerId JOIN itemmaster i ON l.itemId=i.itemId WHERE l.companyId='1' AND l.regcustomerId='"+cstID+"' AND l.saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"' ORDER BY salesId ASC";
					}
					else if((cstID==0) && (fromdate!="") && todate!="")
					{
						sql="SELECT DISTINCT l.regcustomerId,sup.cName,sup.gsttin FROM salemaster l JOIN customermaster sup ON l.regcustomerId=sup.customerId JOIN itemmaster i ON l.itemId=i.itemId WHERE  l.saleDoc BETWEEN '"+fromdate+"' AND '"+todate+"' ORDER BY salesId ASC ";
						
					}
				 
					 else if((cstID>0) && (fromdate=="") && todate=="")
					 {
						 sql="SELECT DISTINCT l.regcustomerId,sup.cName,sup.gsttin FROM salemaster l JOIN customermaster sup ON l.regcustomerId=sup.customerId JOIN itemmaster i ON l.itemId=i.itemId WHERE l.companyId='"+cid+"'  AND l.regcustomerId='"+cstID+"' AND l.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ORDER BY salesId ASC ";
					 }
				 
					else
					{
						sql="SELECT DISTINCT l.regcustomerId,sup.cName,sup.gsttin FROM salemaster l JOIN customermaster sup ON l.regcustomerId=sup.customerId JOIN itemmaster i ON l.itemId=i.itemId WHERE l.companyId='"+cid+"'  AND l.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ORDER BY salesId ASC ";
					}
				    ps=con.prepareStatement(sql);
					System.out.println("ps= "+ps);
				    rs=ps.executeQuery();
				//}
			}
			catch(NullPointerException n)
			{
				sql="SELECT DISTINCT l.regcustomerId,sup.cName,sup.gsttin FROM salemaster l JOIN customermaster sup ON l.regcustomerId=sup.customerId JOIN itemmaster i ON l.itemId=i.itemId WHERE l.companyId='"+cid+"'  AND l.saleDoc BETWEEN '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ORDER BY salesId ASC";
				ps=con.prepareStatement(sql);
				ps.setString(1, cid);
				System.out.println("ps= "+ps);
				rs=ps.executeQuery();
			}
			System.out.println("query "+sql);
			while(rs.next()){
				
				CustomerLedgerDto plDto=new CustomerLedgerDto();
				
				plDto.setCustomerId(rs.getString("regcustomerId"));
				plDto.setCustomerName(rs.getString("cName"));
				plDto.setCustgsttinno(rs.getString("gsttin"));
				
				System.out.println("id= "+plDto.getBillNo());
				list.add(plDto);
			}
			System.out.println("size= "+list.size());
		} catch(Exception e){
			System.out.println("Ex= "+e);
		}finally{
			if(con!=null)
			{
				con.close();
				
				System.out.println("connection closed");
			}
		}
	return list;
	
}
//----------------------------------------------------------------------------------------------------------------------------------------------------------
//********************************************************** Customer Ledger Related Java IMPL CODES Ends here ********************************************
////----------------------------------------------------------------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------------------------------------------------------------
//********************************************************** PURCHASE Ledger Related Java IMPL CODES start here---> ********************************************
////----------------------------------------------------------------------------------------------------------------------------------------------------------
public List<PurchaseLedgerDto> getBillNoDetailsBySupplierIDPURCHASE(int cid,String fromdate,String todate)throws DaoException ,SQLException
{
	List<PurchaseLedgerDto> billlist=new ArrayList<PurchaseLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	try
	{
		
	String sql="SELECT DISTINCT billNo,ldate FROM purchaseledgermaster WHERE supplierId='"+cid+"' AND ldate BETWEEN '"+fromdate+"' AND '"+todate+"'";
		con=ConnectCore.getConnection();
		ps=con.prepareStatement(sql);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+sql);
		while(rs.next()){
			PurchaseLedgerDto plDto=new PurchaseLedgerDto();
			plDto.setBillNo(rs.getString("billNo"));
			plDto.setPurchaseDate(rs.getString("ldate"));
			System.out.println("id= "+plDto.getBillNo());
			billlist.add(plDto);
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return billlist;
}

public List<PurchaseLedgerDto> getItemDetailsForPurchaseLedgerByBillNoPURCHASE(String billno)throws DaoException ,SQLException

{
	List<PurchaseLedgerDto> billlist=new ArrayList<PurchaseLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	try
	{
//		String ledgerItemDetails="SELECT pl.itemId,pl.billno,i.itemName,pl.itemSize,pl.saleVat,pl.saleItemtotal,pl.saleSubtotal,pl.saleLessUnit,pl.saleDiscount,pl.saleLoryFreight,pl.saleGrandTotal,pl.saleQty,pl.buyRate FROM salemaster pl, itemmaster i WHERE i.itemId=pl.itemId AND  pl.billno='"+billno+"' AND pl.companyId=1";
//		String ledgerItemDetails="	SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,sime.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscountRupees,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,sime.imenumber FROM salemaster st, itemmaster i,customermaster c,companymaster co,saleimenumber sime WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=sime.itemid AND sime.billno = '"+billno+"' GROUP BY imenumber ORDER BY itemname";
//		SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno  FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno = '"+billno+"'  ORDER BY itemname Limit 1";	

		String ledgerItemDetails="SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.qty,st.itemTot AS saleItemtotal,st.itemSize,st.ldate,st.subTot,st.purchaseAmount,st.billNo,st.supplierId,st.vat,st.lorryFreight,c.SUPPLIERNAME,c.ADDRESS,c.STATE,c.PIN,c.EMAIL, c.Mobile ,DATE_FORMAT(c.dateofCreation,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,cu.sPaidAmt,cu.dueAmt,cu.sPayDate,st.vat FROM purchaseledgermaster st, itemmaster i,suppliermaster c,companymaster co ,supplierpayment cu WHERE i.itemId=st.itemId AND st.companyId=1 AND st.supplierId=c.supplierId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billNo=cu.purchaseBillNo AND   st.billNo = '"+billno+"' AND st.dnNo='NA'  ORDER BY itemname LIMIT 1";
 // String ledgerItemDetails="  SELECT DISTINCT  st.subtot  FROM purchaseledgermaster st, itemmaster i,suppliermaster c,companymaster co ,supplierpayment cu WHERE i.itemId=st.itemId AND st.companyId=1 AND st.supplierId=c.supplierId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billNo=cu.purchaseBillNo AND   st.billNo = '"+billno+"'   ORDER BY itemname LIMIT 1";
  
  con=ConnectCore.getConnection();
		ps=con.prepareStatement(ledgerItemDetails);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+ledgerItemDetails);
		while(rs.next()){
			PurchaseLedgerDto plDto=new PurchaseLedgerDto();
			plDto.setSubTot(rs.getString("subTot"));
		    plDto.setBillNo(rs.getString("billNo"));
			plDto.setPurchaseAmount(rs.getString("purchaseAmount"));
			plDto.setPurchaseDate(rs.getString("ldate"));
			plDto.setPaymentdate(rs.getString("sPayDate"));
	        plDto.setPaymentamount(rs.getString("sPaidAmt"));
	        plDto.setDueamount(rs.getString("dueAmt"));
	        plDto.setVat(rs.getString("vat"));
			System.out.println("id= "+plDto.getBillNo());
			billlist.add(plDto);
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return billlist;
}


public List<CustomerLedgerDto> getpaymentHistoryListByBillNoPURCHASE(String billno)throws DaoException ,SQLException
{
	List<CustomerLedgerDto> billlist=new ArrayList<CustomerLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	try
	{
//		String ledgerItemDetails="SELECT pl.itemId,pl.billno,i.itemName,pl.itemSize,pl.saleVat,pl.saleItemtotal,pl.saleSubtotal,pl.saleLessUnit,pl.saleDiscount,pl.saleLoryFreight,pl.saleGrandTotal,pl.saleQty,pl.buyRate FROM salemaster pl, itemmaster i WHERE i.itemId=pl.itemId AND  pl.billno='"+billno+"' AND pl.companyId=1";
//		String ledgerItemDetails="	SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,sime.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscountRupees,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,sime.imenumber FROM salemaster st, itemmaster i,customermaster c,companymaster co,saleimenumber sime WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=sime.itemid AND sime.billno = '"+billno+"' GROUP BY imenumber ORDER BY itemname";
//		SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno  FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno = '"+billno+"'  ORDER BY itemname Limit 1";
		
  String ledgerItemDetails="SELECT CPHID,billno,paymentdate,paidamount,dueamount FROM custpayhistory WHERE billno='"+billno+"' ";
		con=ConnectCore.getConnection();
		ps=con.prepareStatement(ledgerItemDetails);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+ledgerItemDetails);
		while(rs.next()){
			CustomerLedgerDto plDto=new CustomerLedgerDto();
			plDto.setBillNo(rs.getString("billno"));
			plDto.setPaymentdate(rs.getString("paymentdate"));
	        plDto.setPaymentamount(rs.getString("paidamount"));
	        plDto.setDueamount(rs.getString("dueamount"));
			System.out.println("id= "+plDto.getBillNo());
			billlist.add(plDto);
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return billlist;
}

public List<PurchaseLedgerDto> getItemDetailsOFDNByBillNoPURCHASE(String billno)throws DaoException ,SQLException
{
	List<PurchaseLedgerDto> billlist=new ArrayList<PurchaseLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;

	try
	{
        HttpServletRequest request=ServletActionContext.getRequest();  
        HttpSession session=request.getSession();
		String fromdate=(String)session.getAttribute("FYFromdate");
		String todate=(String)session.getAttribute("FYTodate");
		
//String ledgerItemDetails="SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,cu.cPaidAmt,cu.dueAmt,cu.cPayDate,cr.cnNo,cr.creditAmount,cr.cnDoc FROM salemaster st, itemmaster i,customermaster c,companymaster co ,customerpayment cu,creditnote cr WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno=cu.saleBillNo AND cr.saleBillNo=st.billno AND   st.billno = '"+billno+"'   ORDER BY itemname LIMIT 1";
		
  String ledgerItemDetails="SELECT DISTINCT d.dnNo,d.dnDoc,d.billNo,p.subTot,p.vat ,p.debitAmount FROM  debitnote d JOIN purchaseledgermaster p ON d.dnNo=p.dnNo WHERE d.supplierId='"+billno+"' and dnDoc between '"+fromdate+"' AND '"+todate+"'";
  
  con=ConnectCore.getConnection();
		ps=con.prepareStatement(ledgerItemDetails);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+ledgerItemDetails);
		while(rs.next()){
			PurchaseLedgerDto plDto=new PurchaseLedgerDto();
			plDto.setBillNo(rs.getString("billNo"));
		    plDto.setDebitAmount(rs.getString("debitAmount"));
		    plDto.setDnDoc(rs.getString("dnDoc"));
		    plDto.setVat(rs.getString("vat"));
		    plDto.setSubTot(rs.getString("subTot"));
		    plDto.setDnNo(rs.getString("dnNo"));
			System.out.println("id= "+plDto.getBillNo());
			billlist.add(plDto);
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return billlist;
}

public List<PurchaseLedgerDto> getItemDetailsOFDNByBillNoPURCHASEByFromDateTodate(String billno,String fromdate,String todate)throws DaoException ,SQLException
{
	List<PurchaseLedgerDto> billlist=new ArrayList<PurchaseLedgerDto>();
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;

	try
	{

//String ledgerItemDetails="SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,cu.cPaidAmt,cu.dueAmt,cu.cPayDate,cr.cnNo,cr.creditAmount,cr.cnDoc FROM salemaster st, itemmaster i,customermaster c,companymaster co ,customerpayment cu,creditnote cr WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno=cu.saleBillNo AND cr.saleBillNo=st.billno AND   st.billno = '"+billno+"'   ORDER BY itemname LIMIT 1";
		
  String ledgerItemDetails="SELECT DISTINCT d.dnNo,d.dnDoc,d.billNo,p.subTot,p.vat ,p.debitAmount FROM  debitnote d JOIN purchaseledgermaster p ON d.dnNo=p.dnNo WHERE d.billNo='"+billno+"' And d.dnDoc between '"+fromdate+"' And '"+todate+"' ";
  
  con=ConnectCore.getConnection();
		ps=con.prepareStatement(ledgerItemDetails);
		System.out.println("ps= "+ps);
	    rs=ps.executeQuery();
		System.out.println("query "+ledgerItemDetails);
		while(rs.next()){
			PurchaseLedgerDto plDto=new PurchaseLedgerDto();
			plDto.setBillNo(rs.getString("billNo"));
		    plDto.setDebitAmount(rs.getString("debitAmount"));
		    plDto.setDnDoc(rs.getString("dnDoc"));
		    plDto.setVat(rs.getString("vat"));
		    plDto.setSubTot(rs.getString("subTot"));
		    plDto.setDnNo(rs.getString("dnNo"));
			System.out.println("id= "+plDto.getBillNo());
			billlist.add(plDto);
		}
		
	} catch(Exception e){
		System.out.println("Ex= "+e);
	}finally{
		if(con!=null)
		{
			con.close();
			
			System.out.println("connection closed");
		}
}
	return billlist;
}


	public List<PurchaseLedgerDto> getPurchaseLedger(String fromdate ,String todate,int supplierID)  throws DaoException, SQLException
	{
		List<PurchaseLedgerDto> list = new LinkedList<PurchaseLedgerDto>();
		Connection con = null;
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String cid=(String)session.getAttribute("companyId");
			String sql="";
			PreparedStatement ps=null;
			ResultSet rs=null;
			try
			{
				con=ConnectCore.getConnection();
				try
				
				{
					/*if(!fromdate.equals("") && !todate.equals(""))
					{
				  */
						if((supplierID>0 && (fromdate!="") && todate!=""))
						{
							sql="SELECT DISTINCT l.supplierid,sup.SUPPLIERNAME FROM supplierpaymentrecord l JOIN suppliermaster sup ON l.supplierid=sup.supplierId  WHERE l.companyId='"+cid+"' AND l.supplierId='"+supplierID+"' AND l.paymentdate between '"+fromdate+"' AND '"+todate+"' ";
						}
						 else if((supplierID==0) && (fromdate!="") && todate!="")
						 {
							 sql="SELECT DISTINCT l.supplierid,sup.SUPPLIERNAME FROM supplierpaymentrecord l JOIN suppliermaster sup ON l.supplierid=sup.supplierId  WHERE l.paymentdate between '"+fromdate+"' AND '"+todate+"' ORDER BY l.supplierid ASC ";
						 }
						
						 else if((supplierID>0) && (fromdate=="") && todate=="")
						 {
							 sql="SELECT DISTINCT l.supplierid,sup.SUPPLIERNAME FROM supplierpaymentrecord l JOIN suppliermaster sup ON l.supplierId=sup.supplierId  WHERE l.supplierId='"+supplierID+"' AND  l.paymentdate between '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'  ";
						 }
						else
						{
							sql="SELECT DISTINCT l.supplierid,sup.SUPPLIERNAME FROM supplierpaymentrecord l JOIN suppliermaster sup ON l.supplierId=sup.supplierId WHERE  l.paymentdate between '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"' ";
						}
					    ps=con.prepareStatement(sql);
						System.out.println("ps= "+ps);
					    rs=ps.executeQuery();
					//}
				}
				catch(NullPointerException n)
				{
					sql="SELECT DISTINCT  l.supplierId,sup.SUPPLIERNAME FROM purchaseledgermaster l JOIN suppliermaster sup ON l.supplierId=sup.supplierId JOIN itemmaster i ON l.itemId=i.itemId WHERE l.companyId=? And l.ldate between '"+CurrrentPreviousYear.fromDate()+"' AND '"+CurrrentPreviousYear.toDate()+"'  ORDER BY ledgerId ASC";
					ps=con.prepareStatement(sql);
					ps.setString(1, cid);
					System.out.println("ps= "+ps);
					rs=ps.executeQuery();
				}
				System.out.println("query "+sql);
				while(rs.next()){
					
					PurchaseLedgerDto plDto=new PurchaseLedgerDto();
					
					plDto.setSupplierId(Integer.parseInt(rs.getString("supplierId")));
					plDto.setSupplierName(rs.getString("SUPPLIERNAME"));
					
					System.out.println("id= "+plDto.getBillNo());
					list.add(plDto);
				}
				System.out.println("size= "+list.size());
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					ps.close();
					System.out.println("connection closed");
				}
			}
		return list;
		
	
	
}
	
	
	public List<PurchaseLedgerDto> getSpaymentHistoryListByBillNo(String billno)throws DaoException ,SQLException
	{
		List<PurchaseLedgerDto> billlist=new ArrayList<PurchaseLedgerDto>();
		Connection con = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{
//			String ledgerItemDetails="SELECT pl.itemId,pl.billno,i.itemName,pl.itemSize,pl.saleVat,pl.saleItemtotal,pl.saleSubtotal,pl.saleLessUnit,pl.saleDiscount,pl.saleLoryFreight,pl.saleGrandTotal,pl.saleQty,pl.buyRate FROM salemaster pl, itemmaster i WHERE i.itemId=pl.itemId AND  pl.billno='"+billno+"' AND pl.companyId=1";
//			String ledgerItemDetails="	SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,sime.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscountRupees,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,sime.imenumber FROM salemaster st, itemmaster i,customermaster c,companymaster co,saleimenumber sime WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=sime.itemid AND sime.billno = '"+billno+"' GROUP BY imenumber ORDER BY itemname";
//			SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno  FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno = '"+billno+"'  ORDER BY itemname Limit 1";
			
	        String ledgerItemDetails="SELECT CPHID,billno,paymentdate,paidamount,dueamount FROM suppayhistory WHERE billno='"+billno+"' ";
			con=ConnectCore.getConnection();
			ps=con.prepareStatement(ledgerItemDetails);
			System.out.println("ps= "+ps);
		    rs=ps.executeQuery();
			System.out.println("query "+ledgerItemDetails);
			while(rs.next()){
				PurchaseLedgerDto plDto=new PurchaseLedgerDto();
				plDto.setBillNo(rs.getString("billno"));
				plDto.setPaymentdate(rs.getString("paymentdate"));
		        plDto.setPaymentamount(rs.getString("paidamount"));
		        plDto.setDueamount(rs.getString("dueamount"));
				System.out.println("id= "+plDto.getBillNo());
				billlist.add(plDto);
			}
			
		} catch(Exception e){
			System.out.println("Ex= "+e);
		}finally{
			if(con!=null)
			{
				con.close();
				
				System.out.println("connection closed");
			}
	}
		return billlist;
	}	
	
	
	public List<PurchaseLedgerDto> getSpaymentHistoryListByBillNoBYFROMDATETODATE(String supplierid,String fromdate,String todate)throws DaoException ,SQLException
	{
		List<PurchaseLedgerDto> billlist=new ArrayList<PurchaseLedgerDto>();
		Connection con = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{
//			String ledgerItemDetails="SELECT pl.itemId,pl.billno,i.itemName,pl.itemSize,pl.saleVat,pl.saleItemtotal,pl.saleSubtotal,pl.saleLessUnit,pl.saleDiscount,pl.saleLoryFreight,pl.saleGrandTotal,pl.saleQty,pl.buyRate FROM salemaster pl, itemmaster i WHERE i.itemId=pl.itemId AND  pl.billno='"+billno+"' AND pl.companyId=1";
//			String ledgerItemDetails="	SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,sime.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscountRupees,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,sime.imenumber FROM salemaster st, itemmaster i,customermaster c,companymaster co,saleimenumber sime WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=sime.itemid AND sime.billno = '"+billno+"' GROUP BY imenumber ORDER BY itemname";
//			SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno  FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno = '"+billno+"'  ORDER BY itemname Limit 1";
			
	        String ledgerItemDetails="SELECT * FROM supplierpaymenthistory WHERE supplierid='"+supplierid+"' and paymentdate BETWEEN '"+fromdate+"' AND '"+todate+"' ";
			con=ConnectCore.getConnection();
			ps=con.prepareStatement(ledgerItemDetails);
			System.out.println("ps= "+ps);
		    rs=ps.executeQuery();
			System.out.println("query "+ledgerItemDetails);
			while(rs.next()){
				PurchaseLedgerDto plDto=new PurchaseLedgerDto();
				plDto.setBillNo("NA");
				plDto.setPaymentdate(rs.getString("paymentdate"));
		        plDto.setPaymentamount(rs.getString("paidamount"));
		        plDto.setDueamount(rs.getString("dueamount"));
				System.out.println("id= "+plDto.getBillNo());
				billlist.add(plDto);
			}
			
		} catch(Exception e){
			System.out.println("Ex= "+e);
		}finally{
			if(con!=null)
			{
				con.close();
				
				System.out.println("connection closed");
			}
	}
		return billlist;
	}	
	
	public double getMinDueAmountByBillnoFromdateTotDate(String billno,String fromdate,String todate)throws DaoException ,SQLException
	{
		double mindue=0.0;
		Connection con = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{
//			String ledgerItemDetails="SELECT pl.itemId,pl.billno,i.itemName,pl.itemSize,pl.saleVat,pl.saleItemtotal,pl.saleSubtotal,pl.saleLessUnit,pl.saleDiscount,pl.saleLoryFreight,pl.saleGrandTotal,pl.saleQty,pl.buyRate FROM salemaster pl, itemmaster i WHERE i.itemId=pl.itemId AND  pl.billno='"+billno+"' AND pl.companyId=1";
//			String ledgerItemDetails="	SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,sime.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscountRupees,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,sime.imenumber FROM salemaster st, itemmaster i,customermaster c,companymaster co,saleimenumber sime WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=sime.itemid AND sime.billno = '"+billno+"' GROUP BY imenumber ORDER BY itemname";
//			SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno  FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno = '"+billno+"'  ORDER BY itemname Limit 1";
			
	        String ledgerItemDetails="SELECT  dueamount  FROM supplierpaymenthistory WHERE supplierid='"+billno+"' and paymentdate BETWEEN '"+fromdate+"' AND '"+todate+"'  order by suphistoryid desc limit 1";
			con=ConnectCore.getConnection();
			ps=con.prepareStatement(ledgerItemDetails);
			System.out.println("ps= "+ps);
		    rs=ps.executeQuery();
			System.out.println("query "+ledgerItemDetails);
			while(rs.next()){
		    mindue=rs.getDouble("dueamount");
			}
			
		} catch(Exception e){
			System.out.println("Ex= "+e);
		}finally{
			if(con!=null)
			{
				con.close();
				
				System.out.println("connection closed");
			}
	}
		return mindue;
	}	
	
	
	public double getMaxDueAmountByBillnoFromdateTotDate(String billno,String fromdate,String todate)throws DaoException ,SQLException
	{
		double mindue=0.0;
		Connection con = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{
//			String ledgerItemDetails="SELECT pl.itemId,pl.billno,i.itemName,pl.itemSize,pl.saleVat,pl.saleItemtotal,pl.saleSubtotal,pl.saleLessUnit,pl.saleDiscount,pl.saleLoryFreight,pl.saleGrandTotal,pl.saleQty,pl.buyRate FROM salemaster pl, itemmaster i WHERE i.itemId=pl.itemId AND  pl.billno='"+billno+"' AND pl.companyId=1";
//			String ledgerItemDetails="	SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,sime.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscountRupees,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,sime.imenumber FROM salemaster st, itemmaster i,customermaster c,companymaster co,saleimenumber sime WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=sime.itemid AND sime.billno = '"+billno+"' GROUP BY imenumber ORDER BY itemname";
//			SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno  FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno = '"+billno+"'  ORDER BY itemname Limit 1";
			
	        String ledgerItemDetails="SELECT MAX(dueamount)AS dueamount  FROM supplierpaymenthistory WHERE supplierid='"+billno+"' and paymentdate BETWEEN '"+fromdate+"' AND '"+todate+"' ";
			con=ConnectCore.getConnection();
			ps=con.prepareStatement(ledgerItemDetails);
			System.out.println("ps= "+ps);
		    rs=ps.executeQuery();
			System.out.println("query "+ledgerItemDetails);
			while(rs.next()){
		    mindue=rs.getDouble("dueamount");
			}
			
		} catch(Exception e){
			System.out.println("Ex= "+e);
		}finally{
			if(con!=null)
			{
				con.close();
				
				System.out.println("connection closed");
			}
	}
		return mindue;
	}	
	

	//----------------------------------------------------------------------------------------------------------------------------------------------------------
	//********************************************************** Debit CoverLetter Related Java IMPL CODES start here---> ********************************************
	////----------------------------------------------------------------------------------------------------------------------------------------------------------
				
		@Override
		public List<PurchaseLedgerDto>printDNLedgerByBillNoDnNo(String bill, String dn) throws DaoException, SQLException {
			List<PurchaseLedgerDto> list = new LinkedList<PurchaseLedgerDto>();
			Connection con = null;
			PreparedStatement ps=null;
				HttpServletRequest request=ServletActionContext.getRequest();
				HttpSession session=request.getSession();
				String cid=(String)session.getAttribute("companyId");
				try
				{
					con=ConnectCore.getConnection();
					String sqlf="SELECT  st.ldate, st.billno,st.itemId,i.itemname,st.qty,st.buyRate,st.itemTot,sto.taxamount ,sto.qty AS ReturnQuantity,sto.itemTot AS ReturnItemTot,sup.SUPPLIERNAME,sto.subTot,sto.debitAmount ,sto.vat,sup.ADDRESS,c.nameofcompany FROM purchaseLedgermaster sto JOIN purchaseLedgermaster st  ON st.itemId=sto.itemId JOIN itemmaster i ON sto.itemId=i.itemId JOIN suppliermaster sup ON sto.supplierId=sup.SupplierId JOIN companymaster c on sto.companyId=c.companyId AND st.billno=sto.billno   AND sto.dnNo='"+dn+"' AND sto.billNo='"+bill+"' AND sto.companyId='"+cid+"'  GROUP BY st.itemId ";
					//String sql="SELECT  st.ldate,st.billno,st.itemId,i.itemname,st.qty,sto.buyRate,sto.itemTot,sto.taxamount ,sto.qty AS ReturnQuantity,sto.itemTot AS ReturnItemTot,sup.SUPPLIERNAME,sto.subTot,sto.debitAmount ,sto.itemSize,sto.vat,sup.ADDRESS,c.nameofcompany FROM purchaseLedgermaster sto JOIN purchaseLedgermaster st  ON st.itemId=sto.itemId JOIN itemmaster i ON sto.itemId=i.itemId JOIN suppliermaster sup ON sto.supplierId=sup.SupplierId JOIN companymaster c ON sto.companyId=c.companyId AND st.billno=sto.billno   AND sto.dnNo='"+cn+"' AND sto.billNo='"+bill+"' AND sto.companyId='"+cid+"'  GROUP BY sto.itemSize,sto.itemId";
					//String sql="SELECT cr.cnDoc, cr.cnNo , cr.saleBillNo, i.itemname, cr.refundItemId, cr.refundItemSize, cr.refundItemQty, sl.saleQty, cr.saleRate, cr.refundItemTot, cr.refundSubTot, cr.saleVat, cr.creditAmount, cus.cName,cus.address,c.nameofcompany FROM creditnote cr JOIN itemmaster i  ON cr.refundItemId=i.itemId JOIN customermaster cus ON cr.customerId=cus.customerId  JOIN companymaster c ON cr.companyId=c.companyId JOIN salemaster sl ON  sl.billno=cr.saleBillno AND sl.buyRate=cr.saleRate AND cr.cnNo='"+cn+"' AND cr.saleBillno='"+bill+"' AND cr.companyId='"+cid+"' GROUP BY cr.refundItemId,cr.refundItemSize";
					String sql="SELECT  st.ldate,st.billno,st.itemId,i.itemname,sto.buyRate,sto.itemTot,sto.taxamount ,sto.qty AS ReturnQuantity,sto.itemTot AS ReturnItemTot,sup.SUPPLIERNAME,sto.subTot,sto.debitAmount ,sto.itemSize,sto.vat,sup.ADDRESS,c.nameofcompany FROM purchaseLedgermaster sto JOIN purchaseLedgermaster st  ON st.itemId=sto.itemId JOIN itemmaster i ON sto.itemId=i.itemId JOIN suppliermaster sup ON sto.supplierId=sup.SupplierId JOIN companymaster c ON sto.companyId=c.companyId AND st.billno=sto.billno   AND sto.dnNo='"+dn+"' AND sto.billNo='"+bill+"' AND sto.companyId='"+cid+"'  GROUP BY sto.itemSize,sto.itemId";
					
					System.out.println("query "+sql);
					ps=con.prepareStatement(sql);
					System.out.println("ps= "+ps);
					ResultSet rs=ps.executeQuery();
					while(rs.next()){
						PurchaseLedgerDto plDto=new PurchaseLedgerDto();
					    plDto.setDnDoc(rs.getString("ldate"));
						plDto.setBillNo(bill);
						plDto.setItemId(rs.getString("itemId"));
						plDto.setItemSize(rs.getString("itemSize"));
						plDto.setItemName(rs.getString("itemName"));
						plDto.setTotalItemQty(getPurchaseQty(bill,rs.getString("itemId"),rs.getString("itemSize")));
						plDto.setBuyRate(rs.getString("buyRAte"));
						plDto.setItemTot(rs.getInt("itemTot"));
						plDto.setTaxamount(rs.getString("taxamount"));
						plDto.setReturnquantity(rs.getString("ReturnQuantity"));
						plDto.setReturnTotal(rs.getString("ReturnItemTot"));
						plDto.setSupplierName(rs.getString("SUPPLIERNAME"));
						plDto.setSubTot(rs.getString("subTot"));
						plDto.setDebitAmount(rs.getString("debitAmount"));
						plDto.setVat(rs.getString("vat"));
						plDto.setSupplieraddress(rs.getString("ADDRESS"));
						plDto.setCurrentCompanyName(rs.getString("nameofcompany"));
						System.out.println("id= "+plDto.getBillNo());
						list.add(plDto);
					}
					System.out.println("size= "+list.size());
					
				} catch(Exception e){
					System.out.println("Ex= "+e);
				}finally{
					if(con!=null)
					{
						con.close();
						ps.close();
						System.out.println("connection closed");
					}
				}
			return list;
		}	
		
		public Integer getPurchaseQty(String billNo,String itemId,String itemSize) throws SQLException
		{
			
			Connection con=null;
			int purchaseQuantity=0;
			
			try
			{
				con=ConnectCore.getConnection();
				String query="SELECT qty FROM purchaseledgermaster WHERE billno='"+billNo+"'AND dnNo='NA' AND itemSize='"+itemSize+"' AND itemId='"+itemId+"'";
				PreparedStatement ps=con.prepareStatement(query);
				
				ResultSet rs=ps.executeQuery();
					while(rs.next())
					{
					  purchaseQuantity=Integer.parseInt(rs.getString("qty"));	 
					}
			
			}catch(Exception e)
			{
				System.out.println("Exception occur type is ---"+e);
				
			}
			finally
			{
				if(con!=null)
				{
					con.close();
					System.out.println("connection closed");
				}
			}
			return purchaseQuantity;
		}
		
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------------
	//********************************************************** Debit CoverLetter Related Java IMPL CODES start here---> ********************************************
	////----------------------------------------------------------------------------------------------------------------------------------------------------------
				
	
	//----------------------------------------------------------------------------------------------------------------------------------------------------------
	//********************************************************** Credit CoverLetter Related Java IMPL CODES start here---> ********************************************
	////----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		@Override
		public List<CustomerLedgerDto> printCNLedgerByBillNoCnNo(String bill,
				String cn) throws DaoException, SQLException {
			List<CustomerLedgerDto> list = new LinkedList<CustomerLedgerDto>();
			Connection con = null;
			PreparedStatement ps=null;
				HttpServletRequest request=ServletActionContext.getRequest();
				HttpSession session=request.getSession();
				String cid=(String)session.getAttribute("companyId");
				try
				{
					con=ConnectCore.getConnection();
					String sqlf="SELECT  st.ldate, st.billno,st.itemId,i.itemname,st.qty,st.buyRate,st.itemTot,sto.taxamount ,sto.qty AS ReturnQuantity,sto.itemTot AS ReturnItemTot,sup.SUPPLIERNAME,sto.subTot,sto.debitAmount ,sto.vat,sup.ADDRESS,c.nameofcompany FROM purchaseLedgermaster sto JOIN purchaseLedgermaster st  ON st.itemId=sto.itemId JOIN itemmaster i ON sto.itemId=i.itemId JOIN suppliermaster sup ON sto.supplierId=sup.SupplierId JOIN companymaster c on sto.companyId=c.companyId AND st.billno=sto.billno   AND sto.dnNo='"+cn+"' AND sto.billNo='"+bill+"' AND sto.companyId='"+cid+"'  GROUP BY st.itemId ";
					//String sql="SELECT  st.ldate,st.billno,st.itemId,i.itemname,st.qty,sto.buyRate,sto.itemTot,sto.taxamount ,sto.qty AS ReturnQuantity,sto.itemTot AS ReturnItemTot,sup.SUPPLIERNAME,sto.subTot,sto.debitAmount ,sto.itemSize,sto.vat,sup.ADDRESS,c.nameofcompany FROM purchaseLedgermaster sto JOIN purchaseLedgermaster st  ON st.itemId=sto.itemId JOIN itemmaster i ON sto.itemId=i.itemId JOIN suppliermaster sup ON sto.supplierId=sup.SupplierId JOIN companymaster c ON sto.companyId=c.companyId AND st.billno=sto.billno   AND sto.dnNo='"+cn+"' AND sto.billNo='"+bill+"' AND sto.companyId='"+cid+"'  GROUP BY sto.itemSize,sto.itemId";
					//String sql="SELECT cr.cnDoc, cr.cnNo , cr.saleBillNo, i.itemname, cr.refundItemId, cr.refundItemSize, cr.refundItemQty, sl.saleQty, cr.saleRate, cr.refundItemTot, cr.refundSubTot, cr.saleVat, cr.creditAmount, cus.cName,cus.address,c.nameofcompany FROM creditnote cr JOIN itemmaster i  ON cr.refundItemId=i.itemId JOIN customermaster cus ON cr.customerId=cus.customerId  JOIN companymaster c ON cr.companyId=c.companyId JOIN salemaster sl ON  sl.billno=cr.saleBillno AND sl.buyRate=cr.saleRate AND cr.cnNo='"+cn+"' AND cr.saleBillno='"+bill+"' AND cr.companyId='"+cid+"' GROUP BY cr.refundItemId,cr.refundItemSize";
					String sql="SELECT cr.cnDoc, cr.cnNo , cr.saleBillNo, i.itemname, cr.refundItemId, cr.refundItemSize, cr.refundItemQty, cr.saleRate, cr.refundItemTot, cr.refundSubTot, cr.saleVat, cr.creditAmount, cus.cName,cus.address,c.nameofcompany FROM creditnote cr JOIN itemmaster i  ON cr.refundItemId=i.itemId JOIN customermaster cus ON cr.customerId=cus.customerId  JOIN companymaster c ON cr.companyId=c.companyId AND cr.cnNo='"+cn+"' AND cr.saleBillno='"+bill+"' AND cr.companyId='"+cid+"' GROUP BY cr.refundItemId,cr.refundItemSize";
					
					System.out.println("query "+sql);
					ps=con.prepareStatement(sql);
					System.out.println("ps= "+ps);
					ResultSet rs=ps.executeQuery();
					while(rs.next()){
						CustomerLedgerDto clDto=new CustomerLedgerDto();
						clDto.setCnDoc(rs.getString("cnDoc"));
						clDto.setBillNo(bill);
						clDto.setItemId(rs.getString("refundItemId"));
						clDto.setItemName(rs.getString("itemname"));
						clDto.setItemSize(rs.getString("refundItemSize"));
						clDto.setTotalItemQty(getSaleQty(bill,rs.getString("refundItemId"),rs.getString("refundItemSize")));
						clDto.setSaleAmount(rs.getString("saleRate"));
						clDto.setReturnquantity(rs.getString("refundItemQty"));
						clDto.setItemTotal(rs.getString("refundItemTot"));
						clDto.setSubTot(rs.getString("refundSubTot"));
						clDto.setVat(rs.getString("saleVat"));
						clDto.setCreditAmount(rs.getString("creditAmount"));
						clDto.setCustomerName(rs.getString("cName"));
						clDto.setCustomeraddress(rs.getString("address"));
						clDto.setCurrentCompanyName(rs.getString("nameofcompany"));
						
						System.out.println("id= "+clDto.getBillNo());
						list.add(clDto);
					}
					System.out.println("size= "+list.size());
					
				} catch(Exception e){
					System.out.println("Ex= "+e);
				}finally{
					if(con!=null)
					{
						con.close();
						ps.close();
						System.out.println("connection closed");
					}
				}
			return list;
		}	
		
		public Integer getSaleQty(String billNo,String itemId,String itemSize) throws SQLException
		{
			
			Connection con=null;
			int SaleQuantity=0;
			
			try
			{
				con=ConnectCore.getConnection();
				String query="SELECT saleQty FROM salemaster WHERE billno='"+billNo+"' AND itemSize='"+itemSize+"' AND itemId='"+itemId+"'";
				PreparedStatement ps=con.prepareStatement(query);
				
				ResultSet rs=ps.executeQuery();
					while(rs.next())
					{
					  SaleQuantity=Integer.parseInt(rs.getString("saleQty"));	 
					}
			
			}catch(Exception e)
			{
				System.out.println("Exception occur type is ---"+e);
				
			}
			finally
			{
				if(con!=null)
				{
					con.close();
					System.out.println("connection closed");
				}
			}
			return SaleQuantity;
		}
		
		
		public List<PurchaseLedgerDto> getSpaymentRecord(String supplierid)throws DaoException ,SQLException
		{
			List<PurchaseLedgerDto> billlist=new ArrayList<PurchaseLedgerDto>();
			Connection con = null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			try
			{
//				String ledgerItemDetails="SELECT pl.itemId,pl.billno,i.itemName,pl.itemSize,pl.saleVat,pl.saleItemtotal,pl.saleSubtotal,pl.saleLessUnit,pl.saleDiscount,pl.saleLoryFreight,pl.saleGrandTotal,pl.saleQty,pl.buyRate FROM salemaster pl, itemmaster i WHERE i.itemId=pl.itemId AND  pl.billno='"+billno+"' AND pl.companyId=1";
//				String ledgerItemDetails="	SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,sime.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscountRupees,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno ,sime.imenumber FROM salemaster st, itemmaster i,customermaster c,companymaster co,saleimenumber sime WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=sime.itemid AND sime.billno = '"+billno+"' GROUP BY imenumber ORDER BY itemname";
//				SELECT DISTINCT st.itemId,i.itemname,st.buyRate,st.saleQty,st.saleItemtotal AS saleItemtotal,st.itemSize,st.saleDoc,st.saleSubtotal,st.saleGrandTotal,st.billno,st.regcustomerId, st.tempCustName,st.saleLessUnit,st.saleDiscount,st.saleVat,st.saleLoryFreight,st.CustomerType,c.cName,c.address,c.state,c.pincode,c.email, c.mobile ,DATE_FORMAT(c.dateofcreate,'%d-%m-%Y ') AS customerDoc,co.nameofcompany,co.phoneno AS CompanyPhone,co.address AS CompanyAddress ,co.email AS CompanyEmail,co.vatno  FROM salemaster st, itemmaster i,customermaster c,companymaster co WHERE i.itemId=st.itemId AND st.companyId=1 AND st.regcustomerId=c.customerId AND st.companyId=co.companyId AND st.itemId=i.itemid AND st.billno = '"+billno+"'  ORDER BY itemname Limit 1";
				HttpServletRequest request=ServletActionContext.getRequest();
				HttpSession session=request.getSession();
				
		        String ledgerItemDetails="SELECT * FROM supplierpaymentrecord WHERE supplierid='"+supplierid+"' and financialyear='"+(String)session.getAttribute("fYear")+"' ";
				con=ConnectCore.getConnection();
				ps=con.prepareStatement(ledgerItemDetails);
				System.out.println("ps= "+ps);
			    rs=ps.executeQuery();
				System.out.println("query "+ledgerItemDetails);
				while(rs.next()){
					PurchaseLedgerDto plDto=new PurchaseLedgerDto();
					plDto.setBillNo("NA");
					plDto.setPaymentdate(rs.getString("paymentdate"));
			        plDto.setTotalamount(rs.getString("totalamount"));
			        plDto.setDueamount(rs.getString("dueamount"));
					
					billlist.add(plDto);
				}
				
			} catch(Exception e){
				System.out.println("Ex= "+e);
			}finally{
				if(con!=null)
				{
					con.close();
					
					System.out.println("connection closed");
				}
		}
			return billlist;
		}	
		
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------------
	//********************************************************** Credit CoverLetter Related Java IMPL CODES Ends here ********************************************
	////----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		
//----------------------------------------------------------------------------------------------------------------------------------------------------------
//********************************************************** PURCHASE Ledger Related Java IMPL CODES Ends here ********************************************
////----------------------------------------------------------------------------------------------------------------------------------------------------------


}
