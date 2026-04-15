package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.mock;

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
        String expectedBDId = mock(BibliographicalDataID.class).toString();
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
                new US023BibliographicalDataDTO(mock(BibliographicalDataID.class).toString(), null, List.of("Author1")));
    }

    @Test
    void constructor_shouldThrowExceptionWhenAuthorNameIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023BibliographicalDataDTO(mock(BibliographicalDataID.class).toString(), "Title", null));
    }


    // =================================================================================================================
    // ==================== RECORD BEHAVIOUR ===========================================================================
    // =================================================================================================================

    @Test
    void equals_shouldReturnTrueWhenTwoDTOsHaveSameValues() {
        // Arrange
        String bdIdDouble = mock(BibliographicalDataID.class).toString();

        // Act
        US023BibliographicalDataDTO dto1 =
                new US023BibliographicalDataDTO(bdIdDouble, "Title", List.of("Author1"));
        US023BibliographicalDataDTO dto2 =
                new US023BibliographicalDataDTO(bdIdDouble, "Title", List.of("Author1"));

        // Assert
        assertEquals(dto1, dto2);
    }

    @Test
    void equals_shouldReturnFalseWhenTwoDTOsHaveDifferentValues() {
        // Act
        US023BibliographicalDataDTO dto1 =
                new US023BibliographicalDataDTO(mock(BibliographicalDataID.class).toString(), "Title1", List.of("Author1"));
        US023BibliographicalDataDTO dto2 =
                new US023BibliographicalDataDTO(mock(BibliographicalDataID.class).toString(), "Title2", List.of("Author1"));

        // Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_shouldBeEqualWhenTwoDTOsHaveSameValues() {
        // Arrange
        String bdIdDouble = mock(BibliographicalDataID.class).toString();

        // Act
        US023BibliographicalDataDTO dto1 =
                new US023BibliographicalDataDTO(bdIdDouble, "Title", List.of("Author1"));
        US023BibliographicalDataDTO dto2 =
                new US023BibliographicalDataDTO(bdIdDouble, "Title", List.of("Author1"));

        // Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_shouldContainAllFields() {
        // Arrange
        String expectedBDId = mock(BibliographicalDataID.class).toString();
        String expectedTitle = "Os Maias";
        List<String> expectedAuthors = List.of("Eça de Queirós");

        // Act
        US023BibliographicalDataDTO dto =
                new US023BibliographicalDataDTO(expectedBDId, expectedTitle, expectedAuthors);

        String result = dto.toString();

        // Assert
        assertTrue(result.contains(expectedBDId.toString()));
        assertTrue(result.contains(expectedTitle));
        assertTrue(result.contains(expectedAuthors.toString()));
    }
}