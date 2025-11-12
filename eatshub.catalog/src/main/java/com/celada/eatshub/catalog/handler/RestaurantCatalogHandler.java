package com.celada.eatshub.catalog.handler;

import com.celada.eatshub.catalog.domain.RestaurantResponse;
import com.celada.eatshub.catalog.domain.enums.PriceRange;
import com.celada.eatshub.catalog.service.definition.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RestaurantCatalogHandler {
    private final RestaurantService restaurantService;

    public Mono<ServerResponse> getAllRestaurants(ServerRequest request) {
        final var restaurantFlux = this.restaurantService.readAll();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(restaurantFlux, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getRestaurantByName(ServerRequest request) {
        final var name = request.pathVariable("name");
        final var response = this.restaurantService.readByNameStartingWithIgnoreCase(name);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getRestaurantsByCousinType(ServerRequest request) {
        final var cousinType = request.queryParam("cousinType").orElse("all");
        final var response = this.restaurantService.readByCuisineType(cousinType);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getRestaurantsBetweenPrice(ServerRequest request) {
        final String prices = request.queryParam("prices").orElse("");
        List<PriceRange> priceRanges = Arrays.stream(prices.split(","))
                .map(String::trim)
                .map(PriceRange::valueOf)
                .toList();
        final var response = this.restaurantService.readByPriceRangeIn(priceRanges);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getRestaurantsByCity(ServerRequest request) {
        final var city = request.queryParam("city").orElse("all");
        final var response = this.restaurantService.readByAddressCity(city);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
