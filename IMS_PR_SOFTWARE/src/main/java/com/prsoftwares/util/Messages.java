package com.prsoftwares.util;

public final class Messages {
	//##################################Item Related Messages  start here ################################################################
	public static String ItemTypeSaveSuccesfully="<p style='color:green;'>Item Type Saved Successfully!<br/><a href='#' onmouseover='called()' onmousemove='called()'  onmouseout='called()' onclick='itemCategoryCall()'>Add Another</a></p>";
	public static String UpdateItemTypeSuccessfully="<p style='color:green;'>Item Type Updated Successfully!</p>";
	public static String ItemSaveSuccesfully="<p style='color:green;'>Item Saved Successfully!<br/><a href='#' onmouseover='called()' onmousemove='called()'  onmouseout='called()' onclick='addItemCall()'>Add Another</a></p>";
	public static String UpdateItemSuccessfully="<p style='color:green;'>Item Updated Successfully!</p>";
	public static String BrandSaveSuccesfully="<p style='color:green;'>Brand Saved Successfully!<br/><a href='#' onmouseover='called()' onmousemove='called()'  onmouseout='called()' onclick='addBrandCall()'>Add Another</a></p>";
	public static String UpdateBrandSuccessfully="<p style='color:green;'>Brand Updated Successfully!</p>";
	public static String SizeSaveSuccesfully="<p style='color:green;'>Size Saved Successfully!<br/><a href='#' onmouseover='called()' onmousemove='called()'  onmouseout='called()' onclick='addSizeCall()'>Add Another</a></p>";
	public static String UpdateSizeSuccessfully="<p style='color:green;'>Size Updated Successfully!</p>";
	//##################################Item Related Messages  End Here ##################################################################
	
	//##################################Scheme Related Messages Start Here #############################################################
	public static String SchemeSaveSuccesfully="<p style='color:green;'>Scheme Saved Successfully!</p>";
	public static String Schemedeleted="<p style='color:green;'> Scheme Deleted Sucessfully !!</p>";
	public static String Schemeupdate="<p style='color:green;'> Scheme Updated Sucessfully !!</p>";
		//##################################Scheme Related Messages End Here ###############################################################
	
	//##################################Location Related Messages Start Here #############################################################
	public static String LocationSaveSuccesfully="<p style='color:green;'>Location Saved Successfully!</p>";
	public static String UpdateLocSuccessfully="<p style='color:green;'>Location Updated Successfully!</p>";
	//##################################Location Related Messages End Here ###############################################################
	
	//################################## Supplier Related Messages  start here ###########################################################
		public static String SupplierSaveSuccesfully="Supplier details save successfully!";
		public static String UpdatesupplierSuccessfully="Supplier details Updated Successfully!";
		public static String errorSavingSupply="Save Un Successful.Please Retry!";
	//################################## SupplierRelated Messages  End Here ##############################################################
	
	//################################## Customer Related Messages  start here ###########################################################
			public static String CustomerSaveSuccesfully="Customer details save successfully!";
			public static String UpdatecustomerSuccessfully="Customer details Updated Successfully!";
	//################################## Customer Related Messages  End Here ##############################################################
		
	//##################################Stock Related Messages Start Here #############################################################
			public static String stockSaveSuccesfully="<p style='color:green;'>Stock details Saved Successfully!<br/><a href='sTockCall.action'>Add Another</a></p>";
			public static String UpdatestockSuccessfully="<p style='color:green;'>stock details Updated Successfully!</p>";
			public static String UpdatealltablesSuccessfully="<p style='color:green;'>Invoice details Updated Successfully!</p>";
			public static String excelExportSuccessforStockdetails="<p style='color:green;'>Excel Exported successfully !</p>";
			public static String displayError(String s)
			{
				String error="<p style='color:red;'>Please retry some technical problem occured!</p>"+"Error type is :"+ s ;
				return error;
			}
	//##################################Stock Related Messages End Here ###############################################################			
			
	//##################################Sales Related Messages Start Here #############################################################
			public static String salesSaveSuccesfully="<p style='color:green;'>Sales details Saved Successfully!</p>";
	//##################################Sales Related Messages End Here #############################################################
			
	//################################### G E N E R A L  E R R O R	 D I S P L A Y  Start here ############################################	
		public static String Error="<p style='color:red;'>Please retry some technical problem occured!</p>";
		public static String Duplicate="<p style='color:red;'>Already Saved!</p>";
		public static String inavlidLogin="<p style='color:red;'>Authentication failed . Password enterd found incorrect.!!</p>";
	
	//################################### G E N E R A L  E R R O R	 D I S P L A Y  end here ##############################################	

	public static synchronized String getCustomerSaveSuccesfully() {
		return CustomerSaveSuccesfully;
	}

	public static synchronized void setCustomerSaveSuccesfully(
	String customerSaveSuccesfully) {
		CustomerSaveSuccesfully = customerSaveSuccesfully;
	}
	
	//##################################Debit Note Related Messages start Here ###############################################################	
	
	public static String DebitSaveSuccesfully="<p style='color:green;'>Debit details Saved Successfully!</p>";
	
	//##################################Debit Note Related Messages end Here ###############################################################	
	
	//################################## Company details config start here  #######################################################################
	
	public static  String companyNewUserAndUpdateinfo;
	
	public static synchronized String getCompanyNewUserAndUpdateinfo() {
		return companyNewUserAndUpdateinfo;
	}

	public static synchronized void setCompanyNewUserAndUpdateinfo(
			String companyNewUserAndUpdateinfo) {
		Messages.companyNewUserAndUpdateinfo = companyNewUserAndUpdateinfo;
	}
	//################################### Company details config End here  ########################################################################

}
