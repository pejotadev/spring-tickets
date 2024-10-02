package com.tickets.spring_app.service;

import com.tickets.spring_app.domain.coupon.Coupon;
import com.tickets.spring_app.domain.coupon.CouponRequestDTO;
import com.tickets.spring_app.domain.event.Event;
import com.tickets.spring_app.repositories.CouponRepository;
import com.tickets.spring_app.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private EventRepository eventRepository;

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO couponData) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        Coupon coupon = new Coupon();
        coupon.setCode(couponData.code());
        coupon.setDiscount(couponData.discount());
        coupon.setValidUntil(new Date(couponData.validUntil()));
        coupon.setEvent(event);

        return couponRepository.save(coupon);
    }

}