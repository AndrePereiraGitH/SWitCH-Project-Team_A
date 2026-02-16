package TOPSECRET.repository;

import TOPSECRET.domain.*;
import java.util.List;

/**
 * Repository responsible for creating and storing Genre objects.
 * NOTE: Returns an unmodifiable copy of the internal list.
 */
public class GenreRepository {
    private final List<Genre> _genres;

    public GenreRepository(List<Genre> genres) {
        if (genres == null) {
            throw new IllegalArgumentException("Genre List cannot be null");
        }
        this._genres = genres;
    }

    public Genre createAndAddGenre(String genre1) {
        Genre genre = new Genre(genre1);
        if (!exists(genre)) {
            this._genres.add(genre);  // Add the genre that was passed in
            return genre;
        }
        throw new IllegalArgumentException("Genre already exists");
    }

    public List<Genre> getAllGenres() {
        return List.copyOf(_genres);            // Defensive copy + unmodifiable list: protects repository state from external changes
    }

    private boolean exists(Genre genre) {
        for (Genre existingGenre : this._genres) {   
            if (genre.equals(existingGenre)) {
                return true;
            }
        }
        return false;
    }
}

