package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationSampleDTO_US015Test {

    // ========================================================
    // ================ Set Up ================================
    // ========================================================

    private PublicationSample pubSample;
    private BibliographicalData bd;
    private Edition edition;
    private Condition condition;
    private User owner;

    @BeforeEach
    void setup() {

        // Arrange
        Author author = mock(Author.class);
        Profile profile = mock(Profile.class);
        Publisher  publisher = mock(Publisher.class);
        PublicationType pubType = mock(PublicationType.class);

        pubSample = mock(PublicationSample.class);
        bd = mock(BibliographicalData.class);
        edition = mock(Edition.class);
        owner = mock(User.class);


        when(pubSample.getBibliographicalData()).thenReturn(bd);
        when(pubSample.getEdition()).thenReturn(edition);
        when(pubSample.getCondition()).thenReturn(Condition.LIKE_NEW);
        when(pubSample.getPublicationStatus()).thenReturn(PublicationStatus.FOR_SALE);
        when(pubSample.getOwner()).thenReturn(owner);

        when(bd.getTitle()).thenReturn("Clean Code");
        when(bd.getAuthors()).thenReturn(List.of(author));
        when(author.getProfile()).thenReturn(profile);
        when(profile.getName()).thenReturn("Robert C. Martin");
        when(bd.getGenres()).thenReturn(List.of(new Genre("Programming")));

        when(edition.getEditionNumber()).thenReturn("3rd");
        when(edition.getCode()).thenReturn(Code.fromIsbn13("9780132350884"));
        when(edition.getPublicationType()).thenReturn(pubType);
        when(pubType.getMediaFormat()).thenReturn(MediaFormat.BOOK);
        when(edition.getEditionLanguage()).thenReturn(Locale.ENGLISH);
        when(edition.getPublisher()).thenReturn(publisher);
        when(publisher.getName()).thenReturn("Prentice Hall");

        when(owner.getUserName()).thenReturn("silvia");

    }

    // ========================================================
    // ================ Tests =================================
    // ========================================================

    // Tests to Equals =================================================================================================
    @Test
    void equals_ShouldReturnTrueForSameObject() {
        // Arrange
        PublicationSampleDTO_US015 dto1 = new PublicationSampleDTO_US015(
                "Title", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

        PublicationSampleDTO_US015 dto2 = new PublicationSampleDTO_US015(
                "Title", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

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
                "Title1", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

        PublicationSampleDTO_US015 dto2 = new PublicationSampleDTO_US015(
                "Title2", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

        // Act
        boolean result = dto1.equals(dto2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_ShouldReturnFalseWhenComparingWithNullOrDifferentClass() {
        // Arrange
        PublicationSampleDTO_US015 dto = new PublicationSampleDTO_US015(
                "Title", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

        // Act + Assert
        assertFalse(dto.equals(null));
        assertFalse(dto.equals("Not a DTO"));
    }

    // Tests to HashCode ===============================================================================================
    @Test
    void hashCode_ShouldReturnTheSameHashCodeForTheSameObject() {
        // Arrange
        PublicationSampleDTO_US015 dto = new PublicationSampleDTO_US015(
                "Title", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

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
                "Title1", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

        PublicationSampleDTO_US015 dto2 = new PublicationSampleDTO_US015(
                "Title2", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

        // Act
        int hash1 = dto1.hashCode();
        int hash2 = dto2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
    }


    // Tests to toDTO =================================================================================================
    @Test
    void toDTO_ShouldMapAllFieldsCorrectly() {
        // Act
        PublicationSampleDTO_US015 dto = PublicationSampleDTO_US015.toDTO(pubSample);

        // Assert
        assertEquals("Clean Code", dto.getTitle());
        assertEquals(List.of("Robert C. Martin"), dto.getAuthorsName());
        assertEquals(List.of("Programming"), dto.getGenres());
        assertEquals("silvia", dto.getOwnerUsername());
        assertEquals("FOR_SALE", dto.getPublicationStatus());
        assertEquals("3rd", dto.getEditionNumber());
        assertEquals("9780132350884", dto.getCode());
        assertEquals("BOOK", dto.getMediaFormat());
        assertEquals("en", dto.getEditionLanguage());
        assertEquals("Prentice Hall", dto.getPublisherName());
        assertEquals("LIKE_NEW", dto.getCondition());
    }
}