package TOPSECRET.dto.genre.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO used to receive Genre information from client requests.
 */
public record GenreRequestDTO (

    @NotBlank(message = "Genre name must be informed")
    String genreName){
}
