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
    @Mapping(target = "date", expression = "java(extractDate(source.getDateTime()))")
    @Mapping(target = "time", expression = "java(extractTime(source.getDateTime()))")
    @Mapping(target = "notes", source = "comment", defaultValue = "")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    ReservationDto toDto(ReservationRequest source);

    @Mapping(target = "dateTime", expression = "java(source.getDate() + \",\" + source.getTime())")
    ReservationResponse toResponse(ReservationDto source);

    default Flux<ReservationResponse> toResponseFlux(Flux<ReservationDto> source) {
        return source.map(this::toResponse);
    }

    default Mono<ReservationResponse> toResponseMono(Mono<ReservationDto> source) {
        return source.map(this::toResponse);
    }

    default Mono<ReservationDto> toDtoMono(Mono<ReservationRequest> source) {
        return source.map(this::toDto);
    }

    default Flux<ReservationDto> toDtoFlux(Flux<ReservationRequest> source) {
        return source.map(this::toDto);
    }

    default String extractDate(String dateTime) {
        return dateTime.split(",")[0];
    }

    default String extractTime(String dateTime) {
        return dateTime.split(",")[1];
    }
}
