package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import java.util.List;
import java.util.ArrayList;

public class AuthorMapper_US025 {
    public AuthorDTO_US025 toDTO(List<Author> authors) {
        List<String> authorNames = new ArrayList<>();
        for (Author author : authors) {
            authorNames.add(author.getProfile().getName());
        }
        return new AuthorDTO_US025(authorNames);
    }
}