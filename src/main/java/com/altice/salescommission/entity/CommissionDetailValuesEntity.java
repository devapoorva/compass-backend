package com.altice.salescommission.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "c_comm_detail_values")
public class CommissionDetailValuesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cdtr_id", nullable = false, updatable = false)
	private Long id;
	@Column(name= "comm_plan_dtl_id")
	private int commPlanDtlId ;
	private String range_val_type;
	@Column(name= "range_low")
	private float rangeLow;
	private float range_high;
	private float modifier_percent;
	private float commission_val;
	private Date created_dt;
	private String created_by;
	private Date update_dt;
	private String updated_by;
	private String tier;
	private Float target;
	private Integer comm_plan_id;
	private int kpi_id;
}
