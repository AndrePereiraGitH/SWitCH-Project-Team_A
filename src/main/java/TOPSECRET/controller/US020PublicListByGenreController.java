
package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.dto.CollectionOfPublicationsDTO;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepository;

import java.util.List;

public class US020PublicListByGenreController {

    private final GenreRepository _genreRepository;
    private final CollectionOfPublicationsRepository _collectionRepo;

    public US020PublicListByGenreController(GenreRepository genreRepository, CollectionOfPublicationsRepository collectionRepo)
    {
        this._genreRepository = genreRepository;
        this._collectionRepo = collectionRepo;
    }

    public List<Genre> getGenres() {
        return _genreRepository.getAllGenres();
    }

    public List<CollectionOfPublicationsDTO> getPublicListsByGenre(Genre genreId) {
        if (genreId == null) {
            throw new IllegalArgumentException("Genre can't be null.");
        }
        return _collectionRepo.findPublicByGenre(genreId);
    }
}
