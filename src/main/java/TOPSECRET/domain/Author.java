package TOPSECRET.domain;

import TOPSECRET.domain.Country.Country;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an Author in the system.
 * <p>
 * An Author is defined by:
 * <ul>
 *     <li>A name identifying the author</li>
 *     <li>A {@link Country} of origin</li>
 *     <li>A date of birth</li>
 *     <li>A textual biography describing the author</li>
 * </ul>
 *
 * <p><b>Invariant:</b> An Author must always have a non-blank name, a non-null
 * {@link Country}, a non-null date of birth, and a non-blank biography.</p>
 *
 * <p>
 * The constructor performs validation to guarantee that no invalid Author
 * instances are created, preserving domain integrity.
 * </p>
 *
 * <p><b>Equality:</b> Two authors are considered equal if they share the same
 * name, {@link Country}, and date of birth. Biography is excluded from equality.</p>
 */
public class Author {
    private final String _name;
    private final Country _country;
    private final LocalDate _dateOfBirth;
    private final String _biography;

    protected Author(String name, Country country, LocalDate dateOfBirth, String  biography) {
        if ( name ==null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (country == null){
            throw new IllegalArgumentException("Country cannot be null");
        }
        if (dateOfBirth == null){
            throw new IllegalArgumentException("DateOfBirth cannot be null");
        }
        if (biography == null || biography.isBlank())
            throw new IllegalArgumentException("Biography cannot be null or blank");
        this._name= name;
        this._country = country;
        this._dateOfBirth = dateOfBirth;
        this._biography = biography;
    }

    public String getName() {
        return _name;
    }

    public Country getCountry() {
        return _country;
    }

    public LocalDate getDateOfBirth() {
        return _dateOfBirth;
    }

    public String getBiography() {
        return _biography;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(_name, author._name) &&
                Objects.equals(_country, author._country) &&
                Objects.equals(_dateOfBirth, author._dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _country, _dateOfBirth);
    }


}