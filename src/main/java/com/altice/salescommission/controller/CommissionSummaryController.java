package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.CommissionSummaryEntity;
import com.altice.salescommission.entity.UploadJsonEntity;
import com.altice.salescommission.entity.WorkflowMessageEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.model.KpiModel;
import com.altice.salescommission.model.UploadJsonHeadersModel;
import com.altice.salescommission.service.CommissionSummaryService;

@RestController
@RequestMapping("/api/salescomm/commissionsummary")
public class CommissionSummaryController {

	//private static final Logger logger = LoggerFactory.getLogger(CommissionSummaryController.class.getName());

	@Autowired
	CommissionSummaryService commissionSummaryService;

	@GetMapping("/getreport/{reporttype}/{commPlanId}/{calRunId}/{payrollDt}")
	public List<Object> streamObjects(@PathVariable String reporttype, @PathVariable String commPlanId,
			@PathVariable String calRunId, @PathVariable String payrollDt)
			throws NumberFormatException, ParseException {
		return commissionSummaryService.getStreamReports(reporttype, commPlanId, calRunId, payrollDt);
	}

//	@GetMapping("/getreport/{reporttype}/{commPlanId}/{calRunId}/{payrollDt}")
//	public ResponseEntity<List<LinkedHashMap<String, String>>> getReport(@PathVariable String reporttype,
//			@PathVariable String commPlanId, @PathVariable String calRunId, @PathVariable String payrollDt) {
//		List<LinkedHashMap<String, String>> getPageData = commissionSummaryService.getReport(reporttype, commPlanId,
//				calRunId, payrollDt);
//		logger.info("getPageData = " + getPageData);
//		return new ResponseEntity<>(getPageData, OK);
//	}

	@GetMapping("/getkpicrosstab")
	public ResponseEntity<List<Map<String, Object>>> getCrosstab() {
		List<Map<String, Object>> getData = commissionSummaryService.getCrosstab();
		return new ResponseEntity<>(getData, OK);
	}

	/* This method is used to get all the active KPI's */
	@GetMapping("/getkpiactivelist/{commplanid}/{effdate}")
	public ResponseEntity<List<KpiModel>> getKpiActiveList(@PathVariable int commplanid, @PathVariable String effdate) {
		List<KpiModel> kpiActiveList = commissionSummaryService.getKpiActiveList(commplanid, effdate);
		return new ResponseEntity<>(kpiActiveList, OK);
	}

	/* Get the list of selected trackids */
	@GetMapping("/gettrackidcount/{trackid}")
	public ResponseEntity<List<WorkflowMessageEntity>> getTrackidCount(@PathVariable String trackid) {
		List<WorkflowMessageEntity> getTrackidList = commissionSummaryService.getTrackidCount(trackid);
		return new ResponseEntity<>(getTrackidList, OK);
	}

	/*
	 * Insert Dispute Message
	 */
	@PostMapping("/insertdispute")
	public ResponseEntity<WorkflowMessageEntity> insertDispute(@RequestBody WorkflowMessageEntity workflowMessageModel)
			throws DuplicateRecordException, IdNotFoundException, ParseException {

		WorkflowMessageEntity insertDispute = commissionSummaryService.insertDispute(workflowMessageModel);
		return new ResponseEntity<>(insertDispute, OK);
	}

	/*
	 * Insert Dispute Message
	 */
	@PostMapping("/insertdisputestatic")
	public ResponseEntity<WorkflowMessageEntity> insertDisputeStatic(
			@RequestBody WorkflowMessageEntity workflowMessageModel)
			throws DuplicateRecordException, IdNotFoundException, ParseException {

		WorkflowMessageEntity insertDisputeStatic = commissionSummaryService.insertDisputeStatic(workflowMessageModel);
		return new ResponseEntity<>(insertDisputeStatic, OK);
	}

	/* Get the list of results based on sql and parms */
	@GetMapping("/runsql/{param}/{pageName}/{gridName}/{commplanid}/{selectedSalesChannel}/{usertype}")
	public ResponseEntity<List<LinkedHashMap<String, Object>>> runReport(@PathVariable String param,
			@PathVariable String pageName, @PathVariable String gridName, @PathVariable int commplanid,
			@PathVariable String selectedSalesChannel, @PathVariable String usertype) {
		List<LinkedHashMap<String, Object>> getPageData = commissionSummaryService.runReport(param, pageName, gridName,
				commplanid, selectedSalesChannel, usertype);
		return new ResponseEntity<>(getPageData, OK);
	}

	/* Get the list of results based on sql and parms */
	@GetMapping("/runreportsearch/{param}/{pageName}/{gridName}/{commplanid}")
	public ResponseEntity<List<LinkedHashMap<String, String>>> runReportSearch(@PathVariable String param,
			@PathVariable String pageName, @PathVariable String gridName, @PathVariable int commplanid) {
		List<LinkedHashMap<String, String>> getPageData = commissionSummaryService.runReportSearch(param, pageName,
				gridName, commplanid);
		return new ResponseEntity<>(getPageData, OK);
	}

	/* Get the list of calendars */
	@GetMapping("/getcommsumdropdowns")
	public ResponseEntity<List<CommissionSummaryEntity>> getCommSumDropDowns() {
		List<CommissionSummaryEntity> getCommSumDropDownsList = commissionSummaryService.getCommSumDropDowns();
		return new ResponseEntity<>(getCommSumDropDownsList, OK);
	}

	/* Get the list of supervisor calendars */
	@GetMapping("/getcommsumsupdropdowns/{empid}")
	public ResponseEntity<List<CommissionSummaryEntity>> getCommSumSupDropDowns(@PathVariable String empid) {
		List<CommissionSummaryEntity> getCommSumDropDownsList = commissionSummaryService.getCommSumSupDropDowns(empid);
		return new ResponseEntity<>(getCommSumDropDownsList, OK);
	}

	/* Get the list of User calendars */
	@GetMapping("/getcommsumuserdropdowns/{empid}")
	public ResponseEntity<List<CommissionSummaryEntity>> getCommSumUserDropDowns(@PathVariable String empid) {
		List<CommissionSummaryEntity> getCommSumDropDownsList = commissionSummaryService.getCommSumUserDropDowns(empid);
		return new ResponseEntity<>(getCommSumDropDownsList, OK);
	}

	/* Get the list of header groups */
	@GetMapping("/getheadergroups")
	public ResponseEntity<List<CommissionSummaryEntity>> getHeaderGroups() {
		List<CommissionSummaryEntity> getHeaderGroupsList = commissionSummaryService.getHeaderGroups();
		return new ResponseEntity<>(getHeaderGroupsList, OK);
	}

	/* Get the list of calendars */
	@GetMapping("/getsqlqueries")
	public ResponseEntity<List<CommissionSummaryEntity>> getSqlQueries() {
		List<CommissionSummaryEntity> getSqlQueriesList = commissionSummaryService.getSqlQueries();
		return new ResponseEntity<>(getSqlQueriesList, OK);
	}

	/* Get the list of sql and json based on pageName */
	@GetMapping("/getdatabypage/{pageName}/{commplanid}/{supervisorStatus}/{selectedSalesChannel}")
	public ResponseEntity<List<UploadJsonEntity>> getDataByPageName(@PathVariable String pageName,
			@PathVariable int commplanid, @PathVariable int supervisorStatus,
			@PathVariable String selectedSalesChannel) {
		List<UploadJsonEntity> getPageData = commissionSummaryService.getDataByPageName(pageName, commplanid,
				supervisorStatus, selectedSalesChannel);
		return new ResponseEntity<>(getPageData, OK);
	}

	/* Get the list of headers */
	@GetMapping("/getheaders/{id}/{salesChannel}/{supervisorStatus}")
	public ResponseEntity<List<UploadJsonHeadersModel>> getHeaders(@PathVariable int id,
			@PathVariable String salesChannel, @PathVariable int supervisorStatus) {
		List<UploadJsonHeadersModel> getPageData = commissionSummaryService.getHeaders(id, salesChannel,
				supervisorStatus);
		return new ResponseEntity<>(getPageData, OK);
	}

	/* Get the list of headers */
	@GetMapping("/getbuttons/{id}")
	public ResponseEntity<List<UploadJsonHeadersModel>> getButtons(@PathVariable int id) {
		List<UploadJsonHeadersModel> getPageData = commissionSummaryService.getButtons(id);
		return new ResponseEntity<>(getPageData, OK);
	}

	/* Get supervisor status */
	@GetMapping("/getsupervisorstatus/{id}")
	public ResponseEntity<List<UploadJsonHeadersModel>> getSupervisorStatus(@PathVariable int id) {
		List<UploadJsonHeadersModel> getSupervisorStatus = commissionSummaryService.getSupervisorStatus(id);
		return new ResponseEntity<>(getSupervisorStatus, OK);
	}

	/* Get status of calc */
	@GetMapping("/getcalcrunningstatus/{id}/{calrunid}")
	public ResponseEntity<List<UploadJsonHeadersModel>> getCalcRunningStatus(@PathVariable int id,
			@PathVariable int calrunid) {
		List<UploadJsonHeadersModel> getCalcRunningStatus = commissionSummaryService.getCalcRunningStatus(id, calrunid);
		return new ResponseEntity<>(getCalcRunningStatus, OK);
	}

	/* Get page name */
	@GetMapping("/getpagename/{commplanid}/{kpiid}")
	public ResponseEntity<List<UploadJsonHeadersModel>> getPageName(@PathVariable int commplanid,
			@PathVariable int kpiid) {
		List<UploadJsonHeadersModel> getPageName = commissionSummaryService.getPageName(commplanid, kpiid);
		return new ResponseEntity<>(getPageName, OK);
	}

}