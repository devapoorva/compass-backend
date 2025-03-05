package com.altice.salescommission.serviceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.altice.salescommission.model.HomeModel;
import com.altice.salescommission.queries.HomeQueries;
import com.altice.salescommission.repository.EmployeeCorpRepository;
import com.altice.salescommission.service.HomeService;

@Transactional
@Service
public class HomeServiceImpl implements HomeService, HomeQueries {

	private static final Logger logger = LoggerFactory.getLogger(HomeServiceImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EmployeeCorpRepository employeeCorpRepository;

	@Override
	public List<HomeModel> getDisputeStatus(int complanid, int calrunid) {
		logger.info("complanid = " + complanid);
		logger.info("calrunid = " + calrunid);
		List<HomeModel> productTypesList = jdbcTemplate.query(GET_DISPUTE_CLOSSE_DATE, new RowMapper<HomeModel>() {

			@Override
			public HomeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				HomeModel homeModel = new HomeModel();

				homeModel.setDispute_close_dt(rs.getDate("dispute_close_dt"));

				return homeModel;
			}
		}, new Object[] { complanid, calrunid });
		return productTypesList;
	}

	@Override
	public List<HomeModel> getCommPlansList(String empid, String role) {
		logger.info("Empid = " + empid);
		logger.info("role = " + role);
		List<HomeModel> productTypesList = jdbcTemplate.query(GET_COMM_PLANS_LIST, new RowMapper<HomeModel>() {

			@Override
			public HomeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				HomeModel homeModel = new HomeModel();

				homeModel.setScempid(rs.getString("sc_emp_id"));
				homeModel.setComm_plan_id(rs.getInt("comm_plan_id"));
				homeModel.setCommissionplan(rs.getString("commission_plan"));

				homeModel.setCommPlanId(rs.getInt("comm_plan_id"));
				homeModel.setCommPlan(rs.getString("commission_plan"));

				homeModel.setSales_channel(rs.getString("sales_channel_desc"));
				homeModel.setComm_img_str(rs.getString("comm_img"));
				homeModel.setEffective_date(rs.getDate("effective_date"));
				homeModel.setUser_role(rs.getString("user_role"));
				return homeModel;
			}
		}, new Object[] { empid });
		return productTypesList;
	}
	
	@Override
	public List<HomeModel> getHomeCommPlansList(String empid, String role) {
		logger.info("Empid = " + empid);
		logger.info("role = " + role);
		List<HomeModel> productTypesList = jdbcTemplate.query(GET_HOME_COMM_PLANS_LIST, new RowMapper<HomeModel>() {

			@Override
			public HomeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				HomeModel homeModel = new HomeModel();

				homeModel.setScempid(rs.getString("sc_emp_id"));
				homeModel.setComm_plan_id(rs.getInt("comm_plan_id"));
				homeModel.setCommissionplan(rs.getString("commission_plan"));

				homeModel.setCommPlanId(rs.getInt("comm_plan_id"));
				homeModel.setCommPlan(rs.getString("commission_plan"));

				homeModel.setSales_channel(rs.getString("sales_channel_desc"));
				homeModel.setComm_img_str(rs.getString("comm_img"));
				homeModel.setEffective_date(rs.getDate("effective_date"));
				homeModel.setUser_role(rs.getString("user_role"));
				return homeModel;
			}
		}, new Object[] { empid });
		return productTypesList;
	}

	@Override
	public List<HomeModel> getCalrunidsList(String scempid) {
		List<HomeModel> productTypesList = jdbcTemplate.query(GET_CALRUNIDS_LIST, new RowMapper<HomeModel>() {

			@Override
			public HomeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				HomeModel homeModel = new HomeModel();

				String calyear = rs.getString("cal_run_id").substring(0, 4).trim();

				homeModel.setCommyear(rs.getString("cal_run_id"));
				homeModel.setCalyear(calyear);
				return homeModel;
			}
		}, new Object[] { scempid });
		return productTypesList;
	}

	private List<HomeModel> getCalrunidsByYearList(String scempid, String calyear) {
		System.out.println("calyear = " + calyear);
		List<HomeModel> productTypesList = jdbcTemplate.query(GET_CALRUNIDS_BY_YEAR_LIST, new RowMapper<HomeModel>() {

			@Override
			public HomeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				HomeModel homeModel = new HomeModel();

				String calyear = rs.getString("cal_run_id").substring(0, 4).trim();

				homeModel.setCommyear(rs.getString("cal_run_id"));
				homeModel.setCalyear(calyear);
				return homeModel;
			}
		}, new Object[] { scempid, calyear });
		return productTypesList;
	}

	@Override
	public List<HomeModel> getCommRevList(String calyear, String scempid, int comm_plan_id) {

		logger.info("calyear = " + calyear);
		logger.info("scempid = " + scempid);
		logger.info("comm_plan_id = " + comm_plan_id);

		List<HomeModel> calrunidsByYearList = getCalrunidsByYearList(scempid, "%" + calyear + "%");
		logger.info("calrunidsByYearList = " + calrunidsByYearList);
		
		

		List<HomeModel> productTypesList = new ArrayList<HomeModel>();
		List<HomeModel> productTypesList2 = new ArrayList<HomeModel>();

		for (HomeModel homeModel : calrunidsByYearList) {
			logger.info("homeModel.getCommyear() = " + homeModel.getCommyear());

			productTypesList = jdbcTemplate.query(GET_COMM_REV_LIST, new RowMapper<HomeModel>() {

				@Override
				public HomeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
					HomeModel homeModel = new HomeModel();

					homeModel.setCommissionval(rs.getString("commission_val"));
					homeModel.setRevenueval(rs.getString("revenue"));
					homeModel.setComm_plan_id(rs.getInt("comm_plan_id"));
					homeModel.setCalendar_type(rs.getString("calendar_type"));
					homeModel.setEdate(rs.getString("effective_date"));
					homeModel.setValid_from_dt(rs.getDate("valid_from_dt"));
					homeModel.setValid_to_dt(rs.getDate("valid_to_dt"));
					homeModel.setCalc_close_flag(rs.getString("calc_close_flag"));

					String calyear = rs.getString("cal_run_id").substring(0, 4).trim();
					logger.info("cal_run_id = "+rs.getString("cal_run_id"));
					String calmon = rs.getString("cal_run_id").substring(4, 6).trim();
					logger.info("calmon = "+calmon);

					if (calmon.equals("01")) {
						calmon = "Jan-" + calyear;
					} else if (calmon.equals("02")) {
						calmon = "Feb-" + calyear;
					} else if (calmon.equals("03")) {
						calmon = "Mar-" + calyear;
					} else if (calmon.equals("04")) {
						calmon = "Apr-" + calyear;
					} else if (calmon.equals("05")) {
						calmon = "May-" + calyear;
					} else if (calmon.equals("06")) {
						calmon = "Jun-" + calyear;
					} else if (calmon.equals("07")) {
						calmon = "Jul-" + calyear;
					} else if (calmon.equals("08")) {
						calmon = "Aug-" + calyear;
					} else if (calmon.equals("09")) {
						calmon = "Sep-" + calyear;
					} else if (calmon.equals("10")) {
						calmon = "Oct-" + calyear;
					} else if (calmon.equals("11")) {
						calmon = "Nov-" + calyear;
					} else if (calmon.equals("12")) {
						calmon = "Dec-" + calyear;
					} else if (calmon.equals("71")) {
						calmon = "Jan-" + calyear;
					}

					homeModel.setCommyear(rs.getString("cal_run_id"));
					homeModel.setScempid(rs.getString("sc_emp_id"));
					homeModel.setCalmon(calmon);
					return homeModel;
				}
			}, new Object[] { homeModel.getCommyear(), "%" + calyear + "%", comm_plan_id, scempid });

			productTypesList2.addAll(productTypesList);
			//System.out.println("productTypesList2 = " + productTypesList2);

		}

		//System.out.println("final productTypesList2 = " + productTypesList2);
		return productTypesList2;
	}

	@Override
	public Map<String, Float> loadCharts(int commyear, String scempid) {
		logger.info(String.valueOf(commyear));
		logger.info(scempid);

		Map<String, Float> result = new HashMap<>();

		List<Object[]> counts = employeeCorpRepository.getKpisByEmpidRecentData(scempid, commyear);

		for (Object[] row : counts) {
			result.put((String) row[0], (Float) row[1]);
		}

		// System.out.println("result = " + result);
		return result;

	}

}
