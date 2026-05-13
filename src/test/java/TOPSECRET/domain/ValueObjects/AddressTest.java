package TOPSECRET.domain.ValueObjects;

import TOPSECRET.domain.City.City;
import org.junit.jupiter.params.provider.ValueSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Address} Value Object.
 * <p>
 * This test suite validates the behaviour of {@link Address} regarding:
 * </p>
 * <ul>
 *     <li>Constructor validation — null, empty, and blank inputs</li>
 *     <li>Getters — correct return of each field</li>
 *     <li>Equality — same data, different data, null, and different class</li>
 *     <li>Hash code — consistency with equals</li>
 *     <li>String representation — correct format</li>
 * </ul>
 * <p>
 * The {@link City} dependency is mocked to ensure isolation from external domain objects.
 * </p>
 */

public class AddressTest {

    // Double for City class
    private final CityId anyCity = mock(CityId.class);

    // --- Constructor validation ---

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void constructorIfCalledWithInvalidStreetThrowsException(String street) {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Address(street, "dummyNumber", "dummyZipCode", anyCity));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void constructorIfNumberIsInvalidThrowsException(String number) {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Address("street", number, "zip", anyCity));
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void constructorWhenZipCodeIsInvalidThrowsException(String zipCode) {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Address("street", "number", zipCode, anyCity));
    }


    @Test
    void constructorIfCityIsNullThrowsException() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Address("street", "number", "zip", null));
    }

    // --- Getters ---

    @Test
    void constructorWithValidArgumentsSetsFieldsCorrectly() {

        // Arrange & Act
        Address address = new Address("street", "number", "zip", anyCity);

        // Assert
        assertEquals("street", address.getStreet());
        assertEquals("number", address.getNumber());
        assertEquals("zip", address.getZipCode());
        assertEquals(anyCity, address.getCityID());
    }

    // --- toString ---
    @Test
    void toStringShouldReturnFormattedString() {

        // Arrange
        City city = mock(City.class);
        when(city.getCityName()).thenReturn("Lisbon");
        Address address = new Address("Rua X", "123", "4795-013", new CityId("Lisbon"));

        // Act
        String result = address.toString();

        // Assert
        assertEquals("Rua X, no. 123, 4795-013, Lisbon", result);
    }

    // --- equals ---
    @Test
    void equals_shouldReturnTrue_whenSameReference() {

        // Arrange
        Address address = new Address("street", "number", "zip", anyCity);

        // Act & Assert
        assertTrue(address.equals(address));
    }

    @Test
    void equals_shouldReturnTrue_whenSameData() {

        // Arrange
        Address address1 = new Address("street", "number", "zip", anyCity);
        Address address2 = new Address("street", "number", "zip", anyCity);

        // Act
        boolean result = address1.equals(address2);

        // Assert
        assertTrue(result);
    }

    @Test
    void equals_shouldReturnFalse_whenNull() {

        // Arrange
        Address address = new Address("street", "number", "zip", anyCity);

        // Act
        boolean result = address.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentClass() {

        // Arrange
        Address address = new Address("street", "number", "zip", anyCity);

        // Act
        boolean result = address.equals("not an address");

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalse_whenStreetDiffers() {

        // Arrange
        Address address1 = new Address("street A", "number", "zip", anyCity);
        Address address2 = new Address("street B", "number", "zip", anyCity);

        // Act
        boolean result = address1.equals(address2);

        // Assert
        assertFalse(result);
    }


    @Test
    void equals_shouldReturnFalse_whenNumberDiffers() {

        // Arrange
        Address address1 = new Address("street", "10", "zip", anyCity);
        Address address2 = new Address("street", "20", "zip", anyCity);

        // Act
        boolean result = address1.equals(address2);

        // Assert
        assertFalse(result);
    }


    @Test
    void equals_shouldReturnFalse_whenZipCodeDiffers() {

        // Arrange
        Address address1 = new Address("street", "number", "1000", anyCity);
        Address address2 = new Address("street", "number", "2000", anyCity);

        // Act
        boolean result = address1.equals(address2);

        // Assert
        assertFalse(result);
    }


    @Test
    void equals_shouldReturnFalse_whenCityDiffers() {

        // Arrange
        CityId otherCity = mock(CityId.class);
        Address address1 = new Address("street", "number", "zip", anyCity);
        Address address2 = new Address("street", "number", "zip", otherCity);

        // Act
        boolean result = address1.equals(address2);

        // Assert
        assertFalse(result);
    }

    // --- hashCode ---
    @Test
    void hashCode_shouldBeEqual_whenSameData() {

        // Arrange
        Address address1 = new Address("street", "number", "zip", anyCity);
        Address address2 = new Address("street", "number", "zip", anyCity);

        // Act & Assert
        assertEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    void hashCode_shouldNotBeZero_forValidAddress() {

        // Arrange
        Address address = new Address("street", "number", "zip", anyCity);

        // Act & Assert
        assertNotEquals(0, address.hashCode());
    }
}



