package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublisherAuthorizationPolicy;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.domain.ValueObjects.UserId;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    // =========================================================
    // createAndSave - SUCCESS
    // =========================================================
    @Test
    void givenValidData_whenCreateAndSave_thenReturnsPublisher() throws InstanceNotFoundException {

        // ARRANGE
        String name = "Pub";
        String vat = "123";
        Contact contact = mock(Contact.class);
        UserId userId = mock(UserId.class);

        User user = mock(User.class);
        Publisher publisher = mock(Publisher.class);

        when(userRepository.findByID(userId))
                .thenReturn(Optional.of(user));

        when(authPolicy.canCreatePublisher(user))
                .thenReturn(true);

        when(factory.create(name, vat, contact))
                .thenReturn(publisher);

        when(repository.save(publisher))
                .thenReturn(publisher);

        // ACT
        Publisher result = service.createAndSave(name, vat, contact, userId);

        // ASSERT
        assertEquals(publisher, result);
    }

    // =========================================================
    // createAndSave - USER NOT FOUND
    // =========================================================
    @Test
    void givenUserNotFound_whenCreateAndSave_thenThrowsInstanceNotFoundException() {

        // ARRANGE
        String name = "Pub";
        String vat = "123";
        Contact contact = mock(Contact.class);
        UserId userId = mock(UserId.class);

        when(userRepository.findByID(userId))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                InstanceNotFoundException.class,
                () -> service.createAndSave(name, vat, contact, userId)
        );
    }

    // =========================================================
    // createAndSave - ACCESS DENIED
    // =========================================================
    @Test
    void givenUnauthorizedUser_whenCreateAndSave_thenThrowsAccessDenied() {

        // ARRANGE
        String name = "Pub";
        String vat = "123";
        Contact contact = mock(Contact.class);
        UserId userId = mock(UserId.class);

        User user = mock(User.class);

        when(userRepository.findByID(userId))
                .thenReturn(Optional.of(user));

        when(authPolicy.canCreatePublisher(user))
                .thenReturn(false);

        // ACT + ASSERT
        assertThrows(
                AccessDeniedException.class,
                () -> service.createAndSave(name, vat, contact, userId)
        );
    }

    // =========================================================
    // findAll
    // =========================================================
    @Test
    void givenPublishersExist_whenFindAll_thenReturnsList() {

        // ARRANGE
        Publisher p1 = mock(Publisher.class);
        Publisher p2 = mock(Publisher.class);

        when(repository.findAll())
                .thenReturn(List.of(p1, p2));

        // ACT
        List<Publisher> result = service.findAll();

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void givenNoPublishers_whenFindAll_thenReturnsEmptyList() {

        // ARRANGE
        when(repository.findAll())
                .thenReturn(List.of());

        // ACT
        List<Publisher> result = service.findAll();

        // ASSERT
        assertTrue(result.isEmpty());
    }

    // =========================================================
    // findById
    // =========================================================
    @Test
    void givenExistingId_whenFindById_thenReturnsPublisher() {

        // ARRANGE
        PublisherID id = mock(PublisherID.class);
        Publisher publisher = mock(Publisher.class);

        when(repository.findById(id))
                .thenReturn(Optional.of(publisher));

        // ACT
        Publisher result = service.findById(id);

        // ASSERT
        assertEquals(publisher, result);
    }

    @Test
    void givenNonExistingId_whenFindById_thenThrowsException() {

        // ARRANGE
        PublisherID id = mock(PublisherID.class);

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                EntityNotFoundException.class,
                () -> service.findById(id)
        );
    }

    @Test
    void givenNullId_whenFindById_thenThrowsNullPointerException() {

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> service.findById(null)
        );
    }
}