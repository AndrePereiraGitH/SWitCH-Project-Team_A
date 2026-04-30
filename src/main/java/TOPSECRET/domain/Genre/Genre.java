package TOPSECRET.domain.Genre;

import java.util.Objects;
import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.GenreId;

/**
 * Represents a literary genre (e.g. "Drama", "Science Fiction").
 * Stores the genre name and ensures it is never null or blank.
 */
public class Genre implements AggregateRoot<GenreId>{

    // --------------------------------- US003 starts ---------------------------------

    private final GenreId _id ;
    private String _genreType;

    /**
     * Creates a Genre with the given name.
     *
     * @param genreType the genre name; must not be null or blank
     * @throws IllegalArgumentException if genreType is null or blank
     */
    protected Genre(String genreType) {
        if (genreType == null || genreType.isBlank()) {
            throw new IllegalArgumentException("Genre type cannot be null or blank");
        }
        this._genreType = genreType;
        this._id = new GenreId(genreType.toLowerCase()); //genreType is the ID
    }

    /**
     * Reconstructs a Genre from persisted data using a known {@link GenreId}.
     * Intended for use by the persistence layer only.
     *
     * @param genreId   the existing ID; must not be null
     * @param genreType the genre name; must not be null or blank
     * @throws IllegalArgumentException if genreType is null or blank
     * @throws IllegalArgumentException if genreId is null
     */
    protected Genre(GenreId genreId, String genreType) {
        if (genreType == null || genreType.isBlank()) {
            throw new IllegalArgumentException("Genre type cannot be null or blank");
        }
        if (genreId == null) {
            throw new IllegalArgumentException("Genre ID cannot be null");
        }
        this._id = genreId;
        this._genreType = genreType;
    }
    // --------------------------------- US003 ends ---------------------------------
    @Override
    public GenreId id() {
        return _id;
    }

    /**
     * Determines whether this Genre has the same identity as another object.
     *
     * @param object the object to compare identity with
     * @return true if object is a Genre with an equal GenreID; false otherwise
     */
    @Override
    public boolean sameAs(Object object) {
        if (object instanceof Genre other)
            return _genreType.equalsIgnoreCase(other._genreType);
        return false;
    }
    // --------------------------------- US004 starts ---------------------------------
    public String getGenreType() {
        return _genreType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;
        Genre other = (Genre) o;
        return _id.equals(other._id); // -id is based on genreType.toLowerCase()
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id); // matches equals() which uses _id
    }

    // --------------------------------- US004 ends ---------------------------------

}


