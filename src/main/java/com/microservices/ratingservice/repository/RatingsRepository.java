package com.microservices.ratingservice.repository;

import com.microservices.ratingservice.entity.Ratings;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingsRepository extends MongoRepository<Ratings, String> {

    List<Ratings> findByHotelId(String hotelId);

    List<Ratings> findByUserId(String userId);

    List<Ratings> findByHotelIdAndUserId(String hotelId, String userId);
}