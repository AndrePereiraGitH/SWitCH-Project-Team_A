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
                Countries.PORTUGAL,
                LocalDate.of(2000, 5, 10)
        );

        assertEquals("Test User", profile.getName());
        assertEquals(Countries.PORTUGAL, profile.getCountry());
        assertEquals(LocalDate.of(2000, 5, 10), profile.getBirthday());
    }

    @Test
    void shouldFailWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile(null, Countries.PORTUGAL, LocalDate.now());
        });
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("   ", Countries.FRANCE, LocalDate.now());
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
            new Profile("Valid Name", Countries.GERMANY, null);
        });
    }
}