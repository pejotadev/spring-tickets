package com.tickets.spring_app.domain.event;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record EventResponseDTO(
    UUID id,
    String title,
    String description,
    Long date,
    String street,
    String city,
    Boolean remote,
    String eventUrl,
    String imageUrl
) {
}
