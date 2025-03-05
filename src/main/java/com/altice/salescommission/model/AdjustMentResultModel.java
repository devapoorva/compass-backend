package com.altice.salescommission.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdjustMentResultModel {
	
    private int sc_emp_id;
    private int adjustment_id;
    private String name;
    private String user_type;
    private String sales_rep_id;
    private String assc_corps;
    private String sales_rep_channel;
    private int employee_id;
    private Date soft_termination_date;
    private String operator_id;
    private String sales_rep_type;
    private String kpi;
    private Long fixedDollarAmt;
    private String newTier;
    private String comments;

}
