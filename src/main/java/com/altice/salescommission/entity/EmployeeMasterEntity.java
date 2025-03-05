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
@Table(name = "c_employee_master")
public class EmployeeMasterEntity {

	@Transient
	private String name;
	private Date effective_date;
	@Column(name = "employee_id")
	private String employeeId;
	private String network_id;
	
	@Column(name = "sc_emp_id")
	private String id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long rowid;

	@Transient
	private String commission_plan;
	@Transient
	private String comPlanTwo;
	@Transient
	private String comPlanThree;

	private String user_type;
	private String sales_rep_id;
	@Transient
	private String assc_corps;
	private String sales_rep_channel;
	private String operator_id;
	private String sales_rep_type;
	private Date soft_termination_date;
	@Column(name = "profile_status")
	private String profileStatus;

	private String email_address;
	private String first_name;
	private String middle_name;
	private String last_name;
	@Column(name = "user_role")
	private String userRole;
	private String sales_rep_channel_desc;
	private Date joining_date;
	private String work_status;
	private Date termination_date;
	private Date vacation_start_date;
	private Date vacation_end_date;
	private Integer comm_plan_id;
	private String commissionable;
	private String supervisor_id;
	private String calendar_type;
	// private String current_rec_flag;
	private Date created_dt;
	private String created_by;
	private Date updated_dt;
	private String updated_by;
	private String supervisor_name;
	private String supervisor_pref_name;
	private String pref_first_name;
	private String pref_last_name;
	private Integer std_hours;

	@Transient
	private String commPlanId;
	@Transient
	private int corp;
	@Transient
	private String language;
	@Transient
	private String activeCommPeriod;
	
	@Transient
	private String curDate;
	@Transient
	private String prevDateOne;
	@Transient
	private String prevDateTwo;
	
	@Transient
	private String checkboxchecked;
	@Transient
	private String sales_channel_desc;
	@Transient
	private String pageStatus;
	

}
