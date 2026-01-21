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
        US005_ToRegisterAuthorController controller = new US005_ToRegisterAuthorController(AuthorRepository.getInstance());
        Author expected = new Author("Maria", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        // Act + Assert
        Author result = controller.createAndAddAuthor("Maria", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        assertEquals(expected, result);
    }


}