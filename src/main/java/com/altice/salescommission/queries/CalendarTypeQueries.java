package com.altice.salescommission.queries;

public interface CalendarTypeQueries {
	public final String GET_CALENDAR_TYPES = "select ctype_rowid,calendar_type ,active_flag,to_char(created_dt,'YYYY-MM-DD') created_dt,"
			+ "updated_dt from c_comm_calendar_type ccct";
	
}
