package TOPSECRET.repository;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Address;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.User.UserRepository;
import TOPSECRET.domain.ValueObjects.Email;
import TOPSECRET.domain.ValueObjects.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
 * </ul>
 */
class UserRepositoryMemTest {

    private UserRepository userRepository; // interface type
    private UserFactory userFactory;

    // --- MOCKED DEPENDENCIES ---
    private Country portugal1;
    private Country portugal2;
    private Email email1;
    private Email email2;
    private Contact contact1;
    private Contact contact2;
    private Profile profile1;
    private Profile profile2;

    @BeforeEach
    void setUp() {
        // Mock countries
        portugal1 = mock(Country.class);
        when(portugal1.getCountryName()).thenReturn("Portugal");

        portugal2 = mock(Country.class);
        when(portugal2.getCountryName()).thenReturn("Portugal");

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
        when(profile1.getCountry()).thenReturn(portugal1);
        when(profile1.getBirthday()).thenReturn(LocalDate.of(1997, 8, 15));

        profile2 = mock(Profile.class);
        when(profile2.getCountry()).thenReturn(portugal2);
        when(profile2.getBirthday()).thenReturn(LocalDate.of(1990, 1, 1));

        // Initialize repository with factory
        userFactory = new UserFactory();
        userRepository = new UserRepositoryMem(userFactory); // interface type
    }

    /**
     * Tests creating and adding a user successfully.
     * Ensures no exception is thrown and the user exists in the repository.
     */
    @Test
    void testCreateAndAdd_Success() {
        assertDoesNotThrow(() ->
                userRepository.createAndAdd(
                        "TestUser",
                        contact1,
                        profile1,
                        UserRole.ADMIN));

        assertTrue(userRepository.exists("TestUser"));
    }

    @Test
    void testCreateAndAdd_ReturnsCreatedUser() {
        User createdUser = userRepository.createAndAdd(
                "ReturnTestUser",
                contact1,
                profile1,
                UserRole.NORMAL_USER);

        assertNotNull(createdUser, "Returned user should not be null");
        assertEquals("ReturnTestUser", createdUser.getUserName(), "Returned user should have the correct username");
        assertTrue(userRepository.exists("ReturnTestUser"), "User should exist in repository after creation");
    }

    /**
     * Tests creating a user with a username that already exists.
     * Expects an IllegalArgumentException and ensures the user count does not increase.
     */
    @Test
    void testCreateAndAdd_UserNameAlreadyExists() {
        userRepository.createAndAdd(
                "DuplicateUser",
                contact1,
                profile1,
                UserRole.ADMIN);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.createAndAdd(
                        "DuplicateUser",
                        contact2,
                        profile2,
                        UserRole.NORMAL_USER));

        assertEquals("Username already exists", exception.getMessage());

        // Only works if using concrete type UserRepositoryMem
        if (userRepository instanceof UserRepositoryMem repoMem) {
            assertEquals(1, repoMem.getUserCount());
        }
    }

    /**
     * Tests the exists method for an existing user.
     * Expects the method to return true.
     */
    @Test
    void testExists_ReturnsTrue_WhenUserExists() {
        userRepository.createAndAdd(
                "ExistingUser",
                contact1,
                profile1,
                UserRole.ADMIN);

        assertTrue(userRepository.exists("ExistingUser"));
    }

    /**
     * Tests the exists method for a non-existing user.
     * Expects the method to return false.
     */
    @Test
    void testExists_ReturnsFalse_WhenUserDoesNotExist() {
        assertFalse(userRepository.exists("NonExistingUser"));
    }
}