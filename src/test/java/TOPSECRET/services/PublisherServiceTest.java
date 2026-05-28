package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublisherAuthorizationPolicy;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherService}.
 *
 * <p>Verifies that {@code createAndSave} correctly delegates to the factory
 * and repository, and rejects null arguments with a {@link NullPointerException}.</p>
 */
@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

    @Mock
    private PublisherFactory factory;

    @Mock
    private IPublisherRepository repository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PublisherAuthorizationPolicy authPolicy;

    @InjectMocks
    private PublisherService service;

    // ========== Tests for createAndSave =========================

    @Test
    void createAndSave_shouldReturnSavedPublisher_whenInputAndUserAreValid() throws AccessDeniedException {
        // Arrange
        String name = "Penguin Books";
        String vat = "123456789";
        Contact contactDouble = mock(Contact.class);
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);
        Publisher publisherDouble = mock(Publisher.class);
        Publisher savedPublisherDouble = mock(Publisher.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(authPolicy.canCreatPublisher(userDouble)).thenReturn(true);
        when(factory.create(name, vat, contactDouble)).thenReturn(publisherDouble);
        when(repository.save(publisherDouble)).thenReturn(savedPublisherDouble);

        // Act
        Publisher result = service.createAndSave(name, vat, contactDouble, userIdDouble);

        // Assert
        assertSame(savedPublisherDouble, result);
    }

    @Test
    void createAndSave_shouldThrowIllegalArgumentException_whenUserNotFound() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        Contact contactDouble = mock(Contact.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.createAndSave("Name", "123", contactDouble, userIdDouble));
    }

    @Test
    void createAndSave_shouldThrowAccessDeniedException_whenPolicyFails() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);
        Contact contactDouble = mock(Contact.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(authPolicy.canCreatPublisher(userDouble)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> service.createAndSave("Name", "123", contactDouble, userIdDouble));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenNameIsNull() {
        assertThrows(NullPointerException.class,
                () -> service.createAndSave(null, "123", mock(Contact.class), mock(UserId.class)));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenVatNumberIsNull() {
        assertThrows(NullPointerException.class,
                () -> service.createAndSave("Name", null, mock(Contact.class), mock(UserId.class)));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenContactIsNull() {
        assertThrows(NullPointerException.class,
                () -> service.createAndSave("Name", "123", null, mock(UserId.class)));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> service.createAndSave("Name", "123", mock(Contact.class), null));
    }

    @Test
    void findAll_shouldReturnAllPublishers() throws AccessDeniedException {

        // Arrange
        Publisher publisher1 = mock(Publisher.class);
        Publisher publisher2 = mock(Publisher.class);

        when(repository.findAll()).thenReturn(List.of(publisher1, publisher2));

        // Act
        var result = service.findAll();

        // Assert
        assertEquals(2, result.size());
    }
}