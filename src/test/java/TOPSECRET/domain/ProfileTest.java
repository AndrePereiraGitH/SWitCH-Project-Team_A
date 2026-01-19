package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Country;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class ProfileTest {
    @Test
    void shouldCreateProfileWithValidData() {
        Profile profile = new Profile(
                "Test User",
                Country.PORTUGAL,
                LocalDate.of(2000, 5, 10)
        );

        assertEquals("Test User", profile.getName());
        assertEquals(Country.PORTUGAL, profile.getCountry());
        assertEquals(LocalDate.of(2000, 5, 10), profile.getBirthday());
    }

    @Test
    void shouldFailWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile(null, Country.PORTUGAL, LocalDate.now());
        });
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("   ", Country.FRANCE, LocalDate.now());
        });
    }

    @Test
    void shouldFailWhenCountryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("Valid Name", null, LocalDate.now());
        });
    }

    @Test
    void shouldFailWhenBirthdayIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("Valid Name", Country.GERMANY, null);
        });
    }
}