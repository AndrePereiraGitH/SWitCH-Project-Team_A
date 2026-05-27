package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CollectionOfPublicationsAuthorizationPolicy;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CollectionOfPublicationsService}.
 *
 * <p>Covers two areas:</p>
 * <ul>
 *   <li><b>createAndSave — happy path</b>: verifies that a
 *       {@link CollectionOfPublications} is correctly created via
 *       {@link CollectionOfPublicationsFactory} and persisted via
 *       {@link ICollectionOfPublicationsRepository}, returning the saved instance.</li>
 *
 *   <li><b>createAndSave — null rejection</b>: verifies that all mandatory
 *       parameters ({@code collectionName}, {@code collectionGenreId},
 *       {@code userId}) throw {@link NullPointerException}
 *       when {@code null}.</li>
 * </ul>
 */
class CollectionOfPublicationsServiceTest {

    @Mock
    ICollectionOfPublicationsRepository collectionOfPublicationsRepository;

    @Mock
    CollectionOfPublicationsFactory factory;

    @Mock
    CollectionOfPublicationsAuthorizationPolicy policy;

    @Mock
    IUserRepository userRepository;

    @InjectMocks
    CollectionOfPublicationsService service;

    private GenreId genreIdDouble;
    private UserId userIdDouble;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        genreIdDouble = mock(GenreId.class);
        userIdDouble = mock(UserId.class);
    }

    // ===============================================================
    // ------------------------- CONSTRUCTOR -------------------------
    // ===============================================================

    @Test
    void constructorShouldThrowExceptionWhenRepositoryIsNull() {
        // Arrange
        ICollectionOfPublicationsRepository repo = null;
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsAuthorizationPolicy policy = mock(CollectionOfPublicationsAuthorizationPolicy.class);
        IUserRepository userRepo = mock(IUserRepository.class);

        // Act + Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new CollectionOfPublicationsService(repo, factory, policy, userRepo));

        assertEquals("collectionOfPublicationsRepository cannot be null", ex.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenPolicyIsNull() {
        // Arrange
        ICollectionOfPublicationsRepository repo = mock(ICollectionOfPublicationsRepository.class);
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);

        // Act + Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new CollectionOfPublicationsService(repo, factory, null, userRepository));

        assertEquals("CollectionOfPublicationsAuthorizationPolicy cannot be null", ex.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenUserRepositoryIsNull() {
        // Arrange
        ICollectionOfPublicationsRepository repo = mock(ICollectionOfPublicationsRepository.class);
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);

        // Act + Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new CollectionOfPublicationsService(repo, factory, policy, null));

        assertEquals("IUserRepository cannot be null", ex.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenFactoryIsNull() {
        // Arrange
        ICollectionOfPublicationsRepository repo = mock(ICollectionOfPublicationsRepository.class);
        CollectionOfPublicationsFactory factory = null;
        CollectionOfPublicationsAuthorizationPolicy policy = mock(CollectionOfPublicationsAuthorizationPolicy.class);
        IUserRepository userRepo = mock(IUserRepository.class);

        // Act + Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new CollectionOfPublicationsService(repo, factory, policy, userRepo));

        assertEquals("CollectionOfPublicationsFactory cannot be null", ex.getMessage());
    }

    @Test
    void constructorShouldCreateInstanceWhenArgumentsAreValid() {

        // Arrange
        ICollectionOfPublicationsRepository repo = mock(ICollectionOfPublicationsRepository.class);
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);

        // Act
        CollectionOfPublicationsService service =
                new CollectionOfPublicationsService(repo, factory, policy, userRepository);

        // Assert
        assertNotNull(service);
    }

    // ===============================================================
    // --------------------- CREATE AND SAVE -------------------------
    // ===============================================================

    @Test
    void createAndSaveShouldReturnACollectionOfPublications()
            throws AccessDeniedException, InstanceAlreadyExistsException {
        // Arrange
        String collectionName = "Works by Frank Herbert";
        User user = mock(User.class);
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canCreateCollectionOfPublications(user)).thenReturn(true);
        when(factory.create(collectionName, genreIdDouble, userIdDouble)).thenReturn(collectionDouble);
        when(collectionOfPublicationsRepository.save(collectionDouble)).thenReturn(collectionDouble);

        // Act
        CollectionOfPublications result = service.createAndSave(collectionName, genreIdDouble, userIdDouble);

        // Assert
        assertEquals(collectionDouble, result);
    }

    @Test
    void createAndSaveShouldThrowIllegalArgumentExceptionWhenUserNotFound() {
        // Arrange
        String collectionName = "Works by Frank Herbert";
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.createAndSave(collectionName, genreIdDouble, userIdDouble));
    }

    @Test
    void createAndSaveShouldThrowAccessDeniedExceptionWhenUserLacksPermission() {
        // Arrange
        String collectionName = "Works by Frank Herbert";
        User user = mock(User.class);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canCreateCollectionOfPublications(user)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> service.createAndSave(collectionName, genreIdDouble, userIdDouble));
    }

    // ===========================================================================
    // ------------------------- MAKE COLLECTION PUBLIC -------------------------
    // ============================================================================

    @Test
    void makeCollectionPublic_shouldReturnTrueAndUpdate_whenCollectionWasPrivate()
            throws InstanceNotFoundException, AccessDeniedException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        User user = mock(User.class);

        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canShareCollectionOfPublications(user)).thenReturn(true);
        when(collection.makePublic()).thenReturn(true);

        // Act
        boolean result = service.makeCollectionPublic(collectionId, userIdDouble);

        // Assert
        assertTrue(result);
        verify(collectionOfPublicationsRepository).update(collection);
    }

    @Test
    void makeCollectionPublic_shouldThrowIllegalArgumentException_whenUserNotFound()
            throws InstanceNotFoundException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.makeCollectionPublic(collectionId, userIdDouble));
    }

    @Test
    void makeCollectionPublic_shouldThrowAccessDeniedException_whenUserLacksPermission()
            throws InstanceNotFoundException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        User user = mock(User.class);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canShareCollectionOfPublications(user)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> service.makeCollectionPublic(collectionId, userIdDouble));
    }

    @Test
    void makeCollectionPublic_shouldReturnFalseAndNotUpdate_whenCollectionWasAlreadyPublic()
            throws InstanceNotFoundException, AccessDeniedException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        User user = mock(User.class);

        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canShareCollectionOfPublications(user)).thenReturn(true);
        when(collection.makePublic()).thenReturn(false);

        // Act
        boolean result = service.makeCollectionPublic(collectionId, userIdDouble);

        // Assert
        assertFalse(result);
        verify(collectionOfPublicationsRepository, never()).update(any(CollectionOfPublications.class));
    }

    @Test
    void makeCollectionPublic_shouldThrow_whenCollectionIdIsNull() {

        // act + assert
        NullPointerException ex = assertThrows(NullPointerException.class, () -> service.makeCollectionPublic
                (null, userIdDouble));

        assertEquals("CollectionOfPublicationsId cannot be null", ex.getMessage());
    }

    // ===========================================================================
    // ---------------------- MAKE COLLECTION PUBLIC UNTIL-------------------------
    // ============================================================================

    @Test
    void makeCollectionPublicUntil_shouldReturnTrueAndUpdate_whenCollectionWasChanged()
            throws InstanceNotFoundException, AccessDeniedException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        User user = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canMakeCollectionPublicUntil(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(collection.makePublicUntil(publicUntil)).thenReturn(true);

        // Act
        boolean result = service.makeCollectionPublicUntil(collectionId, publicUntil, userIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void makeCollectionPublicUntil_shouldReturnFalseAndNotUpdate_whenCollectionWasNotChanged()
            throws InstanceNotFoundException, AccessDeniedException {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        User user = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canMakeCollectionPublicUntil(user)).thenReturn(true);

        when(collectionOfPublicationsRepository.findById(collectionId))
                .thenReturn(collection);
        when(collection.makePublicUntil(publicUntil))
                .thenReturn(false);

        // Act
        boolean result = service.makeCollectionPublicUntil(collectionId, publicUntil, userIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void makeCollectionPublicUntil_shouldThrowException_whenPublicUntilIsNull() {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        LocalDateTime publicUntil = null;

        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> service.makeCollectionPublicUntil(collectionId, publicUntil, userIdDouble)
        );

        // Assert
        assertEquals("Public until date cannot be null", exception.getMessage());
    }

    @Test
    void makeCollectionPublicUntil_shouldThrowException_whenCollectionIdIsNull() {
        // Arrange
        CollectionOfPublicationsId collectionId = null;
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> service.makeCollectionPublicUntil(collectionId, publicUntil, userIdDouble)
        );

        // Assert
        assertEquals("CollectionOfPublications Id cannot be null", exception.getMessage());
    }

    @Test
    void makeCollectionPublicUntil_shouldThrowIllegalArgumentException_whenUserNotFound() {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.makeCollectionPublicUntil(collectionId, publicUntil, userIdDouble));
    }

    @Test
    void makeCollectionPublicUntil_shouldThrowAccessDeniedException_whenUserLacksPermission() {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        User user = mock(User.class);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canMakeCollectionPublicUntil(user)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> service.makeCollectionPublicUntil(collectionId, publicUntil, userIdDouble));
    }

    @Test
    void makeCollectionPublicUntil_shouldCallUpdate_whenCollectionWasChanged()
            throws InstanceNotFoundException, AccessDeniedException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        User user = mock(User.class);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canMakeCollectionPublicUntil(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(collection.makePublicUntil(publicUntil)).thenReturn(true);

        // Act
        service.makeCollectionPublicUntil(collectionId, publicUntil, userIdDouble);

        // Assert
        verify(collectionOfPublicationsRepository).update(collection);
    }

    // ===============================================================
// ------------------------- FIND ALL ----------------------------
// ===============================================================

    @Test
    void findAll_shouldReturnCollectionList_whenUserHasCollections() {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(collectionOfPublicationsRepository.findAllByOwnerId(userIdDouble))
                .thenReturn(List.of(collection));

        // Act
        List<CollectionOfPublications> result = service.findAllByOwnerId(userIdDouble);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void findAll_shouldReturnEmptyList_whenUserHasNoCollections() {
        // Arrange
        when(collectionOfPublicationsRepository.findAllByOwnerId(userIdDouble))
                .thenReturn(List.of());

        // Act
        List<CollectionOfPublications> result = service.findAllByOwnerId(userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ===============================================================
// ------------------------- FIND BY ID --------------------------
// ===============================================================

    @Test
    void findById_shouldReturnCollection_whenCollectionExists()
            throws InstanceNotFoundException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);

        // Act
        CollectionOfPublications result = service.findById(collectionId);

        // Assert
        assertEquals(collection, result);
    }

    @Test
    void findById_shouldThrowInstanceNotFoundException_whenCollectionDoesNotExist()
            throws InstanceNotFoundException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        when(collectionOfPublicationsRepository.findById(collectionId))
                .thenThrow(InstanceNotFoundException.class);

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> service.findById(collectionId));
    }

// ===============================================================
// ------------------------- EXISTS BY ID ------------------------
// ===============================================================

    @Test
    void existsById_shouldReturnTrue_whenCollectionExists() {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        when(collectionOfPublicationsRepository.existsById(collectionId)).thenReturn(true);

        // Act
        boolean result = service.existsById(collectionId);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenCollectionDoesNotExist() {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        when(collectionOfPublicationsRepository.existsById(collectionId)).thenReturn(false);

        // Act
        boolean result = service.existsById(collectionId);

        // Assert
        assertFalse(result);
    }

// ==========================================================================
// ------------------- FIND PUBLIC COLLECTION BY GENRE ID -------------------
// ==========================================================================

    @Test
    void findPublicCollectionsByGenreId_shouldReturnCollections_whenCollectionsExist() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(collectionOfPublicationsRepository.findPublicCollectionsByGenreId(genreIdDouble))
                .thenReturn(List.of(collection));

        // Act
        List<CollectionOfPublications> result =
                service.findPublicCollectionsByGenreId(genreIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals(collection, result.get(0));
    }

    @Test
    void findPublicCollectionsByGenreId_shouldReturnEmptyList_whenNoCollectionsExist() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(collectionOfPublicationsRepository.findPublicCollectionsByGenreId(genreIdDouble))
                .thenReturn(List.of());

        // Act
        List<CollectionOfPublications> result =
                service.findPublicCollectionsByGenreId(genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }
}