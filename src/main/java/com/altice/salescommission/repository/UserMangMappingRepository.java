package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.UserMangMappingEntity;

public interface UserMangMappingRepository extends AbstractBaseRepository<UserMangMappingEntity, Long> {
	@Query(value = "select count(userroleid) cnt from c_user_role_page_nav_map_mgmt where userroleid =?1 and page_id =?2", nativeQuery = true)
	int getCountOfMappedRecord(int roleid, int pageid);
	
	@Query(value = "select count(*) cnt from c_user_role_page_nav_map_mgmt where userroleid =?1 and parent_page_id =?2 and page_id !=?3 and (view_acc ='y' or edit_acc ='y')", nativeQuery = true)
	int getCountOfViewEditAccess(int roleid, int parentpageid, int pageid);
	
	@Query(value = "select usrlpgnvid from c_user_role_page_nav_map_mgmt where userroleid =?1 and page_id =?2", nativeQuery = true)
	int getId(int roleid, int pageid);
	
	@Query(value = "select usrlpgnvid from c_user_role_page_nav_map_mgmt where userroleid =?1 and parent_page_id =?2 and page_id =?3", nativeQuery = true)
	int getParentId(int roleid,int parentpageid, int pageid);
	
	@Query(value = "select coalesce(max(usrlpgnvid),0) maxid from c_user_role_page_nav_map_mgmt", nativeQuery = true)
	int getMaxId();
	
	
}
