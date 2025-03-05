package com.altice.salescommission.serviceImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.PageNavigationEntity;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.model.UserManagementModel;
import com.altice.salescommission.queries.UserManagementQueries;
import com.altice.salescommission.repository.UserMangPageNavigationRepository;
import com.altice.salescommission.service.UserMangPageService;

@Service
@Transactional
public class UserMangPageServiceImpl extends AbstractBaseRepositoryImpl<PageNavigationEntity, Long>
		implements UserMangPageService, UserManagementQueries {
	
	private static final Logger logger = LoggerFactory.getLogger(UserMangPageServiceImpl.class.getName());

	@Autowired
	private UserMangPageNavigationRepository userMangPageNavigationRepository;

	public UserMangPageServiceImpl(UserMangPageNavigationRepository userMangPageNavigationRepository) {
		super(userMangPageNavigationRepository);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/* This method is used to create a new page */
	@Override
	public PageNavigationEntity addPage(String nav_page_name, String nav_page_desc, String comment,
			int page_order, int parent_page_id) throws IOException {
		PageNavigationEntity page = new PageNavigationEntity();
		page.setNav_page_name(nav_page_name);
		page.setNav_page_desc(nav_page_desc);
		page.setComment(comment);
		page.setPage_order(page_order);
		page.setParent_page_id(parent_page_id);
		page.setCreated_by("NA");
		page.setCreated_dt(new Date());
		userMangPageNavigationRepository.save(page);
		return page;
	}

	/* This method is used to update a page */
	@Override
	public PageNavigationEntity updatePage(long id, String nav_page_name, String nav_page_desc, int page_order)
			throws IOException {
		PageNavigationEntity page = userMangPageNavigationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Page id not found" + id));
		//System.out.println("page=" + page);
		page.setNav_page_name(nav_page_name);
		page.setNav_page_desc(nav_page_desc);
		page.setPage_order(page_order);
		page.setUpdated_by("VENKAT SAMMETA");
		page.setUpdated_dt(new Date());
		userMangPageNavigationRepository.save(page);
		return page;
	}

	/* Getting all the main menu tabs data */
	@Override
	public List<UserManagementModel> getMenus() {

		List<UserManagementModel> kpiTypeOneList = jdbcTemplate.query(GET_MENUS_LIST,
				new RowMapper<UserManagementModel>() {
					@Override
					public UserManagementModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						UserManagementModel pageModel = new UserManagementModel();
						pageModel.setPage_nav_id(rs.getInt("page_id"));
						pageModel.setNav_page_name(rs.getString("nav_page_name"));
						return pageModel;
					}
				});
		return kpiTypeOneList;

	}

	/* Getting all the page navigations data */
	@Override
	public List<UserManagementModel> getPages() {

		List<UserManagementModel> pagesList = jdbcTemplate.query(GET_MENUS_LIST, new RowMapper<UserManagementModel>() {

			@Override
			public UserManagementModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserManagementModel pageNavModel = new UserManagementModel();
				pageNavModel.setPage_nav_id(rs.getInt("page_id"));
				pageNavModel.setNav_page_name(rs.getString("nav_page_name"));
				pageNavModel.setNav_page_desc(rs.getString("nav_page_desc"));
				pageNavModel.setPage_order(rs.getInt("page_order"));
				pageNavModel.setCreated_dt(rs.getDate("created_dt"));

				List<Map<String, String>> list = new ArrayList<>();

				jdbcTemplate.query(GET_PAGES_LIST, new RowMapper<UserManagementModel>() {

					@Override
					public UserManagementModel mapRow(ResultSet rs1, int rowNum) throws SQLException {

						Map<String, String> ma = new HashMap<>();

						ma.put("sub_page_id", rs1.getString("page_id"));
						ma.put("sub_nav_page_name", rs1.getString("nav_page_name"));
						ma.put("sub_nav_page_desc", rs1.getString("nav_page_desc"));
						ma.put("sub_created_dt", rs1.getString("created_dt"));
						ma.put("view_acc", rs1.getString("view_acc"));
						ma.put("edit_acc", rs1.getString("edit_acc"));

						list.add(ma);

						pageNavModel.setSubmenus(list);
						return pageNavModel;
					}
				}, new Object[] { rs.getLong("page_id") });

				return pageNavModel;
			}
		});
		return pagesList;

	}

	/* Getting all the page navigations data */
	@Override
	public List<UserManagementModel> getSubMenus(int roleid, int menuid) {
		
		//System.out.println("roleid = "+roleid);
		//System.out.println("menuid = "+menuid);

		List<UserManagementModel> pagesList = jdbcTemplate.query(GET_SUBMENUS_LIST,
				new RowMapper<UserManagementModel>() {
					@Override
					public UserManagementModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						UserManagementModel pageNavModel = new UserManagementModel();

						
						boolean view_acc = false;
						boolean edit_acc = false;

						if (rs.getString("view_acc").equals("y") || rs.getString("view_acc").equals("")) {
							view_acc = true;
						}
						

						if (rs.getString("edit_acc").equals("y") || rs.getString("edit_acc").equals("")) {
							edit_acc = true;
						}

						pageNavModel.setSub_page_id(rs.getInt("page_id"));
						pageNavModel.setParent_page_id(rs.getInt("parent_page_id"));
						pageNavModel.setSub_nav_page_name(rs.getString("nav_page_name"));
						pageNavModel.setSub_nav_page_desc(rs.getString("nav_page_desc"));
						pageNavModel.setSub_created_dt(rs.getDate("created_dt"));
						pageNavModel.setView_acc(view_acc);
						pageNavModel.setEdit_acc(edit_acc);
						return pageNavModel;
					}
				}, new Object[] { roleid, menuid });
		return pagesList;

	}

	@Override
	public List<PageNavigationEntity> getMenuListByRole(String networkId) {
		
		//logger.info("getMenuListByRole = "+networkId);

		List<PageNavigationEntity> userManagePages = jdbcTemplate.query(GET_MENU_LIST_BY_USER, new RowMapper<PageNavigationEntity>() {
			@Override
			public PageNavigationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				PageNavigationEntity userManagePage = new PageNavigationEntity();
				userManagePage.setId(rs.getLong("page_id"));
      			userManagePage.setNav_page_name(rs.getString("nav_page_name"));
      			userManagePage.setNav_page_route(rs.getString("nav_page_route"));
				userManagePage.setNav_page_desc(rs.getString("nav_page_desc"));
				userManagePage.setParent_page_id(rs.getInt("parent_page_id"));
				userManagePage.setPage_order(rs.getInt("page_order"));
				userManagePage.setView_acc(rs.getString("view_acc"));
				userManagePage.setEdit_acc(rs.getString("edit_acc"));
				return userManagePage;
			}
		}, new Object[] {networkId});
		
		logger.info("userManagePages = "+userManagePages);

		return userManagePages;
}
	
	@Override
	public List<PageNavigationEntity> getFavMenuList(String empId) {
		List<PageNavigationEntity> favMenuPages = jdbcTemplate.query(GET_FEV_MENU_LIST_BY_USER, new RowMapper<PageNavigationEntity>() {
			@Override
			public PageNavigationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				PageNavigationEntity favMenuPage = new PageNavigationEntity();
				favMenuPage.setId(rs.getLong("page_id"));
				favMenuPage.setNav_page_name(rs.getString("nav_page_name"));
				favMenuPage.setNav_page_route(rs.getString("nav_page_route"));
				favMenuPage.setNav_page_desc(rs.getString("nav_page_desc"));
				favMenuPage.setParent_page_id(rs.getInt("parent_page_id"));
				favMenuPage.setPage_order(rs.getInt("page_order"));
				return favMenuPage;
			}
		}, new Object[] {empId});
		return favMenuPages;
	}
}
