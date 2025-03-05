package com.altice.salescommission.serviceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.MissingDisputesEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.model.KpiModel;
import com.altice.salescommission.queries.MissingDisputesQueries;
import com.altice.salescommission.repository.MissingDisputesRepository;
import com.altice.salescommission.service.MissingDisputesService;

@Service
@Transactional
public class MissingDisputesServiceImpl implements MissingDisputesService, MissingDisputesQueries {

	private static final Logger logger = LoggerFactory.getLogger(MissingDisputesServiceImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MissingDisputesRepository missingDisputesRepository;
	
		
	/* This method is used to get all the active KPI's */
	@Override
	public List<MissingDisputesEntity> getKpiList(int commplanid) {
		
		List<MissingDisputesEntity> kpiList = jdbcTemplate.query(GET_KPIS_LIST, new RowMapper<MissingDisputesEntity>() {

			@Override
			public MissingDisputesEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				MissingDisputesEntity kpiModel = new MissingDisputesEntity();
				kpiModel.setId(rs.getLong("kpi_id"));
//				kpiModel.setKpi_name(rs.getString("kpi_name"));
				kpiModel.setKpidisplayname(rs.getString("display_name"));
				return kpiModel;
			}
		}, new Object[] { commplanid });
		return kpiList;
	}

	@Override
	public List<MissingDisputesEntity> getMissingDisputesList() {
		// List<MissingDisputesEntity> userInfo = new
		// ArrayList<MissingDisputesEntity>();
		// userInfo = missingDisputesRepository.findAll();

		List<MissingDisputesEntity> userInfo = jdbcTemplate.query(GET_MISSING_DISPUTES_LIST,
				new RowMapper<MissingDisputesEntity>() {

					@Override
					public MissingDisputesEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						MissingDisputesEntity missDisModel = new MissingDisputesEntity();
						missDisModel.setId(rs.getLong("rowid"));
						missDisModel.setKpi_id(rs.getString("kpi_id"));
						missDisModel.setComm_plan_id(rs.getInt("comm_plan_id"));
						missDisModel.setCust_id(rs.getString("cust_id"));
						missDisModel.setCorp(rs.getInt("corp"));
						missDisModel.setHouse(rs.getInt("house"));
						missDisModel.setRevenue(rs.getFloat("revenue"));
						missDisModel.setWordt(rs.getString("wordate"));
						missDisModel.setWfindt(rs.getString("wfindate"));
						missDisModel.setMessage(rs.getString("message"));
						missDisModel.setComment(rs.getString("comment"));
						missDisModel.setCreateddt(rs.getString("created_dt"));
						missDisModel.setSales_rep_id(rs.getString("sales_rep_id"));
						return missDisModel;
					}
				});

		return userInfo;
	}

	@Override
	public int insertMissingDisputes(String kpi_id, String comm_plan_id, String cust_id, String corp,
			String currentUser, String house, String revenue, String wordate, String wfindate, String message,
			 String sales_rep_id) throws DuplicateRecordException {

		int count = 0;

		count = jdbcTemplate.update(
				"INSERT INTO c_missing_disputes (kpi_id, comm_plan_id,cust_id, corp,  house, revenue,wordate, wfindate, message, created_by, created_dt, sales_rep_id) "
						+ "VALUES ('" + kpi_id + "','" + comm_plan_id + "','" + cust_id + "','" + corp + "','" + house
						+ "','" + revenue + "','" + wordate + "','" + wfindate + "','" + message + "','" + currentUser + "','" + new Date() + "','" + sales_rep_id + "')");

		logger.info("count = " + count);
		return count;
	}
	
	@Override
	public int insertMissingDisputesStatic(String kpi_id, String currentUser,  String message
			) throws DuplicateRecordException {
		
		int count = 0;

		count = jdbcTemplate.update(
				"INSERT INTO c_missing_disputes (kpi_id,  message, created_by, created_dt) "
						+ "VALUES ('" + kpi_id + "','" + message + "','" + currentUser + "','" + new Date() + "')");

		logger.info("count = " + count);
		return count;
	}

}
