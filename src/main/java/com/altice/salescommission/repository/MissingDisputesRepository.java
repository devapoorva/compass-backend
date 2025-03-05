package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.altice.salescommission.entity.MissingDisputesEntity;

public interface MissingDisputesRepository extends JpaRepository<MissingDisputesEntity, Long> {

}
