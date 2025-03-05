package com.altice.salescommission.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.altice.salescommission.entity.KomFeedEntity;

public interface KomFeedRepository extends JpaRepository<KomFeedEntity, Long> {

	@Query(value = "select p.* from fd_kom_feed p where p.corp=?1 and p.house =?2 and p.sales_rep_id =?3", nativeQuery = true)
	KomFeedEntity getKomFeedDataByCorpHouseRepId(int corp, String house, String salesrepid);

	@Query(value = "select distinct fkf.kom_feed_id,fkf.*,concat(cwbim.fname,' ',cwbim.lname) custname from fd_kom_feed fkf ,c_wip_buy_info_master cwbim where fkf.kom_feed_id = cwbim.kom_feed_id and fkf.kom_feed_id =?1", nativeQuery = true)
	KomFeedEntity getKomFeedDataBId(Long kom_feeed_id);

	@Query(value = "select fd_wip_buy_info_id from c_wip_buy_info_master cwbim where kom_feed_id =?1 limit 1", nativeQuery = true)
	Long getWIpBuyInfoId(Long corp);

	@Query(value = "select max(kom_feed_id) maxid from fd_kom_feed", nativeQuery = true)
	int getMaxId();

	@Query(value = "select p.kom_feed_id from fd_kom_feed p where p.corp=?1 and p.wordate =?2 limit 1", nativeQuery = true)
	int getKomFeedId(int corp, Date wordate);

	@Query(value = "select concat(shortdes,' (',longdesc,')') rc from c_rptctr_master crm  where rptgcenter =?1 and corp=?2", nativeQuery = true)
	String getReportingCenter(String rctr, int corp);

}
