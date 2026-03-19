package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;

import java.time.LocalDate;
import java.util.List;

public interface AuthorRepository {
    Author createAndAddAuthor(String name, Country country, LocalDate dateOfBirth, String biography);
    List<Author> getAllAuthors();
}
