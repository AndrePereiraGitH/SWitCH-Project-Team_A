package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class US004PublicationTypeDTOTest {

    @Test
    void shouldCreatePublicationTypeDTO() {

        // ARRANGE
        PublicationTypeId id = mock(PublicationTypeId.class);

        // ACT
        US004PublicationTypeDTO dto =
                new US004PublicationTypeDTO(id, "Book");

        // ASSERT
        assertEquals(id, dto.id());
        assertEquals("Book", dto.mediaFormat());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US004PublicationTypeDTO(null, "Book"));
    }

}