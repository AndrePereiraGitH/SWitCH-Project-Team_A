package TOPSECRET.dto.appraisalEntity.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Data Transfer Object used to create a new AppraisalEntity.
 *
 * <p>This record contains all input data required by the API to register
 * a new appraisal, including contact information, address details,
 * and associated publication and genre identifiers.</p>
 *
 * <p>It is used exclusively as an input model and should not contain
 * business logic.</p>
 */
public record RequestAppraisalEntityDTO(

        @NotBlank(message = "Name is required")
        String name,

        // Contact information
        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        String extension,

        // Email information
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,

        // Address information
        @NotBlank(message = "Street is required")
        String street,

        @NotBlank(message = "Number is required")
        String number,

        @NotBlank(message = "Zip code is required")
        String zipCode,

        @NotBlank(message = "City ID is required")
        String cityId,

        @NotEmpty(message = "At least one publication type must be provided")
        List<String> publicationTypeIds,

        @NotEmpty(message = "At least one genre must be provided")
        List<String> genreIds

) {
}