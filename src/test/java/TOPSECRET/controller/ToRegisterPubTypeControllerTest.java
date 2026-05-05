package TOPSECRET.controller;

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
        controller = new ToRegisterPubTypeController(service);
    }

    @Test
    void registerPublicationTypeControllerDelegatesToRepository(){
        // Arrange
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
        when(service.createAndSave(null)).thenThrow(new IllegalArgumentException());

        // Assert
        assertThrows(IllegalArgumentException.class, () -> controller.registerPubType(null));
    }

    @Test
    void registerDuplicatePublicationTypeShouldCreateThenThrowWhenAdding()
    {
        // Arrange
        String mediaFormat = "Book";

        PublicationType expected = mock(PublicationType.class);

        when(service.createAndSave(mediaFormat)).thenReturn(expected);
        doThrow(new IllegalArgumentException()).when(service).createAndSave(mediaFormat);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> controller.registerPubType(mediaFormat));
    }
}