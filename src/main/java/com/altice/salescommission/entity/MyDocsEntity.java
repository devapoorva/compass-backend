package com.altice.salescommission.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "c_com_doc_mgmt")
public class MyDocsEntity extends AbstractBaseEntity {
	private static final long serialVersionUID = 1L;
	
	@Transient
	private String comm_plan_desc;
	private String user_type;
	private Date effective_dt;
	@Column(name = "employee_id")
	private String employeeId;
	@Transient
	private String employeeName;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comm_doc_id", nullable = false, updatable = false)
	private Long id;
	private int comm_plan_id;
	@Transient
	private String sc_emp_id;
	
	private String doc_name;
	private String doc_desc;
	
	private Date created_dt;
	private String created_by;
	private String updated_by;

	@Transient
	private String approved_dt;

	

	@Transient
	private String comm_doc_str;
	@Transient
	private String approved_status;
	private String status;

	@Type(type = "org.hibernate.type.BinaryType")
	@Column(name = "comm_doc")
	@Lob
	private byte[] comm_doc;
	
	@Transient
	private List<Map<String, String>> employeesList;

	public MyDocsEntity(byte[] data) {
		this.comm_doc = data;
	}

	@Transient
	private String salesChannel;
	@Transient
	private String salesChannelDesc;
	
	@Transient
	private String signOffDate;
	@Transient
	private String uploadedDt;
	@Transient
	private String eDate;
	
	

	

}
