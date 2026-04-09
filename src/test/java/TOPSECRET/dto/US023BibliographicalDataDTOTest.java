package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link US023BibliographicalDataDTO} record.
 * <p>
 * Verifies correct field initialization, null-guard validation,
 * and the record-generated equals, hashCode, and toString behaviour.
 * </p>
 */
class US023BibliographicalDataDTOTest {

    // =================================================================================================================
    // ==================== CONSTRUCTOR ================================================================================
    // =================================================================================================================

    @Test
    void constructor_shouldSetAllFields() {
        // Arrange
        BibliographicalDataID expectedBDId = new BibliographicalDataID("bdId");
        String expectedTitle = "Os Maias";
        List<String> expectedAuthors = List.of("Eça de Queirós");

        // Act
        US023BibliographicalDataDTO dto = new US023BibliographicalDataDTO(expectedBDId, expectedTitle, expectedAuthors);

        // Assert
        assertEquals(expectedBDId, dto.bdId());
        assertEquals(expectedTitle, dto.pubTitle());
        assertEquals(expectedAuthors, dto.authorName());
    }

    @Test
    void constructor_shouldThrowExceptionWhenBDIdIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023BibliographicalDataDTO(null, "Title", List.of("Author1")));
    }

    @Test
    void constructor_shouldThrowExceptionWhenTitleIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023BibliographicalDataDTO(new BibliographicalDataID("bdId"), null, List.of("Author1")));
    }

    @Test
    void constructor_shouldThrowExceptionWhenAuthorNameIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023BibliographicalDataDTO(new BibliographicalDataID("bdId"), "Title", null));
    }


    // =================================================================================================================
    // ==================== RECORD BEHAVIOUR ===========================================================================
    // =================================================================================================================

    @Test
    void equals_shouldReturnTrueWhenTwoDTOsHaveSameValues() {
        // Act
        US023BibliographicalDataDTO dto1 =
                new US023BibliographicalDataDTO(new BibliographicalDataID("bdId"), "Title", List.of("Author1"));
        US023BibliographicalDataDTO dto2 =
                new US023BibliographicalDataDTO(new BibliographicalDataID("bdId"), "Title", List.of("Author1"));

        // Assert
        assertEquals(dto1, dto2);
    }

    @Test
    void equals_shouldReturnFalseWhenTwoDTOsHaveDifferentValues() {
        // Act
        US023BibliographicalDataDTO dto1 =
                new US023BibliographicalDataDTO(new BibliographicalDataID("bdId1"), "Title1", List.of("Author1"));
        US023BibliographicalDataDTO dto2 =
                new US023BibliographicalDataDTO(new BibliographicalDataID("bdId2"), "Title2", List.of("Author1"));

        // Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_shouldBeEqualWhenTwoDTOsHaveSameValues() {
        // Act
        US023BibliographicalDataDTO dto1 =
                new US023BibliographicalDataDTO(new BibliographicalDataID("bdId"), "Title", List.of("Author1"));
        US023BibliographicalDataDTO dto2 =
                new US023BibliographicalDataDTO(new BibliographicalDataID("bdId"), "Title", List.of("Author1"));

        // Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_shouldContainAllFields() {
        // Act
        US023BibliographicalDataDTO dto =
                new US023BibliographicalDataDTO(new BibliographicalDataID("bdId"), "Os Maias", List.of("Eça de Queirós"));

        String result = dto.toString();

        // Assert
        assertTrue(result.contains("bdId"));
        assertTrue(result.contains("Os Maias"));
        assertTrue(result.contains("Eça de Queirós"));
    }
}