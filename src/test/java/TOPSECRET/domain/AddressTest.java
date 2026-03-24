package TOPSECRET.domain;

import org.junit.jupiter.params.provider.ValueSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Address} class.
 *
 * <p>This test suite validates the behavior of the {@link Address} entity,
 * focusing on constructor validation, state mutation through setters,
 * and correct string representation.</p>
 *
 * <p>The {@link City} dependency is mocked to ensure isolation and to avoid
 * coupling tests to the behavior of external domain objects.</p>
 */

public class AddressTest {

    //Double for City class
    private final City anyCity = mock(City.class);

    /**
     * Verifies that the constructor throws an {@link IllegalArgumentException}
     * when the street parameter is null, empty, or blank.
     *
     * @param street invalid street values (null, empty, or whitespace)
     */

    @ParameterizedTest //needs to be Parameterized here to test different inputs for multiple tests
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})

    // Ensures constructor rejects null, empty, and blank street values

    void constructorIfCalledWithInvalidStreetThrowsException(String street) {
        assertThrows(IllegalArgumentException.class, () ->
                new Address(street, "dummyNumber", "dummyZipCode", anyCity));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void constructorIfNumberIsInvalidThrowsException(String number) {
        assertThrows(IllegalArgumentException.class, () ->
                new Address("street", number, "zip", anyCity));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void constructorWhenZipCodeIsInvalidThrowsException(String zipCode) {
        assertThrows(IllegalArgumentException.class, () ->
                new Address("street", "number", zipCode, anyCity));
    }

    @Test
    void constructorIfCityIsNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Address("street", "number", "zip", null));
    }

    @Test
    void constructorWithValidArgumentsSetsFieldsCorrectly() {
        Address address = new Address("street", "number", "zip", anyCity);

        assertEquals("street", address.getStreet());
        assertEquals("number", address.getNumber());
        assertEquals("zip", address.getZipCode());
        assertEquals(anyCity, address.getCity());
    }

    @Test
    void setCityShouldUpdateCityCorrectly() {
        City newCity = mock(City.class);

        Address address = new Address("street", "number", "zip", anyCity);
        address.setCity(newCity);

        assertEquals(newCity, address.getCity());
    }

    @Test
    void setStreetShouldUpdateStreetCorrectly() {
        Address address = new Address("Old Street", "10", "1000-001", anyCity);

        address.setStreet("New Street");

        assertEquals("New Street", address.getStreet());
    }

    @Test
    void setNumberShouldUpdateNumberCorrectly() {
        Address address = new Address("Street", "10", "1000-001", anyCity);

        address.setNumber("20");

        assertEquals("20", address.getNumber());
    }

    @Test
    void setZipCodeShouldUpdateZipCodeCorrectly() {
        Address address = new Address("Street", "10", "1000-001", anyCity);

        address.setZipCode("2000-002");

        assertEquals("2000-002", address.getZipCode());
    }


    @Test
    void toStringShouldReturnFormattedString() {

        City city = mock(City.class);
        when(city.getCityName()).thenReturn("Lisbon");

        Address address = new Address("Rua X", "123", "4795-013", city);

        String result = address.toString();

        assertEquals("Rua X, no. 123, 4795-013, Lisbon", result);
    }

}

