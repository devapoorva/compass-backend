package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.UserMangRoleEntity;

public interface UserMangRoleRepository extends AbstractBaseRepository<UserMangRoleEntity, Long>{
	UserMangRoleEntity findRoleById(Long id);
	
	@Query(value="select count(p) from c_user_role_mgmt p where lower(p.role_name) = lower(?1)", nativeQuery = true)
	int getRoleNameCount(String kpi_name);
}
