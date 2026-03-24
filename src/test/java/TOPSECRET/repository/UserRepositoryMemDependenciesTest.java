package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.Address;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.ValueObjects.Email;
import TOPSECRET.domain.Profile;
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
class UserRepositoryMemDependenciesTest {

    private UserRepository userRepository; // usar interface
    private UserFactory userFactory;

    // --- COUNTRY INSTANCES ---
    private Country portugal1;
    private Country portugal2;

    @BeforeEach
    void setUp() {

        portugal1 = mock(Country.class);
        when(portugal1.getCountryName()).thenReturn("Portugal");

        portugal2 = mock(Country.class);
        when(portugal2.getCountryName()).thenReturn("Portugal");

        userFactory = new UserFactory();
        userRepository = new UserRepositoryMem(userFactory); // interface
    }

    private final Email email = new Email("test.user", "example.com");

    private Contact createSampleContact(Country country) {
        return new Contact("987654321", "351", email, new Address("Main Street", "10", "12345", country));
    }

    private Profile createSampleProfile(Country country) {
        return new Profile(
                "Test Profile",
                country,
                LocalDate.of(1997, 8, 15));
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
                        createSampleContact(portugal1),
                        createSampleProfile(portugal1),
                        UserRole.ADMIN));

        assertTrue(userRepository.exists("TestUser"));
    }

    /**
     * Tests creating a user with a username that already exists.
     * Expects an IllegalArgumentException and ensures the user count does not increase.
     */
    @Test
    void testCreateAndAdd_UserNameAlreadyExists() {
        userRepository.createAndAdd(
                "DuplicateUser",
                createSampleContact(portugal1),
                createSampleProfile(portugal1),
                UserRole.ADMIN);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.createAndAdd(
                        "DuplicateUser",
                        createSampleContact(portugal2),
                        createSampleProfile(portugal2),
                        UserRole.NORMAL_USER));

        assertEquals("Username already exists", exception.getMessage());

        // Para verificar contagem só funciona se for Mem
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
                createSampleContact(portugal1),
                createSampleProfile(portugal1),
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