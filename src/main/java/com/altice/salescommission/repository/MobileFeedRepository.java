package com.altice.salescommission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.altice.salescommission.model.MobileFeedModel;

public interface MobileFeedRepository extends JpaRepository<MobileFeedModel, Long> {
	@Query(value = "select max(mob_feed_id) maxid from t_mob_sales_data", nativeQuery = true)
	Long getMaxId();
}
