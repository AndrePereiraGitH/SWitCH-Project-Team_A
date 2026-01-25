package TOPSECRET.controller;


import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToRegisterAuthorControllerTest {

    private final AuthorRepository authorRepository = new AuthorRepository(new ArrayList<>());

    @Test
    void shouldAddAuthor() {
        ToRegisterAuthorController controller = new ToRegisterAuthorController(authorRepository);
        Author expected = new Author("Maria", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        // Act + Assert
        Author result = controller.registerAuthor("Maria", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        assertEquals(expected, result);
    }


}