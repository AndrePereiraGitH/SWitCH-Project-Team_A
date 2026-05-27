package TOPSECRET.dto.bibliographicalData.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Request DTO used to create or update bibliographical data.
 *
 * @param title      the title of the bibliographical entry
 * @param authorName the list of author names associated with the entry
 * @param genreName  the list of genres associated with the entry
 */
public record requestBibliographicalDataDTO(

        @NotBlank(message = "title must not be blank")
        String title,

        @NotEmpty(message = "Authors must not be blank")
        List<String> authorName,

        Locale originalLanguage,

        LocalDate publicationDate,

        @NotEmpty(message = "Genres must not be blank")
        List<String> genreName,

        String chunksOfText
) {
}
