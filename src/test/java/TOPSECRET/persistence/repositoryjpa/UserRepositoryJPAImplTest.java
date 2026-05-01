package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.assembler.UserAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.UserDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IUserRepositoryJPA;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        // Act
        UserRepositoryJPAImpl result = new UserRepositoryJPAImpl(jpaRepoDouble, assemblerDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void constructor_shouldThrowException_whenJpaRepositoryIsNull() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> new UserRepositoryJPAImpl(null, assemblerDouble)
        );
    }

    @Test
    void constructor_shouldThrowException_whenAssemblerIsNull() {
        // Act & Assert
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
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> repo.save(null)
        );
    }

    // ----------------- exists() Tests -----------------

    @Test
    void exists_shouldReturnTrue_whenUserExistsByUsername() {
        // Arrange
        UserDM userDMDouble = mock(UserDM.class);
        when(jpaRepoDouble.findByUserName("Rodolph"))
                .thenReturn(Optional.of(userDMDouble));

        // Act
        boolean result = repo.existsByUsername("Rodolph");

        // Assert
        assertTrue(result);
    }

    @Test
    void exists_ByUsername_shouldReturnFalse_whenUserDoesNotExist() {
        // Arrange
        when(jpaRepoDouble.findByUserName("Rodolph"))
                .thenReturn(Optional.empty());

        // Act
        boolean result = repo.existsByUsername("Rodolph");

        // Assert
        assertFalse(result);
    }

    @Test
    void exists_ByUsername_shouldThrowException_whenUsernameIsNull() {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.existsByUsername(null)
        );
    }

    @Test
    void exists_ByUsername_shouldThrowException_whenUsernameIsBlank() {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.existsByUsername("   ")
        );
    }

    // ----------------- findUserByID() Tests -----------------

    @Test
    void findUserByID_shouldReturn_whenFound() {
        // Arrange
        UserId userId = mock(UserId.class);
        UserDM userDMDouble = mock(UserDM.class);
        User expectedUser = mock(User.class);

        when(jpaRepoDouble.findById(userId)).thenReturn(Optional.of(userDMDouble));
        when(assemblerDouble.toDomain(userDMDouble)).thenReturn(expectedUser);

        // Act
        Optional<User> result = repo.findByID(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    void findByID_shouldReturnEmpty_whenNotFound() {
        // Arrange
        UserId userId = mock(UserId.class);
        when(jpaRepoDouble.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = repo.findByID(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findUserByID_shouldThrowException_whenIdIsNull() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> repo.findByID(null)
        );
    }

    // ----------------- findUserIdByUsername() Tests -----------------

    @Test
    void findUserIdByUsername_shouldReturnUserId_whenUserExistsByUsername() {
        // Arrange
        UserDM userDMDouble = mock(UserDM.class);
        User userDouble = mock(User.class);
        UserId userIdDouble = mock(UserId.class);

        when(jpaRepoDouble.findByUserName("Ines"))
                .thenReturn(Optional.of(userDMDouble));
        when(assemblerDouble.toDomain(userDMDouble))
                .thenReturn(userDouble);
        when(userDouble.id())
                .thenReturn(userIdDouble);

        // Act
        Optional<UserId> result = repo.findUserIdByUsername("Ines");

        // Assert
        assertTrue(result.isPresent());
        assertSame(userIdDouble, result.get());
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
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.findUserIdByUsername(null)
        );
    }

    @Test
    void findUserIdByUsername_shouldThrowException_whenUsernameIsBlank() {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.findUserIdByUsername("   ")
        );
    }
}