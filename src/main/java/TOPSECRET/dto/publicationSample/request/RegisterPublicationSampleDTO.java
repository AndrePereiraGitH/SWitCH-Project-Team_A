package TOPSECRET.dto.publicationSample.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for requesting the registration of a new publication sample.
 * <p>
 * This record encapsulates the required information to register a specific physical or
 * digital copy (sample) of a publication in the system. It links the sample to its
 * overarching bibliographical record and its specific edition, while also capturing
 * its current condition.
 * </p>
 *
 * @param bibliographicalDataId the unique identifier of the overarching bibliographical data;
 *                              must not be null or blank
 * @param editionId             the unique identifier of the specific edition being registered;
 *                              must not be null or blank
 * @param condition             the initial condition of the publication sample
 *                              (e.g., "New", "Good", "Poor"); must not be null or blank
 */
public record RegisterPublicationSampleDTO(

        @NotNull(message = "Bibliographical Id cannot be null")
        @NotBlank(message = "Bibliographical Id cannot be blank")
        String bibliographicalDataId,

        @NotNull(message = "Edition Id cannot be null")
        @NotBlank(message = "Edition Id cannot be blank")
        String editionId,

        @NotNull(message = "Condition cannot be null")
        @NotBlank(message = "Condition cannot be blank")
        String condition) {
}
