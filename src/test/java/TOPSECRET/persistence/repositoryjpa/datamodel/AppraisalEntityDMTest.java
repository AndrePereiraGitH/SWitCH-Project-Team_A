package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AppraisalEntityDMTest {

    // HELPER METHODS
    private AppraisalEntityId validId() {
        return new AppraisalEntityId("1");
    }

    private Contact validContact() {
        return mock(Contact.class);
    }

    private List<PublicationTypeId> validPubTypes() {
        return List.of(new PublicationTypeId("1"));
    }

    private List<GenreID> validGenres() {
        return List.of(new GenreID("1"));
    }

    // CONSTRUCTOR TESTS
    @Test
    void shouldCreateValidDM() {

        // ARRANGE
        AppraisalEntityId id = validId();
        Contact contact = validContact();

        // ACT
        AppraisalEntityDM dm = new AppraisalEntityDM(
                id,
                "Name",
                contact,
                validPubTypes(),
                validGenres()
        );

        // ASSERT
        assertNotNull(dm);
        assertEquals(id, dm.getId());
        assertEquals("Name", dm.getName());
        assertEquals(contact, dm.getContact());
        assertEquals(1, dm.getPublicationTypeIds().size());
        assertEquals(1, dm.getGenreIds().size());
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
    void shouldBeEqualWhenSameId() {

        // ARRANGE
        AppraisalEntityId id = validId();

        AppraisalEntityDM dm1 = new AppraisalEntityDM(
                id,
                "Name1",
                validContact(),
                validPubTypes(),
                validGenres()
        );

        AppraisalEntityDM dm2 = new AppraisalEntityDM(
                id,
                "Different",
                validContact(),
                validPubTypes(),
                validGenres()
        );

        // ACT
        boolean result = dm1.equals(dm2);

        // ASSERT
        assertTrue(result);
        assertEquals(dm1.hashCode(), dm2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {

        // ARRANGE
        AppraisalEntityDM dm1 = new AppraisalEntityDM(
                new AppraisalEntityId("1"),
                "Name",
                validContact(),
                validPubTypes(),
                validGenres()
        );

        AppraisalEntityDM dm2 = new AppraisalEntityDM(
                new AppraisalEntityId("2"),
                "Name",
                validContact(),
                validPubTypes(),
                validGenres()
        );

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