package com.shop_api.backend.service.marketing;

import com.shop_api.backend.dto.CreateMarketingCampaignDto;
import com.shop_api.backend.dto.MarketingCampaignDto;
import com.shop_api.backend.dto.UpdateMarketingCampaignDto;

import java.util.List;

public interface MarketingCampaignService {

    List<MarketingCampaignDto> getAllCampaigns();

    MarketingCampaignDto getCampaignById(Integer id);

    List<MarketingCampaignDto> getCampaignsByProductId(Integer productId);

    MarketingCampaignDto getCampaignByPostId(String postId);

    MarketingCampaignDto createCampaign(CreateMarketingCampaignDto dto);

    MarketingCampaignDto updateCampaign(Integer id, UpdateMarketingCampaignDto dto);

    boolean deleteCampaign(Integer id);
}
