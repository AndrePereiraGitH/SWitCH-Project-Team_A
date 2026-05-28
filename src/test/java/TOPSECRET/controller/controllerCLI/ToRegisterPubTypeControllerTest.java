package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.PublicationTypeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToRegisterPubTypeControllerTest {

    private ToRegisterPubTypeController controller;
    private PublicationTypeService service;

    @BeforeEach
    void setup() {
        service = mock(PublicationTypeService.class);
    }

    // =========================================================
    // SUCCESS
    // =========================================================

    @Test
    void registerPublicationTypeControllerDelegatesToService()
            throws Exception {

        // Arrange
        controller = new ToRegisterPubTypeController(service);

        String mediaFormat = "Book";

        UserId userId = mock(UserId.class);

        PublicationType expected = mock(PublicationType.class);
        when(service.createAndSave(mediaFormat, userId)).thenReturn(expected);

        // Act
        PublicationType result =
                controller.registerPubType(
                        mediaFormat,
                        userId
                );

        // Assert
        assertEquals(expected, result);
    }

    // =========================================================
    // INVALID INPUT
    // =========================================================

    @Test
    void shouldThrowExceptionWhenMediaFormatIsInvalid()
            throws Exception {

        // Arrange
        controller = new ToRegisterPubTypeController(service);

        UserId userId = mock(UserId.class);

        when(service.createAndSave(null, userId)).thenThrow(new IllegalArgumentException());

        // Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> controller.registerPubType(
                        null,
                        userId
                )
        );
    }

    // =========================================================
    // DUPLICATE
    // =========================================================

    @Test
    void registerDuplicatePublicationTypeShouldThrowException()
            throws Exception {

        // Arrange
        controller = new ToRegisterPubTypeController(service);

        String mediaFormat = "Book";

        UserId userId = mock(UserId.class);

        doThrow(new InstanceAlreadyExistsException()).when(service)
                .createAndSave(mediaFormat, userId);
        // Act + Assert
        assertThrows(
                InstanceAlreadyExistsException.class,
                () -> controller.registerPubType(
                        mediaFormat,
                        userId
                )
        );
    }

    // =========================================================
    // ACCESS DENIED
    // =========================================================

    @Test
    void shouldThrowAccessDeniedException()
            throws Exception {

        // Arrange
        controller = new ToRegisterPubTypeController(service);

        String mediaFormat = "Book";

        UserId userId = mock(UserId.class);

        doThrow(new AccessDeniedException("Denied"))
                .when(service)
                .createAndSave(mediaFormat, userId);

        // Act + Assert
        assertThrows(
                AccessDeniedException.class,
                () -> controller.registerPubType(
                        mediaFormat,
                        userId
                )
        );
    }

    // =========================================================
    // NULL SERVICE
    // =========================================================

    @Test
    void shouldThrowExceptionWhenServiceIsNull() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new ToRegisterPubTypeController(null)
                );

        assertEquals("Service must not be null", exception.getMessage()
        );
    }
}