package TOPSECRET.services;

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
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

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
    private IBibliographicalDataRepository repo;

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
        when(repo.save(createdBd)).thenReturn(savedBd);

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
        when(repo.save(createdBd)).thenReturn(savedBd);

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

        when(repo.save(createdBd)).thenThrow(new InstanceAlreadyExistsException("Bibliographical Data already exists"));

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                service.createAndAdd(title, authors, language, date, genres, chunks)
        );
    }

}