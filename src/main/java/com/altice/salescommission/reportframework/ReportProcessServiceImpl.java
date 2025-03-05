package com.altice.salescommission.reportframework;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReportProcessServiceImpl implements ReportProcessService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ReportProcessRepository reportProcessRepository;

	@Override
	public List<ReportProcessModel> getProcessReport() {
		String sql = "select trp.run_time_id,crm.report_name,cri.report_instance_name,trp.status,trp.start_time,trp.end_time,trp.created_dt "
				+ "from t_report_process trp,c_report_instance cri,c_report_master crm "
				+ "where trp.report_instance_id =cri.report_instance_id and cri.report_id =crm.report_id";

		List<ReportProcessModel> processList = jdbcTemplate.query(sql, new RowMapper<ReportProcessModel>() {

			@Override
			public ReportProcessModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReportProcessModel reportProcessModel = new ReportProcessModel();
				reportProcessModel.setId(rs.getLong("run_time_id"));
				reportProcessModel.setReportName(rs.getString("report_name"));
				reportProcessModel.setInstanceName(rs.getString("report_instance_name"));
				reportProcessModel.setStatus(rs.getString("status"));
				reportProcessModel.setStartTime(rs.getString("start_time"));
				reportProcessModel.setEndTime(rs.getString("end_time"));
				reportProcessModel.setCreatedDt(rs.getDate("created_dt"));
				return reportProcessModel;
			}
		});
		return processList;
	}

	@Override
	public List<ReportProcessModel> getReportDetails() {
		String sql = "select trp.run_time_id ,crm.report_name,crm.report_desc ,cri.report_instance_name,"
				+ "cri.report_instance_id ,trp.status,trp.start_time,trp.end_time,trp.created_dt,"
				+ "crdl.id distributionid,crdl.dist_name,crdl.dist_type,crdl.dist_value "
				+ "from t_report_process trp,c_report_instance cri,c_report_master crm,c_report_dist_list crdl "
				+ "where trp.report_instance_id =cri.report_instance_id and cri.report_id =crm.report_id and cri.dist_list =crdl.id";

		List<ReportProcessModel> reportDetailsList = jdbcTemplate.query(sql, new RowMapper<ReportProcessModel>() {

			@Override
			public ReportProcessModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReportProcessModel reportProcessModel = new ReportProcessModel();
				reportProcessModel.setId(rs.getLong("run_time_id"));
				reportProcessModel.setReportName(rs.getString("report_name"));
				reportProcessModel.setReportDesc(rs.getString("report_desc"));
				reportProcessModel.setInstanceId(rs.getString("report_instance_id"));
				reportProcessModel.setInstanceName(rs.getString("report_instance_name"));
				reportProcessModel.setStatus(rs.getString("status"));
				reportProcessModel.setStartTime(rs.getString("start_time"));
				reportProcessModel.setEndTime(rs.getString("end_time"));
				reportProcessModel.setCreatedDt(rs.getDate("created_dt"));
				
				reportProcessModel.setDistId(rs.getString("distributionid"));
				reportProcessModel.setDistName(rs.getString("dist_name"));
				reportProcessModel.setDistType(rs.getString("dist_type"));
				reportProcessModel.setDistValue(rs.getString("dist_value"));
				return reportProcessModel;
			}
		});
		return reportDetailsList;
	}
}
