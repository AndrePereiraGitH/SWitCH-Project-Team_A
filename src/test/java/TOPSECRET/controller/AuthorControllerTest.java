package TOPSECRET.controller;


import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorControllerTest {
    @Test
    void shouldAddAuthor() {
        AuthorRepository repository = new AuthorRepository();

        US005_ToRegisterAuthorController controller = new US005_ToRegisterAuthorController(repository);
        Author expected = new Author("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        // Act + Assert
        Author result = controller.createAndAddAuthor("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        assertEquals(expected, result);
    }


}