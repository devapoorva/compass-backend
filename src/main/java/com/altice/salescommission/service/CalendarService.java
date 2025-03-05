package com.altice.salescommission.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.altice.salescommission.entity.CalendarEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.model.KpiModel;
import com.altice.salescommission.model.PayrollReportModel;

public interface CalendarService extends AbstractBaseService<CalendarEntity, Long> {
	
	int addPayrollCalendar(Date valid_from_dt, Date valid_to_dt, Date payroll_due_dt, Date pay_dt,
			String unlock, String off_cycle,String isSalesRepAccess, int cal_run_id, String calendar_type, String current_user) throws DuplicateRecordException;

	List<CalendarEntity> getCalList(String calendar_type);

	CalendarEntity updateCalcs(List<CalendarEntity> calendarModel) throws IOException;

	List<CalendarEntity> getCalendarTypes();

	List<CalendarEntity> getCommPeriodValues(String calType);

	List<CalendarEntity> getCommPeriodValUnlock(String calType);

	List<PayrollReportModel> getPayrollReportByCalrun(String calRunId);

	List<CalendarEntity> getCalendarData();
	
	List<CalendarEntity> getUserRoles();

	List<CalendarEntity> getCalRunIdsDropdown();

}
