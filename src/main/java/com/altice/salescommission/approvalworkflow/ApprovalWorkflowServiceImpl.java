package com.altice.salescommission.approvalworkflow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.approvalworkflow.models.TicketWorkflowDetailsModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowApprovalModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowCriteriaModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowDefinationModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowMasterModel;
import com.altice.salescommission.approvalworkflow.queries.WorkFlowQueries;
import com.altice.salescommission.approvalworkflow.repository.WorkflowApprovalRepository;
import com.altice.salescommission.approvalworkflow.repository.WorkflowCriteriaRepository;
import com.altice.salescommission.approvalworkflow.repository.WorkflowDefinationRepository;
import com.altice.salescommission.approvalworkflow.repository.WorkflowMasterRepository;
import com.altice.salescommission.approvalworkflow.repository.WorkflowTicketRepository;
import com.altice.salescommission.entity.EmployeeMasterEntity;
import com.altice.salescommission.entity.KpiMasterEntity;
import com.altice.salescommission.entity.WorkflowMessageEntity;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.repository.PersonalInfoRepository;

@Service
public class ApprovalWorkflowServiceImpl implements ApprovalWorkflowService, WorkFlowQueries {
	private static final Logger logger = LoggerFactory.getLogger(ApprovalWorkflowServiceImpl.class.getName());

	@Autowired
	private WorkflowMasterRepository workflowMasterRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private WorkflowDefinationRepository workflowDefinationRepository;

	@Autowired
	private WorkflowApprovalRepository workflowApprovalRepository;

	@Autowired
	private WorkflowCriteriaRepository workflowCriteriaRepository;

	@Autowired
	private PersonalInfoRepository personalInfoRepository;

	@Autowired
	private WorkflowTicketRepository workflowTicketRepository;

	@Override
	public TicketWorkflowDetailsModel createTicket(TicketWorkflowDetailsModel ticketWorkflowDetailsModels) {

		List<String> getLevels = workflowTicketRepository.getLevels(ticketWorkflowDetailsModels.getFlow_id());

		String lev = "";
		String stage = "";
		String appval = "";
		for (String getData : getLevels) {
			lev = getData.split(",")[0];
			stage = getData.split(",")[1];
			appval = getData.split(",")[2];
		}

		TicketWorkflowDetailsModel ticketWorkflowModel = null;

		for (int i = 0; i < 2; i++) {
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
			String trackingId = ft.format(dNow);

			TicketWorkflowDetailsModel ticketsModel = new TicketWorkflowDetailsModel();
			ticketsModel.setTrackingId(trackingId);
			ticketsModel.setFlow_id(ticketWorkflowDetailsModels.getFlow_id());
			ticketsModel.setCreated_by(ticketWorkflowDetailsModels.getCreated_by());
			ticketsModel.setCreated_dt(new Date());
			ticketsModel.setPending_from_dt(new Date());
			ticketsModel.setStatus("Pending");
			ticketsModel.setLevel(Integer.parseInt(lev));
			ticketsModel.setStage(Integer.parseInt(stage));
			ticketsModel.setApproval_from(appval);
			ticketWorkflowModel = workflowTicketRepository.save(ticketsModel);
			System.out.println("ticketWorkflowModel = " + ticketWorkflowModel);
		}

		return ticketWorkflowModel;
	}

	@Override
	public List<WorkflowMasterModel> getWorkflowNames() {
		List<WorkflowMasterModel> workflowNamesList = jdbcTemplate.query(GET_WORKFLOWS,
				new RowMapper<WorkflowMasterModel>() {
					@Override
					public WorkflowMasterModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkflowMasterModel workflowNamesObj = new WorkflowMasterModel();
						workflowNamesObj.setId(rs.getLong("flow_id"));
						workflowNamesObj.setWorkflow_name(rs.getString("workflow_name"));
						workflowNamesObj.setActive_flag(rs.getString("active_flag"));
						workflowNamesObj.setCdt(rs.getString("created_dt"));
						workflowNamesObj.setCreated_by(rs.getString("created_by"));
						workflowNamesObj.setUdt(rs.getString("update_dt"));
						workflowNamesObj.setUpdated_by(rs.getString("updated_by"));
						workflowNamesObj.setEffdt(rs.getString("effective_date"));
						return workflowNamesObj;
					}
				});
		return workflowNamesList;
		// return workflowMasterRepository.findAll();
	}

	@Override
	public List<WorkflowDefinationModel> getWorkflowDefValues() {
		// TODO Auto-generated method stub

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_WORKFLOWDEFFIELDS);
		List<WorkflowDefinationModel> workFlowDefs = new ArrayList<WorkflowDefinationModel>();
		for (Map row : rows) {
			WorkflowDefinationModel workFlowDef = new WorkflowDefinationModel();
			String fieldName = (String) row.get("field_name");
			if (fieldName.equals("Approval Type")) {
				workFlowDef.setApproval_type((String) row.get("description"));
				workFlowDefs.add(workFlowDef);
			} else if (fieldName.equals("Timeout Action")) {
				workFlowDef.setTimeout_action((String) row.get("description"));
				workFlowDefs.add(workFlowDef);
			} else if (fieldName.equals("Approval Criteria")) {
				workFlowDef.setApproval_criteria((String) row.get("description"));
				workFlowDefs.add(workFlowDef);
			}

		}
		return workFlowDefs;
	}

	@Override
	public WorkflowDefinationModel createWorkFlowDef(WorkflowDefinationModel workFLowDefinationModel) {
		workflowDefinationRepository.save(workFLowDefinationModel);
		return workFLowDefinationModel;
	}

	@Override
	public WorkflowMasterModel addFlow(WorkflowMasterModel workflowMasterModel) {
		workflowMasterRepository.save(workflowMasterModel);
		return workflowMasterModel;
	}

	@Override
	public List<WorkflowDefinationModel> findByFlowId(int flowid) {
		List<WorkflowDefinationModel> levelList = jdbcTemplate.query(GET_LEVELS,
				new RowMapper<WorkflowDefinationModel>() {
					@Override
					public WorkflowDefinationModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkflowDefinationModel levlelModel = new WorkflowDefinationModel();
						levlelModel.setLevel(rs.getInt("level"));
						levlelModel.setFlowId(flowid);
						return levlelModel;
					}
				}, new Object[] { flowid });
		return levelList;
	}

	@Override
	public List<WorkflowDefinationModel> findStages(int flowid, int level) {
		List<WorkflowDefinationModel> stageList = jdbcTemplate.query(GET_STAGES,
				new RowMapper<WorkflowDefinationModel>() {

					@Override
					public WorkflowDefinationModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkflowDefinationModel stageModel = new WorkflowDefinationModel();
						stageModel.setId(rs.getLong("defination_id"));
						stageModel.setStage(rs.getInt("stage"));
						stageModel.setApproval_type(rs.getString("approval_type"));
						stageModel.setApproval_value(rs.getString("approval_value"));
						stageModel.setApproval_criteria(rs.getString("approval_criteria"));
						stageModel.setTimeout_action(rs.getString("timeout_action"));
						stageModel.setTimeout(rs.getInt("timeout"));
						stageModel.setFlowId(flowid);
						stageModel.setLevel(level);
						return stageModel;
					}
				}, new Object[] { flowid, level });
		return stageList;
	}

	@Override
	public List<WorkflowDefinationModel> findDefinition(int flowid, int level, int stage) {
		List<WorkflowDefinationModel> stageList = jdbcTemplate.query(GET_DEFINITIONS,
				new RowMapper<WorkflowDefinationModel>() {

					@Override
					public WorkflowDefinationModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkflowDefinationModel workFlowModel = new WorkflowDefinationModel();
						workFlowModel.setApproval_type(rs.getString("approval_type"));
						workFlowModel.setApproval_value(rs.getString("approval_value"));
						workFlowModel.setApproval_criteria(rs.getString("approval_criteria"));
						workFlowModel.setTimeout_action(rs.getString("timeout_action"));
						workFlowModel.setTimeout(rs.getInt("timeout"));
						workFlowModel.setFlowId(flowid);
						workFlowModel.setLevel(level);
						return workFlowModel;
					}
				}, new Object[] { flowid, level, stage });
		return stageList;
	}

	@Override
	public WorkflowApprovalModel addApprovalWorkFlow(List<WorkflowApprovalModel> workflowApprovalModels) {
		// TODO Auto-generated method stub
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
		String datetime = ft.format(dNow);

		for (WorkflowApprovalModel approvalFlow : workflowApprovalModels) {
			approvalFlow.setApprovalId(datetime);
			workflowApprovalRepository.save(approvalFlow);
		}
		return workflowApprovalModels.get(0);
	}

	@Override
	public List<WorkflowApprovalModel> getApprovalWorkFlows() {

		List<WorkflowApprovalModel> approvalList = new ArrayList<WorkflowApprovalModel>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_APPROVALS);

		for (Map row : rows) {
			WorkflowApprovalModel approvalModel = new WorkflowApprovalModel();

			approvalModel.setName((String) row.get("name"));
			approvalModel.setDescription((String) row.get("description"));
			approvalModel.setApprovalId((String) row.get("approval_id"));
			approvalList.add(approvalModel);
		}
		return approvalList;
		// return workflowApprovalRepository.findAll(sort);
	}

	@Override
	public List<WorkflowApprovalModel> findByApprovalId(String approvalId) {
		List<WorkflowApprovalModel> approvalList = jdbcTemplate.query(GET_APPROVALS_BY_APROVALID,
				new RowMapper<WorkflowApprovalModel>() {

					@Override
					public WorkflowApprovalModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkflowApprovalModel approvalModel = new WorkflowApprovalModel();
						approvalModel.setName(rs.getString("name"));
						approvalModel.setDescription(rs.getString("description"));
						approvalModel.setEmployee_id(rs.getString("employee_id"));
						approvalModel.setId(rs.getLong("id_approv"));
						approvalModel.setApprovalId(rs.getString("approval_id"));
						return approvalModel;
					}
				}, new Object[] { approvalId });
		return approvalList;
	}

	@Override
	public WorkflowApprovalModel updateApprovalList(List<WorkflowApprovalModel> approvalModels) {
		// TODO Auto-generated method stub
		WorkflowApprovalModel approvalWorkFlow = null;
		for (WorkflowApprovalModel approvalModel : approvalModels) {
			if (approvalModel.getId() != null) {

				WorkflowApprovalModel workFlowApproval = workflowApprovalRepository.findById(approvalModel.getId())
						.orElseThrow(() -> new ResourceNotFoundException(
								"Workflow approval not found" + approvalModel.getId()));
				workFlowApproval.setName(approvalModel.getName());
				workFlowApproval.setDescription(approvalModel.getDescription());
				workFlowApproval.setEmployee_id(approvalModel.getEmployee_id());
				approvalWorkFlow = workflowApprovalRepository.save(workFlowApproval);
			}

			else {
				WorkflowApprovalModel workFlowApproval = new WorkflowApprovalModel();
				workFlowApproval.setName(approvalModel.getName());
				workFlowApproval.setDescription(approvalModel.getDescription());
				workFlowApproval.setEmployee_id(approvalModel.getEmployee_id());
				workFlowApproval.setApprovalId(approvalModel.getApprovalId());
				workFlowApproval.setCreated_by(approvalModel.getCreated_by());
				workFlowApproval.setCreated_dt(approvalModel.getCreated_dt());
				workflowApprovalRepository.save(workFlowApproval);

			}
		}
		return approvalWorkFlow;
	}

	@Override
	public Map<String, Boolean> deleteApproval(int id) {
		// TODO Auto-generated method stub
		Map<String, Boolean> response = new HashMap<>();
		try {
			workflowApprovalRepository.deleteById((long) id);
			response.put("deleted", Boolean.TRUE);
		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public Map<String, Boolean> removeChargeback(int id, String status) {
		logger.info(status);
		logger.info(String.valueOf(id));
		Map<String, Boolean> response = new HashMap<>();
		try {
			String deleteQuery = "";
			if (status.equals("F")) {
				deleteQuery = "update fd_kom_feed set pr_status_id=? where kom_feed_id =?";
			} else if (status.equals("FM")) {
				logger.info("Inside FM");
			} else if (status.equals("M")) {
				deleteQuery = "update t_mob_sales_data set pr_status_id=? where mob_feed_id =?";
			}
			jdbcTemplate.update(deleteQuery, 0, id);
			response.put("deleted", Boolean.TRUE);
		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public Map<String, Boolean> deleteApprovalsByAprovalId(String approvalId) {
		Map<String, Boolean> response = new HashMap<>();
		try {
			String deleteQuery = "delete from c_workflow_approval cwa where approval_id =?";
			jdbcTemplate.update(deleteQuery, approvalId);
			response.put("deleted", Boolean.TRUE);
		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public WorkflowDefinationModel updateStagelevelDetails(WorkflowDefinationModel workflowDefination) {

		WorkflowDefinationModel workflowDefinationModel = workflowDefinationRepository
				.findById(workflowDefination.getId()).orElseThrow(() -> new ResourceNotFoundException(
						"Workflow defination not found" + workflowDefination.getId()));
		workflowDefinationModel.setApproval_type(workflowDefination.getApproval_type());
		workflowDefinationModel.setApproval_value(workflowDefination.getApproval_value());
		workflowDefinationModel.setTimeout(workflowDefination.getTimeout());
		workflowDefinationModel.setTimeout_action(workflowDefination.getTimeout_action());
		workflowDefinationModel.setApproval_criteria(workflowDefination.getApproval_criteria());
		workflowDefinationModel.setLevel(workflowDefination.getLevel());
		workflowDefinationModel.setStage(workflowDefination.getStage());
		return workflowDefinationRepository.save(workflowDefinationModel);

	}

	@Override
	public Map<String, Boolean> deleteStage(int id) {
		Map<String, Boolean> response = new HashMap<>();
		try {
			workflowDefinationRepository.deleteById((long) id);
			response.put("deleted", Boolean.TRUE);
		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public Map<String, Boolean> deleteLevel(int flowid, int level) {
		Map<String, Boolean> response = new HashMap<>();
		try {
			String deleteQuery = "delete from c_workflow_defination cwd where flow_id =? and level =? ";
			jdbcTemplate.update(deleteQuery, flowid, level);
			response.put("deleted", Boolean.TRUE);
		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public Map<String, String> runSql(String sqlquery) {
		Map<String, String> response = new HashMap<>();
		try {
			List<String> value = jdbcTemplate.query(sqlquery, new RowMapper<String>() {
				String result = null;

				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					result = rs.getString("case");
					System.out.println("==== result " + rs.getString("case"));
					if (result.equals("true")) {
						response.put("message", "sucessTrue");
					} else {
						response.put("message", "sucessFalse");
					}
					return sqlquery;
				}
			});
		} catch (Exception e) {
			response.put("message", e.getMessage());
		}
		return response;
	}

	@Override
	public WorkflowCriteriaModel addWorkfloeCriteria(WorkflowCriteriaModel workflowCriteriaModel) {
		// TODO Auto-generated method stub
		return workflowCriteriaRepository.save(workflowCriteriaModel);
	}

	@Override
	public WorkflowCriteriaModel findCriteria(int flowid, int level) {
		List<WorkflowCriteriaModel> criteriaModel = (List<WorkflowCriteriaModel>) jdbcTemplate.query(GET_CRITERIA,
				new RowMapper<WorkflowCriteriaModel>() {

					@Override
					public WorkflowCriteriaModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkflowCriteriaModel workflowCriteria = new WorkflowCriteriaModel();
						workflowCriteria.setCriteria_sql(rs.getString("criteria_sql"));
						return workflowCriteria;
						// criteriaModel.add(workflowCriteria);
					}
				}, new Object[] { flowid, level });
		return criteriaModel.get(0);
	}

	@Override
	public WorkflowCriteriaModel findStageCriteria(int flowid, int level, int stage) {
		List<WorkflowCriteriaModel> criteriaModel = (List<WorkflowCriteriaModel>) jdbcTemplate.query(GET_STAGE_CRITERIA,
				new RowMapper<WorkflowCriteriaModel>() {

					@Override
					public WorkflowCriteriaModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkflowCriteriaModel workflowCriteria = new WorkflowCriteriaModel();
						workflowCriteria.setCriteria_sql(rs.getString("criteria_sql"));
						return workflowCriteria;
						// criteriaModel.add(workflowCriteria);
					}
				}, new Object[] { flowid, level, stage });
		return criteriaModel.get(0);
	}

	@Override
	public List<WorkflowApprovalModel> findCustGroupEmpIds(String approvalId) {
		// TODO Auto-generated method stub
		return workflowApprovalRepository.findByApprovalId(approvalId);
	}

	@Override
	public List<EmployeeMasterEntity> findEmpIdsByRole(String role) {
		// TODO Auto-generated method stub
		return personalInfoRepository.findByUserRole(role);
	}

	@Override
	public TicketWorkflowDetailsModel createWorkFlowTicket(
			List<TicketWorkflowDetailsModel> ticketWorkflowDetailsModels) {
		// TODO Auto-generated method stub
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
		String datetime = ft.format(dNow);
		for (TicketWorkflowDetailsModel ticketWorkflowDetailsModel : ticketWorkflowDetailsModels) {
			ticketWorkflowDetailsModel.setTrackingId(datetime);
			workflowTicketRepository.save(ticketWorkflowDetailsModel);
		}
		return null;
	}

	@Override
	public List<TicketWorkflowDetailsModel> getTicketTrackingId() {
		List<TicketWorkflowDetailsModel> levelList = jdbcTemplate.query(GET_TICKETS,
				new RowMapper<TicketWorkflowDetailsModel>() {
					@Override
					public TicketWorkflowDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						TicketWorkflowDetailsModel ticketWorkflowModel = new TicketWorkflowDetailsModel();
						ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
						ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
						ticketWorkflowModel.setCdt(rs.getString("cdt"));
						ticketWorkflowModel.setStatus(rs.getString("status"));
						return ticketWorkflowModel;
					}
				});
		return levelList;
	}

	@Override
	public List<TicketWorkflowDetailsModel> findTicketLevels(String trackingId) {
		List<TicketWorkflowDetailsModel> levelList = jdbcTemplate.query(GET_TICKETS_LEVELS,
				new RowMapper<TicketWorkflowDetailsModel>() {
					@Override
					public TicketWorkflowDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						TicketWorkflowDetailsModel ticketWorkflowModel = new TicketWorkflowDetailsModel();
						ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
						ticketWorkflowModel.setLevel(rs.getInt("level"));
						return ticketWorkflowModel;
					}
				}, new Object[] { trackingId });
		return levelList;
	}

	@Override
	public List<TicketWorkflowDetailsModel> findTicketStages(String trackingId, int level) {
		List<TicketWorkflowDetailsModel> levelList = jdbcTemplate.query(GET_TICKETS_STAGES,
				new RowMapper<TicketWorkflowDetailsModel>() {
					@Override
					public TicketWorkflowDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						TicketWorkflowDetailsModel ticketWorkflowModel = new TicketWorkflowDetailsModel();
						ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
						ticketWorkflowModel.setLevel(rs.getInt("level"));
						ticketWorkflowModel.setStage(rs.getInt("stage"));
						return ticketWorkflowModel;
					}
				}, new Object[] { trackingId, level });
		return levelList;
	}

	@Override
	public List<TicketWorkflowDetailsModel> findTicketSubLevels(String trackingId, int level, int stage) {
		List<TicketWorkflowDetailsModel> levelList = jdbcTemplate.query(GET_TICKETS_SUBLEVELS,
				new RowMapper<TicketWorkflowDetailsModel>() {
					@Override
					public TicketWorkflowDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						TicketWorkflowDetailsModel ticketWorkflowModel = new TicketWorkflowDetailsModel();
						ticketWorkflowModel.setApproval_from(rs.getString("approval_from"));
						ticketWorkflowModel.setPending_from_dt(rs.getDate("pending_from_dt"));
						ticketWorkflowModel.setApproved_by(rs.getString("approved_by"));
						ticketWorkflowModel.setApproved_dt(rs.getDate("approved_dt"));
						ticketWorkflowModel.setComments(rs.getString("comments"));
						ticketWorkflowModel.setStatus(rs.getString("status"));
						ticketWorkflowModel.setSub_level(rs.getInt("sub_level"));
						ticketWorkflowModel.setPdt(rs.getString("pdt"));
						ticketWorkflowModel.setApdt(rs.getString("apdt"));
						return ticketWorkflowModel;
					}
				}, new Object[] { trackingId, level, stage });
		return levelList;
	}

	@Override
	public List<TicketWorkflowDetailsModel> findPendingApprovals(String empId) {
		System.out.println("empId = " + empId);
		List<TicketWorkflowDetailsModel> levelList = jdbcTemplate.query(GET_PENDING_APPROVALS,
				new RowMapper<TicketWorkflowDetailsModel>() {
					@Override
					public TicketWorkflowDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						TicketWorkflowDetailsModel ticketWorkflowModel = new TicketWorkflowDetailsModel();
						ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
						ticketWorkflowModel.setLevel(rs.getInt("level"));
						ticketWorkflowModel.setApproval_from(rs.getString("approval_from"));
						ticketWorkflowModel.setPending_from_dt(rs.getDate("pending_from_dt"));
						ticketWorkflowModel.setFlow_id(rs.getInt("flow_id"));
						ticketWorkflowModel.setCreated_by(rs.getString("created_by"));
						ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
						return ticketWorkflowModel;
					}
				}, new Object[] { empId });
		return levelList;
	}

	@Override
	public List<TicketWorkflowDetailsModel> findAllPendingApprovals(String empId, String impersonrole) {
		logger.info("Inside findAllPendingApprovals");
		logger.info("empId = " + empId);
		logger.info("impersonrole = " + impersonrole);

		int role = 0;
		String empid1 = "";
		if (impersonrole.equals("ADMIN")) {
			role = 1;
			empid1 = "0";
		} else if (impersonrole.equals("Sales Representative")) {
			role = 0;
			empid1 = empId;
		} else if (impersonrole.equals("Supervisor")) {
			role = 0;
			empid1 = empId;
		}

		logger.info("role = " + role);

		List<TicketWorkflowDetailsModel> levelList = jdbcTemplate.query(GET_ALL_PENDING_APPROVALS,
				new RowMapper<TicketWorkflowDetailsModel>() {
					@Override
					public TicketWorkflowDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						TicketWorkflowDetailsModel ticketWorkflowModel = new TicketWorkflowDetailsModel();
						ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
						ticketWorkflowModel.setLevel(rs.getInt("level"));
						ticketWorkflowModel.setPending_from_dt(rs.getDate("pending_from_dt"));
						ticketWorkflowModel.setFlow_id(rs.getInt("flow_id"));
						ticketWorkflowModel.setCreated_by(rs.getString("created_by"));
						ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
						ticketWorkflowModel.setCdt(rs.getString("cdt"));
						ticketWorkflowModel.setFlowName(rs.getString("workflow_name"));
						ticketWorkflowModel.setStatus(rs.getString("final_status"));
						return ticketWorkflowModel;
					}
				}, new Object[] { role, empId, empid1 });
		return levelList;
	}

	@Override
	public List<TicketWorkflowDetailsModel> findAllPendingApprovalsDash(String empId, String impersonrole) {
		logger.info("Inside findAllPendingApprovalsDash");
		logger.info("empId = " + empId);
		logger.info("impersonrole = " + impersonrole);

		int role = 0;
		String empid1 = "";
		if (impersonrole.equals("ADMIN")) {
			role = 1;
			empid1 = "0";
		} else if (impersonrole.equals("Sales Representative")) {
			role = 0;
			empid1 = empId;
		} else if (impersonrole.equals("Supervisor")) {
			role = 0;
			empid1 = empId;
		}

		logger.info("role = " + role);

		List<TicketWorkflowDetailsModel> levelList = jdbcTemplate.query(GET_ALL_PENDING_APPROVALS_DASH,
				new RowMapper<TicketWorkflowDetailsModel>() {
					@Override
					public TicketWorkflowDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						TicketWorkflowDetailsModel ticketWorkflowModel = new TicketWorkflowDetailsModel();
						ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
						ticketWorkflowModel.setLevel(rs.getInt("level"));
						ticketWorkflowModel.setPending_from_dt(rs.getDate("pending_from_dt"));
						ticketWorkflowModel.setFlow_id(rs.getInt("flow_id"));
						ticketWorkflowModel.setCreated_by(rs.getString("created_by"));
						ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
						ticketWorkflowModel.setFlowName(rs.getString("workflow_name"));
						ticketWorkflowModel.setStatus(rs.getString("final_status"));
						ticketWorkflowModel.setApproval_name(rs.getString("approval_name"));
						return ticketWorkflowModel;
					}
				}, new Object[] { empId });
		return levelList;
	}

	@Override
	public List<TicketWorkflowDetailsModel> findPendingTickets(String trackingId) {
		List<TicketWorkflowDetailsModel> pendingTickets = jdbcTemplate.query(GET_PENDING_TICKETS,
				new RowMapper<TicketWorkflowDetailsModel>() {
					@Override
					public TicketWorkflowDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						TicketWorkflowDetailsModel ticketWorkflowModel = new TicketWorkflowDetailsModel();
						ticketWorkflowModel.setId(rs.getLong("ticket_id"));
						ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
						ticketWorkflowModel.setLevel(rs.getInt("level"));
						ticketWorkflowModel.setApproval_from(rs.getString("approval_from"));
						ticketWorkflowModel.setApproval_name(rs.getString("approval_name"));
						ticketWorkflowModel.setFlow_id(rs.getInt("flow_id"));
						ticketWorkflowModel.setApproved_by(rs.getString("approved_by"));
						ticketWorkflowModel.setApproved_dt(rs.getDate("approved_dt"));
						ticketWorkflowModel.setPending_from_dt(rs.getDate("pending_from_dt"));
						ticketWorkflowModel.setApdt(rs.getString("apdt"));
						ticketWorkflowModel.setPdt(rs.getString("pdt"));
						ticketWorkflowModel.setComments(rs.getString("comments"));
						ticketWorkflowModel.setStatus(rs.getString("status"));
						ticketWorkflowModel.setSub_level(rs.getInt("sub_level"));
						ticketWorkflowModel.setStage(rs.getInt("stage"));
						ticketWorkflowModel.setCreated_by(rs.getString("created_by"));
						ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
						ticketWorkflowModel.setSubmitted_by(rs.getString("submitted_by"));
						ticketWorkflowModel.setFinal_status(rs.getString("final_status"));
						return ticketWorkflowModel;
					}
				}, new Object[] { trackingId });
		return pendingTickets;
	}

	@Override
	public List<TicketWorkflowDetailsModel> findTicketLevelsStages(String trackingId) {
		List<TicketWorkflowDetailsModel> levelList = jdbcTemplate.query(GET_TICKETS_LEVELS_STAGES,
				new RowMapper<TicketWorkflowDetailsModel>() {
					@Override
					public TicketWorkflowDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						TicketWorkflowDetailsModel ticketWorkflowModel = new TicketWorkflowDetailsModel();
						ticketWorkflowModel.setId(rs.getLong("ticket_id"));
						ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
						ticketWorkflowModel.setLevel(rs.getInt("level"));
						ticketWorkflowModel.setApproval_from(rs.getString("approval_from"));
						ticketWorkflowModel.setPending_from_dt(rs.getDate("pending_from_dt"));
						ticketWorkflowModel.setFlow_id(rs.getInt("flow_id"));
						ticketWorkflowModel.setApproved_by(rs.getString("approved_by"));
						ticketWorkflowModel.setApproved_dt(rs.getDate("approved_dt"));
						ticketWorkflowModel.setComments(rs.getString("comments"));
						ticketWorkflowModel.setStatus(rs.getString("status"));
						ticketWorkflowModel.setSub_level(rs.getInt("sub_level"));
						ticketWorkflowModel.setStage(rs.getInt("stage"));
						ticketWorkflowModel.setCreated_by(rs.getString("created_by"));
						ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
						return ticketWorkflowModel;
					}
				}, new Object[] { trackingId });
		return levelList;
	}

	@Override
	public TicketWorkflowDetailsModel updateWorkflowTickets(List<TicketWorkflowDetailsModel> workflowTickets) {
		TicketWorkflowDetailsModel workflowTicketmodel = null;
		for (TicketWorkflowDetailsModel workflowTicket : workflowTickets) {
			TicketWorkflowDetailsModel workflowTicketModel = workflowTicketRepository.findById(workflowTicket.getId())
					.orElseThrow(
							() -> new ResourceNotFoundException("Workflow ticket not found" + workflowTicket.getId()));
			workflowTicketModel.setStatus(workflowTicket.getStatus());
//		workflowTicketModel.setApproved_dt(workflowTicket.getApproved_dt());
//		workflowTicketModel.setApproval_from(workflowTicket.getApproval_from());
//		workflowTicketModel.setApproved_by(workflowTicket.getApproved_by());
//		workflowTicketModel.setUpdate_dt(workflowTicket.getUpdate_dt());
//		workflowTicketModel.setUpdated_by(workflowTicket.getUpdated_by());

			workflowTicketmodel = workflowTicketRepository.save(workflowTicketModel);
		}
		return workflowTicketmodel;
	}

	@Override
	public void runCronJob() {

		try {
			System.out.println("cron run ------->");
			List<WorkflowDefinationModel> workFlowList = jdbcTemplate.query(RUN_CRON_JOB,
					new RowMapper<WorkflowDefinationModel>() {
						@Override
						public WorkflowDefinationModel mapRow(ResultSet rs, int rowNum) throws SQLException {
							WorkflowDefinationModel ticketWorkflowModel = new WorkflowDefinationModel();

							ticketWorkflowModel.setLevel(rs.getInt("level"));
							ticketWorkflowModel.setTimeout(rs.getInt("timeout"));
							ticketWorkflowModel.setTimeout_action(rs.getString("timeout_action"));
							ticketWorkflowModel.setFlowId(rs.getInt("flow_id"));
							ticketWorkflowModel.setApproval_criteria(rs.getString("approval_criteria"));
							ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
							ticketWorkflowModel.setStage(rs.getInt("stage"));
							ticketWorkflowModel.setTrackingId(rs.getString("trackingId      "));
							return ticketWorkflowModel;
						}
					});

			for (WorkflowDefinationModel workFlow : workFlowList) {

				String dateStr1 = workFlow.getCreated_dt().toString();
				// String dateStr2 = "2021-05-16";

				LocalDate dateObj = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String dateStr2 = dateObj.format(formatter);

				// parsing the string date into LocalDate objects.
				LocalDate localDate1 = LocalDate.parse(dateStr1);
				LocalDate localDate2 = LocalDate.parse(dateStr2);

				// java 8 way - 1
				// Fetching the diff using between() method
				long noOfDaysDifference = ChronoUnit.DAYS.between(localDate1, localDate2);

				if (noOfDaysDifference == workFlow.getTimeout()) {
					if (workFlow.getTimeout_action().equals("Auto Approve")) {

						String updateQuery = "update t_workflow_details twd set status ='Approved',approved_by ='auto approval',updated_by ='auto approval'"
								+ " ,approved_dt =?,update_dt =?, where flow_id =? and level = ? and stage =?";
						jdbcTemplate.update(updateQuery, localDate2, localDate2, workFlow.getFlowId(),
								workFlow.getLevel(), workFlow.getStage());
						changeStatus(workFlow.getTrackingId());

					} else if (workFlow.getTimeout_action().equals("Auto Cancel")) {

						String updateQuery = "update t_workflow_details twd set status ='Rejected',approved_by ='auto reject',updated_by ='auto reject'"
								+ " ,approved_dt =?,update_dt =?, where flow_id =? and level = ? and stage =?";
						jdbcTemplate.update(updateQuery, localDate2, localDate2, workFlow.getFlowId(),
								workFlow.getLevel(), workFlow.getStage());

					} else {

					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return levelList;

	}

	public void changeStatus(String trackingId) {
		List<TicketWorkflowDetailsModel> ticketWorkFlowDtls = findTicketLevelsStages(trackingId);
		TicketWorkflowDetailsModel ticketWorkflowDetailsModel = ticketWorkFlowDtls.get(0);
		ticketWorkflowDetailsModel.setStatus("Pending");
		updateWorkflowTicket(ticketWorkflowDetailsModel);
		runCronJob();

	}

	@Override
	public List<WorkflowMessageEntity> getContentMessage(String trackingId) {
		logger.info(trackingId);
		List<WorkflowMessageEntity> contentMessage = jdbcTemplate.query(GET_CONTENT_MESSAGE,
				new RowMapper<WorkflowMessageEntity>() {
					@Override
					public WorkflowMessageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkflowMessageEntity ticketWorkflowModel = new WorkflowMessageEntity();
						ticketWorkflowModel.setTracking_id(rs.getString("tracking_id"));
						ticketWorkflowModel.setMessage_content(rs.getString("message_content"));

						ticketWorkflowModel.setCreated_by(rs.getString("created_by"));
						ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
						return ticketWorkflowModel;
					}
				}, new Object[] { "%" + trackingId + "%" });
		logger.info(contentMessage.toString());
		return contentMessage;
	}

	@Override
	public List<WorkflowMessageEntity> getApprovalsData(String empId, String loggedinrole, String stat, String fdt,
			String tdt) {

		int dtStatus1 = 0;
		int dtStatus2 = 0;

		if (fdt.equals("1111-11-11") || tdt.equals("1111-11-11")) {
			dtStatus1 = 0;
			dtStatus2 = 1;
		} else {
			dtStatus1 = 1;
			dtStatus2 = 0;
		}

		logger.info("dtStatus1 = " + dtStatus1);
		logger.info("dtStatus2 = " + dtStatus2);

		int against = 0;
		String against1 = "";
		int approvalfrom = 0;
		String approvalfrom1 = "";
		String appRejId = "";
		String query = "";
		String query1 = "";
		int val = 0;

		if (loggedinrole.equals("ADMIN")) {
			logger.info("Inside ADMIN");
			against = 1;
			against1 = "0";
			approvalfrom = 1;
			approvalfrom1 = "ADMIN";
			appRejId = "ADMIN";
			val = 1;
			query = GET_APPROVAL_REJECTED_DATA_SUP;
			query1 = GET_ALL_DATA_SUP;
		} else if (loggedinrole.equals("Sales Representative")) {
			logger.info("Inside Sales Representative");
			against = 0;
			against1 = empId;
			approvalfrom = 1;
			approvalfrom1 = "0";
			appRejId = empId;
			val = 0;
			query = GET_APPROVAL_REJECTED_DATA_EMP;
			query1 = GET_ALL_DATA_EMP;

		} else if (loggedinrole.equals("Supervisor")) {
			logger.info("Inside Supervisor");
			against = 1;
			against1 = "0";
			approvalfrom = 1;
			approvalfrom1 = empId;
			appRejId = empId;
			val = 0;
			query = GET_APPROVAL_REJECTED_DATA_SUP;
			query1 = GET_ALL_DATA_SUP;
		}

		logger.info("query1 = " + query1);

		List<WorkflowMessageEntity> contentMessage = null;

		if (stat.equals("all")) {
			logger.info("Inside ALL");

			if (loggedinrole.equals("Sales Representative")) {
				logger.info("Inside IF");
				contentMessage = getAllUserData(val, appRejId, query1, dtStatus1, dtStatus2, fdt, tdt);
			} else {
				logger.info("Inside ELSE");
				contentMessage = getAllData(val, appRejId, query1, dtStatus1, dtStatus2, fdt, tdt);
			}

		} else if (stat.equals("Approved") || stat.equals("Rejected")) {
			logger.info("Inside approved rejected");
			contentMessage = getAppRejData(stat, stat, appRejId, query);
		} else {
			logger.info("Inside else pending data");
			contentMessage = getPendingData(against, against1, approvalfrom, approvalfrom1);
		}

		return contentMessage;
	}

	public List<WorkflowMessageEntity> getAllData(int val, String appRejId, String query1, int dtStatus1, int dtStatus2,
			String fdt, String tdt) {

		logger.info("Inside getAllData");

		logger.info("Inside dtStatus1 =" + dtStatus1);
		logger.info("Inside dtStatus2 =" + dtStatus2);
		logger.info("Inside fdt =" + fdt);
		logger.info("Inside tdt =" + tdt);
		logger.info("Inside val =" + val);
		logger.info("Inside appRejId =" + appRejId);

		List<WorkflowMessageEntity> contentMessage = jdbcTemplate.query(query1, new RowMapper<WorkflowMessageEntity>() {
			@Override
			public WorkflowMessageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				WorkflowMessageEntity ticketWorkflowModel = new WorkflowMessageEntity();
				ticketWorkflowModel.setId(rs.getLong("ticket_id"));
				ticketWorkflowModel.setFlow_id(rs.getInt("flow_id"));
				ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
				ticketWorkflowModel.setMessage_content(rs.getString("message_content"));
				ticketWorkflowModel.setCreated_by(rs.getString("created_by"));

				ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
				ticketWorkflowModel.setPending_from_dt(rs.getDate("pending_from_dt"));

				ticketWorkflowModel.setCdt(rs.getString("cdt"));
				ticketWorkflowModel.setPdt(rs.getString("pdt"));

				ticketWorkflowModel.setApproval_name(rs.getString("approval_name"));

				ticketWorkflowModel.setApproved_dt(rs.getDate("approved_dt"));
				ticketWorkflowModel.setApproved_by(rs.getString("approved_by"));
				ticketWorkflowModel.setStatus(rs.getString("status"));
				ticketWorkflowModel.setFinal_status(rs.getString("final_status"));
				ticketWorkflowModel.setWorkflow_name(rs.getString("workflow_name"));
				ticketWorkflowModel.setComments(rs.getString("comments"));
				ticketWorkflowModel.setChargeback_delete_id(rs.getString("chargeback_delete_id"));
				ticketWorkflowModel.setComplanid(rs.getInt("comm_plan_id"));
				ticketWorkflowModel.setComplex_kpiid(rs.getInt("complex_kpiid"));
				ticketWorkflowModel.setSalesRepId(rs.getString("sales_rep_id"));
				ticketWorkflowModel.setEmpName(rs.getString("empname"));
				ticketWorkflowModel.setSales_channel(rs.getString("sales_channel"));

				return ticketWorkflowModel;
			}
		}, new Object[] { dtStatus1, dtStatus2, fdt, tdt, val, appRejId });
		logger.info(contentMessage.toString());
		return contentMessage;
	}

	public List<WorkflowMessageEntity> getAllUserData(int val, String appRejId, String query1, int dtStatus1,
			int dtStatus2, String fdt, String tdt) {

		logger.info("Inside getAllData");

		logger.info("Inside dtStatus1 =" + dtStatus1);
		logger.info("Inside dtStatus2 =" + dtStatus2);
		logger.info("Inside fdt =" + fdt);
		logger.info("Inside tdt =" + tdt);
		logger.info("Inside val =" + val);
		logger.info("Inside appRejId =" + appRejId);

		List<WorkflowMessageEntity> contentMessage = jdbcTemplate.query(query1, new RowMapper<WorkflowMessageEntity>() {
			@Override
			public WorkflowMessageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				WorkflowMessageEntity ticketWorkflowModel = new WorkflowMessageEntity();
				ticketWorkflowModel.setId(rs.getLong("ticket_id"));
				ticketWorkflowModel.setFlow_id(rs.getInt("flow_id"));
				ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
				ticketWorkflowModel.setMessage_content(rs.getString("message_content"));
				ticketWorkflowModel.setCreated_by(rs.getString("created_by"));

				ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
				ticketWorkflowModel.setPending_from_dt(rs.getDate("pending_from_dt"));

				ticketWorkflowModel.setCdt(rs.getString("cdt"));
				ticketWorkflowModel.setPdt(rs.getString("pdt"));

				ticketWorkflowModel.setApproval_name(rs.getString("approval_name"));

				ticketWorkflowModel.setApproved_dt(rs.getDate("approved_dt"));
				ticketWorkflowModel.setApproved_by(rs.getString("approved_by"));
				ticketWorkflowModel.setStatus(rs.getString("status"));
				ticketWorkflowModel.setFinal_status(rs.getString("final_status"));
				ticketWorkflowModel.setWorkflow_name(rs.getString("workflow_name"));
				ticketWorkflowModel.setComments(rs.getString("comments"));
				ticketWorkflowModel.setChargeback_delete_id(rs.getString("chargeback_delete_id"));
				ticketWorkflowModel.setComplanid(rs.getInt("comm_plan_id"));
				ticketWorkflowModel.setComplex_kpiid(rs.getInt("complex_kpiid"));

				return ticketWorkflowModel;
			}
		}, new Object[] { dtStatus1, dtStatus2, fdt, tdt, val, appRejId });
		logger.info(contentMessage.toString());
		return contentMessage;
	}

	public List<WorkflowMessageEntity> getPendingData(int against, String against1, int approvalfrom,
			String approvalfrom1) {
		logger.info("Inside getPendingData");
		List<WorkflowMessageEntity> contentMessage = jdbcTemplate.query(GET_PENDING_DATA,
				new RowMapper<WorkflowMessageEntity>() {
					@Override
					public WorkflowMessageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkflowMessageEntity ticketWorkflowModel = new WorkflowMessageEntity();
						ticketWorkflowModel.setId(rs.getLong("ticket_id"));
						ticketWorkflowModel.setFlow_id(rs.getInt("flow_id"));
						ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
						ticketWorkflowModel.setMessage_content(rs.getString("message_content"));
						ticketWorkflowModel.setCreated_by(rs.getString("created_by"));

						ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
						ticketWorkflowModel.setPending_from_dt(rs.getDate("pending_from_dt"));

						ticketWorkflowModel.setCdt(rs.getString("cdt"));
						ticketWorkflowModel.setPdt(rs.getString("pdt"));

						ticketWorkflowModel.setApproval_name(rs.getString("approval_name"));

						ticketWorkflowModel.setApproved_dt(rs.getDate("approved_dt"));
						ticketWorkflowModel.setApproved_by(rs.getString("approved_by"));
						ticketWorkflowModel.setStatus(rs.getString("status"));
						ticketWorkflowModel.setFinal_status(rs.getString("final_status"));
						ticketWorkflowModel.setWorkflow_name(rs.getString("workflow_name"));
						ticketWorkflowModel.setComments(rs.getString("comments"));

						return ticketWorkflowModel;
					}
				}, new Object[] { against, against1, approvalfrom, approvalfrom1 });
		logger.info(contentMessage.toString());
		return contentMessage;
	}

	public List<WorkflowMessageEntity> getAppRejData(String stat1, String stat2, String appRejId, String query) {
		logger.info("Inside getAppRejData");
		List<WorkflowMessageEntity> contentMessage = jdbcTemplate.query(query, new RowMapper<WorkflowMessageEntity>() {
			@Override
			public WorkflowMessageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				WorkflowMessageEntity ticketWorkflowModel = new WorkflowMessageEntity();
				ticketWorkflowModel.setId(rs.getLong("ticket_id"));
				ticketWorkflowModel.setFlow_id(rs.getInt("flow_id"));
				ticketWorkflowModel.setTrackingId(rs.getString("tracking_id"));
				ticketWorkflowModel.setMessage_content(rs.getString("message_content"));
				ticketWorkflowModel.setCreated_by(rs.getString("created_by"));

				ticketWorkflowModel.setCreated_dt(rs.getDate("created_dt"));
				ticketWorkflowModel.setPending_from_dt(rs.getDate("pending_from_dt"));

				ticketWorkflowModel.setCdt(rs.getString("cdt"));
				ticketWorkflowModel.setPdt(rs.getString("pdt"));

				ticketWorkflowModel.setApproval_name(rs.getString("approval_name"));

				ticketWorkflowModel.setApproved_dt(rs.getDate("approved_dt"));
				ticketWorkflowModel.setApproved_by(rs.getString("approved_by"));
				ticketWorkflowModel.setStatus(rs.getString("status"));
				ticketWorkflowModel.setFinal_status(rs.getString("final_status"));
				ticketWorkflowModel.setWorkflow_name(rs.getString("workflow_name"));
				ticketWorkflowModel.setComments(rs.getString("comments"));

				return ticketWorkflowModel;
			}
		}, new Object[] { stat1, stat2, appRejId });
		logger.info(contentMessage.toString());
		return contentMessage;
	}

	@Override
	public TicketWorkflowDetailsModel updateWorkflowTicket(TicketWorkflowDetailsModel workflowTicket) {

		logger.info("workflowTicket = " + workflowTicket);

		TicketWorkflowDetailsModel workflowTicketModel = workflowTicketRepository.findById(workflowTicket.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Workflow ticket not found" + workflowTicket.getId()));

		TicketWorkflowDetailsModel ticketWorkflowDetailsModel = null;

		workflowTicketModel.setStatus(workflowTicket.getStatus());
		workflowTicketModel.setApproved_dt(new Date());
		workflowTicketModel.setApproved_by(workflowTicket.getApproved_by());
		workflowTicketModel.setUpdate_dt(new Date());
		workflowTicketModel.setUpdated_by(workflowTicket.getUpdated_by());
		workflowTicketModel.setComments(workflowTicket.getComments());

		ticketWorkflowDetailsModel = workflowTicketRepository.save(workflowTicketModel);

		int cnt = workflowTicketRepository.getInitiatedTicketCount(workflowTicket.getTrackingId());

		if (cnt > 0) {
			Long tickeid = workflowTicketRepository.getInitiatedTicketDetailsId(workflowTicket.getTrackingId());

			TicketWorkflowDetailsModel ticketStatusUpdate = workflowTicketRepository.findById(tickeid)
					.orElseThrow(() -> new ResourceNotFoundException("Workflow ticket not found" + tickeid));
			ticketStatusUpdate.setStatus("Pending");
			ticketWorkflowDetailsModel = workflowTicketRepository.save(ticketStatusUpdate);
		} else {
			jdbcTemplate.update("update t_workflow_details set final_status = 'Approved' where tracking_id = '"
					+ workflowTicket.getTrackingId() + "'");
		}

		if (workflowTicket.getStatus().equals("Rejected")) {
			jdbcTemplate.update("update t_workflow_details set final_status = 'Rejected' where tracking_id = '"
					+ workflowTicket.getTrackingId() + "'");
		}

		return ticketWorkflowDetailsModel;

	}

	/* This method is used to update multiple tickets */
	@Override
	public TicketWorkflowDetailsModel approvRejectWorkflow(
			List<TicketWorkflowDetailsModel> ticketWorkflowDetailsModel) {

		logger.info("ticketWorkflowDetailsModel = " + ticketWorkflowDetailsModel);

		TicketWorkflowDetailsModel ticketWorkflowDetailsObj = null;

		int i = 0;
		String status = "";
		String app_by = "";
		String updated_by = "";
		String loggedInRole = "";

		for (TicketWorkflowDetailsModel workflowTicket : ticketWorkflowDetailsModel) {
			i++;

			TicketWorkflowDetailsModel workflowTicketModel = workflowTicketRepository.findById(workflowTicket.getId())
					.orElseThrow(
							() -> new ResourceNotFoundException("Workflow ticket not found" + workflowTicket.getId()));

			if (i == 1) {
				status = workflowTicket.getStatus();
				app_by = workflowTicket.getApproved_by();
				updated_by = workflowTicket.getUpdated_by();
				loggedInRole = workflowTicket.getLoggedinrole();
			}

			workflowTicketModel.setStatus(status);
			workflowTicketModel.setApproved_dt(new Date());
			workflowTicketModel.setApproved_by(app_by);
			workflowTicketModel.setUpdate_dt(new Date());
			workflowTicketModel.setUpdated_by(updated_by);
			workflowTicketModel.setComments(workflowTicket.getComments());

			ticketWorkflowDetailsObj = workflowTicketRepository.save(workflowTicketModel);

			int cnt = workflowTicketRepository.getInitiatedTicketCount(workflowTicket.getTrackingId());
			logger.info("cnt = " + cnt);

			logger.info("getTrackingId = " + workflowTicket.getTrackingId());

			if (status.equals("Rejected")) {
				logger.info("Inside rejected IF");
				jdbcTemplate.update(
						"update t_workflow_details set status='Rejected', final_status = 'Rejected',approved_by='"
								+ app_by + "',approved_dt='" + new Date() + "' where ticket_id = '"
								+ workflowTicket.getId() + "'");
			} else {
				if (loggedInRole.equals("ADMIN")) {
					logger.info("Inside approved IF");
					jdbcTemplate.update(
							"update t_workflow_details set status='Approved', final_status = 'Approved',approved_by='"
									+ app_by + "',approved_dt='" + new Date() + "' where ticket_id = '"
									+ workflowTicket.getId() + "'");
				} else {
					if (cnt > 0) {
						logger.info("Inside approved ELSE IF");
						Long tickeid = workflowTicketRepository
								.getInitiatedTicketDetailsId(workflowTicket.getTrackingId());
						logger.info("tickeid = " + tickeid);

						TicketWorkflowDetailsModel ticketStatusUpdate = workflowTicketRepository.findById(tickeid)
								.orElseThrow(
										() -> new ResourceNotFoundException("Workflow ticket not found" + tickeid));
						ticketStatusUpdate.setStatus("Pending");
//						ticketStatusUpdate.setApproved_dt(new Date());
//						ticketStatusUpdate.setApproved_by(app_by);
						ticketWorkflowDetailsObj = workflowTicketRepository.save(ticketStatusUpdate);

						jdbcTemplate.update(
								"update t_workflow_details set status='Approved',final_status = 'Approved',approved_by='"
										+ app_by + "',approved_dt='" + new Date() + "' where ticket_id = '"
										+ workflowTicket.getId() + "'");
					} else {
						logger.info("Inside approved ELSE ELSE");
						jdbcTemplate.update("update t_workflow_details set final_status = 'Approved',approved_by='"
								+ app_by + "',approved_dt='" + new Date() + "' where ticket_id = '"
								+ workflowTicket.getId() + "'");
					}
				}
			}

		}

		return ticketWorkflowDetailsObj;
	}

}
