package com.epam.addressbook;

import org.springframework.stereotype.Component;

import java.util.*;


public interface AccommodationRepository {

    Optional<List<Accommodation>> findAll();

    Optional<Accommodation> getById(Long accommodationId);

    Optional<Accommodation> create(Accommodation accommodation);

    Optional<Accommodation> update(Long accommodationId, Accommodation accommodation);

    void delete(Long accommodationId);


}