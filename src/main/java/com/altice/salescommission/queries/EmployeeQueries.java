package com.altice.salescommission.queries;

public interface EmployeeQueries {
	public final String GET_LOGINS_LIST = "select concat(first_name,' ',middle_name,' ',last_name) as name,user_type ,"
			+ "sales_rep_channel,operator_id ,sales_rep_type "
			+ "from c_employee_master  where first_name like '%?%'";
}
