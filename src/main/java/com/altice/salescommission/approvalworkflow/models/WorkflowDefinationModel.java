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
@Table(name = "c_workflow_defination")
public class WorkflowDefinationModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "defination_id", nullable = false, updatable = false)
	private Long id;
	@Column(name = "flow_id")
	private int flowId;
	private int level;
	private int stage;
	private String approval_type;
	private String approval_value;
	private int timeout;
	private String timeout_action;
	private String approval_criteria;
	private String stage_default_criteria;
	private String stage_criteria_sql;
	private String stage_criteria_code;
	private Date created_dt;
	private String created_by;
	private Date update_dt;
	private String updated_by;

	@Transient
	private String trackingId;
}
