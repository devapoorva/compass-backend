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

import com.altice.salescommission.model.BossCallMappingModel;
import com.altice.salescommission.service.BossCallMappingService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/bosscallmapping")
public class BossCallMappingController {
	@Autowired
	BossCallMappingService bossCallMappingService;
	@Autowired
	Commons commons;
	
	@PostMapping("/savecalldata")
	public ResponseEntity<BossCallMappingModel> saveCallData(@RequestBody BossCallMappingModel callModel) {
		BossCallMappingModel baseCallModel = bossCallMappingService.saveCallData(callModel);
		return new ResponseEntity<>(baseCallModel, OK);
	}
	
	/* Update the existing callDetails in boss_call_mapping table */
	@PutMapping("/updatecallmapping")
	public ResponseEntity<BossCallMappingModel> updateCallMapping(@RequestBody BossCallMappingModel callModel) {
		BossCallMappingModel updateCallMap = bossCallMappingService.updateCallMapping(callModel);
		return new ResponseEntity<>(updateCallMap, OK);
	}
	
	/* Get the callMapping Data from boss_call_mapping table by the date */
	@GetMapping("/getcallmapingdata/{feedDate}")
	public ResponseEntity<List<BossCallMappingModel>> getCallMapingData(@PathVariable String feedDate) {
		List<BossCallMappingModel> callMappingDataList = bossCallMappingService.getCallMapingData(feedDate);
		return new ResponseEntity<>(callMappingDataList, OK);
	}
	
	/* Delete the existing the call detail values in boss_call_mapping table */
	@DeleteMapping("/deletecallmapdata/{id}/{ValidFromDt}")
	public ResponseEntity<HttpResponse> deleteCallMapData(@PathVariable String id, @PathVariable String ValidFromDt) {
		Map<String, Boolean> response = new HashMap<>();
		response = bossCallMappingService.deleteCallMapData(id, ValidFromDt);
		return commons.response(OK, "Record is Deleted Sucessfully");
	}
	
	
}
