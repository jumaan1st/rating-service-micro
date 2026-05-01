package com.microservices.ratingservice.controller;

import com.microservices.ratingservice.entity.Ratings;
import com.microservices.ratingservice.service.RatingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@Tag(name = "Ratings API", description = "Operations related to Ratings Management")
public class RatingsController {

    private final RatingsService ratingsService;

    @Operation(summary = "Get rating by ID")
    @GetMapping("/{ratingId}")
    public Ratings getRatingById(
            @Parameter(description = "Rating ID", required = true)
            @PathVariable String ratingId) {
        return ratingsService.findById(ratingId);
    }

    @PostMapping("/ratings-by-ids")
    public List<Ratings> getRatingByIds(
            @Parameter(description = "Rating ID", required = true)
            @RequestBody List<String> ratingId) {
        return ratingsService.findByIds(ratingId);
    }

    @Operation(summary = "Get all ratings")
    @GetMapping
    public List<Ratings> getAllRatings() {
        return ratingsService.findAllRatings();
    }

    @Operation(summary = "Get ratings by Hotel ID")
    @GetMapping("/hotel/{hotelId}")
    public List<Ratings> getRatingsByHotelId(
            @Parameter(description = "Hotel ID", required = true)
            @PathVariable String hotelId) {
        return ratingsService.findByHotelId(hotelId);
    }

    @Operation(summary = "Get ratings by User ID")
    @GetMapping("/user/{userId}")
    public List<Ratings> getRatingsByUserId(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        return ratingsService.findByUserId(userId);
    }

    @Operation(summary = "Get ratings by Hotel ID and User ID")
    @GetMapping("/filter")
    public List<Ratings> getRatingsByHotelAndUser(
            @Parameter(description = "Hotel ID")
            @RequestParam String hotelId,
            @Parameter(description = "User ID")
            @RequestParam String userId) {
        return ratingsService.findByHotelIdAndUserId(hotelId, userId);
    }

    @Operation(summary = "Create new rating")
    @PostMapping
    public Ratings createRating(
            @RequestBody Ratings ratingRequest) {
        return ratingsService.createRating(ratingRequest);
    }

    @Operation(summary = "Create multiple ratings in bulk")
    @PostMapping("/bulk")
    public ResponseEntity<List<Ratings>> createBulkRatings(@RequestBody List<Ratings> ratings) {
        List<Ratings> savedRatings = ratingsService.createBulkRatings(ratings);
        return ResponseEntity.ok(savedRatings);
    }

    @Operation(summary = "Update existing rating")
    @PutMapping
    public Ratings updateRating(
            @RequestBody Ratings ratingRequest) {
        return ratingsService.updateRating(ratingRequest);
    }

    @Operation(summary = "Delete rating by ID")
    @DeleteMapping("/{ratingId}")
    public void deleteRating(
            @Parameter(description = "Rating ID", required = true)
            @PathVariable String ratingId) {
        ratingsService.deleteRatingById(ratingId);
    }
}