package TOPSECRET.dto.user;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.user.request.RequestUserDTO;
import TOPSECRET.dto.user.response.ResponseUserDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for UserMapper.
 *
 * <p>Here I test all the methods that convert between DTOs and domain objects.
 * I'm using Mockito to simulate domain objects instead of creating real ones,
 * so the tests don't depend on anything external.</p>
 *
 * <p>Each test follows Arrange, Act, Assert and I test two things per method:
 * the normal case (valid input) and the error case (null or invalid input).</p>
 *
 * <p>What's being tested:</p>
 * <ul>
 *   <li>Private constructor: verifies that instantiation throws {@link UnsupportedOperationException}.</li>
 *   <li>{@link UserMapper#toDTO(User)}: takes a {@link User} and returns a {@link ResponseUserDTO}. I check each field separately.</li>
 *   <li>{@link UserMapper#toEmail(RequestUserDTO)}: splits the email string on "@" and builds an {@link Email} object.</li>
 *   <li>{@link UserMapper#toContact(RequestUserDTO)}: builds a {@link Contact} from the DTO fields.</li>
 *   <li>{@link UserMapper#toProfile(RequestUserDTO)}: builds a {@link Profile} from the DTO fields.</li>
 *   <li>{@link UserMapper#toRole(String)}: converts a String like "ADMIN" into the {@link UserRole} enum.</li>
 * </ul>
 *
 * @see UserMapper
 * @see RequestUserDTO
 * @see ResponseUserDTO
 * @see User
 */
class UserMapperTest {

    // -------------------------------------------------------------------------
    // Private constructor
    // -------------------------------------------------------------------------

    @Test
    void constructor_shouldThrowException() throws Exception {
        Constructor<UserMapper> constructor = UserMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    // -------------------------------------------------------------------------
    // toDTO
    // -------------------------------------------------------------------------

    @Test
    void toDTO_shouldMapIdCorrectly() {
        // Arrange
        User userDouble       = mock(User.class);
        UserId userIdDouble   = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);
        Contact contactDouble = mock(Contact.class);
        Email emailDouble     = mock(Email.class);

        when(userDouble.id()).thenReturn(userIdDouble);
        when(userIdDouble.getUserId()).thenReturn("bruno123");
        when(userDouble.getUserName()).thenReturn("bruno123");
        when(userDouble.getProfile()).thenReturn(profileDouble);
        when(profileDouble.getName()).thenReturn("Bruno");
        when(profileDouble.getContact()).thenReturn(contactDouble);
        when(contactDouble.getEmail()).thenReturn(emailDouble);
        when(emailDouble.getEmail()).thenReturn("bruno@isep.ipp.pt");
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act
        ResponseUserDTO result = UserMapper.toDTO(userDouble);

        // Assert
        assertEquals("bruno123", result.getId());
    }

    @Test
    void toDTO_shouldMapUsernameCorrectly() {
        // Arrange
        User userDouble       = mock(User.class);
        UserId userIdDouble   = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);
        Contact contactDouble = mock(Contact.class);
        Email emailDouble     = mock(Email.class);

        when(userDouble.id()).thenReturn(userIdDouble);
        when(userIdDouble.getUserId()).thenReturn("bruno123");
        when(userDouble.getUserName()).thenReturn("bruno123");
        when(userDouble.getProfile()).thenReturn(profileDouble);
        when(profileDouble.getName()).thenReturn("Bruno");
        when(profileDouble.getContact()).thenReturn(contactDouble);
        when(contactDouble.getEmail()).thenReturn(emailDouble);
        when(emailDouble.getEmail()).thenReturn("bruno@isep.ipp.pt");
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act
        ResponseUserDTO result = UserMapper.toDTO(userDouble);

        // Assert
        assertEquals("bruno123", result.getUsername());
    }

    @Test
    void toDTO_shouldMapNameCorrectly() {
        // Arrange
        User userDouble       = mock(User.class);
        UserId userIdDouble   = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);
        Contact contactDouble = mock(Contact.class);
        Email emailDouble     = mock(Email.class);

        when(userDouble.id()).thenReturn(userIdDouble);
        when(userIdDouble.getUserId()).thenReturn("bruno123");
        when(userDouble.getUserName()).thenReturn("bruno123");
        when(userDouble.getProfile()).thenReturn(profileDouble);
        when(profileDouble.getName()).thenReturn("Bruno");
        when(profileDouble.getContact()).thenReturn(contactDouble);
        when(contactDouble.getEmail()).thenReturn(emailDouble);
        when(emailDouble.getEmail()).thenReturn("bruno@isep.ipp.pt");
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act
        ResponseUserDTO result = UserMapper.toDTO(userDouble);

        // Assert
        assertEquals("Bruno", result.getName());
    }

    @Test
    void toDTO_shouldMapEmailCorrectly() {
        // Arrange
        User userDouble       = mock(User.class);
        UserId userIdDouble   = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);
        Contact contactDouble = mock(Contact.class);
        Email emailDouble     = mock(Email.class);

        when(userDouble.id()).thenReturn(userIdDouble);
        when(userIdDouble.getUserId()).thenReturn("bruno123");
        when(userDouble.getUserName()).thenReturn("bruno123");
        when(userDouble.getProfile()).thenReturn(profileDouble);
        when(profileDouble.getName()).thenReturn("Bruno");
        when(profileDouble.getContact()).thenReturn(contactDouble);
        when(contactDouble.getEmail()).thenReturn(emailDouble);
        when(emailDouble.getEmail()).thenReturn("bruno@isep.ipp.pt");
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act
        ResponseUserDTO result = UserMapper.toDTO(userDouble);

        // Assert
        assertEquals("bruno@isep.ipp.pt", result.getEmail());
    }

    @Test
    void toDTO_shouldMapRoleCorrectly() {
        // Arrange
        User userDouble       = mock(User.class);
        UserId userIdDouble   = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);
        Contact contactDouble = mock(Contact.class);
        Email emailDouble     = mock(Email.class);

        when(userDouble.id()).thenReturn(userIdDouble);
        when(userIdDouble.getUserId()).thenReturn("bruno123");
        when(userDouble.getUserName()).thenReturn("bruno123");
        when(userDouble.getProfile()).thenReturn(profileDouble);
        when(profileDouble.getName()).thenReturn("Bruno");
        when(profileDouble.getContact()).thenReturn(contactDouble);
        when(contactDouble.getEmail()).thenReturn(emailDouble);
        when(emailDouble.getEmail()).thenReturn("bruno@isep.ipp.pt");
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act
        ResponseUserDTO result = UserMapper.toDTO(userDouble);

        // Assert
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    void toDTO_shouldThrowException_whenUserIsNull() {
        // Arrange + Act + Assert
        assertThrows(NullPointerException.class, () -> UserMapper.toDTO(null));
    }

    // -------------------------------------------------------------------------
    // toEmail
    // -------------------------------------------------------------------------

    @Test
    void toEmail_shouldMapUsernameCorrectly() {
        // Arrange
        RequestUserDTO dto = new RequestUserDTO(
                "bruno123", "Bruno", "bruno@isep.ipp.pt",
                "912345678", "+351", "2000-05-23", "ADMIN"
        );

        // Act
        Email result = UserMapper.toEmail(dto);

        // Assert
        assertEquals("bruno", result.getUserName());
    }

    @Test
    void toEmail_shouldMapDomainCorrectly() {
        // Arrange
        RequestUserDTO dto = new RequestUserDTO(
                "bruno123", "Bruno", "bruno@isep.ipp.pt",
                "912345678", "+351", "2000-05-23", "ADMIN"
        );

        // Act
        Email result = UserMapper.toEmail(dto);

        // Assert
        assertEquals("isep.ipp.pt", result.getDomain());
    }

    @Test
    void toEmail_shouldThrowException_whenDTOIsNull() {
        // Arrange + Act + Assert
        assertThrows(NullPointerException.class, () -> UserMapper.toEmail(null));
    }

    // -------------------------------------------------------------------------
    // toContact
    // -------------------------------------------------------------------------

    @Test
    void toContact_shouldMapExtensionCorrectly() {
        // Arrange
        RequestUserDTO dto = new RequestUserDTO(
                "bruno123", "Bruno", "bruno@isep.ipp.pt",
                "912345678", "351", "2000-05-23", "ADMIN"
        );

        // Act
        Contact result = UserMapper.toContact(dto);

        // Assert
        assertEquals("351", result.getExtension());
    }

    @Test
    void toContact_shouldMapEmailCorrectly() {
        // Arrange
        RequestUserDTO dto = new RequestUserDTO(
                "bruno123", "Bruno", "bruno@isep.ipp.pt",
                "912345678", "351", "2000-05-23", "ADMIN"
        );

        // Act
        Contact result = UserMapper.toContact(dto);

        // Assert
        assertEquals("bruno@isep.ipp.pt", result.getEmail().getEmail());
    }

    @Test
    void toContact_shouldThrowException_whenDTOIsNull() {
        // Arrange + Act + Assert
        assertThrows(NullPointerException.class, () -> UserMapper.toContact(null));
    }

    // -------------------------------------------------------------------------
    // toProfile
    // -------------------------------------------------------------------------

    @Test
    void toProfile_shouldMapNameCorrectly() {
        // Arrange
        RequestUserDTO dto = new RequestUserDTO(
                "bruno123", "Bruno", "bruno@isep.ipp.pt",
                "912345678", "351", "2000-05-23", "ADMIN"
        );

        // Act
        Profile result = UserMapper.toProfile(dto);

        // Assert
        assertEquals("Bruno", result.getName());
    }

    @Test
    void toProfile_shouldThrowException_whenDTOIsNull() {
        // Arrange + Act + Assert
        assertThrows(NullPointerException.class, () -> UserMapper.toProfile(null));
    }

    // -------------------------------------------------------------------------
    // toRole
    // -------------------------------------------------------------------------

    @Test
    void toRole_shouldReturnAdmin_whenAdminString() {
        // Arrange
        String role = "ADMIN";

        // Act
        UserRole result = UserMapper.toRole(role);

        // Assert
        assertEquals(UserRole.ADMIN, result);
    }

    @Test
    void toRole_shouldReturnNormalUser_whenNormalUserString() {
        // Arrange
        String role = "NORMAL_USER";

        // Act
        UserRole result = UserMapper.toRole(role);

        // Assert
        assertEquals(UserRole.NORMAL_USER, result);
    }

    @Test
    void toRole_shouldThrowException_whenRoleIsInvalid() {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class, () -> UserMapper.toRole("DEUS"));
    }

    @Test
    void toRole_shouldThrowException_whenRoleIsNull() {
        // Arrange + Act + Assert
        assertThrows(NullPointerException.class, () -> UserMapper.toRole(null));
    }
}