package com.tickets.spring_app.service;

import com.amazonaws.services.s3.AmazonS3;
import com.tickets.spring_app.domain.event.Event;
import com.tickets.spring_app.domain.event.EventRequestDTO;
import com.tickets.spring_app.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
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

        eventRepository.save(newEvent);

        return newEvent;
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
