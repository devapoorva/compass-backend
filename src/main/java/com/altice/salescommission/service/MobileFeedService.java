package com.altice.salescommission.service;

import java.util.List;
import java.util.Map;

import com.altice.salescommission.model.MobileFeedModel;

public interface MobileFeedService {
	List<MobileFeedModel> getMobileFeedResults(MobileFeedModel mobileFeedModel);
	
	List<MobileFeedModel> getMobileFeedDataById(int mobfeedid);
	
	Map<String, Boolean> updateMobileFeed(MobileFeedModel feedModel);
	
	MobileFeedModel addMobileFeed(MobileFeedModel feedModel);
}
