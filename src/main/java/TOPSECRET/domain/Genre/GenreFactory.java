package TOPSECRET.domain.Genre;

/**
 * Factory responsible for creating {@link Genre} instances.
 *
 * <p>Encapsulates the instantiation logic for the {@code Genre} aggregate,
 * ensuring all created instances satisfy the domain invariants.</p>
 */
public class GenreFactory {
    /**
     * Creates a new Genre instance.
     *
     * @param genreType the type of genre to create
     * @return a new Genre instance
     * @throws IllegalArgumentException if genreType is null or blank
     */
    public Genre create(String genreType) {
        return new Genre(genreType);
    }
}
