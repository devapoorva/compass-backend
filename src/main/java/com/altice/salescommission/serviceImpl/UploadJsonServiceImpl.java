package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.ID_NOT_FOUND;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.altice.salescommission.entity.UploadJsonEntity;
import com.altice.salescommission.entity.UploadJsonTransEntity;
import com.altice.salescommission.entity.UploadButtonsEntity;
import com.altice.salescommission.entity.UploadHeadersEntity;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.model.UploadJsonHeadersModel;
import com.altice.salescommission.queries.UploadJsonQueries;
import com.altice.salescommission.repository.UploadButtonsRepository;
import com.altice.salescommission.repository.UploadHeadersRepository;
import com.altice.salescommission.repository.UploadJsonRepository;
import com.altice.salescommission.repository.UploadJsonTransRepository;
import com.altice.salescommission.service.UploadJsonService;

@Service
@Transactional
public class UploadJsonServiceImpl extends AbstractBaseRepositoryImpl<UploadJsonEntity, Long>
		implements UploadJsonService, UploadJsonQueries {

	private static final Logger logger = LoggerFactory.getLogger(UploadJsonServiceImpl.class.getName());

	@Autowired
	private UploadJsonRepository uploadJsonRepository;

	@Autowired
	private UploadJsonTransRepository uploadJsonTransRepository;

	@Autowired
	private UploadHeadersRepository uploadHeadersRepository;

	@Autowired
	private UploadButtonsRepository uploadButtonsRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public UploadJsonServiceImpl(UploadJsonRepository uploadJsonRepository) {
		super(uploadJsonRepository);
	}

	/* Upload JSON */
	@Override
	public UploadJsonEntity uploadJSON(String gridName, String sql, String pageName, int orderBy, String current_user,
			String kpiId, String report_type, String description, String comm_plan_id,String salesChannel)
			throws IOException, IdNotFoundException {

		logger.info("comm_plan_id = " + comm_plan_id);
		logger.info("kpiId = " + kpiId);

		UploadJsonEntity uploadJsonModel = new UploadJsonEntity();

		uploadJsonModel.setPageName(pageName);
		uploadJsonModel.setDescription(description);
		uploadJsonModel.setGridName(gridName);
		uploadJsonModel.setColSort(orderBy);
		uploadJsonModel.setReport_type(report_type);
		uploadJsonModel.setSalesChannel(salesChannel);
		//uploadJsonModel.setCommPlanId(Integer.parseInt(comm_plan_id));
		//uploadJsonModel.setKpiId(Integer.parseInt(kpiId));
		uploadJsonModel.setSql(sql);
		uploadJsonModel.setStatus("Active");
		uploadJsonModel.setUser_type("na");
		uploadJsonModel.setCreated_dt(new Date());
		uploadJsonModel.setCreated_by(current_user);

		uploadJsonRepository.save(uploadJsonModel);
		
		int generatedId = Integer.valueOf(uploadJsonModel.getId().toString());

		UploadJsonTransEntity upJsonObj = new UploadJsonTransEntity();
			upJsonObj.setMapping_id(generatedId);
			upJsonObj.setComm_plan_id(Integer.parseInt(comm_plan_id));
			upJsonObj.setKpi_id(Integer.parseInt(kpiId));
			uploadJsonTransRepository.save(upJsonObj);

		return uploadJsonModel;
	}

	/* Clone data */
	@Override
	public UploadJsonEntity cloneData(String sc, int commId, int commPlanId, String salesChannel, String current_user)
			throws IOException, IdNotFoundException {

		logger.info(current_user);

		List<UploadJsonEntity> jsonsList = jdbcTemplate.query(GET_EXISTING_DATA, new RowMapper<UploadJsonEntity>() {

			@Override
			public UploadJsonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				UploadJsonEntity uploadJsonModel = new UploadJsonEntity();
				uploadJsonModel.setPageName(rs.getString("page_name"));
				uploadJsonModel.setGridName(rs.getString("grid_name"));
				uploadJsonModel.setColSort(rs.getInt("order_by"));
				uploadJsonModel.setSql(rs.getString("sql"));
				uploadJsonModel.setStatus(rs.getString("status"));
				uploadJsonModel.setComment(rs.getString("com"));
				uploadJsonModel.setKpiId(rs.getInt("kpi_id"));
				uploadJsonModel.setReport_type(rs.getString("report_type"));
				return uploadJsonModel;
			}
		}, new Object[] { sc, commId });

		UploadJsonEntity upJsonObj = null;
		for (UploadJsonEntity getUpJsonObj : jsonsList) {
			upJsonObj = new UploadJsonEntity();
			upJsonObj.setPageName(getUpJsonObj.getPageName());
			upJsonObj.setGridName(getUpJsonObj.getGridName());
			upJsonObj.setColSort(getUpJsonObj.getColSort());
			upJsonObj.setReport_type(getUpJsonObj.getReport_type());
			upJsonObj.setSql(getUpJsonObj.getSql());
			upJsonObj.setStatus(getUpJsonObj.getStatus());
			upJsonObj.setComment(getUpJsonObj.getComment());
			upJsonObj.setKpiId(getUpJsonObj.getKpiId());
			upJsonObj.setCommPlanId(commPlanId);
			upJsonObj.setSalesChannel(salesChannel);
			upJsonObj.setCreated_dt(new Date());
			upJsonObj.setCreated_by(current_user);
			uploadJsonRepository.save(upJsonObj);

//			long generatedId = upJsonObj.getId();
//
//			UploadJsonEntity jsonModel = uploadJsonRepository.findById(generatedId)
//					.orElseThrow(() -> new IdNotFoundException(generatedId + ID_NOT_FOUND));
//
//			jsonModel.setParentPageId(generatedId);
//			uploadJsonRepository.save(jsonModel);
		}
		return upJsonObj;
	}

	/* Updated File */
	@Override
	public UploadJsonEntity updateJSON(MultipartFile file, String gridName, String sql, String pageName, int orderBy,
			String current_user, int commPlanId, String salesChannel, int kpiId, long id)
			throws IOException, IdNotFoundException {
		System.out.println("commPlanId = " + commPlanId);
		System.out.println("salesChannel = " + salesChannel);
		System.out.println("kpiId = " + kpiId);
		System.out.println("file = " + file);

		UploadJsonEntity uploadJsonModel = uploadJsonRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Id not found" + id));

		// uploadJsonModel = new
		// UploadJsonModel(Base64.getEncoder().encodeToString(file.getBytes()));
		// uploadJsonModel.setJson(Base64.getEncoder().encodeToString(file.getBytes()));
		uploadJsonModel.setGridName(gridName);
		uploadJsonModel.setSql(sql);
		uploadJsonModel.setColSort(orderBy);
		uploadJsonModel.setPageName(pageName);
		uploadJsonModel.setStatus("Active");
		uploadJsonModel.setCommPlanId(commPlanId);
		uploadJsonModel.setSalesChannel(salesChannel);
		uploadJsonModel.setKpiId(kpiId);
		uploadJsonModel.setUpdated_dt(new Date());
		uploadJsonModel.setUpdated_by(current_user);
		uploadJsonRepository.save(uploadJsonModel);

		return uploadJsonModel;
	}

	/* Updated */
	@Override
	public UploadJsonEntity updateJSONWithoutFile(String gridName, String sql, String pageName, int orderBy,
			String current_user, String commPlanId, String salesChannel, String kpiId, long id)
			throws IOException, IdNotFoundException {
		System.out.println("commPlanId = " + commPlanId);
		System.out.println("salesChannel = " + salesChannel);
		System.out.println("kpiId = " + kpiId);

		UploadJsonEntity uploadJsonModel = uploadJsonRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Id not found" + id));

		// uploadJsonModel = new
		// UploadJsonModel(Base64.getEncoder().encodeToString(file.getBytes()));
		// uploadJsonModel.setJson(Base64.getEncoder().encodeToString(file.getBytes()));
		uploadJsonModel.setGridName(gridName);
		uploadJsonModel.setSql(sql);
		uploadJsonModel.setColSort(orderBy);
		uploadJsonModel.setPageName(pageName);
		uploadJsonModel.setStatus("Active");
		uploadJsonModel.setSalesChannel(salesChannel);
		// uploadJsonModel.setKpiId(kpiId);
		// uploadJsonModel.setCommPlanId(commPlanId);
		uploadJsonModel.setUpdated_dt(new Date());
		uploadJsonModel.setUpdated_by(current_user);
		uploadJsonRepository.save(uploadJsonModel);

		String str_kpi[] = kpiId.split(",");
		String str_complan[] = commPlanId.split(",");

		int generatedId = Integer.valueOf(uploadJsonModel.getId().toString());
		
		String deleteQuery = "delete from c_json_sql_mapping_trans where mapping_id=?";
		jdbcTemplate.update(deleteQuery, generatedId);
		
		UploadJsonTransEntity upJsonObj = null;

		for (int i = 0; i < str_kpi.length; i++) {
			upJsonObj = new UploadJsonTransEntity();
			upJsonObj.setMapping_id(generatedId);
			upJsonObj.setComm_plan_id(Integer.valueOf(str_complan[i]));
			upJsonObj.setKpi_id(Integer.valueOf(str_kpi[i]));
			uploadJsonTransRepository.save(upJsonObj);
		}

		return uploadJsonModel;
	}

	@Override
	public List<UploadJsonEntity> getJsonsList(String salesChannels, int commPlanId) {
		List<UploadJsonEntity> jsonsList = jdbcTemplate.query(GET_JSONS_LIST, new RowMapper<UploadJsonEntity>() {

			@Override
			public UploadJsonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				UploadJsonEntity uploadJsonModel = new UploadJsonEntity();
				uploadJsonModel.setId(rs.getLong("id"));
				uploadJsonModel.setGridName(rs.getString("grid_name"));
				uploadJsonModel.setColSort(rs.getInt("order_by"));
				uploadJsonModel.setPageName(rs.getString("page_name"));
				uploadJsonModel.setSql(rs.getString("sql"));
				uploadJsonModel.setKpiId(rs.getInt("kpi_id"));
				uploadJsonModel.setKpiName(rs.getString("kpi_name"));
				uploadJsonModel.setReport_type(rs.getString("report_type"));
				uploadJsonModel.setStatus(rs.getString("status"));
				uploadJsonModel.setCommPlanId(rs.getInt("commplanid"));
				uploadJsonModel.setDescription(rs.getString("description"));
				uploadJsonModel.setSalesChannel(rs.getString("saleschannel"));

				return uploadJsonModel;
			}
		});
		// }, new Object[] { salesChannels, commPlanId });
		return jsonsList;
	}

	@Override
	public List<UploadJsonEntity> getKpisList() {
		List<UploadJsonEntity> jsonsList = jdbcTemplate.query(GET_KPIS_LIST, new RowMapper<UploadJsonEntity>() {

			@Override
			public UploadJsonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				UploadJsonEntity uploadJsonModel = new UploadJsonEntity();
				uploadJsonModel.setKpiId(rs.getInt("kpi_id"));
				uploadJsonModel.setKpiName(rs.getString("kpi_name"));
				return uploadJsonModel;
			}
		});
		return jsonsList;
	}

	@Override
	public List<UploadJsonEntity> getComPlansList() {
		List<UploadJsonEntity> jsonsList = jdbcTemplate.query(GET_COMM_PLANS_LIST, new RowMapper<UploadJsonEntity>() {

			@Override
			public UploadJsonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				UploadJsonEntity uploadJsonModel = new UploadJsonEntity();
				uploadJsonModel.setCommPlanId(rs.getInt("comm_plan_id"));
				uploadJsonModel.setCommPlan(rs.getString("comm_plan"));
				return uploadJsonModel;
			}
		});
		return jsonsList;
	}

	@Override
	public List<UploadJsonEntity> getReportTypesList() {
		List<UploadJsonEntity> jsonsList = jdbcTemplate.query(GET_REPORT_TYPES_LIST, new RowMapper<UploadJsonEntity>() {

			@Override
			public UploadJsonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				UploadJsonEntity uploadJsonModel = new UploadJsonEntity();
				uploadJsonModel.setReport_type(rs.getString("field_value"));
				return uploadJsonModel;
			}
		});
		return jsonsList;
	}

	@Override
	public List<UploadJsonEntity> getSalesChannelsList() {
		List<UploadJsonEntity> jsonsList = jdbcTemplate.query(GET_SALES_CHANNELS_LIST,
				new RowMapper<UploadJsonEntity>() {

					@Override
					public UploadJsonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadJsonEntity uploadJsonModel = new UploadJsonEntity();
						uploadJsonModel.setSalesChannel(rs.getString("saleschannel"));
						return uploadJsonModel;
					}
				});
		return jsonsList;
	}

	@Override
	public List<UploadJsonEntity> getCommPlans(String saleschannel) {
		List<UploadJsonEntity> commPlansList = jdbcTemplate.query(GET_COMMPLANS_LIST,
				new RowMapper<UploadJsonEntity>() {

					@Override
					public UploadJsonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadJsonEntity uJsonObj = new UploadJsonEntity();
						uJsonObj.setCommPlanId(rs.getInt("commplanid"));
						uJsonObj.setCommPlan(rs.getString("comm_plan"));
						return uJsonObj;
					}
				}, new Object[] { saleschannel });

		return commPlansList;
	}

	@Override
	public UploadHeadersEntity updateHeaders(List<UploadHeadersEntity> uploadJsonHeadersModel) {
		System.out.println("uploadJsonHeadersModel = " + uploadJsonHeadersModel);
		UploadHeadersEntity upHeaderObj = null;
		int i = 0;
		int jid = 0;
		System.out.println(
				"uploadJsonHeadersModel.get(0).getJsonsqlid() = " + uploadJsonHeadersModel.get(0).getJsonsqlid());
		String deleteQuery = "delete from c_json_sql_headers where jsonsqlid=?";
		jdbcTemplate.update(deleteQuery, uploadJsonHeadersModel.get(0).getJsonsqlid());

		for (UploadHeadersEntity uploadHeadersModel : uploadJsonHeadersModel) {
			i++;
			if (i == 1) {
				jid = uploadHeadersModel.getJsonsqlid();
			}
			upHeaderObj = new UploadHeadersEntity();
			upHeaderObj.setId(uploadHeadersModel.getId());
			upHeaderObj.setJsonsqlid(jid);
			upHeaderObj.setName(uploadHeadersModel.getName());
			upHeaderObj.setDatakey(uploadHeadersModel.getDatakey());
			upHeaderObj.setPagenav(uploadHeadersModel.getPagenav());
			upHeaderObj.setColspancnt(uploadHeadersModel.getColspancnt());
			upHeaderObj.setColorder(uploadHeadersModel.getColorder());
			upHeaderObj.setPosition(uploadHeadersModel.getPosition());
			upHeaderObj.setIstotalview(uploadHeadersModel.getIstotalview());
			upHeaderObj.setIshyperlinkview(uploadHeadersModel.getIshyperlinkview());
			upHeaderObj.setIsdisputeview(uploadHeadersModel.getIsdisputeview());
			upHeaderObj.setIssortable("false");
			upHeaderObj.setEdit("false");
			upHeaderObj.setUpdated_by("Venkat");
			upHeaderObj.setUpdated_dt(new Date());
			upHeaderObj.setIstotalcalculate("false");
			upHeaderObj = uploadHeadersRepository.save(upHeaderObj);
		}
		return upHeaderObj;
	}

	@Override
	public UploadButtonsEntity updateButtons(List<UploadButtonsEntity> uploadJsonHeadersModel) {
		// System.out.println("uploadJsonHeadersModel = " + uploadJsonHeadersModel);
		UploadButtonsEntity upHeaderObj = null;
		int i = 0;
		int jid = 0;
		// System.out.println("uploadJsonHeadersModel.get(0).getJsonsqlid() = " +
		// uploadJsonHeadersModel.get(0).getJsonsqlid());
		String deleteQuery = "delete from c_json_sql_buttons where jsonsqlid=?";
		jdbcTemplate.update(deleteQuery, uploadJsonHeadersModel.get(0).getJsonsqlid());

		for (UploadButtonsEntity uploadHeadersModel : uploadJsonHeadersModel) {
			i++;
			if (i == 1) {
				jid = uploadHeadersModel.getJsonsqlid();
			}
			upHeaderObj = new UploadButtonsEntity();
			upHeaderObj.setId(uploadHeadersModel.getId());
			upHeaderObj.setJsonsqlid(jid);
			upHeaderObj.setName(uploadHeadersModel.getName());
			upHeaderObj.setBtn_type(uploadHeadersModel.getBtn_type());
			upHeaderObj.setPagenav(uploadHeadersModel.getPagenav());
			upHeaderObj.setUpdated_by("Venkat");
			upHeaderObj.setUpdated_dt(new Date());
			upHeaderObj = uploadButtonsRepository.save(upHeaderObj);
		}
		return upHeaderObj;
	}

	@Override
	public List<UploadJsonHeadersModel> getHeaders(int id) {

		System.out.println("getHeaders = " + id);

		List<UploadJsonHeadersModel> headersList = jdbcTemplate.query(GET_HEADERS,
				new RowMapper<UploadJsonHeadersModel>() {

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
						commissionSummaryModel.setPagenav(rs.getString("pagenav"));
						commissionSummaryModel.setIstotalcalculate(rs.getString("istotalcalculate"));
						commissionSummaryModel.setColorder(rs.getInt("colorder"));

						return commissionSummaryModel;
					}
				}, new Object[] { id });

		System.out.println("headersList = " + headersList);
		return headersList;
	}
	
	@Override
	public List<UploadJsonEntity> getCommPlanAndKpis(int id) {

		System.out.println("getHeaders = " + id);

		List<UploadJsonEntity> headersList = jdbcTemplate.query(GET_KPIS,
				new RowMapper<UploadJsonEntity>() {

					@Override
					public UploadJsonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						UploadJsonEntity commissionSummaryModel = new UploadJsonEntity();

						commissionSummaryModel.setKpiId(rs.getInt("kpi_id"));
						commissionSummaryModel.setCommPlanId(rs.getInt("comm_plan_id"));

						return commissionSummaryModel;
					}
				}, new Object[] { id });

		System.out.println("headersList = " + headersList);
		return headersList;
	}

	@Override
	public List<UploadJsonHeadersModel> getButtons(int id) {

		System.out.println("getButtons = " + id);

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
				}, new Object[] { id });

		System.out.println("buttonsList = " + buttonsList);
		return buttonsList;
	}

}