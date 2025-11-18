package com.shop_api.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.entity.MarketingCampaign;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MarketingCampaignDto {

    private Integer id;

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

    public static MarketingCampaignDto fromEntity(MarketingCampaign campaign) {
        MarketingCampaignDto dto = new MarketingCampaignDto();
        dto.setId(campaign.getId());
        dto.setProductId(campaign.getProductId());
        dto.setProductName(campaign.getProductName());
        dto.setImageUrl(campaign.getImageUrl());
        dto.setTeaserPost(campaign.getTeaserPost());
        dto.setLaunchPost(campaign.getLaunchPost());
        dto.setFaqAnswers(campaign.getFaqAnswers());
        dto.setTeaserPostId(campaign.getTeaserPostId());
        dto.setLaunchPostId(campaign.getLaunchPostId());
        return dto;
    }

    public static List<MarketingCampaignDto> fromEntities(List<MarketingCampaign> campaigns) {
        return campaigns.stream()
                .map(MarketingCampaignDto::fromEntity)
                .collect(Collectors.toList());
    }
}