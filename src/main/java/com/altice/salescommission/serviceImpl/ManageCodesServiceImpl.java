package com.altice.salescommission.serviceImpl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.EmployeeMasterEntity;
import com.altice.salescommission.entity.KpiRcodeRltnEntity;
import com.altice.salescommission.entity.ProductDetailEntity;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.model.ManageCodesModel;
import com.altice.salescommission.queries.CommissionQueries;
import com.altice.salescommission.repository.ActiveRateCodeRepository;
import com.altice.salescommission.repository.KpiRtcodeRepository;
import com.altice.salescommission.repository.ManageCodesRepository;
import com.altice.salescommission.repository.ProductDetailRepository;
import com.altice.salescommission.service.ManageCodesService;

@Service
@Transactional
public class ManageCodesServiceImpl implements ManageCodesService, CommissionQueries {
	
	private static final Logger logger = LoggerFactory.getLogger(ManageCodesServiceImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ManageCodesRepository manageCodesRepository;
	
	@Autowired
	private ActiveRateCodeRepository rateCodeRepository;
	
	@Autowired
	private ProductDetailRepository productDetailRepository;
	
	@Autowired
	private KpiRtcodeRepository kpiRtcodeRepository;

	
	@Override
	public List<ManageCodesModel> getProducts() {
		try {
			List<ManageCodesModel> manageCodesList = jdbcTemplate.query(GET_PRODUCTS,
					new RowMapper<ManageCodesModel>() {

						@Override
						public ManageCodesModel mapRow(ResultSet rs, int rowNum) throws SQLException {
							ManageCodesModel manageCodesModel = new ManageCodesModel();
							manageCodesModel.setProductid(rs.getLong("kpi_id"));
							manageCodesModel.setProductname(rs.getString("kpi_name"));
							return manageCodesModel;
						}
					});
			return manageCodesList;
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}
	
	@Override
	public List<ManageCodesModel> getCorps() {
		try {
			List<ManageCodesModel> manageCodesList = jdbcTemplate.query(GET_CORPS,
					new RowMapper<ManageCodesModel>() {

						@Override
						public ManageCodesModel mapRow(ResultSet rs, int rowNum) throws SQLException {
							ManageCodesModel manageCodesModel = new ManageCodesModel();
							manageCodesModel.setCorp(rs.getLong("corp_id"));
							return manageCodesModel;
						}
					});
			return manageCodesList;
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}
	
	@Override
	public List<ManageCodesModel> getAssignedCorps(String id,String edate) {
		try {
			List<ManageCodesModel> manageCodesList = jdbcTemplate.query(GET_ASSIGNED_CORPS,
					new RowMapper<ManageCodesModel>() {

						@Override
						public ManageCodesModel mapRow(ResultSet rs, int rowNum) throws SQLException {
							ManageCodesModel manageCodesModel = new ManageCodesModel();
							manageCodesModel.setAssc_corps(rs.getString("asscCorps"));
							return manageCodesModel;
						}
			}, new Object[] {id,edate });
			return manageCodesList;
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}
	
	@Override
	public List<EmployeeMasterEntity> getSupervisors() {
		try {
			List<EmployeeMasterEntity> manageCodesList = jdbcTemplate.query(GET_SUPERVISORS,
					new RowMapper<EmployeeMasterEntity>() {

						@Override
						public EmployeeMasterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
							EmployeeMasterEntity userModel = new EmployeeMasterEntity();
							userModel.setSupervisor_id(rs.getString("supervisor_id"));
							userModel.setSupervisor_name(rs.getString("supervisor_name"));
							return userModel;
						}
					});
			return manageCodesList;
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}
	
	@Override
	public List<EmployeeMasterEntity> getEmpSupervisors(String val) {
		logger.info("val = "+val);
		try {
			List<EmployeeMasterEntity> manageCodesList = jdbcTemplate.query(GET_EMP_SUPERVISORS,
					new RowMapper<EmployeeMasterEntity>() {

						@Override
						public EmployeeMasterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
							EmployeeMasterEntity userModel = new EmployeeMasterEntity();
							userModel.setSupervisor_id(rs.getString("supervisor_id"));
							userModel.setSupervisor_name(rs.getString("supervisor_name"));
							return userModel;
						}
			}, new Object[] {val+'%',val+'%',val+'%' });
			return manageCodesList;
		} catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}
	
	
	@Override
	public List<ManageCodesModel> getRateCodes(Long productid, Long corp) {

		int corpid = 0;

		if (corp==0) {
			corpid = 1;
		}

		List<ManageCodesModel> rcodes = new ArrayList<>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_RATE_CODES,
				new Object[] { productid, corpid, corp });

		for (Map row : rows) {
			ManageCodesModel rateCodes = new ManageCodesModel();
			List l= new ArrayList();
			rateCodes.setProductid(((Integer) row.get("kpi_id")).longValue());
//			rateCodes.setProductname((String) row.get("product"));
			rateCodes.setCorp(((Integer) row.get("corp")).longValue());
			rateCodes.setRcode((String) row.get("rcode"));
			//rateCodes.setRname((String) row.get("rname"));
			rateCodes.setValid_from_dt((Date) row.get("effective_from"));
			rateCodes.setValid_to_dt((Date) row.get("effective_to"));
			rateCodes.setEffective_date((Date) row.get("effective_date"));
			rateCodes.setF_level(((BigDecimal) row.get("f_level")).intValue());
			rateCodes.setStatus((String) row.get("active_flag"));
		if(row.get("promoamt") != null)
		rateCodes.setProAmount(((Double) row.get("promoamt")).floatValue());
			rateCodes.setCorps(l);
			rateCodes.setChangeColor(l);
			rcodes.add(rateCodes);
		}
		return rcodes;

	}

	@SuppressWarnings("deprecation")
	@Override
	public ManageCodesModel updateRateCodes(ManageCodesModel manageCodesModel) {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_PRODUCT,
				new Object[] { manageCodesModel.getProductid(),manageCodesModel.getRcode() });
		
		for (Map row : rows) {
			long ipInt = ((Number)row.get("krr_id")).longValue();
			KpiRcodeRltnEntity kpiRcodeRltnModel = kpiRtcodeRepository.findById(ipInt ).orElseThrow(
					() -> new ResourceNotFoundException("Commission plan id not found" +ipInt));
			 
			int corp = (int) row.get("corp");
			
			if(manageCodesModel.getCorps().contains(corp ) && manageCodesModel.getEffective_date().equals(row.get("effective_date")) ) {
				
				kpiRcodeRltnModel.setEffective_from(manageCodesModel.getValid_from_dt());
				kpiRcodeRltnModel.setEffective_to(manageCodesModel.getValid_to_dt());
				kpiRcodeRltnModel.setEffective_date(manageCodesModel.getEffective_date());
				manageCodesModel.getCorps().remove(new Integer(corp));
				kpiRcodeRltnModel.setActive_flag("Y");
				if(manageCodesModel.getProAmount() != null) {
					kpiRcodeRltnModel.setPromoamt(manageCodesModel.getProAmount());
				}
			}
			else if(manageCodesModel.getEffective_date().equals(row.get("effective_date"))) {
				kpiRcodeRltnModel.setActive_flag("N");
//				kpiRcodeRltnModel.setEffective_from(manageCodesModel.getValid_from_dt());
//				kpiRcodeRltnModel.setEffective_to(manageCodesModel.getValid_to_dt());
//				kpiRcodeRltnModel.setEffective_date(manageCodesModel.getEffective_date());
			}
			//kpiRcodeRltnModel.setActive_flag(manageCodesModel.getStatus());
			kpiRtcodeRepository.save(kpiRcodeRltnModel);
		}
		if(manageCodesModel.getCorps().size()>0) {
			manageCodesModel.setCreatedBy(manageCodesModel.getUpdatedBy());
			 addRateCode(manageCodesModel);
		}
		return manageCodesModel;
	}

	@Override
	public ManageCodesModel addRateCode(ManageCodesModel manageCodesModel) {
	
		for(int corpid:manageCodesModel.getCorps()) {
			KpiRcodeRltnEntity kpiRtcodeMdl = new KpiRcodeRltnEntity();
			kpiRtcodeMdl.setKpi_id(manageCodesModel.getProductid());
			kpiRtcodeMdl.setRcode(manageCodesModel.getRcode());
			kpiRtcodeMdl.setF_level(manageCodesModel.getF_level());
			kpiRtcodeMdl.setActive_flag("Y");
			kpiRtcodeMdl.setEffective_from(manageCodesModel.getValid_from_dt());
			kpiRtcodeMdl.setEffective_to(manageCodesModel.getValid_to_dt());
			kpiRtcodeMdl.setEffective_date(manageCodesModel.getEffective_date());
			kpiRtcodeMdl.setCreated_by(manageCodesModel.getCreatedBy());
			kpiRtcodeMdl.setCreated_dt(new Date());
			kpiRtcodeMdl.setCorp(corpid);
			if(manageCodesModel.getProAmount() != null) {
				kpiRtcodeMdl.setPromoamt(manageCodesModel.getProAmount());
			}
			
			kpiRtcodeRepository.save(kpiRtcodeMdl);
		}
			/*
			 * rateCodeModel.setCorp(corpid);
			 * rateCodeModel.setF_level(manageCodesModel.getF_level());
			 * rateCodeModel.setRcode(manageCodesModel.getRcode());
			 * rateCodeModel.setRname(manageCodesModel.getRname());
			 * rateCodeModel.setEffdate(manageCodesModel.getEffdate());
			 * rateCodeModel.setValid_from_dt(manageCodesModel.getValid_from_dt());
			 * rateCodeModel.setValid_to_dt(manageCodesModel.getValid_to_dt());
			 * rateCodeModel.setError_flag("U");
			 * rateCodeModel.setRdesc(manageCodesModel.getRname());
			 * rateCodeRepository.save(rateCodeModel);
			 */
			
			
		return manageCodesModel;
	}
	
	@Override
	public List<ManageCodesModel> getProductsBySalesChannel(String saleschannel) {
		try {
        List<ManageCodesModel> manageCodesList = new ArrayList<>();
        
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_PRODUCTS_DETAILS,
                new Object[] { saleschannel});
        Set<String> productTypeSet = new HashSet<>();
        Set<String> productCatSet = new HashSet<>();
        Set<Integer> productIdSet = new HashSet<>();
        
        for (Map row : rows) {
        	ManageCodesModel manageCodesModel = new ManageCodesModel();
            manageCodesModel.setProductid(((Integer) row.get("kpi_id")).longValue());
            manageCodesModel.setProductname((String) row.get("kpi_name"));
            manageCodesList.add(manageCodesModel);
        	
//        	if(row.get("product").equals("All")&& row.get("product_category") .equals("All") && 
//        	row.get("product_type").equals("All"))
//        	{
//        		return getProducts();
//        	}
//        	
//        	else if(row.get("product").equals("All") && row.get("product_category") .equals("All") && 
//                	( !(row.get("product_type")) .equals("All"))) {
//        		
//        		
//        		if(!productTypeSet.contains( row.get("product_type"))){
//        			List<Map<String, Object>> pCategorys = jdbcTemplate.queryForList(GET_PRODUCTS_BY_PTYPE,
//        	                new Object[] {  row.get("product_type")});
//        	        for (Map result : pCategorys) {
//        	        	 ManageCodesModel manageCodesModel = new ManageCodesModel();
//        	             manageCodesModel.setProductid(((Integer) result.get("product_id")).longValue());
//        	             manageCodesModel.setProductname((String) result.get("product"));
//        	             manageCodesList.add(manageCodesModel);
//        	        }
//        		}
//        		
//        		productTypeSet.add(((String) row.get("product_type")));
//        		
//        	}
//        	
//        	else if((row.get("product")).equals("All") && ( !(row.get("product_category")) .equals("All")) && 
//                	( !(row.get("product_type")) .equals("All"))) {
//        		
//        		
//        		if(!productCatSet.contains( row.get("product_category"))){
//        			List<Map<String, Object>> pCategorys = jdbcTemplate.queryForList(GET_PRODUCTS_BY_PCAT,
//        	                new Object[] { row.get("product_category")});
//        	        for (Map result : pCategorys) {
//        	        	 ManageCodesModel manageCodesModel = new ManageCodesModel();
//        	             manageCodesModel.setProductid(((Integer) result.get("product_id")).longValue());
//        	             manageCodesModel.setProductname((String) result.get("product"));
//        	             manageCodesList.add(manageCodesModel);
//        	        }
//        		}
//        		
//        		productCatSet.add(((String) row.get("product_category")));
//        		
//        	}
//        	else {
//        		if(((Integer) row.get("product_id")) > 0) {
//        		
//        		if(!productIdSet.contains( row.get("product_id"))){
//        			List<Map<String, Object>> pCategorys = jdbcTemplate.queryForList(GET_PRODUCTS_BY_PID,
//        	                new Object[] { ((Integer) row.get("product_id"))});
//        	        for (Map result : pCategorys) {
//        	        	 ManageCodesModel manageCodesModel = new ManageCodesModel();
//        	             manageCodesModel.setProductid(((Integer) result.get("product_id")).longValue());
//        	             manageCodesModel.setProductname((String) result.get("product"));
//        	             manageCodesList.add(manageCodesModel);
//        	        }
//        		}
//        		
//        		productIdSet.add(((Integer) row.get("product_id")));
//        		}
//        	}
//           
       }
        return manageCodesList;
		}
		catch (Exception ex) {
			String exception_msg = this.getClass().getSimpleName() + ": "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
			return null;
		}
	}

	@Override
	public List<String> getCommPlans(Long productid) {
		try {
	        List<String> commPlans = new ArrayList<>();
	        
	        List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_COMMPLANS,
	                new Object[] { productid});
	        
	        for (Map row : rows) {
	        	String commPlan = ((String) row.get("comm_plan"));
	            commPlans.add(commPlan);
	        }
	        return commPlans;
			}
			catch (Exception ex) {
				String exception_msg = this.getClass().getSimpleName() + ": "
						+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": Exception: " + ex;
				return null;
			}
	}

}
