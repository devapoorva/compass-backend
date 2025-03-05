package com.altice.salescommission.serviceImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.approvalworkflow.WorkFlowConstants;
import com.altice.salescommission.entity.CommissionSummaryEntity;
import com.altice.salescommission.entity.UploadJsonEntity;
import com.altice.salescommission.entity.WorkflowMessageEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.model.KpiModel;
import com.altice.salescommission.model.UploadJsonHeadersModel;
import com.altice.salescommission.queries.CommissionSummaryQueries;
import com.altice.salescommission.reportframework.ReportFrameworkModel;
import com.altice.salescommission.repository.CommissionSummaryRepository;
import com.altice.salescommission.repository.UploadJsonRepository;
import com.altice.salescommission.repository.WorkFlowMessageRepository;
import com.altice.salescommission.service.CommissionSummaryService;
import com.altice.salescommission.utility.CreateTicket;

@Service
@Transactional
public class CommissionSummaryServiceImpl extends AbstractBaseRepositoryImpl<CommissionSummaryEntity, Long>
		implements CommissionSummaryService, CommissionSummaryQueries {

	private static final Logger logger = LoggerFactory.getLogger(CommissionSummaryServiceImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UploadJsonRepository uploadJsonRepository;

	@Autowired
	private WorkFlowMessageRepository workFlowMessageRepository;

	@Autowired
	private CreateTicket createTicket;

	@PersistenceContext
	private EntityManager entityManager;

	public CommissionSummaryServiceImpl(CommissionSummaryRepository commissionSummaryRepository) {

		super(commissionSummaryRepository);
	}

	@Override
	public List<LinkedHashMap<String, Object>> runReport(String parms, String pageName, String gridName, int commplanid,
			String selectedSalesChannel, String usertype) {

		logger.info("========================================================");
		logger.info("gridName = " + gridName);
		logger.info("parms = " + parms);
		logger.info("pageName = " + pageName);
		logger.info("selectedSalesChannel = " + selectedSalesChannel);
		logger.info("commplanid = " + commplanid);
		logger.info("usertype = " + usertype);

		List<LinkedHashMap<String, Object>> finalResultsetList = new ArrayList<>();
		String sqlquery = "";

		try {

			String queryString = "";
			String sql_query = "";

			if (selectedSalesChannel.equals("Web Assist")) {
				if (gridName.equals("Complex KPI Details") || gridName.equals("Complex KPI Details Sup")) {
					logger.info("Inside IF");
					String parms1 = parms.substring(0, 18) + ",";
					String parms2 = parms.substring(19);
					parms = parms1 + parms1 + parms1 + parms1 + parms1 + parms2;
					// logger.info("Inside IF = " + parms);
					if (usertype.equals("rep")) {
						logger.info("Inside IF1");
						sql_query = uploadJsonRepository.getSql2(pageName, gridName);
					} else {
						logger.info("Inside ELSEIF1");
						sql_query = uploadJsonRepository.getSql3(pageName, gridName);
					}
				} else {
					logger.info("Inside ELSE1");
					sql_query = uploadJsonRepository.getSql1(pageName, gridName);
				}

			} else {
				logger.info("Inside ELSE2");
				sql_query = uploadJsonRepository.getSql1(pageName, gridName);
			}

			// String sql_query = uploadJsonRepository.getSql(pageName,gridName,commplanid);

			// System.out.println("sql_query === " + sql_query);
			sqlquery = sql_query;

			if (sqlquery.contains("union")) {
				parms = parms + "," + parms + "," + parms + "," + parms;
			}
			logger.info("parms = " + parms);
			String p[] = parms.split(",");
			queryString = sqlquery;
			int z = 0;
			String one = sqlquery;
			for (int i = 0; i < sqlquery.length(); i++) {
				// System.out.println("z = " + z);
				one = queryString;
				if ('~' == sqlquery.charAt(i)) {
					String parmValue = "";
					try {
						Integer.valueOf(p[z]);

						if (pageName.equals("FindHouseCorpCustByKpi") || pageName.equals("FindByHouseCorpCust")
								|| pageName.equals("ListUsers") || pageName.equals("ByCustomer") && z == 6) {
							parmValue = "'" + p[z] + "'";
						} else {
							parmValue = p[z];
						}

					} catch (NumberFormatException e) {
						// Not an Integer
						parmValue = "'" + p[z] + "'";
					}
					// logger.info("parmValue = " + parmValue);
					// System.out.println("one = " + one);
					queryString = one.replaceFirst("~", parmValue);
					z++;
				}
			}
			// logger.info("Query = " + queryString);

			jdbcTemplate.query(queryString, new ColumnMapRowMapper() {
				int i = 1;

				@Override
				public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ReportFrameworkModel reportFrameworkModel = new ReportFrameworkModel();

					LinkedHashMap<String, Object> mapObj = new LinkedHashMap<>();

					ResultSetMetaData rmd = rs.getMetaData();
					int columnsCount = rmd.getColumnCount();
					// logger.info("----------------------------"+i);
					mapObj.put("idx", i);

					for (int k = 1; k <= columnsCount; k++) {
						mapObj.put(rmd.getColumnName(k), rs.getString(k));
					}

					mapObj.put("isDisputeSubmitted", "true");

					finalResultsetList.add(mapObj);
					reportFrameworkModel.setFieldsrunreport(finalResultsetList);
					i++;
					return null;
				}

			});

			logger.info("Resultset finalResultsetList = " + finalResultsetList);

		} catch (Exception e) {
			// System.out.println("Exception = " + e);
			LinkedHashMap<String, Object> errorMapObj = new LinkedHashMap<>();
			errorMapObj.put("Error: ", e.getMessage());
			finalResultsetList.add(errorMapObj);
		}

		return finalResultsetList;
	}

	@Override
	public List<UploadJsonHeadersModel> getHeaders(int id, String salesChannel, int supervisorStatus) {

		logger.info("getHeaders comm plan id = " + id);
		logger.info("getHeaders salesChannel = " + salesChannel);
		logger.info("getHeaders supervisorStatus = " + supervisorStatus);

		String Query = "";

		if (salesChannel.equals("Web Assist")) {

			if (supervisorStatus >= 20) {
				logger.info("Inside IF");
				Query = GET_HEADERS111;
			} else {
				logger.info("Inside ELSE");
				Query = GET_HEADERS11;
			}

//			logger.info("Inside IF1");
//			if (userType.equals("rep")) {
//				logger.info("Inside IF2");
//				Query = GET_HEADERS11;
//			} else if (userType.equals("sup")) {
//				logger.info("Inside ELSE IF");
//				Query = GET_HEADERS111;
//			}

		} else {
			logger.info("Inside ELSE2");
			Query = GET_HEADERS22;
		}

		List<UploadJsonHeadersModel> headersList = jdbcTemplate.query(Query, new RowMapper<UploadJsonHeadersModel>() {

			@Override
			public UploadJsonHeadersModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				UploadJsonHeadersModel commissionSummaryModel = new UploadJsonHeadersModel();

				commissionSummaryModel.setId(rs.getInt("rowid"));
				commissionSummaryModel.setJsonsqlid(rs.getInt("jsonsqlid"));
				commissionSummaryModel.setName(rs.getString("name"));
				commissionSummaryModel.setDatakey(rs.getString("datakey"));
				commissionSummaryModel.setPosition(rs.getString("position"));
				commissionSummaryModel.setIssortable(rs.getString("issortable"));
				commissionSummaryModel.setEdit(rs.getString("edit"));
				commissionSummaryModel.setIstotalview(rs.getString("istotalview"));
				commissionSummaryModel.setColspancnt(rs.getString("colspancnt"));
				commissionSummaryModel.setIshyperlinkview(rs.getString("ishyperlinkview"));
				commissionSummaryModel.setIsdisputeview(rs.getString("isdisputeview"));
				commissionSummaryModel.setIsDisputeSubmitted("true");
				commissionSummaryModel.setPagenav(rs.getString("pagenav"));
				commissionSummaryModel.setIstotalcalculate(rs.getString("istotalcalculate"));
				commissionSummaryModel.setColorder(rs.getInt("colorder"));

				return commissionSummaryModel;
			}
			// }, new Object[] { id });
		});
		// System.out.println("headersList = " + headersList);
		return headersList;
	}

	@Override
	public List<UploadJsonEntity> getDataByPageName(String pageName, int commplanid, int supervisorStatus,
			String selectedSalesChannel) {

		List<UploadJsonEntity> queriesList = null;
		String str_query = "";

		logger.info("pageName = " + pageName);
		logger.info("commplanid = " + commplanid);
		logger.info("supervisorStatus = " + supervisorStatus);
		logger.info("selectedSalesChannel = " + selectedSalesChannel);

//		if (pageName.equals("ListUsers")) {
//			userType = "rep";
//		}

		if (pageName.equals("FindByProduct")) {
			str_query = "GET_BY_PAGENAME_TWO";
			queriesList = jdbcTemplate.query(GET_BY_PAGENAME_TWO, new RowMapper<UploadJsonEntity>() {

				@Override
				public UploadJsonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					UploadJsonEntity commissionSummaryModel = new UploadJsonEntity();
					commissionSummaryModel.setId(rs.getLong("id"));
					commissionSummaryModel.setPageName(rs.getString("page_name"));
					commissionSummaryModel.setGridName(rs.getString("grid_name"));
					commissionSummaryModel.setColSort(rs.getInt("order_by"));
					commissionSummaryModel.setSql(rs.getString("sql"));
					commissionSummaryModel.setCommPlanId(rs.getInt("commplanid"));
					commissionSummaryModel.setSalesChannel(rs.getString("saleschannel"));
					commissionSummaryModel.setKpiId(rs.getInt("kpi_id"));
					commissionSummaryModel.setCreated_by(rs.getString("created_by"));
					commissionSummaryModel.setCreated_dt(rs.getDate("created_dt"));
					commissionSummaryModel.setUpdated_by(rs.getString("updated_by"));
					commissionSummaryModel.setUpdated_dt(rs.getDate("updated_dt"));
					commissionSummaryModel.setUser_type(rs.getString("user_type"));
					return commissionSummaryModel;
				}
			}, new Object[] { pageName });
		} else {
			logger.info("Inside ELSE");

			if (selectedSalesChannel.equals("Web Assist")) {
				logger.info("Inside IF1");

				// if (pageName.equals("ListKPIs")) {
				if (supervisorStatus >= 20) {
					logger.info("Inside IF");
					str_query = GET_BY_PAGENAME_ONE_27;
				} else {
					logger.info("Inside ELSE");
					str_query = GET_BY_PAGENAME_ONE_22;
				}
//				} else {
//					str_query = GET_BY_PAGENAME_ONE;
//				}

			} else {
				str_query = GET_BY_PAGENAME_ONE;
			}

			logger.info("str_query = " + str_query);

			queriesList = jdbcTemplate.query(str_query, new RowMapper<UploadJsonEntity>() {

				@Override
				public UploadJsonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					UploadJsonEntity commissionSummaryModel = new UploadJsonEntity();
					commissionSummaryModel.setId(rs.getLong("id"));
					commissionSummaryModel.setPageName(rs.getString("page_name"));
					commissionSummaryModel.setGridName(rs.getString("grid_name"));
					commissionSummaryModel.setColSort(rs.getInt("order_by"));
					commissionSummaryModel.setSql(rs.getString("sql"));
					commissionSummaryModel.setCommPlanId(rs.getInt("commplanid"));
					commissionSummaryModel.setSalesChannel(rs.getString("saleschannel"));
					commissionSummaryModel.setKpiId(rs.getInt("kpi_id"));
					commissionSummaryModel.setCreated_by(rs.getString("created_by"));
					commissionSummaryModel.setCreated_dt(rs.getDate("created_dt"));
					commissionSummaryModel.setUpdated_by(rs.getString("updated_by"));
					commissionSummaryModel.setUpdated_dt(rs.getDate("updated_dt"));
					commissionSummaryModel.setUser_type(rs.getString("user_type"));
					return commissionSummaryModel;
				}
			}, new Object[] { pageName });
		}

		return queriesList;
	}

	@Override
	public List<UploadJsonHeadersModel> getButtons(int id) {

		// System.out.println("getButtons = " + id);

		List<UploadJsonHeadersModel> buttonsList = jdbcTemplate.query(GET_BUTTONS,
				new RowMapper<UploadJsonHeadersModel>() {

					@Override
					public UploadJsonHeadersModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadJsonHeadersModel commissionSummaryModel = new UploadJsonHeadersModel();

						commissionSummaryModel.setId(rs.getInt("rowid"));
						commissionSummaryModel.setJsonsqlid(rs.getInt("jsonsqlid"));
						commissionSummaryModel.setName(rs.getString("name"));
						commissionSummaryModel.setPagenav(rs.getString("pagenav"));
						commissionSummaryModel.setBtn_type(rs.getString("btn_type"));

						return commissionSummaryModel;
					}
					// }, new Object[] { id });
				});

		// System.out.println("buttonsList = " + buttonsList);
		return buttonsList;
	}

	@Override
	public List<LinkedHashMap<String, String>> runReportSearch(String parms, String pageName, String gridName,
			int commplanid) {

		logger.info("===============runReportSearch called=========================================");
		logger.info("gridName = " + gridName);
		logger.info("parms = " + parms);
		logger.info("pageName = " + pageName);
		logger.info("commplanid = " + commplanid);

		List<LinkedHashMap<String, String>> finalResultsetList = new ArrayList<>();
		String sqlquery = "";

		try {

			String queryString = "";

			// String sql_query = uploadJsonRepository.getSql(pageName,gridName,commplanid);
			String sql_query = uploadJsonRepository.getSql(pageName, gridName);
			// System.out.println("sql_query === " + sql_query);
			sqlquery = sql_query;

			if (sqlquery.contains("union")) {
				parms = parms + "," + parms;
			}
			logger.info("parms = " + parms);
			String p[] = parms.split(",");
			queryString = sqlquery;
			int z = 0;
			String one = sqlquery;
			for (int i = 0; i < sqlquery.length(); i++) {
				// System.out.println("z = " + z);
				one = queryString;
				if ('~' == sqlquery.charAt(i)) {
					String parmValue = "";
					try {
						Integer.valueOf(p[z]);

						if (pageName.equals("FindHouseCorpCustByKpi") || pageName.equals("FindByHouseCorpCust")
								|| pageName.equals("ListUsers") || pageName.equals("ByCustomer") && z == 6) {
							parmValue = "'" + p[z] + "'";
						} else {
							parmValue = p[z];
						}

					} catch (NumberFormatException e) {
						// Not an Integer
						parmValue = "'" + p[z] + "'";
					}
					logger.info("parmValue = " + parmValue);
					// System.out.println("one = " + one);
					queryString = one.replaceFirst("~", parmValue);
					z++;
				}
			}

			// logger.info("Query = " + queryString);

			jdbcTemplate.query(queryString, new ColumnMapRowMapper() {
				@Override
				public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ReportFrameworkModel reportFrameworkModel = new ReportFrameworkModel();

					LinkedHashMap<String, String> mapObj = new LinkedHashMap<>();

					ResultSetMetaData rmd = rs.getMetaData();
					int columnsCount = rmd.getColumnCount();

					for (int k = 1; k <= columnsCount; k++) {
						logger.info("Resultset rmd.getColumnName(k) = " + rmd.getColumnName(k));
						mapObj.put(rmd.getColumnName(k), rs.getString(k));
					}

//					mapObj.put("isDisputeSubmitted", "true");
					logger.info("Resultset mapObj = " + mapObj);

					finalResultsetList.add(mapObj);
					reportFrameworkModel.setLinkedfields(finalResultsetList);

					return null;
				}

			});

			// logger.info("Resultset finalResultsetList = " + finalResultsetList);

		} catch (Exception e) {
			// System.out.println("Exception = " + e);
			LinkedHashMap<String, String> errorMapObj = new LinkedHashMap<>();
			errorMapObj.put("Error: ", e.getMessage());
			finalResultsetList.add(errorMapObj);
		}

		return finalResultsetList;
	}

	@Override
	public List<UploadJsonHeadersModel> getCalcRunningStatus(int id, int calrunid) {

		// System.out.println("getCalcRunningStatus = " + id);

		List<UploadJsonHeadersModel> buttonsList = jdbcTemplate.query(GET_CALC_STATUS,
				new RowMapper<UploadJsonHeadersModel>() {

					@Override
					public UploadJsonHeadersModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadJsonHeadersModel commissionSummaryModel = new UploadJsonHeadersModel();

						commissionSummaryModel.setStatus(rs.getString("status"));

						return commissionSummaryModel;
					}
				}, new Object[] { calrunid, id });

		// System.out.println("buttonsList = " + buttonsList);
		return buttonsList;
	}

	@Override
	public List<UploadJsonHeadersModel> getSupervisorStatus(int id) {

		// System.out.println("getSupervisorStatus = " + id);

		List<UploadJsonHeadersModel> supList = jdbcTemplate.query(GET_SUP_STATUS,
				new RowMapper<UploadJsonHeadersModel>() {

					@Override
					public UploadJsonHeadersModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadJsonHeadersModel commissionSummaryModel = new UploadJsonHeadersModel();

						commissionSummaryModel.setStatus(rs.getString("comm_plan_priority"));

						return commissionSummaryModel;
					}
				}, new Object[] { id });

		// System.out.println("supList = " + supList);
		return supList;
	}

	@Override
	public List<UploadJsonHeadersModel> getPageName(int commplanid, int kpiid) {

		logger.info("getPageName commplanid = " + commplanid);
		logger.info("getPageName kpiid = " + kpiid);

		List<UploadJsonHeadersModel> pagesList = jdbcTemplate.query(GET_PAGE_NAME,
				new RowMapper<UploadJsonHeadersModel>() {

					@Override
					public UploadJsonHeadersModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadJsonHeadersModel commissionSummaryModel = new UploadJsonHeadersModel();

//						String pageName = "NA";
//						logger.info("getPageName feed_type = " + rs.getString("feed_type"));
//						if (rs.getString("feed_type").equals("Mobile")) {
//							pageName = "FindByProductMobileLines";
//						}

						commissionSummaryModel.setPageName(rs.getString("feed_type"));
						commissionSummaryModel.setKpi_type(rs.getString("kpi_type"));

//						logger.info("getPageName page_name = " + rs.getString("page_name"));
//						commissionSummaryModel.setPageName(rs.getString("page_name"));

						return commissionSummaryModel;
					}
				}, new Object[] { kpiid });

		// }, new Object[] { commplanid, kpiid });

		// System.out.println("pagesList = " + pagesList);
		return pagesList;
	}

	@Override
	public List<CommissionSummaryEntity> getCommSumDropDowns() {
		List<CommissionSummaryEntity> queriesList = jdbcTemplate.query(GET_SALES_CHANNELS,
				new RowMapper<CommissionSummaryEntity>() {

					@Override
					public CommissionSummaryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CommissionSummaryEntity commissionSummaryModel = new CommissionSummaryEntity();
						commissionSummaryModel.setCommPlanId(rs.getInt("comm_plan_id"));
						commissionSummaryModel.setSalesChannel(rs.getString("sales_channel_desc"));
						commissionSummaryModel.setCommPlan(rs.getString("comm_plan"));
						commissionSummaryModel.setCalendarType(rs.getString("calendar_type"));
						commissionSummaryModel.setValidToDt(rs.getDate("valid_to_dt"));
						commissionSummaryModel.setValidFromDt(rs.getDate("valid_from_dt"));
						commissionSummaryModel.setCalRunId(rs.getString("cal_run_id"));
						commissionSummaryModel.setEffective_date(rs.getDate("effective_date"));
						commissionSummaryModel.setIssalesrepaccess(rs.getString("issalesrepaccess"));
						return commissionSummaryModel;
					}
				});

		return queriesList;
	}

	@Override
	public List<CommissionSummaryEntity> getCommSumSupDropDowns(String empid) {
		logger.info("empid = " + empid);
		List<CommissionSummaryEntity> queriesList = jdbcTemplate.query(GET_SUP_DROPDOWNS,
				new RowMapper<CommissionSummaryEntity>() {

					@Override
					public CommissionSummaryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CommissionSummaryEntity commissionSummaryModel = new CommissionSummaryEntity();
						commissionSummaryModel.setCommPlanId(rs.getInt("comm_plan_id"));
						commissionSummaryModel.setSalesChannel(rs.getString("sales_channel_desc"));
						commissionSummaryModel.setCommPlan(rs.getString("comm_plan"));
						commissionSummaryModel.setCalendarType(rs.getString("calendar_type"));
						commissionSummaryModel.setValidToDt(rs.getDate("valid_to_dt"));
						commissionSummaryModel.setValidFromDt(rs.getDate("valid_from_dt"));
						commissionSummaryModel.setCalRunId(rs.getString("cal_run_id"));
						commissionSummaryModel.setEffective_date(rs.getDate("effective_date"));
						commissionSummaryModel.setIssalesrepaccess(rs.getString("issalesrepaccess"));
						commissionSummaryModel.setDisplay_flag(rs.getString("display_flag"));
						return commissionSummaryModel;
					}
				}, new Object[] { empid });

		return queriesList;
	}

	@Override
	public List<CommissionSummaryEntity> getCommSumUserDropDowns(String empid) {
		logger.info("empid = " + empid);
		;
		List<CommissionSummaryEntity> queriesList = jdbcTemplate.query(GET_USER_DROPDOWNS,
				new RowMapper<CommissionSummaryEntity>() {

					@Override
					public CommissionSummaryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CommissionSummaryEntity commissionSummaryModel = new CommissionSummaryEntity();
						commissionSummaryModel.setCommPlanId(rs.getInt("comm_plan_id"));
						commissionSummaryModel.setSalesChannel(rs.getString("sales_channel_desc"));
						commissionSummaryModel.setCommPlan(rs.getString("comm_plan"));
						commissionSummaryModel.setCalendarType(rs.getString("calendar_type"));
						commissionSummaryModel.setValidToDt(rs.getDate("valid_to_dt"));
						commissionSummaryModel.setValidFromDt(rs.getDate("valid_from_dt"));
						commissionSummaryModel.setCalRunId(rs.getString("cal_run_id"));
						commissionSummaryModel.setEffective_date(rs.getDate("effective_date"));
						commissionSummaryModel.setIssalesrepaccess(rs.getString("issalesrepaccess"));
						commissionSummaryModel.setDisplay_flag(rs.getString("display_flag"));
						return commissionSummaryModel;
					}
				}, new Object[] { empid });

		return queriesList;
	}

	@Override
	public List<CommissionSummaryEntity> getHeaderGroups() {
		List<CommissionSummaryEntity> queriesList = jdbcTemplate.query(GET_HEADER_GROUPS,
				new RowMapper<CommissionSummaryEntity>() {

					@Override
					public CommissionSummaryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CommissionSummaryEntity commissionSummaryModel = new CommissionSummaryEntity();
						commissionSummaryModel.setField_value(rs.getString("field_value"));
						return commissionSummaryModel;
					}
				});

		return queriesList;
	}

	@Override
	public List<CommissionSummaryEntity> getSqlQueries() {

		List<CommissionSummaryEntity> queriesList = jdbcTemplate.query(GET_SQl_QUERIES,
				new RowMapper<CommissionSummaryEntity>() {

					@Override
					public CommissionSummaryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						CommissionSummaryEntity commissionSummaryModel = new CommissionSummaryEntity();
						commissionSummaryModel.setId(rs.getLong("id"));
						commissionSummaryModel.setSqlQuery(rs.getString("sql_query"));
						// runReport(rs.getString("sql_query"), "NA");
						return commissionSummaryModel;
					}
				});

		return queriesList;
	}

	@Override
	public List<WorkflowMessageEntity> getTrackidCount(String trackid) {
		// logger.info(trackid);

		List<WorkflowMessageEntity> trackidsList = jdbcTemplate.query(GET_TRACK_IDS,
				new RowMapper<WorkflowMessageEntity>() {

					@Override
					public WorkflowMessageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkflowMessageEntity commissionSummaryModel = new WorkflowMessageEntity();

						// logger.info("tracking_id = " + rs.getString("tracking_id"));
						String trackId = "";
						String tracking_id = rs.getString("tracking_id");
						Pattern p = Pattern.compile(".*_\\s*(.*)");
						Matcher m = p.matcher(tracking_id);

						if (m.find()) {
							int val = Integer.parseInt(m.group(1));
							int val1 = val + 1;
							trackId = tracking_id.substring(0, tracking_id.indexOf("_"));
						}

						// logger.info("trackId = " + trackId);

						commissionSummaryModel.setTracking_id(trackId);
						commissionSummaryModel.setStatus(rs.getString("final_status"));
						return commissionSummaryModel;
					}
				}, new Object[] { "%" + trackid + "%" });
		// logger.info(trackidsList.toString());
		return trackidsList;
	}

	/* This method is used to get all the active KPI's */
	@Override
	public List<KpiModel> getKpiActiveList(int commplanid, String effdate) {
		// logger.info("getKpiActiveList called: " + commplanid + " - " + effdate);
		List<KpiModel> kpiList = jdbcTemplate.query(GET_KPIS_LIST, new RowMapper<KpiModel>() {

			@Override
			public KpiModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiModel kpiModel = new KpiModel();
				kpiModel.setId(rs.getLong("kpi_id"));
//				kpiModel.setKpi_name(rs.getString("kpi_name"));
				kpiModel.setKpi_name(rs.getString("display_name"));
				return kpiModel;
			}
		}, new Object[] { commplanid, effdate });
		return kpiList;
	}

	@Override
	public WorkflowMessageEntity insertDispute(WorkflowMessageEntity workflowMessageModel)
			throws DuplicateRecordException, IdNotFoundException, ParseException {

		logger.info("workflowMessageModel = " + workflowMessageModel);
//
//		logger.info("workflowMessageModel getEmployee_id = " + workflowMessageModel.getEmployee_id());
//		logger.info("workflowMessageModel getSales_rep_id = " + workflowMessageModel.getSales_rep_id());
//		logger.info("workflowMessageModel getAccnum = " + workflowMessageModel.getAccnum());
//		logger.info("workflowMessageModel getKom_feed_id = " + workflowMessageModel.getKom_feed_id());
//		logger.info("workflowMessageModel getMob_feed_id = " + workflowMessageModel.getMob_feed_id());
//		logger.info("workflowMessageModel getRecordType = " + workflowMessageModel.getRecordType());
//
//		logger.info("workflowMessageModel getComplex_kpiid = " + workflowMessageModel.getComplex_kpiid());
		logger.info("workflowMessageModel getSc_emp_id = " + workflowMessageModel.getSc_emp_id());
//		logger.info("workflowMessageModel getComplanid = " + workflowMessageModel.getComplanid());
//
//		logger.info("workflowMessageModel getCorp = " + workflowMessageModel.getCorp());
//		logger.info("workflowMessageModel getHouse = " + workflowMessageModel.getHouse());
//		logger.info("workflowMessageModel getCust = " + workflowMessageModel.getCust());

		String against_salesrepid = workFlowMessageRepository.getSalesRepID(workflowMessageModel.getEmployee_id(),
				workflowMessageModel.getLoggedin_role());
		logger.info("against_salesrepid = " + against_salesrepid);

		String curDate = new SimpleDateFormat("MM-dd-yyyy").format(new Date());

		String trackId = "";
		String chargebackDeleteId = "";
		if (workflowMessageModel.getSales_rep_id().equals("NA")) {
			// logger.info("Sales rep id inside IF");

			if (workflowMessageModel.getKom_feed_id() == 0 && workflowMessageModel.getMob_feed_id() == 0) {
				// Manual uploaded data: No kom feed id present
				int manual_komfeedid = workFlowMessageRepository.getAdjustmentID(workflowMessageModel.getComplanid(),
						Integer.parseInt(workflowMessageModel.getCorp()), workflowMessageModel.getHouse(),
						workflowMessageModel.getCust(), workflowMessageModel.getSc_emp_id(),
						workflowMessageModel.getComplex_kpiid());
				// workFlowMessageRepository.getSalesRepID(workflowMessageModel.getEmployee_id());

				trackId = String.valueOf(workflowMessageModel.getCorp() + '-' + workflowMessageModel.getHouse() + '-'
						+ workflowMessageModel.getCust());
				chargebackDeleteId = String.valueOf(manual_komfeedid);
			} else if (workflowMessageModel.getKom_feed_id() == 0) {
				trackId = String.valueOf(workflowMessageModel.getAccnum());
				chargebackDeleteId = String.valueOf(workflowMessageModel.getMob_feed_id());
			} else if (workflowMessageModel.getMob_feed_id() == 0) {
				trackId = String.valueOf(workflowMessageModel.getKom_feed_id());
				chargebackDeleteId = String.valueOf(workflowMessageModel.getKom_feed_id());
			}
//			else {
//				trackId = String.valueOf(workflowMessageModel.getKom_feed_id());
//				chargebackDeleteId = (workflowMessageModel.getKom_feed_id());
//			}

		} else {
			// logger.info("Sales rep id inside ELSE");
			trackId = workflowMessageModel.getCorp() + "_" + workflowMessageModel.getHouse() + "_"
					+ workflowMessageModel.getCust() + "_" + workflowMessageModel.getSales_rep_id();
		}

		// logger.info("chargebackDeleteId = " + chargebackDeleteId);
		// logger.info("trackId 1 = " + trackId);
		String tracking_id = "";

		int tracking_id_cnt = jdbcTemplate.queryForObject("select count(*) cnt from t_workflow_details", Integer.class);
		if (tracking_id_cnt > 0) {
			tracking_id = jdbcTemplate.queryForObject(
					"select twd2.tracking_id from t_workflow_details twd2 where twd2.ticket_id  = (select max(twd.ticket_id) ticket_id from t_workflow_details twd)",
					String.class);
		} else {
			tracking_id = "";
		}

		// logger.info("tracking_id = " + tracking_id);

		Pattern p = Pattern.compile(".*_\\s*(.*)");
		Matcher m = p.matcher(tracking_id);
		// logger.info("m = " + m);

		if (m.find()) {
			// logger.info("value 1 = " + tracking_id.substring(0,
			// tracking_id.indexOf("_")));
			// logger.info("m = " + m.group(1).substring(0, 1));
			int val = Integer.parseInt(m.group(1).substring(0, 1));
			// logger.info("val = " + val);
			int val1 = val + 1;
			// logger.info("val1 = " + val1);
			trackId = trackId + "_" + String.valueOf(val1);
			// logger.info("trackId = " + trackId);
		} else {
			// logger.info("ELSE: Not found");
			trackId = trackId + "_1";
		}
		// logger.info("trackId 2 = " + trackId);

//		if (workflowMessageModel.getKom_feed_id() == 0 && workflowMessageModel.getMob_feed_id() == 0) {
//			chargebackDeleteId = trackId;
//		}

		String trackingId = createTicket.createTicketProcess(WorkFlowConstants.DISPUTE_WORKFLOW,
				workflowMessageModel.getCreated_by(), 0, workflowMessageModel.getEmployee_id(),
				workflowMessageModel.getSc_emp_id(), trackId, against_salesrepid,
				workflowMessageModel.getLoggedin_role());

		String contentMsg = "<TrackingID>" + trackId + "</TrackingID>" + "<SubmittedBy>"
				+ workflowMessageModel.getCreated_by() + "</SubmittedBy>" + "<SubmittedDate>" + curDate
				+ "</SubmittedDate>" + workflowMessageModel.getMessage_content();

		WorkflowMessageEntity workFlowObj = new WorkflowMessageEntity();
		// logger.info("workflowMessageModel getComplex_kpiid = " +
		// workflowMessageModel.getComplex_kpiid());
		workFlowObj.setMessage_content(contentMsg);
		workFlowObj.setTracking_id(trackId);
		workFlowObj.setChargeback_delete_id(chargebackDeleteId);
		workFlowObj.setCreated_by(workflowMessageModel.getCreated_by());
		workFlowObj.setComm_plan_id(workflowMessageModel.getComplanid());
		workFlowObj.setComplex_kpiid(workflowMessageModel.getComplex_kpiid());
		workFlowObj.setCreated_dt(new Date());
		workFlowObj = workFlowMessageRepository.save(workFlowObj);

		return workFlowObj;
	}

	@Override
	public WorkflowMessageEntity insertDisputeStatic(WorkflowMessageEntity workflowMessageModel)
			throws DuplicateRecordException, IdNotFoundException, ParseException {

		// logger.info("insertDisputeStatic workflowMessageModel = " +
		// workflowMessageModel);

		String against_salesrepid = workFlowMessageRepository.getSalesRepID(workflowMessageModel.getEmployee_id(),
				workflowMessageModel.getLoggedin_role());
		// logger.info("against_salesrepid = " + against_salesrepid);
		String curDate = new SimpleDateFormat("MM-dd-yyyy").format(new Date());

		// logger.info("getCalRunId = " + workflowMessageModel.getCalRunId());
		// logger.info("getSc_emp_id = " + workflowMessageModel.getSc_emp_id());

		String trackId = workflowMessageModel.getCalRunId() + "_" + workflowMessageModel.getSc_emp_id();

		// logger.info("trackId 1 = " + trackId);

		int tracking_id_cnt = jdbcTemplate.queryForObject(
				"select count(twd2.tracking_id) cnt from t_workflow_details twd2 where twd2.ticket_id  = (select max(twd.ticket_id) ticket_id from t_workflow_details twd)",
				Integer.class);

		String tracking_id = "";

		if (tracking_id_cnt > 0) {
			tracking_id = jdbcTemplate.queryForObject(
					"select twd2.tracking_id from t_workflow_details twd2 where twd2.ticket_id  = (select max(twd.ticket_id) ticket_id from t_workflow_details twd)",
					String.class);
		} else {
			tracking_id = "1_0";
		}

		// logger.info("tracking_id = " + tracking_id);

		Pattern p = Pattern.compile(".*_\\s*(.*)");
		Matcher m = p.matcher(tracking_id);
		// logger.info("m = " + m);

		if (m.find()) {
			// logger.info("value 1 = " + tracking_id.substring(0,
			// tracking_id.indexOf("_")));
			// logger.info("m.group(1)" + m.group(1));
			int val = Integer.parseInt(m.group(1));
			// logger.info("val" + val);
			int val1 = val + 1;
			trackId = trackId + "_" + String.valueOf(val1);
		} else {
			// logger.info("ELSE: Not found");
			trackId = trackId + "_1";
		}

		// logger.info("trackId 2 = " + trackId);

		String trackingId = createTicket.createTicketProcess(WorkFlowConstants.DISPUTE_WORKFLOW,
				workflowMessageModel.getCreated_by(), 0, workflowMessageModel.getEmployee_id(),
				workflowMessageModel.getSc_emp_id(), trackId, against_salesrepid,
				workflowMessageModel.getLoggedin_role());
		// logger.info("trackingId = " + trackingId);

		String contentMsg = "<TrackingID>" + trackId + "</TrackingID>" + "<SubmittedBy>"
				+ workflowMessageModel.getCreated_by() + "</SubmittedBy>" + "<SubmittedDate>" + curDate
				+ "</SubmittedDate>" + workflowMessageModel.getMessage_content();
		// logger.info("contentMsg = " + contentMsg);

		WorkflowMessageEntity workFlowObj = new WorkflowMessageEntity();
		workFlowObj.setMessage_content(contentMsg);
		workFlowObj.setTracking_id(trackId);
		workFlowObj.setComm_plan_id(workflowMessageModel.getComplanid());
		workFlowObj.setCreated_by(workflowMessageModel.getCreated_by());
		workFlowObj.setCreated_dt(new Date());
		workFlowObj = workFlowMessageRepository.save(workFlowObj);

		return workFlowObj;
	}

	@Override
	public List<Map<String, Object>> getCrosstab() {
		List<UploadJsonEntity> allData = uploadJsonRepository.findAll();
		Set<Long> emplids = allData.stream().map(UploadJsonEntity::getId).collect(Collectors.toSet());
		Set<String> kpiids = allData.stream().map(UploadJsonEntity::getReport_type).collect(Collectors.toSet());

		logger.info("emplids = " + emplids);
		logger.info("kpiids = " + kpiids);

		List<Map<String, Object>> crosstab = emplids.stream().map(emplid -> {
			Map<String, Object> row = new HashMap<>();
			row.put("EMPLID", emplid);

//			kpiids.forEach(kpiid -> {
//				allData.stream()
//				.filter(e -> e.getReEmplid().equals(emplid) && e.getKpiid().equals(kpiid)).findFirst()
//						.ifPresent(e -> row.put(kpiid, e.getValue()));
//			});

			return row;

		}).collect(Collectors.toList());

		return crosstab;
	}

	// @Override
	public List<LinkedHashMap<String, String>> getReport(String reporttype, String commPlanId, String calRunId,
			String payrollDt) {
		logger.info("Inside getReport");
		logger.info("reporttype = " + reporttype);
		logger.info("commPlanId = " + commPlanId);
		logger.info("calRunId = " + calRunId);
		logger.info("payrollDt = " + payrollDt);

		String query = "";

		if (reporttype.equals("CommsByKPIReport")) {
			query = COMMSBYKPIREPORT;
		} else if (reporttype.equals("ChargebackDetailReport")) {
			query = CHARGEBACKDETAILREPORT;
		} else if (reporttype.equals("CustomerDetailReport")) {
			query = CUSTOMERDETAILREPORT;
		} else if (reporttype.equals("CommissionDocReport")) {
			query = COMMISSIONDOCREPORT;
		} else if (reporttype.equals("DumpMonthlyComm")) {
			query = DUMPMONTHLYCOMM;
		} else if (reporttype.equals("DumpMonthlyDetailComm")) {
			query = DUMPMONTHLYDETAILCOMM;
		} else if (reporttype.equals("CommsByKPICrossTabReport")) {
			query = COMMSBYKPICROSSTABREPORT;
		} else if (reporttype.equals("PayRollReport")) {
			query = PAYROLLREPORT;
		}

		logger.info("query = " + query);
		List<LinkedHashMap<String, String>> finalResultsetList = new ArrayList<>();

		if (reporttype.equals("DumpMonthlyDetailComm")) {
			logger.info("Inside IF");
			String[] commPlanIdStr = commPlanId.split("[,]", 0);
			for (int i = 0; i < commPlanIdStr.length; i++) {

				logger.info("commPlanIdStr[i] = " + commPlanIdStr[i]);

				jdbcTemplate.query(query, new ColumnMapRowMapper() {

					@Override
					public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
						ReportFrameworkModel reportFrameworkModel = new ReportFrameworkModel();

						LinkedHashMap<String, String> mapObj = new LinkedHashMap<>();

						ResultSetMetaData rmd = rs.getMetaData();
						int columnsCount = rmd.getColumnCount();

						for (int k = 1; k <= columnsCount; k++) {
							mapObj.put(rmd.getColumnName(k), rs.getString(k));
						}

						finalResultsetList.add(mapObj);
						reportFrameworkModel.setFields(finalResultsetList);

						return null;
					}

				}, new Object[] { calRunId, commPlanIdStr[i], payrollDt });
			}
			logger.info("finalResultsetList = " + finalResultsetList);
		} else {
			logger.info("Inside ELSE");
			String[] commPlanIdStr = commPlanId.split("[,]", 0);
			for (int i = 0; i < commPlanIdStr.length; i++) {

				logger.info("commPlanIdStr[i] = " + commPlanIdStr[i]);

				jdbcTemplate.query(query, new ColumnMapRowMapper() {

					@Override
					public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
						ReportFrameworkModel reportFrameworkModel = new ReportFrameworkModel();

						LinkedHashMap<String, String> mapObj = new LinkedHashMap<>();

						ResultSetMetaData rmd = rs.getMetaData();
						int columnsCount = rmd.getColumnCount();

						for (int k = 1; k <= columnsCount; k++) {
							mapObj.put(rmd.getColumnName(k), rs.getString(k));
						}

						finalResultsetList.add(mapObj);
						reportFrameworkModel.setFields(finalResultsetList);

						return null;
					}

				}, new Object[] { calRunId, commPlanIdStr[i] });
			}
			logger.info("finalResultsetList = " + finalResultsetList);
		}

		return finalResultsetList;
	}

	@Override
	public List<Object> getStreamReports(String reporttype, String commPlanId, String calRunId, String payrollDt)
			throws NumberFormatException, ParseException {
		logger.info("getStreamByDetails() called");

		List<Object> additionalValues = new ArrayList<>();
		String[] commPlanIdStr = commPlanId.split("[,]", 0);
		for (int i = 0; i < commPlanIdStr.length; i++) {
			additionalValues.add(commPlanIdStr[i]);
		}

		Stream<Object> additionalStream = additionalValues.stream();

		List<Object> ids = additionalStream.collect(Collectors.toList());

		String inSQL = ids.stream().map(id -> "?").collect(Collectors.joining(", "));

		PreparedStatementSetter pss = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int index = 1;

				for (Object id : ids) {
					ps.setObject(index++, id);
				}
				ps.setString(index, calRunId);

				if (reporttype.equals("DumpMonthlyDetailComm")) {
					ps.setString(index + 1, payrollDt);
					ps.setString(index + 2, payrollDt);
				}
			}
		};

		String sqlQuery = "";
		
		sqlQuery = uploadJsonRepository.getReportSql(reporttype,reporttype);

		try (Stream<Map<String, Object>> stream = jdbcTemplate.queryForStream(String.format(sqlQuery, inSQL), pss,
				new ColumnMapRowMapper())) {
			return stream.collect(Collectors.toList());
		}

	}

}