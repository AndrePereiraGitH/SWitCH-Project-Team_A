package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationSampleAuthorizationPolicy;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationSampleService}.
 *
 * <p>Validates business logic, authorization enforcement, null guards,
 * and repository interactions across all service operations.</p>
 */
@ExtendWith(MockitoExtension.class)
class PublicationSampleServiceTest {

    @Mock
    private IPublicationSampleRepository publicationSampleRepository;
    @Mock
    private ICollectionOfPublicationsRepository collectionOfPublicationsRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private PublicationSampleFactory factory;
    @Mock
    private PublicationSampleAuthorizationPolicy policy;
    @Mock
    private ILibraryRepository libraryRepository;

    @InjectMocks
    private PublicationSampleService service;

    private UserId userId;
    private User user;
    private PublicationSampleId sampleId;
    private PublicationSample sample;
    private LibraryId libraryId;
    private CollectionOfPublicationsId collectionId;
    private BibliographicalDataId bdId;
    private EditionId editionId;

    @BeforeEach
    void setUp() {
        userId = mock(UserId.class);
        user = mock(User.class);
        sampleId = mock(PublicationSampleId.class);
        sample = mock(PublicationSample.class);
        libraryId = mock(LibraryId.class);
        collectionId = mock(CollectionOfPublicationsId.class);
        bdId = mock(BibliographicalDataId.class);
        editionId = mock(EditionId.class);
    }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenPublicationSampleRepositoryIsNull() {
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleService(
                        null, collectionOfPublicationsRepository, factory, policy, userRepository, libraryRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenCollectionOfPublicationsRepositoryIsNull() {
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleService(
                        publicationSampleRepository, null, factory, policy, userRepository, libraryRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleService(
                        publicationSampleRepository, collectionOfPublicationsRepository, null, policy, userRepository, libraryRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenPolicyIsNull() {
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleService(
                        publicationSampleRepository, collectionOfPublicationsRepository, factory, null, userRepository, libraryRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenUserRepositoryIsNull() {
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleService(
                        publicationSampleRepository, collectionOfPublicationsRepository, factory, policy, null, libraryRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenLibraryRepositoryIsNull() {
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleService(
                        publicationSampleRepository, collectionOfPublicationsRepository, factory, policy, userRepository, null));
    }

    // =========================================================================
    // createAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnPersistedSample_whenSuccessful() throws InstanceAlreadyExistsException {
        // Arrange
        PublicationSample saved = mock(PublicationSample.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(true);
        when(factory.create(bdId, userId, editionId, Condition.NEW)).thenReturn(sample);
        when(sample.id()).thenReturn(sampleId);
        when(publicationSampleRepository.existsById(sampleId)).thenReturn(false);
        when(publicationSampleRepository.save(sample)).thenReturn(saved);

        // Act
        PublicationSample result = service.createAndSave(bdId, userId, editionId, Condition.NEW);

        // Assert
        assertEquals(saved, result);
    }

    @Test
    void createAndSave_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.createAndSave(bdId, userId, editionId, Condition.NEW));
    }

    @Test
    void createAndSave_shouldThrowAccessDeniedException_whenAccessDenied() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.createAndSave(bdId, userId, editionId, Condition.NEW));
    }

    @Test
    void createAndSave_shouldThrowInstanceAlreadyExistsException_whenSampleAlreadyExists() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(true);
        when(factory.create(bdId, userId, editionId, Condition.NEW)).thenReturn(sample);
        when(sample.id()).thenReturn(sampleId);
        when(publicationSampleRepository.existsById(sampleId)).thenReturn(true);

        // Act & Assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                service.createAndSave(bdId, userId, editionId, Condition.NEW));
    }

    // =========================================================================
    // canCreatePublicationSample
    // =========================================================================

    @Test
    void canCreatePublicationSample_shouldReturnTrue_whenPolicyAllows() {
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(true);

        assertTrue(service.canCreatePublicationSample(userId));
    }

    @Test
    void canCreatePublicationSample_shouldReturnFalse_whenPolicyDenies() {
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);

        assertFalse(service.canCreatePublicationSample(userId));
    }

    @Test
    void canCreatePublicationSample_shouldThrowEntityNotFoundException_whenUserNotFound() {
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.canCreatePublicationSample(userId));
    }

    @Test
    void canCreatePublicationSample_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () -> service.canCreatePublicationSample(null));
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnSample_whenFound() {
        // Arrange
        when(publicationSampleRepository.findById(sampleId)).thenReturn(Optional.of(sample));

        // Act
        PublicationSample result = service.findById(sampleId);

        // Assert
        assertEquals(sample, result);
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenSampleNotFound() {
        // Arrange
        when(publicationSampleRepository.findById(sampleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.findById(sampleId));
    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnAllSamples() {
        // Arrange
        List<PublicationSample> all = List.of(sample, mock(PublicationSample.class));
        when(publicationSampleRepository.findAll()).thenReturn(all);

        // Act
        List<PublicationSample> result = service.findAll();

        // Assert
        assertEquals(all, result);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoSamplesExist() {
        // Arrange
        when(publicationSampleRepository.findAll()).thenReturn(List.of());

        // Act
        List<PublicationSample> result = service.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findByBibliographicalDataId
    // =========================================================================

    @Test
    void findByBibliographicalDataId_shouldReturnMatchingSamples() {
        // Arrange
        List<PublicationSample> samples = List.of(sample);
        when(publicationSampleRepository.findByBibliographicalDataId(bdId)).thenReturn(samples);

        // Act
        List<PublicationSample> result = service.findByBibliographicalDataId(bdId);

        // Assert
        assertEquals(samples, result);
    }

    @Test
    void findByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchExists() {
        // Arrange
        when(publicationSampleRepository.findByBibliographicalDataId(bdId)).thenReturn(List.of());

        // Act
        List<PublicationSample> result = service.findByBibliographicalDataId(bdId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBibliographicalDataId_shouldThrowNullPointerException_whenBdIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findByBibliographicalDataId(null));
    }

    // =========================================================================
    // findByBibliographicalDataIdIn
    // =========================================================================

    @Test
    void findByBibliographicalDataIdIn_shouldReturnMatchingSamples() {
        // Arrange
        List<BibliographicalDataId> bdIds = List.of(bdId);
        List<PublicationSample> samples = List.of(sample);
        when(publicationSampleRepository.findByBibliographicalDataIdIn(bdIds)).thenReturn(samples);

        // Act
        List<PublicationSample> result = service.findByBibliographicalDataIdIn(bdIds);

        // Assert
        assertEquals(samples, result);
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnEmptyList_whenNoMatchExists() {
        // Arrange
        List<BibliographicalDataId> bdIds = List.of(bdId);
        when(publicationSampleRepository.findByBibliographicalDataIdIn(bdIds)).thenReturn(List.of());

        // Act
        List<PublicationSample> result = service.findByBibliographicalDataIdIn(bdIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBibliographicalDataIdIn_shouldThrowNullPointerException_whenBdIdsIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findByBibliographicalDataIdIn(null));
    }

    // =========================================================================
    // findIdsByBibliographicalDataIdIn
    // =========================================================================

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnMatchingIds() {
        // Arrange
        List<BibliographicalDataId> bdIds = List.of(bdId);
        List<PublicationSampleId> ids = List.of(sampleId);
        when(publicationSampleRepository.findIdsByBibliographicalDataIdIn(bdIds)).thenReturn(ids);

        // Act
        List<PublicationSampleId> result = service.findIdsByBibliographicalDataIdIn(bdIds);

        // Assert
        assertEquals(ids, result);
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnEmptyList_whenNoMatchExists() {
        // Arrange
        List<BibliographicalDataId> bdIds = List.of(bdId);
        when(publicationSampleRepository.findIdsByBibliographicalDataIdIn(bdIds)).thenReturn(List.of());

        // Act
        List<PublicationSampleId> result = service.findIdsByBibliographicalDataIdIn(bdIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldThrowNullPointerException_whenBdIdsIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findIdsByBibliographicalDataIdIn(null));
    }

    // =========================================================================
    // findByEditionIdIn
    // =========================================================================

    @Test
    void findByEditionIdIn_shouldReturnMatchingSamples() {
        // Arrange
        List<EditionId> editionIds = List.of(editionId);
        List<PublicationSample> samples = List.of(sample);
        when(publicationSampleRepository.findByEditionIdIn(editionIds)).thenReturn(samples);

        // Act
        List<PublicationSample> result = service.findByEditionIdIn(editionIds);

        // Assert
        assertEquals(samples, result);
    }

    @Test
    void findByEditionIdIn_shouldReturnEmptyList_whenNoMatchExists() {
        // Arrange
        List<EditionId> editionIds = List.of(editionId);
        when(publicationSampleRepository.findByEditionIdIn(editionIds)).thenReturn(List.of());

        // Act
        List<PublicationSample> result = service.findByEditionIdIn(editionIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByEditionIdIn_shouldThrowNullPointerException_whenEditionIdsIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findByEditionIdIn(null));
    }

    // =========================================================================
    // findIdsByEditionIdIn
    // =========================================================================

    @Test
    void findIdsByEditionIdIn_shouldReturnMatchingIds() {
        // Arrange
        List<EditionId> editionIds = List.of(editionId);
        List<PublicationSampleId> ids = List.of(sampleId);
        when(publicationSampleRepository.findIdsByEditionIdIn(editionIds)).thenReturn(ids);

        // Act
        List<PublicationSampleId> result = service.findIdsByEditionIdIn(editionIds);

        // Assert
        assertEquals(ids, result);
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnEmptyList_whenNoMatchExists() {
        // Arrange
        List<EditionId> editionIds = List.of(editionId);
        when(publicationSampleRepository.findIdsByEditionIdIn(editionIds)).thenReturn(List.of());

        // Act
        List<PublicationSampleId> result = service.findIdsByEditionIdIn(editionIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdsByEditionIdIn_shouldThrowNullPointerException_whenEditionIdsIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findIdsByEditionIdIn(null));
    }

    // =========================================================================
    // findByCollectionOfPublicationsId
    // =========================================================================

    @Test
    void findByCollectionOfPublicationsId_shouldReturnSamples_whenSuccessful() throws Exception {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        List<PublicationSample> samples = List.of(sample);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.getOwnerId()).thenReturn(userId);
        when(publicationSampleRepository.findByCollectionOfPublicationsId(collectionId)).thenReturn(samples);

        // Act
        List<PublicationSample> result = service.findByCollectionOfPublicationsId(collectionId, userId);

        // Assert
        assertEquals(samples, result);
    }

    @Test
    void findByCollectionOfPublicationsId_shouldThrowAccessDeniedException_whenUserDoesNotOwnCollection() throws Exception {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        UserId otherUserId = mock(UserId.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.getOwnerId()).thenReturn(otherUserId);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.findByCollectionOfPublicationsId(collectionId, userId));
    }

    @Test
    void findByCollectionOfPublicationsId_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.findByCollectionOfPublicationsId(collectionId, userId));
    }

    @Test
    void findByCollectionOfPublicationsId_shouldThrowInstanceNotFoundException_whenCollectionNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () ->
                service.findByCollectionOfPublicationsId(collectionId, userId));
    }

    @Test
    void findByCollectionOfPublicationsId_shouldThrowAccessDeniedException_whenPolicyDenied() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.findByCollectionOfPublicationsId(collectionId, userId));
    }

    @Test
    void findByCollectionOfPublicationsId_shouldThrowNullPointerException_whenCollectionIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findByCollectionOfPublicationsId(null, userId));
    }

    // =========================================================================
    // canQueryByCollection
    // =========================================================================

    @Test
    void canQueryByCollection_shouldReturnTrue_whenPolicyAllowsAndUserOwnsCollection() {
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(true);
        when(collection.getOwnerId()).thenReturn(userId);

        assertTrue(service.canQueryByCollection(collectionId, userId));
    }

    @Test
    void canQueryByCollection_shouldReturnFalse_whenPolicyDenies() {
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(collection.getOwnerId()).thenReturn(userId);

        assertFalse(service.canQueryByCollection(collectionId, userId));
    }

    @Test
    void canQueryByCollection_shouldReturnFalse_whenUserDoesNotOwnCollection() {
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        UserId otherUserId = mock(UserId.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(true);
        when(collection.getOwnerId()).thenReturn(otherUserId);

        assertFalse(service.canQueryByCollection(collectionId, userId));
    }

    @Test
    void canQueryByCollection_shouldThrowEntityNotFoundException_whenUserNotFound() {
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.canQueryByCollection(collectionId, userId));
    }

    @Test
    void canQueryByCollection_shouldThrowEntityNotFoundException_whenCollectionNotFound() {
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.canQueryByCollection(collectionId, userId));
    }

    @Test
    void canQueryByCollection_shouldThrowNullPointerException_whenCollectionIdIsNull() {
        assertThrows(NullPointerException.class, () -> service.canQueryByCollection(null, userId));
    }

    @Test
    void canQueryByCollection_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () -> service.canQueryByCollection(collectionId, null));
    }

    // =========================================================================
    // findAllPublicationsOwnedByUser
    // =========================================================================

    @Test
    void findAllPublicationsOwnedByUser_shouldReturnAllSamples_whenSuccessful() {
        // Arrange
        List<PublicationSample> samples = List.of(sample, mock(PublicationSample.class));
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsOwnedByUserId(user)).thenReturn(true);
        when(publicationSampleRepository.findAllByOwnerId(userId)).thenReturn(samples);

        // Act
        List<PublicationSample> result = service.findAllPublicationsOwnedByUser(userId);

        // Assert
        assertEquals(samples, result);
    }

    @Test
    void findAllPublicationsOwnedByUser_shouldReturnEmptyList_whenUserHasNoPublications() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsOwnedByUserId(user)).thenReturn(true);
        when(publicationSampleRepository.findAllByOwnerId(userId)).thenReturn(List.of());

        // Act
        List<PublicationSample> result = service.findAllPublicationsOwnedByUser(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllPublicationsOwnedByUser_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.findAllPublicationsOwnedByUser(userId));
    }

    @Test
    void findAllPublicationsOwnedByUser_shouldThrowAccessDeniedException_whenAccessDenied() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsOwnedByUserId(user)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.findAllPublicationsOwnedByUser(userId));
    }

    @Test
    void findAllPublicationsOwnedByUser_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findAllPublicationsOwnedByUser(null));
    }

    // =========================================================================
    // canFindOwnPublications
    // =========================================================================

    @Test
    void canFindOwnPublications_shouldReturnTrue_whenPolicyAllows() {
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsOwnedByUserId(user)).thenReturn(true);

        assertTrue(service.canFindOwnPublications(userId));
    }

    @Test
    void canFindOwnPublications_shouldReturnFalse_whenPolicyDenies() {
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsOwnedByUserId(user)).thenReturn(false);

        assertFalse(service.canFindOwnPublications(userId));
    }

    @Test
    void canFindOwnPublications_shouldThrowEntityNotFoundException_whenUserNotFound() {
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.canFindOwnPublications(userId));
    }

    @Test
    void canFindOwnPublications_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () -> service.canFindOwnPublications(null));
    }

    // =========================================================================
    // findPublicationsInLibrary
    // =========================================================================

    @Test
    void findPublicationsInLibrary_shouldReturnSamples_whenSuccessful() {
        // Arrange
        List<PublicationSample> samples = List.of(sample);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(true);
        when(publicationSampleRepository.findPublicationsInLibrary(userId)).thenReturn(samples);

        // Act
        List<PublicationSample> result = service.findPublicationsInLibrary(userId);

        // Assert
        assertEquals(samples, result);
    }

    @Test
    void findPublicationsInLibrary_shouldReturnEmptyList_whenUserHasNoPublicationsInLibrary() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(true);
        when(publicationSampleRepository.findPublicationsInLibrary(userId)).thenReturn(List.of());

        // Act
        List<PublicationSample> result = service.findPublicationsInLibrary(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicationsInLibrary_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.findPublicationsInLibrary(userId));
    }

    @Test
    void findPublicationsInLibrary_shouldThrowAccessDeniedException_whenAccessDenied() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.findPublicationsInLibrary(userId));
    }

    @Test
    void findPublicationsInLibrary_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findPublicationsInLibrary(null));
    }

    // =========================================================================
    // canFindPublicationsInLibrary
    // =========================================================================

    @Test
    void canFindPublicationsInLibrary_shouldReturnTrue_whenPolicyAllows() {
        Library library = mock(Library.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(true);
        when(libraryRepository.findByUserId(userId)).thenReturn(Optional.of(library));

        assertTrue(service.canFindPublicationsInLibrary(userId));
    }

    @Test
    void canFindPublicationsInLibrary_shouldReturnFalse_whenPolicyDenies() {
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);

        assertFalse(service.canFindPublicationsInLibrary(userId));
    }

    @Test
    void canFindPublicationsInLibrary_shouldThrowEntityNotFoundException_whenUserNotFound() {
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.canFindPublicationsInLibrary(userId));
    }

    @Test
    void canFindPublicationsInLibrary_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () -> service.canFindPublicationsInLibrary(null));
    }

    // =========================================================================
    // findPublicationsNotInLibrary
    // =========================================================================

    @Test
    void findPublicationsNotInLibrary_shouldReturnSamples_whenSuccessful() {
        // Arrange
        List<PublicationSample> samples = List.of(sample);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(true);
        when(publicationSampleRepository.findPublicationsNotInLibrary(userId)).thenReturn(samples);

        // Act
        List<PublicationSample> result = service.findPublicationsNotInLibrary(userId);

        // Assert
        assertEquals(samples, result);
    }

    @Test
    void findPublicationsNotInLibrary_shouldReturnEmptyList_whenUserHasNoPublicationsOutsideLibrary() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(true);
        when(publicationSampleRepository.findPublicationsNotInLibrary(userId)).thenReturn(List.of());

        // Act
        List<PublicationSample> result = service.findPublicationsNotInLibrary(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicationsNotInLibrary_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.findPublicationsNotInLibrary(userId));
    }

    @Test
    void findPublicationsNotInLibrary_shouldThrowAccessDeniedException_whenAccessDenied() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.findPublicationsNotInLibrary(userId));
    }

    @Test
    void findPublicationsNotInLibrary_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findPublicationsNotInLibrary(null));
    }

    // =========================================================================
    // findByLibraryIdAndPublicationStatusNotIn
    // =========================================================================

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldExcludeSoldAndForSaleStatuses_whenSuccessful() {
        // Arrange
        Library library = mock(Library.class);
        List<PublicationStatus> excluded = List.of(PublicationStatus.SOLD, PublicationStatus.FOR_SALE);
        List<PublicationSample> samples = List.of(sample);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(true);
        when(libraryRepository.findByUserId(userId)).thenReturn(Optional.of(library));
        when(library.id()).thenReturn(libraryId);
        when(publicationSampleRepository.findByLibraryIdAndPublicationStatusNotIn(libraryId, excluded))
                .thenReturn(samples);

        // Act
        List<PublicationSample> result = service.findByLibraryIdAndPublicationStatusNotIn(userId);

        // Assert
        assertEquals(samples, result);
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnEmptyList_whenNoAvailablePublications() {
        // Arrange
        Library library = mock(Library.class);
        List<PublicationStatus> excluded = List.of(PublicationStatus.SOLD, PublicationStatus.FOR_SALE);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(true);
        when(libraryRepository.findByUserId(userId)).thenReturn(Optional.of(library));
        when(library.id()).thenReturn(libraryId);
        when(publicationSampleRepository.findByLibraryIdAndPublicationStatusNotIn(libraryId, excluded))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result = service.findByLibraryIdAndPublicationStatusNotIn(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.findByLibraryIdAndPublicationStatusNotIn(userId));
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldThrowAccessDeniedException_whenAccessDenied() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.findByLibraryIdAndPublicationStatusNotIn(userId));
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldThrowEntityNotFoundException_whenLibraryNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(true);
        when(libraryRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.findByLibraryIdAndPublicationStatusNotIn(userId));
    }

    // =========================================================================
    // findAvailableForCollection
    // =========================================================================

    @Test
    void findAvailableForCollection_shouldReturnOnlySamplesNotAlreadyInCollection() throws Exception {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        PublicationSample inCollection = mock(PublicationSample.class);
        PublicationSample notInCollection = mock(PublicationSample.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.getOwnerId()).thenReturn(userId);
        when(publicationSampleRepository.findPublicationsInLibrary(userId))
                .thenReturn(List.of(inCollection, notInCollection));
        when(publicationSampleRepository.findByCollectionOfPublicationsId(collectionId))
                .thenReturn(List.of(inCollection));

        // Act
        List<PublicationSample> result = service.findAvailableForCollection(userId, collectionId);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(notInCollection));
        assertFalse(result.contains(inCollection));
    }

    @Test
    void findAvailableForCollection_shouldReturnAllUserSamples_whenCollectionIsEmpty() throws Exception {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        PublicationSample s1 = mock(PublicationSample.class);
        PublicationSample s2 = mock(PublicationSample.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.getOwnerId()).thenReturn(userId);
        when(publicationSampleRepository.findPublicationsInLibrary(userId))
                .thenReturn(List.of(s1, s2));
        when(publicationSampleRepository.findByCollectionOfPublicationsId(collectionId))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result = service.findAvailableForCollection(userId, collectionId);

        // Assert
        assertEquals(List.of(s1, s2), result);
    }

    @Test
    void findAvailableForCollection_shouldReturnEmptyList_whenAllUserSamplesAlreadyInCollection() throws Exception {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.getOwnerId()).thenReturn(userId);
        when(publicationSampleRepository.findPublicationsInLibrary(userId))
                .thenReturn(List.of(sample));
        when(publicationSampleRepository.findByCollectionOfPublicationsId(collectionId))
                .thenReturn(List.of(sample));

        // Act
        List<PublicationSample> result = service.findAvailableForCollection(userId, collectionId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAvailableForCollection_shouldThrowAccessDeniedException_whenUserDoesNotOwnCollection() throws Exception {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        UserId otherUserId = mock(UserId.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.getOwnerId()).thenReturn(otherUserId);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.findAvailableForCollection(userId, collectionId));
    }

    @Test
    void findAvailableForCollection_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.findAvailableForCollection(userId, collectionId));
    }

    @Test
    void findAvailableForCollection_shouldThrowInstanceNotFoundException_whenCollectionNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () ->
                service.findAvailableForCollection(userId, collectionId));
    }

    @Test
    void findAvailableForCollection_shouldThrowAccessDeniedException_whenPolicyDenied() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.findAvailableForCollection(userId, collectionId));
    }

    @Test
    void findAvailableForCollection_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findAvailableForCollection(null, collectionId));
    }

    @Test
    void findAvailableForCollection_shouldThrowNullPointerException_whenCollectionIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findAvailableForCollection(userId, null));
    }

    // =========================================================================
    // addPublicationToLibrary
    // =========================================================================

    @Test
    void addPublicationToLibrary_shouldAssignLibraryAndReturnSavedSample_whenSuccessful() {
        // Arrange
        Library library = mock(Library.class);
        PublicationSample saved = mock(PublicationSample.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAddPublicationToLibrary(user)).thenReturn(true);
        when(libraryRepository.findByUserId(userId)).thenReturn(Optional.of(library));
        when(library.id()).thenReturn(libraryId);
        when(publicationSampleRepository.findById(sampleId)).thenReturn(Optional.of(sample));
        when(sample.isOwnedByUser(userId)).thenReturn(true);
        when(publicationSampleRepository.save(sample)).thenReturn(saved);

        // Act
        PublicationSample result = service.addPublicationToLibrary(sampleId, userId);

        // Assert
        assertEquals(saved, result);
    }

    @Test
    void addPublicationToLibrary_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.addPublicationToLibrary(sampleId, userId));
    }

    @Test
    void addPublicationToLibrary_shouldThrowAccessDeniedException_whenPolicyDenied() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.addPublicationToLibrary(sampleId, userId));
    }

    @Test
    void addPublicationToLibrary_shouldThrowEntityNotFoundException_whenLibraryNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAddPublicationToLibrary(user)).thenReturn(true);
        when(libraryRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.addPublicationToLibrary(sampleId, userId));
    }

    @Test
    void addPublicationToLibrary_shouldThrowEntityNotFoundException_whenSampleNotFound() {
        // Arrange
        Library library = mock(Library.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAddPublicationToLibrary(user)).thenReturn(true);
        when(libraryRepository.findByUserId(userId)).thenReturn(Optional.of(library));
        when(library.id()).thenReturn(libraryId);
        when(publicationSampleRepository.findById(sampleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.addPublicationToLibrary(sampleId, userId));
    }

    @Test
    void addPublicationToLibrary_shouldThrowAccessDeniedException_whenUserDoesNotOwnSample() {
        // Arrange
        Library library = mock(Library.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAddPublicationToLibrary(user)).thenReturn(true);
        when(libraryRepository.findByUserId(userId)).thenReturn(Optional.of(library));
        when(library.id()).thenReturn(libraryId);
        when(publicationSampleRepository.findById(sampleId)).thenReturn(Optional.of(sample));
        when(sample.isOwnedByUser(userId)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.addPublicationToLibrary(sampleId, userId));
    }

    @Test
    void addPublicationToLibrary_shouldThrowNullPointerException_whenPublicationSampleIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.addPublicationToLibrary(null, userId));
    }

    // =========================================================================
    // assignPublicationToCollection
    // =========================================================================

    @Test
    void assignPublicationToCollection_shouldAssignCollectionAndReturnSavedSample_whenSuccessful() {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        PublicationSample saved = mock(PublicationSample.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAssignPublicationToCollection(user)).thenReturn(true);
        when(publicationSampleRepository.findById(sampleId)).thenReturn(Optional.of(sample));
        when(sample.isOwnedByUser(userId)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.getOwnerId()).thenReturn(userId);
        when(publicationSampleRepository.save(sample)).thenReturn(saved);

        // Act
        PublicationSample result = service.assignPublicationToCollection(collectionId, sampleId, userId);

        // Assert
        assertEquals(saved, result);
    }

    @Test
    void assignPublicationToCollection_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.assignPublicationToCollection(collectionId, sampleId, userId));
    }

    @Test
    void assignPublicationToCollection_shouldThrowAccessDeniedException_whenPolicyDenied() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.assignPublicationToCollection(collectionId, sampleId, userId));
    }

    @Test
    void assignPublicationToCollection_shouldThrowEntityNotFoundException_whenSampleNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAssignPublicationToCollection(user)).thenReturn(true);
        when(publicationSampleRepository.findById(sampleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.assignPublicationToCollection(collectionId, sampleId, userId));
    }

    @Test
    void assignPublicationToCollection_shouldThrowAccessDeniedException_whenUserDoesNotOwnSample() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAssignPublicationToCollection(user)).thenReturn(true);
        when(publicationSampleRepository.findById(sampleId)).thenReturn(Optional.of(sample));
        when(sample.isOwnedByUser(userId)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.assignPublicationToCollection(collectionId, sampleId, userId));
    }

    @Test
    void assignPublicationToCollection_shouldThrowNullPointerException_whenCollectionIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.assignPublicationToCollection(null, sampleId, userId));
    }

    @Test
    void assignPublicationToCollection_shouldThrowNullPointerException_whenPublicationSampleIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.assignPublicationToCollection(collectionId, null, userId));
    }

    // =========================================================================
    // findPublicationsInPublicCollection
    // =========================================================================

    @Test
    void findPublicationsInPublicCollection_shouldReturnSamples_whenCollectionIsPublic() throws Exception {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        List<PublicationSample> samples = List.of(sample);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInPublicCollection(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.isCurrentlyPublic()).thenReturn(true);
        when(publicationSampleRepository.findByCollectionOfPublicationsId(collectionId)).thenReturn(samples);

        // Act
        List<PublicationSample> result = service.findPublicationsInPublicCollection(collectionId, userId);

        // Assert
        assertEquals(samples, result);
    }

    @Test
    void findPublicationsInPublicCollection_shouldThrowAccessDeniedException_whenCollectionIsPrivate() {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInPublicCollection(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.isCurrentlyPublic()).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.findPublicationsInPublicCollection(collectionId, userId));
    }

    @Test
    void findPublicationsInPublicCollection_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.findPublicationsInPublicCollection(collectionId, userId));
    }

    @Test
    void findPublicationsInPublicCollection_shouldThrowAccessDeniedException_whenPolicyDenied() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInPublicCollection(user)).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                service.findPublicationsInPublicCollection(collectionId, userId));
    }

    @Test
    void findPublicationsInPublicCollection_shouldThrowInstanceNotFoundException_whenCollectionNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInPublicCollection(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () ->
                service.findPublicationsInPublicCollection(collectionId, userId));
    }

    @Test
    void findPublicationsInPublicCollection_shouldThrowNullPointerException_whenCollectionIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.findPublicationsInPublicCollection(null, userId));
    }

    // =========================================================================
    // canFindPublicationsNotInLibrary
    // =========================================================================

    @Test
    void canFindPublicationsNotInLibrary_shouldReturnTrue_whenPolicyAllows() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(true);

        // Act
        boolean result = service.canFindPublicationsNotInLibrary(userId);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindPublicationsNotInLibrary_shouldReturnFalse_whenPolicyDenies() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);

        // Act
        boolean result = service.canFindPublicationsNotInLibrary(userId);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindPublicationsNotInLibrary_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.canFindPublicationsNotInLibrary(userId));
    }

    @Test
    void canFindPublicationsNotInLibrary_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.canFindPublicationsNotInLibrary(null));
    }

    // =========================================================================
    // canFindAvailableInLibrary
    // =========================================================================

    @Test
    void canFindAvailableInLibrary_shouldReturnTrue_whenPolicyAllows() {
        Library library = mock(Library.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(true);
        when(libraryRepository.findByUserId(userId)).thenReturn(Optional.of(library));

        assertTrue(service.canFindAvailableInLibrary(userId));
    }

    @Test
    void canFindAvailableInLibrary_shouldReturnFalse_whenPolicyDenies() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);

        // Act
        boolean result = service.canFindAvailableInLibrary(userId);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindAvailableInLibrary_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.canFindAvailableInLibrary(userId));
    }

    @Test
    void canFindAvailableInLibrary_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.canFindAvailableInLibrary(null));
    }

    // =========================================================================
    // canFindAvailableForCollection
    // =========================================================================

    @Test
    void canFindAvailableForCollection_shouldReturnTrue_whenPolicyAllowsAndUserOwnsCollection() throws Exception {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.getOwnerId()).thenReturn(userId);

        // Act
        boolean result = service.canFindAvailableForCollection(userId, collectionId);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindAvailableForCollection_shouldReturnFalse_whenPolicyDenies() throws Exception {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.getOwnerId()).thenReturn(userId);

        // Act
        boolean result = service.canFindAvailableForCollection(userId, collectionId);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindAvailableForCollection_shouldReturnFalse_whenUserDoesNotOwnCollection() throws Exception {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        UserId otherUserId = mock(UserId.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.getOwnerId()).thenReturn(otherUserId);

        // Act
        boolean result = service.canFindAvailableForCollection(userId, collectionId);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindAvailableForCollection_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.canFindAvailableForCollection(userId, collectionId));
    }

    @Test
    void canFindAvailableForCollection_shouldThrowInstanceNotFoundException_whenCollectionNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () ->
                service.canFindAvailableForCollection(userId, collectionId));
    }

    @Test
    void canFindAvailableForCollection_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.canFindAvailableForCollection(null, collectionId));
    }

    @Test
    void canFindAvailableForCollection_shouldThrowNullPointerException_whenCollectionIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.canFindAvailableForCollection(userId, null));
    }

    // =========================================================================
    // canAddPublicationToLibrary
    // =========================================================================

    @Test
    void canAddPublicationToLibrary_shouldReturnTrue_whenPolicyAllows() {
        Library library = mock(Library.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAddPublicationToLibrary(user)).thenReturn(true);
        when(libraryRepository.findByUserId(userId)).thenReturn(Optional.of(library));

        assertTrue(service.canAddPublicationToLibrary(userId));
    }

    @Test
    void canAddPublicationToLibrary_shouldReturnFalse_whenPolicyDenies() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);

        // Act
        boolean result = service.canAddPublicationToLibrary(userId);

        // Assert
        assertFalse(result);
    }

    @Test
    void canAddPublicationToLibrary_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.canAddPublicationToLibrary(userId));
    }

    @Test
    void canAddPublicationToLibrary_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.canAddPublicationToLibrary(null));
    }

    // =========================================================================
    // canAssignPublicationToCollection
    // =========================================================================

    @Test
    void canAssignPublicationToCollection_shouldReturnTrue_whenPolicyAllows() {
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAssignPublicationToCollection(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findAllByOwnerId(userId)).thenReturn(List.of(collection));

        assertTrue(service.canAssignPublicationToCollection(userId));
    }

    @Test
    void canAssignPublicationToCollection_shouldReturnFalse_whenPolicyDenies() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        boolean result = service.canAssignPublicationToCollection(userId);

        // Assert
        assertFalse(result);
    }

    @Test
    void canAssignPublicationToCollection_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.canAssignPublicationToCollection(userId));
    }

    @Test
    void canAssignPublicationToCollection_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.canAssignPublicationToCollection(null));
    }

    // =========================================================================
    // canFindPublicationsInPublicCollection
    // =========================================================================

    @Test
    void canFindPublicationsInPublicCollection_shouldReturnTrue_whenPolicyAllows() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(true);

        // Act
        boolean result = service.canFindPublicationsInPublicCollection(userId);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindPublicationsInPublicCollection_shouldReturnFalse_whenPolicyDenies() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);

        // Act
        boolean result = service.canFindPublicationsInPublicCollection(userId);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindPublicationsInPublicCollection_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.canFindPublicationsInPublicCollection(userId));
    }

    @Test
    void canFindPublicationsInPublicCollection_shouldThrowNullPointerException_whenUserIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                service.canFindPublicationsInPublicCollection(null));
    }
}