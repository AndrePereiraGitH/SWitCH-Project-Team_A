/*
package TOPSECRET.domain;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class CollectionOfPublicationsTest {

    // Creates a dummy publication for testing
    private Publication createValidPublication() {
        return new Book(); // Adjust if Book constructor exists
    }

    // =========================
    // PARAMETER PROVIDERS
    // =========================

    // Provides invalid list names
    static Stream<String> invalidNamesProvider() {
        return Stream.of(
                null,
                "",
                " ",
                "   ",
                "\t",
                "\n"
        );
    }

    // =========================
    // CONSTRUCTOR VALIDATION
    // =========================

    // Tests invalid names using parameterized test
    @ParameterizedTest
    @MethodSource("invalidNamesProvider")
    void shouldThrowExceptionWhenListNameIsInvalid(String invalidName) {

        // -------- Act & Assert --------
        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications(invalidName, Genre.FICTION);
        });
    }

    // Tests null genre
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {

        // -------- Act & Assert --------
        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("My List", null);
        });
    }

    // Tests valid construction
    @Test
    void shouldCreatePrivateListWhenDataIsValid() {

        // -------- Arrange --------
        String expectedName = "My List";
        Genre expectedGenre = Genre.FICTION;

        // -------- Act --------
        CollectionOfPublications list = new CollectionOfPublications(expectedName, expectedGenre);

        // -------- Assert --------
        assertNotNull(list);
        assertEquals(expectedName, list.getName());
        assertEquals(expectedGenre, list.getGenre());
        assertTrue(list.getPublications().isEmpty());
    }

    // =========================
    // ADD PUBLICATION
    // =========================

    // Tests adding a publication successfully
    @Test
    void shouldAddPublicationSuccessfully() {

        // -------- Arrange --------
        CollectionOfPublications list = new CollectionOfPublications("My List", Genre.FICTION);
        Publication publication = createValidPublication();

        // -------- Act --------
        boolean result = list.addPublication(publication);

        // -------- Assert --------
        assertTrue(result);
        assertTrue(list.containsPublication(publication));
    }

    // Tests duplicate publication is not allowed
    @Test
    void shouldCreatePrivateCollectionWithValid () {
        Genre genre = new Genre("Fantasy");
        Library library = new Library ();
        List<Publication> publication = new ArrayList<>();
        CollectionOfPublications collection = new CollectionOfPublications("")

        // -------- Act --------
        boolean result = list.addPublication(publication);

        // -------- Assert --------
        assertFalse(result);
    }

}

 */

