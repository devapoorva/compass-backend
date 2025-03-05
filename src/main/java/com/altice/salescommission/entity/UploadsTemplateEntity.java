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
@Table(name = "c_upload_template")
public class UploadsTemplateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rowid", nullable = false, updatable = false)
	private Long id;
	private int templateid;
	private String recordname;
	private String recordtemplate;
	private String recorddesc;
	private String columnname;
	private String columntype;
	private String templateheader;
	private int columnorder;
	private String status;
	private Date created_dt;
	private String created_by;
	private Date updated_dt;
	private String updated_by;
	
}
