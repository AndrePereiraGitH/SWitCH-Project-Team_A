package TOPSECRET.controller;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.repository.GenreRepositoryMem;
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

    // Controller must return the genre that the repository gives back
    @Test
    void shouldRegisterGenreSuccessfully() {

        // Arrange
        Genre genreDouble = mock(Genre.class);
        GenreRepositoryMem repoDouble = mock(GenreRepositoryMem.class);

        // Program the mock: when createAndAdd is called, return the fake genre
        when(repoDouble.createAndAdd("Drama")).thenReturn(genreDouble);

        US003ToRegisterGenreController controller =
                new US003ToRegisterGenreController(repoDouble);

        // Act
        Genre result = controller.registerGenre("Drama");

        // Assert
        assertEquals(genreDouble, result);
    }

    // If the repository throws, the controller must not catch it — let it propagate
    @Test
    void shouldPropagateExceptionWhenGenreAlreadyExists() {

        // Arrange
        GenreRepositoryMem repoDouble = mock(GenreRepositoryMem.class);

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