package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.altice.salescommission.entity.MyDocTransEntity;
import com.altice.salescommission.entity.MyDocsEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.message.ResponseMessage;
import com.altice.salescommission.service.MyDocsService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/upload/mydocs")
public class MyDocsController {

	public static final String ASSIGNED_EMPLOYEE_DELETED_SUCCESSFULLY = "Assigned employee deleted successfully";

	@Autowired
	MyDocsService myDocsService;

	@Autowired
	Commons comm;

	/* This method is used to assign document to selected employees */
	@PutMapping("/assigndoctoemployees")
	public ResponseEntity<MyDocTransEntity> assignDocToEmployees(@RequestBody List<MyDocTransEntity> myDocsModel)
			throws IdNotFoundException {
		MyDocTransEntity assignDocs = myDocsService.assignDocToEmployees(myDocsModel);
		return new ResponseEntity<>(assignDocs, OK);
	}

	/* Save the record to comm_doc_mgmt table */
	@PostMapping("/savedocs")
	public ResponseEntity<MyDocTransEntity> saveDocs(@RequestBody List<MyDocTransEntity> myDocsModel)
			throws IOException, ParseException {
		MyDocTransEntity myDocs = myDocsService.saveDocs(myDocsModel);
		return new ResponseEntity<>(myDocs, OK);
	}

	@PutMapping("/updatesingledoc")
	public ResponseEntity<ResponseMessage> updateSingleFile(@RequestParam("file") MultipartFile file,
			@RequestParam("id") Long id) {
		String message = "";
		try {
			myDocsService.updateSingleFile(file, id);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	/* This method is used to create a new KPI */
	@PostMapping("/submitstatus")
	public ResponseEntity<HttpResponse> submitStatus(@RequestParam("currentUser") String currentUser,
			@RequestParam("status") String status, @RequestParam("docid") int docid) throws DuplicateRecordException {
		try {
			int result = myDocsService.submitStatus(currentUser, status, docid);
			if (result == 0) {
				return comm.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to approve");
			}
			return comm.response(OK, "Approved Successfully");
		} catch (Exception exception) {
			// logger.error("Error: "+exception);
			return comm.response(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
		}
	}

	/* Update the existing commission plan record to comm_plan_master table */
	@PutMapping("/updatedocs")
	public ResponseEntity<MyDocsEntity> updateDocs(@RequestBody List<MyDocsEntity> myDocsModel) throws IOException, ParseException {
		MyDocsEntity myDocs = myDocsService.updateDocs(myDocsModel);
		return new ResponseEntity<>(myDocs, OK);
	}

	@PostMapping("/uploaddocs")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("doc_name") String doc_name, @RequestParam("doc_desc") String doc_desc,
			@RequestParam("comm_plan_id") int comm_plan_id, @RequestParam("current_user") String current_user, @RequestParam("effective_dt") String effective_dt) {
		String message = "";
		try {
			myDocsService.uploadDocs(file, doc_name, doc_desc, comm_plan_id, current_user,effective_dt);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	/* This method is used to get all user documents */
	@GetMapping("/getmydocslist")
	public ResponseEntity<List<MyDocsEntity>> getMyDocsList() {
		List<MyDocsEntity> myDocsList = myDocsService.getMyDocsList();
		return new ResponseEntity<>(myDocsList, OK);
	}

	/* This method is used to get all supervisor documents */
	@GetMapping("/getsupdocslist/{employeeid}")
	public ResponseEntity<List<MyDocsEntity>> getSupDocsList(@PathVariable String employeeid) {
		List<MyDocsEntity> myDocsList = myDocsService.getSupDocsList(employeeid);
		return new ResponseEntity<>(myDocsList, OK);
	}

	/* This method is used to get all documents */
	@GetMapping("/getmydocslistbysaleschannel/{saleschannel}")
	public ResponseEntity<List<MyDocsEntity>> getmydocslistBySaleschannel(@PathVariable String saleschannel) {
		List<MyDocsEntity> myDocsList = myDocsService.getmydocslistBySaleschannel(saleschannel);
		return new ResponseEntity<>(myDocsList, OK);
	}

	/* This method is used to get all documents */
	@GetMapping("/getempdocslist/{employeeid}")
	public ResponseEntity<List<MyDocsEntity>> getEmpDocsList(@PathVariable String employeeid) {
		List<MyDocsEntity> myDocsList = myDocsService.getEmpDocsList(employeeid);
		return new ResponseEntity<>(myDocsList, OK);
	}
	
	/* This method is used to get all documents */
	@GetMapping("/getemployeedocslist/{employeeid}")
	public ResponseEntity<List<MyDocsEntity>> getEmployeeDocsList(@PathVariable String employeeid) {
		List<MyDocsEntity> myDocsList = myDocsService.getEmployeeDocsList(employeeid);
		return new ResponseEntity<>(myDocsList, OK);
	}

	/* This method is used to get all documents based on empId */
	@GetMapping("/filteruserdocbyemp/{empId}")
	public ResponseEntity<List<MyDocTransEntity>> filterUserDocByEmp(@PathVariable String empId) {
		List<MyDocTransEntity> myDocsList = myDocsService.filterUserDocByEmp(empId);
		return new ResponseEntity<>(myDocsList, OK);
	}

	/* Getting employee information based on selected user type */
	@GetMapping("/getemployeelist/{val}/{user_type}/{doc_desc}/{comPlanId}")
	public ResponseEntity<List<MyDocsEntity>> getEmployeeList(@PathVariable String val, @PathVariable String user_type,
			@PathVariable("doc_desc") String doc_desc, @PathVariable("comPlanId") int comPlanId) {
		List<MyDocsEntity> getEmployeeList = myDocsService.getEmployeeList(val, user_type, doc_desc, comPlanId);
		return new ResponseEntity<>(getEmployeeList, OK);
	}

	/* Get the Active list of commission plans */
	@GetMapping("/getsaleschannelslist")
	public ResponseEntity<List<MyDocsEntity>> getSaleschannelsList() {
		List<MyDocsEntity> saleschannelsList = myDocsService.getSaleschannelsList();
		return new ResponseEntity<>(saleschannelsList, OK);
	}

	/* Get the Active list of commission plans */
	@GetMapping("/getcommplanlist/{saleschannel}")
	public ResponseEntity<List<MyDocsEntity>> getCommPlanList(@PathVariable("saleschannel") String saleschannel) {
		List<MyDocsEntity> commPlanList = myDocsService.getCommPlanList(saleschannel);
		return new ResponseEntity<>(commPlanList, OK);
	}

	/* Get the Active list of commission plans */
	@GetMapping("/getcommplandesclist")
	public ResponseEntity<List<MyDocsEntity>> getCommPlanDescList() {
		List<MyDocsEntity> commPlanList = myDocsService.getCommPlanDescList();
		return new ResponseEntity<>(commPlanList, OK);
	}

	/* Get the Active list of roles */
	@GetMapping("/getusertypeslist")
	public ResponseEntity<List<MyDocsEntity>> getUserTypesList() {
		List<MyDocsEntity> userTypesList = myDocsService.getUserTypesList();
		return new ResponseEntity<>(userTypesList, OK);
	}

	/* Get the Assigned Docs List */
	@GetMapping("/getassigneddocslist/{docid}")
	public ResponseEntity<List<MyDocsEntity>> getAssignedDocsList(@PathVariable("docid") int docid) {
		List<MyDocsEntity> assignedDocsList = myDocsService.getAssignedDocsList(docid);
		return new ResponseEntity<>(assignedDocsList, OK);
	}

	/* Get the Assigned Docs List of assigned employees */
	@GetMapping("/getassigneddocsexpandlist/{docid}/{comm_plan_id}/{user_type}/{effective_dt}")
	public ResponseEntity<List<MyDocsEntity>> getAssignedDocsEmpList(@PathVariable("docid") int docid,
			@PathVariable("comm_plan_id") int comm_plan_id, @PathVariable("user_type") String user_type, @PathVariable("effective_dt") String effective_dt) {
		List<MyDocsEntity> assignedEmpDocsList = myDocsService.getAssignedDocsEmpList(docid, comm_plan_id, user_type,effective_dt);
		return new ResponseEntity<>(assignedEmpDocsList, OK);
	}

	/* Get the Assigned Docs List for excel */
	@GetMapping("/getexcelassigneddocslist/{doc_desc}")
	public ResponseEntity<List<MyDocsEntity>> getExcelAssignedDocsList(@PathVariable("doc_desc") String doc_desc) {
		List<MyDocsEntity> assignedDocsList = myDocsService.getExcelAssignedDocsList(doc_desc);
		return new ResponseEntity<>(assignedDocsList, OK);
	}

	/* This method is used to delete assigned employee */
	@DeleteMapping("/deleteemp/{id}/{employeeId}")
	public ResponseEntity<HttpResponse> deleteAssignedEmployee(@PathVariable("id") Long id,
			@PathVariable("employeeId") String employeeId) throws IOException {
		myDocsService.deleteAssignedEmployee(id, employeeId);
		return comm.response(OK, ASSIGNED_EMPLOYEE_DELETED_SUCCESSFULLY);
	}

}
