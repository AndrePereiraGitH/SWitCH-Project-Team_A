package TOPSECRET.dto;

import TOPSECRET.domain.Country;

import java.time.LocalDate;
import java.util.List;

public class AuthorDTO_US025 {
    String name;
    Country country;
    LocalDate dateOfBirth;
    String biography;
    List<AuthorDTO_US025> authors;

    public String getName() { return name; }
    public Country getCountry() { return country; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getBiography() { return biography; }
    public List<AuthorDTO_US025> getAuthors() { return authors; }
}