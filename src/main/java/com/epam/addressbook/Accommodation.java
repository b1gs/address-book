package com.epam.addressbook;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Accommodation {

    private Long id;
    private Long personId;
    private Long addressId;
    private LocalDate accommodationDate;
    private Boolean singleOwned;

    public Accommodation() {
    }

    public Accommodation(Long addressId, Long personId, LocalDate accommodationDate, Boolean singleOwned) {
        this.personId = personId;
        this.addressId = addressId;
        this.accommodationDate = accommodationDate;
        this.singleOwned = singleOwned;
    }

    public Accommodation(Long accommodationId, Long addressId,Long personId, LocalDate accommodationDate, Boolean singleOwned) {
        this.personId = personId;
        this.addressId = addressId;
        this.accommodationDate = accommodationDate;
        this.singleOwned = singleOwned;
        this.id = accommodationId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAccommodationDate() {
        return accommodationDate;
    }

    public void setAccommodationDate(LocalDate accommodationDate) {
        this.accommodationDate = accommodationDate;
    }

    public Boolean isSingleOwned() {
        return singleOwned;
    }

    public void setSingleOwned(Boolean singleOwned) {
        this.singleOwned = singleOwned;
    }
}
