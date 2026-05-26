package TOPSECRET.dto.library.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for creating or updating a {@link TOPSECRET.domain.Library.Library}.
 *
 * @param libraryName the name of the library; must not be blank
 */
public record RequestLibraryDTO(

        @NotBlank(message = "Library name must not be blank")
        String libraryName){
}