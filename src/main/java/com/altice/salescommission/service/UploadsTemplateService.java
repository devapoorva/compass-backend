package com.altice.salescommission.service;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.altice.salescommission.entity.UploadsTemplateEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;

public interface UploadsTemplateService {
	List<UploadsTemplateEntity> getTemplatesList();

	List<UploadsTemplateEntity> getColumnsList(String recordName);

	List<UploadsTemplateEntity> getColumnsEditList(String recordName);

	LinkedHashSet<Map<String, String>> getRecordsList(String recordName);

	UploadsTemplateEntity insertTemplate(List<UploadsTemplateEntity> uploadsTemplateEntity) throws IdNotFoundException, DuplicateRecordException;

	UploadsTemplateEntity updateTemplate(List<UploadsTemplateEntity> uploadsTemplateEntity) throws IdNotFoundException;
	
	int deleteTemplate(int id,String recordname,String recordtemplate) throws IOException;
}
