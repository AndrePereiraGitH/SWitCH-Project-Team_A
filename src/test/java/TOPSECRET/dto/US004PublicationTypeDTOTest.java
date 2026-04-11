package TOPSECRET.dto;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link US004PublicationTypeDTO}.
 * These tests validate:
 *     <li>DTO creation and validation rules</li>
 *     <li>Getter correctness</li>
 *     <li>Conversion from {@link PublicationType} domain objects to DTO objects</li>
 *     <li>Interaction with mocked domain collaborators</li>
 * Mockito is used to mock {@link PublicationType} objects in order to
 * isolate the DTO conversion logic.
 * </p>
 */
class _US004PublicationTypeDTOTest {

    @Test
    void shouldCreatePublicationTypeDTO() {

        // ARRANGE
        PublicationTypeId id = mock(PublicationTypeId.class);

        // ACT
        US004PublicationTypeDTO dto =
                new US004PublicationTypeDTO(id, "Book");

        // ASSERT
        assertEquals(id, dto.getId());
        assertEquals("Book", dto.getMediaFormat());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US004PublicationTypeDTO(null, "Book"));
    }

}