package com.altice.salescommission.service;

import java.util.List;
import java.util.Map;

import com.altice.salescommission.entity.CalcManageEntity;
import com.altice.salescommission.entity.CalcRunCtrlEntity;
import com.altice.salescommission.exception.DuplicateRecordException;
import com.altice.salescommission.exception.IdNotFoundException;

public interface CalcManageService extends AbstractBaseService<CalcManageEntity, Long> {
	
	CalcRunCtrlEntity saveRunCtrls(List<CalcRunCtrlEntity> calcRunCtrlModels) throws DuplicateRecordException;
	
	CalcRunCtrlEntity updateRunCtrls(List<CalcRunCtrlEntity> calcRunCtrlModels);

	List<CalcManageEntity> getSalesChannelDropDown();

	List<CalcManageEntity> getCalRunIdDropDown();

	List<CalcRunCtrlEntity> getRunControlByName();

	List<CalcRunCtrlEntity> getCalRunIdBySalesChnl();

	List<CalcRunCtrlEntity> getCommPlans(String salesChannels);

	List<CalcRunCtrlEntity> getEmpDataList(int commPanId);

	List<CalcManageEntity> getCalcData(String salesChannel, String calRunId);

	CalcManageEntity updateCalc(List<CalcManageEntity> calcManageEntity) throws IdNotFoundException;

	List<CalcRunCtrlEntity> getCalRunByRunId(int runctrlId);

	Map<String, Boolean> deleteCommPlan(int id);

	List<CalcRunCtrlEntity> getInprogressCalRunID();
}
