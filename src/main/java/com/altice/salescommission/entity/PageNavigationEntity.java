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
@Table(name = "c_page_navigation_mgmt")
public class PageNavigationEntity extends AbstractBaseEntity{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "page_id", nullable = false, updatable = false)
	private Long id;
	private String nav_page_name;
	private String nav_page_desc;
	private int parent_page_id;
	private int page_order;
	private String nav_page_route;
	
	@Transient
	private String view_acc;
	@Transient
	private String edit_acc;
}
