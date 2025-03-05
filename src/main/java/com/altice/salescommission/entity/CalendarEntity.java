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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "c_comm_calendar")
public class CalendarEntity extends AbstractBaseEntity {
	private static final long serialVersionUID = 1L;
	
	private Date valid_from_dt;
	private Date valid_to_dt;
	private Date payroll_due_dt;
	private Date pay_dt;
	private String unlock;
	private String off_cycle;
	private String issalesrepaccess;
	private int cal_run_id;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comm_cal_id", nullable = false, updatable = false)
	private Long id;
	private String calendar_type;
	
	
	private String active_flag;
	
	
	private Date created_dt;
	private String created_by;
	private Date updated_dt;
	private String updated_by;
	@Transient
	private boolean editMode;
	@Transient
	private String commPeriod;
	@Transient
	private String userRole;
	

}
