package com.altice.salescommission.repository;

import com.altice.salescommission.entity.CalendarTypeEntity;

public interface CalendarTypeRepository extends AbstractBaseRepository<CalendarTypeEntity, Long> {
	CalendarTypeEntity findByCalendarTypeIgnoreCase(String calType);
}
