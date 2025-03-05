package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.model.CorpEntity;
import com.altice.salescommission.queries.ManageRegionsQueries;
import com.altice.salescommission.repository.ManageRegionsRepository;
import com.altice.salescommission.service.ManageRegionsService;

@Service
@Transactional
public class ManageRegionsServiceImpl extends AbstractBaseRepositoryImpl<CorpEntity, Long>
		implements ManageRegionsService, ManageRegionsQueries {
	@Autowired
	private ManageRegionsRepository manageRegionsRepository;

	public ManageRegionsServiceImpl(ManageRegionsRepository manageRegionsRepository) {
		super(manageRegionsRepository);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/* This method is used to create new major area */
	@Override
	public CorpEntity addMajorArea(Long id, String corp, String majorarea, String region, String area,
			String currentUser) throws DuplicateRecordException {
		CorpEntity corpModel = new CorpEntity();
		Optional<CorpEntity> corpObj = manageRegionsRepository.findById(id);
		System.out.println("corpObj = " + corpObj);
		if (corpObj.isPresent()) {
			throw new DuplicateRecordException(id + DUPLICATE_RECORD);
		} else {
			int count = manageRegionsRepository.getExistingCorpCount(majorarea, region, area, corp);
			if (count > 0) {
				throw new DuplicateRecordException(corp + DUPLICATE_RECORD);
			} else {
				corpModel.setId(id);
				corpModel.setCorp(corp);
				corpModel.setMajorarea(majorarea);
				corpModel.setRegion(region);
				corpModel.setArea(area);
				corpModel.setStatus("Active");
				corpModel.setCreated_by(currentUser);
				corpModel.setCreated_dt(new Date());
				manageRegionsRepository.save(corpModel);
			}
		}
		return corpModel;
	}

	/* This method is used to get all major area's */
	@Override
	public List<CorpEntity> getAllRegionsData() {
		
		List<CorpEntity> regionsList = jdbcTemplate.query(GET_ALL_MANAGE_REGIONS_LIST, new RowMapper<CorpEntity>() {
			@Override
			public CorpEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CorpEntity corpModel = new CorpEntity();
				corpModel.setMajorarea(rs.getString("major_area"));
				corpModel.setRegion(rs.getString("region"));
				corpModel.setArea(rs.getString("area"));
				corpModel.setId(rs.getLong("corp_id"));
				corpModel.setCorp(rs.getString("corp"));
				corpModel.setStatus(rs.getString("status"));
				return corpModel;
			}
		});
		return regionsList;
	}
	
	/* This method is used to get all major areas */
	@Override
	public List<CorpEntity> getMajorAreas() {
		List<CorpEntity> majorAreasList = jdbcTemplate.query(GET_MAJOR_AREAS_LIST,
				(result, rowNum) -> new CorpEntity(result.getString("major_area")));
		return majorAreasList;
	}
	
	/* This method is used to get all active major areas */
	@Override
	public List<CorpEntity> getActiveMajorAreas() {
		List<CorpEntity> majorAreasList = jdbcTemplate.query(GET_ACTIVE_MAJOR_AREAS_LIST,
				(result, rowNum) -> new CorpEntity(result.getString("major_area")));
		return majorAreasList;
	}
	
	/* This method is used to get all corps by major area */
	@Override
	public List<CorpEntity> getActiveCorpsByMajorAreas() {
		List<CorpEntity> corpsList = jdbcTemplate.query(GET_ACTIVE_CORPS_LIST, new RowMapper<CorpEntity>() {
			@Override
			public CorpEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CorpEntity corpModel = new CorpEntity();
				corpModel.setId(rs.getLong("corp_id"));
				return corpModel;
			}
		});
		return corpsList;
	}

	/* This method is used to get all region's based on major area */
	@Override
	public List<CorpEntity> getRegions(String majorarea) {
		List<CorpEntity> regionsList = jdbcTemplate.query(GET_REGIONS_LIST, new RowMapper<CorpEntity>() {
			@Override
			public CorpEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CorpEntity corpModel = new CorpEntity();
				corpModel.setRegion(rs.getString("region"));
				return corpModel;
			}
		}, new Object[] { majorarea });
		return regionsList;

	}

	/* This method is used to get all area's based on region */
	@Override
	public List<CorpEntity> getAreas(String majorarea, String region) {

		List<CorpEntity> regionsList = jdbcTemplate.query(GET_AREAS_LIST, new RowMapper<CorpEntity>() {
			@Override
			public CorpEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CorpEntity corpModel = new CorpEntity();
				corpModel.setArea(rs.getString("area"));
				corpModel.setStatus(rs.getString("status"));
				return corpModel;
			}
		}, new Object[] { majorarea, region });

		return regionsList;

	}

	/* This method is used to get all corp's based on area */
	@Override
	public List<CorpEntity> getCorps(String majorarea, String region, String area) {

		List<CorpEntity> regionsList = jdbcTemplate.query(GET_CORPS_LIST, new RowMapper<CorpEntity>() {
			@Override
			public CorpEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CorpEntity corpModel = new CorpEntity();
				corpModel.setId(rs.getLong("corp_id"));
				corpModel.setCorp(rs.getString("corp"));
				corpModel.setStatus(rs.getString("status"));
				return corpModel;
			}
		}, new Object[] { majorarea, region, area });
		return regionsList;

	}

	/* This method is used to get all area's */
	@Override
	public List<CorpEntity> getAllAreas() {

		List<CorpEntity> allAreasList = jdbcTemplate.query(GET_ALL_AREAS_LIST, new RowMapper<CorpEntity>() {
			@Override
			public CorpEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CorpEntity corpModel = new CorpEntity();
				corpModel.setArea(rs.getString("area"));
				return corpModel;
			}
		});
		return allAreasList;
	}

	/* This method is used to get all region's */
	@Override
	public List<CorpEntity> getAllRegions() {

		List<CorpEntity> regionsList = jdbcTemplate.query(GET_ALL_REGIONS_LIST, new RowMapper<CorpEntity>() {
			@Override
			public CorpEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CorpEntity corpModel = new CorpEntity();
				corpModel.setRegion(rs.getString("region"));
				return corpModel;
			}
		});
		return regionsList;
	}

	/* Update corps */
	@Override
	public int updateCorps(String corp, String area, String status, String fieldstatus, String mainCorpName,
			String currentUser) throws IOException {
		System.out.println("status=" + status);
		int role = 0;
		if (fieldstatus.equals("true")) {
			String corpsplit[] = corp.split(",");
			for (int i = 0; i < corpsplit.length; i++) {
				role = jdbcTemplate.update(UPDATE_CORP_WITHOUT_NAME, area, status, new Date(), currentUser,
						corpsplit[i]);
			}

		} else {
			role = jdbcTemplate.update(UPDATE_CORP_WITH_NAME, corp, area, status, new Date(), currentUser,
					mainCorpName);
		}

		return role;
	}

	@Override
	public int updateMajorArea(String majorarea, String orgname, String currentUser) throws IOException {
		int role = jdbcTemplate.update(UPDATE_MAJOR_AREA, majorarea, new Date(), currentUser, orgname);
		return role;
	}

	@Override
	public int updateRegion(String region, String orgname, String currentUser, String majorarea) throws IOException {
		int role = jdbcTemplate.update(UPDATE_REGION, region, new Date(), currentUser, orgname, majorarea);
		return role;
	}

	@Override
	public int updateArea(String area, String orgname, String currentUser, String majorarea, String region)
			throws IOException {
		int role = jdbcTemplate.update(UPDATE_AREA, area, new Date(), currentUser, orgname, majorarea, region);
		return role;
	}
	
	/* This method is used to get all region's based on major area */
	@Override
	public List<CorpEntity> getMAreas(String searchVal) {
		List<CorpEntity> regionsList = jdbcTemplate.query(GET_MAREAS_LIST, new RowMapper<CorpEntity>() {
			@Override
			public CorpEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CorpEntity corpModel = new CorpEntity();
				corpModel.setMajorarea(rs.getString("major_area"));
				return corpModel;
			}
		}, new Object[] { "%"+searchVal+"%","%"+searchVal+"%","%"+searchVal+"%","%"+searchVal+"%" });
		return regionsList;

	}
}
