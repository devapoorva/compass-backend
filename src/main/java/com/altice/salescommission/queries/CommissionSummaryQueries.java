package com.altice.salescommission.queries;

public interface CommissionSummaryQueries {
	public final String GET_SQl_QUERIES = "select id,sql_query from c_commission_summary_sqls";

	public final String GET_BY_PAGENAME_ONE = "select id,page_name,grid_name,order_by,sql,created_by,"
			+ "created_dt,updated_by,updated_dt,commplanid,saleschannel,kpi_id,user_type "
			+ "from c_json_sql_mapping where page_name =? and status='Active' order by order_by";

	public final String GET_BY_PAGENAME_ONE_22 = "select id,page_name,grid_name,order_by,sql,created_by,"
			+ "created_dt,updated_by,updated_dt,commplanid,saleschannel,kpi_id,user_type "
			+ "from c_json_sql_mapping where page_name =? and status='Active' and user_type !='sup' order by order_by";

	public final String GET_BY_PAGENAME_ONE_27 = "select id,page_name,grid_name,order_by,sql,created_by,"
			+ "created_dt,updated_by,updated_dt,commplanid,saleschannel,kpi_id,user_type "
			+ "from c_json_sql_mapping where page_name =? and status='Active' and user_type !='rep' order by order_by";

	public final String GET_BY_PAGENAME_TWO1 = "select id,page_name,grid_name,order_by,sql,created_by,"
			+ "created_dt,updated_by,updated_dt,commplanid,saleschannel,kpi_id " + "from c_json_sql_mapping "
			+ "where page_name =? and commplanid=? and status='Active' order by order_by limit 1";

	public final String GET_BY_PAGENAME_TWO = "select id,page_name,grid_name,order_by,sql,created_by,"
			+ "created_dt,updated_by,updated_dt,commplanid,saleschannel,kpi_id,user_type "
			+ "from c_json_sql_mapping where page_name =?  and status='Active' order by order_by limit 1";

	public final String GET_SALES_CHANNELS = "select ccpm.comm_plan_id, ccpm.sales_channel_desc,ccpm.comm_plan,ccpm.calendar_type,"
			+ " ccc.valid_to_dt,ccc.valid_from_dt, ccc.cal_run_id,ccpm.effective_date,ccc.issalesrepaccess "
			+ "from c_comm_plan_master ccpm "
			+ "left join c_comm_calendar ccc on(ccpm.calendar_type=ccc.calendar_type ) "
			+ "where ccpm.active_flag='Y' and ccpm.effective_date = (select max(ccpm1.effective_date) from c_comm_plan_master ccpm1 where ccpm.comm_plan_id = ccpm1.comm_plan_id  and ccpm1.effective_date <=(select current_date))  "
			+ "order by ccc.valid_to_dt desc";

	public final String GET_SUP_DROPDOWNS1 = "select ccpm.comm_plan_id, ccpm.sales_channel_desc,ccpm.comm_plan,ccpm.calendar_type,"
			+ " ccc.valid_to_dt,ccc.valid_from_dt, ccc.cal_run_id,ccpm.effective_date,ccc.issalesrepaccess "
			+ "from c_comm_plan_master ccpm "
			+ "left join c_comm_calendar ccc on(ccpm.calendar_type=ccc.calendar_type  and ccc.issalesrepaccess='Y') "
			+ "inner join c_employee_master cem on(cem.comm_plan_id =ccpm.comm_plan_id and cem.profile_status='complete') "
			+ "where ccpm.active_flag='Y' and cem.employee_id =? "
			+ "and ccpm.effective_date = (select max(ccpm1.effective_date) from c_comm_plan_master ccpm1 where ccpm.comm_plan_id = ccpm1.comm_plan_id  and ccpm1.effective_date <=(select current_date))  "
			+ "order by ccc.valid_to_dt desc";

	public final String GET_USER_DROPDOWNS1 = "select ccpm.comm_plan_id, ccpm.sales_channel_desc,ccpm.comm_plan,ccpm.calendar_type,"
			+ " ccc.valid_to_dt,ccc.valid_from_dt, ccc.cal_run_id,ccpm.effective_date,ccc.issalesrepaccess,ccpm.display_flag "
			+ "from c_comm_plan_master ccpm "
			+ "left join c_comm_calendar ccc on(ccpm.calendar_type=ccc.calendar_type  and ccc.issalesrepaccess='Y') "
			+ "inner join c_employee_master cem on(cem.comm_plan_id =ccpm.comm_plan_id and cem.profile_status='complete') "
			+ "where ccpm.active_flag='Y' and cem.employee_id =? "
			+ "and ccpm.effective_date = (select max(ccpm1.effective_date) from c_comm_plan_master ccpm1 where ccpm.comm_plan_id = ccpm1.comm_plan_id  and ccpm1.effective_date <=(select current_date))  "
			+ "order by ccc.valid_to_dt desc";

	public final String GET_SUP_DROPDOWNS = "select distinct ccc.cal_run_id,ccpm.comm_plan_id, ccpm.sales_channel_desc,ccpm.comm_plan,ccpm.calendar_type,\r\n"
			+ "			 ccc.valid_to_dt,ccc.valid_from_dt, ccpm.effective_date,ccc.issalesrepaccess,ccpm.display_flag \r\n"
			+ "			from c_comm_plan_master ccpm \r\n"
			+ "			left join c_comm_calendar ccc on(ccpm.calendar_type=ccc.calendar_type and issalesrepaccess ='Y') \r\n"
			+ "			inner join c_employee_master cem on(cem.comm_plan_id =ccpm.comm_plan_id and cem.calendar_type =ccc.calendar_type  and cem.profile_status='complete') \r\n"
			+ "			where ccpm.active_flag='Y' and ccpm.display_flag ='Y' and cem.employee_id =? and ccc.valid_from_dt >= ccpm.effective_date \r\n"
			+ "		order by ccpm.effective_date  desc,ccc.cal_run_id  desc";

	public final String GET_USER_DROPDOWNS = "select distinct ccc.cal_run_id,ccpm.comm_plan_id, ccpm.sales_channel_desc,ccpm.comm_plan,ccpm.calendar_type,\r\n"
			+ "			 ccc.valid_to_dt,ccc.valid_from_dt, ccpm.effective_date,ccc.issalesrepaccess,ccpm.display_flag \r\n"
			+ "			from c_comm_plan_master ccpm \r\n"
			+ "			left join c_comm_calendar ccc on(ccpm.calendar_type=ccc.calendar_type and issalesrepaccess ='Y') \r\n"
			+ "			inner join c_employee_master cem on(cem.comm_plan_id =ccpm.comm_plan_id and cem.calendar_type =ccc.calendar_type  and cem.profile_status='complete') \r\n"
			+ "			where ccpm.active_flag='Y' and ccpm.display_flag ='Y' and cem.employee_id =? and ccc.valid_from_dt >= ccpm.effective_date \r\n"
			+ "		order by ccpm.effective_date  desc,ccc.cal_run_id  desc";

	public final String GET_TRACK_IDS = "select distinct twm.tracking_id,twd.final_status,twd.ticket_id from t_workflow_details twd ,t_workflow_message twm "
			+ "where twd.tracking_id =twm.tracking_id  and twm.tracking_id like ? order by twd.ticket_id desc limit 1";

	public final String GET_HEADER_GROUPS = "select field_value from c_translate_master ctm where field_name ='Report Type' and effective_status='Y'";

	public final String GET_HEADERS1 = "select cjsh.* from c_json_sql_headers cjsh , c_json_sql_mapping cjsm  where cjsh.jsonsqlid =cjsm.id and cjsm.commplanid =? order by cjsh.colorder";

	public final String GET_BUTTONS2 = "select cjsh.* from c_json_sql_buttons cjsh , c_json_sql_mapping cjsm  where cjsh.jsonsqlid =cjsm.id and cjsm.commplanid =?";

	public final String GET_HEADERS = "select cjsh.* from c_json_sql_headers cjsh , c_json_sql_mapping cjsm  where cjsh.jsonsqlid =cjsm.id  order by cjsh.colorder";

	public final String GET_HEADERS11 = "select cjsh.* from c_json_sql_headers cjsh , c_json_sql_mapping cjsm  where cjsh.jsonsqlid =cjsm.id  and user_type != 'sup' order by cjsh.colorder";

	public final String GET_HEADERS111 = "select cjsh.* from c_json_sql_headers cjsh , c_json_sql_mapping cjsm  where cjsh.jsonsqlid =cjsm.id   and user_type != 'rep' order by cjsh.colorder";

	public final String GET_HEADERS22 = "select cjsh.* from c_json_sql_headers cjsh , c_json_sql_mapping cjsm  where cjsh.jsonsqlid =cjsm.id and saleschannel !='Web Assist' order by cjsh.colorder";

	public final String GET_BUTTONS = "select cjsh.* from c_json_sql_buttons cjsh , c_json_sql_mapping cjsm  where cjsh.jsonsqlid =cjsm.id ";

	public final String GET_CALC_STATUS = "select max(tcc_id) tcc_id,status  from t_calc_ctl tcc where cal_run_id =? and comm_plan_id = ? group by tcc_id,status order by status desc limit 1";

	public final String GET_SUP_STATUS = "select comm_plan_priority from c_comm_plan_master ccpm where comm_plan_id =?";

	public final String GET_PAGE_NAME1 = "select page_name from c_json_sql_mapping cjsm ,c_json_sql_mapping_trans cjsmt where cjsm.id =cjsmt.mapping_id and cjsmt.comm_plan_id = ? and cjsmt.kpi_id =?";

	public final String GET_PAGE_NAME = "select feed_type,kpi_type  from c_kpi_master ckm where kpi_id =? limit 1";

	public final String GET_KPIS_LIST = "select distinct ccpd.kpi_id,ckm.kpi_name,ccpd.display_name  from c_comm_plan_master ccpm, c_comm_plan_detail ccpd,c_kpi_master ckm  "
			+ "where ccpm.comm_plan_id =ccpd.comm_plan_id and ccpm.row_id =ccpd.commplan_rowid and ccpd.kpi_id =ckm.kpi_id "
			+ "and ckm.kpi_status ='Active' and ckm.kpi_type != 'By KPI' and ccpm.comm_plan_id =? and ccpm.effective_date::varchar <= ? "
			+ "order by ckm.kpi_name";

	public final String COMMSBYKPICROSSTABREPORT = "select replace(replace(replace(replace(A.val,'\"\" : {',''),'}}','}'),' : ',':'),'\\\"','\"') str from ( "
			+ "select json_build_object( 'sc_emp_id', tmc.sc_emp_id,'cal_run_id', tmc.cal_run_id,'comm_plan_id', tmc.comm_plan_id "
			+ ",'',json_object_agg(tmc.KPI_ID, tmc.commission_val::varchar(10) ||  '\",\"'||  tmc.KPI_ID::varchar(10) || '\":\"' || tmc.revenue::varchar(10) "
			+ "order by tmc.KPI_ID))::varchar(2000) Val from t_monthly_commission tmc "
			+ "where tmc.cal_run_id::varchar=? and  tmc.comm_plan_id::varchar=? "
			+ "group by tmc.sc_emp_id,tmc.cal_run_id,tmc.comm_plan_id ) A limit 2";

	public final String PAYROLLREPORT = "SELECT ccc.pay_dt ,cem.first_name || ' ' || cem.last_name as name, cem.sales_rep_id ,cem.employee_id , "
			+ "cem.sc_emp_id, ccpm.comm_plan , ccpm.sales_channel,cem.sales_rep_type,tmc.sc_emp_id ,tmc.comm_plan_id ,tmc.cal_run_id,\r\n"
			+ "sum(tmc.commission_val) commission,"
			+ "coalesce((select coalesce(round(tmc1.adjustment::numeric ,2),'0.00') "
			+ "from t_monthly_commission tmc1   where  tmc1.kpi_id =47   and tmc1.sc_emp_id=tmc.sc_emp_id and tmc1.comm_plan_id=tmc.comm_plan_id and tmc1.cal_run_id =  tmc.cal_run_id),'0.00') adjustment,"
			+ "coalesce((select coalesce(round(tmc2.adjustment::numeric ,2),'0.00') "
			+ "from t_monthly_commission tmc2  where   tmc2.kpi_id =46 and tmc2.sc_emp_id=tmc.sc_emp_id and tmc2.comm_plan_id=tmc.comm_plan_id  and tmc2.cal_run_id =  tmc.cal_run_id),'0.00') incentive ,"
			+ "coalesce(round((sum(tmc.commission_val::numeric+coalesce(tmc.adjustment::numeric,0))),2),'0.00') total "
			+ "FROM t_monthly_commission tmc "
			+ "JOIN c_comm_calendar ccc ON ccc.cal_run_id = tmc.cal_run_id AND ccc.calendar_type::text = tmc.calendar_type::text \r\n"
			+ "JOIN c_employee_master cem ON cem.sc_emp_id::text = tmc.sc_emp_id::text AND cem.profile_status::text = 'complete'::text \r\n"
			+ "AND cem.effective_date = (( SELECT max(cem1.effective_date) AS max  FROM c_employee_master cem1 WHERE cem.sc_emp_id::text = cem1.sc_emp_id::text "
			+ "AND cem1.effective_date <= ccc.valid_from_dt AND cem1.profile_status::text = 'complete'::text)) \r\n"
			+ "JOIN c_comm_plan_master ccpm ON ccpm.comm_plan_id = tmc.comm_plan_id \r\n"
			+ "AND ccpm.effective_date = (( SELECT max(ccpm1.effective_date) AS max "
			+ "FROM c_comm_plan_master ccpm1 WHERE ccpm.comm_plan_id = ccpm1.comm_plan_id AND ccpm1.effective_date <= ccc.valid_from_dt)) \r\n "
			+ "join c_comm_plan_detail ccpd on ccpd.comm_plan_id =ccpm.comm_plan_id and ccpd.kpi_id =tmc.kpi_id  and ccpd.comm_plan_id =tmc.comm_plan_id  and ccpd.display_flg ='Y' "
			+ "AND ccpd.effective_date = (( SELECT max(ccpd1.effective_date) AS max FROM c_comm_plan_detail ccpd1 "
			+ "WHERE ccpd.comm_plan_id = ccpd1.comm_plan_id AND ccpd1.effective_date <= ccc.valid_from_dt)) "
			+ "inner join c_kpi_master ckm on(ckm.kpi_id=tmc.kpi_id) "
			+ "WHERE tmc.comm_plan_id::varchar IN (%s) and tmc.cal_run_id::varchar=? "
			+ "group by ccc.pay_dt ,cem.first_name || ' ' || cem.last_name , cem.sales_rep_id ,cem.employee_id , cem.sc_emp_id, ccpm.comm_plan , ccpm.sales_channel,cem.sales_rep_type,tmc.sc_emp_id,tmc.comm_plan_id,tmc.cal_run_id";
	
	public final String DUMPMONTHLYDETAILCOMM = "select distinct tmcd.tmcd_id, tmcd.cal_run_id,ccpm.comm_plan , "
			+ "tmcd.sc_emp_id,cem.sales_rep_id,cem.first_name || ' ' || cem.last_name as empname, tmcd.kpi_id,ckm.kpi_name ,"
			+ "tmcd.house, tmcd.corp, tmcd.cust, tmcd.wordate, tmcd.wfindate, tmcd.wddate, tmcd.commissionable_units, tmcd.commissionable_revenue, "
			+ "tmcd.commission,tmcd.account_number, tmcd.days_line_active, tmcd.score_intermediate,  tmcd.subscriber_number "
			+ "from t_monthly_commission_detail tmcd "
			+ "left join c_comm_plan_master ccpm on (ccpm.comm_plan_id=tmcd.comm_plan_id)  "
			+ "left join c_kpi_master ckm on (ckm.kpi_id=tmcd.kpi_id) "
			+ "left join c_employee_master cem on (cem.sc_emp_id = tmcd.sc_emp_id and cem.comm_plan_id=tmcd.comm_plan_id and cem.profile_status='complete') "
			+ "where tmcd.comm_plan_id::varchar IN (%s) and tmcd.cal_run_id::varchar =? "
			+ "and ccpm.effective_date = (select max(ccpm1.effective_date) from c_comm_plan_master ccpm1 where ccpm.comm_plan_id = ccpm1.comm_plan_id  and ccpm1.effective_date::varchar <=?)";
	
	public final String COMMSBYKPIREPORT = "SELECT  empname,employee_id, sc_emp_id,  sales_rep_id,cal_run_id, comm_plan, comm_plan_id, kpiname, kpi_id,score percentage,yield_goal goal, "
			+ "product_count units, revenue,   commission_amount commission FROM commissions_by_kpi_report_view  where comm_plan_id::varchar IN (%s) and cal_run_id::varchar =?";
	
	public final String CHARGEBACKDETAILREPORT = "SELECT  empname,  employee_id, sc_emp_id,sales_rep_id,house,  corp, cust,cust_name, chargeback_type,cal_run_id  ,comm_plan commplan,comm_plan_id,kpi_id, display_name kpiname, "
			+ "wstat,wordate, wfindate, wddate,  commissionable_units units, net_revenue revenue, commission FROM chargeback_detail_report_view where comm_plan_id::varchar IN (%s) and cal_run_id::varchar =?";
	
	public final String CUSTOMERDETAILREPORT = "SELECT * FROM customer_detail_report_view where comm_plan_id::varchar IN (%s) and cal_run_id::varchar =?";
	
	public final String COMMISSIONDOCREPORT = "SELECT doc_name,employeename,employee_id,sc_emp_id,sales_rep_id,	comm_plan, comm_plan_id ,user_role,	signoffdate	,status "
			+ " FROM commission_doc_report_view where comm_plan_id::varchar IN (%s) and (1=1 or comm_plan_id::varchar=?)";

	public final String DUMPMONTHLYCOMM = "select * from t_monthly_commission tmc where comm_plan_id::varchar IN (%s) and cal_run_id::varchar =?";

}
