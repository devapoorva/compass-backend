package com.altice.salescommission.serviceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.model.BossCallMappingModel;
import com.altice.salescommission.queries.BossCallMappingQueries;
import com.altice.salescommission.service.BossCallMappingService;


@Service
@Transactional
public class BossCallMappingServiceImpl implements BossCallMappingService, BossCallMappingQueries {
	private static final Logger logger = LoggerFactory.getLogger(BossCallMappingServiceImpl.class.getName());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public BossCallMappingModel saveCallData(BossCallMappingModel callModel) {
		// TODO Auto-generated method stub
		//
		try {
			jdbcTemplate.update("INSERT INTO scopmgr.boss_call_mapping\r\n"
					+ "(network_id, operator_id, class_name, sales_rep_id, first_name, last_name, valid_from_dt, valid_to_dt, employee_id)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);",
					new Object[] { callModel.getNetwork_id(), callModel.getOperator_id(), callModel.getClass_name(),
							callModel.getSales_rep_id(), callModel.getFirst_name(), callModel.getLast_name(),
							callModel.getValid_from_dt(), callModel.getValid_to_dt(), callModel.getEmployee_id() });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callModel;
		// callMapingRepository.save(callModel);
	}
	
	@Override
	public BossCallMappingModel updateCallMapping(BossCallMappingModel callModel) {
		try {
			jdbcTemplate.update("UPDATE scopmgr.boss_call_mapping SET network_id=?, operator_id=?,\r\n"
					+ " class_name=?, sales_rep_id=?, first_name=?, last_name=?, valid_from_dt=?, valid_to_dt=?, employee_id=?"
					+ "where network_id =? and valid_from_dt =?",
					new Object[] { callModel.getNetwork_id(), callModel.getOperator_id(), callModel.getClass_name(),
							callModel.getSales_rep_id(), callModel.getFirst_name(), callModel.getLast_name(),
							callModel.getValid_from_dt(), callModel.getValid_to_dt(), callModel.getEmployee_id(),
							callModel.getNetwork_id(), callModel.getValid_from_dt() });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callModel;
	}
	
	@Override
	public List<BossCallMappingModel> getCallMapingData(String feedDate) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(feedDate);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(formatter.format(c.getTime()));
			// System.out.println(" dat " + (formatter.format(c.getTime())));
			List<BossCallMappingModel> bossCallMapDataList = jdbcTemplate.query(GET_CALL_MAP_DATA,
					new RowMapper<BossCallMappingModel>() {

						@Override
						public BossCallMappingModel mapRow(ResultSet rs, int rowNum) throws SQLException {
							BossCallMappingModel callMapModel = new BossCallMappingModel();
							callMapModel.setClass_name(rs.getString("class_name"));
							callMapModel.setEmployee_id(rs.getString("employee_id"));
							callMapModel.setFirst_name(rs.getString("first_name"));
							callMapModel.setLast_name(rs.getString("last_name"));
							callMapModel.setNetwork_id(rs.getString("network_id"));
							callMapModel.setOperator_id(rs.getString("operator_id"));
							callMapModel.setSales_rep_id(rs.getString("sales_rep_id"));
							callMapModel.setValid_from_dt(rs.getDate("valid_from_dt"));
							callMapModel.setValid_to_dt(rs.getDate("valid_to_dt"));
							return callMapModel;
						}
					}, new Object[] { date1, date1 });

			return bossCallMapDataList;
		} catch (Exception ex) {
			ex.printStackTrace();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}
	
	@Override
	public Map<String, Boolean> deleteCallMapData(String id, String validFromDt) {
		// TODO Auto-generated method stub
		Map<String, Boolean> response = new HashMap<>();

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(validFromDt);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(formatter.format(c.getTime()));
			jdbcTemplate.update("DELETE FROM scopmgr.boss_call_mapping  where network_id =? and valid_from_dt =?",
					new Object[] { id, date1 });
			response.put("deleted", Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}
	
	
}
