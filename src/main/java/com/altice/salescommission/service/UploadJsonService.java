package com.altice.salescommission.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.altice.salescommission.entity.UploadButtonsEntity;
import com.altice.salescommission.entity.UploadJsonEntity;
import com.altice.salescommission.entity.UploadHeadersEntity;
import com.altice.salescommission.exception.IdNotFoundException;
import com.altice.salescommission.model.UploadJsonHeadersModel;

public interface UploadJsonService extends AbstractBaseService<UploadJsonEntity, Long> {

	UploadJsonEntity uploadJSON(String gridName, String sql, String pageName, int orderBy, String current_user,
			String kpiId, String report_type, String description, String comm_plan_id,String salesChannel)
			throws IOException, IdNotFoundException;

	UploadJsonEntity cloneData(String sc, int commId, int commPlanId, String salesChannel, String current_user)
			throws IOException, IdNotFoundException;

	UploadJsonEntity updateJSON(MultipartFile file, String gridName, String sql, String pageName, int orderBy,
			String current_user, int commPlanId, String salesChannel, int kpiId, long id)
			throws IOException, IdNotFoundException;

	UploadJsonEntity updateJSONWithoutFile(String gridName, String sql, String pageName, int orderBy,
			String current_user, String commPlanId, String salesChannel, String kpiId, long id)
			throws IOException, IdNotFoundException;

	List<UploadJsonEntity> getJsonsList(String salesChannels, int commPlanId);

	List<UploadJsonEntity> getKpisList();

	List<UploadJsonEntity> getComPlansList();
	
	List<UploadJsonEntity> getReportTypesList();

	List<UploadJsonEntity> getSalesChannelsList();

	List<UploadJsonEntity> getCommPlans(String saleschannel);

	UploadHeadersEntity updateHeaders(List<UploadHeadersEntity> uploadJsonHeadersEntity);

	UploadButtonsEntity updateButtons(List<UploadButtonsEntity> uploadJsonHeadersEntity);

	List<UploadJsonHeadersModel> getHeaders(int id);
	
	List<UploadJsonEntity> getCommPlanAndKpis(int id);

	List<UploadJsonHeadersModel> getButtons(int id);
}
