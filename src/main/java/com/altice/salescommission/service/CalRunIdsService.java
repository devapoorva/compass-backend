package com.altice.salescommission.service;

import java.util.List;

import com.altice.salescommission.entity.CalRunIdsEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;

public interface CalRunIdsService extends AbstractBaseService<CalRunIdsEntity, Long> {
	CalRunIdsEntity addCalRunId(int cal_run_id, String description, String currentUser) throws DuplicateRecordException;

	CalRunIdsEntity updateCalRunId(long id, String description, String currentUser) throws IdNotFoundException;

	List<CalRunIdsEntity> getCalRunIdList();

	List<CalRunIdsEntity> getProcess(String userId, int runCtrlId, String startTime, String endTime);

	List<CalRunIdsEntity> getUserIdsDropDown();

	List<CalRunIdsEntity> getRunCtrlsDropDown();
}
