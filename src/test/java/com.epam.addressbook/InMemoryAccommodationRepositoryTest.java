package com.epam.addressbook;

import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryAccommodationRepositoryTest {
    @Test
    public void create_returnsCreatedEntityOptional() {
        InMemoryAccommodationRepository repo = new InMemoryAccommodationRepository();

        long addressId = 123L;
        long personId = 456L;


        Accommodation createdAccommodation = repo.create(new Accommodation(addressId, personId, LocalDate.parse("2018-01-01"), true)).get();


        long accommodationId = 1L;
        Accommodation expected = new Accommodation(accommodationId, addressId, personId, LocalDate.parse("2018-01-01"), true);
        assertThat(createdAccommodation).isEqualTo(expected);

        Accommodation readEntry = repo.getById(createdAccommodation.getId()).get();
        assertThat(readEntry).isEqualTo(expected);
    }

    @Test
    public void getById_returnFoundEntityOptional() {
        InMemoryAccommodationRepository repo = new InMemoryAccommodationRepository();

        long addressId = 123L;
        long personId = 456L;


        repo.create(new Accommodation(addressId, personId, LocalDate.parse("2018-10-10"), false));


        long accommodationId = 1L;
        Accommodation expected = new Accommodation(accommodationId, addressId, personId, LocalDate.parse("2018-10-10"), false);
        Accommodation readEntry = repo.getById(accommodationId).get();
        assertThat(readEntry).isEqualTo(expected);
    }

    @Test
    public void findAll_returnsEntityListOptional() {
        InMemoryAccommodationRepository repo = new InMemoryAccommodationRepository();
        repo.create(new Accommodation(123L, 456L, LocalDate.parse("2018-10-10"), true));
        repo.create(new Accommodation(789L, 654L, LocalDate.parse("2018-01-01"), false));

        List<Accommodation> expected = asList(
                new Accommodation(1L, 123L, 456L, LocalDate.parse("2018-10-10"), true),
                new Accommodation(2L, 789L, 654L, LocalDate.parse("2018-01-01"), false)
        );


        assertThat(repo.findAll().get()).isEqualTo(expected);
    }

    @Test
    public void update_returnsUpdatedEntityOptional() {
        InMemoryAccommodationRepository repo = new InMemoryAccommodationRepository();
        Accommodation created = repo.create(new Accommodation(123L, 456L, LocalDate.parse("2018-10-10"), true)).get();


        Accommodation updatedEntry = repo.update(
                created.getId(),
                new Accommodation(321L, 654L, LocalDate.parse("2018-01-01"), false)).get();


        Accommodation expected = new Accommodation(created.getId(), 321L, 654L, LocalDate.parse("2018-01-01"), false);
        assertThat(updatedEntry).isEqualTo(expected);
        assertThat(repo.getById(created.getId()).get()).isEqualTo(expected);
    }

    @Test
    public void delete() {
        InMemoryAccommodationRepository repo = new InMemoryAccommodationRepository();

        long addressId = 123L;
        long personId = 456L;
        Accommodation created = repo.create(new Accommodation(addressId, personId, LocalDate.parse("2018-10-10"), true)).get();


        repo.delete(created.getId());


        assertThat(repo.findAll().get()).isEmpty();
    }
}