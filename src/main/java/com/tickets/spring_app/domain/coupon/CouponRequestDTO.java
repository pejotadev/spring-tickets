package com.tickets.spring_app.domain.coupon;

public record CouponRequestDTO(String code, Integer discount, Long validUntil) {
}
