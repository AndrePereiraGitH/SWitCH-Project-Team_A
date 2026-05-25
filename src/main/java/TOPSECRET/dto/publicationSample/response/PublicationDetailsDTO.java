package TOPSECRET.dto.publicationSample.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;


/**
 * Data Transfer Object (DTO) representing the detailed view of a publication sample.
 * <p>
 * This class encapsulates comprehensive information about a specific publication sample,
 * including its title, authors, identification code (e.g., ISBN), and media type.
 * It extends {@link RepresentationModel} to provide support for Spring HATEOAS,
 * allowing hypermedia links to be appended to the API response.
 * </p>
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class PublicationDetailsDTO extends RepresentationModel<PublicationDetailsDTO> {

    private String publicationId;
    private String pubTitle;
    private List<String> authorName;
    private String code;
    private String publicationType;

}
