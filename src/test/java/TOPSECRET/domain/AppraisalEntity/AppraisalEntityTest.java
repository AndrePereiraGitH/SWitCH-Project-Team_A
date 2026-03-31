package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
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

        AppraisalEntity entity =
                new AppraisalEntity(
                        "ApEn",
                        contact,
                        publicationTypes,
                        genres);

        // ACT + ASSERT
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

        // ACT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AppraisalEntity(
                        null,
                        contact,
                        publicationTypes,
                        genres)
        );

        // ASSERT
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

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "Test",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT + ASSERT
        assertEquals(entity.getId(), entity.identity());
    }

    @Test
    void shouldReturnTrueWhenSameId() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType pt = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        List<PublicationType> pts = List.of(pt);
        List<Genre> genres = List.of(genre);

        AppraisalEntity e1 = new AppraisalEntity("Name", contact, pts, genres);
        AppraisalEntity e2 = new AppraisalEntity("Name", contact, pts, genres);

        // Forcing the same ID
        e2._apEnId = e1._apEnId;

        // ACT
        boolean result = e1.equals(e2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnSameIdUsingIdMethod() {

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT
        AppraisalEntityId id = entity.id();

        // ASSERT
        assertEquals(entity.getId(), id);
    }

    @Test
    void shouldContainImportantFieldsInToString() {

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "TestName",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT
        String result = entity.toString();

        // ASSERT
        assertTrue(result.contains("TestName"));
        assertTrue(result.contains("AppraisalEntity"));
    }

    @Test
    void shouldReturnTrueWhenSameAttributes() {

        // ARRANGE
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

        // ACT + ASSERT
        assertTrue(entity1.sameAs(entity2));
    }

    @Test
    void shouldReturnFalseWhenDifferentAttributes() {

        // ARRANGE
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

        // ACT + ASSERT
        assertFalse(entity1.sameAs(entity2));
    }

    @Test
    void shouldHaveSameHashCodeWhenSameId() {

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "Name1",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT + ASSERT
        assertEquals(entity.hashCode(), entity.hashCode());
    }

    @Test
    void shouldReturnNonZeroHashCode() {

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT + ASSERT
        assertNotEquals(0, entity.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCodesForDifferentIds() {

        // ARRANGE
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

        // ACT + ASSERT
        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void shouldReturnTrueWhenSameInstance() {

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT + ASSERT
        assertTrue(entity.equals(entity));
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT + ASSERT
        assertFalse(entity.equals(null));
    }

    @Test
    void shouldReturnFalseWhenDifferentType() {

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT + ASSERT
        assertFalse(entity.equals("Not an entity"));
    }


    @Test
    void shouldReturnFalseWhenDifferentContact() {

        // ARRANGE
        PublicationType pt = mock(PublicationType.class);
        Genre genre = mock(Genre.class);
        Contact contact1 = mock(Contact.class);
        Contact contact2 = mock(Contact.class);

        AppraisalEntity e1 = new AppraisalEntity(
                "Name",
                contact1,
                List.of(pt),
                List.of(genre)
        );

        AppraisalEntity e2 = new AppraisalEntity(
                "Name",
                contact2,
                List.of(pt),
                List.of(genre)
        );

        // ACT
        boolean result = e1.sameAs(e2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentGenres() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType pt = mock(PublicationType.class);
        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);

        AppraisalEntity e1 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pt),
                List.of(genre1)
        );

        AppraisalEntity e2 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pt),
                List.of(genre2)
        );

        // ACT
        boolean result = e1.sameAs(e2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentPublicationTypes() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        Genre genre = mock(Genre.class);
        PublicationType pt1 = mock(PublicationType.class);
        PublicationType pt2 = mock(PublicationType.class);


        AppraisalEntity e1 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pt1),
                List.of(genre)
        );

        AppraisalEntity e2 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pt2),
                List.of(genre)
        );

        // ACT
        boolean result = e1.sameAs(e2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenSameAsDifferentType() {

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT
        boolean result = entity.sameAs("Not an entity");

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenSameAsNull() {

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT
        boolean result = entity.sameAs(null);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenSameInstanceUsingSameAs() {

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT
        boolean result = entity.sameAs(entity);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenListsHaveSameContent() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationType pt = mock(PublicationType.class);
        Genre genre = mock(Genre.class);

        AppraisalEntity e1 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pt),
                List.of(genre)
        );

        AppraisalEntity e2 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pt),
                List.of(genre)
        );

        // ACT
        boolean result = e1.sameAs(e2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenOnlyPublicationTypesDifferent() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        Genre genre = mock(Genre.class);

        PublicationType pt1 = mock(PublicationType.class);
        PublicationType pt2 = mock(PublicationType.class);

        AppraisalEntity e1 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pt1),
                List.of(genre)
        );

        AppraisalEntity e2 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pt2),
                List.of(genre)
        );

        // ACT
        boolean result = e1.sameAs(e2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenOnlyNameDifferent() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        Genre genre = mock(Genre.class);
        PublicationType pt = mock(PublicationType.class);

        AppraisalEntity e1 = new AppraisalEntity(
                "Name1",
                contact,
                List.of(pt),
                List.of(genre)
        );

        AppraisalEntity e2 = new AppraisalEntity(
                "Name2",
                contact,
                List.of(pt),
                List.of(genre)
        );

        // ACT
        boolean result = e1.sameAs(e2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenAllFieldsMatchExactly() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        Genre genre = mock(Genre.class);
        PublicationType pt = mock(PublicationType.class);

        AppraisalEntity e1 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pt),
                List.of(genre)
        );

        AppraisalEntity e2 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pt),
                List.of(genre)
        );

        // ACT
        boolean result = e1.sameAs(e2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenObjectIsNullUsingSameAs() {

        // ARRANGE
        AppraisalEntity entity = new AppraisalEntity(
                "Name",
                mock(Contact.class),
                List.of(mock(PublicationType.class)),
                List.of(mock(Genre.class))
        );

        // ACT
        boolean result = entity.sameAs(null);

        // ASSERT
        assertFalse(result);
    }

}
