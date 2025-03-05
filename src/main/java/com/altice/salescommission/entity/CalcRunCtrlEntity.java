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
@Table(name = "c_calc_run_ctl")
public class CalcRunCtrlEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ccrc_id", nullable = false, updatable = false)
	private Long id;
	private String comm_plan_id;
	private String cal_run_id;
	private String run_control_name;
	private String description;
	private String locked;
	private String sc_emp_id;
	private String created_by;
	private String updated_by;
	private Date created_dt;
	private Date updated_dt;
	private String saleschannel;
	@Column(name = "ccr_id")
	private int runCtrlId;
	private String run_all_open_flg;
	@Transient
	private String commPlan;
	@Transient
	private String empName;
}
