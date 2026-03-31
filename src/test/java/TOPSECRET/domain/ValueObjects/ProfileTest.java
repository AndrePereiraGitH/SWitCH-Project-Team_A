package TOPSECRET.domain.ValueObjects;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
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
        CountryFactory countryFactory = new CountryFactory();
        Country country = countryFactory.create("Portugal");

        Profile profile = new Profile(
                "Test User",
                country,
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
        //Arrange
        Country country = new CountryFactory().create("Spain");

        // Act & Assert — null name must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile(null, country, LocalDate.now());
        });
    }

    // A name with only spaces is not a real name — it must also be rejected.
    // The check uses isBlank(), which catches both null and whitespace-only strings.
    @Test
    void shouldFailWhenNameIsBlank() {
        //Arrange
        Country country = new CountryFactory().create("France");

        // Act & Assert — blank name (only spaces) must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("   ", country, LocalDate.now());
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
        //Arrange
        Country country = new CountryFactory().create("Germany");

        // Act & Assert — null birthday must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("Valid Name", country, null);
        });
    }


    // --- equals ---

    // equals was overridden to compare field values, not memory addresses.

    // Never equal to null.
    @Test
    void equals_shouldBeFalse_whenNull() {
        // Arrange & Act & Assert
        Country country = new CountryFactory().create("Portugal");

        Profile p = new Profile("Ana", country, LocalDate.of(1990, 1, 1));
        assertFalse(p.equals(null));
    }

    // Never equal to a different type.
    @Test
    void equals_shouldBeFalse_whenDifferentClass() {
        // Arrange & Act & Assert
        Country country = new CountryFactory().create("Portugal");
        Profile p = new Profile("Ana", country, LocalDate.of(1990, 1, 1));
        assertFalse(p.equals("not a profile"));
    }

    // Same data in two different objects must be considered equal.
    @Test
    void equals_shouldBeTrue_whenSameData() {
        // Arrange — two separate objects with the same data
        Country country = new CountryFactory().create("Portugal");

        Profile a = new Profile("Ana", country, LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Ana", country, LocalDate.of(1990, 1, 1));

        // Assert — same data must mean equal
        assertEquals(a, b);
    }

    // Passing the exact same reference must return true immediately (this == o branch).
    @Test
    void equals_shouldBeTrue_whenSameReference() {
        // Arrange
        Country country = new CountryFactory().create("Portugal");
        Profile p = new Profile("Ana", country, LocalDate.of(1990, 1, 1));

        // Assert — same reference must be equal to itself
        assertTrue(p.equals(p));
    }

    // Different name — short-circuits at the name comparison, profiles are not equal.
    @Test
    void equals_shouldBeFalse_whenNameDiffers() {
        // Arrange
        Country country = new CountryFactory().create("Portugal");

        Profile a = new Profile("Ana", country, LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Bruno", country, LocalDate.of(1990, 1, 1));

        // Assert — different name means not equal
        assertNotEquals(a, b);
    }

    // Same name but different country — reaches the country comparison and returns false there.
    // Without this test, the false branch of Objects.equals(_country, ...) is never hit.
    @Test
    void equals_shouldBeFalse_whenCountryDiffers() {
        // Arrange — same name and birthday, only country differs
        Country country1 = new CountryFactory().create("Portugal");
        Country country2 = new CountryFactory().create("Spain");

        Profile a = new Profile("Ana", country1, LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Ana", country2, LocalDate.of(1990, 1, 1));

        // Assert — different country means not equal
        assertNotEquals(a, b);
    }

    // Same name and country but different birthday — reaches the birthday comparison and returns false there.
    // Without this test, the false branch of Objects.equals(_birthday, ...) is never hit.
    @Test
    void equals_shouldBeFalse_whenBirthdayDiffers() {
        // Arrange — same name and country, only birthday differs
        Country country = new CountryFactory().create("Portugal");

        Profile a = new Profile("Ana", country, LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Ana", country, LocalDate.of(2000, 6, 15));

        // Assert — different birthday means not equal
        assertNotEquals(a, b);
    }

    @Test
    void hashCode_shouldBeEqual_whenSameData() {

        // Arrange
        Country country = new CountryFactory().create("Portugal");

        Profile a = new Profile("Ana", country, LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Ana", country, LocalDate.of(1990, 1, 1));

        // Assert
        assertEquals(a.hashCode(), b.hashCode());
    }
    
    @Test
    void hashCode_shouldNotBeZero_forValidProfile() {

        // Arrange
        Country country = new CountryFactory().create("Portugal");
        Profile p = new Profile("Ana", country, LocalDate.of(1990, 1, 1));

        // Assert
        assertNotEquals(0, p.hashCode());
    }

}