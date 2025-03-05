package com.altice.salescommission.entity;

import java.util.Date;

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
@Table(name = "c_commission_summary_sqls")
public class CommissionSummaryEntity extends AbstractBaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "sql_query")
	private String sqlQuery;

	@Transient
	private int commPlanId;

	@Transient
	private String salesChannel;

	@Transient
	private String commPlan;

	@Transient
	private String calendarType;
	
	@Transient
	private Date validToDt;
	
	@Transient
	private Date validFromDt;
	
	@Transient
	private String calRunId;
	
	@Transient
	private String field_value;
	
	@Transient
	private Date effective_date;
	
	@Transient
	private String issalesrepaccess;
	
	@Transient
	private String display_flag;

}
