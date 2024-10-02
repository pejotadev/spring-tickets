package com.tickets.spring_app.domain.event;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public record EventRequestDTO(
        String title,
        String description,
        Long date,
        String street,
        String city,
        Boolean remote,
        String eventUrl,
        MultipartFile image
){}