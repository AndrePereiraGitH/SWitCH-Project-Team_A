package TOPSECRET.dto;

import TOPSECRET.domain.Author;

public class AuthorMapper_US026 {

    public static AuthorDTO_US026 fromAuthor(Author author) {

        if (author == null) {
            throw new IllegalArgumentException("auhtor cannot be null");
        }

        return new AuthorDTO_US026(author.getProfile().getName());
    }
}
