package com.altice.salescommission.queries;

public interface HomeQueries {
	
	public final String GET_DISPUTE_CLOSSE_DATE = "select dispute_close_dt  from c_calc_manage ccm where comm_plan_id =? and cal_run_id =?";
	
	public final String GET_COMM_PLANS_LIST1 = "Select max(ccpm.effective_date) effective_date, SC_EMP_ID, ccpm.comm_plan commission_plan,"
			+ "ccpm.sales_channel_desc,encode(ccpm.comm_img,'base64') comm_img,ccpm.comm_plan_id "
			+ "from c_employee_master cem,c_comm_plan_master ccpm  "
			+ "where cem.comm_plan_id = ccpm.comm_plan_id and employee_id = ? and cem.user_role = ? "
			+ "group by ccpm.effective_date,SC_EMP_ID,comm_plan,sales_channel_desc,comm_img,ccpm.comm_plan_id   order by ccpm.effective_date desc limit 1";
	
	public final String GET_COMM_PLANS_LIST = "select max(cem.effective_date) effective_date,sc_emp_id  ,cem.comm_plan_id ,ccpm.comm_plan commission_plan,"
			+ "ccpm.sales_channel_desc,cem.user_role,encode(ccpm.comm_img,'base64') comm_img "
			+ "from c_employee_master cem "
			+ "inner join c_comm_plan_master ccpm on (ccpm.comm_plan_id=cem.comm_plan_id) "
			+ "where profile_status ='complete' and employee_id =? "
			+ "and ccpm.effective_date = (select max(ccpm1.effective_date) from c_comm_plan_master ccpm1 where ccpm.comm_plan_id = ccpm1.comm_plan_id  and ccpm1.effective_date <=(SELECT CURRENT_DATE))  "
			+ "group by sc_emp_id  ,cem.comm_plan_id ,sales_rep_channel,ccpm.comm_plan,ccpm.sales_channel_desc,cem.user_role,ccpm.comm_img order by sc_emp_id desc";
	
	public final String GET_HOME_COMM_PLANS_LIST = "select max(cem.effective_date) effective_date,sc_emp_id  ,cem.comm_plan_id ,ccpm.comm_plan commission_plan,"
			+ "ccpm.sales_channel_desc,cem.user_role,encode(ccpm.comm_img,'base64') comm_img "
			+ "from c_employee_master cem "
			+ "inner join c_comm_plan_master ccpm on (ccpm.comm_plan_id=cem.comm_plan_id and ccpm.display_flag='Y') "
			+ "where profile_status ='complete' and employee_id =? "
			+ "and ccpm.effective_date = (select max(ccpm1.effective_date) from c_comm_plan_master ccpm1 where ccpm.comm_plan_id = ccpm1.comm_plan_id  and ccpm1.effective_date <=(SELECT CURRENT_DATE))  "
			+ "group by sc_emp_id  ,cem.comm_plan_id ,sales_rep_channel,ccpm.comm_plan,ccpm.sales_channel_desc,cem.user_role,ccpm.comm_img order by sc_emp_id desc";
	
	public final String GET_CALRUNIDS_LIST = "Select distinct cal_run_id  from t_monthly_commission tmc where SC_EMP_ID = ? order by cal_run_id desc";
	
	public final String GET_CALRUNIDS_BY_YEAR_LIST = "select distinct cal_run_id from t_monthly_commission tmc where SC_EMP_ID =? and cal_run_id::varchar like ? and exists ( select 'Y' from c_comm_calendar ccc where tmc.cal_run_id  = ccc.cal_run_id and tmc.calendar_type = ccc.calendar_type  and ccc.issalesrepaccess ='Y')";
	
	public final String GET_CALRUNIDS_RECENT_DATA = "select ckm.kpi_name ,tmc.commission_val,cal_run_id "
			+ "from t_monthly_commission tmc,c_kpi_master ckm,c_employee_master cem where tmc.kpi_id =ckm.kpi_id and tmc.sc_emp_id =cem.sc_emp_id "
			+ "and cem.employee_id =? and tmc.commission_val !=0 and cal_run_id = (select max(cal_run_id) from t_monthly_commission where employee_id =?) "
			+ "order by cal_run_id desc";
	
	public final String GET_COMM_REV_LIST1 = "select distinct on(tmc.cal_run_id) cal_run_id,ccpd.effective_date ,cast(round(sum(tmc.commission_val::numeric),2) as money) commission_val,"
			+ "cast(round(sum(tmc.revenue::numeric),2) as money) revenue,tmc.sc_emp_id,tmc.comm_plan_id,tmc.calendar_type "
			+ "from t_monthly_commission tmc,c_comm_plan_detail ccpd "
			+ "where tmc.kpi_id =ccpd.kpi_id and tmc.comm_plan_id =ccpd.comm_plan_id and tmc.sc_emp_id =? and tmc.comm_plan_id=? "
			+ "and tmc.cal_run_id::varchar = ? and tmc.cal_run_id::varchar like ? "
			+ "group by tmc.cal_run_id,tmc.sc_emp_id,tmc.comm_plan_id,tmc.calendar_type,ccpd.effective_date";
	
	public final String GET_COMM_REV_LIST = "select distinct on(tmc.cal_run_id) tmc.cal_run_id,ccpd.effective_date ,"
			+ "cast(round(sum(tmc.commission_val::numeric+coalesce(tmc.adjustment::numeric,0)),2) as money) commission_val,"
//			+ "cast(round(sum(tmc.revenue::numeric),2) as money) revenue,"
			+ "cast((case when ccpm.sales_channel_desc = 'Web Assist' then coalesce(round((select tmc2.revenue from t_monthly_commission tmc2 where tmc2.cal_run_id =tmc.cal_run_id  and tmc2.comm_plan_id=tmc.comm_plan_id  \r\n"
			+ " and tmc2.sc_emp_id =tmc.sc_emp_id and tmc2.kpi_id =54  limit 1),2),'0.00') else coalesce(round(sum(tmc.revenue::numeric),2),'0.00') end) as money) revenue,"
			+ "tmc.sc_emp_id,tmc.comm_plan_id,tmc.calendar_type, ccc.valid_from_dt ,ccc.valid_to_dt,"
			+ "(case when ccm.calc_close_flag = 'Y' then 'Closed' when ccm.calc_close_flag = 'N' then 'Ongoing' else 'Ongoing' end) calc_close_flag   "
			+ "from t_monthly_commission tmc "
			+ "inner join c_comm_plan_master ccpm on (ccpm.comm_plan_id=tmc.comm_plan_id) "
			+ "inner join c_comm_plan_detail ccpd on (ccpm.row_id=ccpd.commplan_rowid and ccpd.kpi_id=tmc.kpi_id and ccpd.comm_plan_id=tmc.comm_plan_id and ccpd.display_flg ='Y')  "
			+ "inner join c_comm_calendar ccc on (ccc.cal_run_id = tmc.cal_run_id)  "
			+ "left join c_calc_manage ccm on(ccm.cal_run_id=tmc.cal_run_id and ccm.comm_plan_id=tmc.comm_plan_id) "
			+ "where tmc.cal_run_id::varchar =? and tmc.cal_run_id::varchar like ? and tmc.comm_plan_id =? and tmc.sc_emp_id =? "
			+ "and ccpd.effective_date = (select max(ccpd1.effective_date) from c_comm_plan_detail ccpd1 "
			+ "where ccpd.comm_plan_id = ccpd1.comm_plan_id  and ccpd1.effective_date <= (select current_date)) "
			+ "group by tmc.cal_run_id,tmc.sc_emp_id,tmc.comm_plan_id,tmc.calendar_type,ccpd.effective_date, ccc.valid_from_dt ,ccc.valid_to_dt,ccm.calc_close_flag,ccpm.sales_channel_desc";
	
}
