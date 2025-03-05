package com.altice.salescommission.model;

import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadJsonHeadersModel {
	private int id;
	private int jsonsqlid;
	private String name;
	private String datakey;
	private String position;
	private String issortable;
	private String edit;
	private String istotalview;
	private String colspancnt;
	private String ishyperlinkview;
	private String isdisputeview;
	private String pagenav;
	private String istotalcalculate;
	private int colorder;
	private String status;
	private String btn_type;
	@Transient
	private String isDisputeSubmitted;
	@Transient
	private String pageName;
	@Transient
	private String kpi_type;
}

