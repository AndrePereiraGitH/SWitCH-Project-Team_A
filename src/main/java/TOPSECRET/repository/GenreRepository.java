package TOPSECRET.repository;
import TOPSECRET.domain.*;
import java.util.ArrayList;
import java.util.List;

public class GenreRepository {
    private final List<Genre> _genres = new ArrayList<>();
    private static GenreRepository INSTANCE;

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
        return List.copyOf(_genres);
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

