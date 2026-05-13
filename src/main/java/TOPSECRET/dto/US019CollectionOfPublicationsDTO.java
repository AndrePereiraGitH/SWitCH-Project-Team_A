package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;

/**
 * Data Transfer Object for US019 - Publicly Share a Collection of Publications.
 *
 * <p>Encapsulates the essential information about a collection of publications
 * for transfer between the controller and the presentation layer.</p>
 */
public record US019CollectionOfPublicationsDTO(
        CollectionOfPublicationsId id,
        String name,
        String genreName,
        String ownerName
) {
    public US019CollectionOfPublicationsDTO {
        if (id == null) throw new IllegalArgumentException("id cannot be null");
        if (name == null) throw new IllegalArgumentException("name cannot be null");
        if (genreName == null) throw new IllegalArgumentException("genreName cannot be null");
        if (ownerName == null) throw new IllegalArgumentException("ownerName cannot be null");
    }
}
