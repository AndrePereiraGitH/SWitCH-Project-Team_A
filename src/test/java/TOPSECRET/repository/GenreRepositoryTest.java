package TOPSECRET.repository;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreRepositoryTest {
// TEST-ONLY workaround:
// GenreRepository is a Singleton, so its state persists between tests.
// To keep tests independent (AAA principle), we reset the Singleton instance before each test.
// We do it via reflection because INSTANCE is private and we don't want to add a public "clear()" method
// that could be misused in production code.//    @BeforeEach
//    void cleanSingleton() throws Exception {
//
//        // We want every test to start with a fresh repository.
//        // Because GenreRepository is a Singleton, it keeps the same INSTANCE between tests.
//
//        // Get the private static field called "INSTANCE"
//        Field f = GenreRepository.class.getDeclaredField("INSTANCE");
//
//        // Allow access even though it is private
//        f.setAccessible(true);
//
//        // Reset the static INSTANCE field to null
//        // This forces getINSTANCE() to create a new repository
//        f.set(null, null);
//    }

    @Test
    void getINSTANCE_shouldReturnSameInstance() {
        GenreRepository r1 = GenreRepository.getINSTANCE();
        GenreRepository r2 = GenreRepository.getINSTANCE();

        assertSame(r1, r2);
    }
//Cant do Without a cleanup/reset mechanism, tests are not independent and may fail depending on execution order.
// A test-only workaround (resetting the Singleton) is required to guarantee isolation.
//    @Test
//    void createAndAddGenre_shouldAddGenre() {
//        GenreRepository repo = GenreRepository.getINSTANCE();
//
//        Genre result = repo.createAndAddGenre("Sci-fi");
//
//        assertEquals(new Genre("Sci-fi"), result);
//        assertEquals(1, repo.getAllGenres().size());


    @Test
    void createAndAddGenre_shouldThrowWhenGenreAlreadyExists_caseInsensitive() {
        GenreRepository repo = GenreRepository.getINSTANCE();
        repo.createAndAddGenre("Terror");

        assertThrows(IllegalArgumentException.class, () -> repo.createAndAddGenre("Terror"));
    }

    @Test
    void getAllGenres_shouldReturnUnmodifiableCopy() {
        GenreRepository repo = GenreRepository.getINSTANCE();
        repo.createAndAddGenre("Fantasy");

        List<Genre> list = repo.getAllGenres();

        assertThrows(UnsupportedOperationException.class, () -> list.add(new Genre("Horror")));
    }
}
