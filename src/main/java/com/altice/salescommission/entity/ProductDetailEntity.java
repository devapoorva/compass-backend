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
@Table(name = "c_product_detail")
public class ProductDetailEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_detail_id", nullable = false, updatable = false)
	private Long  id;
	private Long product_id;
	private String ratecd;
	private String ratesign;
	@Column(name = "corp")
	private int corp_id;
	private Integer promoamt;
	private String active_flag;
	private Date effective_from;
	private Date effective_to;
	private Date created_dt;
	private String created_by;
	private Date update_dt;
	private String updated_by;
	private Date effective_date;
	
	
	
	
	
	
}
