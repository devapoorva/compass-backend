package com.altice.salescommission.approvalworkflow.queries;

public interface WorkFlowQueries {
	public final String GET_WORKFLOWS = "select flow_id ,workflow_name ,active_flag ,to_char(created_dt,'MM-DD-YYYY') created_dt ,created_by ,"
			+ "to_char(update_dt,'MM-DD-YYYY') update_dt ,updated_by ,to_char(effective_date,'MM-DD-YYYY') effective_date  from c_workflow_master";

	public final String GET_LEVELS = "select distinct level  from c_workflow_defination cwd where flow_id =? order by level";

	public final String GET_STAGES = "select * from c_workflow_defination cwd where flow_id =? and level =?  order by stage";

	public final String GET_WORKFLOWDEFFIELDS = "select field_name,description from c_translate_master ctm  where field_name  in ('Approval Type','Timeout Action','Approval Criteria') and effective_status = 'Y'";;

	public final String GET_DEFINITIONS = "select * from c_workflow_defination cwd where flow_id =? and level =? and stage =?";

	public final String GET_APPROVALS = "select distinct approval_id,name,description from c_workflow_approval cwa";

	public final String GET_APPROVALS_BY_APROVALID = "select * from c_workflow_approval cwa  where approval_id =?";

	public final String GET_CRITERIA = "select * from c_workflow_criteria cwlc where flow_id =? and level =? and criteria_type ='Level Criteria'";

	public final String GET_STAGE_CRITERIA = "select * from c_workflow_criteria cwlc where flow_id =? and level =? and stage =? and criteria_type ='Stage Criteria'";

	public final String GET_TICKETS = "select distinct on (twd.tracking_id) tracking_id ,to_char(twd.created_dt,'MM-DD-YYYY') cdt,twd.created_dt,twd.final_status status "
			+ "from t_workflow_details twd,c_workflow_defination cwd "
			+ "where twd.defination_id =cwd.defination_id and twd.flow_id =cwd.flow_id " + "order by twd.tracking_id";

	public final String GET_TICKETS_LEVELS = "select distinct level,tracking_id from t_workflow_details twd  where tracking_id = ?  order by level";

	public final String GET_TICKETS_STAGES = "select distinct stage,level,tracking_id from t_workflow_details twd  where tracking_id = ? and level = ? order by stage";

	public final String GET_TICKETS_SUBLEVELS = "select *,to_char(twd.approved_dt,'MM-DD-YYYY') apdt,to_char(twd.pending_from_dt,'MM-DD-YYYY') pdt from t_workflow_details twd  where tracking_id =?  and level =? and stage = ? order by sub_level";

	public final String GET_PENDING_APPROVALS = "select * from t_workflow_details twd where approval_from =? and status ='Pending' limit 1";

	public final String GET_ALL_PENDING_APPROVALS1 = "select * from t_workflow_details twd where approval_from !=? and status ='Pending'";

	public final String GET_ALL_PENDING_APPROVALS = "select aa.* from (select distinct on (twd.tracking_id) tracking_id,cwm.workflow_name, twd.final_status ,"
			+ "to_char(twd.created_dt,'MM-DD-YYYY') cdt,twd.created_dt,twd.created_by,twd.level ,to_char(twd.pending_from_dt,'MM-DD-YYYY') pdt,twd.pending_from_dt ,twd.flow_id "
			+ "from t_workflow_details twd,c_workflow_master cwm  "
			+ "where twd.flow_id = cwm.flow_id and (1=? or twd.submitted_by = ?) and twd.status  in ('Pending','Approved','Rejected') "
			+ "union all " + "select distinct on (twd.tracking_id) tracking_id,cwm.workflow_name, twd.final_status ,"
			+ "to_char(twd.created_dt,'MM-DD-YYYY') cdt,twd.created_dt ,twd.created_by,twd.level ,to_char(twd.pending_from_dt,'MM-DD-YYYY') pdt,twd.pending_from_dt ,twd.flow_id "
			+ "from t_workflow_details twd,c_workflow_master cwm "
			+ "where twd.flow_id = cwm.flow_id and twd.approval_from = ? and twd.status  in ('Pending','Approved','Rejected')) aa order by aa.tracking_id desc ";

	public final String GET_PENDING_DATA = "select twd.comments,twd.flow_id,twd.ticket_id,twd.tracking_id ,twm.message_content,"
			+ "twd.created_by ,to_char(twd.created_dt,'MM-DD-YYYY') cdt,twd.created_dt,"
			+ "to_char(twd.pending_from_dt,'MM-DD-YYYY') pdt,twd.pending_from_dt,twd.approved_by ,"
			+ "twd.approval_name,twd.approved_dt,twd.approved_by,twd.status, cwm.workflow_name,twd.final_status, twm.chargeback_delete_id,twm.comm_plan_id "
			+ "from  t_workflow_details twd, t_workflow_message twm, c_workflow_master cwm "
			+ "where twd.tracking_id = twm.tracking_id and twd.flow_id =cwm.flow_id and twd.final_status='Pending' "
			+ "and twd.status ='Pending' "
			+ "and twd.created_dt between date_trunc('month', CURRENT_DATE) and date_trunc('month', CURRENT_DATE) + interval '1 month - 1 day' "
			+ "and (1=? or twd.submitted_by = ?) and (1=? or twd.approval_from = ?) "
			+ "order by twd.created_by,twd.created_dt desc";

	public final String GET_APPROVAL_REJECTED_DATA_EMP = "select twd.comments,twd.flow_id,twd.ticket_id,twd.tracking_id ,twm.message_content,"
			+ "twd.created_by ,to_char(twd.created_dt,'MM-DD-YYYY') cdt,twd.created_dt,"
			+ "to_char(twd.pending_from_dt,'MM-DD-YYYY') pdt,twd.pending_from_dt,twd.approved_by ,"
			+ "twd.approval_name,twd.approved_dt,twd.approved_by,twd.status, cwm.workflow_name,twd.final_status, twm.chargeback_delete_id,twm.comm_plan_id "
			+ "from  t_workflow_details twd, t_workflow_message twm, c_workflow_master cwm "
			+ "where twd.tracking_id = twm.tracking_id and twd.flow_id =cwm.flow_id "
			+ "and twd.created_dt between date_trunc('month', CURRENT_DATE) and date_trunc('month', CURRENT_DATE) + interval '1 month - 1 day' "
			+ "and twd.final_status=? and twd.status =? and twd.submitted_by = ? and twd.approval_name ='ADMIN' "
			+ "order by twd.created_by,twd.created_dt desc";

	public final String GET_APPROVAL_REJECTED_DATA_SUP = "select twd.comments,twd.flow_id,twd.ticket_id,twd.tracking_id ,twm.message_content,"
			+ "twd.created_by ,to_char(twd.created_dt,'MM-DD-YYYY') cdt,twd.created_dt,"
			+ "to_char(twd.pending_from_dt,'MM-DD-YYYY') pdt,twd.pending_from_dt,twd.approved_by ,"
			+ "twd.approval_name,twd.approved_dt,twd.approved_by,twd.status, cwm.workflow_name,twd.final_status, twm.chargeback_delete_id,twm.comm_plan_id "
			+ "from  t_workflow_details twd, t_workflow_message twm, c_workflow_master cwm "
			+ "where twd.tracking_id = twm.tracking_id and twd.flow_id =cwm.flow_id "
			+ "and twd.created_dt between date_trunc('month', CURRENT_DATE) and date_trunc('month', CURRENT_DATE) + interval '1 month - 1 day' "
			+ "and twd.final_status=? and twd.status =? and twd.approval_from = ?  "
			+ "order by twd.created_by,twd.created_dt desc";

	public final String GET_ALL_DATA_EMP = "select distinct on(twd.tracking_id) twd.tracking_id,twd.comments,twd.flow_id,twd.ticket_id,twm.message_content,"
			+ "twd.created_by ,to_char(twd.created_dt,'MM-DD-YYYY') cdt,twd.created_dt,"
			+ "to_char(twd.pending_from_dt,'MM-DD-YYYY') pdt,twd.pending_from_dt,twd.approved_by ,"
			+ "twd.approval_name,twd.approved_dt,twd.approved_by,twd.status, cwm.workflow_name,twd.final_status, twm.chargeback_delete_id,twm.comm_plan_id,twm.complex_kpiid "
			+ "from  t_workflow_details twd, t_workflow_message twm, c_workflow_master cwm "
			+ "where twd.tracking_id = twm.tracking_id and twd.flow_id =cwm.flow_id "
			+ "and (1=? or twd.created_dt between date_trunc('month', CURRENT_DATE) and date_trunc('month', CURRENT_DATE) + interval '1 month - 1 day') "
			+ "and (1=? or twd.created_dt::varchar between ? and ?) "
			+ "and (1=? or twd.submitted_by = ?) and twd.status != 'initiated' and twd.flow_id !=4 "
			+ "order by twd.tracking_id desc,twd.stage desc";

	public final String GET_ALL_DATA_SUP = "select distinct on(twd.tracking_id) twd.tracking_id,twd.comments,twd.flow_id,twd.ticket_id,twm.message_content,"
			+ "twd.created_by ,to_char(twd.created_dt,'MM-DD-YYYY') cdt,twd.created_dt,"
			+ "to_char(twd.pending_from_dt,'MM-DD-YYYY') pdt,twd.pending_from_dt,twd.approved_by ,"
			+ "twd.approval_name,twd.approved_dt,twd.approved_by,twd.status, cwm.workflow_name,twd.final_status, twm.chargeback_delete_id,twm.comm_plan_id,twm.complex_kpiid,"
			+ "cem.sales_rep_id ,concat(first_name,' ',last_name) empname, ccpm.sales_channel "
			+ "from  t_workflow_details twd  "
			+ "inner join t_workflow_message twm on(twm.tracking_id=twd.tracking_id) "
			+ "inner join c_workflow_master cwm on(cwm.flow_id = twd.flow_id) "
			+ "inner join c_employee_master cem on(twd.submitted_by = cem.employee_id and cem.profile_status='complete') "
			+ "inner join c_comm_plan_master ccpm on(ccpm.comm_plan_id=twm.comm_plan_id) "
			+ "where twd.tracking_id = twm.tracking_id and twd.flow_id =cwm.flow_id "
			+ "and (1=? or twd.created_dt between date_trunc('month', CURRENT_DATE) and date_trunc('month', CURRENT_DATE) + interval '1 month - 1 day') "
			+ "and (1=? or twd.created_dt::varchar between ? and ?) "
			+ "and (1=? or twd.approval_from in (?,'ADMIN')) and twd.status != 'initiated'  and twd.flow_id !=4 "
			+ "order by twd.tracking_id desc,twd.stage desc";

	public final String GET_ALL_PENDING_APPROVALS_DASH = "select distinct on (twd.tracking_id) tracking_id,cwm.workflow_name, twd.final_status ,twd.created_dt ,twd.created_by,twd.level ,twd.pending_from_dt ,twd.flow_id,twd.approval_name "
			+ "from t_workflow_details twd,c_workflow_master cwm where twd.flow_id = cwm.flow_id and  twd.against = ? and twd.status  in ('Pending') order by twd.tracking_id desc";

	public final String GET_PENDING_TICKETS = "select *,to_char(twd.approved_dt,'MM-DD-YYYY') apdt,to_char(twd.pending_from_dt,'MM-DD-YYYY') pdt from t_workflow_details twd where tracking_id =? order by level,stage,sub_level ";

	public final String GET_CONTENT_MESSAGE = "select rowid,tracking_id ,message_content,created_by ,created_dt from t_workflow_message twm  where tracking_id like ? order by rowid desc limit 1";

	public final String GET_TICKETS_LEVELS_STAGES = "select * from t_workflow_details twd  where tracking_id =? and status is null order by level ,stage,sub_level limit 1 ";

	public final String RUN_CRON_JOB = " select cwd.flow_id ,cwd.level ,cwd.stage ,cwd.timeout ,cwd.timeout_action ,cwd.approval_criteria ,twd.created_dt,twd.tracking_id from "
			+ "c_workflow_defination cwd ,t_workflow_details twd  where twd.status ='Pending' and twd.flow_id =cwd.flow_id"
			+ "	and twd.level =cwd.level and twd.stage = cwd .stage ";

}
