package TOPSECRET.domain;

import java.util.Objects;

public class Genre {

    // Attributes
    private String _genreType;

    // Constructor
    public Genre(String genreType) {
        if (genreType == null || genreType.isBlank()) {
            throw new IllegalArgumentException("Genre type cannot be null or blank");
        }
        this._genreType = genreType;
    }


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


