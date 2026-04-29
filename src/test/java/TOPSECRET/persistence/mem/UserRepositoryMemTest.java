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

    private IUserRepository userRepository;
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

        userFactory = new UserFactory();
        userRepository = new UserRepositoryMem();
    }

    @Test
    void shouldSaveUserSuccessfully() {
        User user = userFactory.create("TestUser", contact1Double, profile1Double, UserRole.ADMIN);

        assertDoesNotThrow(() -> userRepository.save(user));

        assertTrue(userRepository.exists("TestUser"));
    }

    @Test
    void shouldPersistUserAndReturnSameUser() {
        User user = userFactory.create("ReturnTestUser", contact1Double, profile1Double, UserRole.NORMAL_USER);

        User result = userRepository.save(user);

        assertNotNull(result);
        assertSame(user, result);
        assertTrue(userRepository.exists("ReturnTestUser"));

        Optional<UserId> foundId = userRepository.findUserIdByUsername("ReturnTestUser");

        assertTrue(foundId.isPresent());
        assertEquals(user.id(), foundId.get());
    }

    @Test
    void shouldAllowSavingUsersWithDifferentUsernames() {
        User user1 = userFactory.create("UserOne", contact1Double, profile1Double, UserRole.ADMIN);
        User user2 = userFactory.create("UserTwo", contact2Double, profile2Double, UserRole.NORMAL_USER);

        userRepository.save(user1);
        userRepository.save(user2);

        assertTrue(userRepository.exists("UserOne"));
        assertTrue(userRepository.exists("UserTwo"));

        if (userRepository instanceof UserRepositoryMem repoMem) {
            assertEquals(2, repoMem.getUserCount());
        }
    }

    @Test
    void shouldReturnTrueWhenUserExists() {
        User user = userFactory.create("ExistingUser", contact1Double, profile1Double, UserRole.ADMIN);

        userRepository.save(user);

        assertTrue(userRepository.exists("ExistingUser"));
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExist() {
        assertFalse(userRepository.exists("NonExistingUser"));
    }

    @Test
    void findUserIdByUsername_ShouldReturnValidId_WhenUsernameExists() {
        User user = userFactory.create("ValidUser", contact1Double, profile1Double, UserRole.NORMAL_USER);

        userRepository.save(user);

        Optional<UserId> foundId = userRepository.findUserIdByUsername("ValidUser");

        assertTrue(foundId.isPresent());
        assertEquals(user.id(), foundId.get());
    }

    @Test
    void findUserIdByUsername_ShouldReturnEmpty_WhenUsernameNotFound() {
        User user = userFactory.create("ValidUser", contact1Double, profile1Double, UserRole.NORMAL_USER);

        userRepository.save(user);

        Optional<UserId> result = userRepository.findUserIdByUsername("NonExistingUser");

        assertTrue(result.isEmpty());
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> userRepository.findUserIdByUsername(null)
        );
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsEmpty() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.findUserIdByUsername("")
        );
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsBlank() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.findUserIdByUsername("   ")
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.findUserByID(null)
        );
    }

    @Test
    void shouldReturnUserWhenIdExists() {
        User user = userFactory.create("Antonio", contact1Double, profile1Double, UserRole.NORMAL_USER);

        userRepository.save(user);

        Optional<User> result = userRepository.findUserByID(user.id());

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        Optional<User> result = userRepository.findUserByID(new UserId("99"));

        assertTrue(result.isEmpty());
    }
}