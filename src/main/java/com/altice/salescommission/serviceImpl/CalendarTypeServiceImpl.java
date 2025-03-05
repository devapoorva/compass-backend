package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;
import static com.altice.salescommission.constants.ExceptionConstants.ID_NOT_FOUND;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.CalendarEntity;
import com.altice.salescommission.entity.CalendarTypeEntity;
import com.altice.salescommission.entity.KpiMasterEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.exception.ResourceNotFoundException;
import com.altice.salescommission.queries.CalendarTypeQueries;
import com.altice.salescommission.repository.CalendarTypeRepository;
import com.altice.salescommission.service.CalendarTypeService;

@Service
@Transactional
public class CalendarTypeServiceImpl extends AbstractBaseRepositoryImpl<CalendarTypeEntity, Long>
implements CalendarTypeService,CalendarTypeQueries {

	@Autowired
	private CalendarTypeRepository calendarTypeRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CalendarTypeServiceImpl(CalendarTypeRepository calendarTypeRepository) {

		super(calendarTypeRepository);
	}
	
	@Override
	public CalendarTypeEntity addCalendarType(String calendar_type, String currentUser) throws DuplicateRecordException {
		CalendarTypeEntity calTypeEntity = null;
		
		Optional<CalendarTypeEntity> calTypeObj = Optional.ofNullable(calendarTypeRepository.findByCalendarTypeIgnoreCase(calendar_type));
		if (calTypeObj.isPresent()) {
			throw new DuplicateRecordException(calendar_type + DUPLICATE_RECORD);
		} else {
		CalendarTypeEntity CalModel = new CalendarTypeEntity();
		CalModel.setCalendarType(calendar_type);
		CalModel.setCreated_by(currentUser);
		CalModel.setCreated_dt(new Date());
		CalModel.setActive_flag("Active");
		calTypeEntity = calendarTypeRepository.save(CalModel);
		}
		return calTypeEntity;
	}
	
	@Override
	public List<CalendarTypeEntity> getCalendarList() {

		List<CalendarTypeEntity> calList = jdbcTemplate.query(GET_CALENDAR_TYPES, new RowMapper<CalendarTypeEntity>() {

			@Override
			public CalendarTypeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalendarTypeEntity activeRateCodeModel = new CalendarTypeEntity();
				activeRateCodeModel.setId(rs.getLong("ctype_rowid"));
				activeRateCodeModel.setCalendarType(rs.getString("calendar_type"));
				activeRateCodeModel.setActive_flag(rs.getString("active_flag"));
				activeRateCodeModel.setCreated_dt(rs.getDate("created_dt"));
				activeRateCodeModel.setUpdated_dt(rs.getDate("updated_dt"));
				return activeRateCodeModel;
			}
		});

		return calList;
	}

	@Override
	public CalendarTypeEntity updateCalendarType(long id,String calendar_type, String currentUser, String active_flag) throws IdNotFoundException {
		CalendarTypeEntity updateKpi = null;

		CalendarTypeEntity kpiObj = calendarTypeRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException(id + ID_NOT_FOUND));

		kpiObj.setCalendarType(calendar_type);
		kpiObj.setActive_flag(active_flag);
		kpiObj.setUpdated_by(currentUser);
		kpiObj.setUpdated_dt(new Date());
		updateKpi = calendarTypeRepository.save(kpiObj);
		return updateKpi;
	}
	
	/* This method is used to update multiple calc status to active */
	@Override
	public CalendarTypeEntity updateStatusToActive(List<CalendarTypeEntity> calendarTypeEntity) {
		CalendarTypeEntity updateStatus = null;
		for (CalendarTypeEntity cModel : calendarTypeEntity) {
			CalendarTypeEntity cStatus = calendarTypeRepository.findById(cModel.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Id not found" + cModel.getId()));
			cStatus.setActive_flag("Active");
			cStatus.setUpdated_dt(new Date());
			cStatus.setUpdated_by(cModel.getLoggedInUser());
			updateStatus = calendarTypeRepository.save(cStatus);

		}

		return updateStatus;
	}

	/* This method is used to update multiple calc status to inactive */
	@Override
	public CalendarTypeEntity updateStatusToInactive(List<CalendarTypeEntity> calendarTypeEntity) {

		CalendarTypeEntity updateStatus = null;
		for (CalendarTypeEntity cModel : calendarTypeEntity) {
			CalendarTypeEntity cStatus = calendarTypeRepository.findById(cModel.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Id not found" + cModel.getId()));
			cStatus.setActive_flag("Inactive");
			cStatus.setUpdated_dt(new Date());
			cStatus.setUpdated_by(cModel.getLoggedInUser());
			updateStatus = calendarTypeRepository.save(cStatus);

		}

		return updateStatus;
	}

}
