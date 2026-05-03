package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link AppraisalEntityDM}.
 *
 * <p>
 * Validates the data model behavior including:
 * </p>
 *
 * <ul>
 *     <li>Creation with valid data</li>
 *     <li>Validation of constructor arguments</li>
 *     <li>Getters correctness</li>
 *     <li>Equality and hashCode contract</li>
 *     <li>Handling of embedded value objects and collections</li>
 * </ul>
 *
 * <p>
 * Ensures that the data model enforces basic constraints required
 * for persistence while remaining free of business logic.
 * </p>
 *
 * <p>
 * Tests focus on structural integrity and correctness of stored data.
 * </p>
 */
class AppraisalEntityDMTest {

    // HELPER METHODS
    private AppraisalEntityDM createValidDM() {
        return new AppraisalEntityDM(
                validId(),
                "Name",
                validContact(),
                validPubTypes(),
                validGenres()
        );
    }

    private AppraisalEntityId validId() {
        return mock(AppraisalEntityId.class);
    }

    private Contact validContact() {
        return mock(Contact.class);
    }

    private List<PublicationTypeId> validPubTypes() {
        return List.of(mock(PublicationTypeId.class));
    }

    private List<GenreID> validGenres() {
        return List.of(mock(GenreID.class));
    }

    // CONSTRUCTOR TESTS
    @Test
    void shouldCreateAppraisalEntityDM() {
        // ARRANGE
        AppraisalEntityDM dm = createValidDM();

        // ACT + ASSERT
        assertNotNull(dm);
    }

    @Test
    void shouldReturnCorrectId() {
        // ARRANGE
        AppraisalEntityId id = validId();

        // ACT
        AppraisalEntityDM dm = new AppraisalEntityDM(
                id, "Name", validContact(), validPubTypes(), validGenres()
        );

        // ASSERT
        assertEquals(id, dm.getId());
    }

    @Test
    void shouldReturnCorrectName() {
        // ARRANGE
        AppraisalEntityDM dm = createValidDM();

        // ASSERT
        assertEquals("Name", dm.getName());
    }

    @Test
    void shouldReturnCorrectContact() {
        // ARRANGE
        Contact contact = validContact();

        // ACT
        AppraisalEntityDM dm = new AppraisalEntityDM(
                validId(), "Name", contact, validPubTypes(), validGenres()
        );

        // ASSERT
        assertEquals(contact, dm.getContact());
    }

    @Test
    void shouldReturnCorrectPublicationTypes() {
        // ARRANGE
        List<PublicationTypeId> pubs = validPubTypes();

        // ACT
        AppraisalEntityDM dm = new AppraisalEntityDM(
                validId(), "Name", validContact(), pubs, validGenres()
        );

        // ASSERT
        assertEquals(pubs, dm.getPublicationTypeIds());
    }


    @Test
    void shouldFailWhenIdIsNull() {

        // ARRANGE
        Contact contact = validContact();

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntityDM(
                        null,
                        "Name",
                        contact,
                        validPubTypes(),
                        validGenres()
                )
        );
    }

    @Test
    void shouldFailWhenNameIsNull() {

        // ARRANGE
        AppraisalEntityId id = validId();

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntityDM(
                        id,
                        null,
                        validContact(),
                        validPubTypes(),
                        validGenres()
                )
        );
    }

    @Test
    void shouldFailWhenNameIsBlank() {

        // ARRANGE
        AppraisalEntityId id = validId();

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntityDM(
                        id,
                        " ",
                        validContact(),
                        validPubTypes(),
                        validGenres()
                )
        );
    }

    // EQUALS & HASHCODE TESTS
    @Test
    void shouldNotBeEqualWhenDifferentId() {
        // ARRANGE
        AppraisalEntityDM dm1 = createValidDM();

        AppraisalEntityDM dm2 = createValidDM();

        // ACT
        boolean result = dm1.equals(dm2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseForDifferentType() {

        // ARRANGE
        AppraisalEntityDM dm = new AppraisalEntityDM(
                validId(),
                "Name",
                validContact(),
                validPubTypes(),
                validGenres()
        );

        // ACT
        boolean result = dm.equals(new Object());

        // ASSERT
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnTrueForSameInstance() {

        // ARRANGE
        AppraisalEntityDM dm = new AppraisalEntityDM(
                validId(),
                "Name",
                validContact(),
                validPubTypes(),
                validGenres()
        );

        // ACT
        boolean result = dm.equals(dm);

        // ASSERT
        assertTrue(result);
    }

    // COLLECTION TESTS
    @Test
    void shouldStorePublicationTypesCorrectly() {

        // ARRANGE
        List<PublicationTypeId> pubs = validPubTypes();

        // ACT
        AppraisalEntityDM dm = new AppraisalEntityDM(
                validId(),
                "Name",
                validContact(),
                pubs,
                validGenres()
        );

        // ASSERT
        assertEquals(pubs, dm.getPublicationTypeIds());
    }

    @Test
    void shouldStoreGenresCorrectly() {

        // ARRANGE
        List<GenreID> genres = validGenres();

        // ACT
        AppraisalEntityDM dm = new AppraisalEntityDM(
                validId(),
                "Name",
                validContact(),
                validPubTypes(),
                genres
        );

        // ASSERT
        assertEquals(genres, dm.getGenreIds());
    }
}