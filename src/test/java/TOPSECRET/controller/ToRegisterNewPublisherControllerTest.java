package TOPSECRET.controller;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToRegisterNewPublisherControllerTest {

    private IPublisherRepository repo;

    @BeforeEach
    void setup() {
        repo = mock(IPublisherRepository.class);
    }

    @Test
    void shouldBuildControllerSuccessfully() {
        // Arrange


        // Act
        ToRegisterNewPublisherController ctrl =
                new ToRegisterNewPublisherController(repo);

        // Assert
        assertNotNull(ctrl);
    }

    @Test
    void shouldThrowExceptionWhenRepositoryIsNull() {
        // Arrange

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new ToRegisterNewPublisherController(null));
    }


    @Test
    void shouldReturnTrueWhenPublisherIsRegistered() throws InstanceAlreadyExistsException {
        // Arrange
        String name = "Publisher1";
        String vatNumber = "123456789";
        Contact contactDouble = mock(Contact.class);
        Publisher publisherDouble = mock(Publisher.class);
        when(repo.createAndAdd(name, vatNumber, contactDouble)).thenReturn(publisherDouble);
        ToRegisterNewPublisherController ctrl =
                new ToRegisterNewPublisherController(repo);

        // Act
        boolean result = ctrl.registerPublisher(name, vatNumber, contactDouble);

        // Assert
        assertTrue(result);
    }
}