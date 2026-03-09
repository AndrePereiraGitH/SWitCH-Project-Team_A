package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PublicationTypeDTO_US004.
 *
 * Validates:
 * - Successful object creation
 * - Getter correctness
 * - Constructor validations
 */
class PublicationTypeDTO_US004DependenciesTest {

    /**
     * Verifies successful creation of DTO with valid data.
     */
    @Test
    void shouldCreateDTOWhenDataIsValid() {

        // ARRANGE
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;
        String details = "Standard";

        // ACT
        PublicationTypeDTO_US004 dto =
                new PublicationTypeDTO_US004(mediaFormat, typeOfBinding, details);

        // ASSERT
        assertEquals(mediaFormat, dto.getMediaFormat());
        assertEquals(typeOfBinding, dto.getTypeOfBinding());
        assertEquals(details, dto.getDetails());
    }

    /**
     * Verifies exception when MediaFormat is null.
     */
    @Test
    void shouldThrowExceptionWhenMediaFormatIsNull() {

        // ARRANGE
        TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationTypeDTO_US004(null, typeOfBinding, "Standard")
        );
    }

    /**
     * Verifies exception when TypeOfBinding is null.
     */
    @Test
    void shouldThrowExceptionWhenTypeOfBindingIsNull() {

        // ARRANGE
        MediaFormat mediaFormat = MediaFormat.BOOK;

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationTypeDTO_US004(mediaFormat, null, "Standard")
        );
    }

    /**
     * Verifies exception when details is null.
     */
    @Test
    void shouldThrowExceptionWhenDetailsIsNull() {

        // ARRANGE
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationTypeDTO_US004(mediaFormat, typeOfBinding, null)
        );
    }

    /**
     * Verifies exception when details is blank.
     */
    @Test
    void shouldThrowExceptionWhenDetailsIsBlank() {

        // ARRANGE
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationTypeDTO_US004(mediaFormat, typeOfBinding, "   ")
        );
    }

    /**
     * Verifies DTO works correctly with different enum values.
     */
    @Test
    void shouldAllowDifferentEnumValues() {

        // ARRANGE
        MediaFormat mediaFormat = MediaFormat.MAGAZINE;
        TypeOfBinding typeOfBinding = TypeOfBinding.PAPERBACK;
        String details = "Pocket edition";

        // ACT
        PublicationTypeDTO_US004 dto =
                new PublicationTypeDTO_US004(mediaFormat, typeOfBinding, details);

        // ASSERT
        assertEquals(MediaFormat.MAGAZINE, dto.getMediaFormat());
        assertEquals(TypeOfBinding.PAPERBACK, dto.getTypeOfBinding());
        assertEquals(details, dto.getDetails());
    }


}