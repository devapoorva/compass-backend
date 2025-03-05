package com.altice.salescommission.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.altice.salescommission.entity.CalendarTypeEntity;
import com.altice.salescommission.entity.CommissionPlanEntity;
import com.altice.salescommission.entity.EmployeeMasterEntity;
import com.altice.salescommission.entity.FavPageEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.model.KpiModel;
import com.altice.salescommission.model.UserModel;
import com.altice.salescommission.model.personalReportModel;

public interface PersonalInfoService {
	
	List<UserModel> getAsOfDate(String employeedID);
	
	List<UserModel> getUserByAsOfDdate(String employeedID, String effective_date) throws ParseException;
	
	List<EmployeeMasterEntity> getUserFields();

	List<UserModel> getUsers(String profilestatus, String first_name, String last_name,
			String employeeId, String id, String supervisor_id, String user_type,
			String sales_rep_channel, int comm_plan_id, String payroll_start_date, String payroll_end_date,String userRole)
			throws ParseException;
	
	List<UserModel> getUsersReport(String profilestatus, String first_name, String last_name,
			String employeeId, String id, String supervisor_id, String user_type,
			String sales_rep_channel, int comm_plan_id, String payroll_start_date, String userRole)
			throws ParseException;
	
	List<UserModel> getUsersSearchReport(String profilestatus, String first_name, String last_name,
			String employeeId, String id, String supervisor_id, String user_type,
			String sales_rep_channel, int comm_plan_id, String payroll_start_date, String userRole)
			throws ParseException;
	
	
	List<UserModel> getUsersPending(String profilestatus, String first_name, String last_name,
			String employeeId, String id, String userRole)
			throws ParseException;
	
	List<CalendarTypeEntity> getPayrollCalendarList();
	
	List<personalReportModel> downloadPersonnelReport() throws ParseException;

	List<UserModel> getUserInfo(String profilestatus, String payroll_end_date) throws ParseException;
	
	List<EmployeeMasterEntity> validateCredentials(String loggedInEmployeeId,String loggedInNetworkId,String loggedInEmail) throws ParseException;

	List<EmployeeMasterEntity> getProfiles(String profilestatus, String payroll_end_date) throws ParseException;

	List<EmployeeMasterEntity> getUserInfoByDate(String effective_date);

	List<EmployeeMasterEntity> getPendingUsers();

	EmployeeMasterEntity updateUserProfile(EmployeeMasterEntity userModel);

	EmployeeMasterEntity saveUserProfile(List<EmployeeMasterEntity> userModels) throws DuplicateRecordException;
	
	EmployeeMasterEntity saveAdmin(List<EmployeeMasterEntity> userModels) throws DuplicateRecordException;
	
	EmployeeMasterEntity bulkUpdate(List<EmployeeMasterEntity> userModels);

	EmployeeMasterEntity deleteUser(List<EmployeeMasterEntity> userModels);

	List<EmployeeMasterEntity> filterByEmpId(String empid);
	
	List<EmployeeMasterEntity> filterByEmpIdHome(String empid,String effective_date);
	
	int getMultiProfileCount(String empid);
	
	List<EmployeeMasterEntity> filterByEmpIdByRole(String empid,String role);

	FavPageEntity addPageToFav(FavPageEntity favPageModel);

	int deleteFavPage(Long id, String empId);

}
