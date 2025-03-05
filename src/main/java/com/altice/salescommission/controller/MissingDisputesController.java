package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.MissingDisputesEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.service.MissingDisputesService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/missingdisputes")
public class MissingDisputesController {
	private static final Logger logger = LoggerFactory.getLogger(MissingDisputesController.class.getName());

	@Autowired
	MissingDisputesService missingDisputesService;

	@Autowired
	Commons comm;

	/* This method is used to create a new missing dispute */
	@PostMapping("/insertmissingdisputes")
	public ResponseEntity<HttpResponse> insertMissingDisputes(@RequestParam("kpi_id") String kpi_id,
			@RequestParam("comm_plan_id") String comm_plan_id, @RequestParam("cust_id") String cust_id,
			@RequestParam("corp") String corp, @RequestParam("currentUser") String currentUser,
			@RequestParam("house") String house, @RequestParam("revenue") String revenue,
			@RequestParam("wordate") String wordate, @RequestParam("wfindate") String wfindate,
			@RequestParam("message") String message, @RequestParam("sales_rep_id") String sales_rep_id)
			throws DuplicateRecordException {

		int result = missingDisputesService.insertMissingDisputes(kpi_id, comm_plan_id, cust_id, corp, currentUser,
				house, revenue, wordate, wfindate, message,  sales_rep_id);
		if (result == 0) {
			return comm.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert");
		} else if (result == 2) {
			return comm.response(HttpStatus.CONFLICT, "Already exist");
		}
		return comm.response(OK, "Inserted Successfully");
	}
	
	/* This method is used to create a new missing dispute */
	@PostMapping("/insertmissingdisputesstatic")
	public ResponseEntity<HttpResponse> insertMissingDisputesStatic(@RequestParam("kpi_id") String kpi_id,
			 @RequestParam("currentUser") String currentUser,@RequestParam("message") String message)
			throws DuplicateRecordException {

		int result = missingDisputesService.insertMissingDisputesStatic(kpi_id, currentUser, message);
		if (result == 0) {
			return comm.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert");
		} else if (result == 2) {
			return comm.response(HttpStatus.CONFLICT, "Already exist");
		}
		return comm.response(OK, "Inserted Successfully");
	}

	/* This method is used to get all the missing disputes */
	@GetMapping("/getmissingdisputeslist")
	public ResponseEntity<List<MissingDisputesEntity>> getMissingDisputesList() {
		List<MissingDisputesEntity> getMissingDisputesList = missingDisputesService.getMissingDisputesList();
		return new ResponseEntity<>(getMissingDisputesList, OK);
	}
	
	/* This method is used to get all the missing disputes */
	@GetMapping("/getkpilist/{commplanid}")
	public ResponseEntity<List<MissingDisputesEntity>> getKpiList(@PathVariable int commplanid) {
		List<MissingDisputesEntity> getMissingDisputesList = missingDisputesService.getKpiList(commplanid);
		return new ResponseEntity<>(getMissingDisputesList, OK);
	}
}
