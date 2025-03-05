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
@Table(name = "c_wip_buy_info_master")
public class WipBuyInfoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fd_wip_buy_info_id", nullable = false, updatable = false)
	private Long id ; 
	private int ccorp ;
	private String house ;
	private int kom_feed_id ;
	private String serty_id ;
	private int corp ;
	private String cust ;
	private String wpcnt ;
	private Date wordate ;
	private Date ordertime ;
	private Date widate ;
	private Date wbdate ;
	private Date wfindate ;
	private int wjob ;
	private String wstat ;
	private int wtech ;
	private String slsrep ;
	private String who ;
	private String wdwo ;
	private int wntrk ;
	private String wcampg ;
	private String wsalrn ;
	private String wordrsn ;
	private String wchrsn ;
	private String wcdrsn ;
	private String wcycle ;
	private String wpro ;
	private String wmode ;
	private String wdiscnt ;
	private String wpdper ;
	private String wperiod ;
	private int ftax ;
	private int mgt ;
	private String zipcode ;
	private String zip4;
	private String dwell ;
	private String complex;
	private String census ;
	private String clust ;
	private String geocd ;
	private String stnum;
	private String fract ;
	private String dir ;
	private String name;
	private String apt ;
	private String aptn;
	private String lname ;
	private String fname ;
	private String ctype ;
	private String cinfo ;
	private int rareacd ;
	private String rphon ;
	private String cycle ;
	private String ratecd ;
	private String ratesign ;
	private int sercnt ;
	private Double samt;
	private int f_level ;
	private Date feed_dt ;

}
