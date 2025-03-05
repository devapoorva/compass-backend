package com.altice.salescommission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.MyDocsEntity;
import com.altice.salescommission.entity.UploadConfigEntity;

public interface UploadConfigRepository extends JpaRepository<UploadConfigEntity, Long> {
	
	@Query(value = "select coalesce(max(templateid),0) maxid from c_upload_config", nativeQuery = true)
	int getMaxId();
	
	@Query(value = "select sqlquery from c_upload_config where recordtemplate=$1 limit 1", nativeQuery = true)
	String getSQLQuery(String uploadtemplatename);
	
	@Query(value = "select columntype from c_upload_template where status='Y' and recordtemplate=$1 order by columnorder", nativeQuery = true)
	List<String> getDtype(String uploadtemplatename);
	
	@Query(value = "select count(rowid) p from c_upload_config p where uploadtemplatename=?1", nativeQuery = true)
	int getCount(String name);

}
