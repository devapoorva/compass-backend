package com.altice.salescommission.queries;

public interface UploadConfigQueries {
	public final String GET_UPLOAD_TEMPLATES_LIST = "select distinct on (uploadtemplatename) uploadtemplatename,recordtemplate, templateid,rowid ,"
			+ "actiontype,uploadtemplatedesc,transformationroutine,sqlquery from c_upload_config order by uploadtemplatename";

	public final String GET_TEMPLATES_LIST = "select distinct on (recordname) recordname,recorddesc,recordtemplate from c_upload_template";

	public final String GET_COLUMNS_LIST = "select distinct (columnname) columnname,columntype,status,columnorder "
			+ "from c_upload_template where recordname=? order by status desc,columnorder";
	
	public final String GET_COLUMNS_CONFIG_LIST = "select distinct (columnname) columnname,columntype,status,columnorder "
			+ "from c_upload_template where recordtemplate=? and recordname =? order by status desc,columnorder";

	public final String GET_RECORDS_LIST = "select distinct (templateheader) templateheader,columnorder from c_upload_template cut where recordtemplate =? and status ='Y' order by columnorder";

	public final String GET_COLUMNS_EDIT_LIST = "select rowid,uploadtemplatename ,uploadtemplatedesc ,recordtemplate ,actiontype ,transformationroutine ,"
			+ "	columnname ,columntype  ,status,sqlquery,columnorder from c_upload_config cut where recordtemplate = ? order by status desc,columnorder";

}
