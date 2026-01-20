package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.Enum.Countries;
import TOPSECRET.repository.AuthorRepository;
import TOPSECRET.domain.Country;

import java.time.LocalDate;

public class US005_AuthorController {
    private final AuthorRepository _authorRepository;

    public US005_AuthorController(AuthorRepository authorRepository) {
        this._authorRepository = authorRepository;
    }

    public Author addAuthor(String name, Country country, LocalDate dateOfBirth) {

        return _authorRepository.addAuthor(name, country, dateOfBirth);
    }
}
