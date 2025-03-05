package com.altice.salescommission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.CommissionDetailValuesEntity;

public interface CommissionDetailValuesRepository extends JpaRepository<CommissionDetailValuesEntity, Long> {
	public List<CommissionDetailValuesEntity> findByCommPlanDtlIdOrderByRangeLow(int commPlanDtlId);

	int deleteByCommPlanDtlId(int comPlanDetailId);

	int countByCommPlanDtlId(int comPlanDetailId);

	@Query(value = "select ccdv.cdtr_id,ccdv.comm_plan_dtl_id,ccdv.range_val_type,"
			+ "ccdv.range_low,ccdv.range_high,ccdv.tier,ccdv.modifier_percent,ccdv.commission_val,ccdv.target,ccdv.comm_plan_id,"
			+ "ccdv.created_dt,ccdv.created_by,ccdv.update_dt,ccdv.updated_by,ccdv.kpi_id "
			+ "from c_comm_plan_detail ccpd ,c_comm_detail_values ccdv where ccpd.comm_plan_dtl_id =ccdv.comm_plan_dtl_id and ccpd.comm_plan_id = ?1", nativeQuery = true)
	List<CommissionDetailValuesEntity> getCommPlanIdValues(int comm_plan_id);
}
