package TOPSECRET.dto.Country.Response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

/**
 * Response Data Transfer Object that represents a Country resource
 * exposed by the API layer.
 *
 * <p>This DTO is used to send country-related data from the application
 * to external clients while keeping the internal domain model isolated
 * from the presentation layer.</p>
 *
 * <p>It contains the unique identifier and name of a country, which are
 * the essential attributes required by clients consuming the API.</p>
 *
 * <p>The class extends {@link RepresentationModel} to provide HATEOAS
 * support, enabling hypermedia links to be attached to the response
 * representation.</p>
 *
 * <p>Lombok annotations are used to automatically generate common
 * boilerplate functionality such as:</p>
 * <ul>
 *     <li>A constructor for all final fields</li>
 *     <li>Getter methods</li>
 *     <li>equals and hashCode implementations</li>
 *     <li>A toString representation</li>
 * </ul>
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CountryResponseDTO extends RepresentationModel<CountryResponseDTO> {

    private final String countryId;
    private final String countryName;
}