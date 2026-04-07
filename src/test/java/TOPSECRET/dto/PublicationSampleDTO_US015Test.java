package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.PublicationType.PublicationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationSampleDTO_US015Test {

    // ========================================================
    // ================ Set Up ================================
    // ========================================================

    private BibliographicalData bd;
    private List<Author> authors;
    private Edition edition;

    @BeforeEach
    void setup() {

        bd = mock(BibliographicalData.class);
        when(bd.getTitle()).thenReturn("Clean Code");

        Author author = mock(Author.class);
        when(author.getName()).thenReturn("Robert C. Martin");
        authors = List.of(author);

        edition = mock(Edition.class);
        PublicationType pubType = mock(PublicationType.class);
        when(edition.getCode()).thenReturn(Code.fromIsbn13("9780132350884"));
        when(edition.getPublicationType()).thenReturn(pubType);
        when(pubType.getMediaFormat()).thenReturn("Book");
    }

    // ========================================================
    // ================ TO DTO ================================
    // ========================================================

    @Test
    void toDTO_ShouldMapAllFieldsCorrectly() {
        // Act
        PublicationSampleDTO_US015 dto = PublicationSampleDTO_US015.toDTO(bd, authors, edition);

        // Assert
        assertEquals("Clean Code", dto.getTitle());
        assertEquals(List.of("Robert C. Martin"), dto.getAuthorsName());
        assertEquals("9780132350884", dto.getCode());
        assertEquals("Book", dto.getMediaFormat());
    }

    @Test
    void toDTO_ShouldThrowNullPointerExceptionWhenBibliographicalDataIsNull() {
        // Assert
        assertThrows(IllegalArgumentException.class,
                () -> PublicationSampleDTO_US015.toDTO(null, authors, edition));
    }

    @Test
    void toDTO_ShouldThrowNullPointerExceptionWhenAuthorsIsNull() {
        // Assert
        assertThrows(IllegalArgumentException.class,
                () -> PublicationSampleDTO_US015.toDTO(bd, null, edition));
    }

    @Test
    void toDTO_ShouldThrowNullPointerExceptionWhenEditionIsNull() {
        // Assert
        assertThrows(IllegalArgumentException.class,
                () -> PublicationSampleDTO_US015.toDTO(bd, authors, null));
    }

    // ========================================================
    // ================ EQUALS ================================
    // ========================================================

    @Test
    void equals_ShouldReturnTrueForSameObject() {
        // Arrange
        PublicationSampleDTO_US015 dto1 = new PublicationSampleDTO_US015(
                "Title", List.of("Author"), "123", "BOOK");

        PublicationSampleDTO_US015 dto2 = new PublicationSampleDTO_US015(
                "Title", List.of("Author"), "123", "BOOK");

        // Act
        boolean result = dto1.equals(dto2);

        // Assert
        assertTrue(result);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalseForDifferentObjects() {
        // Arrange
        PublicationSampleDTO_US015 dto1 = new PublicationSampleDTO_US015(
                "Title1", List.of("Author"), "123", "BOOK");

        PublicationSampleDTO_US015 dto2 = new PublicationSampleDTO_US015(
                "Title2", List.of("Author"), "123", "BOOK");

        // Act
        boolean result = dto1.equals(dto2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_ShouldReturnFalseWhenComparingWithNull() {
        // Arrange
        PublicationSampleDTO_US015 dto = new PublicationSampleDTO_US015(
                "Title", List.of("Author"), "123", "BOOK");

        // Act + Assert
        assertFalse(dto.equals(null));
    }

    @Test
    void equals_ShouldReturnFalseWhenComparingWithDifferentObject() {
        // Arrange
        PublicationSampleDTO_US015 dto = new PublicationSampleDTO_US015(
                "Title", List.of("Author"), "123", "BOOK");

        // Act + Assert
        assertFalse(dto.equals("Not a DTO"));
    }


    // ========================================================
    // ================ HASHCODE ==============================
    // ========================================================

    @Test
    void hashCode_ShouldReturnTheSameHashCodeForTheSameObject() {
        // Arrange
        PublicationSampleDTO_US015 dto = new PublicationSampleDTO_US015(
                "Title", List.of("Author"), "123", "BOOK");

        // Act
        int hash1 = dto.hashCode();
        int hash2 = dto.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    void hashCode_ShouldBeDifferentForDifferentObjects() {
        // Arrange
        PublicationSampleDTO_US015 dto1 = new PublicationSampleDTO_US015(
                "Title1", List.of("Author"), "123", "BOOK");

        PublicationSampleDTO_US015 dto2 = new PublicationSampleDTO_US015(
                "Title2", List.of("Author"), "123", "BOOK");

        // Act
        int hash1 = dto1.hashCode();
        int hash2 = dto2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
    }
}