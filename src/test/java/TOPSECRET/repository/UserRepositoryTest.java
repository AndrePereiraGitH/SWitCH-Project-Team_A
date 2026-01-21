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

    @Test
    void testCreateAndAddUser_Success() {
        Address address = new Address("Main Street", "10", "12345", new Country("PORTUGAL"));
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile1",
                new Country("PORTUGAL"),
                LocalDate.of(1997, 8, 15)
        );

        String userName = "Test User1";

        userRepository.createAndAddUser(userName, contact, profile, UserRole.ADMIN);

        assertTrue(userRepository.exists(userName), "User should exist after being added");
    }

    @Test
    void testCreateAndAddUser_UserAlreadyExists() {
        Address address = new Address("Main Street", "10", "12345", new Country("PORTUGAL"));
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile2",
                new Country("PORTUGAL"),
                LocalDate.of(1997, 8, 15)
        );

        String userName = "Test User2";

        userRepository.createAndAddUser(userName, contact, profile, UserRole.ADMIN);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.createAndAddUser(userName, contact, profile, UserRole.NORMAL_USER)
        );

        assertEquals("User validation failed or user already exists", exception.getMessage());
    }

    @Test
    void testExists_UserDoesNotExist() {
        assertFalse(userRepository.exists("nonexistentUser"), "User should not exist initially");
    }

    @Test
    void testCreateAndAddUser_NullUsername() {
        Address address = new Address("Main Street", "10", "12345", new Country("PORTUGAL"));
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile",
                new Country("PORTUGAL"),
                LocalDate.of(1997, 8, 15)
        );

        assertThrows(IllegalArgumentException.class, () ->
                userRepository.createAndAddUser(null, contact, profile, UserRole.ADMIN)
        );
    }
}