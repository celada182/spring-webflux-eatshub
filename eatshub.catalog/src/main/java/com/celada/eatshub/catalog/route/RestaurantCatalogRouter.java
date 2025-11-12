package com.celada.eatshub.catalog.route;

import com.celada.eatshub.catalog.handler.RestaurantCatalogHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RestaurantCatalogRouter {

    private final static String BY_NAME_URL = "/{name}";

    @Bean
    public RouterFunction<ServerResponse> routes(RestaurantCatalogHandler restaurantCatalogHandler) {
        return route()
                .path("/restaurants", builder -> builder
                        .GET(BY_NAME_URL, restaurantCatalogHandler::getRestaurantByName)
                        .GET("", request -> {
                            if (request.queryParam("cousinType").isPresent()) {
                                return restaurantCatalogHandler.getRestaurantsByCousinType(request);
                            }
                            if (request.queryParam("prices").isPresent()) {
                                return restaurantCatalogHandler.getRestaurantsBetweenPrice(request);
                            }
                            if (request.queryParam("city").isPresent()) {
                                return restaurantCatalogHandler.getRestaurantsByCity(request);
                            }
                            return restaurantCatalogHandler.getAllRestaurants(request);
                        })
                ).build();
    }
}
