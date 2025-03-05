package com.altice.salescommission.reportframework;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportFrameworkRepository extends JpaRepository<ReportFrameworkModel, Long> {
	ReportFrameworkModel findByReportName(String reportName);
}
