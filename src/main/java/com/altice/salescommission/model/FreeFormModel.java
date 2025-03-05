package com.altice.salescommission.model;

import java.util.Date;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreeFormModel {
	private String searchWith;
	private String  feedType;
	private  int feedID;
	private String searchString;
	private String salesRepId;
    private Date searchDate;

}
