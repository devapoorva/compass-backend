package com.altice.salescommission.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.altice.salescommission.entity.AdjustmentsDetailEntity;
import com.altice.salescommission.entity.KpiGoalsEntity;
import com.altice.salescommission.model.UpdateManualDataModel;

public interface UpdateManualDataService {
	
	AdjustmentsDetailEntity saveAdjustment(AdjustmentsDetailEntity adjustmentsDetailEntity);
	
	KpiGoalsEntity saveGoal(KpiGoalsEntity kpiGoalsEntity);
	
	int deleteAdjustment(int id) throws IOException;
	
	int deleteGoal(int id) throws IOException;
	
	Map<String, Boolean> updateAdjustment(AdjustmentsDetailEntity adjustmentsDetailEntity);
	
	Map<String, Boolean> updateGoal(KpiGoalsEntity kpiGoalsEntity);
	
	AdjustmentsDetailEntity getAdjustmentsById(int id);
	
	KpiGoalsEntity getGoalsById(int id);
	
	List<AdjustmentsDetailEntity> getAdjustmentUploadResults(AdjustmentsDetailEntity updateManualDataModel);
	
	List<KpiGoalsEntity> getGoalUploadResults(KpiGoalsEntity updateManualDataModel);

	List<UpdateManualDataModel> getkpis(String type);
	
	List<UpdateManualDataModel> getCommPlans();
	
	List<UpdateManualDataModel> getCalRunids();

}
