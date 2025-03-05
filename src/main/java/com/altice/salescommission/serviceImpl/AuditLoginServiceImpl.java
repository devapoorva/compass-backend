package com.altice.salescommission.serviceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.AuditLoginEntity;
import com.altice.salescommission.repository.AuditLoginRepository;
import com.altice.salescommission.service.AuditLoginService;

@Service
@Transactional
public class AuditLoginServiceImpl extends AbstractBaseRepositoryImpl<AuditLoginEntity, Long>
		implements AuditLoginService {

	@Autowired
	private AuditLoginRepository auditLoginRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public AuditLoginServiceImpl(AuditLoginRepository auditLoginRepository) {

		super(auditLoginRepository);
	}

	/* This method is used to create a new record on transaction table */
	@Override
	public AuditLoginEntity addLogins(String network_id, String email,String employee_id,String created_by) {
		

		AuditLoginEntity auditLoginModel = new AuditLoginEntity();
		auditLoginModel.setNetwork_id(network_id);
		auditLoginModel.setEmail(email);
		auditLoginModel.setEmployee_id(employee_id);
		auditLoginModel.setLogin(new Date());
		auditLoginModel.setCreated_by(created_by);
		auditLoginModel.setCreated_dt(new Date());
		auditLoginRepository.save(auditLoginModel);
		return auditLoginModel;
	}

	/* Get the list of logins based on employee id */
	@Override
	public List<AuditLoginEntity> getLogins(String employeeid) {
		String sql = "select employee_id,network_id,email ,to_char(login,'MM/DD/YYYY HH24:MI:SS') as login from audit_login al  where employee_id = ?";

		List<AuditLoginEntity> loginsList = jdbcTemplate.query(sql,
				new RowMapper<AuditLoginEntity>() {

					@Override
					public AuditLoginEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						AuditLoginEntity activeRateCodeModel = new AuditLoginEntity();
						activeRateCodeModel.setEmployee_id(rs.getString("employee_id"));
						activeRateCodeModel.setNetwork_id(rs.getString("network_id"));
						activeRateCodeModel.setEmail(rs.getString("email"));
						activeRateCodeModel.setLoggedinTime(rs.getString("login"));
						return activeRateCodeModel;
					}
				}, new Object[] { employeeid });

		return loginsList;
	}

}
