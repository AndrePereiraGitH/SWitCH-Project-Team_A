package TOPSECRET.repository;
import TOPSECRET.domain.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Repository responsible for creating and storing Genre objects (Singleton).
 * NOTE: It prevents duplicates and returns an unmodifiable copy of the internal list.
 */
public class GenreRepository {
    private final List<Genre> _genres = new ArrayList<>();
    private static GenreRepository INSTANCE;           // Singleton instance (only one repository shared in the application)


    private GenreRepository() {
    }

    public static GenreRepository getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new GenreRepository();
        }
        return INSTANCE;
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

    public void clear() {
        _genres.clear();
    }

    private boolean exists(Genre genre) {
        for (Genre newGenre : this._genres) {
            if (genre.equals(newGenre)) {
                return true;
            }
        }
        return false;
    }
}

