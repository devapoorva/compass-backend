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
@Table(name = "c_kpi_detail")
public class KpiDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ckpd_id", nullable = false, updatable = false)
	private Long id;
	private long kpi_id;
	private int product_id;
	private int product_detail_id;
	private Date created_dt;
	private String created_by;
	private Date update_dt;
	private String updated_by;
	@Column(name = "dpdnt_kpi")
	private int dependentKpi;
	private String product_type;
	private String product_category;
	private String product;
	private String combo_string;
	private Date effective_date;
	private String combo_position;
	private String combo_values;
	private String include_count;
	private String include_revenue;

}
