package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.CalendarEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.model.PayrollReportModel;
import com.altice.salescommission.service.CalendarService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/config/calendar")
public class CalendarController {

	private static final Logger logger = LoggerFactory.getLogger(CalendarController.class.getName());

	private static final String commissionPlanService = null;
	@Autowired
	CalendarService calendarService;

	@Autowired
	Commons commons;

	/* This method is used to get all the cal run ids */
	@GetMapping("/getcalrunidsdropdown")
	public ResponseEntity<List<CalendarEntity>> getCalRunIdsDropdown() {
		List<CalendarEntity> calRunIdList = calendarService.getCalRunIdsDropdown();
		return new ResponseEntity<>(calRunIdList, OK);
	}

	/* This method is used to add new payroll calendar */
	@PostMapping("/addpayrollcalendar")
	public ResponseEntity<HttpResponse> addPayrollCalendar(@RequestParam("valid_from_dt") Date valid_from_dt,
			@RequestParam("valid_to_dt") Date valid_to_dt, @RequestParam("payroll_due_dt") Date payroll_due_dt,
			@RequestParam("pay_dt") Date pay_dt, @RequestParam("unlock") String unlock,
			@RequestParam("off_cycle") String off_cycle,@RequestParam("issalesrepaccess") String isSalesRepAccess, @RequestParam("cal_run_id") int cal_run_id,
			@RequestParam("calendar_type") String calendar_type, @RequestParam("current_user") String current_user)
			throws DuplicateRecordException {

		try {
			int result = calendarService.addPayrollCalendar(valid_from_dt, valid_to_dt, payroll_due_dt, pay_dt, unlock,
					off_cycle, isSalesRepAccess,cal_run_id, calendar_type, current_user);
			logger.error("result: " + result);
			if (result == 0) {
				return commons.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert");
			}
			// else if (result == 2) {
			// return commons.response(HttpStatus.INTERNAL_SERVER_ERROR, "Cal Run ID already
			// exists");
			// }
			return commons.response(OK, "Inserted Successfully");
		} catch (Exception exception) {
			logger.error("Error: " + exception);
			return commons.response(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
		}
	}

	/* Get the list of calendar table data */
	@GetMapping("/getcalendardata")
	public ResponseEntity<List<CalendarEntity>> getCalendarData() {
		List<CalendarEntity> getCalendarList = calendarService.getCalendarData();
		return new ResponseEntity<>(getCalendarList, OK);
	}
	
	/* Get the list of user roles data */
	@GetMapping("/getuserroles")
	public ResponseEntity<List<CalendarEntity>> getUserRoles() {
		List<CalendarEntity> getCalendarList = calendarService.getUserRoles();
		return new ResponseEntity<>(getCalendarList, OK);
	}

	/* Get the list of records based on calendar_type */
	@GetMapping("/getcalc/{calendar_type}")
	public ResponseEntity<List<CalendarEntity>> getCalList(@PathVariable("calendar_type") String calendar_type) {
		List<CalendarEntity> getCalList = calendarService.getCalList(calendar_type);
		return new ResponseEntity<>(getCalList, OK);
	}

	@PutMapping("/updatecalc")
	public ResponseEntity<CalendarEntity> updateCalcs(@RequestBody List<CalendarEntity> calendarModel)
			throws IOException {
		CalendarEntity calList = calendarService.updateCalcs(calendarModel);
		return new ResponseEntity<>(calList, OK);
	}

	/* Get the list of calendarTypes */
	@GetMapping("/getcalendartypes")
	public ResponseEntity<List<CalendarEntity>> getCalendarTypes() {
		List<CalendarEntity> getCalendarList = calendarService.getCalendarTypes();
		return new ResponseEntity<>(getCalendarList, OK);
	}

	/* Get the list of calendar commPeriods */
	@GetMapping("/getcommperiodvalues/{calType}")
	public ResponseEntity<List<CalendarEntity>> getCommPeriodValues(@PathVariable("calType") String calType) {
		List<CalendarEntity> getCalendarList = calendarService.getCommPeriodValues(calType);
		return new ResponseEntity<>(getCalendarList, OK);
	}

	/* Get the list of calendar commPeriods by unlock */
	@GetMapping("/getcommperiodvalbyunlock/{calType}")
	public ResponseEntity<List<CalendarEntity>> getCommPeriodValByUnlock(@PathVariable("calType") String calType) {
		List<CalendarEntity> getCalendarList = calendarService.getCommPeriodValUnlock(calType);
		return new ResponseEntity<>(getCalendarList, OK);
	}

	/* Get the list of Payroll Data */
	@GetMapping("/getpayrollreport/{calRunId}")
	public ResponseEntity<List<PayrollReportModel>> getPayrollReportByCalrun(
			@PathVariable("calRunId") String calRunId) {
		List<PayrollReportModel> payrollReportList = calendarService.getPayrollReportByCalrun(calRunId);
		return new ResponseEntity<>(payrollReportList, OK);
	}

}
