package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.UploadsTemplateEntity;

public interface UploadsTemplateRepository extends JpaRepository<UploadsTemplateEntity, Long> {
	@Query(value = "select coalesce(max(templateid),0) maxid from c_upload_template", nativeQuery = true)
	int getMaxId();
	
	@Query(value = "select count(rowid) p from c_upload_template p where recordname=?1", nativeQuery = true)
	int getCount(String name);
}
