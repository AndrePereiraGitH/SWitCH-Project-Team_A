package TOPSECRET.dto.genre.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;


/**
 * DTO used to expose Genre information in API responses.
 *
 * <p>
 * Extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
@Getter
public class GenreResponseDTO extends RepresentationModel<GenreResponseDTO> {

    String id;
    String genreName;
}
