package com.celada.eatshub.catalog.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private String customerId;
    private String customerName;
    private Integer rating;
    private String comment;
    private String timestamp;
}
