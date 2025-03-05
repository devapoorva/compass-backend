package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.altice.salescommission.entity.ProductMasterEntity;

public interface ManageCodesRepository extends JpaRepository<ProductMasterEntity, Long> {
	
}
