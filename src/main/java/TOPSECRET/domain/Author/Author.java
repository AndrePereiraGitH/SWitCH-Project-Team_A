package TOPSECRET.domain.Author;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.ddd.DomainID;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.ValueObjects.AuthorId;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

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
public class Author implements AggregateRoot<AuthorId> {
    private final AuthorId _id;
    private final String _name;
    private final Country _country;
    private final LocalDate _dateOfBirth;
    private final String _biography;

    /**
     * Creates a new {@code Author} instance.
     *
     * <p>An author must always have a non-null and non-blank name, a non-null
     * {@link Country}, a non-null date of birth, and a non-null and non-blank
     * biography. These validations are enforced at construction time to ensure
     * that no invalid {@code Author} instances are created.</p>
     *
     * <p>A unique {@link AuthorId} is automatically generated when the author is created.</p>
     *
     * @param name the author's name
     * @param country the author's country of origin
     * @param dateOfBirth the author's date of birth
     * @param biography the author's biography
     *
     * @throws IllegalArgumentException if {@code name} is null or blank
     * @throws IllegalArgumentException if {@code country} is null
     * @throws IllegalArgumentException if {@code dateOfBirth} is null
     * @throws IllegalArgumentException if {@code biography} is null or blank
     */
    protected Author(String name, Country country,LocalDate dateOfBirth, String  biography) {
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
        this._id = new AuthorId(UUID.randomUUID().toString());
        this._name= name;
        this._country = country;
        this._dateOfBirth = dateOfBirth;
        this._biography = biography;

    }

    @Override
    public AuthorId id() {
        return _id;
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



    /**
     * Compares this author with another object for equality.
     *
     * <p>Two {@code Author} instances are considered equal if they have the same
     * unique identifier ({@link AuthorId}). Other attributes such as name, country,
     * date of birth, or biography are not considered by this method.</p>
     *
     * @param o the object to compare with this author
     * @return {@code true} if the given object is the same instance as this author,
     *         or if it is another {@code Author} with the same identifier;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(_id, author._id);
    }

    /**
     * Returns the hash code of this author.
     *
     * <p>The hash code is based exclusively on the author's unique identifier
     * ({@link AuthorId}), ensuring consistency with {@link #equals(Object)}.</p>
     *
     * @return the hash code derived from this author's identifier
     */
    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    /**
     * Checks whether this author has the same domain identity as another object.
     *
     * <p>Two {@code Author} objects are considered to have the same identity if
     * the given object is also an {@code Author} and both objects have the same
     * name, country, date of birth, and biography.</p>
     *
     * <p>This method represents domain identity comparison and is distinct from
     * {@link #equals(Object)}, which is based solely on the unique identifier
     * ({@link AuthorId}).</p>
     *
     * @param object the object to compare with this author
     * @return {@code true} if the given object is an {@code Author} with the same
     *         name, country, date of birth, and biography; {@code false} otherwise
     */
    @Override
    public boolean sameAs(Object object) {

        if (object instanceof Author) {
            Author oAuthor = (Author) object;

            if (
                    this._name.equals(oAuthor._name) &&
                            this._country.equals(oAuthor._country) &&
                            this._dateOfBirth.equals(oAuthor._dateOfBirth) &&
                            this._biography.equals(oAuthor._biography)
            )
                return true;
        }
        return false;
    }
}