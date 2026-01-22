package TOPSECRET.dto;

import TOPSECRET.domain.Author;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorMapper_US025 {

    public AuthorDTO_US025 toDTO(Author author) {
        if (author == null) return null;

        AuthorDTO_US025 dto = new AuthorDTO_US025();
        dto.name = author.getProfile().getName();
        dto.country = author.getProfile().getCountry();
        dto.dateOfBirth = author.getProfile().getBirthday();
        dto.biography = author.getBiography();

        return dto;
    }

    public AuthorDTO_US025 toDTO(List<Author> authors) {
        if (authors == null || authors.isEmpty()) return null;

        AuthorDTO_US025 dto = new AuthorDTO_US025();

        dto.authors = authors.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return dto;
    }
}