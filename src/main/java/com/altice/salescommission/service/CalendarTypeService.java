package com.altice.salescommission.service;

import java.util.List;

import com.altice.salescommission.entity.CalendarTypeEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;

public interface CalendarTypeService extends AbstractBaseService<CalendarTypeEntity, Long> {
	CalendarTypeEntity addCalendarType(String calendar_type, String currentUser) throws DuplicateRecordException;

	CalendarTypeEntity updateCalendarType(long id, String calendar_type, String currentUser, String active_flag)
			throws IdNotFoundException;

	List<CalendarTypeEntity> getCalendarList();

	CalendarTypeEntity updateStatusToActive(List<CalendarTypeEntity> calendarTypeEntity);

	CalendarTypeEntity updateStatusToInactive(List<CalendarTypeEntity> calendarTypeEntity);
}
