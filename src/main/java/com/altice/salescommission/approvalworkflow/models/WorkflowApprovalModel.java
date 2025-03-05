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
@Table(name = "c_workflow_approval")
public class WorkflowApprovalModel {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_approv", nullable = false, updatable = false)
	private Long id;
	private String name;
	private String employee_id;
	private String description;  
	private Date created_dt ;
	private String created_by ;
	private Date update_dt;
	private String updated_by ;
	@Column(name = "approval_id")
	private String approvalId;
}
