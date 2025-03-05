package com.altice.salescommission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.service.CommPlanService;
import com.altice.salescommission.utility.Commons;

@RestController
@RequestMapping("/api/salescomm/commplan")
public class CommPlanController {
	@Autowired
	CommPlanService commPlanService;
	@Autowired
	Commons commons;
}
