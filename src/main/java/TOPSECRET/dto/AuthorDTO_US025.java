package TOPSECRET.dto;

import TOPSECRET.domain.Country;

import java.time.LocalDate;

public class AuthorDTO_US025 {
    private String name;
    private Country country;
    private LocalDate dateOfBirth;
    private String biography;

    public String getName() { return name; }
    public Country getCountry() { return country; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getBiography() { return biography; }

    public void setName(String name) { this.name = name; }
    public void setCountry(Country country) { this.country = country; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setBiography(String biography) { this.biography = biography; }
}