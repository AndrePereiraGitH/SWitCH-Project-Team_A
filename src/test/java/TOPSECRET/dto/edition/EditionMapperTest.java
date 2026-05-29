package TOPSECRET.dto.edition;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.edition.request.CreateEditionDTO;
import TOPSECRET.dto.edition.response.EditionDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EditionMapper}.
 *
 * <p>This test class verifies the correct mapping behavior between
 * {@link CreateEditionDTO} request objects, {@link Edition} domain objects,
 * and {@link EditionDTO} response objects.</p>
 *
 * <p>The tests validate:</p>
 * <ul>
 *     <li>Conversion of request DTO fields into domain value objects</li>
 *     <li>Conversion of a domain object into a response DTO</li>
 *     <li>Correct transfer of field values</li>
 *     <li>Validation of null arguments</li>
 *     <li>Handling of empty collections</li>
 * </ul>
 *
 * <p>Mockito is used to isolate the mapper from domain object implementations.</p>
 */
class EditionMapperTest {

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private CreateEditionDTO validDto() {
        return new CreateEditionDTO(
                "1st Edition",
                null,
                "9780306406157",
                null,
                20.0, 15.0, 3.0,
                500.0, 300,
                "pub-type-1",
                "publisher-1",
                "Book",
                "en",
                "2010-01-01",
                "HARDBACK"
        );
    }

    private Edition mockEdition() {
        Edition editionDouble = mock(Edition.class);
        EditionId editionIdDouble = mock(EditionId.class);
        Code codeDouble = mock(Code.class);
        PhysicalAttributes physicalAttributesDouble = mock(PhysicalAttributes.class);
        Dimensions dimensionsDouble = mock(Dimensions.class);
        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
        PublisherID publisherIDDouble = mock(PublisherID.class);

        when(editionIdDouble.getID()).thenReturn("edition-uuid");
        when(editionDouble.id()).thenReturn(editionIdDouble);
        when(editionDouble.getEditionNumber()).thenReturn("1st Edition");
        when(codeDouble.toString()).thenReturn("9780306406157");
        when(editionDouble.getCode()).thenReturn(codeDouble);
        when(publicationTypeIdDouble.getId()).thenReturn("pub-type-1");
        when(editionDouble.getPublicationTypeID()).thenReturn(publicationTypeIdDouble);
        when(publisherIDDouble.getId()).thenReturn("publisher-1");
        when(editionDouble.getPublisherID()).thenReturn(publisherIDDouble);
        when(editionDouble.getMediaFormat()).thenReturn("Book");
        when(editionDouble.getEditionLanguage()).thenReturn(Locale.forLanguageTag("en"));
        when(editionDouble.getEditionDate()).thenReturn(LocalDate.of(2010, 1, 1));
        when(editionDouble.getTypeOfBinding()).thenReturn(TypeOfBinding.HARDBACK);
        when(dimensionsDouble.getHeight()).thenReturn(20.0);
        when(dimensionsDouble.getWidth()).thenReturn(15.0);
        when(dimensionsDouble.getDepth()).thenReturn(3.0);
        when(physicalAttributesDouble.getDimensions()).thenReturn(dimensionsDouble);
        when(physicalAttributesDouble.getWeight()).thenReturn(500.0);
        when(physicalAttributesDouble.getNumberOfPages()).thenReturn(300);
        when(editionDouble.getPhysicalAttributes()).thenReturn(physicalAttributesDouble);

        return editionDouble;
    }

    // -------------------------------------------------------------------------
    // toCode()
    // -------------------------------------------------------------------------

    @Test
    void toCode_shouldReturnIsbn13_whenIsbn13IsProvided() {

        // Arrange
        CreateEditionDTO dto = validDto();

        // Act
        Code result = EditionMapper.toCode(dto);

        // Assert
        assertEquals("9780306406157", result.getIsbn13());
    }

    @Test
    void toCode_shouldReturnIsbn10_whenOnlyIsbn10IsProvided() {

        // Arrange
        CreateEditionDTO dto = new CreateEditionDTO(
                "1st Edition",
                "0306406152", null, null,
                20.0, 15.0, 3.0,
                500.0, 300,
                "pub-type-1", "publisher-1",
                "Book", "en", "1990-01-01", "HARDBACK"
        );

        // Act
        Code result = EditionMapper.toCode(dto);

        // Assert
        assertEquals("0306406152", result.getIsbn10());
    }

    @Test
    void toCode_shouldReturnIssn_whenOnlyIssnIsProvided() {

        // Arrange
        CreateEditionDTO dto = new CreateEditionDTO(
                "1st Edition",
                null, null, "03178471",
                20.0, 15.0, 3.0,
                500.0, 300,
                "pub-type-1", "publisher-1",
                "Magazine", "en", "1980-01-01", "PAPERBACK"
        );

        // Act
        Code result = EditionMapper.toCode(dto);

        // Assert
        assertEquals("03178471", result.getIssn());
    }

    @Test
    void toCode_shouldReturnNull_whenNoCodeIsProvided() {

        // Arrange
        CreateEditionDTO dto = new CreateEditionDTO(
                "1st Edition",
                null, null, null,
                20.0, 15.0, 3.0,
                500.0, 300,
                "pub-type-1", "publisher-1",
                "Book", "en", "1960-01-01", "HARDBACK"
        );

        // Act
        Code result = EditionMapper.toCode(dto);

        // Assert
        assertNull(result);
    }

    // -------------------------------------------------------------------------
    // toPhysicalAttributes()
    // -------------------------------------------------------------------------

    @Test
    void toPhysicalAttributes_shouldReturnCorrectDimensions_whenDTOIsValid() {

        // Arrange
        CreateEditionDTO dto = validDto();

        // Act
        PhysicalAttributes result = EditionMapper.toPhysicalAttributes(dto);

        // Assert
        assertEquals(20.0, result.getDimensions().getHeight());
        assertEquals(15.0, result.getDimensions().getWidth());
        assertEquals(3.0, result.getDimensions().getDepth());
    }

    @Test
    void toPhysicalAttributes_shouldReturnCorrectWeightAndPages_whenDTOIsValid() {

        // Arrange
        CreateEditionDTO dto = validDto();

        // Act
        PhysicalAttributes result = EditionMapper.toPhysicalAttributes(dto);

        // Assert
        assertEquals(500.0, result.getWeight());
        assertEquals(300, result.getNumberOfPages());
    }

    // -------------------------------------------------------------------------
    // toPublicationTypeId()
    // -------------------------------------------------------------------------

    @Test
    void toPublicationTypeId_shouldReturnCorrectId_whenDTOIsValid() {

        // Arrange
        CreateEditionDTO dto = validDto();

        // Act
        PublicationTypeId result = EditionMapper.toPublicationTypeId(dto);

        // Assert
        assertEquals("pub-type-1", result.getId());
    }

    // -------------------------------------------------------------------------
    // toPublisherId()
    // -------------------------------------------------------------------------

    @Test
    void toPublisherId_shouldReturnCorrectId_whenDTOIsValid() {

        // Arrange
        CreateEditionDTO dto = validDto();

        // Act
        PublisherID result = EditionMapper.toPublisherId(dto);

        // Assert
        assertEquals("publisher-1", result.getId());
    }

    // -------------------------------------------------------------------------
    // toEditionLanguage()
    // -------------------------------------------------------------------------

    @Test
    void toEditionLanguage_shouldReturnCorrectLocale_whenDTOIsValid() {

        // Arrange
        CreateEditionDTO dto = validDto();

        // Act
        Locale result = EditionMapper.toEditionLanguage(dto);

        // Assert
        assertEquals(Locale.forLanguageTag("en"), result);
    }

    // -------------------------------------------------------------------------
    // toEditionDate()
    // -------------------------------------------------------------------------

    @Test
    void toEditionDate_shouldReturnCorrectDate_whenDTOIsValid() {

        // Arrange
        CreateEditionDTO dto = validDto();

        // Act
        LocalDate result = EditionMapper.toEditionDate(dto);

        // Assert
        assertEquals(LocalDate.of(2010, 1, 1), result);
    }

    // -------------------------------------------------------------------------
    // toTypeOfBinding()
    // -------------------------------------------------------------------------

    @Test
    void toTypeOfBinding_shouldReturnCorrectBinding_whenDTOIsValid() {

        // Arrange
        CreateEditionDTO dto = validDto();

        // Act
        TypeOfBinding result = EditionMapper.toTypeOfBinding(dto);

        // Assert
        assertEquals(TypeOfBinding.HARDBACK, result);
    }

    @Test
    void toTypeOfBinding_shouldThrowException_whenInvalidBinding() {

        // Arrange
        CreateEditionDTO dto = new CreateEditionDTO(
                "1st Edition",
                null, "9780306406157", null,
                20.0, 15.0, 3.0,
                500.0, 300,
                "pub-type-1", "publisher-1",
                "Book", "en", "2010-01-01", "INVALID_BINDING"
        );

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> EditionMapper.toTypeOfBinding(dto));

    }

    // -------------------------------------------------------------------------
    // toDTO() — identity fields
    // -------------------------------------------------------------------------

    @Test
    void toDTO_shouldReturnCorrectIdentityFields_whenEditionIsValid() {

        // Arrange
        Edition editionDouble = mockEdition();

        // Act
        EditionDTO result = EditionMapper.toDTO(editionDouble);

        // Assert
        assertEquals("edition-uuid", result.getEditionId());
        assertEquals("1st Edition", result.getEditionNumber());
        assertEquals("9780306406157", result.getCode());
    }

    // -------------------------------------------------------------------------
    // toDTO() — publication info
    // -------------------------------------------------------------------------

    @Test
    void toDTO_shouldReturnCorrectPublicationInfo_whenEditionIsValid() {

        // Arrange
        Edition editionDouble = mockEdition();

        // Act
        EditionDTO result = EditionMapper.toDTO(editionDouble);

        // Assert
        assertEquals("pub-type-1", result.getPublicationTypeId());
        assertEquals("publisher-1", result.getPublisherId());
        assertEquals("Book", result.getMediaFormat());
        assertEquals("en", result.getEditionLanguage());
        assertEquals("2010-01-01", result.getEditionDate());
        assertEquals("Hardback", result.getTypeOfBinding());
    }

    // -------------------------------------------------------------------------
    // toDTO() — physical attributes
    // -------------------------------------------------------------------------

    @Test
    void toDTO_shouldReturnCorrectDimensions_whenEditionIsValid() {

        // Arrange
        Edition editionDouble = mockEdition();

        // Act
        EditionDTO result = EditionMapper.toDTO(editionDouble);

        // Assert
        assertEquals(20.0, result.getHeight());
        assertEquals(15.0, result.getWidth());
        assertEquals(3.0, result.getDepth());
    }

    @Test
    void toDTO_shouldReturnCorrectWeightAndPages_whenEditionIsValid() {

        // Arrange
        Edition editionDouble = mockEdition();

        // Act
        EditionDTO result = EditionMapper.toDTO(editionDouble);

        // Assert
        assertEquals(500.0, result.getWeight());
        assertEquals(300, result.getNumberOfPages());
    }

    @Test
    void toDTO_shouldThrowException_whenEditionIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> EditionMapper.toDTO(null));
    }

    // -------------------------------------------------------------------------
    // toDTOs()
    // -------------------------------------------------------------------------

    @Test
    void toDTOs_shouldConvertListOfEditionsToListOfDTOs() {

        // Arrange
        Edition editionDouble = mockEdition();

        // Act
        List<EditionDTO> result = EditionMapper.toDTOs(List.of(editionDouble));

        // Assert
        assertEquals(1, result.size());
        assertEquals("edition-uuid", result.get(0).getEditionId());
    }

    @Test
    void toDTOs_shouldReturnEmptyList_whenInputIsEmpty() {

        // Act
        List<EditionDTO> result = EditionMapper.toDTOs(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOs_shouldThrowException_whenListIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> EditionMapper.toDTOs(null));
    }
}