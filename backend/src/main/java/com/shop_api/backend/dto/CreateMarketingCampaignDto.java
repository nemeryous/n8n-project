package com.shop_api.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.entity.MarketingCampaign;
import lombok.Data;

@Data
public class CreateMarketingCampaignDto {

    @JsonProperty("productId")
    private Integer productId;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("teaserPost")
    private String teaserPost;

    @JsonProperty("launchPost")
    private String launchPost;

    @JsonProperty("faqAnswers")
    private String faqAnswers;

    @JsonProperty("teaserPostId")
    private String teaserPostId;

    @JsonProperty("launchPostId")
    private String launchPostId;

    public static MarketingCampaign toEntity(CreateMarketingCampaignDto dto) {
        MarketingCampaign campaign = new MarketingCampaign();
        campaign.setProductId(dto.getProductId());
        campaign.setProductName(dto.getProductName());
        campaign.setImageUrl(dto.getImageUrl());
        campaign.setTeaserPost(dto.getTeaserPost());
        campaign.setLaunchPost(dto.getLaunchPost());
        campaign.setFaqAnswers(dto.getFaqAnswers());
        campaign.setTeaserPostId(dto.getTeaserPostId());
        campaign.setLaunchPostId(dto.getLaunchPostId());
        return campaign;
    }
}