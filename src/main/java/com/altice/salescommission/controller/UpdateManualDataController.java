package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.text.ParseException;
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

import com.altice.salescommission.entity.AdjustmentsDetailEntity;
import com.altice.salescommission.entity.KomFeedEntity;
import com.altice.salescommission.entity.KpiGoalsEntity;
import com.altice.salescommission.model.MobileFeedModel;
import com.altice.salescommission.model.UpdateManualDataModel;
import com.altice.salescommission.service.UpdateManualDataService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/uploads/manualupdates")
public class UpdateManualDataController {
	
	@Autowired
	UpdateManualDataService updateManualDataService;

	@Autowired
	Commons commons;
	

	@PostMapping("/saveadjustment")
	public ResponseEntity<AdjustmentsDetailEntity> saveAdjustment(@RequestBody AdjustmentsDetailEntity adjustmentsDetailEntity) {
		AdjustmentsDetailEntity adjustModel = updateManualDataService.saveAdjustment(adjustmentsDetailEntity);
		return new ResponseEntity<>(adjustModel, OK);
	}
	
	@PostMapping("/savegoal")
	public ResponseEntity<KpiGoalsEntity> saveGoal(@RequestBody KpiGoalsEntity kpiGoalsEntity) {
		KpiGoalsEntity goalModel = updateManualDataService.saveGoal(kpiGoalsEntity);
		return new ResponseEntity<>(goalModel, OK);
	}
	
	@DeleteMapping("/deleteadjustment/{id}")
	public ResponseEntity<HttpResponse> deleteAdjustment(@PathVariable("id") int id) throws IOException {
		updateManualDataService.deleteAdjustment(id);
		return commons.response(OK, "Deleted Sucessfully");
	}
	
	@DeleteMapping("/deletegoal/{id}")
	public ResponseEntity<HttpResponse> deleteGoal(@PathVariable("id") int id) throws IOException {
		updateManualDataService.deleteGoal(id);
		return commons.response(OK, "Deleted Sucessfully");
	}
	
	@PutMapping("/updateadjustment")
	public ResponseEntity<HttpResponse> updateAdjustment(@RequestBody AdjustmentsDetailEntity adjustmentsDetailEntity) {
		Map<String, Boolean> response = new HashMap<>();
		response = updateManualDataService.updateAdjustment(adjustmentsDetailEntity);
		return commons.response(OK, "Updated Sucessfully");
	}
	
	@PutMapping("/updategoal")
	public ResponseEntity<HttpResponse> updateGoal(@RequestBody KpiGoalsEntity kpiGoalsEntity) {
		Map<String, Boolean> response = new HashMap<>();
		response = updateManualDataService.updateGoal(kpiGoalsEntity);
		return commons.response(OK, "Updated Sucessfully");
	}
	
	@GetMapping("/getadjustmentsbyid/{id}")
	public ResponseEntity<AdjustmentsDetailEntity> getAdjustmentsById(@PathVariable int id) throws ParseException {
		AdjustmentsDetailEntity getAdjustmentsById = updateManualDataService.getAdjustmentsById(id);
		return new ResponseEntity<>(getAdjustmentsById, OK);
	}
	
	@GetMapping("/getgoalsbyid/{id}")
	public ResponseEntity<KpiGoalsEntity> getGoalsById(@PathVariable int id) throws ParseException {
		KpiGoalsEntity getGoalsById = updateManualDataService.getGoalsById(id);
		return new ResponseEntity<>(getGoalsById, OK);
	}
	
	@PostMapping("/getadjustmentuploadresults")
	public ResponseEntity<List<AdjustmentsDetailEntity>> getAdjustmentUploadResults(@RequestBody AdjustmentsDetailEntity updateManualDataModel) {
		List<AdjustmentsDetailEntity> getAdjustmentUploadResults = updateManualDataService.getAdjustmentUploadResults(updateManualDataModel);
		return new ResponseEntity<>(getAdjustmentUploadResults, OK);
	}
	
	@PostMapping("/getgoaluploadresults")
	public ResponseEntity<List<KpiGoalsEntity>> getGoalUploadResults(@RequestBody KpiGoalsEntity updateManualDataModel) {
		List<KpiGoalsEntity> getGoalUploadResults = updateManualDataService.getGoalUploadResults(updateManualDataModel);
		return new ResponseEntity<>(getGoalUploadResults, OK);
	}
	
	@GetMapping("/getkpis/{type}")
	public ResponseEntity<List<UpdateManualDataModel>> getkpis(@PathVariable String type) {
		List<UpdateManualDataModel> getkpis = updateManualDataService.getkpis(type);
		return new ResponseEntity<>(getkpis, OK);
	}
	
	@GetMapping("/getcommplans")
	public ResponseEntity<List<UpdateManualDataModel>> getCommPlans() {
		List<UpdateManualDataModel> getCommPlans = updateManualDataService.getCommPlans();
		return new ResponseEntity<>(getCommPlans, OK);
	}
	
	@GetMapping("/getcalrunids")
	public ResponseEntity<List<UpdateManualDataModel>> getCalRunids() {
		List<UpdateManualDataModel> getCalRunids = updateManualDataService.getCalRunids();
		return new ResponseEntity<>(getCalRunids, OK);
	}
}
