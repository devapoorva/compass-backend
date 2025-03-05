package com.altice.salescommission.approvalworkflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.altice.salescommission.approvalworkflow.models.WorkflowMasterModel;

@Repository
public interface WorkflowMasterRepository  extends JpaRepository<WorkflowMasterModel, Long>{

}
