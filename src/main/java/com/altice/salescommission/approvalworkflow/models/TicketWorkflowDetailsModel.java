package com.altice.salescommission.approvalworkflow.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_workflow_details")
public class TicketWorkflowDetailsModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_id", nullable = false, updatable = false)
	private Long id;
	private int flow_id;
	@Column(name = "tracking_id")
	private String trackingId;
	private int level;
	private int stage;
	private int sub_level;
	private String approval_from;
	private String approval_name;
	private String approved_by;
	private String comments;
	private String status;
	private String final_status;
	private int doc_id;
	private Date pending_from_dt;
	private Date approved_dt;
	private Date created_dt;
	private String created_by;
	private Date update_dt;
	private String updated_by;
	private String submitted_by;
	private String against;
	
	@Transient
	private String approval_type;
	@Transient
	private String flowName;
	@Transient
	private String cdt;
	@Transient
	private String apdt;
	@Transient
	private String pdt;
	@Transient
	private String loggedinrole;
}
