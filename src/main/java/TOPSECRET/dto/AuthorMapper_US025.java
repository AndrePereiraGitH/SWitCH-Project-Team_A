package TOPSECRET.dto;

import TOPSECRET.domain.Author;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorMapper_US025 {

    public AuthorDTO_US025 toDTO(Author author) {
        if (author == null) return null;

        AuthorDTO_US025 dto = new AuthorDTO_US025();
        dto.setName(author.getProfile().getName());
        dto.setCountry(author.getProfile().getCountry());
        dto.setDateOfBirth(author.getProfile().getBirthday());
        dto.setBiography(author.getBiography());

        return dto;
    }

    public List<AuthorDTO_US025> toDTO(List<Author> authors) {
        if (authors == null || authors.isEmpty()) return List.of();
        return authors.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
