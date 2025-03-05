package com.altice.salescommission.approvalworkflow.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "c_workflow_criteria")
public class WorkflowCriteriaModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "criteria_id", nullable = false, updatable = false)
	private Long id ;
	private int flow_id;
	private int level;
	private int stage;
	private String criteria_type;
	private String default_criteria;
	private String criteria_sql;
	private String criteria_code;
	private Date created_dt;
	private String created_by;
	private Date update_dt;
	private String updated_by;
}
