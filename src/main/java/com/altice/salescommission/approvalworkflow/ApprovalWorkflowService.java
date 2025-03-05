package com.altice.salescommission.approvalworkflow;

import java.util.List;
import java.util.Map;

import com.altice.salescommission.approvalworkflow.models.TicketWorkflowDetailsModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowApprovalModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowCriteriaModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowDefinationModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowMasterModel;
import com.altice.salescommission.entity.EmployeeMasterEntity;
import com.altice.salescommission.entity.KpiMasterEntity;
import com.altice.salescommission.entity.WorkflowMessageEntity;

public interface ApprovalWorkflowService {

	List<WorkflowMasterModel> getWorkflowNames();

	List<WorkflowDefinationModel> getWorkflowDefValues();

	WorkflowDefinationModel createWorkFlowDef(WorkflowDefinationModel workFLowDefinationModel);
	
	WorkflowMasterModel addFlow(WorkflowMasterModel workflowMasterModel);

	List<WorkflowDefinationModel> findByFlowId(int flowid);

	List<WorkflowDefinationModel> findStages(int flowid, int level);

	List<WorkflowDefinationModel> findDefinition(int flowid, int level, int stage);

	WorkflowApprovalModel addApprovalWorkFlow(List<WorkflowApprovalModel> workflowApprovalModels);

	List<WorkflowApprovalModel> getApprovalWorkFlows();

	List<WorkflowApprovalModel> findByApprovalId(String approvalId);

	WorkflowApprovalModel updateApprovalList(List<WorkflowApprovalModel> approvalModels);

	Map<String, Boolean> deleteApproval(int id);
	
	Map<String, Boolean> removeChargeback(int id,String status);

	Map<String, Boolean> deleteApprovalsByAprovalId(String approvalId);

	WorkflowDefinationModel updateStagelevelDetails(WorkflowDefinationModel workflowDefination);

	Map<String, Boolean> deleteStage(int id);

	Map<String, Boolean> deleteLevel(int flowid, int level);

	Map<String, String> runSql(String sqlquery);

	WorkflowCriteriaModel addWorkfloeCriteria(WorkflowCriteriaModel workflowCriteriaModel);

	WorkflowCriteriaModel findCriteria(int flowid, int level);

	WorkflowCriteriaModel findStageCriteria(int flowid, int level, int stage);

	List<WorkflowApprovalModel> findCustGroupEmpIds(String approvalId);

	List<EmployeeMasterEntity> findEmpIdsByRole(String role);

	TicketWorkflowDetailsModel createWorkFlowTicket(List<TicketWorkflowDetailsModel> ticketWorkflowDetailsModel);
	
	TicketWorkflowDetailsModel createTicket(TicketWorkflowDetailsModel ticketWorkflowDetailsModel);

	List<TicketWorkflowDetailsModel> getTicketTrackingId();

	List<TicketWorkflowDetailsModel> findTicketLevels(String trackingId);

	List<TicketWorkflowDetailsModel> findTicketStages(String trackingId, int level);

	List<TicketWorkflowDetailsModel> findTicketSubLevels(String trackingId, int level, int stage);

	List<TicketWorkflowDetailsModel> findPendingApprovals(String empId);

	List<TicketWorkflowDetailsModel> findPendingTickets(String trackingId);
	
	List<WorkflowMessageEntity> getContentMessage(String trackingId);
	
	List<WorkflowMessageEntity> getApprovalsData(String empId,String loggedinrole, String stat, String fdt, String tdt);

	TicketWorkflowDetailsModel updateWorkflowTicket(TicketWorkflowDetailsModel workflowTicket);

	List<TicketWorkflowDetailsModel> findTicketLevelsStages(String trackingId);

	TicketWorkflowDetailsModel updateWorkflowTickets(List<TicketWorkflowDetailsModel> workflowTickets);

	List<TicketWorkflowDetailsModel> findAllPendingApprovals(String empId,String impersonrole);
	
	List<TicketWorkflowDetailsModel> findAllPendingApprovalsDash(String empId,String impersonrole);
	
	TicketWorkflowDetailsModel approvRejectWorkflow(List<TicketWorkflowDetailsModel> ticketWorkflowDetailsModel);

	void runCronJob();

}
