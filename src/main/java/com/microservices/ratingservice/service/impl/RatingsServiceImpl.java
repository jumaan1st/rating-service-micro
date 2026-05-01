package com.microservices.ratingservice.service.impl;

import com.microservices.ratingservice.entity.Hotel;
import com.microservices.ratingservice.entity.Ratings;
import com.microservices.ratingservice.external.service.HotelService;
import com.microservices.ratingservice.repository.RatingsRepository;
import com.microservices.ratingservice.service.RatingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingsServiceImpl implements RatingsService {

    private final RatingsRepository ratingsRepository;
    private final RestTemplate restTemplate;
    private final HotelService hotelService;

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
    public List<Ratings> findByUserId(String userId) {
        List<Ratings> ratings = ratingsRepository.findByUserId(userId);

        Hotel hotel = null;
        for (Ratings rating : ratings) {

            try {
                ResponseEntity<Hotel> hotelEntity = hotelService.findById(rating.getHotelId());
                if (hotelEntity.getStatusCode().is2xxSuccessful()) {
                    hotel = hotelEntity.getBody();
                } else {
                    log.error("hotel status code is {}", hotelEntity.getStatusCode());
                }
            }catch (Exception e) {
                log.error("hotel status code is {}", e.getMessage());
            }
            log.info("hotel id is {}", rating.getHotelId());
            log.info("hotel name is {}", hotel);
        rating.setHotel(hotel);
        }

        return ratings;
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