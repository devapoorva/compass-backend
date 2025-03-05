package com.altice.salescommission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.EmployeeCorpEntity;

public interface EmployeeCorpRepository extends JpaRepository<EmployeeCorpEntity, Long> {

	@Query(value = "select distinct ccpd.display_name ,(coalesce(tmc.commission_val,'0.00')+coalesce(tmc.adjustment,'0.00')) commission_val,cal_run_id "
			+ "from t_monthly_commission tmc,c_kpi_master ckm,c_employee_master cem,c_comm_plan_detail ccpd  "
			+ "where tmc.kpi_id =ckm.kpi_id and tmc.sc_emp_id =cem.sc_emp_id  and tmc.kpi_id =ccpd.kpi_id and tmc.comm_plan_id = ccpd.comm_plan_id and cem.employee_id =?1 and tmc.commission_val !=0 "
			+ "and cal_run_id = ?2 order by cal_run_id desc", nativeQuery = true)
	List<Object[]> getKpisByEmpidRecentData(String scempid, int commyear);

}
