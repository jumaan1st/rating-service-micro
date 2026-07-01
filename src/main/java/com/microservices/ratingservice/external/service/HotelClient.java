package com.microservices.ratingservice.external.service;

import com.microservices.ratingservice.external.model.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        value = "hotel-service",
        path = "/api/v2/hotel"
)
public interface HotelClient {

    @GetMapping("/{id}")
    Hotel findById(@PathVariable("id") String id);

    @PostMapping("/hotels-by-ids")
    List<Hotel> findByIds(@RequestBody List<String> ids);
}
