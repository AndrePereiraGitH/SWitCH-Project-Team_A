package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Profile}.
 * Profile has no injected dependencies, so no mocks are needed — direct object creation is used.
 */
class ProfileTest {


    // --- Happy path ---

    // Valid data — profile is created and each field must match what was passed in.
    @Test
    void shouldCreateProfileWithValidData() {
        // Arrange & Act
        Profile profile = new Profile(
                "Test User",
                new Country("Portugal"),
                LocalDate.of(2000, 5, 10)
        );

        // Assert — all three fields must match what was passed in
        assertEquals("Test User", profile.getName());
        assertEquals("Portugal", profile.getCountry().getCountryName());
        assertEquals(LocalDate.of(2000, 5, 10), profile.getBirthday());
    }


    // --- Constructor validation ---

    // A profile without a name makes no sense — null must be rejected.
    @Test
    void shouldFailWhenNameIsNull() {
        // Act & Assert — null name must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile(null, new Country("Spain"), LocalDate.now());
        });
    }

    // A name with only spaces is not a real name — it must also be rejected.
    // The check uses isBlank(), which catches both null and whitespace-only strings.
    @Test
    void shouldFailWhenNameIsBlank() {
        // Act & Assert — blank name (only spaces) must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("   ", new Country("France"), LocalDate.now());
        });
    }

    // Country is required — a profile with no country is not valid.
    @Test
    void shouldFailWhenCountryIsNull() {
        // Act & Assert — null country must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("Valid Name", null, LocalDate.now());
        });
    }

    // Birthday is also required — a profile with no birthday is not valid.
    @Test
    void shouldFailWhenBirthdayIsNull() {
        // Act & Assert — null birthday must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("Valid Name", new Country("Germany"), null);
        });
    }


    // --- equals ---

    // equals was overridden to compare field values, not memory addresses.

    // Never equal to null.
    @Test
    void equals_shouldBeFalse_whenNull() {
        // Arrange & Act & Assert
        Profile p = new Profile("Ana", new Country("Portugal"), LocalDate.of(1990, 1, 1));
        assertFalse(p.equals(null));
    }

    // Never equal to a different type.
    @Test
    void equals_shouldBeFalse_whenDifferentClass() {
        // Arrange & Act & Assert
        Profile p = new Profile("Ana", new Country("Portugal"), LocalDate.of(1990, 1, 1));
        assertFalse(p.equals("not a profile"));
    }

    // Same data in two different objects must be considered equal.
    @Test
    void equals_shouldBeTrue_whenSameData() {
        // Arrange — two separate objects with the same data
        Profile a = new Profile("Ana", new Country("Portugal"), LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Ana", new Country("Portugal"), LocalDate.of(1990, 1, 1));

        // Assert — same data must mean equal
        assertEquals(a, b);
    }

}