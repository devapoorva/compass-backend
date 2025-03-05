package com.altice.salescommission.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.altice.salescommission.entity.TranslateMasterEntity;

public interface TranslateTableService extends AbstractBaseService<TranslateMasterEntity, Long> {
	List<TranslateMasterEntity> getFieldNames();
	
	List<TranslateMasterEntity> getTranslateTablelist();
	
	List<TranslateMasterEntity> getExcelTranslateTablelist();
	
	List<TranslateMasterEntity> getFieldValuesList(String field_name);

	TranslateMasterEntity addTrans(String field_name, String field_value, String description, String field_short_name,
			Date effective_date,String currentUser);

	int updateFieldName(String field_name, String fname) throws IOException;

	int updateFieldValue(long id, String field_value, String description, String field_short_name, Date effective_date,
			String effective_status,String currentUser) throws IOException;
}
