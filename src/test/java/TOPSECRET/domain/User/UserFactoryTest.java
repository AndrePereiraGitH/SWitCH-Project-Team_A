package TOPSECRET.domain.User;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link UserFactory}.
 */
class UserFactoryTest {

    @Test
    void create_returnsNewlyConstructedUserInstance() {

        // Arrange
        String username = "FactoryUser";
        Profile profileDouble = mock(Profile.class);
        Set<UserRole> roles = Set.of(UserRole.ADMIN);

        try (MockedConstruction<User> mocked =
                     mockConstruction(User.class, (mock, context) -> {})) {

            UserFactory userFactory = new UserFactory();

            // Act
            User user = userFactory.create(username, profileDouble, roles);

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
        Profile profileDouble = mock(Profile.class);
        Set<UserRole> roles = Set.of(UserRole.ADMIN);

        try (MockedConstruction<User> mocked =
                     mockConstruction(User.class, (mock, context) -> {})) {

            UserFactory userFactory = new UserFactory();

            // Act
            User user = userFactory.create(userIdDouble, username, profileDouble, roles);

            // Assert
            assertNotNull(user);
            assertEquals(1, mocked.constructed().size());

            User constructedUser = mocked.constructed().get(0);
            assertSame(user, constructedUser);
        }
    }
}