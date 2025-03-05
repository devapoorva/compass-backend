package com.altice.salescommission.queries;

public interface CommissionQueries {

	public final String GET_PRODUCTS_CORPS = "select cpd.product_id,cpm.product,crcm.rcode,ccm.corp_id "
			+ "from c_product_detail cpd,c_product_master cpm,c_rate_code_master crcm,c_corp_master ccm "
			+ "where cpd.product_id =cpm.product_id and "
			+ "cpd.ratecd =crcm.rcode and cast(cpd.corp_id as int) = crcm.corp " + "and ccm.corp_id =crcm.corp";

	public final String GET_PRODUCTS = "select distinct ckm.kpi_name ,ccpd.kpi_id from c_comm_plan_detail ccpd, c_kpi_master ckm where ccpd.comm_plan_id in\r\n"
			+ "			(select distinct comm_plan_id from c_comm_plan_master ) and ccpd.kpi_id = ckm.kpi_id order by ckm.kpi_name";

	public final String GET_CORPS = "select corp_id from c_corp_master order by corp_id ASC";
	
	public final String GET_ASSIGNED_CORPS = "select  string_agg(corp::varchar, ', ' order by corp) asscCorps from c_employee_corp_reln cecr where sc_emp_id =? and effective_date::varchar =?";

	public final String GET_MEASURE_AGAINST_LIST = "select field_name,field_value from c_translate_master "
			+ "where field_name in ('Measure Against','Commission Payout','Calc Vars Measure Against') and effective_status='Y'";
	
	public final String GET_SALESCHANNELS_LIST = "select field_name,description field_value from c_translate_master "
			+ "where field_name ='Sales Channel' and effective_status='Y' order by description";

	public final String GET_SUPERVISORS = "select distinct(supervisor_id) supervisor_id,concat(supervisor_name,'-',supervisor_id) supervisor_name "
			+ "from c_employee_master cem where supervisor_id is not null order by supervisor_name";

	public final String GET_EMP_SUPERVISORS = "select distinct(employee_id) supervisor_id,concat(first_name,',', last_name  ,'-',employee_id) supervisor_name "
			+ "from c_employee_master cem where (lower(first_name) like lower(?) or lower(last_name) like lower(?) or lower(employee_id) like lower(?)) and employee_id  is not null order by supervisor_name";

	public final String GET_KPIDETAILS = "select * from c_comm_detail_values ccdv,"
			+ "c_comm_plan_master ccpm ,c_kpi_master ckm, c_comm_plan_detail ccpd  where "
			+ "ccpd.comm_plan_dtl_id = ccdv.comm_plan_dtl_id  and ckm.kpi_id  = ccpd.kpi_id and  ccpd.comm_plan_id = ?  and ccpm.comm_plan_id = ?"
			+ " and ccpd.effective_date = ?   and  ccpm.effective_date = ?  Order by ccpd.view_order asc";

	public final String GET_MEASURETYPES = "select field_value from c_translate_master ctm  where field_name  ='Measure Type' and effective_status = 'Y'";

	public final String GET_RATE_CODES = "select * from c_kpi_rcode_reln where kpi_id = ? and (1=? or corp =?) and active_flag = 'Y' order by rcode,effective_from ,effective_to ";

//			"select distinct cpd.product_id,cpm.product,crcm.rcode,ccm.corp_id,crcm.rname, crcm.f_level ,"
//			+ "cpd.effective_from ,cpd.effective_to ,cpd.effective_date,cpd.promoamt ,cpd.active_flag "
//			+ "from c_product_detail cpd,c_product_master cpm,c_rate_code_master crcm,c_corp_master ccm "
//			+ "where cpd.product_id =cpm.product_id and "
//			+ "cpd.ratecd =crcm.rcode and cast(cpd.corp as int) = crcm.corp "
//			+ "and ccm.corp_id =crcm.corp and cpd.product_id =? and (1=? or ccm.corp_id =?)  and cpd.active_flag = 'Y'  and cpm.product_group not in (0,1,2) order by crcm.rcode asc";
//	
	public final String SELECT_PRODUCT = "select * from c_kpi_rcode_reln ckrr where kpi_id = ? and rcode = ? ";

	public final String GET_PRODUCTS_BY_SC = "select  distinct kpd.product_id,cpm.product "
			+ "from c_kpi_detail kpd join c_comm_plan_detail ccpd on kpd.kpi_id = ccpd.kpi_id "
			+ "join c_comm_plan_master ccpm on ccpm.comm_plan_id = ccpd.comm_plan_id "
			+ "join c_product_master cpm  on cpm.product_id  = kpd.product_id "
			+ "where  ccpm.sales_channel_desc =? and  cpm.status ='Active' "
			+ "and cpm.product_group > 2 or cpm.product_group = 0 order by cpm.product";

	public final String GET_COMMPLANS = "select distinct comm_plan from c_comm_plan_detail ccpm where kpi_id =? order by comm_plan ";

	public final String GET_PRODUCTS_DETAILS = "select distinct ckm.kpi_name ,ccpd.kpi_id from c_comm_plan_detail ccpd, c_kpi_master ckm where ccpd.comm_plan_id in\r\n"
			+ "(select distinct comm_plan_id from c_comm_plan_master where sales_channel_desc =? ) and ccpd.kpi_id = ckm.kpi_id order by ckm.kpi_name";

	public final String GET_PRODUCTS_BY_PTYPE = "select distinct product ,product_id from c_product_master cpm where product_type = ? ";

	public final String GET_PRODUCTS_BY_PCAT = "select distinct product ,product_id from c_product_master cpm where product_category = ? ";

	public final String GET_PRODUCTS_BY_PID = "select distinct product ,product_id from c_product_master cpm where product_id = ? ";

	public final String GET_COMM_PLAN_DETAILS = "select distinct (ccpd.comm_plan_dtl_id) comm_plan_dtl_id , ccpd.comm_plan_id , ccpm.comm_plan ,ccpd.kpi_weightage ,ccpd.kpi_goal, "
			+ "ccpd.display_flg, ccpm.comm_pool ,ccpd.measure_type,ccpd.kpi_type ,"
			+ "ccpd.display_name, ccpd.effective_date, ccpd.kpi_id,ccpd.view_order,ccdv.range_low ,"
			+ "ccdv.range_high ,ccdv.modifier_percent ,ccdv.commission_val ,ccdv.tier ,ccdv.target , ckm.kpi_name,"
			+ "ccpd.charge_back,ccpd.charge_back_days,ccpd.non_charge_back_days,ccpd.measure_against,ccpd.is_percentage,ccpd.calculate_score,ccpd.is_pooled,"
			+ "ccpd.commission_payout,ccpd.calc_vars_score,ccpd.calc_vars_revenue,ccpd.calc_vars_count,ccpd.is_calc_vars,ccpd.is_score_vars,ccpd.commplan_rowid,ccps.colorcode "
			+ "from c_comm_plan_detail ccpd "
			+ "left join c_comm_plan_master ccpm  on (ccpd.commplan_rowid  = ccpm.row_id) "
			+ "left join c_kpi_master ckm on (ccpd.kpi_id = ckm.kpi_id and ckm.kpi_status='Active') "
			+ "left join c_comm_detail_values ccdv on ( ccdv.comm_plan_dtl_id =ccpd.comm_plan_dtl_id) "
			+ "left join c_comm_plan_styles ccps on ( ccps.comm_plan_dtl_id =ccpd.comm_plan_dtl_id) "
			+ "where ccpd.comm_plan_id =? and ccpd.effective_date = ? "
//			+ "and ccpd.effective_date = (select max(ckm3.effective_date) from c_comm_plan_detail ckm3 where ccpd.comm_plan_id = ckm3.comm_plan_id  and ckm3.effective_date::varchar <= ?) "
			+ "order by ccpd.view_order desc";
	
	public final String GET_IMAGE = "Select encode(comm_img,'base64') comm_img from c_comm_plan_master where comm_plan_id =? and effective_date =?";

	public final String GET_TIER_RAGE_VALUES = "SELECT cdtr_id, comm_plan_dtl_id, range_val_type, range_low, range_high, tier, modifier_percent, "
			+ "commission_val,  target FROM compasmgr.c_comm_detail_values  where comm_plan_dtl_id =? order by range_low asc";

	public final String GET_ADJUSTMENTRESULT = "select cem.sc_emp_id as sc_emp_id ,\r\n"
			+ "tad.adjustment_id as adjustment_id,\r\n"
			+ "concat(cem.first_name,' ',cem.middle_name,' ',cem.last_name) as name,\r\n" + "cem.user_type,\r\n"
			+ "cem.sales_rep_id,\r\n" + "cem.assc_corps,\r\n" + "cem.sales_rep_channel,\r\n" + "cem.employee_id ,\r\n"
			+ "cem.soft_termination_date,\r\n" + "cem.operator_id,\r\n" + "cem.sales_rep_type,\r\n" + "tad.kpi ,\r\n"
			+ "tad.adjustment,\r\n" + "tad.new_tier ,\r\n" + "tad.comments\r\n"
			+ "from c_employee_master cem inner join t_adjustment_detail tad on cem.sc_emp_id =tad.sc_emp_id inner join  c_comm_calendar ccc on ccc.cal_run_id =tad.cal_run_id where 1=1 \r\n";

	public final String GET_COMMPLAN_DTL_ID = "select * from c_comm_plan_detail ccpd   where comm_plan_id = ? and kpi_id = ? and effective_date =?";

	public final String GET_COMM_PLAN_BY_DATE = "select * from (select *, rank() over (partition by comm_plan_id order by effective_date desc) rnk "
			+ "from c_comm_plan_master ccpm where comm_plan_id = ? and  effective_date <= ?) as qq where  rnk = 1";

	public final String GET_COMM_PLAN_BY_AS_OF_DATE = "select *,to_char(effective_date,'MM-DD-YYYY') edate from c_comm_plan_master where row_id=?";

	public final String GET_AS_OF_DATE = "select row_id,effective_date from c_comm_plan_master ccpm where comm_plan_id = ? order by effective_date desc";

	public final String GET_ALL_COMM_PLAN_BY_DATE = "select *,to_char(effective_date,'MM-DD-YYYY') edate,coalesce(encode(comm_img,'base64'),'NA') commimg from (select *, rank() over (partition by comm_plan_id order by effective_date desc) rnk "
			+ "from c_comm_plan_master ccpm ) as qq where rnk = 1  order by comm_plan";

	public final String GET_CALENDAR_DATE = " select * from c_comm_calendar ccc where cal_run_id in (select cal_run_id from c_calc_manage ccm  where comm_plan_id =?)";

	public final String GET_SALES_REEPS = "select distinct sales_rep_id  from fd_kom_feed fkf where corp=? and house =? and wordate >=? and wordate <=? order by sales_rep_id ";

	public final String FREE_FORM_RESULT = "select * from fd_kom_search fks where search_string like ?";

	public final String GET_PRODUCT_NAME = "select  distinct  svcs_reporting_name from kom_svcs_rcode_stg ksrs where"
			+ " pos =? and corp =? and begin_date <= ? and \r\n"
			+ "end_date >= ? and val = ? and lower(svcs_type) like ?  ";

	public final String GET_DEPNDNT_KPI_DTLS = "select distinct dep.row_id,dep.kpi_id,ckm.kpi_name kname,dep.kpi_goal,dep.kpi_weight ,dep.operation ,dep.kpi_order ,dep.comp_plan_id ,dep.comm_plan_dtl_id ,dep.complex_kpi_id ,ccpd.is_calc_vars "
			+ "from c_depndnt_kpi_details dep,c_comm_plan_detail ccpd,c_kpi_master ckm  "
			+ "where ccpd.comm_plan_dtl_id = dep.comm_plan_dtl_id  and ckm.kpi_id =dep.kpi_id and dep.complex_kpi_id =? and dep.comp_plan_id = ? and (1=? or dep.comm_plan_dtl_id =?)";

	public final String GET_DEPNDNT_SCORE_KPI_DTLS = "select s.complex_kpi_id,s.kpi_id,s.kpi_name,s.kpi_weight,s.row_id,s.comm_plan_dtl_id,ccpd.is_score_vars from c_depndnt_kpi_score_weight s,c_comm_plan_detail ccpd "
			+ "where s.comp_plan_id =ccpd.comm_plan_id and s.comm_plan_dtl_id =ccpd.comm_plan_dtl_id  and s.complex_kpi_id =? and s.comp_plan_id =? and s.comm_plan_dtl_id =? and ccpd.effective_date::varchar =?";

	public final String GET_DEPNDNT_KPI = "select max(effective_date) effective_date,ck.kpi_name as dpnt_kpi_name,ck.kpi_id  from c_kpi_master ck where  ck.kpi_id in (select distinct ckd.dpdnt_kpi from c_kpi_detail ckd,c_kpi_master ckm where ckm.kpi_type ='By KPI' and "
			+ "	ckd.kpi_id = ckm.kpi_id and ckd.kpi_id =? and ckd.effective_date = (select max(ckm3.effective_date) "
			+ "from c_kpi_master ckm3 where ckd.kpi_id = ckm3.kpi_id  and ckm3.effective_date::varchar <= ?)) "
			+ "group by ck.kpi_name,ck.kpi_id";

	public final String GET_CHARGEBACK_TYPES = "select field_value from c_translate_master where field_name='Chargeback Type' and effective_status='Y'";
	
	public final String GET_COMMISSION_PLANS = "select concat(comm_plan,' - ',comm_plan_id) comm_plan,comm_plan_id  from c_comm_plan_master ccpm where active_flag ='Y' "
			+ "and ccpm.effective_date = (select max(ccpm1.effective_date) from c_comm_plan_master ccpm1 where ccpm.comm_plan_id = ccpm1.comm_plan_id  and ccpm1.effective_date <=(SELECT CURRENT_DATE)) "
			+ "order by comm_plan";
	
	public final String GET_SALES_CHANNELS = "select distinct sales_channel from c_comm_plan_master ccpm";

}