package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToRegisterAuthorController}.
 *
 * <p>Verifies constructor validation and delegation to {@link AuthorService},
 * using doubles to isolate the controller from the service layer.</p>
 */
@ExtendWith(MockitoExtension.class)
class ToRegisterAuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private ToRegisterAuthorController controller;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    @Test
    void givenNullService_whenConstructing_thenThrowsIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToRegisterAuthorController(null)
        );
        assertEquals("AuthorService cannot be null", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // register()
    // -------------------------------------------------------------------------

    @Test
    void givenValidArguments_whenRegister_thenReturnsAuthor() throws InstanceAlreadyExistsException {
        // Arrange
        String name = "John";
        CountryId countryDouble = mock(CountryId.class);
        LocalDate dateOfBirth = LocalDate.of(1984, 12, 3);
        String biography = "A great author";
        Author authorDouble = mock(Author.class);

        when(authorService.createAndSave(name, countryDouble, dateOfBirth, biography))
                .thenReturn(authorDouble);

        // Act
        Author result = controller.register(name, countryDouble, dateOfBirth, biography);

        // Assert
        assertNotNull(result);
        assertSame(authorDouble, result);
        verify(authorService).createAndSave(name, countryDouble, dateOfBirth, biography);
    }

    @Test
    void givenAuthorAlreadyExists_whenRegister_thenPropagatesException() throws InstanceAlreadyExistsException {
        // Arrange
        String name = "John";
        CountryId countryDouble = mock(CountryId.class);
        LocalDate dateOfBirth = LocalDate.of(1984, 12, 3);
        String biography = "A great author";

        doThrow(new InstanceAlreadyExistsException("Author already exists"))
                .when(authorService).createAndSave(name, countryDouble, dateOfBirth, biography);

        // Act & Assert
        assertThrows(InstanceAlreadyExistsException.class,
                () -> controller.register(name, countryDouble, dateOfBirth, biography));
    }
}