package com.shop_api.backend.service.marketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop_api.backend.dto.CreateMarketingCampaignDto;
import com.shop_api.backend.dto.MarketingCampaignDto;
import com.shop_api.backend.dto.UpdateMarketingCampaignDto;
import com.shop_api.backend.entity.MarketingCampaign;
import com.shop_api.backend.repository.MarketingCampaignRepository;

import java.util.List;

@Service
public class MarketingCampaignServiceImpl implements MarketingCampaignService {

    @Autowired
    private MarketingCampaignRepository marketingCampaignRepository;

    @Override
    public List<MarketingCampaignDto> getAllCampaigns() {
        return MarketingCampaignDto.fromEntities(marketingCampaignRepository.findAll());
    }

    @Override
    public MarketingCampaignDto getCampaignById(Integer id) {
        MarketingCampaign campaign = marketingCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marketing campaign not found with id: " + id));
        return MarketingCampaignDto.fromEntity(campaign);
    }

    @Override
    public List<MarketingCampaignDto> getCampaignsByProductId(Integer productId) {
        return MarketingCampaignDto.fromEntities(marketingCampaignRepository.findByProductId(productId));
    }

    @Override
    public MarketingCampaignDto getCampaignByPostId(String postId) {
        MarketingCampaign campaign = marketingCampaignRepository.findByTeaserPostIdOrLaunchPostId(postId)
                .orElseThrow(() -> new RuntimeException("Marketing campaign not found with post_id: " + postId));
        return MarketingCampaignDto.fromEntity(campaign);
    }

    @Override
    public MarketingCampaignDto createCampaign(CreateMarketingCampaignDto dto) {
        MarketingCampaign campaign = CreateMarketingCampaignDto.toEntity(dto);
        MarketingCampaign savedCampaign = marketingCampaignRepository.save(campaign);
        return MarketingCampaignDto.fromEntity(savedCampaign);
    }

    @Override
    public MarketingCampaignDto updateCampaign(Integer id, UpdateMarketingCampaignDto dto) {
        MarketingCampaign campaign = marketingCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marketing campaign not found with id: " + id));

        if (dto.getProductId() != null) {
            campaign.setProductId(dto.getProductId());
        }
        if (dto.getProductName() != null) {
            campaign.setProductName(dto.getProductName());
        }
        if (dto.getImageUrl() != null) {
            campaign.setImageUrl(dto.getImageUrl());
        }
        if (dto.getTeaserPost() != null) {
            campaign.setTeaserPost(dto.getTeaserPost());
        }
        if (dto.getLaunchPost() != null) {
            campaign.setLaunchPost(dto.getLaunchPost());
        }
        if (dto.getFaqAnswers() != null) {
            campaign.setFaqAnswers(dto.getFaqAnswers());
        }
        if (dto.getTeaserPostId() != null) {
            campaign.setTeaserPostId(dto.getTeaserPostId());
        }
        if (dto.getLaunchPostId() != null) {
            campaign.setLaunchPostId(dto.getLaunchPostId());
        }

        MarketingCampaign updatedCampaign = marketingCampaignRepository.save(campaign);
        return MarketingCampaignDto.fromEntity(updatedCampaign);
    }

    @Override
    public boolean deleteCampaign(Integer id) {
        if (marketingCampaignRepository.existsById(id)) {
            marketingCampaignRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
