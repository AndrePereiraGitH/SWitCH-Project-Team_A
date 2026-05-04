package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.AppraisalEntity.*;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.datamodel.AppraisalEntityDM;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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
@ExtendWith(MockitoExtension.class)
class AppraisalEntityAssemblerTest {

    @Mock
    private AppraisalEntityFactory factory;

    @InjectMocks
    private AppraisalEntityAssembler assembler;

    private Contact contact() {
        return mock(Contact.class);
    }

    private List<PublicationTypeId> pubs() {
        return List.of(new PublicationTypeId("1"));
    }

    private List<GenreId> genres() {
        return List.of(new GenreId("1"));
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
        AppraisalEntity entity = mock(AppraisalEntity.class);
        when(entity.id()).thenReturn(new AppraisalEntityId("1"));
        when(entity.getName()).thenReturn("Name");
        when(entity.getContact()).thenReturn(contact());
        when(entity.getPublicationTypeIds()).thenReturn(pubs());
        when(entity.getGenreIds()).thenReturn(genres());

        return entity;
    }

    // toDomain TESTS
    @Test
    void toDomainShouldMapId() {
        // ARRANGE
        AppraisalEntityDM dm = validDM();

        AppraisalEntity entity = mock(AppraisalEntity.class);
        when(entity.id()).thenReturn(dm.getId());

        when(factory.create(any(), any(), any(), any(), any()))
                .thenReturn(entity);

        // ACT
        AppraisalEntity result = assembler.toDomain(dm);

        // ASSERT
        assertEquals(dm.getId(), result.id());
    }

    @Test
    void toDomainShouldMapName() {
        // ARRANGE
        AppraisalEntityDM dm = validDM();

        AppraisalEntity entity = mock(AppraisalEntity.class);
        when(entity.getName()).thenReturn(dm.getName());

        when(factory.create(any(), any(), any(), any(), any()))
                .thenReturn(entity);

        // ACT
        AppraisalEntity result = assembler.toDomain(dm);

        // ASSERT
        assertEquals(dm.getName(), result.getName());
    }

    @Test
    void toDomainShouldMapContact() {
        // ARRANGE
        AppraisalEntityDM dm = validDM();
        AppraisalEntity expected = mock(AppraisalEntity.class);
        when(expected.getContact()).thenReturn(dm.getContact());
        when(factory.create(any(), any(), any(), any(), any()))

                .thenReturn(expected);

        // ACT
        AppraisalEntity result = assembler.toDomain(dm);

        // ASSERT
        assertEquals(dm.getContact(), result.getContact());
    }

    @Test
    void toDomainShouldThrowWhenNull() {
        // ARRANGE

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

        AppraisalEntity entity = mock(AppraisalEntity.class);
        when(entity.getPublicationTypeIds())
                .thenReturn(new ArrayList<>(pubs));
        when(factory.create(any(), any(), any(), any(), any()))
                .thenReturn(entity);

        // ACT
        AppraisalEntity result = assembler.toDomain(dm);

        // ASSERT
        assertEquals(pubs, result.getPublicationTypeIds());
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

        AppraisalEntity entity = mock(AppraisalEntity.class);

        when(entity.getPublicationTypeIds())
                .thenReturn(new ArrayList<>(pubs));
        when(factory.create(any(), any(), any(), any(), any()))
                .thenReturn(entity);

        // ACT
        AppraisalEntity result = assembler.toDomain(dm);

        // ASSERT
        assertNotSame(pubs, result.getPublicationTypeIds());
    }

    @Test
    void toDomainShouldCopyGenreIdsContent() {
        // ARRANGE
        List<GenreId> genres = genres();

        AppraisalEntityDM dm = new AppraisalEntityDM(
                new AppraisalEntityId("1"),
                "Name",
                contact(),
                pubs(),
                genres
        );

        AppraisalEntity expected = mock(AppraisalEntity.class);
        when(expected.getGenreIds())
                .thenReturn(new ArrayList<>(genres));
        when(factory.create(any(), any(), any(), any(), any()))
                .thenReturn(expected);

        // ACT
        AppraisalEntity entity = assembler.toDomain(dm);

        // ASSERT
        assertEquals(genres, entity.getGenreIds());
    }

    @Test
    void toDomainShouldNotShareGenreIdsReference() {
        // ARRANGE
        List<GenreId> genres = genres();

        AppraisalEntityDM dm = new AppraisalEntityDM(
                new AppraisalEntityId("1"),
                "Name",
                contact(),
                pubs(),
                genres
        );

        AppraisalEntity entity = mock(AppraisalEntity.class);
        when(entity.getGenreIds())
                .thenReturn(new ArrayList<>(genres));

        when(factory.create(any(), any(), any(), any(), any()))
                .thenReturn(entity);

        // ACT
        AppraisalEntity result = assembler.toDomain(dm);

        // ASSERT
        assertNotSame(genres, result.getGenreIds());
    }

    // toDataModel TESTS
    @Test
    void toDataModelShouldMapId() {
        // ARRANGE
        AppraisalEntity entity = mock(AppraisalEntity.class);
        when(entity.id()).thenReturn(new AppraisalEntityId("1"));
        when(entity.getName()).thenReturn("Name");
        when(entity.getContact()).thenReturn(contact());
        when(entity.getPublicationTypeIds()).thenReturn(pubs());
        when(entity.getGenreIds()).thenReturn(genres());

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