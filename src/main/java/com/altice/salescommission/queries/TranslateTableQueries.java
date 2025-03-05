package com.altice.salescommission.queries;

public interface TranslateTableQueries {
	public final String GET_NAMES_LIST = "select DISTINCT on (field_name) field_name from c_translate_master order by field_name";
	public final String GET_NAMES_LIST_EXCEL = "select field_name ,field_value ,description ,field_short_name,"
			+ "effective_date,effective_status from c_translate_master order by field_name";
	public final String GET_SHORT_NAMES_LIST = "select translate_id ,field_value ,description ,field_short_name,"
			+ "effective_date,to_char(effective_date,'MM-DD-YYYY') edate,"
			+ "effective_status from c_translate_master where field_name=? order by field_value";
	public final String GET_FIELD_NAMES_LIST = "select field_name from c_translate_master group by field_name order by field_name";
}
