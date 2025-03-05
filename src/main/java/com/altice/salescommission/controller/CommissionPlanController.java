package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.altice.salescommission.entity.CalendarEntity;
import com.altice.salescommission.entity.CommissionDetailValuesEntity;
import com.altice.salescommission.entity.CommissionPlanDetailEntity;
import com.altice.salescommission.entity.CommissionPlanEntity;
import com.altice.salescommission.entity.DependentKPIEntity;
import com.altice.salescommission.entity.DependentScoreKPIEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.message.ResponseMessage;
import com.altice.salescommission.model.AdjustMentResultModel;
import com.altice.salescommission.model.BossCallMappingModel;
import com.altice.salescommission.model.CommissionPlanDetailTierRangeModel;
import com.altice.salescommission.model.FreeFormModel;
import com.altice.salescommission.service.CommissionPlanService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/commission")
public class CommissionPlanController {
	private static final Logger logger = LoggerFactory.getLogger(CommissionPlanController.class.getName());

	@Autowired
	CommissionPlanService commissionPlanService;
	@Autowired
	Commons commons;

	/* Add commission plan */
	@PostMapping("/insertcommplan")
	public ResponseEntity<CommissionPlanEntity> insertCommPlan(@RequestBody CommissionPlanEntity commissionPlanModel)
			throws DuplicateRecordException, IdNotFoundException, ParseException {

		CommissionPlanEntity insertCommPlan = commissionPlanService.insertCommPlan(commissionPlanModel);

		return new ResponseEntity<>(insertCommPlan, OK);
	}
	
	/*
	 * Replicate commission plan record and insert a new record in to
	 * comm_plan_master table
	 */
	@PostMapping("/replicatecommplan")
	public ResponseEntity<CommissionPlanEntity> replicateCommPlan(@RequestBody CommissionPlanEntity commissionPlanModel)
			throws DuplicateRecordException, IdNotFoundException, ParseException {

		CommissionPlanEntity newCommissionPlan = commissionPlanService.replicateCommPlan(commissionPlanModel);

		return new ResponseEntity<>(newCommissionPlan, OK);
	}
	
	/* Add the kpi detail of particular plan to comm_plan_detail table */
	@PostMapping("/addKpitocommplan")
	public ResponseEntity<CommissionPlanDetailEntity> addKpitoCommPlan(
			@RequestBody List<CommissionPlanDetailTierRangeModel> commissionPlanDetailModel)
			throws ParseException, DuplicateRecordException {
		CommissionPlanDetailEntity newCommissionPlan = commissionPlanService
				.addKpitoCommPlan(commissionPlanDetailModel);
		return new ResponseEntity<>(newCommissionPlan, OK);
	}
	
	/*
	 * Replicate commission plan record and insert a new record in to
	 * comm_plan_master table
	 */
//	@PostMapping("/addcommplan")
//	public ResponseEntity<CommissionPlanEntity> addCommPlan(@RequestBody CommissionPlanEntity commissionPlanModel)
//			throws DuplicateRecordException, IdNotFoundException, ParseException {
//
//		CommissionPlanEntity newCommissionPlan = commissionPlanService.addCommPlan(commissionPlanModel);
//
//		return new ResponseEntity<>(newCommissionPlan, OK);
//	}

	@PutMapping("/uploadimages")
	public ResponseEntity<ResponseMessage> updateSingleFile(@RequestParam("file") MultipartFile file,
			@RequestParam("id") Long id) {
		String message = "";
		try {
			commissionPlanService.updateSingleFile(file, id);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	/* This method is used to get all the KPI calc type's */
	@GetMapping("/getchargebacktypes")
	public ResponseEntity<List<CommissionPlanEntity>> getChargebackTypes() {
		List<CommissionPlanEntity> getChargebackTypesList = commissionPlanService.getChargebackTypes();
		return new ResponseEntity<>(getChargebackTypesList, OK);
	}

	/* This method is used to get all the commission plans */
	@GetMapping("/getcommissionplans")
	public ResponseEntity<List<CommissionPlanEntity>> getCommissionPlans() {
		List<CommissionPlanEntity> getCommissionPlansList = commissionPlanService.getCommissionPlans();
		return new ResponseEntity<>(getCommissionPlansList, OK);
	}

	
	/* This method is used to get all the sales channels */
	@GetMapping("/getsaleschannels")
	public ResponseEntity<List<CommissionPlanEntity>> getSalesChannels() {
		List<CommissionPlanEntity> getSalesChannelsList = commissionPlanService.getSalesChannels();
		return new ResponseEntity<>(getSalesChannelsList, OK);
	}
	

	/* Update the existing commission plan record to comm_plan_master table */
	@PutMapping("/updatecommplan")
	public ResponseEntity<CommissionPlanEntity> updateCommPlan(
			@RequestBody List<CommissionPlanEntity> commissionPlanModel) {
		CommissionPlanEntity newCommissionPlan = commissionPlanService.updateCommPlan(commissionPlanModel);
		return new ResponseEntity<>(newCommissionPlan, OK);
	}

	/*
	 * Add the Score Depndnt kpi detail of particular plan to c_depndnt_kpi_details
	 * table
	 */
	@PostMapping("/adddpndntscoreKpitocommplan")
	public ResponseEntity<HttpResponse> addDpndntScoreKpitoCommPlan(@RequestParam("kpi_name") String kpi_name,
			@RequestParam("kpi_weight") String kpi_weight, @RequestParam("complex_kpi_id") String complex_kpi_id,
			@RequestParam("kpi_id") String kpi_id, @RequestParam("created_by") String created_by,
			@RequestParam("comp_plan_id") String comp_plan_id) throws DuplicateRecordException {
		try {
			int result = commissionPlanService.addDpndntScoreKpitoCommPlan(kpi_name, kpi_weight, complex_kpi_id, kpi_id,
					created_by, comp_plan_id);
			if (result == 0) {
				return commons.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert KPI");
			}
			return commons.response(OK, "KPI Inserted/Updated Successfully");
		} catch (Exception exception) {
			logger.error("Error: " + exception);
			return commons.response(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
		}
	}

	/*
	 * update the Score Dependent KPI detail of particular plan to
	 * c_depndnt_kpi_details table
	 */
	@PostMapping("/updatedpndntscoreKpitocommplan")
	public ResponseEntity<HttpResponse> updateDependntScoreKpitoCommplan(@RequestParam("kpi_name") String kpi_name,
			@RequestParam("kpi_weight") String kpi_weight, @RequestParam("complex_kpi_id") String complex_kpi_id,
			@RequestParam("kpi_id") String kpi_id, @RequestParam("created_by") String created_by,
			@RequestParam("comp_plan_id") String comp_plan_id,
			@RequestParam("comm_plan_dtl_id") String comm_plan_dtl_id, @RequestParam("id") String id)
			throws DuplicateRecordException {
		try {
			int result = commissionPlanService.updateDependntScoreKpitoCommplan(kpi_name, kpi_weight, complex_kpi_id,
					kpi_id, created_by, comp_plan_id, comm_plan_dtl_id, id);
			if (result == 0) {
				return commons.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert KPI");
			}
			return commons.response(OK, "KPI Inserted/Updated Successfully");
		} catch (Exception exception) {
			logger.error("Error: " + exception);
			return commons.response(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
		}
	}

	

	/* Update the kpi detail of particular plan to comm_plan_detail table */
	@PutMapping("/updatekpitocommplan")
	public ResponseEntity<CommissionPlanDetailEntity> updateKpiToCommPlan(
			@RequestBody List<CommissionPlanDetailTierRangeModel> commissionPlanDetailModels) {
		CommissionPlanDetailEntity newCommissionPlan = commissionPlanService
				.updateKpiToCommPlan(commissionPlanDetailModels);
		return new ResponseEntity<>(newCommissionPlan, OK);
	}

	/* Get the List of commissions from comm_plan_master table By currentDate */
	@GetMapping("/commplanslist")
	public ResponseEntity<List<CommissionPlanEntity>> getCommPlans() {
		List<CommissionPlanEntity> plans = commissionPlanService.getCommPlans();
		return new ResponseEntity<>(plans, OK);
	}

	/* Get the All List of commissions from comm_plan_master table */
	@GetMapping("/allcommplanslist")
	public ResponseEntity<List<CommissionPlanEntity>> getAllCommPlans() {
		List<CommissionPlanEntity> plans = commissionPlanService.getAllCommPlans();
		return new ResponseEntity<>(plans, OK);
	}

	/*
	 * Get the kpi details of particular plan from comm_plan_detail table based on
	 * commPlanId
	 */
	@GetMapping("/kpiplanslist/{commPlanId}/{effDate}")
	public ResponseEntity<List<CommissionPlanDetailEntity>> getKpiPlans(@PathVariable int commPlanId,
			@PathVariable String effDate) {
		List<CommissionPlanDetailEntity> kpiPlans = commissionPlanService.getKpiPlans(commPlanId, effDate);
		return new ResponseEntity<>(kpiPlans, OK);
	}

	/*
	 * Get the image from comm_plan_detail table based on commPlanId and effdate
	 */
	@GetMapping("/getimage/{commPlanId}/{effDate}")
	public ResponseEntity<List<CommissionPlanDetailEntity>> getImage(@PathVariable int commPlanId,
			@PathVariable String effDate) {
		List<CommissionPlanDetailEntity> kpiPlans = commissionPlanService.getImage(commPlanId, effDate);
		return new ResponseEntity<>(kpiPlans, OK);
	}

	/*
	 * Get the kpi details of particular plan from comm_plan_detail table based on
	 * commPlandtlId
	 */
	@GetMapping("/tierrangevalues/{commPlanDtlId}")
	public ResponseEntity<List<CommissionDetailValuesEntity>> getTierRangeValues(@PathVariable int commPlanDtlId) {
		List<CommissionDetailValuesEntity> kpiPlans = commissionPlanService.getTierRangeValues(commPlanDtlId);
		return new ResponseEntity<>(kpiPlans, OK);
	}

	/*
	 * Get the kpi details of particular plan from comm_plan_detail table based on
	 * commPlanId
	 */
	@GetMapping("/kpiplanslisttoreplicate/{commPlanId}/{effDate}")
	public ResponseEntity<List<CommissionPlanDetailTierRangeModel>> getKpiPlansToReplicate(@PathVariable int commPlanId,
			@PathVariable String effDate) throws ParseException {
		List<CommissionPlanDetailTierRangeModel> kpiPlansToReplicate = commissionPlanService
				.getKpiPlansToReplicate(commPlanId, effDate);
		return new ResponseEntity<>(kpiPlansToReplicate, OK);
	}

	/* Get the commPlan from comm_plan_master table by the as of date */
	@GetMapping("/getcommplanbyasofdate/{id}")
	public ResponseEntity<CommissionPlanEntity> getCommplanByAsOfDdate(@PathVariable int id) {
		CommissionPlanEntity plan = commissionPlanService.getCommplanByAsOfDdate(id);
		return new ResponseEntity<>(plan, OK);
	}

	/* Get the commPlan from comm_plan_master table by the complanid */
	@GetMapping("/getasofdate/{commPlanId}")
	public ResponseEntity<List<CommissionPlanEntity>> getAsOfDate(@PathVariable int commPlanId) {
		List<CommissionPlanEntity> plan = commissionPlanService.getAsOfDate(commPlanId);
		return new ResponseEntity<>(plan, OK);
	}

	/* Delete the existing kpi of commission plan from comm_plan_detail table */
	@PostMapping("/deleteKpitocommplan")
	public ResponseEntity<HttpResponse> deleteKpitoCommPlan(
			@RequestBody CommissionPlanDetailTierRangeModel commissionPlanDetailModels) {
		try {
			int result = commissionPlanService.deleteKpiToCommPlan(commissionPlanDetailModels);
			if (result > 0) {
				return commons.response(OK, "KPI Deleted Successfully");
			} else {
				return commons.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete KPI");
			}
		} catch (Exception exception) {
			logger.error("Error: " + exception);
			return commons.response(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
		}
	}

	/* Delete the existing the comm detail values from c_comm_detail_values table */
	@DeleteMapping("/deletecomdetailvalues/{id}")
	public ResponseEntity<HttpResponse> deleteCommDetailValues(@PathVariable int id) {
		Map<String, Boolean> response = new HashMap<>();
		response = commissionPlanService.deleteCommDetailValues(id);
		return commons.response(OK, "Record deleted sucessfully");
	}

	/* Delete the existing the calc var from commission plan */
	@DeleteMapping("/deletecalcvar/{id}")
	public ResponseEntity<HttpResponse> deleteCalcVar(@PathVariable int id) {
		Map<String, Boolean> response = new HashMap<>();
		response = commissionPlanService.deleteCalcVar(id);
		return commons.response(OK, "Record deleted sucessfully");
	}

	/*
	 * Get the list of measuretypes
	 */
	@GetMapping("/measuretypelist")
	public ResponseEntity<List> getMeasureTypes() {
		List measureTypeList = new ArrayList();
		measureTypeList = commissionPlanService.getMeasureTypes();
		return new ResponseEntity<>(measureTypeList, OK);
	}

	/*
	 * Get the kpi details of particular plan from comm_plan_detail table based on
	 * commPlanId
	 */
	@GetMapping("/getAdjustMentList/{commPeriod}/{salesChannel}/{commPlan}")
	public ResponseEntity<List<AdjustMentResultModel>> getAdjustMentResultList(@PathVariable int commPeriod,
			@PathVariable String salesChannel, @PathVariable int commPlan) {

		List<AdjustMentResultModel> adjustMentResultModels = commissionPlanService.getAdjustMentResultList(commPeriod,
				salesChannel, commPlan);
		return new ResponseEntity<>(adjustMentResultModels, OK);
	}

	/* This method is used to create a new adjustment */
	@PostMapping("/addAdjustMent")
	public ResponseEntity<AdjustMentResultModel> addAdjustMent(@RequestBody AdjustMentResultModel adjustMentModel) {
		// AdjustMentResultModel adjModel =new AdjustMentResultModel();
		AdjustMentResultModel adjModel = commissionPlanService.addAdjustMent(adjustMentModel);
		return new ResponseEntity<>(adjModel, OK);
	}

	@GetMapping("/getvalidatedt/{commPlanId}")
	public ResponseEntity<List<CalendarEntity>> validateEffictiveDt(@PathVariable int commPlanId)
			throws ParseException {
		List<CalendarEntity> calendarData = commissionPlanService.validateEffictiveDt(commPlanId);
		return new ResponseEntity<>(calendarData, OK);
	}

	@GetMapping("/getfreeformresult/{searchwith}")
	public ResponseEntity<List<FreeFormModel>> getFreeFormSerchResults(@PathVariable String searchwith)
			throws ParseException {
		List<FreeFormModel> freeFormModel = commissionPlanService.getFreeFormSerchResults(searchwith);
		return new ResponseEntity<>(freeFormModel, OK);
	}

	@GetMapping("/getdpndntkpi/{kpiId}/{comp_plan_id}/{comm_plan_dtl_id}/{edate}")
	public ResponseEntity<List<DependentKPIEntity>> getDpndntKPI(@PathVariable int kpiId,
			@PathVariable int comp_plan_id, @PathVariable int comm_plan_dtl_id, @PathVariable String edate)
			throws ParseException {
		List<DependentKPIEntity> depndntKpiData = commissionPlanService.getDpndntKPI(kpiId, comp_plan_id,
				comm_plan_dtl_id, edate);
		return new ResponseEntity<>(depndntKpiData, OK);
	}

	@GetMapping("/getdependentscorekpi/{kpiId}/{comp_plan_id}/{comm_plan_dtl_id}/{edate}")
	public ResponseEntity<List<DependentScoreKPIEntity>> getDpndntScoreKPI(@PathVariable int kpiId,
			@PathVariable int comp_plan_id, @PathVariable int comm_plan_dtl_id, @PathVariable String edate)
			throws ParseException {
		List<DependentScoreKPIEntity> depndntKpiData = commissionPlanService.getDpndntScoreKPI(kpiId, comp_plan_id,
				comm_plan_dtl_id, edate);
		return new ResponseEntity<>(depndntKpiData, OK);
	}

	/*
	 * Add the Depndnt kpi detail of particular plan to c_depndnt_kpi_details table
	 */
	@PostMapping("/adddpndntKpitocommplan")
	public ResponseEntity<DependentKPIEntity> addDpndntKpitoCommPlan(
			@RequestBody List<DependentKPIEntity> dependentKPIModelList) throws ParseException {
		DependentKPIEntity dependentKPIModel = commissionPlanService.addDpndntKpitoCommPlan(dependentKPIModelList);
		return new ResponseEntity<>(dependentKPIModel, OK);
	}

	/*
	 * update the Depndnt kpi detail of particular plan to c_depndnt_kpi_details
	 * table
	 */
	@PostMapping("/updatedpndntKpitocommplan")
	public ResponseEntity<DependentKPIEntity> updateDpndntKpitoCommPlan(
			@RequestBody List<DependentKPIEntity> dependentKPIModelList) throws ParseException {
		DependentKPIEntity dependentKPIModel = commissionPlanService.updateDpndntKpitoCommPlan(dependentKPIModelList);
		return new ResponseEntity<>(dependentKPIModel, OK);
	}

	/* This method is used to get all the measure against types */
	@GetMapping("/getmeasureagainstlist")
	public ResponseEntity<List<CommissionPlanDetailEntity>> getmeasureagainstList() {
		List<CommissionPlanDetailEntity> getmeasureagainstList = commissionPlanService.getmeasureagainstList();
		return new ResponseEntity<>(getmeasureagainstList, OK);
	}

	/* This method is used to update KPI's to active */
	@PutMapping("/updateCommPlanStatusToActive")
	public ResponseEntity<CommissionPlanEntity> updateCommPlanStatusToActive(
			@RequestBody List<CommissionPlanEntity> updateCommPlanStatusToActive) {
		CommissionPlanEntity updateCommPlanStatus = commissionPlanService
				.updateCommPlanStatusToActive(updateCommPlanStatusToActive);
		return new ResponseEntity<>(updateCommPlanStatus, OK);
	}

	/* This method is used to update KPI's to inactive */
	@PutMapping("/updateCommPlanStatusToInactive")
	public ResponseEntity<CommissionPlanEntity> updateCommPlanStatusToIactive(
			@RequestBody List<CommissionPlanEntity> updateCommPlanStatusToIactive) {
		CommissionPlanEntity updateCommPlanStatus = commissionPlanService
				.updateCommPlanStatusToIactive(updateCommPlanStatusToIactive);
		return new ResponseEntity<>(updateCommPlanStatus, OK);
	}

	/* This method is used to get all the saleschannels */
	@GetMapping("/getsaleschannelslist")
	public ResponseEntity<List<CommissionPlanDetailEntity>> getSalesChannelsList() {
		List<CommissionPlanDetailEntity> getmeasureagainstList = commissionPlanService.getSalesChannelsList();
		return new ResponseEntity<>(getmeasureagainstList, OK);
	}

}
