package com.altice.salescommission.utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.altice.salescommission.approvalworkflow.models.WorkflowDefinationModel;
import com.altice.salescommission.repository.WorkFlowMessageRepository;

@Component
public class CreateTicket {

	private static final Logger logger = LoggerFactory.getLogger(CreateTicket.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private WorkFlowMessageRepository workFlowMessageRepository;

	public String createTicketProcess(int flowid, String currentUser, int cnt, String employee_id, String sc_emp_id,
			String trackingId, String against_salesrepid, String loggedin_role) {

		logger.info("against_salesrepid = " + against_salesrepid);
		logger.info("sc_emp_id = " + sc_emp_id);
		logger.info("flowid = " + flowid);

		List<WorkflowDefinationModel> getLevels = jdbcTemplate.query(
				"select defination_id,level,stage,approval_type from c_workflow_defination p where flow_id =?",
				new RowMapper<WorkflowDefinationModel>() {
					@Override
					public WorkflowDefinationModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkflowDefinationModel levlelModel = new WorkflowDefinationModel();
						levlelModel.setId(rs.getLong("defination_id"));
						levlelModel.setLevel(rs.getInt("level"));
						levlelModel.setStage(rs.getInt("stage"));
						levlelModel.setApproval_type(rs.getString("approval_type"));
						return levlelModel;
					}
				}, new Object[] { flowid });

		Long id = 0L;
		int lev = 0;
		int stage = 0;
		int i = 0;
		String appval = "";

		logger.info("getLevels = " + getLevels);

		for (WorkflowDefinationModel getData : getLevels) {
			i++;
			// logger.info("i = " + i);
			String status = "initiated";
			if (i == 1) {
				status = "Pending";
			}

			id = getData.getId();
			lev = getData.getLevel();
			stage = getData.getStage();
			appval = getData.getApproval_type();

			logger.info("against_salesrepid = " + against_salesrepid);
			logger.info("employee_id = " + employee_id);
			logger.info("loggedin_role = " + loggedin_role);

			String supId = "";
			String supName = "NA";

			logger.info("appval = " + appval);

			if (appval.equals("By SalesRep Sup")) {
				logger.info("Inside By SalesRep Sup");

				if (loggedin_role.equals("Supervisor") || loggedin_role.equals("ADMIN")) {
					logger.info("Inside IF");
					employee_id = sc_emp_id.substring(0, sc_emp_id.indexOf("_"));
					loggedin_role = "Sales Representative";
					against_salesrepid = workFlowMessageRepository.getSalesRepID(employee_id, loggedin_role);
				}

				logger.info("employee_id = " + employee_id);
				logger.info("loggedin_role = " + loggedin_role);
				logger.info("against_salesrepid = " + against_salesrepid);

				supId = workFlowMessageRepository.getSupId(employee_id, loggedin_role);
				if (flowid == 4) {
					supName = "NA";
				} else {
					supName = workFlowMessageRepository.getSupName(employee_id, loggedin_role);
				}

				logger.info("supId = " + supId);
				logger.info("supName = " + supName);

			} else if (appval.equals("By SalesRep")) {
				logger.info("Inside By SalesRep");
				supId = employee_id;
				if (flowid == 4) {
					supName = "NA";
				} else {
					supName = workFlowMessageRepository.getEmpName(employee_id, loggedin_role);
				}

			} else {
				logger.info("Inside ADMIN");
				supId = "ADMIN";
				supName = "ADMIN";
			}

			// logger.info("supName = "+supName);
			if (supName.contains("'")) {
				// logger.info("Inside IF");
				supName = supName.replace("'", "");
			} else {
				// logger.info("Inside ELSE");
			}
			// logger.info("supName = "+supName);

			

			jdbcTemplate.update(
					"INSERT INTO t_workflow_details (flow_id,tracking_id,level,stage,approval_from,approval_name,pending_from_dt,"
							+ "created_dt,created_by,status,doc_id,sub_level,defination_id,final_status,submitted_by,against) "
							+ "VALUES ('" + flowid + "','" + trackingId + "','" + lev + "','" + stage + "','" + supId
							+ "','" + supName + "','" + new Date() + "','" + new Date() + "','" + currentUser + "','"
							+ status + "',0,0,'" + id + "','Pending','" + employee_id + "','" + against_salesrepid
							+ "')");
		}

		logger.info("trackingId = " + trackingId);

		return trackingId;
	}
}
