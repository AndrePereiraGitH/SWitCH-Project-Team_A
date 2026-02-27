package TOPSECRET.domain;

public class GenreFactory {
    /**
     * Creates a new Genre instance.
     *
     * @param genreType the type of genre to create
     * @return a new Genre instance
     * @throws IllegalArgumentException if genreType is null or blank
     */
    public Genre createGenre(String genreType) {
        return new Genre(genreType);
    }
}
