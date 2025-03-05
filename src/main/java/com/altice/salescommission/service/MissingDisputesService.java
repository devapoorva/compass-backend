package com.altice.salescommission.service;

import java.util.List;

import com.altice.salescommission.entity.MissingDisputesEntity;
import com.altice.salescommission.exception.DuplicateRecordException;

public interface MissingDisputesService {
	
	List<MissingDisputesEntity> getMissingDisputesList();
	
	List<MissingDisputesEntity> getKpiList(int commplanid);

	int insertMissingDisputes(String kpi_id, String comm_plan_id, String cust_id, String corp, String currentUser,
			String house, String revenue, String wordate, String wfindate, String message,  String sales_rep_id)
			throws DuplicateRecordException;
	
	int insertMissingDisputesStatic(String kpi_id,  String currentUser, String message)
			throws DuplicateRecordException;
}
