package com.altice.salescommission.queries;

public interface KomFeedQueries {

	public final String GET_SALES_REPS = "select distinct sales_rep_id  from fd_kom_feed fkf "
			+ "where corp=? and house =? and wordate >=? and wordate <=? order by sales_rep_id ";

	public final String GET_PRODUCT_NAME = "select  distinct svcs_reporting_name from kom_svcs_rcode_stg ksrs "
			+ "where pos =? and corp =? and begin_date <= ? and end_date >= ? and val = ? and lower(svcs_type) like ?  ";
//	concat(svcs_reporting_name,' - ',pos,' - ',val) svcsreportingname
	public final String GET_PRODUCT_NAMES = "select distinct  svcs_reporting_name,svcs_reporting_name svcsreportingname from kom_svcs_rcode_stg where  corp =? and begin_date <= ? and end_date >= ?  and lower(svcs_type) like ?";

	public final String GET_PRODUCT_INFO = "select distinct pos ,val ,svcs_reporting_name from  kom_svcs_rcode_stg where "
			+ "begin_date <=? and end_date >= ? and corp =?  and svcs_reporting_name =?";

	public final String GET_FEED_RESULTS = "select fkf.kom_feed_id,fkf.sales_rep_id ,fkf.corp ,fkf.house,fkf.cust , fkf.wpcnt ,fkf.wstat,fkf.wordate ,fkf.wddate ,fkf.wfindate,concat(cwbim.fname ,' ',cwbim.lname) custname,"
			+ "to_char(fkf.wordate,'MM-DD-YYYY') wordate_stg,to_char(fkf.wddate,'MM-DD-YYYY') wddate_stg,to_char(fkf.wfindate,'MM-DD-YYYY') wfindate_stg,fkf.pr_status_id,fkf.prev_misc_combo_str,fkf.curr_misc_combo_str  "
			+ "from fd_kom_feed fkf "
			+ "left join c_wip_buy_info_master cwbim on (fkf.kom_feed_id = cwbim.kom_feed_id)   "
			+ "where fkf.corp =? and fkf.house =? and fkf.wordate >=? and fkf.wordate <=? and (1=? or fkf.sales_rep_id=?) "
			+ "group by fkf.kom_feed_id,fkf.sales_rep_id ,fkf.corp ,fkf.house,fkf.cust , fkf.wpcnt ,fkf.wstat,fkf.wordate ,fkf.wddate ,fkf.wfindate,custname,fkf.pr_status_id,fkf.prev_misc_combo_str,fkf.curr_misc_combo_str "
			+ "order by fkf.wordate";
	
	public final String GET_FEED_RESULTS_EXCEL = "select fkf.kom_feed_id,fkf.sales_rep_id ,fkf.corp ,fkf.house,fkf.cust , fkf.wpcnt ,fkf.wstat,fkf.wordate ,fkf.wddate ,fkf.wfindate,concat(cwbim.fname ,' ',cwbim.lname) custname,"
			+ "to_char(fkf.wordate,'MM-DD-YYYY') wordate_stg,to_char(fkf.wddate,'MM-DD-YYYY') wddate_stg,to_char(fkf.wfindate,'MM-DD-YYYY') wfindate_stg ,"
			+ "cwbim.serty_id,cwbim.ratecd,cwbim.ratesign,cwbim.sercnt,cwbim.samt,cwbim.f_level  "
			+ "from fd_kom_feed fkf "
			+ "left join c_wip_buy_info_master cwbim on (fkf.kom_feed_id = cwbim.kom_feed_id)   "
			+ "where fkf.corp =? and fkf.house =? and fkf.wordate >=? and fkf.wordate <=? and (1=? or fkf.sales_rep_id=?) "
			+ "group by fkf.kom_feed_id,fkf.sales_rep_id ,fkf.corp ,fkf.house,fkf.cust , fkf.wpcnt ,fkf.wstat,fkf.wordate ,fkf.wddate ,fkf.wfindate,custname ,"
			+ "cwbim.serty_id,cwbim.ratecd,cwbim.ratesign,cwbim.sercnt,cwbim.samt,cwbim.f_level "
			+ "order by fkf.wordate";
	
	public final String GET_FEED_RESULTS_BY_REP = "select fkf.sales_rep_id ,fkf.corp ,fkf.house,fkf.cust,fkf.wpcnt,fkf.wstat,fkf.wordate,fkf.wddate,"
			+ "fkf.wfindate,fkf.kom_feed_id,cwbim.fd_wip_buy_info_id ,cwbim.serty_id,cwbim.ratecd,cwbim.ratesign,cwbim.sercnt,cwbim.samt,cwbim.f_level "
			+ "from fd_kom_feed fkf ,c_wip_buy_info_master cwbim "
			+ "where fkf.kom_feed_id = cwbim.kom_feed_id and fkf.kom_feed_id=? ";

	public final String GET_FEED_RESULTS_BY_WODT = "select fkf.sales_rep_id ,fkf.corp ,fkf.house,fkf.cust,fkf.wpcnt,fkf.wstat,fkf.wordate,fkf.wddate,"
			+ "fkf.wfindate,fkf.kom_feed_id,cwbim.fd_wip_buy_info_id ,cwbim.serty_id,cwbim.ratecd,cwbim.ratesign,cwbim.sercnt,cwbim.samt,cwbim.f_level "
			+ "from fd_kom_feed fkf ,c_wip_buy_info_master cwbim "
			+ "where fkf.kom_feed_id = cwbim.kom_feed_id and fkf.corp =? and  fkf.house = ? and fkf.wordate >=? and fkf.wordate <=? ";

	public final String GET_RATE_CODE_INFO = "select distinct corp,concat(rcode,' (',rdesc,')') rcode,f_level  from c_rateinfo_master crcm where corp= ? and rcode =? and valid_to_dt::varchar >= ?";
	
	public final String GET_REPORTING_CENTERS_INFO = "select DISTINCT rctr01,rctr02,rctr03,	rctr04,	rctr05,	rctr06,	rctr07,	rctr08,	rctr09,	rctr10,	rctr11,	"
			+ "rctr12,	rctr13,	rctr14,	rctr15,	rctr16,	rctr17,	rctr18,	rctr19,	rctr20,	rdesc from c_rateinfo_master crcm where corp= ? and rcode =?  and valid_to_dt::varchar >=?";

	public final String GET_OOL_LIST = "select descrip1 ,descrip2,kom_code  from c_prod_master where substring(descrip1,1,1) in ('0','1') and descrip2 is not null and substring(descrip1,3,1) <> '1' and descrip2 not like '%FreeWheel%'";

	public final String GET_CALLDATA_BY_CALLID = "select * from kom_call_daypart_details kcdd where kom_call_id =?";

	public final String GET_CALLDATA_BY_ALL = "select * from kom_call_daypart_details kcdd where report_date >= ?  and report_date <=? and sales_rep_id = ? "
			+ " and kom_call_id =?";

	public final String GET_CALLDATA_BY_FDCALLID = "select * from kom_call_daypart_details kcdd where report_date >= ?  and report_date <= ? "
			+ " and kom_call_id =?";

	public final String GET_CALLDATA_BY_FD = "select * from kom_call_daypart_details kcdd where report_date >= ?  and report_date <=? ";

	public final String GET_CALLDATA_BY_REPCALLID = "select * from kom_call_daypart_details kcdd where  sales_rep_id = ? "
			+ " and kom_call_id =?";

	public final String GET_CALLDATA_BY_REPID = "select * from kom_call_daypart_details kcdd where  sales_rep_id = ? ";

}
