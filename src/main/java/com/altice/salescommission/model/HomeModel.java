package com.altice.salescommission.model;

import java.util.Date;

import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeModel {
	
	private String scempid;
	private String commissionplan;
	private String commyear;
	private String calyear;
	private String calmon;
	private float commission;
	private int revenue;
	private String employeeid;
	private int comm_plan_id;
	private String calendar_type;
	private String sales_channel;
	@Transient
	private String commissionval;
	@Transient
	private String revenueval;
	@Transient
	private String edate;
	@Transient
	private String comm_img_str;
	@Transient
	private Date effective_date;
	@Transient
	private Date valid_from_dt;
	@Transient
	private Date valid_to_dt;
	@Transient
	private String user_role;
	@Transient
	private int commPlanId;
	@Transient
	private String commPlan;
	@Transient
	private Date dispute_close_dt;
	@Transient
	private String calc_close_flag;
	
	
}
