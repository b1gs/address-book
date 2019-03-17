package com.epam.addressbook;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcAccommodationRepositoryTest {
    private AccommodationRepository subject;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"));

        subject = new JdbcAccommodationRepository(dataSource);

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("DELETE FROM ACCOMMODATION");

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void create_whenSelectFromRepository_returnsCreatedEntityOptional() {
        Accommodation newAccommodation = new Accommodation(123, 321, LocalDate.parse("2018-01-01"), true);
        Accommodation entry = subject.create(newAccommodation).get();


        Map<String, Object> foundAccommodation = jdbcTemplate.queryForMap("SELECT * FROM ACCOMMODATION WHERE ID = ?", entry.getId());


        assertThat(foundAccommodation.get("ID")).isEqualTo(entry.getId());
        assertThat(foundAccommodation.get("ADDRESS_ID")).isEqualTo(123L);
        assertThat(foundAccommodation.get("PERSON_ID")).isEqualTo(321L);
        assertThat(((Date) foundAccommodation.get("ACCOMMODATION_DATE")).toLocalDate()).isEqualTo(LocalDate.parse("2018-01-01"));
        assertThat(foundAccommodation.get("SINGLE_OWNED")).isEqualTo(true);
    }

    @Test
    public void getById_whenCreateAndFindEntity_returnsFoundEntityOptional() {
        jdbcTemplate.execute("INSERT INTO ACCOMMODATION (ID, ADDRESS_ID, PERSON_ID, ACCOMMODATION_DATE, SINGLE_OWNED) " +
                "VALUES (999, 123, 321, '2018-01-01', true)");


        Accommodation foundAccommodation = subject.getById(999L).get();


        assertThat(foundAccommodation.getId()).isEqualTo(999L);
        assertThat(foundAccommodation.getAddressId()).isEqualTo(123L);
        assertThat(foundAccommodation.getPersonId()).isEqualTo(321L);
        assertThat(foundAccommodation.getAccommodationDate()).isEqualTo(LocalDate.parse("2018-01-01"));
        assertThat(foundAccommodation.isSingleOwned()).isEqualTo(true);
    }

    @Test
    public void getById_whenNoFoundEntity_returnsEmptyOptional() {
        Optional<Accommodation> accommodationOptional = subject.getById(100500L);


        assertThat(accommodationOptional).isEqualTo(Optional.empty());
    }

    @Test
    public void findAll_whenEntityAreCreatedAndFoundEntityList_returnsEntities() {
        jdbcTemplate.execute(
                "INSERT INTO ACCOMMODATION (ID, ADDRESS_ID, PERSON_ID, ACCOMMODATION_DATE, SINGLE_OWNED) " +
                        "VALUES (100, 123, 321, '2018-01-01', true), (500, 456, 654, '2018-10-10', false)"
        );


        List<Accommodation> accommodations = subject.findAll().get();


        assertThat(accommodations.size()).isEqualTo(2);

        Accommodation firstAccommodation = accommodations.get(0);
        assertThat(firstAccommodation.getId()).isEqualTo(100L);
        assertThat(firstAccommodation.getAddressId()).isEqualTo(123L);
        assertThat(firstAccommodation.getPersonId()).isEqualTo(321L);
        assertThat(firstAccommodation.getAccommodationDate()).isEqualTo(LocalDate.parse("2018-01-01"));
        assertThat(firstAccommodation.isSingleOwned()).isEqualTo(true);

        Accommodation secondAccommodation = accommodations.get(1);
        assertThat(secondAccommodation.getId()).isEqualTo(500L);
        assertThat(secondAccommodation.getAddressId()).isEqualTo(456L);
        assertThat(secondAccommodation.getPersonId()).isEqualTo(654L);
        assertThat(secondAccommodation.getAccommodationDate()).isEqualTo(LocalDate.parse("2018-10-10"));
        assertThat(secondAccommodation.isSingleOwned()).isEqualTo(false);
    }

    @Test
    public void update_returnsUpdatedEntity() {
        jdbcTemplate.execute(
                "INSERT INTO ACCOMMODATION (ID, ADDRESS_ID, PERSON_ID, ACCOMMODATION_DATE, SINGLE_OWNED) " +
                        "VALUES (100500, 123, 321, '2018-01-01', true)");

        Accommodation accommodationToUpdate = new Accommodation(456, 654, LocalDate.parse("2018-10-10"), false);


        Accommodation updatedAccommodation = subject.update(100500L, accommodationToUpdate).get();


        assertThat(updatedAccommodation.getId()).isEqualTo(100500L);
        assertThat(updatedAccommodation.getAddressId()).isEqualTo(456L);
        assertThat(updatedAccommodation.getPersonId()).isEqualTo(654L);
        assertThat(updatedAccommodation.getAccommodationDate()).isEqualTo(LocalDate.parse("2018-10-10"));
        assertThat(updatedAccommodation.isSingleOwned()).isEqualTo(false);
    }

    @Test
    public void update_whenSelectFromRepository_returnsUpdatedEntity() {
        jdbcTemplate.execute(
                "INSERT INTO ACCOMMODATION (ID, ADDRESS_ID, PERSON_ID, ACCOMMODATION_DATE, SINGLE_OWNED) " +
                        "VALUES (100500, 123, 321, '2018-01-01', true)");

        Accommodation accommodationToUpdate = new Accommodation(456, 654, LocalDate.parse("2018-10-10"), false);

        Accommodation updatedAccommodation = subject.update(100500L, accommodationToUpdate).get();

        Map<String, Object> foundAccommodation = jdbcTemplate.queryForMap("SELECT * FROM ACCOMMODATION WHERE ID = ?", updatedAccommodation.getId());

        assertThat(foundAccommodation.get("ID")).isEqualTo(updatedAccommodation.getId());
        assertThat(foundAccommodation.get("ADDRESS_ID")).isEqualTo(456L);
        assertThat(foundAccommodation.get("PERSON_ID")).isEqualTo(654L);
        assertThat(((Date) foundAccommodation.get("ACCOMMODATION_DATE")).toLocalDate()).isEqualTo(LocalDate.parse("2018-10-10"));
        assertThat(foundAccommodation.get("SINGLE_OWNED")).isEqualTo(false);
    }

    @Test
    public void delete_whenInsertOneEntity_returnsEmptyList() {
        jdbcTemplate.execute(
                "INSERT INTO ACCOMMODATION (ID, ADDRESS_ID, PERSON_ID, ACCOMMODATION_DATE, SINGLE_OWNED) " +
                        "VALUES (999, 123, 321, '2018-01-01', true)"
        );

        subject.delete(999L);

        Map<String, Object> foundEntities = jdbcTemplate.queryForMap("SELECT COUNT(*) c FROM ACCOMMODATION WHERE ID = ?", 999);
        assertThat(foundEntities.get("c")).isEqualTo(0L);
    }
}