package com.altice.salescommission.serviceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import com.altice.salescommission.entity.KomFeedEntity;
import com.altice.salescommission.entity.KpiMasterEntity;
import com.altice.salescommission.entity.WipBuyInfoEntity;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.model.CallDetailsModel;
import com.altice.salescommission.model.ComboStringModel;
import com.altice.salescommission.queries.KomFeedQueries;
import com.altice.salescommission.repository.KomFeedRepository;
import com.altice.salescommission.repository.WibBuyInfoRepository;
import com.altice.salescommission.service.KomFeedService;

@Service
@Transactional
public class KomFeedServiceImpl implements KomFeedService, KomFeedQueries {
	private static final Logger logger = LoggerFactory.getLogger(KomFeedServiceImpl.class.getName());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private WibBuyInfoRepository wibBuyInfoRepository;
	@Autowired
	private KomFeedRepository komFeedRepository;

	@Override
	public KomFeedEntity addAdjustment(KomFeedEntity komFeedModel) {

		KomFeedEntity komFeedEntity = new KomFeedEntity();
		komFeedEntity = komFeedRepository.save(komFeedModel);

		int generatedId = komFeedRepository.getMaxId();
		logger.info("generatedId = " + generatedId);

		WipBuyInfoEntity wipBuyInfo = new WipBuyInfoEntity();

		wipBuyInfo.setKom_feed_id(generatedId);
		wipBuyInfo.setHouse(komFeedModel.getHouse());
		wipBuyInfo.setCorp(komFeedModel.getCorp());
		wipBuyInfo.setCust(komFeedModel.getCust());
		wipBuyInfo.setWordate(komFeedModel.getWordate());
		wipBuyInfo.setWfindate(komFeedModel.getWfindate());
		wibBuyInfoRepository.save(wipBuyInfo);

		return komFeedEntity;

	}

	@Override
	public KomFeedEntity addNewAdjustment(KomFeedEntity komFeedModel) {

		List<WipBuyInfoEntity> getWipByInfoDataList = wibBuyInfoRepository
				.getWipByInfoDataByKomfeedid(komFeedModel.getKomfeedid());
		logger.info("getWipByInfoDataList = " + getWipByInfoDataList);

		KomFeedEntity komFeedEntity = new KomFeedEntity();
		komFeedEntity = komFeedRepository.save(komFeedModel);

		int generatedId = komFeedRepository.getMaxId();
		logger.info("generatedId = " + generatedId);

		for (WipBuyInfoEntity model : getWipByInfoDataList) {

			WipBuyInfoEntity wipBuyInfo = new WipBuyInfoEntity();

			wipBuyInfo.setKom_feed_id(generatedId);

			wipBuyInfo.setSerty_id(model.getSerty_id());
			wipBuyInfo.setRatecd(model.getRatecd());
			wipBuyInfo.setRatesign(model.getRatesign());
			wipBuyInfo.setSercnt(model.getSercnt());
			wipBuyInfo.setSamt(model.getSamt());
			wipBuyInfo.setF_level(model.getF_level());

			wipBuyInfo.setCcorp(model.getCcorp());
			wipBuyInfo.setHouse(model.getHouse());
			wipBuyInfo.setCorp(model.getCorp());
			wipBuyInfo.setCust(model.getCust());
			wipBuyInfo.setWpcnt(model.getWpcnt());
			wipBuyInfo.setWordate(model.getWordate());
			wipBuyInfo.setOrdertime(model.getOrdertime());
			wipBuyInfo.setWidate(model.getWidate());
			wipBuyInfo.setWbdate(model.getWbdate());
			wipBuyInfo.setWfindate(model.getWfindate());
			wipBuyInfo.setWjob(model.getWjob());
			wipBuyInfo.setWstat(model.getWstat());
			wipBuyInfo.setWtech(model.getWtech());
			wipBuyInfo.setSlsrep(model.getSlsrep());
			wipBuyInfo.setWho(model.getWho());
			wipBuyInfo.setWdwo(model.getWdwo());
			wipBuyInfo.setWntrk(model.getWntrk());
			wipBuyInfo.setWcampg(model.getWcampg());
			wipBuyInfo.setWsalrn(model.getWsalrn());
			wipBuyInfo.setWordrsn(model.getWordrsn());
			wipBuyInfo.setWchrsn(model.getWchrsn());
			wipBuyInfo.setWcdrsn(model.getWcdrsn());
			wipBuyInfo.setWcycle(model.getWcycle());
			wipBuyInfo.setWpro(model.getWpro());
			wipBuyInfo.setWmode(model.getWmode());
			wipBuyInfo.setWdiscnt(model.getWdiscnt());
			wipBuyInfo.setWpdper(model.getWpdper());
			wipBuyInfo.setWperiod(model.getWperiod());
			wipBuyInfo.setFtax(model.getFtax());
			wipBuyInfo.setMgt(model.getMgt());
			wipBuyInfo.setZipcode(model.getZipcode());
			wipBuyInfo.setZip4(model.getZip4());
			wipBuyInfo.setDwell(model.getDwell());
			wipBuyInfo.setComplex(model.getComplex());
			wipBuyInfo.setCensus(model.getCensus());
			wipBuyInfo.setClust(model.getClust());
			wipBuyInfo.setGeocd(model.getGeocd());
			wipBuyInfo.setStnum(model.getStnum());
			wipBuyInfo.setFract(model.getFract());
			wipBuyInfo.setDir(model.getDir());
			wipBuyInfo.setName(model.getName());
			wipBuyInfo.setApt(model.getApt());
			wipBuyInfo.setAptn(model.getAptn());
			wipBuyInfo.setLname(model.getLname());
			wipBuyInfo.setFname(model.getFname());
			wipBuyInfo.setCtype(model.getCtype());
			wipBuyInfo.setCinfo(model.getCinfo());
			wipBuyInfo.setRareacd(model.getRareacd());
			wipBuyInfo.setRphon(model.getRphon());
			wipBuyInfo.setCycle(model.getCycle());
			wipBuyInfo.setFeed_dt(model.getFeed_dt());
			wibBuyInfoRepository.save(wipBuyInfo);
		}

		return komFeedEntity;
	}

	@Override
	public Map<String, Boolean> deleteRowONWipByInfo(int id) {
		Map<String, Boolean> response = new HashMap<>();
		try {
			wibBuyInfoRepository.deleteById((long) id);
			response.put("deleted", Boolean.TRUE);
		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public List<ComboStringModel> getProduct(ComboStringModel comboStringModel) {
		logger.info("comboStringModel = " + comboStringModel);
		try {
			List<ComboStringModel> comboStringModelList = jdbcTemplate.query(GET_PRODUCT_NAME,
					new RowMapper<ComboStringModel>() {

						@Override
						public ComboStringModel mapRow(ResultSet rs, int rowNum) throws SQLException {
							ComboStringModel comboMdl = new ComboStringModel();
							comboMdl.setProductName(rs.getString("svcs_reporting_name"));
							comboMdl.setPosition(comboStringModel.getPosition());
							return comboMdl;
						}
					},
					new Object[] { comboStringModel.getPosition(), comboStringModel.getCorp(),
							comboStringModel.getWfinDate(), comboStringModel.getWfinDate(),
							comboStringModel.getCurrentValue(), comboStringModel.getType() });
			logger.info("comboStringModelList = " + comboStringModelList);
			return (List<ComboStringModel>) comboStringModelList;
		} catch (Exception ex) {
			ex.printStackTrace();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}

	@Override
	public List<KomFeedEntity> getSalesRepIds(KomFeedEntity feedModel) {
		logger.info("feedModel = " + feedModel);
		try {

			List<KomFeedEntity> komFeedModelList = jdbcTemplate.query(GET_SALES_REPS, new RowMapper<KomFeedEntity>() {

				@Override
				public KomFeedEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					KomFeedEntity feddMdl = new KomFeedEntity();

					feddMdl.setSales_rep_id(rs.getString("sales_rep_id"));
					return feddMdl;
				}
			}, new Object[] { feedModel.getCorp(), feedModel.getHouse(), feedModel.orderFromDate,
					feedModel.orderToDate });
			logger.info("komFeedModelList = " + komFeedModelList);
			return (List<KomFeedEntity>) komFeedModelList;
		} catch (Exception ex) {
			ex.printStackTrace();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			logger.info("exception_msg = " + exception_msg);
			return null;
		}
	}

	@Override
	public Map<String, Boolean> updateKomFeed(KomFeedEntity feedModel) {
		logger.info("feedModel = " + feedModel);
		Map<String, Boolean> response = new HashMap<>();
		try {
			String sql = "update c_wip_buy_info_master set corp = ?, slsrep = ? where kom_feed_id = ?";
			jdbcTemplate.update(sql, feedModel.getCorp(), feedModel.getSales_rep_id(), feedModel.getId());

			KomFeedEntity komFeedModel = komFeedRepository.findById(feedModel.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Commission plan id not found" + feedModel.getId()));
			komFeedModel = feedModel;
			komFeedRepository.save(komFeedModel);

			response.put("updated", Boolean.TRUE);
		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public List<KomFeedEntity> getFeedResults(KomFeedEntity feedModel) {
		logger.info("feedModel = " + feedModel);

		logger.info("getCorp = " + feedModel.getCorp());
		logger.info("getHouse = " + feedModel.getHouse());
		logger.info("orderFromDate = " + feedModel.orderFromDate);
		logger.info("orderToDate = " + feedModel.orderToDate);
		logger.info("getSales_rep_id = " + feedModel.getSales_rep_id());

		int repid = 0;
		String repid1 = "0";
		if (feedModel.getSales_rep_id() != null) {
			if (feedModel.getSales_rep_id().length() > 0) {
				logger.info("Inside IF");
				repid = 0;
				repid1 = feedModel.getSales_rep_id();
			} else {
				logger.info("Inside ELSE");
				repid = 1;
				repid1 = "0";
			}
		} else {
			repid = 1;
			repid1 = "0";
		}

		try {

			List<KomFeedEntity> komFeedModelList = jdbcTemplate.query(GET_FEED_RESULTS, new RowMapper<KomFeedEntity>() {
				@Override
				public KomFeedEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					KomFeedEntity feddMdl = new KomFeedEntity();
//					WipBuyInfoEntity wipBuyInfo = new WipBuyInfoEntity();

					feddMdl.setSales_rep_id(rs.getString("sales_rep_id"));
					feddMdl.setCorp(rs.getInt("corp"));
					feddMdl.setHouse(rs.getString("house"));
					feddMdl.setCust(rs.getString("cust"));
					feddMdl.setWpcnt(rs.getString("wpcnt"));
					feddMdl.setWstat(rs.getString("wstat"));
					feddMdl.setWordate(rs.getDate("wordate"));
					feddMdl.setWddate(rs.getDate("wddate"));
					feddMdl.setWfindate(rs.getDate("wfindate"));
					feddMdl.setId(rs.getLong("kom_feed_id"));
					feddMdl.setCustname(rs.getString("custname"));

					feddMdl.setWordate_stg(rs.getString("wordate_stg"));
					feddMdl.setWddate_stg(rs.getString("wddate_stg"));
					feddMdl.setWfindate_stg(rs.getString("wfindate_stg"));
					
					feddMdl.setPrev_misc_combo_str(rs.getString("prev_misc_combo_str"));
					feddMdl.setCurr_misc_combo_str(rs.getString("curr_misc_combo_str"));
					feddMdl.setPr_status_id(rs.getInt("pr_status_id"));

					return feddMdl;
				}
			}, new Object[] { feedModel.getCorp(), feedModel.getHouse(), feedModel.orderFromDate, feedModel.orderToDate,
					repid, repid1 });
			return (List<KomFeedEntity>) komFeedModelList;

		} catch (Exception ex) {
			ex.printStackTrace();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}

	}

	@Override
	public List<KomFeedEntity> getFeedResultsExcel(KomFeedEntity feedModel) {
		logger.info("feedModel = " + feedModel);

		logger.info("getCorp = " + feedModel.getCorp());
		logger.info("getHouse = " + feedModel.getHouse());
		logger.info("orderFromDate = " + feedModel.orderFromDate);
		logger.info("orderToDate = " + feedModel.orderToDate);
		logger.info("getSales_rep_id = " + feedModel.getSales_rep_id());

		int repid = 0;
		String repid1 = "0";
		if (feedModel.getSales_rep_id() != null) {
			if (feedModel.getSales_rep_id().length() > 0) {
				logger.info("Inside IF");
				repid = 0;
				repid1 = feedModel.getSales_rep_id();
			} else {
				logger.info("Inside ELSE");
				repid = 1;
				repid1 = "0";
			}
		} else {
			repid = 1;
			repid1 = "0";
		}

		try {

			List<KomFeedEntity> komFeedModelList = jdbcTemplate.query(GET_FEED_RESULTS_EXCEL,
					new RowMapper<KomFeedEntity>() {
						@Override
						public KomFeedEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
							KomFeedEntity feddMdl = new KomFeedEntity();
							WipBuyInfoEntity wipBuyInfo = new WipBuyInfoEntity();

							feddMdl.setSales_rep_id(rs.getString("sales_rep_id"));
							feddMdl.setCorp(rs.getInt("corp"));
							feddMdl.setHouse(rs.getString("house"));
							feddMdl.setCust(rs.getString("cust"));
							feddMdl.setWpcnt(rs.getString("wpcnt"));
							feddMdl.setWstat(rs.getString("wstat"));
							feddMdl.setWordate(rs.getDate("wordate"));
							feddMdl.setWddate(rs.getDate("wddate"));
							feddMdl.setWfindate(rs.getDate("wfindate"));
							feddMdl.setId(rs.getLong("kom_feed_id"));
							feddMdl.setCustname(rs.getString("custname"));

							feddMdl.setWordate_stg(rs.getString("wordate_stg"));
							feddMdl.setWddate_stg(rs.getString("wddate_stg"));
							feddMdl.setWfindate_stg(rs.getString("wfindate_stg"));

							feddMdl.setSerty_id(rs.getString("serty_id"));
							feddMdl.setRatecd(rs.getString("ratecd"));
							feddMdl.setRatesign(rs.getString("ratesign"));
							feddMdl.setSercnt(rs.getInt("sercnt"));
							feddMdl.setSamt(rs.getDouble("samt"));
							feddMdl.setF_level(rs.getInt("f_level"));

							return feddMdl;
						}
					}, new Object[] { feedModel.getCorp(), feedModel.getHouse(), feedModel.orderFromDate,
							feedModel.orderToDate, repid, repid1 });
			return (List<KomFeedEntity>) komFeedModelList;

		} catch (Exception ex) {
			ex.printStackTrace();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}

	}

	@Override
	public List<KomFeedEntity> getKomFeedDataByRepId(int corp, String house, String salesrepid, int id) {

		logger.info("corp = " + corp);
		logger.info("house = " + house);
		logger.info("salesrepid = " + salesrepid);

		try {

			List<KomFeedEntity> komFeedModelList = jdbcTemplate.query(GET_FEED_RESULTS_BY_REP,
					new RowMapper<KomFeedEntity>() {
						@Override
						public KomFeedEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
							KomFeedEntity feddMdl = new KomFeedEntity();

							feddMdl.setSales_rep_id(rs.getString("sales_rep_id"));
							feddMdl.setCorp(rs.getInt("corp"));
							feddMdl.setHouse(rs.getString("house"));
							feddMdl.setCust(rs.getString("cust"));
							feddMdl.setWpcnt(rs.getString("wpcnt"));
							feddMdl.setWstat(rs.getString("wstat"));
							feddMdl.setWordate(rs.getDate("wordate"));
							feddMdl.setWddate(rs.getDate("wddate"));
							feddMdl.setWfindate(rs.getDate("wfindate"));
							feddMdl.setWibBuyInfoID(rs.getInt("fd_wip_buy_info_id"));

							feddMdl.setSerty_id(rs.getString("serty_id"));
							feddMdl.setRatecd(rs.getString("ratecd"));
							feddMdl.setRatesign(rs.getString("ratesign"));
							feddMdl.setSercnt(rs.getInt("sercnt"));
							feddMdl.setSamt(rs.getDouble("samt"));
							feddMdl.setF_level(rs.getInt("f_level"));
							feddMdl.setId(rs.getLong("kom_feed_id"));
							return feddMdl;
						}
					}, new Object[] { id });
			return (List<KomFeedEntity>) komFeedModelList;

		} catch (Exception ex) {
			ex.printStackTrace();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}

	}

	@Override
	public List<KomFeedEntity> getRateCodeInfo(int corp, String ratecd, String orderdt) {

		logger.info("corp = " + corp);
		logger.info("ratecd = " + ratecd);
		logger.info("orderdt = " + orderdt);

		try {

			List<KomFeedEntity> komFeedModelList = jdbcTemplate.query(GET_RATE_CODE_INFO,
					new RowMapper<KomFeedEntity>() {
						@Override
						public KomFeedEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
							KomFeedEntity feddMdl = new KomFeedEntity();

							feddMdl.setCorp(rs.getInt("corp"));
							feddMdl.setRatecd(rs.getString("rcode"));
							feddMdl.setF_level(rs.getInt("f_level"));
							return feddMdl;
						}
					}, new Object[] { corp, ratecd, orderdt });
			return (List<KomFeedEntity>) komFeedModelList;

		} catch (Exception ex) {
			ex.printStackTrace();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}

	}

	private String getReportingCenterName(String rctr, int corp) {
		String rc = komFeedRepository.getReportingCenter(rctr, corp);
		logger.info("rc = " + rc);
		return rc;
	}

	@Override
	public List<KomFeedEntity> getReportingCentersInfo(int corp, String ratecd, String orderdt) {

		logger.info("corp = " + corp);
		logger.info("ratecd = " + ratecd);
		logger.info("orderdt = " + orderdt);

		try {

			List<KomFeedEntity> komFeedModelList = jdbcTemplate.query(GET_REPORTING_CENTERS_INFO,
					new RowMapper<KomFeedEntity>() {
						@Override
						public KomFeedEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
							KomFeedEntity feddMdl = new KomFeedEntity();

							feddMdl.setRctr01(rs.getString("rctr01"));
							if (rs.getString("rctr01") != null) {
								feddMdl.setRc01(getReportingCenterName(rs.getString("rctr01"), corp));
							}

							feddMdl.setRctr02(rs.getString("rctr02"));
							if (rs.getString("rctr02") != null) {
								feddMdl.setRc02(getReportingCenterName(rs.getString("rctr02"), corp));
							}

							feddMdl.setRctr03(rs.getString("rctr03"));
							if (rs.getString("rctr03") != null) {
								feddMdl.setRc03(getReportingCenterName(rs.getString("rctr03"), corp));
							}

							feddMdl.setRctr04(rs.getString("rctr04"));
							if (rs.getString("rctr04") != null) {
								feddMdl.setRc04(getReportingCenterName(rs.getString("rctr04"), corp));
							}

							feddMdl.setRctr05(rs.getString("rctr05"));
							if (rs.getString("rctr05") != null) {
								feddMdl.setRc05(getReportingCenterName(rs.getString("rctr05"), corp));
							}

							feddMdl.setRctr06(rs.getString("rctr06"));
							if (rs.getString("rctr06") != null) {
								feddMdl.setRc06(getReportingCenterName(rs.getString("rctr06"), corp));
							}

							feddMdl.setRctr07(rs.getString("rctr07"));
							if (rs.getString("rctr07") != null) {
								feddMdl.setRc07(getReportingCenterName(rs.getString("rctr07"), corp));
							}

							feddMdl.setRctr08(rs.getString("rctr08"));
							if (rs.getString("rctr08") != null) {
								feddMdl.setRc08(getReportingCenterName(rs.getString("rctr08"), corp));
							}

							feddMdl.setRctr09(rs.getString("rctr09"));
							if (rs.getString("rctr09") != null) {
								feddMdl.setRc09(getReportingCenterName(rs.getString("rctr09"), corp));
							}

							feddMdl.setRctr10(rs.getString("rctr10"));
							if (rs.getString("rctr10") != null) {
								feddMdl.setRc10(getReportingCenterName(rs.getString("rctr10"), corp));
							}

							feddMdl.setRctr11(rs.getString("rctr11"));
							if (rs.getString("rctr11") != null) {
								feddMdl.setRc11(getReportingCenterName(rs.getString("rctr11"), corp));
							}

							feddMdl.setRctr12(rs.getString("rctr12"));
							if (rs.getString("rctr12") != null) {
								feddMdl.setRc12(getReportingCenterName(rs.getString("rctr12"), corp));
							}

							feddMdl.setRctr13(rs.getString("rctr13"));
							if (rs.getString("rctr13") != null) {
								feddMdl.setRc13(getReportingCenterName(rs.getString("rctr13"), corp));
							}

							feddMdl.setRctr14(rs.getString("rctr14"));
							if (rs.getString("rctr14") != null) {
								feddMdl.setRc14(getReportingCenterName(rs.getString("rctr14"), corp));
							}

							feddMdl.setRctr15(rs.getString("rctr15"));
							if (rs.getString("rctr15") != null) {
								feddMdl.setRc15(getReportingCenterName(rs.getString("rctr15"), corp));
							}

							feddMdl.setRctr16(rs.getString("rctr16"));
							if (rs.getString("rctr16") != null) {
								feddMdl.setRc16(getReportingCenterName(rs.getString("rctr16"), corp));
							}

							feddMdl.setRctr17(rs.getString("rctr17"));
							if (rs.getString("rctr17") != null) {
								feddMdl.setRc17(getReportingCenterName(rs.getString("rctr17"), corp));
							}

							feddMdl.setRctr18(rs.getString("rctr18"));
							if (rs.getString("rctr18") != null) {
								feddMdl.setRc18(getReportingCenterName(rs.getString("rctr18"), corp));
							}

							feddMdl.setRctr19(rs.getString("rctr19"));
							if (rs.getString("rctr19") != null) {
								feddMdl.setRc19(getReportingCenterName(rs.getString("rctr19"), corp));
							}

							feddMdl.setRctr20(rs.getString("rctr20"));
							if (rs.getString("rctr20") != null) {
								feddMdl.setRc20(getReportingCenterName(rs.getString("rctr20"), corp));
							}

							logger.info("feddMdl = " + feddMdl);
							// feddMdl.setRatecd(rs.getString("rcode"));
							return feddMdl;
						}
					}, new Object[] { corp, ratecd, orderdt });
			return komFeedModelList;

		} catch (Exception ex) {
			ex.printStackTrace();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}

	}

	@Override
	public KomFeedEntity saveWibByInfoData(KomFeedEntity feedModel) {
		logger.info("feedModel = " + feedModel);
		try {
			logger.info("feedModel.getId() = " + feedModel.getId());
			Long wid = komFeedRepository.getWIpBuyInfoId(feedModel.getId());
			logger.info("wid = " + wid);

			WipBuyInfoEntity model = wibBuyInfoRepository.findById(wid).orElseThrow(
					() -> new ResourceNotFoundException("Commission plan id not found" + feedModel.getWibBuyInfoID()));

			WipBuyInfoEntity wipBuyInfo = new WipBuyInfoEntity();

			wipBuyInfo.setSerty_id(feedModel.getSerty_id());
			wipBuyInfo.setRatecd(feedModel.getRatecd());
			wipBuyInfo.setRatesign(feedModel.getRatesign());
			wipBuyInfo.setSercnt(feedModel.getSercnt());
			wipBuyInfo.setSamt(feedModel.getSamt());
			wipBuyInfo.setF_level(feedModel.getF_level());

			wipBuyInfo.setCcorp(model.getCcorp());
			wipBuyInfo.setHouse(model.getHouse());
			wipBuyInfo.setKom_feed_id(model.getKom_feed_id());
			wipBuyInfo.setCorp(model.getCorp());
			wipBuyInfo.setCust(model.getCust());
			wipBuyInfo.setWpcnt(model.getWpcnt());
			wipBuyInfo.setWordate(model.getWordate());
			wipBuyInfo.setOrdertime(model.getOrdertime());
			wipBuyInfo.setWidate(model.getWidate());
			wipBuyInfo.setWbdate(model.getWbdate());
			wipBuyInfo.setWfindate(model.getWfindate());
			wipBuyInfo.setWjob(model.getWjob());
			wipBuyInfo.setWstat(model.getWstat());
			wipBuyInfo.setWtech(model.getWtech());
			wipBuyInfo.setSlsrep(model.getSlsrep());
			wipBuyInfo.setWho(model.getWho());
			wipBuyInfo.setWdwo(model.getWdwo());
			wipBuyInfo.setWntrk(model.getWntrk());
			wipBuyInfo.setWcampg(model.getWcampg());
			wipBuyInfo.setWsalrn(model.getWsalrn());
			wipBuyInfo.setWordrsn(model.getWordrsn());
			wipBuyInfo.setWchrsn(model.getWchrsn());
			wipBuyInfo.setWcdrsn(model.getWcdrsn());
			wipBuyInfo.setWcycle(model.getWcycle());
			wipBuyInfo.setWpro(model.getWpro());
			wipBuyInfo.setWmode(model.getWmode());
			wipBuyInfo.setWdiscnt(model.getWdiscnt());
			wipBuyInfo.setWpdper(model.getWpdper());
			wipBuyInfo.setWperiod(model.getWperiod());
			wipBuyInfo.setFtax(model.getFtax());
			wipBuyInfo.setMgt(model.getMgt());
			wipBuyInfo.setZipcode(model.getZipcode());
			wipBuyInfo.setZip4(model.getZip4());
			wipBuyInfo.setDwell(model.getDwell());
			wipBuyInfo.setComplex(model.getComplex());
			wipBuyInfo.setCensus(model.getCensus());
			wipBuyInfo.setClust(model.getClust());
			wipBuyInfo.setGeocd(model.getGeocd());
			wipBuyInfo.setStnum(model.getStnum());
			wipBuyInfo.setFract(model.getFract());
			wipBuyInfo.setDir(model.getDir());
			wipBuyInfo.setName(model.getName());
			wipBuyInfo.setApt(model.getApt());
			wipBuyInfo.setAptn(model.getAptn());
			wipBuyInfo.setLname(model.getLname());
			wipBuyInfo.setFname(model.getFname());
			wipBuyInfo.setCtype(model.getCtype());
			wipBuyInfo.setCinfo(model.getCinfo());
			wipBuyInfo.setRareacd(model.getRareacd());
			wipBuyInfo.setRphon(model.getRphon());
			wipBuyInfo.setCycle(model.getCycle());
			wipBuyInfo.setFeed_dt(model.getFeed_dt());

			wibBuyInfoRepository.save(wipBuyInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feedModel;
	}

	@Override
	public List<ComboStringModel> getProductNames(KomFeedEntity feedModel) {
		logger.info("getCorp = " + feedModel.getCorp());
		logger.info("getWfindate = " + feedModel.getWfindate());
		logger.info("getSvcsType = " + feedModel.getSvcsType());

		List<ComboStringModel> comboStringModelList = jdbcTemplate.query(GET_PRODUCT_NAMES,
				new RowMapper<ComboStringModel>() {

					@Override
					public ComboStringModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						ComboStringModel comboMdl = new ComboStringModel();
						comboMdl.setProductName(rs.getString("svcsreportingname"));
						return comboMdl;
					}
				}, new Object[] { feedModel.getCorp(), feedModel.getWfindate(), feedModel.getWfindate(),
						feedModel.getSvcsType() });
		return (List<ComboStringModel>) comboStringModelList;

	}

	@Override
	public List<ComboStringModel> getProductInfo(String productName, KomFeedEntity feedModel) {
		logger.info("productName = " + productName);
		logger.info("feedModel = " + feedModel);
		try {
			List<ComboStringModel> comboStringModelList = jdbcTemplate.query(GET_PRODUCT_INFO,
					new RowMapper<ComboStringModel>() {

						@Override
						public ComboStringModel mapRow(ResultSet rs, int rowNum) throws SQLException {
							ComboStringModel comboMdl = new ComboStringModel();
							comboMdl.setProductName(rs.getString("svcs_reporting_name"));
							comboMdl.setPosition(rs.getInt("pos"));
							comboMdl.setNewValue(rs.getString("val"));
							return comboMdl;
						}
					}, new Object[] { feedModel.getWfindate(), feedModel.getWfindate(), feedModel.getCorp(),
							productName });
			return (List<ComboStringModel>) comboStringModelList;
		} catch (Exception ex) {
			ex.printStackTrace();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}

	@Override
	public List<ComboStringModel> getProductInfo1(KomFeedEntity feedModel) {
		logger.info("getWfindate = " + feedModel.getWfindate());
		logger.info("getCorp = " + feedModel.getCorp());
		logger.info("getProductName = " + feedModel.getProductName());

		List<ComboStringModel> comboStringModelList = jdbcTemplate.query(GET_PRODUCT_INFO,
				new RowMapper<ComboStringModel>() {

					@Override
					public ComboStringModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						ComboStringModel comboMdl = new ComboStringModel();
						comboMdl.setProductName(rs.getString("svcs_reporting_name"));
						comboMdl.setPosition(rs.getInt("pos"));
						comboMdl.setNewValue(rs.getString("val"));
						return comboMdl;
					}
				}, new Object[] { feedModel.getWfindate(), feedModel.getWfindate(), feedModel.getCorp(),
						feedModel.getProductName() });
		return (List<ComboStringModel>) comboStringModelList;

	}

	@Override
	public KomFeedEntity getKomFeedDataById(int komfeedid) {
		logger.info("komfeedid = " + komfeedid);
		Long idAsLong = Long.valueOf(komfeedid);
		KomFeedEntity komFeedModel = new KomFeedEntity();

		komFeedModel = komFeedRepository.findById(idAsLong)
				.orElseThrow(() -> new ResourceNotFoundException("KOM FeedID not found: " + idAsLong));
		System.out.println(komFeedModel);

		return komFeedModel;
	}

	@Override
	public KomFeedEntity getKomFeedDataByCorpHouseRepId(int corp, String house, String salesrepid) {
		logger.info("Inside getKomFeedDataByCorpHouseRepId");
		KomFeedEntity komFeedModel = new KomFeedEntity();
		try {
			komFeedModel = komFeedRepository.getKomFeedDataByCorpHouseRepId(corp, house, salesrepid);
			logger.info("komFeedModel = " + komFeedModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return komFeedModel;
	}

	@Override
	public List<KomFeedEntity> getOOLDetails() {

		List<KomFeedEntity> getmagainstList = jdbcTemplate.query(GET_OOL_LIST, new RowMapper<KomFeedEntity>() {
			@Override
			public KomFeedEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				KomFeedEntity komFeedModel = new KomFeedEntity();
				komFeedModel.setDescrip1(rs.getString("descrip1"));
				komFeedModel.setDescrip2(rs.getString("descrip2"));
				komFeedModel.setKom_code(rs.getInt("kom_code"));
				return komFeedModel;
			}
		});
		return getmagainstList;

	}

	public List<CallDetailsModel> getCallDetailsByAll(CallDetailsModel callDtlModel) {
		List<CallDetailsModel> CallDetailsModelList = jdbcTemplate.query(GET_CALLDATA_BY_ALL,
				new RowMapper<CallDetailsModel>() {
					@Override
					public CallDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						CallDetailsModel callMdl = new CallDetailsModel();

						callMdl.setSales_rep_id(rs.getString("sales_rep_id"));
						callMdl.setNode_name(rs.getString("node_name"));
						callMdl.setAba(rs.getInt("aba"));
						callMdl.setAht(rs.getInt("aht"));
						callMdl.setCall_end_time(rs.getTimestamp("call_end_time").toString());
						callMdl.setCall_start_time(rs.getTimestamp("call_start_time").toString());
						callMdl.setCall_type(rs.getString("call_type"));
						callMdl.setCrc_first_name(rs.getString("crc_first_name"));
						callMdl.setCrc_last_name(rs.getString("crc_last_name"));
						callMdl.setDay_part(rs.getTimestamp("day_part").toString());
						callMdl.setDept(rs.getString("dept"));
						callMdl.setEaid(rs.getInt("eaid"));
						callMdl.setExtn_nbr(rs.getInt("extn_nbr"));

						callMdl.setHold_time(rs.getInt("hold_time"));
						callMdl.setInbound_outbound_call_flag(rs.getString("inbound_outbound_call_flag"));
						callMdl.setKom_call_id(rs.getInt("kom_call_id"));
						callMdl.setKom_last_modified_date(rs.getTimestamp("kom_last_modified_date").toString());

						callMdl.setLang(rs.getString("lang"));
						callMdl.setLoad_date(rs.getTimestamp("load_date").toString());
						callMdl.setLocal_termination_time_id(rs.getInt("local_termination_time_id"));
						callMdl.setNch(rs.getInt("nch"));
						callMdl.setNco(rs.getInt("nco"));

						callMdl.setNetwork_id(rs.getString("network_id"));
						callMdl.setNode_id(rs.getInt("node_id"));
						callMdl.setNto(rs.getInt("nto"));
						callMdl.setPr_status_id(rs.getInt("pr_status_id"));
						callMdl.setQueue_time(rs.getInt("queue_time"));
						callMdl.setReport_date(rs.getDate("report_date").toString());
						callMdl.setTalk_time(rs.getInt("talk_time"));
						callMdl.setTrack_node(rs.getInt("track_node"));
						callMdl.setTransfer_group(rs.getString("transfer_group"));
						callMdl.setSc_emp_id(rs.getInt("sc_emp_id"));

						return callMdl;
					}
				}, new Object[] { callDtlModel.getKom_call_id() });
		return (List<CallDetailsModel>) CallDetailsModelList;
	}

	private List<CallDetailsModel> getCallDetailsByFdRepID(CallDetailsModel callDtlModel) {

		List<CallDetailsModel> CallDetailsModelList = jdbcTemplate.query(GET_CALLDATA_BY_CALLID,
				new RowMapper<CallDetailsModel>() {
					@Override
					public CallDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						CallDetailsModel callMdl = new CallDetailsModel();

						callMdl.setSales_rep_id(rs.getString("sales_rep_id"));
						callMdl.setNode_name(rs.getString("node_name"));
						callMdl.setAba(rs.getInt("aba"));
						callMdl.setAht(rs.getInt("aht"));
						callMdl.setCall_end_time(rs.getTimestamp("call_end_time").toString());
						callMdl.setCall_start_time(rs.getTimestamp("call_start_time").toString());
						callMdl.setCall_type(rs.getString("call_type"));
						callMdl.setCrc_first_name(rs.getString("crc_first_name"));
						callMdl.setCrc_last_name(rs.getString("crc_last_name"));
						callMdl.setDay_part(rs.getTimestamp("day_part").toString());
						callMdl.setDept(rs.getString("dept"));
						callMdl.setEaid(rs.getInt("eaid"));
						callMdl.setExtn_nbr(rs.getInt("extn_nbr"));

						callMdl.setHold_time(rs.getInt("hold_time"));
						callMdl.setInbound_outbound_call_flag(rs.getString("inbound_outbound_call_flag"));
						callMdl.setKom_call_id(rs.getInt("kom_call_id"));
						callMdl.setKom_last_modified_date(rs.getTimestamp("kom_last_modified_date").toString());

						callMdl.setLang(rs.getString("lang"));
						callMdl.setLoad_date(rs.getTimestamp("load_date").toString());
						callMdl.setLocal_termination_time_id(rs.getInt("local_termination_time_id"));
						callMdl.setNch(rs.getInt("nch"));
						callMdl.setNco(rs.getInt("nco"));

						callMdl.setNetwork_id(rs.getString("network_id"));
						callMdl.setNode_id(rs.getInt("node_id"));
						callMdl.setNto(rs.getInt("nto"));
						callMdl.setPr_status_id(rs.getInt("pr_status_id"));
						callMdl.setQueue_time(rs.getInt("queue_time"));
						callMdl.setReport_date(rs.getDate("report_date").toString());
						callMdl.setTalk_time(rs.getInt("talk_time"));
						callMdl.setTrack_node(rs.getInt("track_node"));
						callMdl.setTransfer_group(rs.getString("transfer_group"));
						callMdl.setSc_emp_id(rs.getInt("sc_emp_id"));
						return callMdl;
					}
				}, new Object[] { callDtlModel.getKom_call_id() });
		return (List<CallDetailsModel>) CallDetailsModelList;
	}

	private List<CallDetailsModel> getCallDetailsByFdCallID(CallDetailsModel callDtlModel) {

		List<CallDetailsModel> CallDetailsModelList = jdbcTemplate.query(GET_CALLDATA_BY_FDCALLID,
				new RowMapper<CallDetailsModel>() {
					@Override
					public CallDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						CallDetailsModel callMdl = new CallDetailsModel();

						callMdl.setSales_rep_id(rs.getString("sales_rep_id"));
						callMdl.setNode_name(rs.getString("node_name"));
						callMdl.setAba(rs.getInt("aba"));
						callMdl.setAht(rs.getInt("aht"));
						callMdl.setCall_end_time(rs.getTimestamp("call_end_time").toString());
						callMdl.setCall_start_time(rs.getTimestamp("call_start_time").toString());
						callMdl.setCall_type(rs.getString("call_type"));
						callMdl.setCrc_first_name(rs.getString("crc_first_name"));
						callMdl.setCrc_last_name(rs.getString("crc_last_name"));
						callMdl.setDay_part(rs.getTimestamp("day_part").toString());
						callMdl.setDept(rs.getString("dept"));
						callMdl.setEaid(rs.getInt("eaid"));
						callMdl.setExtn_nbr(rs.getInt("extn_nbr"));

						callMdl.setHold_time(rs.getInt("hold_time"));
						callMdl.setInbound_outbound_call_flag(rs.getString("inbound_outbound_call_flag"));
						callMdl.setKom_call_id(rs.getInt("kom_call_id"));
						callMdl.setKom_last_modified_date(rs.getTimestamp("kom_last_modified_date").toString());

						callMdl.setLang(rs.getString("lang"));
						callMdl.setLoad_date(rs.getTimestamp("load_date").toString());
						callMdl.setLocal_termination_time_id(rs.getInt("local_termination_time_id"));
						callMdl.setNch(rs.getInt("nch"));
						callMdl.setNco(rs.getInt("nco"));

						callMdl.setNetwork_id(rs.getString("network_id"));
						callMdl.setNode_id(rs.getInt("node_id"));
						callMdl.setNto(rs.getInt("nto"));
						callMdl.setPr_status_id(rs.getInt("pr_status_id"));
						callMdl.setQueue_time(rs.getInt("queue_time"));
						callMdl.setReport_date(rs.getDate("report_date").toString());
						callMdl.setTalk_time(rs.getInt("talk_time"));
						callMdl.setTrack_node(rs.getInt("track_node"));
						callMdl.setTransfer_group(rs.getString("transfer_group"));
						callMdl.setSc_emp_id(rs.getInt("sc_emp_id"));
						return callMdl;
					}
				}, new Object[] { callDtlModel.getFeedFromDt(), callDtlModel.getFeedToDt(),
						callDtlModel.getKom_call_id() });
		return (List<CallDetailsModel>) CallDetailsModelList;
	}

	private List<CallDetailsModel> getCallDetailsByFd(CallDetailsModel callDtlModel) {

		List<CallDetailsModel> CallDetailsModelList = jdbcTemplate.query(GET_CALLDATA_BY_FD,
				new RowMapper<CallDetailsModel>() {
					@Override
					public CallDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						CallDetailsModel callMdl = new CallDetailsModel();

						callMdl.setSales_rep_id(rs.getString("sales_rep_id"));
						callMdl.setNode_name(rs.getString("node_name"));
						callMdl.setAba(rs.getInt("aba"));
						callMdl.setAht(rs.getInt("aht"));
						callMdl.setCall_end_time(rs.getTimestamp("call_end_time").toString());
						callMdl.setCall_start_time(rs.getTimestamp("call_start_time").toString());
						callMdl.setCall_type(rs.getString("call_type"));
						callMdl.setCrc_first_name(rs.getString("crc_first_name"));
						callMdl.setCrc_last_name(rs.getString("crc_last_name"));
						callMdl.setDay_part(rs.getTimestamp("day_part").toString());
						callMdl.setDept(rs.getString("dept"));
						callMdl.setEaid(rs.getInt("eaid"));
						callMdl.setExtn_nbr(rs.getInt("extn_nbr"));

						callMdl.setHold_time(rs.getInt("hold_time"));
						callMdl.setInbound_outbound_call_flag(rs.getString("inbound_outbound_call_flag"));
						callMdl.setKom_call_id(rs.getInt("kom_call_id"));
						callMdl.setKom_last_modified_date(rs.getTimestamp("kom_last_modified_date").toString());

						callMdl.setLang(rs.getString("lang"));
						callMdl.setLoad_date(rs.getTimestamp("load_date").toString());
						callMdl.setLocal_termination_time_id(rs.getInt("local_termination_time_id"));
						callMdl.setNch(rs.getInt("nch"));
						callMdl.setNco(rs.getInt("nco"));

						callMdl.setNetwork_id(rs.getString("network_id"));
						callMdl.setNode_id(rs.getInt("node_id"));
						callMdl.setNto(rs.getInt("nto"));
						callMdl.setPr_status_id(rs.getInt("pr_status_id"));
						callMdl.setQueue_time(rs.getInt("queue_time"));
						callMdl.setReport_date(rs.getDate("report_date").toString());
						callMdl.setTalk_time(rs.getInt("talk_time"));
						callMdl.setTrack_node(rs.getInt("track_node"));
						callMdl.setTransfer_group(rs.getString("transfer_group"));
						callMdl.setSc_emp_id(rs.getInt("sc_emp_id"));
						return callMdl;
					}
				}, new Object[] { callDtlModel.getFeedFromDt(), callDtlModel.getFeedToDt() });
		return (List<CallDetailsModel>) CallDetailsModelList;
	}

	private List<CallDetailsModel> getCallDetailsByRepCallID(CallDetailsModel callDtlModel) {

		List<CallDetailsModel> CallDetailsModelList = jdbcTemplate.query(GET_CALLDATA_BY_REPCALLID,
				new RowMapper<CallDetailsModel>() {
					@Override
					public CallDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						CallDetailsModel callMdl = new CallDetailsModel();

						callMdl.setSales_rep_id(rs.getString("sales_rep_id"));
						callMdl.setNode_name(rs.getString("node_name"));
						callMdl.setAba(rs.getInt("aba"));
						callMdl.setAht(rs.getInt("aht"));
						callMdl.setCall_end_time(rs.getTimestamp("call_end_time").toString());
						callMdl.setCall_start_time(rs.getTimestamp("call_start_time").toString());
						callMdl.setCall_type(rs.getString("call_type"));
						callMdl.setCrc_first_name(rs.getString("crc_first_name"));
						callMdl.setCrc_last_name(rs.getString("crc_last_name"));
						callMdl.setDay_part(rs.getTimestamp("day_part").toString());
						callMdl.setDept(rs.getString("dept"));
						callMdl.setEaid(rs.getInt("eaid"));
						callMdl.setExtn_nbr(rs.getInt("extn_nbr"));

						callMdl.setHold_time(rs.getInt("hold_time"));
						callMdl.setInbound_outbound_call_flag(rs.getString("inbound_outbound_call_flag"));
						callMdl.setKom_call_id(rs.getInt("kom_call_id"));
						callMdl.setKom_last_modified_date(rs.getTimestamp("kom_last_modified_date").toString());

						callMdl.setLang(rs.getString("lang"));
						callMdl.setLoad_date(rs.getTimestamp("load_date").toString());
						callMdl.setLocal_termination_time_id(rs.getInt("local_termination_time_id"));
						callMdl.setNch(rs.getInt("nch"));
						callMdl.setNco(rs.getInt("nco"));

						callMdl.setNetwork_id(rs.getString("network_id"));
						callMdl.setNode_id(rs.getInt("node_id"));
						callMdl.setNto(rs.getInt("nto"));
						callMdl.setPr_status_id(rs.getInt("pr_status_id"));
						callMdl.setQueue_time(rs.getInt("queue_time"));
						callMdl.setReport_date(rs.getDate("report_date").toString());
						callMdl.setTalk_time(rs.getInt("talk_time"));
						callMdl.setTrack_node(rs.getInt("track_node"));
						callMdl.setTransfer_group(rs.getString("transfer_group"));
						callMdl.setSc_emp_id(rs.getInt("sc_emp_id"));
						return callMdl;
					}
				}, new Object[] { callDtlModel.getSales_rep_id(), callDtlModel.getKom_call_id() });
		return (List<CallDetailsModel>) CallDetailsModelList;
	}

	private List<CallDetailsModel> getCallDetailsBySalsRepID(CallDetailsModel callDtlModel) {

		List<CallDetailsModel> CallDetailsModelList = jdbcTemplate.query(GET_CALLDATA_BY_REPID,
				new RowMapper<CallDetailsModel>() {
					@Override
					public CallDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						CallDetailsModel callMdl = new CallDetailsModel();

						callMdl.setSales_rep_id(rs.getString("sales_rep_id"));
						callMdl.setNode_name(rs.getString("node_name"));
						callMdl.setAba(rs.getInt("aba"));
						callMdl.setAht(rs.getInt("aht"));
						callMdl.setCall_end_time(rs.getTimestamp("call_end_time").toString());
						callMdl.setCall_start_time(rs.getTimestamp("call_start_time").toString());
						callMdl.setCall_type(rs.getString("call_type"));
						callMdl.setCrc_first_name(rs.getString("crc_first_name"));
						callMdl.setCrc_last_name(rs.getString("crc_last_name"));
						callMdl.setDay_part(rs.getTimestamp("day_part").toString());
						callMdl.setDept(rs.getString("dept"));
						callMdl.setEaid(rs.getInt("eaid"));
						callMdl.setExtn_nbr(rs.getInt("extn_nbr"));

						callMdl.setHold_time(rs.getInt("hold_time"));
						callMdl.setInbound_outbound_call_flag(rs.getString("inbound_outbound_call_flag"));
						callMdl.setKom_call_id(rs.getInt("kom_call_id"));
						callMdl.setKom_last_modified_date(rs.getTimestamp("kom_last_modified_date").toString());

						callMdl.setLang(rs.getString("lang"));
						callMdl.setLoad_date(rs.getTimestamp("load_date").toString());
						callMdl.setLocal_termination_time_id(rs.getInt("local_termination_time_id"));
						callMdl.setNch(rs.getInt("nch"));
						callMdl.setNco(rs.getInt("nco"));

						callMdl.setNetwork_id(rs.getString("network_id"));
						callMdl.setNode_id(rs.getInt("node_id"));
						callMdl.setNto(rs.getInt("nto"));
						callMdl.setPr_status_id(rs.getInt("pr_status_id"));
						callMdl.setQueue_time(rs.getInt("queue_time"));
						callMdl.setReport_date(rs.getTimestamp("report_date").toString());
						callMdl.setTalk_time(rs.getInt("talk_time"));
						callMdl.setTrack_node(rs.getInt("track_node"));
						callMdl.setTransfer_group(rs.getString("transfer_group"));
						callMdl.setSc_emp_id(rs.getInt("sc_emp_id"));
						return callMdl;
					}
				}, new Object[] { callDtlModel.getSales_rep_id() });
		return (List<CallDetailsModel>) CallDetailsModelList;
	}

	public List<CallDetailsModel> getCallDetailsByCallId(CallDetailsModel callDtlModel) {
		List<CallDetailsModel> CallDetailsModelList = jdbcTemplate.query(GET_CALLDATA_BY_CALLID,
				new RowMapper<CallDetailsModel>() {
					@Override
					public CallDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						CallDetailsModel callMdl = new CallDetailsModel();

						callMdl.setSales_rep_id(rs.getString("sales_rep_id"));
						callMdl.setNode_name(rs.getString("node_name"));
						callMdl.setAba(rs.getInt("aba"));
						callMdl.setAht(rs.getInt("aht"));
						callMdl.setCall_end_time(rs.getTimestamp("call_end_time").toString());
						callMdl.setCall_start_time(rs.getTimestamp("call_start_time").toString());
						callMdl.setCall_type(rs.getString("call_type"));
						callMdl.setCrc_first_name(rs.getString("crc_first_name"));
						callMdl.setCrc_last_name(rs.getString("crc_last_name"));
						callMdl.setDay_part(rs.getTimestamp("day_part").toString());
						callMdl.setDept(rs.getString("dept"));
						callMdl.setEaid(rs.getInt("eaid"));
						callMdl.setExtn_nbr(rs.getInt("extn_nbr"));

						callMdl.setHold_time(rs.getInt("hold_time"));
						callMdl.setInbound_outbound_call_flag(rs.getString("inbound_outbound_call_flag"));
						callMdl.setKom_call_id(rs.getInt("kom_call_id"));
						callMdl.setKom_last_modified_date(rs.getTimestamp("kom_last_modified_date").toString());

						callMdl.setLang(rs.getString("lang"));
						callMdl.setLoad_date(rs.getTimestamp("load_date").toString());
						callMdl.setLocal_termination_time_id(rs.getInt("local_termination_time_id"));
						callMdl.setNch(rs.getInt("nch"));
						callMdl.setNco(rs.getInt("nco"));

						callMdl.setNetwork_id(rs.getString("network_id"));
						callMdl.setNode_id(rs.getInt("node_id"));
						callMdl.setNto(rs.getInt("nto"));
						callMdl.setPr_status_id(rs.getInt("pr_status_id"));
						callMdl.setQueue_time(rs.getInt("queue_time"));
						callMdl.setReport_date(rs.getDate("report_date").toString());
						callMdl.setTalk_time(rs.getInt("talk_time"));
						callMdl.setTrack_node(rs.getInt("track_node"));
						callMdl.setTransfer_group(rs.getString("transfer_group"));
						callMdl.setSc_emp_id(rs.getInt("sc_emp_id"));

						return callMdl;
					}
				}, new Object[] { callDtlModel.getKom_call_id() });
		return (List<CallDetailsModel>) CallDetailsModelList;
	}

	@Override
	public List<CallDetailsModel> getCallDetails(CallDetailsModel callDtlModel) {
		try {
			if (callDtlModel.getFeedFromDt() != null && callDtlModel.getFeedToDt() != null
					&& callDtlModel.getSales_rep_id() != null && callDtlModel.getSales_rep_id() != ""
					&& callDtlModel.getKom_call_id() != null) {
				return getCallDetailsByAll(callDtlModel);
			} else if (callDtlModel.getFeedFromDt() != null && callDtlModel.getFeedToDt() != null
					&& callDtlModel.getSales_rep_id() != null) {
				return getCallDetailsByFdRepID(callDtlModel);
			} else if (callDtlModel.getFeedFromDt() != null && callDtlModel.getFeedToDt() != null
					&& callDtlModel.getKom_call_id() != null) {
				return getCallDetailsByFdCallID(callDtlModel);
			} else if (callDtlModel.getFeedFromDt() != null && callDtlModel.getFeedToDt() != null) {
				return getCallDetailsByFd(callDtlModel);
			} else if (callDtlModel.getSales_rep_id() != null && callDtlModel.getKom_call_id() != null) {
				return getCallDetailsByRepCallID(callDtlModel);
			} else if (callDtlModel.getSales_rep_id() != null)
				return getCallDetailsBySalsRepID(callDtlModel);
			else
				return getCallDetailsByCallId(callDtlModel);

		} catch (Exception ex) {
			ex.printStackTrace();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}

	@Override
	public Map<String, Boolean> deleteCallByKomCallID(int id) {
		Map<String, Boolean> response = new HashMap<>();

		try {

			jdbcTemplate.update("DELETE FROM scopmgr.kom_call_daypart_details WHERE kom_call_id = ?",
					new Object[] { id });
			response.put("deleted", Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public CallDetailsModel saveCallDetails(CallDetailsModel callDtlModel) {
		try {
			String sql = "select  max(kom_call_id) as callId   from scopmgr.kom_call_daypart_details ";
			int komCallId = jdbcTemplate.queryForObject(sql, Integer.class);
			int komCalId = komCallId + 1;

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
			Date parsedDate = dateFormat.parse(callDtlModel.getReport_date());
			Timestamp report_timestamp = new java.sql.Timestamp(parsedDate.getTime());

			Date start_parsedDate = dateFormat.parse(callDtlModel.getCall_end_time());
			Timestamp callStart_timestamp = new java.sql.Timestamp(start_parsedDate.getTime());

			Date end_parsedDate = dateFormat.parse(callDtlModel.getCall_start_time());
			Timestamp callend_timestamp = new java.sql.Timestamp(end_parsedDate.getTime());

			Date modify_parsedDate = dateFormat.parse(callDtlModel.getKom_last_modified_date());
			Timestamp modify_timestamp = new java.sql.Timestamp(modify_parsedDate.getTime());

			Date load_parsedDate = dateFormat.parse(callDtlModel.getLoad_date());
			Timestamp load_timestamp = new java.sql.Timestamp(load_parsedDate.getTime());

			Date daypart_parsedDate = dateFormat.parse(callDtlModel.getDay_part());
			Timestamp day_part_timestamp = new java.sql.Timestamp(daypart_parsedDate.getTime());

			// System.out.println(report_timestamp);
			jdbcTemplate.update("INSERT INTO scopmgr.kom_call_daypart_details\r\n"
					+ "(kom_call_id, report_date, node_id, node_name, track_node, dept, call_type, transfer_group, eaid, queue_time, talk_time, hold_time, crc_first_name, crc_last_name,"
					+ " nco, nch, aba, aht, nto, lang, inbound_outbound_call_flag, extn_nbr, local_termination_time_id, "
					+ "call_end_time, call_start_time, pr_status_id, sales_rep_id, sc_emp_id, day_part, kom_last_modified_date, load_date, network_id)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ",
					new Object[] { komCalId, report_timestamp, callDtlModel.getNode_id(), callDtlModel.getNode_name(),
							callDtlModel.getTrack_node(), callDtlModel.getDept(), callDtlModel.getCall_type(),
							callDtlModel.getTransfer_group(), callDtlModel.getEaid(), callDtlModel.getQueue_time(),
							callDtlModel.getTalk_time(), callDtlModel.getHold_time(), callDtlModel.getCrc_first_name(),
							callDtlModel.getCrc_last_name(), callDtlModel.getNco(), callDtlModel.getNch(),
							callDtlModel.getAba(), callDtlModel.getAht(), callDtlModel.getNto(), callDtlModel.getLang(),
							callDtlModel.getInbound_outbound_call_flag(), callDtlModel.getExtn_nbr(),
							callDtlModel.getLocal_termination_time_id(), callend_timestamp, callStart_timestamp,
							callDtlModel.getPr_status_id(), callDtlModel.getSales_rep_id(), callDtlModel.getSc_emp_id(),
							day_part_timestamp, modify_timestamp, load_timestamp, callDtlModel.getNetwork_id() });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callDtlModel;
	}

	@Override
	public CallDetailsModel updateCallDetails(CallDetailsModel callDtlModel) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
			Date parsedDate = dateFormat.parse(callDtlModel.getReport_date());
			Timestamp report_timestamp = new java.sql.Timestamp(parsedDate.getTime());

			Date start_parsedDate = dateFormat.parse(callDtlModel.getCall_end_time());
			Timestamp callStart_timestamp = new java.sql.Timestamp(start_parsedDate.getTime());

			Date end_parsedDate = dateFormat.parse(callDtlModel.getCall_start_time());
			Timestamp callend_timestamp = new java.sql.Timestamp(end_parsedDate.getTime());

			Date modify_parsedDate = dateFormat.parse(callDtlModel.getKom_last_modified_date());
			Timestamp modify_timestamp = new java.sql.Timestamp(modify_parsedDate.getTime());

			Date load_parsedDate = dateFormat.parse(callDtlModel.getLoad_date());
			Timestamp load_timestamp = new java.sql.Timestamp(load_parsedDate.getTime());

			Date daypart_parsedDate = dateFormat.parse(callDtlModel.getDay_part());
			Timestamp day_part_timestamp = new java.sql.Timestamp(daypart_parsedDate.getTime());

			jdbcTemplate.update("UPDATE scopmgr.kom_call_daypart_details\r\n"
					+ "SET report_date=?, node_id=?, node_name=?, track_node=?, dept=?, call_type=?, transfer_group=?, eaid=?, queue_time=?, talk_time=?, "
					+ "hold_time=?, crc_first_name=?, crc_last_name=?, nco=?, nch=?, aba=?, aht=?, nto=?, lang=?, inbound_outbound_call_flag=?, extn_nbr=?, local_termination_time_id=?,"
					+ " call_end_time=?, call_start_time=?, pr_status_id=?, sales_rep_id=?, sc_emp_id=?, day_part=?, kom_last_modified_date=?, load_date=?, network_id=? where  kom_call_id=?; ",
					new Object[] { report_timestamp, callDtlModel.getNode_id(), callDtlModel.getNode_name(),
							callDtlModel.getTrack_node(), callDtlModel.getDept(), callDtlModel.getCall_type(),
							callDtlModel.getTransfer_group(), callDtlModel.getEaid(), callDtlModel.getQueue_time(),
							callDtlModel.getTalk_time(), callDtlModel.getHold_time(), callDtlModel.getCrc_first_name(),
							callDtlModel.getCrc_last_name(), callDtlModel.getNco(), callDtlModel.getNch(),
							callDtlModel.getAba(), callDtlModel.getAht(), callDtlModel.getNto(), callDtlModel.getLang(),
							callDtlModel.getInbound_outbound_call_flag(), callDtlModel.getExtn_nbr(),
							callDtlModel.getLocal_termination_time_id(), callend_timestamp, callStart_timestamp,
							callDtlModel.getPr_status_id(), callDtlModel.getSales_rep_id(), callDtlModel.getSc_emp_id(),
							day_part_timestamp, modify_timestamp, load_timestamp, callDtlModel.getNetwork_id(),
							callDtlModel.getKom_call_id() });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callDtlModel;
	}

}
