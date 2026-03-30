package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationType.PublicationType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for AppraisalEntity.
 *
 * Validates constructor rules, attribute initialization,
 * identity, equality, and general behavior.
 */
class AppraisalEntityTest {

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
                new AppraisalEntity(
                        "ApEn",
                        contact,
                        publicationTypes,
                        genres);

        // ASSERT
        assertNotNull(entity.getId());
        assertEquals("ApEn", entity.getName());
        assertEquals(contact, entity.getContact());
        assertEquals(publicationTypes, entity.getPublicationTypes());
        assertEquals(genres, entity.getGenres());
    }

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
                () -> new AppraisalEntity(
                        null,
                        contact,
                        publicationTypes,
                        genres)
        );

        assertEquals("AppraisalEntity name can't be null or empty.",
                exception.getMessage());
    }

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
                () -> new AppraisalEntity(
                        "Test",
                        null,
                        publicationTypes,
                        genres)
        );

        assertEquals("Contact can't be null.", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectName() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        AppraisalEntity entity =
                new AppraisalEntity(
                        "EntityName",
                        contact,
                        publicationTypes,
                        genres);

        // ACT
        String name = entity.getName();

        // ASSERT
        assertEquals("EntityName", name);
    }

    @Test
    void shouldReturnCorrectContact() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        AppraisalEntity entity =
                new AppraisalEntity(
                        "EntityName",
                        contact,
                        publicationTypes,
                        genres);

        // ACT
        Contact apContact = entity.getContact();

        // ASSERT
        assertEquals(contact, apContact);
    }

    @Test
    void shouldReturnCorrectPublicationTypes() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        AppraisalEntity entity =
                new AppraisalEntity(
                        "EntityName",
                        contact,
                        publicationTypes,
                        genres);

        // ACT
            List<PublicationType> apPublicationTypes = entity.getPublicationTypes();

        // ASSERT
        assertEquals(publicationTypes, apPublicationTypes);
    }

    @Test
    void shouldReturnCorrectListOfGenre() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        AppraisalEntity entity =
                new AppraisalEntity(
                        "EntityName",
                        contact,
                        publicationTypes,
                        genres);

        // ACT
        List<Genre> apGenres = entity.getGenres();

        // ASSERT
        assertEquals(genres, apGenres);
    }

    @Test
    void shouldBeEqualWhenSameNameIgnoreCaseAndSameContact() {

        // ARRANGE
        Contact contact = mock(Contact.class);

        PublicationType publicationType = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> publicationTypes = List.of(publicationType);
        List<Genre> genres = List.of(genre);

        AppraisalEntity entity1 =
                new AppraisalEntity(
                        "NAME",
                        contact,
                        publicationTypes,
                        genres);

        AppraisalEntity entity2 =
                new AppraisalEntity(
                        "NAME",
                        contact, // same instance
                        publicationTypes,
                        genres);

        // ACT
        boolean result = entity1.sameAs(entity2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnCorrectIdentity() {

        AppraisalEntity entity = new AppraisalEntity(
                "Test",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        assertEquals(entity.getId(), entity.identity());
    }

    @Test
    void shouldContainImportantFieldsInToString() {

        AppraisalEntity entity = new AppraisalEntity(
                "TestName",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        String result = entity.toString();

        assertTrue(result.contains("TestName"));
        assertTrue(result.contains("AppraisalEntity"));
    }

    @Test
    void shouldReturnTrueWhenSameAttributes() {

        Contact contact = mock(Contact.class);
        PublicationType pt = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> pts = List.of(pt);
        List<Genre> genres = List.of(genre);

        AppraisalEntity entity1 = new AppraisalEntity(
                "Name",
                contact,
                pts,
                genres
        );

        AppraisalEntity entity2 = new AppraisalEntity(
                "Name",
                contact,
                pts,
                genres
        );

        assertTrue(entity1.sameAs(entity2));
    }

    @Test
    void shouldReturnFalseWhenDifferentAttributes() {

        AppraisalEntity entity1 = new AppraisalEntity(
                "Name1",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        AppraisalEntity entity2 = new AppraisalEntity(
                "Name2",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        assertFalse(entity1.sameAs(entity2));
    }

    @Test
    void shouldHaveSameHashCodeWhenSameId() {

        AppraisalEntity entity = new AppraisalEntity(
                "Name1",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        assertEquals(entity.hashCode(), entity.hashCode());
    }

    @Test
    void shouldReturnNonZeroHashCode() {

        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        assertNotEquals(0, entity.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCodesForDifferentIds() {

        AppraisalEntity entity1 = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        AppraisalEntity entity2 = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void shouldReturnTrueWhenSameInstance() {

        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        assertTrue(entity.equals(entity));
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {

        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        assertFalse(entity.equals(null));
    }

    @Test
    void shouldReturnFalseWhenDifferentType() {

        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        assertFalse(entity.equals("Not an entity"));
    }

    @Test
    void shouldReturnTrueForSameIdentity() {

        Contact contact = mock(Contact.class);
        PublicationType pt = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> pts = List.of(pt);
        List<Genre> genres = List.of(genre);

        AppraisalEntity e1 = new AppraisalEntity("Name", contact, pts, genres);
        AppraisalEntity e2 = new AppraisalEntity("Name", contact, pts, genres);

        assertTrue(e1.sameAs(e2));
    }

}
