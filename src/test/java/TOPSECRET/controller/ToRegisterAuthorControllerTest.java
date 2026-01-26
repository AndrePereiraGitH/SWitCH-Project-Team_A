package TOPSECRET.controller;


import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.Profile;
import TOPSECRET.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ToRegisterAuthorControllerTest {

    private final AuthorRepository authorRepository = new AuthorRepository(new ArrayList<>());
    // ========================================
    // CONSTRUCTOR TESTS
    // ========================================

    @Test
    void shouldCreateControllerWithValidRepository() {
        // Act
        ToRegisterAuthorController controller = new ToRegisterAuthorController(authorRepository);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void shouldThrowExceptionWhenRepositoryIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new ToRegisterAuthorController(null)
        );
        assertEquals("AuthorRepository cannot be null", exception.getMessage());
    }

    // ========================================
    // REGISTER AUTHOR TESTS
    // ========================================

    @Test
    void shouldRegisterAuthor() {
        // Arrange
        ToRegisterAuthorController controller = new ToRegisterAuthorController(authorRepository);

        Profile expectedProfile = new Profile("Maria", new Country("Portugal"), LocalDate.of(1978, 5, 12));
        Author expected = new Author(expectedProfile, "valid bio");

        // Act
        Author result = controller.registerAuthor("Maria", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        assertEquals(1, authorRepository.getAllAuthors().size());
    }

    @Test
    void shouldThrowExceptionWhenRegisteringDuplicateAuthor() {
        // Arrange
        ToRegisterAuthorController controller = new ToRegisterAuthorController(authorRepository);

        controller.registerAuthor("Duplicate", new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio");

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> controller.registerAuthor("Duplicate", new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio")
        );
        assertEquals("Author already exists", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidData() {
        // Arrange
        ToRegisterAuthorController controller = new ToRegisterAuthorController(authorRepository);

        // Act + Assert - null name
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAuthor(null, new Country("USA"), LocalDate.of(1980, 1, 1), "bio")
        );
        // Act + Assert - blank name
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAuthor("  ", new Country("USA"), LocalDate.of(1980, 1, 1), "bio")
        );
        // Act + Assert - null biography
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAuthor("Name", new Country("USA"), LocalDate.of(1980, 1, 1), null)
        );
    }

    @Test
    void shouldRegisterMultipleDifferentAuthors() {
        // Arrange
        AuthorRepository repository = new AuthorRepository(new ArrayList<>());
        ToRegisterAuthorController controller = new ToRegisterAuthorController(repository);

        // Act
        Author author1 = controller.registerAuthor("Author1", new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio1");
        Author author2 = controller.registerAuthor("Author2", new Country("BAHAMAS"), LocalDate.of(1985, 2, 2), "bio2");
        Author author3 = controller.registerAuthor("Author3", new Country("BELARUS"), LocalDate.of(1990, 3, 3), "bio3");

        // Assert
        assertNotNull(author1);
        assertNotNull(author2);
        assertNotNull(author3);
        assertEquals(3, repository.getAllAuthors().size());
    }


}