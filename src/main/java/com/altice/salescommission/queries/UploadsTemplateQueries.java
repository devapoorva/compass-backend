package com.altice.salescommission.queries;

public interface UploadsTemplateQueries {
	public final String GET_TEMPLATES_LIST = "select distinct on (recordname) recordname, templateid,rowid ,recordname,recordtemplate ,recorddesc ,"
			+ "columnname ,columntype ,templateheader ,columnorder ,status,created_by,"
			+ "created_dt from c_upload_template cut";
	
	public final String GET_COLUMNS_LIST = "select column_name,data_type FROM information_schema.columns where table_name = ?";
	
	public final String GET_RECORDS_LIST = "select templateheader from c_upload_template cut where recordname =? and status ='Y' order by columnorder";
	
	public final String GET_COLUMNS_EDIT_LIST = "select rowid,recordname,recordtemplate ,recorddesc ,"
			+ "columnname ,columntype ,templateheader ,columnorder ,status from c_upload_template cut where recordname = ? order by status desc,columnorder";
}
