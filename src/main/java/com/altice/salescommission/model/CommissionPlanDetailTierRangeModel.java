package com.altice.salescommission.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionPlanDetailTierRangeModel {
	private int id;
	private Long comm_plan_dtl_id;
	private Long cdtr_id;
	private int comm_plan_id;
	private String comm_plan;
	private int kpi_id;
	private String kpi_name;
	private String kpi_type;
	private float kpi_weightage;
	private String display_flg;
	private float comm_pool;
	private float rangeLow;
	private float range_high;
	private float modifier_percent;
	private float commission_val;
	private Date created_dt;
	private String created_by;
	private Date update_dt;
	private String updated_by;
	private String tier;
	private Float  target;
	private String display_name;
	private String measure_type;
	private Date effective_date;
	private Integer viewOrder;
	private Integer kpi_goal;
	
	private String charge_back;
	private Integer charge_back_days;
	private Integer non_charge_back_days;
	
	private String measure_against;
	private String is_percentage;
	private String calculate_score;
	private String is_pooled;
	
	private String commission_payout;
	private String calc_vars_score;
	private String calc_vars_count;
	private String calc_vars_revenue;
	private String is_calc_vars;
	private String is_score_vars;
	private String colorcode;
	
}
