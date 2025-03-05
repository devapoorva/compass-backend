package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.EmployeeMasterEntity;
import com.altice.salescommission.service.ViewLoginsActivityService;
import com.altice.salescommission.utility.Commons;

@RestController
@RequestMapping("/api/salescomm/personnel/viewloginsactivity")
public class ViewLoginsActivityController {
	@Autowired
	ViewLoginsActivityService viewLoginsActivityService;

	@Autowired
	Commons comm;
	
	/* This method is used to get all the active KPI's */
	@GetMapping("/getloginsactivitylist/{first_name}")
	public ResponseEntity<List<EmployeeMasterEntity>> getLoginsActivityList(@RequestParam("first_name") String first_name) {
		List<EmployeeMasterEntity> getLoginsActivityList = viewLoginsActivityService.getLoginsActivityList(first_name);
		return new ResponseEntity<>(getLoginsActivityList, OK);
	}
}
