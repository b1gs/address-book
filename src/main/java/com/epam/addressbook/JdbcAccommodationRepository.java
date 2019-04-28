package com.epam.addressbook;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Component
public class JdbcAccommodationRepository implements AccommodationRepository {

    private JdbcTemplate template;

    private RowMapper<Accommodation> mapper = (rs, rowNum) -> new Accommodation(
            rs.getLong("ID"),
            rs.getLong("ADDRESS_ID"),
            rs.getLong("PERSON_ID"),
            rs.getDate("ACCOMMODATION_DATE").toLocalDate(),
            rs.getBoolean("SINGLE_OWNED")
    );

    private final ResultSetExtractor<Accommodation> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;

    public JdbcAccommodationRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Accommodation> create(Accommodation accommodation) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO ACCOMMODATION (ADDRESS_ID, PERSON_ID, ACCOMMODATION_DATE, SINGLE_OWNED) " +
                            "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );

            statement.setLong(1, accommodation.getAddressId());
            statement.setLong(2, accommodation.getPersonId());
            statement.setDate(3, Date.valueOf(accommodation.getAccommodationDate()));
            statement.setBoolean(4, accommodation.isSingleOwned());

            return statement;
        }, generatedKeyHolder);

        return getById(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public Optional<Accommodation> getById(Long id) {
        return Optional.ofNullable(template.query(
                "SELECT ID, ADDRESS_ID, PERSON_ID, ACCOMMODATION_DATE, SINGLE_OWNED FROM ACCOMMODATION WHERE id = ?",
                new Object[]{id},
                extractor));
    }

    @Override
    public Optional<List<Accommodation>> findAll() {
        return Optional.of(template.query("SELECT ID, ADDRESS_ID, PERSON_ID, ACCOMMODATION_DATE, SINGLE_OWNED FROM ACCOMMODATION", mapper));
    }

    @Override
    public Optional<Accommodation> update(Long id, Accommodation accommodation) {
        template.update("UPDATE ACCOMMODATION " +
                        "SET ADDRESS_ID = ?, PERSON_ID = ?, ACCOMMODATION_DATE = ?,  SINGLE_OWNED = ? " +
                        "WHERE ID = ?",
                accommodation.getAddressId(),
                accommodation.getPersonId(),
                Date.valueOf(accommodation.getAccommodationDate()),
                accommodation.isSingleOwned(),
                id);

        return getById(id);
    }

    @Override
    public void delete(Long id) {
        template.update("DELETE FROM ACCOMMODATION WHERE ID = ?", id);
    }
}
