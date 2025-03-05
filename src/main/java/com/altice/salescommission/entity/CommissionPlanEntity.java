package com.altice.salescommission.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "c_comm_plan_master")
public class CommissionPlanEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "row_id", nullable = false, updatable = false)
	private Long id;
	
	@Column(name = "comm_plan_id")
	private int  commPlanId;
	
	private String sales_channel;
	private String sales_channel_desc;
	
	private String comm_plan_type;
	
	@Column(name = "comm_plan")
	private String comPlan;
	
	@Column(name = "comm_plan_desc")
	private String commPlan;
	
	private String active_flag;

	private String commercial_flag;
	private String winbackpool_flag;
	private String partof_sup_mgr_plan;

	private String display_flag;
	private Integer day_multiplier;
	private String show_perf;
	private String language;
	private Date created_dt;
	private String created_by;
	private Date effective_date;
	private String calendar_type;

	private String charge_back;
	private Integer charge_back_days;
	private Integer non_pay_charge_back_days;
	private int comm_plan_priority;
	
	private Date update_dt;
	private String updated_by;
	
	private float comm_pool;
	
	private String charge_back_type;
	
	@Type(type = "org.hibernate.type.BinaryType")
	@Column(name = "comm_img")
	@Lob
	private byte[] comm_img;
	
	
	@Transient
	private String trans_field_value;
	@Transient
	private String eDate;
	@Transient
	private String status;
	@Transient
	private String comm_img_str;
	
}
