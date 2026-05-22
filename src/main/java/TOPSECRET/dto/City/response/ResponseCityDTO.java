package TOPSECRET.dto.City.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object representing the response payload for a city.
 *
 * <p>Returned by the city creation and query use cases. Contains only primitive representations
 * of the domain data — no domain types are exposed.</p>
 *
 * <p>Extends {@link RepresentationModel} to support HATEOAS hypermedia links.</p>
 */
@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@AllArgsConstructor

public class ResponseCityDTO extends RepresentationModel<ResponseCityDTO> {
    private final String cityName;
    private final String countryName;
}