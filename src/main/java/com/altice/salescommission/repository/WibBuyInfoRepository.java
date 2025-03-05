package com.altice.salescommission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.entity.WipBuyInfoEntity;

public interface WibBuyInfoRepository extends JpaRepository<WipBuyInfoEntity, Long>{
	
	@Query(value="select p.* from c_wip_buy_info_master p where p.kom_feed_id=?1", nativeQuery = true)
	List<WipBuyInfoEntity> getWipByInfoDataByKomfeedid(int kom_feed_id); 
}
