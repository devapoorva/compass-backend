package com.altice.salescommission.approvalworkflow;

import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.approvalworkflow.models.TicketWorkflowDetailsModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowApprovalModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowCriteriaModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowDefinationModel;
import com.altice.salescommission.approvalworkflow.models.WorkflowMasterModel;
import com.altice.salescommission.entity.EmployeeMasterEntity;
import com.altice.salescommission.entity.WorkflowMessageEntity;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/approval/workflow")
public class ApprovalWorkflowController {
	
	@Autowired
	private ApprovalWorkflowService approvalWorkflowService;
	@Autowired
	Commons comm;
	
	@GetMapping("/getworkflownames")
	public ResponseEntity<List<WorkflowMasterModel>> getWorkflowNames() {
		List<WorkflowMasterModel> workflows = approvalWorkflowService.getWorkflowNames();
		return new ResponseEntity<>(workflows, OK);
	}
	
	@GetMapping("/getworkflowdefvalues")
	public ResponseEntity<List<WorkflowDefinationModel>> getWorkflowDefValues() {
		List<WorkflowDefinationModel> workflowdefs = approvalWorkflowService.getWorkflowDefValues();
		return new ResponseEntity<>(workflowdefs, OK);
	}
	
	@PostMapping("/createworkflowdef")
	public ResponseEntity<WorkflowDefinationModel> createWorkFlowDef(@RequestBody WorkflowDefinationModel workFLowDefinationModel) {
		WorkflowDefinationModel workflow = approvalWorkflowService.createWorkFlowDef(workFLowDefinationModel);
		return new ResponseEntity<>(workflow, OK);
	}
	
	@PostMapping("/addflow")
	public ResponseEntity<WorkflowMasterModel> addFlow(@RequestBody WorkflowMasterModel workflowMasterModel) {
		WorkflowMasterModel workflow = approvalWorkflowService.addFlow(workflowMasterModel);
		return new ResponseEntity<>(workflow, OK);
	}
	
	@GetMapping("/fetchworkflowdef/{flowid}")
	public ResponseEntity<List<WorkflowDefinationModel>> findByFlowId(@PathVariable int flowid) {
		List<WorkflowDefinationModel> workflowDefinationModels = approvalWorkflowService.findByFlowId(flowid);
		return new ResponseEntity<>(workflowDefinationModels, OK);
	}
	
	@GetMapping("/loadstages/{flowid}/{level}")
	public ResponseEntity<List<WorkflowDefinationModel>> findStages(@PathVariable int flowid,@PathVariable int level) {
		List<WorkflowDefinationModel> workflowDefinationModels = approvalWorkflowService.findStages(flowid,level);
		return new ResponseEntity<>(workflowDefinationModels, OK);
	}
	
	@GetMapping("/loaddefinition/{flowid}/{level}/{stage}")
	public ResponseEntity<List<WorkflowDefinationModel>> findDefinition(@PathVariable int flowid,@PathVariable int level,@PathVariable int stage) {
		List<WorkflowDefinationModel> workflowDefinationModels = approvalWorkflowService.findDefinition(flowid,level,stage);
		return new ResponseEntity<>(workflowDefinationModels, OK);
	}
	
	@PutMapping("/updatestagelevelcriteria")
	public ResponseEntity<WorkflowDefinationModel> updateStagelevelDetails(@RequestBody WorkflowDefinationModel workflowDefination) {
		WorkflowDefinationModel updateWorkflow = approvalWorkflowService.updateStagelevelDetails(workflowDefination);
		return new ResponseEntity<>(updateWorkflow, OK);
	}
	
	@DeleteMapping("/deleteworkflowstage/{id}")
    public ResponseEntity<HttpResponse> deleteStage(@PathVariable int id) {
		Map<String, Boolean> response = new HashMap<>();
		response= approvalWorkflowService.deleteStage(id);
		return comm.response(OK, "Record is Deleted Sucessfully");
    }
	
	@DeleteMapping("/deleteworkflowlevel/{flowid}/{level}")
    public ResponseEntity<HttpResponse> deleteLevel(@PathVariable int flowid,@PathVariable int level) {
		Map<String, Boolean> response = new HashMap<>();
		response= approvalWorkflowService.deleteLevel(flowid,level);
		return comm.response(OK, "Record is Deleted Sucessfully");
    }
	
	@PostMapping("/addapprovalworkFlow")
	public ResponseEntity<WorkflowApprovalModel> addApprovalWorkFlow(@RequestBody List<WorkflowApprovalModel> workflowApprovalModels) {
		WorkflowApprovalModel approvalWorkflow = approvalWorkflowService.addApprovalWorkFlow(workflowApprovalModels);
		return new ResponseEntity<>(approvalWorkflow, OK);
	}
	
	@GetMapping("/getapprovalworkFlows")
	public ResponseEntity<List<WorkflowApprovalModel>> getApprovalWorkFlows() {
		List<WorkflowApprovalModel> workflows = approvalWorkflowService.getApprovalWorkFlows();
		return new ResponseEntity<>(workflows, OK);
	}
	
	@GetMapping("/fetchworkflowbyaprvlid/{approvalId}")
	public ResponseEntity<List<WorkflowApprovalModel>> findByApprovalId(@PathVariable String approvalId) {
		List<WorkflowApprovalModel> workflowApprovalModels = approvalWorkflowService.findByApprovalId(approvalId);
		return new ResponseEntity<>(workflowApprovalModels, OK);
	}
	
	@PutMapping("/updateapprovals")
	public ResponseEntity<WorkflowApprovalModel> updateApprovalList(@RequestBody List<WorkflowApprovalModel> approvalModels) {
		
		WorkflowApprovalModel approvalModel = approvalWorkflowService.updateApprovalList(approvalModels);
		return new ResponseEntity<>(approvalModel, OK);
	}
	
	@DeleteMapping("/removechargeback/{id}/{status}")
    public ResponseEntity<HttpResponse> removeChargeback(@PathVariable int id,@PathVariable String status) {
		Map<String, Boolean> response = new HashMap<>();
		response= approvalWorkflowService.removeChargeback(id, status);
		return comm.response(OK, "Chargeback removed Sucessfully");
    }
	
	@DeleteMapping("/deleteapproval/{id}")
    public ResponseEntity<HttpResponse> deleteApproval(@PathVariable int id) {
		Map<String, Boolean> response = new HashMap<>();
		response= approvalWorkflowService.deleteApproval(id);
		return comm.response(OK, "Record is Deleted Sucessfully");
    }
	
	@DeleteMapping("/deleteapprovals/{approvalId}")
    public ResponseEntity<HttpResponse> deleteApprovalsByAprovalId(@PathVariable String approvalId) {
		Map<String, Boolean> response = new HashMap<>();
		response= approvalWorkflowService.deleteApprovalsByAprovalId(approvalId);
		return comm.response(OK, "Record is Deleted Sucessfully");
    }
	
	/* Get SQL query to test */
	@GetMapping("/runsql/{sqlquery}")
	public Map<String, String> runSql(@PathVariable("sqlquery") String sqlquery) {
		return approvalWorkflowService.runSql(sqlquery);
		//return new ResponseEntity<>(reportList, OK);
	}

	@PostMapping("/addworkflowcriteria")
	public ResponseEntity<WorkflowCriteriaModel> addWorkfloeCriteria(@RequestBody WorkflowCriteriaModel workflowCriteriaModel) {
		WorkflowCriteriaModel workFlowCriteria = approvalWorkflowService.addWorkfloeCriteria(workflowCriteriaModel);
		return new ResponseEntity<>(workFlowCriteria, OK);
	}
	
	@GetMapping("/findcriteria/{flowid}/{level}")
	public ResponseEntity <WorkflowCriteriaModel> findCriteria(@PathVariable int flowid,@PathVariable int level) {
		WorkflowCriteriaModel workflowCriteriaModel = approvalWorkflowService.findCriteria(flowid,level);
		return new ResponseEntity<>(workflowCriteriaModel,OK);
	}
	
	@GetMapping("/findstagecriteria/{flowid}/{level}/{stage}")
	public ResponseEntity <WorkflowCriteriaModel> findstagecriteria(@PathVariable int flowid,@PathVariable int level,@PathVariable int stage) {
		WorkflowCriteriaModel workflowCriteriaModel = approvalWorkflowService.findStageCriteria(flowid,level,stage);
		return new ResponseEntity<>(workflowCriteriaModel,OK);
	}
	
	@GetMapping("/getcustgroupempid/{approvalId}")
	public ResponseEntity<List<WorkflowApprovalModel>> findCustGroupEmpIds(@PathVariable String approvalId) {
		List<WorkflowApprovalModel> workflows = approvalWorkflowService.findCustGroupEmpIds(approvalId);
		return new ResponseEntity<>(workflows, OK);
	}
	
	@GetMapping("/getempidbyrole/{role}")
	public ResponseEntity<List<EmployeeMasterEntity>> findEmpIdsByRole(@PathVariable String role) {
		List<EmployeeMasterEntity> users = approvalWorkflowService.findEmpIdsByRole(role);
		return new ResponseEntity<>(users, OK);
	}
	
	@PostMapping("/createworkflowticket")
	public ResponseEntity<TicketWorkflowDetailsModel> createWorkFlowTicket(@RequestBody List<TicketWorkflowDetailsModel> ticketWorkflowDetailsModel) {
		TicketWorkflowDetailsModel ticketWorkFlow = approvalWorkflowService.createWorkFlowTicket(ticketWorkflowDetailsModel);
		return new ResponseEntity<>(ticketWorkFlow, OK);
	}
	
	@PostMapping("/createticket")
	public ResponseEntity<TicketWorkflowDetailsModel> createTicket(@RequestBody TicketWorkflowDetailsModel ticketWorkflowDetailsModel) {
		TicketWorkflowDetailsModel ticketWorkFlow = approvalWorkflowService.createTicket(ticketWorkflowDetailsModel);
		return new ResponseEntity<>(ticketWorkFlow, OK);
	}
	
	
	@GetMapping("/gettrackingid")
	public ResponseEntity<List<TicketWorkflowDetailsModel>> getTicketTrackingId() {
		List<TicketWorkflowDetailsModel> trackingIds = approvalWorkflowService.getTicketTrackingId();
		return new ResponseEntity<>(trackingIds, OK);
	}
	
	@GetMapping("/getticketlevels/{trackingId}")
	public ResponseEntity<List<TicketWorkflowDetailsModel>> findTicketLevels(@PathVariable String trackingId) {
		List<TicketWorkflowDetailsModel> ticketLevels = approvalWorkflowService.findTicketLevels(trackingId);
		return new ResponseEntity<>(ticketLevels, OK);
	}
	
	@GetMapping("/getticketstages/{trackingId}/{level}")
	public ResponseEntity<List<TicketWorkflowDetailsModel>> findTicketStages(@PathVariable String trackingId,@PathVariable int level) {
		List<TicketWorkflowDetailsModel> ticketStages = approvalWorkflowService.findTicketStages(trackingId,level);
		return new ResponseEntity<>(ticketStages, OK);
	}
	
	@GetMapping("/gettickesubtlevels/{trackingId}/{level}/{stage}")
	public ResponseEntity<List<TicketWorkflowDetailsModel>> findTicketSubLevels(@PathVariable String trackingId,@PathVariable int level,@PathVariable int stage) {
		List<TicketWorkflowDetailsModel> ticketSubLevels = approvalWorkflowService.findTicketSubLevels(trackingId,level,stage);
		return new ResponseEntity<>(ticketSubLevels, OK);
	}
	
	@GetMapping("/pendingapprovals/{empId}")
	public ResponseEntity<List<TicketWorkflowDetailsModel>> findPendingApprovals(@PathVariable String empId) {
		List<TicketWorkflowDetailsModel> pendingApprovals = approvalWorkflowService.findPendingApprovals(empId);
		return new ResponseEntity<>(pendingApprovals, OK);
	}
	
	@GetMapping("/allpendingapprovals/{empId}/{impersonrole}")
	public ResponseEntity<List<TicketWorkflowDetailsModel>> findAllPendingApprovals(@PathVariable String empId,@PathVariable String impersonrole) {
		List<TicketWorkflowDetailsModel> pendingApprovals = approvalWorkflowService.findAllPendingApprovals(empId,impersonrole);
		return new ResponseEntity<>(pendingApprovals, OK);
	}
	
	
	
	@GetMapping("/pendingtickets/{trackingId}")
	public ResponseEntity<List<TicketWorkflowDetailsModel>> findPendingTickets(@PathVariable String trackingId) {
		List<TicketWorkflowDetailsModel> pendingTickets = approvalWorkflowService.findPendingTickets(trackingId);
		return new ResponseEntity<>(pendingTickets, OK);
	}
	
	@GetMapping("/getcontentmessage/{trackingId}")
	public ResponseEntity<List<WorkflowMessageEntity>> getContentMessage(@PathVariable String trackingId) {
		List<WorkflowMessageEntity> getContentMessage = approvalWorkflowService.getContentMessage(trackingId);
		return new ResponseEntity<>(getContentMessage, OK);
	}
	
	@PutMapping("/updateticket")
	public ResponseEntity<TicketWorkflowDetailsModel> updateWorkflowTicket(@RequestBody TicketWorkflowDetailsModel workflowTicket) {
		TicketWorkflowDetailsModel updateWorkflow = approvalWorkflowService.updateWorkflowTicket(workflowTicket);
		return new ResponseEntity<>(updateWorkflow, OK);
	}
	
	@GetMapping("/getticketlevelsstages/{trackingId}")
	public ResponseEntity<List<TicketWorkflowDetailsModel>> findTicketLevelsStages(@PathVariable String trackingId) {
		List<TicketWorkflowDetailsModel> ticketLevels = approvalWorkflowService.findTicketLevelsStages(trackingId);
		return new ResponseEntity<>(ticketLevels, OK);
	}
	
	@PutMapping("/updatetickets")
	public ResponseEntity<TicketWorkflowDetailsModel> updateWorkflowTickets(@RequestBody List<TicketWorkflowDetailsModel> workflowTickets) {
		TicketWorkflowDetailsModel updateWorkflow = approvalWorkflowService.updateWorkflowTickets(workflowTickets);
		return new ResponseEntity<>(updateWorkflow, OK);
	}
	
	@GetMapping("/allpendingapprovalsdash/{empId}/{impersonrole}")
	public ResponseEntity<List<TicketWorkflowDetailsModel>> findAllPendingApprovalsDash(@PathVariable String empId,@PathVariable String impersonrole) {
		List<TicketWorkflowDetailsModel> pendingApprovals = approvalWorkflowService.findAllPendingApprovalsDash(empId,impersonrole);
		return new ResponseEntity<>(pendingApprovals, OK);
	}
	
	@GetMapping("/getapprovalsdata/{empId}/{loggedinrole}/{stat}/{fdt}/{tdt}")
	public ResponseEntity<List<WorkflowMessageEntity>> getApprovalsData(@PathVariable String empId,@PathVariable String loggedinrole,@PathVariable String stat,@PathVariable String fdt,@PathVariable String tdt) {
		List<WorkflowMessageEntity> getContentMessage = approvalWorkflowService.getApprovalsData(empId,loggedinrole,stat,fdt,tdt);
		return new ResponseEntity<>(getContentMessage, OK);
	}
	
	@Scheduled(cron =  "0 0 18  * * *")
	public void performTaskUsingCron() {
		approvalWorkflowService.runCronJob();
	}
	
	/* This method is used to update multiple tickets */
	@PutMapping("/approverejectworkflow")
	public ResponseEntity<TicketWorkflowDetailsModel> approvRejectWorkflow(@RequestBody List<TicketWorkflowDetailsModel> ticketWorkflowDetailsModel) {
		TicketWorkflowDetailsModel approveFlow = approvalWorkflowService.approvRejectWorkflow(ticketWorkflowDetailsModel);
		return new ResponseEntity<>(approveFlow, OK);
	}

}
