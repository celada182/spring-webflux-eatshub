package com.celada.eatshub.catalog.route;

import com.celada.eatshub.catalog.handler.ReservationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReservationRouter {
    private final static String BY_ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> reservationRoutes(ReservationHandler reservationHandler) {
        return route()
                .path("/reservation", builder -> builder
                        .GET(BY_ID, reservationHandler::getReservationById)
                        .POST("", reservationHandler::postReservation)
                        .PUT(BY_ID, reservationHandler::putReservation)
                        .DELETE(BY_ID, reservationHandler::deleteReservation)
                ).build();
    }
}
