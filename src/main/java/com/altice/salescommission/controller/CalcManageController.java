package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.CalcManageEntity;
import com.altice.salescommission.entity.CalcRunCtrlEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.service.CalcManageService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/onlinecalc/calcmanage")
public class CalcManageController {
	@Autowired
	CalcManageService calcManageService;
	@Autowired
	Commons comm;

	/* Get the list of sales channels */
	@PostMapping("/login")
	public ResponseEntity<List<CalcManageEntity>> getLogin(@RequestBody List<CalcManageEntity> calcManageEntity) {

		List<CalcManageEntity> getSalesChannelsList = null;
		return new ResponseEntity<>(getSalesChannelsList, OK);
	}

	/* Get the list of sales channels */
	@GetMapping("/getsaleschanneldropdown")
	public ResponseEntity<List<CalcManageEntity>> getSalesChannelDropDown() {
		List<CalcManageEntity> getSalesChannelsList = calcManageService.getSalesChannelDropDown();
		return new ResponseEntity<>(getSalesChannelsList, OK);
	}

	/* Get the list of cal run ID's */
	@GetMapping("/getcalruniddropdown")
	public ResponseEntity<List<CalcManageEntity>> getCalRunIdDropDown() {
		List<CalcManageEntity> getCalRunIdsList = calcManageService.getCalRunIdDropDown();
		return new ResponseEntity<>(getCalRunIdsList, OK);
	}

	/* Get the list of distinct user control */
	@GetMapping("/getrunControlnames")
	public ResponseEntity<List<CalcRunCtrlEntity>> getRunControlByName() {
		List<CalcRunCtrlEntity> getCalRunNamesList = calcManageService.getRunControlByName();
		return new ResponseEntity<>(getCalRunNamesList, OK);
	}

	/* Get the list of cal run ID's based on saleschanneles */
	@GetMapping("/getcalrunidlist")
	public ResponseEntity<List<CalcRunCtrlEntity>> getCalRunIdBySalesChnl() {
		List<CalcRunCtrlEntity> getCalRunIdsList = calcManageService.getCalRunIdBySalesChnl();
		return new ResponseEntity<>(getCalRunIdsList, OK);
	}

	/* Get the list of commissionPlans based on saleschanneles */
	@GetMapping("/getcommplanslist/{salesChannels}")
	public ResponseEntity<List<CalcRunCtrlEntity>> getCommPlans(@PathVariable("salesChannels") String salesChannels) {
		List<CalcRunCtrlEntity> getCalRunIdsList = calcManageService.getCommPlans(salesChannels);
		return new ResponseEntity<>(getCalRunIdsList, OK);
	}

	/* Get the list of employeeId list based on commplanId */
	@GetMapping("/getempidlist/{commPanId}")
	public ResponseEntity<List<CalcRunCtrlEntity>> getEmpDataList(@PathVariable("commPanId") int commPanId) {
		List<CalcRunCtrlEntity> getCalRunIdsList = calcManageService.getEmpDataList(commPanId);
		return new ResponseEntity<>(getCalRunIdsList, OK);
	}

	/* Get the list of calc data */
	@GetMapping("/getcalcdata/{salesChannel}/{calRunId}")
	public ResponseEntity<List<CalcManageEntity>> getCalcData(@PathVariable("salesChannel") String salesChannel,
			@PathVariable("calRunId") String calRunId) {
		List<CalcManageEntity> getCalcList = calcManageService.getCalcData(salesChannel, calRunId);
		System.out.println("getCalcList = " + getCalcList);
		return new ResponseEntity<>(getCalcList, OK);
	}

	/* This method is used to update Calc dates */
	@PutMapping("/updatecalc")
	public ResponseEntity<CalcManageEntity> updateCalc(@RequestBody List<CalcManageEntity> calcManageEntity)
			throws IdNotFoundException {
		CalcManageEntity updateCalc = calcManageService.updateCalc(calcManageEntity);
		return new ResponseEntity<>(updateCalc, OK);
	}

	/* Add the run controles to the c_calc_run_ctl table */
	@PostMapping("/addrunctrl")
	public ResponseEntity<CalcRunCtrlEntity> saveRunCtrls(@RequestBody List<CalcRunCtrlEntity> calcRunCtrlModels)
			throws DuplicateRecordException {
		CalcRunCtrlEntity calcRunCtrlModel = calcManageService.saveRunCtrls(calcRunCtrlModels);
		return new ResponseEntity<>(calcRunCtrlModel, OK);
	}

	/* update the run controles to the c_calc_run_ctl table */
	@PutMapping("/updaterunctrl")
	public ResponseEntity<CalcRunCtrlEntity> updateRunCtrls(@RequestBody List<CalcRunCtrlEntity> calcRunCtrlModels) {
		CalcRunCtrlEntity calcRunCtrlModel = calcManageService.updateRunCtrls(calcRunCtrlModels);
		return new ResponseEntity<>(calcRunCtrlModel, OK);
	}

	/* Get the list of distinct user control base on runctrl ID */
	@GetMapping("/getrunControls/{runctrlId}")
	public ResponseEntity<List<CalcRunCtrlEntity>> getCalRunByRunId(@PathVariable("runctrlId") int runctrlId) {
		List<CalcRunCtrlEntity> getCalRunNamesList = calcManageService.getCalRunByRunId(runctrlId);
		return new ResponseEntity<>(getCalRunNamesList, OK);
	}

	/*
	 * Delete the commplan associated with run control from the c_calc_run_ctl table
	 */
	@DeleteMapping("/deletecomdetailvalues/{id}")
	public ResponseEntity<HttpResponse> deleteCommPlan(@PathVariable int id) {
		Map<String, Boolean> response = new HashMap<>();
		response = calcManageService.deleteCommPlan(id);
		return comm.response(OK, "Record is Deleted Sucessfully");
	}

	/* Get the list of inprogress calRun ID */
	@GetMapping("/getinprogressids")
	public ResponseEntity<List<CalcRunCtrlEntity>> getInprogressCalRunID() {
		List<CalcRunCtrlEntity> getCalRunNamesList = calcManageService.getInprogressCalRunID();
		return new ResponseEntity<>(getCalRunNamesList, OK);
	}
}
