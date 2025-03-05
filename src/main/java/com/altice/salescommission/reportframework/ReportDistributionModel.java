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
@Table(name = "c_report_dist_list")
public class ReportDistributionModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "dist_id")
	private long distId;

	@Column(name = "dist_name")
	private String distName;
	
	@Column(name = "dist_type")
	private String distType;
	
	@Column(name = "dist_value")
	private String distValue;
	
	@Column(name = "created_dt")
	private Date createdDt;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_dt")
	private Date updatedDt;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "status")
	private String status;
	
	@Transient
	private String empId;
	
	@Transient
	private String empName;
	
	@Transient
	private int userId;
	
	@Transient
	private String roleName;
	
	@Transient
	private String email;
}
