package com.altice.salescommission.serviceImpl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.altice.salescommission.service.CommPlanService;

@Service
@Transactional
public class CommPlanServiceImpl implements CommPlanService {
	private static final Logger logger = LoggerFactory.getLogger(CommPlanServiceImpl.class.getName());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
}
