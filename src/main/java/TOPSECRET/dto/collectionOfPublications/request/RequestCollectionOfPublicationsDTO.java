package TOPSECRET.dto.collectionOfPublications.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for creating or updating a collection of publications.
 *
 * @param collectionName the name of the collection; must not be blank
 * @param genreId        the unique identifier of the genre associated with the collection; must not be blank
 * @param userId         the unique identifier of the user who owns the collection; must not be blank
 * @param isPrivate      whether the collection is private ({@code true}) or publicly visible ({@code false});
 *                       must not be {@code null} — callers must explicitly provide this field
 */
public record RequestCollectionOfPublicationsDTO(

        @NotBlank(message = "Collection name must not be blank")
        String collectionName,

        @NotBlank(message = "Genre must not be blank")
        String genreId,

        @NotBlank(message = "User must not be blank")
        String userId,

        @NotNull(message = "privacyStatus must not be blank")
        Boolean isPrivate
) {
}
