package com.altice.salescommission.queries;

public interface KpiMasterQueries {

	public final String GET_KPI_ACTIVE_LIST = "select ckm.wstat_string,ckm.kpi_id,ckm.kpi_name,ckm.kpi_type,ckm.kpi_status,"
			+ "ckm.kpi_calc_type ,ckm.custom_routine,ckm.effective_date,to_char(ckm.effective_date,'MM-DD-YYYY') edate,ckd.combo_string,ckm.kpi_priority,ckd.include_count,ckd.include_revenue,feed_type "
			+ "from c_kpi_master ckm "
			+ "left join c_kpi_detail ckd on (ckm.kpi_id = ckd.kpi_id and ckm.effective_date = ckd.effective_date) "
//			+ "where ckm.effective_date in (select max(effective_date) effective_date from c_kpi_master ckm2 group by kpi_name) "
			+ "group BY ckm.kpi_id,ckm.kpi_name,ckm.kpi_type,ckm.kpi_status,ckm.effective_date,"
			+ "ckm.kpi_calc_type,ckm.custom_routine,ckd.combo_string,ckd.include_count,ckd.include_revenue,feed_type "
			+ "order by ckm.kpi_name,ckm.effective_date desc";
	
	public final String GET_ACTIVE_KPIS = "select ckm.wstat_string,ckm.kpi_id,ckm.kpi_name,ckm.kpi_type,ckm.kpi_status,"
			+ "ckm.kpi_calc_type ,ckm.custom_routine,ckm.effective_date,to_char(ckm.effective_date,'MM-DD-YYYY') edate,ckd.combo_string,ckm.kpi_priority,ckd.include_count,ckd.include_revenue "
			+ "from c_kpi_master ckm "
			+ "left join c_kpi_detail ckd on (ckm.kpi_id = ckd.kpi_id and ckm.effective_date = ckd.effective_date) "
			+ "where ckm.kpi_status='Active'"
			+ "group BY ckm.kpi_id,ckm.kpi_name,ckm.kpi_type,ckm.kpi_status,ckm.effective_date,"
			+ "ckm.kpi_calc_type,ckm.custom_routine,ckd.combo_string,ckd.include_count,ckd.include_revenue "
			+ "order by ckm.kpi_name,ckm.effective_date desc";

	public final String GET_KPI_ACTIVE_LIST_BY_DATE = "select ckm.wstat_string,ckm.kpi_id,ckm.kpi_name,ckm.kpi_type,ckm.kpi_status,"
			+ "ckm.kpi_calc_type ,ckm.custom_routine,ckm.effective_date,ckm.kpi_priority "
			+ "from c_kpi_master ckm "
			+ "where ckm.effective_date in (select max(effective_date) effective_date from c_kpi_master ckm2 group by kpi_name) and ckm.effective_date::varchar <= ? "
			+ "group BY ckm.kpi_id,ckm.kpi_name,ckm.kpi_type,ckm.kpi_status,ckm.effective_date,"
			+ "ckm.kpi_calc_type,ckm.custom_routine "
			+ "order by ckm.kpi_name,ckm.effective_date desc";

	public final String GET_KPI_ACTIVE_LIST_BY_DATE1 = "select ckm.wstat_string,ckm.kpi_id,ckm.kpi_name,ckm.kpi_type,"
			+ "ckm.kpi_status,ckm.kpi_calc_type ,ckm.custom_routine,ckm.effective_date,ckm.kpi_priority "
			+ "from c_kpi_master ckm " + "where ckm.effective_date::varchar <= ? "
			+ "group BY ckm.kpi_id,ckm.kpi_name,ckm.kpi_type,ckm.kpi_status,ckm.kpi_calc_type,ckm.custom_routine,ckm.effective_date "
			+ "order by ckm.kpi_name";

	public final String GET_KPI_ACTIVE_LIST_EXCEL = "select ckm.wstat_string,ckm.kpi_id,ckm.kpi_name,ckm.kpi_type,"
			+ "ckm.kpi_status,ckm.kpi_calc_type ,ckm.custom_routine,ckm.effective_date,"
			+ "ckpd.product_type,ckpd.product_category,ckpd.product,ckm.kpi_priority  " + "from c_kpi_master ckm "
			+ "left join c_kpi_detail ckpd on (ckpd.kpi_id = ckm.kpi_id) " + "where ckm.kpi_status='Active' "
			+ "group BY ckm.kpi_id,ckm.kpi_type,ckm.kpi_status,ckm.effective_date,"
			+ "ckm.kpi_calc_type,ckm.custom_routine,ckpd.product_type,ckpd.product_category,ckpd.product "
			+ "order by ckm.kpi_name,ckm.effective_date desc";

	public final String GET_KPI_INACTIVE_LIST = "select ckm.wstat_string,ckm.kpi_id,ckm.kpi_name,ckm.kpi_type1,"
			+ "ckm.kpi_status,ckm.kpi_calc_type ,ckm.custom_routine,ckm.effective_date,ckm.kpi_priority "
			+ "from c_kpi_master ckm " + "group BY ckm.kpi_id,ckm.kpi_calc_type,ckm.custom_routine "
			+ "order by ckm.kpi_name";

	public final String GET_KPI_PRODUCTS_LIST = "select cpm.product,ckpd.ckpd_id,ckm.kpi_priority "
			+ "from c_kpi_master ckm,c_kpi_detail ckpd,c_product_master cpm "
			+ "where ckm.kpi_id =ckpd.kpi_id and ckpd.product_id =cpm.product_id and ckm.kpi_id =? "
			+ "group BY cpm.product,ckpd.ckpd_id order by cpm.product";

	public final String GET_KPI_TYPE_ONE_LIST = "select field_name,field_value from c_translate_master where effective_status='Y'";

	public final String GET_COMBO_STRING_LIST = "select field_value from c_translate_master where field_name='By Combo String' and effective_status='Y'";

	public final String GET_KPI_TYPE_TWO_LIST = "select field_value from c_translate_master where field_name='KPI Type Global/Plan Specific' and effective_status='Y'";

	public final String GET_KPI_CALC_TYPES = "select field_value from c_translate_master where field_name='KPI Calc Type' and effective_status='Y'";

	public final String GET_PRODUCTS = "select cpm.product_type,cpm.product_category,cpm.product, cpm.product_id "
			+ "from c_product_master cpm where status='Active' and product_group > 2 or product_group = 0 and cpm.product_type != null "
			+ "order by product_type";

	public final String GET_CATEGORIES = "select product_category from c_kpi_detail ckpd where kpi_id =? and product_type=? and effective_date=? group by product_category";

	public final String GET_CAT_COMBOS = "select concat(combo_position,' - ',combo_values) product_category from c_kpi_detail ckpd where kpi_id =? and combo_string=? and effective_date=?";

	public final String GET_PRODUCTS_FROM_KPI_PRODUCT_DETAIL = "select ckpd_id ,product from c_kpi_detail ckpd where kpi_id =? and product_type=? and product_category=? and effective_date=?  group by ckpd_id,product";

	public final String CHECK_PRODUCT_DETAILS_BY_KPIID = "select coalesce(product_id,'0') product_id,coalesce(product_type,'0') product_type,coalesce(combo_string,'0') combo_string from c_kpi_detail ckpd where kpi_id =? and effective_date=?";

	public final String GET_KPIS = "select distinct(ckm.kpi_name) product_type from c_kpi_detail ckpd,c_kpi_master ckm where ckpd.dpdnt_kpi =ckm.kpi_id and ckpd.kpi_id =? and ckpd.effective_date =?";

	public final String GET_COMBOS = "select distinct(combo_string) product_type from c_kpi_detail ckpd where kpi_id =?  and effective_date =?";

	public final String GET_PTYPES = "select product_type from c_kpi_detail ckpd where kpi_id =? and effective_date =? and ckpd.product_type is not null group by ckpd.product_type";

	public final String GET_COMBO_POSITIONS_VALUES = "select combo_position ,combo_values from c_kpi_detail ckd where kpi_id =? and effective_date=? and combo_position notnull";

	public final String GET_DEPEND_KPIS1 = "select array_to_string(array_agg(dpdnt_kpi), ',') dpdnt_kpi  from c_kpi_detail ckd where kpi_id = ? and effective_date=?";
	
	public final String GET_DEPEND_KPIS = "select dpdnt_kpi,coalesce(include_count,'NA') include_count,coalesce(include_revenue,'NA') include_revenue from c_kpi_detail ckd where kpi_id = ? and effective_date=?";

	public final String GET_ALL_KPIS_LIST = "select max(effective_date) effective_date,kpi_id,kpi_name from c_kpi_master ckm where  kpi_status = 'Active' group by kpi_id,kpi_name order by kpi_name";

	public final String GET_SELECTED_PRODUCTS1 = "select array_to_string(array_agg(product_id), ',') product_id,array_to_string(array_agg(product), ',') product from c_kpi_detail ckd  where kpi_id = ?";
	public final String GET_SELECTED_PRODUCTS = "select product_id,product,product_category,product_type from c_kpi_detail ckd  where kpi_id = ? and effective_date=? order by product_type,product_category,product";

}
