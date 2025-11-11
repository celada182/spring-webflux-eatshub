package com.celada.eatshub.catalog.mapper;

import com.celada.eatshub.catalog.domain.ReservationRequest;
import com.celada.eatshub.catalog.domain.ReservationResponse;
import com.celada.eatshub.catalog.repository.model.ReservationDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationDto toDto(ReservationRequest source);

    @Mapping(target = "dateTime", expression = "java(source.getDate() + \",\" + source.getTime())")
    ReservationResponse toResponse(ReservationDto source);

    @Mapping(target = "notes", source = "comment", defaultValue = "")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "time", ignore = true)
    default Flux<ReservationResponse> toResponse(Flux<ReservationDto> source) {
        return source.map(this::toResponse);
    }

    default Mono<ReservationResponse> toResponse(Mono<ReservationDto> source) {
        return source.map(this::toResponse);
    }

    default Mono<ReservationDto> toDto(Mono<ReservationRequest> source) {
        return source.map(this::toDto);
    }

    default Flux<ReservationDto> toDto(Flux<ReservationRequest> source) {
        return source.map(this::toDto);
    }

    @AfterMapping
    default void splitDateTime(ReservationRequest request, @MappingTarget ReservationDto dto) {
        if (Objects.nonNull(request) && Objects.nonNull(request.getDateTime()) && request.getDateTime().contains(",")) {
            var split = request.getDateTime().split(",");
            dto.setDate(split[0]);
            dto.setTime(split[1]);
        }
    }
}
