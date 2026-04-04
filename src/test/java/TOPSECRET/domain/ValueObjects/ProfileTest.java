package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link Profile}.
 * Profile has no injected dependencies, so no mocks are needed — direct object creation is used.
 */
class ProfileTest {

    private final Contact anyContact = mock(Contact.class);

    // --- Happy path ---

    // Valid data — profile is created and each field must match what was passed in.
    @Test
    void shouldCreateProfileWithValidData() {
        // Arrange & Act
        Profile profile = new Profile(
                "Test User",
                anyContact,
                LocalDate.of(2000, 5, 10)
        );

        // Assert — all three fields must match what was passed in
        assertEquals("Test User", profile.getName());
        assertEquals(anyContact, profile.getContact());
        assertEquals(LocalDate.of(2000, 5, 10), profile.getBirthday());
    }


    // --- Constructor validation ---

    // A profile without a name makes no sense — null must be rejected.
    @Test
    void shouldFailWhenNameIsNull() {
        // Act & Assert — null name must throw
        assertThrows(IllegalArgumentException.class, () ->
                new Profile(null, anyContact, LocalDate.now())
        );
    }

    // A name with only spaces is not a real name — it must also be rejected.
    // The check uses isBlank(), which catches both null and whitespace-only strings.
    @Test
    void shouldFailWhenNameIsBlank() {
        // Arrange, Act & Assert — blank name (only spaces) must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("   ", anyContact, LocalDate.now());
        });
    }

    // Contact is required — a profile with no contact is not valid.
    @Test
    void shouldFailWhenContactIsNull() {
        // Act & Assert — null contact must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("Valid Name", null, LocalDate.now());
        });
    }

    // Birthday is also required — a profile with no birthday is not valid.
    @Test
    void shouldFailWhenBirthdayIsNull() {
        //Arrange, Act & Assert — null birthday must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Profile("Valid Name", anyContact, null);
        });
    }


    // --- equals ---

    // equals was overridden to compare field values, not memory addresses.

    // Never equal to null.
    @Test
    void equals_shouldBeFalse_whenNull() {
        // Arrange & Act & Assert
        Profile p = new Profile("Ana", anyContact, LocalDate.of(1990, 1, 1));
        assertFalse(p.equals(null));
    }

    // Never equal to a different type.
    @Test
    void equals_shouldBeFalse_whenDifferentClass() {
        // Arrange & Act & Assert
       Profile p = new Profile("Ana", anyContact, LocalDate.of(1990, 1, 1));
        assertFalse(p.equals("not a profile"));
    }

    // Same data in two different objects must be considered equal.
    @Test
    void equals_shouldBeTrue_whenSameData() {
        // Arrange — two separate objects with the same data
        Profile a = new Profile("Ana", anyContact, LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Ana", anyContact, LocalDate.of(1990, 1, 1));

        // Assert — same data must mean equal
        assertEquals(a, b);
    }

    // Passing the exact same reference must return true immediately (this == o branch).
    @Test
    void equals_shouldBeTrue_whenSameReference() {
        // Arrange
        Profile p = new Profile("Ana", anyContact, LocalDate.of(1990, 1, 1));

        // Assert — same reference must be equal to itself
        assertTrue(p.equals(p));
    }

    // Different name — short-circuits at the name comparison, profiles are not equal.
    @Test
    void equals_shouldBeFalse_whenNameDiffers() {
        // Arrange
        Profile a = new Profile("Ana", anyContact, LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Bruno", anyContact, LocalDate.of(1990, 1, 1));

        // Assert — different name means not equal
        assertNotEquals(a, b);
    }

    // Same name but different contact — reaches the contact comparison and returns false there.
    // Without this test, the false branch of Objects.equals(_contact, ...) is never hit.
    @Test
    void equals_shouldBeFalse_whenContactDiffers() {
        // Arrange — same name and birthday, only contact differs
        Contact contact1 = mock(Contact.class);
        Contact contact2 = mock(Contact.class);

        Profile a = new Profile("Ana", contact1, LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Ana", contact2, LocalDate.of(1990, 1, 1));

        // Assert — different contact means not equal
        assertNotEquals(a, b);
    }

    // Same name and contact but different birthday — reaches the birthday comparison and returns false there.
    // Without this test, the false branch of Objects.equals(_birthday, ...) is never hit.
    @Test
    void equals_shouldBeFalse_whenBirthdayDiffers() {
        // Arrange — same name and contact, only birthday differs
        Profile a = new Profile("Ana", anyContact, LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Ana", anyContact, LocalDate.of(2000, 6, 15));

        // Assert — different birthday means not equal
        assertNotEquals(a, b);
    }

    @Test
    void hashCode_shouldBeEqual_whenSameData() {
        // Arrange
        Profile a = new Profile("Ana", anyContact, LocalDate.of(1990, 1, 1));
        Profile b = new Profile("Ana", anyContact, LocalDate.of(1990, 1, 1));

        // Assert
        assertEquals(a.hashCode(), b.hashCode());
    }
    
    @Test
    void hashCode_shouldNotBeZero_forValidProfile() {
        // Arrange
        Profile p = new Profile("Ana", anyContact, LocalDate.of(1990, 1, 1));

        // Assert
        assertNotEquals(0, p.hashCode());
    }

}