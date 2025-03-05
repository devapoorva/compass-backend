package com.altice.salescommission.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "c_rate_code_master")
public class ActiveRateCodeEntity extends AbstractBaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rate_code_id", nullable = false, updatable = false)
	private Long id;
	private String rcode;
	private String error_flag;
	private int pcorp;
	private int corp;
	private String ractv;
	private String rtype;
	private String rcatg;
	private String rname;
	private String rdesc;
	private double ramt;
	private String rcombos;
	private double prioamt;
	private Date effdate;
	private String format;
	private int digital;
	private int f_level;
	private Date feed_dt;
	private Date valid_from_dt;
	private Date valid_to_dt;
	@Transient
	private Long productId;
	@Transient
	private String productName;
	@Transient
	private String salesChannelDesc;
	@Transient
	private String salesChannelFieldValue;
	@Transient
	private List<Map<String, String>> fieldValues;

	

}
