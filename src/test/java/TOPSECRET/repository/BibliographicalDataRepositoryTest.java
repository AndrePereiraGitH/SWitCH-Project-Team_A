package TOPSECRET.repository;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class BibliographicalDataRepositoryTest {

    // HELPER ----------------------------------------------------------------------------------------------------------

    private BibliographicalData bd1;
    private BibliographicalData bd2;
    private BibliographicalDataRepository repo;

    @BeforeEach
    void setUp() {
        bd1 = new BibliographicalData(
                "Book One",
                List.of(new Author(
                        new Profile(
                                "Alice Marques",
                                new Country("Portugal"),
                                LocalDate.of(1997, 8, 12)),
                        "valid bio")),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fantasy")),
                "Some text"
        );

        bd2 = new BibliographicalData(
                "Book Two",
                List.of(new Author(
                        new Profile(
                                "João Sousa",
                                new Country("Portugal"),
                                LocalDate.of(1996, 1, 2)),
                        "valid bio")),
                Locale.FRENCH,
                LocalDate.of(2021, 5, 10),
                List.of(new Genre("Drama")),
                "Other text"
        );

        repo = new BibliographicalDataRepository(List.of(bd1, bd2));

    }
        // TESTS -------------------------------------------------------------------------------------------------------

    @Test
    void constructor_CopiesListDefensively() {
        List<BibliographicalData> original = List.of(bd1, bd2);
        BibliographicalDataRepository repo = new BibliographicalDataRepository(original);

        // Tries to modify original list
        assertThrows(UnsupportedOperationException.class, () -> {
            original.add(bd1);
        });

        // Verifies that the repository isn't modified
        assertEquals(2, repo.getAllPublications().size());
    }

    @Test
    void getAllPublications_ReturnsAllItems() {
        List<BibliographicalData> result = repo.getAllPublications();

        assertEquals(2, result.size());
        assertTrue(result.contains(bd1));
        assertTrue(result.contains(bd2));
    }

    @Test
    void getAllPublications_ReturnsUnmodifiableList() {
        List<BibliographicalData> result = repo.getAllPublications();

        assertThrows(UnsupportedOperationException.class, () -> {
            result.add(bd1);
        });
    }
}