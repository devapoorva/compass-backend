package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.TranslateMasterEntity;
import com.altice.salescommission.service.TranslateTableService;
import com.altice.salescommission.utility.Commons;

@RestController
@RequestMapping("/api/salescomm/config/translatetable")
public class TranslateTableController {
	@Autowired
	TranslateTableService translateTableService;

	@Autowired
	Commons comm;
	
	/* This method is used to get all the field names */
	@GetMapping("/getfieldnames")
	public ResponseEntity<List<TranslateMasterEntity>> getFieldNames() {
		List<TranslateMasterEntity> getFieldNamesList = translateTableService.getFieldNames();
		return new ResponseEntity<>(getFieldNamesList, OK);
	}

	/* This method is used to update role */
	@PostMapping("/updatefiledvalue")
	public ResponseEntity<Integer> updateFieldValue(@RequestParam("id") Long id,
			@RequestParam("field_value") String field_value, @RequestParam("description") String description,
			@RequestParam("field_short_name") String field_short_name,
			@RequestParam("effective_date") Date effective_date,
			@RequestParam("effective_status") String effective_status,@RequestParam("currentUser") String currentUser) throws IOException {
		int updatedRole = translateTableService.updateFieldValue(id, field_value, description, field_short_name,
				effective_date, effective_status,currentUser);
		return new ResponseEntity<>(updatedRole, OK);
	}

	/* This method is used to update role */
	@PostMapping("/updatefiledname")
	public ResponseEntity<Integer> updateFieldName(@RequestParam("field_name") String field_name,
			@RequestParam("fname") String fname) throws IOException {
		int updatedRole = translateTableService.updateFieldName(field_name, fname);
		return new ResponseEntity<>(updatedRole, OK);
	}

	/* This method is used to create a new page */
	@PostMapping("/addtrans")
	public ResponseEntity<TranslateMasterEntity> addTrans(@RequestParam("field_name") String field_name,
			@RequestParam("field_value") String field_value, @RequestParam("description") String description,
			@RequestParam("field_short_name") String field_short_name,
			@RequestParam("effective_date") Date effective_date,@RequestParam("currentUser") String currentUser) throws IOException {
		TranslateMasterEntity transObj = translateTableService.addTrans(field_name, field_value, description,
				field_short_name, effective_date,currentUser);
		return new ResponseEntity<>(transObj, OK);
	}

	/* This method is used to get all the field names */
	@GetMapping("/gettranslatetablelist")
	public ResponseEntity<List<TranslateMasterEntity>> getTranslateTablelist() {
		List<TranslateMasterEntity> transList = translateTableService.getTranslateTablelist();
		return new ResponseEntity<>(transList, OK);
	}
	
	/* This method is used to get all the field names for excel */
	@GetMapping("/getexceltranslatetablelist")
	public ResponseEntity<List<TranslateMasterEntity>> getExcelTranslateTablelist() {
		List<TranslateMasterEntity> transList = translateTableService.getExcelTranslateTablelist();
		return new ResponseEntity<>(transList, OK);
	}
	
	/* This method is used to get all the field values */
	@GetMapping("/getfieldvalueslist")
	public ResponseEntity<List<TranslateMasterEntity>> getFieldValuesList(@RequestParam("field_name") String field_name) {
		List<TranslateMasterEntity> fieldValuesList = translateTableService.getFieldValuesList(field_name);
		return new ResponseEntity<>(fieldValuesList, OK);
	}
}
