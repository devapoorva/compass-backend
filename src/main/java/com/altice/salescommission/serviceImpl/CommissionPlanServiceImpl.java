package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.COMMISSION_PLAN_DATA_NOT_FOUND;
import static com.altice.salescommission.constants.ExceptionConstants.COMMISSION_PLAN_DETAIL_DATA_NOT_FOUND;
import static com.altice.salescommission.constants.ExceptionConstants.COMMISSION_PLAN_DETAIL_VALUES_DATA_NOT_FOUND;
import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.altice.salescommission.entity.CalendarEntity;
import com.altice.salescommission.entity.CommissionDetailValuesEntity;
import com.altice.salescommission.entity.CommissionPlanDetailEntity;
import com.altice.salescommission.entity.CommissionPlanEntity;
import com.altice.salescommission.entity.DependentKPIEntity;
import com.altice.salescommission.entity.DependentScoreKPIEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.model.AdjustMentResultModel;
import com.altice.salescommission.model.BossCallMappingModel;
import com.altice.salescommission.model.CommissionPlanDetailTierRangeModel;
import com.altice.salescommission.model.FreeFormModel;
import com.altice.salescommission.queries.CommissionQueries;
import com.altice.salescommission.repository.CommissionDetailValuesRepository;
import com.altice.salescommission.repository.CommissionPlanDetailsRepository;
import com.altice.salescommission.repository.CommissionPlanRepository;
import com.altice.salescommission.repository.DepndntKpiDtlsRepository;
import com.altice.salescommission.repository.DepndntScoreKpiDtlsRepository;
import com.altice.salescommission.service.CommissionPlanService;

@Transactional
@Service("CommissionPlanService")
public class CommissionPlanServiceImpl implements CommissionPlanService, CommissionQueries {

	private static final Logger logger = LoggerFactory.getLogger(CommissionPlanServiceImpl.class.getName());

	@Autowired
	private CommissionPlanRepository commissionPlanRepository;

	@Autowired(required = true)
	private CommissionPlanDetailsRepository commissionPlanDetailsRepository;

	@Autowired(required = true)
	private CommissionDetailValuesRepository commissionDetailValuesRepository;

	@Autowired
	private DepndntKpiDtlsRepository depndntKpiDtlsRepository;

	@Autowired
	private DepndntScoreKpiDtlsRepository depndntScoreKpiDtlsRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	int genertedCommPlanId = 0;

	@Override
	public CommissionPlanEntity insertCommPlan(CommissionPlanEntity compplanMasterObj)
			throws DuplicateRecordException, IdNotFoundException, ParseException {

		logger.info("insertCommPlan compplanMasterObj = " + compplanMasterObj);

		int commPlanCount = commissionPlanRepository.getComPlanCount(compplanMasterObj.getComPlan());
		logger.info("commPlanCount = " + commPlanCount);

		if (commPlanCount > 0) {
			logger.info("Inside IF");
			throw new DuplicateRecordException(
					String.valueOf("Commission Plan " + compplanMasterObj.getComPlan() + DUPLICATE_RECORD));
		} else {
			logger.info("Inside ELSE");
			// CommissionPlanEntity insertCommPlanMasterModel = compplanMasterObj;
			CommissionPlanEntity addCommPlan = new CommissionPlanEntity();

			addCommPlan.setCommPlanId(commissionPlanRepository.getMaxId() + 1);
			addCommPlan.setComPlan(compplanMasterObj.getComPlan());
			addCommPlan.setCommPlan(compplanMasterObj.getCommPlan());
			addCommPlan.setSales_channel(compplanMasterObj.getSales_channel_desc());
			addCommPlan.setSales_channel_desc(compplanMasterObj.getSales_channel_desc());
			addCommPlan.setLanguage(compplanMasterObj.getLanguage());
			addCommPlan.setCalendar_type(compplanMasterObj.getCalendar_type());
			addCommPlan.setDisplay_flag(compplanMasterObj.getDisplay_flag());
			addCommPlan.setEffective_date(compplanMasterObj.getEffective_date());
			addCommPlan.setComm_pool(compplanMasterObj.getComm_pool());
			addCommPlan.setCharge_back_type(compplanMasterObj.getCharge_back_type());
			addCommPlan.setCharge_back(compplanMasterObj.getCharge_back());
			addCommPlan.setComm_plan_priority(compplanMasterObj.getComm_plan_priority());
			addCommPlan.setCharge_back_days(compplanMasterObj.getCharge_back_days());
			addCommPlan.setNon_pay_charge_back_days(compplanMasterObj.getNon_pay_charge_back_days());
			addCommPlan.setCommercial_flag("N");
			addCommPlan.setActive_flag(compplanMasterObj.getActive_flag());
			addCommPlan.setCreated_dt(new Date());
			addCommPlan.setCreated_by(compplanMasterObj.getCreated_by());

			addCommPlan = commissionPlanRepository.save(addCommPlan);

			return addCommPlan;
		}
	}

	private CommissionPlanEntity saveCommissionPlanMaster(CommissionPlanEntity compplanMasterObj, int maxCommPlanId)
			throws IdNotFoundException {

		logger.info("saveCommissionPlanMaster compplanMasterObj = " + compplanMasterObj);
		logger.info("saveCommissionPlanMaster maxCommPlanId = " + maxCommPlanId);

		CommissionPlanEntity saveCommPlanMasterModel = null;

		CommissionPlanEntity commissionPlanModel = commissionPlanRepository
				.findAllByCommPlanId(compplanMasterObj.getId());

		if (commissionPlanModel != null) {
			CommissionPlanEntity newCommPlan = new CommissionPlanEntity();

			newCommPlan.setCommPlanId(maxCommPlanId);
			newCommPlan.setCreated_dt(new Date());
			newCommPlan.setCommercial_flag("N");
			newCommPlan.setCreated_by(compplanMasterObj.getCreated_by());
			newCommPlan.setEffective_date(compplanMasterObj.getEffective_date());
			newCommPlan.setComPlan(compplanMasterObj.getComPlan());
			newCommPlan.setCommPlan(compplanMasterObj.getCommPlan());
			newCommPlan.setSales_channel(compplanMasterObj.getSales_channel());
			newCommPlan.setSales_channel_desc(compplanMasterObj.getSales_channel_desc());
			newCommPlan.setActive_flag(compplanMasterObj.getActive_flag());
			newCommPlan.setLanguage(compplanMasterObj.getLanguage());
			newCommPlan.setCalendar_type(compplanMasterObj.getCalendar_type());
			newCommPlan.setDisplay_flag(compplanMasterObj.getDisplay_flag());
			newCommPlan.setComm_pool(compplanMasterObj.getComm_pool());
			newCommPlan.setCharge_back_type(compplanMasterObj.getCharge_back_type());
			newCommPlan.setCharge_back(compplanMasterObj.getCharge_back());
			newCommPlan.setCharge_back_days(compplanMasterObj.getCharge_back_days());
			newCommPlan.setNon_pay_charge_back_days(compplanMasterObj.getNon_pay_charge_back_days());
			newCommPlan.setComm_plan_priority(compplanMasterObj.getComm_plan_priority());
			newCommPlan.setComm_plan_type(compplanMasterObj.getComm_plan_type());
			newCommPlan.setWinbackpool_flag(compplanMasterObj.getWinbackpool_flag());
			newCommPlan.setDay_multiplier(compplanMasterObj.getDay_multiplier());
			newCommPlan.setShow_perf(compplanMasterObj.getShow_perf());

			saveCommPlanMasterModel = commissionPlanRepository.save(newCommPlan);
			genertedCommPlanId = saveCommPlanMasterModel.getId().intValue();

			logger.info("genertedCommPlanId = " + genertedCommPlanId);
			logger.info("saveCommPlanMasterModel Comm Plan Master Data Inserted");
		} else {
			throw new IdNotFoundException(compplanMasterObj.getCommPlanId() + COMMISSION_PLAN_DATA_NOT_FOUND);
		}
		return saveCommPlanMasterModel;
	}

	@Override
	public CommissionPlanEntity replicateCommPlan(CommissionPlanEntity compplanMasterObj)
			throws DuplicateRecordException, IdNotFoundException, ParseException {

		logger.info("replicateCommPlan compplanMasterObj = " + compplanMasterObj);

		CommissionPlanEntity saveCommPlanMasterModel = null;
		CommissionPlanDetailEntity saveCommPlanDetailModel = null;
		CommissionDetailValuesEntity saveCommPlanDtlValuesModel = null;
		DependentKPIEntity saveDependKpiModel = null;
		DependentScoreKPIEntity saveDependScoreKpiModel = null;

		int maxCommPlanId = 0;
		logger.info("compplanMasterObj Status = " + compplanMasterObj.getStatus());

		if (compplanMasterObj.getStatus().equals("replicate")) {
			logger.info("Inside IF");
			maxCommPlanId = commissionPlanRepository.getMaxId() + 1;
		} else {
			logger.info("Inside ELSE");
			maxCommPlanId = compplanMasterObj.getCommPlanId();
		}

		String val1 = "";
		String val2 = "";

		saveCommPlanMasterModel = saveCommissionPlanMaster(compplanMasterObj, maxCommPlanId);
		logger.info("saveCommPlanMasterModel = " + saveCommPlanMasterModel);

		if (saveCommPlanMasterModel != null) {
			logger.info("Inside IF");
			List<CommissionPlanDetailEntity> commissionPlanDetailModel = commissionPlanDetailsRepository
					.findAllByCommPlanId(compplanMasterObj.getId().intValue());
			logger.info("commissionPlanDetailModel = " + commissionPlanDetailModel);

			if (commissionPlanDetailModel != null) {

				for (CommissionPlanDetailEntity commissionPlaDetailnModel : commissionPlanDetailModel) {
					val1 = val1 + commissionPlaDetailnModel.getId() + "~";
					// logger.info("commissionPlaDetailnModel val1 = " + val1);

					CommissionPlanDetailEntity commPlanDtl = new CommissionPlanDetailEntity();
					commPlanDtl.setCommPlanId(maxCommPlanId);
					commPlanDtl.setCommplan_rowid(genertedCommPlanId);
					commPlanDtl.setKpi_id(commissionPlaDetailnModel.getKpi_id());
					commPlanDtl.setKpi_weightage(commissionPlaDetailnModel.getKpi_weightage());
					commPlanDtl.setDisplay_flg(commissionPlaDetailnModel.getDisplay_flg());
					commPlanDtl.setCreated_by(compplanMasterObj.getCreated_by());
					commPlanDtl.setCreated_dt(new Date());
					commPlanDtl.setDisplay_name(commissionPlaDetailnModel.getDisplay_name());
					commPlanDtl.setMeasure_type(commissionPlaDetailnModel.getMeasure_type());
					commPlanDtl.setEffective_date(compplanMasterObj.getEffective_date());
					commPlanDtl.setViewOrder(commissionPlaDetailnModel.getViewOrder());
					commPlanDtl.setKpi_type(commissionPlaDetailnModel.getKpi_type());
					commPlanDtl.setCharge_back(commissionPlaDetailnModel.getCharge_back());
					commPlanDtl.setCharge_back_days(commissionPlaDetailnModel.getCharge_back_days());
					commPlanDtl.setNon_charge_back_days(commissionPlaDetailnModel.getNon_charge_back_days());
					commPlanDtl.setKpi_goal(commissionPlaDetailnModel.getKpi_goal());
					commPlanDtl.setMeasure_against(commissionPlaDetailnModel.getMeasure_against());
					commPlanDtl.setIs_percentage(commissionPlaDetailnModel.getIs_percentage());
					commPlanDtl.setCalculate_score(commissionPlaDetailnModel.getCalculate_score());
					commPlanDtl.setIs_pooled(commissionPlaDetailnModel.getIs_pooled());

					commPlanDtl.setIs_calc_vars(commissionPlaDetailnModel.getIs_calc_vars());
					commPlanDtl.setIs_score_vars(commissionPlaDetailnModel.getIs_score_vars());
					commPlanDtl.setCalc_vars_revenue(commissionPlaDetailnModel.getCalc_vars_revenue());
					commPlanDtl.setCalc_vars_score(commissionPlaDetailnModel.getCalc_vars_score());
					commPlanDtl.setCalc_vars_count(commissionPlaDetailnModel.getCalc_vars_count());
					commPlanDtl.setCommission_payout(commissionPlaDetailnModel.getCommission_payout());

					saveCommPlanDetailModel = commissionPlanDetailsRepository.save(commPlanDtl);

					val2 = val2 + saveCommPlanDetailModel.getId() + "~";
					// logger.info("saveCommPlanDetailModel val2 = " + val2);

				}

				logger.info("commissionPlaDetailnModel val1 = " + val1);
				logger.info("saveCommPlanDetailModel val2 = " + val2);

				if (saveCommPlanDetailModel != null) {
					logger.info("saveCommPlanDetailModel Comm Plan Detail Inserted");

					// logger.info("compplanMasterObj.getCommPlanId() = " +
					// compplanMasterObj.getCommPlanId());

					List<CommissionDetailValuesEntity> commissionDetailValuesModel = commissionDetailValuesRepository
							.getCommPlanIdValues(compplanMasterObj.getCommPlanId());
					logger.info("commissionDetailValuesModel = " + commissionDetailValuesModel);

					if (commissionDetailValuesModel != null) {
						logger.info("Inside IF...");

						for (CommissionDetailValuesEntity commnDetailValuesModel : commissionDetailValuesModel) {
							CommissionDetailValuesEntity commPlanDtlValues = new CommissionDetailValuesEntity();

							String val1_str[] = val1.split("~");
							String val2_str[] = val2.split("~");

							for (int i = 0; i < val1_str.length; i++) {
								if (Integer.parseInt(val1_str[i]) == commnDetailValuesModel.getCommPlanDtlId()) {
									commPlanDtlValues.setCommPlanDtlId(Integer.parseInt(val2_str[i]));

									commPlanDtlValues.setCreated_by(compplanMasterObj.getCreated_by());
									commPlanDtlValues.setCreated_dt(new Date());
									commPlanDtlValues.setComm_plan_id(maxCommPlanId);
									commPlanDtlValues.setKpi_id(commnDetailValuesModel.getKpi_id());
									commPlanDtlValues.setRangeLow(commnDetailValuesModel.getRangeLow());
									commPlanDtlValues.setRange_high(commnDetailValuesModel.getRange_high());
									commPlanDtlValues.setTier(commnDetailValuesModel.getTier());
									commPlanDtlValues.setModifier_percent(commnDetailValuesModel.getModifier_percent());
									commPlanDtlValues.setCommission_val(commnDetailValuesModel.getCommission_val());
									commPlanDtlValues.setTarget(commnDetailValuesModel.getTarget());

									saveCommPlanDtlValuesModel = commissionDetailValuesRepository
											.save(commPlanDtlValues);
								}

							}

							// commPlanDtlValues.setCommPlanDtlId(commnDetailValuesModel.getCommPlanDtlId());

						}

						if (saveCommPlanDtlValuesModel != null) {
							logger.info("saveCommPlanDtlValuesModel Comm Plan Detail Values Inserted");

							List<DependentKPIEntity> commDependKPIModel = depndntKpiDtlsRepository
									.findAllByCommPlanDependKpiId(compplanMasterObj.getCommPlanId());
							logger.info("commDependKPIModel = " + commDependKPIModel);

							if (commDependKPIModel != null) {

								for (DependentKPIEntity dependentKPIModel : commDependKPIModel) {
									DependentKPIEntity commPlanDpndKpi = new DependentKPIEntity();

									String val1_str[] = val1.split("~");
									String val2_str[] = val2.split("~");

									for (int i = 0; i < val1_str.length; i++) {
										if (Integer.parseInt(val1_str[i]) == dependentKPIModel.getCommPlanDtlId()) {
											commPlanDpndKpi.setCommPlanDtlId(Integer.parseInt(val2_str[i]));

											commPlanDpndKpi.setComp_plan_id(maxCommPlanId);
											commPlanDpndKpi.setComplex_kpi_id(dependentKPIModel.getComplex_kpi_id());
											commPlanDpndKpi.setKpi_id(dependentKPIModel.getKpi_id());
											commPlanDpndKpi.setKpi_name(dependentKPIModel.getKpi_name());
											commPlanDpndKpi.setKpi_weight(dependentKPIModel.getKpi_weight());
											commPlanDpndKpi.setKpi_goal(dependentKPIModel.getKpi_goal());
											commPlanDpndKpi.setCreated_by(compplanMasterObj.getCreated_by());
											commPlanDpndKpi.setCreated_dt(new Date());

											commPlanDpndKpi.setOperation(dependentKPIModel.getOperation());
											commPlanDpndKpi.setKpi_order(dependentKPIModel.getKpi_order());

											saveDependKpiModel = depndntKpiDtlsRepository.save(commPlanDpndKpi);
										}

									}

								}
								if (saveDependKpiModel != null) {
									logger.info("saveDependKpiModel Comm Plan Depend KPI Inserted");

									List<DependentScoreKPIEntity> commDependScoreKPIModel = depndntScoreKpiDtlsRepository
											.findAllByCommPlanDependKpiScoreId(compplanMasterObj.getCommPlanId());
									logger.info("commDependScoreKPIModel = " + commDependScoreKPIModel);

									if (commDependScoreKPIModel != null) {

										for (DependentScoreKPIEntity dependentScoreKPIModel : commDependScoreKPIModel) {
											DependentScoreKPIEntity commPlanDependScoreKpi = new DependentScoreKPIEntity();

											String val1_str[] = val1.split("~");
											String val2_str[] = val2.split("~");

											for (int i = 0; i < val1_str.length; i++) {
												if (Integer.parseInt(val1_str[i]) == dependentScoreKPIModel
														.getCommPlanDtlId()) {
													commPlanDependScoreKpi
															.setCommPlanDtlId(Integer.parseInt(val2_str[i]));
													commPlanDependScoreKpi.setComp_plan_id(maxCommPlanId);
													commPlanDependScoreKpi.setComplex_kpi_id(
															dependentScoreKPIModel.getComplex_kpi_id());
													commPlanDependScoreKpi
															.setKpi_id(dependentScoreKPIModel.getKpi_id());
													commPlanDependScoreKpi
															.setKpi_name(dependentScoreKPIModel.getKpi_name());
													commPlanDependScoreKpi
															.setKpi_weight(dependentScoreKPIModel.getKpi_weight());
													commPlanDependScoreKpi
															.setCreated_by(compplanMasterObj.getCreated_by());
													commPlanDependScoreKpi.setCreated_dt(new Date());

													saveDependScoreKpiModel = depndntScoreKpiDtlsRepository
															.save(commPlanDependScoreKpi);
												}

											}

										}

										if (saveDependScoreKpiModel != null) {
											logger.info("saveDependScoreKpiModel Comm Plan Depend Score KPI Inserted");
										} else {
											// throw new IdNotFoundException(compplanMasterObj.getCommPlanId()
											// + " commission plan dependent score kpi data not inserted");
										}

									} else {
										// throw new IdNotFoundException(compplanMasterObj.getCommPlanId()
										// + COMMISSION_PLAN_DEPENDENT_SCORE_KPI_DATA_NOT_FOUND);
									}

								} else {
									// throw new IdNotFoundException(compplanMasterObj.getCommPlanId()
									// + " commission plan dependent kpi data not inserted");
								}

							} else {
								// throw new IdNotFoundException(compplanMasterObj.getCommPlanId()
								// + COMMISSION_PLAN_DEPENDENT_KPI_DATA_NOT_FOUND);
							}

						}

//						else {
//							throw new IdNotFoundException(compplanMasterObj.getCommPlanId()
//									+ " commission plan detail values data not inserted");
//						}

					}

//					else {
//						throw new IdNotFoundException(
//								compplanMasterObj.getCommPlanId() + COMMISSION_PLAN_DETAIL_VALUES_DATA_NOT_FOUND);
//					}

				}
//				else {
//					throw new IdNotFoundException(
//							compplanMasterObj.getCommPlanId() + " commission plan detail data not inserted");
//				}

			}

//			else {
//				throw new IdNotFoundException(
//						compplanMasterObj.getCommPlanId() + COMMISSION_PLAN_DETAIL_DATA_NOT_FOUND);
//			}

			// } else {
			// throw new IdNotFoundException(
			// compplanMasterObj.getCommPlanId() + " commission plan master data not
			// inserted");
			// }
		} else {
			throw new IdNotFoundException(compplanMasterObj.getCommPlanId() + COMMISSION_PLAN_DATA_NOT_FOUND);
		}

		logger.info("genertedCommPlanId = " + genertedCommPlanId);
		saveCommPlanMasterModel.setId((long) genertedCommPlanId);

		logger.info("saveCommPlanMasterModel = " + saveCommPlanMasterModel);
		return saveCommPlanMasterModel;
	}

	@Override
	public CommissionPlanDetailEntity addKpitoCommPlan(
			List<CommissionPlanDetailTierRangeModel> commissionPlanDetailModels)
			throws ParseException, DuplicateRecordException {

		logger.info("addKpitoCommPlan commissionPlanDetailModels: " + commissionPlanDetailModels);

		List<String> KpiNameList = new ArrayList<>();
		List<CommissionDetailValuesEntity> commDtlTierRangeList = new ArrayList<CommissionDetailValuesEntity>();
		CommissionPlanDetailEntity saveCommPlanDetails = new CommissionPlanDetailEntity();

		int i = 0;
		int cid = 0;
		int kid = 0;
		int commPlanDtlId = 0;

		for (CommissionPlanDetailTierRangeModel commissionPlaDetailnModel : commissionPlanDetailModels) {
			i++;
			CommissionPlanDetailEntity commPlanDtl = new CommissionPlanDetailEntity();
			CommissionDetailValuesEntity commDtlValues = new CommissionDetailValuesEntity();

			if (i == 1) {
				cid = commissionPlaDetailnModel.getComm_plan_id();
				kid = commissionPlaDetailnModel.getKpi_id();
			}

			logger.info("cid = " + cid);
			logger.info("kid = " + kid);
			logger.info("getEffective_date = " + commissionPlaDetailnModel.getEffective_date());

			if (!commissionPlaDetailnModel.getKpi_name().isEmpty() && commissionPlaDetailnModel.getKpi_name() != null
					&& !KpiNameList.contains(commissionPlaDetailnModel.getKpi_name())) {

				commPlanDtl.setKpi_name(commissionPlaDetailnModel.getKpi_name());
				commPlanDtl.setKpi_type(commissionPlaDetailnModel.getKpi_type());
				commPlanDtl.setKpi_weightage(commissionPlaDetailnModel.getKpi_weightage());
				commPlanDtl.setDisplay_flg(commissionPlaDetailnModel.getDisplay_flg());
				commPlanDtl.setDisplay_name(commissionPlaDetailnModel.getDisplay_name());
				commPlanDtl.setViewOrder(commissionPlaDetailnModel.getViewOrder());
				commPlanDtl.setMeasure_type(commissionPlaDetailnModel.getMeasure_type());
				commPlanDtl.setKpi_type(commissionPlaDetailnModel.getKpi_type());
				commPlanDtl.setKpi_id(commissionPlaDetailnModel.getKpi_id());
				commPlanDtl.setCommPlanId(commissionPlaDetailnModel.getComm_plan_id());
				commPlanDtl.setComm_plan(commissionPlaDetailnModel.getComm_plan());
				commPlanDtl.setCreated_dt(commissionPlaDetailnModel.getCreated_dt());
				commPlanDtl.setCreated_by(commissionPlaDetailnModel.getCreated_by());
				commPlanDtl.setKpi_goal(commissionPlaDetailnModel.getKpi_goal());
				commPlanDtl.setCharge_back(commissionPlaDetailnModel.getCharge_back());
				commPlanDtl.setCharge_back_days(commissionPlaDetailnModel.getCharge_back_days());
				commPlanDtl.setNon_charge_back_days(commissionPlaDetailnModel.getNon_charge_back_days());
				commPlanDtl.setMeasure_against(commissionPlaDetailnModel.getMeasure_against());
				commPlanDtl.setIs_percentage(commissionPlaDetailnModel.getIs_percentage());
				commPlanDtl.setIs_pooled(commissionPlaDetailnModel.getIs_pooled());
				commPlanDtl.setCalculate_score(commissionPlaDetailnModel.getCalculate_score());
				commPlanDtl.setCommission_payout(commissionPlaDetailnModel.getCommission_payout());
				commPlanDtl.setCalc_vars_score(commissionPlaDetailnModel.getCalc_vars_score());
				commPlanDtl.setCalc_vars_count(commissionPlaDetailnModel.getCalc_vars_count());
				commPlanDtl.setCalc_vars_revenue(commissionPlaDetailnModel.getCalc_vars_revenue());
				commPlanDtl.setIs_calc_vars(commissionPlaDetailnModel.getIs_calc_vars());
				commPlanDtl.setIs_score_vars(commissionPlaDetailnModel.getIs_score_vars());
				commPlanDtl.setCommplan_rowid(commissionPlaDetailnModel.getId());

				if (commissionPlaDetailnModel.getEffective_date() != null)
					commPlanDtl.setEffective_date(commissionPlaDetailnModel.getEffective_date());
				KpiNameList.add(commissionPlaDetailnModel.getKpi_name());

				saveCommPlanDetails = commissionPlanDetailsRepository.save(commPlanDtl);
				commPlanDtlId = saveCommPlanDetails.getId().intValue();

			}

			commDtlValues.setId(null);
			commDtlValues.setComm_plan_id(commissionPlaDetailnModel.getComm_plan_id());
			commDtlValues.setKpi_id(commissionPlaDetailnModel.getKpi_id());
			commDtlValues.setRangeLow(commissionPlaDetailnModel.getRangeLow());
			commDtlValues.setRange_high(commissionPlaDetailnModel.getRange_high());
			commDtlValues.setModifier_percent(commissionPlaDetailnModel.getModifier_percent());
			commDtlValues.setCommission_val(commissionPlaDetailnModel.getCommission_val());
			commDtlValues.setTier(commissionPlaDetailnModel.getTier());
			commDtlValues.setCreated_dt(commissionPlaDetailnModel.getCreated_dt());
			commDtlValues.setCreated_by(commissionPlaDetailnModel.getCreated_by());
			commDtlValues.setTarget(commissionPlaDetailnModel.getTarget());
			commDtlValues.setCommPlanDtlId(commPlanDtlId);
			commissionDetailValuesRepository.save(commDtlValues);
			commDtlTierRangeList.add(commDtlValues);
		}

		logger.info("saveCommPlanDetails = " + saveCommPlanDetails);
		return saveCommPlanDetails;
	}

//	@Override
//	public CommissionPlanEntity addCommPlan(CommissionPlanEntity commissionPlanModel)
//			throws DuplicateRecordException, IdNotFoundException, ParseException {
//
//		logger.info("AddCommPlan = " + commissionPlanModel);
//
//		CommissionPlanEntity newCommPlan = new CommissionPlanEntity();
//		if (commissionPlanModel.getCommPlanId() == 0) {
//			String sql = "select max(comm_plan_id) from c_comm_plan_master ccpm ";
//			int commPlanId = jdbcTemplate.queryForObject(sql, Integer.class);
//			int comPlnId = commPlanId + 1;
//			commissionPlanModel.setCommPlanId(comPlnId);
//		}
//
//		CommissionPlanEntity saveCommPlan = null;
//
//		newCommPlan.setCommPlanId(commissionPlanModel.getCommPlanId());
//		newCommPlan.setComm_plan_type(commissionPlanModel.getComm_plan_type());
//		newCommPlan.setComPlan(commissionPlanModel.getComPlan());
//		newCommPlan.setSales_channel_desc(commissionPlanModel.getSales_channel_desc());
//		newCommPlan.setSales_channel(commissionPlanModel.getSales_channel());
//		newCommPlan.setActive_flag(commissionPlanModel.getActive_flag());
//		newCommPlan.setWinbackpool_flag(commissionPlanModel.getWinbackpool_flag());
//		newCommPlan.setDay_multiplier(commissionPlanModel.getDay_multiplier());
//		newCommPlan.setShow_perf(commissionPlanModel.getShow_perf());
//		newCommPlan.setDisplay_flag(commissionPlanModel.getDisplay_flag());
//		newCommPlan.setLanguage(commissionPlanModel.getLanguage());
//		newCommPlan.setCommercial_flag("N");
//		newCommPlan.setCommPlan(commissionPlanModel.getComPlan());
//		newCommPlan.setCreated_by(commissionPlanModel.getCreated_by());
//		newCommPlan.setCreated_dt(new Date());
//		newCommPlan.setEffective_date(commissionPlanModel.getEffective_date());
//		newCommPlan.setCalendar_type(commissionPlanModel.getCalendar_type());
//
//		newCommPlan.setCharge_back(commissionPlanModel.getCharge_back());
//		newCommPlan.setCharge_back_days(commissionPlanModel.getCharge_back_days());
//		newCommPlan.setNon_pay_charge_back_days(commissionPlanModel.getNon_pay_charge_back_days());
//		newCommPlan.setComm_plan_priority(commissionPlanModel.getComm_plan_priority());
//		newCommPlan.setComm_pool(commissionPlanModel.getComm_pool());
//		newCommPlan.setCharge_back_type(commissionPlanModel.getCharge_back_type());
//
//		saveCommPlan = commissionPlanRepository.save(newCommPlan);
//		genertedCommPlanId = saveCommPlan.getId().intValue();
//
//		commissionPlanModel.setStatus(commissionPlanModel.getStatus());
//		this.replicateCommPlan(commissionPlanModel);
//
//		saveCommPlan.setId((long) genertedCommPlanId);
//		return saveCommPlan;
//	}

	public CommissionPlanEntity updateCommPlan(List<CommissionPlanEntity> commissionPlanModels) {
		CommissionPlanEntity updateCommPlan = null;

		logger.info("updateCommPlan commissionPlanModels = " + commissionPlanModels);

		int commPlanId = 0;
		String activeStatus = "";

		for (CommissionPlanEntity commissionPlanModel : commissionPlanModels) {

			logger.info("getId = " + commissionPlanModel.getId());

			commPlanId = commissionPlanModel.getCommPlanId();
			activeStatus = commissionPlanModel.getActive_flag();

			CommissionPlanEntity commPlan = commissionPlanRepository.findById(commissionPlanModel.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Commission plan id not found" + commissionPlanModel.getId()));
			commPlan.setComm_plan_type(commissionPlanModel.getComm_plan_type());
			commPlan.setSales_channel(commissionPlanModel.getSales_channel_desc());
			commPlan.setSales_channel_desc(commissionPlanModel.getSales_channel_desc());
			commPlan.setComPlan(commissionPlanModel.getComPlan());
			commPlan.setCommPlan(commissionPlanModel.getCommPlan());
			commPlan.setActive_flag(commissionPlanModel.getActive_flag());
			commPlan.setWinbackpool_flag(commissionPlanModel.getWinbackpool_flag());
			commPlan.setDay_multiplier(commissionPlanModel.getDay_multiplier());
			commPlan.setShow_perf(commissionPlanModel.getShow_perf());
			commPlan.setLanguage(commissionPlanModel.getLanguage());
			commPlan.setDisplay_flag(commissionPlanModel.getDisplay_flag());
			commPlan.setEffective_date(commissionPlanModel.getEffective_date());
			commPlan.setCommercial_flag("N");
			commPlan.setUpdate_dt(commissionPlanModel.getUpdate_dt());
			commPlan.setUpdated_by(commissionPlanModel.getUpdated_by());
			commPlan.setCalendar_type(commissionPlanModel.getCalendar_type());
			commPlan.setComm_pool(commissionPlanModel.getComm_pool());
			commPlan.setCharge_back(commissionPlanModel.getCharge_back());
			commPlan.setCharge_back_days(commissionPlanModel.getCharge_back_days());
			commPlan.setNon_pay_charge_back_days(commissionPlanModel.getNon_pay_charge_back_days());
			commPlan.setComm_plan_priority(commissionPlanModel.getComm_plan_priority());
			commPlan.setCharge_back_type(commissionPlanModel.getCharge_back_type());

			updateCommPlan = commissionPlanRepository.save(commPlan);
		}

		logger.info("activeStatus = " + activeStatus);
		logger.info("commPlanId = " + commPlanId);

		int count = jdbcTemplate.update("UPDATE c_comm_plan_master set active_flag='" + activeStatus
				+ "' where comm_plan_id='" + commPlanId + "'");
		logger.info("count = " + count);

		return updateCommPlan;
	}

	@Override
	public List<CommissionPlanEntity> getCommPlans() {
		logger.info("getCommPlans called");
		// return commissionPlanRepository.findAll(Sort.by("comPlan"));

		try {
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			// Date date = new Date();

			List<CommissionPlanEntity> commPlanMdls = jdbcTemplate.query(GET_ALL_COMM_PLAN_BY_DATE,
					new RowMapper<CommissionPlanEntity>() {

						@Override
						public CommissionPlanEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
							CommissionPlanEntity commPlanMdl = new CommissionPlanEntity();
							// System.out.println("comm_plan_id = " + rs.getInt("comm_plan_id"));

							commPlanMdl.setId(rs.getLong("row_id"));
							commPlanMdl.setCommPlanId(rs.getInt("comm_plan_id"));
							commPlanMdl.setComm_plan_type(rs.getString("comm_plan_type"));
							commPlanMdl.setComPlan(rs.getString("comm_plan"));
							commPlanMdl.setSales_channel_desc(rs.getString("sales_channel_desc"));
							commPlanMdl.setSales_channel(rs.getString("sales_channel"));
							commPlanMdl.setActive_flag(rs.getString("active_flag"));
							commPlanMdl.setWinbackpool_flag(rs.getString("winbackpool_flag"));
							commPlanMdl.setDay_multiplier(rs.getInt("day_multiplier"));
							commPlanMdl.setShow_perf(rs.getString("show_perf"));
							commPlanMdl.setDisplay_flag(rs.getString("display_flag"));
							commPlanMdl.setLanguage(rs.getString("language"));
							commPlanMdl.setCommercial_flag(rs.getString("commercial_flag"));
							commPlanMdl.setCommPlan(rs.getString("comm_plan_desc"));
							commPlanMdl.setCreated_by(rs.getString("created_by"));
							commPlanMdl.setCreated_dt(rs.getDate("created_dt"));
							commPlanMdl.setEffective_date(rs.getDate("effective_date"));
							commPlanMdl.setEDate(rs.getString("edate"));
							commPlanMdl.setCalendar_type(rs.getString("calendar_type"));
							commPlanMdl.setComm_pool(rs.getFloat("comm_pool"));
							commPlanMdl.setCharge_back(rs.getString("charge_back"));
							commPlanMdl.setCharge_back_days(rs.getInt("charge_back_days"));
							commPlanMdl.setNon_pay_charge_back_days(rs.getInt("non_pay_charge_back_days"));
							commPlanMdl.setComm_plan_priority(rs.getInt("comm_plan_priority"));
							commPlanMdl.setComm_img_str(rs.getString("commimg"));
							// System.out.println("comm_img = " + rs.getString("commimg"));

							commPlanMdl.setCharge_back_type(rs.getString("charge_back_type"));

							return commPlanMdl;
						}
					});
			// logger.info("commPlanMdls = "+commPlanMdls);
			return commPlanMdls;
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}

	@Override
	public List<CommissionPlanDetailEntity> getKpiPlans(int commPlanId, String effDate) {
		logger.info("getKpiPlans = commPlanId " + commPlanId);
		logger.info("getKpiPlans effDate = " + effDate);
		try {

			// logger.info("commPlanId=" + commPlanId);
			// logger.info("effDate=" + effDate);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date edate = formatter.parse(effDate);
			logger.info("date=" + edate);

			List<CommissionPlanDetailEntity> comPlanList = jdbcTemplate.query(GET_COMM_PLAN_DETAILS,
					new RowMapper<CommissionPlanDetailEntity>() {

						@Override
						public CommissionPlanDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
							CommissionPlanDetailEntity commissionPlanDetailModel = new CommissionPlanDetailEntity();
							commissionPlanDetailModel.setId(rs.getLong("comm_plan_dtl_id"));
							commissionPlanDetailModel.setComm_plan(rs.getString("comm_plan"));
							commissionPlanDetailModel.setKpi_name(rs.getString("kpi_name"));
							commissionPlanDetailModel.setKpi_type(rs.getString("kpi_type"));
							commissionPlanDetailModel.setKpi_weightage(rs.getInt("kpi_weightage"));
							commissionPlanDetailModel.setDisplay_flg(rs.getString("display_flg"));
							commissionPlanDetailModel.setDisplay_name(rs.getString("display_name"));
							commissionPlanDetailModel.setCommPlanId(rs.getInt("comm_plan_id"));
							commissionPlanDetailModel.setComm_pool(rs.getFloat("comm_pool"));
							commissionPlanDetailModel.setMeasure_type(rs.getString("measure_type"));
							commissionPlanDetailModel.setKpi_id(rs.getInt("kpi_id"));
							commissionPlanDetailModel.setViewOrder(rs.getInt("view_order"));
							commissionPlanDetailModel.setRangeLow(rs.getFloat("range_low"));
							commissionPlanDetailModel.setRange_high(rs.getFloat("range_high"));
							commissionPlanDetailModel.setModifier_percent(rs.getFloat("modifier_percent"));
							commissionPlanDetailModel.setCommission_val(rs.getFloat("commission_val"));
							commissionPlanDetailModel.setTarget(rs.getFloat("target"));
							commissionPlanDetailModel.setTier(rs.getString("tier"));
							commissionPlanDetailModel.setKpi_goal(rs.getInt("kpi_goal"));
							commissionPlanDetailModel.setEffective_date(rs.getDate("effective_date"));

							commissionPlanDetailModel.setCharge_back(rs.getString("charge_back"));
							commissionPlanDetailModel.setCharge_back_days(rs.getInt("charge_back_days"));
							commissionPlanDetailModel.setNon_charge_back_days(rs.getInt("non_charge_back_days"));

							commissionPlanDetailModel.setMeasure_against(rs.getString("measure_against"));
							commissionPlanDetailModel.setIs_percentage(rs.getString("is_percentage"));
							commissionPlanDetailModel.setCalculate_score(rs.getString("calculate_score"));
							commissionPlanDetailModel.setIs_pooled(rs.getString("is_pooled"));

							commissionPlanDetailModel.setCommission_payout(rs.getString("commission_payout"));
							commissionPlanDetailModel.setCalc_vars_score(rs.getString("calc_vars_score"));
							commissionPlanDetailModel.setCalc_vars_count(rs.getString("calc_vars_count"));
							commissionPlanDetailModel.setCalc_vars_revenue(rs.getString("calc_vars_revenue"));
							commissionPlanDetailModel.setIs_calc_vars(rs.getString("is_calc_vars"));
							commissionPlanDetailModel.setIs_score_vars(rs.getString("is_score_vars"));
							commissionPlanDetailModel.setCommplan_rowid(rs.getInt("commplan_rowid"));
							commissionPlanDetailModel.setColorcode(rs.getString("colorcode"));

							return commissionPlanDetailModel;
						}
					}, new Object[] { commPlanId, edate });

			logger.info("comPlanList = " + comPlanList);

			return comPlanList;

		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}

	@Override
	public List<CommissionPlanDetailEntity> getImage(int commPlanId, String effDate) {
		logger.info("getImage commPlanId = " + commPlanId);
		logger.info("getImage effDate = " + effDate);
		try {

			// logger.info("commPlanId=" + commPlanId);
			// logger.info("effDate=" + effDate);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date edate = formatter.parse(effDate);
			logger.info("date=" + edate);

			List<CommissionPlanDetailEntity> getImage = jdbcTemplate.query(GET_IMAGE,
					new RowMapper<CommissionPlanDetailEntity>() {

						@Override
						public CommissionPlanDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
							CommissionPlanDetailEntity commissionPlanDetailModel = new CommissionPlanDetailEntity();
							commissionPlanDetailModel.setComm_img_str(rs.getString("comm_img"));
							return commissionPlanDetailModel;
						}
					}, new Object[] { commPlanId, edate });

			// logger.info("getImage = " + getImage);

			return getImage;

		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}

	@Override
	public List<CommissionPlanDetailTierRangeModel> getKpiPlansToReplicate(int commPlanId, String effDate)
			throws ParseException {

		logger.info("getKpiPlansToReplicate commPlanId = " + commPlanId);
		logger.info("getKpiPlansToReplicate effDate = " + effDate);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = formatter.parse(effDate);

		try {

			List<CommissionPlanDetailTierRangeModel> manageCodesList = jdbcTemplate.query(GET_KPIDETAILS,
					new RowMapper<CommissionPlanDetailTierRangeModel>() {

						@Override
						public CommissionPlanDetailTierRangeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
							CommissionPlanDetailTierRangeModel commPlanDtls = new CommissionPlanDetailTierRangeModel();
							commPlanDtls.setComm_plan(rs.getString("comm_plan"));
							commPlanDtls.setKpi_name(rs.getString("kpi_name"));
							commPlanDtls.setComm_plan_dtl_id(rs.getLong("comm_plan_dtl_id"));
							commPlanDtls.setCdtr_id(rs.getLong("cdtr_id"));
							commPlanDtls.setKpi_type(rs.getString("kpi_type"));
							commPlanDtls.setDisplay_flg(rs.getString("display_flg"));
							commPlanDtls.setCreated_by(rs.getString("created_by"));
							commPlanDtls.setComm_plan_id(rs.getInt("comm_plan_id"));
							commPlanDtls.setKpi_id(rs.getInt("kpi_id"));
							commPlanDtls.setKpi_weightage(rs.getFloat("kpi_weightage"));
							commPlanDtls.setRangeLow(rs.getFloat("range_low"));
							commPlanDtls.setRange_high(rs.getFloat("range_high"));
							commPlanDtls.setModifier_percent(rs.getFloat("modifier_percent"));
							commPlanDtls.setCommission_val(rs.getFloat("commission_val"));
							commPlanDtls.setCreated_dt(rs.getDate("created_dt"));
							commPlanDtls.setUpdate_dt(rs.getDate("update_dt"));
							commPlanDtls.setUpdated_by(rs.getString("updated_by"));
							commPlanDtls.setMeasure_type(rs.getString("measure_type"));
							commPlanDtls.setDisplay_name(rs.getString("display_name"));
							commPlanDtls.setTier(rs.getString("tier"));
							commPlanDtls.setTarget(rs.getFloat("target"));
							commPlanDtls.setViewOrder(rs.getInt("view_order"));
							commPlanDtls.setKpi_goal(rs.getInt("kpi_goal"));

							commPlanDtls.setCharge_back(rs.getString("charge_back"));
							commPlanDtls.setCharge_back_days(rs.getInt("charge_back_days"));
							commPlanDtls.setNon_charge_back_days(rs.getInt("non_charge_back_days"));

							commPlanDtls.setMeasure_against(rs.getString("measure_against"));
							commPlanDtls.setIs_percentage(rs.getString("is_percentage"));
							commPlanDtls.setCalculate_score(rs.getString("calculate_score"));
							commPlanDtls.setIs_pooled(rs.getString("is_pooled"));

							commPlanDtls.setCommission_payout(rs.getString("commission_payout"));
							commPlanDtls.setCalc_vars_score(rs.getString("calc_vars_score"));
							commPlanDtls.setCalc_vars_count(rs.getString("calc_vars_count"));
							commPlanDtls.setCalc_vars_revenue(rs.getString("calc_vars_revenue"));
							commPlanDtls.setIs_calc_vars(rs.getString("is_calc_vars"));
							commPlanDtls.setIs_score_vars(rs.getString("is_score_vars"));

							return commPlanDtls;
						}
					}, new Object[] { commPlanId, commPlanId, date, date });

			logger.info("getKpiPlansToReplicate manageCodesList = " + manageCodesList);

			return manageCodesList;
		} catch (Exception ex) {
			logger.error(getClass().getSimpleName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ ": Exception: " + ex);
			return null;
		}
	}

	@Override
	public CommissionPlanDetailEntity updateKpiToCommPlan(
			List<CommissionPlanDetailTierRangeModel> commissionPlanDetailModels) {

		logger.info("updateKpiToCommPlan commissionPlanDetailModels = " + commissionPlanDetailModels);

		List<Integer> compPlanDtlIdList = new ArrayList<>();
		CommissionPlanDetailEntity updateCommPlan = null;

		int i = 0;

		for (CommissionPlanDetailTierRangeModel commissionPlaDetailnModel : commissionPlanDetailModels) {
			i++;
			if (commissionPlaDetailnModel.getComm_plan_id() != 0
					&& commissionPlaDetailnModel.getComm_plan_dtl_id() != 0) {

				CommissionPlanDetailEntity commPlanDtl = commissionPlanDetailsRepository
						.findById(commissionPlaDetailnModel.getComm_plan_dtl_id())
						.orElseThrow(() -> new ResourceNotFoundException(
								"Commission plan id not found: " + commissionPlaDetailnModel.getComm_plan_dtl_id()));

				// logger.info("getKpi_goal = "+commissionPlaDetailnModel.getKpi_goal());

				commPlanDtl.setKpi_id(commissionPlaDetailnModel.getKpi_id());
				commPlanDtl.setKpi_weightage(commissionPlaDetailnModel.getKpi_weightage());
				commPlanDtl.setDisplay_flg(commissionPlaDetailnModel.getDisplay_flg());
				commPlanDtl.setUpdate_dt(commissionPlaDetailnModel.getUpdate_dt());
				commPlanDtl.setUpdated_by(commissionPlaDetailnModel.getUpdated_by());
				commPlanDtl.setDisplay_name(commissionPlaDetailnModel.getDisplay_name());
				commPlanDtl.setMeasure_type(commissionPlaDetailnModel.getMeasure_type());
				commPlanDtl.setViewOrder(commissionPlaDetailnModel.getViewOrder());
				commPlanDtl.setCharge_back(commissionPlaDetailnModel.getCharge_back());
				commPlanDtl.setCharge_back_days(commissionPlaDetailnModel.getCharge_back_days());
				commPlanDtl.setNon_charge_back_days(commissionPlaDetailnModel.getNon_charge_back_days());
				commPlanDtl.setKpi_name(commissionPlaDetailnModel.getKpi_name());
				commPlanDtl.setKpi_goal(commissionPlaDetailnModel.getKpi_goal());
				commPlanDtl.setMeasure_against(commissionPlaDetailnModel.getMeasure_against());
				commPlanDtl.setIs_percentage(commissionPlaDetailnModel.getIs_percentage());
				commPlanDtl.setCalculate_score(commissionPlaDetailnModel.getCalculate_score());
				commPlanDtl.setIs_pooled(commissionPlaDetailnModel.getIs_pooled());

				commPlanDtl.setCommission_payout(commissionPlaDetailnModel.getCommission_payout());
				commPlanDtl.setCalc_vars_score(commissionPlaDetailnModel.getCalc_vars_score());
				commPlanDtl.setCalc_vars_count(commissionPlaDetailnModel.getCalc_vars_count());
				commPlanDtl.setCalc_vars_revenue(commissionPlaDetailnModel.getCalc_vars_revenue());
				commPlanDtl.setIs_calc_vars(commissionPlaDetailnModel.getIs_calc_vars());
				commPlanDtl.setIs_score_vars(commissionPlaDetailnModel.getIs_score_vars());

				if (!compPlanDtlIdList.contains(commissionPlaDetailnModel.getComm_plan_dtl_id().intValue())) {
					compPlanDtlIdList.add(commissionPlaDetailnModel.getComm_plan_dtl_id().intValue());
				}

				updateCommPlan = commissionPlanDetailsRepository.save(commPlanDtl);

				int colorcode_count = commissionPlanDetailsRepository
						.getColorCodeCount(commissionPlaDetailnModel.getComm_plan_dtl_id());

				if (colorcode_count > 0) {
					String updatecolorsql = "update c_comm_plan_styles set colorcode=? WHERE comm_plan_dtl_id = ?";
					jdbcTemplate.update(updatecolorsql, commissionPlaDetailnModel.getColorcode(),
							commissionPlaDetailnModel.getComm_plan_dtl_id());
				} else {
					String updatecolorsql = "insert into c_comm_plan_styles(comm_plan_dtl_id,colorcode) values(?,?)";
					jdbcTemplate.update(updatecolorsql, commissionPlaDetailnModel.getComm_plan_dtl_id(),
							commissionPlaDetailnModel.getColorcode());
				}

			}

			if (i == 1) {
				String sql = "DELETE FROM c_comm_detail_values WHERE comm_plan_dtl_id = ?";
				jdbcTemplate.update(sql, commissionPlaDetailnModel.getComm_plan_dtl_id());
			}

			CommissionDetailValuesEntity commDetailValuesModel = new CommissionDetailValuesEntity();

			commDetailValuesModel.setCommPlanDtlId(compPlanDtlIdList.get(0));
			commDetailValuesModel.setComm_plan_id(commissionPlaDetailnModel.getComm_plan_id());
			commDetailValuesModel.setKpi_id(commissionPlaDetailnModel.getKpi_id());
			commDetailValuesModel.setRangeLow(commissionPlaDetailnModel.getRangeLow());
			commDetailValuesModel.setRange_high(commissionPlaDetailnModel.getRange_high());
			commDetailValuesModel.setTier(commissionPlaDetailnModel.getTier());
			commDetailValuesModel.setModifier_percent(commissionPlaDetailnModel.getModifier_percent());
			commDetailValuesModel.setCommission_val(commissionPlaDetailnModel.getCommission_val());
			commDetailValuesModel.setCreated_by(commissionPlaDetailnModel.getCreated_by());
			commDetailValuesModel.setCreated_dt(commissionPlaDetailnModel.getCreated_dt());

			if (commissionPlaDetailnModel.getTarget() != null)
				commDetailValuesModel.setTarget(commissionPlaDetailnModel.getTarget());

			commissionDetailValuesRepository.save(commDetailValuesModel);

		}

		return updateCommPlan;
	}

	@Override
	public List<CommissionPlanDetailEntity> getmeasureagainstList() {

		List<CommissionPlanDetailEntity> getmagainstList = jdbcTemplate.query(GET_MEASURE_AGAINST_LIST,
				new RowMapper<CommissionPlanDetailEntity>() {
					@Override
					public CommissionPlanDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CommissionPlanDetailEntity kpiModel = new CommissionPlanDetailEntity();
						kpiModel.setTrans_field_name(rs.getString("field_name"));
						kpiModel.setTrans_field_value(rs.getString("field_value"));
						return kpiModel;
					}
				});
		return getmagainstList;

	}

	@Override
	public List<CommissionPlanDetailEntity> getSalesChannelsList() {

		List<CommissionPlanDetailEntity> getmagainstList = jdbcTemplate.query(GET_SALESCHANNELS_LIST,
				new RowMapper<CommissionPlanDetailEntity>() {
					@Override
					public CommissionPlanDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CommissionPlanDetailEntity kpiModel = new CommissionPlanDetailEntity();
						kpiModel.setTrans_field_name(rs.getString("field_name"));
						kpiModel.setTrans_field_value(rs.getString("field_value"));
						return kpiModel;
					}
				});
		return getmagainstList;

	}

	@Override
	public DependentKPIEntity addDpndntKpitoCommPlan(List<DependentKPIEntity> dependentKPIModelList) {

		logger.info("addDpndntKpitoCommPlan dependentKPIModelList = " + dependentKPIModelList);

		try {
			logger.info("dependentKPIModelList = " + dependentKPIModelList);
			int i = 0;
			int complexid = 0;
			int complanid = 0;

			String sql = "select max(comm_plan_dtl_id) from c_comm_plan_detail ccpd";
			int commPlanDtlId = jdbcTemplate.queryForObject(sql, Integer.class);
			logger.info("commPlanDtlId = " + commPlanDtlId);
			;

			for (DependentKPIEntity dependentKPIModel : dependentKPIModelList) {
				i++;

				if (i == 1) {
					complexid = dependentKPIModel.getComplex_kpi_id();
					complanid = dependentKPIModel.getComp_plan_id();
				}

				logger.info("complexid = " + complexid);
				logger.info("complanid = " + complanid);

				logger.info("getId = " + dependentKPIModel.getId());

				int cnt = depndntKpiDtlsRepository.getCount(dependentKPIModel.getId());
				logger.info("cnt: " + cnt);

				if (cnt == 0) {
					logger.info("Inside IF");
					DependentKPIEntity depKpiModel = new DependentKPIEntity();
					depKpiModel.setKpi_id(dependentKPIModel.getKpi_id());
					depKpiModel.setComplex_kpi_id(complexid);
					depKpiModel.setComp_plan_id(complanid);
					depKpiModel.setCommPlanDtlId(commPlanDtlId);
					depKpiModel.setOperation(dependentKPIModel.getOperation());
					depKpiModel.setKpi_goal(dependentKPIModel.getKpi_goal());
					depKpiModel.setKpi_order(dependentKPIModel.getKpi_order());
					depKpiModel.setKpi_weight(dependentKPIModel.getKpi_weight());
					depKpiModel.setKpi_name(dependentKPIModel.getKpi_name());
					depKpiModel.setCreated_dt(new Date());
					depKpiModel.setCreated_by(dependentKPIModelList.get(0).getCreated_by());
					depndntKpiDtlsRepository.save(depKpiModel);
				} else {
					logger.info("Inside ELSE");

					DependentKPIEntity depKpiModel = depndntKpiDtlsRepository.findById(dependentKPIModel.getId())
							.orElseThrow(() -> new ResourceNotFoundException(
									"Commission plan id not found" + dependentKPIModel.getId()));
					depKpiModel.setKpi_id(dependentKPIModel.getKpi_id());
					depKpiModel.setComplex_kpi_id(complexid);
					depKpiModel.setComp_plan_id(complanid);
					depKpiModel.setCommPlanDtlId(commPlanDtlId);
					depKpiModel.setOperation(dependentKPIModel.getOperation());
					depKpiModel.setKpi_order(dependentKPIModel.getKpi_order());
					depKpiModel.setKpi_goal(dependentKPIModel.getKpi_goal());
					depKpiModel.setKpi_weight(dependentKPIModel.getKpi_weight());
					depKpiModel.setKpi_name(dependentKPIModel.getKpi_name());
					depKpiModel.setUpdate_dt(new Date());
					depKpiModel.setUpdated_by(dependentKPIModelList.get(0).getUpdated_by());
					depndntKpiDtlsRepository.save(depKpiModel);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dependentKPIModelList.get(0);
	}

	@Override
	public int addDpndntScoreKpitoCommPlan(String kpi_name, String kpi_weight, String complex_kpi_id, String kpi_id,
			String created_by, String comp_plan_id) {

		logger.info("addDpndntScoreKpitoCommPlan kpi_name = " + kpi_name);
		logger.info("addDpndntScoreKpitoCommPlan kpi_weight = " + kpi_weight);
		logger.info("addDpndntScoreKpitoCommPlan complex_kpi_id = " + complex_kpi_id);
		logger.info("addDpndntScoreKpitoCommPlan kpi_id = " + kpi_id);
		logger.info("addDpndntScoreKpitoCommPlan created_by = " + created_by);
		logger.info("addDpndntScoreKpitoCommPlan comp_plan_id = " + comp_plan_id);

		int status = 0;
		try {
			// logger.info(
			// "============================addDpndntScoreKpitoCommPlan========================================");
			// logger.info("comp_plan_id: " + comp_plan_id);
			// logger.info("kpi_weight = " + kpi_weight);
			String kpi_name_str[] = kpi_name.split("~");
			String kpi_weight_str[] = kpi_weight.split("~");
			String complex_kpi_id_str[] = complex_kpi_id.split("~");
			String complan_id_str[] = comp_plan_id.split("~");
			String kpi_id_str[] = kpi_id.split("~");
			String created_by_str[] = created_by.split("~");

			String sql = "select max(comm_plan_dtl_id) from c_comm_plan_detail ccpd";
			int commPlanDtlId = jdbcTemplate.queryForObject(sql, Integer.class);

			for (int i = 0; i < kpi_name_str.length; i++) {
				logger.info("complex_kpi_id_str = " + complex_kpi_id_str[i]);
				logger.info("kpi_id_str = " + kpi_id_str[i]);

				int cnt = depndntScoreKpiDtlsRepository.getCount(Integer.parseInt(complex_kpi_id_str[i]),
						Integer.parseInt(kpi_id_str[i]), Integer.parseInt(complan_id_str[i]));
				logger.info("cnt = " + cnt);
				Long id = depndntScoreKpiDtlsRepository.getId(Integer.parseInt(complex_kpi_id_str[i]),
						Integer.parseInt(kpi_id_str[i]), Integer.parseInt(complan_id_str[i]));
				logger.info("id = " + id);
				// logger.info("Inside for complexid: " + complexid);

				int val = 0;
				// logger.info("kpi_weight_str[i] = " + kpi_weight_str[i]);
				if (kpi_weight_str[i].equals("null")) {
					logger.info("Inside IF");
					val = 0;
				} else {
					logger.info("Inside ELSE");
					val = Integer.parseInt(kpi_weight_str[i]);
				}

				logger.info("val = " + val);

				if (cnt == 0) {
					logger.info("Inside IF");

					DependentScoreKPIEntity depKpiModel = new DependentScoreKPIEntity();
					depKpiModel.setKpi_id(Integer.parseInt(kpi_id_str[i]));
					depKpiModel.setComplex_kpi_id(Integer.parseInt(complex_kpi_id_str[i]));
					depKpiModel.setComp_plan_id(Integer.parseInt(complan_id_str[i]));
					depKpiModel.setKpi_weight(val);
					depKpiModel.setCommPlanDtlId(commPlanDtlId);
					depKpiModel.setKpi_name(kpi_name_str[i]);
					depKpiModel.setCreated_dt(new Date());
					depKpiModel.setCreated_by(created_by_str[i]);
					depndntScoreKpiDtlsRepository.save(depKpiModel);
					status = 1;
				} else {
					logger.info("Inside ELSE");

					DependentScoreKPIEntity depKpiModel = depndntScoreKpiDtlsRepository.findById(id)
							.orElseThrow(() -> new ResourceNotFoundException("Commission plan id not found" + id));
					depKpiModel.setKpi_weight(val);
					depKpiModel.setCommPlanDtlId(commPlanDtlId);
					depKpiModel.setKpi_name(kpi_name_str[i]);
					depKpiModel.setUpdate_dt(new Date());
					depKpiModel.setUpdated_by(created_by_str[i]);
					depndntScoreKpiDtlsRepository.save(depKpiModel);
					status = 1;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public DependentKPIEntity updateDpndntKpitoCommPlan(List<DependentKPIEntity> dependentKPIModelList) {
		logger.info("updateDpndntKpitoCommPlan dependentKPIModelList = " + dependentKPIModelList);

		try {
			int i = 0;
			int complexid = 0;
			int complanid = 0;
			int comm_plan_dtl_id = 0;
			String updatedBy = "";

			for (DependentKPIEntity dependentKPIModel : dependentKPIModelList) {
				logger.info("Inside FOR");
				i++;

				if (i == 1) {
					logger.info("Inside Main IF");
					complexid = dependentKPIModel.getComplex_kpi_id();
					complanid = dependentKPIModel.getComp_plan_id();
					comm_plan_dtl_id = dependentKPIModel.getCommPlanDtlId();
					updatedBy = dependentKPIModel.getUpdated_by();

					logger.info("complexid = " + complexid);
					logger.info("complanid = " + complanid);
					logger.info("comm_plan_dtl_id = " + comm_plan_dtl_id);
					logger.info("updatedBy = " + updatedBy);
				}

				logger.info("dependentKPIModel.getId() = " + dependentKPIModel.getId());
				int cnt = depndntKpiDtlsRepository.getCount(dependentKPIModel.getId());
				logger.info("Inside for cnt: " + cnt);

				if (cnt == 0) {
					logger.info("Inside IF");
					DependentKPIEntity depKpiModel = new DependentKPIEntity();
					depKpiModel.setKpi_id(dependentKPIModel.getKpi_id());
					depKpiModel.setComplex_kpi_id(complexid);
					depKpiModel.setComp_plan_id(complanid);
					depKpiModel.setCommPlanDtlId(comm_plan_dtl_id);
					depKpiModel.setOperation(dependentKPIModel.getOperation());
					depKpiModel.setKpi_goal(dependentKPIModel.getKpi_goal());
					depKpiModel.setKpi_order(dependentKPIModel.getKpi_order());
					depKpiModel.setKpi_weight(dependentKPIModel.getKpi_weight());
					depKpiModel.setKpi_name(dependentKPIModel.getKpi_name());
					depKpiModel.setCreated_dt(new Date());
					depKpiModel.setCreated_by(dependentKPIModelList.get(0).getCreated_by());
					depndntKpiDtlsRepository.save(depKpiModel);
				} else {
					logger.info("Inside ELSE");

					DependentKPIEntity depKpiModel = depndntKpiDtlsRepository.findById(dependentKPIModel.getId())
							.orElseThrow(() -> new ResourceNotFoundException(
									"Commission plan id not found" + dependentKPIModel.getId()));
					// logger.info("depKpiModel = " + depKpiModel);
					depKpiModel.setKpi_id(dependentKPIModel.getKpi_id());
					depKpiModel.setComplex_kpi_id(complexid);
					depKpiModel.setComp_plan_id(complanid);
					depKpiModel.setCommPlanDtlId(comm_plan_dtl_id);
					depKpiModel.setOperation(dependentKPIModel.getOperation());
					depKpiModel.setKpi_order(dependentKPIModel.getKpi_order());
					depKpiModel.setKpi_goal(dependentKPIModel.getKpi_goal());
					depKpiModel.setKpi_weight(dependentKPIModel.getKpi_weight());
					depKpiModel.setKpi_name(dependentKPIModel.getKpi_name());
					depKpiModel.setUpdate_dt(new Date());
					depKpiModel.setUpdated_by(updatedBy);
					depndntKpiDtlsRepository.save(depKpiModel);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dependentKPIModelList.get(0);
	}

	@Override
	public int updateDependntScoreKpitoCommplan(String kpi_name, String kpi_weight, String complex_kpi_id,
			String kpi_id, String created_by, String comp_plan_id, String comm_plan_dtl_id, String id) {

		logger.info("=====================updateDependntScoreKpitoCommplan()=============================");

		logger.info("updateDependntScoreKpitoCommplan comm_plan_dtl_id = " + comm_plan_dtl_id);
		logger.info("updateDependntScoreKpitoCommplan id = " + id);

		int status = 0;
		try {

			String kpi_name_str[] = kpi_name.split("~");
			String kpi_weight_str[] = kpi_weight.split("~");
			String complex_kpi_id_str[] = complex_kpi_id.split("~");
			String complan_id_str[] = comp_plan_id.split("~");
			String comm_plan_dtl_id_str[] = comm_plan_dtl_id.split("~");
			String kpi_id_str[] = kpi_id.split("~");
			String updated_by_str[] = created_by.split("~");
			String id_str[] = id.split("~");

			String sql = "DELETE FROM c_depndnt_kpi_score_weight WHERE comm_plan_dtl_id = ?";
			jdbcTemplate.update(sql, Integer.parseInt(comm_plan_dtl_id_str[0]));

			for (int i = 0; i < id_str.length; i++) {

				logger.info("Inside FOR ID: " + Long.parseLong(id_str[i]));

				DependentScoreKPIEntity depKpiModel = new DependentScoreKPIEntity();
				depKpiModel.setKpi_id(Integer.parseInt(kpi_id_str[i]));
				depKpiModel.setComplex_kpi_id(Integer.parseInt(complex_kpi_id_str[i]));
				depKpiModel.setComp_plan_id(Integer.parseInt(complan_id_str[i]));
				depKpiModel.setCommPlanDtlId(Integer.parseInt(comm_plan_dtl_id_str[i]));
				depKpiModel.setKpi_name(kpi_name_str[i]);
				depKpiModel.setKpi_weight(Integer.parseInt(kpi_weight_str[i]));
				depKpiModel.setUpdate_dt(new Date());
				depKpiModel.setUpdated_by(updated_by_str[i]);
				depndntScoreKpiDtlsRepository.save(depKpiModel);
				status = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public int deleteKpiToCommPlan(CommissionPlanDetailTierRangeModel commissionPlanDetailModel) {

		logger.info("deleteKpiToCommPlan commissionPlanDetailModel = " + commissionPlanDetailModel);

		int count = 0;
		int status = 0;

		// logger.info("commissionPlanDetailModel = "+commissionPlanDetailModel);

		String sql = "DELETE FROM c_comm_detail_values WHERE comm_plan_dtl_id = ?";
		count = jdbcTemplate.update(sql, commissionPlanDetailModel.getComm_plan_dtl_id());

		if (count > 0) {
			String sql1 = "DELETE FROM c_comm_plan_detail WHERE comm_plan_dtl_id = ?";
			count = jdbcTemplate.update(sql1, commissionPlanDetailModel.getComm_plan_dtl_id());
			status = 1;

			if (count > 0) {
				String sql2 = "DELETE FROM c_depndnt_kpi_details WHERE comm_plan_dtl_id = ?";
				count = jdbcTemplate.update(sql2, commissionPlanDetailModel.getComm_plan_dtl_id());
				status = 1;

				if (count > 0) {
					String sql3 = "DELETE FROM c_depndnt_kpi_score_weight WHERE comm_plan_dtl_id = ?";
					count = jdbcTemplate.update(sql3, commissionPlanDetailModel.getComm_plan_dtl_id());
					status = 1;
				}
			}
		}
		return status;
	}

	@Override
	public List getMeasureTypes() {

		String sql = GET_MEASURETYPES;
		List<String> measureTypeList = jdbcTemplate.query(sql, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String measureType = rs.getString("field_value");
				return measureType;

			}
		});
		return measureTypeList;

	}

	@Override
	public List<CommissionDetailValuesEntity> getTierRangeValues(int commPlanDtlId) {
		// TODO Auto-generated method stub
		return commissionDetailValuesRepository.findByCommPlanDtlIdOrderByRangeLow(commPlanDtlId);

	}

	@Override
	public Map<String, Boolean> deleteCommDetailValues(int id) {
		// TODO Auto-generated method stub
		Map<String, Boolean> response = new HashMap<>();
		try {
			commissionDetailValuesRepository.deleteById((long) id);
			response.put("deleted", Boolean.TRUE);
		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public Map<String, Boolean> deleteCalcVar(int id) {
		// TODO Auto-generated method stub
		Map<String, Boolean> response = new HashMap<>();
		try {
			depndntKpiDtlsRepository.deleteById((long) id);
			response.put("deleted", Boolean.TRUE);
		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public AdjustMentResultModel addAdjustMent(AdjustMentResultModel adjustMentModel) {

		logger.info("addAdjustMent adjustMentModel = " + adjustMentModel);

		AdjustMentResultModel newAdjustMentModel = new AdjustMentResultModel();

		String sql = "update t_adjustment_detail  set adjustment = ?,new_tier = ?,comments = ? where adjustment_id  = ?";
		jdbcTemplate.update(sql, adjustMentModel.getFixedDollarAmt(), adjustMentModel.getNewTier(),
				adjustMentModel.getComments(), adjustMentModel.getAdjustment_id());

		return newAdjustMentModel;
	}

	@Override
	public List<AdjustMentResultModel> getAdjustMentResultList(int commPeriod, String salesChannel, int commPlan) {

		logger.info("getAdjustMentResultList commPeriod = " + commPeriod);
		logger.info("getAdjustMentResultList salesChannel = " + salesChannel);
		logger.info("getAdjustMentResultList commPlan = " + commPlan);

		try {

			String sql = GET_ADJUSTMENTRESULT;
			if (commPeriod != 0) {
				sql = sql + "AND ccc.cal_run_id =" + commPeriod;
			}
			if (!salesChannel.equals("null")) {
				sql = sql + " AND cem.sales_rep_channel ='" + salesChannel + "'";
			}
			if (commPlan != 0) {
				sql = sql + " AND cem.comm_plan_id ='" + commPlan + "'";
			}
			sql = sql + " order by cem.last_name";

			List<AdjustMentResultModel> adjustMentList = jdbcTemplate.query(sql,
					new RowMapper<AdjustMentResultModel>() {

						@Override
						public AdjustMentResultModel mapRow(ResultSet rs, int rowNum) throws SQLException {
							AdjustMentResultModel adjustMentResultModel = new AdjustMentResultModel();

							adjustMentResultModel.setSc_emp_id(rs.getInt("sc_emp_id"));
							adjustMentResultModel.setAdjustment_id(rs.getInt("adjustment_id"));
							adjustMentResultModel.setName(rs.getString("name"));
							adjustMentResultModel.setUser_type(rs.getString("user_type"));
							adjustMentResultModel.setSales_rep_id(rs.getString("sales_rep_id"));
							adjustMentResultModel.setAssc_corps(rs.getString("assc_corps"));
							adjustMentResultModel.setSales_rep_channel(rs.getString("sales_rep_channel"));
							adjustMentResultModel.setEmployee_id(rs.getInt("employee_id"));
							adjustMentResultModel.setSoft_termination_date(rs.getDate("soft_termination_date"));
							adjustMentResultModel.setOperator_id(rs.getString("operator_id"));
							adjustMentResultModel.setSales_rep_type(rs.getString("sales_rep_type"));
							adjustMentResultModel.setKpi(rs.getString("kpi"));
							adjustMentResultModel.setFixedDollarAmt(rs.getLong("adjustment"));
							adjustMentResultModel.setNewTier(rs.getString("new_tier"));
							adjustMentResultModel.setComments(rs.getString("comments"));

							return adjustMentResultModel;
						}
					});
			return adjustMentList;
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}

	@Override
	public CommissionPlanEntity getCommplanByAsOfDdate(int id) {

		logger.info("getCommplanByAsOfDdate id = " + id);

		try {

			List<CommissionPlanEntity> commPlanMdls = jdbcTemplate.query(GET_COMM_PLAN_BY_AS_OF_DATE,
					new RowMapper<CommissionPlanEntity>() {

						@Override
						public CommissionPlanEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
							CommissionPlanEntity commPlanMdl = new CommissionPlanEntity();
							commPlanMdl.setId(rs.getLong("row_id"));
							commPlanMdl.setCommPlanId(rs.getInt("comm_plan_id"));
							commPlanMdl.setComm_plan_type(rs.getString("comm_plan_type"));
							commPlanMdl.setComPlan(rs.getString("comm_plan"));
							commPlanMdl.setSales_channel_desc(rs.getString("sales_channel_desc"));
							commPlanMdl.setSales_channel(rs.getString("sales_channel"));
							commPlanMdl.setActive_flag(rs.getString("active_flag"));
							commPlanMdl.setWinbackpool_flag(rs.getString("winbackpool_flag"));
							commPlanMdl.setDay_multiplier(rs.getInt("day_multiplier"));
							commPlanMdl.setShow_perf(rs.getString("show_perf"));
							commPlanMdl.setDisplay_flag(rs.getString("display_flag"));
							commPlanMdl.setLanguage(rs.getString("language"));
							commPlanMdl.setCommercial_flag(rs.getString("commercial_flag"));
							commPlanMdl.setCommPlan(rs.getString("comm_plan_desc"));
							commPlanMdl.setCreated_by(rs.getString("created_by"));
							commPlanMdl.setCreated_dt(rs.getDate("created_dt"));
							commPlanMdl.setEffective_date(rs.getDate("effective_date"));
							commPlanMdl.setEDate(rs.getString("edate"));
							commPlanMdl.setCalendar_type(rs.getString("calendar_type"));
							commPlanMdl.setComm_pool(rs.getFloat("comm_pool"));
							commPlanMdl.setCharge_back(rs.getString("charge_back"));
							commPlanMdl.setCharge_back_days(rs.getInt("charge_back_days"));
							commPlanMdl.setNon_pay_charge_back_days(rs.getInt("non_pay_charge_back_days"));
							commPlanMdl.setComm_plan_priority(rs.getInt("comm_plan_priority"));
							commPlanMdl.setCharge_back_type(rs.getString("charge_back_type"));
							return commPlanMdl;
						}
					}, new Object[] { id });

			return commPlanMdls.get(0);
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}

	@Override
	public List<CommissionPlanEntity> getAsOfDate(int commPlanId) {
		logger.info("getAsOfDate commPlanId = " + commPlanId);

		List<CommissionPlanEntity> commPlanMdls = jdbcTemplate.query(GET_AS_OF_DATE,
				new RowMapper<CommissionPlanEntity>() {

					@Override
					public CommissionPlanEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CommissionPlanEntity commPlanMdl = new CommissionPlanEntity();
						commPlanMdl.setId(rs.getLong("row_id"));
						commPlanMdl.setEffective_date(rs.getDate("effective_date"));
						return commPlanMdl;
					}
				}, new Object[] { commPlanId });
		return commPlanMdls;
	}

	@Override
	public List<CommissionPlanEntity> getAllCommPlans() {

		return commissionPlanRepository.findAll(Sort.by("comPlan"));
	}

	@Override
	public List<CalendarEntity> validateEffictiveDt(int commPlanId) {

		logger.info("validateEffictiveDt commPlanId = " + commPlanId);

		try {
			List<CalendarEntity> calnderDtList = jdbcTemplate.query(GET_CALENDAR_DATE, new RowMapper<CalendarEntity>() {

				@Override
				public CalendarEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					CalendarEntity calMdl = new CalendarEntity();

					calMdl.setValid_from_dt(rs.getDate("valid_from_dt"));
					calMdl.setValid_to_dt(rs.getDate("valid_to_dt"));

					return calMdl;
				}
			}, new Object[] { commPlanId });
			return calnderDtList;
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}

	@Override
	public List<FreeFormModel> getFreeFormSerchResults(String searchwith) {

		logger.info("getFreeFormSerchResults searchwith = " + searchwith);

		try {
			String bfr = "%";
			String end = "%";
			String result = bfr + searchwith + end;
			logger.info("result = " + result);
			List<FreeFormModel> freeFormModelList = jdbcTemplate.query(FREE_FORM_RESULT,
					new RowMapper<FreeFormModel>() {

						@Override
						public FreeFormModel mapRow(ResultSet rs, int rowNum) throws SQLException {
							FreeFormModel formMdl = new FreeFormModel();
							formMdl.setFeedType(rs.getString("feed_type"));
							formMdl.setFeedID(rs.getInt("feed_id"));
							formMdl.setSalesRepId(rs.getString("id"));
							formMdl.setSearchDate(rs.getDate("search_date"));
							formMdl.setSearchString(rs.getString("search_string"));
							return formMdl;
						}
					}, new Object[] { result });
			return (List<FreeFormModel>) freeFormModelList;
		} catch (Exception ex) {
			ex.printStackTrace();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}

	// This Method is responsible to store email id in database
	@Override
	public BossCallMappingModel saveEmaildId(BossCallMappingModel callModel) {

		logger.info("saveEmaildId callModel = " + callModel);

		try {
			// logger.info("inside save=" + callModel.getEmail_id());

			String sql = "select count(email_id) from c_emailid_details";
			int cnt = jdbcTemplate.queryForObject(sql, Integer.class);

			// logger.info(String.valueOf(cnt));
			if (cnt == 0) {
				jdbcTemplate.update("INSERT into c_emailid_details(email_id) VALUES(?)",
						new Object[] { callModel.getEmail_id() });
			} else {
				jdbcTemplate.update("UPDATE c_emailid_details SET email_id=?",
						new Object[] { callModel.getEmail_id() });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callModel;
	}

	// This Method is responsible to send email id
	@Override
	public boolean sendEmail(BossCallMappingModel callModel) throws AddressException {
		boolean f = false;
		String from = "amitbiradar02@gmail.com";
		String subject = "changes in bosscall mapping";
		String message = callModel.getEmployee_id() + callModel.getClass_name() + callModel.getSales_rep_id()
				+ callModel.getOperator_id() + callModel.getValid_from_dt() + callModel.getValid_to_dt()
				+ callModel.getNetwork_id() + callModel.getFirst_name() + callModel.getLast_name();
		String test = callModel.getEmail_id();
//		String []to = test.split(",");
//		InternetAddress[] recipientAddress = new InternetAddress[to.length];
//		int counter = 0;
//		for (String recipient : to) {
//		    recipientAddress[counter] = new InternetAddress(recipient.trim());
//		    counter++;
//		}
		String to = "justcricket2323@gmail.com";
		// variable for gmail
		// String host = "smtp.gmail.com";
		String host = "Mail.AlticeUSA.com";

		// get the system properties
		Properties properties = System.getProperties();
		// System.out.println("PROPERTIES" + properties);

		// Setting Important information to properties object
		properties.put("mail.smtp.host", host);
		// properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.port", "25");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Step 1 : To get the session object
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("amitbiradar02@gmail.com", "fcbulqpkkohnascg");
			}
		});

		session.setDebug(true);

		// Step 2 : compose the message
		MimeMessage m = new MimeMessage(session);

		try {
			// from email
			m.setFrom(from);

			// adding recepient to message
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// m.setRecipients(Message.RecipientType.TO, recipientAddress);

			// adding subject to message
			m.setSubject(subject);

			// adding text to message
			m.setText(message);

			// send

			// Step 3 : send the message using Transport class
			Transport.send(m);

			// System.out.println("Sent success.......");
			f = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	@Override
	public String getEmail() {

		String email_Details = "";
		String sql = "select * from c_emailid_details ced ";
		email_Details = jdbcTemplate.queryForObject(sql, String.class);
		return email_Details;

	}

	@Override
	public List<DependentKPIEntity> getDpndntKPI(int kpiId, int comp_plan_id, int comm_plan_dtl_id, String edate) {

		logger.info("Inside getDpndntKPI");
		logger.info("kpiId = " + kpiId);
		logger.info("comp_plan_id = " + comp_plan_id);
		logger.info("comm_plan_dtl_id = " + comm_plan_dtl_id);
		logger.info("edate = " + edate);

		List<DependentKPIEntity> dependentKPIModelList = new ArrayList<DependentKPIEntity>();

		dependentKPIModelList = getDepndentKpiDtls(kpiId, comp_plan_id, comm_plan_dtl_id);
		logger.info("dependentKPIModelList 1 = " + dependentKPIModelList);

		if (dependentKPIModelList.size() == 0) {
			logger.info("Inside IF");
			dependentKPIModelList = getDepndentKpi(kpiId, comp_plan_id, edate);
			logger.info("dependentKPIModelList 2 = " + dependentKPIModelList);

		}
		return dependentKPIModelList;
	}

	public List<DependentKPIEntity> getDepndentKpiDtls(int kpiId, int comp_plan_id, int comm_plan_dtl_id) {

		logger.info("Inside getDepndentKpiDtls");

		int cpid1 = 0;
		if (comm_plan_dtl_id == 0) {
			cpid1 = 1;
			comm_plan_dtl_id = 0;
		} else {
			cpid1 = 0;
		}

		logger.info("kpiId = " + kpiId);
		logger.info("comp_plan_id = " + comp_plan_id);
		logger.info("cpid1 = " + cpid1);
		logger.info("comm_plan_dtl_id = " + comm_plan_dtl_id);

		List<DependentKPIEntity> dependentKPIModelList = jdbcTemplate.query(GET_DEPNDNT_KPI_DTLS,
				new RowMapper<DependentKPIEntity>() {

					@Override
					public DependentKPIEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						DependentKPIEntity depkpimdl = new DependentKPIEntity();
						depkpimdl.setId(rs.getLong("row_id"));
						depkpimdl.setKpi_id(rs.getInt("kpi_id"));
						depkpimdl.setKpi_name(rs.getString("kname"));
						depkpimdl.setKpi_goal(rs.getInt("kpi_goal"));
						depkpimdl.setKpi_weight(rs.getInt("kpi_weight"));
						depkpimdl.setOperation(rs.getString("operation"));
						depkpimdl.setKpi_order(rs.getString("kpi_order"));
						depkpimdl.setComp_plan_id(rs.getInt("comp_plan_id"));
						depkpimdl.setCommPlanDtlId(rs.getInt("comm_plan_dtl_id"));
						depkpimdl.setComplex_kpi_id(rs.getInt("complex_kpi_id"));
						depkpimdl.setIs_calc_vars(rs.getString("is_calc_vars"));

						return depkpimdl;
					}
				}, new Object[] { kpiId, comp_plan_id, cpid1, comm_plan_dtl_id });
		return (List<DependentKPIEntity>) dependentKPIModelList;

	}

	public List<DependentKPIEntity> getDepndentKpi(int kpiId, int comp_plan_id, String edate) {
		logger.info("Inside getDepndentKpi");

		logger.info("kpiId = " + kpiId);
		logger.info("edate = " + edate);

		List<DependentKPIEntity> dependentKPIModelList = jdbcTemplate.query(GET_DEPNDNT_KPI,
				new RowMapper<DependentKPIEntity>() {

					@Override
					public DependentKPIEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						DependentKPIEntity depkpimdl = new DependentKPIEntity();
						depkpimdl.setId(rs.getLong("kpi_id"));
						depkpimdl.setKpi_id(rs.getInt("kpi_id"));
						depkpimdl.setKpi_name(rs.getString("dpnt_kpi_name"));
						return depkpimdl;
					}
				}, new Object[] { kpiId, edate });
		return (List<DependentKPIEntity>) dependentKPIModelList;

	}

	@Override
	public List<DependentScoreKPIEntity> getDpndntScoreKPI(int kpiId, int comp_plan_id, int comm_plan_dtl_id,
			String edate) {

		logger.info("Inside getDpndntScoreKPI");
		logger.info("kpiId = " + kpiId);
		logger.info("comp_plan_id = " + comp_plan_id);
		logger.info("comm_plan_dtl_id = " + comm_plan_dtl_id);
		logger.info("edate = " + edate);

		List<DependentScoreKPIEntity> dependentKPIModelList = new ArrayList<DependentScoreKPIEntity>();

		dependentKPIModelList = getDepndentScoreKpiDtls(kpiId, comp_plan_id, comm_plan_dtl_id, edate);

		if (dependentKPIModelList.size() == 0) {
			dependentKPIModelList = getDepndentScoreKpiDtls(kpiId, comp_plan_id, comm_plan_dtl_id, edate);

		}
		return dependentKPIModelList;
	}

	public List<DependentScoreKPIEntity> getDepndentScoreKpiDtls(int kpiId, int comp_plan_id, int comm_plan_dtl_id,
			String edate) {
		logger.info("getDepndentScoreKpiDtls kpiId = " + kpiId);
		logger.info("getDepndentScoreKpiDtls comp_plan_id = " + comp_plan_id);
		logger.info("getDepndentScoreKpiDtls comm_plan_dtl_id = " + comm_plan_dtl_id);
		logger.info("getDepndentScoreKpiDtls edate = " + edate);

		List<DependentScoreKPIEntity> dependentKPIModelList = jdbcTemplate.query(GET_DEPNDNT_SCORE_KPI_DTLS,
				new RowMapper<DependentScoreKPIEntity>() {

					@Override
					public DependentScoreKPIEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						DependentScoreKPIEntity depkpimdl = new DependentScoreKPIEntity();
						depkpimdl.setComplex_kpi_id(rs.getInt("complex_kpi_id"));
						depkpimdl.setKpi_id(rs.getInt("kpi_id"));
						depkpimdl.setKpi_name(rs.getString("kpi_name"));
						depkpimdl.setKpi_weight(rs.getInt("kpi_weight"));
						depkpimdl.setId(rs.getLong("row_id"));
						depkpimdl.setCommPlanDtlId(rs.getInt("comm_plan_dtl_id"));
						depkpimdl.setIs_score_vars(rs.getString("is_score_vars"));
						return depkpimdl;
					}
				}, new Object[] { kpiId, comp_plan_id, comm_plan_dtl_id, edate });
		return (List<DependentScoreKPIEntity>) dependentKPIModelList;

	}

	/*
	 * This method is used to get all the KPI calc type's data from transaction
	 * table
	 */
	@Override
	public List<CommissionPlanEntity> getChargebackTypes() {

		List<CommissionPlanEntity> manageCodesList = jdbcTemplate.query(GET_CHARGEBACK_TYPES,
				new RowMapper<CommissionPlanEntity>() {

					@Override
					public CommissionPlanEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CommissionPlanEntity kpiModel = new CommissionPlanEntity();
						kpiModel.setTrans_field_value(rs.getString("field_value"));
						return kpiModel;
					}
				});
		return manageCodesList;

	}

	@Override
	public List<CommissionPlanEntity> getCommissionPlans() {

		List<CommissionPlanEntity> commPlansList = jdbcTemplate.query(GET_COMMISSION_PLANS,
				new RowMapper<CommissionPlanEntity>() {

					@Override
					public CommissionPlanEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CommissionPlanEntity commPlanObj = new CommissionPlanEntity();
						commPlanObj.setCommPlan(rs.getString("comm_plan"));
						commPlanObj.setCommPlanId(rs.getInt("comm_plan_id"));
						return commPlanObj;
					}
				});
		return commPlansList;

	}
	
	@Override
	public List<CommissionPlanEntity> getSalesChannels() {

		List<CommissionPlanEntity> commPlansList = jdbcTemplate.query(GET_SALES_CHANNELS,
				new RowMapper<CommissionPlanEntity>() {

					@Override
					public CommissionPlanEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CommissionPlanEntity commPlanObj = new CommissionPlanEntity();
						commPlanObj.setSales_channel(rs.getString("sales_channel"));
						return commPlanObj;
					}
				});
		return commPlansList;

	}

	@Override
	public CommissionPlanEntity updateCommPlanStatusToActive(List<CommissionPlanEntity> updateCommPlanStatusToActive) {
		CommissionPlanEntity updateKpiStatus = null;
		for (CommissionPlanEntity cpModel : updateCommPlanStatusToActive) {

			jdbcTemplate.update("UPDATE c_comm_plan_master set active_flag='Y' where comm_plan_id='"
					+ cpModel.getCommPlanId() + "'");

		}

		return updateKpiStatus;
	}

	@Override
	public CommissionPlanEntity updateCommPlanStatusToIactive(
			List<CommissionPlanEntity> updateCommPlanStatusToIactive) {
		CommissionPlanEntity updateKpiStatus = null;
		for (CommissionPlanEntity cpModel : updateCommPlanStatusToIactive) {

			jdbcTemplate.update("UPDATE c_comm_plan_master set active_flag='N' where comm_plan_id='"
					+ cpModel.getCommPlanId() + "'");

		}

		return updateKpiStatus;
	}

	/* Upload images */
	@Override
	public CommissionPlanEntity updateSingleFile(MultipartFile file, Long id) throws IOException {

		CommissionPlanEntity myDoc = commissionPlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Id not found" + id));

		// myDoc = new MyDocsModel(file.getBytes());
		myDoc.setComm_img(file.getBytes());
		return commissionPlanRepository.save(myDoc);
	}

}
