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

class EditionMapperTest {

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
                LocalDate.of(1990, 10, 10),
                "HARDBACK"
        );
    }

    private Edition mockEdition() {
        Edition edition = mock(Edition.class);
        EditionId editionId = mock(EditionId.class);
        Code code = mock(Code.class);
        PhysicalAttributes physicalAttributes = mock(PhysicalAttributes.class);
        Dimensions dimensions = mock(Dimensions.class);
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);
        PublisherID publisherID = mock(PublisherID.class);

        when(editionId.getID()).thenReturn("edition-uuid");
        when(edition.id()).thenReturn(editionId);

        when(edition.getEditionNumber()).thenReturn("1st Edition");

        when(code.toString()).thenReturn("9780306406157");
        when(edition.getCode()).thenReturn(code);

        when(publicationTypeId.getId()).thenReturn("pub-type-1");
        when(edition.getPublicationTypeID()).thenReturn(publicationTypeId);

        when(publisherID.getId()).thenReturn("publisher-1");
        when(edition.getPublisherID()).thenReturn(publisherID);

        when(edition.getMediaFormat()).thenReturn("Book");
        when(edition.getEditionLanguage()).thenReturn(Locale.forLanguageTag("en"));
        when(edition.getEditionDate()).thenReturn(LocalDate.of(2010, 1, 1));
        when(edition.getTypeOfBinding()).thenReturn(TypeOfBinding.HARDBACK);

        when(dimensions.getHeight()).thenReturn(20.0);
        when(dimensions.getWidth()).thenReturn(15.0);
        when(dimensions.getDepth()).thenReturn(3.0);

        when(physicalAttributes.getDimensions()).thenReturn(dimensions);
        when(physicalAttributes.getWeight()).thenReturn(500.0);
        when(physicalAttributes.getNumberOfPages()).thenReturn(300);

        when(edition.getPhysicalAttributes()).thenReturn(physicalAttributes);

        return edition;
    }

    @Test
    void toCode_shouldReturnIsbn13_whenIsbn13IsProvided() {
        CreateEditionDTO dto = validDto();

        Code result = EditionMapper.toCode(dto);

        assertEquals("9780306406157", result.getIsbn13());
    }

    @Test
    void toCode_shouldReturnIsbn10_whenOnlyIsbn10IsProvided() {
        CreateEditionDTO dto = new CreateEditionDTO(
                "1st Edition",
                "0306406152",
                null,
                null,
                20.0, 15.0, 3.0,
                500.0, 300,
                "pub-type-1",
                "publisher-1",
                "Book",
                "en",
                LocalDate.of(1990, 10, 10),
                "HARDBACK"
        );

        Code result = EditionMapper.toCode(dto);

        assertEquals("0306406152", result.getIsbn10());
    }

    @Test
    void toCode_shouldReturnIssn_whenOnlyIssnIsProvided() {
        CreateEditionDTO dto = new CreateEditionDTO(
                "1st Edition",
                null,
                null,
                "03178471",
                20.0, 15.0, 3.0,
                500.0, 300,
                "pub-type-1",
                "publisher-1",
                "Magazine",
                "en",
                LocalDate.of(1990, 10, 10),
                "PAPERBACK"
        );

        Code result = EditionMapper.toCode(dto);

        assertEquals("03178471", result.getIssn());
    }

    @Test
    void toCode_shouldReturnNull_whenNoCodeIsProvided() {
        CreateEditionDTO dto = new CreateEditionDTO(
                "1st Edition",
                null,
                null,
                null,
                20.0, 15.0, 3.0,
                500.0, 300,
                "pub-type-1",
                "publisher-1",
                "Book",
                "en",
                LocalDate.of(1990, 10, 10),
                "HARDBACK"
        );

        Code result = EditionMapper.toCode(dto);

        assertNull(result);
    }

    @Test
    void toPhysicalAttributes_shouldReturnCorrectDimensions_whenDTOIsValid() {
        CreateEditionDTO dto = validDto();

        PhysicalAttributes result = EditionMapper.toPhysicalAttributes(dto);

        assertEquals(20.0, result.getDimensions().getHeight());
        assertEquals(15.0, result.getDimensions().getWidth());
        assertEquals(3.0, result.getDimensions().getDepth());
    }

    @Test
    void toPhysicalAttributes_shouldReturnCorrectWeightAndPages_whenDTOIsValid() {
        CreateEditionDTO dto = validDto();

        PhysicalAttributes result = EditionMapper.toPhysicalAttributes(dto);

        assertEquals(500.0, result.getWeight());
        assertEquals(300, result.getNumberOfPages());
    }

    @Test
    void toPublicationTypeId_shouldReturnCorrectId_whenDTOIsValid() {
        PublicationTypeId result = EditionMapper.toPublicationTypeId("pub-type-1");

        assertEquals("pub-type-1", result.getId());
    }

    @Test
    void toPublisherId_shouldReturnCorrectId_whenDTOIsValid() {
        PublisherID result = EditionMapper.toPublisherId("publisher-1");

        assertEquals("publisher-1", result.getId());
    }

    @Test
    void toEditionLanguage_shouldReturnCorrectLocale_whenDTOIsValid() {
        Locale result = EditionMapper.toEditionLanguage("en");

        assertEquals(Locale.forLanguageTag("en"), result);
    }

    @Test
    void toTypeOfBinding_shouldReturnCorrectBinding_whenDTOIsValid() {
        TypeOfBinding result = EditionMapper.toTypeOfBinding("HARDBACK");

        assertEquals(TypeOfBinding.HARDBACK, result);
    }

    @Test
    void toTypeOfBinding_shouldThrowException_whenInvalidBinding() {
        assertThrows(IllegalArgumentException.class,
                () -> EditionMapper.toTypeOfBinding("INVALID_BINDING"));
    }

    @Test
    void toDTO_shouldReturnCorrectIdentityFields_whenEditionIsValid() {
        Edition edition = mockEdition();

        EditionDTO result = EditionMapper.toDTO(edition);

        assertEquals("edition-uuid", result.getEditionId());
        assertEquals("1st Edition", result.getEditionNumber());
        assertEquals("9780306406157", result.getCode());
    }

    @Test
    void toDTO_shouldReturnCorrectPublicationInfo_whenEditionIsValid() {
        Edition edition = mockEdition();

        EditionDTO result = EditionMapper.toDTO(edition);

        assertEquals("pub-type-1", result.getPublicationTypeId());
        assertEquals("publisher-1", result.getPublisherId());
        assertEquals("Book", result.getMediaFormat());
        assertEquals("en", result.getEditionLanguage());
        assertEquals(LocalDate.of(2010, 1, 1), result.getEditionDate());
        assertEquals(TypeOfBinding.HARDBACK.toString(), result.getTypeOfBinding());
    }

    @Test
    void toDTO_shouldReturnCorrectDimensions_whenEditionIsValid() {
        Edition edition = mockEdition();

        EditionDTO result = EditionMapper.toDTO(edition);

        assertEquals(20.0, result.getHeight());
        assertEquals(15.0, result.getWidth());
        assertEquals(3.0, result.getDepth());
    }

    @Test
    void toDTO_shouldReturnCorrectWeightAndPages_whenEditionIsValid() {
        Edition edition = mockEdition();

        EditionDTO result = EditionMapper.toDTO(edition);

        assertEquals(500.0, result.getWeight());
        assertEquals(300, result.getNumberOfPages());
    }

    @Test
    void toDTO_shouldThrowException_whenEditionIsNull() {
        assertThrows(NullPointerException.class,
                () -> EditionMapper.toDTO(null));
    }

    @Test
    void toDTOs_shouldConvertListOfEditionsToListOfDTOs() {
        Edition edition = mockEdition();

        List<EditionDTO> result = EditionMapper.toDTOs(List.of(edition));

        assertEquals(1, result.size());
        assertEquals("edition-uuid", result.get(0).getEditionId());
    }

    @Test
    void toDTOs_shouldReturnEmptyList_whenInputIsEmpty() {
        List<EditionDTO> result = EditionMapper.toDTOs(List.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOs_shouldThrowException_whenListIsNull() {
        assertThrows(NullPointerException.class,
                () -> EditionMapper.toDTOs(null));
    }
}