package TOPSECRET.repository;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
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

    private IUserRepository userRepository;
    private UserFactory userFactory;

    // --- TEST DOUBLES ---
    private Email email1Double;
    private Email email2Double;
    private Contact contact1Double;
    private Contact contact2Double;
    private Profile profile1Double;
    private Profile profile2Double;

    @BeforeEach
    void setUp() {
        // Email doubles
        email1Double = mock(Email.class);
        when(email1Double.getEmail()).thenReturn("test.user@example.com");

        email2Double = mock(Email.class);
        when(email2Double.getEmail()).thenReturn("duplicate.user@example.com");

        // Contact doubles
        contact1Double = mock(Contact.class);
        when(contact1Double.getEmail()).thenReturn(email1Double);
        when(contact1Double.getAddress()).thenReturn(mock(Address.class));

        contact2Double = mock(Contact.class);
        when(contact2Double.getEmail()).thenReturn(email2Double);
        when(contact2Double.getAddress()).thenReturn(mock(Address.class));

        // Profile doubles
        profile1Double = mock(Profile.class);
        when(profile1Double.getName()).thenReturn("Profile 1");
        when(profile1Double.getContact()).thenReturn(contact1Double);
        when(profile1Double.getBirthday()).thenReturn(LocalDate.of(1997, 8, 15));

        profile2Double = mock(Profile.class);
        when(profile2Double.getName()).thenReturn("Profile 2");
        when(profile2Double.getContact()).thenReturn(contact2Double);
        when(profile2Double.getBirthday()).thenReturn(LocalDate.of(1990, 1, 1));

        // Initialize repository with factory
        userFactory = new UserFactory();
        userRepository = new UserRepositoryMem(userFactory);
    }

    @Test
    void shouldcreateAndAddUserSuccessfully() {
        // ACT & ASSERT
        assertDoesNotThrow(() -> userRepository.createAndAdd("TestUser", contact1Double, profile1Double, UserRole.ADMIN));
        assertTrue(userRepository.exists("TestUser"));
    }

    @Test
    void shouldReturnUserIdAfterCreation() {
        // ACT
        User result = userRepository.createAndAdd("ReturnTestUser", contact1Double, profile1Double, UserRole.NORMAL_USER);

        // ASSERT
        assertNotNull(result);
        assertTrue(userRepository.exists("ReturnTestUser"));
        Optional<UserId> foundId = userRepository.findUserIdByUsername("ReturnTestUser");
        assertTrue(foundId.isPresent());
        assertEquals(result, foundId.get());
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateUsername() {
        // ARRANGE
        userRepository.createAndAdd("DuplicateUser", contact1Double, profile1Double, UserRole.ADMIN);

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.createAndAdd("DuplicateUser", contact2Double, profile2Double, UserRole.NORMAL_USER)
        );

        if (userRepository instanceof UserRepositoryMem repoMem) {
            assertEquals(1, repoMem.getUserCount());
        }
    }

    @Test
    void shouldReturnTrueWhenUserExists() {
        // ARRANGE
        userRepository.createAndAdd("ExistingUser", contact1Double, profile1Double, UserRole.ADMIN);

        // ACT
        boolean result = userRepository.exists("ExistingUser");

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExist() {
        // ACT
        boolean result = userRepository.exists("NonExistingUser");

        // ASSERT
        assertFalse(result);
    }

    @Test
    void findUserIdByUsername_ShouldReturnValidId_WhenUsernameExists() {
        // ARRANGE
        User createAndAddd = userRepository.createAndAdd("ValidUser", contact1Double, profile1Double, UserRole.NORMAL_USER);

        // ACT
        Optional<UserId> foundId = userRepository.findUserIdByUsername("ValidUser");

        // ASSERT
        assertTrue(foundId.isPresent());
        assertEquals(createAndAddd, foundId.get());
    }

    @Test
    void findUserIdByUsername_ShouldReturnEmpty_WhenUsernameNotFound() {
        // ARRANGE
        userRepository.createAndAdd("ValidUser", contact1Double, profile1Double, UserRole.NORMAL_USER);

        // ACT
        Optional<UserId> result = userRepository.findUserIdByUsername("NonExistingUser");

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsNull() {
        // ACT & ASSERT
        assertThrows(NullPointerException.class,
                () -> userRepository.findUserIdByUsername(null));
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsEmpty() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> userRepository.findUserIdByUsername(""));
    }

    @Test
    void findUserIdByUsername_ShouldThrowException_WhenUsernameIsBlank() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> userRepository.findUserIdByUsername("   "));
    }
}