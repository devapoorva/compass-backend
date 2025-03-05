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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Table(name = "c_translate_master")
public class TranslateMasterEntity extends AbstractBaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "translate_id", nullable = false, updatable = false)
	private Long id;
	private String field_name;
	private String field_value;
	private String description;
	private String field_short_name;
	private Date effective_date;
	private String effective_status;
	@Transient
	private List<Map<String, String>> fieldValues;
	
	@Transient
	private String edate;

	public TranslateMasterEntity(String field_name) {
		this.field_name = field_name;
	}
}
