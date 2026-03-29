package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationSampleDTO_US028Test {

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
        Publisher  publisher = mock(Publisher.class);
        PublicationType pubType = mock(PublicationType.class);
        Code code = mock(Code.class);
        Genre genre = mock(Genre.class);

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
        when(author.getName()).thenReturn("Robert C. Martin");
        when(genre.getGenreType()).thenReturn("Programming");
        when(bd.getGenres()).thenReturn(List.of(genre));

        when(edition.getEditionNumber()).thenReturn("3rd");
        when(edition.getCode()).thenReturn(code);
        when(code.toString()).thenReturn("9780132350884");
        when(edition.getPublicationType()).thenReturn(pubType);
        when(pubType.getMediaFormat()).thenReturn("Book");
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
        PublicationSampleDTO_US028 dto1 = new PublicationSampleDTO_US028(
                "Title", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

        PublicationSampleDTO_US028 dto2 = new PublicationSampleDTO_US028(
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
        PublicationSampleDTO_US028 dto1 = new PublicationSampleDTO_US028(
                "Title1", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

        PublicationSampleDTO_US028 dto2 = new PublicationSampleDTO_US028(
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
        PublicationSampleDTO_US028 dto = new PublicationSampleDTO_US028(
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
        PublicationSampleDTO_US028 dto = new PublicationSampleDTO_US028(
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
        PublicationSampleDTO_US028 dto1 = new PublicationSampleDTO_US028(
                "Title1", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

        PublicationSampleDTO_US028 dto2 = new PublicationSampleDTO_US028(
                "Title2", List.of("Author"), List.of("Genre"), "Owner",
                "AVAILABLE", "1st", "123", "BOOK", "en",
                "Publisher", "Good");

        // Act
        int hash1 = dto1.hashCode();
        int hash2 = dto2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
    }


    // Tests to toDTO ==================================================================================================
    @Test
    void toDTO_ShouldMapAllFieldsCorrectly() {
        // Act
        PublicationSampleDTO_US028 dto = PublicationSampleDTO_US028.toDTO(pubSample);

        // Assert
        assertEquals("Clean Code", dto.getTitle());
        assertEquals(List.of("Robert C. Martin"), dto.getAuthorsName());
        assertEquals(List.of("Programming"), dto.getGenres());
        assertEquals("silvia", dto.getOwnerUsername());
        assertEquals("FOR_SALE", dto.getPublicationStatus());
        assertEquals("3rd", dto.getEditionNumber());
        assertEquals("9780132350884", dto.getCode());
        assertEquals("Book", dto.getMediaFormat());
        assertEquals("en", dto.getEditionLanguage());
        assertEquals("Prentice Hall", dto.getPublisherName());
        assertEquals("LIKE_NEW", dto.getCondition());
    }

    @Test
    void toDTO_ShouldThrowNullPointerExceptionWhenPublicationSampleIsNull() {
        // Act + Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> PublicationSampleDTO_US028.toDTO(null)
        );

        assertEquals("Publication sample can not be null",exception.getMessage());
    }

    // Tests to toDTOs =================================================================================================

    @Test
    void toDTOs_ShouldThrowNullPointerExceptionWhenListIsNull() {
        // Act + Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> PublicationSampleDTO_US028.toDTOs(null)
        );

        assertEquals("List of items can not be null", exception.getMessage());
    }

    @Test
    void toDTOs_ShouldThrowIllegalArgumentExceptionWhenListContainsNullElement() {
        // Arrange
        List<PublicationSample> listWithNull = new ArrayList<>();
        listWithNull.add(pubSample);
        listWithNull.add(null);

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> PublicationSampleDTO_US028.toDTOs(listWithNull));

        // Assert
        assertEquals("List of items must not contain null elements", exception.getMessage());
    }

    @Test
    void toDTOs_ShouldReturnEmptyListWhenInputListIsEmpty() {
        // Act
        List<PublicationSampleDTO_US028> dtoList = PublicationSampleDTO_US028.toDTOs(List.of());

        // Assert
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    void toDTOs_ShouldReturnListWithOneDTOWhenListHasOneElement() {
        // Act
        List<PublicationSampleDTO_US028> dtoList = PublicationSampleDTO_US028.toDTOs(List.of(pubSample));

        // Assert
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
    }

    @Test
    void toDTOs_ShouldReturnListWithSameSizeAsInputList() {
        // Arrange
        // New publicationSample
        PublicationSample anotherSample = mock(PublicationSample.class);
        when(anotherSample.getBibliographicalData()).thenReturn(bd);
        when(anotherSample.getEdition()).thenReturn(edition);
        when(anotherSample.getCondition()).thenReturn(Condition.LIKE_NEW);
        when(anotherSample.getPublicationStatus()).thenReturn(PublicationStatus.FOR_SALE);
        when(anotherSample.getOwner()).thenReturn(owner);

        // Act
        List<PublicationSampleDTO_US028> dtoList = PublicationSampleDTO_US028.toDTOs(List.of(pubSample, anotherSample));

        // Assert
        assertEquals(2, dtoList.size());
    }
    @Test
    void toDTOs_ShouldBeImmutableList() {
        // Act
        List<PublicationSampleDTO_US028> dtoList = PublicationSampleDTO_US028.toDTOs(List.of(pubSample));

        // Assert
        assertThrows(UnsupportedOperationException.class,
                () -> dtoList.add(mock(PublicationSampleDTO_US028.class)));
    }
}