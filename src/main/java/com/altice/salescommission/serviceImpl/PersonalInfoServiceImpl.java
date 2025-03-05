package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.CalendarTypeEntity;
import com.altice.salescommission.entity.EmployeeCorpEntity;
import com.altice.salescommission.entity.EmployeeMasterEntity;
import com.altice.salescommission.entity.FavPageEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.model.KpiModel;
import com.altice.salescommission.model.UserModel;
import com.altice.salescommission.model.personalReportModel;
import com.altice.salescommission.queries.PersonalInfoQueries;
import com.altice.salescommission.repository.EmployeeCorpRepository;
import com.altice.salescommission.repository.FavPageRepository;
import com.altice.salescommission.repository.PersonalInfoRepository;
import com.altice.salescommission.service.PersonalInfoService;
import com.altice.salescommission.utility.Utilities;

@Service
@Transactional
public class PersonalInfoServiceImpl implements PersonalInfoService, PersonalInfoQueries {

	private static final Logger logger = LoggerFactory.getLogger(PersonalInfoServiceImpl.class.getName());

	@Autowired
	private Utilities utilities;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PersonalInfoRepository personalInfoRepository;

	@Autowired
	EmployeeCorpRepository employeeCorpRepository;

	@Autowired
	private FavPageRepository favPageRepository;

	@Override
	public List<UserModel> getAsOfDate(String employeedID) {
		List<UserModel> userEdatesList = jdbcTemplate.query(USER_AS_OF_DATE, new RowMapper<UserModel>() {

			@Override
			public UserModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserModel commPlanMdl = new UserModel();
				commPlanMdl.setEmployeeId(rs.getString("employee_id"));
				commPlanMdl.setEffective_date(rs.getDate("effective_date"));
				return commPlanMdl;
			}
		}, new Object[] { employeedID });
		return userEdatesList;
	}

	@Override
	public List<UserModel> getUserByAsOfDdate(String employeedID, String effective_date) throws ParseException {
		logger.info("employeedID = " + employeedID);
		logger.info("effective_date = " + effective_date);

		List<UserModel> userInfo = new ArrayList<UserModel>();

		List<Map<String, Object>> users = null;

		users = jdbcTemplate.queryForList(GET_USER_BY_AS_OF_DATE, employeedID, effective_date, employeedID,
				effective_date, employeedID, effective_date);

		for (Map row : users) {
			UserModel user = new UserModel();
			user.setRowid((Integer) row.get("id"));
			user.setEmployeeId((String) row.get("employee_id"));
			user.setNetwork_id((String) row.get("network_id"));
			user.setEmail_address((String) row.get("email_address"));
			user.setFirst_name((String) row.get("first_name"));
			user.setLast_name((String) row.get("last_name"));
			user.setMiddle_name((String) row.get("middle_name"));
			user.setName((String) row.get("empname"));

			user.setUserRole((String) row.get("user_role"));
			user.setUser_type((String) row.get("user_type"));
			user.setSales_rep_channel((String) row.get("sales_rep_channel"));
			user.setSales_channel_desc((String) row.get("sales_channel_desc"));
			user.setSales_rep_type((String) row.get("sales_rep_type"));
			user.setSales_rep_id((String) row.get("sales_rep_id"));
			user.setCalendar_type((String) row.get("calendar_type"));
			user.setSoft_termination_date((Date) row.get("soft_termination_date"));
			user.setSoftdate((String) row.get("softdate"));
			user.setTermination_date((Date) row.get("termination_date"));
			user.setOperator_id((String) row.get("operator_id"));
			user.setEffective_date((Date) row.get("effective_date"));
			user.setEdate((String) row.get("edate"));
			user.setActiveCommPeriod((String) row.get("activeCommPeriod"));
			user.setCreated_by((String) row.get("created_by"));
			user.setCreated_dt((Date) row.get("created_dt"));
			user.setProfileStatus((String) row.get("profile_status"));
			user.setAssc_corps((String) row.get("asscCorps"));

			logger.info("commissionable = " + row.get("commissionable"));

			if (row.get("comm_plan_id") != null)
				user.setComm_plan_id(((Integer) row.get("comm_plan_id")).intValue());
			if (row.get("joining_date") != null)
				user.setJoining_date((Date) row.get("joining_date"));
			if (row.get("work_status") != null)
				user.setWork_status((String) row.get("work_status"));
			if (row.get("supervisor_name") != null)
				user.setSupervisor_name((String) row.get("supervisor_name"));
			if (row.get("supname") != null)
				user.setSupname((String) row.get("supname"));
			if (row.get("supervisor_pref_name") != null)
				user.setSupervisor_pref_name((String) row.get("supervisor_pref_name"));
			if (row.get("pref_first_name") != null)
				user.setPref_first_name((String) row.get("pref_first_name"));
			if (row.get("pref_last_name") != null)
				user.setPref_last_name((String) row.get("pref_last_name"));
			if (row.get("supervisor_id") != null)
				user.setSupervisor_id((String) row.get("supervisor_id"));
			if (row.get("std_hours") != null)
				user.setStd_hours(((BigDecimal) row.get("std_hours")).intValue());
			if (row.get("commissionable") != null)
				user.setCommissionable((String) row.get("commissionable"));
			if (row.get("sc_emp_id") != null)
				// user.setId(Integer.valueOf((int) row.get("sc_emp_id")).longValue());
				user.setId((String) row.get("sc_emp_id"));

			userInfo.add(user);
		}

		logger.info("userInfo = " + userInfo);

		// }
		return userInfo;
	}

	@Override
	public List<personalReportModel> downloadPersonnelReport() throws ParseException {
		List<personalReportModel> downloadPersonnelList = jdbcTemplate.query(DOWNLOAD_USER_COMMPLAN_ASSIGNMENTS,
				new RowMapper<personalReportModel>() {
					@Override
					public personalReportModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						personalReportModel userModel = new personalReportModel();
						userModel.setScempid(rs.getString("sc_emp_id"));
						userModel.setName(rs.getString("Name"));
						userModel.setSalesrepid(rs.getString("Sales_rep_ID"));
						userModel.setJan(rs.getString("Jan"));
						userModel.setFeb(rs.getString("Feb"));
						userModel.setMar(rs.getString("Mar"));
						userModel.setApr(rs.getString("Apr"));
						userModel.setMay(rs.getString("May"));
						userModel.setJun(rs.getString("Jun"));
						userModel.setJul(rs.getString("Jul"));
						return userModel;
					}
				});
		return downloadPersonnelList;
	}

	@Override
	public List<UserModel> getUsersSearchReport(String profilestatus, String first_name, String last_name,
			String employeeId, String id, String supervisor_id, String user_type, String sales_rep_channel,
			int comm_plan_id, String payroll_start_date, String userRole) throws ParseException {

		List<UserModel> userInfo = new ArrayList<UserModel>();

		String sql_dates = getNewDates(payroll_start_date);

		List<Map<String, Object>> datesList = jdbcTemplate.queryForList(sql_dates);

		String start_of_month_current_dt = null;
		String end_of_month_current_dt = null;

		String start_of_month_second_dt = null;
		String end_of_month_second_dt = null;

		String start_of_month_three_dt = null;
		String end_of_month_three_dt = null;

		String start_of_month_four_dt = null;
		String end_of_month_four_dt = null;

		for (Map datesListObj : datesList) {
			start_of_month_current_dt = (String) datesListObj.get("start_of_month_current");
			end_of_month_current_dt = (String) datesListObj.get("end_of_month_current");

			start_of_month_second_dt = (String) datesListObj.get("start_of_month_second");
			end_of_month_second_dt = (String) datesListObj.get("end_of_month_second");

			start_of_month_three_dt = (String) datesListObj.get("start_of_month_three");
			end_of_month_three_dt = (String) datesListObj.get("end_of_month_three");

			start_of_month_four_dt = (String) datesListObj.get("start_of_month_four");
			end_of_month_four_dt = (String) datesListObj.get("end_of_month_four");
		}

		logger.info("===============================");
		logger.info("start_of_month_current_dt = " + start_of_month_current_dt);
		logger.info("end_of_month_current_dt = " + end_of_month_current_dt);
		logger.info("start_of_month_three_dt = " + start_of_month_second_dt);
		logger.info("end_of_month_second_dt = " + end_of_month_second_dt);
		logger.info("start_of_month_second_dt = " + start_of_month_three_dt);
		logger.info("end_of_month_three_dt = " + end_of_month_three_dt);
		logger.info("start_of_month_second_dt = " + start_of_month_four_dt);
		logger.info("end_of_month_three_dt = " + end_of_month_four_dt);
		logger.info("-------------------------------------");
		logger.info("first_name = " + first_name);
		logger.info("last_name = " + last_name);
		logger.info("employeeId = " + employeeId);
		logger.info("id = " + id);
		logger.info("supervisor_id = " + supervisor_id);
		logger.info("user_type = " + user_type);
		logger.info("sales_rep_channel = " + sales_rep_channel);
		logger.info("comm_plan_id = " + comm_plan_id);
		logger.info("payroll_start_date = " + payroll_start_date);

		String first_name1 = "";
		String last_name1 = "";
		String employeeId1 = "";
		String id1 = "";
		String supervisor_id1 = "";

		String user_type1 = "";
		String sales_rep_channel1 = "";
		String comm_plan_id1 = "";
		String payroll_start_date1 = "";

		if (first_name.equals("NA")) {
			first_name1 = "1";
		}
		if (last_name.equals("NA")) {
			last_name1 = "1";
		}
		if (employeeId.equals("NA")) {
			employeeId1 = "1";
		}
		if (id.equals("NA")) {
			id1 = "1";
		}
		if (supervisor_id.equals("0")) {
			supervisor_id1 = "1";
		}
		if (user_type.equals("NA")) {
			user_type1 = "1";
		}
		if (sales_rep_channel.equals("NA")) {
			sales_rep_channel1 = "1";
		}
		if (comm_plan_id == 0) {
			comm_plan_id1 = "1";
		}

		if (payroll_start_date.equals("null")) {
			logger.info("Inside IF start");
			payroll_start_date1 = "1";
			payroll_start_date = "1900-01-01";
		}

		List<Map<String, Object>> users = null;

		logger.info("comm_plan_id = " + comm_plan_id);
		logger.info("userRole = " + userRole);
		String query = "";
		if (userRole.equals("ADMIN")) {
			logger.info("Inside IF");
			query = FIND_PROFILES_ADMIN;
		} else {
			logger.info("Inside ELSE");
			query = FIND_PERSONNEL_SEARCH_REPORT;
		}

		users = jdbcTemplate.queryForList(query, start_of_month_current_dt, start_of_month_current_dt,
				start_of_month_second_dt, start_of_month_second_dt, start_of_month_three_dt, start_of_month_three_dt,
				start_of_month_current_dt, start_of_month_current_dt, start_of_month_second_dt,
				start_of_month_second_dt, start_of_month_three_dt, start_of_month_three_dt, profilestatus, first_name1,
				"%" + first_name + "%", last_name1, "%" + last_name + "%", employeeId1, "%" + employeeId + "%", id1,
				"%" + id + "%", supervisor_id1, "%" + supervisor_id + "%", user_type1, user_type, sales_rep_channel1,
				sales_rep_channel, comm_plan_id1, comm_plan_id, payroll_start_date1, payroll_start_date);

		for (Map row : users) {
			UserModel user = new UserModel();
			user.setEmployeeId((String) row.get("employee_id"));
			user.setNetwork_id((String) row.get("network_id"));
			user.setEmail_address((String) row.get("email_address"));
			user.setFirst_name((String) row.get("first_name"));
			user.setLast_name((String) row.get("last_name"));
			user.setMiddle_name((String) row.get("middle_name"));
			user.setName((String) row.get("empname"));

			user.setCurDate(start_of_month_second_dt);
			user.setPrevDateOne(start_of_month_three_dt);
			user.setPrevDateTwo(start_of_month_four_dt);
			user.setUserRole((String) row.get("user_role"));
			user.setUser_type((String) row.get("user_type"));
			user.setSales_rep_channel((String) row.get("sales_rep_channel"));
			user.setSales_channel_desc((String) row.get("sales_channel_desc"));
			user.setSales_rep_type((String) row.get("sales_rep_type"));
			user.setSales_rep_id((String) row.get("sales_rep_id"));
			user.setCalendar_type((String) row.get("calendar_type"));
			user.setSoft_termination_date((Date) row.get("soft_termination_date"));
			user.setSoftdate((String) row.get("softdate"));
			user.setTermination_date((Date) row.get("termination_date"));
			user.setOperator_id((String) row.get("operator_id"));
			user.setEffective_date((Date) row.get("effective_date"));
			user.setEdate((String) row.get("edate"));
			user.setActiveCommPeriod((String) row.get("activeCommPeriod"));
			user.setCreated_by((String) row.get("created_by"));
			user.setCreated_dt((Date) row.get("created_dt"));
			user.setProfileStatus((String) row.get("profile_status"));
			user.setAssc_corps((String) row.get("asscCorps"));

			user.setCommission_plan((String) row.get("complan1"));
			user.setComPlanTwo((String) row.get("complan2"));
			user.setComPlanThree((String) row.get("complan3"));

			user.setSupname1((String) row.get("supname1"));
			user.setSupname2((String) row.get("supname2"));
			user.setSupname3((String) row.get("supname3"));

			if (row.get("comm_plan_id") != null)
				user.setComm_plan_id(((Integer) row.get("comm_plan_id")).intValue());
			if (row.get("joining_date") != null)
				user.setJoining_date((Date) row.get("joining_date"));
			if (row.get("work_status") != null)
				user.setWork_status((String) row.get("work_status"));
			if (row.get("supervisor_name") != null)
				user.setSupervisor_name((String) row.get("supervisor_name"));
			if (row.get("supname") != null)
				user.setSupname((String) row.get("supname"));
			if (row.get("supervisor_pref_name") != null)
				user.setSupervisor_pref_name((String) row.get("supervisor_pref_name"));
			if (row.get("pref_first_name") != null)
				user.setPref_first_name((String) row.get("pref_first_name"));
			if (row.get("pref_last_name") != null)
				user.setPref_last_name((String) row.get("pref_last_name"));
			if (row.get("supervisor_id") != null)
				user.setSupervisor_id((String) row.get("supervisor_id"));
			if (row.get("std_hours") != null)
				user.setStd_hours(((BigDecimal) row.get("std_hours")).intValue());
			if (row.get("commissionable") != null)
				user.setCommissionable((String) row.get("commissionable"));
			if (row.get("sc_emp_id") != null)
				// user.setId(Integer.valueOf((int) row.get("sc_emp_id")).longValue());
				user.setId((String) row.get("sc_emp_id"));

			userInfo.add(user);
		}

		logger.info("userInfo = " + userInfo);

		// }
		return userInfo;
	}

	private String getDates(String payroll_end_date) throws ParseException {
		logger.info("payroll_end_date = " + payroll_end_date);
		String sql = "";
		logger.info("payroll_end_date length = " + payroll_end_date.length());
		if (payroll_end_date.length() == 4) {
			logger.info("Inside getDates IF");

			sql = "SELECT "
					+ "to_char((date_trunc('month', now()::date) + interval '-2 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_three,"
					+ "to_char((date_trunc('month', now()::date) + interval '-1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_three,"
					+ "to_char((date_trunc('month', now()::date) + interval '-1 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_second,"
					+ "to_char((date_trunc('month', now()::date) + interval '0 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_second,"
					+ "to_char((date_trunc('month', now()::date) + interval '0 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_current,"
					+ "to_char((date_trunc('month', now()::date) + interval '1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_current";
		} else {
			logger.info("Inside getDates ELSE");
			Date payrollEndDate = utilities.getStringToDate(payroll_end_date);

			sql = "SELECT " + "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-2 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_three,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_three,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-1 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_second,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '0 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_second,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '0 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_current,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_current";
		}
		return sql;
	}

	private String getNewDates(String payroll_end_date) throws ParseException {
		logger.info("payroll_end_date = " + payroll_end_date);
		String sql = "";
		logger.info("payroll_end_date length = " + payroll_end_date.length());
		if (payroll_end_date.length() == 4) {
			logger.info("Inside getDates IF");

			sql = "SELECT "
					+ "to_char((date_trunc('month', now()::date) + interval '-2 month' - interval '0 day')::date,'YYYY-MM-DD') AS start_of_month_four,"
					+ "to_char((date_trunc('month', now()::date) + interval '-1 month' - interval '1 day')::date,'YYYY-MM-DD') AS end_of_month_four,"
					+ "to_char((date_trunc('month', now()::date) + interval '-1 month' - interval '0 day')::date,'YYYY-MM-DD') AS start_of_month_three,"
					+ "to_char((date_trunc('month', now()::date) + interval '0 month' - interval '1 day')::date,'YYYY-MM-DD') AS end_of_month_three,"
					+ "to_char((date_trunc('month', now()::date) + interval '0 month' - interval '0 day')::date,'YYYY-MM-DD') AS start_of_month_second,"
					+ "to_char((date_trunc('month', now()::date) + interval '1 month' - interval '1 day')::date,'YYYY-MM-DD') AS end_of_month_second,"
					+ "to_char((date_trunc('month', now()::date) + interval '1 month' - interval '0 day')::date,'YYYY-MM-DD') AS start_of_month_current,"
					+ "to_char((date_trunc('month', now()::date) + interval '2 month' - interval '1 day')::date,'YYYY-MM-DD') AS end_of_month_current";
		} else {
			logger.info("Inside getDates ELSE");
			Date payrollEndDate = utilities.getStringToDate(payroll_end_date);

			sql = "SELECT " + "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-2 month' - interval '0 day')::date,'YYYY-MM-DD') AS start_of_month_four,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-1 month' - interval '1 day')::date,'YYYY-MM-DD') AS end_of_month_four,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-1 month' - interval '0 day')::date,'YYYY-MM-DD') AS start_of_month_three,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '0 month' - interval '1 day')::date,'YYYY-MM-DD') AS end_of_month_three,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '0 month' - interval '0 day')::date,'YYYY-MM-DD') AS start_of_month_second,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '1 month' - interval '1 day')::date,'YYYY-MM-DD') AS end_of_month_second,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '1 month' - interval '0 day')::date,'YYYY-MM-DD') AS start_of_month_current,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '2 month' - interval '1 day')::date,'YYYY-MM-DD') AS end_of_month_current";
		}
		return sql;
	}

	@Override
	public List<UserModel> getUsersReport(String profilestatus, String first_name, String last_name, String employeeId,
			String id, String supervisor_id, String user_type, String sales_rep_channel, int comm_plan_id,
			String payroll_start_date, String userRole) throws ParseException {

		List<UserModel> userInfo = new ArrayList<UserModel>();
//		if (profilestatus.equals("pending")) {
//			userInfo = getPendingUsers(profilestatus, first_name, last_name, employeeId, id, supervisor_id, user_type,
//					sales_rep_channel, comm_plan_id, payroll_start_date, payroll_end_date);
//		} else {

		String sql_dates = getNewDates(payroll_start_date);

		List<Map<String, Object>> datesList = jdbcTemplate.queryForList(sql_dates);

		String start_of_month_current_dt = null;
		String end_of_month_current_dt = null;

		String start_of_month_second_dt = null;
		String end_of_month_second_dt = null;

		String start_of_month_three_dt = null;
		String end_of_month_three_dt = null;

		String start_of_month_four_dt = null;
		String end_of_month_four_dt = null;

		for (Map datesListObj : datesList) {
			start_of_month_current_dt = (String) datesListObj.get("start_of_month_current");
			end_of_month_current_dt = (String) datesListObj.get("end_of_month_current");

			start_of_month_second_dt = (String) datesListObj.get("start_of_month_second");
			end_of_month_second_dt = (String) datesListObj.get("end_of_month_second");

			start_of_month_three_dt = (String) datesListObj.get("start_of_month_three");
			end_of_month_three_dt = (String) datesListObj.get("end_of_month_three");

			start_of_month_four_dt = (String) datesListObj.get("start_of_month_four");
			end_of_month_four_dt = (String) datesListObj.get("end_of_month_four");
		}

		logger.info("===============================");
		logger.info("start_of_month_current_dt = " + start_of_month_current_dt);
		logger.info("end_of_month_current_dt = " + end_of_month_current_dt);
		logger.info("start_of_month_three_dt = " + start_of_month_second_dt);
		logger.info("end_of_month_second_dt = " + end_of_month_second_dt);
		logger.info("start_of_month_second_dt = " + start_of_month_three_dt);
		logger.info("end_of_month_three_dt = " + end_of_month_three_dt);
		logger.info("start_of_month_second_dt = " + start_of_month_four_dt);
		logger.info("end_of_month_three_dt = " + end_of_month_four_dt);
		logger.info("-------------------------------------");
		logger.info("first_name = " + first_name);
		logger.info("last_name = " + last_name);
		logger.info("employeeId = " + employeeId);
		logger.info("id = " + id);
		logger.info("supervisor_id = " + supervisor_id);
		logger.info("user_type = " + user_type);
		logger.info("sales_rep_channel = " + sales_rep_channel);
		logger.info("comm_plan_id = " + comm_plan_id);
		logger.info("payroll_start_date = " + payroll_start_date);

		String first_name1 = "";
		String last_name1 = "";
		String employeeId1 = "";
		String id1 = "";
		String supervisor_id1 = "";

		String user_type1 = "";
		String sales_rep_channel1 = "";
		String comm_plan_id1 = "";
		String payroll_start_date1 = "";

		if (first_name.equals("NA")) {
			first_name1 = "1";
		}
		if (last_name.equals("NA")) {
			last_name1 = "1";
		}
		if (employeeId.equals("NA")) {
			employeeId1 = "1";
		}
		if (id.equals("NA")) {
			id1 = "1";
		}
		if (supervisor_id.equals("0")) {
			supervisor_id1 = "1";
		}
		if (user_type.equals("NA")) {
			user_type1 = "1";
		}
		if (sales_rep_channel.equals("NA")) {
			sales_rep_channel1 = "1";
		}
		if (comm_plan_id == 0) {
			comm_plan_id1 = "1";
		}

		if (payroll_start_date.equals("null")) {
			logger.info("Inside IF start");
			payroll_start_date1 = "1";
			payroll_start_date = "1900-01-01";
		}

		List<Map<String, Object>> users = null;

		logger.info("comm_plan_id = " + comm_plan_id);
		logger.info("userRole = " + userRole);
		String query = "";
		if (userRole.equals("ADMIN")) {
			logger.info("Inside IF");
			query = FIND_PROFILES_ADMIN;
			users = jdbcTemplate.queryForList(query, profilestatus, first_name1, "%" + first_name + "%", last_name1,
					"%" + last_name + "%", employeeId1, "%" + employeeId + "%", id1, "%" + id + "%", supervisor_id1,
					"%" + supervisor_id + "%", user_type1, user_type, sales_rep_channel1, sales_rep_channel,
					comm_plan_id1, comm_plan_id, payroll_start_date1, payroll_start_date);

		} else {
			logger.info("Inside ELSE");
			query = FIND_PROFILES_NEW;
			users = jdbcTemplate.queryForList(query, profilestatus, start_of_month_current_dt,
					start_of_month_current_dt, start_of_month_second_dt, start_of_month_second_dt,
					start_of_month_three_dt, start_of_month_three_dt, start_of_month_current_dt,
					start_of_month_current_dt, start_of_month_second_dt, start_of_month_second_dt,
					start_of_month_three_dt, start_of_month_three_dt, profilestatus, first_name1,
					"%" + first_name + "%", last_name1, "%" + last_name + "%", employeeId1, "%" + employeeId + "%", id1,
					"%" + id + "%", supervisor_id1, "%" + supervisor_id + "%", user_type1, user_type,
					sales_rep_channel1, sales_rep_channel, comm_plan_id1, comm_plan_id, payroll_start_date1,
					payroll_start_date);
		}

		for (Map row : users) {
			UserModel user = new UserModel();
			user.setRowid((Integer) row.get("id"));
			user.setEmployeeId((String) row.get("employee_id"));
			user.setNetwork_id((String) row.get("network_id"));
			user.setEmail_address((String) row.get("email_address"));
			user.setFirst_name((String) row.get("first_name"));
			user.setLast_name((String) row.get("last_name"));
			user.setMiddle_name((String) row.get("middle_name"));
			user.setName((String) row.get("empname"));

			user.setCurDate(start_of_month_second_dt);
			user.setPrevDateOne(start_of_month_three_dt);
			user.setPrevDateTwo(start_of_month_four_dt);
			user.setUserRole((String) row.get("user_role"));
			user.setUser_type((String) row.get("user_type"));
			user.setSales_rep_channel((String) row.get("sales_rep_channel"));
			user.setSales_channel_desc((String) row.get("sales_channel_desc"));
			user.setSales_rep_type((String) row.get("sales_rep_type"));
			user.setSales_rep_id((String) row.get("sales_rep_id"));
			user.setCalendar_type((String) row.get("calendar_type"));
			user.setSoft_termination_date((Date) row.get("soft_termination_date"));
			user.setSoftdate((String) row.get("softdate"));
			user.setTermination_date((Date) row.get("termination_date"));
			user.setOperator_id((String) row.get("operator_id"));
			user.setEffective_date((Date) row.get("effective_date"));
			user.setEdate((String) row.get("edate"));
			user.setActiveCommPeriod((String) row.get("activeCommPeriod"));
			user.setCreated_by((String) row.get("created_by"));
			user.setCreated_dt((Date) row.get("created_dt"));
			user.setProfileStatus((String) row.get("profile_status"));
			user.setAssc_corps((String) row.get("asscCorps"));

			user.setCommission_plan((String) row.get("complan1"));
			user.setComPlanTwo((String) row.get("complan2"));
			user.setComPlanThree((String) row.get("complan3"));

			user.setSupname1((String) row.get("supname1"));
			user.setSupname2((String) row.get("supname2"));
			user.setSupname3((String) row.get("supname3"));

			if (row.get("comm_plan_id") != null)
				user.setComm_plan_id(((Integer) row.get("comm_plan_id")).intValue());
			if (row.get("joining_date") != null)
				user.setJoining_date((Date) row.get("joining_date"));
			if (row.get("work_status") != null)
				user.setWork_status((String) row.get("work_status"));
			if (row.get("supervisor_name") != null)
				user.setSupervisor_name((String) row.get("supervisor_name"));
			if (row.get("supname") != null)
				user.setSupname((String) row.get("supname"));
			if (row.get("supervisor_pref_name") != null)
				user.setSupervisor_pref_name((String) row.get("supervisor_pref_name"));
			if (row.get("pref_first_name") != null)
				user.setPref_first_name((String) row.get("pref_first_name"));
			if (row.get("pref_last_name") != null)
				user.setPref_last_name((String) row.get("pref_last_name"));
			if (row.get("supervisor_id") != null)
				user.setSupervisor_id((String) row.get("supervisor_id"));
			if (row.get("std_hours") != null)
				user.setStd_hours(((BigDecimal) row.get("std_hours")).intValue());
			if (row.get("commissionable") != null)
				user.setCommissionable((String) row.get("commissionable"));
			if (row.get("sc_emp_id") != null)
				// user.setId(Integer.valueOf((int) row.get("sc_emp_id")).longValue());
				user.setId((String) row.get("sc_emp_id"));

			userInfo.add(user);
		}

		logger.info("userInfo = " + userInfo);

		// }
		return userInfo;
	}

	@Override
	public List<UserModel> getUsersPending(String profilestatus, String first_name, String last_name, String employeeId,
			String id, String userRole) throws ParseException {

		List<UserModel> userInfo = new ArrayList<UserModel>();

		logger.info("-------------------------------------");
		logger.info("first_name = " + first_name);
		logger.info("last_name = " + last_name);
		logger.info("employeeId = " + employeeId);
		logger.info("id = " + id);

		String first_name1 = "";
		String last_name1 = "";
		String employeeId1 = "";
		String id1 = "";

		if (first_name.equals("NA")) {
			first_name1 = "1";
		}
		if (last_name.equals("NA")) {
			last_name1 = "1";
		}
		if (employeeId.equals("NA")) {
			employeeId1 = "1";
		}
		if (id.equals("NA")) {
			id1 = "1";
		}

		List<Map<String, Object>> users = null;

		users = jdbcTemplate.queryForList(FIND_PROFILES_PENDING, profilestatus, first_name1, "%" + first_name + "%",
				last_name1, "%" + last_name + "%", employeeId1, "%" + employeeId + "%", id1, "%" + id + "%");

		// logger.info("users " + users);

		for (Map row : users) {
			UserModel user = new UserModel();
			user.setRowid((Integer) row.get("id"));
			user.setEmployeeId((String) row.get("employee_id"));
			user.setNetwork_id((String) row.get("network_id"));
			user.setEmail_address((String) row.get("email_address"));
			user.setFirst_name((String) row.get("first_name"));
			user.setLast_name((String) row.get("last_name"));
			user.setMiddle_name((String) row.get("middle_name"));
			user.setName((String) row.get("empname"));

			user.setUserRole((String) row.get("user_role"));
			user.setUser_type((String) row.get("user_type"));
			user.setSales_rep_channel((String) row.get("sales_rep_channel"));
			user.setSales_channel_desc((String) row.get("sales_channel_desc"));
			user.setSales_rep_type((String) row.get("sales_rep_type"));
			user.setSales_rep_id((String) row.get("sales_rep_id"));
			user.setCalendar_type((String) row.get("calendar_type"));
			user.setSoft_termination_date((Date) row.get("soft_termination_date"));
			user.setSoftdate((String) row.get("softdate"));
			user.setTermination_date((Date) row.get("termination_date"));
			user.setOperator_id((String) row.get("operator_id"));
			user.setEffective_date((Date) row.get("effective_date"));
			user.setEdate((String) row.get("edate"));
			user.setActiveCommPeriod((String) row.get("activeCommPeriod"));
			user.setCreated_by((String) row.get("created_by"));
			user.setCreated_dt((Date) row.get("created_dt"));
			user.setProfileStatus((String) row.get("profile_status"));
			user.setAssc_corps((String) row.get("asscCorps"));

			if (row.get("comm_plan_id") != null)
				user.setComm_plan_id(((Integer) row.get("comm_plan_id")).intValue());
			if (row.get("joining_date") != null)
				user.setJoining_date((Date) row.get("joining_date"));
			if (row.get("work_status") != null)
				user.setWork_status((String) row.get("work_status"));
			if (row.get("supervisor_name") != null)
				user.setSupervisor_name((String) row.get("supervisor_name"));
			if (row.get("supname") != null)
				user.setSupname((String) row.get("supname"));
			if (row.get("supervisor_pref_name") != null)
				user.setSupervisor_pref_name((String) row.get("supervisor_pref_name"));
			if (row.get("pref_first_name") != null)
				user.setPref_first_name((String) row.get("pref_first_name"));
			if (row.get("pref_last_name") != null)
				user.setPref_last_name((String) row.get("pref_last_name"));
			if (row.get("supervisor_id") != null)
				user.setSupervisor_id((String) row.get("supervisor_id"));
			if (row.get("std_hours") != null)
				user.setStd_hours(((BigDecimal) row.get("std_hours")).intValue());
			if (row.get("commissionable") != null)
				user.setCommissionable((String) row.get("commissionable"));
			if (row.get("sc_emp_id") != null)
				// user.setId(Integer.valueOf((int) row.get("sc_emp_id")).longValue());
				user.setId((String) row.get("sc_emp_id"));

			userInfo.add(user);
		}

		logger.info("userInfo = " + userInfo);

		// }
		return userInfo;
	}

	@Override
	public List<UserModel> getUsers(String profilestatus, String first_name, String last_name, String employeeId,
			String id, String supervisor_id, String user_type, String sales_rep_channel, int comm_plan_id,
			String payroll_start_date, String payroll_end_date, String userRole) throws ParseException {

		List<UserModel> userInfo = new ArrayList<UserModel>();
		if (profilestatus.equals("pending")) {
			userInfo = getPendingUsers(profilestatus, first_name, last_name, employeeId, id, supervisor_id, user_type,
					sales_rep_channel, comm_plan_id, payroll_start_date, payroll_end_date);
		} else {

			String sql_dates = getNewDates(payroll_end_date);

			List<Map<String, Object>> datesList = jdbcTemplate.queryForList(sql_dates);

			String start_of_month_current_dt = null;
			String end_of_month_current_dt = null;

			String start_of_month_second_dt = null;
			String end_of_month_second_dt = null;

			String start_of_month_three_dt = null;
			String end_of_month_three_dt = null;

			for (Map datesListObj : datesList) {
				start_of_month_current_dt = (String) datesListObj.get("start_of_month_current");
				end_of_month_current_dt = (String) datesListObj.get("end_of_month_current");

				start_of_month_second_dt = (String) datesListObj.get("start_of_month_second");
				end_of_month_second_dt = (String) datesListObj.get("end_of_month_second");

				start_of_month_three_dt = (String) datesListObj.get("start_of_month_three");
				end_of_month_three_dt = (String) datesListObj.get("end_of_month_three");
			}

			logger.info("===============================");
			logger.info("start_of_month_current_dt = " + start_of_month_current_dt);
			logger.info("end_of_month_current_dt = " + end_of_month_current_dt);
			logger.info("start_of_month_three_dt = " + start_of_month_second_dt);
			logger.info("end_of_month_second_dt = " + end_of_month_second_dt);
			logger.info("start_of_month_second_dt = " + start_of_month_three_dt);
			logger.info("end_of_month_three_dt = " + end_of_month_three_dt);
			logger.info("-------------------------------------");
			logger.info("first_name = " + first_name);
			logger.info("last_name = " + last_name);
			logger.info("employeeId = " + employeeId);
			logger.info("id = " + id);
			logger.info("supervisor_id = " + supervisor_id);
			logger.info("user_type = " + user_type);
			logger.info("sales_rep_channel = " + sales_rep_channel);
			logger.info("comm_plan_id = " + comm_plan_id);
			logger.info("payroll_start_date = " + payroll_start_date);
			logger.info("payroll_end_date = " + payroll_end_date);

			String first_name1 = "";
			String last_name1 = "";
			String employeeId1 = "";
			String id1 = "";
			String supervisor_id1 = "";

			String user_type1 = "";
			String sales_rep_channel1 = "";
			String comm_plan_id1 = "";
			String payroll_start_date1 = "";

			if (first_name.equals("NA")) {
				first_name1 = "1";
			}
			if (last_name.equals("NA")) {
				last_name1 = "1";
			}
			if (employeeId.equals("NA")) {
				employeeId1 = "1";
			}
			if (id.equals("NA")) {
				id1 = "1";
			}
			if (supervisor_id.equals("0")) {
				supervisor_id1 = "1";
			}
			if (user_type.equals("NA")) {
				user_type1 = "1";
			}
			if (sales_rep_channel.equals("NA")) {
				sales_rep_channel1 = "1";
			}
			if (comm_plan_id == 0) {
				comm_plan_id1 = "1";
			}

			if (payroll_start_date.equals("null")) {
				logger.info("Inside IF start");
				payroll_start_date1 = "1";
				payroll_start_date = "1900-01-01";
			}

			if (payroll_end_date.equals("null")) {
				logger.info("Inside IF end");
				payroll_start_date1 = "1";
				payroll_end_date = "1900-01-01";
			}

			List<Map<String, Object>> users = null;

			logger.info("comm_plan_id = " + comm_plan_id);
			logger.info("userRole = " + userRole);
			String query = "";
			if (userRole.equals("ADMIN")) {
				logger.info("Inside IF");
				query = FIND_PROFILES_ADMIN;
			} else if (userRole.equals("IMPERSONATE")) {
				logger.info("Inside ELSE IF");
				query = FIND_PROFILES_REPORT;
			} else {
				logger.info("Inside ELSE");
				query = FIND_PROFILE_USERS;
			}

			users = jdbcTemplate.queryForList(query, start_of_month_current_dt, start_of_month_current_dt,
					start_of_month_second_dt, start_of_month_second_dt, start_of_month_three_dt,
					start_of_month_three_dt, start_of_month_current_dt, start_of_month_current_dt,
					start_of_month_second_dt, start_of_month_second_dt, start_of_month_three_dt,
					start_of_month_three_dt, profilestatus, first_name1, "%" + first_name + "%", last_name1,
					"%" + last_name + "%", employeeId1, "%" + employeeId + "%", id1, "%" + id + "%", supervisor_id1,
					"%" + supervisor_id + "%", user_type1, user_type, sales_rep_channel1, sales_rep_channel,
					comm_plan_id1, comm_plan_id, payroll_start_date1, payroll_start_date, payroll_end_date);

			logger.info("users " + users);

			for (Map row : users) {
				UserModel user = new UserModel();
				user.setEmployeeId((String) row.get("employee_id"));
				user.setNetwork_id((String) row.get("network_id"));
				user.setEmail_address((String) row.get("email_address"));
				user.setFirst_name((String) row.get("first_name"));
				user.setLast_name((String) row.get("last_name"));
				user.setMiddle_name((String) row.get("middle_name"));
				user.setName((String) row.get("empname"));

				user.setCurDate(end_of_month_current_dt);
				user.setPrevDateOne(end_of_month_second_dt);
				user.setPrevDateTwo(end_of_month_three_dt);
				user.setUserRole((String) row.get("user_role"));
				user.setUser_type((String) row.get("user_type"));
				user.setSales_rep_channel((String) row.get("sales_rep_channel"));
				user.setSales_channel_desc((String) row.get("sales_channel_desc"));
				user.setSales_rep_type((String) row.get("sales_rep_type"));
				user.setSales_rep_id((String) row.get("sales_rep_id"));
				user.setCalendar_type((String) row.get("calendar_type"));
				user.setSoft_termination_date((Date) row.get("soft_termination_date"));
				user.setSoftdate((String) row.get("softdate"));
				user.setTermination_date((Date) row.get("termination_date"));
				user.setOperator_id((String) row.get("operator_id"));
				user.setEffective_date((Date) row.get("effective_date"));
				user.setEdate((String) row.get("edate"));
				user.setActiveCommPeriod((String) row.get("activeCommPeriod"));
				user.setCreated_by((String) row.get("created_by"));
				user.setCreated_dt((Date) row.get("created_dt"));
				user.setProfileStatus((String) row.get("profile_status"));
				user.setAssc_corps((String) row.get("asscCorps"));

				user.setCommission_plan((String) row.get("complan1"));
				user.setComPlanTwo((String) row.get("complan2"));
				user.setComPlanThree((String) row.get("complan3"));

				user.setSupname1((String) row.get("supname1"));
				user.setSupname2((String) row.get("supname2"));
				user.setSupname3((String) row.get("supname3"));

				if (row.get("comm_plan_id") != null)
					user.setComm_plan_id(((Integer) row.get("comm_plan_id")).intValue());
				if (row.get("joining_date") != null)
					user.setJoining_date((Date) row.get("joining_date"));
				if (row.get("work_status") != null)
					user.setWork_status((String) row.get("work_status"));
				if (row.get("supervisor_name") != null)
					user.setSupervisor_name((String) row.get("supervisor_name"));
				if (row.get("supname") != null)
					user.setSupname((String) row.get("supname"));
				if (row.get("supervisor_pref_name") != null)
					user.setSupervisor_pref_name((String) row.get("supervisor_pref_name"));
				if (row.get("pref_first_name") != null)
					user.setPref_first_name((String) row.get("pref_first_name"));
				if (row.get("pref_last_name") != null)
					user.setPref_last_name((String) row.get("pref_last_name"));
				if (row.get("supervisor_id") != null)
					user.setSupervisor_id((String) row.get("supervisor_id"));
				if (row.get("std_hours") != null)
					user.setStd_hours(((BigDecimal) row.get("std_hours")).intValue());
				if (row.get("commissionable") != null)
					user.setCommissionable((String) row.get("commissionable"));
				if (row.get("sc_emp_id") != null)
					// user.setId(Integer.valueOf((int) row.get("sc_emp_id")).longValue());
					user.setId((String) row.get("sc_emp_id"));

				userInfo.add(user);
			}

			logger.info("userInfo = " + userInfo);

		}
		return userInfo;
	}

	// @Override
	public List<UserModel> getUsersReport1(String profilestatus, String first_name, String last_name, String employeeId,
			String id, String supervisor_id, String user_type, String sales_rep_channel, int comm_plan_id,
			String payroll_start_date, String payroll_end_date) throws ParseException {

		List<UserModel> userInfo = new ArrayList<UserModel>();
		if (profilestatus.equals("pending")) {
			userInfo = getPendingUsers(profilestatus, first_name, last_name, employeeId, id, supervisor_id, user_type,
					sales_rep_channel, comm_plan_id, payroll_start_date, payroll_end_date);
		} else {

			String sql_dates = getDates(payroll_end_date);

			List<Map<String, Object>> datesList = jdbcTemplate.queryForList(sql_dates);

			String start_of_month_current_dt = null;
			String end_of_month_current_dt = null;

			String start_of_month_second_dt = null;
			String end_of_month_second_dt = null;

			String start_of_month_three_dt = null;
			String end_of_month_three_dt = null;

			for (Map datesListObj : datesList) {
				start_of_month_current_dt = (String) datesListObj.get("start_of_month_current");
				end_of_month_current_dt = (String) datesListObj.get("end_of_month_current");

				start_of_month_second_dt = (String) datesListObj.get("start_of_month_second");
				end_of_month_second_dt = (String) datesListObj.get("end_of_month_second");

				start_of_month_three_dt = (String) datesListObj.get("start_of_month_three");
				end_of_month_three_dt = (String) datesListObj.get("end_of_month_three");
			}

			logger.info("===============================");
			logger.info("first_name = " + first_name);
			logger.info("last_name = " + last_name);
			logger.info("employeeId = " + employeeId);
			logger.info("id = " + id);
			logger.info("supervisor_id = " + supervisor_id);
			logger.info("user_type = " + user_type);
			logger.info("sales_rep_channel = " + sales_rep_channel);
			logger.info("comm_plan_id = " + comm_plan_id);
			logger.info("payroll_start_date = " + payroll_start_date);
			logger.info("payroll_end_date = " + payroll_end_date);

			String first_name1 = "";
			String last_name1 = "";
			String employeeId1 = "";
			String id1 = "";
			String supervisor_id1 = "";

			String user_type1 = "";
			String sales_rep_channel1 = "";
			String comm_plan_id1 = "";
			String payroll_start_date1 = "";

			if (first_name.equals("NA")) {
				first_name1 = "1";
			}
			if (last_name.equals("NA")) {
				last_name1 = "1";
			}
			if (employeeId.equals("NA")) {
				employeeId1 = "1";
			}
			if (id.equals("NA")) {
				id1 = "1";
			}
			if (supervisor_id.equals("0")) {
				supervisor_id1 = "1";
			}
			if (user_type.equals("NA")) {
				user_type1 = "1";
			}
			if (sales_rep_channel.equals("NA")) {
				sales_rep_channel1 = "1";
			}
			if (comm_plan_id == 0) {
				comm_plan_id1 = "1";
			}

			if (payroll_start_date.equals("null")) {
				logger.info("Inside IF start");
				payroll_start_date1 = "1";
				payroll_start_date = "1900-01-01";
			}

			if (payroll_end_date.equals("null")) {
				logger.info("Inside IF end");
				payroll_start_date1 = "1";
				payroll_end_date = "1900-01-01";
			}

			List<Map<String, Object>> users = null;

			users = jdbcTemplate.queryForList(FIND_PROFILES_REPORT, profilestatus, first_name1, "%" + first_name + "%",
					last_name1, "%" + last_name + "%", employeeId1, "%" + employeeId + "%", id1, "%" + id + "%",
					supervisor_id1, "%" + supervisor_id + "%", user_type1, user_type, sales_rep_channel1,
					sales_rep_channel, comm_plan_id1, comm_plan_id, payroll_start_date1, payroll_start_date,
					payroll_end_date);

			logger.info("users " + users);

			for (Map row : users) {
				UserModel user = new UserModel();
				user.setEmployeeId((String) row.get("employee_id"));
				user.setNetwork_id((String) row.get("network_id"));
				user.setEmail_address((String) row.get("email_address"));
				user.setFirst_name((String) row.get("first_name"));
				user.setLast_name((String) row.get("last_name"));
				user.setMiddle_name((String) row.get("middle_name"));
				user.setName((String) row.get("empname"));

				String complan1 = getComPlan((String) row.get("employee_id"), start_of_month_current_dt,
						end_of_month_current_dt, profilestatus);
				String complan2 = getComPlan((String) row.get("employee_id"), start_of_month_second_dt,
						end_of_month_second_dt, profilestatus);
				String complan3 = getComPlan((String) row.get("employee_id"), start_of_month_three_dt,
						end_of_month_three_dt, profilestatus);

				logger.info("complan1 = " + complan1);
				logger.info("complan2 = " + complan2);
				logger.info("complan3 = " + complan3);

				if (complan3.equals("NA")) {
					user.setComPlanThree(complan3);
				} else {
					user.setComPlanThree(complan3);
				}

				if (complan2.equals("NA")) {
					user.setComPlanTwo(complan2);
				} else {
					user.setComPlanTwo(complan2);
				}

				if (complan1.equals("NA")) {
					user.setCommission_plan((String) row.get("comm_plan"));
				} else {
					user.setCommission_plan(complan1);
				}

				String supname1 = getSupervisorName((String) row.get("employee_id"), start_of_month_current_dt,
						end_of_month_current_dt);
				String supname2 = getSupervisorName((String) row.get("employee_id"), start_of_month_second_dt,
						end_of_month_second_dt);
				String supname3 = getSupervisorName((String) row.get("employee_id"), start_of_month_three_dt,
						end_of_month_three_dt);

				if (supname1.equals("NA")) {
					user.setSupname1("NA");
				} else {
					user.setSupname1(supname1);
				}

				if (supname2.equals("NA")) {
					user.setSupname2("NA");
				} else {
					user.setSupname2(supname2);
				}

				if (supname3.equals("NA")) {
					user.setSupname3("NA");
				} else {
					user.setSupname3(supname3);
				}

				user.setCurDate(end_of_month_current_dt);
				user.setPrevDateOne(end_of_month_second_dt);
				user.setPrevDateTwo(end_of_month_three_dt);
				user.setUserRole((String) row.get("user_role"));
				user.setUser_type((String) row.get("user_type"));
				user.setSales_rep_channel((String) row.get("sales_rep_channel"));
				user.setSales_channel_desc((String) row.get("sales_channel_desc"));
				user.setSales_rep_type((String) row.get("sales_rep_type"));
				user.setSales_rep_id((String) row.get("sales_rep_id"));
				user.setCalendar_type((String) row.get("calendar_type"));
				user.setSoft_termination_date((Date) row.get("soft_termination_date"));
				user.setSoftdate((String) row.get("softdate"));
				user.setTermination_date((Date) row.get("termination_date"));
				user.setOperator_id((String) row.get("operator_id"));
				user.setEffective_date((Date) row.get("effective_date"));
				user.setEdate((String) row.get("edate"));
				user.setActiveCommPeriod((String) row.get("activeCommPeriod"));
				user.setCreated_by((String) row.get("created_by"));
				user.setCreated_dt((Date) row.get("created_dt"));
				user.setProfileStatus((String) row.get("profile_status"));
				user.setAssc_corps((String) row.get("asscCorps"));

				if (row.get("comm_plan_id") != null)
					user.setComm_plan_id(((Integer) row.get("comm_plan_id")).intValue());
				if (row.get("joining_date") != null)
					user.setJoining_date((Date) row.get("joining_date"));
				if (row.get("work_status") != null)
					user.setWork_status((String) row.get("work_status"));
				if (row.get("supervisor_name") != null)
					user.setSupervisor_name((String) row.get("supervisor_name"));
				if (row.get("supname") != null)
					user.setSupname((String) row.get("supname"));
				if (row.get("supervisor_pref_name") != null)
					user.setSupervisor_pref_name((String) row.get("supervisor_pref_name"));
				if (row.get("pref_first_name") != null)
					user.setPref_first_name((String) row.get("pref_first_name"));
				if (row.get("pref_last_name") != null)
					user.setPref_last_name((String) row.get("pref_last_name"));
				if (row.get("supervisor_id") != null)
					user.setSupervisor_id((String) row.get("supervisor_id"));
				if (row.get("std_hours") != null)
					user.setStd_hours(((BigDecimal) row.get("std_hours")).intValue());
				if (row.get("commissionable") != null)
					user.setCommissionable((String) row.get("commissionable"));
				if (row.get("sc_emp_id") != null)
					// user.setId(Integer.valueOf((int) row.get("sc_emp_id")).longValue());
					user.setId((String) row.get("sc_emp_id"));

				userInfo.add(user);
			}

			logger.info("userInfo = " + userInfo);

		}
		return userInfo;
	}

	@Override
	public List<EmployeeMasterEntity> getProfiles(String profilestatus, String payroll_end_date) throws ParseException {
		List<EmployeeMasterEntity> userInfo = new ArrayList<EmployeeMasterEntity>();
		userInfo = personalInfoRepository.getProfiles();
		System.out.println("userInfo = " + userInfo);
		return userInfo;
	}

	public EmployeeMasterEntity bulkUpdate(List<EmployeeMasterEntity> userModels) {
		// logger.info("bulkUpdate userModels = " + userModels);
		logger.info("=================================================");
		logger.info("bulkUpdate userModels = " + userModels);
		try {
			Date d1 = null;
			Date d2 = null;
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			int i = 0;
			String check = "";

			for (EmployeeMasterEntity userModel : userModels) {

				if (i == 0) {
					logger.info("Inside IF");
					check = userModel.getCheckboxchecked();
				}
				logger.info("check = " + check);

				int edatecount = personalInfoRepository.getOrgEffectiveDateCountBulkupdate(userModel.getEmployeeId(),
						userModel.getEffective_date());
				logger.info("edatecount = " + edatecount);

				if (edatecount == 0) {
					// logger.info("Inside IF Count. Inserting data");
					this.saveBulkUserProfile(userModel, check);
				} else {
					// logger.info("getEmployeeId = " + userModel.getEmployeeId());
					Date orgeffdate1 = personalInfoRepository.getOrgEffectiveDateBulkupdate(userModel.getEmployeeId(),
							userModel.getEffective_date());

					String orgeffdate = dateFormat.format(orgeffdate1);
					logger.info("orgeffdate = " + orgeffdate);

					String effdate = dateFormat.format(userModel.getEffective_date());
					logger.info("effdate = " + effdate);

					d1 = dateFormat.parse(orgeffdate);
					d2 = dateFormat.parse(effdate);
					logger.info("orgeffdate = " + d1);
					logger.info("effdate = " + d2);

					if (d1.compareTo(d2) == 0 && check.equals("false")) {
						logger.info("Inside IF. Updating data");
						this.updateUserProfile(userModel);
					} else {
						logger.info("Inside ELSE. Inserting data");
						this.saveBulkUserProfile(userModel, check);
					}
				}

				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	private EmployeeMasterEntity saveBulkUserProfile(EmployeeMasterEntity userModel, String check) {
		// logger.info("saveBulkUserProfile userModel = " + userModel);

		Calendar date = Calendar.getInstance();
		Date cur_dt = new Date();
		cur_dt.setHours(date.getTime().getHours());
		cur_dt.setMinutes(date.getTime().getMinutes());
		cur_dt.setSeconds(date.getTime().getSeconds());

		String corps = "";
		String created_by = "";
		Date dt = null;
		String id = "";

		try {

			if (userModel.getEffective_date() != null) {
				dt = new Date(userModel.getEffective_date().getTime());
				dt.setHours(date.getTime().getHours());
				dt.setMinutes(date.getTime().getMinutes());
				dt.setSeconds(date.getTime().getSeconds());
			}
			corps = userModel.getAssc_corps();
			created_by = userModel.getCreated_by();

			String[] corpsData = {};
			String complnId = "";

			if (userModel.getCommPlanId() != null) {
				complnId = userModel.getCommPlanId();
			} else {
				complnId = userModel.getComm_plan_id().toString();
			}

			String empid = "";

			if (!userModel.getEmployeeId().equals("N/A")) {
				empid = userModel.getEmployeeId();
			} else {
				String scempId = jdbcTemplate.queryForObject("select max(sc_emp_id) from c_employee_master cem",
						String.class);
				int empId = Integer.parseInt(scempId);
				empid = Integer.toString(empId);
			}

			String scempId = jdbcTemplate
					.queryForObject("select sc_emp_id from c_employee_master cem where employee_id ='"
							+ userModel.getEmployeeId() + "' " + "and effective_date =(select max(effective_date)  "
							+ "from c_employee_master cem where employee_id ='" + userModel.getEmployeeId()
							+ "') order by sc_emp_id desc limit 1", String.class);

			if (check.equals("true")) {
				Pattern p = Pattern.compile(".*_\\s*(.*)");
				Matcher m = p.matcher(scempId);

				if (m.find()) {

					int val = Integer.parseInt(m.group(1));
					int val1 = val + 1;
					String empidstr = scempId.substring(0, scempId.indexOf("_"));
					String finalempidstr = empidstr + "_" + String.valueOf(val1);
					id = finalempidstr;
				}

			} else {
				id = scempId;
			}

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = "2900-11-11";
			Date softDate = null;
			Date vacStartDate = null;
			Date VacEndDate = null;
			Date terminationDate = null;
			Date joiningDate = null;
			String repid = "";
			String operatorid = "";
			int stdhrs = 0;

			// logger.info("getSoft_termination_date = " +
			// userModel.getSoft_termination_date());
			if (userModel.getSoft_termination_date() == null) {
				// logger.info("Inside SOFT IF");
				softDate = sdf.parse(dateString);
			} else {
				// logger.info("Inside SOFT ELSE");
				softDate = userModel.getSoft_termination_date();
			}

			// logger.info("getVacation_start_date = " +
			// userModel.getVacation_start_date());
			if (userModel.getVacation_start_date() == null) {
				// logger.info("Inside vacStartDate IF");
				vacStartDate = sdf.parse(dateString);
			} else {
				// logger.info("Inside vacStartDate ELSE");
				vacStartDate = userModel.getVacation_start_date();
			}

			// logger.info("getVacation_end_date = " + userModel.getVacation_end_date());
			if (userModel.getVacation_end_date() == null) {
				// logger.info("Inside VacEndDate IF");
				VacEndDate = sdf.parse(dateString);
			} else {
				// logger.info("Inside VacEndDate ELSE");
				VacEndDate = userModel.getVacation_end_date();
			}

			// logger.info("getSales_rep_id = " + userModel.getSales_rep_id());
			if (userModel.getSales_rep_id() == null) {
				// logger.info("Inside getSales_rep_id IF");
				repid = "";
				operatorid = "";
			} else {
				// logger.info("Inside getSales_rep_id ELSE");
				repid = userModel.getSales_rep_id();
				operatorid = userModel.getOperator_id();
			}

			// logger.info("getTermination_date = " + userModel.getTermination_date());
			if (userModel.getTermination_date() == null) {
				// logger.info("Inside getTermination_date IF");
				terminationDate = sdf.parse(dateString);
			} else {
				// logger.info("Inside getTermination_date ELSE");
				terminationDate = userModel.getTermination_date();
			}

			// logger.info("getStd_hours = " + userModel.getStd_hours());
			if (userModel.getStd_hours() == null) {
				// logger.info("Inside getStd_hours IF");
				stdhrs = 0;
			} else {
				// logger.info("Inside getStd_hours ELSE");
				stdhrs = userModel.getStd_hours();
			}

			// logger.info("getJoining_date = " + userModel.getJoining_date());
			if (userModel.getJoining_date() == null) {
				// logger.info("Inside getJoining_date IF");
				joiningDate = sdf.parse(dateString);
			} else {
				// logger.info("Inside getJoining_date ELSE");
				joiningDate = userModel.getJoining_date();
			}

			logger.info("id = " + id);
			logger.info("empid = " + empid);
			logger.info("repid = " + repid);
			logger.info("dt = " + dt);

			String fname = userModel.getFirst_name();
			if (fname != null && !fname.trim().isEmpty()) {
				if (fname.contains("'")) {
					fname = fname.replace("'", "");
					logger.info("fname = " + fname);
				}
			}

			String mname = userModel.getMiddle_name();
			if (mname != null && !mname.trim().isEmpty()) {
				if (mname.contains("'")) {
					mname = mname.replace("'", "");
					logger.info("mname = " + mname);
				}
			}

			String lname = userModel.getLast_name();
			if (lname != null && !lname.trim().isEmpty()) {
				if (lname.contains("'")) {
					lname = lname.replace("'", "");
					logger.info("lname = " + lname);
				}
			}

			String pref_first_name = userModel.getPref_first_name();
			if (pref_first_name != null && !pref_first_name.trim().isEmpty()) {
				if (pref_first_name.contains("'")) {
					pref_first_name = pref_first_name.replace("'", "");
					logger.info("pref_first_name = " + pref_first_name);
				}
			}

			String pref_last_name = userModel.getPref_last_name();
			if (pref_last_name != null && !pref_last_name.trim().isEmpty()) {
				if (pref_last_name.contains("'")) {
					pref_last_name = pref_last_name.replace("'", "");
					logger.info("pref_last_name = " + pref_last_name);
				}
			}

			String supervisor_pref_name = userModel.getSupervisor_pref_name();
			if (supervisor_pref_name != null && !supervisor_pref_name.trim().isEmpty()) {
				if (supervisor_pref_name.contains("'")) {
					supervisor_pref_name = supervisor_pref_name.replace("'", "");
					logger.info("supervisor_pref_name = " + supervisor_pref_name);
				}
			}

			jdbcTemplate.update(
					"INSERT INTO c_employee_master (first_name,middle_name,last_name,created_by,created_dt,employee_id,sc_emp_id,"
							+ "sales_rep_id,effective_date,network_id,email_address,work_status,soft_termination_date,supervisor_pref_name,"
							+ "pref_first_name,pref_last_name,profile_status,comm_plan_id,user_type,user_role,sales_rep_channel,"
							+ "sales_rep_channel_desc,calendar_type,supervisor_id,supervisor_name,operator_id,sales_rep_type,"
							+ "joining_date,std_hours,termination_date,vacation_start_date,vacation_end_date) "
							+ "VALUES ('" + fname + "','" + mname + "','" + lname + "'," + "'"
							+ userModel.getCreated_by() + "'," + "'" + userModel.getCreated_dt() + "'," + "'" + empid
							+ "','" + id + "','" + repid + "','" + dt + "','" + userModel.getNetwork_id() + "'," + "'"
							+ userModel.getEmail_address() + "','" + userModel.getWork_status() + "','" + softDate
							+ "'," + "" + "'" + supervisor_pref_name + "','" + pref_first_name + "','" + pref_last_name
							+ "'," + "'" + userModel.getProfileStatus() + "','" + Integer.parseInt(complnId) + "','"
							+ userModel.getUser_type() + "','" + userModel.getUserRole() + "','"
							+ userModel.getSales_rep_channel() + "','" + userModel.getSales_rep_channel_desc() + "',"
							+ "'" + userModel.getCalendar_type() + "','" + userModel.getSupervisor_id() + "','"
							+ userModel.getSupervisor_name() + "','" + operatorid + "','"
							+ userModel.getSales_rep_type() + "'," + "'" + joiningDate + "','" + stdhrs + "','"
							+ terminationDate + "','" + vacStartDate + "'," + "'" + VacEndDate + "')");

//			String maxId = jdbcTemplate.queryForObject("select max(sc_emp_id) from c_employee_master cem",
//					String.class);

			if (!corps.equals("")) {

				String cnt = jdbcTemplate
						.queryForObject("select count(sc_emp_id) cnt from c_employee_corp_reln cecr where sc_emp_id ='"
								+ id + "' and effective_date ='" + dt + "'", String.class);
				// logger.info("cnt = " + cnt);
				if (!cnt.equals("0")) {
					// logger.info("Inside corps cnt IF");
					String deleteQuery = "delete from c_employee_corp_reln where sc_emp_id=? and effective_date =? ";
					jdbcTemplate.update(deleteQuery, id, dt);

				} else {
					logger.info("Inside corps cnt ELSE");

				}

				corpsData = corps.split(",");
				if (userModel.getCommPlanId() != null) {
					for (int i = 0; i < corpsData.length; i++) {

						EmployeeCorpEntity EmployeeCorpEntity = new EmployeeCorpEntity();
						EmployeeCorpEntity.setSc_emp_id(id);
						EmployeeCorpEntity.setCorp(Integer.parseInt(corpsData[i]));
						EmployeeCorpEntity.setEffective_date(dt);
						EmployeeCorpEntity.setCreated_by(created_by);
						EmployeeCorpEntity.setCreated_dt(new Date());
						EmployeeCorpEntity.setValid_from_dt(cur_dt);
						employeeCorpRepository.save(EmployeeCorpEntity);
					}
				}
			} else {
//				EmployeeCorpEntity EmployeeCorpEntity = new EmployeeCorpEntity();
//				EmployeeCorpEntity.setSc_emp_id(id);
//				EmployeeCorpEntity.setCreated_by(created_by);
//				EmployeeCorpEntity.setCreated_dt(new Date());
//				EmployeeCorpEntity.setValid_from_dt(cur_dt);
//				employeeCorpRepository.save(EmployeeCorpEntity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userModel;
	}

	@SuppressWarnings("deprecation")
	@Override
	public EmployeeMasterEntity saveAdmin(List<EmployeeMasterEntity> userModels) throws DuplicateRecordException {
		logger.info("saveUserProfile userModels = " + userModels);

		Calendar date = Calendar.getInstance();
		Date cur_dt = new Date();
		cur_dt.setHours(date.getTime().getHours());
		cur_dt.setMinutes(date.getTime().getMinutes());
		cur_dt.setSeconds(date.getTime().getSeconds());

		String corps = "";
		String created_by = "";
		Date dt = null;
		String id = "";

		for (EmployeeMasterEntity userModel : userModels) {
			if (userModel.getEffective_date() != null) {
				dt = new Date(userModel.getEffective_date().getTime());
				dt.setHours(date.getTime().getHours());
				dt.setMinutes(date.getTime().getMinutes());
				dt.setSeconds(date.getTime().getSeconds());
			}
			corps = userModel.getAssc_corps();
			created_by = userModel.getCreated_by();

			logger.info("getCreated_by.length = " + userModel.getCreated_by().length());
			for (int i = 0; i < 1; i++) {
				logger.info("Inside for loop");

				String empid = "";

				if (!userModel.getEmployeeId().equals("N/A")) {
					empid = userModel.getEmployeeId();
				} else {
					String scempId = jdbcTemplate.queryForObject("select max(sc_emp_id) from c_employee_master cem",
							String.class);
					// int empId = scempId + 1;
					int empId = Integer.parseInt(scempId);
					empid = Integer.toString(empId);
				}

				logger.info("getCheckboxchecked = " + userModel.getCheckboxchecked());

				String scempId_cnt = jdbcTemplate.queryForObject(
						"select count(sc_emp_id) cnt from c_employee_master cem where employee_id ='"
								+ userModel.getEmployeeId() + "' " + "and effective_date =(select max(effective_date)  "
								+ "from c_employee_master cem where employee_id ='" + userModel.getEmployeeId() + "') ",
						String.class);

				String scempId = "";
				logger.info("scempId = " + userModel.getId());

				if (scempId_cnt.equals("0")) {
					scempId = userModel.getId();
				} else {
					scempId = jdbcTemplate
							.queryForObject(
									"select sc_emp_id from c_employee_master cem where employee_id ='"
											+ userModel.getEmployeeId() + "' "
											+ "and effective_date =(select max(effective_date)  "
											+ "from c_employee_master cem where employee_id ='"
											+ userModel.getEmployeeId() + "') order by sc_emp_id desc limit 1",
									String.class);
				}

				logger.info("scempId = " + scempId);

				if (userModel.getCheckboxchecked().equals("true")) {
					logger.info("Inside IF getCheckboxchecked");
					// String sRepId = personalInfoRepository.getSalesRepID(userModel.getId());
					// System.out.println("getCheckboxchecked = " + userModel.getCheckboxchecked());

					logger.info("scempId = " + scempId);

					Pattern p = Pattern.compile(".*_\\s*(.*)");
					Matcher m = p.matcher(scempId);

					if (m.find()) {
						logger.info("value 2 = " + m.group(1));
						logger.info("value 1 = " + scempId.substring(0, scempId.indexOf("_")));

						int val = Integer.parseInt(m.group(1));
						int val1 = val + 1;
						String empidstr = scempId.substring(0, scempId.indexOf("_"));
						String finalempidstr = empidstr + "_" + String.valueOf(val1);
						logger.info("finalempidstr = " + finalempidstr);
						id = finalempidstr;
					}

					// String sempId = scempId.subString();
					// id = sempId;

				} else {
					logger.info("Inside ELSE getCheckboxchecked");
					id = scempId;// userModel.getId();
				}

				logger.info("getSupervisor_id = " + userModel.getSupervisor_id());
				logger.info("getSoft_termination_date = " + userModel.getSoft_termination_date());

				Date std = userModel.getSoft_termination_date();

				if (userModel.getSoft_termination_date() == null) {
					logger.info("inside IF");
					std = new GregorianCalendar(2900, Calendar.NOVEMBER, 11).getTime();
				}

//				int profileCnt = personalInfoRepository.getProfileCount(id, empid, userModel.getSales_rep_id(), dt);
//				logger.info("profileCnt = " + profileCnt);
//				if (profileCnt > 0) {
//					throw new DuplicateRecordException(String.valueOf("Record" + DUPLICATE_RECORD));
//				}

				int count = jdbcTemplate.update(
						"INSERT INTO c_employee_master (first_name,middle_name,last_name,created_by,created_dt,employee_id,sc_emp_id,"
								+ "sales_rep_id,effective_date,network_id,email_address,work_status,soft_termination_date,supervisor_pref_name,"
								+ "pref_first_name,pref_last_name,profile_status,comm_plan_id,user_type,user_role,sales_rep_channel,"
								+ "sales_rep_channel_desc,calendar_type,supervisor_id,supervisor_name,operator_id,sales_rep_type) "
								+ "VALUES ('" + userModel.getFirst_name() + "','" + userModel.getMiddle_name() + "','"
								+ userModel.getLast_name() + "'," + "'" + userModel.getCreated_by() + "'," + "'"
								+ userModel.getCreated_dt() + "'," + "'" + empid + "','" + id + "','"
								+ userModel.getSales_rep_id() + "','" + userModel.getCreated_dt() + "','"
								+ userModel.getNetwork_id() + "'," + "'" + userModel.getEmail_address() + "','"
								+ userModel.getWork_status() + "','" + std + "'," + "" + "'"
								+ userModel.getSupervisor_pref_name() + "','" + userModel.getPref_first_name() + "','"
								+ userModel.getPref_last_name() + "'," + "'" + userModel.getProfileStatus() + "',0,'"
								+ userModel.getUser_type() + "','" + userModel.getUserRole() + "','"
								+ userModel.getSales_rep_channel() + "','" + userModel.getSales_rep_channel_desc()
								+ "'," + "'" + userModel.getCalendar_type() + "','NA','"
								+ userModel.getSupervisor_name() + "','" + userModel.getOperator_id() + "','"
								+ userModel.getSales_rep_type() + "')");

				logger.info("count  = " + count);

			}

		}

		return userModels.get(0);
	}

	@SuppressWarnings("deprecation")
	@Override
	public EmployeeMasterEntity saveUserProfile(List<EmployeeMasterEntity> userModels) throws DuplicateRecordException {
		logger.info("saveUserProfile userModels = " + userModels);

		Calendar date = Calendar.getInstance();
		Date cur_dt = new Date();
		cur_dt.setHours(date.getTime().getHours());
		cur_dt.setMinutes(date.getTime().getMinutes());
		cur_dt.setSeconds(date.getTime().getSeconds());

		String corps = "";
		String created_by = "";
		Date dt = null;
		String id = "";

		for (EmployeeMasterEntity userModel : userModels) {
			if (userModel.getEffective_date() != null) {
				dt = new Date(userModel.getEffective_date().getTime());
				dt.setHours(date.getTime().getHours());
				dt.setMinutes(date.getTime().getMinutes());
				dt.setSeconds(date.getTime().getSeconds());
			}
			corps = userModel.getAssc_corps();
			created_by = userModel.getCreated_by();

			String[] corpsData = {};
			String[] complnId = {};

			if (userModel.getCommPlanId() != null) {
				complnId = userModel.getCommPlanId().split(",");
			} else {
				complnId = userModel.getComm_plan_id().toString().split(",");
			}

			logger.info("complnId.length = " + complnId.length);
			for (int i = 0; i < complnId.length; i++) {
				logger.info("Inside for loop");

				String empid = "";

				if (!userModel.getEmployeeId().equals("N/A")) {
					empid = userModel.getEmployeeId();
				} else {
					String scempId = jdbcTemplate.queryForObject("select max(sc_emp_id) from c_employee_master cem",
							String.class);
					// int empId = scempId + 1;
					int empId = Integer.parseInt(scempId);
					empid = Integer.toString(empId);
				}

				logger.info("getCheckboxchecked = " + userModel.getCheckboxchecked());

				String scempId_cnt = jdbcTemplate.queryForObject(
						"select count(sc_emp_id) cnt from c_employee_master cem where employee_id ='"
								+ userModel.getEmployeeId() + "' " + "and effective_date =(select max(effective_date)  "
								+ "from c_employee_master cem where employee_id ='" + userModel.getEmployeeId() + "') ",
						String.class);

				String scempId = "";
				logger.info("scempId = " + userModel.getId());

				if (scempId_cnt.equals("0")) {
					scempId = userModel.getId();
				} else {
					scempId = jdbcTemplate
							.queryForObject(
									"select sc_emp_id from c_employee_master cem where employee_id ='"
											+ userModel.getEmployeeId() + "' "
											+ "and effective_date =(select max(effective_date)  "
											+ "from c_employee_master cem where employee_id ='"
											+ userModel.getEmployeeId() + "') order by sc_emp_id desc limit 1",
									String.class);
				}

				logger.info("scempId = " + scempId);

				if (userModel.getCheckboxchecked().equals("true")) {
					logger.info("Inside IF getCheckboxchecked");
					// String sRepId = personalInfoRepository.getSalesRepID(userModel.getId());
					// System.out.println("getCheckboxchecked = " + userModel.getCheckboxchecked());

					logger.info("scempId = " + scempId);

					Pattern p = Pattern.compile(".*_\\s*(.*)");
					Matcher m = p.matcher(scempId);

					if (m.find()) {
						logger.info("value 2 = " + m.group(1));
						logger.info("value 1 = " + scempId.substring(0, scempId.indexOf("_")));

						int val = Integer.parseInt(m.group(1));
						int val1 = val + 1;
						String empidstr = scempId.substring(0, scempId.indexOf("_"));
						String finalempidstr = empidstr + "_" + String.valueOf(val1);
						logger.info("finalempidstr = " + finalempidstr);
						id = finalempidstr;
					}

					// String sempId = scempId.subString();
					// id = sempId;

				} else {
					logger.info("Inside ELSE getCheckboxchecked");
					id = scempId;// userModel.getId();
				}

				logger.info("getSupervisor_id = " + userModel.getSupervisor_id());
				logger.info("getSoft_termination_date = " + userModel.getSoft_termination_date());

				Date std = userModel.getSoft_termination_date();

				if (userModel.getSoft_termination_date() == null) {
					logger.info("inside IF");
					std = new GregorianCalendar(2900, Calendar.NOVEMBER, 11).getTime();
				}

				logger.info("std = " + std);
				logger.info("getSupervisor_id = " + userModel.getSupervisor_id());

				String supid = "";
				if (userModel.getSupervisor_id().length() == 5) {
					supid = "0" + userModel.getSupervisor_id();
				} else {
					supid = userModel.getSupervisor_id();
				}
				logger.info("id = " + id);
				logger.info("empid = " + empid);
				logger.info("getSales_rep_id = " + userModel.getSales_rep_id());
				logger.info("dt = " + dt);

				int profileCnt = personalInfoRepository.getProfileCount(id, empid, userModel.getSales_rep_id(), dt);
				logger.info("profileCnt = " + profileCnt);
				if (profileCnt > 0) {
					throw new DuplicateRecordException(String.valueOf("Record" + DUPLICATE_RECORD));
				}

				int count = jdbcTemplate.update(
						"INSERT INTO c_employee_master (first_name,middle_name,last_name,created_by,created_dt,employee_id,sc_emp_id,"
								+ "sales_rep_id,effective_date,network_id,email_address,work_status,soft_termination_date,supervisor_pref_name,"
								+ "pref_first_name,pref_last_name,profile_status,comm_plan_id,user_type,user_role,sales_rep_channel,"
								+ "sales_rep_channel_desc,calendar_type,supervisor_id,supervisor_name,operator_id,sales_rep_type) "
								+ "VALUES ('" + userModel.getFirst_name() + "','" + userModel.getMiddle_name() + "','"
								+ userModel.getLast_name() + "'," + "'" + userModel.getCreated_by() + "'," + "'"
								+ userModel.getCreated_dt() + "'," + "'" + empid + "','" + id + "','"
								+ userModel.getSales_rep_id() + "','" + dt + "','" + userModel.getNetwork_id() + "',"
								+ "'" + userModel.getEmail_address() + "','" + userModel.getWork_status() + "','" + std
								+ "'," + "" + "'" + userModel.getSupervisor_pref_name() + "','"
								+ userModel.getPref_first_name() + "','" + userModel.getPref_last_name() + "'," + "'"
								+ userModel.getProfileStatus() + "','" + Integer.parseInt(complnId[i]) + "','"
								+ userModel.getUser_type() + "','" + userModel.getUserRole() + "','"
								+ userModel.getSales_rep_channel() + "','" + userModel.getSales_rep_channel_desc()
								+ "'," + "'" + userModel.getCalendar_type() + "','" + supid + "','"
								+ userModel.getSupervisor_name() + "','" + userModel.getOperator_id() + "','"
								+ userModel.getSales_rep_type() + "')");

				logger.info("count = " + count);

//					String maxId = jdbcTemplate.queryForObject("select max(sc_emp_id) from c_employee_master cem",
//							String.class);

			}

			logger.info("corps = " + corps);

			if (!corps.equals("")) {
				// logger.info("Inside corps IF");

				String cnt = jdbcTemplate
						.queryForObject("select count(sc_emp_id) cnt from c_employee_corp_reln cecr where sc_emp_id ='"
								+ id + "' and effective_date ='" + dt + "'", String.class);
				// logger.info("cnt = " + cnt);
				if (!cnt.equals("0")) {
					// logger.info("Inside corps cnt IF");
					String deleteQuery = "delete from c_employee_corp_reln where sc_emp_id=? and effective_date =? ";
					jdbcTemplate.update(deleteQuery, id, dt);

				} else {
					logger.info("Inside corps cnt ELSE");

				}

				corpsData = corps.split(",");
				for (int i = 0; i < corpsData.length; i++) {
					// logger.info("corpsData = " + corpsData[i]);

					EmployeeCorpEntity EmployeeCorpEntity = new EmployeeCorpEntity();
					EmployeeCorpEntity.setSc_emp_id(id);
					EmployeeCorpEntity.setCorp(Integer.parseInt(corpsData[i]));
					EmployeeCorpEntity.setEffective_date(dt);
					EmployeeCorpEntity.setCreated_by(created_by);
					EmployeeCorpEntity.setCreated_dt(new Date());
					EmployeeCorpEntity.setValid_from_dt(cur_dt);
					employeeCorpRepository.save(EmployeeCorpEntity);
				}
			} else {
				logger.info("Inside corps ELSE");
			}
//			else {
//				logger.info("Inside 2nd ELSE");
//				EmployeeCorpEntity EmployeeCorpEntity = new EmployeeCorpEntity();
//				EmployeeCorpEntity.setSc_emp_id(id);
//				EmployeeCorpEntity.setCreated_by(created_by);
//				EmployeeCorpEntity.setCreated_dt(new Date());
//				EmployeeCorpEntity.setValid_from_dt(cur_dt);
//				employeeCorpRepository.save(EmployeeCorpEntity);
//			}
		}

		return userModels.get(0);
	}

	@SuppressWarnings("deprecation")
	@Override
	public EmployeeMasterEntity updateUserProfile(EmployeeMasterEntity userModel) {
		try {
			logger.info("updateUserProfile = " + userModel);

			int complanid = 0;

			if (userModel.getUserRole().equals("ADMIN")) {
				complanid = 0;
			} else {
				if (userModel.getCommPlanId() != null) {
					complanid = Integer.parseInt(userModel.getCommPlanId());
				} else {
					complanid = userModel.getComm_plan_id();
				}
			}

			String repid = "";
			String operatorid = "";

			if (userModel.getSales_rep_id() == null) {
				repid = "";
				operatorid = "";
			} else {
				repid = userModel.getSales_rep_id();
				operatorid = userModel.getOperator_id();
			}

			logger.info("getPageStatus = " + userModel.getPageStatus());
			logger.info("getProfileStatus = " + userModel.getProfileStatus());
			logger.info("getId = " + userModel.getId());
			logger.info("getRowid = " + userModel.getRowid());
			logger.info("getEffective_date = " + userModel.getEffective_date());
			logger.info("getEmployeeId = " + userModel.getEmployeeId());

			String updateQuery = "UPDATE c_employee_master set first_name=?,middle_name=?,last_name=?,updated_by=?,updated_dt=?,"
					+ "sales_rep_id=?,email_address=?,joining_date=?,work_status=?,soft_termination_date=?,"
					+ "supervisor_pref_name=?,pref_first_name=?,pref_last_name=?,profile_status=?,"
					+ "comm_plan_id=?,user_type=?,user_role=?,sales_rep_channel=?,sales_rep_channel_desc=?,"
					+ "calendar_type=?,supervisor_id=?,supervisor_name=?,operator_id=?,sales_rep_type=?,commissionable=?,effective_date=? "
					+ "where id = ? ";

			int updateCount = jdbcTemplate.update(updateQuery, userModel.getFirst_name(), userModel.getMiddle_name(),
					userModel.getLast_name(), userModel.getCreated_by(), userModel.getCreated_dt(), repid,
					userModel.getEmail_address(), userModel.getJoining_date(), userModel.getWork_status(),
					userModel.getSoft_termination_date(), userModel.getSupervisor_pref_name(),
					userModel.getPref_first_name(), userModel.getPref_last_name(), userModel.getProfileStatus(),
					complanid, userModel.getUser_type(), userModel.getUserRole(), userModel.getSales_rep_channel(),
					userModel.getSales_rep_channel_desc(), userModel.getCalendar_type(), userModel.getSupervisor_id(),
					userModel.getSupervisor_name(), operatorid, userModel.getSales_rep_type(),
					userModel.getCommissionable(), userModel.getEffective_date(), userModel.getRowid());

			logger.info("updateCount = " + updateCount);

			if (updateCount > 0) {

				String updateQuerySoftDate = "UPDATE c_employee_master set soft_termination_date=? where employee_id =? and effective_date >= ? ";
				int updateCountSoftDate = jdbcTemplate.update(updateQuerySoftDate, userModel.getSoft_termination_date(),
						userModel.getEmployeeId(), userModel.getEffective_date());

				if (updateCountSoftDate > 0) {

					String corps = "";
					String created_by = "";

					Calendar date = Calendar.getInstance();
					Date cur_dt = new Date();
					cur_dt.setHours(date.getTime().getHours());
					cur_dt.setMinutes(date.getTime().getMinutes());
					cur_dt.setSeconds(date.getTime().getSeconds());

					corps = userModel.getAssc_corps();
					created_by = userModel.getCreated_by();

					String[] corpsData = {};
					if (!corps.equals("")) {

						String deleteQuery = "delete from c_employee_corp_reln where sc_emp_id=? and effective_date=? ";
						jdbcTemplate.update(deleteQuery, userModel.getId(), userModel.getEffective_date());

						corpsData = corps.split(",");

						for (int i = 0; i < corpsData.length; i++) {
							EmployeeCorpEntity EmployeeCorpEntity = new EmployeeCorpEntity();
							EmployeeCorpEntity.setSc_emp_id(userModel.getId());
							EmployeeCorpEntity.setCorp(Integer.parseInt(corpsData[i]));
							EmployeeCorpEntity.setEffective_date(userModel.getEffective_date());
							EmployeeCorpEntity.setCreated_by(created_by);
							EmployeeCorpEntity.setCreated_dt(new Date());
							EmployeeCorpEntity.setValid_from_dt(cur_dt);
							employeeCorpRepository.save(EmployeeCorpEntity);
						}
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return userModel;
	}

	@Override
	public List<EmployeeMasterEntity> getUserFields() {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_PersonalInfoFields);
		List<EmployeeMasterEntity> userInfo = new ArrayList<EmployeeMasterEntity>();
		for (Map row : rows) {
			EmployeeMasterEntity userModel = new EmployeeMasterEntity();
			String fieldName = (String) row.get("field_name");

			if (fieldName.equals("User Type")) {
				userModel.setUser_type((String) row.get("description"));
			} else if (fieldName.equals("Sales Channel")) {
				userModel.setSales_rep_channel((String) row.get("field_value"));

				userModel.setSales_rep_channel_desc((String) row.get("description"));
			} else if (fieldName.equals("Sales Rep Type")) {

				userModel.setSales_rep_type((String) row.get("description"));
			} else if (fieldName.equals("PayrollCalendar")) {
				userModel.setCalendar_type((String) row.get("field_value"));
			} else if (fieldName.equals("Language")) {
				userModel.setLanguage((String) row.get("field_value"));
			}

			userInfo.add(userModel);
		}

		List<Map<String, Object>> values = jdbcTemplate.queryForList(GET_CommPlans);
		for (Map value : values) {
			EmployeeMasterEntity userModel = new EmployeeMasterEntity();
			userModel.setCommission_plan((String) value.get("comm_plan"));
			userModel.setComm_plan_id((Integer) value.get("comm_plan_id"));
			userInfo.add(userModel);
		}

		logger.info("userInfo = " + userInfo);

		return userInfo;
	}

	@Override
	public List<EmployeeMasterEntity> getUserInfoByDate(String effective_date) {
		System.out.println("getUserInfoByDate effective_date = " + effective_date);
		List<Map<String, Object>> corpRows = jdbcTemplate.queryForList(FIND_ASSC_CORPS);
		List<EmployeeMasterEntity> corpsInfo = new ArrayList<EmployeeMasterEntity>();
		for (Map corpRow : corpRows) {
			EmployeeMasterEntity userCorps = new EmployeeMasterEntity();
			userCorps.setId((String) corpRow.get("sc_emp_id"));
			userCorps.setCorp(((BigDecimal) corpRow.get("corp")).intValue());
			corpsInfo.add(userCorps);
		}
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(FIND_COMPLETED_PROFILES_BY_DATE,
				new Object[] { effective_date });

		List<EmployeeMasterEntity> userInfo = new ArrayList<EmployeeMasterEntity>();
		for (Map row : rows) {
			EmployeeMasterEntity user = new EmployeeMasterEntity();
			user.setEmployeeId((String) row.get("employee_id"));
			user.setNetwork_id((String) row.get("network_id"));
			user.setEmail_address((String) row.get("email_address"));
			user.setFirst_name((String) row.get("first_name"));
			user.setLast_name((String) row.get("last_name"));
			user.setMiddle_name((String) row.get("middle_name"));
			user.setName((String) row.get("empname"));
			user.setUserRole((String) row.get("user_role"));
			user.setUser_type((String) row.get("user_type"));
			user.setSales_rep_channel((String) row.get("sales_rep_channel"));
			user.setSales_channel_desc((String) row.get("sales_channel_desc"));

			user.setSales_rep_type((String) row.get("sales_rep_type"));
			user.setSales_rep_id((String) row.get("sales_rep_id"));
			user.setCalendar_type((String) row.get("calendar_type"));
			user.setSoft_termination_date((Date) row.get("soft_termination_date"));
			user.setTermination_date((Date) row.get("termination_date"));
			// user.setAssc_corps((String) row.get("assc_corps"));
			user.setOperator_id((String) row.get("operator_id"));
			user.setEffective_date((Date) row.get("effective_date"));
			user.setCommission_plan((String) row.get("comm_plan"));
			user.setActiveCommPeriod((String) row.get("activeCommPeriod"));
			user.setCreated_by((String) row.get("created_by"));
			user.setCreated_dt((Date) row.get("created_dt"));
			user.setProfileStatus((String) row.get("profile_status"));
			if (row.get("comm_plan_id") != null)
				user.setComm_plan_id(((Integer) row.get("comm_plan_id")).intValue());
			if (row.get("joining_date") != null)
				user.setJoining_date((Date) row.get("joining_date"));
			if (row.get("work_status") != null)
				user.setWork_status((String) row.get("work_status"));
			if (row.get("supervisor_name") != null)
				user.setSupervisor_name((String) row.get("supervisor_name"));
			if (row.get("supervisor_pref_name") != null)
				user.setSupervisor_pref_name((String) row.get("supervisor_pref_name"));
			if (row.get("pref_first_name") != null)
				user.setPref_first_name((String) row.get("pref_first_name"));
			if (row.get("pref_last_name") != null)
				user.setPref_last_name((String) row.get("pref_last_name"));
			if (row.get("supervisor_id") != null)
				user.setSupervisor_id((String) row.get("supervisor_id"));
			if (row.get("std_hours") != null)
				user.setStd_hours(((BigDecimal) row.get("std_hours")).intValue());
			if (row.get("commissionable") != null)
				user.setCommissionable((String) row.get("commissionable"));
			if (row.get("sc_emp_id") != null)

				user.setId((String) row.get("sc_emp_id"));

			userInfo.add(user);
		}

		for (EmployeeMasterEntity user : userInfo) {
			List<Integer> corplist = new ArrayList<>();
			for (EmployeeMasterEntity corp : corpsInfo) {
				if (corp.getId().equals(user.getId())) {
					corplist.add(corp.getCorp());
				}
			}
			String asscCorps = corplist.stream().map(String::valueOf).collect(Collectors.joining(", "));
			user.setAssc_corps(asscCorps);
		}

		return userInfo;
	}

	public List<EmployeeMasterEntity> getPendingUsers1() {
		List<EmployeeMasterEntity> userInfo = new ArrayList<EmployeeMasterEntity>();
		userInfo = personalInfoRepository.findByProfileStatus("inactive");
		System.out.println("userInfo = " + userInfo);
		return userInfo;
	}

	@Override
	public List<EmployeeMasterEntity> getPendingUsers() {

		List<Map<String, Object>> corpRows = jdbcTemplate.queryForList(FIND_ASSC_CORPS);
		List<EmployeeMasterEntity> corpsInfo = new ArrayList<EmployeeMasterEntity>();
		for (Map corpRow : corpRows) {
			EmployeeMasterEntity userCorps = new EmployeeMasterEntity();
			userCorps.setId((String) corpRow.get("sc_emp_id"));
			userCorps.setCorp(((BigDecimal) corpRow.get("corp")).intValue());
			corpsInfo.add(userCorps);
		}
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(FIND_PENDING_PROFILES);

		String sql = "SELECT "
				+ "to_char((date_trunc('month', now()::date) + interval '-2 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_three,"
				+ "to_char((date_trunc('month', now()::date) + interval '-1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_three,"
				+ "to_char((date_trunc('month', now()::date) + interval '-1 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_second,"
				+ "to_char((date_trunc('month', now()::date) + interval '0 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_second,"
				+ "to_char((date_trunc('month', now()::date) + interval '0 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_current,"
				+ "to_char((date_trunc('month', now()::date) + interval '1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_current";

		List<Map<String, Object>> rows1 = jdbcTemplate.queryForList(sql);

		String start_of_month_current_dt = null;
		String end_of_month_current_dt = null;

		String start_of_month_second_dt = null;
		String end_of_month_second_dt = null;

		String start_of_month_three_dt = null;
		String end_of_month_three_dt = null;

		for (Map row1 : rows1) {
			start_of_month_current_dt = (String) row1.get("start_of_month_current");
			end_of_month_current_dt = (String) row1.get("end_of_month_current");

			start_of_month_second_dt = (String) row1.get("start_of_month_second");
			end_of_month_second_dt = (String) row1.get("end_of_month_second");

			start_of_month_three_dt = (String) row1.get("start_of_month_three");
			end_of_month_three_dt = (String) row1.get("end_of_month_three");

		}

		List<EmployeeMasterEntity> userInfo = new ArrayList<EmployeeMasterEntity>();
		for (Map row : rows) {
			EmployeeMasterEntity user = new EmployeeMasterEntity();
			user.setEmployeeId((String) row.get("employee_id"));
			user.setNetwork_id((String) row.get("network_id"));
			user.setEmail_address((String) row.get("email_address"));
			user.setFirst_name((String) row.get("first_name"));
			user.setLast_name((String) row.get("last_name"));
			user.setMiddle_name((String) row.get("middle_name"));
			user.setName((String) row.get("empname"));

			// System.out.println("===========================================");
			// System.out.println("employee_id = "+row.get("employee_id"));

			String complan1 = getComPlan((String) row.get("employee_id"), start_of_month_current_dt,
					end_of_month_current_dt, "pending");
			// System.out.println("complan1 = "+complan1);
			String complan2 = getComPlan((String) row.get("employee_id"), start_of_month_second_dt,
					end_of_month_second_dt, "pending");
			String complan3 = getComPlan((String) row.get("employee_id"), start_of_month_three_dt,
					end_of_month_three_dt, "pending");

			if (complan1.equals("NA")) {
				// System.out.println("comm_plan = "+row.get("comm_plan"));
				user.setCommission_plan(
						"(" + end_of_month_current_dt.toString() + ")-" + (String) row.get("comm_plan"));
			} else {
				user.setCommission_plan("(" + end_of_month_current_dt.toString() + ")-" + complan1);
			}

			if (complan2.equals("NA")) {
				// System.out.println("comm_plan = "+row.get("comm_plan"));
				user.setComPlanTwo("(" + end_of_month_second_dt.toString() + ")-" + (String) row.get("comm_plan"));
			} else {
				user.setComPlanTwo("(" + end_of_month_second_dt.toString() + ")-" + complan2);
			}

			if (complan3.equals("NA")) {
				// System.out.println("comm_plan = "+row.get("comm_plan"));
				user.setComPlanThree("(" + end_of_month_three_dt.toString() + ")-" + (String) row.get("comm_plan"));
			} else {
				user.setComPlanThree("(" + end_of_month_three_dt.toString() + ")-" + complan3);
			}

			user.setUserRole((String) row.get("user_role"));
			user.setUser_type((String) row.get("user_type"));
			user.setSales_rep_channel((String) row.get("sales_rep_channel"));
			user.setSales_channel_desc((String) row.get("sales_channel_desc"));
			user.setSales_rep_type((String) row.get("sales_rep_type"));
			user.setSales_rep_id((String) row.get("sales_rep_id"));
			user.setCalendar_type((String) row.get("calendar_type"));
			user.setSoft_termination_date((Date) row.get("soft_termination_date"));
			user.setTermination_date((Date) row.get("termination_date"));
			// user.setAssc_corps((String) row.get("assc_corps"));
			user.setOperator_id((String) row.get("operator_id"));
			user.setEffective_date((Date) row.get("effective_date"));

			user.setActiveCommPeriod((String) row.get("activeCommPeriod"));
			user.setCreated_by((String) row.get("created_by"));
			user.setCreated_dt((Date) row.get("created_dt"));
			user.setProfileStatus((String) row.get("profile_status"));
			if (row.get("comm_plan_id") != null)
				user.setComm_plan_id(((Integer) row.get("comm_plan_id")).intValue());
			if (row.get("joining_date") != null)
				user.setJoining_date((Date) row.get("joining_date"));
			if (row.get("work_status") != null)
				user.setWork_status((String) row.get("work_status"));
			if (row.get("supervisor_name") != null)
				user.setSupervisor_name((String) row.get("supervisor_name"));
			if (row.get("supervisor_pref_name") != null)
				user.setSupervisor_pref_name((String) row.get("supervisor_pref_name"));
			if (row.get("pref_first_name") != null)
				user.setPref_first_name((String) row.get("pref_first_name"));
			if (row.get("pref_last_name") != null)
				user.setPref_last_name((String) row.get("pref_last_name"));
			if (row.get("supervisor_id") != null)
				user.setSupervisor_id((String) row.get("supervisor_id"));
			if (row.get("std_hours") != null)
				user.setStd_hours(((BigDecimal) row.get("std_hours")).intValue());
			if (row.get("commissionable") != null)
				user.setCommissionable((String) row.get("commissionable"));
			if (row.get("sc_emp_id") != null)

				user.setId((String) row.get("sc_emp_id"));

			userInfo.add(user);
		}

		for (EmployeeMasterEntity user : userInfo) {
			List<Integer> corplist = new ArrayList<>();
			for (EmployeeMasterEntity corp : corpsInfo) {
				if (corp.getId().equals(user.getId())) {
					corplist.add(corp.getCorp());
				}
			}
			String asscCorps = corplist.stream().map(String::valueOf).collect(Collectors.joining(", "));
			user.setAssc_corps(asscCorps);
		}

		return userInfo;
	}

	private List<UserModel> getPendingUsers(String profilestatus, String first_name, String last_name,
			String employeeId, String id, String supervisor_id, String user_type, String sales_rep_channel,
			int comm_plan_id, String payroll_start_date, String payroll_end_date) throws ParseException {

		logger.info("profilestatus = " + profilestatus);
		logger.info("first_name = " + first_name);

		List<Map<String, Object>> corpRows = jdbcTemplate.queryForList(FIND_ASSC_CORPS);
		List<UserModel> corpsInfo = new ArrayList<UserModel>();

		for (Map corpRow : corpRows) {
			UserModel userCorps = new UserModel();
			userCorps.setId((String) corpRow.get("sc_emp_id"));
			userCorps.setCorp(((BigDecimal) corpRow.get("corp")).intValue());
			corpsInfo.add(userCorps);
		}

		String sql = "";
		logger.info("payroll_end_date length() = " + payroll_end_date.length());
		if (payroll_end_date.length() == 4) {
			logger.info("Inside IF");
			sql = "SELECT "
					+ "to_char((date_trunc('month', now()::date) + interval '-2 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_three,"
					+ "to_char((date_trunc('month', now()::date) + interval '-1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_three,"
					+ "to_char((date_trunc('month', now()::date) + interval '-1 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_second,"
					+ "to_char((date_trunc('month', now()::date) + interval '0 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_second,"
					+ "to_char((date_trunc('month', now()::date) + interval '0 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_current,"
					+ "to_char((date_trunc('month', now()::date) + interval '1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_current";
		} else {
			logger.info("Inside ELSE");
			Date payrollEndDate = utilities.getStringToDate(payroll_end_date);
			logger.info("payrollEndDate = " + payrollEndDate);
			sql = "SELECT " + "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-2 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_three,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_three,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-1 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_second,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '0 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_second,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '0 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_current,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_current";
		}

		List<Map<String, Object>> rows1 = jdbcTemplate.queryForList(sql);

		String start_of_month_current_dt = null;
		String end_of_month_current_dt = null;

		String start_of_month_second_dt = null;
		String end_of_month_second_dt = null;

		String start_of_month_three_dt = null;
		String end_of_month_three_dt = null;

		for (Map row1 : rows1) {
			start_of_month_current_dt = (String) row1.get("start_of_month_current");
			end_of_month_current_dt = (String) row1.get("end_of_month_current");

			start_of_month_second_dt = (String) row1.get("start_of_month_second");
			end_of_month_second_dt = (String) row1.get("end_of_month_second");

			start_of_month_three_dt = (String) row1.get("start_of_month_three");
			end_of_month_three_dt = (String) row1.get("end_of_month_three");
		}

		logger.info("===============================");
		logger.info("first_name = " + first_name);
		logger.info("last_name = " + last_name);
		logger.info("employeeId = " + employeeId);
		logger.info("id = " + id);
		logger.info("supervisor_id = " + supervisor_id);
		logger.info("user_type = " + user_type);
		logger.info("sales_rep_channel = " + sales_rep_channel);
		logger.info("comm_plan_id = " + comm_plan_id);
		logger.info("payroll_start_date = " + payroll_start_date);
		logger.info("payroll_end_date = " + payroll_end_date);

		String first_name1 = "";
		String last_name1 = "";
		String employeeId1 = "";
		String id1 = "";
		String supervisor_id1 = "";

		String user_type1 = "";
		String sales_rep_channel1 = "";
		String comm_plan_id1 = "";

		if (first_name.equals("NA")) {
			first_name1 = "1";
		}
		if (last_name.equals("NA")) {
			last_name1 = "1";
		}
		if (employeeId.equals("NA")) {
			employeeId1 = "1";
		}
		if (id.equals("NA")) {
			id1 = "1";
		}
		if (supervisor_id.equals("0")) {
			supervisor_id1 = "1";
		}
		if (user_type.equals("NA")) {
			user_type1 = "1";
		}
		if (sales_rep_channel.equals("NA")) {
			sales_rep_channel1 = "1";
		}
		if (comm_plan_id == 0) {
			comm_plan_id1 = "1";
		}

		List<Map<String, Object>> users = null;

//		if (profilestatus.equals("complete")) {
		users = jdbcTemplate.queryForList(FIND_PENDING_PROFILES1, profilestatus, first_name1, "%" + first_name + "%",
				last_name1, "%" + last_name + "%", employeeId1, "%" + employeeId + "%", id1, "%" + id + "%",
				supervisor_id1, "%" + supervisor_id + "%", user_type1, user_type, sales_rep_channel1, sales_rep_channel,
				comm_plan_id1, comm_plan_id);
		// logger.info(comm_plan_id1);
//		} else {
//			users = jdbcTemplate.queryForList(FIND_INACTIVE_PROFILES);
//		}

		logger.info("users " + users);

		List<UserModel> userInfo = new ArrayList<UserModel>();

		for (Map row : users) {
			UserModel user = new UserModel();
			user.setEmployeeId((String) row.get("employee_id"));
			user.setNetwork_id((String) row.get("network_id"));
			user.setEmail_address((String) row.get("email_address"));
			user.setFirst_name((String) row.get("first_name"));
			user.setLast_name((String) row.get("last_name"));
			user.setMiddle_name((String) row.get("middle_name"));
			user.setName((String) row.get("empname"));

			logger.info("==============================================================");
			logger.info("employee_id = " + row.get("employee_id"));

			logger.info("start_of_month_current_dt = " + start_of_month_current_dt);
			logger.info("end_of_month_current_dt = " + end_of_month_current_dt);

			logger.info("start_of_month_current_dt = " + start_of_month_second_dt);
			logger.info("end_of_month_second_dt = " + end_of_month_second_dt);

			logger.info("start_of_month_current_dt = " + start_of_month_three_dt);
			logger.info("end_of_month_three_dt = " + end_of_month_three_dt);

			String complan1 = getComPlan((String) row.get("employee_id"), start_of_month_current_dt,
					end_of_month_current_dt, profilestatus);
			String complan2 = getComPlan((String) row.get("employee_id"), start_of_month_second_dt,
					end_of_month_second_dt, profilestatus);
			String complan3 = getComPlan((String) row.get("employee_id"), start_of_month_three_dt,
					end_of_month_three_dt, profilestatus);

			if (complan3.equals("NA")) {
				user.setComPlanThree(complan3);
			} else {
				user.setComPlanThree(complan3);
			}

			if (complan2.equals("NA")) {
				user.setComPlanTwo(complan2);
			} else {
				user.setComPlanTwo(complan2);
			}

			if (complan1.equals("NA")) {
				user.setCommission_plan((String) row.get("comm_plan"));
			} else {
				user.setCommission_plan(complan1);
			}

			user.setCurDate(end_of_month_current_dt);
			user.setPrevDateOne(end_of_month_second_dt);
			user.setPrevDateTwo(end_of_month_three_dt);
			user.setUserRole((String) row.get("user_role"));
			user.setUser_type((String) row.get("user_type"));
			user.setSales_rep_channel((String) row.get("sales_rep_channel"));
			user.setSales_channel_desc((String) row.get("sales_channel_desc"));
			user.setSales_rep_type((String) row.get("sales_rep_type"));
			user.setSales_rep_id((String) row.get("sales_rep_id"));
			user.setCalendar_type((String) row.get("calendar_type"));
			user.setSoft_termination_date((Date) row.get("soft_termination_date"));
			user.setSoftdate((String) row.get("softdate"));
			user.setTermination_date((Date) row.get("termination_date"));
			user.setOperator_id((String) row.get("operator_id"));
			user.setEffective_date((Date) row.get("effective_date"));
			user.setEdate((String) row.get("edate"));
			user.setActiveCommPeriod((String) row.get("activeCommPeriod"));
			user.setCreated_by((String) row.get("created_by"));
			user.setCreated_dt((Date) row.get("created_dt"));
			user.setProfileStatus((String) row.get("profile_status"));

			if (row.get("comm_plan_id") != null)
				user.setComm_plan_id(((Integer) row.get("comm_plan_id")).intValue());
			if (row.get("joining_date") != null)
				user.setJoining_date((Date) row.get("joining_date"));
			if (row.get("work_status") != null)
				user.setWork_status((String) row.get("work_status"));
			if (row.get("supervisor_name") != null)
				user.setSupervisor_name((String) row.get("supervisor_name"));
			if (row.get("supname") != null)
				user.setSupname((String) row.get("supname"));
			if (row.get("supervisor_pref_name") != null)
				user.setSupervisor_pref_name((String) row.get("supervisor_pref_name"));
			if (row.get("pref_first_name") != null)
				user.setPref_first_name((String) row.get("pref_first_name"));
			if (row.get("pref_last_name") != null)
				user.setPref_last_name((String) row.get("pref_last_name"));
			if (row.get("supervisor_id") != null)
				user.setSupervisor_id((String) row.get("supervisor_id"));
			if (row.get("std_hours") != null)
				user.setStd_hours(((BigDecimal) row.get("std_hours")).intValue());
			if (row.get("commissionable") != null)
				user.setCommissionable((String) row.get("commissionable"));
			if (row.get("sc_emp_id") != null)
				// user.setId(Integer.valueOf((int) row.get("sc_emp_id")).longValue());
				user.setId((String) row.get("sc_emp_id"));

			userInfo.add(user);
		}

		for (UserModel user : userInfo) {
			List<Integer> corplist = new ArrayList<>();
			for (UserModel corp : corpsInfo) {
				if (corp.getId().equals(user.getId())) {
					corplist.add(corp.getCorp());
				}
			}
			String asscCorps = corplist.stream().map(String::valueOf).collect(Collectors.joining(", "));
			user.setAssc_corps(asscCorps);
		}
		logger.info("userInfo = " + userInfo);
		return userInfo;
	}

	// @Override
	public List<UserModel> getUsers1(String profilestatus, String first_name, String last_name, String employeeId,
			String id, String supervisor_id, String user_type, String sales_rep_channel, int comm_plan_id,
			String payroll_start_date, String payroll_end_date, String userRole) throws ParseException {

		List<UserModel> userInfo = new ArrayList<UserModel>();
		if (profilestatus.equals("pending")) {
			userInfo = getPendingUsers(profilestatus, first_name, last_name, employeeId, id, supervisor_id, user_type,
					sales_rep_channel, comm_plan_id, payroll_start_date, payroll_end_date);
		} else {

			String sql_dates = getDates(payroll_end_date);

			List<Map<String, Object>> datesList = jdbcTemplate.queryForList(sql_dates);

			String start_of_month_current_dt = null;
			String end_of_month_current_dt = null;

			String start_of_month_second_dt = null;
			String end_of_month_second_dt = null;

			String start_of_month_three_dt = null;
			String end_of_month_three_dt = null;

			for (Map datesListObj : datesList) {
				start_of_month_current_dt = (String) datesListObj.get("start_of_month_current");
				end_of_month_current_dt = (String) datesListObj.get("end_of_month_current");

				start_of_month_second_dt = (String) datesListObj.get("start_of_month_second");
				end_of_month_second_dt = (String) datesListObj.get("end_of_month_second");

				start_of_month_three_dt = (String) datesListObj.get("start_of_month_three");
				end_of_month_three_dt = (String) datesListObj.get("end_of_month_three");
			}

			logger.info("===============================");
			logger.info("first_name = " + first_name);
			logger.info("last_name = " + last_name);
			logger.info("employeeId = " + employeeId);
			logger.info("id = " + id);
			logger.info("supervisor_id = " + supervisor_id);
			logger.info("user_type = " + user_type);
			logger.info("sales_rep_channel = " + sales_rep_channel);
			logger.info("comm_plan_id = " + comm_plan_id);
			logger.info("payroll_start_date = " + payroll_start_date);
			logger.info("payroll_end_date = " + payroll_end_date);

			String first_name1 = "";
			String last_name1 = "";
			String employeeId1 = "";
			String id1 = "";
			String supervisor_id1 = "";

			String user_type1 = "";
			String sales_rep_channel1 = "";
			String comm_plan_id1 = "";
			String payroll_start_date1 = "";

			if (first_name.equals("NA")) {
				first_name1 = "1";
			}
			if (last_name.equals("NA")) {
				last_name1 = "1";
			}
			if (employeeId.equals("NA")) {
				employeeId1 = "1";
			}
			if (id.equals("NA")) {
				id1 = "1";
			}
			if (supervisor_id.equals("0")) {
				supervisor_id1 = "1";
			}
			if (user_type.equals("NA")) {
				user_type1 = "1";
			}
			if (sales_rep_channel.equals("NA")) {
				sales_rep_channel1 = "1";
			}
			if (comm_plan_id == 0) {
				comm_plan_id1 = "1";
			}

			if (payroll_start_date.equals("null")) {
				logger.info("Inside IF start");
				payroll_start_date1 = "1";
				payroll_start_date = "1900-01-01";
			}

			if (payroll_end_date.equals("null")) {
				logger.info("Inside IF end");
				payroll_start_date1 = "1";
				payroll_end_date = "1900-01-01";
			}

			List<Map<String, Object>> users = null;

			logger.info("comm_plan_id = " + comm_plan_id);
			logger.info("userRole = " + userRole);
			String query = "";
			if (userRole.equals("ADMIN")) {
				logger.info("Inside IF");
				query = FIND_PROFILES_ADMIN;
			} else if (userRole.equals("IMPERSONATE")) {
				logger.info("Inside ELSE IF");
				query = FIND_PROFILES_REPORT;
			} else {
				logger.info("Inside ELSE");
				query = FIND_PROFILES;
			}

			users = jdbcTemplate.queryForList(query, profilestatus, first_name1, "%" + first_name + "%", last_name1,
					"%" + last_name + "%", employeeId1, "%" + employeeId + "%", id1, "%" + id + "%", supervisor_id1,
					"%" + supervisor_id + "%", user_type1, user_type, sales_rep_channel1, sales_rep_channel,
					comm_plan_id1, comm_plan_id, payroll_start_date1, payroll_start_date, payroll_end_date);

			logger.info("users " + users);

			for (Map row : users) {
				UserModel user = new UserModel();
				user.setEmployeeId((String) row.get("employee_id"));
				user.setNetwork_id((String) row.get("network_id"));
				user.setEmail_address((String) row.get("email_address"));
				user.setFirst_name((String) row.get("first_name"));
				user.setLast_name((String) row.get("last_name"));
				user.setMiddle_name((String) row.get("middle_name"));
				user.setName((String) row.get("empname"));

				if (userRole.equals("ADMIN")) {
					user.setComPlanThree("0");
					user.setComPlanTwo("0");
					user.setCommission_plan("0");

					user.setSupname1("NA");
					user.setSupname2("NA");
					user.setSupname3("NA");
				} else {
					String complan1 = getComPlan((String) row.get("employee_id"), start_of_month_current_dt,
							end_of_month_current_dt, profilestatus);
					String complan2 = getComPlan((String) row.get("employee_id"), start_of_month_second_dt,
							end_of_month_second_dt, profilestatus);
					String complan3 = getComPlan((String) row.get("employee_id"), start_of_month_three_dt,
							end_of_month_three_dt, profilestatus);

					logger.info("complan1 = " + complan1);
					logger.info("complan2 = " + complan2);
					logger.info("complan3 = " + complan3);

					if (complan3.equals("NA")) {
						user.setComPlanThree(complan3);
					} else {
						user.setComPlanThree(complan3);
					}

					if (complan2.equals("NA")) {
						user.setComPlanTwo(complan2);
					} else {
						user.setComPlanTwo(complan2);
					}

					if (complan1.equals("NA")) {
						user.setCommission_plan((String) row.get("comm_plan"));
					} else {
						user.setCommission_plan(complan1);
					}

					String supname1 = getSupervisorName((String) row.get("employee_id"), start_of_month_current_dt,
							end_of_month_current_dt);
					String supname2 = getSupervisorName((String) row.get("employee_id"), start_of_month_second_dt,
							end_of_month_second_dt);
					String supname3 = getSupervisorName((String) row.get("employee_id"), start_of_month_three_dt,
							end_of_month_three_dt);

					if (supname1.equals("NA")) {
						user.setSupname1("NA");
					} else {
						user.setSupname1(supname1);
					}

					if (supname2.equals("NA")) {
						user.setSupname2("NA");
					} else {
						user.setSupname2(supname2);
					}

					if (supname3.equals("NA")) {
						user.setSupname3("NA");
					} else {
						user.setSupname3(supname3);
					}
				}

				user.setCurDate(end_of_month_current_dt);
				user.setPrevDateOne(end_of_month_second_dt);
				user.setPrevDateTwo(end_of_month_three_dt);
				user.setUserRole((String) row.get("user_role"));
				user.setUser_type((String) row.get("user_type"));
				user.setSales_rep_channel((String) row.get("sales_rep_channel"));
				user.setSales_channel_desc((String) row.get("sales_channel_desc"));
				user.setSales_rep_type((String) row.get("sales_rep_type"));
				user.setSales_rep_id((String) row.get("sales_rep_id"));
				user.setCalendar_type((String) row.get("calendar_type"));
				user.setSoft_termination_date((Date) row.get("soft_termination_date"));
				user.setSoftdate((String) row.get("softdate"));
				user.setTermination_date((Date) row.get("termination_date"));
				user.setOperator_id((String) row.get("operator_id"));
				user.setEffective_date((Date) row.get("effective_date"));
				user.setEdate((String) row.get("edate"));
				user.setActiveCommPeriod((String) row.get("activeCommPeriod"));
				user.setCreated_by((String) row.get("created_by"));
				user.setCreated_dt((Date) row.get("created_dt"));
				user.setProfileStatus((String) row.get("profile_status"));
				user.setAssc_corps((String) row.get("asscCorps"));

				if (row.get("comm_plan_id") != null)
					user.setComm_plan_id(((Integer) row.get("comm_plan_id")).intValue());
				if (row.get("joining_date") != null)
					user.setJoining_date((Date) row.get("joining_date"));
				if (row.get("work_status") != null)
					user.setWork_status((String) row.get("work_status"));
				if (row.get("supervisor_name") != null)
					user.setSupervisor_name((String) row.get("supervisor_name"));
				if (row.get("supname") != null)
					user.setSupname((String) row.get("supname"));
				if (row.get("supervisor_pref_name") != null)
					user.setSupervisor_pref_name((String) row.get("supervisor_pref_name"));
				if (row.get("pref_first_name") != null)
					user.setPref_first_name((String) row.get("pref_first_name"));
				if (row.get("pref_last_name") != null)
					user.setPref_last_name((String) row.get("pref_last_name"));
				if (row.get("supervisor_id") != null)
					user.setSupervisor_id((String) row.get("supervisor_id"));
				if (row.get("std_hours") != null)
					user.setStd_hours(((BigDecimal) row.get("std_hours")).intValue());
				if (row.get("commissionable") != null)
					user.setCommissionable((String) row.get("commissionable"));
				if (row.get("sc_emp_id") != null)
					// user.setId(Integer.valueOf((int) row.get("sc_emp_id")).longValue());
					user.setId((String) row.get("sc_emp_id"));

				userInfo.add(user);
			}

			logger.info("userInfo = " + userInfo);

		}
		return userInfo;
	}

	@Override
	public List<EmployeeMasterEntity> validateCredentials(String loggedInEmployeeId, String loggedInNetworkId,
			String loggedInEmail) throws ParseException {
		logger.info("loggedInEmployeeId = " + loggedInEmployeeId);
		logger.info("loggedInNetworkId = " + loggedInNetworkId);
		logger.info("loggedInEmail = " + loggedInEmail);

		List<EmployeeMasterEntity> userInfo = new ArrayList<EmployeeMasterEntity>();

		int cnt = personalInfoRepository.getProfileStatusCount(loggedInEmployeeId, loggedInNetworkId);
		logger.info("cnt = " + cnt);

		if (cnt > 0) {

			userInfo = personalInfoRepository.validateCredentials(loggedInEmployeeId, loggedInNetworkId);
			logger.info("userInfo = " + userInfo);

			int tokenUsersCount = personalInfoRepository.getTokenUsersCount(loggedInNetworkId);
			logger.info("tokenUsersCount = " + tokenUsersCount);

			if (tokenUsersCount == 0) {
				String tokenPwd = "$2a$10$VcdzH8Q.o4KEo6df.XesdOmXdXQwT5ugNQvu1Pl0390rmfOeA1bhS";
				int usersCount = jdbcTemplate.update("INSERT INTO users (username,email,password) VALUES ('"
						+ loggedInNetworkId + "','" + loggedInEmail + "','" + tokenPwd + "')");
				logger.info("usersCount = " + usersCount);
				if (usersCount > 0) {
					int tokenId = personalInfoRepository.getTokenUserId(loggedInNetworkId);
					logger.info("tokenId = " + tokenId);
					jdbcTemplate.update("INSERT INTO users_roles (user_id,role_id) VALUES ('" + tokenId + "',3)");
				}

			} else {
				logger.info("Inside else: record available");
			}
		}
//		else {
//			userInfo = null;
//		}

		logger.info("userInfo = " + userInfo);

		return userInfo;

	}

	@Override
	public List<UserModel> getUserInfo(String profilestatus, String payroll_end_date) throws ParseException {

		System.out.println("profilestatus = " + profilestatus);
		System.out.println("payroll_end_date = " + payroll_end_date);

		List<Map<String, Object>> corpRows = jdbcTemplate.queryForList(FIND_ASSC_CORPS);
		List<UserModel> corpsInfo = new ArrayList<UserModel>();

		for (Map corpRow : corpRows) {
			UserModel userCorps = new UserModel();
			userCorps.setId((String) corpRow.get("sc_emp_id"));
			userCorps.setCorp(((BigDecimal) corpRow.get("corp")).intValue());
			corpsInfo.add(userCorps);
		}

		System.out.println("status = 1");

		List<Map<String, Object>> rows = null;

		if (profilestatus.equals("complete")) {
			rows = jdbcTemplate.queryForList(FIND_COMPLETED_PROFILES);
		} else {
			rows = jdbcTemplate.queryForList(FIND_INACTIVE_PROFILES);
		}

		System.out.println("status = 2");

		String sql = "";
		System.out.println("payroll_end_date length() = " + payroll_end_date.length());
		if (payroll_end_date.length() == 4) {
			System.out.println("Inside IF");
			sql = "SELECT "
					+ "to_char((date_trunc('month', now()::date) + interval '-2 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_three,"
					+ "to_char((date_trunc('month', now()::date) + interval '-1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_three,"
					+ "to_char((date_trunc('month', now()::date) + interval '-1 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_second,"
					+ "to_char((date_trunc('month', now()::date) + interval '0 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_second,"
					+ "to_char((date_trunc('month', now()::date) + interval '0 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_current,"
					+ "to_char((date_trunc('month', now()::date) + interval '1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_current";
		} else {
			System.out.println("Inside ELSE");
			Date payrollEndDate = utilities.getStringToDate(payroll_end_date);
			System.out.println("payrollEndDate = " + payrollEndDate);
			sql = "SELECT " + "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-2 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_three,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_three,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '-1 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_second,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '0 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_second,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '0 month' - interval '0 day')::date,'MM-DD-YYYY') AS start_of_month_current,"
					+ "to_char((date_trunc('month', '" + payrollEndDate
					+ "'::date) + interval '1 month' - interval '1 day')::date,'MM-DD-YYYY') AS end_of_month_current";
		}

		System.out.println("status = 3");

		List<Map<String, Object>> rows1 = jdbcTemplate.queryForList(sql);

		String start_of_month_current_dt = null;
		String end_of_month_current_dt = null;

		String start_of_month_second_dt = null;
		String end_of_month_second_dt = null;

		String start_of_month_three_dt = null;
		String end_of_month_three_dt = null;

		for (Map row1 : rows1) {
			start_of_month_current_dt = (String) row1.get("start_of_month_current");
			end_of_month_current_dt = (String) row1.get("end_of_month_current");

			start_of_month_second_dt = (String) row1.get("start_of_month_second");
			end_of_month_second_dt = (String) row1.get("end_of_month_second");

			start_of_month_three_dt = (String) row1.get("start_of_month_three");
			end_of_month_three_dt = (String) row1.get("end_of_month_three");
		}

		List<UserModel> userInfo = new ArrayList<UserModel>();

		for (Map row : rows) {
			UserModel user = new UserModel();
			user.setEmployeeId((String) row.get("employee_id"));
			user.setNetwork_id((String) row.get("network_id"));
			user.setEmail_address((String) row.get("email_address"));
			user.setFirst_name((String) row.get("first_name"));
			user.setLast_name((String) row.get("last_name"));
			user.setMiddle_name((String) row.get("middle_name"));
			user.setName((String) row.get("empname"));

			String complan1 = getComPlan((String) row.get("employee_id"), start_of_month_current_dt,
					end_of_month_current_dt, profilestatus);
			String complan2 = getComPlan((String) row.get("employee_id"), start_of_month_second_dt,
					end_of_month_second_dt, profilestatus);
			String complan3 = getComPlan((String) row.get("employee_id"), start_of_month_three_dt,
					end_of_month_three_dt, profilestatus);

			if (complan1.equals("NA")) {
				user.setCommission_plan((String) row.get("comm_plan"));
			} else {
				user.setCommission_plan(complan1);
			}

			if (complan2.equals("NA")) {
				user.setComPlanTwo((String) row.get("comm_plan"));
			} else {
				user.setComPlanTwo(complan2);
			}

			if (complan3.equals("NA")) {
				user.setComPlanThree((String) row.get("comm_plan"));
			} else {
				user.setComPlanThree(complan3);
			}

			user.setCurDate(end_of_month_current_dt);
			user.setPrevDateOne(end_of_month_second_dt);
			user.setPrevDateTwo(end_of_month_three_dt);
			user.setUserRole((String) row.get("user_role"));
			user.setUser_type((String) row.get("user_type"));
			user.setSales_rep_channel((String) row.get("sales_rep_channel"));
			user.setSales_channel_desc((String) row.get("sales_channel_desc"));
			user.setSales_rep_type((String) row.get("sales_rep_type"));
			user.setSales_rep_id((String) row.get("sales_rep_id"));
			user.setCalendar_type((String) row.get("calendar_type"));
			user.setSoft_termination_date((Date) row.get("soft_termination_date"));
			user.setSoftdate((String) row.get("softdate"));
			user.setTermination_date((Date) row.get("termination_date"));
			user.setOperator_id((String) row.get("operator_id"));
			user.setEffective_date((Date) row.get("effective_date"));
			user.setEdate((String) row.get("edate"));
			user.setActiveCommPeriod((String) row.get("activeCommPeriod"));
			user.setCreated_by((String) row.get("created_by"));
			user.setCreated_dt((Date) row.get("created_dt"));
			user.setProfileStatus((String) row.get("profile_status"));

			if (row.get("comm_plan_id") != null)
				user.setComm_plan_id(((Integer) row.get("comm_plan_id")).intValue());
			if (row.get("joining_date") != null)
				user.setJoining_date((Date) row.get("joining_date"));
			if (row.get("work_status") != null)
				user.setWork_status((String) row.get("work_status"));
			if (row.get("supervisor_name") != null)
				user.setSupervisor_name((String) row.get("supervisor_name"));
			if (row.get("supervisor_pref_name") != null)
				user.setSupervisor_pref_name((String) row.get("supervisor_pref_name"));
			if (row.get("pref_first_name") != null)
				user.setPref_first_name((String) row.get("pref_first_name"));
			if (row.get("pref_last_name") != null)
				user.setPref_last_name((String) row.get("pref_last_name"));
			if (row.get("supervisor_id") != null)
				user.setSupervisor_id((String) row.get("supervisor_id"));
			if (row.get("std_hours") != null)
				user.setStd_hours(((BigDecimal) row.get("std_hours")).intValue());
			if (row.get("commissionable") != null)
				user.setCommissionable((String) row.get("commissionable"));
			if (row.get("sc_emp_id") != null)
				// user.setId(Integer.valueOf((int) row.get("sc_emp_id")).longValue());
				user.setId((String) row.get("sc_emp_id"));

			userInfo.add(user);
		}

		for (UserModel user : userInfo) {
			List<Integer> corplist = new ArrayList<>();
			for (UserModel corp : corpsInfo) {
				if (corp.getId().equals(user.getId())) {
					corplist.add(corp.getCorp());
				}
			}
			String asscCorps = corplist.stream().map(String::valueOf).collect(Collectors.joining(", "));
			user.setAssc_corps(asscCorps);
		}
		System.out.println("userInfo = " + userInfo);
		return userInfo;
	}

	private String getComPlan(String empid, String startdt, String enddt, String status) {
		// logger.info("empid = " + empid);
		// logger.info("startdt = " + startdt);
		// logger.info("enddt = " + enddt);
		String complan = "";
		String complan_prev_cnt = "0";

		String sql_cnt = "select count(employee_id) from c_employee_master cem where cem.employee_id ='" + empid
				+ "' and cem.effective_date between '" + startdt + "' and '" + enddt + "' and comm_plan_id is not null";
		int complan_cnt = jdbcTemplate.queryForObject(sql_cnt, Integer.class);
		// logger.info("complan_cnt = " + complan_cnt);
		if (complan_cnt == 0) {

			if (complan.equals("pending")) {
				// logger.info("Inside IF");
				complan = "NA";
			} else {
				// logger.info("Inside ELSE");

				String sql_prev_cnt = "SELECT count(cem.id) cnt "
						+ "FROM c_employee_master cem, c_comm_plan_master ccpm  "
						+ "WHERE cem.comm_plan_id =ccpm.comm_plan_id  and cem.employee_id ='" + empid + "' "
						+ "and cem.effective_date = (select max(cem1.effective_date) from c_employee_master cem1 where cem.id = cem1.id  and cem1.effective_date <='"
						+ startdt + "')  "
						+ "and ccpm.effective_date = (select max(ccpm1.effective_date) from c_comm_plan_master ccpm1 "
						+ "where ccpm.comm_plan_id = ccpm1.comm_plan_id  and ccpm1.effective_date <='" + startdt
						+ "') ";
				complan_prev_cnt = jdbcTemplate.queryForObject(sql_prev_cnt, String.class);
				// logger.info("complan_prev_cnt = " + complan_prev_cnt);

				if (complan_prev_cnt.equals("0")) {
					complan = "NA";
				} else {
					String sql = "SELECT coalesce(ccpm.comm_plan,'NA') comm_plan "
							+ "FROM c_employee_master cem, c_comm_plan_master ccpm  "
							+ "WHERE cem.comm_plan_id =ccpm.comm_plan_id  and cem.employee_id ='" + empid + "' "
							+ "and cem.effective_date = (select max(cem1.effective_date) from c_employee_master cem1 where cem.id = cem1.id  and cem1.effective_date <='"
							+ startdt + "')  "
							+ "and ccpm.effective_date = (select max(ccpm1.effective_date) from c_comm_plan_master ccpm1 "
							+ "where ccpm.comm_plan_id = ccpm1.comm_plan_id  and ccpm1.effective_date <='" + startdt
							+ "')  "
							+ "group by cem.effective_date,ccpm.comm_plan order by cem.effective_date desc limit 1";
					complan = jdbcTemplate.queryForObject(sql, String.class);
				}

			}

		} else {
			if (complan.equals("pending")) {
				complan = "NA";
			} else {
				String sql = "SELECT coalesce(ccpm.comm_plan,'NA') comm_plan FROM c_employee_master cem, c_comm_plan_master ccpm  "
						+ "WHERE cem.comm_plan_id =ccpm.comm_plan_id  and cem.employee_id ='" + empid + "' and "
						+ "cem.effective_date BETWEEN '" + startdt + "' AND '" + enddt + "' "
						+ "group by cem.effective_date,ccpm.comm_plan,cem.first_name "
						+ "order by cem.effective_date desc,ccpm.comm_plan,cem.first_name limit 1";
				complan = jdbcTemplate.queryForObject(sql, String.class);
			}

		}

		return complan;

	}

	private String getSupervisorName(String empid, String start, String end) {
		// logger.info("empid = " + empid);
		// logger.info("start = " + start);
		// logger.info("end = " + end);
		String sup_name = "NA";
		String sup_name_cnt = "0";

		String sql_cnt = "select count(supervisor_id) from c_employee_master cem where cem.employee_id ='" + empid
				+ "' " + "and cem.effective_date between '" + start + "' and '" + end
				+ "' and comm_plan_id is not null";
		int sup_cnt = jdbcTemplate.queryForObject(sql_cnt, Integer.class);
		// logger.info(String.valueOf(sup_cnt));

		if (sup_cnt == 0) {
			String sql_prev_cnt = "select count(coalesce((SELECT concat(u2.first_name,' ',u2.last_name) FROM c_employee_master u2 where u2.employee_id  = cem.supervisor_id "
					+ "and cem.employee_id='" + empid + "'  limit 1),'NA')) cnt  "
					+ "from c_employee_master cem where cem.employee_id ='" + empid + "'   "
					+ "and cem.effective_date = (select max(cem1.effective_date) from c_employee_master cem1 where cem.id = cem1.id  and cem1.effective_date <='"
					+ start + "') ";
			sup_name_cnt = jdbcTemplate.queryForObject(sql_prev_cnt, String.class);

			if (sup_name_cnt.equals("0")) {
				sup_name = "NA";
			} else {
				String sql = "select coalesce((SELECT concat(u2.first_name,' ',u2.last_name) FROM c_employee_master u2 where u2.employee_id  = cem.supervisor_id "
						+ "and cem.employee_id='" + empid + "'  limit 1),'NA') as supervisor_name  "
						+ "from c_employee_master cem where cem.employee_id ='" + empid + "'   "
						+ "and cem.effective_date = (select max(cem1.effective_date) from c_employee_master cem1 where cem.id = cem1.id  and cem1.effective_date <='"
						+ start + "') " + "order by effective_date desc limit 1";
				sup_name = jdbcTemplate.queryForObject(sql, String.class);
			}

		} else {
			String sql = "select coalesce((SELECT concat(u2.first_name,' ',u2.last_name) FROM c_employee_master u2 where u2.employee_id  = cem.supervisor_id and cem.employee_id='"
					+ empid + "' " + "and cem.effective_date between '" + start + "' AND '" + end
					+ "' and comm_plan_id is not null limit 1),'NA') as supervisor_name  "
					+ "from c_employee_master cem " + "where cem.employee_id ='" + empid
					+ "' and cem.effective_date between '" + start + "' AND '" + end
					+ "' and comm_plan_id is not null limit 1";
			sup_name = jdbcTemplate.queryForObject(sql, String.class);
		}
		// logger.info(sup_name);
		return sup_name;

	}

	@Override
	public List<EmployeeMasterEntity> filterByEmpId(String empid) {
		logger.info("empid = " + empid);

		List<EmployeeMasterEntity> userInfo = new ArrayList<EmployeeMasterEntity>();
		userInfo = personalInfoRepository.getUserInfo(empid);
		logger.info("userInfo = " + userInfo);
		return userInfo;
	}

	@Override
	public List<EmployeeMasterEntity> filterByEmpIdHome(String empid, String effective_date) {
		logger.info("empid = " + empid);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate edate = LocalDate.parse(effective_date, formatter);

		List<EmployeeMasterEntity> userInfo = new ArrayList<EmployeeMasterEntity>();
		userInfo = personalInfoRepository.getUserInfoHome(empid, edate);
		System.out.println("userInfo = " + userInfo.size());

		if (userInfo.size() == 0) {
			logger.info("Insdie IF");
			userInfo = personalInfoRepository.getUserInfoHome1(empid);
		}
		logger.info("userInfo = " + userInfo);
		return userInfo;
	}

	@Override
	public List<CalendarTypeEntity> getPayrollCalendarList() {

		List<CalendarTypeEntity> getPayrollCalcList = jdbcTemplate.query(GET_PAYROLL_CALENDAR_LIST,
				new RowMapper<CalendarTypeEntity>() {
					@Override
					public CalendarTypeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CalendarTypeEntity kpiModel = new CalendarTypeEntity();
						kpiModel.setCalendarType(rs.getString("calendar_type"));
						return kpiModel;
					}
				});
		return getPayrollCalcList;

	}

	@Override
	public int getMultiProfileCount(String empid) {
		logger.info("empid = " + empid);

		int cnt = personalInfoRepository.getMultiProfileCount(empid);
		logger.info("cnt = " + cnt);
		return cnt;
	}

	@Override
	public List<EmployeeMasterEntity> filterByEmpIdByRole(String empid, String role) {
		logger.info("empid = " + empid);
		logger.info("role = " + role);
//		List<EmployeeMasterEntity> userInfo = new ArrayList<EmployeeMasterEntity>();
//		userInfo = personalInfoRepository.findByEmployeeId(empid);
//		System.out.println("userInfo = " + userInfo);
//		return userInfo;

		List<EmployeeMasterEntity> userList = null;

		userList = jdbcTemplate.query(FILTER_BY_EMPID_ROLE_IMPERSONATE, new RowMapper<EmployeeMasterEntity>() {
			@Override
			public EmployeeMasterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				EmployeeMasterEntity kpiModel = new EmployeeMasterEntity();
				kpiModel.setSupervisor_name(rs.getString("supervisor_name"));
				kpiModel.setSales_rep_id(rs.getString("sales_rep_id"));
				return kpiModel;
			}
		}, new Object[] { empid, role });

		return userList;

	}

	@Override
	public FavPageEntity addPageToFav(FavPageEntity favPageModel) {
		// TODO Auto-generated method stub
		favPageRepository.save(favPageModel);
		return favPageModel;
	}

	@Override
	public int deleteFavPage(Long id, String empId) {
		// TODO Auto-generated method stub
		int result = 0;
		try {
			String deleteQuery = "delete from c_user_fav_pages where page_id =? and employee_id =? ";
			result = jdbcTemplate.update(deleteQuery, id, empId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	@Override
	public EmployeeMasterEntity deleteUser(List<EmployeeMasterEntity> userModels) {

		for (EmployeeMasterEntity userModel : userModels) {
			logger.info(String.valueOf(userModel.getRowid()));
			String[] complnId = {};

			if (userModel.getCommPlanId() != null) {
				complnId = userModel.getCommPlanId().split(",");
			} else {
				complnId = userModel.getComm_plan_id().toString().split(",");
			}

			for (int i = 0; i < complnId.length; i++) {

				String updateQuery = "UPDATE c_employee_master set profile_status=?,updated_by=?,updated_dt=? where id = ? ";
				jdbcTemplate.update(updateQuery, "inactive", userModel.getUpdated_by(), new Date(),
						userModel.getRowid());

			}
		}
		return userModels.get(0);
	}

}
