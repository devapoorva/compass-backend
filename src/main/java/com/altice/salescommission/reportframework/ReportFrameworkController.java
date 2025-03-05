package com.altice.salescommission.reportframework;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/reportframework/reportcreate/")
public class ReportFrameworkController {

	public static final String INSTANCE_DELETED_SUCCESSFULLY = "Instance deleted successfully";
	public static final String VALUE_DELETED_SUCCESSFULLY = "Employee/Role deleted successfully";

	@Autowired
	ReportFrameworkService reportFrameworkService;

	@Autowired
	Commons comm;

	/* Get SQL query to test */
	@PutMapping("/runreport")
	public ResponseEntity<List<LinkedHashMap<String, String>>> runReport(@RequestBody ReportFrameworkModel reportFrameworkModel) {
		List<LinkedHashMap<String, String>> reportList = reportFrameworkService.runReport(reportFrameworkModel);
		return new ResponseEntity<>(reportList, OK);
	}

	/* This method is used to create a new report */
	@PostMapping("/addReport")
	public ResponseEntity<ReportInstanceModel> addNewReport(@RequestParam("sqlquery") String sqlquery,
			@RequestParam("report_name") String report_name, @RequestParam("report_desc") String report_desc,
			@RequestParam("category") String category, @RequestParam("report_type") String report_type,
			@RequestParam("current_user") String currentUser, @RequestParam("distId") int distId,
			@RequestParam("instanceName") String instanceName, @RequestParam("sqlParams") String sqlParams, @RequestParam("sqlLables") String sqlLables)
			throws DuplicateRecordException {
		System.out.println("distId == " + distId);
		ReportInstanceModel reportModel = reportFrameworkService.addNewReport(sqlquery, report_name, report_desc,
				category, report_type, currentUser, distId, instanceName, sqlParams,sqlLables);
		return new ResponseEntity<>(reportModel, OK);
	}

	/* This method is used to create a new report */
	@PostMapping("/adddistribution")
	public ResponseEntity<ReportDistributionModel> addDistribution(@RequestParam("distName") String distName,
			@RequestParam("distType") String distType, @RequestParam("distValue") String distValue,
			@RequestParam("current_user") String currentUser) throws DuplicateRecordException {
		ReportDistributionModel distModel = reportFrameworkService.addDistribution(distName, distValue, currentUser,
				distType);
		return new ResponseEntity<>(distModel, OK);
	}

	/* This method is used to update report */
	@PostMapping("/updatereport")
	public ResponseEntity<ReportFrameworkModel> updateReport(@RequestParam("id") long id,
			@RequestParam("sqlquery") String sqlquery, @RequestParam("report_name") String report_name,
			@RequestParam("report_desc") String report_desc, @RequestParam("category") String category,
			@RequestParam("report_type") String report_type, @RequestParam("current_user") String currentUser,
			@RequestParam("status") String status) throws DuplicateRecordException, IdNotFoundException {
		ReportFrameworkModel reportModel = reportFrameworkService.updateReport(id, sqlquery, report_name, report_desc,
				category, report_type, currentUser, status);
		return new ResponseEntity<>(reportModel, OK);
	}

	/* This method is used to update instance */
	@PostMapping("/updateinstance")
	public ResponseEntity<ReportInstanceModel> updateInstance(@RequestParam("id") long id,
			@RequestParam("sqlParams") String sqlParams,@RequestParam("sqlLabels") String sqlLabels,
			@RequestParam("instanceName") String instanceName,
			@RequestParam("scheduleStr") String scheduleStr, @RequestParam("status") String status,
			@RequestParam("current_user") String currentUser, @RequestParam("distId") int distId)
			throws DuplicateRecordException, IdNotFoundException {
		ReportInstanceModel reportModel = reportFrameworkService.updateInstance(id, sqlParams, instanceName,
				scheduleStr, status, currentUser,distId,sqlLabels);
		return new ResponseEntity<>(reportModel, OK);
	}

	/* This method is used to update distribution */
	@PostMapping("/updatedistribution")
	public ResponseEntity<ReportDistributionModel> updateDistribution(@RequestParam("distId") int distId,
			@RequestParam("distName") String distName, @RequestParam("distType") String distType,
			@RequestParam("distValue") String distValue, @RequestParam("current_user") String currentUser)
			throws DuplicateRecordException, IdNotFoundException {
		ReportDistributionModel reportDistributionModel = reportFrameworkService.updateDistribution(distId, distName,
				distValue, currentUser, distType);
		return new ResponseEntity<>(reportDistributionModel, OK);
	}

	/* This method is used to update schedule details */
	@PostMapping("/updateschedule")
	public ResponseEntity<ReportInstanceModel> updateSchedule(@RequestParam("id") long id,
			@RequestParam("minute") String minute, @RequestParam("hour") String hour, @RequestParam("day") String day,
			@RequestParam("month") String month, @RequestParam("week") String week,
			@RequestParam("current_user") String currentUser) throws DuplicateRecordException, IdNotFoundException {
		ReportInstanceModel reportModel = reportFrameworkService.updateSchedule(id, minute, hour, day, month, week,
				currentUser);
		return new ResponseEntity<>(reportModel, OK);
	}

	/* This method is used to update schedule details */
	@PostMapping("/createinstance")
	public ResponseEntity<ReportInstanceModel> createInstance(@RequestParam("id") long id,
			@RequestParam("distId") int distId, @RequestParam("sqlParams") String sqlParams,
			@RequestParam("current_user") String currentUser, @RequestParam("instanceName") String instanceName)
			throws DuplicateRecordException, IdNotFoundException {
		ReportInstanceModel reportModel = reportFrameworkService.createInstance(id, distId, sqlParams, currentUser,
				instanceName);
		return new ResponseEntity<>(reportModel, OK);
	}

	/* Get all the reports */
	@GetMapping("/getreports")
	public ResponseEntity<List<ReportFrameworkModel>> getReports() {
		List<ReportFrameworkModel> reportsList = reportFrameworkService.getReports();
		return new ResponseEntity<>(reportsList, OK);
	}

	/* Get all the distribution lists */
	@GetMapping("/getdistributionlist")
	public ResponseEntity<List<ReportDistributionModel>> getDistributionList() {
		List<ReportDistributionModel> distList = reportFrameworkService.getDistributionList();
		return new ResponseEntity<>(distList, OK);
	}

	/* Get all the categories */
	@GetMapping("/getcategorylist")
	public ResponseEntity<List<ReportFrameworkModel>> getCategoryList() {
		List<ReportFrameworkModel> catList = reportFrameworkService.getCategoryList();
		return new ResponseEntity<>(catList, OK);
	}

	/* Get all the categories */
	@GetMapping("/getdistributiondropdown")
	public ResponseEntity<List<ReportDistributionModel>> getDistributionDropdown() {
		List<ReportDistributionModel> distDropdownList = reportFrameworkService.getDistributionDropdown();
		return new ResponseEntity<>(distDropdownList, OK);
	}

	/* Get all the employees */
	@GetMapping("/getemployees/{filter}")
	public ResponseEntity<List<ReportDistributionModel>> getEmployees(@PathVariable("filter") String filter) {
		List<ReportDistributionModel> employeesList = reportFrameworkService.getEmployees(filter);
		return new ResponseEntity<>(employeesList, OK);
	}

	/* Get all the roles */
	@GetMapping("/getroles")
	public ResponseEntity<List<ReportDistributionModel>> getRoles() {
		List<ReportDistributionModel> rolesList = reportFrameworkService.getRoles();
		return new ResponseEntity<>(rolesList, OK);
	}

	/* This method is used to delete distribution */
	@PutMapping("/deletedistribution")
	public ResponseEntity<ReportDistributionModel> deleteDistribution(
			@RequestBody List<ReportDistributionModel> reportDistributionModel) {
		ReportDistributionModel reportDistModel = reportFrameworkService.deleteDistribution(reportDistributionModel);
		return new ResponseEntity<>(reportDistModel, OK);
	}

	/* This method is used to delete report */
	@PutMapping("/deletereport")
	public ResponseEntity<ReportFrameworkModel> deleteReport(@RequestBody List<ReportFrameworkModel> repModel) {
		ReportFrameworkModel reportFrameworkModel = reportFrameworkService.deleteReport(repModel);
		return new ResponseEntity<>(reportFrameworkModel, OK);
	}

	/*
	 * This method is used to get all the report instances based on provided report
	 * id
	 */
	@GetMapping("/getinstances/{repid}")
	public ResponseEntity<List<ReportInstanceModel>> getInstances(@PathVariable("repid") int repid) {
		List<ReportInstanceModel> instanceslist = reportFrameworkService.getInstances(repid);
		return new ResponseEntity<>(instanceslist, OK);
	}

	/*
	 * This method is used to get all the distribution values based on provided
	 * distribution id
	 */
	@GetMapping("/getdistvalues/{distId}/{distType}")
	public ResponseEntity<List<ReportDistributionModel>> getDistValues(@PathVariable("distId") int distId,
			@PathVariable("distType") String distType) {
		List<ReportDistributionModel> distributionValueslist = reportFrameworkService.getDistValues(distId, distType);
		return new ResponseEntity<>(distributionValueslist, OK);
	}

	/* This method is used to delete a instance */
	@DeleteMapping("/deleteinstance/{id}")
	public ResponseEntity<HttpResponse> deleteInstance(@PathVariable("id") Long id) throws IOException {
		reportFrameworkService.deleteInstance(id);
		return comm.response(OK, INSTANCE_DELETED_SUCCESSFULLY);
	}

	/* This method is used to delete a instance */
	@DeleteMapping("/deletevalue/{id}")
	public ResponseEntity<HttpResponse> deleteValue(@PathVariable("id") Long id) throws IOException {
		reportFrameworkService.deleteValue(id);
		return comm.response(OK, VALUE_DELETED_SUCCESSFULLY);
	}

}
