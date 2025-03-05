package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.UserMangMappingEntity;
import com.altice.salescommission.service.UserMangMappingService;
import com.altice.salescommission.utility.Commons;

@RestController
@RequestMapping("/api/salescomm/usermanagement/userrolemappings")
public class UserMangMappingController {
	@Autowired
	Commons comm;

	@Autowired
	private UserMangMappingService userManagMappingService;

	/* This method is used to delete a role */
	@PutMapping("/assignpagetoroleviews")
	public ResponseEntity<UserMangMappingEntity> assignPageToRoleViews(@RequestParam("id") int id,
			@RequestParam("pageid") int pageid,@RequestParam("status") String status,@RequestParam("currentUser") String currentUser,@RequestParam("parentpageid") int parentpageid) throws IOException {
		UserMangMappingEntity getList = userManagMappingService.assignPageToRoleViews(id, pageid, status,currentUser,parentpageid);
		return new ResponseEntity<>(getList, OK);
	}

	/* This method is used to delete a role */
	@PutMapping("/assignpagetoroleedits")
	public ResponseEntity<UserMangMappingEntity> assignPageToRoleEdits(@RequestParam("id") int id,
			@RequestParam("pageid") int pageid,@RequestParam("status") String status,@RequestParam("currentUser") String currentUser,@RequestParam("parentpageid") int parentpageid) throws IOException {
		UserMangMappingEntity getList = userManagMappingService.assignPageToRoleEdits(id, pageid, status,currentUser,parentpageid);
		return new ResponseEntity<>(getList, OK);
	}

}
