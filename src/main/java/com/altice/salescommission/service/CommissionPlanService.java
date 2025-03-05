package com.altice.salescommission.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;

import org.springframework.web.multipart.MultipartFile;

import com.altice.salescommission.entity.CalendarEntity;
import com.altice.salescommission.entity.CommissionDetailValuesEntity;
import com.altice.salescommission.entity.CommissionPlanDetailEntity;
import com.altice.salescommission.entity.CommissionPlanEntity;
import com.altice.salescommission.entity.DependentKPIEntity;
import com.altice.salescommission.entity.DependentScoreKPIEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.model.AdjustMentResultModel;
import com.altice.salescommission.model.BossCallMappingModel;
import com.altice.salescommission.model.CommissionPlanDetailTierRangeModel;
import com.altice.salescommission.model.FreeFormModel;

public interface CommissionPlanService {

	CommissionPlanEntity insertCommPlan(CommissionPlanEntity commissionPlanModel)
			throws DuplicateRecordException, IdNotFoundException, ParseException;

	CommissionPlanEntity replicateCommPlan(CommissionPlanEntity commissionPlanModel)
			throws DuplicateRecordException, IdNotFoundException, ParseException;
	
	CommissionPlanDetailEntity addKpitoCommPlan(List<CommissionPlanDetailTierRangeModel> commissionPlanDetailModel)
			throws ParseException, DuplicateRecordException;
	
//	CommissionPlanEntity addCommPlan(CommissionPlanEntity commissionPlanModel)
//			throws DuplicateRecordException, IdNotFoundException, ParseException;

	CommissionPlanEntity updateSingleFile(MultipartFile file, Long id) throws IOException;

	List<CommissionPlanEntity> getChargebackTypes();

	List<CommissionPlanEntity> getCommissionPlans();
	
	List<CommissionPlanEntity> getSalesChannels();

	List<CommissionPlanEntity> getCommPlans();

	

	CommissionPlanEntity updateCommPlan(List<CommissionPlanEntity> commissionPlanModel);

	CommissionPlanDetailEntity updateKpiToCommPlan(List<CommissionPlanDetailTierRangeModel> commissionPlanDetailModels);

	

	int deleteKpiToCommPlan(CommissionPlanDetailTierRangeModel commissionPlanDetailModels);

	public List<CommissionPlanDetailEntity> getKpiPlans(int commPlanId, String effDate);

	public List<CommissionPlanDetailEntity> getImage(int commPlanId, String effDate);

	List getMeasureTypes();

	List<CommissionDetailValuesEntity> getTierRangeValues(int commPlanDtlId);

	public List<CommissionPlanDetailTierRangeModel> getKpiPlansToReplicate(int commPlanId, String effDate)
			throws ParseException;

	Map<String, Boolean> deleteCommDetailValues(int id);

	Map<String, Boolean> deleteCalcVar(int id);

	AdjustMentResultModel addAdjustMent(AdjustMentResultModel addAdjustMentModel);

	public List<AdjustMentResultModel> getAdjustMentResultList(int commPeriod, String salesChannel, int commPlan);

	CommissionPlanEntity getCommplanByAsOfDdate(int id);

	List<CommissionPlanEntity> getAsOfDate(int commPlanId);

	List<CommissionPlanEntity> getAllCommPlans();

	List<CalendarEntity> validateEffictiveDt(int commPlanId);

	List<FreeFormModel> getFreeFormSerchResults(String searchwith);

	BossCallMappingModel saveEmaildId(BossCallMappingModel callModel);

	List<DependentKPIEntity> getDpndntKPI(int kpiId, int comp_plan_id, int comm_plan_dtl_id, String edate);

	List<DependentScoreKPIEntity> getDpndntScoreKPI(int kpiId, int comp_plan_id, int comm_plan_dtl_id, String edate);

	DependentKPIEntity addDpndntKpitoCommPlan(List<DependentKPIEntity> dependentKPIModelList);

	DependentKPIEntity updateDpndntKpitoCommPlan(List<DependentKPIEntity> dependentKPIModelList);

	boolean sendEmail(BossCallMappingModel callModel) throws AddressException;

	String getEmail();

	int addDpndntScoreKpitoCommPlan(String kpi_name, String kpi_weight, String complex_kpi_id, String kpi_id,
			String created_by, String comp_plan_id) throws DuplicateRecordException;

	int updateDependntScoreKpitoCommplan(String kpi_name, String kpi_weight, String complex_kpi_id, String kpi_id,
			String created_by, String comp_plan_id, String comm_plan_dtl_id, String id) throws DuplicateRecordException;

	List<CommissionPlanDetailEntity> getmeasureagainstList();

	List<CommissionPlanDetailEntity> getSalesChannelsList();

	CommissionPlanEntity updateCommPlanStatusToActive(List<CommissionPlanEntity> updateCommPlanStatusToActive);

	CommissionPlanEntity updateCommPlanStatusToIactive(List<CommissionPlanEntity> updateCommPlanStatusToIactive);

}
