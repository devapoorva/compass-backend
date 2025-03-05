package com.altice.salescommission.queries;

public interface BossCallMappingQueries {
	public final String GET_CALL_MAP_DATA = "select * from boss_call_mapping bcm where valid_from_dt <=? and valid_to_dt >=?";
}
