package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.KpiMasterEntity;
import com.altice.salescommission.entity.KpiDetailEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.model.KpiModel;
import com.altice.salescommission.queries.KpiMasterQueries;
import com.altice.salescommission.repository.KpiDetailRepository;
import com.altice.salescommission.repository.KpiMasterRepository;
import com.altice.salescommission.repository.KpiProductDetailRepository;
import com.altice.salescommission.service.KpiService;

@Service
@Transactional
public class KpiMasterServiceImpl implements KpiService, KpiMasterQueries {

	private static final Logger logger = LoggerFactory.getLogger(KpiMasterServiceImpl.class.getName());
	long global_kpiid = 0;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private KpiMasterRepository kpiRepository;

	@Autowired
	private KpiDetailRepository kpiDetailRepository;

	@Autowired
	private KpiProductDetailRepository kpiProductDetailRepository;

	@Override
	public int createKPI(KpiModel kpiMasterModel) throws DuplicateRecordException {
		System.out.println(kpiMasterModel);

		int status = 0;
		int kpi_count = kpiRepository.getKpiNameCount(kpiMasterModel.getKpi_name());

		// Duplicate KPI Name checking
		if (kpi_count > 0 && kpiMasterModel.getStat() == 1) {
			logger.info("insertKPI Inside IF");
			status = 2;
			throw new DuplicateRecordException(
					String.valueOf("Record " + kpiMasterModel.getKpi_name() + DUPLICATE_RECORD));
		} else {
			
			long maxkpiid = 0L;

			if (kpiMasterModel.getStat() == 1) {
				 maxkpiid = kpiRepository.getMaxId() + 1;
			} else {
				maxkpiid = global_kpiid;
			}

			KpiMasterEntity kpiMasterObj = new KpiMasterEntity();

			kpiMasterObj.setId(maxkpiid);
			kpiMasterObj.setName(kpiMasterModel.getKpi_name());
			kpiMasterObj.setKpi_type(kpiMasterModel.getKpi_type());
			kpiMasterObj.setKpi_status(kpiMasterModel.getKpi_status());
			kpiMasterObj.setEffective_date(kpiMasterModel.getEffective_date());
			kpiMasterObj.setKpi_calc_type(kpiMasterModel.getKpi_calc_type());
			kpiMasterObj.setCustom_routine(kpiMasterModel.getCustom_routine());
			kpiMasterObj.setWstat_string(kpiMasterModel.getWstat_string());
			kpiMasterObj.setKpi_priority(kpiMasterModel.getKpi_priority());
			kpiMasterObj.setFeed_type(kpiMasterModel.getFeed_type());
			kpiMasterObj.setCreated_dt(new Date());
			kpiMasterObj.setCreated_by(kpiMasterModel.getCreated_by());

			kpiMasterObj = kpiRepository.save(kpiMasterObj);

			if (kpiMasterObj != null) {
				logger.info("Inside IF");
				status = 1;

				if (kpiMasterModel.getKpi_type().equals("NA")) {
					status = 1;
				} else if (kpiMasterModel.getKpi_type().equals("By Combo String")) {
					logger.info("Inside By Combo String");

					String cp[] = kpiMasterModel.getCombo_position().split("~");
					String cv[] = kpiMasterModel.getCombo_values().split("~");

					for (int k = 0; k < cp.length; k++) {

						KpiDetailEntity kpiDetailObj = new KpiDetailEntity();
						kpiDetailObj.setKpi_id(maxkpiid);
						kpiDetailObj.setCombo_position(cp[k]);
						kpiDetailObj.setCombo_values(cv[k]);
						kpiDetailObj.setCombo_string(kpiMasterModel.getCombo_string());
						kpiDetailObj.setEffective_date(kpiMasterModel.getEffective_date());
						kpiDetailObj.setCreated_dt(new Date());
						kpiDetailObj.setCreated_by(kpiMasterModel.getCreated_by());
						kpiDetailObj = kpiDetailRepository.save(kpiDetailObj);

						if (kpiDetailObj != null) {
							status = 1;
						} else {
							logger.info("Inside Combo String ELSE");
							status = 0;
						}

					}

				} else if (kpiMasterModel.getKpi_type().equals("By KPI")) {
					logger.info("Inside By KPI");
					logger.info(kpiMasterModel.getDepndentKpis());
					logger.info(kpiMasterModel.getInclude_count());
					logger.info(kpiMasterModel.getInclude_revenue());

					String dependentKpiIdStr[] = kpiMasterModel.getDepndentKpis().split("~");
					String countStr[] = kpiMasterModel.getInclude_count().split("~");
					String revenueStr[] = kpiMasterModel.getInclude_revenue().split("~");

					System.out.println(dependentKpiIdStr.length);

					for (int i = 0; i < dependentKpiIdStr.length; i++) {
						logger.info("Inside For");
						KpiDetailEntity kpiDetailObj = new KpiDetailEntity();
						kpiDetailObj.setKpi_id(maxkpiid);
						kpiDetailObj.setDependentKpi(Integer.parseInt(dependentKpiIdStr[i]));
						kpiDetailObj.setEffective_date(kpiMasterModel.getEffective_date());
						kpiDetailObj.setInclude_count(countStr[i]);
						kpiDetailObj.setInclude_revenue(revenueStr[i]);
						kpiDetailObj.setCreated_dt(new Date());
						kpiDetailObj.setCreated_by(kpiMasterModel.getCreated_by());
						kpiDetailObj = kpiDetailRepository.save(kpiDetailObj);

						if (kpiDetailObj != null) {
							status = 1;
						} else {
							logger.info("Inside By KPI ELSE");
							status = 0;
						}

					}
				} else if (kpiMasterModel.getKpi_type().equals("By Product")) {
					logger.info("Inside By Product");

					String allValue = "All";

					String product_type = "";
					String product_category = "";
					String product = "";

					if (kpiMasterModel.getProduct_type().equals("All~")
							|| kpiMasterModel.getProduct_category().equals("All~")
							|| kpiMasterModel.getProduct().equals("All~")) {
						product_type = kpiMasterModel.getProduct_type().substring(0,
								kpiMasterModel.getProduct_type().length() - 1);
						product_category = kpiMasterModel.getProduct_category().substring(0,
								kpiMasterModel.getProduct_category().length() - 1);
						product = kpiMasterModel.getProduct().substring(0, kpiMasterModel.getProduct().length() - 1);
					}

					logger.info("product_type=" + product_type);
					logger.info("product_category=" + product_category);
					logger.info("product=" + product);

					if (product_type.equals(allValue)) {

						KpiDetailEntity kpiDetailObj = new KpiDetailEntity();
						kpiDetailObj.setKpi_id(maxkpiid);
						kpiDetailObj.setEffective_date(kpiMasterModel.getEffective_date());
						kpiDetailObj.setCreated_dt(new Date());
						kpiDetailObj.setCreated_by(kpiMasterModel.getCreated_by());

						kpiDetailObj.setProduct_type(product_type);
						kpiDetailObj.setProduct_category(allValue);
						kpiDetailObj.setProduct(allValue);

						kpiDetailObj = kpiDetailRepository.save(kpiDetailObj);

						if (kpiDetailObj != null) {
							status = 1;
						} else {
							logger.info("Inside By KPI ELSE");
							status = 0;
						}

					} else if (product_category.equals(allValue)) {

						KpiDetailEntity kpiDetailObj = new KpiDetailEntity();
						kpiDetailObj.setKpi_id(maxkpiid);
						kpiDetailObj.setEffective_date(kpiMasterModel.getEffective_date());
						kpiDetailObj.setCreated_dt(new Date());
						kpiDetailObj.setCreated_by(kpiMasterModel.getCreated_by());

						kpiDetailObj.setProduct_type(product_type);
						kpiDetailObj.setProduct_category(product_category);
						kpiDetailObj.setProduct(allValue);

						kpiDetailObj = kpiDetailRepository.save(kpiDetailObj);

						if (kpiDetailObj != null) {
							status = 1;
						} else {
							logger.info("Inside By KPI ELSE");
							status = 0;
						}

					} else if (product.equals(allValue)) {

						KpiDetailEntity kpiDetailObj = new KpiDetailEntity();
						kpiDetailObj.setKpi_id(maxkpiid);
						kpiDetailObj.setEffective_date(kpiMasterModel.getEffective_date());
						kpiDetailObj.setCreated_dt(new Date());
						kpiDetailObj.setCreated_by(kpiMasterModel.getCreated_by());

						kpiDetailObj.setProduct_type(product_type);
						kpiDetailObj.setProduct_category(product_category);
						kpiDetailObj.setProduct(product);

						kpiDetailObj = kpiDetailRepository.save(kpiDetailObj);

						if (kpiDetailObj != null) {
							status = 1;
						} else {
							logger.info("Inside By KPI ELSE");
							status = 0;
						}

					} else {
						String pro[] = kpiMasterModel.getProduct().split("~");
						String procat[] = kpiMasterModel.getProduct_category().split("~");
						String protype[] = kpiMasterModel.getProduct_type().split("~");
						int pid = 0;
						for (int i = 0; i < pro.length; i++) {
							logger.info("ELSE product=" + pro[i]);
							if (pro[i].equals("All")) {
								pid = 0;
							} else {
								pid = kpiRepository.getProductId(pro[i], procat[i], protype[i]);
							}

							KpiDetailEntity kpiDetailObj = new KpiDetailEntity();
							kpiDetailObj.setKpi_id(maxkpiid);
							kpiDetailObj.setEffective_date(kpiMasterModel.getEffective_date());
							kpiDetailObj.setCreated_dt(new Date());
							kpiDetailObj.setCreated_by(kpiMasterModel.getCreated_by());

							kpiDetailObj.setProduct_id(pid);
							kpiDetailObj.setProduct_type(protype[i]);
							kpiDetailObj.setProduct_category(procat[i]);
							kpiDetailObj.setProduct(pro[i]);

							kpiDetailObj = kpiDetailRepository.save(kpiDetailObj);

							if (kpiDetailObj != null) {
								status = 1;
							} else {
								logger.info("Inside By KPI ELSE");
								status = 0;
							}

						}
					}
				}

			} else {
				logger.info("Inside ELSE");
				status = 0;
			}
		}

		return status;
	}

	/* This method is used to update KPI master */
	@Override
	public int updateKPI(KpiModel kpiMasterModel) throws DuplicateRecordException, ParseException {
		global_kpiid = kpiMasterModel.getId();

		int deletecount = 0;
		KpiDetailEntity updateKpiProDetail = null;

		// Get existing kpi_type value
		String kpiType = kpiRepository.getKpiTypeOne(kpiMasterModel.getId(), kpiMasterModel.getEffective_date());

		int datecnt = kpiRepository.getEffectiveDateCount(kpiMasterModel.getId(), kpiMasterModel.getEffective_date());
		if (datecnt == 0) {
			logger.info("Inside 1st IF-INSERT");
			deletecount = createKPI(kpiMasterModel);
		} else {
			// Get existing effective_date value
			Date existing_effective_date = kpiRepository.getEffectiveDate(kpiMasterModel.getId(), kpiMasterModel.getEffective_date());

			Date d1 = null;
			Date d2 = null;

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			String ed1 = dateFormat.format(kpiMasterModel.getEffective_date());
			String ed2 = dateFormat.format(existing_effective_date);

			d1 = dateFormat.parse(ed1);
			d2 = dateFormat.parse(ed2);
			logger.info("effective_date = " + d1);
			logger.info("existing_effective_date = " + d2);

			if (d1.compareTo(d2) == 0) {

				if (!kpiType.equals(kpiMasterModel.getKpi_type())) {
					logger.info("Inside IF KPI not matching");
					String deleteQuery = "delete from c_kpi_detail where kpi_id=? and effective_date=?";
					jdbcTemplate.update(deleteQuery, kpiMasterModel.getId(), kpiMasterModel.getEffective_date());
				}

				int count = jdbcTemplate.update("UPDATE c_kpi_master set kpi_name='" + kpiMasterModel.getKpi_name() + "',kpi_priority='"
						+ kpiMasterModel.getKpi_priority() + "',kpi_type='" + kpiMasterModel.getKpi_type() + "',update_dt='" + new Date() + "',updated_by='"
						+ kpiMasterModel.getUpdated_by() + "',kpi_status='" + kpiMasterModel.getKpi_status() + "',effective_date='" + kpiMasterModel.getEffective_date()
						+ "',kpi_calc_type='" + kpiMasterModel.getKpi_calc_type() + "',custom_routine='" + kpiMasterModel.getCustom_routine()
						+ "',wstat_string='" + kpiMasterModel.getWstat_string() + "',feed_type='" + kpiMasterModel.getFeed_type() + "' " + "where kpi_id='" + kpiMasterModel.getId()
						+ "' and effective_date='" + kpiMasterModel.getEffective_date() + "' ");
				logger.info("count = " + count);

				if (count > 0) {

					if (count > 0) {
						String deleteQuery = "delete from c_kpi_detail where kpi_id=? and effective_date=?";
						deletecount = jdbcTemplate.update(deleteQuery, kpiMasterModel.getId(), kpiMasterModel.getEffective_date());

						logger.info("deletecount = "+deletecount);

						if (kpiMasterModel.getKpi_type().equals("By Product")) {
							logger.info("Inside By Product");

							String allValue = "All";
							String product_type = "";
							String product_category = "";
							String product = "";

							if (kpiMasterModel.getProduct_type().equals("All~") || kpiMasterModel.getProduct_category().equals("All~")
									|| kpiMasterModel.getProduct().equals("All~")) {
								logger.info("Inside IF ALL");
								product_type = kpiMasterModel.getProduct_type().substring(0, kpiMasterModel.getProduct_type().length() - 1);
								logger.info("substring product_type = " + product_type);
								product_category = kpiMasterModel.getProduct_category().substring(0, kpiMasterModel.getProduct_category().length() - 1);
								logger.info("substring product_category = " + product_category);
								product = kpiMasterModel.getProduct().substring(0, kpiMasterModel.getProduct().length() - 1);
								logger.info("substring product = " + product);
							}

							if (product_type.equals(allValue)) {
								logger.info("Inside By Product IF product_type");
								KpiDetailEntity kpiModel = new KpiDetailEntity();
								kpiModel.setProduct_type(product_type);
								kpiModel.setProduct_category(allValue);
								kpiModel.setProduct(allValue);
								kpiModel.setKpi_id(kpiMasterModel.getId());
								kpiModel.setUpdate_dt(new Date());
								kpiModel.setUpdated_by(kpiMasterModel.getUpdated_by());
								kpiModel.setEffective_date(kpiMasterModel.getEffective_date());
								updateKpiProDetail = kpiProductDetailRepository.save(kpiModel);
							} else if (product_category.equals(allValue)) {
								logger.info("Inside By Product IF product_category");

								KpiDetailEntity kpiModel = new KpiDetailEntity();
								kpiModel.setProduct_type(product_type);
								kpiModel.setProduct_category(product_category);
								kpiModel.setProduct(allValue);
								kpiModel.setKpi_id(kpiMasterModel.getId());
								kpiModel.setUpdate_dt(new Date());
								kpiModel.setUpdated_by(kpiMasterModel.getUpdated_by());
								kpiModel.setEffective_date(kpiMasterModel.getEffective_date());
								updateKpiProDetail = kpiProductDetailRepository.save(kpiModel);
							} else if (product.equals(allValue)) {
								logger.info("Inside By Product IF product");

								KpiDetailEntity kpiModel = new KpiDetailEntity();
								kpiModel.setProduct_type(product_type);
								kpiModel.setProduct_category(product_category);
								kpiModel.setProduct(product);
								kpiModel.setKpi_id(kpiMasterModel.getId());
								kpiModel.setUpdate_dt(new Date());
								kpiModel.setUpdated_by(kpiMasterModel.getUpdated_by());
								kpiModel.setEffective_date(kpiMasterModel.getEffective_date());
								updateKpiProDetail = kpiProductDetailRepository.save(kpiModel);
							} else {
								logger.info("Inside By Product ELSE");
								String pro[] = kpiMasterModel.getProduct().split("~");
								String procat[] = kpiMasterModel.getProduct_category().split("~");
								String protype[] = kpiMasterModel.getProduct_type().split("~");
								for (int i = 0; i < pro.length; i++) {

									logger.info("pro[i] = " + pro[i]);
									logger.info("procat[i] = " + procat[i]);
									logger.info("protype[i] = " + protype[i]);

									int pid = 0;
									int cnt = 0;

									if (pro[i].equals("All") || procat[i].equals("All") || protype[i].equals("All")) {
										pid = 0;
									} else {
										cnt = kpiRepository.getProductIdCnt(pro[i], procat[i], protype[i]);
										if (cnt == 0) {
											pid = 0;
										} else {
											pid = kpiRepository.getProductId(pro[i], procat[i], protype[i]);
										}
									}

									logger.info("pid = " + pid);

									KpiDetailEntity kpiModel = new KpiDetailEntity();
									kpiModel.setProduct_id(pid);
									kpiModel.setProduct_type(protype[i]);
									kpiModel.setProduct_category(procat[i]);
									kpiModel.setProduct(pro[i]);
									kpiModel.setKpi_id(kpiMasterModel.getId());
									kpiModel.setUpdate_dt(new Date());
									kpiModel.setUpdated_by(kpiMasterModel.getUpdated_by());
									kpiModel.setEffective_date(kpiMasterModel.getEffective_date());
									updateKpiProDetail = kpiProductDetailRepository.save(kpiModel);
								}
							}

						} else if (kpiMasterModel.getKpi_type().equals("By Combo String")) {
							logger.info("inside By Combo String");
							logger.info("combo_position = " + kpiMasterModel.getCombo_position());
							logger.info("combo_values = " + kpiMasterModel.getCombo_values());

							String cp[] = kpiMasterModel.getCombo_position().split("~");
							String cv[] = kpiMasterModel.getCombo_values().split("~");

							for (int k = 0; k < cp.length; k++) {
								deletecount = jdbcTemplate.update(
										"INSERT INTO c_kpi_detail (kpi_id,combo_position,combo_values,combo_string,created_dt,created_by,effective_date) "
												+ "VALUES ('" + kpiMasterModel.getId() + "','" + cp[k] + "','" + cv[k] + "','" + kpiMasterModel.getCombo_string()
												+ "','" + new Date() + "','" + kpiMasterModel.getUpdated_by() + "','" + kpiMasterModel.getEffective_date()
												+ "')");
								logger.info("combo deletecount = " + deletecount);
							}
						} else if (kpiMasterModel.getKpi_type().equals("By KPI")) {
							String kpiIdStr[] = kpiMasterModel.getDepndentKpis().split("~");
							String countStr[] = kpiMasterModel.getInclude_count().split("~");
							String revenueStr[] = kpiMasterModel.getInclude_revenue().split("~");

							for (int i = 0; i < kpiIdStr.length; i++) {
								if (count > 0) {

									KpiDetailEntity kpiModel = new KpiDetailEntity();
									kpiModel.setKpi_id(kpiMasterModel.getId());
									kpiModel.setDependentKpi(Integer.parseInt(kpiIdStr[i]));
									kpiModel.setInclude_count(countStr[i]);
									kpiModel.setInclude_revenue(revenueStr[i]);
									kpiModel.setUpdate_dt(new Date());
									kpiModel.setUpdated_by(kpiMasterModel.getUpdated_by());
									kpiModel.setEffective_date(kpiMasterModel.getEffective_date());
									updateKpiProDetail = kpiProductDetailRepository.save(kpiModel);

								}
							}
						}

						if (kpiMasterModel.getKpi_type().equals("NA")) {
							deletecount = 1;
						} else if (kpiMasterModel.getKpi_type().equals("By Combo String")) {
							logger.info("By Combo String");
						} else {
							logger.info("updateKpiProDetail = " + updateKpiProDetail);
							if (updateKpiProDetail == null) {
								deletecount = 0;
							} else {
								deletecount = 1;
							}
						}
					} // process count closed
				} // count closed
			} else {
				logger.info("Effective dates not matched. Inserting data instead of update");
				deletecount = createKPI(kpiMasterModel);
			}
		}

		logger.info("final deletecount = " + deletecount);
		return deletecount;
	}

	/* This method is used to update KPI master */
	//@Override
//	public int updateKpi1(long id, String kpi_name, int kpi_priority, String kpi_type, String kpi_status,
//			String currentUser, String product, String kpi_calc_type, String custom_routine, Date effective_date,
//			String product_type, String product_category, String depndentKpis, String comboString, String productName,
//			String productid, String combo_position, String combo_values, String wstat_string, String include_count,
//			String include_revenue, String feed_type)
//			throws IOException, IdNotFoundException, DuplicateRecordException, ParseException {
//
//		global_kpiid = id;
//
//		int deletecount = 0;
//		KpiDetailEntity updateKpiProDetail = null;
//
//		// Get existing kpi_type value
//		String kpiType = kpiRepository.getKpiTypeOne(id, effective_date);
//
//		int datecnt = kpiRepository.getEffectiveDateCount(id, effective_date);
//		if (datecnt == 0) {
//			logger.info("Inside 1st IF-INSERT");
//			deletecount = insertKPI(kpi_name, kpi_priority, kpi_type, product, kpi_calc_type, currentUser,
//					custom_routine, effective_date, depndentKpis, product_type, product_category, comboString,
//					productName, 2, combo_position, combo_values, wstat_string, include_count, include_revenue,
//					feed_type);
//		} else {
//			// Get existing effective_date value
//			Date existing_effective_date = kpiRepository.getEffectiveDate(id, effective_date);
//
//			Date d1 = null;
//			Date d2 = null;
//
//			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//			String ed1 = dateFormat.format(effective_date);
//			String ed2 = dateFormat.format(existing_effective_date);
//
//			d1 = dateFormat.parse(ed1);
//			d2 = dateFormat.parse(ed2);
//			logger.info("effective_date = " + d1);
//			logger.info("existing_effective_date = " + d2);
//
//			if (d1.compareTo(d2) == 0) {
//
//				if (!kpiType.equals(kpi_type)) {
//					logger.info("Inside IF KPI not matching");
//					String deleteQuery = "delete from c_kpi_detail where kpi_id=? and effective_date=?";
//					jdbcTemplate.update(deleteQuery, id, effective_date);
//				}
//
//				int count = jdbcTemplate.update("UPDATE c_kpi_master set kpi_name='" + kpi_name + "',kpi_priority='"
//						+ kpi_priority + "',kpi_type='" + kpi_type + "',update_dt='" + new Date() + "',updated_by='"
//						+ currentUser + "',kpi_status='" + kpi_status + "',effective_date='" + effective_date
//						+ "',kpi_calc_type='" + kpi_calc_type + "',custom_routine='" + custom_routine
//						+ "',wstat_string='" + wstat_string + "',feed_type='" + feed_type + "' " + "where kpi_id='" + id
//						+ "' and effective_date='" + effective_date + "' ");
//				logger.info("count = " + count);
//
//				if (count > 0) {
//
//					if (count > 0) {
//						String deleteQuery = "delete from c_kpi_detail where kpi_id=? and effective_date=?";
//						deletecount = jdbcTemplate.update(deleteQuery, id, effective_date);
//
//						logger.info("inside deletecount");
//
//						if (kpi_type.equals("By Product")) {
//
//							String allValue = "All";
//
//							if (product_type.equals("All~") || product_category.equals("All~")
//									|| product.equals("All~")) {
//								product_type = product_type.substring(0, product_type.length() - 1);
//								logger.info("substring product_type = " + product_type);
//								product_category = product_category.substring(0, product_category.length() - 1);
//								logger.info("substring product_category = " + product_category);
//								product = product.substring(0, product.length() - 1);
//								logger.info("substring product = " + product);
//							}
//
//							if (product_type.equals(allValue)) {
//								KpiDetailEntity kpiModel = new KpiDetailEntity();
//								kpiModel.setProduct_type(product_type);
//								kpiModel.setProduct_category(allValue);
//								kpiModel.setProduct(allValue);
//								kpiModel.setKpi_id(id);
//								kpiModel.setCreated_dt(new Date());
//								kpiModel.setCreated_by(currentUser);
//								kpiModel.setEffective_date(effective_date);
//								updateKpiProDetail = kpiProductDetailRepository.save(kpiModel);
//							} else if (product_category.equals(allValue)) {
//								// checkCountOfRecords(id, product_type, product_category, allValue,
//								// effective_date);
//
//								KpiDetailEntity kpiModel = new KpiDetailEntity();
//								kpiModel.setProduct_type(product_type);
//								kpiModel.setProduct_category(product_category);
//								kpiModel.setProduct(allValue);
//								kpiModel.setKpi_id(id);
//								kpiModel.setCreated_dt(new Date());
//								kpiModel.setCreated_by(currentUser);
//								kpiModel.setEffective_date(effective_date);
//								updateKpiProDetail = kpiProductDetailRepository.save(kpiModel);
//							} else if (product.equals(allValue)) {
//								// checkCountOfRecords(id, product_type, product_category, product,
//								// effective_date);
//
//								KpiDetailEntity kpiModel = new KpiDetailEntity();
//								kpiModel.setProduct_type(product_type);
//								kpiModel.setProduct_category(product_category);
//								kpiModel.setProduct(product);
//								kpiModel.setKpi_id(id);
//								kpiModel.setCreated_dt(new Date());
//								kpiModel.setCreated_by(currentUser);
//								kpiModel.setEffective_date(effective_date);
//								updateKpiProDetail = kpiProductDetailRepository.save(kpiModel);
//							} else {
//								String pro[] = product.split("~");
//								String procat[] = product_category.split("~");
//								String protype[] = product_type.split("~");
//								for (int i = 0; i < pro.length; i++) {
//
//									logger.info("pro[i] = " + pro[i]);
//									logger.info("procat[i] = " + procat[i]);
//									logger.info("protype[i] = " + protype[i]);
//
//									int pid = 0;
//									int cnt = 0;
//
//									if (pro[i].equals("All") || procat[i].equals("All") || protype[i].equals("All")) {
//										pid = 0;
//									} else {
//										cnt = kpiRepository.getProductIdCnt(pro[i], procat[i], protype[i]);
//										if (cnt == 0) {
//											pid = 0;
//										} else {
//											pid = kpiRepository.getProductId(pro[i], procat[i], protype[i]);
//										}
//									}
//
//									logger.info("pid = " + pid);
//
//									KpiDetailEntity kpiModel = new KpiDetailEntity();
//									kpiModel.setProduct_id(pid);
//									kpiModel.setProduct_type(protype[i]);
//									kpiModel.setProduct_category(procat[i]);
//									kpiModel.setProduct(pro[i]);
//									kpiModel.setKpi_id(id);
//									kpiModel.setCreated_dt(new Date());
//									kpiModel.setCreated_by(currentUser);
//									kpiModel.setEffective_date(effective_date);
//									updateKpiProDetail = kpiProductDetailRepository.save(kpiModel);
//								}
//							}
//
//						} else if (kpi_type.equals("By Combo String")) {
//							logger.info("inside By Combo String");
//							logger.info("combo_position = " + combo_position);
//							logger.info("combo_values = " + combo_values);
//
//							String cp[] = combo_position.split("~");
//							String cv[] = combo_values.split("~");
//
//							for (int k = 0; k < cp.length; k++) {
//								deletecount = jdbcTemplate.update(
//										"INSERT INTO c_kpi_detail (kpi_id,combo_position,combo_values,combo_string,created_dt,created_by,effective_date) "
//												+ "VALUES ('" + id + "','" + cp[k] + "','" + cv[k] + "','" + comboString
//												+ "','" + new Date() + "','" + currentUser + "','" + effective_date
//												+ "')");
//								logger.info("combo deletecount = " + deletecount);
//							}
//						} else if (kpi_type.equals("By KPI")) {
//							String kpiIdStr[] = depndentKpis.split("~");
//							String countStr[] = include_count.split("~");
//							String revenueStr[] = include_revenue.split("~");
//
//							for (int i = 0; i < kpiIdStr.length; i++) {
//								if (count > 0) {
//
//									KpiDetailEntity kpiModel = new KpiDetailEntity();
//									kpiModel.setKpi_id(id);
//									kpiModel.setDependentKpi(Integer.parseInt(kpiIdStr[i]));
//									kpiModel.setInclude_count(countStr[i]);
//									kpiModel.setInclude_revenue(revenueStr[i]);
//									kpiModel.setCreated_dt(new Date());
//									kpiModel.setCreated_by(currentUser);
//									kpiModel.setEffective_date(effective_date);
//									updateKpiProDetail = kpiProductDetailRepository.save(kpiModel);
//
//								}
//							}
//						}
//
//						if (kpi_type.equals("NA")) {
//							deletecount = 1;
//						} else if (kpi_type.equals("By Combo String")) {
//							logger.info("By Combo String");
//						} else {
//							logger.info("updateKpiProDetail = " + updateKpiProDetail);
//							if (updateKpiProDetail == null) {
//								deletecount = 0;
//							} else {
//								deletecount = 1;
//							}
//						}
//					} // process count closed
//				} // count closed
//			} else {
//				logger.info("Effective dates not matched. Inserting data instead of update");
//				deletecount = insertKPI(kpi_name, kpi_priority, kpi_type, product, kpi_calc_type, currentUser,
//						custom_routine, effective_date, depndentKpis, product_type, product_category, comboString,
//						productName, 2, combo_position, combo_values, wstat_string, include_count, include_revenue,
//						feed_type);
//
//			}
//		}
//
//		logger.info("final deletecount = " + deletecount);
//		return deletecount;
//	}

	/* This method is used to delete product based on provided kpi id */
	@Override
	public int deleteProduct(long id) throws IOException {
		String deleteQuery = "delete from c_kpi_detail where ckpd_id=?";
		int role = jdbcTemplate.update(deleteQuery, id);
		return role;
	}

	/* This method is used to update multiple KPI's status to active */
	@Override
	public KpiMasterEntity updateKpiStatusToActive(List<KpiMasterEntity> kpiModel) {
		KpiMasterEntity updateKpiStatus = null;
		for (KpiMasterEntity kpiMaster : kpiModel) {

			jdbcTemplate.update("UPDATE c_kpi_master set kpi_status='Active' where kpi_id='" + kpiMaster.getId()
					+ "' and effective_date='" + kpiMaster.getEffective_date() + "' ");

		}

		return updateKpiStatus;
	}

	/* This method is used to update multiple KPI's status to inactive */
	@Override
	public KpiMasterEntity updateKpiStatusToInactive(List<KpiMasterEntity> kpiModel) {

		KpiMasterEntity updateKpiStatus = null;
		for (KpiMasterEntity kpiMaster : kpiModel) {

			jdbcTemplate.update("UPDATE c_kpi_master set kpi_status='Inactive' where kpi_id='" + kpiMaster.getId()
					+ "' and effective_date='" + kpiMaster.getEffective_date() + "' ");

		}

		return updateKpiStatus;
	}

	/* This method is used to get all the products */
	@Override
	public List<KpiModel> getProductsFromProductMaster() {
		List<KpiModel> productTypesList = jdbcTemplate.query(GET_PRODUCTS, new RowMapper<KpiModel>() {

			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setProduct_type(rs.getString("product_type"));
				kpiModel.setProduct_category(rs.getString("product_category"));
				kpiModel.setProduct(rs.getString("product"));
				kpiModel.setProductid(rs.getString("product_id"));
				return kpiModel;
			}
		});
		return productTypesList;
	}

	/*
	 * This method is used to get all the kpi type one's data from transaction table
	 */
	@Override
	public List<KpiModel> getKpiTypeOneList() {

		List<KpiModel> kpiTypeOneList = jdbcTemplate.query(GET_KPI_TYPE_ONE_LIST, new RowMapper<KpiModel>() {
			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setTrans_field_value(rs.getString("field_value"));
				kpiModel.setTrans_field_name(rs.getString("field_name"));
				return kpiModel;
			}
		});
		return kpiTypeOneList;

	}

	/*
	 * This method is used to get all the KPI calc type's data from transaction
	 * table
	 */
	@Override
	public List<KpiModel> getKPICalcTypes() {

		List<KpiModel> manageCodesList = jdbcTemplate.query(GET_KPI_CALC_TYPES, new RowMapper<KpiModel>() {

			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setTrans_field_value(rs.getString("field_value"));
				return kpiModel;
			}
		});
		return manageCodesList;

	}

	/* This method is used to get all the active KPI's */
	@Override
	public List<KpiModel> getKpiActiveList() {
		logger.info("getKpiActiveList called");
		List<KpiModel> kpiList = jdbcTemplate.query(GET_KPI_ACTIVE_LIST, new RowMapper<KpiModel>() {

			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setId(rs.getLong("kpi_id"));
				kpiModel.setKpi_name(rs.getString("kpi_name"));
				kpiModel.setKpi_type(rs.getString("kpi_type"));
				kpiModel.setKpi_status(rs.getString("kpi_status"));
				kpiModel.setKpi_calc_type(rs.getString("kpi_calc_type"));
				kpiModel.setCustom_routine(rs.getString("custom_routine"));
				kpiModel.setEffective_date(rs.getDate("effective_date"));
				kpiModel.setEdate(rs.getString("edate"));
				kpiModel.setCombo_string(rs.getString("combo_string"));
				kpiModel.setWstat_string(rs.getString("wstat_string"));
				kpiModel.setKpi_priority(rs.getInt("kpi_priority"));
				kpiModel.setInclude_count(rs.getString("include_count"));
				kpiModel.setInclude_revenue(rs.getString("include_revenue"));
				kpiModel.setFeed_type(rs.getString("feed_type"));
				return kpiModel;
			}
		});
		logger.info("kpiList = " + kpiList);

		// HashSet<Object> seen = new HashSet<>();
		// kpiList.removeIf(e -> !seen.add(e.getKpi_name()));

		// List<KpiModel> distinctItems =
		// kpiList.stream().distinct().collect(Collectors.toList());
		logger.info("kpiList = " + kpiList);
		return kpiList;

	}

	/* This method is used to get all the active KPI's */
	@Override
	public List<KpiModel> getActiveKpis() {
		logger.info("getActiveKpis called");
		List<KpiModel> kpiList = jdbcTemplate.query(GET_ACTIVE_KPIS, new RowMapper<KpiModel>() {

			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setId(rs.getLong("kpi_id"));
				kpiModel.setKpi_name(rs.getString("kpi_name"));
				kpiModel.setKpi_type(rs.getString("kpi_type"));
				kpiModel.setKpi_status(rs.getString("kpi_status"));
				kpiModel.setKpi_calc_type(rs.getString("kpi_calc_type"));
				kpiModel.setCustom_routine(rs.getString("custom_routine"));
				kpiModel.setEffective_date(rs.getDate("effective_date"));
				kpiModel.setEdate(rs.getString("edate"));
				kpiModel.setCombo_string(rs.getString("combo_string"));
				kpiModel.setWstat_string(rs.getString("wstat_string"));
				kpiModel.setKpi_priority(rs.getInt("kpi_priority"));
				kpiModel.setInclude_count(rs.getString("include_count"));
				kpiModel.setInclude_revenue(rs.getString("include_revenue"));
				return kpiModel;
			}
		});
		return kpiList;
	}

	/* This method is used to get all the active KPI's by date */
	@Override
	public List<KpiModel> getkpiactivelistByDate(String effective_date) {

		List<KpiModel> kpiList = jdbcTemplate.query(GET_KPI_ACTIVE_LIST_BY_DATE, new RowMapper<KpiModel>() {

			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setId(rs.getLong("kpi_id"));
				kpiModel.setKpi_name(rs.getString("kpi_name"));
				kpiModel.setKpi_type(rs.getString("kpi_type"));
				kpiModel.setKpi_status(rs.getString("kpi_status"));
				kpiModel.setKpi_calc_type(rs.getString("kpi_calc_type"));
				kpiModel.setCustom_routine(rs.getString("custom_routine"));
				kpiModel.setEffective_date(rs.getDate("effective_date"));
				kpiModel.setWstat_string(rs.getString("wstat_string"));
				kpiModel.setKpi_priority(rs.getInt("kpi_priority"));
				return kpiModel;
			}
		}, new Object[] { effective_date });
		HashSet<Object> seen = new HashSet<>();
		kpiList.removeIf(e -> !seen.add(e.getKpi_name()));
		return kpiList;

	}

	/* This method is used to get all the active KPI's for excel */
	@Override
	public List<KpiModel> getExcelKpiActiveList() {

		List<KpiModel> kpiList = jdbcTemplate.query(GET_KPI_ACTIVE_LIST_EXCEL, new RowMapper<KpiModel>() {

			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setId(rs.getLong("kpi_id"));
				kpiModel.setKpi_name(rs.getString("kpi_name"));
				kpiModel.setEffective_date(rs.getDate("effective_date"));
				kpiModel.setKpi_calc_type(rs.getString("kpi_calc_type"));
				kpiModel.setCustom_routine(rs.getString("custom_routine"));
				kpiModel.setKpi_type(rs.getString("kpi_type"));
				kpiModel.setProduct_type(rs.getString("product_type"));
				kpiModel.setProduct_category(rs.getString("product_category"));
				kpiModel.setProduct(rs.getString("product"));
				kpiModel.setKpi_status(rs.getString("kpi_status"));
				kpiModel.setWstat_string(rs.getString("wstat_string"));
				kpiModel.setKpi_priority(rs.getInt("kpi_priority"));
				return kpiModel;
			}
		});
		HashSet<Object> seen = new HashSet<>();
		kpiList.removeIf(e -> !seen.add(e.getKpi_name()));
		return kpiList;

	}

	/* This method is used to get all the product types */
	@Override
	public List<KpiModel> getProductTypes(int kpiid, LocalDate effective_date) {

		List<KpiModel> proList = checkProductDetailsByKpiid(kpiid, effective_date);
		List<KpiModel> proTypesList = null;

		if (proList.size() > 0) {
			String sql = "";
			if (proList.get(0).getProductid().toString().equals("0") && proList.get(0).getProduct_type().equals("0")
					&& proList.get(0).getCombo_string().equals("0")) {
				sql = GET_KPIS;
			} else if (proList.get(0).getProductid().toString().equals("0")
					&& proList.get(0).getProduct_type().equals("0") && (!proList.get(0).getCombo_string().equals("0")
							|| !proList.get(0).getCombo_string().equals(""))) {
				sql = GET_COMBOS;
			} else {
				sql = GET_PTYPES;
			}
			proTypesList = jdbcTemplate.query(sql, new RowMapper<KpiModel>() {

				@Override
				public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
					KpiModel kpiModel1 = new KpiModel();
					kpiModel1.setProduct_type(rs.getString("product_type"));
					return kpiModel1;
				}
			}, new Object[] { kpiid, effective_date });
		}

		return proTypesList;

	}

	/* This method is used to get all the categories */
	@Override
	public List<KpiModel> getCategories(int kpiid, String proType, LocalDate effective_date) {

		List<KpiModel> proList = checkProductDetailsByKpiid(kpiid, effective_date);

		List<KpiModel> catsList = null;
		if (proList.size() > 0) {
			String sql = "";
			if (proList.get(0).getProductid().toString().equals("0") && proList.get(0).getProduct_type().equals("0")
					&& (!proList.get(0).getCombo_string().equals("0")
							|| !proList.get(0).getCombo_string().equals(""))) {
				sql = GET_CAT_COMBOS;
			} else {
				sql = GET_CATEGORIES;
			}
			catsList = jdbcTemplate.query(sql, new RowMapper<KpiModel>() {
				@Override
				public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
					KpiModel kpiModel = new KpiModel();
					kpiModel.setProduct_category(rs.getString("product_category"));
					return kpiModel;
				}
			}, new Object[] { kpiid, proType, effective_date });
		}
		return catsList;

	}

	/* This method is used to get all the products */
	@Override
	public List<KpiModel> getProductsFromKpiProductDetail(int kpiid, String proType, String cat,
			LocalDate effective_date) {

		List<KpiModel> proList = checkProductDetailsByKpiid(kpiid, effective_date);

		List<KpiModel> productsList = null;
		if (proList.size() > 0) {
			productsList = jdbcTemplate.query(GET_PRODUCTS_FROM_KPI_PRODUCT_DETAIL, new RowMapper<KpiModel>() {
				@Override
				public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
					KpiModel kpiModel = new KpiModel();
					kpiModel.setId(rs.getLong("ckpd_id"));
					kpiModel.setProduct(rs.getString("product"));
					return kpiModel;
				}
			}, new Object[] { kpiid, proType, cat, effective_date });
		}
		return productsList;

	}

	/* This method is used to get all the products based on provided kpi id */
	private List<KpiModel> checkProductDetailsByKpiid(int kpiid, LocalDate effective_date) {
		List<KpiModel> proTypesList = null;

		proTypesList = jdbcTemplate.query(CHECK_PRODUCT_DETAILS_BY_KPIID, new RowMapper<KpiModel>() {
			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();

				kpiModel.setProductid(rs.getString("product_id"));
				kpiModel.setProduct_type(rs.getString("product_type"));
				kpiModel.setCombo_string(rs.getString("combo_string"));

				return kpiModel;
			}
		}, new Object[] { kpiid, effective_date });

		return proTypesList;
	}

	@Override
	public List<KpiModel> getComboStringsList() {
		List<KpiModel> kpiTypeOneList = null;

		kpiTypeOneList = jdbcTemplate.query(GET_COMBO_STRING_LIST, new RowMapper<KpiModel>() {
			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setTrans_field_value(rs.getString("field_value"));
				return kpiModel;
			}
		});

		return kpiTypeOneList;
	}

	@Override
	public List<KpiModel> getPositionsValues(Long kpiid, Date effective_date) {
		List<KpiModel> kpiList = null;

		kpiList = jdbcTemplate.query(GET_COMBO_POSITIONS_VALUES, new RowMapper<KpiModel>() {

			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setCombo_position(rs.getString("combo_position"));
				kpiModel.setCombo_values(rs.getString("combo_values"));
				return kpiModel;
			}
		}, new Object[] { kpiid, effective_date });

		return kpiList;

	}

	@Override
	public List<KpiModel> getDependKpis(Long kpiid, Date effective_date) {
		List<KpiModel> kpiList = null;

		kpiList = jdbcTemplate.query(GET_DEPEND_KPIS, new RowMapper<KpiModel>() {

			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setDepndentKpis(rs.getString("dpdnt_kpi"));

				boolean includecount = false;
				boolean includerevenue = false;

				logger.info("include_count = " + rs.getString("include_count"));
				logger.info("include_revenue = " + rs.getString("include_revenue"));

				if (rs.getString("include_count").equals("Y")) {
					includecount = true;
				} else if (rs.getString("include_count").equals("N")) {
					includecount = false;
				} else {
					includecount = false;
				}

				if (rs.getString("include_revenue").equals("Y")) {
					includerevenue = true;
				} else if (rs.getString("include_revenue").equals("N")) {
					includerevenue = false;
				} else {
					includerevenue = false;
				}

				kpiModel.setIncludecount(includecount);
				kpiModel.setIncluderevenue(includerevenue);

				return kpiModel;
			}
		}, new Object[] { kpiid, effective_date });

		return kpiList;

	}

	@Override
	public List<KpiModel> getDependKpisDropdown() {
		List<KpiModel> kpisList = null;

		kpisList = jdbcTemplate.query(GET_ALL_KPIS_LIST, new RowMapper<KpiModel>() {
			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setId(rs.getLong("kpi_id"));
				kpiModel.setDepndentKpis(rs.getString("kpi_id"));
				kpiModel.setKpi_name(rs.getString("kpi_name"));
				return kpiModel;
			}
		});

		return kpisList;
	}

	@Override
	public List<KpiModel> getSelectedProducts(Long kpiid, Date effective_date) {
		List<KpiModel> productsList = null;

		productsList = jdbcTemplate.query(GET_SELECTED_PRODUCTS, new RowMapper<KpiModel>() {

			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setProductid(rs.getString("product_id"));
				kpiModel.setProduct(rs.getString("product"));
				kpiModel.setProduct_category(rs.getString("product_category"));
				kpiModel.setProduct_type(rs.getString("product_type"));
				kpiModel.setDupli_cat(rs.getString("product_category"));
				kpiModel.setDupli_pro(rs.getString("product"));
				return kpiModel;
			}
		}, new Object[] { kpiid, effective_date });

		return productsList;
	}
	
	/* This method is used to create a new KPI */
	//@Override
//	public int insertKPI(String kpi_name, int kpi_priority, String kpi_type, String product, String kpi_calc_type,
//			String currentUser, String custom_routine, Date effective_date, String kpiId, String product_type,
//			String product_category, String comboString, String productName, int stat, String combo_position,
//			String combo_values, String wstat_string, String include_count, String include_revenue, String feed_type)
//			throws DuplicateRecordException {
//
//		logger.info("Inside insertKPI");
//
//		// KpiProductDetailEntity saveKpiProDetail = null;
//		int count = 0;
//		int kpi_count = kpiRepository.getKpiNameCount(kpi_name);
//		// Duplicate KPI Name checking
//		if (kpi_count > 0 && stat == 1) {
//			logger.info("insertKPI Inside IF");
//			count = 2;
//			throw new DuplicateRecordException(String.valueOf("Record " + kpi_name + DUPLICATE_RECORD));
//			// throw new DuplicateRecordException(kpi_name + DUPLICATE_RECORD);
//		} else {
//			logger.info("insertKPI Inside ELSE");
//
//			logger.info("insertKPI insert effective_date = " + effective_date);
//			// If the request came through UPDATE, insert same rowid
//			// If the request came through ADD, insert incremented rowid
//			Long maxid = 0L;
//
//			if (stat == 1) {
//				maxid = Long.valueOf(kpiRepository.getMaxId() + 1);
//			} else {
//				maxid = global_kpiid;// Long.valueOf(kpiRepository.getMaxId());
//			}
//
//			count = jdbcTemplate.update(
//					"INSERT INTO c_kpi_master (kpi_id,kpi_name,kpi_type,created_dt,created_by,kpi_status,effective_date,kpi_calc_type,custom_routine,wstat_string,kpi_priority,feed_type) "
//							+ "VALUES ('" + maxid + "','" + kpi_name + "','" + kpi_type + "','" + new Date() + "','"
//							+ currentUser + "','Active','" + effective_date + "','" + kpi_calc_type + "','"
//							+ custom_routine + "','" + wstat_string + "','" + kpi_priority + "','" + feed_type + "')");
//			logger.info("count = " + count);
//
//			long kpiid = maxid;
//			logger.info("kpiid = " + kpiid);
//			logger.info("count after insert = " + count);
//
//			if (count > 0) {
//
//				if (kpi_type.equals("By Product")) {
//					logger.info("Inside By Product");
//
//					String allValue = "All";
//
//					if (product_type.equals("All~") || product_category.equals("All~") || product.equals("All~")) {
//						product_type = product_type.substring(0, product_type.length() - 1);
//						product_category = product_category.substring(0, product_category.length() - 1);
//						product = product.substring(0, product.length() - 1);
//					}
//
//					logger.info("product_type=" + product_type);
//					logger.info("product_category=" + product_category);
//					logger.info("product=" + product);
//
//					if (product_type.equals(allValue)) {
//
//						count = jdbcTemplate.update(
//								"INSERT INTO c_kpi_detail (product_type,product_category,product,kpi_id,created_dt,created_by,effective_date) "
//										+ "VALUES ('" + product_type + "','" + allValue + "','" + allValue + "','"
//										+ kpiid + "','" + new Date() + "','" + currentUser + "','" + effective_date
//										+ "')");
//					} else if (product_category.equals(allValue)) {
//
//						count = jdbcTemplate.update(
//								"INSERT INTO c_kpi_detail (product_type,product_category,product,kpi_id,created_dt,created_by,effective_date) "
//										+ "VALUES ('" + product_type + "','" + product_category + "','" + allValue
//										+ "','" + kpiid + "','" + new Date() + "','" + currentUser + "','"
//										+ effective_date + "')");
//					} else if (product.equals(allValue)) {
//
//						count = jdbcTemplate.update(
//								"INSERT INTO c_kpi_detail (product_type,product_category,product,kpi_id,created_dt,created_by,effective_date) "
//										+ "VALUES ('" + product_type + "','" + product_category + "','" + product
//										+ "','" + kpiid + "','" + new Date() + "','" + currentUser + "','"
//										+ effective_date + "')");
//					} else {
//						String pro[] = product.split("~");
//						String procat[] = product_category.split("~");
//						String protype[] = product_type.split("~");
//						int pid = 0;
//						for (int i = 0; i < pro.length; i++) {
//							logger.info("ELSE product=" + pro[i]);
//							if (pro[i].equals("All")) {
//								pid = 0;
//							} else {
//								pid = kpiRepository.getProductId(pro[i], procat[i], protype[i]);
//							}
//
//							count = jdbcTemplate.update(
//									"INSERT INTO c_kpi_detail (product_id,product_type,product_category,product,kpi_id,created_dt,created_by,effective_date) "
//											+ "VALUES ('" + pid + "','" + protype[i] + "','" + procat[i] + "','"
//											+ pro[i] + "','" + kpiid + "','" + new Date() + "','" + currentUser + "','"
//											+ effective_date + "')");
//						}
//					}
//
//				} else if (kpi_type.equals("By Combo String")) {
//
//					String cp[] = combo_position.split("~");
//					String cv[] = combo_values.split("~");
//
//					for (int k = 0; k < cp.length; k++) {
//
//						count = jdbcTemplate.update(
//								"INSERT INTO c_kpi_detail (kpi_id,combo_position,combo_values,combo_string,effective_date,created_dt,created_by) "
//										+ "VALUES ('" + kpiid + "','" + cp[k] + "','" + cv[k] + "','" + comboString
//										+ "','" + effective_date + "','" + new Date() + "','" + currentUser + "')");
//
////							KpiProductDetailEntity kpiModel = new KpiProductDetailEntity();
////							kpiModel.setKpi_id(kpiid);
////							kpiModel.setCombo_position(cp[k]);
////							kpiModel.setCombo_values(cv[k]);
////							kpiModel.setCombo_string(comboString);
////							kpiModel.setCreated_dt(new Date());
////							kpiModel.setCreated_by(currentUser);
////							kpiModel.setEffective_date(effective_date);
////							saveKpiProDetail = kpiProductDetailRepository.save(kpiModel);
//					}
//
//				} else if (kpi_type.equals("By KPI")) {
//					logger.info("By KPI kpiId = " + kpiId);
//					logger.info("By KPI effective_date = " + effective_date);
//					String kpiIdStr[] = kpiId.split("~");
//					String countStr[] = include_count.split("~");
//					String revenueStr[] = include_revenue.split("~");
//					for (int i = 0; i < kpiIdStr.length; i++) {
//
//						count = jdbcTemplate.update(
//								"INSERT INTO c_kpi_detail (kpi_id,dpdnt_kpi,effective_date,created_dt,created_by,include_count,include_revenue) "
//										+ "VALUES ('" + kpiid + "','" + Integer.parseInt(kpiIdStr[i]) + "','"
//										+ effective_date + "','" + new Date() + "','" + currentUser + "','"
//										+ countStr[i] + "','" + revenueStr[i] + "')");
//
////							KpiProductDetailEntity kpiModel = new KpiProductDetailEntity();
////							kpiModel.setKpi_id(kpiid);
////							kpiModel.setDependentKpi(Integer.parseInt(kpiIdStr[i]));
////							kpiModel.setCreated_dt(new Date());
////							kpiModel.setCreated_by(currentUser);
////							kpiModel.setEffective_date(effective_date);
////							saveKpiProDetail = kpiProductDetailRepository.save(kpiModel);
//
//					}
//				}
//			}
//
//			if (kpi_type.equals("NA")) {
//				count = 1;
//			}
////			else {
////				logger.info("saveKpiProDetail = " + saveKpiProDetail);
////				if (saveKpiProDetail == null) {
////					count = 0;
////				} else {
////					count = 1;
////				}
////			}
//			logger.info("final count = " + count);
//		}
//		return count;
//	}

}
