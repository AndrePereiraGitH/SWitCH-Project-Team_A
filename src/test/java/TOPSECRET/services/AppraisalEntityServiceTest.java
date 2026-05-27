package TOPSECRET.services;


import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AppraisalEntityAuthorizationPolicy;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
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

    @Mock private IAppraisalEntityRepository repository;
    @Mock private AppraisalEntityFactory factory;
    @Mock private AppraisalEntityAuthorizationPolicy policy;
    @Mock private IUserRepository userRepo;

    @InjectMocks
    private AppraisalEntityService sut;

    // Helpers
    private UserId userId()              { return mock(UserId.class); }
    private AppraisalEntityId entityId() { return mock(AppraisalEntityId.class); }
    private Contact contact()            { return mock(Contact.class); }
    private List<PublicationTypeId> pubs()   { return List.of(mock(PublicationTypeId.class)); }
    private List<GenreId> genres()       { return List.of(mock(GenreId.class)); }
    private AppraisalEntity entity()     { return mock(AppraisalEntity.class); }
    private User user()                  { return mock(User.class); }


    @Test
    void createAndSave_shouldReturnSavedEntity_whenUserIsAdmin() throws Exception {
        // ARRANGE
        User user = user();
        AppraisalEntity entity = entity();
        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);
        when(factory.create(anyString(), any(), anyList(), anyList())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        // ACT
        AppraisalEntity result = sut.createAndSave("name", contact(), pubs(), genres(), userId());

        // ASSERT
        assertEquals(entity, result);
    }

    @Test
    void createAndSave_shouldThrowAccessDenied_whenUserIsNotAdmin() {
        // ARRANGE
        User user = user();
        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class, () ->
                sut.createAndSave("name", contact(), pubs(), genres(), userId())
        );
    }

    @Test
    void createAndSave_shouldThrowNoSuchElement_whenUserNotFound() {
        // ARRANGE
        when(userRepo.findByID(userId())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(NoSuchElementException.class, () ->
                sut.createAndSave("name", contact(), pubs(), genres(), userId())
        );
    }

    @Test
    void update_shouldReturnUpdatedEntity_whenUserIsAdmin() throws Exception {
        // ARRANGE
        User user = user();
        AppraisalEntity entity = entity();
        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);
        when(repository.findById(entityId())).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        // ACT
        AppraisalEntity result = sut.update(entityId(), "name", contact(), pubs(), genres(), userId());

        // ASSERT
        assertEquals(entity, result);
    }

    @Test
    void update_shouldThrowAccessDenied_whenUserIsNotAdmin() {
        // ARRANGE
        User user = user();
        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class, () ->
                sut.update(entityId(), "name", contact(), pubs(), genres(), userId())
        );
    }

    @Test
    void update_shouldThrowNoSuchElement_whenUserNotFound() {
        // ARRANGE
        when(userRepo.findByID(userId())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(NoSuchElementException.class, () ->
                sut.update(entityId(), "name", contact(), pubs(), genres(), userId())
        );
    }

    @Test
    void update_shouldThrowIllegalArgument_whenIdIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.update(null, "name", contact(), pubs(), genres(), userId())
        );
    }

    @Test
    void update_shouldThrowIllegalArgument_whenEntityNotFound() {
        // ARRANGE
        User user = user();
        when(userRepo.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);
        when(repository.findById(entityId())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.update(entityId(), "name", contact(), pubs(), genres(), userId())
        );
    }

    @Test
    void findById_shouldReturnEntity_whenFound() {
        // ARRANGE
        AppraisalEntity entity = entity();
        when(repository.findById(entityId())).thenReturn(Optional.of(entity));

        // ACT
        Optional<AppraisalEntity> result = sut.findById(entityId());

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        // ARRANGE
        when(repository.findById(entityId())).thenReturn(Optional.empty());

        // ACT
        Optional<AppraisalEntity> result = sut.findById(entityId());

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldThrowIllegalArgument_whenIdIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> sut.findById(null));
    }

    @Test
    void findAll_shouldReturnAllEntities() {
        // ARRANGE
        AppraisalEntity entity = entity();
        when(repository.findAll()).thenReturn(List.of(entity));

        // ACT
        List<AppraisalEntity> result = sut.findAll();

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(entity, result.get(0));
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoEntitiesExist() {
        // ARRANGE
        when(repository.findAll()).thenReturn(List.of());

        // ACT
        List<AppraisalEntity> result = sut.findAll();

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void existsById_shouldReturnTrue_whenEntityExists() {
        // ARRANGE
        when(repository.existsById(entityId())).thenReturn(true);

        // ACT
        boolean result = sut.existsById(entityId());

        // ASSERT
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenEntityDoesNotExist() {
        // ARRANGE
        when(repository.existsById(entityId())).thenReturn(false);

        // ACT
        boolean result = sut.existsById(entityId());

        // ASSERT
        assertFalse(result);
    }

    @Test
    void existsById_shouldThrowIllegalArgument_whenIdIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> sut.existsById(null));
    }

    @Test
    void constructor_shouldThrowIllegalArgument_whenRepositoryIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntityService(null, factory, policy, userRepo)
        );
    }

    @Test
    void constructor_shouldThrowIllegalArgument_whenFactoryIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntityService(repository, null, policy, userRepo)
        );
    }

    @Test
    void constructor_shouldThrowIllegalArgument_whenPolicyIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntityService(repository, factory, null, userRepo)
        );
    }

    @Test
    void constructor_shouldThrowIllegalArgument_whenUserRepositoryIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntityService(repository, factory, policy, null)
        );
    }
}