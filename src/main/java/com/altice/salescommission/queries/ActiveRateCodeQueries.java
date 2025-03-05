package com.altice.salescommission.queries;

public interface ActiveRateCodeQueries {
	public final String GET_PRODUCTS = "select product_id,product from c_product_master where status='Active' and product_group > 2 or product_group = 0 order by product";

	public final String GET_SALES_CHANNELS = "select description salesChannelDesc ,field_value salesChannelFieldValue from c_translate_master where assc_column ='sales_channel' and effective_status='Y'";

	public final String GET_CORPS = "select corp_id from c_corp_master order by corp_id ASC";

	public final String GET_RATE_CODE_DETAILS = "select crcm.corp, crcm.rcode,crcm.rdesc,crcm.valid_from_dt ,crcm.valid_to_dt "
			+ "from c_rate_code_master crcm "
			+ "join c_product_detail cpd on crcm.rcode = cpd.ratecd and crcm.corp = cast(cpd.corp as integer) "
			+ "join (select distinct ccpm.sales_channel , kpd.product_id from c_kpi_detail kpd "
			+ "join c_comm_plan_detail ccpd on kpd.kpi_id = ccpd.kpi_id "
			+ "join c_comm_plan_master ccpm on ccpm.comm_plan_id = ccpd.comm_plan_id "
			+ "where kpd.product_id =? and ccpm.sales_channel =?) as sc_pr on sc_pr.product_id = cpd.product_id "
			+ "where crcm.valid_from_dt=? and crcm.corp =?";
}
