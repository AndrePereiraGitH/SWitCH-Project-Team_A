package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.PublisherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;


import javax.management.InstanceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ToRegisterNewPublisherController.
 * * <p>Validates constructor integrity, successful delegation to the service layer,
 * and proper propagation of business and authorization exceptions.</p>
 */
@ExtendWith(MockitoExtension.class)
class ToRegisterNewPublisherControllerTest {

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private ToRegisterNewPublisherController controller;

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void shouldThrowExceptionWhenServiceIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ToRegisterNewPublisherController(null));
    }

    // =========================================================================
    // registerPublisher
    // =========================================================================

    @Test
    void shouldRegisterPublisherSuccessfully() throws AccessDeniedException {
        // Arrange
        String name = "Publisher1";
        String vatNumber = "123456789";
        Contact contactDouble = mock(Contact.class);
        UserId userIdDouble = mock(UserId.class);

        // Act & Assert
        assertDoesNotThrow(() -> controller.registerPublisher(name, vatNumber, contactDouble, userIdDouble));
    }

    @Test
    void shouldPropagateExceptionWhenPublisherAlreadyExists() throws AccessDeniedException, InstanceNotFoundException {
        // Arrange
        String name = "Publisher1";
        String vatNumber = "123456789";
        Contact contactDouble = mock(Contact.class);
        UserId userIdDouble = mock(UserId.class);

        doThrow(new IllegalArgumentException("Publisher already exists"))
                .when(publisherService).createAndSave(name, vatNumber, contactDouble, userIdDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerPublisher(name, vatNumber, contactDouble, userIdDouble));
    }

    @Test
    void shouldPropagateAccessDeniedExceptionWhenUserIsNotAuthorized() throws AccessDeniedException, InstanceNotFoundException {
        // Arrange
        String name = "Publisher1";
        String vatNumber = "123456789";
        Contact contactDouble = mock(Contact.class);
        UserId userIdDouble = mock(UserId.class);
        doThrow(new AccessDeniedException("User not authorized"))
                .when(publisherService).createAndSave(name, vatNumber, contactDouble, userIdDouble);

        // Act & Assert
        assertThrows(AccessDeniedException.class,
                () -> controller.registerPublisher(name, vatNumber, contactDouble, userIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        // Arrange
        Contact contactDouble = mock(Contact.class);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> controller.registerPublisher("Name", "123", contactDouble, null));
    }
}