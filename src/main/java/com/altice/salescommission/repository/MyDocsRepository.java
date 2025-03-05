package com.altice.salescommission.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.MyDocsEntity;

public interface MyDocsRepository extends AbstractBaseRepository<MyDocsEntity, Long> {
	public List<MyDocsEntity> findByEmployeeId(String empid);

	@Query(value = "select p.effective_dt from c_com_doc_mgmt p where p.comm_doc_id =?1", nativeQuery = true)
	Date getOrgEffectiveDate(Long id);
	
	@Query(value = "select p.comm_doc from c_com_doc_mgmt p where p.comm_doc_id =?1", nativeQuery = true)
	byte[] getDoc(Long id);
}
