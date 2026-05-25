package TOPSECRET.dto.PublicationType.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

/**
 * Response Data Transfer Object (DTO) representing a
 * {@code PublicationType} resource.
 *
 * <p>This DTO is used to expose publication type data
 * through the REST API without exposing internal domain
 * objects directly.</p>
 *
 * <p>It extends {@link RepresentationModel} to support
 * HATEOAS links, enabling clients to navigate related
 * resources dynamically.</p>
 *
 * <p>This DTO represents the external API view of a
 * publication type resource.</p>
 */
@ToString
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PublicationTypeResponseDTO extends RepresentationModel<PublicationTypeResponseDTO> {

    @Getter
    String id;

    @Getter
    String mediaFormat;

}