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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "c_comm_calrunids")
public class CalRunIdsEntity extends AbstractBaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "cal_run_id")
	private int calRunId;
	private String description;
	private String active_flag;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "crun_id", nullable = false, updatable = false)
	private Long id;

	@Transient
	private String userId;

	@Transient
	private String uid;

	@Transient
	private String runControlName;

	@Transient
	private String runControlDesc;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss
	// Z", timezone="America/New_York")
	@Transient
	private String startTime;
	
	@Transient
	private String endTime;

	@Transient
	private String run_status;

	@Transient
	private int comm_plan_id;

	@Transient
	private String progress;

	@Transient
	private int runCtrlId;

}
