package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Profile}.
 *
 * <p>A {@link Profile} holds the personal data of a user: name, country, and birthday.
 * All three fields are required — none can be null, and the name cannot be blank.
 *
 * <p>{@code Profile} is used when creating a {@link User}, which is in turn used across
 * multiple user stories. In US017, a valid User (and therefore a valid Profile) is required
 * to create a collection of publications.
 *
 * <p>These tests use real {@link Country} objects — no mocks — because {@code Profile}
 * has no heavy external dependencies and is simple enough to test directly.
 *
 * <p>Tests are grouped into:
 * <ul>
 *   <li>Happy path — valid data creates a profile with the correct fields</li>
 *   <li>Constructor validation — each required field is tested with an invalid value</li>
 *   <li>equals — two profiles with the same data must be equal</li>
 * </ul>
 */
class ProfileTest {


    // --- Happy path ---

    // When all three fields are valid, the profile must be created with the correct values.
    // We check each getter to confirm the data was stored correctly.
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

    // equals was overridden so Java compares profiles by their data, not by memory address.
    // By default, two different objects with the same data would not be equal in Java.
    // These tests confirm that the override works correctly.

    // A profile must never be equal to null — equals must return false, not crash.
    @Test
    void equals_shouldBeFalse_whenNull() {
        // Arrange & Act & Assert
        Profile p = new Profile("Ana", new Country("Portugal"), LocalDate.of(1990, 1, 1));
        assertFalse(p.equals(null));
    }

    // A profile must not be equal to an object of a different type (e.g. a String).
    @Test
    void equals_shouldBeFalse_whenDifferentClass() {
        // Arrange & Act & Assert
        Profile p = new Profile("Ana", new Country("Portugal"), LocalDate.of(1990, 1, 1));
        assertFalse(p.equals("not a profile"));
    }

    // Two profiles created with the same name, country, and birthday must be equal.
    // They are different objects in memory, but equals must compare by value.
    @Test
    void equals_shouldBeTrue_whenSameData() {
        // Arrange — two separate objects with the same data
        Profile a = new Profile("Ana", new Country("Portugal"), LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Ana", new Country("Portugal"), LocalDate.of(1990, 1, 1));

        // Assert — same data must mean equal
        assertEquals(a, b);
    }

}