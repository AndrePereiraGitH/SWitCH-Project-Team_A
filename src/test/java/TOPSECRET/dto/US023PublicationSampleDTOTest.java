package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link US023PublicationSampleDTO} record.
 * <p>
 * Verifies correct field initialization, null-guard validation,
 * and the record-generated equals, hashCode, and toString behaviour.
 * </p>
 */
class US023PublicationSampleDTOTest {

    // =================================================================================================================
    // ============== CONSTRUCTOR ======================================================================================
    // =================================================================================================================

    @Test
    void constructor_shouldSetAllFields() {
        // Arrange
        String expectedTitle = "Os Maias";
        List<String> expectedAuthors = List.of("Eça de Queirós");
        String expectedCode = "978-1234567890";
        String expectedType = "BOOK";
        String expectedSeller = "user123";
        String expectedPubStatus = "FOR_SALE";
        String expectedCondition = "NEW";

        // Act
        US023PublicationSampleDTO dto = new US023PublicationSampleDTO(expectedTitle, expectedAuthors, expectedCode,
                expectedType, expectedSeller, expectedPubStatus, expectedCondition);

        // Assert
        assertEquals(expectedTitle, dto.pubTitle());
        assertEquals(expectedAuthors, dto.authorName());
        assertEquals(expectedCode, dto.code());
        assertEquals(expectedType, dto.publicationType());
        assertEquals(expectedSeller, dto.seller());
        assertEquals(expectedPubStatus, dto.pubStatus());
        assertEquals(expectedCondition, dto.condition());
    }

    @Test
    void constructor_shouldThrowExceptionWhenTitleIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023PublicationSampleDTO(null, List.of("Author1"), "123", "BOOK",
                        "user123", "FOR_SALE", "NEW"));
    }

    @Test
    void constructor_shouldThrowExceptionWhenAuthorNameIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023PublicationSampleDTO("Title", null, "123", "BOOK",
                        "user123", "FOR_SALE", "NEW"));
    }

    @Test
    void constructor_shouldThrowExceptionWhenCodeIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023PublicationSampleDTO("Title", List.of("Author1"), null, "BOOK",
                        "user123", "FOR_SALE", "NEW"));
    }

    @Test
    void constructor_shouldThrowExceptionWhenPublicationTypeIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023PublicationSampleDTO("Title", List.of("Author1"), "123", null,
                        "user123", "FOR_SALE", "NEW"));
    }

    @Test
    void constructor_shouldThrowExceptionWhenUserIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023PublicationSampleDTO("Title", List.of("Author1"), "123", "BOOK",
                        null, "FOR_SALE", "NEW"));
    }

    @Test
    void constructor_shouldThrowExceptionWhenPublicationStatusIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023PublicationSampleDTO("Title", List.of("Author1"), "123", "BOOK",
                        "user123", null, "NEW"));
    }

    @Test
    void constructor_shouldThrowExceptionWhenConditionIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023PublicationSampleDTO("Title", List.of("Author1"), "123", "BOOK",
                        "user123", "FOR_SALE", null));
    }


    // =================================================================================================================
    // ============== RECORD BEHAVIOUR =================================================================================
    // =================================================================================================================

    @Test
    void equals_shouldReturnTrueWhenTwoDTOsHaveSameValues() {
        // Act
        US023PublicationSampleDTO dto1 = new US023PublicationSampleDTO("Title", List.of("Author1"),
                "123", "BOOK", "user123", "FOR_SALE", "NEW");
        US023PublicationSampleDTO dto2 = new US023PublicationSampleDTO("Title", List.of("Author1"),
                "123", "BOOK", "user123", "FOR_SALE", "NEW");

        // Arrange
        assertEquals(dto1, dto2);
    }

    @Test
    void equals_shouldReturnFalseWhenTwoDTOsHaveDifferentValues() {
        // Act
        US023PublicationSampleDTO dto1 = new US023PublicationSampleDTO("Title1", List.of("Author1"),
                "123", "BOOK", "user123", "FOR_SALE", "NEW");
        US023PublicationSampleDTO dto2 = new US023PublicationSampleDTO("Title2", List.of("Author1"),
                "123", "BOOK", "user123", "FOR_SALE", "NEW");

        // Arrange
        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_shouldBeEqualWhenTwoDTOsHaveSameValues() {
        // Act
        US023PublicationSampleDTO dto1 = new US023PublicationSampleDTO("Title", List.of("Author1"),
                "123", "BOOK", "user123", "FOR_SALE", "NEW");
        US023PublicationSampleDTO dto2 = new US023PublicationSampleDTO("Title", List.of("Author1"),
                "123", "BOOK", "user123", "FOR_SALE", "NEW");

        // Arrange
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_shouldContainAllFields() {
        // Act
        US023PublicationSampleDTO dto = new US023PublicationSampleDTO("Os Maias", List.of("Eça de Queirós"),
                "978-1234567890", "BOOK", "user123", "FOR_SALE", "NEW");

        String result = dto.toString();

        // Arrange
        assertTrue(result.contains("Os Maias"));
        assertTrue(result.contains("Eça de Queirós"));
        assertTrue(result.contains("978-1234567890"));
        assertTrue(result.contains("BOOK"));
    }
}