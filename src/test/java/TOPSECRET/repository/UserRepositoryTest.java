package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    Address address = new Address("Main Street", "10", "12345", new Country("PORTUGAL"));
    Email email = new Email("test.user", "example.com");

    private Contact createSampleContact() {
        return new Contact("987654321", "351", email, address);
    }

    private Profile createSampleProfile() {
        return new Profile(
                "Test Profile",
                new Country("PORTUGAL"),
                LocalDate.of(1997, 8, 15));
    }

    @Test
    void testCreateAndAddUser_Success() {
        assertDoesNotThrow(() ->
                userRepository.createAndAddUser(
                        "TestUser",
                        createSampleContact(),
                        createSampleProfile(),
                        UserRole.ADMIN));
    }

    @Test
    void testCreateAndAddUser_UserAlreadyExists() {
        userRepository.createAndAddUser(
                "DuplicateUser",
                createSampleContact(),
                createSampleProfile(),
                UserRole.ADMIN);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.createAndAddUser(
                        "DuplicateUser",
                        createSampleContact(),
                        createSampleProfile(),
                        UserRole.NORMAL_USER));

        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void testExists_ReturnsFalse_WhenUserDoesNotExist() {
        assertFalse(userRepository.exists("NonExistingUser"));
    }

    @Test
    void testExists_ReturnsTrue_WhenUserExists() {
        userRepository.createAndAddUser(
                "ExistingUser",
                createSampleContact(),
                createSampleProfile(),
                UserRole.ADMIN);

        assertTrue(userRepository.exists("ExistingUser"));
    }
}