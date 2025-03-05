package com.altice.salescommission.approvalworkflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.altice.salescommission.approvalworkflow.models.WorkflowCriteriaModel;

public interface WorkflowCriteriaRepository  extends JpaRepository<WorkflowCriteriaModel, Long> {

}
