package com.altice.salescommission.entity;

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
@Table(name = "t_adjustment_detail")
public class AdjustmentsDetailEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "adjustment_id", nullable = false, updatable = false)
	private Long id;
	private String sc_emp_id;
	@Transient
	private String empname;
	private String cal_run_id;
	private Integer comm_plan_id;
	@Transient
	private String commplanname;
	private Integer kpi_id;
	@Transient
	private String kpi_name;
	private Float adjustment;
	private String adjustment_comments;
	private String commission_period;
	private String kpi;
	
	
	
	
}
