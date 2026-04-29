package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.AppraisalEntity.*;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.datamodel.AppraisalEntityDM;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityAssembler}.
 *
 * <p>
 * Validates conversion logic between
 * {@link AppraisalEntity} and
 * {@link AppraisalEntityDM}.
 * </p>
 *
 * <ul>
 *     <li>Conversion from domain to data model</li>
 *     <li>Conversion from data model to domain</li>
 *     <li>Preservation of field values during mapping</li>
 *     <li>Defensive copying of collections (no shared references)</li>
 *     <li>Handling of null inputs</li>
 * </ul>
 *
 * <p>
 * Ensures that persistence concerns remain isolated and that
 * domain integrity is preserved during transformations.
 * </p>
 *
 * <p>
 * Tests use real objects for value validation and verify that
 * lists are not shared between layers.
 * </p>
 */
class AppraisalEntityAssemblerTest {

    // HELPER
    private AppraisalEntityFactory factory = new AppraisalEntityFactory();
    private AppraisalEntityAssembler assembler = new AppraisalEntityAssembler(factory);

    private Contact contact() {
        return mock(Contact.class);
    }

    private List<PublicationTypeId> pubs() {
        return List.of(new PublicationTypeId("1"));
    }

    private List<GenreID> genres() {
        return List.of(new GenreID("1"));
    }

    // toDomain TESTS
    @Test
    void toDomainShouldConvertCorrectly() {

        // ARRANGE
        AppraisalEntityDM dm = new AppraisalEntityDM(
                new AppraisalEntityId("1"),
                "Name",
                contact(),
                pubs(),
                genres()
        );

        // ACT
        AppraisalEntity result = assembler.toDomain(dm);

        // ASSERT
        assertEquals(dm.getId(), result.id());
        assertEquals(dm.getName(), result.getName());
        assertEquals(dm.getContact(), result.getContact());
    }

    @Test
    void toDomainShouldThrowWhenNull() {

        // ARRANGE

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDomain(null));
    }

    // toDataModel TESTS
    @Test
    void toDataModelShouldConvertCorrectly() {

        // ARRANGE
        AppraisalEntity entity = factory.create(
                "Name",
                contact(),
                pubs(),
                genres()
        );

        // ACT
        AppraisalEntityDM dm = assembler.toDataModel(entity);

        // ASSERT
        assertEquals(entity.id(), dm.getId());
        assertEquals(entity.getName(), dm.getName());
        assertEquals(entity.getContact(), dm.getContact());
    }

    @Test
    void toDomainShouldThrowWhenListsAreNull() {

        // ARRANGE
        AppraisalEntityDM dm = new AppraisalEntityDM(
                new AppraisalEntityId("1"),
                "Name",
                contact(),
                null,
                null
        );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDomain(dm));
    }

    // DEFENSIVE COPY TEST
    @Test
    void toDomainShouldNotShareListReferences() {

        // ARRANGE
        List<PublicationTypeId> pubs = pubs();

        AppraisalEntityDM dm = new AppraisalEntityDM(
                new AppraisalEntityId("1"),
                "Name",
                contact(),
                pubs,
                genres()
        );

        // ACT
        AppraisalEntity entity = assembler.toDomain(dm);

        // ASSERT
        assertEquals(pubs, entity.getPublicationTypeIds());
    }

    @Test
    void toDataModelShouldNotShareListReferences() {

        // ARRANGE
        AppraisalEntity entity = factory.create(
                "Name",
                contact(),
                pubs(),
                genres()
        );

        // ACT
        AppraisalEntityDM dm = assembler.toDataModel(entity);

        // ASSERT
        assertEquals(entity.getPublicationTypeIds(), dm.getPublicationTypeIds());
    }

    @Test
    void toDomainShouldCopyListContent() {

        // ARRANGE
        List<PublicationTypeId> pubs = pubs();
        List<GenreID> genres = genres();

        AppraisalEntityDM dm = new AppraisalEntityDM(
                new AppraisalEntityId("1"),
                "Name",
                contact(),
                pubs,
                genres
        );

        // ACT
        AppraisalEntity entity = assembler.toDomain(dm);

        // ASSERT
        assertEquals(pubs, entity.getPublicationTypeIds());
        assertEquals(genres, entity.getGenreIds());
    }

    @Test
    void toDataModelShouldCopyListContent() {

        // ARRANGE
        AppraisalEntity entity = factory.create(
                "Name",
                contact(),
                pubs(),
                genres()
        );

        // ACT
        AppraisalEntityDM dm = assembler.toDataModel(entity);

        // ASSERT
        assertEquals(entity.getPublicationTypeIds(), dm.getPublicationTypeIds());
        assertEquals(entity.getGenreIds(), dm.getGenreIds());
    }

    @Test
    void toDataModelShouldHandleNullLists() {

        // ARRANGE
        AppraisalEntity entity = mock(AppraisalEntity.class);

        when(entity.id()).thenReturn(new AppraisalEntityId("1"));
        when(entity.getName()).thenReturn("Name");
        when(entity.getContact()).thenReturn(contact());
        when(entity.getPublicationTypeIds()).thenReturn(null);
        when(entity.getGenreIds()).thenReturn(null);

        // ACT
        AppraisalEntityDM dm = assembler.toDataModel(entity);

        // ASSERT
        assertNotNull(dm.getPublicationTypeIds());
        assertNotNull(dm.getGenreIds());
    }
}