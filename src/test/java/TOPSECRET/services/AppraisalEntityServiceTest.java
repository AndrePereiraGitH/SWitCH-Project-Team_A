package TOPSECRET.services;


import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AppraisalEntityAuthorizationPolicy;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityService}.
 *
 * Tests follow the AAA pattern and cover:
 * - successful creation and update
 * - access denied when user is not ADMIN
 * - validation guards (null id, entity not found)
 */
@ExtendWith(MockitoExtension.class)
class AppraisalEntityServiceTest {

    @Mock
    private IAppraisalEntityRepository repository;

    @Mock
    private AppraisalEntityFactory factory;

    @Mock
    private AppraisalEntityAuthorizationPolicy policy;

    @Mock
    private IUserRepository userRepo;

    @InjectMocks
    private AppraisalEntityService service;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private UserId userId() { return new UserId("user-1"); }
    private AppraisalEntityId entityId() { return new AppraisalEntityId("entity-1"); }
    private Contact contact() { return mock(Contact.class); }
    private List<PublicationTypeId> pubs() { return List.of(mock(PublicationTypeId.class)); }
    private List<GenreId> genres() { return List.of(mock(GenreId.class)); }
    private AppraisalEntity entity() { return mock(AppraisalEntity.class); }
    private User user() { return mock(User.class); }

    @Test
    void createAndSave_shouldReturnSavedEntityWhenUserIsAdmin() throws Exception {
        // ARRANGE
        User user = user();
        AppraisalEntity entity = entity();

        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);
        when(factory.create(anyString(), any(), anyList(), anyList())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        // ACT
        AppraisalEntity result = service.createAndSave("name", contact(), pubs(), genres(), userId());

        // ASSERT
        assertEquals(entity, result);
    }

    @Test
    void createAndSave_shouldCallRepositorySaveWhenUserIsAdmin() throws Exception {
        // ARRANGE
        User user = user();
        AppraisalEntity entity = entity();

        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);
        when(factory.create(anyString(), any(), anyList(), anyList())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        // ACT
        service.createAndSave("name", contact(), pubs(), genres(), userId());

        // ASSERT
        verify(repository, times(1)).save(entity);
    }

    @Test
    void createAndSave_shouldThrowAccessDeniedWhenUserIsNotAdmin() {
        // ARRANGE
        User user = user();

        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class, () ->
                service.createAndSave("name", contact(), pubs(), genres(), userId())
        );
    }

    @Test
    void createAndSave_shouldNotCallFactoryWhenUserIsNotAdmin() {
        // ARRANGE
        User user = user();

        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(false);

        // ACT
        assertThrows(AccessDeniedException.class, () ->
                service.createAndSave("name", contact(), pubs(), genres(), userId())
        );

        // ASSERT
        verify(factory, never()).create(any(), any(), anyList(), anyList());
    }

    @Test
    void createAndSave_shouldNotCallRepositoryWhenUserIsNotAdmin() {
        // ARRANGE
        User user = user();

        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(false);

        // ACT
        assertThrows(AccessDeniedException.class, () ->
                service.createAndSave("name", contact(), pubs(), genres(), userId())
        );

        // ASSERT
        verify(repository, never()).save(any());
    }

    @Test
    void update_shouldReturnSavedEntityWhenUserIsAdmin() throws Exception {
        // ARRANGE
        User user = user();
        AppraisalEntity entity = entity();

        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);
        when(repository.findById(entityId())).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        // ACT
        AppraisalEntity result = service.update(entityId(), "name", contact(), pubs(), genres(), userId());

        // ASSERT
        assertEquals(entity, result);
    }

    @Test
    void update_shouldCallRepositorySaveWhenUserIsAdmin() throws Exception {
        // ARRANGE
        User user = user();
        AppraisalEntity entity = entity();

        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);
        when(repository.findById(entityId())).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        // ACT
        service.update(entityId(), "name", contact(), pubs(), genres(), userId());

        // ASSERT
        verify(repository, times(1)).save(entity);
    }

    @Test
    void update_shouldThrowAccessDeniedWhenUserIsNotAdmin() {
        // ARRANGE
        User user = user();

        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class, () ->
                service.update(entityId(), "name", contact(), pubs(), genres(), userId())
        );
    }

    @Test
    void update_shouldNotCallRepositorySaveWhenUserIsNotAdmin() {
        // ARRANGE
        User user = user();

        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(false);

        // ACT
        assertThrows(AccessDeniedException.class, () ->
                service.update(entityId(), "name", contact(), pubs(), genres(), userId())
        );

        // ASSERT
        verify(repository, never()).save(any());
    }

    @Test
    void update_shouldThrowIllegalArgumentWhenIdIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                service.update(null, "name", contact(), pubs(), genres(), userId())
        );
    }

    @Test
    void update_shouldThrowIllegalArgumentWhenEntityNotFound() {
        // ARRANGE
        User user = user();

        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);
        when(repository.findById(entityId())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                service.update(entityId(), "name", contact(), pubs(), genres(), userId())
        );
    }

    @Test
    void update_shouldNotCallRepositorySaveWhenEntityNotFound() {
        // ARRANGE
        User user = user();

        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);
        when(repository.findById(entityId())).thenReturn(Optional.empty());

        // ACT
        assertThrows(IllegalArgumentException.class, () ->
                service.update(entityId(), "name", contact(), pubs(), genres(), userId())
        );

        // ASSERT
        verify(repository, never()).save(any());
    }

    @Test
    void findById_shouldReturnEntityWhenFound() {
        // ARRANGE
        AppraisalEntity entity = entity();
        when(repository.findById(entityId())).thenReturn(Optional.of(entity));

        // ACT
        Optional<AppraisalEntity> result = service.findById(entityId());

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        // ARRANGE
        when(repository.findById(entityId())).thenReturn(Optional.empty());

        // ACT
        Optional<AppraisalEntity> result = service.findById(entityId());

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldThrowIllegalArgumentWhenIdIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
    }

    @Test
    void findById_shouldNotCallRepositoryWhenIdIsNull() {
        // ACT
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));

        // ASSERT
        verify(repository, never()).findById(any());
    }

    @Test
    void findAll_shouldReturnAllEntities() {
        // ARRANGE
        AppraisalEntity entity = entity();
        when(repository.findAll()).thenReturn(List.of(entity));

        // ACT
        List<AppraisalEntity> result = service.findAll();

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(entity, result.get(0));
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoEntitiesExist() {
        // ARRANGE
        when(repository.findAll()).thenReturn(List.of());

        // ACT
        List<AppraisalEntity> result = service.findAll();

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void existsById_shouldReturnTrueWhenEntityExists() {
        // ARRANGE
        when(repository.existsById(entityId())).thenReturn(true);

        // ACT
        boolean result = service.existsById(entityId());

        // ASSERT
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalseWhenEntityDoesNotExist() {
        // ARRANGE
        when(repository.existsById(entityId())).thenReturn(false);

        // ACT
        boolean result = service.existsById(entityId());

        // ASSERT
        assertFalse(result);
    }

    @Test
    void existsById_shouldThrowIllegalArgumentWhenIdIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> service.existsById(null));
    }

    @Test
    void existsById_shouldNotCallRepositoryWhenIdIsNull() {
        // ACT
        assertThrows(IllegalArgumentException.class, () -> service.existsById(null));

        // ASSERT
        verify(repository, never()).existsById(any());
    }


    @Test
    void constructor_shouldThrowWhenRepositoryIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntityService(null, factory, policy, userRepo)
        );
    }

    @Test
    void constructor_shouldThrowWhenFactoryIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntityService(repository, null, policy, userRepo)
        );
    }

    @Test
    void constructor_shouldThrowWhenPolicyIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntityService(repository, factory, null, userRepo)
        );
    }

    @Test
    void constructor_shouldThrowWhenUserRepositoryIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntityService(repository, factory, policy, null)
        );
    }
}