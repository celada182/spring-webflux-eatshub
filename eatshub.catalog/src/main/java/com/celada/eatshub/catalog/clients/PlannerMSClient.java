package com.celada.eatshub.catalog.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class PlannerMSClient {
    private static final String UNAVAILABLE_RESTAURANT_ID = "dfcbe98d-392b-4b93-9a49-27005223d15d";

    public Mono<Boolean> verifyAvailability(String date, String time, UUID restaurantId){
        return Mono.fromCallable(()-> !UNAVAILABLE_RESTAURANT_ID.equals(restaurantId.toString()))
                .delayElement(getRandomDuration())
                .doOnNext(r -> log.info("Verifying availability for restaurant with id: {}, date: {}, time: {}",
                        restaurantId, date, time));
    }

    private Duration getRandomDuration() {
        final var randomMillis = ThreadLocalRandom.current().nextInt(20, 1000);
        return Duration.ofMillis(randomMillis);
    }
}
