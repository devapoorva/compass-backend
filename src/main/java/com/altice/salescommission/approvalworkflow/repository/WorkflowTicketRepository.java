package com.altice.salescommission.approvalworkflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.approvalworkflow.models.TicketWorkflowDetailsModel;

public interface WorkflowTicketRepository extends JpaRepository<TicketWorkflowDetailsModel, Long> {
	public List<TicketWorkflowDetailsModel> findByTrackingId(String trackingId);

	@Query(value = "select level,stage,approval_value from c_workflow_defination p where flow_id =?1", nativeQuery = true)
	List<String> getLevels(int flowid);
	
	@Query(value = "select ticket_id from t_workflow_details twd where tracking_id = $1 and status = 'initiated' limit 1", nativeQuery = true)
	Long getInitiatedTicketDetailsId(String TrackingId);
	
	@Query(value = "select count(ticket_id) cnt from t_workflow_details twd where tracking_id = $1 and status = 'initiated' limit 1", nativeQuery = true)
	int getInitiatedTicketCount(String TrackingId);
}
