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
//@EqualsAndHashCode(callSuper = true)
@Table(name = "c_depndnt_kpi_score_weight")
public class DependentScoreKPIEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "row_id", nullable = false, updatable = false)
	private Long id;
	@Column(name= "comm_plan_dtl_id")
	private int commPlanDtlId ;
	private int  kpi_id ;
	private int complex_kpi_id;
	private int comp_plan_id;
	private String  kpi_name ;
	private float  kpi_weight ;
	private Date  created_dt ;
	private String  created_by ;
	private Date  update_dt ;
	private String  updated_by ;
	@Transient
	private String is_score_vars;
}
