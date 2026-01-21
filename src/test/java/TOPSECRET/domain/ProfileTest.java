package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class ProfileTest {
    @Test
    void shouldCreateProfileWithValidData() {
        Profile profile = new Profile(
                "Test User",
                new Country("Portugal"),
                LocalDate.of(2000, 5, 10)
        );

        assertEquals("Test User", profile.getName());
        assertEquals("Portugal", profile.getCountry().getCountryName());
        assertEquals(LocalDate.of(2000, 5, 10), profile.getBirthday());
    }

    @Test
    void shouldFailWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile(null, new Country("Spain"), LocalDate.now());
        });
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("   ", new Country("France"), LocalDate.now());
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
            new Profile("Valid Name", new Country("Germany"), null);
        });
    }
}