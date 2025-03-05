package com.altice.salescommission.service;

import java.util.List;
import java.util.Map;

import com.altice.salescommission.model.BossCallMappingModel;

public interface BossCallMappingService {
	BossCallMappingModel saveCallData(BossCallMappingModel callModel);
	
	BossCallMappingModel updateCallMapping(BossCallMappingModel callModel);
	
	List<BossCallMappingModel> getCallMapingData(String feedDate);
	
	Map<String, Boolean> deleteCallMapData(String id, String validFromDt);
	
	
}
