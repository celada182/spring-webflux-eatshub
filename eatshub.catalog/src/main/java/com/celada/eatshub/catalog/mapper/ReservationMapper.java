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
    @Mapping(target = "notes", source = "comment")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "time", ignore = true)
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

    @AfterMapping
    default void splitDateTime(ReservationRequest source, @MappingTarget ReservationDto target) {
        if (Objects.nonNull(source.getDateTime()) && source.getDateTime().contains(",")) {
            String[] dateTime = source.getDateTime().split(",");
            target.setDate(dateTime[0]);
            target.setTime(dateTime[1]);
        }
    }
}
