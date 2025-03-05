package com.altice.salescommission.entity;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "c_com_doc_trans")
public class MyDocTransEntity extends AbstractBaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Transient
	private String doc_name;
	
	@Transient
	private String doc_desc;
	
	@Column(name = "employee_id")
	private String employeeId;
	
	
	@Transient
	private String employeeName;
	@Transient
	private String comm_plan;
	private Date approved_on;
	private int comm_doc_id;
	@Transient
	private String signOffDate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comm_doc_trans_id", nullable = false, updatable = false)
	private Long id;
	
	private int comm_plan_id;
	@Column(name = "sc_emp_id")
	private String scempId;
	
	private String user_type;
	private Date effective_dt;
	private String status;
	private String approved_status;
	private Date created_dt;
	private String created_by;
	private Date updated_dt;
	private String updated_by;
	
	private String approved_by;
	private Date pending_from_dt;
	
	private String tracking_id;
	
	@Transient
	private String eDate;
	
	public MyDocTransEntity(String doc_name, String doc_desc, String employeeId, String employeeName, String comm_plan,
			String signOffDate, java.sql.Date approved_on, int comm_doc_id, Date effective_dt, String edate) {
		this.doc_name = doc_name;
        this.doc_desc = doc_desc;
        
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        
        this.comm_plan = comm_plan;
        this.signOffDate = signOffDate;
        
        this.approved_on = approved_on;
        this.comm_doc_id = comm_doc_id;
        
        this.effective_dt = effective_dt;
        
        this.eDate = edate;
	}
	
}
