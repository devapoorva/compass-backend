package com.altice.salescommission.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComboStringModel {
	private String productName;
    private Integer position;
    private String currentValue;
    private String newValue;
    private int corp;
    private Date wfinDate;
    private String type;

}
