package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.CountryId;
import jakarta.persistence.*;

/**
 * Data model representing a {@link TOPSECRET.domain.Country.Country} for JPA persistence.
 *
 * <p>This class belongs to the infrastructure layer and is mapped to the
 * {@code countries} table. It uses {@link CountryId} as an embedded value object.</p>
 */
@Entity
@Table(name = "countries")
public class CountryDM {

    @EmbeddedId
    @AttributeOverride(name = "countryId", column = @Column(name = "country_id", unique = true, nullable = false))
    private CountryId countryID;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    /**
     * Required by JPA. Not for direct use.
     */
    protected CountryDM() {}

    /**
     * Constructs a new {@code CountryDM}.
     *
     * @param countryID   the domain identifier
     * @param countryName the name of the country
     */
    public CountryDM(CountryId countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    public CountryId getCountryID() {
        return countryID;
    }

    public String getCountryName() {
        return countryName;
    }
}