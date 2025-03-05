package com.altice.salescommission.queries;

public interface CalRunIdsQueries {
	
	public final String GET_CAL_RUN_IDS = "select crun_id,cal_run_id,description ,active_flag ,"
			+ "created_by,created_dt,coalesce(updated_by,'-') updated_by,coalesce(updated_dt,'9999-01-01') updated_dt "
			+ "from c_comm_calrunids ccc";
	
	public final String GET_PROCESS_STATUS = "select tcc.created_by,tcc_id,TO_CHAR(tcc.start_time, 'mm-dd-yyyy HH24:MI:SS') start_time,TO_CHAR(tcc.end_time, 'mm-dd-yyyy HH24:MI:SS') end_time,"
			+ "tcc.status,ccrc.run_control_name,ccrc.description,tcc.uid,tcc.cal_run_id,tcc.comm_plan_id "
			+ "from t_calc_ctl tcc left join c_calc_run_ctl ccrc on (tcc.ccrc_id=ccrc.ccr_id) "
			+ "where (1=? or tcc.created_by =?) and (1=? or tcc.ccrc_id=?) and to_char(tcc.start_time,'YYYY-MM-DD') between ? and ? "
			+ "group by tcc.uid,tcc.cal_run_id,tcc.comm_plan_id ,tcc.created_by,tcc_id,ccrc.run_control_name,ccrc.description "
			+ "order by tcc.start_time desc";
	
	public final String GET_USER_IDS = "select created_by userid from t_calc_ctl group by created_by order by created_by";
	
	public final String GET_RUN_CTRLS = "select ccr_id, run_control_name from c_calc_run_ctl order by run_control_name";
}
