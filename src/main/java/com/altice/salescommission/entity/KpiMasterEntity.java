package com.altice.salescommission.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "c_kpi_master")
public class KpiMasterEntity {

	@Id
	@Column(name = "kpi_id", nullable = false, updatable = false)
	private Long id;
	@Column(name = "kpi_name")
	private String name;
	private String kpi_type;
	private String feed_type;
	private Date created_dt;
	private String created_by;
	private Date update_dt;
	private String updated_by;
	private String kpi_status;
	private Date effective_date;
	private String wstat_string;
	private String kpi_calc_type;
	private String custom_routine;
	private int kpi_priority;
	

}
