package com.altice.salescommission.model;

import java.util.Date;

import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallDetailsModel {
	
	private Integer kom_call_id ;
	private String report_date ;
	private int node_id ;
	private String node_name ;
	private int track_node ;
	private String dept ;
	private String call_type ;
	private String transfer_group ;
	private int eaid ;
	private int queue_time ;
	private int talk_time ;
	private int hold_time ;
	private String crc_first_name ;
	private String crc_last_name ;
	private int nco ;
	private int nch ;
	private int aba ;
	private int aht ;
	private int nto ;
	private String lang ;
	private String inbound_outbound_call_flag ;
	private int extn_nbr ;
	private int local_termination_time_id ;
	private String call_end_time ;
	private String call_start_time ;
	private int pr_status_id ;
	private String sales_rep_id ;
	private int sc_emp_id ;
	private String day_part ;
	private String kom_last_modified_date ;
	private String load_date ;
	private String network_id ;
	@Transient
	private Date feedFromDt;
	@Transient
	private Date feedToDt;
}
