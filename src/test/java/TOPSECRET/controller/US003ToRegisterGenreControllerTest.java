package TOPSECRET.controller;

import TOPSECRET.domain.Genre.IGenreRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link US003ToRegisterGenreController}.
 * The repository is replaced by a mock so only the controller logic is tested.
 */
class US003ToRegisterGenreControllerTest {

    // Null repository must be rejected immediately in the constructor
    @Test
    void shouldThrowExceptionWhenRepositoryIsNull() {

        assertThrows(IllegalArgumentException.class,
                () -> new US003ToRegisterGenreController(null));
    }

    // Controller must return true when the repository successfully creates the genre
    @Test
    void shouldRegisterGenreSuccessfully() {

        // Arrange
        IGenreRepository repoDouble = mock(IGenreRepository.class);

        US003ToRegisterGenreController controller =
                new US003ToRegisterGenreController(repoDouble);

        // Act
        boolean result = controller.registerGenre("Drama");

        // Assert
        assertTrue(result);
    }

    // If the repository throws, the controller must not catch it — let it propagate
    @Test
    void shouldPropagateExceptionWhenGenreAlreadyExists() {

        // Arrange
        IGenreRepository repoDouble = mock(IGenreRepository.class);

        // Program the mock to simulate a duplicate genre error
        when(repoDouble.createAndAdd("Drama"))
                .thenThrow(new IllegalArgumentException("Genre already exists"));

        US003ToRegisterGenreController controller =
                new US003ToRegisterGenreController(repoDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerGenre("Drama"));
    }
}