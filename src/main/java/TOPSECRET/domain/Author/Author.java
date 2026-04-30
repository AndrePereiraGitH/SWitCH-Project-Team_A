package TOPSECRET.domain.Author;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an Author in the system.
 * <p>
 * An Author is defined by:
 * <ul>
 *     <li>A name identifying the author</li>
 *     <li>A {@link CountryId} of origin</li>
 *     <li>A date of birth</li>
 *     <li>A textual biography describing the author</li>
 * </ul>
 *
 * <p><b>Invariant:</b> An Author must always have a non-blank name, a non-null
 * {@link CountryId}, a non-null date of birth, and a non-blank biography.</p>
 *
 * <p>
 * The constructor performs validates these rules to ensure that no invalid Author
 * instances are created, preserving domain integrity.
 * </p>
 *
 * <p><b>Equality:</b> Two authors are considered equal if they share the same
 * {@link AuthorId}.</p>
 */
public class Author implements AggregateRoot<AuthorId> {
    private final AuthorId _id;
    private final String _name;
    private final CountryId _countryId;
    private final LocalDate _dateOfBirth;
    private final String _biography;


    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Creates a new {@code Author} instance.
     *
     * <p>A unique {@link AuthorId} is automatically generated when the author is created.</p>
     *
     * @param name the author's name
     * @param countryID the author's country of origin
     * @param dateOfBirth the author's date of birth
     * @param biography the author's biography
     *
     * @throws IllegalArgumentException if {@code name} is null or blank
     * @throws IllegalArgumentException if {@code countryID} is null
     * @throws IllegalArgumentException if {@code dateOfBirth} is null
     * @throws IllegalArgumentException if {@code biography} is null or blank
     */
    protected Author(String name, CountryId countryID, LocalDate dateOfBirth, String  biography) {
        if ( name ==null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (countryID == null){
            throw new IllegalArgumentException("Country cannot be null");
        }
        if (dateOfBirth == null){
            throw new IllegalArgumentException("DateOfBirth cannot be null");
        }
        if (biography == null || biography.isBlank())
            throw new IllegalArgumentException("Biography cannot be null or blank");
        this._id = new AuthorId(UUID.randomUUID().toString());
        this._name= name;
        this._countryId = countryID;
        this._dateOfBirth = dateOfBirth;
        this._biography = biography;
    }

    /**
     * Recreates an {@code Author} instance with an existing {@link AuthorId}.
     *
     * <p>
     * This constructor is intended to be used when rebuilding an author from
     * persisted data, where the identifier already exists and must be preserved.
     * </p>
     *
     * @param id the existing author identifier
     * @param name the author's name
     * @param countryID the author's country of origin
     * @param dateOfBirth the author's date of birth
     * @param biography the author's biography
     *
     * @throws IllegalArgumentException if {@code id} is null
     * @throws IllegalArgumentException if {@code name} is null or blank
     * @throws IllegalArgumentException if {@code countryID} is null
     * @throws IllegalArgumentException if {@code dateOfBirth} is null
     * @throws IllegalArgumentException if {@code biography} is null or blank
     */
    protected Author(AuthorId id, String name, CountryId countryID, LocalDate dateOfBirth, String  biography) {

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if ( name ==null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (countryID == null){
            throw new IllegalArgumentException("Country cannot be null");
        }
        if (dateOfBirth == null){
            throw new IllegalArgumentException("DateOfBirth cannot be null");
        }
        if (biography == null || biography.isBlank())
            throw new IllegalArgumentException("Biography cannot be null or blank");
        this._id = id;
        this._name= name;
        this._countryId = countryID;
        this._dateOfBirth = dateOfBirth;
        this._biography = biography;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    @Override
    public AuthorId id() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public CountryId getCountryID() {
        return _countryId;
    }

    public LocalDate getDateOfBirth() {
        return _dateOfBirth;
    }

    public String getBiography() {
        return _biography;
    }

    // -------------------------------------------------------------------------
    // Comparison
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(_id, author._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    @Override
    public boolean sameAs(Object object) {

        if (object instanceof Author) {
            Author oAuthor = (Author) object;

            if (
                    this._name.equals(oAuthor._name) &&
                            this._countryId.equals(oAuthor._countryId) &&
                            this._dateOfBirth.equals(oAuthor._dateOfBirth) &&
                            this._biography.equals(oAuthor._biography)
            )
                return true;
        }
        return false;
    }
}