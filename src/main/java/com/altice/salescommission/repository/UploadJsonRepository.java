package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.UploadJsonEntity;

public interface UploadJsonRepository extends AbstractBaseRepository<UploadJsonEntity, Long> {

	@Query(value = "select p.sql from c_json_sql_mapping p where p.page_name =?1 and p.grid_name =?2", nativeQuery = true)
	String getSql(String pagename, String gridname);

	@Query(value = "select p.sql from c_json_sql_mapping p where p.page_name =?1 and p.grid_name =?2 and p.saleschannel !='Web Assist'", nativeQuery = true)
	String getSql1(String pagename, String gridname);

	@Query(value = "select p.sql from c_json_sql_mapping p where p.page_name =?1 and p.grid_name =?2 and p.saleschannel ='Web Assist' and p.user_type='rep'", nativeQuery = true)
	String getSql2(String pagename, String gridname);

	@Query(value = "select p.sql from c_json_sql_mapping p where p.page_name =?1 and p.grid_name =?2 and p.saleschannel ='Web Assist' and p.user_type='sup'", nativeQuery = true)
	String getSql3(String pagename, String gridname);
	
	@Query(value = "select p.sql from c_json_sql_mapping p where p.page_name =?1 and p.grid_name=?2 and report_type='Reports'", nativeQuery = true)
	String getReportSql(String reportType1,String reportType2);

}