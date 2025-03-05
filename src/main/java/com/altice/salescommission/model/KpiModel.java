package com.altice.salescommission.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KpiModel {

	private String kpi_name;
	private Long id;
	private Date effective_date;
	private String edate;
	private String kpi_calc_type;
	private String custom_routine;
	private String kpi_type;
	private String feed_type;
	private String kpi_status;

	private String product_type;
	private String product_category;
	private String product;
	private String productid;
	private Date created_dt;
	private String created_by;
	private Date update_dt;
	private String updated_by;
	private String trans_field_value;
	private String trans_field_name;
	private String wstat_string;

	private String depndentKpis;

	private List<Map<String, String>> products;
	private String combo_string;

	private String combo_position;
	private String combo_values;
	private String dupli_cat;
	private String dupli_pro;
	private int kpi_priority;

	private String include_count;
	private String include_revenue;
	
	private boolean includecount;
	private boolean includerevenue;
	private int stat;
	

}
