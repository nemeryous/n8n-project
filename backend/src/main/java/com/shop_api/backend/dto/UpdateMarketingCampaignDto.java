package com.shop_api.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateMarketingCampaignDto {

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
}