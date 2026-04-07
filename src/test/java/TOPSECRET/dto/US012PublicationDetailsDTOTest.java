package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link US012PublicationDetailsDTO} record.
 * <p>
 * Verifies correct field initialization, null-guard validation,
 * and the record-generated equals, hashCode, and toString behaviour.
 * </p>
 */
class US012PublicationDetailsDTOTest {

    // -------------------------------------------------------------------------
    // Constructor — happy path
    // -------------------------------------------------------------------------

    @Test
    void constructor_shouldSetAllFields() {
        // Arrange
        String expectedTitle = "Os Maias";
        List<String> expectedAuthors = List.of("Eça de Queirós");
        String expectedCode = "978-1234567890";
        String expectedType = "BOOK";

        // Act
        US012PublicationDetailsDTO dto = new US012PublicationDetailsDTO(expectedTitle, expectedAuthors, expectedCode, expectedType);

        // Assert
        assertEquals(expectedTitle, dto.pubTitle());
        assertEquals(expectedAuthors, dto.authorName());
        assertEquals(expectedCode, dto.code());
        assertEquals(expectedType, dto.publicationType());
    }

    // -------------------------------------------------------------------------
    // Constructor — null guards
    // -------------------------------------------------------------------------

    @Test
    void constructor_shouldThrowException_whenTitleIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US012PublicationDetailsDTO(null, List.of("Author1"), "123", "BOOK"));
    }

    @Test
    void constructor_shouldThrowException_whenAuthorNameIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US012PublicationDetailsDTO("Title", null, "123", "BOOK"));
    }

    @Test
    void constructor_shouldThrowException_whenCodeIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US012PublicationDetailsDTO("Title", List.of("Author1"), null, "BOOK"));
    }

    @Test
    void constructor_shouldThrowException_whenPublicationTypeIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US012PublicationDetailsDTO("Title", List.of("Author1"), "123", null));
    }

    // -------------------------------------------------------------------------
    // Record behaviour
    // -------------------------------------------------------------------------

    @Test
    void equals_shouldReturnTrue_whenTwoDTOsHaveSameValues() {
        US012PublicationDetailsDTO dto1 = new US012PublicationDetailsDTO("Title", List.of("Author1"), "123", "BOOK");
        US012PublicationDetailsDTO dto2 = new US012PublicationDetailsDTO("Title", List.of("Author1"), "123", "BOOK");

        assertEquals(dto1, dto2);
    }

    @Test
    void equals_shouldReturnFalse_whenTwoDTOsHaveDifferentValues() {
        US012PublicationDetailsDTO dto1 = new US012PublicationDetailsDTO("Title1", List.of("Author1"), "123", "BOOK");
        US012PublicationDetailsDTO dto2 = new US012PublicationDetailsDTO("Title2", List.of("Author1"), "123", "BOOK");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_shouldBeEqual_whenTwoDTOsHaveSameValues() {
        US012PublicationDetailsDTO dto1 = new US012PublicationDetailsDTO("Title", List.of("Author1"), "123", "BOOK");
        US012PublicationDetailsDTO dto2 = new US012PublicationDetailsDTO("Title", List.of("Author1"), "123", "BOOK");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_shouldContainAllFields() {
        US012PublicationDetailsDTO dto = new US012PublicationDetailsDTO("Os Maias", List.of("Eça de Queirós"), "978-1234567890", "BOOK");

        String result = dto.toString();

        assertTrue(result.contains("Os Maias"));
        assertTrue(result.contains("Eça de Queirós"));
        assertTrue(result.contains("978-1234567890"));
        assertTrue(result.contains("BOOK"));
    }
}