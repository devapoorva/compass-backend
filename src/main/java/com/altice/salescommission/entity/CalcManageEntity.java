package com.altice.salescommission.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "c_calc_manage")
public class CalcManageEntity extends AbstractBaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ccm_id", nullable = false, updatable = false)
	private Long id;
	private int comm_plan_id;
	@Column(name = "cal_run_id")
	private int calRunId;
	
	
	private LocalDate adjustment_close_dt;
	private LocalDate dispute_close_dt;
	
	@JsonFormat(pattern = "MM/dd/YYYY")
	@Transient
	private Date adjust_close_dt;
	@JsonFormat(pattern = "MM/dd/YYYY")
	@Transient
	private Date disp_close_dt;
	
	private String calc_close_flag;
	private String created_by;
	private String updated_by;
	private Date created_dt;
	private Date updated_dt;
	
	@Transient
	private String salesChannel;
	
	@Transient
	private String calDt;
	
	@Transient
	private String calendarType;
	
	@Transient
	private String commPlan;
	
	@Transient
	private String loggedinUser;
	
	@Transient
	private String username;
	
	@Transient
	private String password;
}
