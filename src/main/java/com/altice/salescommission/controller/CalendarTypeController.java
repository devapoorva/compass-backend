package com.altice.salescommission.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altice.salescommission.entity.CalendarTypeEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.service.CalendarTypeService;

@RestController
@RequestMapping("/api/salescomm/config/calendartype")
public class CalendarTypeController {
	@Autowired
	CalendarTypeService calendarTypeService;
	
	/* This method is used to create a new KPI */
	@PostMapping("/addcalendartype")
	public ResponseEntity<CalendarTypeEntity> addCalendarType(@RequestParam("calendar_type") String calendar_type,
			@RequestParam("currentUser") String currentUser) throws DuplicateRecordException {
		System.out.println("current_user = "+currentUser);
		CalendarTypeEntity calendarTypeEntity = calendarTypeService.addCalendarType(calendar_type,currentUser);
		return new ResponseEntity<>(calendarTypeEntity, OK);
	}
	
	/* This method is used to create a new KPI */
	@PostMapping("/updatecalendartype")
	public ResponseEntity<CalendarTypeEntity> updateCalendarType(@RequestParam("id") long id,@RequestParam("calendar_type") String calendar_type,@RequestParam("active_flag") String active_flag,
			@RequestParam("currentUser") String currentUser) throws DuplicateRecordException, IdNotFoundException {
		System.out.println("current_user = "+currentUser);
		CalendarTypeEntity calendarTypeEntity = calendarTypeService.updateCalendarType(id,calendar_type,currentUser,active_flag);
		return new ResponseEntity<>(calendarTypeEntity, OK);
	}
	
	/* Get the list of calendars */
	@GetMapping("/getcalendarlist")
	public ResponseEntity<List<CalendarTypeEntity>> getCalendarList() {
		List<CalendarTypeEntity> getCalendarList = calendarTypeService.getCalendarList();
		return new ResponseEntity<>(getCalendarList, OK);
	}
	
	/* This method is used to update active flag to active */
	@PutMapping("/updatestatustoactive")
	public ResponseEntity<CalendarTypeEntity> updateStatusToActive(@RequestBody List<CalendarTypeEntity> calendarTypeEntity) {
		CalendarTypeEntity updateKpiStatusToActive = calendarTypeService.updateStatusToActive(calendarTypeEntity);
		return new ResponseEntity<>(updateKpiStatusToActive, OK);
	}

	/* This method is used to update active flag to inactive */
	@PutMapping("/updatestatustoinactive")
	public ResponseEntity<CalendarTypeEntity> updateStatusToInactive(@RequestBody List<CalendarTypeEntity> calendarTypeEntity) {
		CalendarTypeEntity updateKpiStatusToInactive = calendarTypeService.updateStatusToInactive(calendarTypeEntity);
		return new ResponseEntity<>(updateKpiStatusToInactive, OK);
	}
}
