package com.altice.salescommission.entity;

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
@Table(name = "c_json_sql_mapping")
public class UploadJsonEntity extends AbstractBaseEntity {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "saleschannel")
	private String salesChannel;
	@Transient
	private String commPlan;
	@Column(name = "page_name")
	private String pageName;
	private String description;
	@Column(name = "grid_name")
	private String gridName;
	@Column(name = "order_by")
	private int colSort;
	@Transient
	private String kpiName;
	private String sql;
	private String report_type;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;
	
	private String status;
	
	@Column(name = "commplanid")
	private int commPlanId;
	
	@Column(name = "kpi_id")
	private int kpiId;
	
	private String user_type;
	
	

}
