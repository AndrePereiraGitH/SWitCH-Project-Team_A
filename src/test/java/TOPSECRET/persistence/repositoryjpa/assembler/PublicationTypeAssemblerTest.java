package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationTypeDM;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PublicationTypeAssembler}.
 *
 * <p>
 * Validates conversion logic between
 * {@link PublicationType} and
 * {@link PublicationTypeDM}.
 * </p>
 *
 * <ul>
 *     <li>Conversion from domain to data model</li>
 *     <li>Conversion from data model to domain</li>
 *     <li>Preservation of field values during mapping</li>
 *     <li>Validation of null inputs</li>
 * </ul>
 *
 * <p>
 * Ensures that persistence concerns remain isolated from the domain layer
 * and that transformations are consistent and reliable.
 * </p>
 *
 * <p>
 * Tests focus on correctness of mappings and proper handling of invalid data.
 * </p>
 */
class PublicationTypeAssemblerTest {

    // HELPER
    private final PublicationTypeFactory factory = new PublicationTypeFactory();
    private final PublicationTypeAssembler assembler = new PublicationTypeAssembler(factory);

    // toDomain
    @Test
    void shouldConvertToDomainCorrectly() {

        // ARRANGE
        PublicationTypeId id = new PublicationTypeId("1");
        PublicationTypeDM dm = new PublicationTypeDM(id, "Book");

        // ACT
        PublicationType result = assembler.toDomain(dm);

        // ASSERT
        assertEquals(id, result.id());
        assertEquals("Book", result.getMediaFormat());
    }

    @Test
    void shouldThrowWhenToDomainReceivesNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDomain(null));
    }

    // toDataModel
    @Test
    void shouldConvertToDataModelCorrectly() {

        // ARRANGE
        PublicationType entity = factory.create("Book");

        // ACT
        PublicationTypeDM dm = assembler.toDM(entity);

        // ASSERT
        assertEquals(entity.id(), dm.getId());
        assertEquals(entity.getMediaFormat(), dm.getMediaFormat());
    }

    @Test
    void shouldThrowWhenToDataModelReceivesNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDM(null));
    }

    @Test
    void shouldThrowWhenFactoryIsNull() {

        // ARRANGE

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new PublicationTypeAssembler(null));
    }

    // toDomainList
    @Test
    void shouldConvertListWithCorrectSize() {

        // ARRANGE
        PublicationTypeDM dm1 = new PublicationTypeDM(new PublicationTypeId("1"), "Book");
        PublicationTypeDM dm2 = new PublicationTypeDM(new PublicationTypeId("2"), "Magazine");

        List<PublicationTypeDM> dmList = List.of(dm1, dm2);

        // ACT
        List<PublicationType> result = assembler.toDomainList(dmList);

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void shouldConvertFirstElementCorrectly() {

        // ARRANGE
        PublicationTypeId id = new PublicationTypeId("1");
        PublicationTypeDM dm = new PublicationTypeDM(id, "Book");

        List<PublicationTypeDM> dmList = List.of(dm);

        // ACT
        PublicationType result = assembler.toDomainList(dmList).get(0);

        // ASSERT
        assertEquals(id, result.id());
    }

    @Test
    void shouldPreserveMediaFormatInConversion() {

        // ARRANGE
        PublicationTypeDM dm = new PublicationTypeDM(new PublicationTypeId("1"), "Book");

        List<PublicationTypeDM> dmList = List.of(dm);

        // ACT
        PublicationType result = assembler.toDomainList(dmList).get(0);

        // ASSERT
        assertEquals("Book", result.getMediaFormat());
    }

    @Test
    void shouldReturnEmptyListWhenInputListIsEmpty() {

        // ARRANGE
        List<PublicationTypeDM> dmList = List.of();

        // ACT
        List<PublicationType> result = assembler.toDomainList(dmList);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenToDomainListReceivesNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDomainList(null));
    }
}