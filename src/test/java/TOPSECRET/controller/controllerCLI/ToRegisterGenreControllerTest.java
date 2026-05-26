package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.GenreService;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;

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
    void shouldReturnTrueWhenGenreIsRegisteredSuccessfully() throws AccessDeniedException, InstanceAlreadyExistsException {

        // Arrange
        GenreService serviceDouble = mock(GenreService.class);
        ToRegisterGenreController controller = new ToRegisterGenreController(serviceDouble);
        UserId id = new UserId("1");

        // Act
        boolean result = controller.registerGenre("Drama", id);

        // Assert
        assertTrue(result);
        verify(serviceDouble).createAndSave("Drama", id);
    }

    @Test
    void shouldPropagateExceptionWhenGenreAlreadyExists() throws AccessDeniedException, InstanceAlreadyExistsException {

        // Arrange
        GenreService serviceDouble = mock(GenreService.class);
        UserId id = new UserId("1");

        doThrow(new IllegalArgumentException("Genre already exists"))
                .when(serviceDouble).createAndSave("Drama", id);

        ToRegisterGenreController controller = new ToRegisterGenreController(serviceDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerGenre("Drama", id));
    }
}