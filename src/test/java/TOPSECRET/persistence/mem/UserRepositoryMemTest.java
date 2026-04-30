package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryMemTest {

    private UserFactory userFactory;

    private Email email1Double;
    private Email email2Double;
    private Contact contact1Double;
    private Contact contact2Double;
    private Profile profile1Double;
    private Profile profile2Double;

    @BeforeEach
    void setUp() {
        email1Double = mock(Email.class);
        when(email1Double.getEmail()).thenReturn("test.user@example.com");

        email2Double = mock(Email.class);
        when(email2Double.getEmail()).thenReturn("duplicate.user@example.com");

        contact1Double = mock(Contact.class);
        when(contact1Double.getEmail()).thenReturn(email1Double);
        when(contact1Double.getAddress()).thenReturn(mock(Address.class));

        contact2Double = mock(Contact.class);
        when(contact2Double.getEmail()).thenReturn(email2Double);
        when(contact2Double.getAddress()).thenReturn(mock(Address.class));

        profile1Double = mock(Profile.class);
        when(profile1Double.getName()).thenReturn("Profile 1");
        when(profile1Double.getContact()).thenReturn(contact1Double);
        when(profile1Double.getBirthday()).thenReturn(LocalDate.of(1997, 8, 15));

        profile2Double = mock(Profile.class);
        when(profile2Double.getName()).thenReturn("Profile 2");
        when(profile2Double.getContact()).thenReturn(contact2Double);
        when(profile2Double.getBirthday()).thenReturn(LocalDate.of(1990, 1, 1));

        userFactory = mock(UserFactory.class);
    }

    private User mockUser(String username) {
        User user = mock(User.class);
        UserId userId = mock(UserId.class);

        when(user.getUserName()).thenReturn(username);
        when(user.id()).thenReturn(userId);

        return user;
    }

    @Test
    void shouldSaveUserSuccessfully() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        User user = mockUser("TestUser");
        when(userFactory.create("TestUser", contact1Double, profile1Double, UserRole.ADMIN))
                .thenReturn(user);

        // Act
        User createdUser = userFactory.create("TestUser", contact1Double, profile1Double, UserRole.ADMIN);

        // Assert
        assertDoesNotThrow(() -> userRepository.save(createdUser));
        assertTrue(userRepository.exists("TestUser"));
    }

    @Test
    void shouldPersistUserAndReturnSameUser() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        User user = mockUser("ReturnTestUser");
        when(userFactory.create("ReturnTestUser", contact1Double, profile1Double, UserRole.NORMAL_USER))
                .thenReturn(user);

        // Act
        User createdUser = userFactory.create("ReturnTestUser", contact1Double, profile1Double, UserRole.NORMAL_USER);
        User result = userRepository.save(createdUser);
        Optional<UserId> foundId = userRepository.findUserIdByUsername("ReturnTestUser");

        // Assert
        assertNotNull(result);
        assertSame(user, result);
        assertTrue(userRepository.exists("ReturnTestUser"));
        assertTrue(foundId.isPresent());
    }

    @Test
    void shouldAllowSavingUsersWithDifferentUsernames() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        User user1 = mockUser("UserOne");
        User user2 = mockUser("UserTwo");

        when(userFactory.create("UserOne", contact1Double, profile1Double, UserRole.ADMIN))
                .thenReturn(user1);
        when(userFactory.create("UserTwo", contact2Double, profile2Double, UserRole.NORMAL_USER))
                .thenReturn(user2);

        // Act
        userRepository.save(userFactory.create("UserOne", contact1Double, profile1Double, UserRole.ADMIN));
        userRepository.save(userFactory.create("UserTwo", contact2Double, profile2Double, UserRole.NORMAL_USER));

        // Assert
        assertTrue(userRepository.exists("UserOne"));
        assertTrue(userRepository.exists("UserTwo"));

        if (userRepository instanceof UserRepositoryMem repoMem) {
            assertEquals(2, repoMem.getUserCount());
        }
    }

    @Test
    void shouldReturnTrueWhenUserExists() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        User user = mockUser("ExistingUser");
        when(userFactory.create("ExistingUser", contact1Double, profile1Double, UserRole.ADMIN))
                .thenReturn(user);

        userRepository.save(userFactory.create("ExistingUser", contact1Double, profile1Double, UserRole.ADMIN));

        // Act
        boolean result = userRepository.exists("ExistingUser");

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExist() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        // Act
        boolean result = userRepository.exists("NonExistingUser");

        // Assert
        assertFalse(result);
    }

    @Test
    void findUserIdByUsername_ShouldReturnValidId_WhenUsernameExists() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        User user = mockUser("ValidUser");
        when(userFactory.create("ValidUser", contact1Double, profile1Double, UserRole.NORMAL_USER))
                .thenReturn(user);

        userRepository.save(userFactory.create("ValidUser", contact1Double, profile1Double, UserRole.NORMAL_USER));

        // Act
        Optional<UserId> foundId = userRepository.findUserIdByUsername("ValidUser");

        // Assert
        assertTrue(foundId.isPresent());
    }

    @Test
    void findUserIdByUsername_ShouldReturnEmpty_WhenUsernameNotFound() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        User user = mockUser("ValidUser");
        when(userFactory.create("ValidUser", contact1Double, profile1Double, UserRole.NORMAL_USER))
                .thenReturn(user);

        userRepository.save(userFactory.create("ValidUser", contact1Double, profile1Double, UserRole.NORMAL_USER));

        // Act
        Optional<UserId> result = userRepository.findUserIdByUsername("NonExistingUser");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsNull() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> userRepository.findUserIdByUsername(null)
        );
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsEmpty() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.findUserIdByUsername("")
        );
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsBlank() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.findUserIdByUsername("   ")
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.findByID(null)
        );
    }

    @Test
    void shouldReturnUserWhenIdExists() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();

        User user = mockUser("Antonio");
        when(userFactory.create("Antonio", contact1Double, profile1Double, UserRole.NORMAL_USER))
                .thenReturn(user);

        userRepository.save(userFactory.create("Antonio", contact1Double, profile1Double, UserRole.NORMAL_USER));

        // Act
        Optional<User> result = userRepository.findByID(user.id());

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        // Arrange
        IUserRepository userRepository = new UserRepositoryMem();
        UserId userId = mock(UserId.class);

        // Act
        Optional<User> result = userRepository.findByID(userId);

        // Assert
        assertTrue(result.isEmpty());
    }
}