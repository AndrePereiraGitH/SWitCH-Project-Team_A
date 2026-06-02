package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CollectionOfPublicationsAuthorizationPolicy;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.collectionOfPublications.response.PublicCollectionOfPublicationsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Mock
    UserId userIdDouble;

    @Mock
    UserId ownerIdDouble;

    @InjectMocks
    CollectionOfPublicationsService service;

    private GenreId genreIdDouble;

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
    void createAndSave_shouldThrowNullPointerException_whenCollectionNameIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave(null, genreIdDouble, userIdDouble));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenGenreIdIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave("My Collection", null, userIdDouble));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenUserIdIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave("My Collection", genreIdDouble, null));
    }


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
    void createAndSaveShouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        // Arrange
        String collectionName = "Works by Frank Herbert";
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UsernameNotFoundException.class,
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
    void makeCollectionPublic_shouldReturnTrue_whenCollectionWasPrivate()
            throws InstanceNotFoundException, AccessDeniedException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        User user = mock(User.class);

        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canShareCollectionOfPublications(user)).thenReturn(true);
        when(collection.getOwnerId()).thenReturn(userIdDouble);
        when(collection.makePublic()).thenReturn(true);

        // Act
        boolean result = service.makeCollectionPublic(collectionId, userIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void makeCollectionPublic_shouldThrowAccessDeniedException_whenUserDoesNotOwnCollection()
            throws InstanceNotFoundException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        User user = mock(User.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        UserId ownerIdDouble = mock(UserId.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canShareCollectionOfPublications(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(collection.getOwnerId()).thenReturn(ownerIdDouble);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> service.makeCollectionPublic(collectionId, userIdDouble));
    }

    @Test
    void makeCollectionPublicShouldThrowUsernameNotFoundExceptionWhenUserNotFound()
            throws InstanceNotFoundException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UsernameNotFoundException.class,
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
    void makeCollectionPublic_shouldReturnFalse_whenCollectionWasAlreadyPublic()
            throws InstanceNotFoundException, AccessDeniedException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        User user = mock(User.class);

        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canShareCollectionOfPublications(user)).thenReturn(true);
        when(collection.getOwnerId()).thenReturn(userIdDouble);
        when(collection.makePublic()).thenReturn(false);

        // Act
        boolean result = service.makeCollectionPublic(collectionId, userIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void makeCollectionPublic_shouldThrow_whenCollectionIdIsNull() {

        // act + assert
        NullPointerException ex = assertThrows(NullPointerException.class, () -> service.makeCollectionPublic
                (null, userIdDouble));

        assertEquals("CollectionOfPublicationsId cannot be null", ex.getMessage());
    }

    @Test
    void makeCollectionPublic_shouldThrowAccessDenied_whenUserIsNotOwner() throws InstanceNotFoundException {

        // ARRANGE
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);

        User user = mock(User.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));

        when(policy.canShareCollectionOfPublications(user)).thenReturn(true);

        when(collectionOfPublicationsRepository.findById(collectionId))
                .thenReturn(collection);

        when(collection.getOwnerId()).thenReturn(ownerIdDouble);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> service.makeCollectionPublic(collectionId, userIdDouble));
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
        when(collection.getOwnerId()).thenReturn(userIdDouble);
        when(collection.makePublicUntil(publicUntil)).thenReturn(true);

        // Act
        boolean result = service.makeCollectionPublicUntil(collectionId, publicUntil, userIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void makeCollectionPublicUntil_shouldThrowIllegalArgumentException_whenPublicUntilIsNotInFuture() {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        LocalDateTime publicUntil = LocalDateTime.now().minusDays(1);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.makeCollectionPublicUntil(collectionId, publicUntil, userIdDouble));

        assertEquals("Public until date must be in the future", ex.getMessage());
    }

    @Test
    void makeCollectionPublicUntil_shouldThrowAccessDeniedException_whenUserDoesNotOwnCollection()
            throws InstanceNotFoundException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        User user = mock(User.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        UserId ownerIdDouble = mock(UserId.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canMakeCollectionPublicUntil(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(collection.getOwnerId()).thenReturn(ownerIdDouble);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> service.makeCollectionPublicUntil(collectionId, publicUntil, userIdDouble));
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
        when(collection.getOwnerId()).thenReturn(userIdDouble);
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
    void makeCollectionPublicUntilShouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UsernameNotFoundException.class,
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
    void makeCollectionPublicUntil_shouldReturnTrue_whenCollectionWasChanged()
            throws InstanceNotFoundException, AccessDeniedException {
        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        User user = mock(User.class);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));
        when(policy.canMakeCollectionPublicUntil(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findById(collectionId)).thenReturn(collection);
        when(collection.getOwnerId()).thenReturn(userIdDouble);
        when(collection.makePublicUntil(publicUntil)).thenReturn(true);

        // Act
        boolean result = service.makeCollectionPublicUntil(collectionId, publicUntil, userIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void makeCollectionPublicUntil_shouldThrowAccessDenied_whenUserIsNotOwner() throws InstanceNotFoundException {

        // ARRANGE
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);

        LocalDateTime future = LocalDateTime.now().plusDays(2);

        User user = mock(User.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(user));

        when(policy.canMakeCollectionPublicUntil(user)).thenReturn(true);

        when(collectionOfPublicationsRepository.findById(collectionId))
                .thenReturn(collection);

        when(collection.getOwnerId()).thenReturn(ownerIdDouble);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> service.makeCollectionPublicUntil(collectionId, future, userIdDouble));
    }

    // ===============================================================
// ------------------------- FIND ALL ----------------------------
// ===============================================================

    @Test
    void findAll_shouldThrowNullPointerException_whenOwnerIdIsNull() {
        // Act + Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> service.findAllByOwnerId(null));

        assertEquals("ownerId cannot be null", ex.getMessage());
    }

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
    void existsById_shouldThrowNullPointerException_whenCollectionIdIsNull() {
        // Act + Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> service.existsById(null));

        assertEquals("collectionOfPublicationsId cannot be null", ex.getMessage());
    }

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
// ------------------- GET USER NAME -------------------
// ==========================================================================

    @Test
    void getUserName_ShouldReturnUserName_WhenUserExists() {

        // ARRANGE
        UserId userIdDouble = mock(UserId.class);

        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble))
                .thenReturn(Optional.of(userDouble));

        when(userDouble.getUserName())
                .thenReturn("john_doe");

        // ACT
        String result = service.getUserName(userIdDouble);

        // ASSERT
        assertEquals("john_doe", result);
    }

    @Test
    void getUserName_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {

        // ARRANGE
        UserId userIdDouble = mock(UserId.class);

        when(userRepository.findByID(userIdDouble))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                UsernameNotFoundException.class,
                () -> service.getUserName(userIdDouble)
        );
    }

    @Test
    void getUserName_ShouldThrowNullPointerException_WhenUserIdIsNull() {

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> service.getUserName(null)
        );
    }

// ==========================================================================
// ------------------- FIND PUBLIC COLLECTION BY GENRE ID -------------------
// ==========================================================================

    @Test
    void findPublicCollectionsByGenreId_shouldThrowNullPointerException_whenGenreIdIsNull() {
        // Act + Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> service.findPublicCollectionsByGenreId(null));

        assertEquals("genreId cannot be null", ex.getMessage());
    }

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

// ==========================================================================
// ------------------- FIND ALL PUBLIC COLLECTIONS --------------------------
// ==========================================================================

    @Test
    void findAllPublicCollections_shouldReturnOnlyPublicDTOs() {

        // ARRANGE
        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
        CollectionOfPublications c2 = mock(CollectionOfPublications.class);

        UserId owner1 = mock(UserId.class);
        UserId owner2 = mock(UserId.class);

        User userMock = mock(User.class);

        CollectionOfPublicationsId id1 = mock(CollectionOfPublicationsId.class);
        CollectionOfPublicationsId id2 = mock(CollectionOfPublicationsId.class);

        when(c1.id()).thenReturn(id1);
        when(c2.id()).thenReturn(id2);

        when(id1.toString()).thenReturn("col-1");
        when(id2.toString()).thenReturn("col-2");

        when(c1.isCurrentlyPublic()).thenReturn(true);
        when(c2.isCurrentlyPublic()).thenReturn(false);

        when(c1.getOwnerId()).thenReturn(owner1);
        when(c2.getOwnerId()).thenReturn(owner2);

        when(collectionOfPublicationsRepository.findAll())
                .thenReturn(List.of(c1, c2));

        when(userRepository.findByID(owner1)).thenReturn(Optional.of(userMock));
        when(userRepository.findByID(owner2)).thenReturn(Optional.of(userMock));

        when(userMock.getUserName()).thenReturn("owner-1");

        CollectionOfPublicationsService service =
                new CollectionOfPublicationsService(
                        collectionOfPublicationsRepository,
                        factory,
                        policy,
                        userRepository
                );

        // ACT
        List<PublicCollectionOfPublicationsDTO> result =
                service.findAllPublicCollections();

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("owner-1", result.get(0).getOwnerName());
    }

    @Test
    void findAllPublicCollections_shouldReturnEmptyList_whenNoPublicCollections() {

        when(collectionOfPublicationsRepository.findAll())
                .thenReturn(List.of());

        CollectionOfPublicationsService service =
                new CollectionOfPublicationsService(
                        collectionOfPublicationsRepository,
                        factory,
                        policy,
                        userRepository
                );

        List<PublicCollectionOfPublicationsDTO> result =
                service.findAllPublicCollections();

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllPublicCollections_shouldThrow_whenRepositoryReturnsNull() {

        when(collectionOfPublicationsRepository.findAll())
                .thenReturn(null);

        CollectionOfPublicationsService service =
                new CollectionOfPublicationsService(
                        collectionOfPublicationsRepository,
                        factory,
                        policy,
                        userRepository
                );

        assertThrows(NullPointerException.class,
                service::findAllPublicCollections);
    }

// ==========================================================================
// ------------------- FIND ALL COLLECTIONS ---------------------------------
// ==========================================================================

    @Test
    void findAllCollections_shouldReturnAllCollections_whenUserIsAdmin() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        when(policy.canViewAllCollections(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findAll())
                .thenReturn(List.of(collection));

        // ACT
        List<CollectionOfPublications> result =
                service.findAllCollections(userId);

        // ASSERT
        assertEquals(1, result.size());
        assertTrue(result.contains(collection));
    }

    @Test
    void findAllCollections_shouldThrowAccessDeniedException_whenUserIsNotAdmin() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        when(policy.canViewAllCollections(user)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> service.findAllCollections(userId));
    }

    @Test
    void findAllCollections_shouldThrowNullPointerException_whenUserIdIsNull() {

        assertThrows(NullPointerException.class,
                () -> service.findAllCollections(null));
    }

    @Test
    void findAllCollections_shouldReturnEmptyList_whenNoCollectionsExist() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        when(policy.canViewAllCollections(user)).thenReturn(true);
        when(collectionOfPublicationsRepository.findAll())
                .thenReturn(List.of());

        // ACT
        List<CollectionOfPublications> result =
                service.findAllCollections(userId);

        // ASSERT
        assertTrue(result.isEmpty());
    }
}