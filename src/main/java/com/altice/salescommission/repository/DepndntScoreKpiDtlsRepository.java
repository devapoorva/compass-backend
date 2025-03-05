package com.altice.salescommission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.DependentScoreKPIEntity;

public interface DepndntScoreKpiDtlsRepository extends JpaRepository<DependentScoreKPIEntity, Long> {
	@Query(value = "select count(p.row_id) from c_depndnt_kpi_score_weight p where p.complex_kpi_id =?1 and p.kpi_id=?2 and p.comp_plan_id = ?3", nativeQuery = true)
	int getCount(int complexkpiid, int kpiid,int comm_plan_id);

	@Query(value = "select p.row_id from c_depndnt_kpi_score_weight p where p.complex_kpi_id =?1 and p.kpi_id=?2 and p.comp_plan_id = ?3", nativeQuery = true)
	Long getId(int complexkpiid, int kpiid,int comm_plan_id);

	@Query(value = "select count(p.row_id) from c_depndnt_kpi_score_weight p where p.row_id=?1", nativeQuery = true)
	int getScoreCount(Long id);

	@Query(value = "select count(p.row_id) from c_depndnt_kpi_score_weight p where p.row_id =?1", nativeQuery = true)
	int getUpdateCount(Long id);

	@Query(value = "select p.* from c_depndnt_kpi_score_weight p where p.comp_plan_id = ?1", nativeQuery = true)
	List<DependentScoreKPIEntity> findAllByCommPlanDependKpiScoreId(int comm_plan_id);
}
