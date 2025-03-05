package com.altice.salescommission.queries;

public interface MissingDisputesQueries {
	public final String GET_MISSING_DISPUTES_LIST = "select rowid,kpi_id, comm_plan_id,cust_id, corp,  house, revenue,"
			+ "to_char(wordate,'MM-DD-YYYY') wordate,"
			+ "to_char(wfindate,'MM-DD-YYYY') wfindate,"
			+ "to_char(created_dt,'MM-DD-YYYY') created_dt,"
			+ "message, comment,created_by,sales_rep_id from c_missing_disputes";
	
	public final String GET_KPIS_LIST = "select distinct on(ccpd.kpi_id) ccpd.kpi_id ,ccpd.display_name,ccpm.effective_date  "
			+ "from c_comm_plan_master ccpm, c_comm_plan_detail ccpd,c_kpi_master ckm  "
			+ "where ccpm.comm_plan_id =ccpd.comm_plan_id and ccpm.row_id =ccpd.commplan_rowid and ccpd.kpi_id =ckm.kpi_id "
			+ "and ckm.kpi_status ='Active' and ckm.kpi_type != 'By KPI' and ccpm.comm_plan_id =? "
			+ "group by ccpd.kpi_id,ccpd.display_name,ccpm.effective_date,ccpd.comm_plan_dtl_id "
			+ "order by ccpd.kpi_id,ccpd.display_name";
}
