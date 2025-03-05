package com.altice.salescommission.model;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.altice.salescommission.entity.AbstractBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "c_corp_master")
public class CorpEntity extends AbstractBaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "major_area")
	private String majorarea;
	private String region;
	private String area;
	
	@Id
	@Column(name = "corp_id", nullable = false, updatable = false)
	private Long id;
	private String corp;
	private String status;
	
	
	@Transient
	private List<Map<String, String>> regionsList;
	@Transient
	private List<Map<String, String>> areasList;
	@Transient
	private List<Map<String, String>> corpsList;
	
	public CorpEntity(String majorarea) {
		this.majorarea=majorarea;
	}
	

	
}
