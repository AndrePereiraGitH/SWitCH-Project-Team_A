package TOPSECRET.dto.PublicationType.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request Data Transfer Object (DTO) used to create
 * a new {@code PublicationType} resource.
 *
 * <p>This DTO represents the payload received from
 * API clients when creating a publication type through
 * the REST API.</p>
 *
 * <p>It contains only the information required for the
 * creation process and acts as a boundary object between
 * the API layer and the domain layer.</p>
 */
public record CreatePublicationTypeDTO(
        @NotBlank(message = "MediaFormat cannot be null or blank")
        String mediaFormat) {}
