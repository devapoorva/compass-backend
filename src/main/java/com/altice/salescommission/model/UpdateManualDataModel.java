package com.altice.salescommission.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateManualDataModel {
	private int kpiid;
	private String kpi_name;
	private int commplanid;
	private String commplanname;
	private int calrunid;
}
