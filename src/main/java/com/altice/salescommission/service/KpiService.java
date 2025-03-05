package com.altice.salescommission.service;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.altice.salescommission.entity.KpiMasterEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.model.KpiModel;

public interface KpiService {
	
	int createKPI(KpiModel kpiMasterModel) throws DuplicateRecordException;
	int updateKPI(KpiModel kpiMasterModel) throws DuplicateRecordException, ParseException;

//	int insertKPI(String kpi_name, int kpi_priority, String kpi_type, String product, String kpi_calc_type,
//			String currentUser, String custom_routine, Date effective_date, String kpiId, String product_type,
//			String product_category, String comboString, String productName, int stat, String combo_position,
//			String combo_values, String wstat_string, String include_count, String include_revenue, String feed_type)
//			throws DuplicateRecordException;

//	int updateKpi(long id, String kpi_name, int kpi_priority, String kpi_type, String kpi_status, String currentUser,
//			String product, String kpi_calc_type, String custom_routine, Date effective_date, String product_type,
//			String product_category, String depndentKpis, String comboString, String productName, String productid,
//			String combo_position, String combo_values, String wstat_string, String include_count,
//			String include_revenue,String feed_type) throws IOException, IdNotFoundException, DuplicateRecordException, ParseException;

	List<KpiModel> getKpiTypeOneList();

	List<KpiModel> getComboStringsList();

	List<KpiModel> getKpiActiveList();

	List<KpiModel> getActiveKpis();

	List<KpiModel> getkpiactivelistByDate(String effective_date);

	List<KpiModel> getPositionsValues(Long kpiid, Date effective_date);

	List<KpiModel> getDependKpis(Long kpiid, Date effective_date);

	List<KpiModel> getSelectedProducts(Long kpiid, Date effective_date);

	List<KpiModel> getExcelKpiActiveList();

	List<KpiModel> getDependKpisDropdown();

	List<KpiModel> getKPICalcTypes();

	KpiMasterEntity updateKpiStatusToActive(List<KpiMasterEntity> kpiMasterModel);

	KpiMasterEntity updateKpiStatusToInactive(List<KpiMasterEntity> kpiMasterModel);

	int deleteProduct(long id) throws IOException;

	List<KpiModel> getProductsFromProductMaster();

	List<KpiModel> getProductTypes(int kpiid, LocalDate effective_date);

	List<KpiModel> getCategories(int kpiid, String proType, LocalDate effective_date);

	List<KpiModel> getProductsFromKpiProductDetail(int kpiid, String proType, String cat, LocalDate effective_date);

}
