package TOPSECRET.dto.library.request;

import jakarta.validation.constraints.NotBlank;

public record RequestLibraryDTO(

        @NotBlank(message = "Library name must not be blank")
        String libraryName,

        @NotBlank(message = "User ID must not be blank")
        String userId) {
}