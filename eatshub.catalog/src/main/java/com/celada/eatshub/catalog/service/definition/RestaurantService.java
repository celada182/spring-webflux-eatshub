package com.celada.eatshub.catalog.service.definition;

import com.celada.eatshub.catalog.repository.enums.PriceRange;
import com.celada.eatshub.catalog.repository.model.RestaurantDto;
import reactor.core.publisher.Flux;

import java.util.List;

public interface RestaurantService {
    Flux<RestaurantDto> readByCuisineType(String cuisineType);
    Flux<RestaurantDto> readByNameStartingWithIgnoreCase(String name);
    Flux<RestaurantDto> readByPriceRangeIn(List<PriceRange> prices);
    Flux<RestaurantDto> readByAddressCity(String city);
}
