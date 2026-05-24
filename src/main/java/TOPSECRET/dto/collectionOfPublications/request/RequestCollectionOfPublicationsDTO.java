package TOPSECRET.dto.collectionOfPublications.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for creating or updating a collection of publications.
 *
 * @param collectionName the name of the collection; must not be blank
 * @param genreId        the unique identifier of the genre associated with the collection; must not be blank
 * @param isPrivate      whether the collection is private ({@code true}) or publicly visible ({@code false});
 *                       must not be {@code null} — callers must explicitly provide this field
 */
public record RequestCollectionOfPublicationsDTO(

        @NotBlank(message = "Collection name must not be blank")
        String collectionName,

        @NotBlank(message = "Genre must not be blank")
        String genreId,

        @NotNull(message = "privacyStatus must not be null")
        Boolean isPrivate
) {
}
