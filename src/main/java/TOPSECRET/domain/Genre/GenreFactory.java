package TOPSECRET.domain.Genre;

import org.springframework.stereotype.Service;

/**
 * Factory responsible for creating {@link Genre} instances.
 *
 * <p>Encapsulates the instantiation logic for the {@code Genre} aggregate,
 * ensuring all created instances satisfy the domain invariants.</p>
 */
@Service
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

    /**
     * Reconstructs an existing {@link Genre} from persisted data.
     *
     * <p>Intended for use by the persistence layer only, when rehydrating
     * a Genre from the database. Unlike {@link #create(String)}, this method
     * does not imply a new genre is being registered in the system.</p>
     *
     * @param genreType the genre name as stored in the database
     * @return a reconstructed Genre instance
     * @throws IllegalArgumentException if genreType is null or blank
     */
    public Genre reconstruct(String genreType) {
        return new Genre(genreType);
    }

}
