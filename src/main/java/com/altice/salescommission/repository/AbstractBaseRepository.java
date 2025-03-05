package com.altice.salescommission.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.altice.salescommission.entity.AbstractBaseEntity;



@NoRepositoryBean
public interface AbstractBaseRepository<T extends AbstractBaseEntity, ID extends Serializable>
extends JpaRepository<T, ID>{
    
}