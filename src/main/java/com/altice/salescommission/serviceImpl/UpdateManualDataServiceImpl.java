package com.altice.salescommission.serviceImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.AdjustmentsDetailEntity;
import com.altice.salescommission.entity.KpiGoalsEntity;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.model.UpdateManualDataModel;
import com.altice.salescommission.queries.UpdateManualDataQueries;
import com.altice.salescommission.repository.AdjustmentsDetailRepository;
import com.altice.salescommission.repository.KpiGoalsRepository;
import com.altice.salescommission.service.UpdateManualDataService;

@Service
@Transactional
public class UpdateManualDataServiceImpl implements UpdateManualDataService, UpdateManualDataQueries {
	private static final Logger logger = LoggerFactory.getLogger(UpdateManualDataServiceImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment environment;

	@Autowired
	private AdjustmentsDetailRepository adjustmentsDetailRepository;

	@Autowired
	private KpiGoalsRepository kpiGoalsRepository;

	@Override
	public AdjustmentsDetailEntity saveAdjustment(AdjustmentsDetailEntity adjustmentsDetailEntity) {
		logger.info("adjustmentsDetailEntity = " + adjustmentsDetailEntity);
		try {
			AdjustmentsDetailEntity adjustmentObj = new AdjustmentsDetailEntity();

			adjustmentObj.setSc_emp_id(adjustmentsDetailEntity.getSc_emp_id());
			adjustmentObj.setCal_run_id(adjustmentsDetailEntity.getCal_run_id());
			adjustmentObj.setComm_plan_id(adjustmentsDetailEntity.getComm_plan_id());
			adjustmentObj.setKpi_id(adjustmentsDetailEntity.getKpi_id());
			adjustmentObj.setAdjustment(adjustmentsDetailEntity.getAdjustment());
			adjustmentObj.setAdjustment_comments(adjustmentsDetailEntity.getAdjustment_comments());

			adjustmentsDetailRepository.save(adjustmentObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adjustmentsDetailEntity;
	}

	@Override
	public KpiGoalsEntity saveGoal(KpiGoalsEntity kpiGoalsEntity) {
		logger.info("kpiGoalsEntity = " + kpiGoalsEntity);
		try {
			KpiGoalsEntity goalObj = new KpiGoalsEntity();

			goalObj.setSc_emp_id(kpiGoalsEntity.getSc_emp_id());
			goalObj.setCal_run_id(kpiGoalsEntity.getCal_run_id());
			goalObj.setComm_plan_id(kpiGoalsEntity.getComm_plan_id());
			goalObj.setKpi_id(kpiGoalsEntity.getKpi_id());
			goalObj.setYield_goal(kpiGoalsEntity.getYield_goal());
			goalObj.setComm_plan_type(kpiGoalsEntity.getComm_plan_type());
			goalObj.setUpload_type(kpiGoalsEntity.getUpload_type());

			kpiGoalsRepository.save(goalObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kpiGoalsEntity;
	}

	@Override
	public Map<String, Boolean> updateAdjustment(AdjustmentsDetailEntity adjustmentsDetailEntity) {
		logger.info("adjustmentsDetailEntity = " + adjustmentsDetailEntity);
		Map<String, Boolean> response = new HashMap<>();
		try {

			int count = jdbcTemplate.update("UPDATE t_adjustment_detail set cal_run_id='"
					+ adjustmentsDetailEntity.getCal_run_id() + "',kpi_id='" + adjustmentsDetailEntity.getKpi_id()
					+ "',comm_plan_id='" + adjustmentsDetailEntity.getComm_plan_id() + "'," + "adjustment='"
					+ adjustmentsDetailEntity.getAdjustment() + "',adjustment_comments='"
					+ adjustmentsDetailEntity.getAdjustment_comments() + "' where adjustment_id='"
					+ adjustmentsDetailEntity.getId() + "'");
			logger.info("count = " + count);

			if (count > 0) {
				logger.info("Inside count IF");
				response.put("updated", Boolean.TRUE);
			} else {
				logger.info("Inside count ELSE");
				response.put("updated", Boolean.FALSE);
			}

		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public Map<String, Boolean> updateGoal(KpiGoalsEntity kpiGoalsEntity) {
		logger.info("kpiGoalsEntity = " + kpiGoalsEntity);
		Map<String, Boolean> response = new HashMap<>();
		try {

			int count = jdbcTemplate.update("UPDATE t_kpi_goal set cal_run_id='" + kpiGoalsEntity.getCal_run_id()
					+ "',kpi_id='" + kpiGoalsEntity.getKpi_id() + "',comm_plan_id='" + kpiGoalsEntity.getComm_plan_id()
					+ "'," + "yield_goal='" + kpiGoalsEntity.getYield_goal() + "',comm_plan_type='"
					+ kpiGoalsEntity.getComm_plan_type() + "',upload_type='" + kpiGoalsEntity.getUpload_type()
					+ "' where goal_id='" + kpiGoalsEntity.getId() + "'");
			logger.info("count = " + count);

			if (count > 0) {
				logger.info("Inside count IF");
				response.put("updated", Boolean.TRUE);
			} else {
				logger.info("Inside count ELSE");
				response.put("updated", Boolean.FALSE);
			}

		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public int deleteAdjustment(int id) throws IOException {
		logger.info("id = " + id);
		int deleteCount = 0;
		String deleteQuery = "delete from t_adjustment_detail where adjustment_id=?";
		deleteCount = jdbcTemplate.update(deleteQuery, id);
		logger.info("deleteCount = " + deleteCount);
		return deleteCount;
	}

	@Override
	public int deleteGoal(int id) throws IOException {
		logger.info("id = " + id);
		int deleteCount = 0;
		String deleteQuery = "delete from t_kpi_goal where goal_id=?";
		deleteCount = jdbcTemplate.update(deleteQuery, id);
		logger.info("deleteCount = " + deleteCount);
		return deleteCount;
	}

	@Override
	public AdjustmentsDetailEntity getAdjustmentsById(int id) {
		logger.info("id = " + id);
		AdjustmentsDetailEntity adjustmentsModel = new AdjustmentsDetailEntity();
		try {
			adjustmentsModel = adjustmentsDetailRepository.findById((long) id)
					.orElseThrow(() -> new ResourceNotFoundException("ID not found: " + id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adjustmentsModel;
	}

	@Override
	public KpiGoalsEntity getGoalsById(int id) {
		logger.info("id = " + id);
		KpiGoalsEntity goalsModel = new KpiGoalsEntity();
		try {
			goalsModel = kpiGoalsRepository.findById((long) id)
					.orElseThrow(() -> new ResourceNotFoundException("ID not found: " + id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goalsModel;
	}

	@Override
	public List<KpiGoalsEntity> getGoalUploadResults(KpiGoalsEntity updateManualDataModel) {
		logger.info("updateManualDataModel = " + updateManualDataModel);

		logger.info("getCalrunid = " + updateManualDataModel.getCal_run_id());
		logger.info("getCommplanid = " + updateManualDataModel.getComm_plan_id());
		logger.info("getKpiid = " + updateManualDataModel.getKpi_id());

		int kpiid1 = 0;
		int kpiid = 0;

		if (updateManualDataModel.getKpi_id() == 0) {
			kpiid1 = 1;
			kpiid = 0;
		} else {
			kpiid1 = 0;
			kpiid = updateManualDataModel.getKpi_id();
		}

		List<KpiGoalsEntity> updateModelList = jdbcTemplate.query(GET_GOAL_UPDATE_RESULTS,
				new RowMapper<KpiGoalsEntity>() {
					@Override
					public KpiGoalsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						KpiGoalsEntity updateObj = new KpiGoalsEntity();

						updateObj.setId(rs.getLong("goal_id"));
						updateObj.setSc_emp_id(rs.getString("sc_emp_id"));
						updateObj.setCal_run_id(rs.getInt("cal_run_id"));
						updateObj.setComm_plan_id(rs.getInt("comm_plan_id"));
						updateObj.setKpi_id(rs.getInt("kpi_id"));
						updateObj.setYield_goal(rs.getFloat("yield_goal"));
						updateObj.setComm_plan_type(rs.getString("comm_plan_type"));
						updateObj.setUpload_type(rs.getString("upload_type"));
						updateObj.setKpi_name(rs.getString("kpi_name"));
						updateObj.setCommplanname(rs.getString("comm_plan"));
						updateObj.setEmpname(rs.getString("empname"));

						return updateObj;
					}
				}, new Object[] { updateManualDataModel.getCal_run_id(), updateManualDataModel.getComm_plan_id(),
						kpiid1, kpiid });
		return (List<KpiGoalsEntity>) updateModelList;
	}

	@Override
	public List<AdjustmentsDetailEntity> getAdjustmentUploadResults(AdjustmentsDetailEntity updateManualDataModel) {
		logger.info("updateManualDataModel = " + updateManualDataModel);

		logger.info("getCalrunid = " + updateManualDataModel.getCal_run_id());
		logger.info("getCommplanid = " + updateManualDataModel.getComm_plan_id());
		logger.info("getKpiid = " + updateManualDataModel.getKpi_id());

		int kpiid1 = 0;
		int kpiid = 0;

		if (updateManualDataModel.getKpi_id() == 0) {
			kpiid1 = 1;
			kpiid = 0;
		} else {
			kpiid1 = 0;
			kpiid = updateManualDataModel.getKpi_id();
		}

		List<AdjustmentsDetailEntity> updateModelList = jdbcTemplate.query(GET_ADJUSTMENT_UPDATE_RESULTS,
				new RowMapper<AdjustmentsDetailEntity>() {
					@Override
					public AdjustmentsDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						AdjustmentsDetailEntity updateObj = new AdjustmentsDetailEntity();

						updateObj.setId(rs.getLong("adjustment_id"));
						updateObj.setSc_emp_id(rs.getString("sc_emp_id"));
						updateObj.setCal_run_id(rs.getString("cal_run_id"));
						updateObj.setComm_plan_id(rs.getInt("comm_plan_id"));
						updateObj.setKpi_id(rs.getInt("kpi_id"));
						updateObj.setAdjustment(rs.getFloat("adjustment"));
						updateObj.setAdjustment_comments(rs.getString("adjustment_comments"));
						updateObj.setKpi_name(rs.getString("kpi_name"));
						updateObj.setCommplanname(rs.getString("comm_plan"));
						updateObj.setEmpname(rs.getString("empname"));

						return updateObj;
					}
				}, new Object[] { updateManualDataModel.getCal_run_id(), updateManualDataModel.getComm_plan_id(),
						kpiid1, kpiid });
		return (List<AdjustmentsDetailEntity>) updateModelList;
	}

	@Override
	public List<UpdateManualDataModel> getkpis(String type) {
		List<UpdateManualDataModel> updateList = null;

		String query = "";

		if (type.equals("Adjustments")) {
			String[] activeProfiles = environment.getActiveProfiles();
			logger.info("active profiles: " + Arrays.toString(activeProfiles));

			if (activeProfiles[0].equals("prod") || activeProfiles[0].equals("prodlocal")) {
				logger.info("Active Profile: " + activeProfiles[0]);
				query = GET_KPIS_PROD;
			} else {
				logger.info("Active Profile: " + activeProfiles[0]);
				query = GET_KPIS_UAT;
			}
		} else {
			query = GET_KPIS;
		}

		updateList = jdbcTemplate.query(query, new RowMapper<UpdateManualDataModel>() {
			@Override
			public UpdateManualDataModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				UpdateManualDataModel updateModel = new UpdateManualDataModel();

				updateModel.setKpiid(rs.getInt("kpi_id"));
				updateModel.setKpi_name(rs.getString("kpi_name"));

				return updateModel;
			}
		});
		// }, new Object[] { kpiid, effective_date });

		return updateList;
	}

	@Override
	public List<UpdateManualDataModel> getCommPlans() {
		List<UpdateManualDataModel> updateList = null;

		updateList = jdbcTemplate.query(GET_COMMPLANS, new RowMapper<UpdateManualDataModel>() {
			@Override
			public UpdateManualDataModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				UpdateManualDataModel updateModel = new UpdateManualDataModel();

				updateModel.setCommplanid(rs.getInt("comm_plan_id"));
				updateModel.setCommplanname(rs.getString("comm_plan"));

				return updateModel;
			}
		});
		// }, new Object[] { kpiid, effective_date });

		return updateList;
	}

	@Override
	public List<UpdateManualDataModel> getCalRunids() {
		List<UpdateManualDataModel> updateList = null;

		updateList = jdbcTemplate.query(GET_CALRUNIDS, new RowMapper<UpdateManualDataModel>() {
			@Override
			public UpdateManualDataModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				UpdateManualDataModel updateModel = new UpdateManualDataModel();

				updateModel.setCalrunid(rs.getInt("cal_run_id"));

				return updateModel;
			}
		});
		// }, new Object[] { kpiid, effective_date });

		return updateList;
	}

}
