package com.altice.salescommission.service;

import java.util.List;
import java.util.Map;

import com.altice.salescommission.model.HomeModel;

public interface HomeService {
	
	List<HomeModel> getCommPlansList(String empid, String role);
	
	List<HomeModel> getHomeCommPlansList(String empid, String role);
	
	List<HomeModel> getDisputeStatus(int complanid, int calrunid);

	List<HomeModel> getCalrunidsList(String scempid);

	List<HomeModel> getCommRevList(String calyear, String scempid, int comm_plan_id);

	Map<String, Float> loadCharts(int commyear, String scempid);

}
