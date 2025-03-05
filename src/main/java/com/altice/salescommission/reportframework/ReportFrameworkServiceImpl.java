package com.altice.salescommission.reportframework;

import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;
import static com.altice.salescommission.constants.ExceptionConstants.ID_NOT_FOUND;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.serviceImpl.CommissionPlanServiceImpl;

@Service
@Transactional
public class ReportFrameworkServiceImpl implements ReportFrameworkService {

	private static final Logger logger = LoggerFactory.getLogger(ReportFrameworkServiceImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ReportFrameworkRepository reportFrameworkRepository;
	@Autowired
	private ReportInstanceReporsitory reportInstanceReporsitory;
	@Autowired
	private ReportDistributionRepository reportDistributionRepository;

	@Override
	public List<LinkedHashMap<String, String>> runReport(ReportFrameworkModel reportFrameworkModel) {
		List<LinkedHashMap<String, String>> finalResultsetList = new ArrayList<>();

		String queryString = "";
		try {

			String sqlquery = reportFrameworkModel.getReportSql();
			String parms = reportFrameworkModel.getSqlParams();

			logger.info(sqlquery);
			logger.info(parms);

			String p[] = parms.split(",");
			queryString = sqlquery;
			int z = 0;
			String one = sqlquery;
			for (int i = 0; i < sqlquery.length(); i++) {
				one = queryString;
				if ('~' == sqlquery.charAt(i)) {
					String parmValue = "";
					try {
						Integer.valueOf(p[z]);
						parmValue = p[z];
					} catch (NumberFormatException e) {
						// Not an Integer
						parmValue = "'" + p[z] + "'";
					}

					queryString = one.replaceFirst("~", parmValue);
					z++;
				}
			}

			jdbcTemplate.query(queryString,  new ColumnMapRowMapper() {

				@Override
				public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ReportFrameworkModel reportFrameworkModel = new ReportFrameworkModel();

					LinkedHashMap<String, String> mapObj = new LinkedHashMap<>();

					ResultSetMetaData rmd = rs.getMetaData();
					int columnsCount = rmd.getColumnCount();

					for (int k = 1; k <= columnsCount; k++) {
						// System.out.println("rmd.getColumnName(k) = "+rmd.getColumnName(k));
						mapObj.put(rmd.getColumnName(k), rs.getString(k));
					}
					finalResultsetList.add(mapObj);
					reportFrameworkModel.setFields(finalResultsetList);
					return null;
				}
			});
		} catch (Exception e) {
			// System.out.println("Exception 3 === " + e);
			LinkedHashMap<String, String> errorMapObj = new LinkedHashMap<>();
			errorMapObj.put("Error", e.getMessage());
			finalResultsetList.add(errorMapObj);
		}
		System.out.println("Resultset === " + finalResultsetList);
		return finalResultsetList;
	}

	@Override
	public ReportInstanceModel addNewReport(String sqlquery, String report_name, String report_desc, String category,
			String report_type, String currentUser, int distId, String instanceName, String sqlParams, String sqlLables)
			throws DuplicateRecordException {

		System.out.println("sqlParams = " + sqlParams);
		System.out.println("sqlLables = " + sqlLables);

		Optional<ReportFrameworkModel> repObj = Optional
				.ofNullable(reportFrameworkRepository.findByReportName(report_name));
		if (repObj.isPresent()) {
			throw new DuplicateRecordException(report_name + DUPLICATE_RECORD);
		} else {
			ReportFrameworkModel saveReport = null;
			ReportInstanceModel saveInstanceReport = null;

			ReportFrameworkModel reportFrameworkModel = new ReportFrameworkModel();
			reportFrameworkModel.setReportSql(sqlquery);
			reportFrameworkModel.setReportName(report_name);
			reportFrameworkModel.setReportDesc(report_desc);
			reportFrameworkModel.setCategory(category);
			reportFrameworkModel.setReportType(report_type);
			reportFrameworkModel.setStatus("Active");
			reportFrameworkModel.setCreatedBy(currentUser);
			reportFrameworkModel.setCreatedDt(new Date());
			saveReport = reportFrameworkRepository.save(reportFrameworkModel);

			long generatedId = reportFrameworkModel.getId();
			System.out.println("Generated Id = " + generatedId);
			if (saveReport != null) {
				ReportInstanceModel reportInstanceModel = new ReportInstanceModel();
				reportInstanceModel.setInstanceName(instanceName);
				reportInstanceModel.setSqlParams(sqlParams);
				reportInstanceModel.setSqlLabels(sqlLables);
				reportInstanceModel.setReportId(generatedId);
				reportInstanceModel.setStatus("Active");
				reportInstanceModel.setDistList(distId);
				reportInstanceModel.setCreatedBy(currentUser);
				reportInstanceModel.setCreatedDt(new Date());
				saveInstanceReport = reportInstanceReporsitory.save(reportInstanceModel);
			}
			return saveInstanceReport;
		}

	}

	@Override
	public ReportFrameworkModel updateReport(long id, String sqlquery, String report_name, String report_desc,
			String category, String report_type, String currentUser, String status)
			throws DuplicateRecordException, IdNotFoundException {

		ReportFrameworkModel reportFrameworkModel = reportFrameworkRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException(id + ID_NOT_FOUND));

		reportFrameworkModel.setReportSql(sqlquery);
		reportFrameworkModel.setReportName(report_name);
		reportFrameworkModel.setReportDesc(report_desc);
		reportFrameworkModel.setCategory(category);
		reportFrameworkModel.setReportType(report_type);
		reportFrameworkModel.setStatus(status);
		reportFrameworkModel.setUpdated_By(currentUser);
		reportFrameworkModel.setUpdated_Dt(new Date());
		reportFrameworkRepository.save(reportFrameworkModel);
		return reportFrameworkModel;
	}

	@Override
	public List<ReportFrameworkModel> getReports() {
		String sql = "select report_id ,report_name ,report_desc ,status, report_type, category,report_sql from c_report_master cr where status='Active'";

		List<ReportFrameworkModel> reportsList = jdbcTemplate.query(sql, new RowMapper<ReportFrameworkModel>() {

			@Override
			public ReportFrameworkModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReportFrameworkModel reportFrameworkModel = new ReportFrameworkModel();
				reportFrameworkModel.setId(rs.getLong("report_id"));
				reportFrameworkModel.setReportName(rs.getString("report_name"));
				reportFrameworkModel.setReportDesc(rs.getString("report_desc"));
				reportFrameworkModel.setStatus(rs.getString("status"));
				reportFrameworkModel.setReportType(rs.getString("report_type"));
				reportFrameworkModel.setCategory(rs.getString("category"));
				reportFrameworkModel.setReportSql(rs.getString("report_sql"));
				return reportFrameworkModel;
			}
		});
		return reportsList;
	}

	@Override
	public List<ReportFrameworkModel> getCategoryList() {
		String sql = "select distinct(category) category from c_report_master";

		List<ReportFrameworkModel> catList = jdbcTemplate.query(sql, new RowMapper<ReportFrameworkModel>() {

			@Override
			public ReportFrameworkModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReportFrameworkModel reportFrameworkModel = new ReportFrameworkModel();
				reportFrameworkModel.setCategory(rs.getString("category"));
				return reportFrameworkModel;
			}
		});
		return catList;
	}

	@Override
	public ReportInstanceModel updateSchedule(long id, String minute, String hour, String day, String month,
			String week, String currentUser) throws DuplicateRecordException, IdNotFoundException {

		String cronjob = minute + " " + hour + " " + day + " " + month + " " + week;

		ReportInstanceModel reportInstanceModel = reportInstanceReporsitory.findById(id)
				.orElseThrow(() -> new IdNotFoundException(id + ID_NOT_FOUND));

		reportInstanceModel.setScheduleStr(cronjob);
		reportInstanceModel.setUpdated_By(currentUser);
		reportInstanceModel.setUpdated_Dt(new Date());
		reportInstanceReporsitory.save(reportInstanceModel);
		return reportInstanceModel;
	}

	@Override
	public List<ReportDistributionModel> getDistributionDropdown() {
		String sql = "select distinct(dist_name) dist_name,dist_id from c_report_dist_list where status='Active'";

		List<ReportDistributionModel> distList = jdbcTemplate.query(sql, new RowMapper<ReportDistributionModel>() {

			@Override
			public ReportDistributionModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReportDistributionModel reportDistributionModel = new ReportDistributionModel();
				reportDistributionModel.setDistId(rs.getLong("dist_id"));
				reportDistributionModel.setDistName(rs.getString("dist_name"));
				return reportDistributionModel;
			}
		});
		return distList;
	}

	@Override
	public List<ReportDistributionModel> getDistributionList() {
		String sql = "select distinct(dist_name) dist_name,dist_type,dist_id from c_report_dist_list where status='Active'";

		List<ReportDistributionModel> distList = jdbcTemplate.query(sql, new RowMapper<ReportDistributionModel>() {

			@Override
			public ReportDistributionModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReportDistributionModel reportDistributionModel = new ReportDistributionModel();
				reportDistributionModel.setDistId(rs.getLong("dist_id"));
				reportDistributionModel.setDistName(rs.getString("dist_name"));
				reportDistributionModel.setDistType(rs.getString("dist_type"));
				return reportDistributionModel;
			}
		});
		return distList;
	}

	@Override
	public List<ReportDistributionModel> getEmployees(String filter) {
		System.out.println("Filter = " + filter);
		String sql = "select distinct(concat(first_name,' ',middle_name,' ',last_name,'_',employee_id)) empname,employee_id "
				+ "from c_employee_master cem where user_type=? order by empname";
		List<ReportDistributionModel> employeesList = jdbcTemplate.query(sql, new RowMapper<ReportDistributionModel>() {

			@Override
			public ReportDistributionModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReportDistributionModel reportDistributionModel = new ReportDistributionModel();
				reportDistributionModel.setEmpId(rs.getString("employee_id"));
				reportDistributionModel.setEmpName(rs.getString("empname"));
				return reportDistributionModel;
			}
		}, new Object[] { filter });
		return employeesList;
	}

	@Override
	public List<ReportDistributionModel> getRoles() {
		String sql = "select userrole_id, role_name from c_user_role_mgmt curm where role_status ='ACTIVE'";

		List<ReportDistributionModel> rolesList = jdbcTemplate.query(sql, new RowMapper<ReportDistributionModel>() {

			@Override
			public ReportDistributionModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReportDistributionModel reportDistributionModel = new ReportDistributionModel();
				reportDistributionModel.setUserId(rs.getInt("userrole_id"));
				reportDistributionModel.setRoleName(rs.getString("role_name"));
				return reportDistributionModel;
			}
		});
		return rolesList;
	}

	@Override
	public ReportDistributionModel addDistribution(String distName, String distValue, String currentUser,
			String distType) throws DuplicateRecordException {

		int count = reportDistributionRepository.getCountOfDistName(distName);
		if (count > 0) {
			throw new DuplicateRecordException(distName + DUPLICATE_RECORD);
		} else {
			String distVal[] = distValue.split(",");
			ReportDistributionModel reportDistModel = null;
			for (int i = 0; i < distVal.length; i++) {
				ReportDistributionModel reportDistributionModel = new ReportDistributionModel();
				reportDistributionModel.setDistName(distName);
				reportDistributionModel.setDistType(distType);
				reportDistributionModel.setDistValue(distVal[i]);
				reportDistributionModel.setCreatedBy(currentUser);
				reportDistributionModel.setCreatedDt(new Date());
				reportDistributionModel.setStatus("Active");
				reportDistModel = reportDistributionRepository.save(reportDistributionModel);
			}

			long distid = reportDistModel.getId();

			String updateQuery = "update c_report_dist_list set dist_id=? where dist_name=?";
			jdbcTemplate.update(updateQuery, distid, distName);

			return reportDistModel;
		}
	}

	@Override
	public ReportDistributionModel updateDistribution(int distId, String distName, String distValue, String currentUser,
			String distType) throws DuplicateRecordException, IdNotFoundException {

		ReportDistributionModel reportDistList = new ReportDistributionModel();

		String distTypeName = reportDistributionRepository.getTypeName(distId);

		if (!distTypeName.equals(distType)) {
			String deleteQuery = "delete from c_report_dist_list where dist_id=?";
			jdbcTemplate.update(deleteQuery, distId);
			reportDistList = addDistribution(distName, distValue, currentUser, distType);
		} else {
			String deleteQuery = "update c_report_dist_list set dist_name=?,updated_dt=?,updated_by=? where dist_id=?";
			jdbcTemplate.update(deleteQuery, distName, new Date(), currentUser, distId);

			String distValueStr[] = distValue.split(",");
			for (int i = 0; i < distValueStr.length; i++) {
				int cnt = reportDistributionRepository.getValueCount(distName, distType, distValueStr[i]);

				if (!distValue.equals("NA")) {
					if (cnt == 0) {
						ReportDistributionModel reportDistModel = new ReportDistributionModel();
						reportDistModel.setDistId(distId);
						reportDistModel.setDistName(distName);
						reportDistModel.setDistType(distType);
						reportDistModel.setDistValue(distValueStr[i]);
						reportDistModel.setCreatedBy(currentUser);
						reportDistModel.setCreatedDt(new Date());
						reportDistModel.setStatus("Active");
						reportDistList = reportDistributionRepository.save(reportDistModel);
					} else {
						throw new DuplicateRecordException("Role/Employee" + DUPLICATE_RECORD);
					}
				}
			}
		}
		return reportDistList;
	}

	@Override
	public ReportInstanceModel updateInstance(long id, String sqlParams, String instanceName, String scheduleStr,
			String status, String current_user, int distId, String sqlLabels)
			throws DuplicateRecordException, IdNotFoundException {
		ReportInstanceModel reportInstanceModel = reportInstanceReporsitory.findById(id)
				.orElseThrow(() -> new IdNotFoundException(id + ID_NOT_FOUND));

		reportInstanceModel.setSqlParams(sqlParams);
		reportInstanceModel.setSqlLabels(sqlLabels);
		reportInstanceModel.setInstanceName(instanceName);
		reportInstanceModel.setDistList(distId);
		reportInstanceModel.setScheduleStr(scheduleStr);
		reportInstanceModel.setStatus(status);
		reportInstanceModel.setUpdated_By(current_user);
		reportInstanceModel.setUpdated_Dt(new Date());
		reportInstanceReporsitory.save(reportInstanceModel);
		return reportInstanceModel;
	}

	@Override
	public ReportDistributionModel deleteDistribution(List<ReportDistributionModel> reportDistributionModel) {
		ReportDistributionModel updateStatus = null;
		int status = 0;
		for (ReportDistributionModel reportDistModel : reportDistributionModel) {
			System.out.println("reportDistModel.getDistId() = " + reportDistModel.getDistId());

			// int cnt =
			// reportDistributionRepository.getDistIdCount(reportDistModel.getDistId());
			// System.out.println("cnt==" + cnt);

			String updateQuery = "update c_report_dist_list set status='Inactive' where dist_id=?";
			status = jdbcTemplate.update(updateQuery, reportDistModel.getDistId());

		}

		return updateStatus;
	}

	@Override
	public ReportFrameworkModel deleteReport(List<ReportFrameworkModel> reportFrameworkModel) {
		ReportFrameworkModel updateStatus = null;
		for (ReportFrameworkModel repModel : reportFrameworkModel) {
			ReportFrameworkModel rModel = reportFrameworkRepository.findById(repModel.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Id not found" + repModel.getId()));
			rModel.setStatus("Inactive");
			updateStatus = reportFrameworkRepository.save(rModel);
		}
		return updateStatus;
	}

	@Override
	public ReportInstanceModel createInstance(long id, int distId, String sqlParams, String currentUser,
			String instanceName) throws DuplicateRecordException, IdNotFoundException {
		// ReportInstanceModel reportInstanceModel =
		// reportInstanceReporsitory.findByReportId(id)
		// .orElseThrow(() -> new IdNotFoundException(id + ID_NOT_FOUND));
		Optional<ReportInstanceModel> instanceObj = Optional
				.ofNullable(reportInstanceReporsitory.findByInstanceNameIgnoreCase(instanceName));
		if (instanceObj.isPresent()) {
			throw new DuplicateRecordException(instanceName + DUPLICATE_RECORD);
		} else {
			ReportInstanceModel instanceModel = new ReportInstanceModel();
			instanceModel.setReportId(id);
			instanceModel.setInstanceName(instanceName);
			instanceModel.setDistList(distId);
			instanceModel.setSqlParams(sqlParams);
			instanceModel.setStatus("Active");
			instanceModel.setCreatedDt(new Date());
			instanceModel.setCreatedBy(currentUser);
			reportInstanceReporsitory.save(instanceModel);
			return instanceModel;
		}
	}

	@Override
	public List<ReportInstanceModel> getInstances(int repid) {
		String sql = "select distinct(cri.report_instance_id) report_instance_id,cri.report_instance_name,cri.status,cri.sql_params,cri.sql_labels,coalesce(cri.report_schedule_str,'') report_schedule_str,crdl.dist_name,crdl.dist_id "
				+ "from c_report_instance cri,c_report_dist_list crdl "
				+ "where cri.dist_list=crdl.dist_id and cri.report_id =?";

		List<ReportInstanceModel> instancesList = jdbcTemplate.query(sql, new RowMapper<ReportInstanceModel>() {

			@Override
			public ReportInstanceModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReportInstanceModel reportInstanceModel = new ReportInstanceModel();
				reportInstanceModel.setId(rs.getLong("report_instance_id"));
				reportInstanceModel.setInstanceName(rs.getString("report_instance_name"));
				reportInstanceModel.setStatus(rs.getString("status"));
				reportInstanceModel.setSqlParams(rs.getString("sql_params"));
				reportInstanceModel.setSqlLabels(rs.getString("sql_labels"));
				reportInstanceModel.setScheduleStr(rs.getString("report_schedule_str"));
				reportInstanceModel.setDistName(rs.getString("dist_name"));
				reportInstanceModel.setDistId(rs.getInt("dist_id"));
				return reportInstanceModel;
			}
		}, new Object[] { repid });
		return instancesList;
	}

	@Override
	public int deleteInstance(long id) throws IOException {
		String deleteQuery = "delete from c_report_instance where report_instance_id=?";
		int status = jdbcTemplate.update(deleteQuery, id);
		return status;
	}

	@Override
	public int deleteValue(long id) throws IOException {
		String deleteQuery = "delete from c_report_dist_list where id=?";
		int status = jdbcTemplate.update(deleteQuery, id);
		return status;
	}

	@Override
	public List<ReportDistributionModel> getDistValues(int distId, String distType) {
		String sql = "";
		if (distType.equals("By User")) {
			sql = "select concat(cem.first_name,' ',cem.middle_name,' ',cem.last_name,'-',cem.employee_id,'-',cem.email_address) emp_name,crdl.id "
					+ "from c_report_dist_list crdl,c_employee_master cem "
					+ "where crdl.dist_value=cem.employee_id and crdl.dist_id =? and crdl.dist_type =? "
					+ "group by emp_name,crdl.id order by emp_name";
		} else {
			sql = "select curm.role_name emp_name,crdl.id " + "from c_report_dist_list crdl,c_user_role_mgmt curm "
					+ "where crdl.dist_value=curm.userrole_id::varchar and crdl.dist_id =? and crdl.dist_type =? "
					+ "group by emp_name,crdl.id order by emp_name";
		}

		List<ReportDistributionModel> distributionValuesList = jdbcTemplate.query(sql,
				new RowMapper<ReportDistributionModel>() {

					@Override
					public ReportDistributionModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						ReportDistributionModel reportDistributionModel = new ReportDistributionModel();
						reportDistributionModel.setId(rs.getLong("id"));
						reportDistributionModel.setEmpName(rs.getString("emp_name"));
						return reportDistributionModel;
					}
				}, new Object[] { distId, distType });
		return distributionValuesList;
	}

}
