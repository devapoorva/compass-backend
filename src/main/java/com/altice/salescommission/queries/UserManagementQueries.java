package com.altice.salescommission.queries;

public interface UserManagementQueries {
	public final String GET_SUBMENUS_LIST = "select cpnm.page_id,cpnm.nav_page_name,cpnm.nav_page_desc ,"
			+ "cpnm.created_dt,coalesce(curpnmm.view_acc,'n') view_acc,coalesce(curpnmm.edit_acc,'n') edit_acc,cpnm.parent_page_id "
			+ "from c_page_navigation_mgmt cpnm " + "left join c_user_role_page_nav_map_mgmt curpnmm "
			+ "on (cpnm.page_id=curpnmm.page_id and curpnmm.userroleid=?) where cpnm.parent_page_id = ? order by cpnm.nav_page_name";

	public final String GET_MENUS_LIST = "select page_id,nav_page_name,nav_page_desc,page_order ,created_dt from c_page_navigation_mgmt "
			+ "where parent_page_id =0 order by nav_page_name";

	public final String GET_PAGES_LIST = "select DISTINCT ON(cpnm.page_id) cpnm.page_id,cpnm.nav_page_name,cpnm.nav_page_desc ,cpnm.created_dt,curpnmm.view_acc , "
			+ "curpnmm.edit_acc from c_page_navigation_mgmt cpnm, c_user_role_page_nav_map_mgmt curpnmm  "
			+ "where cpnm.page_id = curpnmm.page_id  and cpnm.parent_page_id = ? ";

	public final String GET_NAV_PAGE_LIST = "select a.nav_page_name menu, b.nav_page_name submenu,a.nav_page_desc,"
			+ "a.created_dt from c_page_navigation_mgmt a "
			+ "left outer join c_page_navigation_mgmt b on a.page_id = b.parent_page_id order by b.page_order";

	public final String GET_SUBMENUS_LIST_DUMMY = "select cpnm.page_id,cpnm.nav_page_name,cpnm.nav_page_desc ,cpnm.created_dt,curpnmm.view_acc , "
			+ "curpnmm.edit_acc from c_page_navigation_mgmt cpnm, c_user_role_page_nav_map_mgmt curpnmm, c_user_role_mgmt curm  "
			+ "where cpnm.page_id = curpnmm.page_id and curm.userrole_id=curpnmm.userroleid and cpnm.parent_page_id = ? and curm.userrole_id=?";



	public final String GET_MENU_LIST_BY_USER = "select cpnm.page_id ,cpnm.nav_page_name,cpnm.nav_page_desc,cpnm.parent_page_id,"
			+ "cpnm.page_order,cpnm.nav_page_route,curpnmm.view_acc,curpnmm.edit_acc "
			+ "from c_user_role_mgmt curm ,c_user_role_page_nav_map_mgmt curpnmm ,c_page_navigation_mgmt cpnm "
			+ "where curm.userrole_id = curpnmm.userroleid "
			+ "and curpnmm.page_id = cpnm.page_id and curm.role_name = ? and (curpnmm.view_acc = 'y' or curpnmm.edit_acc ='y') "
			+ "order by cpnm.page_order asc";

	public final String GET_FEV_MENU_LIST_BY_USER = "select cufp.page_id ,cpnm.nav_page_name,cpnm.nav_page_desc,cpnm.parent_page_id,cpnm.page_order,cpnm.nav_page_route "
			+ "from c_user_fav_pages cufp,c_page_navigation_mgmt cpnm where cufp.page_id =cpnm.page_id and cufp.employee_id =? ";

}
