package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.UploadsTemplateEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.service.UploadsTemplateService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/uploads/recordtemplate")
public class UploadsTemplateController {
	
	public static final String TEMPLATE_DELETED_SUCCESSFULLY = "Template deleted successfully";
	
	@Autowired
	UploadsTemplateService uploadsTemplateService;

	@Autowired
	Commons comm;
	
	/* This method is used to delete a template */
	@DeleteMapping("/deletetemplate/{id}/{recordname}/{recordtemplate}")
	public ResponseEntity<HttpResponse> deleteTemplate(@PathVariable("id") int id,@PathVariable("recordname") String recordname,@PathVariable("recordtemplate") String recordtemplate) throws IOException {
		uploadsTemplateService.deleteTemplate(id,recordname,recordtemplate);
		return comm.response(OK, TEMPLATE_DELETED_SUCCESSFULLY);
	}

	/* This method is used to get all templates */
	@GetMapping("/gettemplateslist")
	public ResponseEntity<List<UploadsTemplateEntity>> getTemplatesList() {
		List<UploadsTemplateEntity> myTemplatesList = uploadsTemplateService.getTemplatesList();
		return new ResponseEntity<>(myTemplatesList, OK);
	}

	/* This method is used to get all columns based on provided record */
	@GetMapping("/getcolumnslist/{recordName}")
	public ResponseEntity<List<UploadsTemplateEntity>> getColumnsList(@PathVariable("recordName") String recordName) {
		List<UploadsTemplateEntity> myDocsList = uploadsTemplateService.getColumnsList(recordName);
		return new ResponseEntity<>(myDocsList, OK);
	}
	
	/* This method is used to get all columns based on provided record */
	@GetMapping("/getcolumnseditlist/{recordName}")
	public ResponseEntity<List<UploadsTemplateEntity>> getColumnsEditList(@PathVariable("recordName") String recordName) {
		List<UploadsTemplateEntity> myDocsList = uploadsTemplateService.getColumnsEditList(recordName);
		return new ResponseEntity<>(myDocsList, OK);
	}

	

	/* This method is used to get all columns based on provided record */
	@GetMapping("/getrecordslist/{recordName}")
	public ResponseEntity<LinkedHashSet<Map<String, String>>> getRecordsList(@PathVariable("recordName") String recordName) {
		LinkedHashSet<Map<String, String>> myDocsList = uploadsTemplateService.getRecordsList(recordName);
		return new ResponseEntity<>(myDocsList, OK);
	}

	/* This method is used to add template */
	@PostMapping("/addtemplate")
	public ResponseEntity<UploadsTemplateEntity> insertTemplate(
			@RequestBody List<UploadsTemplateEntity> uploadsTemplateEntity) throws IdNotFoundException, DuplicateRecordException {
		UploadsTemplateEntity insertTemplateObj = uploadsTemplateService.insertTemplate(uploadsTemplateEntity);
		return new ResponseEntity<>(insertTemplateObj, OK);
	}
	
	/* This method is used to update template */
	@PostMapping("/updatetemplate")
	public ResponseEntity<UploadsTemplateEntity> updateTemplate(
			@RequestBody List<UploadsTemplateEntity> uploadsTemplateEntity) throws IdNotFoundException {
		UploadsTemplateEntity updateTemplateObj = uploadsTemplateService.updateTemplate(uploadsTemplateEntity);
		return new ResponseEntity<>(updateTemplateObj, OK);
	}
}
