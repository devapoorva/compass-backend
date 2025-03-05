package com.altice.salescommission.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.KpiMasterEntity;

public interface KpiMasterRepository extends JpaRepository<KpiMasterEntity, Long> {

	KpiMasterEntity findByNameIgnoreCase(String kpi_name);

	@Query(value = "select count(p.kpi_name) from c_kpi_master p where UPPER(p.kpi_name)=UPPER(?1)", nativeQuery = true)
	int getKpiNameCount(String kpi_name);

	@Query(value = "select p.product from c_product_master p where p.product_id =?1", nativeQuery = true)
	String getProductName(int productId);

	@Query(value = "select p.product_id from c_product_master p where p.product =?1 and p.product_category =?2 and p.product_type =?3", nativeQuery = true)
	int getProductId(String productName, String productCat, String productType);

	@Query(value = "select count(p.product_id) cnt from c_product_master p where p.product =?1 and p.product_category =?2 and p.product_type =?3", nativeQuery = true)
	int getProductIdCnt(String productName, String productCat, String productType);

	@Query(value = "select p.kpi_type from c_kpi_master p where p.kpi_id =?1 and p.effective_date=?2 group by p.kpi_type", nativeQuery = true)
	String getKpiTypeOne(long kpiId, Date effective_date);

	@Query(value = "select coalesce(p.product_type,'NA') product_type from c_kpi_detail p where p.kpi_id =?1 and p.effective_date=?2", nativeQuery = true)
	List<String> getProductType(long kpiId, Date effective_date);

	@Query(value = "select coalesce(p.product_category,'NA') product_category from c_kpi_detail p where p.kpi_id =?1 and p.product_type=?2 and p.effective_date=?3", nativeQuery = true)
	List<String> getCategories(long kpiId, String ptype, Date effective_date);

	@Query(value = "select coalesce(p.product,'NA') product from c_kpi_detail p where p.kpi_id =?1 and p.product_type=?2 and p.product_category=?3 and p.effective_date=?4", nativeQuery = true)
	List<String> getProducts(long kpiId, String ptype, String cat, Date effective_date);

	@Query(value = "select coalesce(max(kpi_id),0) maxid from c_kpi_master", nativeQuery = true)
	int getMaxId();
	
	@Query(value = "select p.effective_date from c_kpi_master p where p.kpi_id =?1 and p.effective_date=?2", nativeQuery = true)
	Date getEffectiveDate(long kpiId, Date effective_date);
	
	@Query(value = "select count(p.effective_date) effective_date from c_kpi_master p where p.kpi_id =?1 and p.effective_date=?2", nativeQuery = true)
	int getEffectiveDateCount(long kpiId, Date effective_date);

}
