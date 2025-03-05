package com.altice.salescommission.reportframework;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ReportDistributionRepository extends JpaRepository<ReportDistributionModel, Long> {
	
	Optional<ReportDistributionModel> findByDistId(long distId);
	
	@Query(value="select count(p) from c_report_dist_list p where p.dist_name =?1 and p.dist_type =?2 and p.dist_value =?3", nativeQuery = true)
	int getValueCount(String distName,String distType,String distValue);
	
	@Query(value="select count(p) from c_report_dist_list p where lower(p.dist_name) =lower(?1)", nativeQuery = true)
	int getCountOfDistName(String distName);
	
	@Query(value="select count(p) from c_report_dist_list p where dist_id=?1", nativeQuery = true)
	int getDistIdCount(long distId);
	
	@Query(value="select distinct(dist_type) from c_report_dist_list p where dist_id=?1", nativeQuery = true)
	String getTypeName(long distId);
}
