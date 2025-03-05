package com.altice.salescommission.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.altice.salescommission.entity.EmployeeMasterEntity;

@Repository
public interface PersonalInfoRepository extends JpaRepository<EmployeeMasterEntity, Long> {

	@Query(value = "select * from c_employee_master cem where profile_status ='complete' and employee_id =?1 order by case when user_role ='Sales Representative' then 1 when user_role ='Supervisor' then 2 else 3 end  desc,effective_date desc limit 1", nativeQuery = true)
	List<EmployeeMasterEntity> getUserInfo(String loggedInEmployeeId);

	@Query(value = "select * from c_employee_master cem where employee_id =?1 and effective_date=?2 order by case when user_role ='Sales Representative' then 1 when user_role ='Supervisor' then 2 else 3 end  desc,effective_date desc limit 1", nativeQuery = true)
	List<EmployeeMasterEntity> getUserInfoHome(String loggedInEmployeeId, LocalDate edate);
	
	@Query(value = "select * from c_employee_master cem where employee_id =?1  order by case when user_role ='Sales Representative' then 1 when user_role ='Supervisor' then 2 else 3 end  desc,effective_date desc limit 1", nativeQuery = true)
	List<EmployeeMasterEntity> getUserInfoHome1(String loggedInEmployeeId);

	@Query(value = "select count(employee_id) employee_id from c_employee_master cem where profile_status ='complete' and employee_id =?1 and user_role ='Supervisor'", nativeQuery = true)
	int getMultiProfileCount(String loggedInEmployeeId);

	public List<EmployeeMasterEntity> findByProfileStatus(String profileStatus);

	public List<EmployeeMasterEntity> findByEmployeeId(String empid);

	public List<EmployeeMasterEntity> findByUserRole(String role);

	@Query(value = "select max(effective_date) edate from c_employee_master cem where profile_status ='complete' and employee_id =?1 and effective_date=?2", nativeQuery = true)
	Date getOrgEffectiveDate(String employeeid, Date edate);

	@Query(value = "select max(effective_date) edate from c_employee_master cem where  employee_id =?1 and effective_date=?2", nativeQuery = true)
	Date getOrgEffectiveDateBulkupdate(String employeeid, Date edate);

	@Query(value = "select count(effective_date) edate from c_employee_master cem where profile_status ='complete' and employee_id =?1 and effective_date=?2", nativeQuery = true)
	int getOrgEffectiveDateCount(String employeeid, Date edate);

	@Query(value = "select count(effective_date) edate from c_employee_master cem where employee_id =?1 and effective_date=?2", nativeQuery = true)
	int getOrgEffectiveDateCountBulkupdate(String employeeid, Date edate);

	@Query(value = "select max(coalesce(p.sales_rep_id,'NA')) sales_rep_id from c_employee_master p where p.profile_status ='complete' and p.sc_emp_id =?1", nativeQuery = true)
	String getSalesRepID(String id);

	@Query(value = "select max(sc_emp_id) maxid from c_employee_master", nativeQuery = true)
	Long getMaxID();

	@Query(value = "select p.* from c_employee_master p where profile_status ='complete' and user_role notnull", nativeQuery = true)
	List<EmployeeMasterEntity> getProfiles();

	@Query(value = "select p.* from c_employee_master p where p.profile_status='complete' and LOWER(p.employee_id) =LOWER(?1) and LOWER(p.network_id) =LOWER(?2)", nativeQuery = true)
	List<EmployeeMasterEntity> validateCredentials(String loggedInEmployeeId, String loggedInNetworkId);

	@Query(value = "select count(p.employee_id) from c_employee_master p where p.profile_status='complete' and LOWER(p.employee_id) =LOWER(?1) and LOWER(p.network_id) =LOWER(?2)", nativeQuery = true)
	int getProfileStatusCount(String loggedInEmployeeId, String loggedInNetworkId);

	@Query(value = "select count(sc_emp_id) cnt from c_employee_master p where p.sc_emp_id =?1 and p.employee_id =?2 and p.sales_rep_id =?3 and effective_date =?4", nativeQuery = true)
	int getProfileCount(String sc_emp_id, String employee_id, String sales_rep_id, Date effective_date);

	@Query(value = "select count(username) cnt from users cem where LOWER(username) =LOWER(?1)", nativeQuery = true)
	int getTokenUsersCount(String username);

	@Query(value = "select id from users cem where LOWER(username) =LOWER(?1)", nativeQuery = true)
	int getTokenUserId(String username);

}
