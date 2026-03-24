package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link AppraisalEntity}.
 *
 * <p>This test class validates the constructor and basic behavior
 * of the entity
 *
 * <p>Mocks are used only for domain dependencies
 * ({@code Contact}, {@code PublicationType}, {@code Genre})
 * to isolate the entity under test.
 *
 * <p>Collections such as {@code List} are NOT mocked because:
 * <ul>
 *     <li>They are simple JDK data structures</li>
 *     <li>They do not represent external dependencies</li>
 *     <li>They do not require behavioral isolation</li>
 * </ul>
 */
class AppraisalEntityTest {

    /**
     * Ensures a valid AppraisalEntity is created when
     * all constructor arguments satisfy business rules.
     */
    @Test
    void shouldCreateValidAppraisalEntity() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        // ACT
        AppraisalEntity entity =
                new AppraisalEntity("ApEn",
                        contact,
                        publicationTypes,
                        genres);

        // ASSERT
        assertEquals("ApEn", entity.getName());
        assertEquals(contact, entity.getContact());
        assertEquals(publicationTypes, entity.getPublicationTypes());
        assertEquals(genres, entity.getGenres());
    }

    /**
     * Ensures that an exception is thrown when name is null.
     */
    @Test
    void shouldThrowExceptionWhenNameIsNull() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        // ACT + ASSERT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AppraisalEntity(null,
                        contact,
                        publicationTypes,
                        genres)
        );

        assertEquals("AppraisalEntity name can't be null or empty.",
                exception.getMessage());
    }

    /**
     * Ensures that an exception is thrown when contact is null.
     */
    @Test
    void shouldThrowExceptionWhenContactIsNull() {

        // ARRANGE
        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        // ACT + ASSERT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AppraisalEntity("Test",
                        null,
                        publicationTypes,
                        genres)
        );

        assertEquals("Contact can't be null.", exception.getMessage());
    }

    /**
     * Ensures getName() returns the correct value.
     */
    @Test
    void shouldReturnCorrectName() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        AppraisalEntity entity =
                new AppraisalEntity("EntityName",
                        contact,
                        publicationTypes,
                        genres);

        // ACT
        String name = entity.getName();

        // ASSERT
        assertEquals("EntityName", name);
    }

    /**
     * Ensures getContact() returns the correct value.
     */
    @Test
    void shouldReturnCorrectContact() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        AppraisalEntity entity =
                new AppraisalEntity("EntityName",
                        contact,
                        publicationTypes,
                        genres);

        // ACT
        Contact apContact = entity.getContact();

        // ASSERT
        assertEquals(contact, apContact);
    }

    /**
     * Ensures getPublicationTypes() returns the correct value.
     */
    @Test
    void shouldReturnCorrectPublicationTypes() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        AppraisalEntity entity =
                new AppraisalEntity("EntityName",
                        contact,
                        publicationTypes,
                        genres);

        // ACT
            List<PublicationType> apPublicationTypes = entity.getPublicationTypes();

        // ASSERT
        assertEquals(publicationTypes, apPublicationTypes);
    }

    /**
     * Ensures getName() returns the correct value.
     */
    @Test
    void shouldReturnCorrectListOfGenre() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        AppraisalEntity entity =
                new AppraisalEntity("EntityName",
                        contact,
                        publicationTypes,
                        genres);

        // ACT
        List<Genre> apGenres = entity.getGenres();

        // ASSERT
        assertEquals(genres, apGenres);
    }

    /**
     * Ensures two AppraisalEntity objects are equal
     * when they have the same name (case-insensitive)
     * and the same contact.
     */
    @Test
    void shouldBeEqualWhenSameNameIgnoreCaseAndSameContact() {

        // ARRANGE
        Contact contact = mock(Contact.class);

        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        AppraisalEntity entity1 =
                new AppraisalEntity("NAME",
                        contact,
                        publicationTypes,
                        genres);

        AppraisalEntity entity2 =
                new AppraisalEntity("name",
                        contact, // same instance
                        publicationTypes,
                        genres);

        // ACT
        boolean result = entity1.equals(entity2);

        // ASSERT
        assertTrue(result);
    }

}
