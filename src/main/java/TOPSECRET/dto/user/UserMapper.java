package TOPSECRET.dto.user;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Email;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.dto.user.request.RequestUserDTO;
import TOPSECRET.dto.user.response.ResponseUserDTO;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Mapper responsible for converting between {@link RequestUserDTO},
 * domain Value Objects, and {@link ResponseUserDTO}.
 */
public final class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Splits the email string from the DTO on "@" and builds
     * an {@link Email} Value Object.
     *
     * @param dto the request DTO containing the email string
     * @return the corresponding Email Value Object
     * @throws NullPointerException if the DTO is null
     */
    public static Email toEmail(RequestUserDTO dto) {
        Objects.requireNonNull(dto, "RequestUserDTO cannot be null");

        String[] parts = dto.email().split("@");
        return new Email(parts[0], parts[1]);
    }

    /**
     * Builds a {@link Contact} Value Object from the DTO fields.
     * Calls {@link #toEmail} internally to construct the email.
     *
     * @param dto the request DTO containing contact information
     * @return the corresponding Contact Value Object
     * @throws NullPointerException if the DTO is null
     */
    public static Contact toContact(RequestUserDTO dto) {
        Objects.requireNonNull(dto, "RequestUserDTO cannot be null");

        Email email = toEmail(dto);
        return new Contact(dto.phoneNumber(), dto.extension(), email, null);
    }

    /**
     * Builds a {@link Profile} Value Object from the DTO fields.
     * Calls {@link #toContact} internally to construct the contact.
     *
     * @param dto the request DTO containing profile information
     * @return the corresponding Profile Value Object
     * @throws NullPointerException if the DTO is null
     */
    public static Profile toProfile(RequestUserDTO dto) {
        Objects.requireNonNull(dto, "RequestUserDTO cannot be null");

        Contact contact = toContact(dto);
        LocalDate birthday = LocalDate.parse(dto.birthday());
        return new Profile(dto.name(), contact, birthday);
    }

    /**
     * Converts a String into the corresponding {@link UserRole} enum value.
     *
     * @param role the role as a String (e.g. "ADMIN" or "NORMAL_USER")
     * @return the corresponding UserRole enum value
     * @throws NullPointerException if the role is null
     * @throws IllegalArgumentException if the role string does not match any UserRole
     */
    public static UserRole toRole(String role) {
        Objects.requireNonNull(role, "Role cannot be null");

        return UserRole.valueOf(role);
    }

    /**
     * Converts a {@link User} domain object into a {@link ResponseUserDTO}.
     *
     * @param user the User domain object to convert
     * @return the corresponding ResponseUserDTO
     * @throws NullPointerException if the user is null
     */
    public static ResponseUserDTO toDTO(User user) {
        Objects.requireNonNull(user, "User cannot be null");

        return new ResponseUserDTO(
                user.id().getUserId(),
                user.getUserName(),
                user.getProfile().getName(),
                user.getProfile().getContact().getEmail().getEmail(),
                user.getUserRoles().iterator().next().name()
        );
    }
}