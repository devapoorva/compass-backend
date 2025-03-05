package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.model.HomeModel;
import com.altice.salescommission.service.HomeService;

@RestController
@RequestMapping("/api/salescomm/home")
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class.getName());

	@Autowired
	HomeService homeService;
	
	/* This method is used to get all the comm plans */
	@GetMapping("/getcommplanslist/{empid}/{role}")
	public ResponseEntity<List<HomeModel>> getCommPlansList(@PathVariable("empid") String empid,@PathVariable("role") String role) {
		List<HomeModel> getCommPlansList = homeService.getCommPlansList(empid,role);
		return new ResponseEntity<>(getCommPlansList, OK);
	}
	
	/* This method is used to get all the comm plans */
	@GetMapping("/gethomecommplanslist/{empid}/{role}")
	public ResponseEntity<List<HomeModel>> getHomeCommPlansList(@PathVariable("empid") String empid,@PathVariable("role") String role) {
		List<HomeModel> getHomeCommPlansList = homeService.getHomeCommPlansList(empid,role);
		return new ResponseEntity<>(getHomeCommPlansList, OK);
	}
	
	/* This method is used to get all dispute close status */
	@GetMapping("/getdisputestatus/{complanid}/{calrunid}")
	public ResponseEntity<List<HomeModel>> getDisputeStatus(@PathVariable("complanid") int complanid,@PathVariable("calrunid") int calrunid) {
		List<HomeModel> getDisputeStatus = homeService.getDisputeStatus(complanid,calrunid);
		return new ResponseEntity<>(getDisputeStatus, OK);
	}
	
	/* This method is used to get all the years */
	@GetMapping("/getcalrunidslist/{scempid}")
	public ResponseEntity<List<HomeModel>> getCalrunidsList(@PathVariable("scempid") String scempid) {
		List<HomeModel> getCalrunidsList = homeService.getCalrunidsList(scempid);
		return new ResponseEntity<>(getCalrunidsList, OK);
	}
	
	/* This method is used to get all the years */
	@GetMapping("/getcommrevlist/{calyear}/{scempid}/{comm_plan_id}")
	public ResponseEntity<List<HomeModel>> getCommRevList(@PathVariable("calyear") String calyear,@PathVariable("scempid") String scempid,@PathVariable("comm_plan_id") int comm_plan_id) {
		List<HomeModel> getCommRevList = homeService.getCommRevList(calyear,scempid,comm_plan_id);
		return new ResponseEntity<>(getCommRevList, OK);
	}
	
	/* This method is used to get all data to load charts for commissions */
	@GetMapping("/loadchart/{commyear}/{scempid}")
	public ResponseEntity<Map<String, Float>> loadCharts(@PathVariable("commyear") int commyear,@PathVariable("scempid") String scempid) {
		Map<String, Float> loadChartsList = homeService.loadCharts(commyear,scempid);
		return new ResponseEntity<>(loadChartsList, OK);
	}
}
