package com.microservices.ratingservice.external.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Hotel {
    private String id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String email;
}
