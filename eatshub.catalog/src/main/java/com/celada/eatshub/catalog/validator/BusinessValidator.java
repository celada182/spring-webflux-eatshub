package com.celada.eatshub.catalog.validator;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface BusinessValidator<T> {
    Mono<Void> validate(T input);
}
