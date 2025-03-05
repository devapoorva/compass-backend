package com.altice.salescommission.queries;

public interface UploadJsonQueries {
	public final String GET_JSONS_LIST1 = "select cjsm.id,cjsm.grid_name,cjsm.order_by,cjsm.page_name,"
			+ "cjsm.sql,cjsm.commplanid,cjsm.saleschannel,cjsm.kpi_id,"
			+ "ccpm.comm_plan, ccpm.sales_channel_desc,coalesce(ckm.kpi_name,'NA') kpi_name,cjsm.report_type,cjsm.status "
			+ "from c_json_sql_mapping cjsm "
			+ "inner join c_comm_plan_master ccpm on(cjsm.commplanid = ccpm.comm_plan_id)"
			+ "left join c_kpi_master ckm on(cjsm.kpi_id =ckm.kpi_id)"
			+ "where cjsm.status ='Active' and cjsm.saleschannel=? and cjsm.commplanid=? "
			+ "order by cjsm.report_type,cjsm.page_name,cjsm.order_by";

	public final String GET_JSONS_LIST = "select distinct cjsm.id,cjsm.grid_name,cjsm.order_by,cjsm.page_name,"
			+ "cjsm.sql,coalesce(ckm.kpi_name,'NA') kpi_name,cjsm.report_type,cjsm.status,cjsm.description,cjsm.commplanid,cjsm.kpi_id,cjsm.saleschannel "
			+ "from c_json_sql_mapping cjsm left join c_kpi_master ckm on(cjsm.kpi_id =ckm.kpi_id) "
			+ "where cjsm.status ='Active'  order by cjsm.report_type,cjsm.page_name,cjsm.order_by";

	public final String GET_KPIS_LIST = "select distinct on(kpi_name) kpi_name,kpi_id,kpi_status from c_kpi_master ckm where kpi_status = 'Active' order by kpi_name";

	public final String GET_COMM_PLANS_LIST = "select distinct on(comm_plan) comm_plan, comm_plan_id  from c_comm_plan_master ccpm where active_flag ='Y' order by comm_plan";

	public final String GET_REPORT_TYPES_LIST = "select field_value from c_translate_master ctm where field_name ='Report Type' and effective_status='Y'";

	public final String GET_SALES_CHANNELS_LIST = "select distinct saleschannel "
			+ "from c_json_sql_mapping cjsm where status ='Active' order by saleschannel";

	public final String GET_COMMPLANS_LIST = "select distinct(cjsm.commplanid) commplanid,ccpm.comm_plan  "
			+ "from c_json_sql_mapping cjsm  "
			+ "inner join c_comm_plan_master ccpm on(ccpm.comm_plan_id = cjsm.commplanid) "
			+ "where cjsm.status ='Active' and cjsm.saleschannel =? order by cjsm.commplanid";

	public final String GET_EXISTING_DATA = "select page_name,grid_name,order_by,"
			+ "sql,status ,coalesce(comment,'NA') com,kpi_id,report_type  "
			+ "from c_json_sql_mapping where status ='Active' and saleschannel = ? and commplanid =? order by id";

	public final String GET_HEADERS = "select * from c_json_sql_headers where jsonsqlid = ? order by colorder";

	public final String GET_KPIS = "select cjsmt.kpi_id ,cjsmt.comm_plan_id  from c_json_sql_mapping cjsm ,c_json_sql_mapping_trans cjsmt where cjsm.id = cjsmt.mapping_id and cjsm.id=?";

	public final String GET_BUTTONS = "select * from c_json_sql_buttons where jsonsqlid = ?";
}
