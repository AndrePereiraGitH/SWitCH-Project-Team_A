package TOPSECRET.controller;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.services.PublisherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void shouldBuildControllerSuccessfully() {
        // Arrange & Act
        ToRegisterNewPublisherController ctrl =
                new ToRegisterNewPublisherController(publisherService);

        // Assert
        assertNotNull(ctrl);
    }

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
    void shouldReturnTrueWhenPublisherIsRegistered() {
        // Arrange
        String name = "Publisher1";
        String vatNumber = "123456789";
        Contact contactDouble = mock(Contact.class);

        // Act
        boolean result = controller.registerPublisher(name, vatNumber, contactDouble);

        // Assert
        assertTrue(result);
        verify(publisherService).createAndSave(name, vatNumber, contactDouble);
    }

    @Test
    void shouldPropagateExceptionWhenPublisherAlreadyExists() {
        // Arrange
        String name = "Publisher1";
        String vatNumber = "123456789";
        Contact contactDouble = mock(Contact.class);

        doThrow(new IllegalArgumentException("Publisher already exists"))
                .when(publisherService).createAndSave(name, vatNumber, contactDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerPublisher(name, vatNumber, contactDouble));
    }
}