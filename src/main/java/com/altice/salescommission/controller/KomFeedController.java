package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.text.ParseException;
import java.util.Date;
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

import com.altice.salescommission.entity.KomFeedEntity;
import com.altice.salescommission.model.CallDetailsModel;
import com.altice.salescommission.model.ComboStringModel;
import com.altice.salescommission.service.KomFeedService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/komfeed")
public class KomFeedController {
	@Autowired
	KomFeedService komFeedService;
	@Autowired
	Commons commons;

	@DeleteMapping("/deleterowbywipbyinfoid/{id}")
	public ResponseEntity<HttpResponse> deleteRowONWipByInfo(@PathVariable int id) {
		Map<String, Boolean> response = new HashMap<>();
		response = komFeedService.deleteRowONWipByInfo(id);
		return commons.response(OK, "Deleted Sucessfully");
	}

	@PostMapping("/getproduct")
	public ResponseEntity<ComboStringModel> getProduct(@RequestBody ComboStringModel comboStringModel) {
		ComboStringModel comboModel = new ComboStringModel();
		List<ComboStringModel> comboStringModelList = komFeedService.getProduct(comboStringModel);
		if (comboStringModelList.size() == 1)
			comboModel = comboStringModelList.get(0);
		return new ResponseEntity<>(comboModel, OK);
	}
	@PostMapping("/getsalesrepids")
	public ResponseEntity<List<KomFeedEntity>> getSalesRepIds(@RequestBody KomFeedEntity feedModel) {
		List<KomFeedEntity> komFeedModel = komFeedService.getSalesRepIds(feedModel);
		return new ResponseEntity<>(komFeedModel, OK);
	}
	
	@PostMapping("/getfeedresults")
	public ResponseEntity<List<KomFeedEntity>> getFeedResults(@RequestBody KomFeedEntity feedModel) {
		List<KomFeedEntity> komFeedModel = komFeedService.getFeedResults(feedModel);
		return new ResponseEntity<>(komFeedModel, OK);
	}
	
	@PostMapping("/getfeedresultsexcel")
	public ResponseEntity<List<KomFeedEntity>> getFeedResultsExcel(@RequestBody KomFeedEntity feedModel) {
		List<KomFeedEntity> komFeedModel = komFeedService.getFeedResultsExcel(feedModel);
		return new ResponseEntity<>(komFeedModel, OK);
	}
	
	@PostMapping("/savewipbyifodata")
	public ResponseEntity<KomFeedEntity> saveWibByInfoData(@RequestBody KomFeedEntity feedModel) {
		KomFeedEntity komFeedModel = komFeedService.saveWibByInfoData(feedModel);
		return new ResponseEntity<>(komFeedModel, OK);
	}
	@PostMapping("/getproductnames")
	public ResponseEntity<List<ComboStringModel>> getProductNames(@RequestBody KomFeedEntity feedModel) {
		List<ComboStringModel> comboStringModel = komFeedService.getProductNames(feedModel);
		return new ResponseEntity<>(comboStringModel, OK);
	}
	@PostMapping("/getproductinfo/{productName}")
	public ResponseEntity<List<ComboStringModel>> getProductInfo(@PathVariable String productName,
			@RequestBody KomFeedEntity feedModel) throws ParseException {
		List<ComboStringModel> comboStringModel = komFeedService.getProductInfo(productName, feedModel);
		return new ResponseEntity<>(comboStringModel, OK);
	}
	@PostMapping("/getproductinfo1")
	public ResponseEntity<List<ComboStringModel>> getProductInfo1(@RequestBody KomFeedEntity feedModel) throws ParseException {
		List<ComboStringModel> comboStringModel = komFeedService.getProductInfo1(feedModel);
		return new ResponseEntity<>(comboStringModel, OK);
	}
	
	@PutMapping("/updatekomfeed")
	public ResponseEntity<HttpResponse> updateKomFeed(@RequestBody KomFeedEntity feedModel) {
		Map<String, Boolean> response = new HashMap<>();
		response = komFeedService.updateKomFeed(feedModel);
		return commons.response(OK, "Updated Sucessfully");
	}
	
	@GetMapping("/getkomfeedatabyid/{komfeedid}")
	public ResponseEntity<KomFeedEntity> getKomFeedDataById(@PathVariable int komfeedid) throws ParseException {
		KomFeedEntity komFeedModel = komFeedService.getKomFeedDataById(komfeedid);
		return new ResponseEntity<>(komFeedModel, OK);
	}
	
	@GetMapping("/getkomfeedatabycorphouserepid/{corp}/{house}/{salesrepid}")
	public ResponseEntity<KomFeedEntity> getKomFeedDataByCorpHouseRepId(@PathVariable int corp,@PathVariable String house,@PathVariable String salesrepid) throws ParseException {
		KomFeedEntity komFeedModel = komFeedService.getKomFeedDataByCorpHouseRepId(corp,house,salesrepid);
		return new ResponseEntity<>(komFeedModel, OK);
	}
	
	@GetMapping("/getkomfeedatabyrepid/{corp}/{house}/{salesrepid}/{id}")
	public ResponseEntity<List<KomFeedEntity>> getKomFeedDataByRepId(@PathVariable int corp,@PathVariable String house,@PathVariable String salesrepid,@PathVariable int id) throws ParseException {
		List<KomFeedEntity> komFeedModel = komFeedService.getKomFeedDataByRepId(corp,house,salesrepid,id);
		return new ResponseEntity<>(komFeedModel, OK);
	}
	
	@GetMapping("/getratecodeinfo/{corp}/{ratecd}/{orderdt}")
	public ResponseEntity<List<KomFeedEntity>> getRateCodeInfo(@PathVariable int corp,@PathVariable String ratecd,@PathVariable String orderdt) throws ParseException {
		List<KomFeedEntity> komFeedModel = komFeedService.getRateCodeInfo(corp,ratecd,orderdt);
		return new ResponseEntity<>(komFeedModel, OK);
	}
	
	@GetMapping("/getreportingcentersinfo/{corp}/{ratecd}/{orderdt}")
	public ResponseEntity<List<KomFeedEntity>> getReportingCentersInfo(@PathVariable int corp,@PathVariable String ratecd,@PathVariable String orderdt) throws ParseException {
		List<KomFeedEntity> komFeedModel = komFeedService.getReportingCentersInfo(corp,ratecd,orderdt);
		return new ResponseEntity<>(komFeedModel, OK);
	}
	
	/* This method is used to get all the OOL details */
	@GetMapping("/getooldetails")
	public ResponseEntity<List<KomFeedEntity>> getOOLDetails() {
		List<KomFeedEntity> getOOLDetails = komFeedService.getOOLDetails();
		return new ResponseEntity<>(getOOLDetails, OK);
	}
	
	@PostMapping("/addnewadjustment")
	public ResponseEntity<KomFeedEntity> addNewAdjustment(@RequestBody KomFeedEntity feedModel) {
		KomFeedEntity addNewAdjustment = komFeedService.addNewAdjustment(feedModel);
		return new ResponseEntity<>(addNewAdjustment, OK);
	}
	
	@PostMapping("/addadjustment")
	public ResponseEntity<KomFeedEntity> addAdjustment(@RequestBody KomFeedEntity feedModel) {
		KomFeedEntity addNewAdjustment = komFeedService.addAdjustment(feedModel);
		return new ResponseEntity<>(addNewAdjustment, OK);
	}
	
	@PostMapping("/getCallData")
	public ResponseEntity<List<CallDetailsModel>> getCallDetails(@RequestBody CallDetailsModel callDtlModel) {
		List<CallDetailsModel> komCallModel = komFeedService.getCallDetails(callDtlModel);
		return new ResponseEntity<>(komCallModel, OK);
	}
	
	@DeleteMapping("/deletecallbykomcallid/{id}")
	public ResponseEntity<HttpResponse> deleteCallByKomCallID(@PathVariable int id) {
		Map<String, Boolean> response = new HashMap<>();
		response = komFeedService.deleteCallByKomCallID(id);
		return commons.response(OK, "Deleted Sucessfully");
	}
	
	@PostMapping("/savecalldetails")
	public ResponseEntity<CallDetailsModel> saveCallDetails(@RequestBody CallDetailsModel callDtlModel) {
		CallDetailsModel callDtlMdel = komFeedService.saveCallDetails(callDtlModel);
		return new ResponseEntity<>(callDtlMdel, OK);
	}
	
	@PutMapping("/updatecalldetails")
	public ResponseEntity<CallDetailsModel> updateCallDetails(@RequestBody CallDetailsModel callDtlModel) {
		CallDetailsModel callDtlMdel = komFeedService.updateCallDetails(callDtlModel);
		return new ResponseEntity<>(callDtlMdel, OK);
	}
	
	
}
