package com.altice.salescommission.serviceImpl;

import static com.altice.salescommission.constants.ExceptionConstants.DUPLICATE_RECORD;
import static com.altice.salescommission.constants.ExceptionConstants.ID_NOT_FOUND;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.altice.salescommission.entity.UploadConfigEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.model.MobileFeedModel;
import com.altice.salescommission.queries.MobileFeedQueries;
import com.altice.salescommission.repository.MobileFeedRepository;
import com.altice.salescommission.service.MobileFeedService;

@Service
@Transactional
public class MobileFeedServiceImpl implements MobileFeedService, MobileFeedQueries {

	private static final Logger logger = LoggerFactory.getLogger(MobileFeedServiceImpl.class.getName());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MobileFeedRepository mobileFeedRepository;

	@Override
	public List<MobileFeedModel> getMobileFeedResults(MobileFeedModel mobileFeedModel) {
		logger.info("mobileFeedModel = " + mobileFeedModel);

		logger.info("getTransaction_complete_from_date = " + mobileFeedModel.getTransaction_complete_from_date());
		logger.info("getTransaction_complete_to_date = " + mobileFeedModel.getTransaction_complete_to_date());
		logger.info("getAccount_number = " + mobileFeedModel.getAccount_number());

		String accnum = "";
		int accnum1 = 0;
		String fname = "";
		int fname1 = 0;
		String lname = "";
		int lname1 = 0;

		String subnum = "";
		int subnum1 = 0;
		String msisdn = "";
		int msisdn1 = 0;

		if (mobileFeedModel.getAccount_number().equals("")) {
			accnum = "0";
			accnum1 = 1;
		} else {
			accnum = mobileFeedModel.getAccount_number();
			accnum1 = 0;
		}

		if (mobileFeedModel.getFirst_name().equals("")) {
			fname = "0";
			fname1 = 1;
		} else {
			fname = mobileFeedModel.getFirst_name();
			fname1 = 0;
		}

		if (mobileFeedModel.getLast_name().equals("")) {
			lname = "0";
			lname1 = 1;
		} else {
			lname = mobileFeedModel.getLast_name();
			lname1 = 0;
		}

		if (mobileFeedModel.getSubscriber_number().equals("")) {
			subnum = "0";
			subnum1 = 1;
		} else {
			subnum = mobileFeedModel.getSubscriber_number();
			subnum1 = 0;
		}

		if (mobileFeedModel.getMsisdn().equals("")) {
			msisdn = "0";
			msisdn1 = 1;
		} else {
			msisdn = mobileFeedModel.getMsisdn();
			msisdn1 = 0;
		}

		List<MobileFeedModel> mobileFeedModelList = jdbcTemplate.query(GET_MOBILE_FEED_RESULTS,
				new RowMapper<MobileFeedModel>() {
					@Override
					public MobileFeedModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						MobileFeedModel mobObj = new MobileFeedModel();

						mobObj.setMob_feed_id(rs.getLong("mob_feed_id"));
						mobObj.setPr_status_id(rs.getInt("pr_status_id"));
						mobObj.setTransaction_type_name(rs.getString("transaction_type_name"));
						mobObj.setTransaction_complete_date(rs.getString("transaction_complete_date"));
						mobObj.setAccount_start_date(rs.getString("account_start_date"));
						mobObj.setLine_add_create_datetime(rs.getString("line_add_create_datetime"));
						mobObj.setLine_disco_create_datetime(rs.getString("line_disco_create_datetime"));
						mobObj.setAccount_number(rs.getString("account_number"));
						mobObj.setFirst_name(rs.getString("first_name"));
						mobObj.setLast_name(rs.getString("last_name"));
						mobObj.setEmployee_id(rs.getString("employee_id"));

						mobObj.setAccount_sales_rep_id(rs.getString("account_sales_rep_id"));
						mobObj.setSubscriber_number(rs.getString("subscriber_number"));
						mobObj.setMsisdn(rs.getString("msisdn"));
						mobObj.setOrder_id(rs.getString("order_id"));
						mobObj.setLine_status(rs.getString("line_status"));
						mobObj.setDayslineaddedfromacctstart(rs.getString("dayslineaddedfromacctstart"));
						mobObj.setDays_line_active(rs.getString("days_line_active"));
						mobObj.setPlan_description(rs.getString("plan_description"));
						mobObj.setFixed_line_cust(rs.getString("fixed_line_cust"));

						mobObj.setTrans_type(rs.getString("trans_type"));
						mobObj.setElig_revenue(rs.getDouble("elig_revenue"));
						mobObj.setSim_activation_date(rs.getString("sim_activation_date"));
						mobObj.setLine_add_status(rs.getString("line_add_status"));
						mobObj.setLine_disco_status(rs.getString("line_disco_status"));
						mobObj.setService_plan(rs.getString("service_plan"));
						mobObj.setTotal_count(rs.getInt("total_count"));
						mobObj.setLine_sales_rep_id(rs.getString("line_sales_rep_id"));

						return mobObj;
					}
				},
				new Object[] { mobileFeedModel.getTransaction_complete_from_date(),
						mobileFeedModel.getTransaction_complete_to_date(), accnum1, accnum, fname1, fname, lname1,
						lname, subnum1, subnum, msisdn1, msisdn });
		return (List<MobileFeedModel>) mobileFeedModelList;
	}

	@Override
	public List<MobileFeedModel> getMobileFeedDataById(int mobfeedid) {
		logger.info("mobfeedid = " + mobfeedid);

		List<MobileFeedModel> mobileFeedModelList = jdbcTemplate.query(GET_MOBILE_FEED_RESULTS_BY_ID,
				new RowMapper<MobileFeedModel>() {
					@Override
					public MobileFeedModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						MobileFeedModel mobObj = new MobileFeedModel();

						mobObj.setMob_feed_id(rs.getLong("mob_feed_id"));
						mobObj.setPr_status_id(rs.getInt("pr_status_id"));
						mobObj.setTransaction_type_name(rs.getString("transaction_type_name"));
						mobObj.setTransaction_complete_date(rs.getString("transaction_complete_date"));
						mobObj.setAccount_start_date(rs.getString("account_start_date"));
						mobObj.setLine_add_create_datetime(rs.getString("line_add_create_datetime"));
						mobObj.setLine_disco_create_datetime(rs.getString("line_disco_create_datetime"));
						mobObj.setAccount_number(rs.getString("account_number"));
						mobObj.setFirst_name(rs.getString("first_name"));
						mobObj.setLast_name(rs.getString("last_name"));
						mobObj.setEmployee_id(rs.getString("employee_id"));

						mobObj.setAccount_sales_rep_id(rs.getString("account_sales_rep_id"));
						mobObj.setSubscriber_number(rs.getString("subscriber_number"));
						mobObj.setMsisdn(rs.getString("msisdn"));
						mobObj.setOrder_id(rs.getString("order_id"));
						mobObj.setLine_status(rs.getString("line_status"));
						mobObj.setDayslineaddedfromacctstart(rs.getString("dayslineaddedfromacctstart"));
						mobObj.setDays_line_active(rs.getString("days_line_active"));
						mobObj.setPlan_description(rs.getString("plan_description"));
						mobObj.setFixed_line_cust(rs.getString("fixed_line_cust"));

						mobObj.setTrans_type(rs.getString("trans_type"));
						mobObj.setElig_revenue(rs.getDouble("elig_revenue"));
						mobObj.setSim_activation_date(rs.getString("sim_activation_date"));
						mobObj.setLine_add_status(rs.getString("line_add_status"));
						mobObj.setLine_disco_status(rs.getString("line_disco_status"));
						mobObj.setService_plan(rs.getString("service_plan"));
						mobObj.setTotal_count(rs.getInt("total_count"));
						mobObj.setLine_sales_rep_id(rs.getString("line_sales_rep_id"));

						return mobObj;
					}
				}, new Object[] { mobfeedid });
		return (List<MobileFeedModel>) mobileFeedModelList;

	}

	@Override
	public Map<String, Boolean> updateMobileFeed(MobileFeedModel feedModel) {
		logger.info("feedModel = " + feedModel);
		Map<String, Boolean> response = new HashMap<>();
		try {

			String ls = "";

			if (feedModel.getLine_status() == null) {
				logger.info("Inside getLine_status IF");
				ls = "";
			} else {
				logger.info("Inside getLine_status ELSE");
				ls = feedModel.getLine_status();
			}
			logger.info("Inside feedModel getLast_modified_by = " + feedModel.getElig_revenue());
			int count = jdbcTemplate.update("UPDATE t_mob_sales_data set transaction_type_name='"
					+ feedModel.getTransaction_type_name() + "',pr_status_id=1,trans_type='" + feedModel.getTrans_type()
					+ "',account_number='" + feedModel.getAccount_number() + "',last_modified_by='" + feedModel.getLast_modified_by() + "',last_modified_on='"+new Date()+"'," + "first_name='"
					+ feedModel.getFirst_name() + "',last_name='" + feedModel.getLast_name() + "'," + "employee_id='"
					+ feedModel.getEmployee_id() + "'," + "account_sales_rep_id='" + feedModel.getAccount_sales_rep_id()
					+ "',subscriber_number='" + feedModel.getSubscriber_number() + "'," + "transaction_complete_date='"
					+ feedModel.getTransaction_complete_date() + "',account_start_date='"
					+ feedModel.getAccount_start_date() + "', " + "line_add_create_datetime='"
					+ feedModel.getLine_add_create_datetime() + "',line_disco_create_datetime='"
					+ feedModel.getLine_disco_create_datetime() + "'," + "msisdn='" + feedModel.getMsisdn()
					+ "',order_id='" + feedModel.getOrder_id() + "',line_status='" + feedModel.getLine_status() + "',"
					+ "dayslineaddedfromacctstart='" + feedModel.getDayslineaddedfromacctstart()
					+ "',days_line_active='" + feedModel.getDays_line_active() + "'," + "plan_description='"
					+ feedModel.getPlan_description() + "',fixed_line_cust='" + feedModel.getFixed_line_cust() + "',"
					+ "elig_revenue='" + feedModel.getElig_revenue() + "',sim_activation_date='"
					+ feedModel.getSim_activation_date() + "',line_add_status='" + feedModel.getLine_add_status() + "',"
					+ "line_disco_status='" + feedModel.getLine_disco_status() + "',service_plan='"
					+ feedModel.getService_plan() + "',total_count='" + feedModel.getTotal_count() + "',"
					+ "line_sales_rep_id='" + ls + "' " + "where mob_feed_id='" + feedModel.getMob_feed_id() + "'");
			logger.info("count = " + count);

			if (count > 0) {
				logger.info("Inside count IF");
				response.put("updated", Boolean.TRUE);
			} else {
				logger.info("Inside count ELSE");
				response.put("updated", Boolean.FALSE);
			}

		} catch (Exception e) {
			response.put(e.getMessage(), Boolean.FALSE);
		}
		return response;
	}

	@Override
	public MobileFeedModel addMobileFeed(MobileFeedModel feedModel) {
		logger.info("feedModel = " + feedModel);

		MobileFeedModel insertMobileFeed = null;
		Long maxid = mobileFeedRepository.getMaxId() + 1;
		logger.info("maxid = " + maxid);

		MobileFeedModel mobileFeedObj = new MobileFeedModel();
		mobileFeedObj.setMob_feed_id(maxid);
		mobileFeedObj.setPr_status_id(1);

		mobileFeedObj.setTransaction_type_name(feedModel.getTransaction_type_name());
		mobileFeedObj.setTrans_type(feedModel.getTrans_type());
		mobileFeedObj.setAccount_number(feedModel.getAccount_number());
		mobileFeedObj.setFirst_name(feedModel.getFirst_name());

		mobileFeedObj.setLast_name(feedModel.getLast_name());
		mobileFeedObj.setAccount_sales_rep_id(feedModel.getAccount_sales_rep_id());
		mobileFeedObj.setSubscriber_number(feedModel.getSubscriber_number());
		mobileFeedObj.setTransaction_complete_date(feedModel.getTransaction_complete_date());

		mobileFeedObj.setAccount_start_date(feedModel.getAccount_start_date());
		mobileFeedObj.setLine_add_create_datetime(feedModel.getLine_add_create_datetime());
		mobileFeedObj.setLine_disco_create_datetime(feedModel.getLine_disco_create_datetime());
		mobileFeedObj.setMsisdn(feedModel.getMsisdn());

		mobileFeedObj.setOrder_id(feedModel.getOrder_id());
		mobileFeedObj.setLine_status(feedModel.getLine_status());
		mobileFeedObj.setDayslineaddedfromacctstart(feedModel.getDayslineaddedfromacctstart());
		mobileFeedObj.setDays_line_active(feedModel.getDays_line_active());

		mobileFeedObj.setPlan_description(feedModel.getPlan_description());
		mobileFeedObj.setFixed_line_cust(feedModel.getFixed_line_cust());
		mobileFeedObj.setElig_revenue(feedModel.getElig_revenue());
		mobileFeedObj.setSim_activation_date(feedModel.getSim_activation_date());

		mobileFeedObj.setLine_add_status(feedModel.getLine_add_status());
		mobileFeedObj.setLine_disco_status(feedModel.getLine_disco_status());
		mobileFeedObj.setService_plan(feedModel.getService_plan());
		mobileFeedObj.setTotal_count(feedModel.getTotal_count());

		mobileFeedObj.setLine_sales_rep_id(feedModel.getLine_sales_rep_id());
		mobileFeedObj.setCreated_by(feedModel.getCreated_by());
		
		insertMobileFeed = mobileFeedRepository.save(mobileFeedObj);

		return insertMobileFeed;
	}

}
