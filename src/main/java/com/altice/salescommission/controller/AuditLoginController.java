package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.AuditLoginEntity;
import com.altice.salescommission.service.AuditLoginService;
import com.altice.salescommission.utility.Commons;

@RestController
@RequestMapping("/api/salescomm/audit/logins")
public class AuditLoginController {
	@Autowired
	AuditLoginService auditLoginService;

	@Autowired
	Commons comm;
	
	
	/* This method is used to save login details */
	@PostMapping("/addlogins")
	public ResponseEntity<AuditLoginEntity> addLogins(@RequestParam("network_id") String network_id,
			@RequestParam("email") String email,@RequestParam("employee_id") String employee_id,@RequestParam("created_by") String created_by) throws IOException {
		AuditLoginEntity auditLoginModel = auditLoginService.addLogins(network_id, email,employee_id,created_by);
		return new ResponseEntity<>(auditLoginModel, OK);
	}
	
	/* Get the list of logins based on employee id */
	@GetMapping("/getlogins/{employeeid}")
	public ResponseEntity<List<AuditLoginEntity>> getLogins(@PathVariable("employeeid") String employeeid) {
		List<AuditLoginEntity> getLoginsList = auditLoginService.getLogins(employeeid);
		return new ResponseEntity<>(getLoginsList, OK);
	}
}
