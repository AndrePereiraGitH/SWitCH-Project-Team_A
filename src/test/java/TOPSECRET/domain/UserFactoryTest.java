package TOPSECRET.domain;

import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
}