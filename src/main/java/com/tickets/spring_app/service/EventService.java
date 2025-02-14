package com.tickets.spring_app.service;

import com.amazonaws.services.s3.AmazonS3;
import com.tickets.spring_app.domain.event.Event;
import com.tickets.spring_app.domain.event.EventRequestDTO;
import com.tickets.spring_app.domain.event.EventResponseDTO;
import com.tickets.spring_app.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    public AmazonS3 s3Client;

    @Autowired
    private EventRepository eventRepository;


    @Value("${aws.bucket.name}")
    private String bucketname;

    public Event createEvent(EventRequestDTO data) {
        String imageUrl = "";

        if (data.image() != null) {
            imageUrl = this.uploadImage(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setRemote(data.remote());

        newEvent.setImgUrl(imageUrl);

        this.eventRepository.save(newEvent);

        return newEvent;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Event> eventsPage = this.eventRepository.findUpcomingEvents(new Date(), pageable);

        return eventsPage.map(event -> new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate().getTime(),
                "",
                "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl()
                )).stream().toList();
    }

    private String uploadImage(MultipartFile image) {
        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();

        try{
            File file = this.convertMultiPartToFile(image);
            s3Client.putObject(bucketname, fileName, file);
            boolean delete = file.delete();
            if(delete == false) {
                System.out.println("File not deleted");
            }
            return s3Client.getUrl(bucketname, fileName).toString();
        }catch (Exception e){
            System.out.println("caiu no erro parça");
            e.printStackTrace();
            return "";
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();

        return convertedFile;
    }
}
