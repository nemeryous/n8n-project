package com.shop_api.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class CheckoutRequestDto {

    @JsonProperty("customer_id")
    private Integer customerId;

    @JsonProperty("cart_id")
    private Integer cartId;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("notes")
    private String notes;
}