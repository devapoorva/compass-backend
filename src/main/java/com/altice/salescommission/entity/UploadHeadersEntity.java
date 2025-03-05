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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "c_json_sql_headers")
public class UploadHeadersEntity extends AbstractBaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rowid", nullable = false, updatable = false)
	private Long id;
	private int jsonsqlid;
	private String name;
	private String datakey;
	private String position;
	private String issortable;
	private String edit;
	private String istotalview;
	private String colspancnt;
	private String ishyperlinkview;
	private String pagenav;
	private String created_by;
	private Date created_dt;
	private String updated_by;
	private Date updated_dt;
	private String istotalcalculate;
	private String isdisputeview;
	private int colorder;

}
