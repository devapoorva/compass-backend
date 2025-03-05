package com.altice.salescommission.reportframework;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/salescomm/reportframework/reportprocess/")
public class ReportProcessController {
	@Autowired
	ReportProcessService reportProcessService;
	
	/* Get all the process report */
	@GetMapping("/getprocessreport")
	public ResponseEntity<List<ReportProcessModel>> getProcessReport() {
		List<ReportProcessModel> processList = reportProcessService.getProcessReport();
		return new ResponseEntity<>(processList, OK);
	}
	
	/* Get all the process report */
	@GetMapping("/getreportdetails")
	public ResponseEntity<List<ReportProcessModel>> getReportDetails() {
		List<ReportProcessModel> reportDetailsList = reportProcessService.getReportDetails();
		return new ResponseEntity<>(reportDetailsList, OK);
	}
}
