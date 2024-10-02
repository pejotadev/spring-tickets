package com.tickets.spring_app.domain.address;

import com.tickets.spring_app.domain.event.Event;
import jakarta.persistence.*;

import java.util.UUID;

@Table(name = "address")
@Entity
public class Address {

    @Id
    @GeneratedValue
    private UUID id;

    private String street;

    private String city;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

}