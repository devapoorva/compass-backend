package com.altice.salescommission.serviceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.ExcelHeadersEntity;
import com.altice.salescommission.queries.ExcelHeadersQueries;
import com.altice.salescommission.service.ExcelHeadersService;

@Transactional
@Service
public class ExcelHeadersServiceImpl implements ExcelHeadersService, ExcelHeadersQueries {
	private static final Logger logger = LoggerFactory.getLogger(ExcelHeadersServiceImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ExcelHeadersEntity> getKPIExcelHeadersList() {
		List<ExcelHeadersEntity> getKPIExcelHeadersList = jdbcTemplate.query(GET_KPI_HEADERS,
				new RowMapper<ExcelHeadersEntity>() {

					@Override
					public ExcelHeadersEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						ExcelHeadersEntity excelHeadersEntity = new ExcelHeadersEntity();

						excelHeadersEntity.setId(rs.getLong("rowid"));
						excelHeadersEntity.setPagename(rs.getString("pagename"));
						excelHeadersEntity.setName(rs.getString("name"));
						excelHeadersEntity.setDataKey(rs.getString("datakey"));
						excelHeadersEntity.setPosition(rs.getString("pos"));
						excelHeadersEntity.setIsSortable(rs.getString("issortable"));
						excelHeadersEntity.setIsView(rs.getString("isview"));
						excelHeadersEntity.setEdit(rs.getString("edit"));
						excelHeadersEntity.setStatus(rs.getString("status"));
						excelHeadersEntity.setExcelstatus(rs.getString("excelstatus"));
						excelHeadersEntity.setHeaderstatus(rs.getString("headerstatus"));
						
						return excelHeadersEntity;
					}
				});
		return getKPIExcelHeadersList;
	}
}
