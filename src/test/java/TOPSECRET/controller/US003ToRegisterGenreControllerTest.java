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

    // TODO: re-enable when US003 is refactored to use GenreService.createAndSave()
//    @Test
//    void shouldRegisterGenreSuccessfully() {
//        IGenreRepository repoDouble = mock(IGenreRepository.class);
//        US003ToRegisterGenreController controller =
//                new US003ToRegisterGenreController(repoDouble);
//        boolean result = controller.registerGenre("Drama");
//        assertTrue(result);
//    }

//    @Test
//    void shouldPropagateExceptionWhenGenreAlreadyExists() {
//        IGenreRepository repoDouble = mock(IGenreRepository.class);
//        when(repoDouble.createAndAdd("Drama"))
//                .thenThrow(new IllegalArgumentException("Genre already exists"));
//        US003ToRegisterGenreController controller =
//                new US003ToRegisterGenreController(repoDouble);
//        assertThrows(IllegalArgumentException.class,
//                () -> controller.registerGenre("Drama"));
//    }
}
