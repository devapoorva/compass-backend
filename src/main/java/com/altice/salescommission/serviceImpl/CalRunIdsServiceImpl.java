package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;
import static com.altice.salescommission.constants.ExceptionConstants.ID_NOT_FOUND;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.CalRunIdsEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.queries.CalRunIdsQueries;
import com.altice.salescommission.repository.CalRunIdsRepository;
import com.altice.salescommission.service.CalRunIdsService;

@Service
@Transactional
public class CalRunIdsServiceImpl extends AbstractBaseRepositoryImpl<CalRunIdsEntity, Long>
		implements CalRunIdsService, CalRunIdsQueries {
	
	private static final Logger logger = LoggerFactory.getLogger(CalRunIdsServiceImpl.class.getName());

	@Autowired
	private CalRunIdsRepository calRunIdsRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CalRunIdsServiceImpl(CalRunIdsRepository calRunIdsRepository) {
		super(calRunIdsRepository);
	}

	@Override
	public List<CalRunIdsEntity> getUserIdsDropDown() {
		List<CalRunIdsEntity> queriesList = jdbcTemplate.query(GET_USER_IDS, new RowMapper<CalRunIdsEntity>() {

			@Override
			public CalRunIdsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalRunIdsEntity calcManageEntity = new CalRunIdsEntity();
				calcManageEntity.setUserId(rs.getString("userid"));
				return calcManageEntity;
			}
		});

		return queriesList;
	}
	
	@Override
	public List<CalRunIdsEntity> getRunCtrlsDropDown() {
		List<CalRunIdsEntity> queriesList = jdbcTemplate.query(GET_RUN_CTRLS, new RowMapper<CalRunIdsEntity>() {

			@Override
			public CalRunIdsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalRunIdsEntity calcManageEntity = new CalRunIdsEntity();
				calcManageEntity.setRunCtrlId(rs.getInt("ccr_id"));
				calcManageEntity.setRunControlName(rs.getString("run_control_name"));
				return calcManageEntity;
			}
		});

		return queriesList;
	}

	@Override
	public CalRunIdsEntity addCalRunId(int cal_run_id, String description, String currentUser)
			throws DuplicateRecordException {

		CalRunIdsEntity calTypeEntity = null;
		Optional<CalRunIdsEntity> calTypeObj = Optional.ofNullable(calRunIdsRepository.findByCalRunId(cal_run_id));
		if (calTypeObj.isPresent()) {
			throw new DuplicateRecordException(String.valueOf(cal_run_id) + DUPLICATE_RECORD);
		} else {
			CalRunIdsEntity CalModel = new CalRunIdsEntity();
			CalModel.setCalRunId(cal_run_id);
			CalModel.setDescription(description);
			CalModel.setCreated_by(currentUser);
			CalModel.setCreated_dt(new Date());
			CalModel.setActive_flag("Active");
			calTypeEntity = calRunIdsRepository.save(CalModel);
		}
		return calTypeEntity;
	}

	@Override
	public CalRunIdsEntity updateCalRunId(long id, String description, String currentUser) throws IdNotFoundException {
		CalRunIdsEntity updateKpi = null;

		CalRunIdsEntity kpiObj = calRunIdsRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException(id + ID_NOT_FOUND));

		kpiObj.setDescription(description);
		kpiObj.setUpdated_by(currentUser);
		kpiObj.setUpdated_dt(new Date());
		updateKpi = calRunIdsRepository.save(kpiObj);
		return updateKpi;
	}

	@Override
	public List<CalRunIdsEntity> getCalRunIdList() {

		List<CalRunIdsEntity> calList = jdbcTemplate.query(GET_CAL_RUN_IDS, new RowMapper<CalRunIdsEntity>() {

			@Override
			public CalRunIdsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalRunIdsEntity activeRateCodeModel = new CalRunIdsEntity();
				activeRateCodeModel.setId(rs.getLong("crun_id"));
				String runStatus = checkCalRunIdStatus(rs.getInt("cal_run_id"));
				activeRateCodeModel.setCalRunId(rs.getInt("cal_run_id"));
				activeRateCodeModel.setDescription(rs.getString("description"));
				activeRateCodeModel.setActive_flag(rs.getString("active_flag"));
				activeRateCodeModel.setCreated_by(rs.getString("created_by"));
				activeRateCodeModel.setCreated_dt(rs.getDate("created_dt"));
				activeRateCodeModel.setUpdated_by(rs.getString("updated_by"));
				activeRateCodeModel.setUpdated_dt(rs.getDate("updated_dt"));
				activeRateCodeModel.setRun_status(runStatus);
				return activeRateCodeModel;
			}
		});

		return calList;
	}

	@Override
	public List<CalRunIdsEntity> getProcess(String userId,int runCtrlId, String startTime, String endTime) {
		
		
		int userId1 = 0;
		int runCtrlId1 = 0;
		
		if(userId.equals("0")) {
			userId1 = 1;
		}
		if(runCtrlId == 0) {
			runCtrlId1 = 1;
		}
		
		logger.info("userId1 = "+userId1);
		logger.info("userId = "+userId);
		logger.info("runCtrlId1 = "+runCtrlId1);
		logger.info("runCtrlId = "+runCtrlId);
		logger.info("startTime = "+startTime);
		logger.info("endTime = "+endTime);

		List<CalRunIdsEntity> calList = jdbcTemplate.query(GET_PROCESS_STATUS, new RowMapper<CalRunIdsEntity>() {

			@Override
			public CalRunIdsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalRunIdsEntity CalRunIdsEntityObj = new CalRunIdsEntity();
				CalRunIdsEntityObj.setId(rs.getLong("tcc_id"));
				CalRunIdsEntityObj.setStartTime(rs.getString("start_time"));
				CalRunIdsEntityObj.setEndTime(rs.getString("end_time"));
				CalRunIdsEntityObj.setProgress(rs.getString("status"));
				CalRunIdsEntityObj.setUserId(rs.getString("created_by"));
				CalRunIdsEntityObj.setRunControlName(rs.getString("run_control_name"));
				CalRunIdsEntityObj.setRunControlDesc(rs.getString("description"));
				CalRunIdsEntityObj.setUid(rs.getString("uid"));
				CalRunIdsEntityObj.setCalRunId(rs.getInt("cal_run_id"));
				CalRunIdsEntityObj.setComm_plan_id(rs.getInt("comm_plan_id"));
				return CalRunIdsEntityObj;
			}
		}, new Object[] { userId1,userId,runCtrlId1,runCtrlId, startTime, endTime });

		return calList;
	}

	// check status of cal run id
	private String checkCalRunIdStatus(int calrunid) {
		String status = "Not Started";
		List<String> getCalData = calRunIdsRepository.getCalendarData(calrunid);

		if (getCalData.isEmpty()) {
			status = "Not Started";
		} else {
			for (String getData : getCalData) {
				if (getData.split(",")[1].equals("N")) {
					status = "Completed";
				} else if (getData.split(",")[1].equals("Y")) {
					status = "Completed";

					LocalDate dateObj = LocalDate.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					String curdate = dateObj.format(formatter);
					System.out.println("curdate = " + curdate);

					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

					try {
						Date d1 = sdformat.parse(getData.split(",")[0]);
						Date d2 = sdformat.parse(curdate);
						if (d1.compareTo(d2) > 0) {
							//System.out.println("Date 1 occurs after Date 2");
							status = "Not Started";
						} else if (d1.compareTo(d2) < 0) {
							//System.out.println("Date 1 occurs before Date 2");
							status = "Inprogress";
						} else if (d1.compareTo(d2) == 0) {
							//System.out.println("Both dates are equal");
							status = "Inprogress";
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}
		return status;

	}

}
