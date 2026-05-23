package TOPSECRET.dto.publicationSample.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for requesting to add a publication to a library.
 * <p>
 * This record encapsulates the necessary information required for the operation,
 * specifically the unique identifier of the publication.
 * </p>
 * <p>
 * <b>Note:</b> The ID of the user performing this action is intentionally omitted
 * from this payload. The user ID should be securely retrieved directly from the
 * authentication context (e.g., security token or session) to prevent unauthorized
 * actions and spoofing.
 * </p>
 *
 * @param publicationId the unique identifier of the publication to be added;
 *                      must not be null or blank
 */
public record AddPublicationToLibraryDTO(
        @NotNull(message = "Publication ID cannot be null")
        @NotBlank(message = "Publication ID cannot be blank")
        String publicationId) {
}