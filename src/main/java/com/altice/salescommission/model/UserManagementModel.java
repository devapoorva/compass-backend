package com.altice.salescommission.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.altice.salescommission.entity.AbstractBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserManagementModel extends AbstractBaseEntity{

	private static final long serialVersionUID = 1L;
	private Long role_id;
	private String role_name;
	private String role_desc;
	private String email;
	private String nav_page_name;
	private String nav_page_desc;
	private int page_order;
	private String sub_nav_page_name;
	private String sub_nav_page_desc;
	private boolean view_acc;
	private boolean edit_acc;
	
	private int page_nav_id;
	
	private int parent_page_id;
	
	private List<Map<String, String>> submenus;
	
	private int mapping_id;
	private int userroleid;
	private int page_id;
	private int access_level;
	private String status;
	
	
	
	private int sub_page_id;
	
	private Date sub_created_dt;
	
	private String role_status;
}
