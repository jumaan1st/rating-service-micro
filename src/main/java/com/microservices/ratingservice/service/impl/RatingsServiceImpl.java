package com.microservices.ratingservice.service.impl;

import com.microservices.ratingservice.entity.Ratings;
import com.microservices.ratingservice.external.model.Hotel;
import com.microservices.ratingservice.external.service.HotelClient;
import com.microservices.ratingservice.repository.RatingsRepository;
import com.microservices.ratingservice.service.RatingsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingsServiceImpl implements RatingsService {

    private final RatingsRepository ratingsRepository;
    private final HotelClient hotelClient;

    @Override
    public Ratings findById(String ratingId) {
        return ratingsRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + ratingId));
    }

    @Override
    public List<Ratings> findByIds(List<String> ids) {
        return ratingsRepository.findAllById(ids);
    }

    @Override
    public List<Ratings> findByHotelId(String hotelId) {
        return ratingsRepository.findByHotelId(hotelId);
    }

    @Override
    @CircuitBreaker(
            name = "hotel-service",
            fallbackMethod = "hotelFallback"
    )
    @Retry(name = "hotel-service", fallbackMethod = "hotelFallback")
    public List<Ratings> findByUserId(String userId) {

        List<Ratings> ratings = ratingsRepository.findByUserId(userId);

        if (ratings == null || ratings.isEmpty()) {
            return Collections.emptyList();
        }

        // Extract distinct hotel IDs to make a bulk call
        List<String> hotelIds = ratings.stream()
                .map(Ratings::getHotelId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // Fetch hotels in a single bulk call
        List<Hotel> hotels = List.of();
        if (!hotelIds.isEmpty()) {
            hotels = hotelClient.findByIds(hotelIds);
        }

        // Map hotel ID to Hotel object for quick lookup
        Map<String, Hotel> hotelMap = hotels.stream()
                .collect(Collectors.toMap(Hotel::getId, h -> h, (h1, h2) -> h1));

        // Assign hotels to their respective ratings
        for (Ratings rating : ratings) {
            if (rating.getHotelId() != null) {
                Hotel hotelEntity = hotelMap.get(rating.getHotelId());
                if (hotelEntity != null) {
                    log.info("Hotel id is {}", rating.getHotelId());
                    log.info("Hotel name is {}", hotelEntity.getName());
                    rating.setHotel(hotelEntity);
                }
            }
        }

        return ratings;
    }

    public List<Ratings> hotelFallback(
            String userId,
            Throwable throwable) {

        log.error(
                "Hotel service is down: {}",
                throwable.getMessage()
        );

        return Collections.emptyList();
    }

    @Override
    public List<Ratings> findByHotelIdAndUserId(String hotelId, String userId) {
        return ratingsRepository.findByHotelIdAndUserId(hotelId, userId);
    }

    @Override
    public List<Ratings> findAllRatings() {
        return ratingsRepository.findAll();
    }

    @Override
    public Ratings createRating(Ratings ratingRequest) {
        return ratingsRepository.save(ratingRequest);
    }

    @Override
    public Ratings updateRating(Ratings ratingRequest) {

        Ratings existingRating = ratingsRepository.findById(ratingRequest.getRatingsId())
                .orElseThrow(() -> new RuntimeException(
                        "Cannot update. Rating not found with id: " + ratingRequest.getRatingsId()
                ));

        existingRating.setUserId(ratingRequest.getUserId());
        existingRating.setHotelId(ratingRequest.getHotelId());
        existingRating.setRating(ratingRequest.getRating());
        existingRating.setFeedback(ratingRequest.getFeedback());

        return ratingsRepository.save(existingRating);
    }
    @Override
    public List<Ratings> createBulkRatings(List<Ratings> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            throw new RuntimeException("Ratings list cannot be empty");
        }
        return ratingsRepository.saveAll(ratings);
    }

    @Override
    public void deleteRatingById(String ratingId) {

        if (!ratingsRepository.existsById(ratingId)) {
            throw new RuntimeException("Cannot delete. Rating not found with id: " + ratingId);
        }

        ratingsRepository.deleteById(ratingId);
    }
}