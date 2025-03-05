package com.altice.salescommission.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/salescomm/main")
public class MainController {
	
	
	@GetMapping("/gethello")
	public String getCalrunidsList() {
		return "Hello, COMPAS";
	}
}
