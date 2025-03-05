package com.altice.salescommission.repository;


import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.TranslateMasterEntity;

public interface TranslateTableRepository extends AbstractBaseRepository<TranslateMasterEntity, Long> {
	
	@Query(value = "update c_translate_master set field_name=?1 where field_name=?2", nativeQuery = true)
	TranslateMasterEntity updateFieldName(String field_name,String fname);
}
