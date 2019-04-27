package com.epam.addressbook;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AccommodationController {

    private ObjectMapper mapper = new ObjectMapper();
    private AccommodationRepository repository;

    public AccommodationController(AccommodationRepository repository) {
        this.repository = repository;
    }

    @GetMapping("accommodations")
    public ResponseEntity<List<Accommodation>> findAll() {
        Optional<List<Accommodation>> all = repository.findAll();
        if (all.isPresent()) {
            return ResponseEntity.ok(all.get());
        }
        return new ResponseEntity<List<Accommodation>>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("accommodations/{id}")
    public ResponseEntity<Accommodation> getById(@PathVariable("id") Long id) {
        Optional<Accommodation> optional = repository.getById(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        }
        return new ResponseEntity<Accommodation>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("accommodations")
    public ResponseEntity<Accommodation> create(Accommodation accommodation) {
        repository.create(accommodation);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("accommodations")
    public ResponseEntity<Accommodation> update(Long accommodationId, Accommodation accommodation) {
        Optional<Accommodation> updated = repository.update(accommodationId, accommodation);
        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        }
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public ResponseEntity<Accommodation> delete(Long accommodationId) {
        repository.delete(accommodationId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
