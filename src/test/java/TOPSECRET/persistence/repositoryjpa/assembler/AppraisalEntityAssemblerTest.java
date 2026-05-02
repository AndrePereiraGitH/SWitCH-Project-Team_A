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

    private AppraisalEntityDM validDM() {

        return new AppraisalEntityDM(
                new AppraisalEntityId("1"),
                "Name",
                contact(),
                pubs(),
                genres()
        );
    }

    private AppraisalEntity validEntity() {
        return factory.create(
                "Name",
                contact(),
                pubs(),
                genres()
        );
    }

    // toDomain TESTS
    @Test
    void toDomainShouldMapId() {
        // ARRANGE
        AppraisalEntityDM dm = validDM();

        // ACT
        AppraisalEntity result = assembler.toDomain(dm);

        // ASSERT
        assertEquals(dm.getId(), result.id());
    }

    @Test
    void toDomainShouldMapName() {
        // ARRANGE
        AppraisalEntityDM dm = validDM();

        // ACT
        AppraisalEntity result = assembler.toDomain(dm);

        // ASSERT
        assertEquals(dm.getName(), result.getName());
    }

    @Test
    void toDomainShouldMapContact() {
        // ARRANGE
        AppraisalEntityDM dm = validDM();

        // ACT
        AppraisalEntity result = assembler.toDomain(dm);

        // ASSERT
        assertEquals(dm.getContact(), result.getContact());
    }

    @Test
    void toDomainShouldThrowWhenNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDomain(null));
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

    @Test
    void toDomainShouldCopyPublicationTypeIdsContent() {

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
    void toDomainShouldNotSharePublicationTypeIdsReference() {
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
        assertNotSame(pubs, entity.getPublicationTypeIds());
    }

    @Test
    void toDomainShouldCopyGenreIdsContent() {
        // ARRANGE
        List<GenreID> genres = genres();
        AppraisalEntityDM dm = new AppraisalEntityDM(
                new AppraisalEntityId("1"),
                "Name",
                contact(),
                pubs(),
                genres
        );

        // ACT
        AppraisalEntity entity = assembler.toDomain(dm);

        // ASSERT
        assertEquals(genres, entity.getGenreIds());
    }

    @Test
    void toDomainShouldNotShareGenreIdsReference() {

        // ARRANGE
        List<GenreID> genres = genres();
        AppraisalEntityDM dm = new AppraisalEntityDM(
                new AppraisalEntityId("1"),
                "Name",
                contact(),
                pubs(),
                genres
        );

        // ACT
        AppraisalEntity entity = assembler.toDomain(dm);

        // ASSERT
        assertNotSame(genres, entity.getGenreIds());
    }

    // toDataModel TESTS
    @Test
    void toDataModelShouldMapId() {
        // ARRANGE
        AppraisalEntity entity = validEntity();

        // ACT
        AppraisalEntityDM dm = assembler.toDM(entity);

        // ASSERT
        assertEquals(entity.id(), dm.getId());
    }

    @Test
    void toDataModelShouldMapName() {
        // ARRANGE
        AppraisalEntity entity = validEntity();

        // ACT
        AppraisalEntityDM dm = assembler.toDM(entity);

        // ASSERT
        assertEquals(entity.getName(), dm.getName());
    }

    @Test
    void toDataModelShouldMapContact() {

        // ARRANGE
        AppraisalEntity entity = validEntity();

        // ACT
        AppraisalEntityDM dm = assembler.toDM(entity);

        // ASSERT
        assertEquals(entity.getContact(), dm.getContact());
    }

    @Test
    void toDataModelShouldCopyPublicationTypeIdsContent() {
        // ARRANGE
        AppraisalEntity entity = validEntity();

        // ACT
        AppraisalEntityDM dm = assembler.toDM(entity);

        // ASSERT
        assertEquals(entity.getPublicationTypeIds(), dm.getPublicationTypeIds());
    }

    @Test
    void toDataModelShouldNotSharePublicationTypeIdsReference() {

        // ARRANGE
        AppraisalEntity entity = validEntity();

        // ACT
        AppraisalEntityDM dm = assembler.toDM(entity);

        // ASSERT
        assertNotSame(entity.getPublicationTypeIds(), dm.getPublicationTypeIds());
    }

    @Test
    void toDataModelShouldCopyGenreIdsContent() {
        // ARRANGE
        AppraisalEntity entity = validEntity();

        // ACT
        AppraisalEntityDM dm = assembler.toDM(entity);

        // ASSERT
        assertEquals(entity.getGenreIds(), dm.getGenreIds());
    }

    @Test
    void toDataModelShouldNotShareGenreIdsReference() {
        // ARRANGE
        AppraisalEntity entity = validEntity();

        // ACT
        AppraisalEntityDM dm = assembler.toDM(entity);

        // ASSERT
        assertNotSame(entity.getGenreIds(), dm.getGenreIds());
    }
}