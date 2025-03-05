package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.model.CorpEntity;

public interface ManageRegionsRepository extends AbstractBaseRepository<CorpEntity, Long> {
		
	@Query(value="select count(p) from c_corp_master p  where p.major_area =?1 and p.region =?2 and p.area =?3 and lower(p.corp) =lower(?4)", nativeQuery = true)
	int getExistingCorpCount(String majorarea, String region, String area,String corp);
}
