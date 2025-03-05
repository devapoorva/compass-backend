package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.CalendarEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.model.KpiModel;
import com.altice.salescommission.model.PayrollReportModel;
import com.altice.salescommission.queries.CalendarQueries;
import com.altice.salescommission.repository.CalendarRepository;
import com.altice.salescommission.service.CalendarService;

@Service
@Transactional
public class CalendarServiceImpl extends AbstractBaseRepositoryImpl<CalendarEntity, Long>
		implements CalendarService, CalendarQueries {

	private static final Logger logger = LoggerFactory.getLogger(CalendarServiceImpl.class.getName());

	@Autowired
	private CalendarRepository calendarRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CalendarServiceImpl(CalendarRepository calendarRepository) {

		super(calendarRepository);
	}

	@Override
	public List<CalendarEntity> getCalList(String calendar_type) {
		List<CalendarEntity> calList = null;
		try {
			calList = jdbcTemplate.query(GET_CALENDARS_LIST, new RowMapper<CalendarEntity>() {

				@Override
				public CalendarEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					CalendarEntity calendarModel = new CalendarEntity();

					boolean editMode = false;
					if (rs.getString("unlock").equals("Y")) {
						editMode = true;
					} else {
						editMode = false;
					}

					calendarModel.setId(rs.getLong("comm_cal_id"));
					calendarModel.setValid_from_dt(rs.getDate("valid_from_dt"));
					calendarModel.setValid_to_dt(rs.getDate("valid_to_dt"));
					calendarModel.setPayroll_due_dt(rs.getDate("payroll_due_dt"));
					calendarModel.setPay_dt(rs.getDate("pay_dt"));
					calendarModel.setUnlock(rs.getString("unlock"));
					calendarModel.setOff_cycle(rs.getString("off_cycle"));
					calendarModel.setIssalesrepaccess(rs.getString("issalesrepaccess"));
					calendarModel.setCal_run_id(rs.getInt("cal_run_id"));
					calendarModel.setEditMode(editMode);
					return calendarModel;
				}
			}, new Object[] { calendar_type });
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			logger.error(exception_msg);
			return null;
		}
		return calList;
	}

	/* update calendar */
	@Override
	public CalendarEntity updateCalcs(List<CalendarEntity> calendarModel) throws IOException {
		CalendarEntity updateCalcs = null;
		try {
			for (CalendarEntity myCalcs : calendarModel) {
				logger.info("getIssalesrepaccess = " + myCalcs.getIssalesrepaccess());

				CalendarEntity myDoc = calendarRepository.findById(myCalcs.getId())
						.orElseThrow(() -> new ResourceNotFoundException("Id " + myCalcs.getId() + " not found"));
				myDoc.setValid_from_dt(myCalcs.getValid_from_dt());
				myDoc.setValid_to_dt(myCalcs.getValid_to_dt());
				myDoc.setPayroll_due_dt(myCalcs.getPayroll_due_dt());
				myDoc.setPay_dt(myCalcs.getPay_dt());
				myDoc.setUnlock(myCalcs.getUnlock());
				myDoc.setOff_cycle(myCalcs.getOff_cycle());
				myDoc.setIssalesrepaccess(myCalcs.getIssalesrepaccess());
				myDoc.setCal_run_id(myCalcs.getCal_run_id());
				myDoc.setUpdated_by(myCalcs.getUpdated_by());
				myDoc.setUpdated_dt(new Date());

				updateCalcs = calendarRepository.save(myDoc);

			}
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			logger.error(exception_msg);
			return null;
		}
		return updateCalcs;
	}

	/* This method is used to add payroll calendar */
	@Override
	public int addPayrollCalendar(Date valid_from_dt, Date valid_to_dt, Date payroll_due_dt, Date pay_dt, String unlock,
			String off_cycle, String isSalesRepAccess, int cal_run_id, String calendar_type, String current_user)
			throws DuplicateRecordException {
		int status = 0;
		CalendarEntity saveNewKpiProDetail = null;

		int count = calendarRepository.getCalcRunIDCount(cal_run_id);
		logger.info("count = " + count);
		// if (count > 0) {
		// throw new DuplicateRecordException("Calc Run ID: "+cal_run_id +
		// DUPLICATE_RECORD);
		// status = 2;
		// } else {

		try {
			CalendarEntity CalModel = new CalendarEntity();
			CalModel.setValid_from_dt(valid_from_dt);
			CalModel.setValid_to_dt(valid_to_dt);
			CalModel.setPayroll_due_dt(payroll_due_dt);
			CalModel.setPay_dt(pay_dt);
			CalModel.setUnlock(unlock);
			CalModel.setOff_cycle(off_cycle);
			CalModel.setIssalesrepaccess(isSalesRepAccess);
			CalModel.setCal_run_id(cal_run_id);
			CalModel.setCalendar_type(calendar_type);
			CalModel.setCreated_by(current_user);
			CalModel.setCreated_dt(new Date());
			CalModel.setActive_flag("Y");
			saveNewKpiProDetail = calendarRepository.save(CalModel);
			status = 1;
		} catch (Exception ex) {
			// ex.getStackTrace()[0].getLineNumber();
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			logger.error("Line No: " + ex.getStackTrace()[0].getLineNumber() + ": " + exception_msg);
			return status;
		}

		// }
		logger.info("status = " + status);
		return status;
	}

	@Override
	public List<CalendarEntity> getCalendarTypes() {
		List<CalendarEntity> calTypes = null;
		try {
			calTypes = jdbcTemplate.query(GET_ALL_CALENDAR_TYPES, new RowMapper<CalendarEntity>() {

				@Override
				public CalendarEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					CalendarEntity calendarTypeModel = new CalendarEntity();
					calendarTypeModel.setCalendar_type(rs.getString("calendar_type"));
					return calendarTypeModel;
				}
			});
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			logger.error(exception_msg);
			return null;
		}
		return calTypes;
	}

	@Override
	public List<CalendarEntity> getCommPeriodValues(String calType) {
		List<CalendarEntity> calTypes = null;
		try {
			calTypes = jdbcTemplate.query(GET_CALENDAR_COMM_PERIODS, new RowMapper<CalendarEntity>() {

				@Override
				public CalendarEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					CalendarEntity calendarModel = new CalendarEntity();
					calendarModel.setCalendar_type(rs.getString("calendar_type"));
					calendarModel.setId(rs.getLong("comm_cal_id"));
					calendarModel.setValid_from_dt(rs.getDate("valid_from_dt"));
					calendarModel.setValid_to_dt(rs.getDate("valid_to_dt"));
					calendarModel.setPayroll_due_dt(rs.getDate("payroll_due_dt"));
					calendarModel.setPay_dt(rs.getDate("pay_dt"));
					calendarModel.setUnlock(rs.getString("unlock"));
					calendarModel.setOff_cycle(rs.getString("off_cycle"));
					calendarModel.setCal_run_id(rs.getInt("cal_run_id"));
					calendarModel.setCommPeriod(rs.getString("commPeriod"));
					return calendarModel;
				}
			}, new Object[] { calType });
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			logger.error(exception_msg);
			return null;
		}
		return calTypes;
	}

	@Override
	public List<CalendarEntity> getCommPeriodValUnlock(String calType) {
		List<CalendarEntity> calTypes = null;
		try {
			calTypes = jdbcTemplate.query(GET_CALENDAR_COMM_UNLOCK_PERIODS, new RowMapper<CalendarEntity>() {

				@Override
				public CalendarEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					CalendarEntity calendarModel = new CalendarEntity();
					calendarModel.setCalendar_type(rs.getString("calendar_type"));
					calendarModel.setId(rs.getLong("comm_cal_id"));
					calendarModel.setValid_from_dt(rs.getDate("valid_from_dt"));
					calendarModel.setValid_to_dt(rs.getDate("valid_to_dt"));
					calendarModel.setPayroll_due_dt(rs.getDate("payroll_due_dt"));
					calendarModel.setPay_dt(rs.getDate("pay_dt"));
					calendarModel.setUnlock(rs.getString("unlock"));
					calendarModel.setOff_cycle(rs.getString("off_cycle"));
					calendarModel.setCal_run_id(rs.getInt("cal_run_id"));
					calendarModel.setCommPeriod(rs.getString("commPeriod"));
					return calendarModel;
				}
			}, new Object[] { calType });
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			logger.error(exception_msg);
			return null;
		}
		return calTypes;
	}

	@Override
	public List<PayrollReportModel> getPayrollReportByCalrun(String calRunId) {
		List<PayrollReportModel> payrollReportList = null;
		try {
			payrollReportList = jdbcTemplate.query(GET_PAYROLL_REPORT_DATA, new RowMapper<PayrollReportModel>() {

				@Override
				public PayrollReportModel mapRow(ResultSet rs, int rowNum) throws SQLException {
					PayrollReportModel payrollModel = new PayrollReportModel();
					payrollModel.setName(rs.getString("empname"));
					payrollModel.setScEmpId(rs.getString("sc_emp_id"));
					payrollModel.setPeopleSoftId(rs.getString("employee_id"));
					payrollModel.setCommAmt(rs.getString("commission_val"));
					payrollModel.setIncentive(rs.getString("incentive_amt"));
					payrollModel.setCommPlan(rs.getString("comm_plan"));
					payrollModel.setSalesRepId(rs.getString("sales_rep_id"));
					payrollModel.setSalesChannel(rs.getString("sales_channel_desc"));
					payrollModel.setTotalAmt(rs.getString("total"));
					payrollModel.setAdjComments(rs.getString("adjustment_comments"));

					return payrollModel;
				}
			}, new Object[] { calRunId });
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			logger.error(exception_msg);
			return null;
		}
		return payrollReportList;
	}

	@Override
	public List<CalendarEntity> getCalendarData() {
		List<CalendarEntity> calTypes = null;
		try {
			calTypes = jdbcTemplate.query(GET_CALENDAR_DATA, new RowMapper<CalendarEntity>() {

				@Override
				public CalendarEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					CalendarEntity calendarModel = new CalendarEntity();
					calendarModel.setValid_from_dt(rs.getDate("valid_from_dt"));
					calendarModel.setValid_to_dt(rs.getDate("valid_to_dt"));
					return calendarModel;
				}
			});
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			logger.error(exception_msg);
			return null;
		}
		return calTypes;
	}

	@Override
	public List<CalendarEntity> getUserRoles() {
		List<CalendarEntity> userRolesList = null;

		userRolesList = jdbcTemplate.query(GET_USER_ROLES_DATA, new RowMapper<CalendarEntity>() {

			@Override
			public CalendarEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalendarEntity calendarModel = new CalendarEntity();
				calendarModel.setUserRole(rs.getString("role_name"));
				return calendarModel;
			}
		});

		return userRolesList;
	}

	@Override
	public List<CalendarEntity> getCalRunIdsDropdown() {
		List<CalendarEntity> manageCodesList = null;
		try {
			manageCodesList = jdbcTemplate.query(GET_CAL_RUN_IDS, new RowMapper<CalendarEntity>() {

				@Override
				public CalendarEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					CalendarEntity kpiModel = new CalendarEntity();
					kpiModel.setCal_run_id(rs.getInt("cal_run_id"));
					return kpiModel;
				}
			});
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			logger.error(exception_msg);
			return null;
		}
		return manageCodesList;
	}

}
