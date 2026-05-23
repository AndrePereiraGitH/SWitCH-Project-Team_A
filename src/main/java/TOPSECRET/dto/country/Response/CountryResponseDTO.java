package TOPSECRET.dto.country.Response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

/**
 * Response Data Transfer Object used to represent a Country in API responses.
 *
 * <p>This DTO belongs to the presentation layer and is responsible for
 * exposing country data to external clients in a simplified and controlled format,
 * without leaking the internal domain model.</p>
 *
 * <p>It contains the essential attributes of a country, specifically its
 * unique identifier and name, which are sufficient for client-side consumption.</p>
 *
 * <p>This class extends {@link RepresentationModel} to support HATEOAS,
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
public class CountryResponseDTO extends RepresentationModel<CountryResponseDTO> {

    private final String countryId;
    private final String countryName;
}