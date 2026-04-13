package TOPSECRET.dto;


import TOPSECRET.domain.ValueObjects.PublisherID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        PublisherID id = new PublisherID("pub123");

        // Act
        US027PublisherDTO dto = new US027PublisherDTO(id, "Penguin Books");

        // Assert
        assertEquals(id, dto.publisherId());
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
        PublisherID id = new PublisherID("pub123");

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new US027PublisherDTO(id, null));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        // Arrange
        PublisherID id = new PublisherID("pub123");

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new US027PublisherDTO(id, "   "));
    }
}