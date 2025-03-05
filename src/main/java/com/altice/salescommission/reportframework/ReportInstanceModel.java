package com.altice.salescommission.reportframework;

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
@Table(name = "c_report_instance")
public class ReportInstanceModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_instance_id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "report_instance_name")
	private String instanceName;

	@Column(name = "report_id")
	private long reportId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "sql_params")
	private String sqlParams;
	
	@Column(name = "sql_labels")
	private String sqlLabels;
	
	@Column(name = "dist_list")
	private int distList;
	
	@Column(name = "report_schedule_str")
	private String scheduleStr;
	
	@Column(name = "created_dt")
	private Date createdDt;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_dt")
	private Date updated_Dt;
	
	@Column(name = "updated_by")
	private String updated_By;
	
	@Transient
	private String distName;
	
	@Transient
	private int distId;
}
