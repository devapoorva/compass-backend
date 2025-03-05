package com.altice.salescommission.model;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManageCodesModel {
	
	private String rcode;
	private int f_level;
	private Date valid_from_dt;
	private Date valid_to_dt;
	private Long corp;
	private Date effective_date;
	private Float proAmount;
	
	private Long productid;
	private String productname;
	private String corp_id;
	private String rname;
	private Date effdate;
	private List<Integer> corps;
	private List changeColor;
	private String status;
	private String createdBy;
	private String updatedBy;
	private String assc_corps;
	
	public ManageCodesModel(long long1, String string, String string2, String string3, String string4, String string5,
			java.sql.Date date, java.sql.Date date2, java.sql.Date date3) {
		// TODO Auto-generated constructor stub
	}
}
