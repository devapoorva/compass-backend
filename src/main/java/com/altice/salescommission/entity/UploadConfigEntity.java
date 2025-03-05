package com.altice.salescommission.entity;

import java.util.Date;

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
@Table(name = "c_upload_config")
public class UploadConfigEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rowid", nullable = false, updatable = false)
	private Long id;
	private int templateid;
	private String uploadtemplatename;
	private String recordtemplate;
	private String actiontype;
	private String transformationroutine;
	private String uploadtemplatedesc;
	private String columnname;
	private String columntype;
	private String status;
	private Date created_dt;
	private String created_by;
	private Date updated_dt;
	private String updated_by;
	private String sqlquery;
	private int columnorder;
}
