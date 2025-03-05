package com.altice.salescommission.service;

import java.io.IOException;
import java.util.List;

import com.altice.salescommission.entity.UserMangRoleEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.model.UserManagementModel;

public interface UserMangRoleService extends AbstractBaseService<UserMangRoleEntity, Long>{
	UserMangRoleEntity addRole(String role_name,String role_desc,String comment,String email,String currentUser) throws IOException, DuplicateRecordException;
	UserMangRoleEntity updateRole(long id,String role_name,String role_desc,String email,String comment,String currentUser) throws IOException;
	void deleteRole(Long id) throws IOException;
	UserMangRoleEntity findRoleById(Long id);
	List<UserMangRoleEntity> getRoles();
	List<UserManagementModel> getAllData();
}
