package com.altice.salescommission.approvalworkflow.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "c_workflow_master")
public class WorkflowMasterModel {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "flow_id", nullable = false, updatable = false)
	private Long id;
    private String workflow_name;
    private String active_flag;
    private Date created_dt ;
    private String created_by;
    private Date update_dt ;
    private String updated_by;
    private Date effective_date;
    
    @Transient
    private String cdt;
    @Transient
    private String udt;
    @Transient
    private String effdt;
	

}
