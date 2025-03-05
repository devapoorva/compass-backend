package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.ID_NOT_FOUND;
import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.UploadsTemplateEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.queries.UploadsTemplateQueries;
import com.altice.salescommission.repository.UploadsTemplateRepository;
import com.altice.salescommission.service.UploadsTemplateService;

@Service
@Transactional
public class UploadsTemplateServiceImpl implements UploadsTemplateService, UploadsTemplateQueries {

	private static final Logger logger = LoggerFactory.getLogger(UploadsTemplateServiceImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UploadsTemplateRepository uploadsTemplateRepository;

	/* Getting all the templates */
	@Override
	public List<UploadsTemplateEntity> getTemplatesList() {

		List<UploadsTemplateEntity> myTemplatesList = jdbcTemplate.query(GET_TEMPLATES_LIST,
				new RowMapper<UploadsTemplateEntity>() {
					@Override
					public UploadsTemplateEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadsTemplateEntity myTemplatesModel = new UploadsTemplateEntity();
						myTemplatesModel.setId(rs.getLong("rowid"));
						myTemplatesModel.setTemplateid(rs.getInt("templateid"));
						myTemplatesModel.setRecordname(rs.getString("recordname"));
						myTemplatesModel.setRecordtemplate(rs.getString("recordtemplate"));
						myTemplatesModel.setRecorddesc(rs.getString("recorddesc"));

						return myTemplatesModel;
					}
				});
		return myTemplatesList;

	}

	@Override
	public List<UploadsTemplateEntity> getColumnsList(String recordName) {
		List<UploadsTemplateEntity> myColumnsList = jdbcTemplate.query(GET_COLUMNS_LIST,
				new RowMapper<UploadsTemplateEntity>() {
					@Override
					public UploadsTemplateEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadsTemplateEntity myColumnsModel = new UploadsTemplateEntity();
						myColumnsModel.setColumnname(rs.getString("column_name"));
						myColumnsModel.setColumntype(rs.getString("data_type"));
						myColumnsModel.setTemplateheader(rs.getString("column_name"));
						myColumnsModel.setStatus("Y");

						return myColumnsModel;
					}
				}, new Object[] { recordName });
		return myColumnsList;
	}

	@Override
	public List<UploadsTemplateEntity> getColumnsEditList(String recordName) {
		logger.info("recordName = " + recordName);
		List<UploadsTemplateEntity> myColumnsList = jdbcTemplate.query(GET_COLUMNS_EDIT_LIST,
				new RowMapper<UploadsTemplateEntity>() {
					@Override
					public UploadsTemplateEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadsTemplateEntity myColumnsModel = new UploadsTemplateEntity();
						myColumnsModel.setId(rs.getLong("rowid"));
						myColumnsModel.setRecordname(rs.getString("recordname"));
						myColumnsModel.setRecordtemplate(rs.getString("recordtemplate"));
						myColumnsModel.setRecorddesc(rs.getString("recorddesc"));
						myColumnsModel.setColumnname(rs.getString("columnname"));
						myColumnsModel.setColumntype(rs.getString("columntype"));
						myColumnsModel.setTemplateheader(rs.getString("templateheader"));
						myColumnsModel.setColumnorder(rs.getInt("columnorder"));
						myColumnsModel.setStatus(rs.getString("status"));

						return myColumnsModel;
					}
				}, new Object[] { recordName });
		return myColumnsList;
	}

	@Override
	public UploadsTemplateEntity insertTemplate(List<UploadsTemplateEntity> uploadsTemplateEntity)
			throws IdNotFoundException, DuplicateRecordException {
		UploadsTemplateEntity insertTemplateStatus = null;
		int maxid = uploadsTemplateRepository.getMaxId() + 1;

		this.logger.info("uploadsTemplateEntity = " + uploadsTemplateEntity);
		this.logger.info("name = " + uploadsTemplateEntity.get(0).getRecordname().toString());
		int cnt = uploadsTemplateRepository.getCount(uploadsTemplateEntity.get(0).getRecordname().toString());
		this.logger.info("cnt = " + cnt);

		if (cnt > 0) {
			throw new DuplicateRecordException(
					String.valueOf("Record " + uploadsTemplateEntity.get(0).getRecordname().toString())
							+ DUPLICATE_RECORD);
		}

		for (UploadsTemplateEntity uploadsTemplate : uploadsTemplateEntity) {
			UploadsTemplateEntity uploadObj = new UploadsTemplateEntity();
			uploadObj.setRecordname(uploadsTemplate.getRecordname());
			uploadObj.setRecordtemplate(uploadsTemplate.getRecordtemplate());
			uploadObj.setRecorddesc(uploadsTemplate.getRecorddesc());
			uploadObj.setColumnname(uploadsTemplate.getColumnname());
			uploadObj.setColumntype(uploadsTemplate.getColumntype());
			uploadObj.setTemplateheader(uploadsTemplate.getTemplateheader());
			uploadObj.setColumnorder(uploadsTemplate.getColumnorder());
			uploadObj.setStatus(uploadsTemplate.getStatus());
			uploadObj.setCreated_by(uploadsTemplate.getCreated_by());
			uploadObj.setCreated_dt(new Date());
			insertTemplateStatus = uploadsTemplateRepository.save(uploadObj);

			long generatedId = uploadObj.getId();

			UploadsTemplateEntity templateModel = uploadsTemplateRepository.findById(generatedId)
					.orElseThrow(() -> new IdNotFoundException(generatedId + ID_NOT_FOUND));

			templateModel.setTemplateid(maxid);
			uploadsTemplateRepository.save(templateModel);
		}

		return insertTemplateStatus;
	}

	@Override
	public UploadsTemplateEntity updateTemplate(List<UploadsTemplateEntity> uploadsTemplateEntity)
			throws IdNotFoundException {
		logger.info("uploadsTemplateEntity = " + uploadsTemplateEntity);

		UploadsTemplateEntity insertTemplateStatus = null;

		for (UploadsTemplateEntity uploadsTemplate : uploadsTemplateEntity) {
			logger.info("id = " + uploadsTemplate.getId());
			UploadsTemplateEntity uploadObj = uploadsTemplateRepository.findById(uploadsTemplate.getId())
					.orElseThrow(() -> new IdNotFoundException(uploadsTemplate.getId() + ID_NOT_FOUND));
			uploadObj.setRecordname(uploadsTemplate.getRecordname());
			uploadObj.setRecordtemplate(uploadsTemplate.getRecordtemplate());
			uploadObj.setRecorddesc(uploadsTemplate.getRecorddesc());
			uploadObj.setTemplateheader(uploadsTemplate.getTemplateheader());
			uploadObj.setColumnorder(uploadsTemplate.getColumnorder());
			uploadObj.setStatus(uploadsTemplate.getStatus());
			uploadObj.setUpdated_by(uploadsTemplate.getCreated_by());
			uploadObj.setUpdated_dt(new Date());
			insertTemplateStatus = uploadsTemplateRepository.save(uploadObj);

			logger.info("getStatus = " + uploadsTemplate.getStatus());
			logger.info("getRecordname = " + uploadsTemplate.getRecordname());
			logger.info("getColumnname = " + uploadsTemplate.getColumnname());

			jdbcTemplate.update("update c_upload_config set status='" + uploadsTemplate.getStatus() + "' "
					+ "where recordtemplate='" + uploadsTemplate.getRecordname() + "' and columnname='"
					+ uploadsTemplate.getColumnname() + "'");

		}

		return insertTemplateStatus;
	}

	@Override
	public LinkedHashSet<Map<String, String>> getRecordsList(String recordName) {
		LinkedHashSet<Map<String, String>> finalResultsetList = new LinkedHashSet<>();
		Map<String, String> mapObj = new LinkedHashMap<>();

		jdbcTemplate.query(GET_RECORDS_LIST, new RowMapper<UploadsTemplateEntity>() {
			@Override
			public UploadsTemplateEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				UploadsTemplateEntity myDocsModel = new UploadsTemplateEntity();

				mapObj.put(rs.getString("templateheader"), rs.getString("templateheader"));
				finalResultsetList.add(mapObj);

				return myDocsModel;
			}
		}, new Object[] { recordName });

		return finalResultsetList;
	}

	@Override
	public int deleteTemplate(int id, String recordname, String recordtemplate) throws IOException {
		int deleteCount = 0;
		String deleteQuery1 = "delete from c_upload_template where templateid=? and recordname=? and recordtemplate=?";
		deleteCount = jdbcTemplate.update(deleteQuery1, id, recordname, recordtemplate);

		String deleteQuery2 = "delete from c_upload_config where uploadtemplatename=? and recordtemplate=?";
		deleteCount = jdbcTemplate.update(deleteQuery2, recordname, recordtemplate);
		return deleteCount;
	}

}
