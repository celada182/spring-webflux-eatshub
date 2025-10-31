package com.celada.eatshub.catalog.repository.records;

import lombok.Builder;

@Builder
public record Address(
        String street,
        String city,
        String postalCode
) {
}
