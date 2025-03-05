package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.PageNavigationEntity;
import com.altice.salescommission.entity.UserMangMappingEntity;
import com.altice.salescommission.entity.UserMangRoleEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.model.UserManagementModel;
import com.altice.salescommission.repository.UserMangMappingRepository;
import com.altice.salescommission.repository.UserMangRoleRepository;
import com.altice.salescommission.service.UserMangRoleService;

@Service
@Transactional
public class UserMangRoleServiceImpl extends AbstractBaseRepositoryImpl<UserMangRoleEntity, Long>
		implements UserMangRoleService {

	private static final Logger logger = LoggerFactory.getLogger(UserMangRoleServiceImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserMangRoleRepository userMangRoleRepository;

	@Autowired
	private UserMangMappingRepository userMangMappingRepository;

	public UserMangRoleServiceImpl(UserMangRoleRepository userMangRoleRepository) {
		super(userMangRoleRepository);
	}

	/* This method is used to create new role */
	@Override
	public UserMangRoleEntity addRole(String role_name, String role_desc, String comment, String email,
			String currentUser) throws IOException, DuplicateRecordException {
		UserMangRoleEntity role = new UserMangRoleEntity();
		System.out.println("currentUser = " + currentUser);
		int rowcount = userMangRoleRepository.getRoleNameCount(role_name);

		if (rowcount == 0) {
			role.setRole_name(role_name);
			role.setRole_desc(role_desc);
			role.setEmail(email);
			role.setComment(comment);
			role.setCreated_by(currentUser);
			role.setCreated_dt(new Date());
			role.setRole_status("ACTIVE");
			userMangRoleRepository.save(role);

			long generatedId = role.getId();

			List<PageNavigationEntity> UserMangPageNavigationList = jdbcTemplate.query(
					"select page_id from c_page_navigation_mgmt cpnm where parent_page_id = 0",
					new RowMapper<PageNavigationEntity>() {

						@Override
						public PageNavigationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
							PageNavigationEntity userMangPageNavigationModelObj = new PageNavigationEntity();
							userMangPageNavigationModelObj.setId(rs.getLong("page_id"));
							return userMangPageNavigationModelObj;
						}
					});

			Long maxid = Long.valueOf(userMangMappingRepository.getMaxId());
			Long i = 0L;
			for (PageNavigationEntity userObj : UserMangPageNavigationList) {
				i++;
				UserMangMappingEntity mappingObj = new UserMangMappingEntity();
				//mappingObj.setId(maxid + i);
				mappingObj.setUserroleid((int) generatedId);
				mappingObj.setPage_id(userObj.getId().intValue());
				mappingObj.setParent_page_id(userObj.getId().intValue());
				mappingObj.setView_acc("y");
				mappingObj.setEdit_acc("y");
				mappingObj.setCreated_by(currentUser);
				mappingObj.setCreated_dt(new Date());
				mappingObj.setStatus("ACTIVE");
				userMangMappingRepository.save(mappingObj);
			}

		} else {
			throw new DuplicateRecordException(role_name + DUPLICATE_RECORD);
		}

		return role;
	}

	/* This method is used to update a role */
	@Override
	public UserMangRoleEntity updateRole(long id, String role_name, String role_desc, String email, String comment,
			String currentUser) throws IOException {
		UserMangRoleEntity role = userMangRoleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Role id not found" + id));

		role.setRole_name(role_name);
		role.setRole_desc(role_desc);
		role.setEmail(email);
		role.setComment(comment);
		role.setUpdated_by(currentUser);
		role.setUpdated_dt(new Date());
		userMangRoleRepository.save(role);
		return role;
	}

	/* This method is used to delete a role */
	@Override
	public void deleteRole(Long id) throws IOException {
		userMangRoleRepository.deleteById(id);
	}

	/* This method is used to get roles list */
	@Override
	public List<UserMangRoleEntity> getRoles() {
		return userMangRoleRepository.findAll();
	}

	/* This method is used to get role by id */
	@Override
	public UserMangRoleEntity findRoleById(Long id) {
		return userMangRoleRepository.findRoleById(id);
	}

	/* This method is used to get all the rolea and pages data */
	@Override
	public List<UserManagementModel> getAllData() {
		List<UserManagementModel> rolesList = jdbcTemplate
				.query("select curm.role_name ,curm.role_desc ,curm.email ,curm.comment ,cpnm.nav_page_name ,"
						+ "cpnm.nav_page_desc ,cpnm.page_order ,"
						+ "cpnm.created_dt, curpnmm.view_acc ,curpnmm.edit_acc,cpnm.parent_page_id "
						+ "from c_page_navigation_mgmt cpnm ,c_user_role_page_nav_map_mgmt curpnmm, c_user_role_mgmt curm  "
						+ "where cpnm.page_id =curpnmm.page_id  and curpnmm.userroleid =curm.userrole_id and cpnm.parent_page_id !=0 "
						+ "order by curm.role_name,cpnm.nav_page_name", new RowMapper<UserManagementModel>() {

							@Override
							public UserManagementModel mapRow(ResultSet rs, int rowNum) throws SQLException {
								UserManagementModel roleModel = new UserManagementModel();
								roleModel.setRole_name(rs.getString("role_name"));
								roleModel.setRole_desc(rs.getString("role_desc"));
								roleModel.setEmail(rs.getString("email"));
								roleModel.setComment(rs.getString("comment"));
								roleModel.setNav_page_name(rs.getString("nav_page_name"));
								roleModel.setNav_page_desc(rs.getString("nav_page_desc"));
								roleModel.setPage_order(rs.getInt("page_order"));
								roleModel.setCreated_dt(rs.getDate("created_dt"));
								roleModel.setView_acc(rs.getBoolean("view_acc"));
								roleModel.setEdit_acc(rs.getBoolean("edit_acc"));
								roleModel.setParent_page_id(rs.getInt("parent_page_id"));
								return roleModel;
							}
						});
		return rolesList;
	}
}
