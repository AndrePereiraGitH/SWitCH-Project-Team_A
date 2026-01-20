package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.AuthorRepository;
import java.time.LocalDate;

public class US005_ToRegisterAuthorController {
    private final AuthorRepository _authorRepository;

    public US005_ToRegisterAuthorController(AuthorRepository authorRepository) {
        if (authorRepository == null) throw new IllegalArgumentException("AuthorRepository cannot be null");

        this._authorRepository = authorRepository;
    }

    public Author createAndAddAuthor(String name, Country country, LocalDate dateOfBirth, String biography) {

        return _authorRepository.createAndAddAuthor(name, country, dateOfBirth, biography);
    }
}
