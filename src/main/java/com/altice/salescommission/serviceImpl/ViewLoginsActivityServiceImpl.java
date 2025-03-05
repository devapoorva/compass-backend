package com.altice.salescommission.serviceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.EmployeeMasterEntity;
import com.altice.salescommission.queries.EmployeeQueries;
import com.altice.salescommission.service.ViewLoginsActivityService;

@Service
@Transactional
public class ViewLoginsActivityServiceImpl implements ViewLoginsActivityService, EmployeeQueries {

	

	@Autowired
	private JdbcTemplate jdbcTemplate;

	

	@Override
	public List<EmployeeMasterEntity> getLoginsActivityList(String first_name) {

		List<EmployeeMasterEntity> empList = jdbcTemplate.query(GET_LOGINS_LIST, new RowMapper<EmployeeMasterEntity>() {

			@Override
			public EmployeeMasterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				EmployeeMasterEntity empModel = new EmployeeMasterEntity();
				empModel.setName(rs.getString("name"));
				empModel.setUser_type(rs.getString("user_type"));
				empModel.setSales_rep_channel(rs.getString("sales_rep_channel"));
				empModel.setOperator_id(rs.getString("operator_id"));
				empModel.setSales_rep_type(rs.getString("sales_rep_type"));
				return empModel;
			}
		}, new Object[] { first_name });

		return empList;
	}

}
