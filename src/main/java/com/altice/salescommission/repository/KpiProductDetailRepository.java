package com.altice.salescommission.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.KpiDetailEntity;

public interface KpiProductDetailRepository extends JpaRepository<KpiDetailEntity, Long> {
	@Query(value = "select COUNT(p) from c_kpi_detail p where p.kpi_id=?1 and p.product_id=?2", nativeQuery = true)
	int countByKpiIdAndProductID(long kpiId, int productId);

	@Query(value = "select COUNT(p) from c_kpi_detail p where p.kpi_id = ?1 and p.product_type = ?2 and p.product_category = ?3 and p.product = ?4 and p.effective_date=?5", nativeQuery = true)
	int countByKpiIdAndProductTypeAndProCatAndProduct(long kpiId, String product_type, String product_category,
			String product,Date effective_date);
	
	@Query(value = "select COUNT(p) from c_kpi_detail p where p.kpi_id = ?1 and p.dpdnt_kpi = ?2 and p.effective_date=?3", nativeQuery = true)
	int getCountOfDependentKpi(long kpiId, int dependentkpi,Date effective_date);
	
	@Query(value = "select COUNT(p) from c_kpi_detail p where p.kpi_id = ?1 and p.combo_string = ?2 and p.product = ?3 and p.effective_date=?4", nativeQuery = true)
	int getCountOfComboString(long kpiId, String combo_string, String product,Date effective_date);
	
	@Query(value = "select kpi_id from c_kpi_master where kpi_name=?1", nativeQuery = true)
	int getKpiId(String kpi_name);
}
