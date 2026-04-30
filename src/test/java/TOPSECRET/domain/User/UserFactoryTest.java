package TOPSECRET.domain.User;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import TOPSECRET.domain.ValueObjects.UserId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link UserFactory}.
 * <p>
 * This test class verifies the behaviour of {@code UserFactory} regarding:
 * </p>
 * <ul>
 *     <li>Creation — verifies that the factory delegates construction to {@link User}</li>
 * </ul>
 */
class UserFactoryTest {

    @Test
    void create_returnsNewlyConstructedUserInstance() {

        // Arrange
        String username = "FactoryUser";
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);
        UserRole role = UserRole.ADMIN;

        try (MockedConstruction<User> mocked =
                     mockConstruction(User.class, (mock, context) -> {
                     })) {

            UserFactory userFactory = new UserFactory();

            // Act
            User user = userFactory.create(username, contactDouble, profileDouble, role);

            // Assert
            assertNotNull(user);
            assertEquals(1, mocked.constructed().size());

            User constructedUser = mocked.constructed().get(0);
            assertSame(user, constructedUser);
        }
    }

    @Test
    void create_withUserId_returnsNewlyConstructedUserInstance() {

        // Arrange
        UserId userIdDouble = mock(UserId.class);
        String username = "FactoryUser";
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);
        UserRole role = UserRole.ADMIN;

        try (MockedConstruction<User> mocked =
                     mockConstruction(User.class, (mock, context) -> {
                     })) {

            UserFactory userFactory = new UserFactory();

            // Act
            User user = userFactory.create(userIdDouble, username, contactDouble, profileDouble, role);

            // Assert
            assertNotNull(user);
            assertEquals(1, mocked.constructed().size());

            User constructedUser = mocked.constructed().get(0);
            assertSame(user, constructedUser);
        }
    }
}