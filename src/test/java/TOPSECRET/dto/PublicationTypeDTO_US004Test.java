package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import java.util.List;

import static TOPSECRET.domain.Enum.MediaFormat.BOOK;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PublicationTypeDTO_US004.
 */
class PublicationTypeDTO_US004Test {

    /**
     * Verifies conversion from domain to DTO.
     */
    @Test
    void shouldConvertPublicationTypeToDTO() {

        // Arrange
        PublicationType type =
                new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Standard");

        // Act
        PublicationTypeDTO_US004 dto =
                PublicationTypeDTO_US004.publicationTypeToDTO(type);

        // Assert
        assertEquals(MediaFormat.BOOK, dto.getMediaFormat());
        assertEquals(TypeOfBinding.HARDBACK, dto.getTypeOfBinding());
        assertEquals("Standard", dto.getDetails());
    }

    /**
     * Verifies list conversion.
     */
    @Test
    void shouldConvertPublicationTypeListToDTO() {

        // Arrange
        PublicationTypeDTO_US004 dto =
                new PublicationTypeDTO_US004(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Standard");

        // Act
        List<PublicationTypeDTO_US004> result =
                dto.publicationTypeListToDTO(
                        List.of(
                                new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "A"),
                                new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "B")
                        )
                );

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * Verifies conversion from DTO to domain.
     */
    @Test
    void shouldConvertDTOToPublicationType() {

        // Arrange
        PublicationTypeDTO_US004 dto =
                new PublicationTypeDTO_US004(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Standard");

        // Act
        PublicationType result = dto.dtoToPublicationType();

        // Assert
        assertEquals(MediaFormat.BOOK, result.getMediaFormat());
        assertEquals(TypeOfBinding.HARDBACK, result.getTypeOfBinding());
        assertEquals("Standard", result.getDetails());
    }
}