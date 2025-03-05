package com.altice.salescommission.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;

@MappedSuperclass
public class AbstractBaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String created_by;
	private String updated_by;
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date created_dt;
	private Date updated_dt;
	private String comment;
	
	@Override
	public String toString() {
		return "AbstractBaseEntity [created_by=" + created_by + ", updated_by=" + updated_by + ", created_dt="
				+ created_dt + ", updated_dt=" + updated_dt + ", comment=" + comment + "]";
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
	public Date getCreated_dt() {
		return created_dt;
	}
	public void setCreated_dt(Date created_dt) {
		this.created_dt = created_dt;
	}
	public Date getUpdated_dt() {
		return updated_dt;
	}
	public void setUpdated_dt(Date updated_dt) {
		this.updated_dt = updated_dt;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}