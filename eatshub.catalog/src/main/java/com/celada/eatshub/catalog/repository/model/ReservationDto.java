package com.celada.eatshub.catalog.repository.model;

import com.celada.eatshub.catalog.repository.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Data Transfer Object for Reservation entity in MongoDB
 */
@Document(collection = "reservations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    @Id
    private UUID id;  // Using Binary for MongoDB UUID
    @Indexed
    private String restaurantId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String date;
    private String time;  // Storing as String in "HH:mm" format
    private Integer partySize;
    @Indexed
    private ReservationStatus status;  // Could be an enum in a real application
    private String notes;
}
