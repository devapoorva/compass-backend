package com.altice.salescommission.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_mob_sales_data")
public class MobileFeedModel {
	@Id
	private Long mob_feed_id;
	private int pr_status_id;
	
	private String transaction_type_name;
	private String trans_type;
	private String account_number;
	private String first_name;
	private String last_name;
	private String employee_id;

	private String account_sales_rep_id;
	private String subscriber_number;
	private String transaction_complete_date;
	
	private String account_start_date;
	private String line_add_create_datetime;
	private String line_disco_create_datetime;
	
	
	private String msisdn;
	private String order_id;
	private String line_status;
	private String dayslineaddedfromacctstart;
	private String days_line_active;
	
	private String plan_description;
	private String fixed_line_cust;
	
	private Double elig_revenue;
	private String sim_activation_date;
	
	private String line_add_status;
	private String line_disco_status;
	private String service_plan;
	private int total_count;
	private String line_sales_rep_id;
	private String last_modified_by;	
	private String created_by;	
	
	@Transient
	private String transaction_complete_from_date;
	@Transient
	private String transaction_complete_to_date;
	
	
}
