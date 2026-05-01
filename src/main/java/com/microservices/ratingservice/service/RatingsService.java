package com.microservices.ratingservice.service;

import com.microservices.ratingservice.entity.Ratings;

import java.util.List;

public interface RatingsService {

    Ratings findById(String id);
    List<Ratings> findByIds(List<String> ids);

    List<Ratings> findByHotelId(String hotelId);

    List<Ratings> findByUserId(String userId);

    List<Ratings> findByHotelIdAndUserId(String hotelId, String userId);

    List<Ratings> findAllRatings();

    Ratings createRating(Ratings ratings);
    Ratings updateRating(Ratings ratings);
    List<Ratings> createBulkRatings(List<Ratings> ratings);
    void deleteRatingById(String id);
}
