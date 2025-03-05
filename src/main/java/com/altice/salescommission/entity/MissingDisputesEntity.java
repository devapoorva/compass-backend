package com.altice.salescommission.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "c_missing_disputes")
public class MissingDisputesEntity {
	@Id
	@Column(name = "rowid", nullable = false, updatable = false)
	private Long id;
	private String kpi_id;
	private int comm_plan_id;
	private String cust_id;
	private int corp;
	private int house;
	private float revenue;
	private Date wordate;
	private Date wfindate;
	private String message;
	private String comment;
	private Date created_dt;
	private String created_by;
	private Date updated_dt;
	private String updated_by;
	private String sales_rep_id;
	
	@Transient
	private String wordt;
	@Transient
	private String wfindt;
	@Transient
	private String createddt;
	@Transient
	private String kpidisplayname;
}
