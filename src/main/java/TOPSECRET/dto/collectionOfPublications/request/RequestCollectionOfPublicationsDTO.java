package TOPSECRET.dto.collectionOfPublications.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for creating or updating a collection of publications.
 *
 * @param collectionName the name of the collection; must not be blank
 * @param genreId        the unique identifier of the genre associated with the collection; must not be blank
 *                       must not be {@code null} — callers must explicitly provide this field
 */
public record RequestCollectionOfPublicationsDTO(

        @NotBlank(message = "Collection name must not be blank")
        String collectionName,

        @NotBlank(message = "Genre must not be blank")
        String genreId

) {
}
