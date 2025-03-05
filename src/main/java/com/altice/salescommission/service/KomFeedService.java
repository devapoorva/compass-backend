package com.altice.salescommission.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.altice.salescommission.entity.KomFeedEntity;
import com.altice.salescommission.model.CallDetailsModel;
import com.altice.salescommission.model.ComboStringModel;

public interface KomFeedService {
	Map<String, Boolean> deleteRowONWipByInfo(int id);

	List<ComboStringModel> getProduct(ComboStringModel comboStringModel);
	
	List<KomFeedEntity> getSalesRepIds(KomFeedEntity feedModel);
	
	List<KomFeedEntity> getFeedResults(KomFeedEntity feedModel);
	
	List<KomFeedEntity> getFeedResultsExcel(KomFeedEntity feedModel);
	
	KomFeedEntity saveWibByInfoData(KomFeedEntity feedModel);
	
	List<ComboStringModel> getProductNames(KomFeedEntity feedModel);
	
	List<ComboStringModel> getProductInfo(String productName, KomFeedEntity feedModel);
	
	List<ComboStringModel> getProductInfo1( KomFeedEntity feedModel);
	
	Map<String, Boolean> updateKomFeed(KomFeedEntity feedModel);
	
	KomFeedEntity getKomFeedDataById(int komfeedid);
	
	KomFeedEntity getKomFeedDataByCorpHouseRepId(int corp,String house,String salesrepid);
	
	List<KomFeedEntity> getKomFeedDataByRepId(int corp,String house,String salesrepid,int id);
	
	List<KomFeedEntity> getRateCodeInfo(int corp,String ratecd,String orderdt);
	
	List<KomFeedEntity> getReportingCentersInfo(int corp,String ratecd,String orderdt);
	
	List<KomFeedEntity> getOOLDetails();
	
	List<CallDetailsModel> getCallDetails(CallDetailsModel callDtlModel);
	
	Map<String, Boolean> deleteCallByKomCallID(int id);
	
	CallDetailsModel saveCallDetails(CallDetailsModel callDtlModel);
	
	CallDetailsModel updateCallDetails(CallDetailsModel callDtlModel);
	
	KomFeedEntity addNewAdjustment(KomFeedEntity feedModel);

	KomFeedEntity addAdjustment(KomFeedEntity komFeedModel);
	
	
}
