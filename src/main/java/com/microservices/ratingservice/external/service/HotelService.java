package com.microservices.ratingservice.external.service;

import com.microservices.ratingservice.entity.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "HOTELSERVICE",path = "/api/v2/hotel")
public interface HotelService {

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> findById(@PathVariable String id);
}
