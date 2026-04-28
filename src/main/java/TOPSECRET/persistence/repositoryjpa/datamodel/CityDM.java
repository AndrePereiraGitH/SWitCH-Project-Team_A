package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.CityID;
import TOPSECRET.domain.ValueObjects.CountryID;
import jakarta.persistence.*;

/**
 * Data model representing a {@link TOPSECRET.domain.City.City} for JPA persistence.
 *
 * <p>This class belongs to the infrastructure layer and is mapped to the
 * {@code cities} table. It uses {@link CityID} and {@link CountryID} as
 * embedded value objects.</p>
 *
 * <p>It is converted to and from the domain object by {@link TOPSECRET.persistence.repositoryjpa.assembler.CityAssembler}.</p>
 */
@Entity
@Table(name = "cities")
public class CityDM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "cityID", column = @Column(name = "city_id", unique = true, nullable = false))
    private CityID cityID;

    @Embedded
    @AttributeOverride(name = "countryId", column = @Column(name = "country_id", nullable = false))
    private CountryID countryID;

    @Column(name = "city_name", nullable = false)
    private String cityName;

    /**
     * Required by JPA. Not for direct use.
     */
    protected CityDM() {}

    /**
     * Constructs a new {@code CityDM} with the given domain identifiers and city name.
     *
     * @param cityID    the domain identifier of the city
     * @param countryID the domain identifier of the country
     * @param cityName  the name of the city
     */
    public CityDM(CityID cityID, CountryID countryID, String cityName) {
        this.cityID = cityID;
        this.countryID = countryID;
        this.cityName = cityName;
    }

    public Long getId() { return id; }
    public CityID getCityID() { return cityID; }
    public CountryID getCountryID() { return countryID; }
    public String getCityName() { return cityName; }
}