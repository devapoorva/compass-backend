package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.ExcelHeadersEntity;
import com.altice.salescommission.service.ExcelHeadersService;

@RestController
@RequestMapping("/api/salescomm/excelheaders")
public class ExcelHeadersController {
	@Autowired
	ExcelHeadersService excelHeadersService;
	
	/* Get the list of calendars */
	@GetMapping("/getkpiexcelheaders")
	public ResponseEntity<List<ExcelHeadersEntity>> getKPIExcelHeadersList() {
		List<ExcelHeadersEntity> getKPIExcelHeadersList = excelHeadersService.getKPIExcelHeadersList();
		return new ResponseEntity<>(getKPIExcelHeadersList, OK);
	}
}
