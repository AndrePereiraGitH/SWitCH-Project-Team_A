package TOPSECRET.controller;

import TOPSECRET.services.GenreService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ToRegisterGenreController}.
 * The repository is replaced by a mock so only the controller logic is tested.
 */
class ToRegisterGenreControllerTest {

    @Test
    void shouldThrowExceptionWhenServiceIsNull() {

        assertThrows(IllegalArgumentException.class,
                () -> new ToRegisterGenreController(null));
    }

    @Test
    void shouldReturnTrueWhenGenreIsRegisteredSuccessfully() {

        // Arrange
        GenreService serviceDouble = mock(GenreService.class);
        ToRegisterGenreController controller = new ToRegisterGenreController(serviceDouble);

        // Act
        boolean result = controller.registerGenre("Drama");

        // Assert
        assertTrue(result);
        verify(serviceDouble).createAndSave("Drama");
    }

    @Test
    void shouldPropagateExceptionWhenGenreAlreadyExists() {

        // Arrange
        GenreService serviceDouble = mock(GenreService.class);
        doThrow(new IllegalArgumentException("Genre already exists"))
                .when(serviceDouble).createAndSave("Drama");

        ToRegisterGenreController controller = new ToRegisterGenreController(serviceDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerGenre("Drama"));
    }
}