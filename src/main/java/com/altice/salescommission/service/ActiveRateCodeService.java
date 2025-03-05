package com.altice.salescommission.service;

import java.util.Date;
import java.util.List;

import com.altice.salescommission.entity.ActiveRateCodeEntity;

public interface ActiveRateCodeService extends AbstractBaseService<ActiveRateCodeEntity, Long>{
	List<ActiveRateCodeEntity> getProducts();
	List<ActiveRateCodeEntity> getSalesChannels();
	List<ActiveRateCodeEntity> getCorps();
	List<ActiveRateCodeEntity> getActiveCodeDetails(String corps,String sc,int proid,Date validdt);
}
