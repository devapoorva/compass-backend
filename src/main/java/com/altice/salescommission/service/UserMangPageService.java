package com.altice.salescommission.service;

import java.io.IOException;
import java.util.List;

import com.altice.salescommission.entity.PageNavigationEntity;
import com.altice.salescommission.model.UserManagementModel;

public interface UserMangPageService extends AbstractBaseService<PageNavigationEntity, Long> {
	PageNavigationEntity addPage(String nav_page_name, String nav_page_desc, String comment, int page_order,
			int parent_page_id) throws IOException;


	PageNavigationEntity updatePage(long id, String nav_page_name, String nav_page_desc, int page_order)
			throws IOException;

	List<UserManagementModel> getMenus();

	List<UserManagementModel> getPages();

	List<UserManagementModel> getSubMenus(int roleid, int menuid);

	List<PageNavigationEntity> getMenuListByRole(String networkId);

	List<PageNavigationEntity> getFavMenuList(String empId);

}
