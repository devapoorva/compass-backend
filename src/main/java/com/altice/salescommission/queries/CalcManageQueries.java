package com.altice.salescommission.queries;

public interface CalcManageQueries {
	public final String GET_SALES_CHANNELS = "select ccpm.comm_plan_id, ccpm.sales_channel_desc,"
			+ "ccpm.comm_plan,ccpm.calendar_type,ccc.valid_from_dt,  ccc.valid_to_dt, ccc.cal_run_id from c_comm_plan_master ccpm left join c_comm_calendar ccc on(ccpm.calendar_type=ccc.calendar_type)";
	
	public final String GET_CAL_RUN_IDS = "select concat(valid_from_dt,' - ',valid_to_dt) caldt,cal_run_id from c_comm_calendar";
	
	public final String GET_RUN_CTRL_NAME ="select distinct run_control_name,description,ccr_id,saleschannel from c_calc_run_ctl ccrc order by run_control_name";

	public final String GET_CAL_RUN_IDS_BY_SC ="select distinct cal_run_id from  c_comm_calendar ccc order by cal_run_id ";
	
//	where calendar_type in \r\n"
//	+ "(select distinct  calendar_type from c_comm_plan_master ccpm where sales_channel_desc in(:deptParamName) and calendar_type is not null)\r\n"
	
	public final String GET_COMM_PLANS = "select distinct comm_plan_id ,concat(sales_channel_desc,' - ',comm_plan,' - ',comm_plan_id) comm_plan from   c_comm_plan_master ccpm where "
			+ "sales_channel_desc in(:schannelsParamName) and ccpm.active_flag ='Y' and calendar_type is not null "
			+ "and ccpm.effective_date =(select MAX(ccpm1.effective_date) from c_comm_plan_master ccpm1 where ccpm.comm_plan_id = ccpm1.comm_plan_id and ccpm1.effective_date<=current_date) "
			+ "order by comm_plan" ;

	public final String GET_EMP_LIST = "select concat(first_name,' ',last_name,'-',sc_emp_id) as name,sc_emp_id from c_employee_master cem where comm_plan_id =?";


	public final String GET_CALC_DATA = "select distinct ccpm.comm_plan_id ,ccpm.sales_channel_desc ,ccpm.comm_plan,ccpm.calendar_type ,ccm.cal_run_id,"
			+ "ccm.adjustment_close_dt ,ccm.dispute_close_dt ,"
			+ "ccm.calc_close_flag ,ccm.created_by ,ccm.created_dt , ccm.updated_by ,ccm.updated_dt ,ccm.comment,ccm.ccm_id "
			+ "from c_comm_plan_master ccpm "
			+ "left join c_calc_manage ccm on (ccpm.comm_plan_id=ccm.comm_plan_id and ccm.cal_run_id::varchar =? ) "
			+ "where sales_channel_desc = ? and lower(ccpm.active_flag) = lower('Y') "
			+ "and calendar_type = (select calendar_type from c_comm_calendar ccc where cal_run_id::varchar = ? and lower(unlock)=lower('y'))";
	
	public final String GET_CALC_RUN_DATA=	"select *,concat(ccpm.sales_channel_desc,' - ',ccpm.comm_plan) commPlanName from c_calc_run_ctl ccrc,c_comm_plan_master ccpm"
			+ "	where ccrc.ccr_id = ? and ccrc.comm_plan_id = ccpm.comm_plan_id ";
	
	public final String GET_IP_CALRUNID= "select  distinct tcc.cal_run_id from t_calc_ctl tcc where status  ='inactive' and cal_run_id in(\r\n"
			+ "select cal_run_id from c_calc_manage ccm where calc_close_flag !='y' and cal_run_id in\r\n"
			+ "(select cal_run_id from c_comm_calendar ccc where unlock ='Y')) ";

}
