package com.altice.salescommission.service;

import com.altice.salescommission.entity.UserMangMappingEntity;

public interface UserMangMappingService extends AbstractBaseService<UserMangMappingEntity, Long>{
	UserMangMappingEntity assignPageToRoleViews(int id, int pageid, String status,String currentUser,int parentpageid);
	UserMangMappingEntity assignPageToRoleEdits(int id, int pageid, String status,String currentUser,int parentpageid);
}
