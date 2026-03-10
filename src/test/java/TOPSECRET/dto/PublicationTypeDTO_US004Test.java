package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
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
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding binding = TypeOfBinding.HARDBACK;
        String details = "Standard";

        // ACT
        PublicationTypeDTO_US004 dto =
                new PublicationTypeDTO_US004(mediaFormat, binding, details);

        // ASSERT
        assertEquals(mediaFormat, dto.getMediaFormat());
        assertEquals(binding, dto.getTypeOfBinding());
        assertEquals(details, dto.getDetails());
    }

    /**
     * Ensures an exception is thrown when MediaFormat is null.
     */
    @Test
    void shouldThrowExceptionWhenMediaFormatIsNull() {

        // ARRANGE
        TypeOfBinding binding = TypeOfBinding.HARDBACK;

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new PublicationTypeDTO_US004(null, binding, "details")
        );
    }

    /**
     * Ensures an exception is thrown when TypeOfBinding is null.
     */
    @Test
    void shouldThrowExceptionWhenTypeOfBindingIsNull() {

        // ARRANGE
        MediaFormat mediaFormat = MediaFormat.BOOK;

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new PublicationTypeDTO_US004(mediaFormat, null, "details")
        );
    }

    /**
     * Ensures an exception is thrown when details are null.
     */
    @Test
    void shouldThrowExceptionWhenDetailsIsNull() {

        // ARRANGE
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding binding = TypeOfBinding.HARDBACK;

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new PublicationTypeDTO_US004(mediaFormat, binding, null)
        );
    }

    /**
     * Ensures domain PublicationType objects are correctly converted into DTO objects.
     */
    @Test
    void shouldConvertPublicationTypesToDTO() {

        // ARRANGE
        PublicationType publicationTypeMock = mock(PublicationType.class);

        when(publicationTypeMock.getMediaFormat()).thenReturn(MediaFormat.BOOK);
        when(publicationTypeMock.getTypeOfBinding()).thenReturn(TypeOfBinding.HARDBACK);
        when(publicationTypeMock.getDetails()).thenReturn("Standard");

        // ACT
        List<PublicationTypeDTO_US004> result =
                PublicationTypeDTO_US004.publicationTypesToDTO(
                        List.of(publicationTypeMock)
                );

        // ASSERT
        assertEquals(1, result.size());

        PublicationTypeDTO_US004 dto = result.get(0);

        assertEquals(MediaFormat.BOOK, dto.getMediaFormat());
        assertEquals(TypeOfBinding.HARDBACK, dto.getTypeOfBinding());
        assertEquals("Standard", dto.getDetails());
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