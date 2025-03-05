package com.altice.salescommission.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fd_kom_feed")
public class KomFeedEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "kom_feed_id", nullable = false, updatable = false)
	private Long id ;
	private String house ; 
	private String cust ; 
	private String wpcnt ; 
	private String wstat ; 
	private Date wordate ; 
	private Date wfindate ; 
	private Date wddate ; 
	private String who ; 
	private String sales_rep_id ;
	private Integer tech ; 
	private Integer sales_addition_code ; 
	private Integer addition_code_id ; 
	private Integer deletion_code_id ; 
	private Integer curr_product_class_id ; 
	private Integer prev_product_class_id ; 
	private Integer curr_mdm_tier_rpt ; 
	private Integer prev_mdm_tier_rpt ; 
	private String prem_upgr_downgr_flag ; 
	private Double net_rev_change ;
	private String cust_type ; 
	private String revenue_upgr_downgr_flag ; 
	private String io_upgr_downgr_flag ;
	private String winback_flag ; 
	private Date feed_dt ; 
	private Integer recurring_rev ;
	private Integer video_add_flag ;
	private Integer ool_add_flag;
	private Integer restel_add_flag ;
	private Integer io_add_flag ;
	private Integer ov_add_flag ;
	private Integer comm_year ; 
	private String comm_month ;
	private Integer promo_amt ;
	private Date last_modified_on;
	private Integer Integerernal_net_ool ;
	private String flag1 ; 
	private String flag2 ; 
	private String flag3 ; 
	private String flag4 ; 
	private String flag5 ; 
	private String flag6 ; 
	private String flag7 ; 
	private String flag8 ;
	private String wcampg ; 
	private String prev_prem_combo_str ;
	private String curr_prem_combo_str ;
	private String prev_sports_combo_str ;
	private String curr_sports_combo_str ;
	private String prev_svod_combo_str ;
	private String curr_svod_combo_str ;
	private String prev_Integerl_combo_str ; 
	private String curr_Integerl_combo_str ; 
	private String prev_io_combo_str ;
	private String curr_io_combo_str ;
	private String prev_ool_combo_str ;
	private String curr_ool_combo_str ;
	private String prev_ov_combo_str ;
	private String curr_ov_combo_str ;
	private String prev_misc_combo_str ;
	private String curr_misc_combo_str ;
	
	private String drsn ; 
	private String drsn_remove_chgbk ; 
	
	private String m2m_speed ; 
	private Date ordertime ; 
	
	private String add_svod_combo_str ;
	private String del_svod_combo_str ;
	
	private String customer_type ;
	private Date begin_date ; 
	private Date created_dt ; 
	private String created_by ;
	private Date updated_dt ; 
	private String updated_by ;
	private Integer vr_unit ; 
	private Integer vr_unit_revenue ;
	private Integer sip_acq ; 
	private Integer sip_conv_unit ; 
	private Double pia_revenue ;
	
	private Integer sip_add_revenue ; 
	private Integer sip_remove_revenue ; 
	private Integer vow_unit ; 
	private Integer vow_unit_revenue ;  
	private Integer svod_revenue_change ; 
	private Integer misc_video_revenue_change ; 
	private Integer total_dvr_revenue_change ; 
	private Integer dvr_revenue_change ; 
	private Integer curr_chrg_buffers ; 
	private Integer curr_nonchrg_buffers ; 
	private Integer prev_chrg_buffers ; 
	private Integer prev_nonchrg_buffers ; 
	private Integer ool_revenue_change ; 
	private Integer iptel_revenue_change ; 
	private Integer analog_revenue_change ; 
	private Integer io_revenue_change ; 
	private Integer curr_addl_did_blocks ; 
	private Integer prev_addl_did_blocks ; 
	private Integer m2m_revenue_change ; 
	 
	private Integer install_revenue ; 
	private Integer mdu_flag ; 
	private Integer auto_ind ; 
	private Integer auto_pay ; 
	private Integer account_id ;
	private Integer prev_non_ported_lines ; 
	private Integer curr_non_ported_lines ; 
	private Integer prev_tf_lines ; 
	private Integer curr_tf_lines ; 
	private Integer dvr_plus_add_flag ; 
	private Integer dvr_plus_free_flag ; 
	private Integer dvr_plus_remove_flag ; 
	private Double dvr_plus_unit_revenue ; 
	private Integer dvr_unit_revenue ; 
	private Integer free_dvr_unit ; 
	private Integer dvr_plus_unit ; 
	private Integer trunk_units ; 
	private Integer sip_add_units ; 
	private Integer sip_remove_units ; 
	private Integer free_dvr_unit_revenue ; 
	private Integer exclude_from_yield ; 
	private Integer net_ov ; 
	private Integer Integerernal_net_ov ; 
	private Integer zip_blitz ; 
	private Integer dvr_unit ; 
	private Double dvr_revenue ; 
	private Integer prev_new_video_offer_id ; 
	private Integer curr_new_video_offer_id ;  
	private Double net_rev_change_a ;
	private Double net_rev_change_o ;
	private Double net_rev_change_d ;
	private Double net_rev_change_ov ;
	private Integer net_rev_change_restel ;
	private Integer net_prem_chanels ; 
	private Integer net_svod ; 
	private Integer net_sports ; 
	private Integer net_converters ; 
	private Integer net_restel ; 
	private Integer net_ool ; 
	private Integer pr_status_id ; 
	private String last_modified_by ; 
	private Integer employee_pay_id ; 
	private Integer corp ; 
	private Integer flat_rate_ncr ;
	private String si_activation_flag;
	private Date si_activation_date;
	@Transient
	public Date orderFromDate ;
	@Transient
    public Date orderToDate;
	@Transient
	public WipBuyInfoEntity wipBuyInfoModel;
	
	@Transient
	public String serty_id;
	@Transient
	public String ratecd;
	@Transient
	public String rdesc;
	@Transient
	public String ratesign;
	@Transient
	public Integer sercnt;
	@Transient
	public Double samt;
	@Transient
	public Integer f_level;
	@Transient
	public Integer wibBuyInfoID;
	@Transient
	public String svcsType;
	
	@Transient
	public String descrip1;
	@Transient
	public String descrip2;
	@Transient
	public String status;
	@Transient
	public int komfeedid;
	
	@Transient
	public String productName;
	
	@Transient
	public String custname;
	
	@Transient
	public String wordate_stg;
	@Transient
	public String wfindate_stg;
	@Transient
	public String wddate_stg;
	
	@Transient
	public int kom_code;
	
	@Transient
	public String rctr01;
	@Transient
	public String rctr02;
	@Transient
	public String rctr03;
	@Transient
	public String rctr04;
	@Transient
	public String rctr05;
	@Transient
	public String rctr06;
	@Transient
	public String rctr07;
	@Transient
	public String rctr08;
	@Transient
	public String rctr09;
	@Transient
	public String rctr10;
	
	@Transient
	public String rctr11;
	@Transient
	public String rctr12;
	@Transient
	public String rctr13;
	@Transient
	public String rctr14;
	@Transient
	public String rctr15;
	@Transient
	public String rctr16;
	@Transient
	public String rctr17;
	@Transient
	public String rctr18;
	@Transient
	public String rctr19;
	@Transient
	public String rctr20;
	
	@Transient
	public String rc01;
	@Transient
	public String rc02;
	@Transient
	public String rc03;
	@Transient
	public String rc04;
	@Transient
	public String rc05;
	@Transient
	public String rc06;
	@Transient
	public String rc07;
	@Transient
	public String rc08;
	@Transient
	public String rc09;
	@Transient
	public String rc10;
	
	@Transient
	public String rc11;
	@Transient
	public String rc12;
	@Transient
	public String rc13;
	@Transient
	public String rc14;
	@Transient
	public String rc15;
	@Transient
	public String rc16;
	@Transient
	public String rc17;
	@Transient
	public String rc18;
	@Transient
	public String rc19;
	@Transient
	public String rc20;

}
