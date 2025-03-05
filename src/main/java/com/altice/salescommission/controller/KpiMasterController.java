package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.KpiMasterEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.model.KpiModel;
import com.altice.salescommission.service.KpiService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/commission/kpimaster")
public class KpiMasterController {

	private static final Logger logger = LoggerFactory.getLogger(KpiMasterController.class.getName());
	public static final String PRODUCT_DELETED_SUCCESSFULLY = "Product deleted successfully";

	@Autowired
	KpiService KpiService;

	@Autowired
	Commons comm;
	
	/* This method is used to create a new KPI */
	@PostMapping("/createkpi")
	public ResponseEntity<HttpResponse> createKPI(@RequestBody KpiModel kpiMasterModel)
			throws DuplicateRecordException {

			int result = KpiService.createKPI(kpiMasterModel);
			if (result == 0) {
				return comm.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create KPI");
			} else if (result == 2) {
				return comm.response(HttpStatus.CONFLICT, "KPI already exist");
			}
			return comm.response(OK, "KPI Inserted Successfully");

	}
	
	/* This method is used to create a new KPI */
	@PostMapping("/updatekpi")
	public ResponseEntity<HttpResponse> updateKPI(@RequestBody KpiModel kpiMasterModel)
			throws DuplicateRecordException, ParseException {

			int result = KpiService.updateKPI(kpiMasterModel);
			if (result == 0) {
				return comm.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create KPI");
			}
			return comm.response(OK, "KPI Inserted Successfully");

	}

	/* This method is used to create a new KPI */
//	@PostMapping("/insertkpi")
//	public ResponseEntity<HttpResponse> insertKPI(@RequestParam("kpi_name") String kpi_name,@RequestParam("kpi_priority") int kpi_priority,
//			@RequestParam("kpi_type") String kpi_type, @RequestParam("product") String product,
//			@RequestParam("kpi_calc_type") String kpi_calc_type, @RequestParam("currentUser") String currentUser,
//			@RequestParam("custom_routine") String custom_routine, @RequestParam("effective_date") Date effective_date,
//			@RequestParam("id") String kpiId, @RequestParam("product_type") String product_type,
//			@RequestParam("product_category") String product_category, @RequestParam("comboString") String comboString,
//			@RequestParam("productName") String productName, @RequestParam("stat") int stat,
//			@RequestParam("combo_position") String combo_position, @RequestParam("combo_values") String combo_values, @RequestParam("wstat_string") String wstat_string,
//			@RequestParam("include_count") String include_count, @RequestParam("include_revenue") String include_revenue, @RequestParam("feed_type") String feed_type)
//			throws DuplicateRecordException {
//
//			int result = KpiService.insertKPI(kpi_name, kpi_priority,kpi_type, product, kpi_calc_type, currentUser, custom_routine,
//					effective_date, kpiId, product_type, product_category, comboString, productName, stat,
//					combo_position, combo_values,wstat_string,include_count,include_revenue,feed_type);
//			if (result == 0) {
//				return comm.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert KPI");
//			} else if (result == 2) {
//				return comm.response(HttpStatus.CONFLICT, "KPI already exist");
//			}
//			return comm.response(OK, "KPI Inserted Successfully");
//
//	}

	/* This method is used to update KPI master */
//	@PostMapping("/updatekpi")
//	public ResponseEntity<HttpResponse> updateKpi(@RequestParam("id") long id,
//			@RequestParam("kpi_name") String kpi_name,@RequestParam("kpi_priority") int kpi_priority, @RequestParam("kpi_type") String kpi_type,
//			@RequestParam("kpi_status") String kpi_status, @RequestParam("currentUser") String currentUser,
//			@RequestParam("product") String product, @RequestParam("kpi_calc_type") String kpi_calc_type,
//			@RequestParam("custom_routine") String custom_routine, @RequestParam("effective_date") Date effective_date,
//			@RequestParam("product_type") String product_type,
//			@RequestParam("product_category") String product_category,
//			@RequestParam("depndentKpis") String depndentKpis, @RequestParam("comboString") String comboString,
//			@RequestParam("productName") String productName, @RequestParam("productid") String productid,
//			@RequestParam("combo_position") String combo_position, @RequestParam("combo_values") String combo_values, @RequestParam("wstat_string") String wstat_string,
//			@RequestParam("include_count") String include_count, @RequestParam("include_revenue") String include_revenue, @RequestParam("feed_type") String feed_type)
//			throws IOException, IdNotFoundException, DuplicateRecordException, ParseException {
//		
//		
//			int result = KpiService.updateKpi(id, kpi_name,kpi_priority, kpi_type, kpi_status, currentUser, product, kpi_calc_type,
//					custom_routine, effective_date, product_type, product_category, depndentKpis, comboString,
//					productName, productid, combo_position, combo_values,wstat_string,include_count,include_revenue,feed_type);
//			logger.info("controller result = " + result);
//			if (result == 0) {
//				return comm.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update KPI");
//			}
//			return comm.response(OK, "KPI Updated Successfully");
//		
//	}

	/* This method is used to delete a product */
	@DeleteMapping("/deleteproduct/{id}")
	public ResponseEntity<HttpResponse> deleteProduct(@PathVariable("id") Long id) throws IOException {
		KpiService.deleteProduct(id);
		return comm.response(OK, PRODUCT_DELETED_SUCCESSFULLY);
	}

	/* This method is used to update KPI's to active */
	@PutMapping("/updatekpistatustoactive")
	public ResponseEntity<KpiMasterEntity> updateKpiStatusToActive(@RequestBody List<KpiMasterEntity> kpiMasterModel) {
		KpiMasterEntity updateKpiStatusToActive = KpiService.updateKpiStatusToActive(kpiMasterModel);
		return new ResponseEntity<>(updateKpiStatusToActive, OK);
	}

	/* This method is used to update KPI's to inactive */
	@PutMapping("/updatekpistatustoinactive")
	public ResponseEntity<KpiMasterEntity> updateKpiStatusToIactive(@RequestBody List<KpiMasterEntity> kpiMasterModel) {
		KpiMasterEntity updateKpiStatusToInactive = KpiService.updateKpiStatusToInactive(kpiMasterModel);
		return new ResponseEntity<>(updateKpiStatusToInactive, OK);
	}

	/* This method is used to get all the products */
	@GetMapping("/getproducts")
	public ResponseEntity<List<KpiModel>> getProductsFromProductMaster() {
		List<KpiModel> productTypeslist = KpiService.getProductsFromProductMaster();
		return new ResponseEntity<>(productTypeslist, OK);
	}

	/* This method is used to get all the KPI type one's */
	@GetMapping("/getkpitypeonelist")
	public ResponseEntity<List<KpiModel>> getKpiTypeOneList() {
		List<KpiModel> getKpiTypeOneList = KpiService.getKpiTypeOneList();
		return new ResponseEntity<>(getKpiTypeOneList, OK);
	}

	/* This method is used to get all the combo strings */
	@GetMapping("/getcombostringslist")
	public ResponseEntity<List<KpiModel>> getComboStringsList() {
		List<KpiModel> getComboStringsList = KpiService.getComboStringsList();
		return new ResponseEntity<>(getComboStringsList, OK);
	}

	/* This method is used to get all the active KPI's */
	@GetMapping("/getkpiactivelist")
	public ResponseEntity<List<KpiModel>> getKpiActiveList() {
		List<KpiModel> kpiActiveList = KpiService.getKpiActiveList();
		return new ResponseEntity<>(kpiActiveList, OK);
	}
	
	/* This method is used to get all the active KPI's */
	@GetMapping("/getactivekpis")
	public ResponseEntity<List<KpiModel>> getActiveKpis() {
		List<KpiModel> getActiveKpis = KpiService.getActiveKpis();
		return new ResponseEntity<>(getActiveKpis, OK);
	}

	/* This method is used to get all the active KPI's by date */
	@GetMapping("/getkpiactivelistbydate/{effective_date}")
	public ResponseEntity<List<KpiModel>> getkpiactivelistByDate(
			@PathVariable("effective_date") String effective_date) {
		List<KpiModel> kpiActiveList = KpiService.getkpiactivelistByDate(effective_date);
		return new ResponseEntity<>(kpiActiveList, OK);
	}

	/* This method is used to get all the position and values by kpiid */
	@GetMapping("/getpositionsvalues/{kpiid}/{effective_date}")
	public ResponseEntity<List<KpiModel>> getPositionsValues(@PathVariable("kpiid") Long kpiid,
			@PathVariable("effective_date") Date effective_date) {
		List<KpiModel> kpiActiveList = KpiService.getPositionsValues(kpiid, effective_date);
		return new ResponseEntity<>(kpiActiveList, OK);
	}

	/* This method is used to get all the dependent KPI's by kpiid */
	@GetMapping("/getdependkpis/{kpiid}/{effective_date}")
	public ResponseEntity<List<KpiModel>> getDependKpis(@PathVariable("kpiid") Long kpiid,
			@PathVariable("effective_date") Date effective_date) {
		List<KpiModel> kpiActiveList = KpiService.getDependKpis(kpiid, effective_date);
		return new ResponseEntity<>(kpiActiveList, OK);
	}

	/* This method is used to get all the dependent KPI's by kpiid */
	@GetMapping("/getselectedproducts/{kpiid}/{effective_date}")
	public ResponseEntity<List<KpiModel>> getSelectedProducts(@PathVariable("kpiid") Long kpiid,
			@PathVariable("effective_date") Date effective_date) {
		List<KpiModel> kpiActiveList = KpiService.getSelectedProducts(kpiid, effective_date);
		return new ResponseEntity<>(kpiActiveList, OK);
	}

	/* This method is used to get all the active KPI's */
	@GetMapping("/getdependkpisdropdown")
	public ResponseEntity<List<KpiModel>> getDependKpisDropdown() {
		List<KpiModel> kpiActiveList = KpiService.getDependKpisDropdown();
		return new ResponseEntity<>(kpiActiveList, OK);
	}

	/* This method is used to get all the active KPI's for excel */
	@GetMapping("/getexcelkpiactivelist")
	public ResponseEntity<List<KpiModel>> getExcelKpiActiveList() {
		List<KpiModel> kpiActiveList = KpiService.getExcelKpiActiveList();
		return new ResponseEntity<>(kpiActiveList, OK);
	}

	/* This method is used to get all the KPI calc type's */
	@GetMapping("/getkpicalc")
	public ResponseEntity<List<KpiModel>> getKPICalcTypes() {
		List<KpiModel> kpiCalcList = KpiService.getKPICalcTypes();
		return new ResponseEntity<>(kpiCalcList, OK);
	}

	/* This method is used to get all the product types based on provided kpi id */
	@GetMapping("/getprotypes/{kpiid}/{effective_date}")
	public ResponseEntity<List<KpiModel>> getProductTypes(@PathVariable("kpiid") int kpiid,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate effective_date) {

		List<KpiModel> proTypeslist = KpiService.getProductTypes(kpiid, effective_date);
		return new ResponseEntity<>(proTypeslist, OK);
	}

	/*
	 * This method is used to get all the categories based on provided kpi id and
	 * product type
	 */
	@GetMapping("/getcats/{kpiid}/{proType}/{effective_date}")
	public ResponseEntity<List<KpiModel>> getCategories(@PathVariable("kpiid") int kpiid,
			@PathVariable("proType") String proType,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate effective_date) {
		List<KpiModel> catslist = KpiService.getCategories(kpiid, proType, effective_date);
		return new ResponseEntity<>(catslist, OK);
	}

	/*
	 * This method is used to get all the products based on provided kpi id, product
	 * type and category
	 */
	@GetMapping("/getpros/{kpiid}/{proType}/{cat}/{effective_date}")
	public ResponseEntity<List<KpiModel>> getProductsFromKpiProductDetail(@PathVariable("kpiid") int kpiid,
			@PathVariable("proType") String proType, @PathVariable("cat") String cat,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate effective_date) {
		List<KpiModel> catslist = KpiService.getProductsFromKpiProductDetail(kpiid, proType, cat, effective_date);
		return new ResponseEntity<>(catslist, OK);
	}
}
