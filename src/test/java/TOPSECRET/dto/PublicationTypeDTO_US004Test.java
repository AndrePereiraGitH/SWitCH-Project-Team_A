package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType.PublicationType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PublicationTypeDTO_US004}.
 * These tests validate:
 *     <li>DTO creation and validation rules</li>
 *     <li>Getter correctness</li>
 *     <li>Conversion from {@link PublicationType} domain objects to DTO objects</li>
 *     <li>Interaction with mocked domain collaborators</li>
 * Mockito is used to mock {@link PublicationType} objects in order to
 * isolate the DTO conversion logic.
 * </p>
 */
class PublicationTypeDTO_US004Test {

    /**
     * Ensures a DTO is created with valid attributes.
     */
    @Test
    void shouldCreateDTOWithValidValues() {

        // ARRANGE
        String mediaFormat = "Book";

        // ACT
        PublicationTypeDTO_US004 dto =
                new PublicationTypeDTO_US004(mediaFormat);

        // ASSERT
        assertEquals(mediaFormat, dto.getMediaFormat());
    }

    /**
     * Ensures an exception is thrown when MediaFormat is null.
     */
    @Test
    void shouldThrowExceptionWhenMediaFormatIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new PublicationTypeDTO_US004(null)
        );
    }


    /**
     * Ensures domain PublicationType objects are correctly converted into DTO objects.
     */
    @Test
    void shouldConvertPublicationTypesToDTO() {

        // ARRANGE
        PublicationType publicationTypeMock = mock(PublicationType.class);

        when(publicationTypeMock.getMediaFormat()).thenReturn("Book");


        // ACT
        List<PublicationTypeDTO_US004> result =
                PublicationTypeDTO_US004.publicationTypesToDTO(
                        List.of(publicationTypeMock)
                );

        // ASSERT
        assertEquals(1, result.size());

        PublicationTypeDTO_US004 dto = result.get(0);

        assertEquals("Book", dto.getMediaFormat());

    }

    /**
     * Ensures an empty list results in an empty DTO list.
     */
    @Test
    void shouldReturnEmptyListWhenPublicationTypeListIsEmpty() {

        // ARRANGE
        List<PublicationType> types = List.of();

        // ACT
        List<PublicationTypeDTO_US004> result =
                PublicationTypeDTO_US004.publicationTypesToDTO(types);

        // ASSERT
        assertTrue(result.isEmpty());
    }
}