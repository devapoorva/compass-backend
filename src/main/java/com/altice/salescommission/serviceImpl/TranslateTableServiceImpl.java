package com.altice.salescommission.serviceImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.TranslateMasterEntity;
import com.altice.salescommission.queries.TranslateTableQueries;
import com.altice.salescommission.repository.TranslateTableRepository;
import com.altice.salescommission.service.TranslateTableService;

@Service
@Transactional
public class TranslateTableServiceImpl extends AbstractBaseRepositoryImpl<TranslateMasterEntity, Long>
		implements TranslateTableService, TranslateTableQueries {
	@Autowired
	private TranslateTableRepository translateTableRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public TranslateTableServiceImpl(TranslateTableRepository translateTableRepository) {

		super(translateTableRepository);
	}

	/* This method is used to get all field name's */
	@Override
	public List<TranslateMasterEntity> getFieldNames() {
		List<TranslateMasterEntity> getFieldNamesList = jdbcTemplate.query(GET_FIELD_NAMES_LIST,
				(result, rowNum) -> new TranslateMasterEntity(result.getString("field_name")));
		return getFieldNamesList;
	}

	/* This method is used to create a new record on transaction table */
	@Override
	public TranslateMasterEntity addTrans(String field_name, String field_value, String description,
			String field_short_name, Date effective_date, String currentUser) {

		TranslateMasterEntity trans = new TranslateMasterEntity();
		trans.setField_name(field_name);
		trans.setField_value(field_value);
		trans.setDescription(description);
		trans.setField_short_name(field_short_name);
		trans.setEffective_date(effective_date);
		trans.setCreated_by(currentUser);
		trans.setCreated_dt(new Date());
		trans.setEffective_status("Y");
		translateTableRepository.save(trans);
		return trans;
	}

	/* Getting all the records of transaction table */
	@Override
	public List<TranslateMasterEntity> getTranslateTablelist() {

		List<TranslateMasterEntity> transValuesList = jdbcTemplate.query(GET_NAMES_LIST,
				new RowMapper<TranslateMasterEntity>() {

					@Override
					public TranslateMasterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						TranslateMasterEntity transModel = new TranslateMasterEntity();
						transModel.setField_name(rs.getString("field_name"));
						return transModel;
					}
				});
		return transValuesList;

	}

	/* Getting all the records of transaction table for excel */
	@Override
	public List<TranslateMasterEntity> getExcelTranslateTablelist() {

		List<TranslateMasterEntity> transValuesList = jdbcTemplate.query(GET_NAMES_LIST_EXCEL,
				new RowMapper<TranslateMasterEntity>() {

					@Override
					public TranslateMasterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						TranslateMasterEntity transModel = new TranslateMasterEntity();
						transModel.setField_name(rs.getString("field_name"));
						transModel.setField_value(rs.getString("field_value"));
						transModel.setDescription(rs.getString("description"));
						transModel.setField_short_name(rs.getString("field_short_name"));
						transModel.setEffective_date(rs.getDate("effective_date"));
						transModel.setEffective_status(rs.getString("effective_status"));
						return transModel;
					}
				});
		return transValuesList;

	}

	/* Getting all the records of transaction table */
	@Override
	public List<TranslateMasterEntity> getFieldValuesList(String field_name) {
		System.out.println("field_name = " + field_name);

		List<TranslateMasterEntity> transValuesList = jdbcTemplate.query(GET_SHORT_NAMES_LIST,
				new RowMapper<TranslateMasterEntity>() {

					@Override
					public TranslateMasterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						TranslateMasterEntity transModel = new TranslateMasterEntity();
						transModel.setId(rs.getLong("translate_id"));
						transModel.setField_value(rs.getString("field_value"));
						transModel.setDescription(rs.getString("description"));
						transModel.setField_short_name(rs.getString("field_short_name"));
						transModel.setEffective_date(rs.getDate("effective_date"));
						transModel.setEffective_status(rs.getString("effective_status"));
						transModel.setEdate(rs.getString("edate"));
						return transModel;
					}

				}, new Object[] { field_name });
		return transValuesList;

	}

	/* Update transaction table's field name */
	@Override
	public int updateFieldName(String field_name, String fname) throws IOException {

		String updateQuery = "update c_translate_master set field_name=? where field_name=?";
		int role = jdbcTemplate.update(updateQuery, field_name, fname);

		return role;
	}

	/* Update transaction table */
	@Override
	public int updateFieldValue(long id, String field_value, String description, String field_short_name,
			Date effective_date, String effective_status, String currentUser) throws IOException {
		String updateQuery = "update c_translate_master set field_value=?,description=?,field_short_name=?,effective_date=?,effective_status=?,updated_by=?,updated_dt=? where translate_id=?";
		int role = jdbcTemplate.update(updateQuery, field_value, description, field_short_name, effective_date,
				effective_status, currentUser, new Date(), id);

		return role;
	}

}
