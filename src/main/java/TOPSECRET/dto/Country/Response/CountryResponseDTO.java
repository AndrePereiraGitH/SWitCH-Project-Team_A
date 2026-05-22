package TOPSECRET.dto.Country.Response;

import org.springframework.hateoas.RepresentationModel;
import java.util.Objects;

/**
 * Data Transfer Object used to represent a Country in API responses.
 *
 * <p>This DTO is part of the presentation layer and is used to expose
 * country data in a safe and simplified way to clients.</p>
 *
 * <p>It contains the country's identifier and name, which are the only
 * fields exposed externally from the domain model.</p>
 *
 * <p>This class extends {@link RepresentationModel} to support HATEOAS,
 * allowing hypermedia links (such as self-links or related resources)
 * to be attached to the response.</p>
 *
 * <p>Equality and hashCode include both the DTO fields and HATEOAS links
 * (via the superclass implementation), ensuring link-aware comparisons.</p>
 */
public class CountryResponseDTO extends RepresentationModel<CountryResponseDTO> {

    private final String countryId;
    private final String countryName;

    public CountryResponseDTO(String countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof CountryResponseDTO other))
            return false;
        if (!super.equals(object))
            return false;
        return Objects.equals(countryId, other.countryId)
                && Objects.equals(countryName, other.countryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), countryId, countryName);
    }
}