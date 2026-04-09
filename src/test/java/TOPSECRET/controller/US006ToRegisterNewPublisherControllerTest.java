package TOPSECRET.controller;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US006ToRegisterNewPublisherControllerTest {

    private IPublisherRepository repo;

    @BeforeEach
    void setup() {
        repo = mock(IPublisherRepository.class);
    }

    @Test
    void shouldBuildControllerSuccessfully() {
        // Arrange
        // (no additional setup needed)

        // Act
        US006ToRegisterNewPublisherController ctrl =
                new US006ToRegisterNewPublisherController(repo);

        // Assert
        assertNotNull(ctrl);
    }

    @Test
    void shouldThrowExceptionWhenRepositoryIsNull() {
        // Arrange
        // (no additional setup needed)

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new US006ToRegisterNewPublisherController(null));
    }


    @Test
    void shouldReturnTrueWhenPublisherIsRegistered() throws InstanceAlreadyExistsException {
        // Arrange
        String name = "Publisher1";
        String vatNumber = "123456789";
        Contact contactDouble = mock(Contact.class);
        Publisher publisherDouble = mock(Publisher.class);
        when(repo.createAndAdd(name, vatNumber, contactDouble)).thenReturn(publisherDouble);
        US006ToRegisterNewPublisherController ctrl =
                new US006ToRegisterNewPublisherController(repo);

        // Act
        boolean result = ctrl.registerPublisher(name, vatNumber, contactDouble);

        // Assert
        assertTrue(result);
        verify(repo).createAndAdd(name, vatNumber, contactDouble);
    }
}