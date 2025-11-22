package com.shop_api.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateCampaignPostIdsDto {

    @JsonProperty("teaserPostId")
    private String teaserPostId;

    @JsonProperty("launchPostId")
    private String launchPostId;
}
