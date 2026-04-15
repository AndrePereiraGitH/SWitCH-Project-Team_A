package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.List;
import java.util.Locale;

/**
 * Data Transfer Object representing a publication sample available in a user's library.
 *
 * @param publicationSampleID the unique identifier of the publication sample
 * @param title the title of the publication
 * @param authorName the list of author names associated with the publication
 * @param language the original language of the publication
 * @param genreType the list of genres associated with the publication
 * @param ownerName the identifier of the user who owns the publication sample
 * @param condition the condition of the publication sample
 */
public record US016PublicationSampleDTO(
        String publicationSampleID,
        String title,
        List<String>authorName,
        Locale language,
        List<String> genreType,
        String ownerName,
        String condition
) {
    public US016PublicationSampleDTO {
        if (publicationSampleID == null || title == null || authorName == null || language == null || genreType == null || ownerName == null || condition == null
        ) {
            throw new IllegalArgumentException("All fields must be not-null!");
        }
    }
}
