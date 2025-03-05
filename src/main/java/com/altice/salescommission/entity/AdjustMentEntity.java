package com.altice.salescommission.entity;

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
@Table(name = "t_adjustment_detail")
public class AdjustMentEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "adjustment_id", nullable = false, updatable = false)
	private Long id;		
	
	@Column(name = "commission_period")
	private String commissionPeriod;
	
	@Column(name = "kpi")
	private String kpi;	
	
	@Column(name = "adjustment_comments")
	private String comments;
	
	@Column(name = "sc_emp_id")
	private int scEmpId;
	
	@Column(name = "adjustment")
	private Long fixedDollarAmt;
	
	private int cal_run_id;

}
