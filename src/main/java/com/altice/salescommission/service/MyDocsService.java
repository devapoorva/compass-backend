package com.altice.salescommission.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.altice.salescommission.entity.MyDocTransEntity;
import com.altice.salescommission.entity.MyDocsEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;

public interface MyDocsService extends AbstractBaseService<MyDocsEntity, Long> {
	List<MyDocsEntity> getMyDocsList();
	
	List<MyDocsEntity> getSupDocsList(String employeeid);
	
	List<MyDocsEntity> getmydocslistBySaleschannel(String saleschannel);
	
	List<MyDocsEntity> getEmpDocsList(String employeeid);
	
	List<MyDocsEntity> getEmployeeDocsList(String employeeid);
	
	int submitStatus(String currentUser,String status, int docid) throws DuplicateRecordException;

	
	MyDocsEntity updateDocs(List<MyDocsEntity> myDocsModel) throws IOException, ParseException;

	MyDocsEntity uploadDocs(MultipartFile file, String doc_name, String doc_desc, int comm_plan_id, String current_user, String effective_dt)
			throws IOException, ParseException;

	MyDocsEntity updateSingleFile(MultipartFile file, Long id) throws IOException;
	
	List<MyDocsEntity> getSaleschannelsList();

	List<MyDocsEntity> getCommPlanList(String saleschannel);
	
	List<MyDocsEntity> getCommPlanDescList();

	List<MyDocsEntity> getUserTypesList();

	List<MyDocsEntity> getAssignedDocsList(int doc_desc);
	
	List<MyDocsEntity> getAssignedDocsEmpList(int docid,int comm_plan_id,String user_type, String effective_dt);
	
	List<MyDocsEntity> getExcelAssignedDocsList(String doc_desc);

	List<MyDocsEntity> getEmployeeList(String val,String user_type,String doc_desc,int comPlanId);

	MyDocTransEntity assignDocToEmployees(List<MyDocTransEntity> myDocsModel) throws IdNotFoundException;

	int deleteAssignedEmployee(long id, String employeeId) throws IOException;

	List<MyDocTransEntity> filterUserDocByEmp(String empId);

	MyDocTransEntity saveDocs(List<MyDocTransEntity> myDocsModel) throws ParseException;

}
