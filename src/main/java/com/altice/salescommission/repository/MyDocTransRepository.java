package com.altice.salescommission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.MyDocTransEntity;

public interface MyDocTransRepository extends AbstractBaseRepository<MyDocTransEntity, Long> {
	@Query(value = "select coalesce(max(sc_emp_id),'0') sc_emp_id from c_employee_master p where p.employee_id =?1", nativeQuery = true)
	String getSCEmpId(String empid);

	@Query(value = "select sales_rep_id from c_employee_master cem where employee_id =?1 and comm_plan_id is not null limit 1", nativeQuery = true)
	String getSalesRepID(String scempid);

	@Query(value = "select tracking_id from c_com_doc_trans p where p.comm_doc_trans_id =?1", nativeQuery = true)
	String getTrackingId(int docid);

	public List<MyDocTransEntity> findByEmployeeId(String empid);

}
