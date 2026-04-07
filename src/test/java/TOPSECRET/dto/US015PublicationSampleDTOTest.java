package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link US015PublicationSampleDTO} record.
 * <p>
 * Verifies correct field initialization, null-guard validation,
 * and the record-generated equals, hashCode, and toString behaviour.
 * </p>
 */
class US015PublicationSampleDTOTest {

    // ========================================================
    // ================ CONSTRUCTOR ===========================
    // ========================================================

    @Test
    void constructor_shouldSetAllFields() {
        // Arrange
        String expectedTitle = "Os Maias";
        List<String> expectedAuthors = List.of("Eça de Queirós");
        String expectedCode = "978-1234567890";
        String expectedType = "BOOK";

        // Act
        US015PublicationSampleDTO dto = new US015PublicationSampleDTO(expectedTitle, expectedAuthors, expectedCode, expectedType);

        // Assert
        assertEquals(expectedTitle, dto.pubTitle());
        assertEquals(expectedAuthors, dto.authorName());
        assertEquals(expectedCode, dto.code());
        assertEquals(expectedType, dto.publicationType());
    }

    @Test
    void constructor_shouldThrowException_whenTitleIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US015PublicationSampleDTO(null, List.of("Author1"), "123", "BOOK"));
    }

    @Test
    void constructor_shouldThrowException_whenAuthorNameIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US015PublicationSampleDTO("Title", null, "123", "BOOK"));
    }

    @Test
    void constructor_shouldThrowException_whenCodeIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US015PublicationSampleDTO("Title", List.of("Author1"), null, "BOOK"));
    }

    @Test
    void constructor_shouldThrowException_whenPublicationTypeIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US015PublicationSampleDTO("Title", List.of("Author1"), "123", null));
    }

    // ========================================================
    // ================ RECORD METHODS ========================
    // ========================================================

    @Test
    void equals_shouldReturnTrue_whenTwoDTOsHaveSameValues() {
        US015PublicationSampleDTO dto1 = new US015PublicationSampleDTO("Title", List.of("Author1"), "123", "BOOK");
        US015PublicationSampleDTO dto2 = new US015PublicationSampleDTO("Title", List.of("Author1"), "123", "BOOK");

        assertEquals(dto1, dto2);
    }

    @Test
    void equals_shouldReturnFalse_whenTwoDTOsHaveDifferentValues() {
        US015PublicationSampleDTO dto1 = new US015PublicationSampleDTO("Title1", List.of("Author1"), "123", "BOOK");
        US015PublicationSampleDTO dto2 = new US015PublicationSampleDTO("Title2", List.of("Author1"), "123", "BOOK");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_shouldBeEqual_whenTwoDTOsHaveSameValues() {
        US015PublicationSampleDTO dto1 = new US015PublicationSampleDTO("Title", List.of("Author1"), "123", "BOOK");
        US015PublicationSampleDTO dto2 = new US015PublicationSampleDTO("Title", List.of("Author1"), "123", "BOOK");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_shouldContainAllFields() {
        US015PublicationSampleDTO dto = new US015PublicationSampleDTO("Os Maias", List.of("Eça de Queirós"), "978-1234567890", "BOOK");

        String result = dto.toString();

        assertTrue(result.contains("Os Maias"));
        assertTrue(result.contains("Eça de Queirós"));
        assertTrue(result.contains("978-1234567890"));
        assertTrue(result.contains("BOOK"));
    }
}