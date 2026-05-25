package TOPSECRET.dto.publicationSample.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object (DTO) representing a lightweight reference view of a publication sample.
 * <p>
 * This class encapsulates minimal identifying information about a publication sample,
 * typically used in lists, summaries, or as a reference embedded within other objects
 * where the full details are not required. It extends {@link RepresentationModel} to
 * provide support for Spring HATEOAS, allowing hypermedia links to be appended to
 * the API response.
 * </p>
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class PublicationReferenceDTO extends RepresentationModel<PublicationReferenceDTO> {

    private String id;
    private String pubTitle;
}
