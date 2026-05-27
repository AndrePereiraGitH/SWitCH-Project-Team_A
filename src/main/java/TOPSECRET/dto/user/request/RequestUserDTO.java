package TOPSECRET.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for creating a new User.
 *
 * <p>This record holds all the data sent by the client when registering a new user.
 * All fields are Strings so the domain layer stays completely isolated from the API layer.
 * The {@link TOPSECRET.dto.user.UserMapper} is responsible for converting these fields
 * into domain Value Objects.</p>
 *
 * @param username    the username of the user (min 5 characters)
 * @param name        the full name of the user
 * @param email       the email address in format "username@domain"
 * @param phoneNumber the phone number of the user (7 to 10 digits)
 * @param extension   the phone extension of the user (required)
 * @param birthday    the date of birth in format "yyyy-MM-dd"
 * @param role        the role assigned to the user ("ADMIN" or "NORMAL_USER")
 */

public record RequestUserDTO (

    @NotBlank(message = "Username is required")
    String username,

    @NotBlank(message = "Name is required")
    String name,

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    String email,

    @NotBlank(message = "Phone number is required")
    String phoneNumber,

    @NotBlank(message = "Extension is required")
    String extension,

    @NotBlank(message = "Birthday is required")
    String birthday,

    @NotBlank(message = "Role is required")
    String role

){
}
