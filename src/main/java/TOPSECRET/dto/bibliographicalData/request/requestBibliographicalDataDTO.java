package TOPSECRET.dto.bibliographicalData.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

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

        @NotBlank(message = "Authors must not be blank")
        List<String> authorName,

        @NotBlank(message = "Genres must not be blank")
        List<String> genreName
) {
}
