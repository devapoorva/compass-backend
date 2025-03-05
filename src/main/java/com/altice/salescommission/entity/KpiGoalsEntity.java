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
@Table(name = "t_kpi_goal")
public class KpiGoalsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "goal_id", nullable = false, updatable = false)
	private Long id;
	private String sc_emp_id;
	@Transient
	private String empname;
	private Integer cal_run_id;
	private Integer comm_plan_id;
	@Transient
	private String commplanname;
	private Integer kpi_id;
	@Transient
	private String kpi_name;
	private Float yield_goal;
	private String comm_plan_type;
	private String upload_type;
	
}
