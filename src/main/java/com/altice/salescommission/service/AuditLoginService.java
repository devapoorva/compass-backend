package com.altice.salescommission.service;

import java.util.List;

import com.altice.salescommission.entity.AuditLoginEntity;

public interface AuditLoginService extends AbstractBaseService<AuditLoginEntity, Long>{
	AuditLoginEntity addLogins(String network_id, String email,String employee_id,String created_by);
	List<AuditLoginEntity> getLogins(String employeeid);
}
