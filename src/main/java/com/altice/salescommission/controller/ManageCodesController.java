package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.EmployeeMasterEntity;
import com.altice.salescommission.model.ManageCodesModel;
import com.altice.salescommission.service.ManageCodesService;
import com.altice.salescommission.utility.Commons;

@RestController
@RequestMapping("/api/salescomm/managecodes")
public class ManageCodesController {

	@Autowired
	ManageCodesService manageCodesService;
	
	@Autowired
	Commons comm;
	
	/*Get the Active list of product from product_master table */
	@GetMapping("/getproducts")
	public ResponseEntity<List<ManageCodesModel>> getProducts() {
		List<ManageCodesModel> productslist = manageCodesService.getProducts();
		return new ResponseEntity<>(productslist, OK);
	}
	
	  @GetMapping("/getproductsbysc/{saleschannel}")
	    public ResponseEntity<List<ManageCodesModel>> getProductsBySalesChannel(@PathVariable String saleschannel) {
	        List<ManageCodesModel> productslist = manageCodesService.getProductsBySalesChannel(saleschannel);
	        return new ResponseEntity<>(productslist, OK);
	    }
	
	/*Get the  list of corpId's from corp_master table */
	@GetMapping("/getcorps")
	public ResponseEntity<List<ManageCodesModel>> getCorps() {
		List<ManageCodesModel> corpslist = manageCodesService.getCorps();
		return new ResponseEntity<>(corpslist, OK);
	}
	
	/*Get the list of assigned corps */
	@GetMapping("/getassignedcorps/{id}/{edate}")
	public ResponseEntity<List<ManageCodesModel>> getAssignedCorps(@PathVariable String id,@PathVariable String edate) {
		List<ManageCodesModel> corpslist = manageCodesService.getAssignedCorps(id,edate);
		return new ResponseEntity<>(corpslist, OK);
	}
	
	/*Get the  list of supervisors from c_employee_master table */
	@GetMapping("/getsupervisors")
	public ResponseEntity<List<EmployeeMasterEntity>> getSupervisors() {
		List<EmployeeMasterEntity> suplist = manageCodesService.getSupervisors();
		return new ResponseEntity<>(suplist, OK);
	}
	
	
	/*Get the  list of supervisors from c_employee_master table */
	@GetMapping("/getempsupervisors/{val}")
	public ResponseEntity<List<EmployeeMasterEntity>> getEmpSupervisors(@PathVariable String val) {
		List<EmployeeMasterEntity> suplist = manageCodesService.getEmpSupervisors(val);
		return new ResponseEntity<>(suplist, OK);
	}
	
	/*Get the  list of rate codes  from based on productId and corpId  */
	@GetMapping("/ratecodeslist/{productid}/{corp}")
	public ResponseEntity<List<ManageCodesModel>> getRateCodes(@PathVariable Long productid,@PathVariable Long corp) {
		List<ManageCodesModel> rateCodesList = manageCodesService.getRateCodes(productid,corp);
		return new ResponseEntity<>(rateCodesList, OK);
	}
	
	@GetMapping("/commPlanlist/{productid}")
	public ResponseEntity<List<String>> getCommPlans(@PathVariable Long productid) {
		List<String> commPlans = manageCodesService.getCommPlans(productid);
		return new ResponseEntity<>(commPlans, OK);
	}
	
	@PostMapping("/updateRateCodes")
    public ResponseEntity<ManageCodesModel> updateRateCodes(@RequestBody ManageCodesModel manageCodesModel) {
		ManageCodesModel updateRateCodes = manageCodesService.updateRateCodes(manageCodesModel);
        return new ResponseEntity<>(updateRateCodes, OK);
    }
	
	@PostMapping("/addRateCode")
    public ResponseEntity<ManageCodesModel> addRateCode(@RequestBody ManageCodesModel manageCodesModel) {
		ManageCodesModel addRateCode = manageCodesService.addRateCode(manageCodesModel);
        return new ResponseEntity<>(addRateCode, OK);
    }
}
