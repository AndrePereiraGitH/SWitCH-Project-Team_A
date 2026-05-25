package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.services.PublicationTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;


class ToRegisterPubTypeControllerTest {


    private ToRegisterPubTypeController controller;
    private PublicationTypeService service;

    @BeforeEach
    void setup(){
        service = mock(PublicationTypeService.class);
    }

    @Test
    void registerPublicationTypeControllerDelegatesToService(){
        // Arrange
        controller = new ToRegisterPubTypeController(service);

        String mediaFormat = "Book";

        PublicationType expected = mock(PublicationType.class);

        when(service.createAndSave(mediaFormat)).thenReturn(expected);

        // Act
        PublicationType result = controller.registerPubType(mediaFormat);

        // Assert
        assertEquals(expected, result);
    }


    // Invalid input tests

    @Test
    void shouldThrowExceptionWhenMediaFormatIsInvalid(){
        // Arrange
        controller = new ToRegisterPubTypeController(service);

        when(service.createAndSave(null)).thenThrow(new IllegalArgumentException());

        // Assert
        assertThrows(IllegalArgumentException.class, () -> controller.registerPubType(null));
    }

    @Test
    void registerDuplicatePublicationTypeShouldCreateThenThrowWhenAdding() {
        // Arrange
        controller = new ToRegisterPubTypeController(service);

        String mediaFormat = "Book";

        doThrow(new IllegalArgumentException()).when(service).createAndSave(mediaFormat);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> controller.registerPubType(mediaFormat));
    }

    @Test
    void shouldThrowExceptionWhenServiceIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToRegisterPubTypeController(null)
        );

        assertEquals("Service must not be null", exception.getMessage());
    }
}