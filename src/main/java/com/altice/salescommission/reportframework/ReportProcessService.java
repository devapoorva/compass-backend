package com.altice.salescommission.reportframework;

import java.util.List;

public interface ReportProcessService {
	List<ReportProcessModel> getProcessReport();
	List<ReportProcessModel> getReportDetails();
}
