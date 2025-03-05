package com.altice.salescommission.model;

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


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BossCallMappingModel {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "row_id", nullable = false, updatable = false)
//	private Long id;
	private String network_id ;
	private String operator_id ;
	private String class_name ;
	private String sales_rep_id ;
	private String first_name ;
	private String last_name ;
	private Date valid_from_dt  ;
	private Date valid_to_dt  ;
	private String employee_id  ;
	@Transient
	private Date feedDate;
	private String email_id;
	private String to;
	private String subject;
	private String message;
}
