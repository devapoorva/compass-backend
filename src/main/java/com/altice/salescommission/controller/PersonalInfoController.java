package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.CalendarTypeEntity;
import com.altice.salescommission.entity.CommissionPlanEntity;
import com.altice.salescommission.entity.EmployeeMasterEntity;
import com.altice.salescommission.entity.FavPageEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.model.KpiModel;
import com.altice.salescommission.model.UserModel;
import com.altice.salescommission.model.personalReportModel;
import com.altice.salescommission.service.PersonalInfoService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/personal")
public class PersonalInfoController {

	@Autowired
	private PersonalInfoService personalInfoService;

	@Autowired
	Commons comm;

	@GetMapping("/validatecredentials/{loggedInEmployeeId}/{loggedInNetworkId}/{loggedInEmail}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<EmployeeMasterEntity>> validateCredentials(@PathVariable String loggedInEmployeeId,
			@PathVariable String loggedInNetworkId, @PathVariable String loggedInEmail) throws ParseException {
		List<EmployeeMasterEntity> users = personalInfoService.validateCredentials(loggedInEmployeeId,
				loggedInNetworkId, loggedInEmail);
		return new ResponseEntity<>(users, OK);
	}
	
	/* This method is used to get all the payroll calendars */
	@GetMapping("/getpayrollcalendarlist")
	public ResponseEntity<List<CalendarTypeEntity>> getPayrollCalendarList() {
		List<CalendarTypeEntity> getPayrollCalendarList = personalInfoService.getPayrollCalendarList();
		return new ResponseEntity<>(getPayrollCalendarList, OK);
	}

	@GetMapping("/getuserfields")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<EmployeeMasterEntity>> getUserFields() {
		List<EmployeeMasterEntity> users = personalInfoService.getUserFields();
		return new ResponseEntity<>(users, OK);
	}

	/* Get the effective dates */
	@GetMapping("/getuserasofdate/{employeedID}")
	public ResponseEntity<List<UserModel>> getAsOfDate(@PathVariable String employeedID) {
		List<UserModel> plan = personalInfoService.getAsOfDate(employeedID);
		return new ResponseEntity<>(plan, OK);
	}

	/* Get the commPlan from comm_plan_master table by the as of date */
	@GetMapping("/getuserbyasofdate/{employeedID}/{effective_date}")
	public ResponseEntity<List<UserModel>> getUserByAsOfDdate(@PathVariable String employeedID,
			@PathVariable String effective_date) throws ParseException {
		List<UserModel> plan = personalInfoService.getUserByAsOfDdate(employeedID, effective_date);
		return new ResponseEntity<>(plan, OK);
	}

	@GetMapping("/getusers/{profilestatus}/{first_name}/{last_name}/{employeeId}/{id}/{supervisor_id}/{user_type}/{sales_rep_channel}/{comm_plan_id}/{payroll_start_date}/{userRole}")
	public ResponseEntity<List<UserModel>> getUsers(@PathVariable String profilestatus, @PathVariable String first_name,
			@PathVariable String last_name, @PathVariable String employeeId, @PathVariable String id,
			@PathVariable String supervisor_id, @PathVariable String user_type, @PathVariable String sales_rep_channel,
			@PathVariable int comm_plan_id, @PathVariable String payroll_start_date, @PathVariable String userRole)
			throws ParseException {

		List<UserModel> users = personalInfoService.getUsersReport(profilestatus, first_name, last_name, employeeId, id,
				supervisor_id, user_type, sales_rep_channel, comm_plan_id, payroll_start_date, userRole);
		return new ResponseEntity<>(users, OK);
	}

	@GetMapping("/getuserspending/{profilestatus}/{first_name}/{last_name}/{employeeId}/{id}/{userRole}")
	public ResponseEntity<List<UserModel>> getUsersPending(@PathVariable String profilestatus,
			@PathVariable String first_name, @PathVariable String last_name, @PathVariable String employeeId,
			@PathVariable String id, @PathVariable String userRole) throws ParseException {

		List<UserModel> users = personalInfoService.getUsersPending(profilestatus, first_name, last_name, employeeId,
				id, userRole);
		return new ResponseEntity<>(users, OK);
	}

	@GetMapping("/getusersreport/{profilestatus}/{first_name}/{last_name}/{employeeId}/{id}/{supervisor_id}/{user_type}/{sales_rep_channel}/{comm_plan_id}/{payroll_start_date}/{userRole}")
	public ResponseEntity<List<UserModel>> getUsersSearchReport(@PathVariable String profilestatus,
			@PathVariable String first_name, @PathVariable String last_name, @PathVariable String employeeId,
			@PathVariable String id, @PathVariable String supervisor_id, @PathVariable String user_type,
			@PathVariable String sales_rep_channel, @PathVariable int comm_plan_id,
			@PathVariable String payroll_start_date, @PathVariable String userRole) throws ParseException {

		List<UserModel> users = personalInfoService.getUsersSearchReport(profilestatus, first_name, last_name,
				employeeId, id, supervisor_id, user_type, sales_rep_channel, comm_plan_id, payroll_start_date,
				userRole);
		return new ResponseEntity<>(users, OK);
	}

	@GetMapping("/downloadpersonnelreport")
	public ResponseEntity<List<personalReportModel>> downloadPersonnelReport() throws ParseException {

		List<personalReportModel> users = personalInfoService.downloadPersonnelReport();
		return new ResponseEntity<>(users, OK);
	}

	@GetMapping("/getuserinfo/{profilestatus}/{payroll_end_date}")
	public ResponseEntity<List<UserModel>> getUserInfo(@PathVariable String profilestatus,
			@PathVariable String payroll_end_date) throws ParseException {
		List<UserModel> users = personalInfoService.getUserInfo(profilestatus, payroll_end_date);
		return new ResponseEntity<>(users, OK);
	}

	@GetMapping("/getprofiles/{profilestatus}/{payroll_end_date}")
	public ResponseEntity<List<EmployeeMasterEntity>> getProfiles(@PathVariable String profilestatus,
			@PathVariable String payroll_end_date) throws ParseException {
		List<EmployeeMasterEntity> getprofiles = personalInfoService.getProfiles(profilestatus, payroll_end_date);
		return new ResponseEntity<>(getprofiles, OK);
	}

	@GetMapping("/getuserinfobydate/{effective_date}")
	public ResponseEntity<List<EmployeeMasterEntity>> getUserInfoByDate(@PathVariable String effective_date) {
		List<EmployeeMasterEntity> users = personalInfoService.getUserInfoByDate(effective_date);
		return new ResponseEntity<>(users, OK);
	}

	@GetMapping("/pendingprofiles")
	public ResponseEntity<List<EmployeeMasterEntity>> getPendingUsers() {
		List<EmployeeMasterEntity> users = personalInfoService.getPendingUsers();
		return new ResponseEntity<>(users, OK);
	}

	@PutMapping("/updateuserprofile")
	public ResponseEntity<EmployeeMasterEntity> updateUserProfile(@RequestBody EmployeeMasterEntity userModel) {
		//userModel.setProfileStatus("complete");
		EmployeeMasterEntity user = personalInfoService.updateUserProfile(userModel);
		return new ResponseEntity<>(user, OK);
	}

	@PostMapping("/bulkupdateuserprofiles")
	public ResponseEntity<EmployeeMasterEntity> bulkUpdateUserProfile(
			@RequestBody List<EmployeeMasterEntity> userModels) throws DuplicateRecordException {
		EmployeeMasterEntity user = personalInfoService.saveUserProfile(userModels);
		return new ResponseEntity<>(user, OK);
	}
	
	@PostMapping("/saveadmin")
	public ResponseEntity<EmployeeMasterEntity> saveAdmin(
			@RequestBody List<EmployeeMasterEntity> userModels) throws DuplicateRecordException {
		EmployeeMasterEntity user = personalInfoService.saveAdmin(userModels);
		return new ResponseEntity<>(user, OK);
	}

	@PostMapping("/deleteUser")
	public ResponseEntity<EmployeeMasterEntity> deleteUser(@RequestBody List<EmployeeMasterEntity> userModels) {
		EmployeeMasterEntity user = personalInfoService.deleteUser(userModels);
		return new ResponseEntity<>(user, OK);
	}

	@GetMapping("/filterbyempid/{empid}")
	public ResponseEntity<List<EmployeeMasterEntity>> filterByEmpId(@PathVariable String empid) {
		List<EmployeeMasterEntity> users = personalInfoService.filterByEmpId(empid);
		return new ResponseEntity<>(users, OK);
	}
	
	@GetMapping("/filterbyempidhome/{empid}/{effective_date}")
	public ResponseEntity<List<EmployeeMasterEntity>> filterByEmpIdHome(@PathVariable String empid,@PathVariable String effective_date) {
		List<EmployeeMasterEntity> users = personalInfoService.filterByEmpIdHome(empid,effective_date);
		return new ResponseEntity<>(users, OK);
	}
	
	@GetMapping("/getmultiprofilecount/{empid}")
	public int getMultiProfileCount(@PathVariable String empid) {
		int cnt = personalInfoService.getMultiProfileCount(empid);
		return cnt;
	}
	
	@GetMapping("/filterbyempidbyrole/{empid}/{role}")
	public ResponseEntity<List<EmployeeMasterEntity>> filterByEmpId(@PathVariable String empid,@PathVariable String role) {
		List<EmployeeMasterEntity> users = personalInfoService.filterByEmpIdByRole(empid,role);
		return new ResponseEntity<>(users, OK);
	}

	/*
	 * Adding subMenus in to Favourites
	 */
	@PostMapping("/addpagetofav")
	public ResponseEntity<FavPageEntity> addPageToFav(@RequestBody FavPageEntity favPageModel) {
		FavPageEntity favPage = personalInfoService.addPageToFav(favPageModel);
		return new ResponseEntity<>(favPage, OK);
	}

	/* Delete assigned Favourite page to the login employee */
	@DeleteMapping("/deletefavpage/{id}/{empId}")
	public ResponseEntity<HttpResponse> deleteFavPage(@PathVariable("id") Long id, @PathVariable("empId") String empId)
			throws IOException {
		personalInfoService.deleteFavPage(id, empId);
		return comm.response(OK, "Page Deleted Sucessfully");
	}

	@PostMapping("/bulkupdate")
	public ResponseEntity<EmployeeMasterEntity> bulkUpdate(@RequestBody List<EmployeeMasterEntity> userModels) {
		EmployeeMasterEntity user = personalInfoService.bulkUpdate(userModels);
		return new ResponseEntity<>(user, OK);
	}
}
