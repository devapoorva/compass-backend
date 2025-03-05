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
@Table(name = "c_json_excel_headers")
public class ExcelHeadersEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rowid", nullable = false, updatable = false)
	private Long id;
	private String pagename;
	private String name;
	private String dataKey;
	private String position;
	private String isSortable;
	private String edit;
	private String isView;
	private String status;
	private String excelstatus;
	private String headerstatus;
	private Date created_dt;
	private String created_by;
	private Date updated_dt;
	private String updated_by;
}
