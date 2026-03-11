package TOPSECRET.domain;

import java.util.Objects;

/**
 * Represents a literary genre (e.g. "Drama", "Science Fiction").
 * Stores the genre name and ensures it is never null or blank.
 */
public class Genre {

    // --------------------------------- US003 starts ---------------------------------

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
    }

    // --------------------------------- US003 ends ---------------------------------

    // --------------------------------- US004 starts ---------------------------------
    /**
     * Returns the genre title.
     * @return Genre title.
     */
    public String getGenreType() {
        return _genreType;
    }

    /**
     * Compares two Genre objects based on their title.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;
        Genre genre = (Genre) o;
        return _genreType.equalsIgnoreCase(genre._genreType);
    }

    /**
     * Generates hash code based on genre title.
     */
    @Override
    public int hashCode() {
        return Objects.hash(_genreType.toLowerCase());
    }

    // --------------------------------- US004 ends ---------------------------------

}


