package com.prsoftwares.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.prsoftwares.dao.ConnectCore;
import com.prsoftwares.dao.DaoException;
import com.prsoftwares.dao.SchemeMasterDao;
import com.prsoftwares.dto.CustomerMasterDTO;
import com.prsoftwares.dto.ItemCategoryTypeMasterDTO;
import com.prsoftwares.dto.SchemeMasterDto;
import com.prsoftwares.util.CurrentDate;



public class SchemeMasterDaoImpl implements SchemeMasterDao {

	@Override
	public boolean checkschdaoSchemeDuplicateORNOT(SchemeMasterDto schemeDto)
			throws DaoException, SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SchemeMasterDto checkDupScheme(String sName) throws DaoException,
			SQLException {
		SchemeMasterDto sdto = new SchemeMasterDto();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		String companyId = (String) session.getAttribute("companyId");
		companyId = companyId.trim();
		try {
			con = ConnectCore.getConnection();
			;
			String sql = "SELECT * FROM schememaster WHERE companyId=? AND schemeName LIKE '"
					+ sName + "'";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(companyId));
			System.out.println("ps=" + ps);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				while (rs.next()) {
					sdto.setSchemeName(rs.getString("schemeName"));
					sdto.setSchemeDoc(rs.getString("schemeDoc"));
					sdto.setSchemeSdate(rs.getString("schemeSdate"));
					sdto.setSchemeEdate(rs.getString("schemeEdate"));
					sdto.setSchemeQty(rs.getInt("schemeQty"));
				}
			} else {
				sdto = null;
			}
		} catch (Exception e) {
			System.out.println("ex= " + e);
		} finally {
			con.close();
		}

		return sdto;
	}

	public boolean deleteScheme(String scheme) throws DaoException,
			SQLException {
		Connection con = null;
		boolean rs = false;
		int i = 0;
		try {
			con = ConnectCore.getConnection();
			;
			String sql = "DELETE FROM schememaster WHERE schemeName=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, scheme);
			i = ps.executeUpdate();
			if (i > 0) {
				rs = true;
			}
		} catch (Exception e) {
			System.out.println("Excep= " + e);
		}
		return rs;
	}

	// #########################################################################################################################################################################
	// ########################################################### Scheme save and Edit code start here #################################################################################
	// #########################################################################################################################################################################
	@Override
	public Boolean saveAndEditScheme(SchemeMasterDto schemeDto,
			String[] itemIds, String[] customerIds, int itemSize,
			int customerSize) throws DaoException, SQLException {
		boolean result = false;
		Connection con = null;
		int x = 0;
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String uid = (String) session.getAttribute("userId");
			uid = uid.trim();
			String cid = (String) session.getAttribute("companyId");
			cid = cid.trim();
			con = ConnectCore.getConnection();
			if (schemeDto.getSchemeEditFlag() == 0) {
				for (int j = 0; j < customerIds.length; j++) {
					for (int i = 0; i < itemIds.length; i++) {
						String query = "insert into schememaster values(default,?,?,?,?,?,?,?,?,?)";
						PreparedStatement ps = con.prepareStatement(query);
						ps.setString(1, schemeDto.getSchemeName());
						ps.setDate(2, CurrentDate
								.mysqlDateConvertor(CurrentDate
										.getOnlyDateWithMySQLFORMAT()));
						ps.setDate(3, CurrentDate.mysqlDateConvertor(schemeDto
								.getSchemeSdate()));
						ps.setDate(4, CurrentDate.mysqlDateConvertor(schemeDto
								.getSchemeEdate()));
						ps.setInt(5, schemeDto.getSchemeQty());
						ps.setInt(6, Integer.parseInt(itemIds[i]));
						ps.setInt(7, Integer.parseInt(customerIds[j]));
						ps.setInt(8, Integer.parseInt(uid));
						ps.setInt(9, Integer.parseInt(cid));
						System.out.println("query= " + ps);
						x = ps.executeUpdate();
					}
				}
				if (x > 0) {
					result = true;
					System.out.println("save Successful");
				}
			} else {
				System.out.println("Scheme name found..."
						+ schemeDto.getOldSchemeName());
				int checkCustomerSize;
				int checkItemSize;
				List<ItemCategoryTypeMasterDTO> itemList1 = new ArrayList<ItemCategoryTypeMasterDTO>();
				itemList1 = getItemNamesBySchemeName(schemeDto
						.getOldSchemeName());
				checkItemSize = itemList1.size();
				System.out.println("Item size found :" + checkItemSize);
				List<CustomerMasterDTO> cdtoList = new ArrayList<CustomerMasterDTO>();
				cdtoList = getCustomersNamesBySchemeName(schemeDto
						.getOldSchemeName());
				checkCustomerSize = cdtoList.size();
				System.out.println("Customer size found :" + checkItemSize);
				if (checkItemSize == itemSize
						&& customerSize == checkCustomerSize) {
					int flag = 0;
					try {
						for (int s = 0; s < checkItemSize; s++) {
							String h = itemIds[s];
							String k = itemList1.get(s).getItemId();
							int j = Integer.parseInt(customerIds[s]);
							int d = Integer.parseInt(cdtoList.get(s)
									.getCustomerId());
							if (h.equalsIgnoreCase(k) && j == d) {
								System.out.println("Match found");
							} else {
								System.out.println("Not match");
								flag = 1;
							}
						}
					} catch (ArrayIndexOutOfBoundsException a) {
						flag = 1;
					}
					if (flag == 0) {
						String query = "UPDATE schememaster SET  schemeSdate=?,schemeEdate=?,schemeQty=?,schemeName=? WHERE schemeName=? AND companyId=?";
						PreparedStatement ps2 = con.prepareStatement(query);
						ps2.setDate(1, CurrentDate.mysqlDateConvertor(schemeDto
								.getSchemeSdate()));
						ps2.setDate(2, CurrentDate.mysqlDateConvertor(schemeDto
								.getSchemeEdate()));
						ps2.setInt(3, schemeDto.getSchemeQty());
						ps2.setString(4, schemeDto.getSchemeName());
						ps2.setString(5, schemeDto.getOldSchemeName());
						ps2.setString(6, cid);
						x = ps2.executeUpdate();
						if (x > 0) {
							result = true;
							System.out.println("Update Successful");
						}
					} else {
						boolean rsc = deleteSchemeBySchemeNameAndCid(
								schemeDto.getSchemeEdate(), cid, schemeDto,
								itemIds, customerIds, uid);
						if (rsc) {
							result = true;
						}
					}
				} else {
					boolean rsc = deleteSchemeBySchemeNameAndCid(
							schemeDto.getSchemeEdate(), cid, schemeDto,
							itemIds, customerIds, uid);
					if (rsc) {
						result = true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			con.close();
		}
		return result;
	}

	public boolean deleteSchemeBySchemeNameAndCid(String schemename,
			String cid, SchemeMasterDto schemeDto, String[] itemIds,
			String[] customerIds, String uid) throws DaoException, SQLException {
		boolean result = false;
		int x = 0;
		Connection con = null;
		try {
			con = ConnectCore.getConnection();
			String query2 = "DELETE FROM schememaster WHERE schemeName=? AND companyId=?";
			PreparedStatement ps3 = con.prepareStatement(query2);
			ps3.setString(1, schemeDto.getOldSchemeName());
			ps3.setString(2, cid);
			x = ps3.executeUpdate();
			if (x > 0) {
				System.out.println("Deleted sucessfully");
				for (int j = 0; j < customerIds.length; j++) {
					for (int i = 0; i < itemIds.length; i++) {
						String query = "insert into schememaster values(default,?,?,?,?,?,?,?,?,?)";
						PreparedStatement ps = con.prepareStatement(query);
						ps.setString(1, schemeDto.getSchemeName());
						ps.setDate(2, CurrentDate
								.mysqlDateConvertor(CurrentDate
										.getOnlyDateWithMySQLFORMAT()));
						ps.setDate(3, CurrentDate.mysqlDateConvertor(schemeDto
								.getSchemeSdate()));
						ps.setDate(4, CurrentDate.mysqlDateConvertor(schemeDto
								.getSchemeEdate()));
						ps.setInt(5, schemeDto.getSchemeQty());
						ps.setInt(6, Integer.parseInt(itemIds[i]));
						ps.setInt(7, Integer.parseInt(customerIds[j]));
						ps.setInt(8, Integer.parseInt(uid));
						ps.setInt(9, Integer.parseInt(cid));
						System.out.println("query= " + ps);
						x = ps.executeUpdate();
						result = true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			con.close();
		}
		return result;
	}

	// #########################################################################################################################################################################
	// ########################################################### Scheme save
	// and Edit code End here
	// #################################################################################
	// #########################################################################################################################################################################
	@Override
	public List<SchemeMasterDto> getAllSchemes() throws DaoException,
			SQLException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;

		String companyId = (String) session.getAttribute("companyId");
		companyId = companyId.trim();
		List<SchemeMasterDto> schList = new LinkedList<SchemeMasterDto>();
		try {
			con = ConnectCore.getConnection();
			;
			String sql = "SELECT DISTINCT  u.userName, s.schemeName, s.schemeDoc, s.schemeSdate, s.schemeEdate, s.schemeQty FROM schememaster s, usermaster u, itemmaster i WHERE u.userId=s.userID AND i.itemId=s.itemId AND s.companyId=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(companyId));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				SchemeMasterDto sdto = new SchemeMasterDto();
				sdto.setUserName(rs.getString("userName"));
				sdto.setSchemeName(rs.getString("schemeName"));
				sdto.setSchemeDoc(rs.getString("schemeDoc"));
				sdto.setSchemeSdate(rs.getString("schemeSdate"));
				sdto.setSchemeEdate(rs.getString("schemeEdate"));
				sdto.setSchemeQty(rs.getInt("schemeQty"));
				sdto.setOldSchemeName(rs.getString("schemeName"));

				schList.add(sdto);
			}
			System.out.println("size= " + schList.size());
		} catch (Exception e) {
			System.out.println("Excep= " + e);
		}
		return schList;
	}

	public List<SchemeMasterDto> getAllSchemesBYFROMAndTODATE(String fromDate,
			String toDate) throws DaoException, SQLException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;

		String companyId = (String) session.getAttribute("companyId");
		companyId = companyId.trim();
		List<SchemeMasterDto> schList = new LinkedList<SchemeMasterDto>();
		try {
			con = ConnectCore.getConnection();
			;
			String sql = "SELECT DISTINCT u.userName, s.schemeName, s.schemeDoc, s.schemeSdate, s.schemeEdate, s.schemeQty FROM schememaster s, usermaster u, itemmaster i WHERE u.userId=s.userID AND i.itemId=s.itemId AND s.companyId=? AND s.schemeSdate BETWEEN ? AND ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(companyId));
			ps.setDate(2, CurrentDate.mysqlDateConvertor(fromDate));
			ps.setDate(3, CurrentDate.mysqlDateConvertor(toDate));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				SchemeMasterDto sdto = new SchemeMasterDto();
				sdto.setUserName(rs.getString("userName"));
				sdto.setSchemeName(rs.getString("schemeName"));
				sdto.setSchemeDoc(rs.getString("schemeDoc"));
				sdto.setSchemeSdate(rs.getString("schemeSdate"));
				sdto.setSchemeEdate(rs.getString("schemeEdate"));
				sdto.setSchemeQty(rs.getInt("schemeQty"));
				System.out.println("username " + sdto.getUserName());
				System.out.println("sch name " + sdto.getSchemeName());
				System.out.println("item name  " + sdto.getItemName());
				schList.add(sdto);
			}
			System.out.println("size= " + schList.size());
		} catch (Exception e) {
			System.out.println("Excep= " + e);
		}
		return schList;
	}

	@Override
	public List<SchemeMasterDto> getSchemesByDate(SchemeMasterDto sdto)
			throws DaoException, SQLException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;

		String companyId = (String) session.getAttribute("companyId");
		companyId = companyId.trim();
		List<SchemeMasterDto> schList = new LinkedList<SchemeMasterDto>();
		try {
			con = ConnectCore.getConnection();
			;
			String sql = "SELECT u.userName, s.schemeName, s.schemeDoc, s.schemeSdate, s.schemeEdate, s.schemeQty, s.itemId, i.itemName FROM schememaster s, usermaster u, itemmaster i WHERE u.userId=s.userID AND i.itemId=s.itemId AND s.companyId=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(companyId));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				sdto.setUserName(rs.getString("userName"));
				sdto.setSchemeName(rs.getString("schemeName"));
				sdto.setSchemeDoc(rs.getString("schemeDoc"));
				sdto.setSchemeSdate(rs.getString("schemeSdate"));
				sdto.setSchemeEdate(rs.getString("schemeEdate"));
				sdto.setSchemeQty(rs.getInt("schemeQty"));
				sdto.setItemId(rs.getString("itemId"));
				sdto.setItemName(rs.getString("itemName"));
				System.out.println("username " + sdto.getUserName());
				System.out.println("sch name " + sdto.getSchemeName());
				System.out.println("item name  " + sdto.getItemName());
				schList.add(sdto);
			}
			System.out.println("size= " + schList.size());
		} catch (Exception e) {
			System.out.println("Excep= " + e);
		}
		return schList;
	}

	public List<ItemCategoryTypeMasterDTO> getItemNamesBySchemeName(
			String schemename) throws DaoException, SQLException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		String companyId = (String) session.getAttribute("companyId");
		companyId = companyId.trim();
		List<ItemCategoryTypeMasterDTO> schList = new LinkedList<ItemCategoryTypeMasterDTO>();
		try {
			con = ConnectCore.getConnection();
			;
			String sql = "SELECT distinct sc.itemId,i.itemname,i.itemMrp,i.itemUnit FROM schememaster sc JOIN itemmaster i ON sc.itemId=i.itemId WHERE schemeName=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, schemename);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ItemCategoryTypeMasterDTO sdto = new ItemCategoryTypeMasterDTO();
				sdto.setItemId(rs.getString("itemId"));
				sdto.setItemName(rs.getString("itemname"));
				sdto.setItemMrp(rs.getString("itemMrp"));
				sdto.setItemUnit(rs.getString("itemUnit"));

				schList.add(sdto);
			}
			System.out.println("size= " + schList.size());
		} catch (Exception e) {
			System.out.println("Excep= " + e);
		}
		return schList;
	}

	public List<ItemCategoryTypeMasterDTO> searchItemNamesByItemsName(
			String itemnames) throws DaoException, SQLException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		String companyId = (String) session.getAttribute("companyId");
		companyId = companyId.trim();
		List<ItemCategoryTypeMasterDTO> schList = new LinkedList<ItemCategoryTypeMasterDTO>();
		try {
			con = ConnectCore.getConnection();
			;
			String sql = "SELECT itemId,itemname FROM itemmaster WHERE itemname LIKE ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, "%" + itemnames + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ItemCategoryTypeMasterDTO sdto = new ItemCategoryTypeMasterDTO();
				sdto.setItemId(rs.getString("itemId"));
				sdto.setItemName(rs.getString("itemname"));

				schList.add(sdto);
			}
			System.out.println("size= " + schList.size());
		} catch (Exception e) {
			System.out.println("Excep= " + e);
		}
		return schList;
	}

	public List<CustomerMasterDTO> getCustomersNamesBySchemeName(
			String schemename) throws DaoException, SQLException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		String companyId = (String) session.getAttribute("companyId");
		companyId = companyId.trim();
		List<CustomerMasterDTO> cList = new LinkedList<CustomerMasterDTO>();
		try {
			con = ConnectCore.getConnection();
			;
			String sql = "SELECT DISTINCT sc.customerId,c.cName,c.phoneno,c.address,c.Remarks  FROM schememaster sc JOIN customermaster c ON sc.customerId=c.customerId WHERE sc.schemeName=?;";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, schemename);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CustomerMasterDTO cdto = new CustomerMasterDTO();
				cdto.setCustomerId(rs.getString("customerId"));
				cdto.setcName(rs.getString("cName"));
				cdto.setPhoneno(rs.getString("phoneno"));
				cdto.setAddress(rs.getString("address"));
				cdto.setRemarks(rs.getString("Remarks"));
				cList.add(cdto);
			}
			System.out.println("size= " + cList.size());
		} catch (Exception e) {
			System.out.println("Excep= " + e);
		}
		return cList;
	}

	public List<CustomerMasterDTO> searchCustomersByNames(String customernames)
			throws DaoException, SQLException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		String companyId = (String) session.getAttribute("companyId");
		companyId = companyId.trim();
		List<CustomerMasterDTO> cList = new LinkedList<CustomerMasterDTO>();
		try {
			con = ConnectCore.getConnection();
			;
			String sql = "SELECT customerId,cName FROM customermaster WHERE  cName LIKE ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, "%" + customernames + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CustomerMasterDTO cdto = new CustomerMasterDTO();
				cdto.setCustomerId(rs.getString("customerId"));
				cdto.setcName(rs.getString("cName"));
				cList.add(cdto);
			}
			System.out.println("size= " + cList.size());
		} catch (Exception e) {
			System.out.println("Excep= " + e);
		}
		return cList;
	}

	@Override
	public SchemeMasterDto getSchemeDetailsBySchemeName(String sName)
			throws DaoException, SQLException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;

		String companyId = (String) session.getAttribute("companyId");
		companyId = companyId.trim();
		SchemeMasterDto sdto = new SchemeMasterDto();
		try {
			con = ConnectCore.getConnection();
			;
			String sql = "SELECT DISTINCT  u.userName, s.schemeName, s.schemeDoc, s.schemeSdate, s.schemeEdate, s.schemeQty FROM schememaster s, usermaster u, itemmaster i WHERE u.userId=s.userID AND i.itemId=s.itemId AND s.schemeName = '"
					+ sName + "' AND s.companyId=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(companyId));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				sdto.setUserName(rs.getString("userName"));
				sdto.setSchemeName(rs.getString("schemeName"));
				sdto.setSchemeDoc(rs.getString("schemeDoc"));
				sdto.setSchemeSdate(rs.getString("schemeSdate"));
				sdto.setSchemeEdate(rs.getString("schemeEdate"));
				sdto.setSchemeQty(rs.getInt("schemeQty"));
				sdto.setOldSchemeName(rs.getString("schemeName"));

			}
			System.out.println("doc= " + sdto.getSchemeDoc());
		} catch (Exception e) {
			System.out.println("Excep= " + e);
		}
		return sdto;
	}

}
