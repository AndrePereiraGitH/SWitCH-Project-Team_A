package TOPSECRET.domain;

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

    // Getter
    public String getGenreType() {
        return _genreType;
    }

    // US0018
    // Necessary methods to compare the objects
    // Allows Genre objects to be compared by value instead of reference.
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (!(obj instanceof Genre)) return false;

        Genre other = (Genre) obj;

        return _genreType.equalsIgnoreCase(other._genreType);
    }

    // US0018
    // Ensures consistency with equals().
    @Override
    public int hashCode() {
        return _genreType.toLowerCase().hashCode();
    }
}


