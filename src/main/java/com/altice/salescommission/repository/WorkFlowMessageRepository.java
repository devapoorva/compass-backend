package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.altice.salescommission.entity.WorkflowMessageEntity;

public interface WorkFlowMessageRepository extends JpaRepository<WorkflowMessageEntity, Long> {
	@Query(value = "select p.supervisor_id from c_employee_master p where p.employee_id = ?1 and user_role = ?2 and p.profile_status ='complete' and comm_plan_id is not null "
			+ "and p.effective_date = (select max(p1.effective_date) from c_employee_master p1 where p.sc_emp_id  = p1.sc_emp_id and p1.profile_status ='complete' and p1.effective_date <=(SELECT CURRENT_DATE))", nativeQuery = true)
	String getSupId(String employee_id,String role);
	
	@Query(value = "select (select concat(cem1.first_name,' ',cem1.last_name)  from c_employee_master cem1 where cem1.profile_status ='complete' and cem1.employee_id =cem2.supervisor_id limit 1) supervisor_name "
			+ "from c_employee_master cem2 where cem2.profile_status ='complete' and cem2.employee_id =?1 and cem2.user_role =?2 "
			+ "and cem2.effective_date = (select max(p1.effective_date) from c_employee_master p1 where cem2.sc_emp_id  = p1.sc_emp_id and p1.profile_status ='complete' and p1.effective_date <=(SELECT CURRENT_DATE))", nativeQuery = true)
	String getSupName(String employee_id,String role);
	
	@Query(value = "select concat(p.first_name ,' ', p.last_name) empname from c_employee_master p where p.employee_id = ?1 and p.user_role =?2 and p.profile_status ='complete' and comm_plan_id is not null "
			+ "and p.effective_date = (select max(p1.effective_date) from c_employee_master p1 where p.sc_emp_id  = p1.sc_emp_id and p1.profile_status ='complete' and p1.effective_date <=(SELECT CURRENT_DATE)) limit 1", nativeQuery = true)
	String getEmpName(String employee_id,String role);
	
	@Query(value = "select p.sales_rep_id from c_employee_master p where p.employee_id =?1 and p.user_role =?2 and p.profile_status ='complete' and p.comm_plan_id is not null "
			+ "and p.effective_date = (select max(p1.effective_date) from c_employee_master p1 where p.sc_emp_id  = p1.sc_emp_id and p1.profile_status ='complete' and p1.effective_date <=(SELECT CURRENT_DATE))", nativeQuery = true)
	String getSalesRepID(String employee_id,String role);
	
	@Query(value = "select distinct kpi_name from c_kpi_master ckm  where kpi_id = ?1 order by kpi_name desc limit 1", nativeQuery = true)
	String getKPIName(int kpiid);
	
	@Query(value = "select distinct comm_plan from c_comm_plan_master ccpm where comm_plan_id = ?1 order by comm_plan desc limit 1", nativeQuery = true)
	String getCommPlanName(int commplanid);
	
	@Query(value = "select adjustment_id  from t_kpi_chargeback_override tkco  where comm_plan_id =?1 and corp=?2 and house=?3 and cust =?4 and sc_emp_id =?5 and kpi_id =?6", nativeQuery = true)
	int getAdjustmentID(int comm_plan_id,int corp,String house,String cust,String sc_emp_id,int kpi_id);
}
