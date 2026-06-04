package TOPSECRET.dto.publicationSample.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;


/**
 * Data Transfer Object (DTO) representing a comprehensive view of a publication sample.
 * <p>
 * This class encapsulates the primary details of a publication sample, including its
 * metadata (title, authors, language, genres), ownership information, and its current condition.
 * It extends {@link RepresentationModel} to provide support for Spring HATEOAS,
 * allowing hypermedia links to be appended to the API response.
 * </p>
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class PublicationDTO extends RepresentationModel<PublicationDTO> {

    private String id;
    private String title;
    private List<String> authors;
    private String language;
    private List<String> genres;
    private String ownerName;
    private String libraryId;
    private String condition;
    private String collectionId;

}
