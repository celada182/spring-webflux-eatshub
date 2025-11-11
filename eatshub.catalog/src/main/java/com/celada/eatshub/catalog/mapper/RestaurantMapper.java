package com.celada.eatshub.catalog.mapper;

import com.celada.eatshub.catalog.domain.RestaurantResponse;
import com.celada.eatshub.catalog.repository.model.RestaurantDto;
import com.celada.eatshub.catalog.repository.model.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantDto toDto(RestaurantResponse source);

    @Mapping(target = "globalRating", expression = "java(calculateGlobalRating(source.getReviews()))")
    RestaurantResponse toResponse(RestaurantDto source);

    default Flux<RestaurantResponse> toResponse(Flux<RestaurantDto> source) {
        return source.map(this::toResponse);
    }

    default Mono<RestaurantResponse> toResponse(Mono<RestaurantDto> source) {
        return source.map(this::toResponse);
    }

    default Double calculateGlobalRating(List<ReviewDto> reviews) {
        if (Objects.isNull(reviews) || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .filter(Objects::nonNull)
                .mapToInt(ReviewDto::getRating)
                .average()
                .orElse(0.0);
    }
}
