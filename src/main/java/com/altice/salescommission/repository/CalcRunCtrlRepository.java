package com.altice.salescommission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.CalcRunCtrlEntity;
import com.altice.salescommission.exception.ResourceNotFoundException;

public interface CalcRunCtrlRepository extends JpaRepository<CalcRunCtrlEntity, Long> {

	List<CalcRunCtrlEntity> findByRunCtrlId(int runctrlId) throws ResourceNotFoundException;
	
	@Query(value="select count(p.run_control_name) from c_calc_run_ctl p where UPPER(p.run_control_name) = UPPER(?1)", nativeQuery = true)
	int getRunControlNameCount(String run_control_name);

}
