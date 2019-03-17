package com.epam.addressbook;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AddressBookApplication.class, webEnvironment = RANDOM_PORT)
public class AccommodationApiSpringBootTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final long addressId = 123L;
    private final long personId = 456L;

    private Accommodation accommodation = new Accommodation(addressId, personId, LocalDate.parse("2017-01-08"), true);

    @Test
    public void create_returnsCreatedEntityAndHttpStatusIsCreated() {
        ResponseEntity<String> createResponse = restTemplate.postForEntity("/accommodations", accommodation, String.class);


        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext createJson = parse(createResponse.getBody());
        assertThat(createJson.read("$.id", Long.class)).isGreaterThan(0);
        assertThat(createJson.read("$.addressId", Long.class)).isEqualTo(addressId);
        assertThat(createJson.read("$.personId", Long.class)).isEqualTo(personId);
        assertThat(createJson.read("$.accommodationDate", String.class)).isEqualTo("2017-01-08");
        assertThat(createJson.read("$.singleOwned", Boolean.class)).isEqualTo(true);
    }

    @Test
    public void getById_returnsFoundEntityAndHttpStatusIsOk() {
        Accommodation accommodation = createAndPostAccommodation();


        ResponseEntity<String> readResponse = restTemplate.getForEntity("/accommodations/" + accommodation.getId(), String.class);


        assertThat(readResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext readJson = parse(readResponse.getBody());
        assertThat(readJson.read("$.id", Long.class)).isEqualTo(accommodation.getId());
        assertThat(readJson.read("$.addressId", Long.class)).isEqualTo(accommodation.getAddressId());
        assertThat(readJson.read("$.personId", Long.class)).isEqualTo(accommodation.getPersonId());
        assertThat(readJson.read("$.accommodationDate", String.class)).isEqualTo(accommodation.getAccommodationDate().toString());
        assertThat(readJson.read("$.singleOwned", Boolean.class)).isEqualTo(accommodation.isSingleOwned());
    }

    @Test
    public void findAll_returnsFoundEntityListAndHttpStatusIsOk() {
        Accommodation firstAccommodation = createAndPostAccommodation();
        Accommodation secondAccommodation = createAndPostAccommodation();


        ResponseEntity<String> readResponse = restTemplate.getForEntity("/accommodations/", String.class);


        assertThat(readResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext readJson = parse(readResponse.getBody());

        assertThat(readJson.read("$[0].id", Long.class)).isEqualTo(firstAccommodation.getId());
        assertThat(readJson.read("$[0].addressId", Long.class)).isEqualTo(firstAccommodation.getAddressId());
        assertThat(readJson.read("$[0].personId", Long.class)).isEqualTo(firstAccommodation.getPersonId());
        assertThat(readJson.read("$[0].accommodationDate", String.class)).isEqualTo(firstAccommodation.getAccommodationDate().toString());
        assertThat(readJson.read("$[0].singleOwned", Boolean.class)).isEqualTo(firstAccommodation.isSingleOwned());

        assertThat(readJson.read("$[1].id", Long.class)).isEqualTo(secondAccommodation.getId());
        assertThat(readJson.read("$[1].addressId", Long.class)).isEqualTo(secondAccommodation.getAddressId());
        assertThat(readJson.read("$[1].personId", Long.class)).isEqualTo(secondAccommodation.getPersonId());
        assertThat(readJson.read("$[1].accommodationDate", String.class)).isEqualTo(secondAccommodation.getAccommodationDate().toString());
        assertThat(readJson.read("$[1].singleOwned", Boolean.class)).isEqualTo(secondAccommodation.isSingleOwned());
    }

    @Test
    public void update_returnsUpdatedEntityAndHttpStatusIsOk() {
        Long id = createAndPostAccommodation().getId();

        long addressId = 2L;
        long personId = 3L;
        String dateString = "2018-01-01";
        boolean singleOwned = true;

        Accommodation updatedAccommodation = new Accommodation(addressId, personId, LocalDate.parse(dateString), singleOwned);


        ResponseEntity<String> updateResponse = restTemplate.exchange("/accommodations/" + id, HttpMethod.PUT, new HttpEntity<>(updatedAccommodation, null), String.class);


        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext updateJson = parse(updateResponse.getBody());
        assertThat(updateJson.read("$.id", Long.class)).isEqualTo(id);
        assertThat(updateJson.read("$.addressId", Long.class)).isEqualTo(updatedAccommodation.getAddressId());
        assertThat(updateJson.read("$.personId", Long.class)).isEqualTo(updatedAccommodation.getPersonId());
        assertThat(updateJson.read("$.accommodationDate", String.class)).isEqualTo(updatedAccommodation.getAccommodationDate().toString());
        assertThat(updateJson.read("$.singleOwned", Boolean.class)).isEqualTo(updatedAccommodation.isSingleOwned());
    }

    @Test
    public void delete_returnsNoContent() {
        Long id = createAndPostAccommodation().getId();


        ResponseEntity<String> deleteResponse = restTemplate.exchange("/accommodations/" + id, HttpMethod.DELETE, null, String.class);


        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> deletedReadResponse = restTemplate.getForEntity("/accommodations/" + id, String.class);
        assertThat(deletedReadResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Accommodation createAndPostAccommodation() {
        HttpEntity<Accommodation> entity = new HttpEntity<>(accommodation);


        ResponseEntity<Accommodation> response = restTemplate.exchange("/accommodations", HttpMethod.POST, entity, Accommodation.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        return response.getBody();
    }
}