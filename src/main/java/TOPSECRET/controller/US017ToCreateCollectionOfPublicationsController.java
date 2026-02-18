package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.User;
import TOPSECRET.dto.GenreDTO_US017;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller da US017 - criar coleção privada.
 * Basicamente faz duas coisas:
 * - Dá a lista de genres disponíveis
 * - Cria a coleção chamando o repositório
 */
public class US017ToCreateCollectionOfPublicationsController {
    private final GenreRepository genreRepository;
    private final CollectionOfPublicationsRepository collectionRepository;

    public US017ToCreateCollectionOfPublicationsController(
            GenreRepository genreRepository,
            CollectionOfPublicationsRepository collectionRepository
    ) {
        if (genreRepository == null || collectionRepository == null) {
            throw new IllegalArgumentException("Dependencies cannot be null.");
        }

        this.genreRepository = genreRepository;
        this.collectionRepository = collectionRepository;
    }

    /**
     * Vai buscar todos os genres e converte para DTOs.
     * A UI usa isto para mostrar as opções ao user.
     */
    public List<GenreDTO_US017> getAllGenres() {

        List<GenreDTO_US017> result = new ArrayList<>();  //faz parte da lógica interna do controller

        List<Genre> genres = genreRepository.getAllGenres();

        // Converte cada Genre para DTO
        for (int i = 0; i < genres.size(); i++) {
            Genre genre = genres.get(i);
            result.add(GenreDTO_US017.toDTO(genre));
        }

        return result;
    }

    /**
     * Cria uma coleção privada para o user.
     * O repositório valida se já existe ou não.
     */
    public CollectionOfPublications createCollection(
            String name,
            Genre selectedGenre,
            User user
    ) {
        if (user == null || selectedGenre == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        return collectionRepository.createIfNotExists(user, name, selectedGenre, true);
    }

}

