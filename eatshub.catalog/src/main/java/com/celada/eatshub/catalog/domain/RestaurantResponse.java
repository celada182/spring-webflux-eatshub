package com.celada.eatshub.catalog.domain;

import com.celada.eatshub.catalog.domain.enums.PriceRange;
import com.celada.eatshub.catalog.domain.records.Address;
import com.celada.eatshub.catalog.domain.records.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestaurantResponse {

    private String name;
    private Address address;
    private String cuisineType;
    private PriceRange priceRange;
    private String openHours;
    private String logoUrl;
    private String closeAt;
    private ContactInfo contactInfo;
    private Double globalRating;
}

