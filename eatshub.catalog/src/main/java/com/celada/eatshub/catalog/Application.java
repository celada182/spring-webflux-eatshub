package com.celada.eatshub.catalog;

import com.celada.eatshub.catalog.repository.ReservationRepository;
import com.celada.eatshub.catalog.repository.model.ReservationDto;
import com.celada.eatshub.catalog.service.definition.ReservationService;
import com.celada.eatshub.catalog.service.definition.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== STARTING RESERVATION INSERT TESTS ===\n");

        final var parrillaModernaID = "0ee619ba-e95f-4103-99f7-ee9cdf831d90";
        final var cafeNostalgiaID = "be33011c-13dd-45b9-a60e-e9adb8f4e022";

        final var sarahReservation = createTestReservation(
                parrillaModernaID,
                "Sarah Johnson",
                4,
                "2025-06-15",
                "19:30",
                "Window table preferred"
        );

        final var michaelReservation = createTestReservation(
                parrillaModernaID,
                "Michael Davis",
                2,
                "2025-06-16",
                "20:00",
                "Anniversary dinner - romantic table"
        );

        final var emmaReservation = createTestReservation(
                cafeNostalgiaID,
                "Emma Wilson",
                6,
                "2025-06-17",
                "18:00",
                "Family birthday celebration"
        );

        final var sarahReservationCreated = reservationService.create(sarahReservation)
                .block();

        System.out.println("Sarah reservation: " + sarahReservationCreated.getId());

        final var michaelReservationCreated = reservationService.create(michaelReservation)
                .block();
        System.out.println("Michael reservation: " + michaelReservationCreated.getId());

        final var emmaReservationCreated = reservationService.create(emmaReservation)
                .block();
        System.out.println("Emma reservation: " + emmaReservationCreated.getId());

        System.out.println("=== FINISHED RESERVATION INSERT TESTS ===");

        System.out.println("=== INIT RESERVATION UPDATE TESTS ===");

        final var michaelReservationToUpdate = reservationService.readById(michaelReservationCreated.getId()).block();

        michaelReservationToUpdate.setDate("20:30");
        michaelReservationToUpdate.setPartySize(3);

        final var michaelReservationUpdated = this.reservationRepository.save(michaelReservationToUpdate).block();

        System.out.println("michael reservation updated: " + michaelReservationUpdated.getDate());
        System.out.println("michael reservation updated: " + michaelReservationUpdated.getPartySize());

        System.out.println("=== FINISHED RESERVATION INSERT TESTS ===");


//        Thread.sleep(60000);
//        System.out.println("=== INIT RESERVATION DELETE TESTS ===");
//        this.reservationService.delete(michaelReservationCreated.getId()).block();
//        System.out.println("=== FINISHED RESERVATION DELETE TESTS ===");

        System.out.println("=== VALIDATION TESTS ===");
        final var michaelReservation2 = createTestReservation(
                parrillaModernaID,
                "Michael Davis",
                2,
                "2025-06-16",
                "23:00",
                "Anniversary dinner - romantic table"
        );

        reservationService.create(michaelReservation2)
                .subscribe();

        Thread.sleep(10000);
        final var unavailableID = "dfcbe98d-392b-4b93-9a49-27005223d15d";
        final var unavailableReservation = createTestReservation(
                unavailableID,
                "Michael Davis",
                2,
                "2025-06-16",
                "19:00",
                "Anniversary dinner - romantic table"
        );

        reservationService.create(unavailableReservation)
                .subscribe();

    }

    private ReservationDto createTestReservation(String restaurantId, String customerName,
                                                 int partySize, String date, String time, String notes) {
        return ReservationDto.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurantId)
                .customerName(customerName)
                .partySize(partySize)
                .date(date)
                .time(time)
                .notes(notes)
                .build();
    }

}
