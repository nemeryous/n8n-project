package com.shop_api.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "marketing_campaigns")
public class MarketingCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "teaser_post", columnDefinition = "TEXT")
    private String teaserPost;

    @Column(name = "launch_post", columnDefinition = "TEXT")
    private String launchPost;

    @Column(name = "faq_answers", columnDefinition = "TEXT")
    private String faqAnswers;

    @Column(name = "teaser_post_id")
    private String teaserPostId;

    @Column(name = "launch_post_id")
    private String launchPostId;
}