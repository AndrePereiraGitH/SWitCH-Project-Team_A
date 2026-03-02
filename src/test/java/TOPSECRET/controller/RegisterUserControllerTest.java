package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserControllerTest {

    private RegisterUserController registerUserController;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserFactory userFactory = new UserFactory();
        userRepository = new UserRepository(userFactory);
        registerUserController = new RegisterUserController(userRepository);
    }

    private Contact createSampleContact() {
        Address address = new Address("Main Street", "10", "12345", new Country("PORTUGAL"));
        Email email = new Email("test.user", "example.com");
        return new Contact("987654321", "351", email, address);
    }

    private Profile createSampleProfile() {
        return new Profile(
                "Test Profile",
                new Country("PORTUGAL"),
                LocalDate.of(1997, 8, 15)
        );
    }

    // To test the happy path
    /*
    @Test
    void testRegisterUser_Success() {
        String userName = "TestUser1";
        Contact contact = createSampleContact();
        Profile profile = createSampleProfile();

        registerUserController.registerUser(userName, contact, profile, UserRole.ADMIN);
        assertTrue(userRepository.exists(userName), "User registered successfully");
    }

     */

    // To test any null parameter
    @Test
    void testRegisterUser_NullParameters() {
        Contact contact = createSampleContact();
        Profile profile = createSampleProfile();

        assertThrows(IllegalArgumentException.class, () ->
                registerUserController.registerUser(null, contact, profile, UserRole.ADMIN));

        assertThrows(IllegalArgumentException.class, () ->
                registerUserController.registerUser("User", null, profile, UserRole.ADMIN));

        assertThrows(IllegalArgumentException.class, () ->
                registerUserController.registerUser("User", contact, null, UserRole.ADMIN));

        assertThrows(IllegalArgumentException.class, () ->
                registerUserController.registerUser("User", contact, profile, null));
    }
}