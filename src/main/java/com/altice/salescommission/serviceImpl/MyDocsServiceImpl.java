package com.altice.salescommission.serviceImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.altice.salescommission.approvalworkflow.WorkFlowConstants;
import com.altice.salescommission.entity.MyDocTransEntity;
import com.altice.salescommission.entity.MyDocsEntity;
import com.altice.salescommission.entity.WorkflowMessageEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.queries.MyDocsQueries;
import com.altice.salescommission.repository.MyDocTransRepository;
import com.altice.salescommission.repository.MyDocsRepository;
import com.altice.salescommission.repository.WorkFlowMessageRepository;
import com.altice.salescommission.service.MyDocsService;
import com.altice.salescommission.utility.CreateTicket;

@Service
@Transactional
public class MyDocsServiceImpl extends AbstractBaseRepositoryImpl<MyDocsEntity, Long>
		implements MyDocsService, MyDocsQueries {

	private static final Logger logger = LoggerFactory.getLogger(MyDocsServiceImpl.class.getName());

	@Autowired
	private MyDocsRepository myDocsRepository;

	@Autowired
	private MyDocTransRepository myDocTransRepository;

	@Autowired
	private WorkFlowMessageRepository workFlowMessageRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CreateTicket createTicket;

	public MyDocsServiceImpl(MyDocsRepository myDocsRepository) {
		super(myDocsRepository);
	}

	/* Upload New Documents */
	@Override
	public MyDocsEntity uploadDocs(MultipartFile file, String doc_name, String doc_desc, int comm_plan_id,
			String current_user, String effective_dt) throws IOException, ParseException {


		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date edate = formatter.parse(effective_dt);

		MyDocsEntity myDocsModel = new MyDocsEntity(file.getBytes());
		myDocsModel.setDoc_name(doc_name);
		myDocsModel.setDoc_desc(doc_desc);
		myDocsModel.setComm_plan_id(comm_plan_id);
		myDocsModel.setEffective_dt(edate);
		myDocsModel.setCreated_dt(new Date());
		myDocsModel.setStatus("Active");
		myDocsModel.setCreated_by(current_user);

		return myDocsRepository.save(myDocsModel);
	}

	/* update multiple documents */
	@Override
	public MyDocsEntity updateDocs(List<MyDocsEntity> myDocsModel) throws IOException, ParseException {
		MyDocsEntity updateDocs = null;
		int i = 0;
		String updatedBy = "";
		for (MyDocsEntity myDocs : myDocsModel) {

			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
			String dateAsString = formatter.format(myDocs.getEffective_dt());
			Date edate = formatter.parse(dateAsString);

			Date existingEffectiveDate = myDocsRepository.getOrgEffectiveDate(myDocs.getId());
			String dateexAsString = formatter.format(existingEffectiveDate);
			Date exedate = formatter.parse(dateexAsString);

			
			if (i == 0) {
				 updatedBy = myDocs.getUpdated_by();
			}
			
			byte[] docName = myDocsRepository.getDoc(myDocs.getId());
			
			
			if (exedate.equals(edate)) {
				MyDocsEntity myDoc = myDocsRepository.findById(myDocs.getId())
						.orElseThrow(() -> new ResourceNotFoundException("Id not found" + myDocs.getId()));

				myDoc.setDoc_name(myDocs.getDoc_name());
				myDoc.setDoc_desc(myDocs.getDoc_desc());
				myDoc.setEffective_dt(myDocs.getEffective_dt());
				myDoc.setStatus(myDocs.getStatus());
				myDoc.setUpdated_by(updatedBy);
				myDoc.setUpdated_dt(new Date());

				updateDocs = myDocsRepository.save(myDoc);
			} else {
				MyDocsEntity docObj = new MyDocsEntity();
				docObj.setComm_doc(docName);
				docObj.setDoc_name(myDocs.getDoc_name());
				docObj.setDoc_desc(myDocs.getDoc_desc());
				docObj.setComm_plan_id(myDocs.getComm_plan_id());
				docObj.setEffective_dt(myDocs.getEffective_dt());
				docObj.setStatus("Active");
				docObj.setCreated_by(updatedBy);
				docObj.setCreated_dt(new Date());

				updateDocs =  myDocsRepository.save(docObj);
			}
			i++;

		}

		return updateDocs;
	}

	@Override
	public MyDocTransEntity saveDocs(List<MyDocTransEntity> myDocsModel) throws ParseException {
		MyDocTransEntity saveDocs = null;
		int i = 0;
		String str = "";

		for (MyDocTransEntity myDocsObj : myDocsModel) {

			str = myDocsObj.getEmployeeId();
		}

		String[] empStr = str.split("[,]", 0);
		for (String empid : empStr) {

			for (MyDocTransEntity myDocsObj : myDocsModel) {
				i++;

				String against_salesrepid = myDocTransRepository.getSalesRepID(empid);

				String trackId = myDocTransRepository.getSCEmpId(empid) + myDocsObj.getId().intValue();

				String trackingId = createTicket.createTicketProcess(WorkFlowConstants.EMPLOYEE_DOC_WORKFLOW,
						myDocsObj.getCreated_by(), i, empid, myDocTransRepository.getSCEmpId(empid), trackId,
						against_salesrepid,"Sales Representative");

				MyDocTransEntity myDocSaveObj = new MyDocTransEntity();

				SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
				Date edate = formatter.parse(myDocsObj.getEDate());

				myDocSaveObj.setComm_doc_id(myDocsObj.getId().intValue());
				myDocSaveObj.setComm_plan_id(myDocsObj.getComm_plan_id());
				myDocSaveObj.setEmployeeId(empid);
				myDocSaveObj.setScempId(myDocTransRepository.getSCEmpId(empid));
				myDocSaveObj.setEffective_dt(edate);
				myDocSaveObj.setStatus("Active");
				myDocSaveObj.setApproved_status("Pending");
				myDocSaveObj.setCreated_by(myDocsObj.getCreated_by());
				myDocSaveObj.setCreated_dt(new Date());
				myDocSaveObj.setUser_type(myDocsObj.getUser_type());
				myDocSaveObj.setTracking_id(trackingId);

				saveDocs = myDocTransRepository.save(myDocSaveObj);

				String contentMsg = "<TrackingID>" + trackId + "</TrackingID>" + "<SubmittedBy>"
						+ myDocsObj.getCreated_by() + "</SubmittedBy>" + "<SubmittedDate>" + new Date()
						+ "</SubmittedDate>" + "<Message>Document Approval</Message>";

				WorkflowMessageEntity workFlowObj = new WorkflowMessageEntity();
				workFlowObj.setMessage_content(contentMsg);
				workFlowObj.setTracking_id(trackId);
				workFlowObj.setCreated_by(myDocsObj.getCreated_by());
				workFlowObj.setCreated_dt(new Date());
				workFlowObj = workFlowMessageRepository.save(workFlowObj);

			}
		}
		return saveDocs;
	}

	/* This method is used to assign document to selected employees */
	@Override
	public MyDocTransEntity assignDocToEmployees(List<MyDocTransEntity> myDocsModel) throws IdNotFoundException {
		MyDocTransEntity saveEmpData = null;

		int i = 0;
		for (MyDocTransEntity myDocsObj : myDocsModel) {
			i++;

			String trackId = myDocTransRepository.getSCEmpId(myDocsObj.getEmployeeId()) + myDocsObj.getId().intValue();

			String trackingId = createTicket.createTicketProcess(WorkFlowConstants.EMPLOYEE_DOC_WORKFLOW,
					myDocsObj.getCreated_by(), i, myDocsObj.getEmployeeId(),
					myDocTransRepository.getSCEmpId(myDocsObj.getEmployeeId()), trackId, myDocsObj.getEmployeeId(),"Sales Representative");

			MyDocTransEntity myDocSaveObj = new MyDocTransEntity();
			myDocSaveObj.setComm_doc_id(myDocsObj.getId().intValue());
			myDocSaveObj.setComm_plan_id(myDocsObj.getComm_plan_id());
			myDocSaveObj.setEmployeeId(myDocsObj.getEmployeeId());
			myDocSaveObj.setScempId(myDocTransRepository.getSCEmpId(myDocsObj.getEmployeeId()));
			myDocSaveObj.setEffective_dt(myDocsObj.getEffective_dt());
			myDocSaveObj.setStatus("Active");
			myDocSaveObj.setApproved_status("Pending");
			myDocSaveObj.setCreated_by(myDocsObj.getCreated_by());
			myDocSaveObj.setCreated_dt(new Date());
			myDocSaveObj.setUser_type(myDocsObj.getUser_type());
			myDocSaveObj.setTracking_id(trackingId);
			saveEmpData = myDocTransRepository.save(myDocSaveObj);

			String contentMsg = "<TrackingID>" + trackId + "</TrackingID>" + "<SubmittedBy>" + myDocsObj.getCreated_by()
					+ "</SubmittedBy>" + "<SubmittedDate>" + new Date() + "</SubmittedDate>"
					+ "<Message>Document Approval</Message>";

			WorkflowMessageEntity workFlowObj = new WorkflowMessageEntity();
			workFlowObj.setMessage_content(contentMsg);
			workFlowObj.setTracking_id(trackId);
			workFlowObj.setCreated_by(myDocsObj.getCreated_by());
			workFlowObj.setCreated_dt(new Date());
			workFlowObj = workFlowMessageRepository.save(workFlowObj);

		}

		return saveEmpData;
	}

	/* Getting employee information based on selected user type */
	@Override
	public List<MyDocTransEntity> filterUserDocByEmp(String empId) {
		logger.info("empId = " + empId);
		List<String> ids = new ArrayList<String>();
		String[] empStr = empId.split("[,]", 0);
		for (int i = 0; i < empStr.length; i++) {
			ids.add(empStr[i]);
		}

		String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
		logger.info("inSql = " + inSql);
		@SuppressWarnings("deprecation")
		List<MyDocTransEntity> docsList = jdbcTemplate.query(
				String.format("select ccdm.doc_name ,ccdm.doc_desc ,ccdt.comm_doc_id ,ccdt.employee_id,"
						+ "	ccdt.approved_on,ccdt.approved_on signOffDate,concat(cem.first_name,',',cem.last_name) employeename ,ccpm.comm_plan,ccdm.effective_dt,to_char(ccdm.effective_dt,'MM-DD-YYYY') eDate  "
						+ "	from c_com_doc_trans ccdt ,c_com_doc_mgmt ccdm,c_employee_master cem,c_comm_plan_master ccpm "
						+ "	where ccdt.comm_doc_id =ccdm.comm_doc_id and ccdt.employee_id =cem.employee_id and ccdt.comm_plan_id =ccpm.comm_plan_id and ccdt.employee_id IN (%s)  "
						+ "	group by ccdm.doc_name ,ccdm.doc_desc ,ccdt.comm_doc_id ,ccdt.employee_id,ccdt.approved_on, employeename ,ccpm.comm_plan,ccdm.effective_dt ",
						inSql),
				ids.toArray(),
				(rs, rowNum) -> new MyDocTransEntity(rs.getString("doc_name"), rs.getString("doc_desc"),
						rs.getString("employee_id"), rs.getString("employeename"), rs.getString("comm_plan"),
						rs.getString("signOffDate"), rs.getDate("approved_on"), rs.getInt("comm_doc_id"),
						rs.getDate("effective_dt"), rs.getString("eDate")));

		logger.info("docsList = " + docsList);

		return docsList;
	}

	/* Getting employee information based on selected user type */
	@Override
	public List<MyDocsEntity> getEmployeeList(String val, String user_type, String doc_desc, int comPlanId) {

		logger.info("user_type = " + user_type);
		logger.info("doc_desc = " + doc_desc);
		logger.info("comPlanId = " + comPlanId);

		List<MyDocsEntity> empList = jdbcTemplate.query(GET_EMPLOYEE_LIST, new RowMapper<MyDocsEntity>() {

			@Override
			public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				MyDocsEntity myDocsModel = new MyDocsEntity();
				myDocsModel.setEmployeeId(rs.getString("employee_id"));
				myDocsModel.setEmployeeName(rs.getString("name"));
				myDocsModel.setSc_emp_id(rs.getString("sc_emp_id"));
				return myDocsModel;
			}
		}, new Object[] { val + '%', val + '%', val + '%', user_type, user_type, doc_desc, comPlanId });

		return empList;
	}

	/* Upload documents */
	@Override
	public MyDocsEntity updateSingleFile(MultipartFile file, Long id) throws IOException {

		MyDocsEntity myDoc = myDocsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Id not found" + id));

		// myDoc = new MyDocsModel(file.getBytes());
		myDoc.setComm_doc(file.getBytes());
		return myDocsRepository.save(myDoc);
	}

	/* Getting all the records of c_com_doc_mgmt table */
	@Override
	public List<MyDocsEntity> getMyDocsList() {

		List<MyDocsEntity> myDocsList = jdbcTemplate.query(GET_MY_DOCS_LIST, new RowMapper<MyDocsEntity>() {

			@Override
			public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				MyDocsEntity myDocsModel = new MyDocsEntity();
				myDocsModel.setId(rs.getLong("comm_doc_id"));
				myDocsModel.setDoc_name(rs.getString("doc_name"));
				myDocsModel.setDoc_desc(rs.getString("doc_desc"));
				myDocsModel.setComm_doc_str(rs.getString("comm_doc"));
				myDocsModel.setStatus(rs.getString("status"));
				myDocsModel.setCreated_dt(rs.getDate("created_dt"));
				myDocsModel.setUploadedDt(rs.getString("uploadeddt"));
				myDocsModel.setEffective_dt(rs.getDate("effective_dt"));
				myDocsModel.setEDate(rs.getString("eDate"));
				myDocsModel.setComm_plan_desc(rs.getString("comm_plan"));
				myDocsModel.setComm_plan_id(rs.getInt("comm_plan_id"));

				return myDocsModel;
			}
		});
		return myDocsList;

	}

	/* Getting all the records of c_com_doc_mgmt table */
	@Override
	public List<MyDocsEntity> getSupDocsList(String employeeid) {

		List<MyDocsEntity> myDocsList = jdbcTemplate.query(GET_EMP_DOCS_LIST, new RowMapper<MyDocsEntity>() {

			@Override
			public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				MyDocsEntity myDocsModel = new MyDocsEntity();
				myDocsModel.setId(rs.getLong("comm_doc_trans_id"));
				myDocsModel.setDoc_name(rs.getString("doc_name"));
				myDocsModel.setDoc_desc(rs.getString("doc_desc"));
				myDocsModel.setComm_doc_str(rs.getString("comm_doc"));
				myDocsModel.setStatus(rs.getString("status"));
				myDocsModel.setApproved_status(rs.getString("approved_status"));
				myDocsModel.setCreated_dt(rs.getDate("created_dt"));
				myDocsModel.setApproved_dt(rs.getString("approved_on"));
				myDocsModel.setComm_plan_desc(rs.getString("comm_plan"));

				return myDocsModel;
			}
		}, new Object[] { employeeid });
		return myDocsList;

	}

	/* Getting all the records of c_com_doc_mgmt table */
	@Override
	public List<MyDocsEntity> getEmpDocsList(String employeeid) {
		logger.info("employeeid = " + employeeid);

		List<MyDocsEntity> myDocsList = jdbcTemplate.query(GET_SUP_DOCS_LIST, new RowMapper<MyDocsEntity>() {

			@Override
			public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				MyDocsEntity myDocsModel = new MyDocsEntity();
				myDocsModel.setId(rs.getLong("comm_doc_id"));
				myDocsModel.setDoc_name(rs.getString("doc_name"));
				myDocsModel.setDoc_desc(rs.getString("doc_desc"));
				myDocsModel.setComm_doc_str(rs.getString("comm_doc"));
				myDocsModel.setStatus(rs.getString("status"));
				myDocsModel.setCreated_dt(rs.getDate("created_dt"));
				myDocsModel.setUploadedDt(rs.getString("uploadeddt"));
				myDocsModel.setEmployeeName(rs.getString("ename"));
				myDocsModel.setApproved_dt(rs.getString("approved_on"));
				myDocsModel.setApproved_status(rs.getString("approved_status"));

				return myDocsModel;
			}
		}, new Object[] { employeeid, employeeid });
		return myDocsList;

	}

	/* Getting all the records of c_com_doc_mgmt table */
	@Override
	public List<MyDocsEntity> getEmployeeDocsList(String employeeid) {

		List<MyDocsEntity> myDocsList = jdbcTemplate.query(GET_EMP_DOCS_LIST, new RowMapper<MyDocsEntity>() {

			@Override
			public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				MyDocsEntity myDocsModel = new MyDocsEntity();
				myDocsModel.setId(rs.getLong("comm_doc_trans_id"));
				myDocsModel.setDoc_name(rs.getString("doc_name"));
				myDocsModel.setDoc_desc(rs.getString("doc_desc"));
				myDocsModel.setComm_doc_str(rs.getString("comm_doc"));
				myDocsModel.setStatus(rs.getString("status"));
				myDocsModel.setApproved_status(rs.getString("approved_status"));
				myDocsModel.setCreated_dt(rs.getDate("created_dt"));
				myDocsModel.setApproved_dt(rs.getString("approved_on"));
				myDocsModel.setComm_plan_desc(rs.getString("comm_plan"));

				return myDocsModel;
			}
		}, new Object[] { employeeid });
		return myDocsList;

	}

	/* Getting all the records of c_com_doc_mgmt table */
	@Override
	public List<MyDocsEntity> getmydocslistBySaleschannel(String saleschannel) {
		logger.info("saleschannel = " + saleschannel);

		List<MyDocsEntity> myDocsList = jdbcTemplate.query(GET_MY_DOCS_LIST_BY_SALESCHANNEL,
				new RowMapper<MyDocsEntity>() {

					@Override
					public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						MyDocsEntity myDocsModel = new MyDocsEntity();
						myDocsModel.setId(rs.getLong("comm_doc_id"));
						myDocsModel.setDoc_name(rs.getString("doc_name"));
						myDocsModel.setDoc_desc(rs.getString("doc_desc"));
						myDocsModel.setComm_doc_str(rs.getString("comm_doc"));
						myDocsModel.setStatus(rs.getString("status"));
						myDocsModel.setCreated_dt(rs.getDate("created_dt"));
						myDocsModel.setComm_plan_desc(rs.getString("comm_plan"));
						myDocsModel.setEDate(rs.getString("edate"));
						

						return myDocsModel;
					}
				});
//	}, new Object[] { saleschannel });
		return myDocsList;

	}

	@Override
	public List<MyDocsEntity> getSaleschannelsList() {

		List<MyDocsEntity> saleschannelsList = jdbcTemplate.query(GET_SALES_CHANNEL_LIST,
				new RowMapper<MyDocsEntity>() {

					@Override
					public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						MyDocsEntity activeRateCodeModel = new MyDocsEntity();
						activeRateCodeModel.setSalesChannelDesc(rs.getString("sales_channel_desc"));
						return activeRateCodeModel;
					}
				});
		return saleschannelsList;

	}

	@Override
	public List<MyDocsEntity> getCommPlanList(String saleschannel) {

		List<MyDocsEntity> commPlanDescList = jdbcTemplate.query(GET_COMMPLAN_LIST, new RowMapper<MyDocsEntity>() {

			@Override
			public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				MyDocsEntity activeRateCodeModel = new MyDocsEntity();
				activeRateCodeModel.setComm_plan_id(rs.getInt("comm_plan_id"));
				activeRateCodeModel.setComm_plan_desc(rs.getString("comm_plan"));
				return activeRateCodeModel;
			}
		}, new Object[] { saleschannel });
		return commPlanDescList;

	}

	@Override
	public List<MyDocsEntity> getCommPlanDescList() {

		List<MyDocsEntity> commPlanDescList = jdbcTemplate.query(GET_COMMPLAN_DESC_LIST, new RowMapper<MyDocsEntity>() {

			@Override
			public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				MyDocsEntity activeRateCodeModel = new MyDocsEntity();
				activeRateCodeModel.setComm_plan_id(rs.getInt("comm_plan_id"));
				activeRateCodeModel.setComm_plan_desc(rs.getString("comm_plan"));
				return activeRateCodeModel;
			}
		});
		return commPlanDescList;

	}

	@Override
	public List<MyDocsEntity> getUserTypesList() {

		List<MyDocsEntity> activeRateCodesList = jdbcTemplate.query(GET_USER_TYPES_LIST, new RowMapper<MyDocsEntity>() {

			@Override
			public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				MyDocsEntity activeRateCodeModel = new MyDocsEntity();
				activeRateCodeModel.setUser_type(rs.getString("field_value"));
				return activeRateCodeModel;
			}
		});
		return activeRateCodesList;

	}

	/* Getting all the assigned docs */
	@Override
	public List<MyDocsEntity> getAssignedDocsList(int docid) {

		List<MyDocsEntity> assignedDocsList = jdbcTemplate.query(GET_ASSIGNED_DOCS_LIST_ONE,
				new RowMapper<MyDocsEntity>() {

					@Override
					public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						MyDocsEntity myDocsModel = new MyDocsEntity();
						myDocsModel.setComm_plan_desc(rs.getString("comm_plan"));
						myDocsModel.setUser_type(rs.getString("user_type"));
						myDocsModel.setEffective_dt(rs.getDate("effective_dt"));
						myDocsModel.setComm_plan_id(rs.getInt("comm_plan_id"));

						return myDocsModel;
					}
				}, new Object[] { docid });
		return assignedDocsList;

	}

	/* Getting all the assigned docs emp list */
	@Override
	public List<MyDocsEntity> getAssignedDocsEmpList(int docid, int comm_plan_id, String user_type,
			String effective_dt) {

		List<MyDocsEntity> assignedDocsList = jdbcTemplate.query(GET_ASSIGNED_DOCS_LIST_TWO,
				new RowMapper<MyDocsEntity>() {

					@Override
					public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						MyDocsEntity myDocsModel = new MyDocsEntity();
						myDocsModel.setEmployeeId(rs.getString("employee_id"));
						myDocsModel.setEmployeeName(rs.getString("name"));
						myDocsModel.setId(rs.getLong("comm_doc_id"));

						return myDocsModel;
					}
				}, new Object[] { docid, comm_plan_id, user_type, effective_dt });
		return assignedDocsList;

	}

	/* Delete assigned employee by id */
	@Override
	public int deleteAssignedEmployee(long id, String employeeId) throws IOException {
		logger.info(String.valueOf(id));
		logger.info(employeeId);
		String deleteQuery = "delete from c_com_doc_trans where comm_doc_id=? and employee_id=?";
		int role = jdbcTemplate.update(deleteQuery, id, employeeId);
		return role;
	}

	@Override
	public List<MyDocsEntity> getExcelAssignedDocsList(String doc_desc) {
		List<MyDocsEntity> assignedDocsList = jdbcTemplate.query(GET_ASSIGNED_DOCS_ALL_LIST,
				new RowMapper<MyDocsEntity>() {

					@Override
					public MyDocsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						MyDocsEntity myDocsModel = new MyDocsEntity();
						myDocsModel.setComm_plan_desc(rs.getString("comm_plan"));
						myDocsModel.setUser_type(rs.getString("user_type"));
						myDocsModel.setEffective_dt(rs.getDate("effective_dt"));
						myDocsModel.setEmployeeId(rs.getString("employee_id"));
						myDocsModel.setEmployeeName(rs.getString("name"));

						return myDocsModel;
					}
				}, new Object[] { doc_desc });
		System.out.println("assignedDocsList = " + assignedDocsList);
		return assignedDocsList;
	}

	/* Update document approval or rejected status */
	@Override
	public int submitStatus(String currentUser, String status, int docid) throws DuplicateRecordException {
		logger.info("currentUser = " + currentUser);
		Instant instant = Instant.now();
		logger.info("instant = " + instant);
		int ticketcnt = 0;
		String trackingid = myDocTransRepository.getTrackingId(docid);
		logger.info("trackingid = " + trackingid);

		String updateDocStatusQuery = "update c_com_doc_trans set approved_by=?, approved_on=(select now()), approved_status=? where comm_doc_trans_id=?";
		int doccnt = jdbcTemplate.update(updateDocStatusQuery, currentUser, status, docid);

		if (doccnt > 0) {
			String updateTicketQuery = "update t_workflow_details set approved_by=?, approved_dt=?, status=?, final_status=? where tracking_id=?";
			ticketcnt = jdbcTemplate.update(updateTicketQuery, currentUser, new Date(), status, status, trackingid);
		}

		return ticketcnt;
	}

}
