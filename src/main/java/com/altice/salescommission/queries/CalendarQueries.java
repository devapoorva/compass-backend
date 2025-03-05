package com.altice.salescommission.queries;

public interface CalendarQueries {
	public final String GET_CALENDARS_LIST = "select comm_cal_id,valid_from_dt ,valid_to_dt,payroll_due_dt,pay_dt,unlock,off_cycle,cal_run_id,issalesrepaccess "
			+ "from c_comm_calendar "
			+ "where calendar_type = ? order by unlock desc, cal_run_id desc";
	public final String GET_ALL_CALENDAR_TYPES = "select distinct calendar_type from c_comm_calendar ccc order by calendar_type";
	public final String GET_CALENDAR_COMM_PERIODS = "select *,concat(valid_from_dt,'-',valid_to_dt) commPeriod from c_comm_calendar "
			+ "where calendar_type = ? order by valid_from_dt";
	public final String GET_CALENDAR_COMM_UNLOCK_PERIODS = "select *,concat(valid_from_dt,'-',valid_to_dt) commPeriod from c_comm_calendar "
			+ "where calendar_type = ? AND lower(unlock) = 'y' order by valid_from_dt";
	
	public final String GET_PAYROLL_REPORT_DATA ="select coalesce(round(sum(tmc.revenue),2),'0.00') revenue, "
			+ "coalesce(round((sum(tmc.commission_val::numeric)+tmc.adjustment::numeric),2),'0.00') commission_val,ccpm.comm_plan , "
			+ "coalesce(round((sum(tmc.commission_val::numeric)+(coalesce(tmc.adjustment::numeric,'0.00'))+(coalesce(tmc.incentive_amt::numeric,'0.00'))),2),'0.00') total, "
			+ "tmc.adjustment_comments ,coalesce(round(sum(tmc.incentive_amt::numeric),2),'0.00') incentive_amt ,concat(cem.last_name,',',cem.first_name) empname , "
			+ "cem.user_type,ccpm.sales_channel_desc,cem.sales_rep_id ,cem.employee_id,cem.sc_emp_id from c_comm_plan_master "
			+ "ccpm inner join c_comm_calendar ccc on(ccpm.calendar_type=ccc.calendar_type) inner join t_monthly_commission tmc "
			+ "on(tmc.comm_plan_id=ccpm.comm_plan_id and tmc.cal_run_id = ccc.cal_run_id) inner join c_employee_master cem on(cem.sc_emp_id=tmc.sc_emp_id) where "
			+ "ccc.cal_run_id::varchar = ? group by ccpm.comm_plan,empname,cem.user_type, ccpm.sales_channel_desc,tmc.adjustment_comments, "
			+ "tmc.incentive_amt,cem.sales_rep_id,cem.employee_id,cem.sc_emp_id,tmc.adjustment order by empname;";
	
	public final String GET_CALENDAR_DATA ="select valid_from_dt ,valid_to_dt from c_comm_calendar ccc order by valid_from_dt desc";
	
	public final String GET_USER_ROLES_DATA ="select role_name  from c_user_role_mgmt curm where role_status ='ACTIVE'";
	
	public final String GET_CAL_RUN_IDS ="select cal_run_id from c_comm_calrunids ccc";
}
