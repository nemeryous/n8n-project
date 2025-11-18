package com.shop_api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shop_api.backend.entity.MarketingCampaign;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarketingCampaignRepository extends JpaRepository<MarketingCampaign, Integer> {
    
    List<MarketingCampaign> findByProductId(Integer productId);
    
    Optional<MarketingCampaign> findByTeaserPostId(String teaserPostId);
    
    Optional<MarketingCampaign> findByLaunchPostId(String launchPostId);
    
    @Query("SELECT m FROM MarketingCampaign m WHERE m.teaserPostId = :postId OR m.launchPostId = :postId")
    Optional<MarketingCampaign> findByTeaserPostIdOrLaunchPostId(@Param("postId") String postId);
    
    List<MarketingCampaign> findByProductNameContainingIgnoreCase(String productName);
}