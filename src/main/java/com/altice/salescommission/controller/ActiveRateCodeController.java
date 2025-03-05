package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.ActiveRateCodeEntity;
import com.altice.salescommission.service.ActiveRateCodeService;
import com.altice.salescommission.utility.Commons;

@RestController
@RequestMapping("/api/salescomm/reports/activeratecode")
public class ActiveRateCodeController {
	@Autowired
	ActiveRateCodeService activeRateCodeService;

	@Autowired
	Commons comm;

	/* Get the Active list of product from product_master table */
	@GetMapping("/getproducts")
	public ResponseEntity<List<ActiveRateCodeEntity>> getProducts() {
		List<ActiveRateCodeEntity> productslist = activeRateCodeService.getProducts();
		return new ResponseEntity<>(productslist, OK);
	}

	/* Get the Active list of sales channels from translate table */
	@GetMapping("/getsaleschannels")
	public ResponseEntity<List<ActiveRateCodeEntity>> getSalesChannels() {
		List<ActiveRateCodeEntity> salesChannelslist = activeRateCodeService.getSalesChannels();
		return new ResponseEntity<>(salesChannelslist, OK);
	}

	/* Get the list of corpId's from corp_master table */
	@GetMapping("/getcorps")
	public ResponseEntity<List<ActiveRateCodeEntity>> getCorps() {
		List<ActiveRateCodeEntity> corpslist = activeRateCodeService.getCorps();
		return new ResponseEntity<>(corpslist, OK);
	}

	/* Get the list of active rate codes from c_rate_code_master table */
	@GetMapping("/getactivecodedetails/{corps}/{sc}/{proid}/{validdt}")
	public ResponseEntity<List<ActiveRateCodeEntity>> getActiveCodeDetails(@PathVariable("corps") String corps,
			@PathVariable("sc") String sc, @PathVariable("proid") int proid, @PathVariable("validdt") Date validdt) {
		List<ActiveRateCodeEntity> getActiveCodeDetailsList = activeRateCodeService.getActiveCodeDetails(corps, sc, proid,
				validdt);
		return new ResponseEntity<>(getActiveCodeDetailsList, OK);
	}
}
