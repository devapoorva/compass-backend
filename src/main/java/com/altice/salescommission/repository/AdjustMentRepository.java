package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.altice.salescommission.entity.AdjustMentEntity;



@Repository
public interface AdjustMentRepository extends JpaRepository<AdjustMentEntity, Long> {
}
