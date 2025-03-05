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
@Table(name = "c_employee_corp_reln")
public class EmployeeCorpEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ecr_id", nullable = false, updatable = false)
	private Long id;
	private String sc_emp_id;
	private int corp;
    private Date valid_from_dt;
    private Date valid_to_dt;
	private String created_by;
	private Date created_dt;
	private Date effective_date;
}
