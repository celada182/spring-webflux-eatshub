package com.celada.eatshub.catalog;

import com.celada.eatshub.catalog.repository.enums.PriceRange;
import com.celada.eatshub.catalog.service.definition.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {

    @Autowired
    private RestaurantService restaurantService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("By cuisine type: French");
        this.restaurantService.readByCuisineType("French")
                .subscribe();

        log.info("By price range: CHEAP, MEDIUM");
        this.restaurantService.readByPriceRangeIn(List.of(PriceRange.CHEAP, PriceRange.MEDIUM))
                .subscribe();

        log.info("By name starting with: A");
        this.restaurantService.readByNameStartingWithIgnoreCase("A")
                .subscribe();

        log.info("By address city: Seattle");
        this.restaurantService.readByAddressCity("Seattle")
                .subscribe();

    }
}
