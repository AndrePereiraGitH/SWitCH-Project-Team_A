package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.ValueObject;
import TOPSECRET.domain.City;

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
    private final City city;

    /**
     * Constructs a new {@code Address} with the given fields.
     *
     * @param street  the street name; must not be {@code null} or blank
     * @param number  the address number; must not be {@code null} or blank
     * @param zipCode the zip/postal code; must not be {@code null} or blank
     * @param city    the city; must not be {@code null}
     * @throws IllegalArgumentException if any field is {@code null}, or if street,
     *                                  number, or zip code are blank
     */
    public Address(String street, String number, String zipCode, City city) {

        if (street == null || street.isBlank() || number == null || number.isBlank() || zipCode == null
                || zipCode.isBlank() || city == null) {
            throw new IllegalArgumentException("Street, number, zip Code and city are all required fields.");
        }

        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
        this.city = city;
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

    public City getCity() {
        return city;
    }

    @Override
    public String toString() {
        return street + ", no. " + number + ", " + zipCode + ", " + city.getCityName();
    }

    /**
     * Indicates whether some other object is equal to this {@code Address}.
     * <p>
     * Two {@code Address} instances are considered equal if all their fields
     * (street, number, zip code, and city) are equal.
     * </p>
     *
     * @param o the object to compare with
     * @return {@code true} if the given object is an {@code Address} with the same fields;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street)
                && Objects.equals(number, address.number)
                && Objects.equals(zipCode, address.zipCode)
                && Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, zipCode, city);
    }
}

