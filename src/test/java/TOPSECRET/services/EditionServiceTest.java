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
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    private UserId userId() { return new UserId("user-1"); }
    private User user() { return mock(User.class); }
    private Edition edition() { return mock(Edition.class); }
    private Code code() { return mock(Code.class); }
    private PhysicalAttributes physicalAttributes() { return mock(PhysicalAttributes.class); }
    private PublicationTypeId publicationTypeId() { return mock(PublicationTypeId.class); }
    private PublisherID publisherId() { return mock(PublisherID.class); }
    private EditionId editionId() { return mock(EditionId.class); }
    private Locale language() { return Locale.ENGLISH; }
    private LocalDate date() { return LocalDate.of(2020, 1, 1); }
    private TypeOfBinding binding() { return TypeOfBinding.HARDBACK; }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowWhenRepositoryIsNull() {
        // act + assert
        assertThrows(NullPointerException.class,
                () -> new EditionService(null, factory, policy, userRepository));
    }

    @Test
    void constructor_shouldThrowWhenFactoryIsNull() {
        // act + assert
        assertThrows(NullPointerException.class,
                () -> new EditionService(editionRepository, null, policy, userRepository));
    }

    @Test
    void constructor_shouldThrowWhenPolicyIsNull() {
        // act + assert
        assertThrows(NullPointerException.class,
                () -> new EditionService(editionRepository, factory, null, userRepository));
    }

    @Test
    void constructor_shouldThrowWhenUserRepositoryIsNull() {

        // act + assert
        assertThrows(NullPointerException.class,
                () -> new EditionService(editionRepository, factory, policy, null));
    }

    // =========================================================================
    // createAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedEdition() throws AccessDeniedException {
        // Arrange
        UserId userId = userId();
        User user = user();
        Edition created = edition();
        Edition saved = edition();

        Code code = code();
        PhysicalAttributes physicalAttributes = physicalAttributes();
        PublicationTypeId publicationTypeId = publicationTypeId();
        PublisherID publisherId = publisherId();

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);
        when(factory.create(
                "1st",
                code,
                physicalAttributes,
                publicationTypeId,
                publisherId,
                "Book",
                language(),
                date(),
                binding()
        )).thenReturn(created);
        when(editionRepository.save(created)).thenReturn(saved);

        // Act
        Edition result = service.createAndSave(
                "1st",
                code,
                physicalAttributes,
                publicationTypeId,
                publisherId,
                "Book",
                language(),
                date(),
                binding(),
                userId
        );

        // Assert
        assertSame(saved, result);
    }

    @Test
    void createAndSave_shouldThrowAccessDenied_whenUserIsNotAuthorized() {
        // Arrange
        UserId userId = userId();
        User user = user();

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> service.createAndSave(
                        "1st",
                        code(),
                        physicalAttributes(),
                        publicationTypeId(),
                        publisherId(),
                        "Book",
                        language(),
                        date(),
                        binding(),
                        userId
                ));

    }

    @Test
    void createAndSave_shouldThrowIllegalArgument_whenUserNotFound() {
        // Arrange
        UserId userId = userId();

        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.createAndSave(
                        "1st",
                        code(),
                        physicalAttributes(),
                        publicationTypeId(),
                        publisherId(),
                        "Book",
                        language(),
                        date(),
                        binding(),
                        userId
                ));

    }

    @Test
    void createAndSave_shouldPropagateException_whenRepositoryThrowsOnSave() throws AccessDeniedException {
        // Arrange
        UserId userId = userId();
        User user = user();
        Edition created = edition();

        Code code = code();
        PhysicalAttributes physicalAttributes = physicalAttributes();
        PublicationTypeId publicationTypeId = publicationTypeId();
        PublisherID publisherId = publisherId();

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);
        when(factory.create(
                "1st",
                code,
                physicalAttributes,
                publicationTypeId,
                publisherId,
                "Book",
                language(),
                date(),
                binding()
        )).thenReturn(created);
        when(editionRepository.save(created)).thenThrow(new RuntimeException("DB error"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createAndSave(
                        "1st",
                        code,
                        physicalAttributes,
                        publicationTypeId,
                        publisherId,
                        "Book",
                        language(),
                        date(),
                        binding(),
                        userId
                ));

        assertEquals("DB error", ex.getMessage());
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnEdition_whenEditionExists() {
        // Arrange
        EditionId editionId = editionId();
        Edition edition = edition();

        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));

        // Act
        Edition result = service.findById(editionId);

        // Assert
        assertSame(edition, result);
    }

    @Test
    void findById_shouldThrowNullPointerException_whenEditionIdIsNull() {
        // act + assert
        assertThrows(NullPointerException.class,
                () -> service.findById(null));

    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnAllEditions() {
        // Arrange
        Edition edition1 = edition();
        Edition edition2 = edition();
        List<Edition> editions = List.of(edition1, edition2);

        when(editionRepository.findAll()).thenReturn(editions);

        // Act
        List<Edition> result = service.findAll();

        // Assert
        assertSame(editions, result);
        assertEquals(2, result.size());
    }

    // =========================================================================
    // findIdsByPublisherId
    // =========================================================================

    @Test
    void findIdsByPublisherId_shouldReturnEditionIds() {
        // Arrange
        PublisherID publisherId = publisherId();
        EditionId editionId1 = editionId();
        EditionId editionId2 = editionId();
        List<EditionId> ids = List.of(editionId1, editionId2);

        when(editionRepository.findIdsByPublisherId(publisherId)).thenReturn(ids);

        // Act
        List<EditionId> result = service.findIdsByPublisherId(publisherId);

        // Assert
        assertSame(ids, result);
        assertEquals(2, result.size());
    }

    @Test
    void findIdsByPublisherId_shouldThrowNullPointerException_whenPublisherIdIsNull() {
        // act + assert
        assertThrows(NullPointerException.class,
                () -> service.findIdsByPublisherId(null));

    }

    // =========================================================================
    // findByPublicationTypeId
    // =========================================================================

    @Test
    void findByPublicationTypeId_shouldReturnEditions() {
        // Arrange
        PublicationTypeId publicationTypeId = publicationTypeId();
        Edition edition1 = edition();
        Edition edition2 = edition();
        List<Edition> editions = List.of(edition1, edition2);

        when(editionRepository.findByPublicationTypeId(publicationTypeId)).thenReturn(editions);

        // Act
        List<Edition> result = service.findByPublicationTypeId(publicationTypeId);

        // Assert
        assertSame(editions, result);
        assertEquals(2, result.size());
        verify(editionRepository).findByPublicationTypeId(publicationTypeId);
    }

    @Test
    void findByPublicationTypeId_shouldThrowNullPointerException_whenPublicationTypeIdIsNull() {
        // act + assert
        assertThrows(NullPointerException.class,
                () -> service.findByPublicationTypeId(null));

    }

    // =========================================================================
    // findByPublisherId
    // =========================================================================

    @Test
    void findByPublisherId_shouldReturnEditions() {
        // Arrange
        PublisherID publisherId = publisherId();
        Edition edition1 = edition();
        Edition edition2 = edition();
        List<Edition> editions = List.of(edition1, edition2);

        when(editionRepository.findByPublisherId(publisherId)).thenReturn(editions);

        // Act
        List<Edition> result = service.findByPublisherId(publisherId);

        // Assert
        assertSame(editions, result);
        assertEquals(2, result.size());
    }

    @Test
    void findByPublisherId_shouldThrowNullPointerException_whenPublisherIdIsNull() {
        // act + assert
        assertThrows(NullPointerException.class,
                () -> service.findByPublisherId(null));

    }
}