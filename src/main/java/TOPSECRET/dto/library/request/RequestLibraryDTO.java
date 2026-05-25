package TOPSECRET.dto.library.request;

import jakarta.validation.constraints.NotBlank;

public record RequestLibraryDTO(

        @NotBlank(message = "Library name must not be blank")
        String libraryName){
}