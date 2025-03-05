package com.altice.salescommission.serviceImpl;

import java.util.Date;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.UserMangMappingEntity;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.repository.UserMangMappingRepository;
import com.altice.salescommission.service.UserMangMappingService;

@Service
@Transactional
public class UserMangMappingServiceImpl extends AbstractBaseRepositoryImpl<UserMangMappingEntity, Long>
		implements UserMangMappingService {

	private static final Logger logger = LoggerFactory.getLogger(UserMangMappingServiceImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserMangMappingRepository userMangMappingRepository;

	public UserMangMappingServiceImpl(UserMangMappingRepository userMangMappingRepository) {
		super(userMangMappingRepository);
	}

	/* This method is used to assign pages to role */
	@Override
	public UserMangMappingEntity assignPageToRoleViews(int roleid, int pageid, String status, String currentUser,
			int parentpageid) {
		int cnt = 0;
		UserMangMappingEntity updateKpiStatusRes = null;

		cnt = userMangMappingRepository.getCountOfMappedRecord(roleid, pageid);

		logger.info("==================View======================");
		logger.info("roleid = " + roleid);
		logger.info("pageid = " + pageid);
		logger.info("parentpageid = " + parentpageid);
		logger.info("cnt = " + cnt);
		logger.info("currentUser = " + currentUser);

		//Long maxid = Long.valueOf(userMangMappingRepository.getMaxId() + 1);

		if (cnt == 0) {
			logger.info("Inside if");

			jdbcTemplate.update(
					"INSERT INTO c_user_role_page_nav_map_mgmt (userroleid,page_id,created_dt,created_by,status,view_acc,parent_page_id) "
							+ "VALUES ('" + roleid + "','" + pageid + "','" + new Date() + "','"
							+ currentUser + "','ACTIVE','" + status + "','" + parentpageid + "')");

		} else {
			logger.info("Inside else");

			Long mapid = Long.valueOf(userMangMappingRepository.getId(roleid, pageid));
			logger.info("Mpping Rowid = " + mapid);
			Long mapparentid = Long.valueOf(userMangMappingRepository.getParentId(roleid, parentpageid, parentpageid));
			logger.info("Mpping Parent Rowid = " + mapparentid);

			UserMangMappingEntity mapStatus = userMangMappingRepository.findById(mapid)
					.orElseThrow(() -> new ResourceNotFoundException("Id not found" + mapid));

			mapStatus.setView_acc(status);
			mapStatus.setParent_page_id(parentpageid);
			mapStatus.setUpdated_by(currentUser);
			mapStatus.setUpdated_dt(new Date());
			updateKpiStatusRes = userMangMappingRepository.save(mapStatus);

			int parentcnt = userMangMappingRepository.getCountOfViewEditAccess(roleid, parentpageid, parentpageid);
			logger.info("parentpageid : " + parentpageid);
			logger.info("parentcnt = " + parentcnt);

			UserMangMappingEntity updateStatus = userMangMappingRepository.findById(Long.valueOf(mapparentid))
					.orElseThrow(() -> new ResourceNotFoundException("Id not found" + mapparentid));

			if (parentcnt == 0) {
				logger.info("inside parentcnt IF");
				updateStatus.setView_acc("n");
				updateStatus.setEdit_acc("n");
			} else {
				logger.info("inside parentcnt ELSE");
				updateStatus.setView_acc("y");
				updateStatus.setEdit_acc("y");
			}
			updateKpiStatusRes = userMangMappingRepository.save(updateStatus);
			logger.info("updateKpiStatusRes = " + updateKpiStatusRes);
		}

		return updateKpiStatusRes;
	}

	/* This method is used to assign pages to role */
	@Override
	public UserMangMappingEntity assignPageToRoleEdits(int roleid, int pageid, String status, String currentUser,
			int parentpageid) {
		int cnt = 0;
		UserMangMappingEntity updateKpiStatusRes = null;

		cnt = userMangMappingRepository.getCountOfMappedRecord(roleid, pageid);

		logger.info("==================Edit======================");
		logger.info("roleid = " + roleid);
		logger.info("pageid = " + pageid);
		logger.info("parentpageid = " + parentpageid);
		logger.info("cnt = " + cnt);
		logger.info("currentUser = " + currentUser);

		//Long maxid = Long.valueOf(userMangMappingRepository.getMaxId());

		logger.info("currentUser = " + currentUser);

		if (cnt == 0) {

			jdbcTemplate.update(
					"INSERT INTO c_user_role_page_nav_map_mgmt (userroleid,page_id,created_dt,created_by,status,edit_acc,parentpageid) "
							+ "VALUES ('" + roleid + "','" + pageid + "','" + new Date() + "','"
							+ currentUser + "','ACTIVE','" + status + "','" + parentpageid + "')");
		} else {
			Long mapid = Long.valueOf(userMangMappingRepository.getId(roleid, pageid));
			Long mapparentid = Long.valueOf(userMangMappingRepository.getParentId(roleid, parentpageid, parentpageid));
			logger.info("Mapping Parent Rowid = " + mapparentid);

			UserMangMappingEntity mapStatus = userMangMappingRepository.findById(mapid)
					.orElseThrow(() -> new ResourceNotFoundException("Id not found" + mapid));
			mapStatus.setEdit_acc(status);
			mapStatus.setParent_page_id(parentpageid);
			mapStatus.setUpdated_by(currentUser);
			mapStatus.setUpdated_dt(new Date());
			updateKpiStatusRes = userMangMappingRepository.save(mapStatus);

			int parentcnt = userMangMappingRepository.getCountOfViewEditAccess(roleid, parentpageid, parentpageid);

			UserMangMappingEntity updateStatus = userMangMappingRepository.findById(Long.valueOf(mapparentid))
					.orElseThrow(() -> new ResourceNotFoundException("Id not found" + mapparentid));
			if (parentcnt == 0) {
				updateStatus.setView_acc("n");
				updateStatus.setEdit_acc("n");
			} else {
				updateStatus.setView_acc("y");
				updateStatus.setEdit_acc("y");
			}
			updateKpiStatusRes = userMangMappingRepository.save(updateStatus);
		}

		return updateKpiStatusRes;
	}

}
