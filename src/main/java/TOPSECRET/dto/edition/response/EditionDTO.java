package TOPSECRET.dto.edition.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

/**
 * Response Data Transfer Object used to represent an Edition in API responses.
 *
 * <p>This DTO belongs to the presentation layer and is responsible for
 * exposing edition data to external clients in a simplified and controlled format,
 * without leaking the internal domain model.</p>
 *
 * <p>It extends {@link RepresentationModel} to support HATEOAS,
 * allowing hypermedia links to be attached to the response and enabling
 * richer RESTful navigation.</p>
 *
 * <p>Lombok annotations are used to reduce boilerplate by automatically generating:</p>
 * <ul>
 *     <li>A required-arguments constructor</li>
 *     <li>Getter methods for all fields</li>
 *     <li>equals and hashCode methods</li>
 *     <li>A toString implementation</li>
 * </ul>
 *
 * <p>Note: Equality does not include HATEOAS links due to
 * {@code @EqualsAndHashCode(callSuper = false)}.</p>
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class EditionDTO extends RepresentationModel<EditionDTO> {

    private final String editionId;
    private final String editionNumber;
    private final String code;
    private final String publicationTypeId;
    private final String publisherId;
    private final String mediaFormat;
    private final String editionLanguage;
    private final LocalDate editionDate;
    private final String typeOfBinding;
    private final double height;
    private final double width;
    private final double depth;
    private final double weight;
    private final int numberOfPages;
}
