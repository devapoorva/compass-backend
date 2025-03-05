package com.altice.salescommission.approvalworkflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.altice.salescommission.approvalworkflow.models.WorkflowApprovalModel;

public interface WorkflowApprovalRepository  extends JpaRepository<WorkflowApprovalModel, Long> {
	public List<WorkflowApprovalModel> findByApprovalId(String approvalId);
}
