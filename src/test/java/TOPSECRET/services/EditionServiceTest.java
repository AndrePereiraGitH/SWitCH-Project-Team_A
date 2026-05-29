package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.EditionAuthorizationPolicy;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EditionService}.
 *
 * <p>Validates the main business rules of edition creation,
 * including authorization checks and repository interaction.</p>
 *
 * <p>All dependencies are mocked using Mockito and tests follow the AAA pattern.</p>
 */
@ExtendWith(MockitoExtension.class)
class EditionServiceTest {

    @Mock
    private IEditionRepository editionRepository;

    @Mock
    private EditionFactory factory;

    @Mock
    private EditionAuthorizationPolicy policy;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private EditionService service;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private UserId userId() { return new UserId("user-1"); }
    private User user() { return mock(User.class); }
    private Edition edition() { return mock(Edition.class); }
    private Code code() { return mock(Code.class); }
    private PhysicalAttributes physicalAttributes() { return mock(PhysicalAttributes.class); }
    private PublicationTypeId publicationTypeId() { return mock(PublicationTypeId.class); }
    private PublisherID publisherId() { return mock(PublisherID.class); }
    private Locale language() { return Locale.ENGLISH; }
    private LocalDate date() { return LocalDate.of(2020, 1, 1); }
    private TypeOfBinding binding() { return TypeOfBinding.HARDBACK; }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowWhenRepositoryIsNull() {

        // Act
        Exception ex = assertThrows(NullPointerException.class,
                () -> new EditionService(null, factory, policy, userRepository));

        // Assert
        assertNotNull(ex);
    }

    @Test
    void constructor_shouldThrowWhenFactoryIsNull() {

        // Act
        Exception ex = assertThrows(NullPointerException.class,
                () -> new EditionService(editionRepository, null, policy, userRepository));

        // Assert
        assertNotNull(ex);
    }

    @Test
    void constructor_shouldThrowWhenPolicyIsNull() {

        // Act
        Exception ex = assertThrows(NullPointerException.class,
                () -> new EditionService(editionRepository, factory, null, userRepository));

        // Assert
        assertNotNull(ex);
    }

    @Test
    void constructor_shouldThrowWhenUserRepositoryIsNull() {

        // Act
        Exception ex = assertThrows(NullPointerException.class,
                () -> new EditionService(editionRepository, factory, policy, null));

        // Assert
        assertNotNull(ex);
    }

    // =========================================================================
    // createAndSave — success
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedEdition() throws AccessDeniedException {

        // ARRANGE
        User user = user();
        Edition created = edition();
        Edition saved = edition();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);
        when(factory.create("1st", code(), physicalAttributes(), publicationTypeId(),
                publisherId(), "Book", language(), date(), binding()))
                .thenReturn(created);
        when(editionRepository.save(created)).thenReturn(saved);

        // ACT
        Edition result = service.createAndSave(
                "1st", code(), physicalAttributes(), publicationTypeId(),
                publisherId(), "Book", language(), date(), binding(), userId());

        // ASSERT
        assertSame(saved, result);
    }

    // =========================================================================
    // createAndSave — access denied
    // =========================================================================

    @Test
    void createAndSave_shouldThrowAccessDenied_whenUserIsNotAuthorized() {

        // ARRANGE
        User user = user();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        // ACT
        Exception ex = assertThrows(AccessDeniedException.class,
                () -> service.createAndSave(
                        "1st", code(), physicalAttributes(), publicationTypeId(),
                        publisherId(), "Book", language(), date(), binding(), userId()));

        // ASSERT
        assertNotNull(ex);
    }

    // =========================================================================
    // createAndSave — user not found
    // =========================================================================

    @Test
    void createAndSave_shouldThrowIllegalArgument_whenUserNotFound() {

        // ARRANGE
        when(userRepository.findByID(userId())).thenReturn(Optional.empty());

        // ACT
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> service.createAndSave(
                        "1st", code(), physicalAttributes(), publicationTypeId(),
                        publisherId(), "Book", language(), date(), binding(), userId()));

        // ASSERT
        assertNotNull(ex);
    }

    // =========================================================================
    // createAndSave — repository error
    // =========================================================================

    @Test
    void createAndSave_shouldPropagateException_whenRepositoryThrowsOnSave()
            throws AccessDeniedException {

        // ARRANGE
        User user = user();
        Edition created = edition();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);
        when(factory.create("1st", code(), physicalAttributes(), publicationTypeId(),
                publisherId(), "Book", language(), date(), binding()))
                .thenReturn(created);
        when(editionRepository.save(created)).thenThrow(new RuntimeException("DB error"));

        // ACT
        Exception ex = assertThrows(RuntimeException.class,
                () -> service.createAndSave(
                        "1st", code(), physicalAttributes(), publicationTypeId(),
                        publisherId(), "Book", language(), date(), binding(), userId()));

        // ASSERT
        assertNotNull(ex);
    }
}