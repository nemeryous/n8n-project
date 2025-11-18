package com.shop_api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shop_api.backend.dto.CreateMarketingCampaignDto;
import com.shop_api.backend.dto.MarketingCampaignDto;
import com.shop_api.backend.dto.UpdateMarketingCampaignDto;
import com.shop_api.backend.service.marketing.MarketingCampaignService;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/marketing-campaigns")
public class MarketingCampaignController {

    @Autowired
    private MarketingCampaignService marketingCampaignService;

    @GetMapping
    public ResponseEntity<List<MarketingCampaignDto>> getAllCampaigns() {
        List<MarketingCampaignDto> campaigns = marketingCampaignService.getAllCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketingCampaignDto> getCampaignById(@PathVariable Integer id) {
        try {
            MarketingCampaignDto campaign = marketingCampaignService.getCampaignById(id);
            return ResponseEntity.ok(campaign);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<MarketingCampaignDto>> getCampaignsByProductId(@PathVariable Integer productId) {
        List<MarketingCampaignDto> campaigns = marketingCampaignService.getCampaignsByProductId(productId);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<MarketingCampaignDto> getCampaignByPostId(@PathVariable String postId) {
        try {
            MarketingCampaignDto campaign = marketingCampaignService.getCampaignByPostId(postId);
            return ResponseEntity.ok(campaign);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<MarketingCampaignDto> createCampaign(@RequestBody CreateMarketingCampaignDto dto) {
        MarketingCampaignDto createdCampaign = marketingCampaignService.createCampaign(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCampaign);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketingCampaignDto> updateCampaign(
            @PathVariable Integer id,
            @RequestBody UpdateMarketingCampaignDto dto) {
        try {
            MarketingCampaignDto updatedCampaign = marketingCampaignService.updateCampaign(id, dto);
            return ResponseEntity.ok(updatedCampaign);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Integer id) {
        boolean deleted = marketingCampaignService.deleteCampaign(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}