package TOPSECRET.controller;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US006ToRegisterNewPublisherControllerTest {

    private IPublisherRepository repo;
    private PublisherFactory factory;
    private Contact contactDouble;
    private Publisher publisherDouble;

    private final String name = "Publisher1";
    private final String vatNumber = "123456789";

    @BeforeEach
    void setup() {
        repo = mock(IPublisherRepository.class);
        factory = mock(PublisherFactory.class);
        contactDouble = mock(Contact.class);
        publisherDouble = mock(Publisher.class);
    }

    @Test
    void shouldBuildControllerSuccessfully() {
        // Arrange
        // (no additional setup needed)

        // Act
        US006ToRegisterNewPublisherController ctrl =
                new US006ToRegisterNewPublisherController(repo, factory);

        // Assert
        assertNotNull(ctrl);
    }

    @Test
    void shouldThrowExceptionWhenRepositoryIsNull() {
        // Arrange
        // (no additional setup needed)

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new US006ToRegisterNewPublisherController(null, factory));
    }

    @Test
    void shouldThrowExceptionWhenFactoryIsNull() {
        // Arrange
        // (no additional setup needed)

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new US006ToRegisterNewPublisherController(repo, null));
    }

    @Test
    void shouldReturnTrueWhenPublisherIsRegistered() throws InstanceAlreadyExistsException {
        // Arrange
        when(factory.create(name, vatNumber, contactDouble)).thenReturn(publisherDouble);
        when(repo.exists(publisherDouble)).thenReturn(false);
        US006ToRegisterNewPublisherController ctrl =
                new US006ToRegisterNewPublisherController(repo, factory);

        // Act
        boolean result = ctrl.registerPublisher(name, vatNumber, contactDouble);

        // Assert
        assertTrue(result);
        verify(repo).save(publisherDouble);
    }

    @Test
    void shouldThrowExceptionWhenPublisherAlreadyExists() {
        // Arrange
        when(factory.create(name, vatNumber, contactDouble)).thenReturn(publisherDouble);
        when(repo.exists(publisherDouble)).thenReturn(true);
        US006ToRegisterNewPublisherController ctrl =
                new US006ToRegisterNewPublisherController(repo, factory);

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class,
                () -> ctrl.registerPublisher(name, vatNumber, contactDouble));
    }
}