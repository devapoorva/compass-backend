package com.altice.salescommission.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.altice.salescommission.entity.AbstractBaseEntity;

public interface AbstractBaseService<T extends AbstractBaseEntity, ID extends Serializable> {
	public abstract T save(T entity);

	public abstract List<T> findAll();

	public abstract Optional<T> findById(ID entityId);

	public abstract T update(T entity);

	public abstract T updateById(T entity, ID entityId);

	public abstract void delete(T entity);

	public abstract void deleteById(ID entityId);

	

	// other methods u might need to be generic

}