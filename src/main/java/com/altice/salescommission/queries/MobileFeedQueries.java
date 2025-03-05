package com.altice.salescommission.queries;

public interface MobileFeedQueries {
	public final String GET_MOBILE_FEED_RESULTS = "select mob_feed_id,	 pr_status_id,	 transaction_type_name,	 "
			+ "transaction_complete_date,		"
			+ "account_start_date,	 "
			+ "line_add_create_datetime,	"
			+ "line_disco_create_datetime,	 "
			+ "account_number,	 first_name,	 last_name,"
			+ "employee_id,	 account_sales_rep_id,	 subscriber_number,	 msisdn,	 order_id,	 line_status,	 dayslineaddedfromacctstart,	 days_line_active,"
			+ "plan_description,	 fixed_line_cust,		 trans_type,	 elig_revenue,	 sim_activation_date,	 line_add_status,	 line_disco_status,	 service_plan,	 total_count,	 line_sales_rep_id "
			+ "from t_mob_sales_data where transaction_complete_date between ? and ? "
			+ "and (1=? or account_number=?) and (1=? or first_name=?) and (1=? or last_name =?) and (1=? or subscriber_number=?) and (1=? or msisdn = ?)";
	
	public final String GET_MOBILE_FEED_RESULTS_BY_ID = "select mob_feed_id,	 pr_status_id,	 transaction_type_name,	 "
			+ "transaction_complete_date,		"
			+ "account_start_date,	 "
			+ "line_add_create_datetime,	"
			+ "line_disco_create_datetime,	 "
			+ "account_number,	 first_name,	 last_name,"
			+ "employee_id,	 account_sales_rep_id,	 subscriber_number,	 msisdn,	 order_id,	 line_status,	 dayslineaddedfromacctstart,	 days_line_active,"
			+ "plan_description,	 fixed_line_cust,		 trans_type,	 elig_revenue,	 sim_activation_date,	 line_add_status,	 line_disco_status,	 service_plan,	 total_count,	 line_sales_rep_id "
			+ "from t_mob_sales_data where mob_feed_id= ?";
}
