package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class US018PublicationSampleDTOTest {

    @Test
    void shouldCreateDTOSuccessfully() {

        // ARRANGE
        PublicationSampleId id = mock(PublicationSampleId.class);

        // ACT
        US018PublicationSampleDTO dto =
                new US018PublicationSampleDTO(id, "Title");

        // ASSERT
        assertEquals(id, dto.id());
        assertEquals("Title", dto.title());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {

        // ARRANGE

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US018PublicationSampleDTO(null, "Title"));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull() {

        // ARRANGE
        PublicationSampleId id = mock(PublicationSampleId.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US018PublicationSampleDTO(id, null));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsBlank() {

        // ARRANGE
        PublicationSampleId id = mock(PublicationSampleId.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US018PublicationSampleDTO(id, " "));
    }
}