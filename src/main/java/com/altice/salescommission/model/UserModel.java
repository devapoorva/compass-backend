package com.altice.salescommission.model;

import java.util.Date;

import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

	public UserModel(String name, String employeeId) {
		this.name = name;
        this.employeeId = employeeId;
	}
	
	private String name;
	private String edate;
	
	private String employeeId;
	private String network_id;

	private String id;
	private String commPlanId;
	private String commission_plan;
	private String comPlanTwo;
	private String comPlanThree;
	
	private String supname1;
	private String supname2;
	private String supname3;

	private String user_type;
	private String sales_rep_id;
	private String assc_corps;
	private String sales_rep_channel;
	private String operator_id;
	private String sales_rep_type;
	private Date soft_termination_date;
	private String softdate;
	private String profileStatus;

	private String email_address;
	private String first_name;
	private String middle_name;
	private String last_name;
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
	private Date created_dt;
	private String created_by;
	private Date updated_dt;
	private String updated_by;
	private String supervisor_name;
	private String supervisor_pref_name;
	private String pref_first_name;
	private String pref_last_name;
	private Integer std_hours;

	
	private int corp;
	private String language;
	private String activeCommPeriod;

	private String curDate;
	private String prevDateOne;
	private String prevDateTwo;
	private Date effective_date;
	@Transient
	private String supname;
	@Transient
	private String sales_channel_desc;
	private int rowid;
	
	
	
	
	

}
