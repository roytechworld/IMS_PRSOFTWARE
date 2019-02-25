package com.prsoftwares.dao;

import java.util.List;

import com.prsoftwares.dto.LocationMasterDto;



public interface LocationMasterDao {
	public boolean addWareHouse(LocationMasterDto locDto) throws DaoException, Exception;
	public List<LocationMasterDto> getAllLocationsByCompanyId() throws DaoException, Exception;
	public boolean editLocationnDetailsById(LocationMasterDto locDto) throws DaoException, Exception;
	public LocationMasterDto getLocationByCompanyIdLocId(int locId) throws DaoException, Exception;
	public boolean checkLocationDuplicateORNOT(LocationMasterDto locDto)throws DaoException, Exception;
}
