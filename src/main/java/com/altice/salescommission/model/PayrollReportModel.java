package com.altice.salescommission.model;

import java.util.Date;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollReportModel {
	private String name;
	private String scEmpId;
	private String salesRepId;
	private String peopleSoftId;
	private String commPlan;
	private String commAmt;
	private String incentive;
	private String totalAmt;
	private String salesChannel;
	private String adjComments;
}
