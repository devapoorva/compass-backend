package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.KomFeedEntity;
import com.altice.salescommission.model.MobileFeedModel;
import com.altice.salescommission.service.MobileFeedService;
import com.altice.salescommission.serviceImpl.MobileFeedServiceImpl;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/mobilefeed")
public class MobileFeedController {
	private static final Logger logger = LoggerFactory.getLogger(MobileFeedController.class.getName());
	@Autowired
	MobileFeedService mobileFeedService;
	@Autowired
	Commons commons;

	@PostMapping("/getmobilefeedresults")
	public ResponseEntity<List<MobileFeedModel>> getMobileFeedResults(@RequestBody MobileFeedModel mobileFeedModel) {
		List<MobileFeedModel> mobileFeedModelList = mobileFeedService.getMobileFeedResults(mobileFeedModel);
		return new ResponseEntity<>(mobileFeedModelList, OK);
	}

	@GetMapping("/getmobilefeedatabyid/{mobfeedid}")
	public ResponseEntity<List<MobileFeedModel>> getKomFeedDataById(@PathVariable int mobfeedid) throws ParseException {
		List<MobileFeedModel> komFeedModel = mobileFeedService.getMobileFeedDataById(mobfeedid);
		return new ResponseEntity<>(komFeedModel, OK);
	}

	@PutMapping("/updatemobilefeed")
	public ResponseEntity<HttpResponse> updateKomFeed(@RequestBody MobileFeedModel feedModel) {
		Map<String, Boolean> response = new HashMap<>();
		response = mobileFeedService.updateMobileFeed(feedModel);
		logger.info("response = " + response);
		return commons.response(OK, "Updated Sucessfully");
	}

	@PostMapping("/addobilefeed")
	public ResponseEntity<MobileFeedModel> addMobileFeed(@RequestBody MobileFeedModel feedModel) {
		MobileFeedModel response = mobileFeedService.addMobileFeed(feedModel);
		logger.info("response = " + response);
		return new ResponseEntity<>(response, OK);
	}
}
