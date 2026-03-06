package TOPSECRET.controller;


import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorFactory;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.Profile;
import TOPSECRET.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Integration tests for {@link ToRegisterAuthorController}.
 *
 * <p>This test class uses <b>real collaborators</b> ({@link AuthorRepository} and {@link AuthorFactory})
 * rather than mocks, making these integration tests — they verify that
 * {@link ToRegisterAuthorController}, {@link AuthorRepository}, {@link AuthorFactory},
 * and {@link Author} work correctly together as a whole.
 *
 * <p><b>What is tested here:</b>
 * <ul>
 *   <li>Constructor — null repository</li>
 *   <li>{@link ToRegisterAuthorController#register} — happy path, duplicate prevention,
 *       invalid input propagation, multiple authors</li>
 * </ul>
 */
class ToRegisterAuthorControllerDependenciesTest {

    private final AuthorRepository authorRepository = new AuthorRepository(new ArrayList<>(), new AuthorFactory());
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
    void shouldRegister() {
        // Arrange
        ToRegisterAuthorController controller = new ToRegisterAuthorController(authorRepository);

        Profile expectedProfile = new Profile("Maria", new Country("Portugal"), LocalDate.of(1978, 5, 12));
        AuthorFactory factory = new AuthorFactory();
        Author expected = factory.createWithExistingProfile(expectedProfile, "bio");

        // Act
        Author result = controller.register("Maria", new Country("Portugal"), LocalDate.of(1978, 5, 12), "bio");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        assertEquals(1, authorRepository.getAllAuthors().size());
    }

    @Test
    void shouldThrowExceptionWhenRegisteringDuplicateAuthor() {
        // Arrange
        ToRegisterAuthorController controller = new ToRegisterAuthorController(authorRepository);

        controller.register("Duplicate", new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio");

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> controller.register("Duplicate", new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio")
        );
        assertEquals("Author already exists", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidData() {
        // Arrange
        ToRegisterAuthorController controller = new ToRegisterAuthorController(authorRepository);

        // Act + Assert - null name
        assertThrows(IllegalArgumentException.class,
                () -> controller.register(null, new Country("USA"), LocalDate.of(1980, 1, 1), "bio")
        );
        // Act + Assert - blank name
        assertThrows(IllegalArgumentException.class,
                () -> controller.register("  ", new Country("USA"), LocalDate.of(1980, 1, 1), "bio")
        );
        // Act + Assert - null biography
        assertThrows(IllegalArgumentException.class,
                () -> controller.register("Name", new Country("USA"), LocalDate.of(1980, 1, 1), null)
        );
    }

    @Test
    void shouldRegisterMultipleDifferentAuthors() {
        // Arrange
        AuthorRepository repository = new AuthorRepository(new ArrayList<>(), new AuthorFactory());
        ToRegisterAuthorController controller = new ToRegisterAuthorController(repository);

        // Act
        Author author1 = controller.register("Author1", new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio1");
        Author author2 = controller.register("Author2", new Country("BAHAMAS"), LocalDate.of(1985, 2, 2), "bio2");
        Author author3 = controller.register("Author3", new Country("BELARUS"), LocalDate.of(1990, 3, 3), "bio3");

        // Assert
        assertNotNull(author1);
        assertNotNull(author2);
        assertNotNull(author3);
        assertEquals(3, repository.getAllAuthors().size());
    }


}