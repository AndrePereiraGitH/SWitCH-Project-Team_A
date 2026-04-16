package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.PublisherID;

/**
 * Data Transfer Object (DTO) used in US027 to represent a Publisher
 * when listing items on direct sale for a given publisher.
 * <p>
 * This DTO exposes only the publisher's identifier and its name.
 * </p>
 *
 * @param publisherId the unique identifier of the publisher; must not be null
 * @param name the publisher's name; must not be null or blank
 *
 * @throws IllegalArgumentException if {@code publisherId} is null,
 *                                  or if {@code name} is null or blank
 */
public record US027PublisherDTO(String publisherId, String name) {

    public US027PublisherDTO {
        if (publisherId == null || publisherId.isBlank()) throw new IllegalArgumentException("publisherId cannot be null or blank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name cannot be null or blank");
    }
}
