package com.celada.eatshub.catalog.repository.model;

import com.celada.eatshub.catalog.repository.enums.PriceRange;
import com.celada.eatshub.catalog.repository.records.Address;
import com.celada.eatshub.catalog.repository.records.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for Restaurant entity in MongoDB
 */
@Document(collection = "restaurants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {

    @Id
    private UUID id;
    @Indexed
    private String name;
    private Integer capacity;
    private Address address;
    @Indexed
    private String cuisineType;
    @Indexed
    private PriceRange priceRange;
    private String openHours;
    private String logoUrl;
    private String closeAt;
    private String maxCapacity;
    private ContactInfo contactInfo;
    private List<ReviewDto> reviews;
}
