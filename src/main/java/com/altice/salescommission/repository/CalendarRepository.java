package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.CalendarEntity;

public interface CalendarRepository extends AbstractBaseRepository<CalendarEntity, Long> {
	@Query(value = "select count(p.cal_run_id) from c_comm_calendar p where p.cal_run_id =?1", nativeQuery = true)
	int getCalcRunIDCount(int cal_run_id);
}
