package TOPSECRET.services;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BibliographicalDataServiceTest {

    private final String title = "title";
    private List<AuthorId> authors;
    private Locale language;
    private LocalDate date;
    private List<GenreID> genres;
    private final String chunks = "Chunks of text";
    private BibliographicalDataFactory factory;
    private IBibliographicalDataRepository repo;

    private BibliographicalDataService service;

    private BibliographicalData createdBd;
    private BibliographicalData savedBd;

    @BeforeEach
    void setUp() {
        AuthorId authorId = mock(AuthorId.class);
        GenreID genreId = mock(GenreID.class);
        authors = List.of(authorId);
        language = Locale.ENGLISH;
        date = LocalDate.of(2010, 1, 14);
        genres = List.of(genreId);
        factory = mock(BibliographicalDataFactory.class);
        repo = mock(IBibliographicalDataRepository.class);

        createdBd = mock(BibliographicalData.class);
        savedBd = mock(BibliographicalData.class);
    }


    @Test
    void createAndAdd_shouldCreateSaveAndReturnSavedBibliographicalData() throws InstanceAlreadyExistsException {
        // Arrange
        service = new BibliographicalDataService(repo, factory);

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
        // Arrange
        service = new BibliographicalDataService(repo, factory);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndAdd(null, authors, language, date, genres, chunks)
        );
    }

    @Test
    void createShouldThrowWhenAuthorsIsNull() {
        // Arrange
        service = new BibliographicalDataService(repo, factory);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndAdd(title, null, language, date, genres, chunks)
        );
    }

    @Test
    void createShouldThrowWhenGenresIsNull() {
        // Arrange
        service = new BibliographicalDataService(repo, factory);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndAdd(title, authors, language, date, null, chunks)
        );
    }

    @Test
    void createAndAddShouldAllowOptionalFieldsToBeNull() throws InstanceAlreadyExistsException {
        // Arrange
        service = new BibliographicalDataService(repo, factory);

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
        service = new BibliographicalDataService(repo, factory);

        when(factory.create(title, authors, language, date, genres, chunks)).thenReturn(createdBd);

        when(repo.save(createdBd)).thenThrow(new InstanceAlreadyExistsException("Bibliographical Data already exists"));

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                service.createAndAdd(title, authors, language, date, genres, chunks)
        );
    }

}