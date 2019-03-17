package com.epam.addressbook;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AccommodationControllerTest {
    private AccommodationRepository accommodationRepository;
    private AccommodationController subject;

    @Before
    public void setUp() {
        accommodationRepository = mock(AccommodationRepository.class);
        subject = new AccommodationController(accommodationRepository);
    }

    @Test
    public void create_returnsCreatedEntityAndHttpStatusIsOk() {
        long addressId = 123L;
        long personId = 456L;
        Accommodation accommodationToCreate = new Accommodation(addressId, personId, LocalDate.parse("2018-10-10"), true);

        long accommodationId = 1L;
        Accommodation expectedResult = new Accommodation(accommodationId, addressId, personId, LocalDate.parse("2018-10-10"), true);
        doReturn(Optional.of(expectedResult))
                .when(accommodationRepository)
                .create(any(Accommodation.class));


        ResponseEntity response = subject.create(accommodationToCreate);


        verify(accommodationRepository).create(accommodationToCreate);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expectedResult);
    }

    @Test
    public void getById_whenEntityExists_returnsFoundEntityAndHttpStatusIsOk() {
        long accommodationId = 1L;
        long addressId = 123L;
        long personId = 456L;
        Accommodation expected = new Accommodation(accommodationId, addressId, personId, LocalDate.parse("2018-10-10"), true);
        doReturn(Optional.of(expected))
                .when(accommodationRepository)
                .getById(accommodationId);


        ResponseEntity<Accommodation> response = subject.getById(accommodationId);


        verify(accommodationRepository).getById(accommodationId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void getById_whenEntityNotExists_returnsNotFound() {
        long nonExistentAccommodationId = 1L;
        doReturn(Optional.empty())
                .when(accommodationRepository)
                .getById(nonExistentAccommodationId);


        ResponseEntity<Accommodation> response = subject.getById(nonExistentAccommodationId);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void findAll_whenEntitiesAreExist_returnsEntityListAndHttpStatusIsOk() {
        List<Accommodation> expected = asList(
                new Accommodation(1L, 123L, 456L, LocalDate.parse("2018-10-10"), true),
                new Accommodation(2L, 789L, 321L, LocalDate.parse("2018-01-01"), false)
        );
        doReturn(Optional.of(expected)).when(accommodationRepository).findAll();


        ResponseEntity<List<Accommodation>> response = subject.findAll();


        verify(accommodationRepository).findAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void update_whenEntityForUpdateExists_returnsUpdatedEntityAndHttpStatusIsOk() {
        long accommodationId = 1L;
        long addressId = 987L;
        long personId = 654L;

        Accommodation expected = new Accommodation(accommodationId, addressId, personId, LocalDate.parse("2018-01-01"), true);

        doReturn(Optional.of(expected))
                .when(accommodationRepository)
                .update(eq(accommodationId), any(Accommodation.class));


        ResponseEntity response = subject.update(accommodationId, expected);


        verify(accommodationRepository).update(accommodationId, expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void update_whenEntityToUpdateNotExists_returnsUnprocessableEntity() {
        long nonExistentAccommodationId = 1L;
        doReturn(Optional.empty())
                .when(accommodationRepository)
                .update(eq(nonExistentAccommodationId), any(Accommodation.class));


        ResponseEntity response = subject.update(nonExistentAccommodationId, new Accommodation());


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void delete_returnsNoContent() {
        long accommodationId = 1L;


        ResponseEntity<Accommodation> response = subject.delete(accommodationId);


        verify(accommodationRepository).delete(accommodationId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}