package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.ValueObject;

import java.util.Objects;

/**
 * Represents an address with street, number, zip code, and city.
 * <p>
 * This class validates required fields in the constructor.
 */

public class Address implements ValueObject {

    private final String street;
    private final String number;
    private final String zipCode;
    private final CityID cityID;

    /**
     * Constructs a new {@code Address} with the given fields.
     *
     * @param street  the street name; must not be {@code null} or blank
     * @param number  the address number; must not be {@code null} or blank
     * @param zipCode the zip/postal code; must not be {@code null} or blank
     * @param cityID    the city; must not be {@code null}
     * @throws IllegalArgumentException if any field is {@code null}, or if street,
     *                                  number, or zip code are blank
     */
    public Address(String street, String number, String zipCode, CityID cityID) {

        if (street == null || street.isBlank() || number == null || number.isBlank() || zipCode == null
                || zipCode.isBlank() || cityID == null) {
            throw new IllegalArgumentException("Street, number, zip Code and city are all required fields.");
        }

        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
        this.cityID = cityID;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public CityID getCityID() {
        return cityID;
    }

    @Override
    public String toString() {
        return street + ", no. " + number + ", " + zipCode + ", " + cityID.getCityID();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street)
                && Objects.equals(number, address.number)
                && Objects.equals(zipCode, address.zipCode)
                && Objects.equals(cityID, address.cityID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, zipCode, cityID);
    }
}

