package TOPSECRET.dto;


import TOPSECRET.domain.ValueObjects.PublisherID;

/**
 * Data Transfer Object representing a Publisher for use case US028.
 * <p>
 * This record encapsulates the data required to present publisher information
 * in the application layer, without exposing the internal {@link TOPSECRET.domain.Publisher.Publisher}
 * domain entity.
 * </p>
 *
 * @param name        the name of the publisher; must not be null or blank
 * @param publisherId the unique identifier of the publisher; must not be null
 *
 * @throws IllegalArgumentException if {@code name} is null or blank, or if {@code publisherId} is null
 */

public record US028PublisherDTO(String name, PublisherID publisherId) {
    public US028PublisherDTO {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name must not be null or blank");
        if (publisherId == null)
            throw new IllegalArgumentException("Publisher ID must not be null or blank");
    }
}