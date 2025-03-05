package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.PageNavigationEntity;
import com.altice.salescommission.model.UserManagementModel;
import com.altice.salescommission.service.UserMangPageService;
import com.altice.salescommission.utility.Commons;
@RestController
@RequestMapping("/api/salescomm/usermanagement/pagenavigations")
public class UserMangPageController {
	@Autowired
	Commons comm;
	
	@Autowired
	private UserMangPageService userManagPageService;
	
	/* This method is used to create a new page */
	@PostMapping("/addpage")
	public ResponseEntity<PageNavigationEntity> addPage(@RequestParam("nav_page_name") String nav_page_name,
			@RequestParam("nav_page_desc") String nav_page_desc, @RequestParam("comment") String comment,
			@RequestParam("page_order") int page_order, @RequestParam("parent_page_id") int parent_page_id)
			throws IOException {
		PageNavigationEntity page = userManagPageService.addPage(nav_page_name, nav_page_desc, comment,
				page_order, parent_page_id);
		return new ResponseEntity<>(page, OK);
	}
	
	/* This method is used to update page navigation */
	@PostMapping("/updatepage")
	public ResponseEntity<PageNavigationEntity> updatePage(@RequestParam("id") long id,@RequestParam("nav_page_name") String nav_page_name,
			@RequestParam("nav_page_desc") String nav_page_desc,@RequestParam("page_order") int page_order) throws IOException {
		PageNavigationEntity updatedRole = userManagPageService.updatePage(id, nav_page_name, nav_page_desc,page_order);
		return new ResponseEntity<>(updatedRole, OK);
	}

	/* This method is used to get all the active KPI's */
	@GetMapping("/getmenus")
	public ResponseEntity<List<UserManagementModel>> getMenus() {
		List<UserManagementModel> getMenusList = userManagPageService.getMenus();
		return new ResponseEntity<>(getMenusList, OK);
	}
	
	/* This method is used to get all the active KPI's */
	@GetMapping("/getmenuList/{networkId}")
	public ResponseEntity<List<PageNavigationEntity>> getMenuListByRole(@PathVariable String networkId) {
		List<PageNavigationEntity> getMenusList = userManagPageService.getMenuListByRole(networkId);
		return new ResponseEntity<>(getMenusList, OK);
	}
	
	/* This method is used to get all the Favourite Menu's */
	@GetMapping("/getfavmenuList/{empId}")
	public ResponseEntity<List<PageNavigationEntity>> getFavMenuList(@PathVariable String empId) {
		List<PageNavigationEntity> getMenusList = userManagPageService.getFavMenuList(empId);
		return new ResponseEntity<>(getMenusList, OK);
	}

	/* This method is used to get all the pages */
	@GetMapping("/getpages")
	public ResponseEntity<List<UserManagementModel>> getPages() {
		List<UserManagementModel> getList = userManagPageService.getPages();
		return new ResponseEntity<>(getList, OK);
	}
	
	/* This method is used to get all the sub menus list */
	@GetMapping("/getsubmenus/{roleid}/{menuid}")
	public ResponseEntity<List<UserManagementModel>> getSubMenus(@PathVariable("roleid") int roleid,@PathVariable("menuid") int menuid) {
		List<UserManagementModel> getRegionsList = userManagPageService.getSubMenus(roleid,menuid);
		return new ResponseEntity<>(getRegionsList, OK);
	}
}
