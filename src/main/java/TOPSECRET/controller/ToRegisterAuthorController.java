package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.AuthorRepository;
import java.time.LocalDate;

public class ToRegisterAuthorController {
    private final AuthorRepository _authorRepository;

    public ToRegisterAuthorController(AuthorRepository authorRepository) {
        if (authorRepository == null) throw new IllegalArgumentException("AuthorRepository cannot be null");

        this._authorRepository = authorRepository;
    }

    public Author registerAuthor(String name, Country country, LocalDate dateOfBirth, String biography) {

        return _authorRepository.createAndAddAuthor(name, country, dateOfBirth, biography);
    }

}
