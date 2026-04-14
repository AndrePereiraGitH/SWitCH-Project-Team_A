package TOPSECRET.dto;


import TOPSECRET.domain.ValueObjects.PublisherID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the US027PublisherDTO record.
 * <p>
 * These tests verify correct creation of the DTO with valid data and ensure
 * that invalid arguments (null or blank fields) trigger the expected exceptions.
 * </p>
 */
class US027PublisherDTOTest {

    @Test
    void shouldCreateDTOWhenArgumentsAreValid() {
        // Arrange
        PublisherID idDouble = mock(PublisherID.class);

        // Act
        US027PublisherDTO dto = new US027PublisherDTO(idDouble, "Penguin Books");

        // Assert
        assertEquals(idDouble, dto.publisherId());
        assertEquals("Penguin Books", dto.name());
    }

    @Test
    void shouldThrowExceptionWhenPublisherIdIsNull() {
        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new US027PublisherDTO(null, "Penguin Books"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // Arrange
        PublisherID idDouble = mock(PublisherID.class);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new US027PublisherDTO(idDouble, null));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        // Arrange
        PublisherID idDouble = mock(PublisherID.class);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new US027PublisherDTO(idDouble, "   "));
    }
}