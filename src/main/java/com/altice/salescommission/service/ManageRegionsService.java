package com.altice.salescommission.service;

import java.io.IOException;
import java.util.List;

import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.model.CorpEntity;

public interface ManageRegionsService extends AbstractBaseService<CorpEntity, Long> {
	
	List<CorpEntity> getMajorAreas();
	
	List<CorpEntity> getActiveMajorAreas();
	
	List<CorpEntity> getActiveCorpsByMajorAreas();

	List<CorpEntity> getAllRegionsData();

	List<CorpEntity> getRegions(String majorarea);

	List<CorpEntity> getAreas(String majorarea, String region);

	List<CorpEntity> getCorps(String majorarea, String region, String area);

	List<CorpEntity> getAllAreas();

	List<CorpEntity> getAllRegions();

	CorpEntity addMajorArea(Long id, String corp, String majorarea, String region, String area, String currentUser)
			throws DuplicateRecordException;

	int updateMajorArea(String majorarea, String orgname, String currentUser) throws IOException;

	int updateRegion(String region, String orgname, String currentUser, String majorarea) throws IOException;

	int updateArea(String area, String orgname, String currentUser, String majorarea, String region) throws IOException;

	int updateCorps(String corp, String area, String status, String fieldstatus, String mainCorpName,
			String currentUser) throws IOException;

	List<CorpEntity> getMAreas(String searchVal);

}
