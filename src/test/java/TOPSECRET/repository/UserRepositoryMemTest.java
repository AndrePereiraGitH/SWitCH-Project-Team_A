package TOPSECRET.repository;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.User.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserRepositoryMem}.
 *
 * This class tests the main functionalities of UserRepository including:
 * <ul>
 *     <li>Creating and adding users via the factory</li>
 *     <li>Handling duplicate usernames</li>
 *     <li>Checking user existence</li>
 *     <li>Finding a user ID by username, including valid, invalid, null, empty and blank inputs</li>
 * </ul>
 */
class UserRepositoryMemTest {

    private IUserRepository userRepository; // interface type
    private UserFactory userFactory;

    // --- MOCKED DEPENDENCIES ---
    private Email email1;
    private Email email2;
    private Contact contact1;
    private Contact contact2;
    private Profile profile1;
    private Profile profile2;

    @BeforeEach
    void setUp() {
        // Mock emails
        email1 = mock(Email.class);
        when(email1.getEmail()).thenReturn("test.user@example.com");

        email2 = mock(Email.class);
        when(email2.getEmail()).thenReturn("duplicate.user@example.com");

        // Mock contacts
        contact1 = mock(Contact.class);
        when(contact1.getEmail()).thenReturn(email1);
        when(contact1.getAddress()).thenReturn(mock(Address.class));

        contact2 = mock(Contact.class);
        when(contact2.getEmail()).thenReturn(email2);
        when(contact2.getAddress()).thenReturn(mock(Address.class));

        // Mock profiles
        profile1 = mock(Profile.class);
        when(profile1.getName()).thenReturn("Profile 1");
        when(profile1.getContact()).thenReturn(contact1);
        when(profile1.getBirthday()).thenReturn(LocalDate.of(1997, 8, 15));

        profile2 = mock(Profile.class);
        when(profile2.getName()).thenReturn("Profile 2");
        when(profile2.getContact()).thenReturn(contact2);
        when(profile2.getBirthday()).thenReturn(LocalDate.of(1990, 1, 1));

        // Initialize repository with factory
        userFactory = new UserFactory();
        userRepository = new UserRepositoryMem(userFactory);
    }

    @Test
    void testSaveUser_Success() {
        User user = userFactory.create("TestUser", contact1, profile1, UserRole.ADMIN);

        assertDoesNotThrow(() -> userRepository.save(user));
        assertTrue(userRepository.exists("TestUser"));
    }

    @Test
    void testSaveUser_ReturnsCorrectUser() {
        User user = userFactory.create("ReturnTestUser", contact1, profile1, UserRole.NORMAL_USER);
        userRepository.save(user);

        assertTrue(userRepository.exists("ReturnTestUser"));
        Optional<UserId> foundId = userRepository.findUserIdByUsername("ReturnTestUser");
        assertTrue(foundId.isPresent());
        assertEquals(user.getId(), foundId.get());
    }

    @Test
    void testSaveUser_DuplicateUsernameThrowsException() {
        User user1 = userFactory.create("DuplicateUser", contact1, profile1, UserRole.ADMIN);
        userRepository.save(user1);

        User user2 = userFactory.create("DuplicateUser", contact2, profile2, UserRole.NORMAL_USER);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.save(user2)
        );
        assertEquals("User already exists", exception.getMessage());

        if (userRepository instanceof UserRepositoryMem repoMem) {
            assertEquals(1, repoMem.getUserCount());
        }
    }

    @Test
    void testExists_ReturnsTrue_WhenUserExists() {
        User user = userFactory.create("ExistingUser", contact1, profile1, UserRole.ADMIN);
        userRepository.save(user);

        assertTrue(userRepository.exists("ExistingUser"));
    }

    @Test
    void testExists_ReturnsFalse_WhenUserDoesNotExist() {
        assertFalse(userRepository.exists("NonExistingUser"));
    }

    @Test
    void findUserIdByUsername_ShouldReturnValidId_WhenUsernameExists() {
        User user = userFactory.create("ValidUser", contact1, profile1, UserRole.NORMAL_USER);
        userRepository.save(user);

        Optional<UserId> userId = userRepository.findUserIdByUsername("ValidUser");

        assertTrue(userId.isPresent());
        assertEquals(user.getId(), userId.get());
    }

    @Test
    void findUserIdByUsername_ShouldReturnEmpty_WhenUsernameNotFound() {
        User user = userFactory.create("ValidUser", contact1, profile1, UserRole.NORMAL_USER);
        userRepository.save(user);

        Optional<UserId> userId = userRepository.findUserIdByUsername("NonExistingUser");
        assertTrue(userId.isEmpty());
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsNull() {
        assertThrows(NullPointerException.class,
                () -> userRepository.findUserIdByUsername(null));
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> userRepository.findUserIdByUsername(""));
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> userRepository.findUserIdByUsername("   "));
    }
}