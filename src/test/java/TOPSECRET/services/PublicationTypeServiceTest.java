package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationTypeAuthorizationPolicy;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.UserId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationTypeService}.
 */
@ExtendWith(MockitoExtension.class)
class PublicationTypeServiceTest {

    @Mock
    private IPublicationTypeRepository repository;

    @Mock
    private PublicationTypeFactory factory;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PublicationTypeAuthorizationPolicy policy;

    @InjectMocks
    private PublicationTypeService service;
// =========================================================
// CREATE SUCCESS
// =========================================================

    @Test
    void shouldCreatePublicationTypeSuccessfully()
            throws Exception {

        // ARRANGE
        String format = "Book";

        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        PublicationType entity = mock(PublicationType.class);

        when(userRepository.findByID(userId))
                .thenReturn(Optional.of(user));

        when(policy.canCreatePublicationType(user))
                .thenReturn(true);

        when(repository.findByMediaFormat(format))
                .thenReturn(Optional.empty());

        when(factory.create(format))
                .thenReturn(entity);

        when(repository.save(entity))
                .thenReturn(entity);

        // ACT
        PublicationType result =
                service.createAndSave(format, userId);

        // ASSERT
        assertEquals(entity, result);
    }

// =========================================================
// DUPLICATE
// =========================================================

    @Test
    void shouldThrowWhenPublicationTypeAlreadyExists()
            throws Exception {

        // ARRANGE
        String format = "Book";

        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        PublicationType existing =
                mock(PublicationType.class);

        when(userRepository.findByID(userId))
                .thenReturn(Optional.of(user));

        when(policy.canCreatePublicationType(user))
                .thenReturn(true);

        when(repository.findByMediaFormat(format))
                .thenReturn(Optional.of(existing));

        // ACT + ASSERT
        assertThrows(
                InstanceAlreadyExistsException.class,
                () -> service.createAndSave(format, userId)
        );
    }

// =========================================================
// FIND BY MEDIA FORMAT SUCCESS
// =========================================================

    @Test
    void shouldFindPublicationTypeByMediaFormat() {

        // ARRANGE
        String format = "Book";

        PublicationType publicationType =
                mock(PublicationType.class);

        when(repository.findByMediaFormat(format))
                .thenReturn(Optional.of(publicationType));

        // ACT
        PublicationType result =
                service.findByMediaFormat(format);

        // ASSERT
        assertEquals(publicationType, result);
    }

// =========================================================
// FIND BY MEDIA FORMAT NOT FOUND
// =========================================================

    @Test
    void shouldThrowWhenMediaFormatDoesNotExist() {

        // ARRANGE
        String format = "Book";

        when(repository.findByMediaFormat(format))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.findByMediaFormat(format)
        );
    }

// =========================================================
// FIND BY MEDIA FORMAT NULL
// =========================================================

    @Test
    void shouldThrowWhenMediaFormatIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.findByMediaFormat(null)
        );
    }

// =========================================================
// FIND BY ID SUCCESS
// =========================================================

    @Test
    void shouldFindPublicationTypeById() {

        // ARRANGE
        PublicationTypeId id =
                mock(PublicationTypeId.class);

        PublicationType publicationType =
                mock(PublicationType.class);

        when(repository.findById(id))
                .thenReturn(Optional.of(publicationType));

        // ACT
        PublicationType result =
                service.findById(id);

        // ASSERT
        assertEquals(publicationType, result);
    }

// =========================================================
// FIND BY ID NOT FOUND
// =========================================================

    @Test
    void shouldThrowWhenIdDoesNotExist() {

        // ARRANGE
        PublicationTypeId id =
                mock(PublicationTypeId.class);

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.findById(id)
        );
    }

// =========================================================
// FIND BY ID NULL
// =========================================================

    @Test
    void shouldThrowWhenIdIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.findById(null)
        );
    }

// =========================================================
// FIND ALL SUCCESS
// =========================================================

    @Test
    void shouldReturnAllPublicationTypes() {

        // ARRANGE
        PublicationType pt1 = mock(PublicationType.class);
        PublicationType pt2 = mock(PublicationType.class);

        when(repository.findAll())
                .thenReturn(List.of(pt1, pt2));

        // ACT
        List<PublicationType> result =
                service.findAll();

        // ASSERT
        assertEquals(2, result.size());
        assertTrue(result.contains(pt1));
        assertTrue(result.contains(pt2));
    }

// =========================================================
// FIND ALL EMPTY
// =========================================================

    @Test
    void shouldReturnEmptyListWhenNoPublicationTypesExist() {

        // ARRANGE
        when(repository.findAll())
                .thenReturn(List.of());

        // ACT
        List<PublicationType> result =
                service.findAll();

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}