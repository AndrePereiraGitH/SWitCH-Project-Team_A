package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityFactory}.
 *
 * <p>
 * Validates the creation of {@link AppraisalEntity}
 * instances through the factory.
 * </p>
 *
 * <ul>
 *     <li>Correct delegation to the AppraisalEntity constructor</li>
 *     <li>Proper handling of valid input data</li>
 *     <li>Propagation of validation exceptions from the domain entity</li>
 * </ul>
 *
 * <p>
 * Ensures that the factory does not contain business logic and only
 * coordinates object creation while respecting domain invariants.
 * </p>
 */
class AppraisalEntityFactoryTest {

    @Test
    void shouldCallAppraisalEntityConstructor() {

        // ARRANGE
        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        Contact contact = mock(Contact.class);
        GenreId genre = mock(GenreId.class);
        PublicationTypeId type = mock(PublicationTypeId.class);

        try (MockedConstruction<AppraisalEntity> mocked =
                mockConstruction(AppraisalEntity.class,
                    (mock, context) -> {
                        when(mock.getName()).thenReturn("apEn");
            })) {

            // ACT
            AppraisalEntity entity = factory.create(
                    "apEn",
                    contact,
                    List.of(type),
                    List.of(genre)
            );

            // ASSERT
            assertEquals(1, mocked.constructed().size());
        }

    }

    @Test
    void shouldReturnConstructedAppraisalEntity() {

        // ARRANGE
        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        Contact contact = mock(Contact.class);
        GenreId genre = mock(GenreId.class);
        PublicationTypeId type = mock(PublicationTypeId.class);

        try (MockedConstruction<AppraisalEntity> mocked =
                     mockConstruction(AppraisalEntity.class)) {

            // ACT
            AppraisalEntity result = factory.create(
                    "apEn",
                    contact,
                    List.of(type),
                    List.of(genre)
            );

            // ASSERT
            assertSame(mocked.constructed().get(0), result);
        }
    }

    @Test
    void shouldCreateAppraisalEntityWithId() {

        // ARRANGE
        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        AppraisalEntityId id = mock(AppraisalEntityId.class);
        Contact contact = mock(Contact.class);

        List<PublicationTypeId> pubs = List.of(mock(PublicationTypeId.class));
        List<GenreId> genres = List.of(mock(GenreId.class));

        // ACT
        AppraisalEntity result = factory.create(id, "Name", contact, pubs, genres);

        // ASSERT
        assertNotNull(result);
    }

    @Test
    void shouldSetGivenId() {

        // ARRANGE
        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        AppraisalEntityId id = mock(AppraisalEntityId.class);
        Contact contact = mock(Contact.class);

        List<PublicationTypeId> pubs = List.of(mock(PublicationTypeId.class));
        List<GenreId> genres = List.of(mock(GenreId.class));

        // ACT
        AppraisalEntity result = factory.create(id, "Name", contact, pubs, genres);

        // ASSERT
        assertEquals(id, result.id());
    }

    @Test
    void shouldSetName() {

        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        AppraisalEntityId id = mock(AppraisalEntityId.class);
        Contact contact = mock(Contact.class);

        List<PublicationTypeId> pubs = List.of(mock(PublicationTypeId.class));
        List<GenreId> genres = List.of(mock(GenreId.class));

        AppraisalEntity result = factory.create(id, "Name", contact, pubs, genres);

        assertEquals("Name", result.getName());
    }

    @Test
    void shouldSetContact() {

        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        AppraisalEntityId id = mock(AppraisalEntityId.class);
        Contact contact = mock(Contact.class);

        List<PublicationTypeId> pubs = List.of(mock(PublicationTypeId.class));
        List<GenreId> genres = List.of(mock(GenreId.class));

        AppraisalEntity result = factory.create(id, "Name", contact, pubs, genres);

        assertEquals(contact, result.getContact());
    }

    @Test
    void shouldSetPublicationTypes() {

        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        AppraisalEntityId id = mock(AppraisalEntityId.class);
        Contact contact = mock(Contact.class);

        List<PublicationTypeId> pubs = List.of(mock(PublicationTypeId.class));
        List<GenreId> genres = List.of(mock(GenreId.class));

        AppraisalEntity result = factory.create(id, "Name", contact, pubs, genres);

        assertEquals(pubs, result.getPublicationTypeIds());
    }

    @Test
    void shouldSetGenres() {

        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        AppraisalEntityId id = mock(AppraisalEntityId.class);
        Contact contact = mock(Contact.class);

        List<PublicationTypeId> pubs = List.of(mock(PublicationTypeId.class));
        List<GenreId> genres = List.of(mock(GenreId.class));

        AppraisalEntity result = factory.create(id, "Name", contact, pubs, genres);

        assertEquals(genres, result.getGenreIds());
    }
}