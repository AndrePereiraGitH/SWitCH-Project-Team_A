package TOPSECRET.domain;

/**
 * Represents an address with street, number, zip code, and city.
 * <p>
 * This class validates required fields in the constructor.
 */

public class Address {

    private String street;
    private String number;
    private String zipCode;
    private City city;

    /**
     * Creates a new address.
     *
     * @param street  street name (required, must not be null or blank)
     * @param number  address number (required, must not be null or blank)
     * @param zipCode zip/postal code (required, must not be null or blank)
     * @param city    city (required, must not be null)
     * @throws IllegalArgumentException if any required field is invalid
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

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return street + ", no. " + number + ", " + zipCode + ", " + city.getCityName();
    }
}

