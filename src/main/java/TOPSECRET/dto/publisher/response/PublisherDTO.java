package TOPSECRET.dto.publisher.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import TOPSECRET.domain.ValueObjects.PublisherID;

/**
 * Data Transfer Object representing a Publisher for use case US028.
 * <p>
 * This DTO is used in the application layer to expose publisher information
 * to the client without leaking the internal domain model
 * ({@link TOPSECRET.domain.Publisher.Publisher}).
 * <p>
 * This class is a pure data container and does not contain any HATEOAS logic.
 * Hypermedia links should be added externally using Spring HATEOAS
 * {@code EntityModel}.
 */


@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@AllArgsConstructor
public class PublisherDTO extends RepresentationModel<PublisherDTO> {

    @NonNull
    @NotBlank
    private final String name;

    @NonNull
    private final PublisherID publisherId;

}
