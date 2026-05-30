package TOPSECRET.dto.author.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

/**
 * Data Transfer Object used when registering a new author in the system.
 *
 * <p>This request DTO is used by the controller layer to receive
 * input data required to create and persist a new {@link TOPSECRET.domain.Author.Author}
 * domain object.</p>
 *
 * @param name        the author's full name
 * @param countryId   the identifier of the author's country of origin
 * @param dateOfBirth the author's date of birth in ISO format (yyyy-MM-dd)
 * @param biography   a textual biography describing the author
 */
public record CreateAuthorDTO(
        @NotBlank(message = "Author name cannot be blank or null!")
        String name,

        @NotBlank(message = "Country ID cannot be blank or null!")
        String countryId,

        @DateTimeFormat(iso = DATE)
        @NotBlank(message = "Date of birth cannot be blank or null!")
        LocalDate dateOfBirth,

        @NotBlank(message = "Biography cannot be blank or null!")
        String biography
) {}