package com.altice.salescommission.queries;

public interface MyDocsQueries {
	
	public final String GET_MY_DOCS_LIST_BY_SALESCHANNEL1 = "select distinct ccdm.comm_doc_id,encode(ccdm.comm_doc,'base64') comm_doc,"
			+ "ccdm.comm_doc,ccdm.doc_name ,ccdm.doc_desc ,ccdm.status ,ccpm.comm_plan,ccdm.created_dt,ccdm.employee_id "
			+ "from c_com_doc_mgmt ccdm,c_comm_plan_master ccpm where ccdm.comm_plan_id =ccpm.comm_plan_id and ccpm.sales_channel_desc =? and coalesce(ccdm.employee_id, '') = ''";
	
	public final String GET_MY_DOCS_LIST_BY_SALESCHANNEL = "select distinct ccdm.comm_doc_id,encode(ccdm.comm_doc,'base64') comm_doc,"
			+ "ccdm.comm_doc,ccdm.doc_name ,ccdm.doc_desc ,ccdm.status ,ccdm.created_dt,ccpm.comm_plan,ccdm.effective_dt,to_char(ccdm.effective_dt,'MM-DD-YYYY') eDate  "
			+ "from c_com_doc_mgmt ccdm,c_comm_plan_master ccpm where ccdm.comm_plan_id =ccpm.comm_plan_id  and ccdm.status ='Active' "
			+ "and ccdm.effective_dt =(select MAX(ccdm1.effective_dt) from c_com_doc_mgmt ccdm1 where ccdm.doc_name  = ccdm1.doc_name  and ccdm1.effective_dt<=current_date) "
			+ "order by ccdm.effective_dt desc,ccpm.comm_plan ";
	
	public final String GET_EMP_DOCS_LIST = "select distinct ccdm.comm_doc_id,encode(ccdm.comm_doc,'base64') comm_doc,"
			+ "ccdm.comm_doc,ccdm.doc_name ,ccdm.doc_desc ,ccdm.status ,ccpm.comm_plan,ccdm.employee_id,ccdt.comm_doc_trans_id,ccdt.approved_status,"
			+ "ccdt.created_dt,to_char(ccdt.approved_on ,'MM-DD-YYYY HH:MI:SS') approved_on "
			+ "from c_com_doc_mgmt ccdm,c_comm_plan_master ccpm,c_com_doc_trans ccdt "
			+ "where ccdt.comm_plan_id =ccpm.comm_plan_id and ccdm.comm_doc_id = ccdt.comm_doc_id and ccdt.employee_id = ? "
			+ "and ccdt.approved_status in ('Pending','Approved')  and coalesce(ccdm.employee_id, '') = ''";
	
	public final String GET_SUP_DOCS_LIST = "select distinct ccdm.comm_doc_id,encode(ccdm.comm_doc,'base64') comm_doc,"
			+ "ccdm.comm_doc,ccdm.doc_name ,ccdm.doc_desc ,ccdm.status ,ccpm.comm_plan,ccdm.created_dt,"
			+ "to_char(ccdm.created_dt,'MM-DD-YYYY') uploadeddt,ccdm.employee_id, concat(cem.last_name,', ' ,cem.first_name) ename,to_char(ccdt.approved_on ,'MM-DD-YYYY HH:MI:SS') approved_on,ccdt.approved_status "
			+ "from c_com_doc_mgmt ccdm,c_com_doc_trans ccdt ,c_employee_master cem,c_comm_plan_master ccpm "
			+ "where ccdm.comm_doc_id =ccdt.comm_doc_id and ccdt.employee_id =cem.employee_id  "
			+ "and ccdm.comm_plan_id =ccpm.comm_plan_id and cem.supervisor_id =? and cem.employee_id != ? and coalesce(ccdm.employee_id, '') = ''";
	
	public final String GET_MY_DOCS_LIST = "select distinct ccdm.comm_doc_id,encode(ccdm.comm_doc,'base64') comm_doc,"
			+ "ccdm.comm_doc,ccdm.doc_name ,ccdm.doc_desc ,ccdm.status ,ccpm.comm_plan,ccdm.comm_plan_id,ccdm.created_dt,to_char(ccdm.created_dt,'MM-DD-YYYY') uploadeddt,ccdm.effective_dt,to_char(ccdm.effective_dt,'MM-DD-YYYY') eDate,ccdm.employee_id "
			+ "from c_com_doc_mgmt ccdm,c_comm_plan_master ccpm where ccdm.comm_plan_id =ccpm.comm_plan_id and coalesce(ccdm.employee_id, '') = ''";

	public final String GET_COMMPLAN_DESC_LIST = "select concat(comm_plan,' - ',comm_plan_id) comm_plan,comm_plan_id  from c_comm_plan_master ccpm where active_flag ='Y' "
			+ "and ccpm.effective_date = (select max(ccpm1.effective_date) from c_comm_plan_master ccpm1 where ccpm.comm_plan_id = ccpm1.comm_plan_id  and ccpm1.effective_date <=(SELECT CURRENT_DATE)) order by comm_plan";
	
	public final String GET_COMMPLAN_LIST = "select distinct comm_plan_id ,comm_plan from c_comm_plan_master ccpm where sales_channel_desc =? order by comm_plan";
	
	public final String GET_SALES_CHANNEL_LIST = "select distinct sales_channel_desc  from c_comm_plan_master  ccpm  order by sales_channel_desc";

	public final String GET_USER_TYPES_LIST = "select field_value from c_translate_master where field_name ='User Type' and effective_status='Y'";

	public final String GET_ASSIGNED_DOCS_LIST_ONE = "select ccpm.comm_plan ,ccdm.user_type ,ccdm.effective_dt, ccdm.comm_plan_id "
			+ "from c_com_doc_trans ccdm,c_comm_plan_master ccpm "
			+ "where ccdm.comm_plan_id =ccpm.comm_plan_id and length(ccdm.employee_id) > 2 and ccdm.comm_doc_id =? "
			+ "group by ccpm.comm_plan ,ccdm.user_type,ccdm.effective_dt, ccdm.comm_plan_id";

	public final String GET_ASSIGNED_DOCS_LIST_TWO = "select distinct(ccdm.comm_doc_id) as comm_doc_id ,ccdm.employee_id,concat(cem.first_name,' ',cem.middle_name,' ',cem.last_name) as name "
			+ "from c_com_doc_trans ccdm, c_employee_master cem "
			+ "where ccdm.employee_id =cem.employee_id and ccdm.comm_doc_id =? and ccdm.comm_plan_id =? and ccdm.user_type =? and ccdm.effective_dt::varchar=?";

	public final String GET_EMPLOYEE_LIST = "select distinct(employee_id) employee_id ,concat(first_name,' ',middle_name,' ',last_name) as name,sc_emp_id "
			+ "from c_employee_master where (lower(first_name) like lower(?) or lower(last_name) like lower(?) or lower(employee_id) like lower(?)) and user_type =? "
			+ "and employee_id NOT IN (select ccdt.employee_id from c_com_doc_trans ccdt,c_com_doc_mgmt ccdm  "
			+ "where ccdt.comm_doc_id =ccdm.comm_doc_id  and ccdt.user_type = ? and ccdm.doc_name =? and ccdt.comm_plan_id=?) "
			+ "order by name";

	public final String GET_ASSIGNED_DOCS_ALL_LIST = "select ccpm.comm_plan ,ccdt.user_type ,ccdt.effective_dt, ccdm.comm_plan_id,ccdt.employee_id,concat(cem.first_name,' ',cem.middle_name,' ',cem.last_name) as name "
			+ "from c_com_doc_mgmt ccdm,c_com_doc_trans ccdt ,c_comm_plan_master ccpm,c_employee_master cem "
			+ "where ccdm.comm_doc_id =ccdt.comm_doc_id  and ccdt.comm_plan_id =ccpm.comm_plan_id and ccdt.employee_id =cem.employee_id and ccdm.doc_desc = ? "
			+ "group by ccpm.comm_plan ,ccdt.user_type,ccdt.effective_dt, ccdm.comm_plan_id,ccdt.employee_id,name";

	public final String GET_EMPLOYEE_ASSIGNED_DOCS_LIST1 = "select ccdm.doc_name ,ccdm.doc_desc ,ccdt.comm_doc_id ,ccdt.employee_id,"
			+ "ccdt.approved_on,to_char(ccdt.approved_on,'MM-DD-YYYY') signOffDate,concat(cem.first_name,',',cem.last_name) employeename ,ccpm.comm_plan "
			+ "from c_com_doc_trans ccdt ,c_com_doc_mgmt ccdm,c_employee_master cem,c_comm_plan_master ccpm "
			+ "where ccdt.comm_doc_id =ccdm.comm_doc_id and ccdt.employee_id =cem.employee_id and ccdt.comm_plan_id =ccpm.comm_plan_id  and ccdt.employee_id =? "
			+ "group by ccdm.doc_name ,ccdm.doc_desc ,ccdt.comm_doc_id ,ccdt.employee_id,ccdt.approved_on, employeename ,ccpm.comm_plan";
	
	public final String GET_EMPLOYEE_ASSIGNED_DOCS_LIST = "select ccdm.doc_name ,ccdm.doc_desc ,ccdt.comm_doc_id ,ccdt.employee_id,"
			+ "ccdt.approved_on,ccdt.approved_on signOffDate,concat(cem.first_name,',',cem.last_name) employeename ,ccpm.comm_plan "
			+ "from c_com_doc_trans ccdt ,c_com_doc_mgmt ccdm,c_employee_master cem,c_comm_plan_master ccpm "
			+ "where ccdt.comm_doc_id =ccdm.comm_doc_id and ccdt.employee_id =cem.employee_id and ccdt.comm_plan_id =ccpm.comm_plan_id and ccdt.employee_id IN (?)  "
			+ "group by ccdm.doc_name ,ccdm.doc_desc ,ccdt.comm_doc_id ,ccdt.employee_id,ccdt.approved_on, employeename ,ccpm.comm_plan";
}
