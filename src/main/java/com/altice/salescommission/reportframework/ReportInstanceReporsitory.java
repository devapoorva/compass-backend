package com.altice.salescommission.reportframework;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportInstanceReporsitory extends JpaRepository<ReportInstanceModel, Long> {

	
	ReportInstanceModel findByInstanceNameIgnoreCase(String instanceName);
	
	
}
