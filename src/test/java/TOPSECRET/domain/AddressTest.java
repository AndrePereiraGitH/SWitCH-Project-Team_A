package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.params.provider.ValueSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

    public class AddressTest {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t", "\n"})
        void constructor_whenCalledWithInvalidStreet_ThrowsException(String street) {
            assertThrows(IllegalArgumentException.class, () -> new Address(street, "dummyNumber", "dummyZipCode", new Country("Portugal")));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t", "\n"})
        void constructor_whenCalledWithInvalidNumber_ThrowsException(String number) {
            assertThrows(IllegalArgumentException.class, () -> new Address("dummyStreet", number, "dummyZipCode", new Country("Portugal")));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t", "\n"})
        void constructor_whenCalledWithInvalidZipCode_ThrowsException(String zipCode) {
            assertThrows(IllegalArgumentException.class, () -> new Address("dummyStreet", "dummyNumber", zipCode, new Country("Portugal")));
        }

        @ParameterizedTest
        @NullSource
        void constructor_whenCalledWithInvalidCountry_ThrowsException(Countries country) {
            assertThrows(IllegalArgumentException.class, () -> new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("Portugal")));
        }

        @Test
        void constructor_whenCalledWithValidParameters_DoesNotThrowExceptionAndSetsValues() {
            Address address = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("Portugal"));

            assertEquals("dummyStreet", address.getStreet());
            assertEquals("dummyNumber", address.getNumber());
            assertEquals("dummyZipCode", address.getZipCode());
            assertEquals(new Country("Portugal"), address.getCountry());
        }

        @Test
        void getStreet_whenCalled_ReturnsExpectedStreet() {
            String street = "Rua X";

            Address address = new Address(street, "dummyNumber", "dummyZipCode", new Country("Portugal"));

            assertEquals(street, address.getStreet());
        }

        @Test
        void setStreet_whenCalled_SetsExpectedStreet() {
            String street = "Rua X";

            Address address = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("Portugal"));
            address.setStreet(street);

            assertEquals(street, address.getStreet());
        }

        @Test
        void getNumber_whenCalled_ReturnsExpectedNumber() {
            String number = "123";

            Address address = new Address("dummyStreet", number, "dummyZipCode", new Country("Portugal"));

            assertEquals(number, address.getNumber());
        }

        @Test
        void setNumber_whenCalled_SetsExpectedNumber() {
            String number = "123";

            Address address = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("Portugal"));
            address.setNumber(number);

            assertEquals(number, address.getNumber());
        }

        @Test
        void getZipCode_whenCalled_ReturnsExpectedZipCode() {
            String zipCode = "4795-013";

            Address address = new Address("dummyStreet", "dummyNumber", zipCode, new Country("Portugal"));

            assertEquals(zipCode, address.getZipCode());
        }

        @Test
        void setZipCode_whenCalled_SetsExpectedZipCode() {
            String zipCode = "4795-013";

            Address address = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("Portugal"));
            address.setZipCode(zipCode);

            assertEquals(zipCode, address.getZipCode());
        }

        @Test
        void getCountry_whenCalled_ReturnsExpectedCountry() {
            Country country = new Country("Portugal");

            Address address = new Address("dummyStreet", "dummyNumber", "dummyZipCode", country);

            assertEquals(country, address.getCountry());
        }

        @Test
        void setCountry_whenCalled_SetsExpectedCountry() {
            Country country = new Country("Portugal");

            Address address = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("Portugal"));
            address.setCountry(country);

            assertEquals(country, address.getCountry());
        }

        @Test
        void toString_whenCalled_ReturnsExpectedString() {
            Address address = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("Portugal"));

            assertEquals("dummyStreet, no. dummyNumber, dummyZipCode, PORTUGAL", address.toString());
        }
    }

