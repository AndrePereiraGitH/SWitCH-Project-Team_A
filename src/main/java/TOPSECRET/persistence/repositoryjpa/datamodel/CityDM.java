package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * JPA data model representing a {@link TOPSECRET.domain.City.City}.
 *
 * <p>This class is mapped to the {@code cities} table and is used exclusively
 * for persistence operations. It stores the city's identity, associated country,
 * and name using embedded value objects.</p>
 *
 * <p>Conversion between this data model and the domain object is handled by
 * {@link TOPSECRET.persistence.repositoryjpa.assembler.CityAssembler}.</p>
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cities")
@Getter
public class CityDM {

    @EmbeddedId
    @AttributeOverride(name = "cityID", column = @Column(name = "city_id", unique = true, nullable = false))
    private CityId cityID;

    @Embedded
    @AttributeOverride(name = "countryId", column = @Column(name = "country_id", nullable = false))
    private CountryId countryID;

    @Column(name = "city_name", nullable = false)
    private String cityName;

    /**
     * Constructs a new {@code CityDM} with the given domain identifiers and city name.
     *
     * @param cityID    the domain identifier of the city
     * @param countryID the domain identifier of the country
     * @param cityName  the name of the city
     */
    public CityDM(CityId cityID, CountryId countryID, String cityName) {
        this.cityID = Objects.requireNonNull(cityID, "City ID cannot be null");
        this.countryID = Objects.requireNonNull(countryID, "Country ID cannot be null");
        this.cityName = Objects.requireNonNull(cityName, "City name cannot be null");
    }

    public CityId getCityID() {
        return cityID;
    }

    public CountryId getCountryID() {
        return countryID;
    }

    public String getCityName() {
        return cityName;
    }
}