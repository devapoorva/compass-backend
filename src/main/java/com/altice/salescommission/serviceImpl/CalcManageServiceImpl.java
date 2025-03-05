package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.ID_NOT_FOUND;
import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.CalcManageEntity;
import com.altice.salescommission.entity.CalcRunCtrlEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.queries.CalcManageQueries;
import com.altice.salescommission.repository.CalcManageRepository;
import com.altice.salescommission.repository.CalcRunCtrlRepository;
import com.altice.salescommission.service.CalcManageService;

@Service
@Transactional
public class CalcManageServiceImpl extends AbstractBaseRepositoryImpl<CalcManageEntity, Long>
		implements CalcManageService, CalcManageQueries {

	private static final Logger logger = LoggerFactory.getLogger(CalcManageServiceImpl.class.getName());

	@Autowired
	private CalcManageRepository calcManageRepository;

	@Autowired
	private CalcRunCtrlRepository calcRunCtrlRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	public CalcManageServiceImpl(CalcManageRepository calcManageRepository) {

		super(calcManageRepository);
	}

	@Override
	public List<CalcManageEntity> getSalesChannelDropDown() {
		List<CalcManageEntity> queriesList = jdbcTemplate.query(GET_SALES_CHANNELS, new RowMapper<CalcManageEntity>() {

			@Override
			public CalcManageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalcManageEntity calcManageEntity = new CalcManageEntity();
				calcManageEntity.setSalesChannel(rs.getString("sales_channel_desc"));
				return calcManageEntity;
			}
		});

		return queriesList;
	}

	@Override
	public List<CalcManageEntity> getCalRunIdDropDown() {
		List<CalcManageEntity> queriesList = jdbcTemplate.query(GET_CAL_RUN_IDS, new RowMapper<CalcManageEntity>() {

			@Override
			public CalcManageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalcManageEntity calcManageEntity = new CalcManageEntity();
				calcManageEntity.setCalDt(rs.getString("caldt"));
				calcManageEntity.setCalRunId(rs.getInt("cal_run_id"));
				return calcManageEntity;
			}
		});

		return queriesList;
	}

	@Override
	public List<CalcRunCtrlEntity> getRunControlByName() {
//		List<CalcRunCtrlModel> runCtrlNameList = calcRunCtrlRepository.findAllDistinctByRunCtrlId();
		List<CalcRunCtrlEntity> runCtrlNameList = jdbcTemplate.query(GET_RUN_CTRL_NAME,
				new RowMapper<CalcRunCtrlEntity>() {

					@Override
					public CalcRunCtrlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CalcRunCtrlEntity calcRunCtrlModel = new CalcRunCtrlEntity();
						calcRunCtrlModel.setRun_control_name(rs.getString("run_control_name"));
						calcRunCtrlModel.setDescription(rs.getString("description"));
//						calcRunCtrlModel.setCreated_by(rs.getString("created_by"));
//						calcRunCtrlModel.setCreated_dt(rs.getDate("created_dt"));
						calcRunCtrlModel.setRunCtrlId(rs.getInt("ccr_id"));
						calcRunCtrlModel.setSaleschannel(rs.getString("saleschannel"));
						return calcRunCtrlModel;
					}
				});

		return runCtrlNameList;
	}

	@Override
	public List<CalcRunCtrlEntity> getCalRunIdBySalesChnl() {

		List<CalcRunCtrlEntity> runCtrlNameList = namedJdbcTemplate.query(String.format(GET_CAL_RUN_IDS_BY_SC),
				new RowMapper<CalcRunCtrlEntity>() {
					@Override
					public CalcRunCtrlEntity mapRow(ResultSet resultSet, int i) throws SQLException {
						CalcRunCtrlEntity calcRunCtrlModel = new CalcRunCtrlEntity();
						calcRunCtrlModel.setCal_run_id(resultSet.getString("cal_run_id"));
						return calcRunCtrlModel;
					}
				});

//		List<CalcRunCtrlModel> runCtrlNameList = jdbcTemplate.query(String.format(GET_CAL_RUN_IDS_BY_SC),
//				new RowMapper<CalcRunCtrlModel>() {
//
//					@Override
//					public CalcRunCtrlModel mapRow(ResultSet rs, int rowNum) throws SQLException {
//						CalcRunCtrlModel calcRunCtrlModel = new CalcRunCtrlModel();
//						calcRunCtrlModel.setCal_run_id(rs.getInt("cal_run_id"));
//						return calcRunCtrlModel;
//					}
//				}, new Object[] { salesChannels });

		return runCtrlNameList;
	}

	@Override
	public List<CalcRunCtrlEntity> getCommPlans(String salesChannels) {
		List<String> schannels = Arrays.asList(salesChannels.split(",", -1));
		System.out.println("schannels = " + schannels);
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("schannelsParamName", schannels);
		System.out.println("parameters = " + parameters);
		List<CalcRunCtrlEntity> runCtrlNameList = namedJdbcTemplate.query(String.format(GET_COMM_PLANS), parameters,
				new RowMapper<CalcRunCtrlEntity>() {
					@Override
					public CalcRunCtrlEntity mapRow(ResultSet resultSet, int i) throws SQLException {
						CalcRunCtrlEntity calcRunCtrlModel = new CalcRunCtrlEntity();
						calcRunCtrlModel.setComm_plan_id(resultSet.getString("comm_plan_id"));
						calcRunCtrlModel.setCommPlan(resultSet.getString("comm_plan"));
						return calcRunCtrlModel;
					}
				});
		return runCtrlNameList;
	}

	@Override
	public List<CalcRunCtrlEntity> getEmpDataList(int commPanId) {
		List<CalcRunCtrlEntity> runCtrlNameList = jdbcTemplate.query(String.format(GET_EMP_LIST),
				new RowMapper<CalcRunCtrlEntity>() {

					@Override
					public CalcRunCtrlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CalcRunCtrlEntity calcRunCtrlModel = new CalcRunCtrlEntity();
						calcRunCtrlModel.setSc_emp_id(rs.getString("sc_emp_id"));
						calcRunCtrlModel.setEmpName(rs.getString("name"));
						return calcRunCtrlModel;
					}
				}, new Object[] { commPanId });

		return runCtrlNameList;
	}

	public List<CalcManageEntity> getCalcData(String salesChannel, String calRunId) {

		List<CalcManageEntity> queriesList = jdbcTemplate.query(GET_CALC_DATA, new RowMapper<CalcManageEntity>() {

			@Override
			public CalcManageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalcManageEntity calcManageEntity = new CalcManageEntity();
				calcManageEntity.setId(rs.getLong("ccm_id"));
				calcManageEntity.setCommPlan(rs.getString("comm_plan"));
				calcManageEntity.setCalendarType(rs.getString("calendar_type"));
				calcManageEntity.setAdjust_close_dt(rs.getDate("adjustment_close_dt"));
				calcManageEntity.setDisp_close_dt(rs.getDate("dispute_close_dt"));
				calcManageEntity.setCalc_close_flag(rs.getString("calc_close_flag"));
				calcManageEntity.setComment(rs.getString("comment"));
				calcManageEntity.setComm_plan_id(rs.getInt("comm_plan_id"));
				return calcManageEntity;
			}
		}, new Object[] { calRunId, salesChannel, calRunId });

		System.out.println("queriesList = " + queriesList);
		return queriesList;
	}

	@Override
	public CalcManageEntity updateCalc(List<CalcManageEntity> calcManageEntity) throws IdNotFoundException {
		// Get count of records based on cal run id and comm plan id
		CalcManageEntity updateOrSaveCalcStatus = null;

		logger.info("calcManageEntity = " + calcManageEntity);

		for (CalcManageEntity calEntity : calcManageEntity) {

			int cnt = calcManageRepository.getCount(calEntity.getCalRunId(), calEntity.getComm_plan_id());
			System.out.println("cnt = " + cnt);

			if (cnt == 0) {
				logger.info("SAVE");
				CalcManageEntity calcManageObj = new CalcManageEntity();
				calcManageObj.setComm_plan_id(calEntity.getComm_plan_id());
				calcManageObj.setCalRunId(calEntity.getCalRunId());
				calcManageObj.setAdjustment_close_dt(calEntity.getAdjustment_close_dt());
				calcManageObj.setDispute_close_dt(calEntity.getDispute_close_dt());
				calcManageObj.setCalc_close_flag(calEntity.getCalc_close_flag());
				calcManageObj.setComment(calEntity.getComment());
				calcManageObj.setCreated_by(calEntity.getLoggedinUser());
				calcManageObj.setCreated_dt(new Date());
				updateOrSaveCalcStatus = calcManageRepository.save(calcManageObj);
			} else {
				logger.info("UPDATE");

				logger.info("getAdjustment_close_dt = " + calEntity.getAdjustment_close_dt());
				logger.info("getDispute_close_dt = " + calEntity.getDispute_close_dt());
				logger.info("getComment = " + calEntity.getComment());

				CalcManageEntity CalcManEntObj = calcManageRepository.findById(calEntity.getId())
						.orElseThrow(() -> new IdNotFoundException(calEntity.getId() + ID_NOT_FOUND));

				if (calEntity.getAdjustment_close_dt() != null) {
					logger.info("Inside IF1");
					CalcManEntObj.setAdjustment_close_dt(calEntity.getAdjustment_close_dt());
				}
				if (calEntity.getDispute_close_dt() != null) {
					logger.info("Inside IF2");
					CalcManEntObj.setDispute_close_dt(calEntity.getDispute_close_dt());
				}
				if (calEntity.getComment() != null) {
					logger.info("Inside IF3");
					CalcManEntObj.setComment(calEntity.getComment());
				}

				CalcManEntObj.setCalc_close_flag(calEntity.getCalc_close_flag());
				CalcManEntObj.setUpdated_by(calEntity.getLoggedinUser());
				CalcManEntObj.setUpdated_dt(new Date());
				updateOrSaveCalcStatus = calcManageRepository.save(CalcManEntObj);
			}

		}
		return updateOrSaveCalcStatus;
	}

	@Override
	public CalcRunCtrlEntity saveRunCtrls(List<CalcRunCtrlEntity> calcRunCtrlModels) throws DuplicateRecordException {
		return saveRunCtrl(calcRunCtrlModels);
	}

	public CalcRunCtrlEntity saveRunCtrl(List<CalcRunCtrlEntity> calcRunCtrlModels) throws DuplicateRecordException {

		logger.info("calcRunCtrlModels = " + calcRunCtrlModels);
		logger.info("calcRunCtrlModels = " + calcRunCtrlModels.size());
		logger.info("calcRunCtrlModels = " + calcRunCtrlModels.get(0).getRun_control_name());

		String sql = "select  coalesce(max(ccr_id),0) as runCtrlId from c_calc_run_ctl ccrc ";
		int runCtrlId = jdbcTemplate.queryForObject(sql, Integer.class);

		logger.info("runCtrlId = " + runCtrlId);
		int runId = runCtrlId + 1;
		logger.info("new runCtrlId = " + runId);

		int cnt = calcRunCtrlRepository.getRunControlNameCount(calcRunCtrlModels.get(0).getRun_control_name());
		logger.info("cnt = " + cnt);
		if (cnt > 0) {
//			throw new DuplicateRecordException(
//					String.valueOf("Record " + calcRunCtrlModels.get(0).getRun_control_name() + DUPLICATE_RECORD));
			logger.info("Inside IF = " + calcRunCtrlModels.get(0).getRunCtrlId());
			runId = calcRunCtrlModels.get(0).getRunCtrlId();
		}

		CalcRunCtrlEntity responce = new CalcRunCtrlEntity();

		if (calcRunCtrlModels.size() > 1) {
			logger.info("Inside IF");
			for (CalcRunCtrlEntity calcRunCtrlModel : calcRunCtrlModels) {
				logger.info("Inside FOR = " + calcRunCtrlModel.getRunCtrlId());
				if (calcRunCtrlModel.getRunCtrlId() == 0)
					calcRunCtrlModel.setRunCtrlId(runId);
				responce = calcRunCtrlRepository.save(calcRunCtrlModel);
			}
		} else {
			logger.info("Inside ELSE");
			CalcRunCtrlEntity calcRunCtrlObj = new CalcRunCtrlEntity();
			calcRunCtrlObj.setCal_run_id(calcRunCtrlModels.get(0).getCal_run_id());
			calcRunCtrlObj.setRunCtrlId(runId);
			calcRunCtrlObj.setComm_plan_id(calcRunCtrlModels.get(0).getComm_plan_id());
			calcRunCtrlObj.setDescription(calcRunCtrlModels.get(0).getDescription());
			calcRunCtrlObj.setRun_control_name(calcRunCtrlModels.get(0).getRun_control_name());
			calcRunCtrlObj.setSaleschannel(calcRunCtrlModels.get(0).getSaleschannel());
			calcRunCtrlObj.setSc_emp_id(calcRunCtrlModels.get(0).getSc_emp_id());
			calcRunCtrlObj.setRun_all_open_flg(calcRunCtrlModels.get(0).getRun_all_open_flg());
			calcRunCtrlObj.setCreated_by(calcRunCtrlModels.get(0).getCreated_by());
			calcRunCtrlObj.setCreated_dt(calcRunCtrlModels.get(0).getCreated_dt());
			responce = calcRunCtrlRepository.save(calcRunCtrlObj);
		}

		logger.info("responce = " + responce);

		return responce;
	}

	@Override
	public CalcRunCtrlEntity updateRunCtrls(List<CalcRunCtrlEntity> calcRunCtrlModels) {
		logger.info("calcRunCtrlModels = " + calcRunCtrlModels);

		logger.info("calcRunCtrlModels getRunCtrlId = " + calcRunCtrlModels.get(0).getRunCtrlId());

		List<CalcRunCtrlEntity> calcRunCtrlModelList = calcRunCtrlRepository
				.findByRunCtrlId(calcRunCtrlModels.get(0).getRunCtrlId());

		logger.info("calcRunCtrlModelList = " + calcRunCtrlModelList);

		List<CalcRunCtrlEntity> result = compareList(calcRunCtrlModels, calcRunCtrlModelList);

		return result.get(0);
	}

	public List<CalcRunCtrlEntity> compareList(List<CalcRunCtrlEntity> calcRunCtrlModel1,
			List<CalcRunCtrlEntity> calcRunCtrlModel2) {
		// We build up a result by...
		List<CalcRunCtrlEntity> result = new ArrayList<CalcRunCtrlEntity>();
		try {
			Iterator<CalcRunCtrlEntity> iterator = calcRunCtrlModel1.iterator();
			while (iterator.hasNext()) {
				logger.info("Inside While 1");
				CalcRunCtrlEntity one = iterator.next();
				logger.info("Inside While 1 1 = " + one);
				// going through each element in the second list,
				if (!one.getRun_all_open_flg().equals("Y")) {
					logger.info("Inside IF 1");
					Iterator<CalcRunCtrlEntity> iterator2 = calcRunCtrlModel2.iterator();
					while (iterator2.hasNext()) {
						logger.info("Inside While 2");
						CalcRunCtrlEntity two = iterator2.next();
						logger.info("Inside While 2 2 = " + two);

						logger.info("Inside While 2 2 = " + one.getId());

						if (one.getId() != null) {
							logger.info("Inside IF 2");
							if (one.getId().equals(two.getId())) {

								logger.info("Inside IF 3");

								CalcRunCtrlEntity calcRunCtrlModel = calcRunCtrlRepository.findById(two.getId())
										.orElseThrow(() -> new ResourceNotFoundException(
												"Commission plan id not found" + one.getId()));
								calcRunCtrlModel.setCal_run_id(one.getCal_run_id());
								calcRunCtrlModel.setSaleschannel(one.getSaleschannel());
								calcRunCtrlModel.setRun_all_open_flg(one.getRun_all_open_flg());
								calcRunCtrlModel.setComm_plan_id(one.getComm_plan_id());
								calcRunCtrlModel.setRun_control_name(one.getRun_control_name());
								calcRunCtrlModel.setDescription(one.getDescription());
								if (one.getSc_emp_id() != null)
									calcRunCtrlModel.setSc_emp_id(one.getSc_emp_id());
								calcRunCtrlRepository.save(calcRunCtrlModel);
								iterator.remove();
								iterator2.remove();
								result.add(one);
							}
						}
						logger.info("Inside size 1 = " + calcRunCtrlModel2.size());
						if (calcRunCtrlModel2.size() == 0)
							break;
					}
					logger.info("Inside size 2 1 = " + calcRunCtrlModel2.size());
					logger.info("Inside size 2 2 = " + calcRunCtrlModel1.size());
					if (calcRunCtrlModel2.size() == 0 || calcRunCtrlModel1.size() == 0)
						break;
				}
			}
			logger.info("Inside size 2 3 = " + calcRunCtrlModel1.size());
			if (calcRunCtrlModel1.size() > 0) {
				logger.info("Inside Final IF 1");
				CalcRunCtrlEntity responce = saveRunCtrl(calcRunCtrlModel1);
				result.add(responce);
			}
			logger.info("Inside size 2 4 = " + calcRunCtrlModel2.size());
			if (calcRunCtrlModel2.size() > 0) {
				logger.info("Inside Final IF 2");
				for (CalcRunCtrlEntity calcModl2 : calcRunCtrlModel2) {
					logger.info("calcModl2.getId() = " + calcModl2.getId());
					calcRunCtrlRepository.deleteById(calcModl2.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result.size() == 0) {
			logger.info("Inside Final IF 3");
			result.add(calcRunCtrlModel1.get(0));
		}
		return result;

	}

	@Override
	public List<CalcRunCtrlEntity> getCalRunByRunId(int runctrlId) {
		logger.info("runctrlId = " + runctrlId);
		List<CalcRunCtrlEntity> runCtrlNameList = calcRunCtrlRepository.findByRunCtrlId(runctrlId);

		for (CalcRunCtrlEntity runCtrl : runCtrlNameList) {
			if (runCtrl.getComm_plan_id() != null) {
				String sql = "select concat(sales_channel_desc,' - ',comm_plan) comm_plan from   c_comm_plan_master where comm_plan_id = '"
						+ runCtrl.getComm_plan_id() + "' limit 1";
				String commPlan = jdbcTemplate.queryForObject(sql, String.class);
				runCtrl.setCommPlan(commPlan);
			}

		}

//		List<CalcRunCtrlModel> runCtrlNameList = jdbcTemplate.query(String.format(GET_CALC_RUN_DATA),
//				new RowMapper<CalcRunCtrlModel>() {
//
//					@Override
//					public CalcRunCtrlModel mapRow(ResultSet rs, int rowNum) throws SQLException {
//						CalcRunCtrlModel calcRunCtrlModel = new CalcRunCtrlModel();
//						
//						calcRunCtrlModel.setId(rs.getLong("ccrc_id"));
//						calcRunCtrlModel.setRun_control_name(rs.getString("run_control_name"));
//						calcRunCtrlModel.setDescription(rs.getString("description"));
//						calcRunCtrlModel.setCal_run_id(rs.getString("cal_run_id"));
//						calcRunCtrlModel.setSaleschannel(rs.getString("saleschannel"));
//						calcRunCtrlModel.setSc_emp_id(rs.getString("sc_emp_id"));
//						calcRunCtrlModel.setComm_plan_id(rs.getInt("comm_plan_id"));
//						calcRunCtrlModel.setRun_all_open_flg(rs.getString("run_all_open_flg"));
//						calcRunCtrlModel.setRunCtrlId(rs.getInt("ccr_id"));
//						calcRunCtrlModel.setCommPlan(rs.getString("commPlanName"));
//
//						return calcRunCtrlModel;
//					}
//				}, new Object[] { runctrlId });

		return runCtrlNameList;
	}

	@Override
	public Map<String, Boolean> deleteCommPlan(int id) {
		Map<String, Boolean> response = new HashMap<>();
		try {
			calcRunCtrlRepository.deleteById((long) id);
			response.put("deleted", Boolean.TRUE);
		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public List<CalcRunCtrlEntity> getInprogressCalRunID() {
		List<CalcRunCtrlEntity> inProgressCalRunIds = jdbcTemplate.query(String.format(GET_IP_CALRUNID),
				new RowMapper<CalcRunCtrlEntity>() {
					@Override
					public CalcRunCtrlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CalcRunCtrlEntity calcRunCtrlModel = new CalcRunCtrlEntity();
						calcRunCtrlModel.setCal_run_id(rs.getString("cal_run_id"));
						return calcRunCtrlModel;
					}

				});

		return inProgressCalRunIds;
	}
}
