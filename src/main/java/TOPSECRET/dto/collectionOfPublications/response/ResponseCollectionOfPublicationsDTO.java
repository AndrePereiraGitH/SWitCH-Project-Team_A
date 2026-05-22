package TOPSECRET.dto.collectionOfPublications.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object representing a response for a collection of publications.
 *
 * <p>Extends {@link RepresentationModel} to support HATEOAS links, allowing
 * clients to navigate related resources via hypermedia.</p>
 *
 * @see RepresentationModel
 */
@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@AllArgsConstructor
public class ResponseCollectionOfPublicationsDTO extends
        RepresentationModel<ResponseCollectionOfPublicationsDTO> {

    /**
     * The name of the collection.
     * Must not be blank.
     */
    @NotBlank
    private final String collectionName;

    /**
     * The unique identifier of the collection.
     * Must not be blank.
     */
    @NotBlank
    private final String collectionId;
}