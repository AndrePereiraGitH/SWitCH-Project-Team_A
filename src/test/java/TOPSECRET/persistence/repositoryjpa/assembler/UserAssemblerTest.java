package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.datamodel.UserDM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserAssembler}.
 *
 * <p>Ensures correct conversion between:
 * <ul>
 *     <li>{@link User} (Domain Model)</li>
 *     <li>{@link UserDM} (Persistence Model)</li>
 * </ul>
 *
 * <p>All tests follow the AAA pattern:
 * <b>Arrange → Act → Assert</b>.</p>
 */
class UserAssemblerTest {

    @Test
    void shouldCreateAssemblerSuccessfully() {
        // ARRANGE
        UserFactory userFactoryDouble = mock(UserFactory.class);

        // ACT
        UserAssembler sut = new UserAssembler(userFactoryDouble);

        // ASSERT
        assertNotNull(sut);
    }

    @Test
    void shouldThrowExceptionWhenUserFactoryIsNull() {
        // ARRANGE + ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> new UserAssembler(null)
        );
    }

    @Test
    void shouldConvertUserToDM() {
        // ARRANGE
        UserFactory userFactoryDouble = mock(UserFactory.class);
        User userDouble = mock(User.class);
        UserId userIdDouble = mock(UserId.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        when(userDouble.id()).thenReturn(userIdDouble);
        when(userDouble.getUserName()).thenReturn("TestUser");
        when(userDouble.getContact()).thenReturn(contactDouble);
        when(userDouble.getProfile()).thenReturn(profileDouble);
        when(userDouble.getUserRole()).thenReturn(UserRole.ADMIN);

        UserAssembler sut = new UserAssembler(userFactoryDouble);

        // ACT
        UserDM result = sut.toDM(userDouble);

        // ASSERT
        assertNotNull(result);
        assertEquals(userIdDouble, result.getId());
        assertEquals("TestUser", result.getUserName());
        assertEquals(contactDouble, result.getContact());
        assertEquals(profileDouble, result.getProfile());
        assertEquals(UserRole.ADMIN, result.getUserRole());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // ARRANGE
        UserFactory userFactoryDouble = mock(UserFactory.class);
        UserAssembler sut = new UserAssembler(userFactoryDouble);

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> sut.toDM(null)
        );
    }

    @Test
    void shouldConvertDMToDomain() {
        // ARRANGE
        UserFactory userFactoryDouble = mock(UserFactory.class);
        User userDouble = mock(User.class);
        UserId userIdDouble = mock(UserId.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        UserDM userDM = new UserDM(
                userIdDouble,
                "TestUser",
                contactDouble,
                profileDouble,
                UserRole.ADMIN
        );

        when(userFactoryDouble.create(
                userIdDouble,
                "TestUser",
                contactDouble,
                profileDouble,
                UserRole.ADMIN
        )).thenReturn(userDouble);

        UserAssembler sut = new UserAssembler(userFactoryDouble);

        // ACT
        User result = sut.toDomain(userDM);

        // ASSERT
        assertNotNull(result);
        assertSame(userDouble, result);
    }

    @Test
    void shouldThrowExceptionWhenDMIsNull() {
        // ARRANGE
        UserFactory userFactoryDouble = mock(UserFactory.class);
        UserAssembler sut = new UserAssembler(userFactoryDouble);

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> sut.toDomain(null)
        );
    }
}