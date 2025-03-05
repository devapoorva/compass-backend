package com.altice.salescommission.service;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.altice.salescommission.entity.CommissionSummaryEntity;
import com.altice.salescommission.entity.UploadJsonEntity;
import com.altice.salescommission.entity.WorkflowMessageEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.model.KpiModel;
import com.altice.salescommission.model.UploadJsonHeadersModel;

public interface CommissionSummaryService extends AbstractBaseService<CommissionSummaryEntity, Long> {

	List<Object> getStreamReports(String reporttype, String commPlanId, String calRunId, String payrollDt)
			throws NumberFormatException, ParseException;

//	List<LinkedHashMap<String, String>> getReport(String reporttype, String commPlanId, String calRunId,
//			String payrollDt);

	List<CommissionSummaryEntity> getSqlQueries();

	List<UploadJsonEntity> getDataByPageName(String pageName, int commplanid, int supervisorStatus,
			String selectedSalesChannel);

	List<LinkedHashMap<String, Object>> runReport(String parms, String pageName, String gridName, int commplanid,
			String selectedSalesChannel, String usertype);

	List<LinkedHashMap<String, String>> runReportSearch(String parms, String pageName, String gridName, int commplanid);

	List<UploadJsonHeadersModel> getHeaders(int id, String salesChannel, int supervisorStatus);

	List<UploadJsonHeadersModel> getButtons(int id);

	List<UploadJsonHeadersModel> getCalcRunningStatus(int id, int calrunid);

	List<UploadJsonHeadersModel> getSupervisorStatus(int id);

	List<UploadJsonHeadersModel> getPageName(int commplanid, int kpiid);

	List<CommissionSummaryEntity> getCommSumDropDowns();

	List<CommissionSummaryEntity> getCommSumSupDropDowns(String empid);

	List<CommissionSummaryEntity> getCommSumUserDropDowns(String empid);

	List<WorkflowMessageEntity> getTrackidCount(String trackid);

	List<KpiModel> getKpiActiveList(int commplanid, String effdate);

	List<CommissionSummaryEntity> getHeaderGroups();

	WorkflowMessageEntity insertDispute(WorkflowMessageEntity workflowMessageEntity)
			throws DuplicateRecordException, IdNotFoundException, ParseException;

	WorkflowMessageEntity insertDisputeStatic(WorkflowMessageEntity workflowMessageEntity)
			throws DuplicateRecordException, IdNotFoundException, ParseException;

	List<Map<String, Object>> getCrosstab();
}