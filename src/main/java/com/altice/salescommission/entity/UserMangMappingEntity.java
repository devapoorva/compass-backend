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
@Table(name = "c_user_role_page_nav_map_mgmt")
public class UserMangMappingEntity extends AbstractBaseEntity{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "usrlpgnvid", nullable = false, updatable = false)
	private Long id;
	private int userroleid;
	private int page_id;
	private int parent_page_id;
	private String view_acc;
	private String edit_acc;
	private String del_acc;
	private String status;
	@Transient
	private String sub_page_id;
}
