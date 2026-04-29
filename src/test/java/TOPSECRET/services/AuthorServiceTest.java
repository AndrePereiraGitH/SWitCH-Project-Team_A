package TOPSECRET.services;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorServiceTest {

    private AuthorFactory authorFactory;
    private IAuthorRepository authorRepo;
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorFactory = mock(AuthorFactory.class);
        authorRepo = mock(IAuthorRepository.class);

        authorService = new AuthorService(authorFactory, authorRepo);
    }

    @Test
    void createAndSaveShouldReturnSavedAuthor() throws InstanceAlreadyExistsException {
        // Arrange
        String name = "Fernando Pessoa";
        CountryID countryID = mock(CountryID.class);
        LocalDate dateOfBirth = LocalDate.of(1888, 6, 13);
        String biography = "Portuguese poet and writer.";

        Author createdAuthor = mock(Author.class);
        Author savedAuthor = mock(Author.class);

        when(authorFactory.create(name, countryID, dateOfBirth, biography))
                .thenReturn(createdAuthor);

        when(authorRepo.save(createdAuthor))
                .thenReturn(savedAuthor);

        // Act
        Author result = authorService.createAndSave(name, countryID, dateOfBirth, biography);

        // Assert
        assertSame(savedAuthor, result);
    }

    @Test
    void createAndSaveShouldThrowExceptionWhenRepositoryThrowsInstanceAlreadyExistsException()
            throws InstanceAlreadyExistsException {

        // Arrange
        String name = "Fernando Pessoa";
        CountryID countryID = mock(CountryID.class);
        LocalDate dateOfBirth = LocalDate.of(1888, 6, 13);
        String biography = "Portuguese poet and writer.";

        Author createdAuthor = mock(Author.class);

        when(authorFactory.create(name, countryID, dateOfBirth, biography))
                .thenReturn(createdAuthor);

        when(authorRepo.save(createdAuthor))
                .thenThrow(new InstanceAlreadyExistsException("AuthorId already exists"));

        // Act + Assert
        InstanceAlreadyExistsException exception = assertThrows(
                InstanceAlreadyExistsException.class,
                () -> authorService.createAndSave(name, countryID, dateOfBirth, biography)
        );

        assertEquals("AuthorId already exists", exception.getMessage());
    }

    @Test
    void createAndSaveShouldPropagateExceptionWhenFactoryThrowsException()
            throws InstanceAlreadyExistsException {

        // Arrange
        String name = null;
        CountryID countryID = mock(CountryID.class);
        LocalDate dateOfBirth = LocalDate.of(1888, 6, 13);
        String biography = "Portuguese poet and writer.";

        when(authorFactory.create(name, countryID, dateOfBirth, biography))
                .thenThrow(new IllegalArgumentException("name cannot be null"));

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authorService.createAndSave(name, countryID, dateOfBirth, biography)
        );

        assertEquals("name cannot be null", exception.getMessage());
    }

}