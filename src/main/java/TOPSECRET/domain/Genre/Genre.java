package TOPSECRET.domain.Genre;

import java.util.Objects;
import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.GenreID;

/**
 * Represents a literary genre (e.g. "Drama", "Science Fiction").
 * Stores the genre name and ensures it is never null or blank.
 */
public class Genre implements AggregateRoot<GenreID>{

    // --------------------------------- US003 starts ---------------------------------

    private final GenreID _id ;
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
        this._id = new GenreID(genreType.toLowerCase()); //genreType is the ID
    }

    // --------------------------------- US003 ends ---------------------------------
    @Override
    public GenreID id() {
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
        return _id.equals(other._id); // -auctionId is based on genreType.toLowerCase()
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id); // matches equals() which uses _id
    }

    // --------------------------------- US004 ends ---------------------------------

}


