package TOPSECRET.dto.genre.request;

import lombok.*;

/**
 * DTO used to receive Genre information from client requests.
 */
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Getter
public class GenreRequestDTO {

    @NonNull
    String genreName;
}
