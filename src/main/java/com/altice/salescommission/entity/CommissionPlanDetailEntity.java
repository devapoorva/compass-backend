package com.altice.salescommission.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "c_comm_plan_detail")
public class CommissionPlanDetailEntity {
	@Transient
	private String kpi_name;
	private String display_name;
	private String display_flg;
	@Column(name = "view_order")
	private Integer viewOrder;
	private float kpi_weightage;
	private String measure_type;
	private String charge_back;
	private int charge_back_days;
	private int non_charge_back_days;
	private String measure_against;
	private String is_percentage;
	private String calculate_score;
	private String is_pooled;
	
	private String commission_payout;
	private String calc_vars_count;
	private String calc_vars_score;
	private String calc_vars_revenue;
	private String is_calc_vars;
	private String is_score_vars;
	private int commplan_rowid;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comm_plan_dtl_id", nullable = false, updatable = false)
	private Long id;
	@Column(name = "comm_plan_id")
	private int commPlanId;
	private int kpi_id;
	private String kpi_type;
	
	
	private Date created_dt;
	private String created_by;
	private Date update_dt;
	private String updated_by;
	
	
	private Date effective_date;
	
	private Integer kpi_goal;
	
	
	@Transient
	private float rangeLow;
	@Transient
	private float range_high;
	@Transient
	private float modifier_percent;
	@Transient
	private float commission_val;
	@Transient
	private String tier;
	@Transient
	private float  target;
	@Transient
	private String comm_plan;
	
	@Transient
	private float comm_pool;
	@Transient
	private String trans_field_value;
	@Transient
	private String trans_field_name;
	@Transient
	private String colorcode;
	@Transient
	private String comm_img_str;
}
