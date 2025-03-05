package com.altice.salescommission.queries;


public interface ExcelHeadersQueries {
	public final String GET_KPI_HEADERS = "select rowid,pagename,name,datakey ,status ,excelstatus ,headerstatus ,position as pos ,issortable ,isview ,edit "
			+ "from c_json_excel_headers cjeh where pagename in ('KPI','KPIMasterProductData','KPIMasterProductCatData','KPIMasterProductTypeData','KPIMasterProductTypeByKPI','KPIMasterProductTypeByCombo','KPIMasterProductCatByCombo','ExcelUsersData','ExcelUsersReportData') "
			+ "order by vieworder";
}
