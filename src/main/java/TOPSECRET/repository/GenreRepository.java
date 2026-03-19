package TOPSECRET.repository;

import TOPSECRET.domain.Genre;

import java.util.List;

public interface GenreRepository {
        Genre createAndAdd(String genreType);
        void addGenre(Genre genre);
        Genre findByTitle(String title);
        List<Genre> getAllGenres();
}

