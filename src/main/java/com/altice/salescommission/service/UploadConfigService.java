package com.altice.salescommission.service;

import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.altice.salescommission.entity.UploadConfigEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;

public interface UploadConfigService {
	List<UploadConfigEntity> getTemplatesList();
	
	List<UploadConfigEntity> getTemplatesDropdown();
	
	List<UploadConfigEntity> getColumnsList(String recordName);
	
	List<UploadConfigEntity> getColumnsConfigList(String recordName,String name);
	
	UploadConfigEntity insertTemplate(List<UploadConfigEntity> uploadsTemplateEntity) throws IdNotFoundException, DuplicateRecordException;
	
	LinkedHashSet<Map<String, String>> getRecordsList(String recordName);
	
	List<UploadConfigEntity> getColumnsEditList(String recordName);
	
	UploadConfigEntity updateTemplate(List<UploadConfigEntity> uploadsTemplateEntity) throws IdNotFoundException;
	
	
	ResponseEntity<Map<String, Object>> uploadExcelData(List<List<Object>> excelData,String uploadtemplatename) throws ParseException;
}


