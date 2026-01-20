package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AppraisalEntityTest {

    // TEST DATA HELPERS
    // Creates a valid Contact for testing
    private Contact createValidContact() {
        return new Contact("912345678", "351", null, null);
    }

    // Creates a valid PublicationType for testing
    private PublicationType createValidPublicationType() {
        return new PublicationType(); // Adjust when class is ready
    }

    // Static helper used by MethodSource
    private static Contact createValidContactStatic() {
        return new Contact("999999999", "351", null, null);
    }

    // Static helper used by MethodSource
    private static PublicationType createValidPublicationTypeStatic() {
        return new PublicationType();
    }


    // PARAMETER PROVIDERS
    // Provides invalid name values for constructor validation
    static Stream<String> invalidNamesProvider() {
        return Stream.of(
                null,       // Null value
                "",         // Empty string
                " ",        // Single space
                "   ",      // Multiple spaces
                "\t",       // Tab
                "\n"        // New line
        );
    }

    // Provides combinations where one mandatory parameter is null
    static Stream<Arguments> nullParameterProvider() {
        return Stream.of(
                Arguments.of(null, createValidPublicationTypeStatic(), Genre.FICTION),   // Null contact
                Arguments.of(createValidContactStatic(), null, Genre.FICTION),          // Null publicationType
                Arguments.of(createValidContactStatic(), createValidPublicationTypeStatic(), null) // Null genre
        );
    }


    // VALID CONSTRUCTION
    // Verifies that a valid AppraisalEntity can be created
    @Test
    void shouldCreateAppraisalEntityWhenAllDataIsValid() {

        // ARRANGE
        String expectedName = "Entity One";
        Contact expectedContact = createValidContact();
        PublicationType expectedPublicationType = createValidPublicationType();
        Genre expectedGenre = Genre.FICTION;

        // ACT
        AppraisalEntity entity =
                new AppraisalEntity(
                        expectedName,
                        expectedContact,
                        expectedPublicationType,
                        expectedGenre
                );

        // ASSERT
        assertNotNull(entity);
        assertEquals(expectedName, entity.getName());
        assertEquals(expectedContact, entity.getContact());
        assertEquals(expectedPublicationType, entity.getPublicationType());
        assertEquals(expectedGenre, entity.getGenre());
    }


    // INVALID NAME (PARAMETERIZED)
    // Verifies that invalid names throw an exception
    @ParameterizedTest
    @MethodSource("invalidNamesProvider")
    void shouldThrowExceptionWhenInvalidNameIsProvided(String invalidName) {

        // ARRANGE
        Contact contact = createValidContact();
        PublicationType publicationType = createValidPublicationType();
        Genre genre = Genre.FICTION;

        // ACT AND ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            new AppraisalEntity(
                    invalidName,
                    contact,
                    publicationType,
                    genre
            );
        });
    }


    // NULL PARAMETERS (PARAMETERIZED)
    // Verifies that null mandatory parameters throw an exception
    @ParameterizedTest
    @MethodSource("nullParameterProvider")
    void shouldThrowExceptionWhenMandatoryParameterIsNull(Contact contact, PublicationType publicationType, Genre genre) {

        // ARRANGE
        String validName = "Valid Name";

        // ACT AND ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            new AppraisalEntity(
                    validName,
                    contact,
                    publicationType,
                    genre
            );
        });
    }

    // EQUALS METHOD
    // Verifies equality when name and contact are equal
    @Test
    void shouldReturnTrueWhenNameAndContactAreEqual() {

        // ARRANGE
        Contact contact = createValidContact();
        PublicationType publicationType = createValidPublicationType();

        AppraisalEntity entity1 =
                new AppraisalEntity("Entity", contact, publicationType, Genre.FICTION);

        AppraisalEntity entity2 =
                new AppraisalEntity("entity", contact, publicationType, Genre.DRAMA);

        // ACT
        boolean result = entity1.equals(entity2);

        // ASSERT
        assertTrue(result);
    }

    // Verifies inequality when name is different
    @Test
    void shouldReturnFalseWhenNameIsDifferent() {

        // ARRANGE
        Contact contact = createValidContact();
        PublicationType publicationType = createValidPublicationType();

        AppraisalEntity entity1 =
                new AppraisalEntity("Entity A", contact, publicationType, Genre.FICTION);

        AppraisalEntity entity2 =
                new AppraisalEntity("Entity B", contact, publicationType, Genre.FICTION);

        // ACT
        boolean result = entity1.equals(entity2);

        // ASSERT
        assertFalse(result);
    }

    // Verifies inequality when contact is different
    @Test
    void shouldReturnFalseWhenContactIsDifferent() {

        // ARRANGE
        PublicationType publicationType = createValidPublicationType();

        AppraisalEntity entity1 =
                new AppraisalEntity(
                        "Entity",
                        new Contact("111111111", "351", null, null),
                        publicationType,
                        Genre.FICTION
                );

        AppraisalEntity entity2 =
                new AppraisalEntity(
                        "Entity",
                        new Contact("222222222", "351", null, null),
                        publicationType,
                        Genre.FICTION
                );

        // ACT
        boolean result = entity1.equals(entity2);

        // ASSERT
        assertFalse(result);
    }


    // HASHCODE METHOD
    // Verifies hashCode consistency with equals
    @Test
    void hashCodeShouldBeEqualWhenObjectsAreEqual() {

        // ARRANGE
        Contact contact = createValidContact();
        PublicationType publicationType = createValidPublicationType();

        AppraisalEntity entity1 =
                new AppraisalEntity("Entity", contact, publicationType, Genre.FICTION);

        AppraisalEntity entity2 =
                new AppraisalEntity("ENTITY", contact, publicationType, Genre.DRAMA);

        // ACT
        int hash1 = entity1.hashCode();
        int hash2 = entity2.hashCode();

        // ASSERT
        assertEquals(hash1, hash2);
    }


    // FULL INFORMATION METHOD
    // Verifies that the full information string contains all data
    @Test
    void shouldReturnFullInformationString() {

        // ARRANGE
        String name = "Entity One";
        Contact contact = createValidContact();
        PublicationType publicationType = createValidPublicationType();
        Genre genre = Genre.FICTION;

        AppraisalEntity entity =
                new AppraisalEntity(name, contact, publicationType, genre);

        // ACT
        String result = entity.AppraisalEntityFullInformation();

        // ASSERT
        assertTrue(result.contains(name));
        assertTrue(result.contains(contact.toString()));
        assertTrue(result.contains(publicationType.toString()));
        assertTrue(result.contains(genre.toString()));
    }
}
