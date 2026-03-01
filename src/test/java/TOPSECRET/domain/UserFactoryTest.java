package TOPSECRET.domain;

import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UserFactory}.
 *
 * This class ensures that the UserFactory creates User instances correctly.
 */
class UserFactoryTest {

    private final UserFactory userFactory = new UserFactory();

    private final Address address = new Address("Main Street", "10", "12345", new Country("PORTUGAL"));
    private final Email email = new Email("test.user", "example.com");

    private Contact createSampleContact() {
        return new Contact("987654321", "351", email, address);
    }

    private Profile createSampleProfile() {
        return new Profile(
                "Test Profile",
                new Country("PORTUGAL"),
                LocalDate.of(1997, 8, 15));
    }

    /**
     * Tests creating a user using the factory.
     * Ensures that the created user is not null and all fields are correctly initialized.
     */
    @Test
    void testCreateUser_Success() {
        User user = userFactory.create(
                "FactoryUser",
                createSampleContact(),
                createSampleProfile(),
                UserRole.ADMIN);

        assertNotNull(user);
        assertEquals("FactoryUser", user.getUserName());
        assertEquals(UserRole.ADMIN, user.getUserRole());
        assertEquals("Test Profile", user.getProfile().getName());
        assertEquals("987654321", user.getContact().getPhoneNum());
    }
}