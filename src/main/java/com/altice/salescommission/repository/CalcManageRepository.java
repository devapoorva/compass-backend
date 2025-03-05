package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.CalcManageEntity;

public interface CalcManageRepository extends AbstractBaseRepository<CalcManageEntity, Long> {
	@Query(value="select count(p) from c_calc_manage p where p.cal_run_id =?1 and p.comm_plan_id =?2", nativeQuery = true)
	int getCount(int cal_run_id,int comm_plan_id);
	
}
