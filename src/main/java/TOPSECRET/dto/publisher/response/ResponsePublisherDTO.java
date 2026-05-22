package TOPSECRET.dto.publisher.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object representing publisher data exposed to clients.
 * <p>
 * This DTO is used in the application layer to transfer publisher
 * information without exposing the internal domain model
 * ({@link TOPSECRET.domain.Publisher.Publisher}).
 * <p>
 * By extending {@link RepresentationModel}, this DTO supports the addition
 * of HATEOAS links externally in the controller layer.
 */


@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@AllArgsConstructor
public class ResponsePublisherDTO extends RepresentationModel<ResponsePublisherDTO> {

    @NotBlank
    private final String name;

    @NonNull
    private final String publisherId;

}
