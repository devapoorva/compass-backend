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

import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.model.CorpEntity;
import com.altice.salescommission.service.ManageRegionsService;
import com.altice.salescommission.utility.Commons;

@RestController
@RequestMapping("/api/salescomm/regions/manageregions")
public class ManageRegionsController {
	@Autowired
	Commons comm;

	@Autowired
	private ManageRegionsService manageRegionsService;

	/* This method is used to update majorarea details from corp mater table */
	@PostMapping("/updatemajorarea")
	public ResponseEntity<Integer> updateMajorArea(@RequestParam("majorarea") String majorarea,
			@RequestParam("orgname") String orgname, @RequestParam("currentUser") String currentUser)
			throws IOException {
		int updatedCorp = manageRegionsService.updateMajorArea(majorarea, orgname, currentUser);
		return new ResponseEntity<>(updatedCorp, OK);
	}

	/* This method is used to update region details from corp mater table */
	@PostMapping("/updateregion")
	public ResponseEntity<Integer> updateRegion(@RequestParam("region") String region,
			@RequestParam("majorarea") String majorarea, @RequestParam("orgname") String orgname,
			@RequestParam("currentUser") String currentUser) throws IOException {
		int updatedCorp = manageRegionsService.updateRegion(region, orgname, currentUser, majorarea);
		return new ResponseEntity<>(updatedCorp, OK);
	}

	/* This method is used to update area details from corp mater table */
	@PostMapping("/updatearea")
	public ResponseEntity<Integer> updateArea(@RequestParam("region") String region,
			@RequestParam("majorarea") String majorarea, @RequestParam("area") String area,
			@RequestParam("orgname") String orgname, @RequestParam("currentUser") String currentUser)
			throws IOException {
		int updatedCorp = manageRegionsService.updateArea(area, orgname, currentUser,majorarea,region);
		return new ResponseEntity<>(updatedCorp, OK);
	}

	/* This method is used to update corp details from corp mater table */
	@PostMapping("/updatecorp")
	public ResponseEntity<Integer> updateCorps(@RequestParam("corp") String corp, @RequestParam("area") String area,
			@RequestParam("status") String status, @RequestParam("fieldstatus") String fieldstatus,
			@RequestParam("mainCorpName") String mainCorpName, @RequestParam("currentUser") String currentUser)
			throws IOException {
		int updatedCorp = manageRegionsService.updateCorps(corp, area, status, fieldstatus, mainCorpName, currentUser);
		return new ResponseEntity<>(updatedCorp, OK);
	}

	/* This method is used to create a new role */
	@PostMapping("/insertmajorarea")
	public ResponseEntity<CorpEntity> addMajorArea(@RequestParam("id") Long id, @RequestParam("corp") String corp,
			@RequestParam("majorarea") String majorarea, @RequestParam("region") String region,
			@RequestParam("area") String area, @RequestParam("currentUser") String currentUser)
			throws IOException, DuplicateRecordException {
		CorpEntity role = manageRegionsService.addMajorArea(id, corp, majorarea, region, area, currentUser);
		return new ResponseEntity<>(role, OK);
	}

	/* This method is used to get all the major areas list */
	@GetMapping("/getmajorareas")
	public ResponseEntity<List<CorpEntity>> getMajorAreas() {
		List<CorpEntity> getMajorAreasList = manageRegionsService.getMajorAreas();
		return new ResponseEntity<>(getMajorAreasList, OK);
	}
	
	/* This method is used to get all the regions list for excel*/
	@GetMapping("/getallmanageregions")
	public ResponseEntity<List<CorpEntity>> getAllRegionsData() {
		List<CorpEntity> getMajorAreasList = manageRegionsService.getAllRegionsData();
		return new ResponseEntity<>(getMajorAreasList, OK);
	}

	/* This method is used to get all the regions list */
	@GetMapping("/getregions/{majorarea}")
	public ResponseEntity<List<CorpEntity>> getRegions(@PathVariable("majorarea") String majorarea) {
		List<CorpEntity> getRegionsList = manageRegionsService.getRegions(majorarea);
		return new ResponseEntity<>(getRegionsList, OK);
	}

	/* This method is used to get all the areas list */
	@GetMapping("/getareas/{majorarea}/{region}")
	public ResponseEntity<List<CorpEntity>> getAreas(@PathVariable("majorarea") String majorarea,@PathVariable("region") String region) {
		List<CorpEntity> getAreasList = manageRegionsService.getAreas(majorarea,region);
		return new ResponseEntity<>(getAreasList, OK);
	}

	/* This method is used to get all the areas list */
	@GetMapping("/getallareas")
	public ResponseEntity<List<CorpEntity>> getAllAreas() {
		List<CorpEntity> getAllAreasList = manageRegionsService.getAllAreas();
		return new ResponseEntity<>(getAllAreasList, OK);
	}

	/* This method is used to get all the regions list */
	@GetMapping("/getallregions")
	public ResponseEntity<List<CorpEntity>> getAllRegions() {
		List<CorpEntity> getAllAreasList = manageRegionsService.getAllRegions();
		return new ResponseEntity<>(getAllAreasList, OK);
	}

	/* This method is used to get all the roles list */
	@PostMapping("/getcorps")
	public ResponseEntity<List<CorpEntity>> getCorps(@RequestParam("majorarea") String majorarea,
			@RequestParam("region") String region, @RequestParam("area") String area) {
		System.out.println("majorarea = "+majorarea);
		List<CorpEntity> getCorpsList = manageRegionsService.getCorps(majorarea, region, area);
		return new ResponseEntity<>(getCorpsList, OK);
	}
	
	/* This method is used to get all the roles list */
	@GetMapping("/getmareas/{searchVal}")
	public ResponseEntity<List<CorpEntity>> getMAreas(@PathVariable("searchVal") String searchVal) {
		List<CorpEntity> getRegionsList = manageRegionsService.getMAreas(searchVal);
		return new ResponseEntity<>(getRegionsList, OK);
	}
	
	/* This method is used to get all the active major areas list */
	@GetMapping("/getactivemajorareas")
	public ResponseEntity<List<CorpEntity>> getActiveMajorAreas() {
		List<CorpEntity> getActiveMajorAreasList = manageRegionsService.getActiveMajorAreas();
		return new ResponseEntity<>(getActiveMajorAreasList, OK);
	}
	
	/* This method is used to get all the active corps list by major area */
	@GetMapping("/getactivecorps")
	public ResponseEntity<List<CorpEntity>> getActiveCorpsByMajorAreas() {
		List<CorpEntity> getActiveMajorAreasList = manageRegionsService.getActiveCorpsByMajorAreas();
		return new ResponseEntity<>(getActiveMajorAreasList, OK);
	}

}
