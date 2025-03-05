package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

import com.altice.salescommission.entity.UploadButtonsEntity;
import com.altice.salescommission.entity.UploadJsonEntity;
import com.altice.salescommission.entity.UploadHeadersEntity;
import com.altice.salescommission.message.ResponseMessage;
import com.altice.salescommission.model.UploadJsonHeadersModel;
import com.altice.salescommission.service.UploadJsonService;

@RestController
@RequestMapping("/api/salescomm/commsumm/upload")
public class UploadJsonController {
	@Autowired
	UploadJsonService uploadJsonService;

	@PostMapping("/uploadjsons")
	public ResponseEntity<ResponseMessage> uploadJSON(@RequestParam("gridName") String gridName, @RequestParam("sql") String sql,
			@RequestParam("pageName") String pageName, @RequestParam("orderBy") int orderBy,
			@RequestParam("current_user") String current_user, @RequestParam("kpiId") String kpiId,@RequestParam("report_type") String report_type,
			@RequestParam("description") String description,@RequestParam("comm_plan_id") String comm_plan_id,@RequestParam("salesChannel") String salesChannel) {
		String message = "";
		try {
			uploadJsonService.uploadJSON(gridName, sql, pageName, orderBy, current_user,kpiId,report_type,description,comm_plan_id,salesChannel);
			message = "Uploaded the file successfully";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Something went wrong";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
	
	@PostMapping("/clonedata")
	public ResponseEntity<ResponseMessage> cloneData(@RequestParam("sc") String sc, @RequestParam("commId") int commId,
			@RequestParam("commPlanId") int commPlanId, @RequestParam("salesChannel") String salesChannel,
			@RequestParam("current_user") String current_user) {
		String message = "";
		try {
			uploadJsonService.cloneData(sc,commId,commPlanId,salesChannel,current_user);
			message = "Cloned successfully";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Failed!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
	
	@PutMapping("/updatejsons")
	public ResponseEntity<ResponseMessage> updateJSON(@RequestParam("file") MultipartFile file,
			@RequestParam("gridName") String gridName, @RequestParam("sql") String sql,
			@RequestParam("pageName") String pageName, @RequestParam("orderBy") int orderBy,
			@RequestParam("current_user") String current_user, @RequestParam("commPlanId") int commPlanId,
			@RequestParam("salesChannel") String salesChannel,@RequestParam("kpiId") int kpiId,@RequestParam("id") long id) {
		String message = "";
		try {
			uploadJsonService.updateJSON(file, gridName, sql, pageName, orderBy, current_user,commPlanId,salesChannel,kpiId,id);
			message = "Updated Successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
	
	@PutMapping("/updatejsonswithoutfile")
	public ResponseEntity<ResponseMessage> updateJSONWithoutFile(@RequestParam("gridName") String gridName, @RequestParam("sql") String sql,
			@RequestParam("pageName") String pageName, @RequestParam("orderBy") int orderBy,
			@RequestParam("current_user") String current_user, @RequestParam("commPlanId") String commPlanId,
			@RequestParam("salesChannel") String salesChannel,@RequestParam("kpiId") String kpiId,@RequestParam("id") long id) {
		String message = "";
		try {
			uploadJsonService.updateJSONWithoutFile(gridName, sql, pageName, orderBy, current_user,commPlanId,salesChannel,kpiId,id);
			message = "Updated Successfully";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Failed To Update";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	/* Get the Active list of jsons */
	@GetMapping("/getjsonslist/{salesChannels}/{commPlanId}")
	public ResponseEntity<List<UploadJsonEntity>> getJsonsList(@PathVariable("salesChannels") String salesChannels,@PathVariable("commPlanId") int commPlanId) {
		List<UploadJsonEntity> jsonsList = uploadJsonService.getJsonsList(salesChannels,commPlanId);
		return new ResponseEntity<>(jsonsList, OK);
	}
	
	/* Get kpis list */
	@GetMapping("/getkpis")
	public ResponseEntity<List<UploadJsonEntity>> getKpis() {
		List<UploadJsonEntity> kpisList = uploadJsonService.getKpisList();
		return new ResponseEntity<>(kpisList, OK);
	}
	
	/* Get commission plans list */
	@GetMapping("/getcomplans")
	public ResponseEntity<List<UploadJsonEntity>> getComPlans() {
		List<UploadJsonEntity> comPlansList = uploadJsonService.getComPlansList();
		return new ResponseEntity<>(comPlansList, OK);
	}
	
	/* Get report types list */
	@GetMapping("/getreporttypes")
	public ResponseEntity<List<UploadJsonEntity>> getReportTypes() {
		List<UploadJsonEntity> reportTypesList = uploadJsonService.getReportTypesList();
		return new ResponseEntity<>(reportTypesList, OK);
	}
	
	/* Get all the sales channels list */
	@GetMapping("/getsaleschannelslist")
	public ResponseEntity<List<UploadJsonEntity>> getSalesChannelsList() {
		List<UploadJsonEntity> getSalesChannelsList = uploadJsonService.getSalesChannelsList();
		return new ResponseEntity<>(getSalesChannelsList, OK);
	}
	
	/*  Get all the comm plans based on provided saleschannel */
	@GetMapping("/getcommplans/{saleschannel}")
	public ResponseEntity<List<UploadJsonEntity>> getCommPlans(@PathVariable("saleschannel") String saleschannel) {
		List<UploadJsonEntity> getCommPlans = uploadJsonService.getCommPlans(saleschannel);
		return new ResponseEntity<>(getCommPlans, OK);
	}
	
	/* This method is used to update comm summ headers */
	@PostMapping("/updateheaders")
	public ResponseEntity<UploadHeadersEntity> updateHeaders(@RequestBody List<UploadHeadersEntity> uploadJsonHeadersModel) {
		UploadHeadersEntity updateHeaders = uploadJsonService.updateHeaders(uploadJsonHeadersModel);
		return new ResponseEntity<>(updateHeaders, OK);
	}
	
	/* This method is used to update comm summ buttons */
	@PostMapping("/updatebuttons")
	public ResponseEntity<UploadButtonsEntity> updateButtons(@RequestBody List<UploadButtonsEntity> uploadJsonHeadersModel) {
		UploadButtonsEntity updateButtons = uploadJsonService.updateButtons(uploadJsonHeadersModel);
		return new ResponseEntity<>(updateButtons, OK);
	}
	
	/* Get the list of headers */
	@GetMapping("/getheaders/{id}")
	public ResponseEntity<List<UploadJsonHeadersModel>> getHeaders(@PathVariable int id) {
		List<UploadJsonHeadersModel> getPageData = uploadJsonService.getHeaders(id);
		return new ResponseEntity<>(getPageData, OK);
	}
	
	/* Get the list of kpis */
	@GetMapping("/getcommplanandkpis/{id}")
	public ResponseEntity<List<UploadJsonEntity>> getCommPlanAndKpis(@PathVariable int id) {
		List<UploadJsonEntity> getKpisData = uploadJsonService.getCommPlanAndKpis(id);
		return new ResponseEntity<>(getKpisData, OK);
	}

	/* Get the list of headers */
	@GetMapping("/getbuttons/{id}")
	public ResponseEntity<List<UploadJsonHeadersModel>> getButtons(@PathVariable int id) {
		List<UploadJsonHeadersModel> getPageData = uploadJsonService.getButtons(id);
		return new ResponseEntity<>(getPageData, OK);
	}

}