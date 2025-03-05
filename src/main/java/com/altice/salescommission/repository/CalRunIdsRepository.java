package com.altice.salescommission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.CalRunIdsEntity;

public interface CalRunIdsRepository extends AbstractBaseRepository<CalRunIdsEntity, Long> {
	@Query(value = "select valid_from_dt ,unlock  from c_comm_calendar ccc where cal_run_id =?1", nativeQuery = true)
	List<String> getCalendarData(int calRunId);
	
	CalRunIdsEntity findByCalRunId(int calRunId);
}
