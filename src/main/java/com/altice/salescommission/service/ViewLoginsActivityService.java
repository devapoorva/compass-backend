package com.altice.salescommission.service;

import java.util.List;

import com.altice.salescommission.entity.EmployeeMasterEntity;


public interface ViewLoginsActivityService{
	List<EmployeeMasterEntity> getLoginsActivityList(String first_name);

}
