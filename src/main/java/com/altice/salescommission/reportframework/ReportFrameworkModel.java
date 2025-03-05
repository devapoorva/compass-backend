package com.altice.salescommission.reportframework;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
@Table(name = "c_report_master")
public class ReportFrameworkModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "report_name")
	private String reportName;

	@Column(name = "report_desc")
	private String reportDesc;

	@Column(name = "report_sql")
	private String reportSql;

	private String status;

	@Column(name = "report_type")
	private String reportType;

	private String category;
	
	@Transient
	private List<LinkedHashMap<String, Object>> fieldsrunreport;

	@Transient
	private List<LinkedHashMap<String, String>> fields;
	
	@Transient
	private List<LinkedHashMap<String, String>> linkedfields;
	
	@Column(name = "created_dt")
	private Date createdDt;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_dt")
	private Date updated_Dt;
	
	@Column(name = "updated_by")
	private String updated_By;
	
	@Transient
	private String sqlParams;

}
