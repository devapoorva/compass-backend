package com.altice.salescommission.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.CommissionPlanEntity;


public interface CommissionPlanRepository extends JpaRepository<CommissionPlanEntity, Long> {
	//CommissionPlanModel findSalesChannelByCommplanid(Long id);
	
	@Query(value="select count(*) cnt from c_comm_plan_master ccpm where comm_plan =?1", nativeQuery = true)
	int getComPlanCount(String comPlan);
	
	@Query(value="SELECT coalesce(max(ccpm.comm_plan_id), 0) FROM c_comm_plan_master ccpm", nativeQuery = true)
	int getMaxId();
	
	@Query(value="select p.* from c_comm_plan_master p where p.row_id = ?1", nativeQuery = true)
    CommissionPlanEntity findAllByCommPlanId(long id);
	
	@Query(value="select max(effective_date) edate from c_comm_plan_master ccpm where comm_plan_id = ?1", nativeQuery = true)
    Date getMaxEffectiveDate(int comm_plan_id);
}
