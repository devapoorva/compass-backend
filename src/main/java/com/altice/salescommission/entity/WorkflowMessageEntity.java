package com.altice.salescommission.entity;

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
@Table(name = "t_workflow_message")
public class WorkflowMessageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rowid", nullable = false, updatable = false)
	private Long id;
	private String tracking_id;
	private String message_content;
	private String created_by;
	private Date created_dt;
	private String chargeback_delete_id;
	private int comm_plan_id;
	private int complex_kpiid;
	
	@Transient
	private String employee_id;
	@Transient
	private String kpiid;
	@Transient
	private int complanid;
	@Transient
	private String sc_emp_id;
	@Transient
	private String corp;
	@Transient
	private String house;
	@Transient
	private String cust;
	
	@Transient
	private String approval_name;
	@Transient
	private Date pending_from_dt;
	@Transient
	private String cdt;
	@Transient
	private String pdt;
	@Transient
	private Date approved_dt;
	@Transient
	private String approved_by;
	@Transient
	private String status;
	@Transient
	private String workflow_name;
	@Transient
	private String trackingId;
	@Transient
	private int flow_id;
	@Transient
	private String comments;
	@Transient
	private int kom_feed_id;
	@Transient
	private String sales_rep_id;
	@Transient
	private String calRunId;
	@Transient
	private String isValueDisabled;
	@Transient
	private String final_status;
	@Transient
	private int accnum;
	@Transient
	private int mob_feed_id;
	@Transient
	private String recordType;
	@Transient
	private String loggedin_role;
	@Transient
	private String salesRepId;
	@Transient
	private String empName;
	@Transient
	private String sales_channel;
	
}
