package com.altice.salescommission.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.altice.salescommission.entity.CommissionPlanDetailEntity;

@Repository("CommissionPlanDetailsRepository")
public interface CommissionPlanDetailsRepository extends JpaRepository<CommissionPlanDetailEntity, Long> {

	public List<CommissionPlanDetailEntity> findByCommPlanIdOrderByViewOrder(int commPlanId);

	@Query(value="select p.* from c_comm_plan_detail p where p.commplan_rowid = ?1", nativeQuery = true)
	List<CommissionPlanDetailEntity> findAllByCommPlanId(int commplan_rowid);
	
	@Query(value="select count(*) from c_comm_plan_detail ccpd where comm_plan_id =?1 and kpi_id =?2 and effective_date =?3", nativeQuery = true)
	int getKPICount(int commplanid,int kpiid, Date edate);
	
	@Query(value="select count(p.row_id) from c_comm_plan_styles p where p.comm_plan_dtl_id =?1", nativeQuery = true)
	int getColorCodeCount(Long commplandetailid);
}