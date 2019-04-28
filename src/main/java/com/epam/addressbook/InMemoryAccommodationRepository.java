package com.epam.addressbook;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryAccommodationRepository implements AccommodationRepository {

    public static long sequence = 1;

    private Map<Long,Accommodation> database = new HashMap<>();

    public Optional<List<Accommodation>> findAll(){
        if (database.size() > 0 ){
        return Optional.of(new ArrayList<Accommodation>(database.values()));
        }
        return Optional.empty();
    }

    public Optional<Accommodation> getById(Long accommodationId){
        return Optional.of(database.get(accommodationId));
    }

    public Optional<Accommodation> create(Accommodation accommodation){
        long accommodationId = sequence++;
        accommodation.setId(accommodationId);
        database.put(accommodationId ,accommodation);
        return Optional.of(accommodation);
    }

    public Optional<Accommodation> update(Long accommodationId , Accommodation accommodation){
        if (database.get(accommodationId) == null ){
            return Optional.empty();
        }
        database.put(accommodationId , accommodation);
        return Optional.of(accommodation);
    }

    public void delete(Long accommodationId){
        database.remove(accommodationId);
    }

}
