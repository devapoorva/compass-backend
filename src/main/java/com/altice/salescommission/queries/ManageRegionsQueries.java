package com.altice.salescommission.queries;

public interface ManageRegionsQueries {

	public final String GET_MAJOR_AREAS_LIST = "select major_area from c_corp_master group by major_area order by major_area";
	
	public final String GET_ACTIVE_MAJOR_AREAS_LIST = "select distinct major_area  from c_corp_master ccm where status ='Active' order by major_area";
	
	public final String GET_ACTIVE_CORPS_LIST = "select corp_id  from c_corp_master ccm where status ='Active'  order by corp_id";
	
	public final String GET_REGIONS_LIST = "select region from c_corp_master where major_area=? group by region order by region";
	
	public final String GET_AREAS_LIST = "select distinct on (area) area,status from c_corp_master where major_area=? and region=? order by area";
	
	public final String GET_CORPS_LIST = "select corp_id,corp,status from c_corp_master "
			+ "where major_area =? and region =? and area=? group by corp_id,corp order by corp";
	
	public final String GET_ALL_MANAGE_REGIONS_LIST = "select major_area,region ,area,corp_id,corp,status from c_corp_master order by major_area";

	public final String GET_ALL_AREAS_LIST = "select area from c_corp_master group by area order by area";
	
	public final String GET_ALL_REGIONS_LIST = "select region from c_corp_master group by region order by region";

	public final String UPDATE_MAJOR_AREA = "update c_corp_master set major_area=?,updated_dt=?,updated_by=? where major_area=?";
	
	public final String UPDATE_REGION = "update c_corp_master set region=?,updated_dt=?,updated_by=? where region=? and major_area=?";
	
	public final String UPDATE_AREA = "update c_corp_master set area=?,updated_dt=?,updated_by=? where area=? and major_area=? and region=?";
	
	public final String UPDATE_CORP_WITH_NAME = "update c_corp_master set corp=?,area=?,status=?,updated_dt=?,updated_by=? where corp=?";
	
	public final String UPDATE_CORP_WITHOUT_NAME = "update c_corp_master set area=?,status=?,updated_dt=?,updated_by=? where corp=?";

	public final String GET_MAREAS_LIST = "select major_area "
			+ "from c_corp_master where lower(corp_id::varchar) like ? or lower(major_area::varchar)  like ?  or lower(region::varchar)  like ? or lower(area::varchar)  like ? group by major_area order by major_area ";
}
