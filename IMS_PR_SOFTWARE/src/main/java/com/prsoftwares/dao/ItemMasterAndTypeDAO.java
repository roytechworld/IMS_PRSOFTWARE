package com.prsoftwares.dao;

import java.sql.SQLException;
import java.util.List;

import com.prsoftwares.dto.ItemCategoryTypeMasterDTO;



public interface ItemMasterAndTypeDAO {
	
	public boolean SaveItemMasterType(ItemCategoryTypeMasterDTO itemType) throws DaoException,SQLException;
	public List<ItemCategoryTypeMasterDTO> getAllItemType() throws DaoException,SQLException;
	public ItemCategoryTypeMasterDTO getItemTypeById(int id)throws DaoException,SQLException;
	public boolean editItemTypeById(ItemCategoryTypeMasterDTO itemType) throws DaoException,SQLException;
	
	public boolean getItemDetailsByItemNameBrandId(String iName, String bId) throws DaoException, SQLException;
	public boolean getItemDetailsByItemCode(String iCode) throws DaoException, SQLException;
	public boolean saveItemDetails(ItemCategoryTypeMasterDTO item) throws DaoException,SQLException;
	public List<ItemCategoryTypeMasterDTO> getAllItemByCompanyId() throws DaoException, SQLException;
	public ItemCategoryTypeMasterDTO getItemByCompanyIdItemId(String itemId) throws DaoException, SQLException;
	public String getLastItemCode()  throws DaoException, SQLException;
	public boolean editItemDetailsById(ItemCategoryTypeMasterDTO item) throws DaoException,SQLException;
	
	public boolean saveBrandDetails(ItemCategoryTypeMasterDTO brand) throws DaoException,SQLException;
	public List<ItemCategoryTypeMasterDTO> getAllBrandByCompanyId() throws DaoException, SQLException;
	public ItemCategoryTypeMasterDTO getItemBrandByBrandId(int ibId) throws DaoException, SQLException;
	public Boolean editBrandDetailsById(ItemCategoryTypeMasterDTO brand) throws DaoException, SQLException;
	
	public boolean saveSizeDetails(ItemCategoryTypeMasterDTO size) throws DaoException, SQLException;
	public List<ItemCategoryTypeMasterDTO> getAllSizeByCompanyId() throws DaoException, SQLException;
	public ItemCategoryTypeMasterDTO getItemSizeBySizeId(int sizeId) throws DaoException, SQLException;
	public Boolean editSizeDetailsById(ItemCategoryTypeMasterDTO size) throws DaoException, SQLException;
	boolean deleteSizeById(int sid) throws DaoException, SQLException;
	
	public boolean checkItemDuplicateORNOT(ItemCategoryTypeMasterDTO item) throws DaoException, SQLException;
	public boolean checkItemBrandDuplicateORNOT(ItemCategoryTypeMasterDTO brand) throws DaoException, SQLException;
	public boolean checkItemTypeDuplicateORNOT(ItemCategoryTypeMasterDTO iType) throws DaoException, SQLException;
	public boolean checkSizeDuplicateORNOT(ItemCategoryTypeMasterDTO idto) throws DaoException, SQLException;
	public int getSizeofTotalItems() throws DaoException, SQLException;
	List<ItemCategoryTypeMasterDTO> getAllItemByItemSize() throws DaoException, SQLException;
	
	//################################ Barcode related work start here ##########################################################
		public List<ItemCategoryTypeMasterDTO> getBarcodeList(String fromdate,String todate)throws DaoException, SQLException;
		//################################ Barcode related work End here ############################################################
		public boolean checkItemIDDuplicate(String id)throws DaoException, SQLException;	
	
	
	
}
