package com.altice.salescommission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.DependentKPIEntity;

public interface DepndntKpiDtlsRepository extends JpaRepository<DependentKPIEntity, Long> {
	@Query(value = "select count(p.row_id) from c_depndnt_kpi_details p where p.row_id =?1", nativeQuery = true)
	int getCount(Long id);

	@Query(value = "select p.* from c_depndnt_kpi_details p where p.comp_plan_id = ?1", nativeQuery = true)
	List<DependentKPIEntity> findAllByCommPlanDependKpiId(int comm_plan_id);
}
