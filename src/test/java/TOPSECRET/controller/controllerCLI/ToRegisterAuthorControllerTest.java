package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.AuthorService;
import TOPSECRET.domain.Author.Author;

import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ToRegisterAuthorController}.
 * The service is replaced by a mock so only the controller logic is tested.
 */
class ToRegisterAuthorControllerTest {

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void shouldThrowExceptionWhenServiceIsNull() {

        // Act
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> new ToRegisterAuthorController(null));

        // Assert
        assertNotNull(ex);
    }

    // =========================================================================
    // register — success
    // =========================================================================

    @Test
    void shouldRegisterAuthorSuccessfully()
            throws AccessDeniedException, InstanceAlreadyExistsException {

        // Arrange
        AuthorService serviceDouble = mock(AuthorService.class);
        ToRegisterAuthorController controller = new ToRegisterAuthorController(serviceDouble);

        String name = "José Saramago";
        CountryId countryId = new CountryId("PORTUGAL");
        LocalDate dateOfBirth = LocalDate.of(1922, 11, 16);
        String biography = "Portuguese Nobel Prize-winning author.";
        UserId userId = new UserId("user-1");

        Author authorDouble = mock(Author.class);
        when(serviceDouble.createAndSave(name, countryId, dateOfBirth, biography, userId))
                .thenReturn(authorDouble);

        // Act
        Author result = controller.register(name, countryId, dateOfBirth, biography, userId);

        // Assert
        assertSame(authorDouble, result);
        verify(serviceDouble).createAndSave(name, countryId, dateOfBirth, biography, userId);
    }

    // =========================================================================
    // register — exceptions
    // =========================================================================

    @Test
    void shouldPropagateExceptionWhenAccessDenied()
            throws AccessDeniedException, InstanceAlreadyExistsException {

        // Arrange
        AuthorService serviceDouble = mock(AuthorService.class);
        ToRegisterAuthorController controller = new ToRegisterAuthorController(serviceDouble);

        String name = "José Saramago";
        CountryId countryId = new CountryId("PORTUGAL");
        LocalDate dateOfBirth = LocalDate.of(1922, 11, 16);
        String biography = "Portuguese Nobel Prize-winning author.";
        UserId userId = new UserId("user-1");

        doThrow(new AccessDeniedException("Access denied"))
                .when(serviceDouble).createAndSave(name, countryId, dateOfBirth, biography, userId);

        // Act
        Exception ex = assertThrows(AccessDeniedException.class,
                () -> controller.register(name, countryId, dateOfBirth, biography, userId));

        // Assert
        assertNotNull(ex);
    }

    @Test
    void shouldPropagateExceptionWhenInstanceAlreadyExists()
            throws AccessDeniedException, InstanceAlreadyExistsException {

        // Arrange
        AuthorService serviceDouble = mock(AuthorService.class);
        ToRegisterAuthorController controller = new ToRegisterAuthorController(serviceDouble);

        String name = "José Saramago";
        CountryId countryId = new CountryId("PORTUGAL");
        LocalDate dateOfBirth = LocalDate.of(1922, 11, 16);
        String biography = "Portuguese Nobel Prize-winning author.";
        UserId userId = new UserId("user-1");

        doThrow(new InstanceAlreadyExistsException("Author already exists"))
                .when(serviceDouble).createAndSave(name, countryId, dateOfBirth, biography, userId);

        // Act
        Exception ex = assertThrows(InstanceAlreadyExistsException.class,
                () -> controller.register(name, countryId, dateOfBirth, biography, userId));

        // Assert
        assertNotNull(ex);
    }
}