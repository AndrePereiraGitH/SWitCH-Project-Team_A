package TOPSECRET.dto;

import TOPSECRET.domain.Author;

import java.util.List;

public class AuthorMapper_US025 {

    // Creates an Author DTO only with the author's name
    public AuthorDTO_US025 toDTO(Author author) {
        if (author == null) return null;

        AuthorDTO_US025 dto = new AuthorDTO_US025();
        dto.setName(author.getProfile().getName());

        return dto;
    }

    // Converts a list of authors to a list of Author DTO
    public List<AuthorDTO_US025> toDTO(List<Author> authors) {
        return authors == null ? List.of() : authors.stream().map(this::toDTO).toList();
    }
}