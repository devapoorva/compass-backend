package com.altice.salescommission.approvalworkflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.altice.salescommission.approvalworkflow.models.WorkflowDefinationModel;

public interface WorkflowDefinationRepository extends JpaRepository<WorkflowDefinationModel, Long> {
	public List<WorkflowDefinationModel> findByFlowId(int flowId);
}
