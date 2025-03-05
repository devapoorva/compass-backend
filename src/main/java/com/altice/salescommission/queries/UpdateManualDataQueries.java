package com.altice.salescommission.queries;

public interface UpdateManualDataQueries {
	
	public final String GET_KPIS = "select max(effective_date) effective_date,kpi_id ,concat(kpi_name,' - ',kpi_id) kpi_name  from c_kpi_master ckm where kpi_status ='Active' group by kpi_id ,kpi_name order by kpi_name";

	public final String GET_KPIS_UAT = "select max(effective_date) effective_date,kpi_id ,concat(kpi_name,' - ',kpi_id) kpi_name  from c_kpi_master ckm where kpi_status ='Active' and kpi_id in(46,47) group by kpi_id ,kpi_name order by kpi_name";
	
	public final String GET_KPIS_PROD = "select max(effective_date) effective_date,kpi_id ,concat(kpi_name,' - ',kpi_id) kpi_name  from c_kpi_master ckm where kpi_status ='Active' and kpi_id in(46,47) group by kpi_id ,kpi_name order by kpi_name";

	public final String GET_COMMPLANS = "select max(effective_date) effective_date,comm_plan_id  ,concat(comm_plan,' - ',comm_plan_id) comm_plan  from c_comm_plan_master ccpm  where active_flag  ='Y' group by comm_plan_id ,comm_plan order by comm_plan";

	public final String GET_CALRUNIDS = "select cal_run_id from c_comm_calendar ccc where active_flag ='Y' and valid_from_dt <= current_date order by cal_run_id  desc";

	public final String GET_ADJUSTMENT_UPDATE_RESULTS = "select max(ccpm.effective_date) effective_date,adjustment_id ,tad.sc_emp_id ,cal_run_id ,"
			+ "tad.comm_plan_id,ccpm.comm_plan ,tad.kpi_id,ckm.kpi_name  ,adjustment ,adjustment_comments,concat(cem.first_name,' ',cem.last_name) empname "
			+ "from t_adjustment_detail tad\r\n"
			+ "inner join c_comm_plan_master ccpm on(tad.comm_plan_id=ccpm.comm_plan_id)  \r\n"
			+ "inner join c_kpi_master ckm  on(tad.kpi_id =ckm.kpi_id)  "
			+ "inner join c_employee_master cem  on(cem.sc_emp_id=tad.sc_emp_id and cem.profile_status='complete') "
			+ "where cal_run_id =? and tad.comm_plan_id =? and (1=? or tad.kpi_id=?)  \r\n"
			+ "group by adjustment_id ,tad.sc_emp_id ,cal_run_id ,tad.comm_plan_id,ccpm.comm_plan ,tad.kpi_id,ckm.kpi_name  ,adjustment ,adjustment_comments,cem.first_name,cem.last_name";

	public final String GET_GOAL_UPDATE_RESULTS = "select max(ccpm.effective_date) effective_date,goal_id,tkg.sc_emp_id ,cal_run_id ,tkg.comm_plan_id,"
			+ "ccpm.comm_plan ,tkg.kpi_id,ckm.kpi_name ,yield_goal ,tkg.comm_plan_type,upload_type,concat(cem.first_name,' ',cem.last_name) empname "
			+ "from t_kpi_goal tkg \r\n"
			+ "inner join c_comm_plan_master ccpm on(tkg.comm_plan_id=ccpm.comm_plan_id and ccpm.active_flag='Y')  \r\n"
			+ "inner join c_kpi_master ckm  on(tkg.kpi_id =ckm.kpi_id and ckm.kpi_status='Active')  "
			+ "inner join c_employee_master cem  on(cem.sc_emp_id=tkg.sc_emp_id and cem.profile_status='complete') "
			+ "where cal_run_id =? and tkg.comm_plan_id =? and (1=? or tkg.kpi_id=?)  \r\n"
			+ "group by goal_id,tkg.sc_emp_id ,cal_run_id ,tkg.comm_plan_id,ccpm.comm_plan ,tkg.kpi_id,ckm.kpi_name ,yield_goal ,tkg.comm_plan_type,upload_type,cem.first_name,cem.last_name";

}
