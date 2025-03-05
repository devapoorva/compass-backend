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
@Table(name = "t_report_process")
public class ReportProcessModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "run_time_id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "report_instance_id")
	private int reportInstanceId;

	@Column(name = "report_doc_id")
	private int reportDocId;

	@Column(name = "status")
	private String status;

	@Column(name = "start_time")
	private String startTime;

	@Column(name = "end_time")
	private String endTime;

	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_dt")
	private Date updatedDt;

	@Column(name = "updated_by")
	private String updatedBy;
	
	@Transient
	private String reportName;
	
	@Transient
	private String instanceName;
	
	@Transient
	private String reportDesc;
	@Transient
	private String instanceId;
	@Transient
	private String distId;
	@Transient
	private String distName;
	@Transient
	private String distType;
	@Transient
	private String distValue;

}
