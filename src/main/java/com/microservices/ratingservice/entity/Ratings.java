package com.microservices.ratingservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("rating_service")
public class Ratings {
    @Id
    private String ratingsId;
    private String userId;
    private String hotelId;
    private String rating;
    private String feedback;
    @Transient
    private Hotel hotel;
}
