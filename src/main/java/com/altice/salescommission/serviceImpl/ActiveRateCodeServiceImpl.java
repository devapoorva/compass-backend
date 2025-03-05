package com.altice.salescommission.serviceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.ActiveRateCodeEntity;
import com.altice.salescommission.queries.ActiveRateCodeQueries;
import com.altice.salescommission.repository.ActiveRateCodeRepository;
import com.altice.salescommission.service.ActiveRateCodeService;

@Service
@Transactional
public class ActiveRateCodeServiceImpl extends AbstractBaseRepositoryImpl<ActiveRateCodeEntity, Long>
		implements ActiveRateCodeService, ActiveRateCodeQueries {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ActiveRateCodeServiceImpl(ActiveRateCodeRepository rateCodeRepository) {
		super(rateCodeRepository);
	}
	

	@Override
	public List<ActiveRateCodeEntity> getProducts() {

		List<ActiveRateCodeEntity> activeRateCodesList = jdbcTemplate.query(GET_PRODUCTS,
				new RowMapper<ActiveRateCodeEntity>() {

					@Override
					public ActiveRateCodeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						ActiveRateCodeEntity activeRateCodeModel = new ActiveRateCodeEntity();
						activeRateCodeModel.setProductId(rs.getLong("product_id"));
						activeRateCodeModel.setProductName(rs.getString("product"));
						return activeRateCodeModel;
					}
				});
		return activeRateCodesList;

	}

	@Override
	public List<ActiveRateCodeEntity> getSalesChannels() {
		List<ActiveRateCodeEntity> activeRateCodesList = jdbcTemplate.query(GET_SALES_CHANNELS,
				new RowMapper<ActiveRateCodeEntity>() {

					@Override
					public ActiveRateCodeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						ActiveRateCodeEntity activeRateCodeModel = new ActiveRateCodeEntity();
						activeRateCodeModel.setSalesChannelDesc(rs.getString("salesChannelDesc"));
						activeRateCodeModel.setSalesChannelFieldValue(rs.getString("salesChannelFieldValue"));
						return activeRateCodeModel;
					}
				});
		return activeRateCodesList;
	}

	@Override
	public List<ActiveRateCodeEntity> getCorps() {

		List<ActiveRateCodeEntity> corpsList = jdbcTemplate.query(GET_CORPS, new RowMapper<ActiveRateCodeEntity>() {

			@Override
			public ActiveRateCodeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				ActiveRateCodeEntity activeRateCodeModel = new ActiveRateCodeEntity();
				activeRateCodeModel.setCorp(rs.getInt("corp_id"));
				return activeRateCodeModel;
			}
		});
		return corpsList;

	}

	@Override
	public List<ActiveRateCodeEntity> getActiveCodeDetails(String corps, String sc, int proid, Date validdt) {

		String str[] = corps.split(",");
		List<ActiveRateCodeEntity> corpsList = null;
		ActiveRateCodeEntity activeRateCodeModel = new ActiveRateCodeEntity();

		for (int i = 0; i < str.length; i++) {

			corpsList = jdbcTemplate.query(GET_RATE_CODE_DETAILS, new RowMapper<ActiveRateCodeEntity>() {

				@Override
				public ActiveRateCodeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					activeRateCodeModel.setCorp(rs.getInt("corp"));
					activeRateCodeModel.setRcode(rs.getString("rcode"));
					activeRateCodeModel.setRdesc(rs.getString("rdesc"));
					activeRateCodeModel.setValid_from_dt(rs.getDate("valid_from_dt"));
					activeRateCodeModel.setValid_to_dt(rs.getDate("valid_to_dt"));
					return activeRateCodeModel;
				}
			}, new Object[] { proid, sc, validdt, Integer.parseInt(str[i]) });

		}
		return corpsList;
	}

}
