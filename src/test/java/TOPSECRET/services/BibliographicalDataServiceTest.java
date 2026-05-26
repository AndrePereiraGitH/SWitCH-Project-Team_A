package TOPSECRET.services;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.GenreId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BibliographicalDataServiceTest {

    private final String title = "title";
    private List<AuthorId> authors;
    private Locale language;
    private LocalDate date;
    private List<GenreId> genres;
    private final String chunks = "Chunks of text";

    @Mock
    private AuthorId authorId;

    @Mock
    private GenreId genreId;

    @Mock
    private BibliographicalDataFactory factory;

    @Mock
    private IBibliographicalDataRepository bibliographicalDataRepo;

    @Mock
    private IAuthorRepository authorRepo;

    @Mock
    private BibliographicalData createdBd;

    @Mock
    private BibliographicalData savedBd;

    @InjectMocks
    private BibliographicalDataService service;

    @BeforeEach
    void setUp() {
        authors = List.of(authorId);
        language = Locale.ENGLISH;
        date = LocalDate.of(2010, 1, 14);
        genres = List.of(genreId);
    }


    @Test
    void createAndAdd_shouldCreateSaveAndReturnSavedBibliographicalData() throws InstanceAlreadyExistsException {
        // Arrange
        when(factory.create(title, authors, language, date, genres, chunks)).thenReturn(createdBd);
        when(bibliographicalDataRepo.save(createdBd)).thenReturn(savedBd);

        // Act
        BibliographicalData result = service.createAndAdd(
                title, authors, language, date, genres, chunks
        );

        // Assert
        assertSame(savedBd, result);
    }

    @Test
    void createShouldThrowWhenTitleIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndAdd(null, authors, language, date, genres, chunks)
        );
    }

    @Test
    void createShouldThrowWhenAuthorsIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndAdd(title, null, language, date, genres, chunks)
        );
    }

    @Test
    void createShouldThrowWhenGenresIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndAdd(title, authors, language, date, null, chunks)
        );
    }

    @Test
    void createAndAddShouldAllowOptionalFieldsToBeNull() throws InstanceAlreadyExistsException {
        // Arrange
        when(factory.create(title, authors, null, null, genres, null)).thenReturn(createdBd);
        when(bibliographicalDataRepo.save(createdBd)).thenReturn(savedBd);

        // Act
        BibliographicalData result = service.createAndAdd(
                title, authors, null, null, genres, null
        );

        // Assert
        assertSame(savedBd, result);
    }

    @Test
    void createAndAddShouldThrowInstanceAlreadyExistsException() throws InstanceAlreadyExistsException {
        // Arrange
        when(factory.create(title, authors, language, date, genres, chunks)).thenReturn(createdBd);

        when(bibliographicalDataRepo.save(createdBd)).thenThrow(new InstanceAlreadyExistsException("Bibliographical Data already exists"));

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                service.createAndAdd(title, authors, language, date, genres, chunks)
        );
    }

    @Test
    void getAuthorsShouldReturnAuthorNamesWhenAuthorsAreValid() throws InstanceNotFoundException {
        // Arrange
        BibliographicalData bd = mock(BibliographicalData.class);

        AuthorId authorId1 = mock(AuthorId.class);
        AuthorId authorId2 = mock(AuthorId.class);

        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);

        when(bd.getAuthors()).thenReturn(List.of(authorId1, authorId2));

        when(authorRepo.findById(authorId1)).thenReturn(Optional.of(author1));
        when(authorRepo.findById(authorId2)).thenReturn(Optional.of(author2));

        when(author1.getName()).thenReturn("Author One");
        when(author2.getName()).thenReturn("Author Two");

        // Act
        List<String> result = service.getAuthors(bd);

        // Assert
        assertEquals(List.of("Author One", "Author Two"), result);
    }

    @Test
    void getAuthorsShouldThrowInstanceNotFoundExceptionWhenAuthorNotFound() {
        // Arrange
        BibliographicalData bd = mock(BibliographicalData.class);
        AuthorId authorId = mock(AuthorId.class);

        when(authorId.toString()).thenReturn("A1");
        when(bd.getAuthors()).thenReturn(List.of(authorId));
        when(authorRepo.findById(authorId)).thenReturn(Optional.empty());

        // Act
        InstanceNotFoundException exception = assertThrows(
                InstanceNotFoundException.class,
                () -> service.getAuthors(bd)
        );

        // Assert
        assertTrue(exception.getMessage().contains("Author was not found:"));
    }

    @Test
    void getAuthorsShouldReturnEmptyListWhenNoAuthorsExist() throws InstanceNotFoundException {
        // Arrange
        BibliographicalData bd = mock(BibliographicalData.class);

        when(bd.getAuthors()).thenReturn(List.of());

        // Act
        List<String> result = service.getAuthors(bd);

        // Arrange
        assertTrue(result.isEmpty());
    }
}