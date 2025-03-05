package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.CalRunIdsEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.service.CalRunIdsService;

@RestController
@RequestMapping("/api/salescomm/config/calrunids")
public class CalRunIdsController {
	@Autowired
	CalRunIdsService calRunIdsService;
	
	/* This method is used to create a new KPI */
	@PostMapping("/addcalrunid")
	public ResponseEntity<CalRunIdsEntity> addCalRunId(@RequestParam("cal_run_id") int cal_run_id,@RequestParam("description") String description,
			@RequestParam("currentUser") String currentUser) throws DuplicateRecordException {
		System.out.println("cal_run_id = "+cal_run_id);
		System.out.println("description = "+description);
		System.out.println("currentUser = "+currentUser);
		CalRunIdsEntity calendarTypeEntity = calRunIdsService.addCalRunId(cal_run_id,description,currentUser);
		return new ResponseEntity<>(calendarTypeEntity, OK);
	}
	
	/* This method is used to create a new KPI */
	@PostMapping("/updatecalrunid")
	public ResponseEntity<CalRunIdsEntity> updateCalRunId(@RequestParam("id") long id,@RequestParam("description") String description,
			@RequestParam("currentUser") String currentUser) throws DuplicateRecordException, IdNotFoundException {
		CalRunIdsEntity calendarTypeEntity = calRunIdsService.updateCalRunId(id,description,currentUser);
		return new ResponseEntity<>(calendarTypeEntity, OK);
	}
	
	/* Get the list of calendars */
	@GetMapping("/getcalrunidlist")
	public ResponseEntity<List<CalRunIdsEntity>> getCalRunIdList() {
		List<CalRunIdsEntity> getCalendarList = calRunIdsService.getCalRunIdList();
		return new ResponseEntity<>(getCalendarList, OK);
	}
	
	/* Get the process status */
	@GetMapping("/getprocessstatus/{userId}/{runCtrlId}/{startTime}/{endTime}")
	public ResponseEntity<List<CalRunIdsEntity>> getProcess(@PathVariable("userId") String userId,
			@PathVariable("runCtrlId") int runCtrlId,@PathVariable("startTime") String startTime,@PathVariable("endTime") String endTime) {
		
		List<CalRunIdsEntity> getCalendarList = calRunIdsService.getProcess(userId,runCtrlId,startTime,endTime);
		return new ResponseEntity<>(getCalendarList, OK);
	}
	
	/* Get the list of user names */
	@GetMapping("/getuseridsdropdown")
	public ResponseEntity<List<CalRunIdsEntity>> getUserIdsDropDown() {
		List<CalRunIdsEntity> getUserNamesList = calRunIdsService.getUserIdsDropDown();
		return new ResponseEntity<>(getUserNamesList, OK);
	}
	
	/* Get the list of run controls */
	@GetMapping("/getruncontrolsdropdown")
	public ResponseEntity<List<CalRunIdsEntity>> getRunCtrlsDropDown() {
		List<CalRunIdsEntity> getRunCtrlsList = calRunIdsService.getRunCtrlsDropDown();
		return new ResponseEntity<>(getRunCtrlsList, OK);
	}
}
