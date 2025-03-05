package com.altice.salescommission.reportframework;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;

public interface ReportFrameworkService {
	List<LinkedHashMap<String, String>> runReport(ReportFrameworkModel reportFrameworkModel);

	ReportInstanceModel addNewReport(String sqlquery, String report_name, String report_desc, String category,
			String report_type, String currentUser, int distId, String instanceName,String sqlParams, String sqlLables) throws DuplicateRecordException;

	ReportDistributionModel addDistribution(String distName, String distValue, String currentUser, String distType)
			throws DuplicateRecordException;

	ReportFrameworkModel updateReport(long id, String sqlquery, String report_name, String report_desc, String category,
			String report_type, String currentUser, String status)
			throws DuplicateRecordException, IdNotFoundException;
	
	ReportInstanceModel updateInstance(long id, String sqlParams, String instanceName, String scheduleStr,
			String status,String current_user,int distId,String sqlLabels)
			throws DuplicateRecordException, IdNotFoundException;

	ReportDistributionModel updateDistribution(int distId, String distName, String distValue, String currentUser,
			String distType) throws DuplicateRecordException, IdNotFoundException;

	ReportInstanceModel updateSchedule(long id, String minute, String hour, String day, String month, String week,
			String currentUser) throws DuplicateRecordException, IdNotFoundException;
	
	ReportInstanceModel createInstance(long id, int distId, String sqlParams, String currentUser, String instanceName) throws DuplicateRecordException, IdNotFoundException;

	List<ReportFrameworkModel> getReports();

	List<ReportFrameworkModel> getCategoryList();

	List<ReportDistributionModel> getDistributionList();

	List<ReportDistributionModel> getDistributionDropdown();

	List<ReportDistributionModel> getEmployees(String filter);

	List<ReportDistributionModel> getRoles();
	
	List<ReportInstanceModel> getInstances(int repid);
	
	List<ReportDistributionModel> getDistValues(int distId, String distType);
	
	ReportDistributionModel deleteDistribution(List<ReportDistributionModel> reportDistributionModel);
	
	ReportFrameworkModel deleteReport(List<ReportFrameworkModel> reportFrameworkModel);
	
	int deleteInstance(long id) throws IOException;
	
	int deleteValue(long id) throws IOException;
	
	
}
