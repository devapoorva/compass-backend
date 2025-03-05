package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;
import static com.altice.salescommission.constants.ExceptionConstants.ID_NOT_FOUND;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.UploadConfigEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.queries.UploadConfigQueries;
import com.altice.salescommission.repository.UploadConfigRepository;
import com.altice.salescommission.service.UploadConfigService;

@Service
@Transactional
public class UploadConfigServiceImpl implements UploadConfigService, UploadConfigQueries {

	private static final Logger logger = LoggerFactory.getLogger(UploadConfigServiceImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate NamedJdbcTemplate;

	@Autowired
	private UploadConfigRepository uploadConfigRepository;

	@Override
	public List<UploadConfigEntity> getTemplatesList() {
		List<UploadConfigEntity> myTemplatesList = jdbcTemplate.query(GET_UPLOAD_TEMPLATES_LIST,
				new RowMapper<UploadConfigEntity>() {
					@Override
					public UploadConfigEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadConfigEntity myTemplatesModel = new UploadConfigEntity();
						myTemplatesModel.setId(rs.getLong("rowid"));
						myTemplatesModel.setTemplateid(rs.getInt("templateid"));
						myTemplatesModel.setUploadtemplatename(rs.getString("uploadtemplatename"));
						myTemplatesModel.setRecordtemplate(rs.getString("recordtemplate"));
						myTemplatesModel.setUploadtemplatedesc(rs.getString("uploadtemplatedesc"));
						myTemplatesModel.setActiontype(rs.getString("actiontype"));
						myTemplatesModel.setTransformationroutine(rs.getString("transformationroutine"));
						myTemplatesModel.setSqlquery(rs.getString("sqlquery"));

						return myTemplatesModel;
					}
				});
		return myTemplatesList;
	}

	@Override
	public List<UploadConfigEntity> getTemplatesDropdown() {
		List<UploadConfigEntity> myTemplatesList = jdbcTemplate.query(GET_TEMPLATES_LIST,
				new RowMapper<UploadConfigEntity>() {
					@Override
					public UploadConfigEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadConfigEntity myTemplatesModel = new UploadConfigEntity();
						myTemplatesModel.setUploadtemplatedesc(rs.getString("recorddesc"));
						myTemplatesModel.setUploadtemplatename(rs.getString("recordname"));
						myTemplatesModel.setRecordtemplate(rs.getString("recordtemplate"));

						return myTemplatesModel;
					}
				});
		return myTemplatesList;
	}

	@Override
	public List<UploadConfigEntity> getColumnsList(String recordName) {
		logger.info("recordName = " + recordName);
		List<UploadConfigEntity> myColumnsList = jdbcTemplate.query(GET_COLUMNS_LIST,
				new RowMapper<UploadConfigEntity>() {
					@Override
					public UploadConfigEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadConfigEntity myColumnsModel = new UploadConfigEntity();
						myColumnsModel.setColumnname(rs.getString("columnname"));
						myColumnsModel.setColumntype(rs.getString("columntype"));
						myColumnsModel.setStatus(rs.getString("status"));
						myColumnsModel.setColumnorder(rs.getInt("columnorder"));

						return myColumnsModel;
					}
				}, new Object[] { recordName });
		return myColumnsList;
	}

	@Override
	public List<UploadConfigEntity> getColumnsConfigList(String recordName, String name) {
		List<UploadConfigEntity> myColumnsList = jdbcTemplate.query(GET_COLUMNS_CONFIG_LIST,
				new RowMapper<UploadConfigEntity>() {
					@Override
					public UploadConfigEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadConfigEntity myColumnsModel = new UploadConfigEntity();
						myColumnsModel.setColumnname(rs.getString("columnname"));
						myColumnsModel.setColumntype(rs.getString("columntype"));
						myColumnsModel.setStatus(rs.getString("status"));
						myColumnsModel.setColumnorder(rs.getInt("columnorder"));

						return myColumnsModel;
					}
				}, new Object[] { recordName, name });
		return myColumnsList;
	}

	@Override
	public UploadConfigEntity insertTemplate(List<UploadConfigEntity> uploadsTemplateEntity)
			throws IdNotFoundException, DuplicateRecordException {

		UploadConfigEntity insertTemplateStatus = null;
		int maxid = uploadConfigRepository.getMaxId() + 1;

		int cnt = uploadConfigRepository.getCount(uploadsTemplateEntity.get(0).getUploadtemplatename().toString());

		if (cnt > 0) {
			throw new DuplicateRecordException(
					String.valueOf("Record " + uploadsTemplateEntity.get(0).getUploadtemplatename().toString())
							+ DUPLICATE_RECORD);
		}

		for (UploadConfigEntity uploadsTemplate : uploadsTemplateEntity) {
			UploadConfigEntity uploadObj = new UploadConfigEntity();
			uploadObj.setUploadtemplatename(uploadsTemplate.getUploadtemplatename());
			uploadObj.setRecordtemplate(uploadsTemplate.getRecordtemplate());
			uploadObj.setUploadtemplatedesc(uploadsTemplate.getUploadtemplatedesc());
			uploadObj.setTransformationroutine(uploadsTemplate.getTransformationroutine());
			uploadObj.setActiontype(uploadsTemplate.getActiontype());
			uploadObj.setColumnname(uploadsTemplate.getColumnname());
			uploadObj.setColumntype(uploadsTemplate.getColumntype());
			uploadObj.setStatus(uploadsTemplate.getStatus());
			uploadObj.setSqlquery(uploadsTemplate.getSqlquery());
			uploadObj.setColumnorder(uploadsTemplate.getColumnorder());
			uploadObj.setCreated_by(uploadsTemplate.getCreated_by());
			uploadObj.setCreated_dt(new Date());
			insertTemplateStatus = uploadConfigRepository.save(uploadObj);

			long generatedId = uploadObj.getId();

			UploadConfigEntity templateModel = uploadConfigRepository.findById(generatedId)
					.orElseThrow(() -> new IdNotFoundException(generatedId + ID_NOT_FOUND));

			templateModel.setTemplateid(maxid);
			uploadConfigRepository.save(templateModel);
		}

		return insertTemplateStatus;
	}

	@Override
	public UploadConfigEntity updateTemplate(List<UploadConfigEntity> uploadsTemplateEntity)
			throws IdNotFoundException {
		UploadConfigEntity insertTemplateStatus = null;

		for (UploadConfigEntity uploadsTemplate : uploadsTemplateEntity) {
			System.out.println("id = " + uploadsTemplate.getId());
			UploadConfigEntity uploadObj = uploadConfigRepository.findById(uploadsTemplate.getId())
					.orElseThrow(() -> new IdNotFoundException(uploadsTemplate.getId() + ID_NOT_FOUND));
			uploadObj.setUploadtemplatename(uploadsTemplate.getUploadtemplatename());
			uploadObj.setRecordtemplate(uploadsTemplate.getRecordtemplate());
			uploadObj.setUploadtemplatedesc(uploadsTemplate.getUploadtemplatedesc());
			uploadObj.setTransformationroutine(uploadsTemplate.getTransformationroutine());
			uploadObj.setActiontype(uploadsTemplate.getActiontype());
			uploadObj.setStatus(uploadsTemplate.getStatus());
			uploadObj.setSqlquery(uploadsTemplate.getSqlquery());
			uploadObj.setUpdated_by(uploadsTemplate.getCreated_by());
			uploadObj.setUpdated_dt(new Date());
			insertTemplateStatus = uploadConfigRepository.save(uploadObj);

		}

		return insertTemplateStatus;
	}

	@Override
	public LinkedHashSet<Map<String, String>> getRecordsList(String recordName) {
		LinkedHashSet<Map<String, String>> finalResultsetList = new LinkedHashSet<>();
		Map<String, String> mapObj = new LinkedHashMap<>();
		System.out.println("recordName = " + recordName);
		jdbcTemplate.query(GET_RECORDS_LIST, new RowMapper<UploadConfigEntity>() {
			@Override
			public UploadConfigEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				UploadConfigEntity myDocsModel = new UploadConfigEntity();

				mapObj.put(rs.getString("templateheader"), rs.getString("templateheader"));
				finalResultsetList.add(mapObj);

				return myDocsModel;
			}
		}, new Object[] { recordName });

		return finalResultsetList;
	}

	@Override
	public List<UploadConfigEntity> getColumnsEditList(String recordName) {
		logger.info("recordName = " + recordName);
		List<UploadConfigEntity> myColumnsList = jdbcTemplate.query(GET_COLUMNS_EDIT_LIST,
				new RowMapper<UploadConfigEntity>() {
					@Override
					public UploadConfigEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadConfigEntity myColumnsModel = new UploadConfigEntity();
						myColumnsModel.setId(rs.getLong("rowid"));
						myColumnsModel.setUploadtemplatename(rs.getString("uploadtemplatename"));
						myColumnsModel.setRecordtemplate(rs.getString("recordtemplate"));
						myColumnsModel.setUploadtemplatedesc(rs.getString("uploadtemplatedesc"));
						myColumnsModel.setActiontype(rs.getString("actiontype"));
						myColumnsModel.setTransformationroutine(rs.getString("transformationroutine"));
						myColumnsModel.setColumnname(rs.getString("columnname"));
						myColumnsModel.setColumntype(rs.getString("columntype"));
						myColumnsModel.setStatus(rs.getString("status"));
						myColumnsModel.setSqlquery(rs.getString("sqlquery"));
						myColumnsModel.setColumnorder(rs.getInt("columnorder"));

						return myColumnsModel;
					}
				}, new Object[] { recordName });
		return myColumnsList;
	}

	@Override
	public ResponseEntity<Map<String, Object>> uploadExcelData(List<List<Object>> excelData, String uploadtemplatename)
			throws ParseException {
		String sqlQuery = uploadConfigRepository.getSQLQuery(uploadtemplatename);
		List<String> dtype = uploadConfigRepository.getDtype(uploadtemplatename);
		logger.info("dtype = " + dtype);

		int rowCount = 0;
		int successCount = 0;
		int failureCount = 0;
		String failedRows = "";
		String finalFailedRows = "";
		String exceptionStr = "";

		logger.info("============================================================");
		logger.info("sqlQuery = " + sqlQuery);

		logger.info("uploadtemplatename = " + uploadtemplatename);
		logger.info("excelData = " + excelData);
		logger.info("excelData size = " + excelData.size());
		rowCount = excelData.size();

		for (List<Object> exData : excelData) {
			try {
				Map<String, Object> parms = new LinkedHashMap<>();
				failedRows = failedRows + exData + ",";
				for (int k = 0; k < exData.size(); k++) {
					logger.info("===================================");
					logger.info("dtype = " + dtype.get(k));
					if (dtype.get(k).equals("integer") || dtype.get(k).equals("bigint")) {
						logger.info("exData.get(k) = " + exData.get(k));
						if (exData.get(k) == null || exData.get(k) == "" || exData.get(k) == "0") {
							logger.info("Inside 1st IF ----------------");
							parms.put("param" + k, 0);
							logger.info("Inside 1st IF ----------------" + parms);
						} else {
							logger.info("Inside 1st ELSE ----------------");
							String val = exData.get(k).toString();
							parms.put("param" + k, Integer.parseInt(val));
							logger.info("Inside 1st ELSE =" + parms);
						}
						// failedRows = failedRows + exData.get(k) + ",";

					} else if (dtype.get(k).equals("date")) {
						 logger.info("ELSE IF date = " + exData.get(k));

						if (exData.get(k) == null || exData.get(k) == "") {
							// logger.info("Inside 2nd IF ----------------");
							parms.put("param" + k, null);
						} else {
							// logger.info("Inside 2nd ELSE ----------------");
							DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
							java.util.Date dutyDay = (java.util.Date) simpleDateFormat.parse(exData.get(k).toString());
							// logger.info("dutyDay = " + dutyDay);
							parms.put("param" + k, dutyDay);
						}
						// failedRows = failedRows + exData.get(k) + ",";

					} else if (dtype.get(k).contains("double") || dtype.get(k).equals("numeric")
							|| dtype.get(k).contains("real")) {
						 logger.info("ELSE IF double = " + exData.get(k));

						if (exData.get(k) == null || exData.get(k) == "") {
							// logger.info("Inside 3rd IF ----------------");
							parms.put("param" + k, null);
						} else {

							// logger.info("Inside 3rd ELSE ----------------");
							String val = exData.get(k).toString();

							// logger.info("val = " + val);

							parms.put("param" + k, Double.parseDouble(val));

						}
						// failedRows = failedRows + exData.get(k) + ",";

					} else {
					 logger.info("ELSE = " + exData.get(k));
						if (exData.get(k) == null || exData.get(k) == "") {
							// logger.info("Inside last IF ----------------");
							parms.put("param" + k, "");
						} else {
							// logger.info("Inside last ELSE ----------------");
							parms.put("param" + k, exData.get(k));
						}
						// failedRows = failedRows + exData.get(k) + ",";

					}

				} // for close

				logger.info("sqlQuery = " + sqlQuery);
				logger.info("params = " + parms);
				logger.info("params0 = " + parms.get("param0"));
				logger.info("params1 = " + parms.get("param1"));
				logger.info("params2 = " + parms.get("param2"));
				logger.info("params3 = " + parms.get("param3"));

				if (uploadtemplatename.equals("t_kpi_goal")) {
					// logger.info("Insdie IF = " + uploadtemplatename);

					String deleteQuery = "delete from t_kpi_goal where sc_emp_id=? and cal_run_id=? and kpi_id=? and comm_plan_id=?";
					int count_delete = jdbcTemplate.update(deleteQuery, parms.get("param0"), parms.get("param1"),
							parms.get("param2"), parms.get("param3"));

					// logger.info("count = " + count_delete);
					if (count_delete > 0) {
						// output_status = "Success";
					}
				} else if (uploadtemplatename.equals("t_kpi_score_override")) {
					// logger.info("Insdie IF = " + uploadtemplatename);

					String deleteQuery = "delete from t_kpi_score_override where sc_emp_id=? and cal_run_id=? and kpi_id=? and comm_plan_id=?";
					int count_delete = jdbcTemplate.update(deleteQuery, parms.get("param0"), parms.get("param1"),
							parms.get("param2"), parms.get("param3"));

					// logger.info("count = " + count_delete);
					if (count_delete > 0) {
						// output_status = "Success";
					}
				} else if (uploadtemplatename.equals("t_adjustment_detail")) {
					// logger.info("Insdie else IF = " + uploadtemplatename);

					String deleteQuery = "delete from t_adjustment_detail where sc_emp_id=? and cal_run_id=? and comm_plan_id=? and kpi_id=? ";
					int count_delete = jdbcTemplate.update(deleteQuery, parms.get("param0"), parms.get("param1"),
							parms.get("param2"), parms.get("param3"));

					// logger.info("count = " + count_delete);
					if (count_delete > 0) {
						// output_status = "Success";
					}
				}

				int count = NamedJdbcTemplate.update(sqlQuery, parms);
				// logger.info("count = " + count);
				if (count > 0) {
					failedRows = "";
					successCount++;
					// output_status = "Success";
				}
			} catch (Exception e) {
				System.out.println("Exception = " + e);
				System.out.println("Exception = " + e.getMessage());
				exceptionStr = e.getMessage();

				finalFailedRows = finalFailedRows + failedRows + " - " + exceptionStr
						+ System.getProperty("line.separator");
				System.out.println("finalFailedRows = " + finalFailedRows);

				failureCount++;
				failedRows = "";
			}

		} // main for close

		return ResponseEntity.ok(Map.of("rowCount", (rowCount), "successCount", successCount, "failureCount",
				failureCount, "failedRows", finalFailedRows));
	}

}
