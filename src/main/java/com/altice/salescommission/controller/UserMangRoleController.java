package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.UserMangRoleEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.model.UserManagementModel;
import com.altice.salescommission.service.UserMangRoleService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/usermanagement/roles")
public class UserMangRoleController {
	public static final String ROLE_DELETED_SUCCESSFULLY = "Role deleted successfully";

	@Autowired
	Commons comm;

	@Autowired
	private UserMangRoleService userManagRoleService;

	/* This method is used to create a new role */
	@PostMapping("/addrole")
	public ResponseEntity<UserMangRoleEntity> addRole(@RequestParam("role_name") String role_name,
			@RequestParam("role_desc") String role_desc, @RequestParam("comment") String comment,
			@RequestParam("email") String email,@RequestParam("currentUser") String currentUser) throws IOException, DuplicateRecordException {
		UserMangRoleEntity role = userManagRoleService.addRole(role_name, role_desc, comment, email,currentUser);
		return new ResponseEntity<>(role, OK);
	}

	/* This method is used to update role */
	@PostMapping("/updaterole")
	public ResponseEntity<UserMangRoleEntity> updateRole(@RequestParam("id") long id,@RequestParam("role_name") String role_name,
			@RequestParam("role_desc") String role_desc,
			@RequestParam("email") String email,
			@RequestParam("comment") String comment,@RequestParam("currentUser") String currentUser) throws IOException {
		UserMangRoleEntity updatedRole = userManagRoleService.updateRole(id,role_name,role_desc,email,comment,currentUser);
		return new ResponseEntity<>(updatedRole, OK);
	}

	/* This method is used to get all the roles list */
	@GetMapping("/getrolelist")
	public ResponseEntity<List<UserMangRoleEntity>> getAllRoles() {
		List<UserMangRoleEntity> roles = userManagRoleService.findAll();
		return new ResponseEntity<>(roles, OK);
	}
	
	/* This method is used to get all the roles and pages list for excel */
	@GetMapping("/getalldata")
	public ResponseEntity<List<UserManagementModel>> getAllData() {
		List<UserManagementModel> roles = userManagRoleService.getAllData();
		return new ResponseEntity<>(roles, OK);
	}

	/* This method is used to delete a role */
	@DeleteMapping("/deleterole/{id}")
	public ResponseEntity<HttpResponse> deleteRole(@PathVariable("id") Long id) throws IOException {
		userManagRoleService.deleteRole(id);
		return comm.response(OK, ROLE_DELETED_SUCCESSFULLY);
	}

	/* This method is used to get a role based on provided id */
	@GetMapping("/findrole/{id}")
	public ResponseEntity<UserMangRoleEntity> getRole(@PathVariable("id") Long id) {
		UserMangRoleEntity role = userManagRoleService.findRoleById(id);
		return new ResponseEntity<>(role, OK);
	}
}
