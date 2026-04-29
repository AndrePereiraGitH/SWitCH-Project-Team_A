package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.assembler.UserAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.UserDM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Unit tests for {@link UserRepositoryJPAImpl}.
 *
 * <p>
 * Verifies persistence, existence checks, and lookup operations,
 * ensuring correct delegation to {@link IUserRepositoryJPA} and
 * mapping via {@link UserAssembler}.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class UserRepositoryJPAImplTest {

    @Mock
    private IUserRepositoryJPA jpaRepoDouble;

    @Mock
    private UserAssembler assemblerDouble;

    @InjectMocks
    private UserRepositoryJPAImpl repo;

    // ----------------- Constructor Tests -----------------

    @Test
    void constructor_shouldCreateInstance_whenValidArguments() {
        // Arrange

        // Act
        UserRepositoryJPAImpl result = new UserRepositoryJPAImpl(jpaRepoDouble, assemblerDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void constructor_shouldThrowException_whenJpaRepositoryIsNull() {
        // Arrange

        // Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> new UserRepositoryJPAImpl(null, assemblerDouble)
        );
    }

    @Test
    void constructor_shouldThrowException_whenAssemblerIsNull() {
        // Arrange

        // Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> new UserRepositoryJPAImpl(jpaRepoDouble, null)
        );
    }

    // ----------------- save() Tests -----------------

    @Test
    void save_shouldReturnDomainUser_whenValidUser() {
        // Arrange
        User userDouble = mock(User.class);
        UserDM userDMDouble = mock(UserDM.class);
        UserDM savedDMDouble = mock(UserDM.class);
        User expectedUser = mock(User.class);

        when(assemblerDouble.toDM(userDouble)).thenReturn(userDMDouble);
        when(jpaRepoDouble.save(userDMDouble)).thenReturn(savedDMDouble);
        when(assemblerDouble.toDomain(savedDMDouble)).thenReturn(expectedUser);

        // Act
        User result = repo.save(userDouble);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUser, result);
    }

    @Test
    void save_shouldThrowException_whenUserIsNull() {
        // Arrange

        // Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> repo.save(null)
        );
    }

    // ----------------- exists() Tests -----------------

    @Test
    void exists_shouldReturnTrue_whenUserExists() {
        // Arrange
        UserDM userDMDouble = mock(UserDM.class);

        when(jpaRepoDouble.findByUserName("Rodolph"))
                .thenReturn(Optional.of(userDMDouble));

        // Act
        boolean result = repo.exists("Rodolph");

        // Assert
        assertTrue(result);
    }

    @Test
    void exists_shouldReturnFalse_whenUserDoesNotExist() {
        // Arrange
        when(jpaRepoDouble.findByUserName("Rodolph"))
                .thenReturn(Optional.empty());

        // Act
        boolean result = repo.exists("Rodolph");

        // Assert
        assertFalse(result);
    }

    @Test
    void exists_shouldThrowException_whenUsernameIsNull() {
        // Arrange

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.exists(null)
        );
    }

    @Test
    void exists_shouldThrowException_whenUsernameIsBlank() {
        // Arrange

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.exists("   ")
        );
    }

    // ----------------- findUserByID() Tests -----------------

    @Test
    void findUserByID_shouldReturnUser_whenFound() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID().toString());
        UserDM userDMDouble = mock(UserDM.class);
        User expectedUser = mock(User.class);

        when(jpaRepoDouble.findById(userId)).thenReturn(Optional.of(userDMDouble));
        when(assemblerDouble.toDomain(userDMDouble)).thenReturn(expectedUser);

        // Act
        Optional<User> result = repo.findUserByID(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    void findUserByID_shouldReturnEmpty_whenNotFound() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID().toString());

        when(jpaRepoDouble.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = repo.findUserByID(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findUserByID_shouldThrowException_whenUserIdIsNull() {
        // Arrange

        // Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> repo.findUserByID(null)
        );
    }

    // ----------------- findUserIdByUsername() Tests -----------------

    @Test
    void findUserIdByUsername_shouldReturnUserId_whenUserExists() {
        // Arrange
        UserDM userDMDouble = mock(UserDM.class);
        UserId userId = new UserId(UUID.randomUUID().toString());

        when(jpaRepoDouble.findByUserName("Rodolph"))
                .thenReturn(Optional.of(userDMDouble));
        when(userDMDouble.getId()).thenReturn(userId);

        // Act
        Optional<UserId> result = repo.findUserIdByUsername("Rodolph");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get());
    }

    @Test
    void findUserIdByUsername_shouldReturnEmpty_whenUserDoesNotExist() {
        // Arrange
        when(jpaRepoDouble.findByUserName("Rodolph"))
                .thenReturn(Optional.empty());

        // Act
        Optional<UserId> result = repo.findUserIdByUsername("Rodolph");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findUserIdByUsername_shouldThrowException_whenUsernameIsNull() {
        // Arrange

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.findUserIdByUsername(null)
        );
    }

    @Test
    void findUserIdByUsername_shouldThrowException_whenUsernameIsBlank() {
        // Arrange

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.findUserIdByUsername("   ")
        );
    }
}