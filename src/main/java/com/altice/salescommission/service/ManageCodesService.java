package com.altice.salescommission.service;

import java.util.Date;
import java.util.List;

import com.altice.salescommission.entity.EmployeeMasterEntity;
import com.altice.salescommission.model.ManageCodesModel;

public interface ManageCodesService {

	List<ManageCodesModel> getProducts();

	List<ManageCodesModel> getCorps();
	
	List<ManageCodesModel> getAssignedCorps(String id,String edate);
	
	List<EmployeeMasterEntity> getSupervisors();
	
	List<EmployeeMasterEntity> getEmpSupervisors(String val);

	List<ManageCodesModel> getProductsBySalesChannel(String saleschannel);

	List<ManageCodesModel> getRateCodes(Long productid, Long corp);

	ManageCodesModel updateRateCodes(ManageCodesModel manageCodesModel);

	ManageCodesModel addRateCode(ManageCodesModel manageCodesModel);

	List<String> getCommPlans(Long productid);

}
