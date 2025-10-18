package com.shop_api.backend.service.n8n;

import com.shop_api.backend.dto.CartDto;
import com.shop_api.backend.dto.OrderDto;
import com.shop_api.backend.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class N8nWebHookService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${n8n.webhook.url}")
    private String n8nWebhookUrl;

    public void triggerCartCreatedWebhook(CartDto cartPayload) {
        try {
            log.info("Triggering N8N webhook for cart creation: {}", cartPayload.getId());
            restTemplate.postForObject(n8nWebhookUrl + "/cart-created", cartPayload, String.class);
            log.info("Successfully triggered N8N webhook for cart ID: {}", cartPayload.getId());
        } catch (Exception e) {
            log.error("Failed to trigger N8N webhook for cart ID: {}. Error: {}",
                    cartPayload.getId(), e.getMessage());
            // Optionally, add retry logic or queue the event
        }
    }

    public void triggerCartAbandonedWebhook(CartDto cartPayload) {
        try {
            log.info("Triggering N8N webhook for cart abandonment: {}", cartPayload.getId());
            restTemplate.postForObject(n8nWebhookUrl + "/cart-abandoned", cartPayload,
                    String.class);
            log.info("Successfully triggered N8N webhook for abandoned cart ID: {}",
                    cartPayload.getId());
        } catch (Exception e) {
            log.error("Failed to trigger N8N webhook for abandoned cart ID: {}. Error: {}",
                    cartPayload.getId(), e.getMessage());
        }
    }

    public void triggerCreateProductWebhook(ProductDto productPayload) {
        try {
            log.info("Triggering N8N webhook for product creation: {}", productPayload.getName());
            restTemplate.postForObject(n8nWebhookUrl + "/new-product-launch", productPayload,
                    String.class);
            log.info("Successfully triggered N8N webhook for product: {}",
                    productPayload.getName());
        } catch (Exception e) {
            log.error("Failed to trigger N8N webhook for product: {}. Error: {}",
                    productPayload.getName(), e.getMessage());
        }
    }

    public void triggerOrderCompletedWebhook(OrderDto orderPayload) {
        try {
            log.info("Triggering N8N webhook for order completion: {}", orderPayload.getId());
            restTemplate.postForObject(n8nWebhookUrl + "/order-completed", orderPayload,
                    String.class);
        } catch (Exception e) {
            log.error("Failed to trigger N8N webhook for order ID: {}. Error: {}",
                    orderPayload.getId(), e.getMessage());
        }
    }

    public void triggerShippingUpdatedWebhook(Integer orderId) {
        try {
            log.info("Triggering N8N webhook for shipping update: {}", orderId);
            restTemplate.postForObject(n8nWebhookUrl + "/shipping-updated", orderId, String.class);
        } catch (Exception e) {
            log.error("Failed to trigger N8N webhook for order ID: {}. Error: {}", orderId,
                    e.getMessage());
        }
    }

    public void triggerOrderDeliveredWebhook(Integer orderId) {
        try {
            log.info("Triggering N8N webhook for order delivered: {}", orderId);
            restTemplate.postForObject(n8nWebhookUrl + "/order-delivered", orderId, String.class);
        } catch (Exception e) {
            log.error("Failed to trigger N8N webhook for order ID: {}. Error: {}", orderId,
                    e.getMessage());
        }
    }
}
