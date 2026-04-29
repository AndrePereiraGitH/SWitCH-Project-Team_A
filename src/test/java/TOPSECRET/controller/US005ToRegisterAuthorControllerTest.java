package TOPSECRET.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link US005ToRegisterAuthorController}.
 *
 * <p>Verifies constructor validation and delegation to {@link IAuthorRepository},
 * using doubles to isolate the controller from the persistence layer.
 */

class US005ToRegisterAuthorControllerTest {

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    @Test
    void givenNullRepository_whenConstructing_thenThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new US005ToRegisterAuthorController(null)
        );
        assertEquals("AuthorRepository cannot be null", ex.getMessage());
    }

    @Test
    void givenValidRepository_whenConstructing_thenCreatesSuccessfully() {
        IAuthorRepository repositoryDouble = mock(IAuthorRepository.class);
        assertDoesNotThrow(() -> new US005ToRegisterAuthorController(repositoryDouble));
    }

    // -------------------------------------------------------------------------
    // register()
    // -------------------------------------------------------------------------

//    @Test
//    void givenValidArguments_whenRegister_thenReturnsAuthorId() {
//        IAuthorRepository repositoryDouble = mock(IAuthorRepository.class);
//        Author authorDouble = mock(Author.class);
//        CountryID countryDouble = mock(CountryID.class);
//
//        when(repositoryDouble.createAndAddAuthor("John", countryDouble, LocalDate.of(1984, 12, 3), "bio"))
//                .thenReturn(authorDouble);
//
//        US005ToRegisterAuthorController controller = new US005ToRegisterAuthorController(repositoryDouble);
//        Author result = controller.register("John", countryDouble, LocalDate.of(1984, 12, 3), "bio");
//
//        assertSame(authorDouble, result);
//    }
//
//    @Test
//    void givenAuthorAlreadyExists_whenRegister_thenThrowsIllegalArgumentException() {
//        IAuthorRepository repositoryDouble = mock(IAuthorRepository.class);
//        CountryID countryDouble = mock(CountryID.class);
//
//        when(repositoryDouble.createAndAddAuthor("John", countryDouble, LocalDate.of(1984, 12, 3), "bio"))
//                .thenThrow(new IllegalArgumentException("Author already exists"));
//
//        US005ToRegisterAuthorController controller = new US005ToRegisterAuthorController(repositoryDouble);
//
//        IllegalArgumentException ex = assertThrows(
//                IllegalArgumentException.class,
//                () -> controller.register("John", countryDouble, LocalDate.of(1984, 12, 3), "bio")
//        );
//        assertEquals("Author already exists", ex.getMessage());
//    }
}
