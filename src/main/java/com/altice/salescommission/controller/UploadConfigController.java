package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.altice.salescommission.entity.UploadConfigEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.repository.UploadConfigRepository;
import com.altice.salescommission.service.UploadConfigService;
import com.altice.salescommission.utility.Commons;
import com.altice.salescommission.utility.HttpResponse;

@RestController
@RequestMapping("/api/salescomm/uploadsconfig/uploadtemplate")
public class UploadConfigController {

	private static final Logger logger = LoggerFactory.getLogger(UploadConfigController.class.getName());

	@Autowired
	UploadConfigService uploadConfigService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UploadConfigRepository uploadConfigRepository;

	@Autowired
	private NamedParameterJdbcTemplate NamedJdbcTemplate;

	@Autowired
	Commons comm;

	/* This method is used to get all templates */
	@GetMapping("/gettemplateslist")
	public ResponseEntity<List<UploadConfigEntity>> getTemplatesList() {
		List<UploadConfigEntity> myTemplatesList = uploadConfigService.getTemplatesList();
		return new ResponseEntity<>(myTemplatesList, OK);
	}

	/* This method is used to get all templates */
	@GetMapping("/getrecordtemplateslist")
	public ResponseEntity<List<UploadConfigEntity>> getTemplatesDropdown() {
		List<UploadConfigEntity> getTemplatesDropdown = uploadConfigService.getTemplatesDropdown();
		return new ResponseEntity<>(getTemplatesDropdown, OK);
	}

	/* This method is used to get all columns based on provided record */
	@GetMapping("/getcolumnslist/{recordName}")
	public ResponseEntity<List<UploadConfigEntity>> getColumnsList(@PathVariable("recordName") String recordName) {
		List<UploadConfigEntity> myDocsList = uploadConfigService.getColumnsList(recordName);
		return new ResponseEntity<>(myDocsList, OK);
	}

	/* This method is used to get all columns based on provided record */
	@GetMapping("/getcolumnsconfiglist/{recordName}/{name}")
	public ResponseEntity<List<UploadConfigEntity>> getColumnsConfigList(@PathVariable("recordName") String recordName,
			@PathVariable("name") String name) {
		List<UploadConfigEntity> myDocsList = uploadConfigService.getColumnsConfigList(recordName, name);
		return new ResponseEntity<>(myDocsList, OK);
	}

	/* This method is used to add template */
	@PostMapping("/addtemplate")
	public ResponseEntity<UploadConfigEntity> insertTemplate(
			@RequestBody List<UploadConfigEntity> uploadsTemplateEntity)
			throws IdNotFoundException, DuplicateRecordException {
		UploadConfigEntity insertTemplateObj = uploadConfigService.insertTemplate(uploadsTemplateEntity);
		// logger.info("insertTemplateObj = "+insertTemplateObj);
		return new ResponseEntity<>(insertTemplateObj, OK);
	}

	/* This method is used to get all columns based on provided record */
	@GetMapping("/getrecordslist/{recordName}")
	public ResponseEntity<LinkedHashSet<Map<String, String>>> getRecordsList(
			@PathVariable("recordName") String recordName) {
		LinkedHashSet<Map<String, String>> myDocsList = uploadConfigService.getRecordsList(recordName);
		return new ResponseEntity<>(myDocsList, OK);
	}

	/* This method is used to get all columns based on provided record */
	@GetMapping("/getcolumnseditlist/{recordName}")
	public ResponseEntity<List<UploadConfigEntity>> getColumnsEditList(@PathVariable("recordName") String recordName) {
		List<UploadConfigEntity> myDocsList = uploadConfigService.getColumnsEditList(recordName);
		return new ResponseEntity<>(myDocsList, OK);
	}

	/* This method is used to update template */
	@PostMapping("/updatetemplate")
	public ResponseEntity<UploadConfigEntity> updateTemplate(
			@RequestBody List<UploadConfigEntity> uploadsTemplateEntity) throws IdNotFoundException {
		UploadConfigEntity updateTemplateObj = uploadConfigService.updateTemplate(uploadsTemplateEntity);
		return new ResponseEntity<>(updateTemplateObj, OK);
	}

	@PostMapping("/uploadExcelData/{recordTemplate}")
	public ResponseEntity<Map<String, Object>> uploadExcelData(@RequestBody List<List<Object>> excelData,
			@PathVariable("recordTemplate") String recordTemplate) throws ParseException {
		//try {
			ResponseEntity<Map<String, Object>> result = uploadConfigService.uploadExcelData(excelData, recordTemplate);
			System.out.println("Result = " + result);
//			if (!result.equals("Success")) {
//				return comm.response(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + result);
//			}
//			return comm.response(OK, "Data Uploaded Successfully");
//		} catch (Exception exception) {
//			return comm.response(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
//		}
			return result;
	}

	@PostMapping("/uploadExcelDataNew/{recordTemplate}")
	public ResponseEntity<Map<String, Object>> uploadExcelFile(@RequestParam("file") MultipartFile file,
			@PathVariable("recordTemplate") String recordTemplate) {

		int rowCount = 0;
		int successCount = 0;
		int failureCount = 0;
		String failedRows = "";
		String finalFailedRows = "";
		String exceptionStr = "";

		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

			String sqlQuery = uploadConfigRepository.getSQLQuery(recordTemplate);
			Map<String, Object> parms = new LinkedHashMap<>();
			Sheet sheet = workbook.getSheetAt(0);
			rowCount = sheet.getPhysicalNumberOfRows(); // Get the number of rows in the sheet
			// System.out.println("rowCount = " + (rowCount - 1));

			for (Row row : sheet) {
				System.out.println("=============================");

				if (row.getRowNum() == 0) {
					continue; // skip header row
				}

				try {
					int k = 0;
					for (Cell cell : row) {
						System.out.println(cell.getCellType());
						switch (cell.getCellType()) {
						case STRING:
							System.out.println("STRING=" + cell.getStringCellValue());
							parms.put("param" + k, cell.getStringCellValue());
							failedRows = failedRows + cell.getStringCellValue() + ",";
							break;
						case NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) {
								System.out.println("NUMERIC IF=" + cell.getDateCellValue());
								parms.put("param" + k, cell.getDateCellValue());
								failedRows = failedRows + cell.getDateCellValue() + ",";
							} else {
								System.out.println("NUMERIC ELSE=" + cell.getNumericCellValue());
								parms.put("param" + k, cell.getNumericCellValue());
								failedRows = failedRows + cell.getNumericCellValue() + ",";
							}
							break;
						case BOOLEAN:
							System.out.println("BOOLEAN=" + cell.getBooleanCellValue());
							parms.put("param" + k, cell.getBooleanCellValue());
							failedRows = failedRows + cell.getBooleanCellValue() + ",";
							break;
						case FORMULA:
							System.out.println("FORMULA=" + cell.getCellFormula());
							parms.put("param" + k, cell.getCellFormula());
							failedRows = failedRows + cell.getCellFormula() + ",";
							break;
						default:
							System.out.println("Unknown cell type");
						}

						k++;
					} // for close

					// failedRows = failedRows + System.getProperty("line.separator");
					// System.out.println("failedRows = " + failedRows);

					logger.info("parms = " + parms);

					if (recordTemplate.equals("t_kpi_goal")) {
						String deleteQuery = "delete from t_kpi_goal where sc_emp_id=? and cal_run_id=? and kpi_id=? and comm_plan_id=?";
						jdbcTemplate.update(deleteQuery, parms.get("param0"), parms.get("param1"), parms.get("param2"),
								parms.get("param3"));
					} else if (recordTemplate.equals("t_adjustment_detail")) {
						String deleteQuery = "delete from t_adjustment_detail where sc_emp_id=? and cal_run_id=? and comm_plan_id=? and kpi_id=? ";
						jdbcTemplate.update(deleteQuery, parms.get("param0"), parms.get("param1"), parms.get("param2"),
								parms.get("param3"));
					}

					int count = NamedJdbcTemplate.update(sqlQuery, parms);
					System.out.println("count = " + count);
					if (count > 0) {
						failedRows = "";
						successCount++;
					}
					System.out.println("failedRows = " + failedRows);

				} catch (Exception e) {
					System.out.println("Exception = " + e.getMessage());
					String substring = "";

					String startWord = "ERROR:";
					String endWord = "Hint:";

					int startIndex = e.getMessage().indexOf(startWord);
					int endIndex = e.getMessage().indexOf(endWord);

					if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
						substring = e.getMessage().substring(startIndex, endIndex + endWord.length());
						substring = substring.substring(0, substring.length() - 5);

						System.out.println("Substring: " + substring);
					} else {
						System.out.println("Keywords not found or out of order.");
					}

					exceptionStr = substring;

					failedRows = failedRows.substring(0, failedRows.length() - 1);

					finalFailedRows = finalFailedRows + failedRows + " - " + exceptionStr
							+ System.getProperty("line.separator");
					System.out.println("finalFailedRows = " + finalFailedRows);

					failureCount++;
					failedRows = "";
				}
			} // main for close

		} catch (Exception e) {
			System.out.println("Exception = "
					+ ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage())));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}

		return ResponseEntity.ok(Map.of("rowCount", (rowCount - 1), "successCount", successCount, "failureCount",
				failureCount, "failedRows", finalFailedRows));
	}

}
